function api(){
    return data1= fetch('http://localhost:8988/restApi/hostList')
        .then(res => res.json())
        .then(json => json.maps);
}

const promise = api();
const getData=()=>{
    promise.then((data)=> {
        console.log(data);
        console.log(data.length);

        var markers = [];

        for (var i = 0, ii = data.length; i < ii; i++) {
            var spot = data[i],
                latlng = new naver.maps.LatLng(spot.lat, spot.lng),
                marker = new naver.maps.Marker({
                    position: latlng,
                    draggable: true
                });

            markers.push(marker);
        }
    })
};

getData();