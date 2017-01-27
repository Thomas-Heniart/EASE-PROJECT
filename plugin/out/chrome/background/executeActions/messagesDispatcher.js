//RECOIT LES MESSAGES DE EASELISTENER ET APPELLE LES FONCTIONS CORRESPONDANTES

extension.runtime.onMessage.addListener("NewConnection", function (msg, easeTab, sendResponse) {
    extension.windows.getFromTab(easeTab, function (win) {
        extension.ease.getTabs(win, function (res) {
            if (res.length < 2 || msg.detail.highlight == false) {
                connect(null, msg);
            } else {
                connect(easeTab, msg);
            }
        });
    });
});

extension.runtime.onMessage.addListener("GlobalLogout", function (msg, easeTab, sendResponse) {
    globalLogout(easeTab);
});

extension.runtime.onMessage.addListener("NewLinkToOpen", function (msg, easeTab, sendResponse) {
    extension.windows.getFromTab(easeTab, function (win) {
        extension.ease.getTabs(win, function (res) {
            if (res.length < 2 || msg.detail.highlight == false) {
                extension.tabs.create(win, msg.url, msg.highlight, function (tab) {});
            } else {
                extension.tabs.update(easeTab, msg.url, function (tab) {});
            }
        });
    });
});
