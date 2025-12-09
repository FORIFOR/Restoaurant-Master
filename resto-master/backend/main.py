from fastapi import FastAPI, File, HTTPException, UploadFile
from fastapi.middleware.cors import CORSMiddleware

from .gemini_ocr import call_gemini_menu_ocr

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # 開発中は緩め、本番は必要な Origin に絞る
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


@app.post("/api/ocr/menu-gemini")
async def ocr_menu_gemini(file: UploadFile = File(...)):
    if file.content_type not in ("image/jpeg", "image/png"):
        raise HTTPException(status_code=400, detail="JPEG/PNG のみ対応です")

    image_bytes = await file.read()

    try:
        items = call_gemini_menu_ocr(image_bytes)
    except Exception as e:
        raise HTTPException(
            status_code=500, detail=f"Gemini 呼び出しに失敗しました: {e}"
        ) from e

    return {"items": items}
