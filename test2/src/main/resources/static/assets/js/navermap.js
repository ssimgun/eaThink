//
////네이버 지도 스크립트
//
//var mapOptions = {
//    center: new naver.maps.LatLng(37.3595704, 127.105399),
//    zoom: 10,
//    mapTypeId: naver.maps.MapTypeId.NORMAL
//};
//
//var map = new naver.maps.Map('map', mapOptions);
//
//var infowindow = new naver.maps.InfoWindow();
//
//function onSuccessGeolocation(position) {
//    var location = new naver.maps.LatLng(position.coords.latitude,
//                                         position.coords.longitude);
//
//    map.setCenter(location); // 얻은 좌표를 지도의 중심으로 설정합니다.
//    map.setZoom(10); // 지도의 줌 레벨을 변경합니다.
//
//    infowindow.setContent('<div style="padding:20px;">' + '내 위치' + '</div>');
//
//    infowindow.open(map, location);
//    console.log('Coordinates: ' + location.toString());
//}
//
//function onErrorGeolocation() {
//    var center = map.getCenter();
//
//    infowindow.setContent('<div style="padding:20px;">' +
//        '<h5 style="margin-bottom:5px;color:#f00;">Geolocation failed!</h5>'+ "latitude: "+ center.lat() +"<br />longitude: "+ center.lng() +'</div>');
//
//    infowindow.open(map, center);
//}
//
//$(window).on("load", function() {
//    if (navigator.geolocation) {
//        /**
//         * navigator.geolocation 은 Chrome 50 버젼 이후로 HTTP 환경에서 사용이 Deprecate 되어 HTTPS 환경에서만 사용 가능 합니다.
//         * http://localhost 에서는 사용이 가능하며, 테스트 목적으로, Chrome 의 바로가기를 만들어서 아래와 같이 설정하면 접속은 가능합니다.
//         * chrome.exe --unsafely-treat-insecure-origin-as-secure="http://example.com"
//         */
//        navigator.geolocation.getCurrentPosition(onSuccessGeolocation, onErrorGeolocation);
//    } else {
//        var center = map.getCenter();
//        infowindow.setContent('<div style="padding:20px;"><h5 style="margin-bottom:5px;color:#f00;">Geolocation not supported</h5></div>');
//        infowindow.open(map, center);
//    }
//});

// 건태 테스트 코드

var map = new naver.maps.Map("map", {
        center: new naver.maps.LatLng(37.5666103, 126.9783882),
        zoom: 16
    }),
    infoWindow = null;

function initGeocoder() {
    var latlng = map.getCenter();
    var utmk = naver.maps.TransCoord.fromLatLngToUTMK(latlng); // 위/경도 -> UTMK
    var tm128 = naver.maps.TransCoord.fromUTMKToTM128(utmk);   // UTMK -> TM128
    var naverCoord = naver.maps.TransCoord.fromTM128ToNaver(tm128); // TM128 -> NAVER

    infoWindow = new naver.maps.InfoWindow({
        content: ''
    });

    map.addListener('click', function(e) {
        var latlng = e.coord,
            utmk = naver.maps.TransCoord.fromLatLngToUTMK(latlng),
            tm128 = naver.maps.TransCoord.fromUTMKToTM128(utmk),
            naverCoord = naver.maps.TransCoord.fromTM128ToNaver(tm128);

        utmk.x = parseFloat(utmk.x.toFixed(1));
        utmk.y = parseFloat(utmk.y.toFixed(1));

        infoWindow.setContent([
            '<div style="padding:10px;width:380px;font-size:14px;line-height:20px;">',
            '<strong>LatLng</strong> : '+ '좌 클릭 지점 위/경도 좌표' +'<br />',
            '<strong>UTMK</strong> : '+ '위/경도 좌표를 UTMK 좌표로 변환한 값' +'<br />',
            '<strong>TM128</strong> : '+ '변환된 UTMK 좌표를 TM128 좌표로 변환한 값' +'<br />',
            '<strong>NAVER</strong> : '+ '변환된 TM128 좌표를 NAVER 좌표로 변환한 값' +'<br />',
            '</div>'
        ].join(''));

        infoWindow.open(map, latlng);
        console.log('LatLng: ' + latlng.toString());
        console.log('UTMK: ' + utmk.toString());
        console.log('TM128: ' + tm128.toString());
        console.log('NAVER: ' + naverCoord.toString());
    });
}

naver.maps.onJSContentLoaded = initGeocoder;