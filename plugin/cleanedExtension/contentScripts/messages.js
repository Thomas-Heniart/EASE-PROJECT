if (window.top === window) {
    extension.runtime.send('reloaded', {}, function () {});
    extension.runtime.onMessage.addListener('executeActions', function(msg, sendResponse){
        executeActions(msg, sendResponse);
    });
    extension.runtime.onMessage.addListener('checkWhoIsConnected', function(msg, sendResponse){
        checkWhoIsConnected(msg, sendResponse);
    });
    extension.runtime.onMessage.addListener('removeOverlay', function(msg, sendResponse){
        //remove overlay
    });
} else {
    window.addEventListener("executeOnFrame", function(event){
        
    }, false);
}