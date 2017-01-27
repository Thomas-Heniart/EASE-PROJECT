if (window.top === window) {
    extension.sendMessage('reloaded', {}, function () {});
    extension.onMessage.addListener('executeActions', function (msg, sendResponse) {
        openedByEase = true;
        if (msg.actions[msg.step].action != "overlay" && msg.step > 0) {
            for (var i = msg.step - 1; i >= 0; i--) {
                if (msg.actions[i].action == "overlay") {
                    actions.overlay(msg.actions[i], function () {});
                    break;
                }
            }
        }
        executeActions(msg, function (res) {
            sendResponse(res);
            setTimeout(function () {
                openedByEse = false;
            }, 3000);
        });
    });
    extension.onMessage.addListener('removeOverlay', function (msg, sendResponse) {
        overlay.remove();
    });
} else {
    parentframe.onMessage.addListener("executeOnFrame", function (msg, sendResponse) {
        openedByEase = true;
        executeActions(msg, function (res) {
            sendResponse(res);
            setTimeout(function () {
                openedByEse = false;
            }, 3000);
        });
    });
}
