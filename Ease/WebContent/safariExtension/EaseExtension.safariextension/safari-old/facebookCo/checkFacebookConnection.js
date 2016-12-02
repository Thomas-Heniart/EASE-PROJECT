if(window.location.href.indexOf("://www.facebook.com")!=-1 && document.getElementById("email")){
    extensionLight.runtime.sendMessage("fbDisconnected",{},function(){});
}