function checkIsConnected(msg, connected, unconnected, i) {
    var length = $(msg.detail[msg.bigStep].website.checkAlreadyLogged).length;
    if (i >= length) {
        connected();
    } else {
        if (msg.detail[msg.bigStep].website.checkAlreadyLogged[i].action === undefined) {
            var object = $(msg.detail[msg.bigStep].website.checkAlreadyLogged[i].search);
            var type = msg.detail[msg.bigStep].website.checkAlreadyLogged[i].type;
            if (type === "absent") {
                if (object.length > 0)
                    unconnected();
                else {
                    if (i === length - 1)
                        connected();
                }
            } else if (type != "absent" && object.length <= 0) {
                unconnected();
            } else {
                checkIsConnected(msg, connected, unconnected, i + 1);
            }
        } else if (msg.detail[msg.bigStep].website.checkAlreadyLogged[i].action === "waitfor") {
            var time = msg.detail[msg.bigStep].website.checkAlreadyLogged[i].time;
            if (!time) {
                time = 100;
            }
            var iteration = 0;
            var waitfor = setInterval(function () {
                var obj = $(msg.detail[msg.bigStep].website.checkAlreadyLogged[i].search);
                if (obj.length) {
                    checkIsConnected(msg, connected, unconnected, i + 1);
                    clearInterval(waitfor);
                } else {
                    if (iteration > 100)
                        unconnected();
                }
                iteration++;
            }, 100);
            /* function waitfor() {
                var obj = $(msg.detail[msg.bigStep].website.checkAlreadyLogged[i].search);
                if (obj.length > 0) {
                    checkIsConnected(msg, connected, unconnected, i + 1);
                } else {
                    setTimeout(function () {
                        iteration++;
                        if (iteration > 100) {
                            unconnected();
                        } else {
                            waitfor();
                        }
                    }, time);
                }
            }
            waitfor(); */
        } else if (msg.detail[msg.bigStep].website.checkAlreadyLogged[i].action === "search") {
            var actionStep = msg.detail[msg.bigStep].website.checkAlreadyLogged[i];
            var obj = $(actionStep.search);
            alert("Found: " + obj.length + " search: " + actionStep.search);
            checkIsConnected(msg, connected, unconnected, i + 1);
        } else {
            unconnected();
        }
    }
}


function isConnected(msg) {
    console.log("-- Ease action : checking connnection --");
    var object = $(msg.detail[msg.bigStep].website.checkAlreadyLogged[0].search);
    if (object.length === 0) {
        console.log("-- Ease action : not connected Intialize connection --");
        return false;
    }
    console.log("-- Ease action : connected. Intialize logout --");
    return true;
}

function whoIsConnected(msg) {
    console.log("-- Ease action : checking connnection --");

}

function getNewLogin(msg, i) {
    if (msg.detail[i].user) {
        return {"user": msg.detail[i].user.login, "password": msg.detail[i].user.password};
    } else if (msg.detail[i].logWith) {
        return {"user": getNewLogin(msg, i - 1).user, "logWith": getHost(msg.detail[i - 1].website.loginUrl)};
    }
}

function alreadyVisited(msg) {
    var lastConnection = msg.lastConnections[getHost(msg.detail[msg.bigStep].website.loginUrl)];
    if (lastConnection) {
        var NewLogin = getNewLogin(msg, msg.bigStep);
        if (NewLogin.logWith) {
            if (NewLogin.user == lastConnection.user && NewLogin.logWith == lastConnection.logWith)
                return true
        } else {
            if (lastConnection.user) {
                if (lastConnection.user == getNewLogin(msg, msg.bigStep).user)
                    return true;
            } else {
                if (lastConnection == getNewLogin(msg, msg.bigStep).user)
                    return true;
            }
        }
    }
    return false;
}

if (window.top === window) {

    extension.runtime.onMessage("goooo", function (msg, sendResponse) {
        console.log("goooo");
        /* $("<input type=\"password\" " +
            "style=\"position: absolute; border:none;width:0px;height:0px;background-color:white;overflow: hidden; opacity: 0;\" " +
            "onfocus=\"$(this).next().focus()\" " +
            "/>").insertBefore($("input[type='password']")); */
        if (msg.todo == "checkAlreadyConnected") {
            checkConnectionOverlay(msg);
            checkIsConnected(msg, function () {
                if (alreadyVisited(msg) == true) {
                    msg.todo = "connect";
                    msg.actionStep = msg.detail[msg.bigStep].website[msg.todo].todo.length;
                    msg.type = "completed";
                    sendResponse(msg);
                } else {
                    msg.waitreload = true;
                    msg.todo = "logout";
                    doThings(msg, sendResponse);
                    logoutOverlay(msg);
                }
            }, function () {
                msg.waitreload = true;
                if (typeof msg.detail[msg.bigStep].logWith === "undefined") {
                    msg.todo = "connect";
                } else {
                    msg.todo = msg.detail[msg.bigStep].logWith;
                }
                doThings(msg, sendResponse);
                if (msg.todo == "logout") {
                    logoutOverlay(msg);
                } else if (msg.todo == "connect") {
                    loginOverlay(msg);
                }
            }, 0);
        } else if (msg.todo == "end") {
            endOverlay();
            msg.type = "completed";
            sendResponse(msg);
        } else if (msg.todo == "nextBigStep") {
            msg.type = "completed";
            sendResponse(msg);
        }
        else {
            doThings(msg, sendResponse);
            if (msg.todo == "logout") {
                logoutOverlay(msg);
            } else if (msg.todo == "connect") {
                loginOverlay(msg);
            }
        }
    });
}