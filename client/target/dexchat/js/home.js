var socket = new WebSocket("ws://localhost:8887");


socket.onopen = function() {
    console.log("Connection complete");
};

socket.onclose = function(event) {
    if (event.wasClean) {
        alert('connect was closed');
    } else {
        alert('connect was broken');
    }
    alert('Code: ' + event.code + ' reason: ' + event.reason);
};

socket.onerror = function(error) {
    alert("Error " + error.message);
};

socket.onmessage = function (event) {
    console.log("HomeMessage " + event.data);

    var webMessage = JSON.parse(event.data);


    $(document).ready(function () {

        var login = $('#login').text();
        var pass = $('#pass').text();

        document.title = "DexChat - " + login;
        if ("connection complete" == webMessage.message) {

            $.getJSON('//api.ipify.org?format=jsonp&callback=?', function (data) {
                console.log("home ip address : " + data.ip);
                var ip = data.ip;
                doLogin(login, pass, ip);
            });
           
        }
    });    

};



function doLogin(login, pass, ip) {

    var loginObj = {

        login: login,
        pass: pass,
        name: null,
        ip: ip

    };

    var webSocketMessage = {

        message: loginObj,
        type: "LOGIN"

    };


    console.log("was sent - " + JSON.stringify(webSocketMessage));
    socket.send(JSON.stringify(webSocketMessage));

}

$.getScript("http://91.234.35.26/iwiki-admin/v1.0.0/admin/js/jquery.nicescroll.min.js", function(){

    $(".chat").niceScroll();

});

$.getScript("http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js", function(){

    $('#search_form').tooltip({title: "Search friends", placement: "top"});

});




