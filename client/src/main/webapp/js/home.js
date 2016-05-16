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

    var mainLogin;

    $(document).ready(function () {

        mainLogin = $('#login').text();
        var pass = $('#pass').text();

        document.title = "DexChat - " + mainLogin;
        if ("connection complete" == webMessage.message) {

            $.getJSON('//api.ipify.org?format=jsonp&callback=?', function (data) {
                console.log("home ip address : " + data.ip);
                var ip = data.ip;
                doLogin(mainLogin, pass, ip);
            });
           
        }
    });

    if ("incorrect friend login" == webMessage.message){
        $("#model_message").show();
        $("#myModal").modal();
    }

    if("FRIEND" == webMessage.type){
        
        console.log(webMessage.message.friends);
 
        if(webMessage.message.friends.length == 0 && webMessage.message.login != mainLogin){

            $('<div class="user_model"/>').attr({

                id: webMessage.message.login

            }).appendTo("#chat-users_model");

            console.log("created user_model div");
            createFriend(webMessage.message.login);

            $("<button type='button' class='btn btn-default' />").text("add").click(function(){


                $( ".user_model" ).remove();
                $("#myModal").modal();
                
                createUserDiv(webMessage.message.login);

                var friend = {

                    login: mainLogin,
                    name: "unknown",
                    email: "unknown",
                    friends: [{login: webMessage.message.login, name: "unknown", email: "unknown", friends:[]}]

                };

                sendMessage("FRIEND", friend);
                
            }).appendTo('#' + webMessage.message.login + "_avatar");

            $("#model_message").hide();
            $("#myModal").modal();


        } else{

            for(i = 0; i != webMessage.message.friends.length; ++i){
                console.log(webMessage.message.friends[i].login);


                

                // EXAMPLE

                // <div class="user">
                //     <div class="avatar">
                //         <img src="http://bootdey.com/img/Content/avatar/avatar2.png" alt="User name">
                //             <!-- status online; off; busy; offline-->
                //             <div class="status online"></div>
                //     </div>
                //     <div class="name">Friend</div>
                //     <div class="mood">mood</div>
                // </div>

                createUserDiv(webMessage.message.friends[i].login);

            }
        }
    } else  if("HISTORY" == webMessage.type){

        console.log(webMessage.message);
        // init dialog frame

    }
};

$(document).on("click", "#search_button", function () {

    // 1) delete previous
    $( ".user_model" ).remove();
    $("#model_message").show();

    // 2) read input
    var input = $("#search_input").val();
    console.log("search - " + input);
    // 3) send input
    
    var friend = {
        
        login: input,
        name: "unknown",
        email: "unknown",
        friends: []
        
    };
    
    sendMessage("FRIEND", friend);

});

function doLogin(login, pass, ip) {

    var loginObj = {

        login: login,
        pass: pass,
        name: null,
        ip: ip

    };

    sendMessage("LOGIN", loginObj);
}

function sendMessage(type, message) {
    var webSocketMessage = {

        message: message,
        type: type

    };


    console.log("was sent - " + JSON.stringify(webSocketMessage));
    socket.send(JSON.stringify(webSocketMessage));
}

function createUserDiv(login) {

    $('<div/>', {

        class: "user",
        id: login

    }).appendTo('#chat-users_frame');
    console.log("created user div");
    createFriend(login);

}

function createFriend(login) {

    $('<div calss="avatar"/>').attr({

        id: login + "_avatar"

    }).appendTo('#' + login);
    console.log("created avatar div");

    $('<img src="http://bootdey.com/img/Content/avatar/avatar2.png"/>').attr({

        alt: login

    }).appendTo('#' + login + "_avatar");
    console.log("created img div");
    
    $('<div class="name"/>').text(login)
        .appendTo('#' + login + "_avatar");
    console.log("created name div");
    
    $('<div class="mood"/>').text("mood").appendTo('#' + login + "_avatar");
    console.log("created mood div");

}

$.getScript("http://91.234.35.26/iwiki-admin/v1.0.0/admin/js/jquery.nicescroll.min.js", function(){

    $(".chat").niceScroll();

});

$.getScript("http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js", function(){

    $('#search_form').tooltip({title: "Search friends", placement: "top"});

});




