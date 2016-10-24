
var UpdateManager = function (rootEl, catalog) {
	var self = this;
	this.catalog = catalog;
	this.qRoot = rootEl;
	this.isOpen = false;
	this.qTitle = this.qRoot.find(".title");
	this.qContent = this.qRoot.find(".content .contentList");
	this.emails = [];
	this.updates = [];
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
	};
	this.getNewUpdates();
	this.launchAnim = function (index) {
		for (var cpt = 1; cpt < 6 && index + cpt < self.updates.length; ++cpt){
			self.updates[index + cpt].animate();
		}
	};
	this.stopAnim = function () {
		self.updates.forEach(function (elem) {
			elem.unanimate();
		});
	};
	this.test = function () {
		self.catalog.apps.forEach(function (catalogApp) {
			self.qContent.append("<div class='updateBox'>" +
								 "	<div class='updateApp'>" +
								 "		<div>" +
								 "			<img src='' >" +
								 "		</div>" +
								 "	</div>" +
								 "	<div class='updateType'>" +
								 "		<div class='updateInfo'>" +
								 "			<p class='title'>New Account </p>" +
								 "			<p>pierre.debruyne@epitech.eu </p>" +
								 "			<p>********** </p>" +
								 "		</div>" +
								 "		<div class='updateButton'>" +
								 "			<button class='updateValidate'><p>Yes</p></button>" +
								 "			<button class='updateCancel'><p>No</p></button>" +
								 "		</div>" +
								 "	</div>" +
								 "</div>");
			var elem = catalogApp.qRoot.clone();
			var updateBox = self.qContent.find('.updateBox').last();
			updateBox.find("img").attr("src", catalogApp.qRoot.find("img").attr("src"));
			self.updates.push(new Update($(updateBox), self, self.updates.length));
		});
		
		self.qRoot.show();
		self.isOpen = true;
		self.qTitle.html(self.catalog.apps.length + " Updates");
		self.catalog.onResize();
	}
	
	//[{url, usr, pass}, {url, logwith, usr}]
}