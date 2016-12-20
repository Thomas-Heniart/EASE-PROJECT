document.addEventListener("ScrapFacebook", function(event){
    console.log("recieved");
    extension.runtime.sendMessage("ScrapFacebook",event.detail,function(res){
        document.dispatchEvent(new CustomEvent("ScrapFacebookResult", {"detail":res}));
    });
}, false);

document.addEventListener("ScrapLinkedin", function(event){
    extension.runtime.sendMessage("ScrapLinkedin",event.detail,function(res){
        document.dispatchEvent(new CustomEvent("ScrapLinkedinResult", {"detail":res}));
    });
}, false);

document.addEventListener("ScrapChrome", function(event){
    extension.runtime.sendMessage("ScrapChrome",event.detail,function(res){
        document.dispatchEvent(new CustomEvent("ScrapChromeResult", {"detail":res}));
    });
}, false);

document.addEventListener("GetChromeUser", function(event){
    extension.runtime.sendMessage("GetChromeUser",{},function(res){
        document.dispatchEvent(new CustomEvent("ChromeUserEmail", {"detail":res}));
    });
}, false);