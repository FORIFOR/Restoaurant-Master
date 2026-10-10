# dependency-map

[解析トップへ戻る](../README.md)

内容確認したソースの静的依存関係。PythonはAST、JavaScript／TypeScriptはimport文の字句抽出です。型だけのimport、再export、コメントや動的importの扱いには制限があり、ランタイム依存の完全なグラフではありません。


```mermaid
graph TD
    n-resto-master-backend-main-py-5fb7c4["main.py\nresto-master/backend/main.py"]
    n-restomaster-payments-app-main-py-cdb4cf["main.py\nrestomaster-payments/app/main.py"]
    n-resto-master-backend-stripe-server-main-py-70d2d8["main.py\nresto-master/backend/stripe_server/main.py"]
    n-restomaster-payments-app-paypay-client-py-a1b237["paypay_client.py\nrestomaster-payments/app/paypay_client.py"]
    n-resto-master-backend-gemini-ocr-py-54d3d2["gemini_ocr.py\nresto-master/backend/gemini_ocr.py"]
    n-restomaster-payments-app-routes-paypay-py-0963c4["routes_paypay.py\nrestomaster-payments/app/routes_paypay.py"]
    n-restomaster-payments-app-routes-webhook-py-0055fc["routes_webhook.py\nrestomaster-payments/app/routes_webhook.py"]
```

## 要素の説明

### n-resto-master-backend-gemini-ocr-py-54d3d2

確認済みファイル: `resto-master/backend/gemini_ocr.py`

解析: Python AST。静的なimport／export参照です。関数の実行順序やHTTP通信は表していません。

宣言: `call_gemini_menu_ocr`

- `base64` → 外部・標準ライブラリ・別名など。ローカル対応先は未確定
- `json` → 外部・標準ライブラリ・別名など。ローカル対応先は未確定
- `os` → 外部・標準ライブラリ・別名など。ローカル対応先は未確定
- `typing` → 外部・標準ライブラリ・別名など。ローカル対応先は未確定
- `google` → 外部・標準ライブラリ・別名など。ローカル対応先は未確定
- `google.genai` → 外部・標準ライブラリ・別名など。ローカル対応先は未確定

[詳細な図と説明を見る](n-resto-master-backend-gemini-ocr-py-54d3d2/README.md)

### n-resto-master-backend-main-py-5fb7c4

確認済みファイル: `resto-master/backend/main.py`

解析: Python AST。静的なimport／export参照です。関数の実行順序やHTTP通信は表していません。

宣言: `ocr_menu_gemini`

- `fastapi` → 外部・標準ライブラリ・別名など。ローカル対応先は未確定
- `fastapi.middleware.cors` → 外部・標準ライブラリ・別名など。ローカル対応先は未確定
- `gemini_ocr` → `resto-master/backend/gemini_ocr.py`（内容確認済み）

[詳細な図と説明を見る](n-resto-master-backend-main-py-5fb7c4/README.md)

### n-resto-master-backend-stripe-server-main-py-70d2d8

確認済みファイル: `resto-master/backend/stripe_server/main.py`

解析: Python AST。静的なimport／export参照です。関数の実行順序やHTTP通信は表していません。

宣言: `CreateSessionRequest`, `VerifyRequest`, `read_root`, `create_checkout_session`, `verify_payment`

- `os` → 外部・標準ライブラリ・別名など。ローカル対応先は未確定
- `stripe` → 外部・標準ライブラリ・別名など。ローカル対応先は未確定
- `fastapi` → 外部・標準ライブラリ・別名など。ローカル対応先は未確定
- `pydantic` → 外部・標準ライブラリ・別名など。ローカル対応先は未確定
- `typing` → 外部・標準ライブラリ・別名など。ローカル対応先は未確定

[詳細な図と説明を見る](n-resto-master-backend-stripe-server-main-py-70d2d8/README.md)

### n-restomaster-payments-app-main-py-cdb4cf

確認済みファイル: `restomaster-payments/app/main.py`

解析: Python AST。静的なimport／export参照です。関数の実行順序やHTTP通信は表していません。

宣言: `root`, `health`

- `fastapi` → 外部・標準ライブラリ・別名など。ローカル対応先は未確定
- `fastapi.middleware.cors` → 外部・標準ライブラリ・別名など。ローカル対応先は未確定
- `routes_paypay` → `restomaster-payments/app/routes_paypay.py`（内容確認済み）
- `routes_webhook` → `restomaster-payments/app/routes_webhook.py`（内容確認済み）
- `config` → `restomaster-payments/app/config.py`（一覧確認・内容未読）

[詳細な図と説明を見る](n-restomaster-payments-app-main-py-cdb4cf/README.md)

### n-restomaster-payments-app-paypay-client-py-a1b237

確認済みファイル: `restomaster-payments/app/paypay_client.py`

解析: Python AST。静的なimport／export参照です。関数の実行順序やHTTP通信は表していません。

宣言: `PayPayClient`

- `time` → 外部・標準ライブラリ・別名など。ローカル対応先は未確定
- `uuid` → 外部・標準ライブラリ・別名など。ローカル対応先は未確定
- `hashlib` → 外部・標準ライブラリ・別名など。ローカル対応先は未確定
- `hmac` → 外部・標準ライブラリ・別名など。ローカル対応先は未確定
- `base64` → 外部・標準ライブラリ・別名など。ローカル対応先は未確定
- `json` → 外部・標準ライブラリ・別名など。ローカル対応先は未確定
- `typing` → 外部・標準ライブラリ・別名など。ローカル対応先は未確定
- `httpx` → 外部・標準ライブラリ・別名など。ローカル対応先は未確定
- `config` → `restomaster-payments/app/config.py`（一覧確認・内容未読）

[詳細な図と説明を見る](n-restomaster-payments-app-paypay-client-py-a1b237/README.md)

### n-restomaster-payments-app-routes-paypay-py-0963c4

確認済みファイル: `restomaster-payments/app/routes_paypay.py`

解析: Python AST。静的なimport／export参照です。関数の実行順序やHTTP通信は表していません。

宣言: `CreatePayPayRequest`, `CreatePayPayResponse`, `create_paypay_qr`, `PaymentStatusResponse`, `get_payment_status`, `RefundRequest`, `refund_payment`, `check_connection`

- `time` → 外部・標準ライブラリ・別名など。ローカル対応先は未確定
- `fastapi` → 外部・標準ライブラリ・別名など。ローカル対応先は未確定
- `pydantic` → 外部・標準ライブラリ・別名など。ローカル対応先は未確定
- `typing` → 外部・標準ライブラリ・別名など。ローカル対応先は未確定
- `paypay_client` → `restomaster-payments/app/paypay_client.py`（内容確認済み）
- `config` → `restomaster-payments/app/config.py`（一覧確認・内容未読）

[詳細な図と説明を見る](n-restomaster-payments-app-routes-paypay-py-0963c4/README.md)

### n-restomaster-payments-app-routes-webhook-py-0055fc

確認済みファイル: `restomaster-payments/app/routes_webhook.py`

解析: Python AST。静的なimport／export参照です。関数の実行順序やHTTP通信は表していません。

宣言: `verify_webhook_signature`, `paypay_webhook`, `payment_thanks`

- `hmac` → 外部・標準ライブラリ・別名など。ローカル対応先は未確定
- `hashlib` → 外部・標準ライブラリ・別名など。ローカル対応先は未確定
- `base64` → 外部・標準ライブラリ・別名など。ローカル対応先は未確定
- `json` → 外部・標準ライブラリ・別名など。ローカル対応先は未確定
- `fastapi` → 外部・標準ライブラリ・別名など。ローカル対応先は未確定
- `typing` → 外部・標準ライブラリ・別名など。ローカル対応先は未確定
- `config` → `restomaster-payments/app/config.py`（一覧確認・内容未読）

[詳細な図と説明を見る](n-restomaster-payments-app-routes-webhook-py-0055fc/README.md)

