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
                            anchor : new naver.maps.Point(17,0)
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