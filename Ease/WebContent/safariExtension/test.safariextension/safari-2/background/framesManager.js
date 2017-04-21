//PERMET DE GERER L'ENVOI DE MESSAGE ENTRE UN DOCUMENT ET SES FRAMES

extension.runtime.bckgrndOnMessage("sendToChildFrame", function(msg, senderTab, sendResponse){
	console.log("send message to child frame");
    extension.tabs.sendMessage(senderTab, "toChildFrame", msg, function(res){
    	if (res) {
    		console.log("response receive, send response to parent:");
        	sendResponse(res);
    	}
    });
    var fct = extension.runtime.bckgrndOnMessage("haveFrames", function() {
    	extension.runtime.removeListener(fct);
    	extension.tabs.sendMessage(senderTab, "toChildFrame", msg, function(res){
    		console.log("response receive, send response to parent.");
        	sendResponse(res);
    	});
    });
});

extension.runtime.onMessage("sendToParentFrame", function(msg, senderTab, sendResponse){
    extension.tabs.sendMessage(senderTab, "toParentFrame", msg, function(res){
        sendResponse(res);
    });
});