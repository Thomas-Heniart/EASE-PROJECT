//LE GLOBAL LOGOUT COMMENCE ICI
//Fait le logout de chaque site et clear le storage des websites à logout.
function globalLogout(easeTab) {
    var toSend = [];
    for(var i=0;i<storage.websitesToLogout.length;i++){
        toSend.push(storage.websitesToLogout.get(i).website);
    }
    extension.tabs.onReloaded.addListener(easeTab, function onLogout(msg, tab, sendResponse) {
        extension.tabs.onReloaded.removeListener(onLogout);
        extension.tabs.sendMessage(easeTab, "logoutFrom", toSend, function () {});
    });
    //SEND CORRECT DATAS FOR LOGOUT OVERLAY
    for (var i = 0; i < storage.websitesToLogout.length; i++) {
        logOutFrom(storage.websitesToLogout.get(i), easeTab);
    }
    storage.websitesToLogout.clear();
}

//EXECUTE UN LOGOUT
//Crée le tab, génère les steps de checkAlreadyLogged et les execute
// Si personne est co, end
// Sinon génère les steps de Logout et les execute
function logOutFrom(bigStepToLogout, easeTab) {
    extension.windows.getCurrent(function (currentWindow) {
        extension.tabs.create(currentWindow, bigStepToLogout.website.home, false, function (tab) {
            extension.tabs.onReloaded.addListener(tab, function startLogout(tab) {
                extension.tabs.onReloaded.removeListener(startLogout);
                generateSteps("checkAlreadyLogged", "checkAlreadyLogged", bigStepToLogout, function (actionsCheckWhoIsConnected) {
                    executeSteps(tab, actionsCheckWhoIsConnected, function (tab, response) {
                        generateSteps("logout", "logout", bigStepToLogout, function(logoutSteps){
                            executeSteps(tab, logoutSteps, endLogout, endLogout);
                        });
                    }, function (tab, response) {
                        endLogout(tab);
                    });
                });

            });
        });
    });
}

function endLogout(tab) {
    setTimeout(function(){
        extension.tabs.sendMessage(easeTab, "logoutDone", {"siteSrc":website.siteSrc}, function (){});
        extension.tabs.close(tab);
    },1000)
}
