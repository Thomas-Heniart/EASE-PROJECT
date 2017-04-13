var I = {};

I.Identity = function () {
	
	var magic = "4577LaMenuiserieMec!";
	this.getMagic = function() {
		return magic;
	}
}

var IDENTITY = new I.Identity();

var global = {};

var tool = {
	generateSingleId:function() {
		var date = new Date();
		var components = [
    		date.getYear(),
    		date.getMonth(),
    		date.getDate(),
    		date.getHours(),
    		date.getMinutes(),
    		date.getSeconds(),
    		date.getMilliseconds()
		];
		return components.join("");
	},
    getCode:function(filePath, callback) {
        var tab = filePath.split(".");
        chrome.storage.local.get("updater", function(item) {
            var thing = item.updater.files;
            while (tab.length > 0) {
                thing = thing[tab[0]];
                tab.shift();
            }
            callback(thing);
        });
    },
    sizeOf:function ( object ) {

        var objectList = [];
        var stack = [ object ];
        var bytes = 0;

        while ( stack.length ) {
            var value = stack.pop();

            if ( typeof value === 'boolean' ) {
                bytes += 4;
            }
            else if ( typeof value === 'string' ) {
                bytes += value.length * 2;
            }
            else if ( typeof value === 'number' ) {
                bytes += 8;
            }
            else if
                (
                    typeof value === 'object'
                    && objectList.indexOf( value ) === -1
                    )
            {
                objectList.push( value );

                for( var i in value ) {
                    stack.push( value[ i ] );
                }
            }
        }
        return bytes;
    }
}

I.BrowserManager = function () {
	var parentIn = I;
	var I = {};
	var self = this;
	this.print = function() {
		console.log(self);
	}
	I.WindowManager = function (singleId, browserWindow, parentIn) {
		var parentIn = parentIn;
		var I = {};
		var self = this;
		this.print = function() {
			console.log(self);
		}

		I.TabManager = parentIn.TabManager;

//==WindowManager==//		

		I.type = "window";

		I.singleId = singleId;
		I.browserWindow = browserWindow;

		I.tabsSingleId = {};
		I.tabsBrowserId = {};
		I.openTabCallback = undefined;
		I.closeTabCallback = undefined;
		I.userOpenTab = {};
		

		I.closeCallback = undefined;

		I.fromParent = {
			addTab : function(browserTab) {
				var singleId = tool.generateSingleId();
				var tab = new I.TabManager(singleId, browserTab, I);
				if (I.userOpenTab[browserTab.id]) {
					I.userOpenTab[browserTab.id](tab);
					delete I.userOpenTab[browserTab.id];
				}
				console.log("add tab");
				return tab;
			},
			removeTab : function(browserTabId) {
				if (I.tabsBrowserId[browserTabId]) {
					I.tabsBrowserId[browserTabId].tab.silence();
					delete I.tabsSingleId[I.tabsBrowserId[browserTabId].singleId];
					delete I.tabsBrowserId[browserTabId];
					console.log("remove Tab");
				}
			},
			addContent : function(browserTabId, pathTab) {
				if (I.tabsBrowserId[browserTabId]) {
					console.log("tab trouvé");
					I.tabsBrowserId[browserTabId].fromParent.addContent(pathTab);
				}
			},
			removeContent : function(browserTabId, pathTab) {
				if (I.tabsBrowserId[browserTabId]) {
					I.tabsBrowserId[browserTabId].fromParent.removeContent(pathTab);	
				}
			},
			actionFinished : function(browserTabId, actionName) {
				if (I.tabsBrowserId[browserTabId]) {
					I.tabsBrowserId[browserTabId].fromParent.actionFinished(actionName);	
				}
			},
			pushMemory : function(browserTabId, actionName, memory) {
				if (I.tabsBrowserId[browserTabId]) {
					I.tabsBrowserId[browserTabId].fromParent.pushMemory(actionName, memory);	
				}
			},
			message : function(browserTabId, actionName, msgName, msg, sendResponse) {
				if (I.tabsBrowserId[browserTabId]) {
					I.tabsBrowserId[browserTabId].fromParent.message(actionName, msgName, msg, sendResponse);	
				}
			},
			update : function() {
				Object.keys(I.tabsSingleId).forEach(function(attr) {
					I.tabsSingleId[attr].fromParent.update();
				});
			},
			setPrerenderTab : function(addedTabId, removedTabId) {
				Object.keys(I.tabsBrowserId).forEach(function(attr) {
					if (attr == removedTabId) {
						var actions = I.tabsSingleId[I.tabsBrowserId[attr].singleId].fromParent.getActions();
						delete I.tabsSingleId[I.tabsBrowserId[attr].singleId];
						delete I.tabsBrowserId[attr];
						if (parentIn.prerenderTabsBrowserId[addedTabId]) {
							I.tabsBrowserId[addedTabId] = parentIn.prerenderTabsBrowserId[addedTabId];
							I.tabsSingleId[I.tabsBrowserId[addedTabId].singleId] = parentIn.prerenderTabsSingleId[I.tabsBrowserId[addedTabId].singleId];
							I.tabsBrowserId[addedTabId].fromParent.setActions(actions);
							delete parentIn.prerenderTabsBrowserId[addedTabId];
							delete parentIn.prerenderTabsSingleId[I.tabsBrowserId[addedTabId].singleId];	
						} else {
							chrome.tabs.get(addedTabId, function(browserTab) {
								I.fromParent.addTab(browserTab);
								I.tabsBrowserId[browserTab.id].fromParent.setActions(actions);
							});
						}
						
					}
				});
			},
			serverActionMsg : function(tabSingleId, actionName, order, data) {
				console.log("message Recuuuuuuu lalalalal");
				if (I.tabsSingleId[tabSingleId]) {
					I.tabsSingleId[tabSingleId].fromParent.serverActionMsg(actionName, order, data);
				}
			}
		}

		I.sendToServer = function(msg) {
			msg.action.windowSingleId = I.singleId;
			parentIn.sendToServer(msg);
		}

		this.getTabs = function () {
			var tabs = [];
			Object.keys(I.tabsSingleId).forEach(function(attr, index) {
				tabs.push(I.tabsSingleId[attr].tab);
			});
			return tabs;
		}
		this.getTabById = function(singleId) {
			return tabsSingleId[singleId].tab;
		}

		this.getPosition = function() {
			return I.browserTab.index;
		}

		this.openTab = function(url, callback, position) {
			chrome.tabs.create({'windowId':browserWindow.id, 'index':position, 'url':url}, function(browserTab) {
				if (I.tabsBrowserId[browserTab.id]) {
					callback(I.tabsBrowserId[browserTab.id].tab);
					delete I.userOpenTab[browserTab.id];
				} else {
					I.userOpenTab[browserTab.id] = callback;
				}
			});
		}
		this.onTabOpened = function(callback) {
			I.openTabCallback = callback;
		}
		this.onTabClosed = function(callback) {
			I.closeTabCallback = callback;
		}
		this.getCurrentTab = function(callback) {
			chrome.tabs.getCurrent(function(browserTab) {
				if (I.tabs[browserTab.id]) {
					callback(I.tabs[browserTab.id].tab);
				} else {
					callback(undefined);
				}
			});
		}
		this.close = function() {
			chrome.windows.remove(I.browserWindow.id);
		}
		this.onClosed = function(callback) {
			parentIn.windowsSingleId[singleId].onClosed = callback;
			parentIn.windowsBrowserId[browserWindow.id].onClosed = callback;
		}
		this.silence = function() {
					
		}

		parentIn.windowsSingleId[singleId] = {"window": this, "browserWindowId":I.browserWindow.id, 'fromParent':I.fromParent};
		parentIn.windowsBrowserId[browserWindow.id] = {"window": this, "singleId":I.singleId, 'fromParent':I.fromParent};

//=FIN=WindowManager==//
	}


	I.TabManager = function (singleId, browserTab, parentIn) {
		var parentIn = parentIn;
		var I = {};
		var self = this;
		this.print = function() {
			console.log(self);
		}
		I.ContentManager = function (singleId, parentIn, type, tab) {
			var parentIn = parentIn;
			var I = {};
			I.ContentManager = parentIn.ContentManager;
			
			var self = this;
			this.print = function() {
				console.log(self);
			}
//==ContentManager==//

			I.singleId = singleId;
			I.type = type;

			I.iframes = {};
			I.popups = {};

			I.tab = tab;

			I.fromParent = {
				addContent : function(pathTab) {
					if (pathTab.length > 0) {
						var content = pathTab[0];
						pathTab.shift();
						if (I[content.type + "s"][content.singleId]) {
							I[content.type + "s"][content.singleId].fromParent.addContent(pathTab);
						} else {
							new I.ContentManager(content.singleId, I, content.type, tab);
							console.log("new " + content.type);
							I[content.type + "s"][content.singleId].fromParent.addContent(pathTab);
						}
					}
				},
				removeContent : function(pathTab) {
					
					var content = pathTab[0];
					if (pathTab.length == 1) {
						if (I[content.type + "s"][content.singleId]) {
							delete I[content.type + "s"][content.singleId];
							console.log("remove " + content.type);
						}
					} else {
						pathTab.shift();
						if (I[content.type + "s"][content.singleId]) {
							I[content.type + "s"][content.singleId].fromParent.removeContent(pathTab);
						}
					}
				}
			}

			this.getIframes = function() {
				var tab = [];
				Object.keys(I.iframes).forEach(function(attr) {
					tab.push(I.iframes[attr].content);
				});
				return tab;
			}
			this.getIframeById = function(singleId) {

			}
			this.getPopups = function() {
				var tab = [];
				Object.keys(I.popups).forEach(function(attr) {
					tab.push(I.popups[attr].content);
				});
				return tab;
			}
			this.getPopupById = function (singleId) {

			}
			this.silence = function() {

			}

			if (type === 'tab') {
				parentIn.content = {"content":this, 'fromParent':I.fromParent, 'singleId':I.singleId};
			} else {
				parentIn[type + "s"][singleId] = {"content": this, "fromParent":I.fromParent};	
			}
				
//=FIN=ContentManager==//
		}

		I.ActionManager = function(actionName, parentIn, memory) {
			var parentIn = parentIn;
			var I = {};
			var self;

			I.sendMessage = function(msgName, msg, onResponse) {
				parentIn.sendMessage({"magic":IDENTITY.getMagic(), "msgName":msgName, "msg":msg, "actionName": actionName}, onResponse);
			}

			I.onMessage = function(msgName, callback) {
				parentIn.actions[actionName].onMessage[msgName] = callback;
			}
			I.removeMsgListener = function(msgName) {
				delete parentIn.actions[actionName].onMessage[msgName];
			}

			I.sendMessageToServer = function(order, data) {
				parentIn.sendToServer({'context':"action", "action":{"actionName":actionName}, 'order':order, 'data': data});
			}

			I.onMessageFromServer = function(callback) {
				parentIn.actions[actionName].serverMsgCallback = callback;
			}

			I.removeServerMsgListener = function() {
				parentIn.actions[actionName].serverMsgCallback = undefined;
			}

			I.memory = memory;

			chrome.storage.local.get("updater", function(item) {
				var parentIn = {};
				eval(item.updater.actions.background[actionName]);
				console.log(item.updater.actions.background[actionName]);
				self = new Background();
			});
			this.destroy = function() {
				self.destroy();
			}

			this.reload = function() {
				chrome.storage.local.get("updater", function(item) {
					var parentIn = {};
					self.destroy();
					eval(item.updater.actions.background[actionName]);
					self = new Background();
				});
			}
			this.setMemory = function(memory) {
				I.memory = memory;
				console.log("memory-size: " + tool.sizeOf(memory));
			}
		}

//==TabManager==//

		I.singleId = singleId;
		I.browserTab = browserTab;

		I.content = undefined;
		I.closeCallback = undefined;
		I.actions = {};

		I.fromParent = {
			addContent : function(pathTab) {
				var content = pathTab[0];
				pathTab.shift();
				if (I.content && I.content.singleId === content.singleId) {
					I.content.fromParent.addContent(pathTab);
					console.log("tab reloaded");
				} else {
					new I.ContentManager(content.singleId, I, content.type, this);
					Object.keys(I.actions).forEach(function(attr) {
						I.actions[attr].action.reload();
						chrome.tabs.sendMessage(I.browserTab.id, {"magic":IDENTITY.getMagic(), "type":"launchAction", "actionName":attr, "memory":I.actions[attr].memory});
					});
					console.log("new content");
					I.content.fromParent.addContent(pathTab);
				}
			},
			removeContent : function(pathTab) {
				var content = pathTab[0];
				if (pathTab.length == 1) {
					delete I.content;
					console.log("remove content");
				} else {
					if (I.content) {
						pathTab.shift();
						I.content.fromParent.removeContent(pathTab);
					}
				}
			},
			actionFinished : function(actionName) {
				if (I.actions[actionName]) {
					I.actions[actionName].action.destroy();
					delete I.actions[actionName];
				}
			},
			pushMemory : function(actionName, memory) {
				if (I.actions[actionName]) {
					I.actions[actionName].memory = memory;
					I.actions[actionName].action.setMemory(memory);
				}
			},
			message : function(actionName, msgName, msg, sendResponse) {
				if (I.actions[actionName]) {
					if (I.actions[actionName].onMessage[msgName]) {
						I.actions[actionName].onMessage[msgName](msg, sendResponse);
					}
				}
			},
			update : function() {
				chrome.tabs.sendMessage(I.browserTab.id, {"magic":IDENTITY.getMagic(), "type":"update"})
			},
			getActions : function() {
				return I.actions;
			},
			setActions : function(actions) {
				I.actions = actions;
				Object.keys(I.actions).forEach(function(attr) {
					I.actions[attr].action.reload();
					chrome.tabs.sendMessage(I.browserTab.id, {"magic":IDENTITY.getMagic(), "type":"launchAction", "actionName":attr, "memory":I.actions[attr].memory});
				});
			},
			serverActionMsg : function(actionName, order, data) {
				if (I.actions[actionName]) {
					if (I.actions[actionName].serverMsgCallback)
						I.actions[actionName].serverMsgCallback(order, data);
				}
			}
		}
	
		I.sendMessage = function(msg, onResponse) {
			chrome.tabs.sendMessage(I.browserTab.id, msg, onResponse);
		}
		I.sendToServer = function(msg) {
			msg.action.tabSingleId = I.singleId;
			parentIn.sendToServer(msg);
		}

		this.getIframes = function() {
			return (I.content) ? I.content.getIframes() : [];
		}
		this.getIframeById = function(singleId) {

		}
		this.getPopups = function() {
			return (I.content) ? I.content.getPopups() : [];
		}
		this.getPopupById = function (singleId) {

		}
		this.launchAction = function(actionName, memory) {
			if (memory == undefined) {
				memory = {};
			}
			chrome.tabs.sendMessage(I.browserTab.id, {"magic":IDENTITY.getMagic(), "type":"launchAction", "actionName":actionName, "memory":memory});
			I.actions[actionName] = {"action":undefined, "onMessage":{}, "memory":memory};
			var action = new I.ActionManager(actionName, I, I.actions[actionName].memory);
			I.actions[actionName].action = action;
		}
		this.getPosition = function() {

		}
		this.close = function() {

		}
		this.onClosed = function(callback) {

		}
		this.silence = function() {
					
		}

		if (parentIn.type === "browserManager") {
			parentIn.prerenderTabsSingleId[singleId] = {"tab": this, "browserTabId":I.browserTab.id, 'fromParent': I.fromParent};
			parentIn.prerenderTabsBrowserId[browserTab.id] = {"tab": this,"singleId":I.singleId, 'fromParent': I.fromParent};
		} else if (parentIn.type === "window") {
			parentIn.tabsSingleId[singleId] = {"tab": this, "browserTabId":I.browserTab.id, 'fromParent': I.fromParent};
			parentIn.tabsBrowserId[browserTab.id] = {"tab": this,"singleId":I.singleId, 'fromParent': I.fromParent};
		}

//=FIN=TabManager==//
	}

	I.BackgroundManager = function(parentIn, memory) {
				var parentIn = parentIn;
				var I = {};
				var self;


				I.onMessage = function(msgName, callback) {
					parentIn.background.onMessage[msgName] = callback;
				}
				I.removeMsgListener = function(msgName) {
					delete parentIn.background.onMessage[msgName];
				}

				I.memory = memory;

				I.sendMessageToServer = function(order, data) {
					parentIn.sendToServer({'context':"action", "action":{"actionName":"background"}, 'order':order, 'data': data});
				}

				I.onMessageFromServer = function(callback) {
					parentIn.background.serverMsgCallback = callback;
				}

				I.removeServerMsgListener = function() {
					parentIn.background.serverMsgCallback = undefined;
				}

				chrome.storage.local.get("updater", function(item) {
					var parentIn = {};
					eval(item.updater.background);
					self = new Background();
				});

				this.destroy = function() {
					self.destroy();
				}

				this.reload = function() {
					chrome.storage.local.get("updater", function(item) {
						var parentIn = {};
						self.destroy();
						eval(item.updater.background);
						self = new Background();
					});
				}
			}

//==BrowserManager==//
	
	I.type = "browserManager";

	I.windowsSingleId = {};
	I.windowsBrowserId = {};
	I.openWindowCallback = undefined;
	I.closeWindowCallback = undefined;
	I.userOpenWindow = {};
	
	I.prerenderTabsBrowserId = {};
	I.prerenderTabsSingleId = {};
	
	I.addPrerenderTab = function(browserTab) {
		var singleId = tool.generateSingleId();
		var tab = new I.TabManager(singleId, browserTab, I);
		return tab;
	}
	I.removePrerenderTab = function(browserTabId) {
		if (I.prerenderTabsBrowserId[browserTabId]) {
			I.prerenderTabsBrowserId[browserTabId].tab.silence();
			delete I.prerenderTabsSingleId[I.prerenderTabsBrowserId[browserTabId].singleId];
			delete I.prerenderTabsBrowserId[browserTabId];
			console.log("remove prerender Tab");
		}
	}

	I.addWindow = function(browserWindow) {
		var singleId = tool.generateSingleId();
		var window = new I.WindowManager(singleId, browserWindow, I);
		console.log("new window")
		return window;
	}
	I.removeWindow = function(browserWindowId) {
		var singleId = I.windowsBrowserId[browserWindowId].singleId;
		I.windowsSingleId[singleId].window.silence();
		if (I.windowsSingleId[singleId].onClosed)
			I.windowsSingleId[singleId].onClosed();
		delete I.windowsSingleId[singleId];
		delete I.windowsBrowserId[browserWindowId];
		console.log("window removed");
	}

	I.websocket = null;
	I.interval = null;
	I.serverUrl = "localhost:8080/";


	I.connectionState = "unconnected";
	I.connectionStateChangedCallback = {"extension":undefined, "background":undefined};
	I.setConnectionState = function(connectionState) {
		if (I.connectionState !== connectionState) {
			I.connectionState = connectionState;
			if (I.connectionStateChangedCallback.extension)
				I.connectionStateChangedCallback.extension(connectionState);
			if (I.connectionStateChangedCallback.background)
				I.connectionStateChangedCallback.background(connectionState);
		}
	}


	I.sources = {	"background":undefined, 
					"actions":{"background":{"default":undefined}, "content":{"default":undefined}},
					"version":undefined
				};
	I.isActive = false;
	I.inactiveCallback = {"update":undefined};

	I.background = undefined;

	I.identityMsgHandler = {
    	welcome : function(data) {
    		I.websocket.send(JSON.stringify({'context':"identity", 'order':"lastSingleId", 'data': {'lastSingleId': I.singleId}}));
    		I.singleId = data.singleId;
    		chrome.storage.local.set({"identity": {"lastSingleId":I.singleId}});
    	}
    }

    I.updaterMsgHandler = {
    	update : function(data) {
    		if (data.version !== I.sources.version) {
    			I.websocket.send(JSON.stringify({'context':"updater", 'order':"getSources", 'data': {}}));
    		}
    	},
    	sources : function(data) {
    		function launchUpdate() {
    			chrome.storage.local.set({"updater":data}, function() {
    				I.sources.version = data.version;
    				I.sources.background = data.background;
    				I.sources.actions.background.default = data.actions.background.default;
    				I.sources.actions.content.default = data.actions.content.default;
    				
    				I.background.background.reload();
    				Object.keys(I.windowsSingleId).forEach(function(attr) {
    					I.windowsSingleId[attr].fromParent.update();
    				});
    			});
    		}
    		if (!I.isActive) {
				launchUpdate();
			} else {
				I.inactiveCallback.update = launchUpdate;
			}
    	}
    }

	I.openSocket = function() {
		var xmlHttp = new XMLHttpRequest();
    	xmlHttp.open( "GET", "http://" + I.serverUrl + "extensionOpenSession", false ); // false for synchronous request
    	xmlHttp.send( null );
    	
    	I.websocket = new WebSocket("ws://" + I.serverUrl + "extensionEndpoint");

    	I.websocket.onmessage = function(message) {
			var msg = JSON.parse(message.data);
			if (msg.context === "identity") {
    			if (I.identityMsgHandler[msg.order] != undefined)
    				I.identityMsgHandler[msg.order](msg.data);
    		} else if (msg.context === "updater") {
    			if (I.updaterMsgHandler[msg.order] != undefined)
    				I.updaterMsgHandler[msg.order](msg.data);
    		} else if (msg.context === "action") {
    			 if (msg.actionName === "background") {
    			 	I.background.serverMsgCallback(msg.order, msg.data);
    			} else if (I.windowsSingleId[msg.windowSingleId])
    				I.windowsSingleId[msg.windowSingleId].fromParent.serverActionMsg(msg.tabSingleId, msg.actionName, msg.order, msg.data);
    		}
    	}
    	I.websocket.onopen = function() {
    		console.log("open");
    		I.setConnectionState("connected");
    	}
    	I.websocket.onclose = function(evt) {
    		console.log("close: " + evt.code);
 			I.setConnectionState("unconnected");
    	}
    	I.websocket.onerror = function(evt) {
    		console.log("error: " + evt.type);
    	}
    }

    chrome.storage.local.get("updater", function(item) {
		if (item.updater == undefined) {
			item.updater = {"background":"var Background = function () {var self = this;this.destroy = function () {delete self;}}",
							"content":"var Content = function () {var self = this;this.destroy = function () {delete self;}}",
							"actions": {"background":{}, "content":{}},
							"files":{},
							"magic":IDENTITY.getMagic(),
							"version":"0.0.0"};
			chrome.storage.local.set({"updater":item.updater});
		}
		I.sources.version = item.updater.version;
		I.sources.background = item.updater.background;
		I.sources.actions.background.default = item.updater.actions.background.default;
    	I.sources.actions.content.default = item.updater.actions.content.default;
		
		
		I.background = {"background": new I.BackgroundManager(I, {}), "memory": {}};

		I.interval = setInterval(function() {
			if (I.websocket.readyState === I.websocket.CLOSED) {
				console.log("Connection lost, try to reconnect...");
				I.openSocket();
			}
		}, 5000);
		I.openSocket(); 

		I.sendToServer = function(msg) {
			var msgId = tool.generateSingleId();
			var toSend = JSON.stringify(msg);
			var length = Math.floor(toSend.length / 1024) + 1;
			var sendFirstPart = function(index, msg) {
				var toSend = {'context':"action", 'msgId': msgId, 'size':length.toString(), "index":index.toString() ,'data':msg.slice(0, 1024)};
				I.websocket.send(JSON.stringify(toSend));
				if (msg.length > 1024) {
					sendFirstPart(index + 1, msg.slice(1024));
				}	
			}
			console.log(toSend);
			sendFirstPart(0, toSend);
		}

		I.messageHandler = {
			newContent : function(event) {
				if (event.sender.tab && event.sender.tab.windowId) {
					console.log("receive new content from tab: " + event.msg.url);
					if (I.windowsBrowserId[event.sender.tab.windowId] != undefined) {
						
						I.windowsBrowserId[event.sender.tab.windowId].fromParent.addContent(event.sender.tab.id, event.msg.pathTab);	
					} else {
						I.addPrerenderTab(event.sender.tab);
						console.log("add prerender tab: " + event.msg.url);
						console.log("--> tabId = " + event.sender.tab.id);
					}
				}
			},
			closeContent : function(event) {
				if (event.sender.tab && event.sender.tab.windowId) {
					I.windowsBrowserId[event.sender.tab.windowId].fromParent.removeContent(event.sender.tab.id, event.msg.pathTab);
				}
			},
			actionFinished : function(event) {
				if (event.sender.tab && event.sender.tab.windowId) {
					I.windowsBrowserId[event.sender.tab.windowId].fromParent.actionFinished(event.sender.tab.id, event.msg.actionName);
				}
			},
			pushMemory : function(event) {
				if (event.sender.tab && event.sender.tab.windowId) {
					I.windowsBrowserId[event.sender.tab.windowId].fromParent.pushMemory(event.sender.tab.id, event.msg.actionName, event.msg.memory);
				}
			},
			message : function(event, sendResponse) {
				if (event.sender.tab && event.sender.tab.windowId) {
					I.windowsBrowserId[event.sender.tab.windowId].fromParent.message(event.sender.tab.id, event.msg.actionName, event.msg.msgName, event.msg.msg, sendResponse);
				}
			}
		}

		chrome.runtime.onMessage.addListener(function(request, sender, sendResponse) {
			if (request.magic === IDENTITY.getMagic()) {
				if (I.messageHandler[request.type]) {
					I.messageHandler[request.type]({'msg':request, 'sender':sender}, sendResponse);
				}
			}
		});

		chrome.tabs.onCreated.addListener(function(browserTab) {
			console.log("tab created: " + browserTab.id);
			if (I.windowsBrowserId[browserTab.windowId]) {
				I.windowsBrowserId[browserTab.windowId].fromParent.addTab(browserTab);
			}
		});

		chrome.tabs.onRemoved.addListener(function(browserTabId, removeInfo) {
			if (I.windowsBrowserId[removeInfo.windowId]) {
				I.windowsBrowserId[removeInfo.windowId].fromParent.removeTab(browserTabId);
			}
		});

		chrome.windows.onCreated.addListener(function(browserWindow) {
			console.log("window created");
			if (I.windowsBrowserId[browserWindow.id] == undefined) {
				var window = I.addWindow(browserWindow);
				if (I.openWindowcallback)
					openWindowcallback(window);
				if (I.userOpenWindow[browserWindow.id]) {
					I.userOpenWindow[browserWindow.id](window);
					delete(I.userOpenWindow[browserWindow.id]);
				}
			}
		});

		chrome.windows.onRemoved.addListener(function(browserWindowId) {
			if (I.windowsBrowserId[browserWindowId]) {
				I.removeWindow(browserWindowId);
				if (I.closeWindowCallback) {
					I.closeWindowCallback();
				}
			}
		});

		chrome.tabs.onReplaced.addListener(function(addedTabId, removedTabId) {
			console.log("added: " + addedTabId);
			console.log("removed: " + removedTabId);
			Object.keys(I.windowsSingleId).forEach(function(attr) {
				console.log("une window");
				I.windowsSingleId[attr].fromParent.setPrerenderTab(addedTabId, removedTabId);
			});
		});
	});

	this.getWindows = function() {
		var windows = [];
		Object.keys(I.windowsSingleId).forEach(function(attr) {
			windows.push(I.windowsSingleId[attr].window);
		});
		return windows;
	}

	this.getWindowById = function(singleId) {
		if (windowsSingleId[singleId]) {
			return windowsSingleId[singleId].window;
		}
		return undefined;
	}

	this.getCurrentWindow = function(callback) {
		chrome.windows.getCurrent(function(browserWindow) {
			if (I.windowsBrowserId[browserWindow.id]) {
				callback(I.windowsBrowserId[browserWindow.id].window);
			}
			else {
				callback(undefined);
			}
		});
	}

	this.openWindow = function(url, callback) {
		chrome.windows.create({'url':url}, function (browserWindow) {
			I.userOpenWindow[browserWindow.id] = callback;
		});
	}

	this.onWindowOpened = function(callback) {
		I.openWindowCallback = callback;
	}
	this.onWindowClosed = function(callback) {
		I.closeWindowCallback = callback;
	}
	
	this.onConnectionStateChanged = function(callback) {
		I.connectionStateChangedCallback = callback;
	}

	I.drawContent = function(content, cpt) {
		function display(type) {
				var ret = "";
				for (var i = 0; i < cpt; i++) {
					ret += "-";
				};
				ret += type;
				return ret;
		}
		content.getIframes().forEach(function(iframe) {
			console.log(display("I"));
			I.drawContent(iframe, cpt + 1);
		});
		content.getPopups().forEach(function(popup) {
			console.log(display("P"));
			I.drawContent(popup, cpt + 1);
		});
	}

	this.drawMap = function() {
		this.getWindows().forEach(function(window) {
			console.log("=====");
			console.log("W")
			window.getTabs().forEach(function(tab) {
				console.log("-T");
				I.drawContent(tab, 2);
				console.log(" ");
			});
			console.log("=====");
		});
	}

	chrome.windows.getAll({populate:true}, function(browserWindows) {
		browserWindows.forEach(function(browserWindow) {
			I.addWindow(browserWindow);
			browserWindow.tabs.forEach(function(tab) {
				I.windowsBrowserId[tab.windowId].fromParent.addTab(tab);
				if (tab.url.substring(0, 9) !== "chrome://")
					chrome.tabs.reload(tab.id);
			});
		});
	});
//=FIN=BrowserManager==//
}

var browserManager = new I.BrowserManager();