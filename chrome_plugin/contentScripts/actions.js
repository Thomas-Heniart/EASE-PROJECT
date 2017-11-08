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
    fillThenSubmit: function (msg, callback, sendResponse) {
        var actionStep = msg.detail[msg.bigStep].website[msg.todo].todo[msg.actionStep];
        var loginInput = $(actionStep.login);
        var passwordInput = $(actionStep.password);
        if (loginInput.length == 0) {
            if (actionStep.grave == true) {
                msg.type = "error: " + actionStep.what + " input not found";
                sendResponse(msg);
                errorOverlay(msg);
            } else {
                msg.actionStep++;
                callback(msg, sendResponse);
            }
        } else {
            loginInput.click();
            loginInput.val(msg.detail[0].user.login);
            loginInput.blur();
            passwordInput.click();
            passwordInput.val(msg.detail[0].user.password);
            passwordInput.blur();
            $("body").append("<script type='text/javascript'>$('" + actionStep.login + "').change(); $('" + actionStep.password + "').change();</script>");
            $(actionStep.submit).click();

            msg.actionStep++;
            callback(msg, sendResponse);
        }
    },
    erasecookies: function (msg, callback, sendResponse) {

        var name = msg.detail[msg.bigStep].website[msg.todo].todo[msg.actionStep].search;

        function deleteCookie(cookieName) {
            var domain = document.domain;
            var path = "/";
            document.cookie = cookieName + "=; expires=" + +new Date + "; domain=" + domain + "; path=" + path;
        }

        if (name) {
            deleteCookie(name)
        }
        else {
            var cookies = document.cookie.split(";");
            for (var i = 0; i < cookies.length; i++) {
                var cookie = cookies[i];
                var eqPos = cookie.indexOf("=");
                var name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
                deleteCookie(name)
            }
        }
        msg.actionStep++;
        callback(msg, sendResponse);

    },
    waitfor: function (msg, callback, sendResponse) {

        var div = msg.detail[msg.bigStep].website[msg.todo].todo[msg.actionStep].search;
        var time = msg.detail[msg.bigStep].website[msg.todo].todo[msg.actionStep].time;
        var iteration = 0;
        var checkExist = setInterval(function () {
            iteration++;
            if ($(div).length) {
                console.log("Exists!");
                msg.actionStep++;
                callback(msg, sendResponse);
                clearInterval(checkExist);
            }
            if (iteration > 100) {
                msg.type = "error: connection too long";
                sendResponse(msg);
                errorOverlay(msg);
                clearInterval(checkExist);
            }
            if (iteration % 20 == 0) {
                console.log("-- waiting for element(s) " + div + " --");
            }
        }, 200);
    },
    setattr: function (msg, callback, sendResponse) {
        var actionStep = msg.detail[msg.bigStep].website[msg.todo].todo[msg.actionStep];
        var input = $(actionStep.search);
        if (input.length == 0) {
            if (actionStep.grave == true) {
                msg.type = "error: element not found";
                sendResponse(msg);
                errorOverlay(msg);
            } else {
                msg.actionStep++;
                callback(msg, sendResponse);
            }
        } else {
            input.attr(actionStep.attr, actionStep.content);
            msg.actionStep++;
            callback(msg, sendResponse);
        }
    },
    simulateKeyPress: function (msg, callback, sendResponse) {
        var actionStep = msg.detail[msg.bigStep].website[msg.todo].todo[msg.actionStep];
        var input = $(actionStep.search);
        if (input.length == 0) {
            if (actionStep.grave == true) {
                msg.type = "error: " + actionStep.what + " input not found";
                sendResponse(msg);
                errorOverlay(msg);
            } else {
                msg.actionStep++;
                callback(msg, sendResponse);
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
            msg.actionStep++;
            callback(msg, sendResponse);
        }
    },
    fill: function (msg, callback, sendResponse) {
        var actionStep
            = msg.detail[msg.bigStep].website[msg.todo].todo[msg.actionStep];
        var input = $(actionStep.search);
        if (input.length == 0) {
            if (actionStep.grave == true) {
                msg.type = "error: " + actionStep.what + " input not found";
                sendResponse(msg);
                errorOverlay(msg);
            } else {
                msg.actionStep++;
                callback(msg, sendResponse);
            }
        } else {
            var jInput = $(input[0]);
            input[0].focus();
            jInput.select();
            jInput.click();
            fire_before_fill(input[0]);
            input[0].value = msg.detail[0].user[actionStep.what];
            fire_onchange(input[0]);
            input[0].blur();
            msg.actionStep++;
            callback(msg, sendResponse);
        }
    },
    val: function (msg, callback, sendResponse) {
        var actionStep = msg.detail[msg.bigStep].website[msg.todo].todo[msg.actionStep];
        var input = $(actionStep.search);
        if (input.length == 0) {
            if (actionStep.grave == true) {
                msg.type = "error: " + actionStep.what + " input not found";
                sendResponse(msg);
                errorOverlay(msg);
            } else {
                msg.actionStep++;
                callback(msg, sendResponse);
            }
        } else {
            input.val(msg.detail[0].user[actionStep.what]);
            msg.actionStep++;
            callback(msg, sendResponse);
        }
    },
    checkIfPopup: function (msg, callback, sendResponse) {
        /*var popupButton = document.createElement("button");
        popupButton.id = "testtest23";
         document.body.appendChild(popupButton);
        $("#testtest23").click(function(){window.open('http://www.zebest3000.com','lenomdusite','width=300, height=250'); return false;});

        $("#testtest23").click();*/


        msg.actionStep++;
        callback(msg, sendResponse);
    },
    click: function (msg, callback, sendResponse) {
        var actionStep = msg.detail[msg.bigStep].website[msg.todo].todo[msg.actionStep];
        var button = $(actionStep.search);
        if (button.length == 0) {
            if (actionStep.grave == true) {
                msg.type = "error: button not found";
                sendResponse(msg);
                errorOverlay(msg);
            }
            else {
                msg.actionStep++;
                callback(msg, sendResponse);
            }
        } else {
            button.prop("disabled", false);
            window.setTimeout(function () {
                button.click();
            }, 250);
            msg.actionStep++;
            callback(msg, sendResponse);
        }
    },
    clickona: function (msg, callback, sendResponse) {
        var actionStep = msg.detail[msg.bigStep].website[msg.todo].todo[msg.actionStep];
        var button = $(actionStep.search);
        if (button.length == 0) {
            if (actionStep.grave == true) {
                msg.type = "error: button not found";
                sendResponse(msg);
                errorOverlay(msg);
            }
            else {
                msg.actionStep++;
                callback(msg, sendResponse);
            }
        } else {
            button.prop("disabled", false);
            button.get(0).click();
            button.click();
            msg.actionStep++;
            callback(msg, sendResponse);
        }
    },
    aclick: function (msg, callback, sendResponse) {
        var actionStep = msg.detail[msg.bigStep].website[msg.todo].todo[msg.actionStep];
        var button = $(actionStep.search);
        if (button.length == 0) {
            if (actionStep.grave == true) {
                msg.type = "error: link not found";
                sendResponse(msg);
                errorOverlay(msg);
            }
            else {
                msg.actionStep++;
                callback(msg, sendResponse);
            }
        } else {
            window.location.href = button.attr('href');
            msg.actionStep++;
            msg.type = "completed";
            sendResponse(msg);
        }
    },
    trueClick: function (msg, callback, sendResponse) {
        var actionStep = msg.detail[msg.bigStep].website[msg.todo].todo[msg.actionStep];
        var button = $(actionStep.search);
        if (button.length == 0) {
            if (actionStep.grave == true) {
                msg.type = "error: button not found";
                sendResponse(msg);
                errorOverlay(msg);
            }
            else {
                msg.actionStep++;
                callback(msg, sendResponse);
            }
        } else {
            button.prop("disabled", false);
            window.setTimeout(function () {
                for (var i = 0; i < button.length; i++)
                    button[i].click();
            }, 250);
            msg.actionStep++;
            callback(msg, sendResponse);
        }
    },
    submit: function (msg, callback, sendResponse) {
        var actionStep = msg.detail[msg.bigStep].website[msg.todo].todo[msg.actionStep];
        var form = $(actionStep.search);
        if (form.length == 0) {
            if (actionStep.grave == true) {
                msg.type = "error: connection form not found";
                sendResponse(msg);
                errorOverlay(msg);
            }
            else {
                msg.actionStep++;
                callback(msg, sendResponse);
            }
        } else {
            form.submit();
            msg.actionStep++;
            callback(msg, sendResponse);
        }
    },
    wait: function (msg, callback, sendResponse) {
        setTimeout(function () {
            msg.actionStep++;
            callback(msg, sendResponse);
        }, msg.detail[msg.bigStep].website[msg.todo].todo[msg.actionStep].timeout);
    },
    waitload: function (msg, callback, sendResponse) {
        msg.actionStep++;
        msg.type = "completed";
        sendResponse(msg);
    },
    search: function (msg, callback, sendResponse) {
        var actionStep = msg.detail[msg.bigStep].website[msg.todo].todo[msg.actionStep];
        var obj = $(actionStep.search);
        alert("Found: " + obj.length + " search: " + actionStep.search);
        msg.actionStep++;
        callback(msg, sendResponse);
    },
    goto: function (msg, callback, sendResponse) {
        var actionStep = msg.detail[msg.bigStep].website[msg.todo].todo[msg.actionStep];
        var siteUrl;
        if (typeof actionStep.url == "object") {
            siteUrl = (actionStep.url.http + msg.detail[0].user[actionStep.url.subdomain] + "." + actionStep.url.domain);
        }
        else {
            siteUrl = actionStep.url;
        }
        if (siteUrl != undefined)
            window.location.href = siteUrl;
        msg.actionStep++;
        msg.type = "completed";
        sendResponse(msg);
    },
    enterFrame: function (msg, callback, sendResponse) {
        var actionStep = msg.detail[msg.bigStep].website[msg.todo].todo[msg.actionStep];
        if ($(actionStep.search).length) {
            msg.frameUrl = $(actionStep.search)[0].src;
            extension.runtime.sendMessage("sendToChildFrame", msg, function (resp) {
                msg = resp;
                msg.actionStep++;
                callback(msg, sendResponse);
            });
        } else {
            msg.actionStep++;
            callback(msg, sendResponse);
        }
    },
    exitFrame: function (msg, callback, sendResponse) {
        if (window.top === window) {
            msg.actionStep++;
            callback(msg, sendResponse);
        } else {
            var actionStep = msg.detail[msg.bigStep].website[msg.todo].todo[msg.actionStep];
            sendResponse(msg);
        }
    }
};

function doThings(msg, sendResponse) {
    var todo = msg.detail[msg.bigStep].website[msg.todo].todo;
    if (msg.actionStep >= todo.length) {
        msg.type = "completed";
        sendResponse(msg);
        if (msg.todo == "logout") {
            msg.todo = "connect";
            msg.actionStep = 0;
            actions[todo[msg.actionStep].action](msg, doThings, sendResponse);
        }
        return;
    }
    console.log("-- Ease action : " + todo[msg.actionStep].action + " --");
    actions[todo[msg.actionStep].action](msg, doThings, sendResponse);
}

function getHost(url) {
    var getLocation = function (href) {
        var l = document.createElement("a");
        l.href = href;
        return l;
    };
    return getLocation(url).hostname;
}

if (window.top === window) {

} else {
    extension.runtime.onMessage("toChildFrame", function (msg, sendResponse) {
        var actionStep = msg.detail[msg.bigStep].website[msg.todo].todo[msg.actionStep];
        if (window.location.href == msg.frameUrl) {
            msg.actionStep++;
            doThings(msg, sendResponse);
        }
    });
    extension.runtime.sendMessage("haveFrames", window.location.href, function (resp) {

    });
}
