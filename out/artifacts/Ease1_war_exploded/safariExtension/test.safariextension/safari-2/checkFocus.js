if(window === window.top){

if(document.hasFocus && !document.hidden) {
    extensionLight.runtime.sendMessage('newFocus', {'url':document.URL}, function(){});
}

window.onfocus = function(){
    extensionLight.runtime.sendMessage('newFocus', {'url':document.URL}, function(){});
}

document.addEventListener("visibilitychange", function(){
    if(!document.hidden) {
        extensionLight.runtime.sendMessage('newFocus', {'url':document.URL}, function(){});
    }
}, false);
    
}