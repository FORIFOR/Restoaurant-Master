"""
RestoMaster Payments Backend - PayPay Routes
"""
import time
from fastapi import APIRouter, HTTPException
from pydantic import BaseModel
from typing import Optional

from .paypay_client import paypay_client
from .config import get_settings

router = APIRouter(prefix="/payments/paypay", tags=["paypay"])


class CreatePayPayRequest(BaseModel):
    """Request body for creating PayPay QR code"""
    bill_id: int
    amount: int
    table_number: Optional[int] = None
    description: Optional[str] = None


class CreatePayPayResponse(BaseModel):
    """Response for PayPay QR creation"""
    payment_id: str
    bill_id: int
    amount: int
    paypay_url: str
    merchant_payment_id: str


@router.post("/create", response_model=CreatePayPayResponse)
async def create_paypay_qr(req: CreatePayPayRequest):
    """
    Create a PayPay QR code for payment.
    
    This endpoint:
    1. Generates a unique merchant_payment_id
    2. Calls PayPay API to create QR code
    3. Returns the QR URL for display
    """
    # Generate unique merchant payment ID
    merchant_payment_id = f"rm-{req.bill_id}-{int(time.time())}"
    
    # Build description
    description = req.description or f"RestoMaster Bill #{req.bill_id}"
    if req.table_number:
        description = f"Table {req.table_number} - {description}"
    
    try:
        settings = get_settings()
        redirect_url = f"{settings.base_url}/paypay/thanks?m={merchant_payment_id}"
        
        result = await paypay_client.create_qr_code(
            merchant_payment_id=merchant_payment_id,
            amount=req.amount,
            description=description,
            redirect_url=redirect_url
        )
        
        # Extract QR URL from response (structure may vary - check docs)
        qr_url = result.get("data", {}).get("url", "")
        code_id = result.get("data", {}).get("codeId", "")
        
        if not qr_url:
            raise HTTPException(500, "Failed to generate QR URL")
        
        return CreatePayPayResponse(
            payment_id=code_id,
            bill_id=req.bill_id,
            amount=req.amount,
            paypay_url=qr_url,
            merchant_payment_id=merchant_payment_id
        )
        
    except Exception as e:
        raise HTTPException(500, f"PayPay API error: {str(e)}")


class PaymentStatusResponse(BaseModel):
    """Payment status response"""
    merchant_payment_id: str
    status: str  # PENDING, COMPLETED, FAILED, etc.
    amount: Optional[int] = None


@router.get("/status/{merchant_payment_id}", response_model=PaymentStatusResponse)
async def get_payment_status(merchant_payment_id: str):
    """
    Get payment status by merchant payment ID.
    
    Used for polling payment completion from Android app.
    """
    try:
        result = await paypay_client.get_payment_status(merchant_payment_id)
        
        data = result.get("data", {})
        status = data.get("status", "UNKNOWN")
        amount = data.get("amount", {}).get("amount")
        
        return PaymentStatusResponse(
            merchant_payment_id=merchant_payment_id,
            status=status,
            amount=amount
        )
        
    except Exception as e:
        raise HTTPException(500, f"PayPay API error: {str(e)}")


class RefundRequest(BaseModel):
    payment_id: str
    amount: int
    merchant_refund_id: str
    reason: Optional[str] = None


@router.post("/refund")
async def refund_payment(req: RefundRequest):
    """
    Execute a refund (full or partial).
    
    CRITICAL: merchant_refund_id comes from client to ensure idempotency.
    """
    try:
        result = await paypay_client.refund_payment(
            payment_id=req.payment_id,
            amount=req.amount,
            merchant_refund_id=req.merchant_refund_id,
            reason=req.reason
        )
        return result
    except Exception as e:
        raise HTTPException(500, f"Refund failed: {str(e)}")


@router.get("/connected")
async def check_connection():
    """
    Simple check to verify backend connection and PayPay config.
    Returns JSON as requested.
    """
    settings = get_settings()
    is_configured = bool(settings.paypay_api_key and settings.paypay_merchant_id)
    return {
        "connected": True,
        "paypay_configured": is_configured,
        "merchant_id": settings.paypay_merchant_id if is_configured else None
    }
