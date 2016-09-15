extension.tabs.onNewTab(function(tab){
    extension.storage.get("isConnected", function(res){
        if(res=="true"){
            console.log("User is connected. open ease in tab");
            setTimeout(function(){
                extension.tabs.update(tab, "https://ease.space", function(){
                    console.log("ease opened in new tab");
                });
            }, 100);  
        }
    });    
});

/*extension.onNewWindow(function(tab){
    extension.tabs.update(tab, "https://ease.space", function(){
        console.log("ease opened in new window");
    });
});*/