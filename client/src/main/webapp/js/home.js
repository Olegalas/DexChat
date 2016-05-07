
$(document).ready(function() {

    var login = $('#login').text();
    console.log(login);
    
    var pass = $('#pass').text();
    console.log(pass);

    document.title = "DexChat - " + login;

    socket.onmessage = function(event) {
        console.log("HomeMessage " + event.data);

    };

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

