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

                    x = item.x;
                    y = item.y;
                    var centerPosition = new naver.maps.LatLng(y, x);

                    map = new naver.maps.Map('map', {
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

                    // 지도에 마커 생성
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

                } else {
                    alert('응답에서 유효한 장소를 찾을 수 없습니다.');
                }
            } else {
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

    var addressEle = document.getElementById('addressHidden');
    var address = addressEle ? addressEle.value : null; // addressEle가 존재하는지 체크

    if (!address) {
        address = '서울 중구 세종대로 110 서울특별시청'; // 기본 주소 설정
    }

    getCoordinates(address); // 처음 주소로 좌표 요청
});

