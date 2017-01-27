if (window.location.href.indexOf("https://www.facebook.com") == 0) {

    if ($("#email").length > 0) {
        extension.sendMessage("fbDisconnected", {}, function () {});
    }

}
