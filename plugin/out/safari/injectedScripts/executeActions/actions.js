var wait = true;

function getHost(url) {
    var getLocation = function (href) {
        var l = document.createElement("a");
        l.href = href;
        return l;
    };
    return getLocation(url).hostname;
}

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
    enterFrame: function (actionStep, callback) {
        var frameUrl = $(actionStep.search)[0].src;
        var msg = {};
        msg.step = 0;
        msg.actions = actionStep.todo;
        frames.sendMessage(frameUrl, "executeOnFrame", msg, function (resMsg) {
            callback(resMsg.status);
        });
    },
    exitFrame: function (actionStep, callback) {
        callback("exitFrame");
    },
    fillThenSubmit: function (actionStep, callback) {
        var loginInput = $(actionStep.login);
        var passwordInput = $(actionStep.password);
        if (loginInput.length == 0) {
            if (actionStep.grave == true) {
                callback("error: input not found");
            } else {
                callback("next");
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
            callback("next");
        }
    },
    erasecookies: function (actionStep, callback) {
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
        callback("next");

    },
    waitfor: function (actionStep, callback) {
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
                console.log("-- Waiting for element " + div[0] + " --");
            }
            if (iteration > 100) {
                callback("error: cant find element");
            } else if (absent) {
                setTimeout(function () {
                    iteration++;
                    waitfor();
                }, time);
            } else {
                callback("next");
            }
        }
        waitfor();
    },
    setattr: function (actionStep, callback) {
        var input = $(actionStep.search);
        if (input.length == 0) {
            if (actionStep.grave == true) {
                callback("error: element not found");
            } else {
                callback("next");
            }
        } else {
            input.attr(actionStep.attr, actionStep.content);
            callback("next");
        }
    },
    simulateKeyPress: function (actionStep, callback) {
        var input = $(actionStep.search);
        if (input.length == 0) {
            if (actionStep.grave == true) {
                callback("error: " + actionStep.what + " input not found");
            } else {
                callback("next");
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
            callback("next");
        }
    },
    fill: function (actionStep, callback) {
        var input = $(actionStep.search);
        if (input.length == 0) {
            if (actionStep.grave == true) {
                callback("error: input not found");
            } else {
                callback("next");
            }
        } else {
            var t = 1;
            if (wait) {
                t = 250;
            }
            setTimeout(function () {
                wait = false;
                input.select();
                input.click();
                input[0].focus();
                fire_before_fill(input[0]);
                input[0].value = actionStep.what;
                fire_onchange(input[0]);
                input[0].blur();
                callback("next");
            }, t);

        }
    },
    val: function (actionStep, callback) {
        var input = $(actionStep.search);
        if (input.length == 0) {
            if (actionStep.grave == true) {
                callback("error: " + actionStep.what + " input not found");
            } else {
                callback("next");
            }
        } else {
            var t = 1;
            if (wait) {
                t = 250;
            }
            setTimeout(function () {
                wait = false;
                input.val(actionStep.what);
                callback("next");
            }, t);
        }
    },
    checkIfPopup: function (actionStep, callback) {
        /*var popupButton = document.createElement("button");
        popupButton.id = "testtest23";
         document.body.appendChild(popupButton);
        $("#testtest23").click(function(){window.open('http://www.zebest3000.com','lenomdusite','width=300, height=250'); return false;});

        $("#testtest23").click();*/

        callback("next");
    },
    click: function (actionStep, callback) {
        var button = $(actionStep.search);
        if (button.length == 0) {
            if (actionStep.grave == true) {
                callback("error: button not found");
            } else {
                callback("next");
            }
        } else {
            button.prop("disabled", false);
            window.setTimeout(function () {
                button.click();
            }, 250);
            callback("next");
        }
    },
    clickona: function (actionStep, callback) {
        var button = $(actionStep.search);
        if (button.length == 0) {
            if (actionStep.grave == true) {
                callback("error: button not found");
            } else {
                callback("next");
            }
        } else {
            button.prop("disabled", false);
            button.get(0).click();
            button.click();
            callback("next");
        }
    },
    aclick: function (actionStep, callback) {
        var button = $(actionStep.search);
        if (button.length == 0) {
            if (actionStep.grave == true) {
                callback("error: link not found");
            } else {
                callback("next");
            }
        } else {
            window.top.location.href = button.attr('href');
            callback("waitload");
        }
    },
    submit: function (actionStep, callback) {
        var form = $(actionStep.search);
        if (form.length == 0) {
            if (actionStep.grave == true) {
                callback("error: connection form not found");
            } else {
                callback("next");
            }
        } else {
            form.submit();
            callback("next");
        }
    },
    wait: function (actionStep, callback) {
        setTimeout(function () {
            callback("next");
        }, actionStep.timeout);
    },
    waitload: function (actionStep, callback) {
        callback("waitload");
    },
    search: function (actionStep, callback) {
        var obj = $(actionStep.search);
        alert("Found: " + obj.length + " search: " + actionStep.search);
        callback("next");
    },
    goto: function (actionStep, callback) {
        window.top.location.href = actionStep.url;
        callback("waitload");
    },
    overlay: function (actionStep, callback) {
        if (window.top === window) {
            if (actionStep.type == "classic") {
                if ($("#ease_overlay_mm").length == 0) {
                    overlay.create();
                }
                overlay.set(actionStep.info);
            } else if (actionStep.type == "scrap") {
                if ($("#ease_overlay_scrap").length == 0) {
                    overlayscrap.create(actionStep.info);
                }
            }
        }
        callback("next");
    },
    check: function (actionStep, callback) {
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
                if (regex.test(location.href)) {
                    success();
                } else {
                    fail();
                }
            }
        }
        checks[actionStep.type](actionStep, function () {
            callback("next");
        }, function () {
            callback("error : check " + actionStep.type + " failed");
        });
    },
    getUser: function (actionStep, callback) {
        var element = $(actionStep.search);
        if (element.length == 0) {
            if (actionStep.grave == true) {
                callback("error : element not found");
            } else {
                callback("next");
            }
        } else {
            if (actionStep.attribute == "text") {
                var res = $(actionStep.search).text();
            } else if (actionStep.attribute == "val") {
                var res = $(actionStep.search).val();
            } else {
                var res = $(actionStep.search).attr(actionStep.attribute);
            }
            callback("user :" + res);
        }
    }
};

function executeActions(msg, sendResponse) {
    if (msg.step >= msg.actions.length) {
        msg.status = "done";
        sendResponse(msg);
        return;
    }
    console.log("-- Ease action : " + msg.actions[msg.step].action + " --");
    actions[msg.actions[msg.step].action](msg.actions[msg.step], function (response) {
        if (response == "next") {
            msg.step++;
            executeActions(msg, sendResponse);
        } else if (response == "waitload") {
            msg.step++;
            msg.status = "waitload";
            sendResponse(msg);
        } else if (response.indexOf("error") == 0) {
            msg.status = response;
            sendResponse(msg);
        } else if (response.indexOf("user") == 0) {
            msg.user = response.substring(response.indexOf(":") + 1, response.length);
            msg.step++;
            executeActions(msg, sendResponse);
        } else if (response == "exitFrame") {
            msg.status = "next";
            sendResponse(msg);
        } else {
            msg.status = "error : wrong callback";
            sendResponse(msg);
        }
    });
}
