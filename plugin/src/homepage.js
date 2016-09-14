extension.tabs.onNewTab(function(tab){
    extension.storage.get("isConnected", function(res){
        if(res=="true"){
            extension.tabs.update(tab, "https://ease.space", function(){
                console.log("ease opened in new tab");
            });
        }
    });    
});

extension.onNewWindow(function(tab){
    extension.tabs.update(tab, "https://ease.space", function(){
        console.log("ease opened in new window");
    });
});