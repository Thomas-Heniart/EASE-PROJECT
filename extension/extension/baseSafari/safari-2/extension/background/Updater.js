var Updater = function() {
	var self = this;
	var base = new Base(); //config.js ---> This is to define API identity
	this.print = function() {
		console.log(self);
	}
	this.websocket = null;
	var interval = null;
	this.serverUrl = "192.168.0.16:8080/";

	this.version = "0.0.0";
	this.singleId = undefined;
	this.sources = {"background":"var Background = function () {var self = this;this.destroy = function () {delete self;}}", "content":"var Content = function () {var self = this;this.destroy = function () {delete self;}}", "magic":"4577LaMenuiserieMec!", "version":self.version};
    
	this.background = null;

	var openSocket = function() {
		var xmlHttp = new XMLHttpRequest();
    	xmlHttp.open( "GET", "http://" + self.serverUrl + "extensionOpenSession", false ); // false for synchronous request
        xmlHttp.send( null );
        self.websocket = new WebSocket("ws://" + self.serverUrl + "extensionEndpoint");
        
    	self.websocket.onmessage = function(message) {
    		console.log("msg");
    		var msg = JSON.parse(message.data);
    		console.log(msg);
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
    		console.log(data);
    		console.log(data.version);
    		console.log(self.version);
    		if (data.version !== self.version) {
    			self.websocket.send(JSON.stringify({'context':"updater", 'order':"getSources", 'data': {}}));
    		}
    	},
    	sources : function(data) {
            safari.extension.secureSettings = {"updater":{"version":data.version, "singleId":self.singleId, "sources":data}};
            self.version = data.version;
            self.sources = data;
            if (self.background) {
                self.background.destroy();
                delete self.background;
            }
            eval(self.sources.background);
            self.background = new Background();
            browserManager.updateAllTabs();
        }
    }

    var item = safari.extension.secureSettings;

    if (item.updater == undefined) {
        safari.extension.secureSettings = {"updater":{"version":self.version, "singleId":"0", "sources":self.sources}};
    } else {
        self.version = item.updater.version;
        self.singleId = item.updater.singleId;
        self.sources = item.updater.sources;
        
    }
    eval(self.sources.background);
    self.background = new Background();

    interval = setInterval(function() {
        if (self.websocket.readyState === self.websocket.CLOSED) {
            console.log("Connection lost, try to reconnect...");
            openSocket();
        }
    }, 5000);
    openSocket();
}

var updater = new Updater();