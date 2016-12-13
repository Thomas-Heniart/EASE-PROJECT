var socket = new WebSocket("ws://localhost:8080/actions");
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
    } else if (action === "logout") {
    	window.location.replace("index.jsp");
    } else if (action === "ping") {
    	console.log("ping");
    	pingServer();
    } else if (action === "removeProfile") {
    	var profile_index = profiles.findIndex(function(profile) {
    		return profile.id === data.profile_id;
    	});
    	console.log("remove profile " + profile_index);
    	console.log(profiles);
    	if (profile_index > -1) {
    		console.log(profiles[profile_index]);
    	    profiles[profile_index].remove();
    	}
    	console.log(profiles);
    }
}

function pingServer() {
	console.log("ping loop");
	$.get("localhost:8080/connectionWithCookies");
	setTimeout(function() {
		pingServer();
	}, 5000);
}