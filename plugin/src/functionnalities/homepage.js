extension.storage.get("settings", function(res){
    if(res==null) extension.storage.set("settings", {"homepage":true}, function(){});
});

extension.tabs.onNewTab(function(tab){
    extension.storage.get("settings", function(res){
        if(res.homepage==true){
            extension.tabs.update(tab, "https://ease.space", function(){
                console.log("ease opened in new tab");
            }); 
        }
    });    
});

extension.onNewWindow(function(tab){
    extension.storage.get("settings", function(res){
        if(res.homepage==true){
            extension.tabs.update(tab, "https://ease.space", function(){
                console.log("ease opened in new window");
            });
        }
    });
});