extension.storage.get("settings", function(res){
    if(!res || res.homepage==undefined) {
        extension.storage.set("settings", {"homepage":true}, function(){});
        res = {"homepage":true};
    }
    if(res.homepage==true){
        chrome.runtime.sendMessage({"name":"changeHomepage", "message":{}}, function(){});
        //window.location.replace("https://ease.space");
    } else {
        window.location.replace("about:blank");
    }
});