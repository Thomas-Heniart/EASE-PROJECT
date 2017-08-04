extension.runtime.bckgrndOnMessage("NewLinkToOpen", function (msg, senderTab, sendResponse) {
    
    extension.currentWindow(function(currentWindow) {
        extension.tabs.createOrUpdate(currentWindow, senderTab, msg.url, msg.highlight, function(){});
    });
});