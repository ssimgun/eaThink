from pydantic import BaseModel
from fastapi import FastAPI, Depends, HTTPException
from fastapi.security import APIKeyHeader
from fastapi.middleware.cors import CORSMiddleware
from app.model_chatbot.knowledge_bot import knowledge_bot as knowledge
from app.model_chatbot.recommend_bot import recommend_bot as recommend
import warnings
warnings.filterwarnings('ignore')

class ChatRequest(BaseModel):
    user_input: str  # 사용자 입력
    user_id : int
    x : float
    y : float

class ChatResponse(BaseModel):
    bot_output: str  # 챗봇 응답

app = FastAPI()

API_KEY = "MY_API_KEY"
api_key_header = APIKeyHeader(name="X-API-Key")

def verify_api_key(api_key: str = Depends(api_key_header)):
    if api_key != API_KEY:
        raise HTTPException(status_code=403, detail="Invalid API Key")

origins = [
    "http://localhost:9099",
    "http://127.0.0.1:8000",
]

app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.post("/chat_recommend/", dependencies=[Depends(verify_api_key)], response_model=ChatResponse)
async def chat_recommend(request:ChatRequest):
    response = recommend.test_genarate(user_input=request.user_input, x=request.x, y=request.y, id=request.user_id)
    return {"bot_output": response}

@app.post("/chat_knowledge/", dependencies=[Depends(verify_api_key)], response_model=ChatResponse)
async def chat_knowledge(user_input: ChatRequest):
    response = knowledge.generate_knowledge_response(user_input.user_input)
    return {"bot_output": response}

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="127.0.0.1", port=8000, reload=True)  # 호스트 확인