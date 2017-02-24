var easeExtension = {
	background:{
		sendMessage:function(msg, fResponse){
			chrome.runtime.sendMessage({"msg": msg, "magicNb": "373B60"}, fResponse);
		}
	},
	content:{
		sendMessage:function(tabId, msg, fResponse) {
			chrome.tabs.sendMessage(tabId, {"msg": msg, "magicNb": "373B60"}, fResponse);
		}
	},
	onMessage:function(callback){
		chrome.runtime.onMessage.addListener(function(event, sender, sendResponse){
			if (event.magicNb === "373B60") {
				callback(event.msg, sendResponse);
			}
		});
	}
}