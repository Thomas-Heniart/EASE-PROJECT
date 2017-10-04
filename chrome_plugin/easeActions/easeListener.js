$('body').prepend('<div id="new_ease_extension" safariversion="2.2.4" style="display:none;">');
$(".displayedByPlugin").show();
extension.runtime.sendMessage("getSettings", {}, function (response) {
    if (response.homepage) {
        $("#homePageSwitch").prop("checked", true);
    } else {
        $("#homePageSwitch").prop("checked", false);
    }

    $("body").on("change", "#homePageSwitch", function () {
        if ($(this).is(":checked")) {
            extension.runtime.sendMessage("setSettings", {"homepage": true}, function (response) {
            });
        } else {
            extension.runtime.sendMessage("setSettings", {"homepage": false}, function (response) {
            });
        }
    });
});

document.addEventListener("GetSettings", function (event) {
    extension.runtime.sendMessage("getSettings", {}, function (response) {
        document.dispatchEvent(new CustomEvent("GetSettingsDone", {"detail": response.homepage}));
    });
});

document.addEventListener("SetHompage", function (event) {
    extension.runtime.sendMessage("setSettings", {"homepage": event.detail}, function () {
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
    extension.runtime.sendMessage("NewLinkToOpen", event.detail, function (response) {
    });
}, false);

/* Crypto part */

document.addEventListener("GetPublicKey", function (event) {
    extension.runtime.sendMessage("GetPublicKey", null, function (publicKey) {
        document.dispatchEvent(new CustomEvent("ReturnPublicKey", {detail: publicKey}));
    });
});

document.addEventListener("SetServerPublicKey", function (event) {
    extension.runtime.sendMessage("SetServerPublicKey", event.detail, function (data) {

    });
})

document.addEventListener("Cipher", function (event) {
    extension.runtime.sendMessage("Cipher", event.detail, function (data) {
        document.dispatchEvent(new CustomEvent("ReturnCipheredData", {detail: data}));
    });
}, false);

document.addEventListener("Decipher", function (event) {
    extension.runtime.sendMessage("Decipher", event.detail, function (data) {
        document.dispatchEvent(new CustomEvent("ReturnDecipheredData", {detail: data}));
    });
});