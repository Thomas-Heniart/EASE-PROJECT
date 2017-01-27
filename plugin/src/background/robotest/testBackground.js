var timeout = 10000;

extension.runtime.onMessage.addListener("TestConnection", function (msg, senderTab, sendResponse) {
    extension.windows.getFromTab(senderTab, function (win) {
        singleTest(msg, win);
    });
});

function singleTest(msg, window) {
    var resultRow = msg.detail[msg.detail.length - 1].website.name;
    if (typeof msg.detail[msg.detail.length - 1].logWith === "undefined") {
        resultRow += "-login-" + msg.detail[msg.detail.length - 1].user.login;
    } else {
        resultRow += "-loginwth-" + msg.detail[msg.detail.length - 1].logWith;
    }
    msg.resultRow = resultRow;
    storage.testResults.set(msg.resultRow, "> " + msg.detail[msg.detail.length - 1].website.name + " : Initialize test");
    sendTestResults();
    startTest(msg, window, function (tab) {
        extension.tabs.close(tab);
    });

}

extension.runtime.onMessage.addListener("TestMultipleConnections", function (msg, senderTab, sendResponse) {
    results = {};
    extension.windows.getFromTab(senderTab, function (win) {
        multipleTests(msg, win, 0);
    });
});

function multipleTests(msg, window, i) {
    var websiteMsg = {};
    if (i < msg.detail.length) {
        websiteMsg.detail = msg.detail[i];
        var resultRow = websiteMsg.detail[websiteMsg.detail.length - 1].website.name;
        if (typeof websiteMsg.detail[websiteMsg.detail.length - 1].logWith === "undefined") {
            resultRow += "-login-" + websiteMsg.detail[websiteMsg.detail.length - 1].user.login;
        } else {
            resultRow += "-loginwth-" + websiteMsg.detail[websiteMsg.detail.length - 1].logWith;
        }
        websiteMsg.resultRow = resultRow;
        storage.testResults.set(websiteMsg.resultRow, "> " + websiteMsg.detail[websiteMsg.detail.length - 1].website.name + " : Initialize test");
        sendTestResults();
        console.log("test start : "+ websiteMsg.detail[websiteMsg.detail.length-1].website.home)
        startTest(websiteMsg, window, function (tab) {
            extension.tabs.close(tab);
            multipleTests(msg, window, i + 1);
        });
    } else {
        //TODO
    }

}

function startTest(msg, window, callback) {
    msg.bigStep = 0;
    startBigStepTest(null, window, msg, callback);
}

function startBigStepTest(tab, window, msg, callback) {
    var home = msg.detail[msg.bigStep].website.home;
    if (typeof home == "object") {
        msg.detail[msg.bigStep].website.home = home.http + msg.detail[0].user[home.subdomain] + "." + home.domain;
    }
    if (tab == null) {
        extension.tabs.create(window, msg.detail[msg.bigStep].website.home, msg.highlight, function (tab) {
            executeBigStepTest(tab, msg, callback);
        });
    } else {
        extension.tabs.update(tab, msg.detail[msg.bigStep].website.home, function (tab) {
            executeBigStepTest(tab, msg, callback);
        });
    }

}

function executeBigStepTest(tab, msg, callback) {
    extension.tabs.onReloaded.addListener(tab, function reloadListener1(tab) {
        extension.tabs.onReloaded.removeListener(reloadListener1);
        generateSteps("checkAlreadyLogged", "checkAlreadyLogged", msg.detail[msg.bigStep], function (actionsCheckWhoIsConnected) {
            executeSteps(tab, actionsCheckWhoIsConnected, function (tab, response) {
                generateSteps("switchOrLogout", null, msg.detail[msg.bigStep], function (actionSteps) {
                    doTest(tab, msg, actionSteps, callback);
                });
            }, function (tab, response) {
                doTest(tab, msg, [], callback);
            });
        });

    });
}

function doTest(tab, msg, actionSteps, callback) {
    if (msg.detail[msg.bigStep].logWith) {
        var mainAction = msg.detail[msg.bigStep].logWith;
    } else {
        var mainAction = "connect";
    }
    generateSteps(mainAction, "connect", msg.detail[msg.bigStep], function (connectSteps) {
        actionSteps = actionSteps.concat(connectSteps);
        executeSteps(tab, actionSteps, function (tab, response) {
            console.log("connnection done");
            if (msg.bigStep + 1 < msg.detail.length) {
                console.log("next big step");
                nextBigStepTest(tab, msg, callback);
            } else {
                console.log("test all");
                setTimeout(function () {
                    afterFirstCo();
                }, timeout);

                function afterFirstCo() {
                    generateSteps("checkAlreadyLogged", "checkAlreadyLogged", msg.detail[msg.bigStep], function (checkCoSteps) {
                        generateSteps("switchOrLogout", null, msg.detail[msg.bigStep], function (switchOrLogoutSteps) {
                            actionSteps = [{
                                "action": "goto",
                                "url": msg.detail[msg.bigStep].website.home
                            }];
                            actionSteps = actionSteps.concat(checkCoSteps);
                            actionSteps = actionSteps.concat(switchOrLogoutSteps);
                            actionSteps = actionSteps.concat(connectSteps);
                            executeSteps(tab, actionSteps, function (tab, response) {
                                console.log("first steps done");
                                setTimeout(function () {
                                    afterSecondCo();
                                }, timeout);

                                function afterSecondCo() {
                                    generateSteps("logout", "logout", msg.detail[msg.bigStep], function (logoutSteps) {
                                        actionSteps = [{
                                            "action": "goto",
                                            "url": msg.detail[msg.bigStep].website.home
                                        }];
                                        actionSteps = actionSteps.concat(checkCoSteps);
                                        actionSteps = actionSteps.concat(logoutSteps);
                                        executeSteps(tab, actionSteps, function (tab, response) {
                                            console.log("second steps done");
                                            setTimeout(function () {
                                                afterLogout();
                                            }, timeout);

                                            function afterLogout() {
                                                actionSteps = [{
                                                    "action": "goto",
                                                    "url": msg.detail[msg.bigStep].website.home
                                                }];
                                                actionSteps = actionSteps.concat(checkCoSteps);
                                                executeSteps(tab, actionSteps, function (tab, response) {
                                                    endTest(tab, false, "logout", msg, callback);
                                                }, function (tab, response) {
                                                    console.log("last steps done");
                                                    endTest(tab, true, "", msg, callback);
                                                });
                                            }
                                        }, function (tab, response) {
                                            endTest(tab, false, "reconnection or logout", msg, callback);
                                        });
                                    });
                                }
                            }, function (tab, response) {
                                endTest(tab, false, "connection or reconnection", msg, callback);
                            });
                        });
                    });
                }
            }
        }, function (tab, response) {
            endTest(tab, false, "connection", msg, callback);
        });
    });
}

function nextBigStepTest(tab, msg, callback) {
    msg.bigStep++;
    extension.tabs.onReloaded.addListener(tab, function waitForNextBigStep() {
        extension.tabs.onReloaded.removeListener(waitForNextBigStep);
        startBigStepTest(tab, null, msg, callback);
    });
}

function sendTestResults() {
    extension.ease.getTabs(null, function (tabs) {
        for (var j in tabs) {
            var tab = tabs[j];
            extension.tabs.sendMessage(tab, "printResults", storage.testResults.getDatas(), function () {});
        }
    });
}

function addTestResult(success, type, msg){
    var website = msg.detail[msg.detail.length-1].website.name;
    if (typeof msg.detail[msg.detail.length-1].logWith === "undefined") {
        var connectionType = "classic connection (login : "+msg.detail[msg.detail.length-1].user.login+")";
    } else {
        var connectionType = "connection with "+ msg.detail[msg.detail.length-1].logWith;
    }
    if(success){
         storage.testResults.set(msg.resultRow, "> "+website + " : SUCCESS connection, logout and reconnection for "+connectionType);
    } else {
        if(type != "logout")
           storage.testResults.set(msg.resultRow, "> "+website + " : FAIL "+type+" for "+connectionType);
        else
            storage.testResults.set(msg.resultRow, "> "+website + " : FAIL "+type);
    }
    sendTestResults();
}

function endTest(tab, success, type, msg, callback) {
    addTestResult(success, type, msg);
    callback(tab);
}
