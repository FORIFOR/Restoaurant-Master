"""
RestoMaster Payments Backend - Configuration
"""
import os
from pydantic_settings import BaseSettings
from functools import lru_cache


class Settings(BaseSettings):
    # PayPay API
    paypay_api_base: str = "https://api.sandbox.paypay.ne.jp"  # 本番: https://api.paypay.ne.jp
    paypay_api_key: str = ""
    paypay_api_secret: str = ""
    paypay_merchant_id: str = ""
    paypay_webhook_secret: str = ""
    
    # Database
    database_url: str = "sqlite:///./payments.db"
    
    # Server
    base_url: str = "http://localhost:8000"  # For redirect URLs
    
    class Config:
        env_file = ".env"
        env_file_encoding = "utf-8"


@lru_cache()
def get_settings() -> Settings:
    return Settings()
