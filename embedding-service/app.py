from fastapi import FastAPI
from sentence_transformers import SentenceTransformer
from typing import List

app = FastAPI()
model = SentenceTransformer('all-MiniLM-L6-v2')

@app.post("/embed")
def embed(payload: dict):

    texts: List[str] = payload.get("texts", [])

    if not texts:
        return {"vectors": []}

    vectors = model.encode(texts)

    return {
        "vectors": vectors.tolist()
    }