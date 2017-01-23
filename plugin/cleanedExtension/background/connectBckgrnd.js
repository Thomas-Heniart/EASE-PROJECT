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
        console.log("reloaded");
        generateSteps("checkAlreadyLogged", msg.detail[msg.bigStep], function (actionsCheckWhoIsConnected) {
            console.log(actionsCheckWhoIsConnected);
            executeSteps(tab, actionsCheckWhoIsConnected, function (tab, response) {
                var actionSteps = [];
                extension.storage.get("lastConnections", function (lastConnections) {
                    var user = "";
                    if (response.user) {
                        user = response.user;
                    } else if (lastConnections && lastConnections[getHost(msg.detail[msg.bigStep].website.home)]) {
                        user = lastConnections[getHost(msg.detail[msg.bigStep].website.home)].user;
                    }
                    if (user == msg.detail[0].user.login) {
                        nextBigStep(tab, msg);
                    } else {
                        generateSteps("switchOrLogout", msg.detail[msg.bigStep], function (addedSteps) {
                            actionSteps.concat(addedSteps);
                            doConnect(tab, msg, actionSteps);
                        });
                    }
                });
            }, function (tab, response) {
                doConnect(tab, msg, [])
            });

            function doConnect(tab, msg, actionSteps) {
                if (msg.detail[msg.bigStep].logWith) {
                    var mainAction = msg.detail[msg.bigStep].logWith;
                } else {
                    var mainAction = "connect";
                }
                generateSteps(mainAction, msg.detail[msg.bigStep], function (addedSteps) {
                    actionSteps.concat(addedSteps);
                    executeSteps(tab, actionSteps, function (tab, response) {
                        nextBigStep(tab, msg);
                    }, function (tab, response) {
                        console.log(response);
                        endConnection(tab);
                    });
                });

            }
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
    extension.tabs.sendMessage(tab, "removeOverlay", {}, function (res) {});
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
        rememberConnection(website.lastLogin.user, getHost(website.loginUrl), true);
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
