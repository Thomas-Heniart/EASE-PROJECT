if (window.top === window) {
    console.log("waiting for messages");
    extension.runtime.sendMessage('reloaded', {}, function () {});
    extension.runtime.onMessage.addListener('executeActions', function (msg, sendResponse) {
        console.log(msg);
        if (msg.actions[msg.step].action != "overlay" && msg.step > 0) {
            for (var i = msg.step - 1; i >= 0; i--) {
                if (msg.actions[i].action == "overlay") {
                    actions.overlay(msg.actions[i], function () {
                        executeActions(msg, sendResponse);
                    });
                    break;
                }
            }
        } else {
            executeActions(msg, sendResponse);
        }
    });
    extension.runtime.onMessage.addListener('removeOverlay', function (msg, sendResponse) {
        //remove overlay
    });
} else {
    window.addEventListener("message", function (event) {
        if (event.origin == "Ease" && event.data.name == "executeOnFrame") {
            executeActions(msg, function (msg) {
                event.source.postMessage({
                    "name": "executeOnFrameResponse",
                    "msg": msg
                }, event.origin)
            });
        }

    }, false);
}
