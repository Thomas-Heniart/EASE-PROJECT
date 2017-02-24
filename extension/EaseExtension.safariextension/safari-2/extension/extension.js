var easeExtension = {
	background:{
		sendMessage:function(msg, fResponse){
			safari.self.addEventListener("message", function(event){
				if(event.name==name){
                    function sendResponse(response){
                        safari.self.tab.dispatchMessage(event.name+" response from tab "+event.message.tab, response);
                    }
					fct(event.message.msg, sendResponse);
				}
			}, false);
		}
	},
	content:{
		sendMessage:function(tabId, msg, fResponse) {
			chrome.tabs.sendMessage(tabId, {"msg": msg, "magicNb": "373B60"}, fResponse);
		}
	},
	onMessage:function(callback){
		safari.self.addEventListener("message", function(event){
			if (event.magicNb === "373B60") {
				function fResponse(response) {
					event.target.page.dispatchMessage("response", response);
				}
				callback(event.msg, fResponse);
			}
		}
	}
}