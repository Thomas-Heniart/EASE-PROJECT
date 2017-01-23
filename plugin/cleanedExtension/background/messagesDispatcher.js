extension.runtime.onMessage("NewConnection", function (msg, easeTab, sendResponse) {
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

extension.runtime.bckgrndOnMessage("Logout", function (msg, easeTab, sendResponse) {
    globalLogout(easeTab);
});

extension.runtime.bckgrndOnMessage("NewLinkToOpen", function (msg, easeTab, sendResponse) {
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
