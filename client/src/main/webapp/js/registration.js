
socket.onmessage = function(event) {
    console.log("RegistrationMessage " + event.data);
    var webMessage = JSON.parse(event.data);


    if("Login or email has already used" == webMessage.message){
        $('#mainAlert').show();
    } else if ("Registration complete" == webMessage.message) {
        alert(webMessage.message);
        redirect("/", "GET");
    }

};

var ip;

$(document).ready( function () {

    $.getJSON('//api.ipify.org?format=jsonp&callback=?', function(data) {
        console.log("home ip address : " + data.ip);
        ip = data.ip;
    });

    $(document).on("click", "#Ready", function () {

        $('#mainAlert').hide();
        $('#loginAlert').hide();
        $('#nameAlert').hide();
        $('#emailAlert').hide();
        $('#passAlert').hide();
        $('#repassAlert').hide();

        var fail = false;

        var login = $('#login').val();
        var name = $('#name').val();
        var email = $('#email').val();
        var pass = $('#pass').val();
        var rePass = $('#rePass').val();

        var stringPattern = /^[a-zA-Z0-9]{4,20}$/i;
        var mailPattern = /[0-9a-z_-]+@[0-9a-z_-]+\.[a-z]{2,5}/i;

        if (!stringPattern.test(login)) {
            $('#loginAlert').show();
            fail = "Incorrect login";
        } else if (!stringPattern.test(name)) {
            $('#nameAlert').show();
            fail = "Incorrect name";
        } else if (!mailPattern.test(email)) {
            $('#emailAlert').show();
            fail = "Enter your email";
        } else if (!stringPattern.test(pass)) {
            $('#passAlert').show();
            fail = "Enter your pass";
        } else if (pass != rePass) {
            $('#repassAlert').show();
            fail = "Your passwords are different";
        }

        if (!fail) {

            $.ajax({

                type: 'POST',
                data: {login : login, name : name, email : email, pass : pass},
                url: '/do_registration',
                success: function (response) {

                    sendRegistrationForm(login, name, email, pass);

                }

            });

        }

    });

});

function sendRegistrationForm(login, name, email, pass){

    
    var loginObj = {

        login : login,
        pass : pass,
        name : name,
        ip : ip,
        email : email

    };

    var webSocketMessage = {

        message : loginObj,
        type : "REGISTRATION"

    };

    console.log("was sent - " + JSON.stringify(webSocketMessage));
    socket.send(JSON.stringify(webSocketMessage));
    
}

var redirect = function(url, method) {
    $('<form>', {
        method: method,
        action: url
    }).submit();
};