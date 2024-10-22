import torch
from transformers import PreTrainedTokenizerFast, GPT2LMHeadModel
from torch.utils.data import TensorDataset, DataLoader
from transformers import AdamW
import pandas as pd

# 모델 실행
device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
print(f"사용 중인 장치: {device}")

# 지식 봇 - 모델과 토크나이저 로드
knwoledge_tokenizer = PreTrainedTokenizerFast.from_pretrained("./app/model_chatbot/knowledge_bot/food_chatbot_model_최종")
knowledge_model = GPT2LMHeadModel.from_pretrained("./app/model_chatbot/knowledge_bot/food_chatbot_model_최종")
knowledge_model.to(device)
knowledge_model.eval()

# 음식 지식 챗봇
def generate_knowledge_response(input_text):
    # 입력 길이 제한 (512자까지 허용)
    input_text = input_text[:512]
    
    # 입력 텍스트 인코딩
    input_ids = knwoledge_tokenizer.encode(f"질문: {input_text}\n답변: ", return_tensors='pt').to(device)

    # 답변 생성
    with torch.no_grad():
        output_ids = knowledge_model.generate(
            input_ids,
            max_length=200,  # 질문 길이 + 50자
            min_length=20,  # 최소 답변 길이
            pad_token_id=knwoledge_tokenizer.eos_token_id,
            do_sample=True,
            top_k=50,
            top_p=0.92,
            temperature=0.7,
            repetition_penalty=1.2,
            num_return_sequences=1,
            eos_token_id=knwoledge_tokenizer.eos_token_id,
            use_cache=True
        )

    # 출력 디코딩
    output = knwoledge_tokenizer.decode(
        output_ids[0],
        skip_special_tokens=True,
        clean_up_tokenization_spaces=True
    )

    # '답변:' 이후의 텍스트 추출
    try:
        response = output.split("답변:")[1].strip()
    except IndexError:
        response = "죄송해요, 답변을 생성하는 데 문제가 발생했어요."

    return response