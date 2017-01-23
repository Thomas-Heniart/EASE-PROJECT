function connect(tab, msg) {
    checkSkipFirstStep(msg, function (skipStep) {
        if (skipStep) {
            msg.bigStep = 1;
        } else {
            msg.bigStep = 0;
        }
        executeBigStep(tab, msg);
    });
}

function checkSkipFirstStep(msg, callback) {
    if (msg.detail[0].website.name == "Facebook" && msg.detail[1]) { //Pour l'instant que FB. Faire pour linkedin aussi
        extension.storage.get("lastConnections", function (lastConnections) {
            if (lastConnections != undefined) {
                if (lastConnections["www.facebook.com"] && lastConnections["www.facebook.com"].user == msg.detail[0].user.login) {
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
        });
    } else {
        callback(false);
        return
    }
}

function executeBigStep(tab, msg) {
    var home = msg.detail[msg.bigStep].website.home;
    if (typeof home == "object") {
        msg.detail[msg.bigStep].website.home = home.http + msg.detail[0].user[home.subdomain] + "." + home.domain;
    }
    if (tab == null) {
        extension.windows.getCurrent(function (window) {
            extension.tabs.create(window, msg.detail[msg.bigStep].website.home, msg.highlight, function (tab) {
                startBigStep(tab, msg);
            });
        });
    } else {
        extension.tabs.update(tab, msg.detail[msg.bigStep].website.home, function (tab) {
            startBigStep(tab, msg);
        });
    }

}

function startBigStep(tab, msg) {
    extension.tabs.onReloaded.addListener(tab, function reloadListener1(tab) {
        extension.tabs.onReloaded.removeListener(reloadListener1);
        extension.tabs.sendMessage(tab, "checkWhoIsConnected", msg.detail[msg.bigStep].website, function (response) {
            var actionSteps = [];
            if (response.fail) {
                endConnection(tab);
                return;
            }
            if (response.connected) {
                if (response.user == msg.detail[0].user[login]) {
                    nextBigStep(tab, msg);
                } else {
                    actionSteps.concat(generateSteps("switchOfLogout", msg.detail[msg.bigStep]));
                }
            }
            if (msg.detail[msg.bigStep].logWith) {
                actionSteps.concat(generateSteps(msg.detail[msg.bigStep].logWith, msg.detail[msg.bigStep]));
            } else {
                actionSteps.concat(generateSteps("connect", msg.detail[msg.bigStep]));
            }

            executeSteps(tab, actionSteps, function (tab) {
                nextBigStep(tab, msg);
            }, endConnection);
        });
    });
}

function nextBigStep(tab, msg) {
    msg.detail[msg.bigStep].user = getUser(msg, msg.bigStep);
    rememberConnection(msg[msg.bigStep]);
    msg.bigStep++;
    if (msg.bigStep >= msg.detail.length) {
        endConnection(tab);
    } else {
        extension.tabs.onReloaded.addListener(tab, function waitForNextBigStep() {
            extension.tabs.onReloaded.removeListener(waitForNextBigStep);
            executeBigStep(tab, msg);
        });
    }
}

function endConnection(tab) {
    extension.tabs.sendMessage(tab, "removeOverlay", {}, function (reponse) {});
}

function rememberConnection(bigStep, callback) {
    extension.storage.get("websitesToLogout", function (websitesToLogout) {
        if (websitesToLogout) {
            for (var i in websitesToLogout) {
                if (websitesToLogout[i].website.name == bigStep.website.name) {
                    websitesToLogout.splice(i, 1);
                    break;
                }
            }
        } else {
            websitesToLogout = [];
        }
        websitesToLogout.push(bigStep);
        extension.storage.set("websitesToLogout", websitesToLogout, function () {});
    });
    /*if (website.lastLogin.logWith) {
        rememberDirectLogWithConnection(getHost(website.loginUrl), website.lastLogin);
    } else {
        rememberConnection(website.lastLogin.user, null, getHost(website.loginUrl), true);
    }*/
    //REVOIR LA SAUVEGARDE DES CONNECTIONS

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
