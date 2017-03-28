var websitesRequestsRow;
var websitesRequests = [];
var editRequestUrlPopup;
var websitesSelected = [];
var editedUrls = [];

var EditRequestUrlPopup = function(rootEl) {
	var self = this;
	this.rootEl = rootEl;
	this.parentHandler = this.rootEl.closest('#easePopupsHandler');
	this.errorRowHandler = $('.errorHandler', this.rootEl);
	this.goBackButtonHandler = $("#goBack", this.rootEl);
	this.urlInput = $("#websiteUrl", this.rootEl);
	this.submitButton = $("button[type='submit']", this.rootEl);
	this.emailSenderRow = $("#emailsToSend", this.rootEl);
	this.selectedRequest = null;
	this.submitButton.click(self.nextStep);
	var i = 0;
	this.urlInput.on("input", function() {
		if (self.urlInput.val() == null || self.urlInput.val() === "")
			self.submitButton.addClass("locked");
		else
			self.submitButton.removeClass("locked");
	})
	this.open = function() {
		if (websitesSelected.length == 0)
			return;
		currentAdminPopup = self;
		self.goToNextStep();
		self.submitButton.removeClass("locked");
		self.parentHandler.addClass('myshow');
		self.rootEl.addClass('show');
	}
	this.nextStep = function() {
		console.log("haha");
		var editedUrl = self.urlInput.val();
		postHandler.post("EditRequestWebsiteUrl", {
			"db_id": self.selectedRequest.db_id,
			"url": editedUrl
		}, function() {
			
		}, function(data) {
			self.selectedRequest.setUrl(editedUrl);
			i++
			if (i < websitesSelected.length)
				self.goToNextStep();
			else
				self.goToSubmit();
		});
	}
	this.goToNextStep = function() {
		self.selectedRequest = websitesSelected[i];
		self.urlInput.val(self.selectedRequest.url);
	}
	this.goToSubmit = function() {
		self.printEmails();
		self.submitButton.text("Send emails");
	}
	this.printEmails = function() {
		self.urlInput.parent().addClass("hidden");
		self.emailSenderRow.removeClass("hidden");
		self.websitesSelected.forEach(function(websiteRequest) {
			self.emailSenderRow.append("<div class='request'>"
											+ websiteRequest.url
											+ " "
											+ websiteRequest.userEmail);
		});
	}
	this.submit = function() {
		if (self.submitButton.hasClass("locked"))
			return;
		var editedUrl = self.urlInput.val();
		postHandler.post("SendWebsiteIntegratedEmail", {
			db_id: self.selectedRequest.db_id,
			website: editedUrl,
			name: self.selectedRequest.userName,
			email: self.selectedRequest.userEmail
		}, function() {
		}, function(data) {
			self.selectedRequest.remove();
			self.reset();
			self.close();
		}, function(data) {
			$("p", self.errorRowHandler).text(data);
		});
	}
	this.reset = function() {
		self.selectedRequest = null;
		self.urlInput.val("");
		self.submitButton.addClass("locked");
	}
	this.close = function() {
		self.rootEl.removeClass("show");
		self.parentHandler.removeClass("myshow");
	}
}

var WebsiteRequest = function(rootEl, url, date, userName, userEmail, db_id) {
	var self = this;
	this.rootEl = rootEl;
	this.url = url;
	this.date = date;
	this.userName = userName;
	this.userEmail = userEmail;
	this.db_id = db_id;
	this.elem = null;
	this.selected = false;
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
						+ "</p>"
						+ "</div>").appendTo(self.rootEl);
		self.elem.on("click", ".quit", self.remove);
		self.elem.on("click", self.select);
	}
	this.removeFromDocument = function() {
		self.elem.remove();
	}
	this.select = function() {
		self.elem.toggleClass("selected");
		self.selected = !self.selected;
		if (self.selected)
			websitesSelected.push(self);
		else {
			var index = websitesSelected.indexOf(self);
			if (index == - 1)
				return;
			websitesSelected.splice(index, 1);
		}
	}
	this.remove = function(e) {
		e.stopPropagation();
		postHandler.post("EraseRequestedWebsite", {
			db_id: self.db_id
		}, function() {
		}, function(data) {
			var index = websitesSelected.indexOf(self);
			if (index > - 1)
				websitesSelected.splice(index, 1);
			index = websitesRequests.indexOf(self);
			if (index == -1)
				return;
			websitesRequests.splice(index, 1);
			self.removeFromDocument();
		}, function(data) {
			
		});
	}
	this.setUrl = function(url) {
		self.url = url;
		//Front modifications TODO
	}
	this.doneEmail = function(e) {
		e.preventDefault();
		editRequestUrlPopup.open(self);
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

function printRequestedWebsites(string) {
	var json = JSON.parse(string);
	json.forEach(function(element) {
		var tmp = new WebsiteRequest(websitesRequestsRow, element.url, element.date, element.userName, element.userEmail, element.db_id);
		tmp.printOnDocument();
		websitesRequests.push(tmp);
	});
}

$(document).ready(function() {
	websitesRequestsRow = $(".requestedWebsitesView");
	loadRequestedWebsites();
	editRequestUrlPopup = new EditRequestUrlPopup($("#editRequestedWebsitePopup"));
	$("#done").click(function() {
		editRequestUrlPopup.open();
	})
})