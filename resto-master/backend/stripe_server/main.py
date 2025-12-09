import os
import stripe
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import Optional

# Initialize Stripe
# In production, use os.environ.get("STRIPE_SECRET_KEY")
# For dev, we can hardcode or use a .env file. 
# WARNING: Do not commit real keys to public repos.
stripe.api_key = os.environ.get("STRIPE_SECRET_KEY", "sk_test_placeholder")

app = FastAPI()

class CreateSessionRequest(BaseModel):
    bill_id: int
    amount: int
    currency: str
    table_label: str
    return_base_url: str

class VerifyRequest(BaseModel):
    session_id: str

@app.get("/")
def read_root():
    return {"status": "ok", "service": "RestoMaster Stripe Backend"}

@app.post("/payments/stripe/create-checkout-session")
def create_checkout_session(req: CreateSessionRequest):
    try:
        # Create a Checkout Session
        session = stripe.checkout.Session.create(
            mode="payment",
            payment_method_types=["card"],
            line_items=[{
                "price_data": {
                    "currency": req.currency,
                    "product_data": {
                        "name": f"Table {req.table_label} - Payment",
                        "description": f"Bill ID: {req.bill_id}",
                    },
                    "unit_amount": req.amount,
                },
                "quantity": 1,
            }],
            metadata={
                "bill_id": str(req.bill_id),
                "table_label": req.table_label
            },
            # Redirect URLs
            success_url=f"{req.return_base_url}/payments/stripe/success?session_id={{CHECKOUT_SESSION_ID}}",
            cancel_url=f"{req.return_base_url}/payments/stripe/cancel?session_id={{CHECKOUT_SESSION_ID}}",
        )
        return {
            "checkout_url": session.url,
            "session_id": session.id,
        }
    except Exception as e:
        print(f"Error creating session: {e}")
        raise HTTPException(status_code=500, detail=str(e))

@app.post("/payments/stripe/verify")
def verify_payment(req: VerifyRequest):
    try:
        # Retrieve the session from Stripe to verify status
        session = stripe.checkout.Session.retrieve(req.session_id)
        
        if session.payment_status == "paid":
            return {
                "status": "SUCCEEDED",
                "amount": session.amount_total,
                "currency": session.currency,
                "payment_intent_id": session.payment_intent,
                "receipt_url": session.url, # In real mode, payment_intent has receipt_url
                "metadata": session.metadata
            }
        else:
            return {
                "status": "PENDING", # or FAILED/CANCELED
                "detail": f"Payment status is {session.payment_status}"
            }
            
    except Exception as e:
        print(f"Error verifying payment: {e}")
        raise HTTPException(status_code=500, detail=str(e))
