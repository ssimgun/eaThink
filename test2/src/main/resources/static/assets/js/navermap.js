//var map = new naver.maps.Map("map", {
//        center: new naver.maps.LatLng(37.5666103, 126.9783882),
//        zoom: 15
//    }),
//    infoWindow = null;
//
//function initGeocoder() {
//    var latlng = map.getCenter();
//
//    infoWindow = new naver.maps.InfoWindow({
//        content: ''
//    });
//
//    function() {
//        var address = document.getElementById('addressHidden');
//        var latlng = address.coord;
//
//        infoWindow.setContent([
//            '<div style="padding:10px;width:380px;font-size:14px;line-height:20px;">',
//            '<strong>LatLng</strong> : '+ latlng.toString() +'<br />',
//            '</div>'
//        ].join(''));
//
//        infoWindow.open(map, latlng);
//        console.log('LatLng: ' + latlng.toString());
//    };
//}
//
//naver.maps.onJSContentLoaded = initGeocoder;