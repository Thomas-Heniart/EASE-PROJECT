var socket = new WebSocket("ws://localhost:8080/HelloWorld/actions");
socket.onmessage = onMessage;
socketId = null;

function onMessage(event) {
    var response = JSON.parse(event.data);
    console.log(response);
    if (response.action === "connect") {
    	location.href = "index.jsp";
    } else if (response.action === "setSocketId") {
    	socketId = response.data.socketId;
    }
}