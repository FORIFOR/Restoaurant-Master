# restomaster-payments

PayPay決済バックエンドサーバー

## セットアップ

```bash
cd restomaster-payments
python -m venv venv
source venv/bin/activate
pip install -r requirements.txt
```

## 環境変数 (.env)

```
PAYPAY_API_KEY=your_api_key
PAYPAY_API_SECRET=your_api_secret
PAYPAY_MERCHANT_ID=your_merchant_id
PAYPAY_WEBHOOK_SECRET=your_webhook_secret
DATABASE_URL=postgresql://user:pass@localhost:5432/restomaster
```

## 起動

```bash
uvicorn app.main:app --reload --host 0.0.0.0 --port 8000
```

## エンドポイント

| Method | Path | Description |
|--------|------|-------------|
| POST | `/payments/paypay/create` | QR コード作成 |
| GET | `/payments/paypay/status/{merchantPaymentId}` | ステータス確認 |
| POST | `/paypay/webhook` | Webhook 受信 |
