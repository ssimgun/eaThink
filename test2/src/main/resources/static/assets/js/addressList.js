var element_wrap = document.getElementById('wrap');
var addressField = document.getElementById("address"); // 주소 입력 필드
var addressMessage = document.getElementById("addressMessage"); // 주소 적합성 여부 메세지
var address_nameField = document.getElementById("address_name"); // 주소 별칭 입력 필드
var address_nameMessage = document.getElementById("address_nameMessage"); // 주소 별칭 적합성 여부 메세지
var submitButton = document.getElementById("submitButton"); // 주소 추가하기 버튼

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

function execDaumPostcode() {
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

function deleteAddress(id) {
    // 사용자에게 확인을 요청하는 다이얼로그 표시
    var confirmation = confirm("해당 주소를 삭제하시겠습니까?");

    if (confirmation) {
        // 사용자가 '예'를 클릭한 경우
        fetch('/delete-user', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ Id: id })
        })
        .then(response => {
            if (response.ok) {
                return response.text(); // 서버에서 반환하는 데이터 읽기
            } else {
                throw new Error("서버 오류");
            }
        })
        .then(data => {
            alert("삭제가 완료되었습니다.");
            window.location.href = "/myInfo/addressList"; // 탈퇴 완료 후 이동할 페이지
        })
        .catch(error => {
            console.error('Error:', error);
            alert("삭제 과정에서 오류가 발생했습니다.");
        });
    } else {
        // 사용자가 '아니오'를 클릭한 경우 아무 작업도 하지 않음
        return;
    }
}

function editAddress(id) {
    var default_editBtn = document.getElementById("default_btn_edit_" + id); // 기본 주소지 수정 버튼

    // 주소와 주소 별칭 필드를 입력 가능하게 표시
    document.getElementById('address_name_input_' + id).style.display = 'inline';
    document.getElementById('address_input_' + id).style.display = 'inline';

    document.getElementById('address_name_display_' + id).style.display = 'none';
    document.getElementById('address_display_' + id).style.display = 'none';

    if(!default_editBtn){
        // 수정 버튼 숨기기 및 저장 버튼 표시
        document.getElementById('btn_edit_' + id).style.display = 'none';
        document.getElementById('btn_save_' + id).style.display = 'inline';
    }
}

function saveAddress(id) {
    // 수정된 값을 가져옴
    var newAddressName = document.getElementById('address_name_input_' + id);
    var newAddress = document.getElementById('address_input_' + id);
    var address_nameDisplay = document.getElementById('address_name_display_' + id);
    var addressDisplay = document.getElementById('address_display_' + id);
    var default_editBtn = document.getElementById("default_btn_edit_" + id); // 기본 주소지 수정 버튼

    // AJAX 요청을 통해 서버에 업데이트 요청을 보냄
    fetch('/update-address', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            id: id,
            address_name: newAddressName.value,
            address: newAddress.value
        })
    })
    .then(response => {
        if (response.ok) {
            return response.text(); // 서버에서 반환하는 데이터 읽기
        } else {
            throw new Error("서버 오류");
        }
    })
    .then(data => {
        if (data === '수정이 완료되었습니다.') {
            // 서버에서 성공 응답을 받으면 필드 업데이트
            address_nameDisplay.textContent = newAddressName.value;
            addressDisplay.textContent = newAddress.value;

            // 입력 필드 숨기기 및 수정 버튼 표시
            newAddressName.style.display = 'none';
            newAddress.style.display = 'none';

            address_nameDisplay.style.display = 'inline';
            addressDisplay.style.display = 'inline';
            document.getElementById('default_address').style.display = 'inline';

            if(!default_editBtn){
                // 저장 버튼 숨기기 및 수정 버튼 표시
                document.getElementById('btn_save_' + id).style.display = 'none';
                document.getElementById('btn_edit_' + id).style.display = 'inline';
            }

        } else {
            alert('주소 수정에 실패했습니다.');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('주소 수정 중 오류가 발생했습니다.');
    });
}


function sample3_execDaumPostcode(id) {
    var newInputAddress = document.getElementById('address_input_' + id);
    var newAddressDisplay = document.getElementById('address_display_' + id);

    new daum.Postcode({
        oncomplete: function (data) {
            // 주소 필드에 값 넣기
            newInputAddress.value = data.address; // 선택한 주소
            newAddressDisplay.textContent = data.address;
            checkNewAddressInput(id);
        }
    }).open();
}

function checkNewAddressNameInput(id){
    var newAddressNameElement = document.getElementById('address_name_input_' + id);
    var new_address_nameMessage = document.getElementById('new_address_nameMessage_' + id);
    var default_saveBtn = document.getElementById("default_btn_save_" + id); // 기본 주소지 저장 버튼

    newAddressNameElement.addEventListener("input", function () {
        if (!newAddressNameElement.checkValidity()) {
            new_address_nameMessage.textContent = '적합하지 않은 유형의 별칭입니다.'
            new_address_nameMessage.style.color = 'red';
            if(!default_saveBtn){
                document.getElementById('btn_save_' + id).disabled = true;
            }else{
                default_saveBtn.disabled = true;
            }
        } else {
            new_address_nameMessage.textContent = ''
            if(!default_saveBtn){
                document.getElementById('btn_save_' + id).disabled = false;
            }else{
                default_saveBtn.disabled = false;
            }
        }
    });
}

function checkNewAddressInput(id){
    var newAddress = document.getElementById('address_input_' + id).value;
    var new_addressMessage = document.getElementById('new_addressMessage_' + id);
    var default_saveBtn = document.getElementById("default_btn_save_" + id); // 기본 주소지 저장 버튼



    if (!newAddress.startsWith('서울')) {
        new_addressMessage.textContent = '서울특별시의 주소만 가능합니다';
        new_addressMessage.style.color = 'red';
        if(!default_saveBtn){
            document.getElementById('btn_save_' + id).disabled = true;
        }else{
            default_saveBtn.disabled = true;
        }
    } else {
        new_addressMessage.textContent = '';
        if(!default_saveBtn){
            document.getElementById('btn_save_' + id).disabled = false;
        }else{
            default_saveBtn.disabled = false;
        }
    }
}

const MAX_ADDRESSES = 6;

function checkAddressLimit() {
    const addressRows = document.querySelectorAll('tr[id^="address-row-"]');
    console.log('주소개수 : ', addressRows);
    return addressRows.length >= MAX_ADDRESSES; // 주소 개수가 최대 개수를 초과하면 true 반환
}

document.getElementById('addressForm').addEventListener('submit', function(event) {
    if (checkAddressLimit()) {
        alert('주소는 최대 6개까지 저장 가능합니다.' + '\n' + '(기본 주소지 1개 + 추가 주소지 5개)');
        event.preventDefault(); // 폼 제출 방지
    }
});

function saveDefaultAddress(id){
    // 수정된 값을 가져옴
    var newAddressName = document.getElementById('address_name_input_' + id);
    var newAddress = document.getElementById('address_input_' + id);
    var address_nameDisplay = document.getElementById('address_name_display_' + id);
    var addressDisplay = document.getElementById('address_display_' + id);
    var default_editBtn = document.getElementById("default_btn_edit_" + id); // 기본 주소지 수정 버튼

    // AJAX 요청을 통해 서버에 업데이트 요청을 보냄
    fetch('/update-default-address', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            id: id,
            address_name: newAddressName.value,
            address: newAddress.value
        })
    })
    .then(response => {
        if (response.ok) {
            return response.text(); // 서버에서 반환하는 데이터 읽기
        } else {
            throw new Error("서버 오류");
        }
    })
    .then(data => {
        if (data === '수정이 완료되었습니다.') {
            // 서버에서 성공 응답을 받으면 필드 업데이트
            address_nameDisplay.textContent = newAddressName.value;
            addressDisplay.textContent = newAddress.value;

            // 입력 필드 숨기기 및 수정 버튼 표시
            newAddressName.style.display = 'none';
            newAddress.style.display = 'none';

            address_nameDisplay.style.display = 'inline';
            addressDisplay.style.display = 'inline';
            document.getElementById('default_address').style.display = 'inline';

            if(!default_editBtn){
                // 저장 버튼 숨기기 및 수정 버튼 표시
                document.getElementById('btn_save_' + id).style.display = 'none';
                document.getElementById('btn_edit_' + id).style.display = 'inline';
            }

        } else {
            alert('주소 수정에 실패했습니다.');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('주소 수정 중 오류가 발생했습니다.');
    });
}