if (window.location.href.indexOf("https://ease.space") == 0 || window.location.href.indexOf("https://51.254.207.91:8443") == 0 || window.location.href.indexOf("https://localhost:8443") == 0 || window.location.href.indexOf("https://192.168.0.19:8443") == 0 || window.location.href.indexOf("https://turfu.ease.space:8443") == 0) {

    $('body').prepend('<div id="ease_extension" safariVersion="2.2.5" style="display:none;">');
    $("input[type='password']").attr("data-password-autocomplete", "off");
    $("input[type='password']").each(function () {
        $(this).prop('type', 'text');
        $('<input type="password"/>').hide().insertBefore(this);
        $(this).focus(function () {
            $(this).prop('type', 'password');
        });
    });
    $(".displayedByPlugin").show();
    extension.runtime.sendMessage("getSettings", {}, function (response) {
        if (response.homepage) {
            $("#homePageSwitch").prop("checked", true);
        } else {
            $("#homePageSwitch").prop("checked", false);
        }

        $('#homePageSwitch').change(function () {
            if ($(this).is(":checked")) {
                extension.runtime.sendMessage("setSettings", {"homepage": true}, function (response) {
                });
            } else {
                extension.runtime.sendMessage("setSettings", {"homepage": false}, function (response) {
                });
            }
        });
    });

    document.addEventListener("Logout", function (event) {
        extension.runtime.sendMessage("Logout", null, function (response) {
        });
    }, false);

    extension.runtime.onMessage("logoutFrom", function logoutHandler(visitedWebsites, sendResponse) {
        document.dispatchEvent(new CustomEvent("LogoutFrom", {"detail": visitedWebsites}));
    });

    extension.runtime.onMessage("logoutDone", function logoutHandler(message, sendResponse) {
        document.dispatchEvent(new CustomEvent("LogoutDone", {"detail": message}));
    });

    document.addEventListener("NewConnection", function (event) {
        if (event.detail.highlight == undefined) event.detail.highlight = true;
        extension.runtime.sendMessage("NewConnection", {
            "highlight": event.detail.highlight,
            "detail": event.detail
        }, function (response) {
        });
    }, false);

    document.addEventListener("NewLinkToOpen", function (event) {
        if (event.detail.highlight == undefined) event.detail.highlight = true;
        extension.runtime.sendMessage("NewLinkToOpen", {
            "highlight": event.detail.highlight,
            "detail": event.detail
        }, function (response) {
        });
    }, false);


}
