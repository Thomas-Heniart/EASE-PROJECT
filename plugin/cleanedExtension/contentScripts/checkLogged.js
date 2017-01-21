var checks = {
    hasElement: function (checkStep, success, fail) {
        if ($(checkStep.search).length > 0) {
            success();
        } else {
            fail();
        }
    },
    absentElement: function (checkStep, success, fail) {
        if ($(checkStep.search).lenght > 0) {
            fail();
        } else {
            success();
        }
    },
    hasCookie: function (checkStep, success, fail) {
        var cookies = document.cookie.split(";");
        for (var i in cookies) {
            if (cookies[i].indexOf(" ") == 0) {
                cookies[i] = cookies[i].substring(1, cookies[i].length);
            }
            if (cookies[i].split("=")[0] == checkStep.name) {
                success();
                return;
            }
        }
        fail();
    },
    matchUrl: function (checkStep, success, fail) {
        var urlPattern = checkStep.url;
        var regex = urlPattern.replace(/\*/g, "[^ ]*");
        if(regex.test(location.href)){
            success();
        } else {
            fail();
        }
    }
}

function checkWhoIsConnected(msg, sendResponse) {
    var todoList = {};
    todoList.step = 0;
    if (msg.checkAlreadyLogged.todo) {
        todoList.checks = msg.checkAlreadyLogged.todo;
    } else { // POUR LES ANCIENS JSONS
        todoList.checks = msg.checkAlreadyLogged;
        todoList.checks[0].check = "hasElement";
    }
    checkAlreadyLogged(todoList, function (result) {
        if (result.connected) {
            if (msg.checkUser.todo) {
                todoList.step = 0;
                todoList.checks = msg.checkUser.todo;
                checkUser(todoList, function (user) {
                    sendResponse({
                        "connected": true,
                        "user": user
                    });
                });
            } else [
                extension.storage.get( "lastConnections" , function (lastConnection) {
                    //TODO
                    //TODO
                    //TODO
                    //TODO
                    sendResponse({
                        "connected": true,
                        "user": user
                    });
                });
            ]
        } else {
            sendResponse(result); //send fail ou connected:false
        }
    });
}

function checkAlreadyLogged(todoList, callback) {
    if (todoList.step >= todoList.checks.length) {
        callback({
            connected: true
        });
    }
    if (todoList.checks[todoList.step].action) {
        actions[todoList.checks[todoList.step].action](todoList.checks[todoList.step], function () {
            todoList.step++;
            checkAlreadyLogged(todoList, callback);
        }, function (res) {
            if (res != "done")
                callback({
                    fail: true
                });
        });
    } else if (todoList.checks[todoList.step].check) {
        checks[todoList.checks[todoList.step].check](todoList.checks[todoList.step], function success() {
            todoList.step++;
            checkAlreadyLogged(todoList, callback);
        }, function fail() {
            callback({
                connected: false
            });
        });
    }
}

function checkUser(todoList, callback) {
    if (todoList.step >= todoList.checks.length) {
        callback("");
    }
    if (todoList.checks[todoList.step].action) {
        actions[todoList.checks[todoList.step].action](todoList.checks[todoList.step], function () {
            todoList.step++;
            checkAlreadyLogged(todoList, callback);
        }, function (res) {
            if (res != "done")
                callback({
                    fail: true
                });
        });
    } else if (todoList.checks[todoList.step].get) {
        getUserInDOM([todoList.checks[todoList.step]], callback);
    }
}

function getUserInDOM(infos, callback) {
    if (infos.get == "text") {
        callback($(infos.search).text());
    } else {
        callback($(infos.search).attr(infos.get));
    }
}
