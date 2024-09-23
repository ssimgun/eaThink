const chatbotToggler = document.querySelector(".chatbot-toggler");
const closeBtn = document.querySelector(".close-btn");
const chatbox = document.querySelector(".chatbox");
const chatInput = document.querySelector(".chat-input textarea");
const sendChatBtn = document.querySelector(".chat-input span");

let userMessage = null; // 사용자 메시지를 저장하기 위한 변수
const inputInitHeight = chatInput.scrollHeight; // 입력창의 초기 높이

// API URL
const API_URL = `http://127.0.0.1:8000/chat/`; // FastAPI 엔드포인트

const createChatLi = (message, className) => {
  const chatLi = document.createElement("li");
  chatLi.classList.add("chat", className);
  const chatContent = className === "outgoing" 
    ? `<p></p>` 
    : `<span class="material-symbols-outlined">smart_toy</span><p></p>`;
  chatLi.innerHTML = chatContent;
  chatLi.querySelector("p").textContent = message;
  return chatLi;
}

const generateResponse = async (chatElement) => {
  const messageElement = chatElement.querySelector("p");

  // API 요청을 위한 설정
  const requestOptions = {
    method: "POST",
    headers: {
        "Content-Type": "application/json",
        "X-API-Key": "MY_API_KEY"
     },
    body: JSON.stringify({ user_input: userMessage }),  // FastAPI에 맞게 수정
  }

  // POST 요청 보내기
  try {
    const response = await fetch(API_URL, requestOptions);
    const data = await response.json();
    if (!response.ok) throw new Error(data.error || "서버 오류");

    // 챗봇 응답을 메시지 요소에 업데이트
    messageElement.textContent = data.bot_output; // FastAPI의 응답에 맞게 수정
  } catch (error) {
    messageElement.classList.add("error");
    messageElement.textContent = error.message;
  } finally {
    chatbox.scrollTo(0, chatbox.scrollHeight);
  }
}

const handleChat = () => {
  userMessage = chatInput.value.trim();
  if (!userMessage) return;

  // 입력 필드 초기화
  chatInput.value = "";
  chatInput.style.height = `${inputInitHeight}px`;

  // 사용자 메시지를 채팅 박스에 추가
  chatbox.appendChild(createChatLi(userMessage, "outgoing"));
  chatbox.scrollTo(0, chatbox.scrollHeight);

  setTimeout(() => {
    const incomingChatLi = createChatLi("잠시만 기다려주시오...", "incoming");
    chatbox.appendChild(incomingChatLi);
    chatbox.scrollTo(0, chatbox.scrollHeight);
    generateResponse(incomingChatLi);
  }, 600);
}

// 입력창의 높이를 조정
chatInput.addEventListener("input", () => {
  chatInput.style.height = `${inputInitHeight}px`;
  chatInput.style.height = `${chatInput.scrollHeight}px`;
});

// Enter 키로 메시지 전송
chatInput.addEventListener("keydown", (e) => {
  if (e.key === "Enter" && !e.shiftKey) {
    e.preventDefault();
    handleChat();
  }
});

// 버튼 클릭으로 메시지 전송
sendChatBtn.addEventListener("click", handleChat);

// 챗봇 열기/닫기 이벤트 리스너 (주석 처리된 부분 복원)
// closeBtn.addEventListener("click", () => document.body.classList.remove("show-chatbot"));
// chatbotToggler.addEventListener("click", () => document.body.classList.toggle("show-chatbot"));
