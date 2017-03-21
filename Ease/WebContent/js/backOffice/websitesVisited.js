var WebsiteVisited = function(rootEl, url, count) {
		var self = this;
		this.rootEl = rootEl;
		this.url = url;
		this.count = count;
		this.elem = null;
		this.printOnDocument = function() {
			self.elem = $("<div>"
				+ "<button>Blacklist</button>"
				+ self.url
				+ (count == null ? "" : (" (" + self.count + ")"))
				+ "</div>").appendTo(self.rootEl);
			$("button", self.elem).click(self.remove);
		}
		this.initWithElem = function(elem) {
			self.elem = elem.appendTo(self.rootEl);
		}
		this.remove = function() {
			postHandler.post("BlacklistWebsite", {
				url: self.url
			}, function() {
				
			}, function(data) {
				self.elem.remove();
			}, function(data) {
				
			});
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
		self.elem.remove();
	}
}

var websitesVisited = [];
var websitesDone = [];
var resultsRow;
var websitesDoneRow;

function loadWebsitesVisited() {
	postHandler.post("GetWebsitesVisited", {
		
	}, function() {
		websitesVisited.forEach(function(websiteVisited) {
			websiteVisited.remove();
		});
	}, function(data) {
		var json = JSON.parse(data);
		console.log(resultsRow);
		json.forEach(function(websiteVisited) {
			var tmp = new WebsiteVisited(resultsRow, websiteVisited.url, websiteVisited.count);
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
			websiteDone.remove();
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

$(document).ready(function() {
	websitesDoneRow = $("#WebsitesVisitedTab #websitesDone");
	resultsRow = $("#WebsitesVisitedTab #results");
	$(".adminButton[target='WebsitesVisitedTab']").click(function() {
		loadWebsitesVisited();
		loadWebsitesDone();
	});
});