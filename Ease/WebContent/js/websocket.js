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
    	location.href = "index.jsp";
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
    } else if (action === "moveProfile") {
    	easeDashboard.columns.each(function(){
			if (!($(this).find('.item').length)){
				$(this).width('24.8%');
			}
		});
    	var profile_id = data.profile_id;
    	var profileIdx = data.profileIdx;
    	var columnIdx = data.columnIdx;
    	var profile = $("#" + profile_id + ".item");
    	$(".dashboardColumn:nth-child(" + columnIdx + ") .item:nth-child(" + profileIdx + ")").append(profile);
    	easeDashboard.columns.each(function(){
			if (!($(this).find('.item').length)){
				$(this).width(0);
			}
		});
    } else if (action == "addClassicApp") {
    	console.log("addClassicApp");
    	var sitesContainerId = data.sitesContainerId;
    	var newAppItem = $.parseHTML(data.newAppItem);
    	newAppItem.attr("webid", data.websiteId);
    	newAppItem.attr("name", data.name);
    	newAppItem.attr("logwith", "false");
    	newAppItem.attr("id", data.appId);
    	newAppItem.attr("move", "true");
    	newAppItem.attr("ssoid", "0");
    	newAppItem.attr("class", "siteLinkBox");
    	$("#" + sitesContainerId + ".SitesContainer").append(newAppItem);
    }
}

function pingServer() {
	console.log("ping loop");
	$.get("localhost:8080/connectionWithCookies");
	setTimeout(function() {
		pingServer();
	}, 5000);
}