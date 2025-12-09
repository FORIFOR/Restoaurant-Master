"""
RestoMaster Payments Backend - PayPay Webhook Handler
"""
import hmac
import hashlib
import base64
import json
from fastapi import APIRouter, Request, HTTPException, Header
from typing import Optional

from .config import get_settings

router = APIRouter(prefix="/paypay", tags=["paypay-webhook"])


def verify_webhook_signature(signature: str, body: bytes) -> bool:
    """
    Verify PayPay webhook signature.
    
    NOTE: The exact signature verification method should be
    confirmed against PayPay's official documentation.
    """
    settings = get_settings()
    
    if not settings.paypay_webhook_secret:
        # If no secret configured, skip verification (development only!)
        return True
    
    expected = hmac.new(
        settings.paypay_webhook_secret.encode("utf-8"),
        body,
        hashlib.sha256
    ).digest()
    expected_b64 = base64.b64encode(expected).decode("utf-8")
    
    return hmac.compare_digest(signature, expected_b64)


@router.post("/webhook")
async def paypay_webhook(
    request: Request,
    x_paypay_signature: Optional[str] = Header(None, alias="X-PayPay-Signature")
):
    """
    Receive PayPay webhook notifications.
    
    PayPay sends webhooks for:
    - PAYMENT_COMPLETED: Payment successful
    - PAYMENT_CANCELLED: Payment cancelled
    - PAYMENT_FAILED: Payment failed
    
    This handler should:
    1. Verify signature
    2. Parse event
    3. Update local payment record
    4. Update bill status
    """
    body = await request.body()
    
    # Verify signature (if configured)
    if x_paypay_signature:
        if not verify_webhook_signature(x_paypay_signature, body):
            raise HTTPException(400, "Invalid webhook signature")
    
    try:
        data = json.loads(body.decode("utf-8"))
    except json.JSONDecodeError:
        raise HTTPException(400, "Invalid JSON body")
    
    event_type = data.get("notification_type") or data.get("eventType")
    merchant_payment_id = (
        data.get("data", {}).get("merchantPaymentId") or 
        data.get("merchantPaymentId")
    )
    
    print(f"[PayPay Webhook] event={event_type}, merchantPaymentId={merchant_payment_id}")
    
    if not merchant_payment_id:
        print("[PayPay Webhook] No merchantPaymentId found, ignoring")
        return {"status": "ignored"}
    
    # TODO: Integrate with RestoMaster database
    # 
    # In production, you would:
    # 1. Find Payment record by merchant_payment_id
    # 2. Update Payment status to SUCCEEDED/FAILED/CANCELLED
    # 3. If SUCCEEDED, update Bill.status to PAID
    # 4. Optionally notify Android app via WebSocket/SSE
    
    if event_type in ("PAYMENT_COMPLETED", "payment.confirmed"):
        print(f"[PayPay Webhook] Payment COMPLETED: {merchant_payment_id}")
        # db.mark_payment_succeeded(merchant_payment_id, data)
        # db.update_bill_if_fully_paid(payment.bill_id)
        
    elif event_type in ("PAYMENT_CANCELLED", "payment.cancelled"):
        print(f"[PayPay Webhook] Payment CANCELLED: {merchant_payment_id}")
        # db.mark_payment_cancelled(merchant_payment_id, data)
        
    elif event_type in ("PAYMENT_FAILED", "payment.failed"):
        print(f"[PayPay Webhook] Payment FAILED: {merchant_payment_id}")
        # db.mark_payment_failed(merchant_payment_id, data)
    
    # Return 200 OK to acknowledge receipt
    return {"status": "ok", "event": event_type}


@router.get("/thanks")
async def payment_thanks(m: Optional[str] = None):
    """
    Redirect destination after PayPay payment.
    
    Shows a simple thank you message to the customer.
    In production, this could redirect back to the mobile order page.
    """
    return {
        "status": "success",
        "message": "お支払いありがとうございました",
        "merchant_payment_id": m
    }
