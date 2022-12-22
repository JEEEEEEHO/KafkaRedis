var map = new naver.maps.Map("map", {
                                        zoom: 6,
                                        maxZoom: 13,
                                        center: new naver.maps.LatLng(36.2253017, 127.6460516),
                                        zoomControl: true,
                                        zoomControlOptions: {
                                            position: naver.maps.Position.TOP_LEFT,
                                            style: naver.maps.ZoomControlStyle.SMALL
                                        }
                                    }
                                );
/*function api(){
                return data1= fetch('http://localhost:8988/restApi/hostList')
                                .then(res => res.json())
                                .then(json => json.maps);
                }

const promise = api();
const getData=()=>{
                    promise.then((data)=>{
                                            console.log(data);
                                            console.log(data.length);

                                            var markers = []; // 마커 정보
                                            let infoWindows= []; // 정보창 배열

                                            for (var i = 0, ii = data.length; i < ii; i++) {
                                                var spot = data[i],
                                                    infoWindow = new naver.maps.InfoWindow({
                                                        content: '<div style="width:200px;text-align:center;padding:10px;"><b><a href="HostDetail?farmno='+spot.farmno+'">'+spot.shortintro+'</a></b></div>'
                                                    }); // 클릭했을 때 띄워줄 정보 입력


                                                latlng = new naver.maps.LatLng(spot.lat, spot.lng),
                                                marker = new naver.maps.Marker({
                                                    position: latlng,
                                                    draggable: true
                                                });

                                                markers.push(marker);
                                                infoWindows.push(infoWindow);
                                            }

                                            function getClickHandler(seq) {

                                                return function(e) {  // 마커를 클릭하는 부분
                                                    var marker = markers[seq], // 클릭한 마커의 시퀀스로 찾는다.
                                                        infoWindow = infoWindows[seq]; // 클릭한 마커의 시퀀스로 찾는다
                                                    if (infoWindow.getMap()) {
                                                        infoWindow.close();
                                                    } else {
                                                        infoWindow.open(map, marker); // 표출
                                                    }
                                                }
                                            }

                                            for (var i=0, ii=markers.length; i<ii; i++) {
                                                console.log(markers[i] , getClickHandler(i));
                                                naver.maps.Event.addListener(markers[i], 'click', getClickHandler(i)); // 클릭한 마커 핸들러
                                            }

                                            var htmlMarker1 = {
                                                    content: '<div style="cursor:pointer;width:40px;height:40px;line-height:42px;font-size:10px;color:white;text-align:center;font-weight:bold;background:url(/img/cluster-marker-1.png);background-size:contain;"></div>',
                                                    size: N.Size(40, 40),
                                                    anchor: N.Point(20, 20)
                                                },
                                                htmlMarker2 = {
                                                    content: '<div style="cursor:pointer;width:40px;height:40px;line-height:42px;font-size:10px;color:white;text-align:center;font-weight:bold;background:url(/img/cluster-marker-2.png);background-size:contain;"></div>',
                                                    size: N.Size(40, 40),
                                                    anchor: N.Point(20, 20)
                                                },
                                                htmlMarker3 = {
                                                    content: '<div style="cursor:pointer;width:40px;height:40px;line-height:42px;font-size:10px;color:white;text-align:center;font-weight:bold;background:url(/img/cluster-marker-3.png);background-size:contain;"></div>',
                                                    size: N.Size(40, 40),
                                                    anchor: N.Point(20, 20)
                                                },
                                                htmlMarker4 = {
                                                    content: '<div style="cursor:pointer;width:40px;height:40px;line-height:42px;font-size:10px;color:white;text-align:center;font-weight:bold;background:url(/img/cluster-marker-4.png);background-size:contain;"></div>',
                                                    size: N.Size(40, 40),
                                                    anchor: N.Point(20, 20)
                                                },
                                                htmlMarker5 = {
                                                    content: '<div style="cursor:pointer;width:40px;height:40px;line-height:42px;font-size:10px;color:white;text-align:center;font-weight:bold;background:url(/img/cluster-marker-5.png);background-size:contain;"></div>',
                                                    size: N.Size(40, 40),
                                                    anchor: N.Point(20, 20)
                                                };

                                                var markerClustering = new MarkerClustering({
                                                    minClusterSize: 2,
                                                    maxZoom: 13,
                                                    map: map,
                                                    markers: markers,
                                                    disableClickZoom: false,
                                                    gridSize: 120,
                                                    icons: [htmlMarker1, htmlMarker2, htmlMarker3, htmlMarker4, htmlMarker5],
                                                    indexGenerator: [10, 100, 200, 500, 1000],
                                                    stylingFunction: function(clusterMarker, count) {
                                                        $(clusterMarker.getElement()).find('div:first-child').text(count);
                                                    }
                                                });

                                                console.log(markers);
                                            });
                    };
getData();*/