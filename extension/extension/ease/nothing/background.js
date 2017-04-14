var Background = function() {

	console.log("creation background");
	global.launchAddWebsite = function() {
		browserManager.getWindows()[1].openTab("https://google.com/", function(tab) {
			console.log("ahahahahah");
			tab.launchAction("addWebsite");
		});
	}

	global.connectWebsite = function(websiteName) {
		I.sendMessageToServer("getConnectJSON", {"websiteName":websiteName});
		I.onMessageFromServer(function(order, data) {
			console.log("JSON RECU:");
				console.log(data);
			if (order === "connectJSON") {
				browserManager.getWindows()[1].openTab(data.json.homePage, function(tab) {
					tab.launchAction("websiteConnect", data);
				});
			} else if (order === "connectJSONRetro") {
				browserManager.getWindows()[1].openTab(data.json.home, function(tab) {
					tab.launchAction("websiteConnectRetro", data);
				});
			}
		});
	}

	global.logoutWebsite = function() {
		I.sendMessageToServer("getConnectJSON", {"websiteName":websiteName});
		I.onMessageFromServer(function(order, data) {
			if (order === "connectJSON") {
				console.log("JSON RECU:");
				console.log(data);
				browserManager.getWindows()[1].openTab(data.json.homePage, function(tab) {
					tab.launchAction("websiteLogout", data);
				});
			}
		});
	}

	this.destroy = function() {
		console.log("destruction background");
	}

	global.launchTheFamScrapping = function() {
		browserManager.getWindows()[0].openTab("https://fellowship.thefamily.co/people#?count=1671&query=&display=1671&scrollOffset=1671", function(tab) {
			tab.launchAction("theFamScrapping");
		});
	}
}
