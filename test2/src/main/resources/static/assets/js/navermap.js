// 네이버 지도 스크립트
document.addEventListener('DOMContentLoaded', function() {
    function getCoordinates(address) {
        naver.maps.Service.geocode({
            query: address
        }, function(status, response) {
            if (status === naver.maps.Service.Status.OK) {
                var items = response.v2.addresses;

                if (Array.isArray(items) && items.length > 0) {
                    // 첫 번째 결과만 사용
                    var item = items[0];
                    console.log(item);

                    var x = item.x;
                    var y = item.y;
                    var centerPosition = new naver.maps.LatLng(y, x);

                    var map = new naver.maps.Map('map', {
                        center: centerPosition,
                        zoom: 16
                    });

                    var markerOptions = {
                        position: centerPosition,
                        map: map,
                        icon: {
                            url: './images/marker/chick.png',
                            origin: new naver.maps.Point(0, 0),
                            anchor: new naver.maps.Point(17, 0)
                        }
                    };

                    var marker = new naver.maps.Marker(markerOptions);

                    var infowindow = new naver.maps.InfoWindow({
                        content: '<div style="padding:10px; background-color: white; color: black;">나의 위치</div>',
                    });

                    var radius = 500;
                    var circle = new naver.maps.Circle({
                        map: map,
                        center: centerPosition,
                        radius: radius,
                        fillColor: '#a3a7ff4a',
                        fillOpacity: 0.9,
                        strokeWeight: 0
                    });

                    // 마커 hover 이벤트 추가
                    naver.maps.Event.addListener(marker, 'mouseover', function() {
                        infowindow.open(map, marker.getPosition());
                    });

                    naver.maps.Event.addListener(marker, 'mouseout', function() {
                        infowindow.close();
                    });

                    // 추가적인 위치 처리 (원하는 경우 사용할 수 있음)
                    const locations = [
                        { x: 126.977318341, y: 37.566370748 },
                        { x: 126.977718351, y: 37.526370758 },
                        { x: 126.977118361, y: 37.576370768 },
                        { x: 126.937918371, y: 37.596370778 },
                        { x: 126.973918381, y: 37.526370788 },
                        { x: 126.976918391, y: 37.536370798 },
                        { x: 126.957918491, y: 37.566370898 },
                        { x: 126.973818591, y: 37.589370098 },
                        { x: 126.977918691, y: 37.545370498 },
                        { x: 126.971918891, y: 37.588370298 },
                    ];

                    locations.forEach(function(location){
                        var food_x = location.x;
                        var food_y = location.y;

                        if(calculateDistance(x,y, food_x, food_y)){
                            var position = new naver.maps.LatLng(food_y,food_x);

                            var foodMarkerOptions = {
                                position:position,
                                map: map,
                                icon:{
                                    url: './images/marker/restaurant_marker.png',
                                    origin: new naver.maps.Point(0, 0),
                                    anchor: new naver.maps.Point(17, 0)
                                }
                            };

                        }else{
                            console.log(location);
                        }
                        // 음식점 마커 생성
                        new naver.maps.Marker(foodMarkerOptions);

                    });

                } else {
                    console.error('응답에서 유효한 장소를 찾을 수 없습니다.');
                }
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
        });
    }

     // 거리 계산 함수
     function calculateDistance(lat1, lng1, lat2, lng2) {
         const toRadians = (degrees) => degrees * (Math.PI / 180);
         const earthRadius = 6371e3; // 지구의 반지름 (미터)

         const lat1Rad = toRadians(lat1);
         const lat2Rad = toRadians(lat2);
         const deltaLatRad = toRadians(lat2 - lat1);
         const deltaLngRad = toRadians(lng2 - lng1);

         const a = Math.sin(deltaLatRad / 2) * Math.sin(deltaLatRad / 2) +
                   Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                   Math.sin(deltaLngRad / 2) * Math.sin(deltaLngRad / 2);
         const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

         const distance = earthRadius * c; // 결과를 미터로 변환
         return 500 >= distance;
     }

    // 챗봇에서 카테고리 추출
    function getCategoryFromChatbot(){
        const chatbotOutput = document.querySelector("#chatbot-container > div > ul > li:last-child > p");
        if(chatbotOutput){
            console.log(chatbotOutput); // 챗봇 output 추출 확인
            return chatbotOutput.textContent;
        }
        return null;
    }

    // 추출한 카테고리로 데이터베이스에 조회 => 해당 카테고리에 해당하는 음식점 List 반환
    function fetchRestaurantByCategory(category){
        if(!category) return;

        $.ajax({
            url:'/category',
            method: 'GET',
            data:{category:category},
            success: function(restaurants){
                restaurants.forEach(function(restaurant){
                    console.log(restaurant);
                })
            },
            error:function(error){
                console.error('데이터 조회 실패',error);
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

