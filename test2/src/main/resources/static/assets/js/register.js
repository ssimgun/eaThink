// 변수 선언
var checkID = document.getElementById("uk_id"); // 아이디 확인 버튼
var idMessage = document.getElementById("idMessage"); // 아이디 사용 가능 여부 메세지
var useridField = document.getElementById('userId'); // 아이디 필드
var passwordField = document.getElementById('password'); // 비밀번호 입력 필드
var passwordCheckField = document.getElementById('passwordCheck'); // 비밀번호 확인 입력 필드
var passwordMatchMessage = document.getElementById('passwordMatchMessage'); // 비밀번호 확인 여부 메세지
var passwordMessage = document.getElementById('passwordMessage'); // 비밀번호 적합성 여부 메세지
var checkNN = document.getElementById("uk_nickname"); // 닉네임 확인 버튼
var nicknameMessage = document.getElementById("nicknameMessage"); // 닉네임 적합성 여부 메세지
var nicknameField = document.getElementById("nickname"); // 닉네임 입력 필드
var element_wrap = document.getElementById('wrap');
var addressField = document.getElementById("address"); // 주소 입력 필드
var addressMessage = document.getElementById("addressMessage"); // 주소 적합성 여부 메세지
var address_nameField = document.getElementById("address_name"); // 주소 별칭 입력 필드
var address_nameMessage = document.getElementById("address_nameMessage"); // 주소 별칭 적합성 여부 메세지

var originalUserId = ""; // 처음 입력된 아이디 값
var originalNickname = ""; // 처음 입력된 닉네임 값
var isUserIdChecked = false; // 아이디 확인 여부
var isNicknameChecked = false; // 닉네임 확인 여부

// 폼 필드 적합성 확인 후 submitButton 활성화 / 비활성화
function validateForm() {
    var allValid = true;
    var submitButton = document.getElementById("submitButton");

    // 아이디 필드 유효성 검사
    var userId_value = useridField.value;
    if (!userId_value || !useridField.checkValidity() || !isUserIdChecked) {
        allValid = false;
    }

    // 비밀번호 필드 유효성 검사
    if (!passwordField.value || !passwordField.checkValidity()) {
        allValid = false;
    }

    // 비밀번호 확인 필드 유효성 검사
    if (passwordField.value !== passwordCheckField.value) {
        allValid = false;
    }

    // 닉네임 필드 유효성 검사
    if (!nicknameField.value || !nicknameField.checkValidity() || !isNicknameChecked) {
        allValid = false;
    }

    // 주소 필드 유효성 검사
    if (!addressField.value.startsWith('서울')) {
        allValid = false;
    }

    // 주소 별칭 필드 유효성 검사
    if (!address_nameField.value || !address_nameField.checkValidity()) {
        allValid = false;
    }

    submitButton.disabled = !allValid;
}

// 아이디 중복 확인 버튼 클릭시 이벤트 발생
checkID.addEventListener('click', function () {
    var userId_value = useridField.value;

    if (!userId_value) {
        idMessage.textContent = '아이디를 입력해주세요.';
        idMessage.style.color = 'red';
        validateForm();
        return;
    }

    if (!useridField.checkValidity()) {
        idMessage.textContent = '적합하지 않은 유형의 아이디입니다.';
        idMessage.style.color = 'red';
        validateForm();
        return;
    }

    fetch('/check-id', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ userId: userId_value })
    })
        .then(response => response.json())
        .then(data => {
            if (data.exists) {
                idMessage.textContent = '이미 존재하는 아이디입니다.';
                idMessage.style.color = 'red';
                isUserIdChecked = false;
            } else {
                idMessage.textContent = '사용 가능한 아이디입니다.';
                idMessage.style.color = 'green';
                isUserIdChecked = true;
            }
            validateForm();
        })
        .catch(error => {
            idMessage.textContent = '서버 오류 발생';
            idMessage.style.color = 'red';
            console.error('Error :', error);
            validateForm();
        });
});

// 아이디 필드 초기값 설정
useridField.addEventListener('focus', function(){
    originalUserId = useridField.value;
});

// 닉네임 필드 초기값 설정
nicknameField.addEventListener('focus', function(){
    originalNickname = nicknameField.value;
});

// 아이디, 닉네임 입력값 수정 시 확인 버튼 상태 업데이트
useridField.addEventListener('input', function () {
    if (useridField.value !== originalUserId) {
        isUserIdChecked = false;
    }
    validateForm();
});

nicknameField.addEventListener('input', function () {
    if (nicknameField.value !== originalNickname) {
        isNicknameChecked = false;
    }
    validateForm();
});

// 폼 제출 시 확인 버튼이 눌렸는지 체크
document.getElementById('signupForm').addEventListener('submit', function (event) {
    if (!isUserIdChecked) {
        event.preventDefault();
        alert('아이디 확인 버튼을 눌러주세요');
        return;
    }

    if (!isNicknameChecked) {
        event.preventDefault();
        alert('닉네임 확인 버튼을 눌러주세요');
        return;
    }
});

// 비밀번호 일치 확인
passwordField.addEventListener('input', function () {
    var password = passwordField.value;

    if (!passwordField.checkValidity()) {
        passwordMessage.textContent = '적합하지 않은 유형의 비밀번호입니다.'
        passwordMessage.style.color = 'red';
    } else {
        passwordMessage.textContent = '사용 가능한 비밀번호입니다.'
        passwordMessage.style.color = 'green';
    }
    if (password !== passwordCheck){
        passwordMatchMessage.textContent = '비밀번호가 일치하지 않습니다.';
        passwordMatchMessage.style.color = 'red';
    } else {
        passwordMatchMessage.textContent = '비밀번호가 일치합니다.';
        passwordMatchMessage.style.color = 'green';
    }
    validateForm();
});

passwordCheckField.addEventListener('input', function () {
    var password = passwordField.value;
    var passwordCheck = passwordCheckField.value;

    if (password === passwordCheck) {
        passwordMatchMessage.textContent = '비밀번호가 일치합니다.';
        passwordMatchMessage.style.color = 'green';
    } else {
        passwordMatchMessage.textContent = '비밀번호가 일치하지 않습니다.';
        passwordMatchMessage.style.color = 'red';
    }
    validateForm();
});

// 닉네임 중복 확인 버튼 클릭시 이벤트 발생
checkNN.addEventListener('click', function () {
    var nickname_value = nicknameField.value;

    if (!nickname_value) {
        nicknameMessage.textContent = '닉네임을 입력해주세요.';
        nicknameMessage.style.color = 'red';
        validateForm();
        return;
    }

    if (!nicknameField.checkValidity()) {
        nicknameMessage.textContent = '적합하지 않은 유형의 닉네임입니다.';
        nicknameMessage.style.color = 'red';
        validateForm();
        return;
    }

    fetch('/check-nickname', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ nickname: nickname_value })
    })
        .then(response => response.json())
        .then(data => {
            if (data.exists) {
                nicknameMessage.textContent = '이미 존재하는 닉네임입니다.';
                nicknameMessage.style.color = 'red';
                isNicknameChecked = false;
            } else {
                nicknameMessage.textContent = '사용 가능한 닉네임입니다.';
                nicknameMessage.style.color = 'green';
                isNicknameChecked = true;
            }
            validateForm();
        })
        .catch(error => {
            nicknameMessage.textContent = '서버 오류 발생';
            nicknameMessage.style.color = 'red';
            console.error('Error :', error);
            validateForm();
        });
});


// 우편번호 찾기 찾기 화면을 넣을 element
function foldDaumPostcode() {
    element_wrap.style.display = 'none';
}

function sample3_execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function (data) {
            // 주소 필드에 값 넣기
            addressField.value = data.address; // 선택한 주소
            validateAddress();
        }
    }).open();
}

// 주소 필드 유효성 검사 함수
function validateAddress() {
    var address_val = addressField.value;

    if (!address_val.startsWith('서울')) {
        addressMessage.textContent = '서울특별시의 주소만 가능합니다';
        addressMessage.style.color = 'red';
    } else {
        addressMessage.textContent = '';
    }
    validateForm();
};

// 주소 별칭 확인
address_nameField.addEventListener("input", function () {
    var address_name_val = address_nameField.value;

    if (!address_nameField.checkValidity()) {
        address_nameMessage.textContent = '적합하지 않은 유형의 별칭입니다.'
        address_nameMessage.style.color = 'red';
    } else {
        address_nameMessage.textContent = '사용가능한 별칭입니다.'
        address_nameMessage.style.color = 'green';
    }
    validateForm();
});
