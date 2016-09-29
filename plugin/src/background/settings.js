extension.runtime.bckgrndOnMessage("settings", function (msg, sendResponse) {
    console.log("HEY HEY CHANGE LES SETTINGS");
    extension.storage.get("settings", function(res){
        if(res==null) {res = {}; res.homepage = false;}
        res.homepage = msg.homepage;
        extension.storage.set("settings", res, function(){});
    });
});