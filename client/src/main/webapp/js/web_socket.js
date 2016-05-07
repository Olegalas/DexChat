var socket = new WebSocket("ws://localhost:8887");
var login;
var pass;

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

socket.onmessage = function(event) {
    console.log("Message " + event.data);
};

socket.onerror = function(error) {
    alert("Error " + error.message);
};



