var hostsave ={
    init : function (){
        var _this = this;
        $('#hosts-save').on('click', function () {
            _this.save();
        });
    },
    save : function (){
        var data = {
            id : $('#userid').val(),
            region: $('#region').val(),
            gender: $('#gender').val(),
            age: $('#age').val(),
            farmsts: $('#farmsts').val(),
            shortintro: $('#shortintro').val(),
            intro: $('#intro').val(),
            lat: $('#lat').val(),
            lng: $('#lng').val()
        };

        $.ajax({
            type: 'POST',
            url: '/api/v1/hosts',
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function() {
            alert('호스트 신청이 완료되었습니다.');
            window.location.href = '/hosts/save';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }


};
hostsave.init();