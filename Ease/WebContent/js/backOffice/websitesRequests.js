var websitesRequestsRow;
var websitesRequests = [];
var WebsiteRequest = function(rootEl, url, date, userName, userEmail, db_id) {
	var self = this;
	this.rootEl = rootEl;
	this.url = url;
	this.date = date;
	this.userName = userName;
	this.userEmail = userEmail;
	this.db_id = db_id;
	this.elem = null;
	this.printOnDocument = function() {
		self.elem = $("<div class='requestedWebsite'>"
						+ "<p>"
						+ "<span class='quit'>x</span>"
						+ self.url
						+ " requested by "
						+ self.userName
						+ " ("
						+ self.date
						+ ")"
						+ "<a href='#' class='done'>Done</a>"
						+ "<a href='#' class='fail'>Fail</a>"
						+ "</p>"
						+ "</div>").appendTo(self.rootEl);
		self.elem.on("click", ".done", self.doneEmail);
		self.elem.on("click", ".fail", self.failEmail);
		self.elem.on("click", ".quit", self.remove);
	}
	this.removeFromDocument = function() {
		self.elem.remove();
	}
	this.remove = function() {
		postHandler.post("EraseRequestedWebsite", {
			db_id: self.db_id
		}, function() {
			
		}, function(data) {
			var index = websitesRequests.indexOf(self);
			if (index == -1)
				return;
			websitesRequests.splice(index, 1);
			self.removeFromDocument();
		}, function(data) {
			
		});
	}
	this.doneEmail = function(e) {
		e.preventDefault();
		var subject = "";
		var emailBody = "";
		window.location = "mailto:" + self.userEmail + "?subject=" + subject + "&body=" + emailBody;
	}
	this.failEmail = function(e) {
		e.preventDefault();
		var subject = "";
		var emailBody = "";
		window.location = "mailto:" + self.userEmail + "?subject=" + subject + "&body=" + emailBody;
	}
}

function loadRequestedWebsites() {
	$.get("WebsiteRequest").done(function(retMsg) {
		printRequestedWebsites(retMsg.substring(4, retMsg.length));
	});
}

$(document).ready(function() {
	websitesRequestsRow = $(".requestedWebsitesView");
	loadRequestedWebsites();
})

function printRequestedWebsites(string) {
	var json = JSON.parse(string);
	json.forEach(function(element) {
		var tmp = new WebsiteRequest(websitesRequestsRow, element.url, element.date, element.userName, element.userEmail, element.db_id);
		tmp.printOnDocument();
		websitesRequests.push(tmp);
	});
}