var socket = new WebSocket("ws://localhost:8887");
var mainId;
var mainLogin;
var friendLogin;
var friendId;

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

    switch (webMessage.type){

        case "FRIEND":{

            console.log(webMessage.message.friends);
            mainId = webMessage.message.idClient;
            console.log("your id : " + mainId);


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
                        idClient: "unknown",
                        friends: [{login: webMessage.message.login, name: "unknown", email: "unknown", idClient: "unknown", friends:[]}]

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

                    createUserDiv(webMessage.message.friends[i].login, webMessage.message.friends[i].idClient);

                }
            }

            break;
        }
        case "HISTORY":{
            
            console.log(webMessage.message);
            if("EMPTY" == webMessage.message.type){
                console.log("history is empty");
                $("#empty_answer").show();
            } else{
                
                $("#empty_answer").hide();
                
                for(i = 0; i < webMessage.message.messages.length; ++i){
                    if(webMessage.message.messages[i].idSender == mainId){
                        initRightAnswer(mainLogin, webMessage.message.messages[i].message, webMessage.message.messages[i].date);
                    }else{
                        initLeftAnswer(friendLogin, webMessage.message.messages[i].message, webMessage.message.messages[i].date);
                    }
                }
            }
            
            break;
        }
        case "TEXT":{

            if ("incorrect friend login" == webMessage.message){
                $("#model_message").show();
                $("#myModal").modal();
            }
            break;
        }

    }
};

$(document).on("keydown", "#search_input", function (e) {

    e.which = e.which || e.keyCode;
    if(e.which == 13) {
        searchFriends();
    }
    
});

$(document).on("click", "#search_button", function () {
    // 1) delete previous
    $( ".user_model" ).remove();
    $("#model_message").show();
    $("#model_add_message").hide();

    // 2) read input
    var input = $("#search_input").val();
    $("#search_input").val('');

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
        idClient: "unknown",
        friends: []

    };

    sendMessage("FRIEND", friend);
});

function searchFriends(){

    // 1) delete previous
    $( ".user_model" ).remove();
    $("#model_message").show();
    $("#model_add_message").hide();

    // 2) read input
    var input = $("#search_input").val();
    $("#search_input").val('');

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
        idClient: "unknown",
        friends: []

    };

    sendMessage("FRIEND", friend);


}

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

function createUserDiv(login, id) {

    var icon = $('<span class="glyphicon glyphicon-option-horizontal" style="right:0; position: absolute;"/>').click(function () {

        $(".user_model").remove();
        $("#model_add_message").hide();
        $("#model_message").hide();

        var removeButton =  $("<button type='button' class='btn btn-default' data-dismiss='modal'>").text("remove").click(function () {

            $('#' + login).remove();
            alert("Friend was deleted");
            var friend = {

                login: login,
                name: mainLogin,
                email: "delete",
                idClient: "unknown",
                friends: []

            };

            sendMessage("FRIEND", friend);
        });

        var mood = $('<div class="mood"/>').text("mood");
        var name = $('<div class="name"/>').text(login);

        var image = $('<img src="http://bootdey.com/img/Content/avatar/avatar2.png"/>').attr({alt: login});
        var avatar = $('<div class="avatar" />').append(image);

        var userModel = $('<div class="user_model"/>').append(avatar).append(name).append(mood)
            .append(removeButton).appendTo("#chat-users_model");

        $("#myModal").modal();
    });

    var mood = $('<div class="mood"/>').text("mood");
    var name = $('<div class="name"/>').text(login);

    var image = $('<img src="http://bootdey.com/img/Content/avatar/avatar2.png"/>').attr({alt: login});
    var avatar = $('<div class="avatar" />').append(image);

    var user = $('<div class="user" />').attr({id: login}).click(function(){

            friendLogin = login;
            friendId = id;

            $(".answer").remove();
            $("#login_speaker").text(login);

            $(".user").css("background", "#46be8a")
            $(this).css("background", "#33ccff");
        
            var webMessage = {loginFriend: friendLogin, login: mainLogin};
            
            sendMessage("HISTORY", webMessage);

        }).append(avatar).append(name).append(mood).append(icon).appendTo('#chat-users_frame');

}

function createFriend(login) {

    $('<div class="avatar" style="top: 0; left: 0; width: 40px; height: 40px; position: absolute;"/>').attr({

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

    $('<div class="avatar"/>').attr({

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

function initRightAnswer(login, message, date){

    var image = $('<img src="http://bootdey.com/img/Content/avatar/avatar1.png" />').attr({alt: login});

    var avatar = $('<div class="avatar"/>').append(image);
    var name = $('<div class="name"/>').text(login);
    var text = $('<div class="text"/>').text(message);
    var time = $('<div class="time"/>').text(date);
    
    $('<div class="answer right" />')
        .append(avatar).append(name).append(text).append(time).appendTo(".chat-body");
    
    $("#empty_answer").hide();

}

function initLeftAnswer(login, message, date){

    var image = $('<img src="http://bootdey.com/img/Content/avatar/avatar2.png" />').attr({alt: login});

    var avatar = $('<div class="avatar"/>').append(image);
    var name = $('<div class="name"/>').text(login);
    var text = $('<div class="text"/>').text(message);
    var time = $('<div class="time"/>').text(date);

    $('<div class="answer left" />')
        .append(avatar).append(name).append(text).append(time).appendTo(".chat-body");

    $("#empty_answer").hide();
}


$(document).on("click", "#answer_button", function () {

    // 1) get message from input
    var messageText = $("#answer_input").val();
    $("#answer_input").val('');
    console.log("from input : " + messageText);
    
    // 2) init new answer tag to dialog frame
    var date = new Date();
    console.log(date.toLocaleString());
    initRightAnswer(mainLogin, messageText, date.toLocaleString());
    console.log("after initAnswer");

    console.log("idReceiver: " + friendId);
    console.log("idSender: " + mainId);
    var message = {idReceiver: friendId, idSender: mainId, message: messageText, date: date.getMilliseconds()};
    
    // send it on server to permanently persist in database history
    sendMessage("MESSAGE", message);

});




//
// <fieldset class="form-group">
//     <label for="exampleInputFile">File input</label>
//     <input type="file" class="form-control-file" id="exampleInputFile">
//         <small class="text-muted">This is some placeholder block-level help text for the above input. It's a bit lighter and easily wraps to a new line.</small>
// </fieldset>