//PERMET DE GERER L'ENVOI DE MESSAGE ENTRE UN DOCUMENT ET SES FRAMES

extension.runtime.onMessage.addListener("sendBackToFrame", function(msg, senderTab, sendResponse){
    extension.tabs.sendMessage(senderTab, "toChildFrame", msg, function(res){
        sendResponse(res);
    });
});

extension.runtime.onMessage.addListener("sendBackToParent", function(msg, senderTab, sendResponse){
    extension.tabs.sendMessage(senderTab, "toParentFrame", msg, function(res){
        sendResponse(res);
    });
});