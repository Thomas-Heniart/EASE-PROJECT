if(window.location.href.indexOf("https://www.facebook.com")==0 && document.getElementById("email")){
    extensionLight.runtime.sendMessage("fbDisconnected",{},function(){});
}