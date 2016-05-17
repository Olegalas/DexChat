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

            createFriendForModel(webMessage.message.login, webMessage.message.login);

            $("<button type='button' class='btn btn-default' data-dismiss='modal'>").text("add").click(function(){
                
                $( ".user_model" ).remove();
                $("#model_add_message").show();
                
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
    $("#model_add_message").hide();

    // 2) read input
    var input = $("#search_input").val();
    
    if(input == ""){
        $("#model_message").show();
        $("#myModal").modal();
        return;
    }
    
    console.log("search - " + input);
    // 3) send input
    
    var friend = {
        
        login: input,
        name: $('#login').text(),
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

    $('<div class="user" style="position: relative;padding: 0 0 0 50px; display: block; cursor: pointer; margin: 0 0 20px;"/>').attr({

        id: login

    }).appendTo('#chat-users_frame');

    createFriend(login);

    $('<button type="button" class="btn btn-default"></button>').text("menu")
        .click(function () {

        $( ".user_model" ).remove();
        $("#model_add_message").hide();
        $("#model_message").hide();

        $('<div class="user_model"/>').attr({

            id: "id_model_" + login

        }).appendTo("#chat-users_model");
        
        createFriendForModel("id_model_" + login, login);

        $("<button type='button' class='btn btn-default' data-dismiss='modal'>").text("remove").click(function(){

            alert("CLICK");
            
        }).appendTo('#id_model_' + login + "_avatar");

        $("#myModal").modal();
        
    }).appendTo("#" + login);

}

function createFriend(login) {

    $('<div calss="avatar" style="top: 0; left: 0; width: 40px; height: 40px; position: absolute;"/>').attr({

        id: login + "_avatar"

    }).appendTo('#' + login);

    $('<img src="http://bootdey.com/img/Content/avatar/avatar2.png" style="display: block; border-radius: 20px; height: 100%;"/>').attr({

        alt: login

    }).appendTo('#' + login + "_avatar");
    
    $('<div class="name" style="font-size: 14px; font-weight: bold; line-height: 20px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;"/>').text(login)
        .appendTo('#' + login);
    
    $('<div class="mood" style="white-space: nowrap; overflow: hidden; text-overflow: ellipsis;"/>').text("mood")
        .appendTo('#' + login);

}

function createFriendForModel(login, loginName) {

    $('<div calss="avatar"/>').attr({

        id: login + "_avatar"

    }).appendTo('#' + login);

    $('<img src="http://bootdey.com/img/Content/avatar/avatar2.png" />').attr({

        alt: login

    }).appendTo('#' + login + "_avatar");

    $('<div class="name" />').text(loginName)
        .appendTo('#' + login);

    $('<div class="mood" />').text("mood")
        .appendTo('#' + login);

}

$.getScript("http://91.234.35.26/iwiki-admin/v1.0.0/admin/js/jquery.nicescroll.min.js", function(){

    $(".chat").niceScroll();

});

$.getScript("http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js", function(){

    $('#search_form').tooltip({title: "Search friends", placement: "top"});

});




