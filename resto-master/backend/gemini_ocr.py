import base64
import json
import os
from typing import List

from google import genai
from google.genai import types as gtypes

# 環境変数から API キーを取得（未設定なら例外を投げる）
client = genai.Client(api_key=os.environ["GEMINI_API_KEY"])

MENU_ITEM_SCHEMA = {
    "type": "array",
    "items": {
        "type": "object",
        "properties": {
            "name": {
                "type": "string",
                "description": "料理名（日本語）。例: '白子ポン酢', 'もつ鍋'",
            },
            "price_yen": {
                "type": "integer",
                "description": "税込み価格（円）。例: 800",
            },
            "notes": {
                "type": ["string", "null"],
                "description": "補足情報（産地、数量など）。不要なら null",
            },
        },
        "required": ["name", "price_yen"],
    },
}

PROMPT = """
これは日本語の飲食店メニューの写真です。
画像の中から「料理名」と「税込価格（円）」をすべて抽出してください。

・料理名は日本語でそのまま書いてください（例: '白子ポン酢', 'もつ鍋'）。
・価格は数字のみで、単位「円」は付けず整数にしてください。
・同じ料理が複数箇所に書いてあっても、1つだけでかまいません。
・ドリンクなど不要なものがあれば notes に 'ドリンク' などと書いて区別してください。
・読めないものはスキップしてください（無理に推測しない）。
"""


def call_gemini_menu_ocr(image_bytes: bytes) -> List[dict]:
    """
    メニュー画像から [{name, price_yen, notes}] を返す
    """
    b64 = base64.b64encode(image_bytes).decode("utf-8")

    result = client.models.generate_content(
        model="gemini-2.5-flash",
        contents=[
            gtypes.Content(
                role="user",
                parts=[
                    gtypes.Part(text=PROMPT),
                    gtypes.Part(
                        inline_data=gtypes.Blob(
                            mime_type="image/jpeg",
                            data=b64,
                        )
                    ),
                ],
            )
        ],
        config=gtypes.GenerateContentConfig(
            response_mime_type="application/json",
            response_schema=MENU_ITEM_SCHEMA,
        ),
    )

    return json.loads(result.text)
