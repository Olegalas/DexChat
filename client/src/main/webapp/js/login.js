
socket.onmessage = function(event) {
    console.log("LoginMessage " + event.data);
    var webMessage = JSON.parse(event.data);
    
    if("Incorrect pass or Login" == webMessage.message){
        alert(webMessage.message);
    } else if("Welcome to DexChat" == webMessage.message){
        alert(webMessage.message + " " + login);

        $('#form').attr('action', '/home').submit();
        
    }

};

$(document).ready( function () {

    $(document).on("click", "#Sign_in", function () {

        login = $('#login').val();
        pass = $('#pass').val();

        
        var loginObj = {

            login : login,
            pass : pass,
            name : null

        };

        var webSocketMessage = {
            
            message : loginObj,
            type : "LOGIN"
            
        };
        
        socket.send(JSON.stringify(webSocketMessage));

    });

});

var redirect = function(url, method) {

    $('<form>', {
        method: method,
        action: url


    }).submit();
};