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

// 네이버 지도 스크립트
document.addEventListener('DOMContentLoaded', function() {
    function getCoordinates(address) {
        $.ajax({
            url: "https://api.vworld.kr/req/address?",
            type: "GET",
            dataType: "jsonp",
            data: {
                service: "address",
                request: "GetCoord",
                version: "2.0",
                crs: "EPSG:4326",
                type: "ROAD",
                address: address,
                format: "json",
                errorformat: "json",
                key: "0301B7A8-5706-38A7-859E-0434C70ACD7C"
            },
            success: function(result) {
                console.log(result);

                if (result.response.status === 'OK') {
                    var data = result.response.result;

                    var x = data.point.x;
                    var y = data.point.y;

                    console.log(x, y);

                    var position = new naver.maps.LatLng(y, x);
                    var map = new naver.maps.Map('map', {
                        center: position,
                        zoom: 14
                    });

                    var markerOptions = {
                        position: position,
                        map: map,
                        icon:{
                            url:'./images/chick.png',
                            origin : new naver.maps.Point(0,0),
                            anchor : new naver.maps.Point(29,0)
                        }
                    };

                    var marker = new naver.maps.Marker(markerOptions);

                    var infowindow = new naver.maps.InfoWindow({
                        content: '<div style="padding:10px; background-color: white; color: black;">나의 위치</div>',
                    });

                    // 마커 hover 이벤트 추가
                    naver.maps.Event.addListener(marker, 'mouseover', function() {
                        infowindow.open(map, marker.getPosition());
                    });

                    naver.maps.Event.addListener(marker, 'mouseout', function() {
                        infowindow.close();
                    });
                } else {
                    console.error('좌표를 찾을 수 없습니다.');
                    alert('요청하신 주소를 지도에서 찾을 수 없습니다. 기본 주소로 재요청합니다.');

                    var firstAddressElement = document.querySelector('.myAddress');
                    if (firstAddressElement) {
                           address = firstAddressElement.textContent.split('(')[1].replace(')', '').trim();
                    } else {
                        address = '서울 중구 세종대로 110 서울특별시청'; // 기본 주소 설정
                    }

                    getCoordinates(address);
                }
            },
            error: function(error) {
                console.error('AJAX 요청 실패:', error);
            }
        });
    }

    var addressEle = document.getElementById('addressHidden');
    var address = addressEle ? addressEle.value : null; // addressEle가 존재하는지 체크

    if (!address) {
        address = '서울 중구 세종대로 110 서울특별시청'; // 기본 주소 설정
    }

    getCoordinates(address); // 처음 주소로 좌표 요청
});
