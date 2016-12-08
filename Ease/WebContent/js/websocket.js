var socket = new WebSocket("ws://localhost:8080/HelloWorld/actions");
socket.onmessage = onMessage;

function onMessage(event) {
    var response = JSON.parse(event.data);
    if (response.action === "connect") {
    	console.log("pouette");
    	location.href = "index.jsp";
    }
}