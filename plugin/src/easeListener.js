$('body').prepend('<div id="ease_extension" style="dislay:none;">');

extension.runtime.sendMessage("getSettings", {}, function(response) {
    if(response.homepage){
        $('body').prepend('<div class="containerParam"><label class="switchParam"><input type="checkbox" class="checkParam" checked><div class="sliderParam"></div></label><p>Homepage</p></div>')
    } else {
        $('body').prepend('<div class="containerParam"><label class="switchParam"><input type="checkbox" class="checkParam" ><div class="sliderParam"></div></label><p>Homepage</p></div>')
    }
    $(window).resize(function () {
        var width = $('.containerParam').width();
        var height = $('.containerParam').height();
        $('.containerParam p').css({
            'font-size': (width/9) + 'px',
            'line-height': height + 'px'
        });
    }).trigger('resize');
    var $div = $('li.leaf.item');

    $('.checkParam').change(function() {
        if($(this).is(":checked")) {
            extension.runtime.sendMessage("setSettings", {"homepage":true}, function(response) {});
        } else {
            extension.runtime.sendMessage("setSettings", {"homepage":false}, function(response) {});
        }
    });
});


document.addEventListener("isConnected", function(event){
    if(event.detail=="true"){
        extension.storage.get("isConnected",function(res){
            if(res == "false")  extension.storage.set("visitedWebsites", []);
        });
        extension.storage.set("isConnected","true", function(){});
    } else {
        extension.storage.set("isConnected","false", function(){});
        extension.runtime.sendMessage("Disconnected", null, function(response) {});
    }
}, false);

document.addEventListener("Logout", function(event){
    extension.runtime.sendMessage("Logout", null, function(response) {});
}, false);

extension.runtime.onMessage("logoutFrom", function logoutHandler(visitedWebsites, sendResponse){
    document.dispatchEvent(new CustomEvent("LogoutFrom", {"detail":visitedWebsites}));
});

extension.runtime.onMessage("logoutDone", function logoutHandler(message, sendResponse){
    document.dispatchEvent(new CustomEvent("LogoutDone", {"detail":message}));
});

document.addEventListener("NewConnection", function(event){
    extension.runtime.sendMessage("NewConnection", {"highlight":true, "detail":event.detail}, function(response) {});
}, false);

