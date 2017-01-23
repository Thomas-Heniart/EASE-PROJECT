extension.runtime.onMessage.addListener("NewConnection", function (msg, easeTab, sendResponse) {
    extension.windows.getFromTab(easeTab, function (win) {
        extension.ease.getTabs(win, function (res) {
            if (res.length > 1) {
                connect(easeTab, msg);
            } else {
                connect(null, msg);
            }
        });
    });
});

extension.runtime.onMessage.addListener("Logout", function (msg, easeTab, sendResponse) {
    globalLogout(easeTab);
});

extension.runtime.onMessage.addListener("NewLinkToOpen", function (msg, easeTab, sendResponse) {
    extension.windows.getFromTab(easeTab, function (win) {
        extension.ease.getTabs(win, function (res) {
            if (res.length > 1) {
                extension.tabs.update(easeTab, msg.url, function (tab) {});
            } else {
                extension.tabs.create(win, msg.url, msg.highlight, function (tab) {

                });
            }
        });
    });
});
