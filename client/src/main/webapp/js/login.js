
$(document).ready( function () {

    $(document).on("click", "#Sign_in", function () {

        var login = $('#login').val();
        var pass = $('#pass').val();

        
        var loginObj = {

            login : login,
            pass : pass,
            name : null

        };

        var webSocketMessage = {
            
            message : loginObj,
            type : "LOGIN"
            
        };
        
        alert("message was sent : " + JSON.stringify(webSocketMessage));
        socket.send(JSON.stringify(webSocketMessage));

    });

});