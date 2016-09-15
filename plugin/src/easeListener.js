
var isHere = $('#ease_extension').get();
if (!isHere[0]) {
    $('body').prepend('<div id="ease_extension" style="dislay:none;">');
}

document.addEventListener("isConnected", function(event){
    console.log('Is someone connected : '+event.detail);
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
    console.log(event.detail);
    extension.runtime.sendMessage("NewConnection", {"detail":event.detail}, function(response) {});

}, false);

