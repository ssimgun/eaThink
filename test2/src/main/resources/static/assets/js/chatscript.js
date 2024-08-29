
const chatbotToggler = document.querySelector(".chatbot-toggler");
const closeBtn = document.querySelector(".close-btn");
const chatbox = document.querySelector(".chatbox");
const chatInput = document.querySelector(".chat-input textarea");
const sendChatBtn = document.querySelector(".chat-input span");

//chatbotToggler: 챗봇을 열고 닫는 버튼을 선택합니다.
//closeBtn: 챗봇을 닫는 버튼을 선택합니다.
//chatbox: 채팅 메시지를 표시하는 영역입니다.
//chatInput: 사용자가 입력하는 텍스트 입력창을 선택합니다.
//sendChatBtn: 사용자가 메시지를 전송할 때 클릭하는 버튼입니다


let userMessage = null; // Variable to store user's message
const inputInitHeight = chatInput.scrollHeight;
//userMessage 사용자 메시지를 저장하기 위한 변수
// inputInHeight 입력창의 초기 높이


// API configuration
const API_KEY = "PASTE-YOUR-API-KEY"; // Your API key here
const API_URL = `https://generativelanguage.googleapis.com/v1/models/gemini-pro:generateContent?key=${API_KEY}`;

const createChatLi = (message, className) => {
  // 새로운 채팅 메시지를 리스트 항목으로 생성
  // message : 채팅에 표시할 텍스트 메시지
  // className : 메시지 클래스 이름 (outgoing 또는 incoming 을 통해 사용자로 부터 온 것인지 시스템으로 온건지 구분)

  const chatLi = document.createElement("li");
  chatLi.classList.add("chat", `${className}`);
  // 사용자 입력이면 <p> 태그 생성 시스템 입력이면 <span>+<p> 생성
  let chatContent = className === "outgoing" ? `<p></p>` : `<span class="material-symbols-outlined">smart_toy</span><p></p>`;
  chatLi.innerHTML = chatContent;
  chatLi.querySelector("p").textContent = message;
  return chatLi; // return chat <li> element
}

const generateResponse = async (chatElement) => {
  const messageElement = chatElement.querySelector("p");

  // Define the properties and message for the API request
  const requestOptions = {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ 
      contents: [{ 
        role: "user", 
        parts: [{ text: userMessage }] 
      }] 
    }),
  }

  // Send POST request to API, get response and set the reponse as paragraph text
  try {
    const response = await fetch(API_URL, requestOptions);
    const data = await response.json();
    if (!response.ok) throw new Error(data.error.message);
    
    // Get the API response text and update the message element
    messageElement.textContent = data.candidates[0].content.parts[0].text.replace(/\*\*(.*?)\*\*/g, '$1');
  } catch (error) {
    // Handle error
    messageElement.classList.add("error");
    messageElement.textContent = error.message;
  } finally {
    chatbox.scrollTo(0, chatbox.scrollHeight);
  }
}

const handleChat = () => {
  userMessage = chatInput.value.trim(); // Get user entered message and remove extra whitespace
  if (!userMessage) return;

  // Clear the input textarea and set its height to default
  chatInput.value = "";
  chatInput.style.height = `${inputInitHeight}px`;

  // Append the user's message to the chatbox
  chatbox.appendChild(createChatLi(userMessage, "outgoing"));
  chatbox.scrollTo(0, chatbox.scrollHeight);

  setTimeout(() => {
    // Display "Thinking..." message while waiting for the response
    const incomingChatLi = createChatLi("잠시만 기다려주시오", "incoming");
    chatbox.appendChild(incomingChatLi);
    chatbox.scrollTo(0, chatbox.scrollHeight);
    generateResponse(incomingChatLi);
  }, 600);
}

chatInput.addEventListener("input", () => {
  // Adjust the height of the input textarea based on its content
  chatInput.style.height = `${inputInitHeight}px`;
  chatInput.style.height = `${chatInput.scrollHeight}px`;
});

chatInput.addEventListener("keydown", (e) => {
  // If Enter key is pressed without Shift key and the window 
  // width is greater than 800px, handle the chat
  if (e.key === "Enter" && !e.shiftKey && window.innerWidth > 800) {
    e.preventDefault();
    handleChat();
  }
});

sendChatBtn.addEventListener("click", handleChat);

// 토글로 챗봇 열기 관련 이벤트 리스너
//closeBtn.addEventListener("click", () => document.body.classList.remove("show-chatbot"));
//chatbotToggler.addEventListener("click", () => document.body.classList.toggle("show-chatbot"));