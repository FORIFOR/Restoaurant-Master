"""
RestoMaster Payments Backend - PayPay API Client

NOTE: 署名フォーマットは必ず PayPay 公式ドキュメントで確認してください。
https://developer.paypay.ne.jp/
"""
import time
import uuid
import hashlib
import hmac
import base64
import json
from typing import Optional
import httpx

from .config import get_settings


class PayPayClient:
    """PayPay API wrapper with HMAC signature"""
    
    def __init__(self):
        settings = get_settings()
        self.api_base = settings.paypay_api_base
        self.api_key = settings.paypay_api_key
        self.api_secret = settings.paypay_api_secret
        self.merchant_id = settings.paypay_merchant_id
    
    def _sign(self, method: str, path: str, body: Optional[dict] = None) -> tuple[str, str, str]:
        """
        Generate HMAC signature for PayPay API request.
        
        NOTE: The exact canonical string format should be verified against
        PayPay's official documentation. This is a template implementation.
        """
        timestamp = str(int(time.time()))
        nonce = str(uuid.uuid4())
        
        body_str = json.dumps(body, separators=(',', ':')) if body else ""
        
        # Build canonical string (format may vary - check docs)
        message = f"{method}\n{path}\n{timestamp}\n{nonce}\n{body_str}"
        
        sig = hmac.new(
            self.api_secret.encode("utf-8"),
            message.encode("utf-8"),
            hashlib.sha256
        ).digest()
        signature = base64.b64encode(sig).decode("utf-8")
        
        # Authorization header format (check PayPay docs for exact format)
        auth_header = f"hmac OPA-Auth:{self.api_key}:{nonce}:{timestamp}:{signature}"
        
        return auth_header, timestamp, nonce
    
    async def create_qr_code(
        self,
        merchant_payment_id: str,
        amount: int,
        description: str,
        redirect_url: Optional[str] = None
    ) -> dict:
        """
        Create a PayPay QR code for payment.
        
        Args:
            merchant_payment_id: Unique identifier for this payment (idempotency key)
            amount: Payment amount in JPY
            description: Payment description (shown to customer)
            redirect_url: URL to redirect after payment completion
        
        Returns:
            PayPay API response containing QR code URL
        """
        path = "/v2/codes"  # Verify path in official docs
        url = self.api_base + path
        
        settings = get_settings()
        
        body = {
            "merchantPaymentId": merchant_payment_id,
            "amount": {
                "amount": amount,
                "currency": "JPY"
            },
            "orderItems": [
                {
                    "name": description,
                    "category": "restaurant",
                    "quantity": 1,
                    "productId": merchant_payment_id,
                    "unit": "item",
                    "unitPrice": {
                        "amount": amount,
                        "currency": "JPY"
                    }
                }
            ],
            "codeType": "ORDER_QR",
            "redirectUrl": redirect_url or f"{settings.base_url}/paypay/thanks",
            "redirectType": "WEB_LINK",
            "requestedAt": int(time.time()),
            "isAuthorization": False  # Direct payment (not auth-capture)
        }
        
        auth_header, ts, nonce = self._sign("POST", path, body)
        
        headers = {
            "Authorization": auth_header,
            "Content-Type": "application/json;charset=UTF-8",
            "X-ASSUME-MERCHANT": self.merchant_id,
        }
        
        async with httpx.AsyncClient() as client:
            resp = await client.post(url, headers=headers, json=body, timeout=10.0)
            resp.raise_for_status()
            return resp.json()
    
    async def get_payment_status(self, merchant_payment_id: str) -> dict:
        """
        Get payment status by merchant payment ID.
        """
        path = f"/v2/codes/payments/{merchant_payment_id}"
        url = self.api_base + path
        
        auth_header, ts, nonce = self._sign("GET", path)
        
        headers = {
            "Authorization": auth_header,
            "X-ASSUME-MERCHANT": self.merchant_id,
        }
        
        async with httpx.AsyncClient() as client:
            resp = await client.get(url, headers=headers, timeout=10.0)
            resp.raise_for_status()
            return resp.json()

    async def refund_payment(
        self,
        payment_id: str,
        amount: int,
        merchant_refund_id: str,
        reason: Optional[str] = None
    ) -> dict:
        """
        Refund a payment.
        
        Args:
            payment_id: The payment ID to refund (returned from payment status)
            amount: Amount to refund
            merchant_refund_id: Unique refund ID (crucial for idempotency)
            reason: Reason for refund
        """
        path = "/v2/refunds"
        url = self.api_base + path
        
        body = {
            "merchantRefundId": merchant_refund_id,
            "paymentId": payment_id,
            "amount": {
                "amount": amount,
                "currency": "JPY"
            },
            "reason": reason or "Customer request",
            "requestedAt": int(time.time())
        }
        
        auth_header, ts, nonce = self._sign("POST", path, body)
        
        headers = {
            "Authorization": auth_header,
            "Content-Type": "application/json;charset=UTF-8",
            "X-ASSUME-MERCHANT": self.merchant_id,
        }
        
        async with httpx.AsyncClient() as client:
            resp = await client.post(url, headers=headers, json=body, timeout=10.0)
            # 409 Conflict often means same merchantRefundId was used with different params
            # or it's a replay. Let caller handle it, but we can log/raise.
            if resp.status_code != 200 and resp.status_code != 201:
                 # PayPay APIs return JSON error details
                 pass
            resp.raise_for_status()
            return resp.json()



# Singleton instance
paypay_client = PayPayClient()
