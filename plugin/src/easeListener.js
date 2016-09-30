$('body').prepend('<div id="ease_extension" style="dislay:none;">');

document.addEventListener("isConnected", function(event){
    if(event.detail==true){
        extension.storage.set("isConnected","true", function(){});
    } else {
        extension.storage.set("isConnected","false", function(){});
    }
}, false);

document.addEventListener("Logout", function(event){

    extension.runtime.sendMessage("Logout", null, function(response) {});

}, false);

document.addEventListener("NewConnection", function(event){
    extension.runtime.sendMessage("NewConnection", {"highlight":true, "detail":event.detail}, function(response) {});

}, false);

var settings = $('#settings').get()[0];
extension.runtime.sendMessage("settings", {"homepage":settings.getAttribute("homepage")}, function(response){});
