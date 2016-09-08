
var isHere = $('#ease_extension').get();
if (!isHere[0]) {
	$('body').prepend('<div id="ease_extension" style="dislay:none;">');
}

document.addEventListener("Logout", function(event){

    extension.runtime.sendMessage("Logout", null, function(response) {});

}, false);

document.addEventListener("NewConnection", function(event){
    console.log(event.detail);
    extension.runtime.sendMessage("NewConnection", {"detail":event.detail}, function(response) {});

}, false);
