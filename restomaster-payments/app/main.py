"""
RestoMaster Payments Backend - FastAPI Entry Point
"""
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

from .routes_paypay import router as paypay_router
from .routes_webhook import router as webhook_router
from .config import get_settings

app = FastAPI(
    title="RestoMaster Payments",
    description="Payment processing backend for RestoMaster POS",
    version="1.0.0"
)

# CORS (adjust origins for production)
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # TODO: Restrict in production
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Include routers
app.include_router(paypay_router)
app.include_router(webhook_router)
app.include_router(images_router)


@app.get("/")
async def root():
    """Health check endpoint"""
    return {"status": "ok", "service": "restomaster-payments"}


@app.get("/health")
async def health():
    """Health check for load balancers"""
    settings = get_settings()
    return {
        "status": "ok",
        "paypay_configured": bool(settings.paypay_api_key),
        "api_base": settings.paypay_api_base
    }
