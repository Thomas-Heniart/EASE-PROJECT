document.addEventListener("ScrapFacebook", function(event){
    extension.runtime.sendMessage("ScrapFacebook",{user:event.detail},function(res){
        document.dispatchEvent(new CustomEvent("ScrapFacebookResult", {"detail":res}));
    });
}, false);

document.addEventListener("ScrapLinkedin", function(event){
    extension.runtime.sendMessage("ScrapLinkedin",{user:event.detail},function(res){
        document.dispatchEvent(new CustomEvent("ScrapLinkedinResult", {"detail":res}));
    });
}, false);

document.addEventListener("ScrapChrome", function(event){
    extension.runtime.sendMessage("ScrapChrome",{user:event.detail},function(res){
        document.dispatchEvent(new CustomEvent("ScrapChromeResult", {"detail":res}));
    });
}, false);

document.addEventListener("GetChromeUser", function(event){
    extension.runtime.sendMessage("GetChromeUser",{},function(res){
        document.dispatchEvent(new CustomEvent("ChromeUserEmail", {"detail":res}));
    });
}, false);