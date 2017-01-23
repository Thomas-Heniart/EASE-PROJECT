function addSteps(action, bigStep, previousSteps, callback) {
    var steps = [];
    if (action == "switchOrLogout") {
        if (bigStep.website.switch) {
            addSteps("switch", bigStep, previousSteps, callback);
            return;
        } else {
            addSteps("logout", bigStep, previousSteps, callback);
            return;
        }
    } else {
        if (action != "doInFrame") {
            var overlay = "connect";
            if (action == "logout" || action == "switch" || action == "checkAlreadyLogged") {
                overlay = action;
            }
            steps.push({
                "action": "overlay",
                "type": overlay
            });
        }
        if (action == "checkAlreadyLogged") {
            if (Array.isArray(bigStep.website[action])) {
                var createTodo = {
                    "todo": [{
                        "action": "check",
                        "type": "hasElement",
                        "search": bigStep.website[action][0].search
                    }]
                };
                bigStep.website[action] = createTodo;
            }
        }
        console.log(action);
        console.log(bigStep.website);
        var todoList = bigStep.website[action].todo;
        for (var i in todoList) {
            if (todoList[i].action == "fill") {
                todoList[i].what = bigStep.user[todoList[i].what];
            }
            if (todoList[i].action == "fillThenSubmit") {
                todoList[i].loginValue = bigStep.user.login;
                todoList[i].passwordValue = bigStep.user.password;
            }
            if (todoList[i].action == "val") {
                todoList[i].what = bigStep.user[todoList[i].what];
            }
            if (todoList[i].action == "goto" && typeof todoList[i].action.url == "object") {
                todoList[i].action.url = (todoList[i].action.url.http + bigStep.user[todoList[i].action.url.subdomain] + "." + todoList[i].action.url.domain);
            }
            if (todoList[i].action == "enterFrame") {
                var j = i + 1;
                bigStep.website.inFrame = {
                    "todo": []
                };
                while (todoList[j].action != "exitFrame" && j < todoList.length) {
                    bigStep.website.inFrame.todo.push(todoList[j]);
                    j++;
                }
                bigStep.website.inFrame.todo.push({
                    "action": "exitFrame"
                });
                todoList.splice(j, 1);
                todoList[i].todo = generateSteps("inFrame", bigStep);
            } else {

            }
            steps.push(todoList[i]);
        }
        previousSteps.concat(steps);
        callback(previousSteps);
    }

}

function executeSteps(tab, actionSteps, successCallback, failCallback) {
    var step = 0;
    console.log(actionSteps);

    function sendActions(tab) {
        extension.tabs.sendMessage(tab, "executeActions", {
            "actions": actionSteps,
            "step": step
        }, function (response) {
            if (response) {
                step = response.step;
                if (response.status.indexOf("error") == 0) {
                    failCallback(tab, response);
                } else if (step >= actionSteps.length) {
                    extension.tabs.onReloaded.removeListener(sendActions);
                    successCallback(tab, response);
                }
            } else {
                failCallback(tab, response);
            }

        });
    }
    extension.tabs.onReloaded.addListener(tab, sendActions);
}
