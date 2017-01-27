//GENERE LES STEPS A PARTIR D'UN BIGSTEP ET D'UNE ACTION
//Si overlay == null, prend l'overlay par défaut de l'action
function generateSteps(action, overlay, bigStep, callback) {
    var steps = [];
    if (action == "switchOrLogout") {
        if (bigStep.website.switch) {
            generateSteps("switch", overlay, bigStep, callback);
            return;
        } else {
            generateSteps("logout", overlay, bigStep, callback);
            return;
        }
    } else {
        var overlayStep = generateOverlay(action, overlay);
        if (overlayStep) {
            steps.push(overlayStep);
        }
        if (action == "checkAlreadyLogged" && Array.isArray(bigStep.website[action])) {
            var createTodo = {
                "todo": [{
                    "action": "check",
                    "type": "hasElement",
                    "search": bigStep.website[action][0].search
                    }]
            };
            bigStep.website[action] = createTodo;
        }
        var todoList = JSON.parse(JSON.stringify(bigStep.website[action].todo));

        function appendStep(i) {
            if (i >= todoList.length) {
                callback(steps);
                return;
            }
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
                    todoList.splice(j, 1);
                }
                bigStep.website.inFrame.todo.push({
                    "action": "exitFrame"
                });
                todoList.splice(j, 1);
                generateSteps("inFrame", false, bigStep, function (stepsOfFrame) {
                    todoList[i].todo = stepsOfFrame;
                    steps.push(todoList[i]);
                    i++;
                    appendStep(i);
                });
            } else {
                steps.push(todoList[i]);
                i++;
                appendStep(i);
            }
        }
        appendStep(0);
    }
}

//GENERE LE STEP OVERLAY
function generateOverlay(action, overlay) {
    if (overlay == false){
        return false;
    } else if (overlay == null) {
        return {
            "action": "overlay",
            "type": "classic",
            "info": action
        };
    } else if (overlay.indexOf("scrap") == 0) {
        return {
            "action": "overlay",
            "type": "scrap",
            "info": overlay.substring(5, overlay.length)
        };
    } else {
        return {
            "action": "overlay",
            "type": "classic",
            "info": overlay
        };
    }
}

//EXECUTE LES STEPS
function executeSteps(tab, actionSteps, successCallback, failCallback) {
    var toSend = {
        "actions":actionSteps,
        "step":0
    };
    function sendActions(tab) {
        extension.tabs.sendMessage(tab, "executeActions", toSend, function (response) {
            if (response) {
                toSend.step = response.step;
                if (response.status.indexOf("error") == 0) {
                    extension.tabs.onReloaded.removeListener(sendActions);
                    failCallback(tab, response);
                } else if (toSend.step >= actionSteps.length) {
                    extension.tabs.onReloaded.removeListener(sendActions);
                    successCallback(tab, response);
                }
            }
        });
    }
    sendActions(tab);
    extension.tabs.onReloaded.addListener(tab, sendActions);
}
