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


    $(document).ready(function () {

        var login = $('#login').text();
        var pass = $('#pass').text();

        document.title = "DexChat - " + login;
        if ("connection complete" == webMessage.message) {

            $.getJSON('//api.ipify.org?format=jsonp&callback=?', function (data) {
                console.log("home ip address : " + data.ip);
                var ip = data.ip;
                doLogin(login, pass, ip);
            });
           
        }
    });

    if("FRIEND" == webMessage.type){
        
        console.log(webMessage.friends);
 
        if(webMessage.friends.length == 0){

            $('<div/>', {

                class: "user_model",
                id: webMessage.friends[i].login

            }).appendTo('#chat-users_model');

            createFriend(webMessage.friends[i].login);

            $("#model_message").hide();
            $("#myModal").modal();


        } else{

            for(i = 0; i != webMessage.friends.length; ++i){
                console.log(webMessage.friends[i].login);



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



                $('<div/>', {

                    class: "user",
                    id: webMessage.friends[i].login

                }).appendTo('#chat-users_frame');

                createFriend(webMessage.friends[i].login);

            }
        }
    }
};

$(document).on("click", "#search_button", function () {

    // 1) delete previous
    $( ".user_model" ).remove();
    $("#model_message").show();

    // 2) read input
    var input = $("#search_input").text();

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

function createFriend(login) {

    $('<div/>', {

        class: "avatar",
        id: login + "_avatar"

    }).appendTo('#' + webMessage.friends[i].login);

    $('<img/>', {

        src: "http://bootdey.com/img/Content/avatar/avatar2.png",
        alt: login

    }).appendTo('#' + login + "_avatar");

    $('<div/>', {

        class: "name",
        html: login

    }).appendTo('#' + login + "_avatar");

    $('<div/>', {

        class: "mood",
        html: "mood"

    }).appendTo('#' + login + "_avatar");


}

$.getScript("http://91.234.35.26/iwiki-admin/v1.0.0/admin/js/jquery.nicescroll.min.js", function(){

    $(".chat").niceScroll();

});

$.getScript("http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js", function(){

    $('#search_form').tooltip({title: "Search friends", placement: "top"});

});




