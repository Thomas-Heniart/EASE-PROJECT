
var UpdateManager = function (rootEl, catalog) {
	var self = this;
	this.catalog = catalog;
	this.qRoot = rootEl;
	this.qContent = this.qRoot.find(".content");
	this.emails = [];
	$(".userEmail").each(function (index, elem) {
		self.emails.push($(elem).find('p').html());
	});
	this.getNewUpdates = function() {
		event = new CustomEvent("GetUpdates", self.emails);
	    document.dispatchEvent(event);
	};
	this.qRoot.on("NewUpdates", self.newMessageHandler, false);
	this.newMessageHandler = function (e) {
		e.detail.forEach(function (elem) {
			var catalogApp = null;
			if ((catalogApp = self.catalog.haveThisUrl(elem.url)) != null) {
				self.displayUpdate(catalogApp);
			}
		});
	};
	this.displayUpdate = function(catalogApp) {
		var updateBox = self.qContent.add("<div class='updateBox'> </div>")
		var $elem = catalogApp.qRoot.clone();
		updateBox.html($elem);
		updateBox.add("<div><p> lalal</p></div>");
	}
	this.getNewUpdates();
	this.test = function () {
		self.catalog.apps.forEach(function (catalogApp) {
			self.qContent.append("<div class='updateBox'> </div>")
			var elem = catalogApp.qRoot.clone();
			self.qContent.find('.updateBox').last().append(elem);
			//updateBox.add("<div><p> lalal</p></div>");
		});
		this.qRoot.show();
	}
	
	//[{url, usr, pass}, {url, logwith, usr}]
}