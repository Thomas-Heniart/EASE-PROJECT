if(window.location.href.indexOf("https://ease.space")==0 || window.location.href.indexOf("http://51.254.207.91")==0){

document.addEventListener("ScrapFacebook", function(event){
    console.log("recieved");
    extension.runtime.sendMessage("ScrapFacebook",event.detail,function(res){
        document.dispatchEvent(new CustomEvent("ScrapFacebookResult", {"detail":res}));
    });
}, false);

document.addEventListener("ScrapLinkedIn", function(event){
    console.log("recieved");
    extension.runtime.sendMessage("ScrapLinkedIn",event.detail,function(res){
        document.dispatchEvent(new CustomEvent("ScrapLinkedInResult", {"detail":res}));
    });
}, false);

document.addEventListener("ScrapChrome", function(event){
    console.log("recieved");
    extension.runtime.sendMessage("ScrapChrome",event.detail,function(res){
        document.dispatchEvent(new CustomEvent("ScrapChromeResult", {"detail":res}));
    });
}, false);

}