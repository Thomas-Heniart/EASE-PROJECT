var Content = function() {
	console.log("connectWebsiteAction");

	this.destroy = function() {
		console.log("destroy addWebsiteAction");
	}

	console.log(I.memory);

	var actions = {
		click : function(target) {
			$(target)[0].click();
			console.log("click: " + target);
		},
		val : function(target) {
			if (I.memory.what === "login") {
				I.memory.what = "password";
				I.pushMemory();
				$(target)[0].value = I.memory.login;
			} else {
				$(target)[0].value = I.memory.password;
			}
			console.log("val: " + target);
		}
	}

	if (context.getType() !== "tab") {
		I.onMessage(function(msgName, msg) {
			if (msgName === "inFrame") {
				actions[I.memory.json[msg.type][msg.cpt].action](I.memory.json[msg.type][msg.cpt].target);
			}
		});
		I.sendMessage("openFrame");
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

			function isConnected(check) {
				
				return $(check).length > 0 || $(I.memory.json.checkUnconnected).length < 1
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

			function doAction(json, step) {
				console.log(json);
				console.log(step);
				if (json != undefined) {
					if (json[step].inFrame == true) {
						I.sendMessage("inFrame", {"type":I.memory.stepType, "cpt":I.memory.step});
						I.memory.step++;
						if (I.memory.step < json.length) {
							I.pushMemory();
							doAction(json, I.memory.step);
						} else {
							if (I.memory.stepType === "logout") {
								I.memory.stepType = "login";
								I.memory.what = "login";
							} else {
								I.memory.stepType = "finish";
								chrome.storage.local.get("lastLogin", function(item) {
									item.lastLogin[I.memory.websiteName] = I.memory.login;
									chrome.storage.local.set({"lastLogin":item.lastLogin});
								});
							}
							I.memory.step = 0;
							I.pushMemory();
							doAction(I.memory.json[I.memory.stepType], I.memory.step);
						}
					} else {
						waitfor(json[step].target, function() {
							actions[json[step].action](json[step].target);
							I.memory.step++;
							if (I.memory.step < json.length) {
								I.pushMemory();
								doAction(json, I.memory.step);
							} else {
								if (I.memory.stepType === "logout") {
									I.memory.stepType = "login";
									I.memory.what = "login";
								} else {
									I.memory.stepType = "finish";
									chrome.storage.local.get("lastLogin", function(item) {
										item.lastLogin[I.memory.websiteName] = I.memory.login;
										chrome.storage.local.set({"lastLogin":item.lastLogin});
									});
								}
								I.memory.step = 0;
								I.pushMemory();
								doAction(I.memory.json[I.memory.stepType], I.memory.step);
							}
						});
					}
				}
			}

			if (I.memory.stepType == undefined) {
				console.log("Check connected");
				if (isConnected(I.memory.json.checkConnected) == true) {
					if (lastLogin === I.memory.login) {
						console.log("You are connected.");
						// You are connected
					} else {
						console.log("You are connected to bad account.");
						I.memory.stepType = "logout";
						I.memory.step = 0;
						I.pushMemory();
						doAction(I.memory.json[I.memory.stepType], I.memory.step);
					}
				} else {
					console.log("You are not connected");
					I.memory.stepType = "login";
					I.memory.what = "login";
					I.memory.step = 0;
					I.pushMemory();
					doAction(I.memory.json[I.memory.stepType], I.memory.step);
				}
			} else {
				console.log("reload with not terminated");
				if (I.memory.stepType !== "finish") {
					doAction(I.memory.json[I.memory.stepType], I.memory.step);
				}
			}

		});
	}
}