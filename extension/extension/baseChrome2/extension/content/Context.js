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

I.Context = function () {
	var parentIn = I;
	var I = {};
	var self = this;
	this.print = function() {
		console.log(self);
	}

	I.ContentManager = function (ContentString) {
		var parentIn = {};
		var I = {};
		var self = {};
		this.print = function() {
			console.log(self);
		}
//==BackgroundManager==//
		
		eval(ContentString);
		self = new Content();
		Content = undefined;

		this.update = function(ContentString) {
			self.destroy();
			delete self;	
			eval(ContentString);
    		self = new Content();
    		Content = undefined;
		}

//=FIN=BackgroundManager==//
	}

	I.ActionManager = function(actionName, parentIn, memory) {
		var parentIn = parentIn;
		var I = {};
		var self;


		I.onMessage = function(callback) {
			this.onMessage = callback;
		}

		I.sendMessage = function(msgName, msg, onResponse) {
			chrome.runtime.sendMessage({'magic': IDENTITY.getMagic(), 'type':"message", 'actionName':actionName, 'msgName':msgName, 'msg':msg}, onResponse);
		}

		I.finish = function() {
			chrome.runtime.sendMessage({'magic': IDENTITY.getMagic(), 'type':"actionFinished", 'actionName':actionName});
		}

		I.import = function(path, callback) {
			tool.getCode(path, function(code) {
				eval(code);
				callback();
			});
		}

		I.memory = memory;

		I.pushMemory = function() {
			chrome.runtime.sendMessage({'magic': IDENTITY.getMagic(), 'type':"pushMemory", 'actionName':actionName, "memory":I.memory});
		}

		chrome.storage.local.get("updater", function(item) {
			parentIn = {};
			eval(item.updater.actions.content[actionName]);
			self = new Content();
		});
		
		this.destroy = function() {
			self.destroy();
		}
	}
//==Context==//

	I.singleId = tool.generateSingleId();

	I.actions = {};

	if (window.self !== window.top) {
		I.type = "iframe";
		I.parent = window.parent;

	} else if (window.opener != undefined) {
		I.type = "popup";
		I.parent = window.opener;

	} else {
		I.type = "tab";
		I.actions = [];
	}
	this.getType = function() {
		return I.type;
	}


	if (I.type === "iframe" || I.type === "popup") {
		I.initContentToBackground = function(pathTab) {
			pathTab.unshift({"type":I.type, "singleId":I.singleId});
			I.parent.postMessage({'magic': IDENTITY.getMagic(), 'name': "childInit", 'pathTab':pathTab}, "*");
		}
		I.closeContentToBAckground = function(pathTab) {
			pathTab.unshift({"type":I.type, "singleId":I.singleId});
			I.parent.postMessage({'magic': IDENTITY.getMagic(), 'name': "childClose", 'pathTab':pathTab}, "*");
		}
	} else {
		I.initContentToBackground = function(pathTab) {
			console.log('send new content');
			pathTab.unshift({"type":I.type, "singleId":I.singleId});
			chrome.runtime.sendMessage({'magic': IDENTITY.getMagic(), 'type':"newContent", 'pathTab':pathTab, 'url':window.location.href });
		}
		I.closeContentToBAckground = function(pathTab) {
			pathTab.unshift({"type":I.type, "singleId":I.singleId});
			chrome.runtime.sendMessage({'magic': IDENTITY.getMagic(), 'type':"closeContent", 'pathTab':pathTab});
		}
	}
	I.windowMessageHandler = {
		childInit : function(msg) {
			I.initContentToBackground(msg.pathTab);
		},
		childClose : function(msg) {
			I.closeContentToBAckground(msg.pathTab);
		}
	}
	window.addEventListener("message", function(event) {
		var msg = event.data;
		if (msg.magic === IDENTITY.getMagic()) {
			I.windowMessageHandler[msg.name](msg);
		}
	});
	I.initContentToBackground([]);

	window.onbeforeunload = function(event) {
		I.closeContentToBAckground([]);
	};


	I.backgroundMessageHandler = {
		update : function(msg) {
			console.log("updated!!");
			chrome.storage.local.get("updater", function(item) {
				I.content.update(item.updater.content);
			});
		},
		launchAction : function(msg){
			I.actions[msg.actionName] = new I.ActionManager(msg.request.actionName, I, msg.request.memory);
		},
		message : function(msg) {
			if (I.actions[msg.request.actionName]) {
				if (I.actions[msg.request.actionName].onMessage)
					I.actions[msg.request.actionName].onMessage(msg.request.msgName, msg.request.msg);
			}
		}
	}

	chrome.runtime.onMessage.addListener(function(request, sender) {
		if (request.magic === IDENTITY.getMagic()) {
			if (I.backgroundMessageHandler[request.type]) {
				I.backgroundMessageHandler[request.type]({'request':request, 'sender':sender});
			}
		}
	});

	chrome.storage.local.get("updater", function(item) {
		I.content = new I.ContentManager(item.updater.content);
	});


//=FIN=Context==//

}

var context = new I.Context();