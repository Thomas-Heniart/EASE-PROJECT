var socket = new WebSocket("ws://localhost:8080/HelloWorld/actions");
socket.onmessage = onMessage;
socketId = null;

function onMessage(event) {
    var response = JSON.parse(event.data);
    var action = response.action;
    var data = response.data;
    if (action === "connect") {
    	location.href = "index.jsp";
    } else if (action === "setSocketId") {
    	socketId = data.socketId;
    } else if (action === "addProfile") {
    	addProfileToDashboard(data.name, data.color, data.profile_id);
    }
}