$('body').prepend('<div id="new_ease_extension" safariversion="2.2.4" style="display:none;">');
$("input[type='password']").attr("data-password-autocomplete", "off");
$("input[type='password']").each(function () {
    $(this).prop('type', 'text');
    $('<input type="password"/>').hide().insertBefore(this);
    $(this).focus(function () {
        $(this).prop('type', 'password');
    });
});
//$("form, input").attr('autocomplete', 'off');
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