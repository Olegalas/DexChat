
socket.onmessage = function(event) {
    console.log("LoginMessage " + event.data);
    var webMessage = JSON.parse(event.data);
    
    if("Incorrect pass or Login" == webMessage.message){
        alert(webMessage.message);
    } else if("Welcome to DexChat" == webMessage.message){
        alert(webMessage.message);

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
    //
    // var loginField = $('<input>');
    //
    // loginField.attr("type", "hidden");
    // loginField.attr("name", "login");
    // loginField.attr("value", login);
    //
    //
    // var passField = $('<input>');
    //
    // passField.attr("type", "hidden");
    // passField.attr("name", "pass");
    // passField.attr("value", pass);


    $('<form>', {
        method: method,
        action: url


    }).submit();
};