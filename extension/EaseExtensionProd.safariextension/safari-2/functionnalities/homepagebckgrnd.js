extension.storage.get("settings", function(res){
    if(!res || res.homepage==undefined) extension.storage.set("settings", {"homepage":true}, function(){});
});

extension.onNewWindow(function(window){
    extension.storage.get("settings", function(res){
        if(res.homepage==true){
            setTimeout(function(){
                extension.tabs.create(window, "https://ease.space", true, function(newTab){});
            },10);
            console.log("-- Ease opened in new window --");
        }
    });
});