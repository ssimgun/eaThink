const chatbotToggler = document.querySelector(".chatbot-toggler");
const closeBtn = document.querySelector(".close-btn");
const chatbox = document.querySelector(".chatbox");
const chatInput = document.querySelector(".chat-input textarea");
const sendChatBtn = document.querySelector(".chat-input span");

let userMessage = null; // 사용자 메시지를 저장하기 위한 변수
const inputInitHeight = chatInput.scrollHeight; // 입력창의 초기 높이

// API URL
const API_URL = `http://127.0.0.1:8000`; // FastAPI 엔드포인트
let currentEndpoint = '';

const createChatLi = (message, className, isHTML = false) => {
  const chatLi = document.createElement("li");
  chatLi.classList.add("chat", className);
  const chatContent = className === "outgoing" 
    ? `<p></p>` 
    : `<span class="material-symbols-outlined">robot_2</span><p></p>`;
  chatLi.innerHTML = chatContent;

   if (isHTML) {
      chatLi.querySelector("p").innerHTML = message; // HTML로 처리
    } else {
      chatLi.querySelector("p").textContent = message; // 일반 텍스트
    }

  return chatLi;
}

const recommendBtn = document.getElementById("recommend");
const knowledgeBtn = document.getElementById("knowledge");

recommendBtn.addEventListener("click", () => {
    currentEndpoint = '/chat_recommend/';
    chatbox.appendChild(createChatLi("<strong>추천</strong> 모드를 선택하셨습니다!", "incoming", true));
});

knowledgeBtn.addEventListener("click", () => {
    currentEndpoint = '/chat_knowledge/';
    chatbox.appendChild(createChatLi("<b>지식</b> 모드를 선택하셨습니다!", "incoming", true));
});

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
    const response = await fetch(API_URL+currentEndpoint, requestOptions);
    const data = await response.json();
    if (!response.ok) throw new Error(data.error || "챗봇 유형을 선택해주세요!");

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

var maximizeBtn = document.getElementById('maximize');
var btnContainer = document.getElementById("btn-container");

maximizeBtn.addEventListener("click", () => {
    const isHidden = btnContainer.classList.toggle("hidden"); // btn-container 숨기기

    if (isHidden) {
        btnContainer.classList.remove("visible"); // 숨길 때 visible 클래스 제거
    } else {
        btnContainer.classList.add("visible"); // 보일 때 visible 클래스 추가
    }

    // 아이콘 변경
    maximizeBtn.textContent = isHidden ? "keyboard_arrow_down" : "keyboard_arrow_up";
});