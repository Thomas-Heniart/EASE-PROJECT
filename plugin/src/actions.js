var actions = {
fillThenSubmit:function(msg, callback, sendResponse) {
  var actionStep = msg.detail[msg.bigStep].website[msg.todo].todo[msg.actionStep];
	var loginInput = $(actionStep.login);
  var passwordInput = $(actionStep.password);
	if (loginInput.length == 0){
		if (actionStep.grave == true){
			msg.type = "error: "+ actionStep.what +" input not found";
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
    $("body").append("<script type='text/javascript'>$('"+actionStep.login+"').change(); $('"+ actionStep.password +"').change();</script>");
    $(actionStep.submit).click();

    msg.actionStep++;
    callback(msg, sendResponse);
	}
},
erasecookies:function(msg, callback, sendResponse){

    var name = msg.detail[msg.bigStep].website[msg.todo].todo[msg.actionStep].search;

    function deleteCookie(cookieName){
        var domain = document.domain;
        var path = "/";
    	document.cookie = cookieName + "=; expires=" + +new Date + "; domain=" + domain + "; path=" + path;
    }

    if(name) {deleteCookie(name)}
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
waitfor:function(msg, callback, sendResponse){

    var div = msg.detail[msg.bigStep].website[msg.todo].todo[msg.actionStep].search;
    var time = msg.detail[msg.bigStep].website[msg.todo].todo[msg.actionStep].time;
    if(!time){time = 100;}
    var iteration = 0;

	function waitfor(callback){
    if(typeof div === 'string'){div = [div];}
        var absent = true;
        for(var i in div){
            var obj = $(div[i]);
            if(obj.length>0){
                absent = false;
                break;
            }
        }
        if (iteration > 100){
            msg.type = "error: connection too long";
            sendResponse(msg);
            errorOverlay(msg);
        } else if(absent){
            setTimeout(function(){
                iteration ++;
                waitfor(callback);
            }, time);
        } else {
            msg.actionStep++;
            callback(msg, sendResponse);
        }
	}

	waitfor(callback);
},
setattr:function(msg, callback, sendResponse){
	var actionStep = msg.detail[msg.bigStep].website[msg.todo].todo[msg.actionStep];
	var input = $(actionStep.search);
	if (input.length == 0){
		if (actionStep.grave == true){
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
fill:function(msg, callback, sendResponse){
	var actionStep = msg.detail[msg.bigStep].website[msg.todo].todo[msg.actionStep];
	var input = $(actionStep.search);
	if (input.length == 0){
		if (actionStep.grave == true){
			msg.type = "error: "+ actionStep.what +" input not found";
			sendResponse(msg);
            errorOverlay(msg);
		} else {
			msg.actionStep++;
			callback(msg, sendResponse);
		}
	} else {
		input.click();
    input.focus();
		if (actionStep.what == "login") {
			input.val(msg.detail[0].user.login);
		} else if (actionStep.what == "password") {
			input.val(msg.detail[0].user.password);
		}
    input.change();
		input.blur();
		msg.actionStep++;
		callback(msg, sendResponse);
	}
},
click:function(msg, callback, sendResponse){
	var actionStep = msg.detail[msg.bigStep].website[msg.todo].todo[msg.actionStep];
	var button = $(actionStep.search);
	if (button.length == 0){
		if (actionStep.grave == true){
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
		window.setTimeout(function() {button.click();}, 250);
		msg.actionStep++;
		callback(msg, sendResponse);
	}
},
clickona:function(msg, callback, sendResponse){
	var actionStep = msg.detail[msg.bigStep].website[msg.todo].todo[msg.actionStep];
	var button = $(actionStep.search);
	if (button.length == 0){
		if (actionStep.grave == true){
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
aclick:function(msg, callback, sendResponse){
	var actionStep = msg.detail[msg.bigStep].website[msg.todo].todo[msg.actionStep];
	var button = $(actionStep.search);
	if (button.length == 0){
		if (actionStep.grave == true){
			msg.type = "error: link not found";
			sendResponse(msg);
            errorOverlay(msg);
		}
		else{
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
submit:function(msg, callback, sendResponse){
	var actionStep = msg.detail[msg.bigStep].website[msg.todo].todo[msg.actionStep];
	var form = $(actionStep.search);
	if (form.length == 0){
		if (actionStep.grave == true){
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
wait:function(msg, callback, sendResponse){
    setTimeout(function(){ msg.actionStep++; callback(msg, sendResponse);}, msg.detail[msg.bigStep].website[msg.todo].todo[msg.actionStep].timeout);
},
waitload:function(msg, callback, sendResponse){
	msg.actionStep++;
	msg.type = "completed";
	sendResponse(msg);
},
search:function(msg, callback, sendResponse){
	var actionStep = msg.detail[msg.bigStep].website[msg.todo].todo[msg.actionStep];
	var obj = $(actionStep.search);
	alert("Found: " + obj.length + " search: " + actionStep.search);
	msg.actionStep++;
	callback(msg, sendResponse);
},
goto:function(msg, callback, sendResponse){
	var actionStep = msg.detail[msg.bigStep].website[msg.todo].todo[msg.actionStep];
    window.location.href = actionStep.url;
    msg.actionStep++;
    msg.type = "completed";
	sendResponse(msg);
}
};

function doThings(msg, sendResponse) {
	var todo =  msg.detail[msg.bigStep].website[msg.todo].todo;
	if (msg.actionStep >= todo.length){
		msg.type = "completed";
		if(msg.todo != "logout") msg.detail[msg.bigStep].website.lastLogin = getNewLogin(msg, msg.bigStep);
		sendResponse(msg);
		return ;
	}
    console.log(todo[msg.actionStep].action);
	actions[todo[msg.actionStep].action](msg, doThings, sendResponse);
}
