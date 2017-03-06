extension.runtime.bckgrndOnMessage("NewLinkToOpen", function (msg, senderTab, sendResponse) {
   console.log("okok2");
   extension.currentWindow(function(currentWindow) {
       extension.tabs.createOrUpdate(currentWindow, senderTab, msg.detail.url, msg.highlight, function(tab){
            extension.tabs.onUpdated(tab, function (newTab) {
               tab = newTab;
           });
       });
   });
});

/*extension.runtime.bckgrndOnMessage("NewLinkToOpen", function (msg, senderTab, sendResponse) {
    console.log("okokokokok");
    extension.currentWindow(function(currentWindow) {
        extension.tabs.createOrUpdate(currentWindow, senderTab, msg.url, msg.highlight, function(){});
    });
});*/
