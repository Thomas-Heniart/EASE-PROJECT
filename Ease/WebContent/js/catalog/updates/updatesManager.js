
var UpdateManager = function (rootEl, catalog) {
	var self = this;
	this.catalog = catalog;
	this.qRoot = rootEl;
	this.isOpen = false;
	this.qTitle = this.qRoot.find(".catalogHeader p");
	this.qContent = this.qRoot.find(".content .contentList");
	this.emails = [];
	this.updates = [];
	$(".emailLine input").each(function (index, elem) {
		self.emails.push($(elem).val());
	});
	this.getNewUpdates = function() {
		setTimeout( function () {
			event = new CustomEvent("GetUpdates", {"detail": self.emails});
		    document.dispatchEvent(event);
		}, 500);
	};
	$(document).on("NewUpdates", function (e) {
		e.detail.forEach(function (elem) {
			var catalogApp = null;
			if ((catalogApps = self.catalog.haveThisUrl(elem.website)) != null) {
				catalogApps.forEach(function (catalogApp) {
					var haveThisApp = false;
					ease.apps.forEach(function (app) {
						if (app.webId == catalogApp.id && app.login == elem.user) {
							haveThisApp = true;
						}
					});
					if (!haveThisApp) {
						self.displayUpdate(catalogApp, elem);
						if (self.isOpen == false) {
							self.qRoot.show();
							self.isOpen = true;
							self.catalog.onResize();
						}
						self.qTitle.html(self.updates.length + " Update" + ((self.updates.length > 1) ? "s" : "") +" available");
					}
				});	
			}
		});
		$('.updateBox').draggable({
			cursor : 'move',
			cursorAt : {
				left : 25,
				top : 25
			},
			appendTo: "body",
			helper : function(e, ui) {
				easeHiddenProfile.rootEl.on('mouseenter', function(){
					easeHiddenProfile.show();
				});
				var ret;
				ret = $('<div class="dragHelperLogo" style="position: fixed; z-index: 3;pointer-events:none;"/>');
				ret.attr("type", "update");
				ret.append($('<img />'));
				ret.attr("siteId", $(this).find('.updateApp').attr('siteId'));
				ret.attr("login", $(this).find(".updateType .updateInfo p.updateLogin").html());
				ret.attr("cryptedPassword", $(this).attr("crypt"));
				ret.attr("siteName", $(this).find('.updateApp').attr('siteName'));
				ret.find('img').attr("src", $(this).find('img').attr("src"));
				return ret;
			}
		});
	});
	
	this.displayUpdate = function(catalogApp, elem) {
		self.qContent.append("<div class='updateBox' crypt='" + elem.password + "'>" +
				 "	<div class='updateApp' siteId='" + catalogApp.id + "' siteName='" + catalogApp.name + "'>" +
				 "		<div>" +
				 "			<img src='' >" +
				 "			<div class='cancel'>" + 
				 "				<i class='fa fa-times' aria-hidden='true'></i>" +
				 "			</div>" +
				 "		</div>" +
				 "	</div>" +
				 "	<div class='updateType'>" +
				 "		<div class='updateInfo'>" +
				 "			<p class='title'><u>New account</u>: " + catalogApp.name + "</p>" +
				 "			<p class='updateLogin'>" + elem.user + " </p>" +
				 "			<p>" + ((elem.password != null) ? "********" : "elem.logWith") + " </p>" +
				 "		</div>" +
				 " 	</div>" +
				 "</div>");

		var updateBox = self.qContent.find('.updateBox').last();
		updateBox.find("img").attr("src", catalogApp.qRoot.find("img").attr("src"));
		self.updates.push(new Update($(updateBox), self, self.updates.length));
	};
	//this.getNewUpdates();
	this.launchAnim = function (index) {
		for (var cpt = 1; cpt < 6 && index + cpt < self.updates.length; ++cpt){
			self.updates[index + cpt].animate();
		}
	};
	this.stopAnim = function (index) {
		self.updates.forEach(function (elem) {
			elem.unanimate();
		});
		delete self.updates[index];
		self.updates.splice(index, 1);
		for (var cpt = index; cpt < self.updates.length; ++cpt) {
			self.updates[cpt].index--;
		}
		if (!self.updates.length) {
			self.qRoot.hide();
			self.isOpen = false;
			self.catalog.onResize();
		}
		self.qTitle.html(self.updates.length + " Update" + ((self.updates.length > 1) ? "s" : "") +" available");
	};
	this.test = function () {
		self.catalog.apps.forEach(function (catalogApp) {
			self.qContent.append("<div class='updateBox'>" +
								 "	<div class='updateApp'>" +
								 "		<div>" +
								 "			<img src='' >" +
								 "			<div class='cancel'>" + 
								 "				<i class='fa fa-times' aria-hidden='true'></i>" +
								 "			</div>" +
								 "		</div>" +
								 "	</div>" +
								 "	<div class='updateType'>" +
								 "		<div class='updateInfo'>" +
								 "			<p class='title'>New Account </p>" +
								 "			<p>pierre.debruyne@epitech.eu </p>" +
								 "			<p>********** </p>" +
								 "		</div>" +
								 " 	</div>" +
								 "</div>");
			
			var elem = catalogApp.qRoot.clone();
			var updateBox = self.qContent.find('.updateBox').last();
			updateBox.find("img").attr("src", catalogApp.qRoot.find("img").attr("src"));
			self.updates.push(new Update($(updateBox), self, self.updates.length));
		});
		
		self.qRoot.show();
		self.isOpen = true;
		self.qTitle.html(self.catalog.apps.length + " Updates available");
		self.catalog.onResize();
	}
	
	//[{url, usr, pass}, {url, logwith, usr}]
}
