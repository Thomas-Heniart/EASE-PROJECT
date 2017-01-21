extension.runtime.onMessage("NewConnection", function (msg, easeTab, sendResponse) {
    connect(msg);
});

extension.runtime.bckgrndOnMessage("Logout", function (msg, easeTab, sendResponse) {
    globalLogout(easeTab);
});