var msgQueue = [];

chrome.runtime.onMessage.addListener(function (event, sender, sendResponse) {
	var msg = {"event":event, "sender":sender, "sendResponse":sendResponse};
	msgQueue.push(msg);
	console.log("Msg received.");
	setTimeout(function() {
		msgQueue.shift();
		console.log("Msg deleted.");
	}, 2000);
});

function getMessage(msgName) {
	for (var i = 0; i < msgQueue.length; ++i) {
		if (msgQueue[i].event.name == msgName) {
			return msgQueue[i];
		}
	}
	return null;
};

var logwith = {"fbBtn":null, "lastUrl":null, "tabId":null}

chrome.tabs.onUpdated.addListener(function (tabId, params, newTab) {
	console.log("tabId=" + logwith.tabId + " lastUrl= " + logwith.lastUrl + " newUrl= " + (newTab.url.split("/")[2]).split(".")[1]);
	if (newTab.url.includes("facebook")) {
		console.log("Facebook url opened.");
		var msg = getMessage("clickBtn");
		if (msg != null && msg.sender.tab.id == tabId) {
			logwith.fbBtn = msg.event.msg.btn;
			logwith.lastUrl = msg.event.msg.url;
			logwith.tabId = msg.sender.tab.id;
			console.log("Btn getted!!!");
		}
	} else if (logwith.lastUrl != null && (newTab.url.includes(logwith.lastUrl) || logwith.lastUrl.includes((newTab.url.split("/")[2]).split(".")[1]))) {
		console.log("IL FAUT CHECKER MAINTENANT.");
		logwith = {"fbBtn":null, "lastUrl":null, "tabId":null};
	} else {
		logwith = {"fbBtn":null, "lastUrl":null, "tabId":null};
	}
});

chrome.tabs.onUpdated.addListener(function (tabId, params, newTab) {

	chrome.tabs.executeScript(tabId, {"file":"jquery-3.1.0.js"}, function(){
		chrome.tabs.executeScript(tabId, {"file":"scrapLogwith.js"}, function(){
			console.log("Script sended.");
		});
	});
});

chrome.webRequest.onBeforeRequest.addListener(function(details) {
    	
   	if (details.url.includes("facebook")) {
		console.log("POST to Facebook.");
		var fbPostMsgLtr = function(event, sender, sendResponse) {
			chrome.runtime.onMessage.removeListener(fbPostMsgLtr);
			var msg = getMessage("clickBtn");
			if (msg != null && msg.sender.tab.id == details.tabId) {
				logwith.fbBtn = msg.event.msg.btn;
				logwith.lastUrl = msg.event.msg.url;
				logwith.tabId = msg.sender.tab.id;
				console.log("Btn getted with facebook post!!!");
			}
		}
		chrome.runtime.onMessage.addListener(fbPostMsgLtr);
	}
  	return ;
}, {urls: ["<all_urls>"]}, []);


