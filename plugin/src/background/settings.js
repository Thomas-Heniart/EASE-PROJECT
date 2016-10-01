extension.runtime.bckgrndOnMessage("setSettings", function (msg, tab, sendResponse) {
    extension.storage.get("settings", function(res){
        if(res==null) {res = {}; res.homepage = true;}
        res.homepage = msg.homepage;
        console.log("-- Ease set as homepage : "+res.homepage + " --");
        extension.storage.set("settings", res, function(){});
    });
});

extension.runtime.bckgrndOnMessage("getSettings", function (msg, tab, sendResponse) {
    extension.storage.get("settings", function(res){
        if(res==null) {res = {}; res.homepage = true;}
        sendResponse(res);
    });
});