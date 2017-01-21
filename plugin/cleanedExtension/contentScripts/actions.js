function sendKey(input, key) {
    var e = input.ownerDocument.createEvent("KeyboardEvent");
    // FIREFOX : e.initKeyEvent("keydown", 1, 1, null, 0, 0, 0, 0, key, 0)
    e.initKeyboardEvent("keydown", 1, 1, document.defaultView, 0, 0, 0, 0, key, key);
    var f = input.dispatchEvent(e);
    //FIREFOX f && (e = input.ownerDocument.createEvent("KeyboardEvent"), e.initKeyEvent("keypress", 1, 1, null, 0, 0, 0, 0, key, 0), f = input.dispatchEvent(e));
    e = input.ownerDocument.createEvent("KeyboardEvent");
    //FIREFOX e.initKeyEvent("keyup", 1, 1, null, 0, 0, 0, 0, key, 0)
    e.initKeyboardEvent("keyup", 1, 1, null, 0, 0, 0, 0, key, key);
    input.dispatchEvent(e);
}

function fire_before_fill(a) {
    sendKey(a, 16); //shift
    sendKey(a, 32); //space
    sendKey(a, 8); //backspace
}

function fire_onchange(a) {
    var d = a.ownerDocument.createEvent("Events");
    d.initEvent("change", !0, !0);
    a.dispatchEvent(d);
    d = a.ownerDocument.createEvent("Events");
    d.initEvent("input", !0, !0);
    a.dispatchEvent(d);
}

var actions = {
    fillThenSubmit: function (actionStep, nextAction, returnToBackground) {
        var loginInput = $(actionStep.login);
        var passwordInput = $(actionStep.password);
        if (loginInput.length == 0) {
            if (actionStep.grave == true) {
                returnToBackground("error: input not found");
            } else {
                nextAction();
            }
        } else {
            loginInput.click();
            loginInput.val(actionStep.loginValue);
            loginInput.blur();
            passwordInput.click();
            passwordInput.val(actionStep.passwordValue);
            passwordInput.blur();
            $("body").append("<script type='text/javascript'>$('" + actionStep.login + "').change(); $('" + actionStep.password + "').change();</script>");
            $(actionStep.submit).click();
            nextAction();
        }
    },
    erasecookies: function (actionStep, nextAction, returnToBackground) {
        var name = actionStep.search;
        function deleteCookie(cookieName) {
            var domain = document.domain;
            var path = "/";
            document.cookie = cookieName + "=; expires=" + +new Date + "; domain=" + domain + "; path=" + path;
        }

        if (name) {
            deleteCookie(name)
        } else {
            var cookies = document.cookie.split(";");
            for (var i = 0; i < cookies.length; i++) {
                var cookie = cookies[i];
                var eqPos = cookie.indexOf("=");
                var name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
                deleteCookie(name)
            }
        }
        nextAction();

    },
    waitfor: function (actionStep, nextAction, returnToBackground) {

        var div = actionStep.search;
        var time = actionStep.time;
        if (!time) {
            time = 100;
        }
        var iteration = 0;

        function waitfor() {
            if (typeof div === 'string') {
                div = [div];
            }
            var absent = true;
            for (var i in div) {
                var obj = $(div[i]);
                if (obj.length > 0) {
                    absent = false;
                    break;
                }
            }
            if (iteration % 20 == 0) {
                console.log("-- waiting for element " + div[0] + " --");
            }
            if (iteration > 100) {
                returnToBackground("error: cant find element");
            } else if (absent) {
                setTimeout(function () {
                    iteration++;
                    waitfor();
                }, time);
            } else {
                nextAction();
            }
        }
        waitfor();
    },
    setattr: function (actionStep, nextAction, returnToBackground) {
        var input = $(actionStep.search);
        if (input.length == 0) {
            if (actionStep.grave == true) {
                returnToBackground("error: element not found");
            } else {
                nextAction();
            }
        } else {
            input.attr(actionStep.attr, actionStep.content);
            nextAction();
        }
    },
    simulateKeyPress: function (actionStep, nextAction, returnToBackground) {
        var input = $(actionStep.search);
        if (input.length == 0) {
            if (actionStep.grave == true) {
                returnToBackground("error: " + actionStep.what + " input not found");
            } else {
                nextAction();
            }
        } else {
            input.click();
            input.focus();
            var e = jQuery.Event("keydown");
            e.which = actionStep.keyCode;
            e.keyCode = actionStep.keyCode;
            input.trigger(e);
            e = jQuery.Event("keypress");
            e.which = actionStep.keyCode;
            e.keyCode = actionStep.keyCode;
            input.trigger(e);
            input.val(input.val() + String.fromCharCode(e.which));
            e = jQuery.Event("keyup");
            e.which = actionStep.keyCode;
            e.keyCode = actionStep.keyCode;
            input.trigger(e);
            nextAction();
        }
    },
    fill: function (actionStep, nextAction, returnToBackground) {
        var input = $(actionStep.search);
        if (input.length == 0) {
            if (actionStep.grave == true) {
                returnToBackground("error: input not found");
            } else {
                nextAction();
            }
        } else {
            input.select();
            input.click();
            input[0].focus();
            fire_before_fill(input[0]);
            input[0].value = actionStep.what;
            fire_onchange(input[0]);
            input[0].blur();
            nextAction();
        }
    },
    val: function (actionStep, nextAction, returnToBackground) {
        var input = $(actionStep.search);
        if (input.length == 0) {
            if (actionStep.grave == true) {
                returnToBackground("error: " + actionStep.what + " input not found");
            } else {
                nextAction();
            }
        } else {
            input.val(actionStep.what);
            nextAction();
        }
    },
    checkIfPopup: function (actionStep, nextAction, returnToBackground) {
        /*var popupButton = document.createElement("button");
        popupButton.id = "testtest23";
         document.body.appendChild(popupButton);
        $("#testtest23").click(function(){window.open('http://www.zebest3000.com','lenomdusite','width=300, height=250'); return false;});

        $("#testtest23").click();*/

        nextAction();
    },
    click: function (actionStep, nextAction, returnToBackground) {
        var button = $(actionStep.search);
        if (button.length == 0) {
            if (actionStep.grave == true) {
                returnToBackground("error: button not found");
            } else {
                nextAction();
            }
        } else {
            button.prop("disabled", false);
            window.setTimeout(function () {
                button.click();
            }, 250);
            nextAction();
        }
    },
    clickona: function (actionStep, nextAction, returnToBackground) {
        var button = $(actionStep.search);
        if (button.length == 0) {
            if (actionStep.grave == true) {
                returnToBackground("error: button not found");
            } else {
                nextAction();
            }
        } else {
            button.prop("disabled", false);
            button.get(0).click();
            button.click();
            nextAction();
        }
    },
    aclick: function (actionStep, nextAction, returnToBackground) {
        var button = $(actionStep.search);
        if (button.length == 0) {
            if (actionStep.grave == true) {
                returnToBackground("error: link not found");
            } else {
                nextAction();
            }
        } else {
            window.location.href = button.attr('href');
            returnToBackground("done");
        }
    },
    submit: function (actionStep, nextAction, returnToBackground) {
        var form = $(actionStep.search);
        if (form.length == 0) {
            if (actionStep.grave == true) {
                returnToBackground("error: connection form not found");
            } else {
                nextAction();
            }
        } else {
            form.submit();
            nextAction();
        }
    },
    wait: function (actionStep, nextAction, returnToBackground) {
        setTimeout(function () {
            nextAction();
        }, actionStep.timeout);
    },
    waitload: function (actionStep, nextAction, returnToBackground) {
        returnToBackground("done");
    },
    search: function (actionStep, nextAction, returnToBackground) {
        var obj = $(actionStep.search);
        alert("Found: " + obj.length + " search: " + actionStep.search);
        nextAction();
    },
    goto: function (actionStep, nextAction, returnToBackground) {
        window.location.href = actionStep.url;
        returnToBackground("done");
    },
    overlay: function(actionStep, nextAction, returnToBackground){
        //
        //*********
        //*******************
        //TODO TODO TODO TODO
        //*******************
        //*********
        //
        nextAction();
    }
};

function executeActions(msg, sendResponse) {
    if (msg.step >= msg.actions) {
        msg.status = "done";
        sendResponse(msg);
        return;
    }
    console.log("-- Ease action : " + msg.actions[msg.step].action + " --");
    actions[msg.actions[msg.step].action](msg.actions[msg.step], function () {
        msg.step++;
        executeActions(msg, sendResponse);
    }, function (status) {
        msg.status = status;
        msg.step++;
        sendResponse(msg);
    });
}

function getHost(url) {
    var getLocation = function (href) {
        var l = document.createElement("a");
        l.href = href;
        return l;
    };
    return getLocation(url).hostname;
}
