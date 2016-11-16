document.addEventListener("Test", function(event){
    extension.runtime.sendMessage("TestConnections", event.detail, function(response) {});
}, false);
