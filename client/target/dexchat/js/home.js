
$(document).ready(function() {

    $.getJSON('//api.ipify.org?format=jsonp&callback=?', function(data) {
        console.log("home ip address : " + data.ip);
        ip = data.ip;
    });
    
    var login = $('#login').text();
    var pass = $('#pass').text();

    document.title = "DexChat - " + login;

    socket.onmessage = function(event) {
        console.log("HomeMessage " + event.data);

        var webMessage = JSON.parse(event.data);

        if("connection complete" == webMessage.message){

            $.getJSON('//api.ipify.org?format=jsonp&callback=?', function(data) {
                console.log("home ip address : " + data.ip);
                var ip = data.ip;
                doLogin(login, pass, ip);
            });
            
        }
        
    };
    
});

function doLogin(login, pass, ip) {

    var loginObj = {

        login : login,
        pass : pass,
        name : null,
        ip : ip

    };

    var webSocketMessage = {

        message : loginObj,
        type : "LOGIN"

    };


    console.log("was sent - " + JSON.stringify(webSocketMessage));
    socket.send(JSON.stringify(webSocketMessage));

}

