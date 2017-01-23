function checkWhoIsConnected(todoList, sendResponse) {
    executeActions(todoList, function(res){
        if(res.status == "done"){
            
        }
    });
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
    executeActions(todoList) {
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
