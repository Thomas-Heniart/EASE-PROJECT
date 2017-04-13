

var background = undefined;

var Updater = function() {
	var self = this;
	var base = new Base(); //config.js ---> This is to define API identity
	this.print = function() {
		console.log(self);
	}
	this.websocket = null;
	var interval = null;
	this.serverUrl = "localhost:8080/";

	this.version = "0.0.0";
	this.singleId = undefined;
	this.sources = {"background":"var Background = function () {var self = this;this.destroy = function () {delete self;}}", "content":"var Content = function () {var self = this;this.destroy = function () {delete self;}}", "magic":"4577LaMenuiserieMec!", "version":self.version};


	chrome.storage.local.get("updater", function(item) {
		if (item.updater == undefined) {
			chrome.storage.local.set({"updater":{"version":self.version, "singleId":"0", "sources":self.sources}});
		} else {
			self.version = item.updater.version;
			self.singleId = item.updater.singleId;
			self.sources = item.updater.sources;
		}
		eval(self.sources.background);
		background = new Background();
		
		interval = setInterval(function() {
			if (self.websocket.readyState === self.websocket.CLOSED) {
				console.log("Connection lost, try to reconnect...");
				openSocket();
			}
		}, 5000);
		openSocket();
	});

	var openSocket = function() {
		var xmlHttp = new XMLHttpRequest();
    	xmlHttp.open( "GET", "http://" + self.serverUrl + "extensionOpenSession", false ); // false for synchronous request
    	xmlHttp.send( null );
    	self.websocket = new WebSocket("ws://" + self.serverUrl + "extensionEndpoint");
    	self.websocket.onmessage = function(message) {
    		var msg = JSON.parse(message.data);
    		if (msg.context === "socket") {
    			if (socketMsgHandler[msg.order] != undefined)
    				socketMsgHandler[msg.order](msg.data);
    		} else if (msg.context === "updater") {
    			if (updaterMsgHandler[msg.order] != undefined)
    				updaterMsgHandler[msg.order](msg.data);
    		}
    	}
    	self.websocket.onopen = function() {
    		console.log("open");
    	}
    	self.websocket.onclose = function() {
    		console.log("close");
    	}
    	self.websocket.onerror = function() {
    		console.log("error");
    	}
    }

    var socketMsgHandler = {
    	welcome : function(data) {
    		self.websocket.send(JSON.stringify({'context':"socket", 'order':"lastSingleId", 'data': {'lastSingleId': self.singleId}}));
    		self.singleId = data.singleId;
    	}
    }
    var updaterMsgHandler = {
    	update : function(data) {
    		if (data.version !== self.version) {
    			self.websocket.send(JSON.stringify({'context':"updater", 'order':"getSources", 'data': {}}));
    		}
    	},
    	sources : function(data) {
    		function launchUpdate() {
    			chrome.storage.local.set({"updater":{"version":data.version, "singleId":self.singleId, "sources":data}}, function() {
    				self.version = data.version;
    				self.sources = data;
    				if (background) {
    					background.destroy();
    					delete background;
    				}
    				eval(self.sources.background);
    				background = new Background();
    				browserManager.updateAllTabs();
    			});
    		}
    		if (jQuery.isEmptyObject(tabActions)) {
				launchUpdate();
			} else {
				notActiveCallback.push(launchUpdate);
			}
    	}
    }
}

var updater = new Updater();