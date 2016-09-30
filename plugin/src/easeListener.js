$('body').prepend('<div id="ease_extension" style="dislay:none;">');
if($('#loginBody').length!=0){
    console.log($('#loginBody'));
    extension.runtime.sendMessage("getSettings", {}, function(response) {
        if(response.homepage){
            $('body').prepend('<div class="containerParam"><label class="switchParam"><input type="checkbox" class="checkParam" checked><div class="sliderParam"></div></label><p>Ease as homepage</p></div>')
        } else {
            $('body').prepend('<div class="containerParam"><label class="switchParam"><input type="checkbox" class="checkParam" ><div class="sliderParam"></div></label><p>Ease as homepage</p></div>')
        }
        $('.checkParam').change(function() {
            if($(this).is(":checked")) {
                extension.runtime.sendMessage("setSettings", {"homepage":true}, function(response) {});
            } else {
                extension.runtime.sendMessage("setSettings", {"homepage":false}, function(response) {});
            }
        });
    });
}

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

var settings = $('#settingsExtension').get()[0];
if(settings) extension.runtime.sendMessage("settings", {"homepage":settings.getAttribute("homepage")}, function(response){});
