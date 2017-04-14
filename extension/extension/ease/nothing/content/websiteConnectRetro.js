var Content = function() {
	console.log("connectWebsiteRetroAction");

	this.destroy = function() {
		console.log("destroy connectWebsiteRetroAction");
	}

	console.log(I.memory);

	function sendKey(input, key) {
		var e = input.ownerDocument.createEvent("KeyboardEvent");
		e.initKeyboardEvent("keydown", 1, 1, document.defaultView, 0, 0, 0, 0, key, key);
		var f = input.dispatchEvent(e);
		e = input.ownerDocument.createEvent("KeyboardEvent");
		e.initKeyboardEvent("keyup", 1, 1, null, 0, 0, 0, 0, key, key);
		input.dispatchEvent(e);
	}

	function fire_before_fill(a){
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
		fillThenSubmit:function() {
			var actionStep = I.memory.json[I.memory.stepType].todo[I.memory.step];
			var loginInput = $(actionStep.login);
			var passwordInput = $(actionStep.password);

			loginInput.click();
			loginInput.val(I.memory.login);
			loginInput.blur();
			passwordInput.click();
			passwordInput.val(I.memory.login.password);
			passwordInput.blur();
			$("body").append("<script type='text/javascript'>$('"+actionStep.login+"').change(); $('"+ actionStep.password +"').change();</script>");
			$(actionStep.submit).click();			
		},
		erasecookies:function(){

			var name = I.memory.json[I.memory.stepType].todo[I.memory.step].search;

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
						deleteCookie(name);
					}
				}
			},
			waitfor:function(callback){
				var div = I.memory.json[I.memory.stepType].todo[I.memory.step].search;
				var time = I.memory.json[I.memory.stepType].todo[I.memory.step].time;
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
					if(iteration % 20 == 0){
						console.log("-- waiting for element "+div[0]+" --");
					}
					if(absent){
						setTimeout(function(){
							iteration ++;
							waitfor(callback);
						}, time);
					} else {
						I.memory.step++;
						I.pushMemory();
						callback();
					}
				}
				waitfor(callback);
			},
			setattr:function(){
				var actionStep = I.memory.json[I.memory.stepType].todo[I.memory.step];
				var input = $(actionStep.search);
				input.attr(actionStep.attr, actionStep.content);	
			},
			fill:function(){
				var actionStep = I.memory.json[I.memory.stepType].todo[I.memory.step];
				var input = $(actionStep.search);
				input.select();
				input.click();
				input[0].focus();
				fire_before_fill(input[0]);
				input[0].value = I.memory[actionStep.what];
				fire_onchange(input[0]);
				input[0].blur();
			},
			val:function(){
				var actionStep =  I.memory.json[I.memory.stepType].todo[I.memory.step];
				var input = $(actionStep.search);
				input.val(I.memory[actionStep.what]);
			},
			click:function(){
				var actionStep = I.memory.json[I.memory.stepType].todo[I.memory.step];
				var button = $(actionStep.search);

				button.prop("disabled", false);
				window.setTimeout(function() {button.click();}, 250);
			},
			clickona:function(){
				var actionStep = I.memory.json[I.memory.stepType].todo[I.memory.step];
				var button = $(actionStep.search);
				button.prop("disabled", false);
				button.get(0).click();
				button.click();
			},
			aclick:function(){
				var actionStep = I.memory.json[I.memory.stepType].todo[I.memory.step];
				var button = $(actionStep.search);
				if (button[0]) {
					button[0].click();
				}
			},
			submit:function(){
				var actionStep = I.memory.json[I.memory.stepType].todo[I.memory.step];
				var form = $(actionStep.search);

				form.submit();			
			},
			waitload:function(){

			},
			search:function(){
				var actionStep = I.memory.json[I.memory.stepType].todo[I.memory.step];
				var obj = $(actionStep.search);
				alert("Found: " + obj.length + " search: " + actionStep.search);
			},
			goto:function(){
				var actionStep = I.memory.json[I.memory.stepType].todo[I.memory.step];
				var siteUrl;
				if (typeof actionStep.url == "object") {
					siteUrl = (actionStep.url.http + msg.detail[0].user[actionStep.url.subdomain] + "." + actionStep.url.domain);
				}
				else {
					siteUrl = actionStep.url;
				}
				if (siteUrl != undefined)
					window.location.href = siteUrl;
			},
			enterFrame:function() {
				var actionStep = I.memory.json[I.memory.stepType].todo[I.memory.step];
				I.sendMessage("enterFrame", {"type":I.memory.stepType, "cpt":I.memory.step});
				while (I.memory.json[I.memory.stepType].todo[I.memory.step] && I.memory.json[I.memory.stepType].todo[I.memory.step].action !== "exitFrame") {
					I.memory.step++;
				}
				I.pushMemory();
			},
			exitFrame:function() {

			}
		};

	if (context.getType() !== "tab") {
		console.log("openFrame");
		I.onMessage(function(msgName, msg) {
			console.log("msgRecu: " + msgName);
			if (msgName === "inFrame") {
				msg.cpt++;
				I.memory.stepType = msg.type;
				I.memory.step = msg.cpt;
				while (I.memory.json[I.memory.stepType].todo[I.memory.step] && I.memory.json[I.memory.stepType].todo[I.memory.step].action !== "exitFrame") {
					console.log("inFrame");
					actions[I.memory.json[I.memory.stepType].todo[I.memory.step].action]();
					I.memory.step++;
				}
				
			}
		});
		I.sendMessage("openFrame", undefined);
	}


	if (context.getType() === 'tab') {

		var lastLogin;
		chrome.storage.local.get("lastLogin", function(item) {
			if (item.lastLogin == undefined) {
				chrome.storage.local.set({"lastLogin":{}});
				lastLogin = " ";
				console.log("First time connect");
			} else {
				if (item.lastLogin[I.memory.websiteName] == undefined) {
					lastLogin = " ";
				} else {
					lastLogin = item.lastLogin[I.memory.websiteName];
				}
			}

			function waitfor(target, callback) {
				var interval = setInterval(function() {
					console.log("waitfor: " + target);
					if ($(target).length > 0) {
						clearInterval(interval);
						callback();
					}
				}, 10);
			}

			function isConnected(check) {
				return $(check[0].search).length > 0;
			}


			function doAction() {
				console.log(I.memory.step);
				console.log(I.memory.stepType);
				console.log(I.memory);
				if (I.memory.stepType !== "finish") {
					if (I.memory.json[I.memory.stepType].todo[I.memory.step].action === "waitfor") {
						actions[I.memory.json[I.memory.stepType].todo[I.memory.step].action](doAction);
					} else {
						actions[I.memory.json[I.memory.stepType].todo[I.memory.step].action]();
						I.memory.step++;
						if (I.memory.step < I.memory.json[I.memory.stepType].todo.length) {
							I.pushMemory();
							doAction();
						} else {
							if (I.memory.stepType === "logout") {
								I.memory.stepType = "connect";
							} else {
								I.memory.stepType = "finish";
								chrome.storage.local.get("lastLogin", function(item) {
									item.lastLogin[I.memory.websiteName] = I.memory.login;
									chrome.storage.local.set({"lastLogin":item.lastLogin});
								});
							}
							I.memory.step = 0;
							I.pushMemory();
							doAction();
						}
					}
				}
			}

			if (I.memory.stepType == undefined) {
				console.log("Check connected");
				if (isConnected(I.memory.json.checkAlreadyLogged) == true) {
					if (lastLogin === I.memory.login) {
						console.log("You are connected.");
						// You are connected
					} else {
						console.log("You are connected to bad account.");
						I.memory.stepType = "logout";
						I.memory.step = 0;
						I.pushMemory();
						doAction();
					}
				} else {
					console.log("You are not connected");
					I.memory.stepType = "connect";
					I.memory.step = 0;
					I.pushMemory();
					doAction();
				}
			} else {
				console.log("reload with not terminated");
				if (I.memory.stepType !== "finish") {
					doAction();
				}
			}

		});
	}

}