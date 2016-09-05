var actions = {
waitfor:function(msg, callback, sendResponse){
	function waitfor(msg, callback){
		var obj = $(msg.detail[msg.bigStep].website[msg.todo].todo[msg.actionStep].search);
		if (obj.length == 0){
			setTimeout(function(){
				waitfor(msg, callback);
			}, msg.detail[msg.bigStep].website[msg.todo].todo[msg.actionStep].time);
		}
		else {
			msg.actionStep++;
			callback(msg, sendResponse);
		}
	}
	waitfor(msg, callback);
},
setattr:function(msg, callback, sendResponse){
	var actionStep = msg.detail[msg.bigStep].website[msg.todo].todo[msg.actionStep];
	var input = $(actionStep.search);
	if (input.length == 0){
		if (actionStep.grave == true){
			msg.type = "error: input not found";
			sendResponse(msg);
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
			msg.type = "error: input not found";
			sendResponse(msg);
		} else {
			msg.actionStep++;
			callback(msg, sendResponse);
		}
	} else {
		input.click();
		if (actionStep.what == "login") {
			input.val(msg.detail[0].user.login);
		} else if (actionStep.what == "password") {
			input.val(msg.detail[0].user.password);
		}
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
		}
		else {
			msg.actionStep++;
			callback(msg, sendResponse);
		}
	} else {
		button.prop("disabled", false);
		button.click();
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
		}
		else {
			msg.actionStep++;
			callback(msg, sendResponse);
		}
	} else {
		button.prop("disabled", false);
		button.get(0).click();
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
			msg.type = "error: form not found";
			sendResponse(msg);
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
		msg.detail[msg.bigStep].website.lastLogin = getNewLogin(msg, msg.bigStep);
		sendResponse(msg);
		return ;
	}
    console.log(todo[msg.actionStep]);
	actions[todo[msg.actionStep].action](msg, doThings, sendResponse);
}
