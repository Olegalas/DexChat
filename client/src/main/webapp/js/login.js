
socket.onmessage = function(event) {
    console.log("LoginMessage " + event.data);
    var webMessage = JSON.parse(event.data);
    
    if("Incorrect pass or Login" == webMessage.message){
        $('#mainAlert').show();
    } else if("Welcome to DexChat" == webMessage.message){
        alert(webMessage.message + " " + login);

        $('#form').attr('action', '/home').submit();
        
    } else if("Password on your email was sent" == webMessage.message || "Incorrect Email or Login" == webMessage.message){
        alert(webMessage.message);
        $('forgotPass').click();
    }

};

$(document).ready( function () {

    var ip;

    $.getJSON('//api.ipify.org?format=jsonp&callback=?', function(data) {
        console.log("home ip address : " + data.ip);
        ip = data.ip;
    });

    $(document).on("click", "#Sign_in", function () {

        $('#mainAlert').hide();

        login = $('#login').val();
        pass = $('#pass').val();

        
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

    });

    $(document).on("click", "#Send_email", function () {

        $('#mainAlert').hide();


        var email = $('#email').val();
        var loginEmail = $('#loginEmail').val();

        var loginObj = {

            login : loginEmail,
            ip : ip,
            email : email

        };
        
        
        var webSocketMessage = {

            message : loginObj,
            type : "EMAIL"

        };

        console.log("was sent - " + JSON.stringify(webSocketMessage));
        socket.send(JSON.stringify(webSocketMessage));
        
    });

});

var redirect = function(url, method) {

    $('<form>', {
        method: method,
        action: url


    }).submit();
};