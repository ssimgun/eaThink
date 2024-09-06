var element_wrap = document.getElementById('wrap');
var addressField = document.getElementById("address"); // 주소 입력 필드
var addressMessage = document.getElementById("addressMessage"); // 주소 적합성 여부 메세지
var address_nameField = document.getElementById("address_name"); // 주소 별칭 입력 필드
var address_nameMessage = document.getElementById("address_nameMessage"); // 주소 별칭 적합성 여부 메세지
var submitButton = document.getElementById("submitButton"); // 제출 버튼

// 폼 필드 적합성 확인 후 submitButton 활성화 / 비활성화
function validateForm() {
    var allValid = true;


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

// 우편번호 찾기 화면을 넣을 element
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


submitButton.addEventListener("click", validateAddress);

