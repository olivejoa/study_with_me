var read_main = {
    init           : function () {
        var _this = this;

        _this.loadMapApi();
        _this.loadLikeStatus();

        $('.btn-like').on('click', function (e) {
            _this.clickLike(e.target);
        })
    },
    loadMapApi : function () {
        var mapContainer = document.getElementById('map'), // 지도를 표시할 div
            mapOption = {
                center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
                level: 3 // 지도의 확대 레벨
            };

        // 지도를 생성합니다
        var map = new kakao.maps.Map(mapContainer, mapOption);

        // 주소-좌표 변환 객체를 생성합니다
        var geocoder = new kakao.maps.services.Geocoder();

        // 주소로 좌표를 검색합니다
        geocoder.addressSearch($('#address').text(), function(result, status) {

            // 정상적으로 검색이 완료됐으면
            if (status === kakao.maps.services.Status.OK) {

                var coords = new kakao.maps.LatLng(result[0].y, result[0].x);

                // 결과값으로 받은 위치를 마커로 표시합니다
                var marker = new kakao.maps.Marker({
                    map: map,
                    position: coords
                });

                // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
                map.setCenter(coords);
            }
        });
    },
    loadLikeStatus : function () {
        var likeStatus = $('#like-status').val();
        console.log(likeStatus);

        if(likeStatus !== "") {
            if(likeStatus) {
                $('button[name=btn-like]').css('display', 'none');
                $('button[name=btn-like-chosen]').css('display', 'inline-block');
            } else {
                $('button[name=btn-dislike]').css('display', 'none');
                $('button[name=btn-dislike-chosen]').css('display', 'inline-block');
            }
        }
    },
    clickLike : function (target) {
        var name = $(target).attr('name');
        console.log(target);
        switch (name) {
            case 'btn-like':
                console.log('btn-like');
                break;
            case 'btn-like-chosen':
                console.log('btn-like-chosen');
                break;
            case 'btn-dislike':
                console.log('btn-dislike');
                break;
            case 'btn-dislike-chosen':
                console.log('btn-dislike-chosen');
                break;
        }
    }
};

read_main.init();