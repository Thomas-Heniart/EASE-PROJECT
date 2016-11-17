document.addEventListener("Test", function(event){
    console.log(event);
    extension.runtime.sendMessage("TestConnection", {detail:event.detail}, function(response) {});
}, false);
