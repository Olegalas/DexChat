if(socket == null && socket == undefined){

    var socket = new WebSocket("ws://localhost:8887");

    socket.onopen = function() {
        alert("Connection complete ");
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
        alert("LoginMessage " + event.data);
        // if message will "welcome" redirect on home page
    };

    socket.onerror = function(error) {
        alert("Error " + error.message);
    };    
    
}


