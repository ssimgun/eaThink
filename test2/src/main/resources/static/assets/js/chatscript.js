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
    : `<span class="material-symbols-outlined robot-icon">robot_2</span><p></p>`;
  chatLi.innerHTML = chatContent;

  // 아이콘 배경색을 모드에 따라 설정
   if (className === "incoming") {
       const robotIcon = chatLi.querySelector(".robot-icon");
       if (currentEndpoint === '/chat_recommend/') {
           robotIcon.style.backgroundColor = "#f6755e"; // 추천 모드 배경색
       } else if (currentEndpoint === '/chat_knowledge/') {
           robotIcon.style.backgroundColor = "#1a8d40"; // 지식 모드 배경색
       }
   }

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
    const restaurantName = getNameFromChatbot(); // 챗봇 응답 후 해당 음식점 이름 추출
    if (restaurantName) {
        fetchRestaurantByName(restaurantName); // 추출한 음식점 이름으로 비동기 요청 후 마커 생성
    }
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
sendChatBtn.addEventListener("click", () => {
    handleChat();
});


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

// 거리 계산 함수
function calculateDistance(current_x, current_y, data_x, data_y) {
    const toRadians = (degrees) => degrees * (Math.PI / 180);
    const earthRadius = 6371e3; // 지구의 반지름 (미터)

    const lat1Rad = toRadians(current_x);
    const lat2Rad = toRadians(data_x);
    const deltaLatRad = toRadians(data_x - current_x);
    const deltaLngRad = toRadians(data_y - current_y);

    const a = Math.sin(deltaLatRad / 2) * Math.sin(deltaLatRad / 2) +
           Math.cos(lat1Rad) * Math.cos(lat2Rad) *
           Math.sin(deltaLngRad / 2) * Math.sin(deltaLngRad / 2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    const distance = earthRadius * c; // 결과를 미터로 변환
    return 500 >= distance;
}

// 챗봇 출력값에서 음식점 이름 추출
function getNameFromChatbot(){
    const chatbotOutputElement = document.querySelector("#chatbot-container > div > ul > .chat.incoming:last-child > p");
    let restaurantName;
    if(chatbotOutputElement){
        var chatbotOutput = chatbotOutputElement.textContent; // 출력 텍스트 가져오기
        var lines = chatbotOutput.split('\n'); // 줄바꿈 기준으로 나누기
        restaurantName = lines[0]; // 첫 번째 줄이 음식적 이름

    } else{
        console.error('출력 요소를 찾을 수 없습니다. ')

    }
    return restaurantName;
}

// 추출한 음식점 이름로 데이터베이스에 조회 => 해당 음식점이름에 해당하는 음식점 row 를 List 로 반환
function fetchRestaurantByName(restaurantName){
    if(!restaurantName) return;

    $.ajax({
        url:'/nameToRestaurant',
        method: 'GET',
        data:{name:restaurantName},
        success: function(restaurants){
            restaurants.forEach(function(restaurant){
                var food_x = restaurant.x;
                var food_y = restaurant.y;

                // 현재 위치와 음식점 위치 계산
                if(calculateDistance(x, y, food_x, food_y)){
                    var position = new naver.maps.LatLng(food_y, food_x);
                    var restaurant_marker = './images/marker/restaurant.png';
                    // 음식점 마커 옵션
                    var foodMarkerOptions = {
                        position: position,
                        map: map,
                        icon: {
                            url: restaurant_marker,
                            origin: new naver.maps.Point(0, 0),
                            anchor: new naver.maps.Point(17, 0)
                        }
                    };

                    // 마커에 마우스 올려놓을시 음식점 이름 표시
                    var foodInfo = new naver.maps.InfoWindow({
                        content: `<div style="padding:10px; background-color: white; color: black;">${restaurant.name}</div>`
                    });

                    // 음식점 마커 생성
                    var restaurantMarker = new naver.maps.Marker(foodMarkerOptions);

                    // 마커에 마우스 hover => 음식점 이름
                    naver.maps.Event.addListener(restaurantMarker, 'mouseover', function(){
                        foodInfo.open(map, restaurantMarker.getPosition());
                    });

                    // 마커에서 마우스 벗어날시 해제
                    naver.maps.Event.addListener(restaurantMarker, 'mouseout', function() {
                        foodInfo.close();
                    });

                    // 음식점 정보 반영
                    const restaurantContainer = document.querySelector('.restaurant-container');
                    const restaurantElement = document.createElement('div');
                    restaurantElement.classList.add('restaurant-info',`restaurant-${restaurant.id}`,'hidden');
                    restaurantElement.innerHTML = `
                        <h2>${restaurant.name}</h2>
                        <figure>
                            <blockquote>${restaurant.number_address}</blockquote>
                            <footer>
                                ${restaurant.road_address}
                            </footer>
                        </figure>
                    `;
                    restaurantContainer.appendChild(restaurantElement);

                    // 마지막으로 열린 음식점 요소 저장
                    let lastOpenedRestaurantElement = null;

                    // 해당음식점 마커 클릭시 음식점 정보 나타내기
                    naver.maps.Event.addListener(restaurantMarker, 'click', function() {
                        // 이전에 열려 있던 음식점 요소가 있으면 숨김
                        if (lastOpenedRestaurantElement && lastOpenedRestaurantElement !== restaurantElement){
                            lastOpenedRestaurantElement.classList.add('hidden');
                            lastOpenedRestaurantElement.classList.remove('visible');
                        }

                        // 현재 클릭한 음식점 요소 표시
                        if (restaurantElement) {
                            restaurantElement.classList.remove('hidden');
                            restaurantElement.classList.add('visible'); // 추가
                            lastOpenedRestaurantElement = restaurantElement; // 현재 음식점 요소 저장
                        } else {
                            console.error('해당 음식점 요소를 찾을 수 없습니다:', restaurant.id); // 오류 메시지
                        }
                    });

                }else{
                    console.log('반경안에 없음');
                }
            });
        },
        error:function(error){
            console.error('데이터 조회 실패', error);
        }
    });
}