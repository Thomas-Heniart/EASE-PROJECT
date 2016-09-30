extension.storage.get("settings", function(res){
    if(res==null) extension.storage.set("settings", {"homepage":true}, function(){});
});

extension.tabs.onNewTab(function(tab){
    console.log("NEW TAB");
    extension.storage.get("settings", function(res){
        console.log(res.homepage);
        if(res.homepage==true){
            console.log("GO TO EASE");
            extension.tabs.update(tab, "https://ease.space", function(){
                console.log("ease opened in new tab");
            }); 
        }
    });    
});

extension.onNewWindow(function(tab){
    
    console.log("NEW TAB");
    extension.storage.get("settings", function(res){
        console.log(res.homepage);
        if(res.homepage==true){
            console.log("GO TO EASE");
            extension.tabs.update(tab, "https://ease.space", function(){
                console.log("ease opened in new window");
            });
        }
    });
});