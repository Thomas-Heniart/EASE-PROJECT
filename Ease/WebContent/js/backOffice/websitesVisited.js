var websitesVisited = [];
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
		this.initWithElem = function(elem) {
			self.elem = elem.appendTo(self.rootEl);
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

var WebsiteDone = function(rootEl, url) {
	var self = this;
	this.rootEl = rootEl;
	this.url = url;
	this.elem = null;
	this.printOnDocument = function() {
		self.elem = $("<div>"
			+ "<i class='fa fa-times'></i>"
			+ self.url
			+ "</div>").appendTo(self.rootEl);
		$("i", self.elem).click(self.remove);
	}
	this.initWithElem = function(elem) {
		self.elem = elem.appendTo(self.rootEl);
	}
	this.remove = function() {
		self.removeFromDocument();
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
			var tmp = new WebsiteDone(websitesDoneRow, websiteDone.url);
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
		loadBlacklistedWebsites();
	});
});