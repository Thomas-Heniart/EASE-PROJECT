extension.storage.get("settings", function(res){
    if(!res || res.homepage==undefined) {
        extension.storage.set("settings", {"homepage":false}, function(){});
        res = {"homepage":false};
    }
    
    chrome.tabs.query({ active: true, currentWindow: true }, function(tabs) {
        var active = tabs[0].id;
        if(res.homepage==true){
            chrome.tabs.update(active, { url: "https://ease.space" }, function(){});
            //chrome.runtime.sendMessage({"name":"changeHomepage", "message":{}}, function(){});
            //window.location.replace("https://ease.space");
        } else {
            // Set the URL to the Local-NTP (New Tab Page)
			chrome.tabs.update(active, { url: "chrome-search://local-ntp/local-ntp.html" }, function(){});
		}

        //window.location.replace("about:blank");
    });

});
