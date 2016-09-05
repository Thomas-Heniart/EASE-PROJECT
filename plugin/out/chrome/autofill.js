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
			input.val(msg.detail[msg.bigStep].user.login);
			msg.detail[msg.bigStep].website.lastLogin = getNewLogin(msg, msg.bigStep);
		} else if (actionStep.what == "password") {
			input.val(msg.detail[msg.bigStep].user.password);
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
waitload:function(msg, callback, sendResponse){
	msg.actionStep++;
	msg.type = "completed";
	sendResponse(msg);
},
search:function(msg, callback, sendResponse){
	var actionStep = msg.detail[msg.bigStep].website[msg.todo].todo[msg.actionStep];
	var obj = $(actionStep.search);
	console.log("Found: " + obj.length + " search: " + todo[i].search);
	msg.actionStep++;
	callback(msg, sendResponse);
}
};

function isConnected(msg){
	var object = $(msg.detail[msg.bigStep].website.checkAlreadyLogged[0].search);
	if (object.length == 0)
		return false;
	return true;
}

function getNewLogin(msg, i){
	if (msg.detail[i].user){
		return (msg.detail[i].user.login);
	} else if (msg.detail[i].logWith){
		return getNewLogin(msg, i-1) + "-" + msg.detail[i].website.name;
	}
}

function alreadyVisited(msg){
	for (var i in (msg.visitedWebsites)){
		if (msg.visitedWebsites[i].name == msg.detail[msg.bigStep].website.name) {
			if (getNewLogin(msg, msg.bigStep) == msg.visitedWebsites[i].lastLogin){
				return true;
			}
			else {
				return false;
			}
		}
	}
	return false;
}

function doThings(msg, sendResponse) {
	var todo =  msg.detail[msg.bigStep].website[msg.todo].todo;
	if (msg.actionStep >= todo.length){
		msg.type = "completed";
		sendResponse(msg);
		return ;
	}
	console.log(todo[msg.actionStep].action);
	actions[todo[msg.actionStep].action](msg, doThings, sendResponse);
}

extension.runtime.onMessage("goooo", function(msg, sendResponse) {
	if (msg.todo == "checkAlreadyLogged"){
		if (isConnected(msg) == true) {
            console.log("You are already connected");
			if (alreadyVisited(msg) == true){
				msg.todo = "connect";
				msg.actionStep = msg.detail[msg.bigStep].website[msg.todo].todo.length;
				msg.type = "completed";
				//alert("sendResponse");
				sendResponse(msg);
				return ;
			} else {
				console.log("This is not your account");
				msg.todo = "logout";
			}
		} else {
			if (typeof msg.detail[msg.bigStep].logWith === "undefined") {
				msg.todo = "connect";
			} else {
				msg.todo = msg.detail[msg.bigStep].logWith;
			}
			console.log("You are not connected");
		}
	}
	doThings(msg, sendResponse);
});