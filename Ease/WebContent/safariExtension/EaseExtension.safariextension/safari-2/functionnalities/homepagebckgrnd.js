extension.storage.get("settings", function(res){
    if(!res || res.homepage==undefined) extension.storage.set("settings", {"homepage":true}, function(){});
});

extension.onNewWindow(function(window){
    extension.storage.get("settings", function(res){
        if(res.homepage==true){
            if(extension.nbOfEaseTabs(window)==0){
             extension.tabs.create(window, "https://ease.space", false, function(newTab){});
             console.log("-- Ease opened in new window --");
            }
        }
    });
});