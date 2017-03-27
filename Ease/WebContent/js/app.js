var easeApp = function(rootEl){
	var self = this;
	this.rootEl = rootEl;
	this.image = this.rootEl.find('.linkImage');
	this.nameArea = this.rootEl.find('.siteName p');
	this.name = this.rootEl.attr('name');
	this.id = this.rootEl.attr('id');
	this.webId = this.rootEl.attr('webid');
	this.login = this.rootEl.attr('login');
	this.increaseCatalagAppCount = function() {
		var x = parseInt($(".catalogApp[idx='" + self.webId + "'] span.apps-integrated i.count").html());
		$(".catalogApp[idx='" + self.webId + "'] span.apps-integrated i.count").html(x+1);
		$(".catalogApp[idx='" + self.webId + "'] span.apps-integrated").addClass("showCounter");
	};
	self.increaseCatalagAppCount();
	this.setName = function(name){
		self.name = name;
		self.nameArea.text(name);
		self.rootEl.attr('name', name);
	};
	this.setId = function(id){
		self.id = id;
		self.rootEl.attr('id', id);
	};
	this.remove = function(){
		easeApps.splice(apps.indexOf(self), 1);
		self.rootEl.remove();
	};
	this.image.on('click', function(){
		sendEvent(this);
	});
	this.rootEl.find('.modifyAppButton').length && this.rootEl.find('.modifyAppButton').on('click', function(e){
		showModifyAppPopup(this, e);
	});
	this.rootEl.find('.deleteAppButton').length && this.rootEl.find('.deleteAppButton').on('click', function(e){
		showConfirmDeleteAppPopup(this, e);
	});
}

var easeApps = [];
var easeAppsManager;

var appsManager = function(){
	var self = this;
	this.getAppsBySsoId = function(val){
		var retVal = [];
		for (var i = 0; i < easeApps.length; i++) {
			if (easeApps[i].ssoId == val && !(easeApps[i].isEmpty))
				retVal.push(easeApps[i]);
		}
		return retVal;
	}
	this.getAppsByLogin = function(val){
		var retVal = [];
		for (var i = 0; i < easeApps.length; i++) {
			if (easeApps[i].login == val)
				retVal.push(easeApps[i]);
		}
		return retVal;
	}
	this.getAppsByWebsiteId = function(val){
		var retVal = [];
		for (var i = 0; i < easeApps.length; i++) {
			if (easeApps[i].websiteId == val)
				retVal.push(easeApps[i]);
		}
		return retVal;
	}
	this.getAppsByWebsiteIdNotEmpty = function(val){
		var retVal = [];
		for (var i = 0; i < easeApps.length; i++) {
			if (easeApps[i].websiteId == val && !(easeApps[i].isEmpty))
				retVal.push(easeApps[i]);
		}
		return retVal;
	}
	this.getAppById = function(val){
		for (var i = 0; i < easeApps.length; i++) {
			if (easeApps[i].id == val)
				return easeApps[i];
		}
		return null;
	}
	this.getAppByLoginAndSsoId = function(login, ssoId){
		for (var i = 0; i < easeApps.length; i++) {
			if (easeApps[i].ssoId == ssoId && easeApps[i].getAccountInformationValue("login") == login)
				return easeApps[i];
		}
		return null;
	}
	this.getAppsByLoginAndSsoId = function(login, ssoId){
		var retVal = [];
		for (var i = 0; i < easeApps.length; i++) {
			if (easeApps[i].ssoId == ssoId && easeApps[i].getAccountInformationValue("login") == login)
				retVal.push(easeApps[i]);
		}
		return retVal;		
	}
	this.removeApp = function(app){
		easeApps.splice(easeApps.indexOf(app), 1);
	}
	this.addApp = function(app){
		easeApps.push(app);
	}
};

$(document).ready(function(){
	easeAppsManager = new appsManager();
});

var MyApp = function(){
	var self = this;
	this.qRoot = null;
	this.logoHandler;
	this.appNameHandler;
	this.settingsButton;
	this.modifyAppButton;
	this.deleteAppButton;
	this.imageArea;
	this.imgSrc = '';
	this.login= '';
	this.id = '';
	this.accountInformations = [];
	this.ssoId = -1;
	this.name;
	this.websiteId;
	this.logWith = '';
	this.url = "";
	this.currentProfile = null;
	this.isEmpty = false;

	this.changeName = function(name){
		self.appNameHandler.text(name);
		self.name = name;
	}
	this.init = function(logWith, accountInformations, catalogId, name, id, ssoId, canMove, imgSrc, url){
		self.qRoot = $($('#boxHelper').html());
		self.qRoot.attr('logwith', logWith.length ? logWith : "false");
		self.qRoot.attr('webid', catalogId);
		self.qRoot.attr('id', id);
		self.qRoot.attr('ssoid', ssoId);
		self.qRoot.attr('move', canMove);
		self.qRoot.attr('name', name);
		self.accountInformations = accountInformations;
		self.id = id;
		self.ssoId = ssoId;
		self.name = name;
		self.websiteId = catalogId;
		self.logWith = logWith;
		self.logoHandler = self.qRoot.find('img.logo');
		self.logoHandler.attr('src', imgSrc);
		self.imgSrc = imgSrc;
		self.url = url;
		self.appNameHandler = self.qRoot.find('.siteName p');
		self.settingsButton = self.qRoot.find('.showAppActionsButton');
		self.imageArea = self.qRoot.find('.linkImage');
		self.appNameHandler.text(name);
		self.imageArea.click(function(){
			sendEvent(this);
		});
		self.settingsButton.click(function(e){
			e.preventDefault();
			e.stopPropagation();
			easeModifyAppPopup.open(self);
		});
		return self;
	};
	this.initAccountInformations = function() {
		postHandler.post("GetAccountInformations", {
			appId: self.id
		}, function() {
			
		}, function(data) {
			self.accountInformations = JSON.parse(data);
		}, function(data) {
			
		});
	};
	this.getAccountInformationValue = function(name) {
		for(var i=0; i<self.accountInformations.length; i++) {
			var info = self.accountInformations[i];
			if (info.name === name)
				return info.value;
		}
		return "";
	}
	this.setAccountInformationValue = function(name, value) {
		for(var i=0; i<self.accountInformations.length; i++) {
			var info = self.accountInformations[i];
			if (info.name === name) {
				info.value = value;
				return;
			}
		}
	}
	this.initWithQRoot = function(rootEl){
		self.qRoot = $(rootEl);
		self.name = self.qRoot.attr('name');
		self.id = self.qRoot.attr('id');
		self.websiteId = self.qRoot.attr('webid');
		self.ssoId = self.qRoot.attr('ssoid');
		self.logWith = self.qRoot.attr('logwith');
		self.logoHandler = self.qRoot.find('img.logo');
		self.imgSrc = self.logoHandler.attr('lazy-src');
		self.initAccountInformations();
		if (self.qRoot.attr('url')){
			self.url = self.qRoot.attr('url');
			self.qRoot.attr('url', '');
		}
		if (self.qRoot.hasClass('emptyApp')){
			self.isEmpty = true;
			self.qRoot.find('.emptyAppIndicator').click(function(e){
				e.preventDefault();
				e.stopPropagation();
				easeModifyAppPopup.open(self);
			});
		}
		self.appNameHandler = self.qRoot.find('.siteName p');
		self.settingsButton = self.qRoot.find('.showAppActionsButton');
		self.imageArea = self.qRoot.find('.linkImage');
		self.imageArea.click(function(){
			sendEvent(this);
		});
		self.settingsButton.click(function(e){
			e.preventDefault();
			e.stopPropagation();
			easeModifyAppPopup.open(self);
		});
		return self;
	}

	this.remove = function(){
		self.qRoot.remove();
	};
	this.scaleAnimate = function(){
		self.imageArea.addClass('scaleinAnimation');
		self.imageArea.one('webkitAnimationEnd oanimationend msAnimationEnd animationend', function(e){
			self.imageArea.removeClass('scaleinAnimation');
		});
	};
};
