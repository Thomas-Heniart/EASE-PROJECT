function isConnected(msg){
	var object = $(msg.detail[msg.bigStep].website.checkAlreadyLogged[0].search);
	if (object.length == 0)
		return false;
	return true;
}

function getNewLogin(msg, i){
	if (msg.detail[i].user){
		return (msg.detail[i].user.login);
	} else if (msg.detail[i].logWith){
		return getNewLogin(msg, i-1) + "-" + msg.detail[i].website.name;
	}
}

function alreadyVisited(msg){
	
	for (var i in (msg.visitedWebsites)){
		if (msg.visitedWebsites[i].name == msg.detail[msg.bigStep].website.name) {
			if (getNewLogin(msg, msg.bigStep) == msg.visitedWebsites[i].lastLogin){
				return true;
			}
			else {
				return false;
			}
		}
	}
	return false;
}

if (window.top === window) {

extension.runtime.onMessage("goooo", function(msg, sendResponse) {
	if (msg.todo == "checkAlreadyLogged"){
		if (isConnected(msg) == true) {
			if (alreadyVisited(msg) == true){
				msg.todo = "connect";
				msg.actionStep = msg.detail[msg.bigStep].website[msg.todo].todo.length;
				msg.type = "completed";
				sendResponse(msg);
			} else {
				msg.todo = "logout";
				doThings(msg, sendResponse);
			}
		} else {
			if (typeof msg.detail[msg.bigStep].logWith === "undefined") {
				msg.todo = "connect";
			} else {
				msg.todo = msg.detail[msg.bigStep].logWith;
			}
			doThings(msg, sendResponse);
		}
	}
	else
		doThings(msg, sendResponse);
});

}