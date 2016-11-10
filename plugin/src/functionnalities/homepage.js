extension.storage.get("settings", function(res){
    if(!res || res.homepage==undefined) extension.storage.set("settings", {"homepage":true}, function(){});
    if(res.homepage==true){
        window.location.replace("https://ease.space");
    } else {
        window.location.replace("about:blank");
    }
});
/*
extension.tabs.onNewTab(function(tab){
    extension.storage.get("settings", function(res){
        if(res.homepage==true){
            extension.tabs.stopLoad(tab, function(){
                extension.tabs.update(tab, "https://ease.space", function(){
                    console.log("-- Ease opened in new window --");
                });
            });
        }
    });    
});

extension.onNewWindow(function(tab){
    extension.storage.get("settings", function(res){
        if(res.homepage==true){
            extension.tabs.stopLoad(tab, function(){
                extension.tabs.update(tab, "https://ease.space", function(){
                    console.log("-- Ease opened in new window --");
                });
            });  
        }
    });
});*/