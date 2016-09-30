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
		return {"user":getNewLogin(msg, i-1), "logWith":getHost(msg.detail[i].website.loginUrl)};
	}
}

function alreadyVisited(msg){
    if(msg.allConnections[getHost(msg.detail[msg.bigStep].website.loginUrl)]){
         if(msg.allConnections[getHost(msg.detail[msg.bigStep].website.loginUrl)] == getNewLogin(msg, msg.bigStep))
            return true;
    }
	return false;
}

if (window.top === window) {

extension.runtime.onMessage("goooo", function(msg, sendResponse) {
	if (msg.todo == "checkAlreadyLogged"){
        checkConnectionOverlay(msg);
		if (isConnected(msg) == true) {
			if (alreadyVisited(msg) == true){
				msg.todo = "connect";
				msg.actionStep = msg.detail[msg.bigStep].website[msg.todo].todo.length;
				msg.type = "completed";
				sendResponse(msg);
			} else {
				msg.todo = "logout";
				doThings(msg, sendResponse);
                logoutOverlay(msg);
			}
		} else {
            msg.waitreload = true;
			if (typeof msg.detail[msg.bigStep].logWith === "undefined") {
				msg.todo = "connect";
			} else {
				msg.todo = msg.detail[msg.bigStep].logWith;
			}
			doThings(msg, sendResponse);
            if(msg.todo == "logout"){
                logoutOverlay(msg);
            } else if(msg.todo == "connect"){
                loginOverlay(msg);
            }
		}
	} else if(msg.todo == "end"){
        endOverlay();
        /*if(isConnected(msg) == true){
            msg.type = "completed";
            sendResponse(msg);
        } else {
            msg.type = "error: not connected";
            sendResponse(msg);
        }*/
        msg.type = "completed";
        sendResponse(msg);
    } else if(msg.todo=="nextBigStep"){
        msg.type = "completed";
        sendResponse(msg);
    }
	else {
        doThings(msg, sendResponse);
        if(msg.todo == "logout"){
            logoutOverlay(msg);
        } else if(msg.todo == "connect"){
            loginOverlay(msg);
        }
    }
		
});

}