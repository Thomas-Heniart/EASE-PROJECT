$(".displayedByPlugin").show();
extension.getStorage("settings", function(response) {
    if(response.homepage){
        $("#homePageSwitch").prop("checked", true);
    } else {
        $("#homePageSwitch").prop("checked", false);
    }

    $('#homePageSwitch').change(function() {
        if($(this).is(":checked")) {
            extension.setStorage("settings", {"homepage":true}, function(response) {});
        } else {
            extension.setStorage("settings", {"homepage":false}, function(response) {});
        }
    });
});

document.addEventListener("Logout", function(event){
    extension.sendMessage("GlobalLogout", null, function(response) {});
}, false);

extension.onMessage.addListener("logoutFrom", function logoutHandler(visitedWebsites, sendResponse){
    document.dispatchEvent(new CustomEvent("LogoutFrom", {"detail":visitedWebsites}));
});

extension.onMessage.addListener("logoutDone", function logoutHandler(message, sendResponse){
    document.dispatchEvent(new CustomEvent("LogoutDone", {"detail":message}));
});

document.addEventListener("NewConnection", function(event){
    if(event.detail.highlight == undefined) event.detail.highlight = true;
    extension.sendMessage("NewConnection", {"highlight":event.detail.highlight, "detail":event.detail}, function(response) {});
}, false);

document.addEventListener("NewLinkToOpen", function(event){
    extension.sendMessage("NewLinkToOpen", event.detail, function(response) {});
}, false);
