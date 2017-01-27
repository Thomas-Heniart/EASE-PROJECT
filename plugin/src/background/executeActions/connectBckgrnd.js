//LA CONNEXION COMMENCE ICI
//Regarde si on peut skip le premier bigStep (si déjà connecté à Facebook), puis execute le premier bigStep de la liste
function connect(tab, msg) {
    checkSkipFirstStep(msg, function (skipStep) {
        if (skipStep) {
            msg.bigStep = 1;
        } else {
            msg.bigStep = 0;
        }
        startBigStep(tab, msg);
    });
}

//CHECK SI ON PEUT SKIP LE PREMIER BIGSTEP QUAND CONNECT WITH FACEBOOK
//Pour l'instant que avec Fb. Faisable églament avec Linkedin, mais il faut mettre un listener sur linkedin pour voir quand le user se déco
function checkSkipFirstStep(msg, callback) {
    if (msg.detail[0].website.name == "Facebook" && msg.detail[1]) {
        if (storage.lastConnections.get("www.facebook.com").user == msg.detail[0].user.login) {
            callback(true);
            return
        } else {
            callback(false);
            return
        }
    } else {
        callback(false);
        return
    }
}

//START UN BIGSTEP
//Crée la tab si null passé en param, update la tab sinon, puis start le bigStep
function startBigStep(tab, msg) {
    var home = msg.detail[msg.bigStep].website.home;
    if (typeof home == "object") {
        msg.detail[msg.bigStep].website.home = home.http + msg.detail[0].user[home.subdomain] + "." + home.domain;
    }
    if (tab == null) {
        extension.windows.getCurrent(function (window) {
            extension.tabs.create(window, msg.detail[msg.bigStep].website.home, msg.highlight, function (tab) {
                executeBigStep(tab, msg);
            });
        });
    } else {
        extension.tabs.update(tab, msg.detail[msg.bigStep].website.home, function (tab) {
            executeBigStep(tab, msg);
        });
    }

}

//EXCUTE UN BIGSTEP EN ENTIER
//Génère les steps de checkAlreadyLogged, et les execute
//Si success (=qqn est connecté), vérifie le user. 
//  Si c'est le bon user, nextBigStep, return
//  Si c'est le mauvais user, génère les steps de logout (ou switch)
//Dans tous les cas, génère les steps de connect
//Execute les steps
//  Si success, nextBigStep
//  Sinon, endConnection
function executeBigStep(tab, msg) {
    extension.tabs.onReloaded.addListener(tab, function reloadListener1(tab) {
        extension.tabs.onReloaded.removeListener(reloadListener1);
        generateSteps("checkAlreadyLogged", "checkAlreadyLogged", msg.detail[msg.bigStep], function (actionsCheckWhoIsConnected) {
            executeSteps(tab, actionsCheckWhoIsConnected, function (tab, response) {
                var user = "";
                if (response.user) {// Si action getUser
                    user = response.user;
                } else if (storage.lastConnections.get(getHost(msg.detail[msg.bigStep].website.home))) {
                    user = storage.lastConnections.get(getHost(msg.detail[msg.bigStep].website.home)).user;
                }
                if (user == msg.detail[0].user.login) {
                    nextBigStep(tab, msg);
                } else {
                    generateSteps("switchOrLogout", null, msg.detail[msg.bigStep], function (actionSteps) {
                        doConnect(tab, msg, actionSteps);
                    });
                }
            }, function (tab, response) {
                doConnect(tab, msg, [])
            });

            function doConnect(tab, msg, actionSteps) {
                if (msg.detail[msg.bigStep].logWith) {
                    var mainAction = msg.detail[msg.bigStep].logWith;
                } else {
                    var mainAction = "connect";
                }
                generateSteps(mainAction, "connect", msg.detail[msg.bigStep], function (addedSteps) {
                    actionSteps = actionSteps.concat(addedSteps);
                    executeSteps(tab, actionSteps, function (tab, response) {
                        nextBigStep(tab, msg);
                    }, function (tab, response) {
                        endConnection(tab);
                    });
                });

            }
        });
    });
}

//PASSE AU BIGSTEP SUIVANT
//Remember la connexion
//Si dernier step, endConnection, sinon, startBigStep suivant.
function nextBigStep(tab, msg) {
    msg.detail[msg.bigStep].user = getUser(msg, msg.bigStep);
    rememberConnection(msg.detail[msg.bigStep]);
    msg.bigStep++;
    if (msg.bigStep >= msg.detail.length) {
        endConnection(tab);
    } else {
        extension.tabs.onReloaded.addListener(tab, function waitForNextBigStep() {
            extension.tabs.onReloaded.removeListener(waitForNextBigStep);
            startBigStep(tab, msg);
        });
    }
}

//END CONNECTION - remove l'overlay
function endConnection(tab) {
    extension.tabs.sendMessage(tab, "removeOverlay", {}, function (res) {});
}

//REMEMBER CONNECTION
//Enregistre le bigStep dans les sites à Logout
//Enregistre le bigStep dans les lastConnections
function rememberConnection(bigStep, callback) {
    for (var i = 0; i < storage.websitesToLogout.length; i++) {
        if (storage.websitesToLogout.get(i).website.name == bigStep.website.name) {
            storage.websitesToLogout.remove(i);
            break;
        }
    }
    bigStep.user.password = undefined;
    storage.websitesToLogout.push(bigStep);
    if (bigStep.user.logWith) {
        rememberDirectLogWithConnection(getHost(bigStep.website.loginUrl), {"user":bigStep.user.login, "logWith":bigStep.user.logWith});
    } else {
        rememberClassicConnection(bigStep.user.login, getHost(bigStep.website.loginUrl), true);
    }
}

function getUser(msg, i) {
    if (msg.detail[i].user) {
        msg.detail[i].user.password = undefined;
        return msg.detail[i].user;
    } else if (msg.detail[i].logWith) {
        var res = getUser(msg, i - 1);
        res.logWith = getHost(msg.detail[i - 1].website.loginUrl);
        return res;
    }
}
