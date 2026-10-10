# Restaurant Orchestration System (RestoMaster)

RestoMasterは、モバイルオーダー、POSレジ、決済連携、そしてAI活用機能を統合した次世代のレストラン管理システムです。Android端末1台でサーバ兼クライアントとして動作する「Masterモード」と、客席やスタッフハンディとして動作するモードを備え、Pythonバックエンドと連携して高度な機能を提供します。

## 📂 プロジェクト構成

このリポジトリは以下の2つの主要コンポーネントで構成されています。

| ディレクトリ | コンポーネント | 説明 |
| :--- | :--- | :--- |
| `resto-master/` | **Android Application** | システムの中核。POS、管理画面、ローカルHTTPサーバ(Ktor)、モバイルオーダーWeb UIを提供します。 |
| `restomaster-payments/` | **Backend Service** | Python/FastAPI製のマイクロサービス。PayPay/Stripe決済処理やGemini APIを用いた画像処理を担当します。 |

---

## 🚀 主な機能

### 1. モバイルオーダー (QR Order)
*   **客席スマホ注文**: 顧客はQRコードを読み取るだけで、アプリインストール不要でブラウザから注文可能。
*   **リアルタイム同期**: 注文状況や「お会計待ち」「支払済」などのステータスがリアルタイムでスタッフ端末と同期。
*   **多言語メニュー**: 日本語、英語、中国語、フランス語などへのAI自動翻訳対応。

### 2. POS・管理機能 (Master Terminal)
*   **テーブル管理**: 空席・滞在・会計待ち・支払い済みなどの状態をひと目で把握。
*   **注文管理**: ハンディ端末としての注文入力、調理ステータス管理。
*   **会計・レシート**: インボイス制度（適格請求書）に対応したレシート印刷、分割会計。

### 3. 決済連携 (Payments)
*   **PayPay**: QRコード生成、ステータス確認、返金処理（部分返金対応）。
*   **Stripe**: クレジットカード決済。

### 4. AI・スマート機能 (Powered by Gemini)
*   **メニュー画像補正**: 撮影した料理写真をAIが自動で「美味しそう」に補正（明るさ、コントラスト、湯気感など）。
*   **アレルギー抽出**: メニュー名や画像からアレルギー品目をAIが予測・提案。
*   **メニュー翻訳**: メニュー名と説明文を一括で他言語へ翻訳。

---

## 🛠 セットアップ & 起動手順

### 前提条件
*   **Android Studio** (Ladybug | 2024.2.1 以上推奨)
*   **JDK 17**
*   **Python 3.11+**
*   **API Keys**:
    *   Google Gemini API Key
    *   PayPay API Key / Secret / Merchant ID
    *   Stripe API Key

### 1. Python Backend の起動

決済処理やAI機能を利用するために必要です。

```bash
cd restomaster-payments

# 仮想環境の作成と有効化
python -m venv venv
source venv/bin/activate  # Windows: venv\Scripts\activate

# 依存関係のインストール
pip install -r requirements.txt

# 設定ファイルの作成 (.env)
cp config.py.example config.py # 必要に応じて作成
# 環境変数を設定 (または直接 config.py を編集)
export PAYPAY_API_KEY="your_key"
export PAYPAY_API_SECRET="your_secret"
export PAYPAY_MERCHANT_ID="your_id"
export GEMINI_API_KEY="your_gemini_key"

# サーバ起動 (ポート8000)
uvicorn app.main:app --reload --host 0.0.0.0 --port 8000
```

### 2. Android App のビルド & 実行

1.  プロジェクト `resto-master` を Android Studio で開きます。
2.  `local.properties` に必要なAPIキー設定が含まれているか確認します（またはビルド設定を確認）。
3.  **Build Variant** を `debug` に設定します。
4.  実機またはエミュレータで実行します。

> **注意**: AndroidエミュレータからローカルのPythonバックエンド(localhost:8000)に接続する場合、Android側は `10.0.2.2:8000` を使用するように設定されています (`NetworkModule.kt` 参照)。実機の場合はPCのIPアドレスを指定する必要があります。

---

## 🏗 アーキテクチャ概要

### Android (Kotlin / Jetpack Compose)
*   **MVVM**: UIとロジックの分離。
*   **Room Database**: ローカルデータの永続化。
*   **Ktor Server (Embedded)**: Android端末内でWebサーバを起動し、客席スマホ向けのHTML/APIを提供。
*   **Hilt**: 依存関係注入。
*   **Retrofit**: 外部API (Python Backend) との通信。

### Backend (Python / FastAPI)
*   **FastAPI**: 高速な非同期Webフレームワーク。
*   **Services**:
    *   `/payments/paypay`: PayPay SDKを用いた決済処理。
    *   `/images/food/enhance`: Gemini APIを用いた画像処理。

## 📝 開発ガイド

### コードスタイル
*   **Kotlin**: 公式スタイルガイドに準拠。
*   **Python**: PEP8準拠。

### 主要なコマンド

**Androidビルド (CLI)**
```bash
cd resto-master
./gradlew assembleDebug
```

**Pythonテスト (例)**
```bash
cd restomaster-payments
# (テスト作成時)
```

## © Authors
Project RestoMaster Team

<!-- omm-scan-2026-10-11 -->

## 構成図・依存関係

[日本語の構成図と説明を見る](docs/architecture/omm-scan-2026-10-11/README.md)（2026-10-11 初回解析）。解析範囲と未検証事項はリンク先に記載しています。
