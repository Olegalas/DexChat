
socket.onmessage = function(event) {
    alert("RegistrationMessage " + event.data);
    // if message will "Registration complete" redirect to login page
    
    // alert("Thank you for your registration");
    //
    // //return tu home page
    // window.location = '/';
    //
};

$(document).ready( function () {

    $(document).on("click", "#Ready", function () {

        var fail = false;

        var login = $('#login').val();
        var name = $('#name').val();
        var email = $('#email').val();
        var pass = $('#pass').val();
        var rePass = $('#rePass').val();

        var stringPattern = /^[a-zA-Z0-9]{4,20}$/i;
        var mailPattern = /[0-9a-z_-]+@[0-9a-z_-]+\.[a-z]{2,5}/i;

        if (!stringPattern.test(login)) {
            fail = "Incorrect login";
        } else if (!stringPattern.test(name)) {
            fail = "Incorrect name";
        } else if (!mailPattern.test(email)) {
            fail = "Enter your email";
        } else if (!stringPattern.test(pass)) {
            fail = "Enter your pass";
        } else if (pass != rePass) {
            fail = "Your passwords are different";
        }

        if (fail) {
            alert(fail);
        } else {
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
        name : name

    };

    var webSocketMessage = {

        message : loginObj,
        type : "REGISTRATION"

    };

    alert("message was sent : " + JSON.stringify(webSocketMessage));
    socket.send(JSON.stringify(webSocketMessage));
    
}