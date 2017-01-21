function globalLogout(easeTab) {
    extension.storage.get("websitesToLogout", function (websitesToLogout) {
        extension.runtime.onMessage.addListener("Disconnected", function onLogout(msg, tab, sendResponse) {
            extension.runtime.onMessage.removeListener(onLogout);
            extension.tabs.sendMessage(easeTab, "logoutFrom", websitesToLogout, function () {});
        });
        for (var i in websitesToLogout) {
            logOutFrom(websitesToLogout[i], easeTab);
        }
        extension.storage.set("websitesToLogout", [], function () {});
    });
}

function logOutFrom(bigStepToLogout, easeTab) {
    extension.windows.getCurrent(function (currentWindow) {
        extension.tabs.create(currentWindow, msg.detail[0].website.home, false, function (tab) {
            extension.tabs.onReloaded.addListener(tab, function startLogout(tab) {
                extension.tabs.onReloaded.removeListener(startLogout);
                    executeSteps(tab, generateSteps("logout", bigStepToLogout), endLogout, endLogout);
            });
        });
    });
}

function endLogout(tab){
    extension.tabs.close(tab);
}
