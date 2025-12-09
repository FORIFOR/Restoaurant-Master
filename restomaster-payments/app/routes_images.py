import os
from io import BytesIO
from fastapi import APIRouter, UploadFile, File, HTTPException, Response
from PIL import Image
from google import genai
from google.genai import types

router = APIRouter()

# Initialize Gemini Client
# Ensure GEMINI_API_KEY is set in environment variables
client = genai.Client(api_key=os.environ.get("GEMINI_API_KEY"))

FOOD_ENHANCE_PROMPT = """
この料理写真をメニュー用に最適化してください。
- 自然で現実的な範囲で明るさ・コントラストを改善
- 料理の色味を温かく、清潔感のあるトーンへ
- 湯気や艶感を控えめに強調
- 不自然な生成物や過度な加工は避ける
- 皿の色飛びを抑える
""".strip()

def enhance_food_photo(image_bytes: bytes) -> tuple[bytes, str]:
    try:
        img = Image.open(BytesIO(image_bytes)).convert("RGB")
        
        # Resize if too large to save tokens/time (optional, but recommended by user)
        max_size = 1536
        if max(img.size) > max_size:
            img.thumbnail((max_size, max_size))

        resp = client.models.generate_content(
            model="gemini-2.5-flash-image-preview",
            contents=[img, FOOD_ENHANCE_PROMPT],
            config=types.GenerateContentConfig(
                response_modalities=[types.Modality.TEXT, types.Modality.IMAGE]
            ),
        )

        out_img = None
        texts = []

        if not resp.candidates:
             raise RuntimeError("No candidates returned from Gemini.")

        parts = resp.candidates[0].content.parts
        for p in parts:
            if getattr(p, "text", None):
                texts.append(p.text)
            elif getattr(p, "inline_data", None):
                out_img = Image.open(BytesIO(p.inline_data.data)).convert("RGB")

        if out_img is None:
            raise RuntimeError("No image returned from Gemini.")

        buf = BytesIO()
        out_img.save(buf, format="JPEG", quality=95, optimize=True)
        return buf.getvalue(), "\n".join(texts)
    except Exception as e:
        print(f"Error in enhance_food_photo: {e}")
        raise e

@router.post("/images/food/enhance")
async def enhance_food(file: UploadFile = File(...)):
    if not file.content_type or not file.content_type.startswith("image/"):
        raise HTTPException(status_code=400, detail="image only")

    raw = await file.read()
    try:
        enhanced_bytes, note = enhance_food_photo(raw)
        
        # Clean note text (remove newlines from headers if passing in header)
        safe_note = note.replace("\n", " ").replace("\r", " ")[:200]

        return Response(
            content=enhanced_bytes,
            media_type="image/jpeg",
            headers={"X-Enhance-Note": safe_note}
        )
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
