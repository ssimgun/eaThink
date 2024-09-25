/*
	Intensify by TEMPLATED
	templated.co @templatedco
	Released for free under the Creative Commons Attribution 3.0 license (templated.co/license)
*/

(function($) {

	skel.breakpoints({
		xlarge:	'(max-width: 1680px)',
		large:	'(max-width: 1280px)',
		medium:	'(max-width: 980px)',
		small:	'(max-width: 736px)',
		xsmall:	'(max-width: 480px)'
	});

	$(function() {

		var	$window = $(window),
			$body = $('body'),
			$header = $('#header');

		// Disable animations/transitions until the page has loaded.
			$body.addClass('is-loading');

			$window.on('load', function() {
				window.setTimeout(function() {
					$body.removeClass('is-loading');
				}, 100);
			});

		// Fix: Placeholder polyfill.
			$('form').placeholder();

		// Prioritize "important" elements on medium.
			skel.on('+medium -medium', function() {
				$.prioritize(
					'.important\\28 medium\\29',
					skel.breakpoint('medium').active
				);
			});

		// Scrolly.
			$('.scrolly').scrolly({
				offset: function() {
					return $header.height();
				}
			});

		// Menu.
			$('#menu')
				.append('<a href="#menu" class="close"></a>')
				.appendTo($body)
				.panel({
					delay: 500,
					hideOnClick: true,
					hideOnSwipe: true,
					resetScroll: true,
					resetForms: true,
					side: 'right'
				});

	});

})(jQuery);


// 사이드 바 js
const dropbtn = document.querySelector('.dropbtn');
const dropdownContent = document.querySelector('.dropdown-content');
const dropbtnContent = document.querySelector('.dropbtn_content');

// 드롭다운 토글 함수
function dropdown() {
    dropdownContent.classList.toggle('show');
    if(dropbtn.style.background === ''){
        dropbtn.style.background = 'antiquewhite';
    } else{
        dropbtn.style.background = '';
    }
}

// 주소 선택 함수
function selectAddress(address_name,id,address) {
    dropbtnContent.style.color = '#252525';
    dropbtn.style.borderColor = '#fcfcfc';
    dropbtn.style.background = '';
    dropdownContent.classList.remove('show');

    if(confirm(address_name+'주소로 변경하시겠습니까?')){
        updateUserAddress(id);
        initGeocoder(address);
        dropbtn.innerHTML = address_name;
    }


}

// 드롭다운 버튼 클릭 이벤트 핸들러 설정
if (dropbtn) {
    dropbtn.onclick = dropdown;
}

// 드롭다운 외부 클릭 시 닫기
window.onclick = (e) => {
    if (!e.target.matches('.dropbtn')) {
        const dropdowns = document.getElementsByClassName('dropdown-content');
        Array.from(dropdowns).forEach(dropdown => {
            if (dropdown.classList.contains('show')) {
                dropdown.classList.remove('show');
            }
        });
    }
};

function updateUserAddress(id){
    fetch('/selectAddress',{
        method:'POST',
        headers:{
            'Content-Type':'application/json'
        },
        body:JSON.stringify({
            id : id
        })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('서버 응답 없음');
        }
        return response.text();  // 응답 본문을 텍스트로 읽기
    })
    .then(message => {
        console.log(message);  // 서버에서 반환한 텍스트 메시지를 콘솔에 출력
        if (message.includes("선택 완료")) {
            window.location.href='/home';
            console.log('주소 선택 성공');
        } else {
            console.log('선택 오류:', message);  // 오류 메시지를 출력
        }
    })
    .catch(error => {
        console.error('오류:', error);
    });
}

//    날씨 이미지에 마우스를 가져다 대면 상세정보 띄우기
document.addEventListener('DOMContentLoaded', function() {
    const weatherInfo = document.getElementById('weatherInfo');
    const detailsInfo = document.getElementById('weatherDetails');

    // 마우스를 weather-info 요소에 올리면 details 요소를 보이게 함
    weatherInfo.addEventListener('mouseenter', function() {
        detailsInfo.style.display = 'block';
    });

    // 마우스를 weather-info 요소에서 벗어나면 details 요소를 숨김
    weatherInfo.addEventListener('mouseleave', function() {
        detailsInfo.style.display = 'none';
    });
});

// 주소 목록 변경시 날씨 정보 업데이트
// id를 통해 요소를 선택
const weatherInfoImage = document.getElementById('weather-info-image');
const weatherDetailsImage = document.getElementById('weather-details-image');
const weatherDetailsDiv = document.getElementById('weather-details-div');

function updateWeatherInfo(){
    weatherInfoImage.src = "/images/weather_{{weatherImageName}}.png"
    weatherInfoImage.alt = "{{weatherImageName}}"
    weatherDetailsImage.src = "/images/weather_{{weatherImageName}}.png"
    weatherDetailsImage.alt = "{{weatherImageName}}"

    weatherDetailsDiv.innerHTML = `
       <p> 현재 {{secondName}}의 날씨</p>
       <p> 기온 : {{t1h}}도</p>
       <p> 습도 : {{reh}}%</p>
       <p> 강수량 : {{rn1}}</p>
    `;
}


