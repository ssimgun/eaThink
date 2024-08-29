// 우편번호 찾기 찾기 화면을 넣을 element
var element_wrap = document.getElementById('wrap');

function foldDaumPostcode() {
    element_wrap.style.display = 'none';
}

function sample3_execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            // 주소 필드에 값 넣기
            document.getElementById("address").value = data.address; // 선택한 주소
            // 별칭 필드는 필요에 따라 설정 가능
            document.getElementById("address_name").value = ''; // 별칭 필드 초기화
        }
    }).open();
}

function foldDaumPostcode() {
    document.getElementById("wrap").style.display = 'none';
}

// 폼 필드 적합성 여부 확인
// 아이디 중복 확인 버튼 클릭시 이벤트 발생
var checkID = document.getElementById("uk_id"); // 아이디 확인 버튼
var idMessage = document.getElementById("idMessage"); // 아이디 사용 가능 여부 메세지
var useridField = document.getElementById('userId'); // 아이디 필드

checkID.addEventListener("click", function() {
    var userId_value = useridField.value; // 아이디 필드 입력값

    // 입력 값이 비어있으면 리턴
    if (!userId_value) {
        idMessage.textContent = '아이디를 입력해주세요.';
        idMessage.style.color = 'red';
        return;
    }

    // 아이디 폼 유효성 확인
    if (!useridField.checkValidity()) {
        idMessage.textContent = '적합하지 않은 유형의 아이디입니다.';
        idMessage.style.color = 'red';
        return;
    }

    // 서버에 아이디 중복 확인 요청
    fetch('/check-id', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ userId: userId_value }) // 서버 요청 시 사용되는 필드명
    })
    .then(response => response.json())
    .then(data => {
        console.log('Response data:', data);
        if (data.exists) {
            idMessage.textContent = '이미 존재하는 아이디입니다.';
            idMessage.style.color = 'red';
        } else {
            idMessage.textContent = '사용 가능한 아이디입니다.';
            idMessage.style.color = 'green';
        }
    })
    .catch(error => {
        idMessage.textContent = '서버 오류 발생';
        idMessage.style.color = 'red';
        console.error('Error :', error);
    });

    console.log('userId:', userId_value);

});

// 비밀번호 일치 확인
var passwordField = document.getElementById('password');
var passwordCheckField = document.getElementById('passwordCheck');
var passwordMatchMessage = document.getElementById('passwordMatchMessage');
var passwordMessage = document.getElementById('passwordMessage');

passwordField.addEventListener('input', function(){
    var password = passwordField.value;
    var passwordCheck = passwordCheckField.value;

    if(!passwordField.checkValidity()){
        passwordMessage.textContent = '적합하지 않은 유형의 비밀번호입니다.'
        passwordMessage.style.color = 'red';
    } else{
        passwordMessage.textContent = '사용 가능한 비밀번호입니다.'
        passwordMessage.style.color = 'green';
    }
})

passwordCheckField.addEventListener('input', function(){
     var password = passwordField.value;
     var passwordCheck = passwordCheckField.value;

    if (password === passwordCheck) {
        passwordMatchMessage.textContent = '비밀번호가 일치합니다.';
        passwordMatchMessage.style.color = 'green';
    } else {
        passwordMatchMessage.textContent = '비밀번호가 일치하지 않습니다.';
        passwordMatchMessage.style.color = 'red';
    }
})

// 닉네임 중복 확인
var checkNN = document.getElementById("uk_nickname");
var nicknameMessage = document.getElementById("nicknameMessage");
var nicknameField = document.getElementById("nickname");

checkNN.addEventListener('click', function(){
    var nickname_value = nicknameField.value;

//  닉네임 미입력시
    if(!nickname_value){
        nicknameMessage.textContent = '닉네임을 입력해주세요.';
        nicknameMessage.style.color = 'red';
        return;
    }

//  닉네임 폼 유효성 확인
    if(!nicknameField.checkValidity()){
        nicknameMessage.textContent = '적합하지 않은 유형의 닉네임입니다.';
        nicknameMessage.style.color = 'red';
        return;
    }

    // 서버에 닉네임 중복 확인 요청
    fetch('/check-nickname', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ nickname: nickname_value }) // 서버 요청 시 사용되는 필드명
    })
    .then(response => response.json())
    .then(data => {
        console.log('Response data:', data);
        if (data.exists) {
            nicknameMessage.textContent = '이미 존재하는 닉네임입니다.';
            nicknameMessage.style.color = 'red';
        } else {
            nicknameMessage.textContent = '사용 가능한 닉네임입니다.';
            nicknameMessage.style.color = 'green';
        }
    })
    .catch(error => {
        nicknameMessage.textContent = '서버 오류 발생';
        nicknameMessage.style.color = 'red';
        console.error('Error :', error);
    });

    console.log('nickname:', nickname_value);
})

// 주소, 주소 별칭 확인
var addressField = document.getElementById("address")
var address_nameField = document.getElementById("address_name");
var address_nameMessage = document.getElementById("address_nameMessage");

address_nameField.addEventListener("input", function(){
    var address_name_val = address_nameField.value;

    if(!address_nameField.checkValidity()){
        address_nameMessage.textContent = '적합하지 않은 유형의 별칭입니다.'
        address_nameMessage.style.color = 'red';
    }else{
        address_nameMessage.textContent = '사용가능한 별칭입니다.'
        address_nameMessage.style.color = 'green';
    }
})
