var websitesVisited = [];
var websitesBroken = [];
var websitesDone = [];
var blacklistedWebsites = [];
var resultsRow;
var websitesDoneRow;
var blacklistRow;

var WebsiteVisited = function(rootEl, url, count, single_id) {
		var self = this;
		this.rootEl = rootEl;
		this.url = url;
		this.count = count;
		this.single_id = single_id
		this.elem = null;
		this.printOnDocument = function() {
			self.elem = $("<div>"
				+ "<button>Blacklist</button>"
				+ self.url
				+ ((count == null && count) > 0 ? "" : (" (" + self.count + ")"))
				+ "</div>").appendTo(self.rootEl);
			$("button", self.elem).click(self.remove);
		}
		this.remove = function() {
			postHandler.post("BlacklistWebsite", {
				single_id: self.single_id
			}, function() {
				
			}, function(data) {
				var index = websitesVisited.indexOf(self);
				if (index == -1)
					return;
				self.removeFromDocument();
				websitesVisited.splice(index, 1);
				loadBlacklistedWebsites();
			}, function(data) {
				
			});
		}
		this.removeFromDocument = function() {
			self.elem.remove();
		}
}

var WebsiteBroken = function(rootEl, url, count, single_id) {
	var self = this;
	this.rootEl = rootEl;
	this.url = url;
	this.count = count;
	this.single_id = single_id;
	this.printOnDocument = function() {
		self.elem = $("<div>"
			+ "<button>TurnOn</button>"
			+ self.url
			+ ((count == null && count) > 0 ? "" : (" (" + self.count + ")"))
			+ "</div>").prependTo(self.rootEl);
		$("button", self.elem).click(self.remove);
	}
	this.remove = function() {
		postHandler.post("TurnOnWebsite", {
			single_id: self.single_id
		}, function() {
			
		}, function(data) {
			var index = websitesBroken.indexOf(self);
			if (index == -1)
				return;
			self.removeFromDocument();
			websitesBroken.splice(index, 1);
			loadWebsitesDone();
		}, function(data) {
			
		});
	}
	this.removeFromDocument = function() {
		self.elem.remove();
	}
}

var WebsiteDone = function(rootEl, url, count, single_id) {
	var self = this;
	this.rootEl = rootEl;
	this.url = url;
	this.count = count;
	this.single_id = single_id;
	this.elem = null;
	this.printOnDocument = function() {
		self.elem = $("<div>"
			+ "<button>TurnOff</button>"
			+ self.url
			+ ((count == null && count) > 0 ? "" : (" (" + self.count + ")"))
			+ "</div>").appendTo(self.rootEl);
		$("button", self.elem).click(self.remove);
	}
	this.remove = function() {
		postHandler.post("TurnOffWebsite", {
			single_id: single_id
		}, function() {
			
		}, function(data) {
			var index = websitesDone.indexOf(self);
			if (index == -1)
				return;
			self.removeFromDocument();
			websitesDone.splice(index, 1);
			loadWebsitesBroken();
		}, function(data) {
			
		});
	}
	this.removeFromDocument = function() {
		self.elem.remove();
	}
}

var BlacklistedWebsite = function(rootEl, url, count, single_id) {
	var self = this;
	this.rootEl = rootEl;
	this.url = url;
	this.count = count;
	this.single_id = single_id;
	this.elem = null;
	this.printOnDocument = function() {
		self.elem = $("<div>"
			+ "<button>Remove from blacklist</button>"
			+ self.url
			+ ((count == null && count) > 0 ? "" : (" (" + self.count + ")"))
			+ "</div>").appendTo(self.rootEl);
		$("button", self.elem).click(self.remove);
	}
	this.remove = function() {
		postHandler.post("RemoveFromBlackList", {
			single_id: self.single_id
		}, function() {
			
		}, function(data) {
			var index = blacklistedWebsites.indexOf(self);
			if (index == -1)
				return;
			self.removeFromDocument();
			blacklistedWebsites.splice(index, 1);
			loadWebsitesVisited();
		}, function(data) {
			
		});
	}
	this.removeFromDocument = function() {
		self.elem.remove();
	}
}

function loadWebsitesBroken() {
	postHandler.post("GetWebsitesBroken", {
		
	}, function() {
		websitesBroken.forEach(function(websiteBroken) {
			websiteBroken.removeFromDocument();
		});
	}, function(data) {
		var json = JSON.parse(data);
		json.forEach(function(websiteBroken) {
			console.log(websiteBroken);
			var tmp = new WebsiteBroken(resultsRow, websiteBroken.url, websiteBroken.count, websiteBroken.single_id);
			websitesBroken.push(tmp);
			tmp.printOnDocument();
		});
	}, function(data) {
		
	});
}

function loadWebsitesVisited() {
	postHandler.post("GetWebsitesVisited", {
		
	}, function() {
		websitesVisited.forEach(function(websiteVisited) {
			websiteVisited.removeFromDocument();
		});
	}, function(data) {
		var json = JSON.parse(data);
		json.forEach(function(websiteVisited) {
			var tmp = new WebsiteVisited(resultsRow, websiteVisited.url, websiteVisited.count, websiteVisited.single_id);
			websitesVisited.push(tmp);
			tmp.printOnDocument();
		});
	}, function(data) {
		
	});
}

function loadWebsitesDone() {
postHandler.post("GetWebsitesDone", {
		
	}, function() {
		websitesDone.forEach(function(websiteDone) {
			websiteDone.removeFromDocument();
		});
	}, function(data) {
		var json = JSON.parse(data);
		json.forEach(function(websiteDone) {
			var tmp = new WebsiteDone(websitesDoneRow, websiteDone.url, websiteDone.count, websiteDone.single_id);
			websitesDone.push(tmp);
			tmp.printOnDocument();
		});
	}, function(data) {
		
	});
}

function loadBlacklistedWebsites() {
	postHandler.post("GetBlacklistedWebsites", {
		
	}, function() {
		blacklistedWebsites.forEach(function(blacklistedWebsite) {
			blacklistedWebsite.removeFromDocument();
		});
	}, function(data) {
		var json = JSON.parse(data);
		json.forEach(function(blacklistedWebsite) {
			var tmp = new BlacklistedWebsite(blacklistRow, blacklistedWebsite.url, blacklistedWebsite.count, blacklistedWebsite.single_id);
			blacklistedWebsites.push(tmp);
			tmp.printOnDocument();
		});
	}, function(data) {
		
	});
}

$(document).ready(function() {
	websitesDoneRow = $("#WebsitesVisitedTab #websitesDone");
	resultsRow = $("#WebsitesVisitedTab #results");
	blacklistRow = $("#WebsitesVisitedTab #blacklist");
	$(".adminButton[target='WebsitesVisitedTab']").click(function() {
		loadWebsitesVisited();
		loadWebsitesDone();
		loadWebsitesBroken();
		loadBlacklistedWebsites();
	});
});