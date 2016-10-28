$('body').prepend('<div id="ease_extension" style="dislay:none;">');

extension.runtime.sendMessage("getSettings", {}, function(response) {
    $(".displayedByPlugin").show();
    if(response.homepage){
        $("#homePageSwitch").prop("checked", true);
    } else {
        $("#homePageSwitch").prop("checked", false);
    }

    $('#homePageSwitch').change(function() {
        if($(this).is(":checked")) {
            extension.runtime.sendMessage("setSettings", {"homepage":true}, function(response) {});
        } else {
            extension.runtime.sendMessage("setSettings", {"homepage":false}, function(response) {});
        }
    });
});

document.addEventListener("isConnected", function(event){
    if(event.detail=="true"){
        extension.storage.get("isConnected",function(res){
            if(res == "false")  extension.storage.set("visitedWebsites", []);
        });
        extension.storage.set("isConnected","true", function(){});
    } else {
        extension.storage.set("isConnected","false", function(){});
        extension.runtime.sendMessage("Disconnected", null, function(response) {});
    }
}, false);

document.addEventListener("Logout", function(event){
    extension.runtime.sendMessage("Logout", null, function(response) {});
}, false);

extension.runtime.onMessage("logoutFrom", function logoutHandler(visitedWebsites, sendResponse){
    document.dispatchEvent(new CustomEvent("LogoutFrom", {"detail":visitedWebsites}));
});

extension.runtime.onMessage("logoutDone", function logoutHandler(message, sendResponse){
    document.dispatchEvent(new CustomEvent("LogoutDone", {"detail":message}));
});

document.addEventListener("NewConnection", function(event){
    extension.runtime.sendMessage("NewConnection", {"highlight":event.detail.highlight, "detail":event.detail}, function(response) {});
}, false);

document.addEventListener("NewLinkToOpen", function(event){
    console.log(event);
    extension.runtime.sendMessage("NewLinkToOpen", event.detail, function(response) {});
}, false);

document.addEventListener("GetUpdates", function(event){
    var updatesToSend = [];
    var id = 0;
    extension.storage.get("allConnections", function(res){
        if(res==undefined || !res.validator) res = {validator:"ok"};
        var toDelete = [];
        for(var email in event.detail){
            if(res[email]){
                for(var website in res[email]){
                    if(res[email][website].logWith) var toSend = {user:email, website:website, logWith:res[email][website].logWith};
                    else  var toSend = {user:email, website:website, password:res[email][website].password};
                    id++;
                    updatesToSend.push(toSend);
                }
            }
        }
        extension.storage.set("allConnections", res, function(){});
        document.dispatchEvent(new CustomEvent("NewUpdates", {"detail":updatesToSend}));
    });
});

document.addEventListener("RemoveUpdate", function remover(event){
    if(res[event.detail.user]){
        if(res[event.detail.user][event.detail.website]){
            delete res[event.detail.user][event.detail.website];
        }
    }            
});