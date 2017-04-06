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

AppTypeMap = {
	LinkApp : '<i class="fa fa-star appTypeIndicator"></i>',
	LogwithApp: {
		Facebook : '<i class="fa fa-facebook appTypeIndicator"></i>',
		LinkedIn : '<i class="fa fa-linkedin appTypeIndicator"></i>'
	}
}

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
	this.logWithName = '';
	this.url = "";
	this.currentProfile = null;
	this.isEmpty = false;
	this.type = null;

	this.changeName = function(name){
		self.appNameHandler.text(name);
		self.name = name;
	}
	this.init = function(logWith, accountInformations, catalogId, name, id, ssoId, canMove, imgSrc, url, type){
		self.qRoot = $($('#boxHelper').html());
		self.qRoot.attr('logwith', logWith.length ? logWith : "false");
		self.qRoot.attr('webid', catalogId);
		self.qRoot.attr('id', id);
		self.qRoot.attr('ssoid', ssoId);
		self.qRoot.attr('move', canMove);
		self.qRoot.attr('name', name);
		self.accountInformations = accountInformations;
		self.id = id;
		self.type = type;
		self.ssoId = ssoId;
		self.name = name;
		self.websiteId = catalogId;
		self.logWith = logWith;
		if (logWith.length)
			self.logWithName = catalog.getAppById(easeAppsManager.getAppById(logWith).websiteId).name;
		self.logoHandler = self.qRoot.find('img.logo');
		self.logoHandler.attr('src', imgSrc);
		self.imgSrc = imgSrc;
		self.url = url;
		self.appNameHandler = self.qRoot.find('.siteName p');
		self.settingsButton = self.qRoot.find('.showAppActionsButton');
		self.imageArea = self.qRoot.find('.linkImage');
		self.appNameHandler.text(name);
		self.setupAppTypeIndicator();
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
		self.type = self.qRoot.attr('data-type');
		self.websiteId = self.qRoot.attr('webid');
		self.ssoId = self.qRoot.attr('ssoid');
		self.logWith = self.qRoot.attr('logwith');
		self.logWithName = self.qRoot.attr('logwithname');
		self.logoHandler = self.qRoot.find('img.logo');
		self.imgSrc = self.logoHandler.attr('lazy-src');
		var accountInf = self.qRoot.attr('account');
		if (accountInf){
			self.accountInformations = JSON.parse(accountInf);
			self.qRoot.attr('account', '');
		}
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
//		self.imageArea.append('<i class="fa fa-star appTypeIndicator" style="color:#2bcfff;"></i>');
		self.setupAppTypeIndicator();
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

	this.setupAppTypeIndicator = function(){
		self.imageArea.find('.appTypeIndicator').remove();
		if (self.type == 'LogwithApp'){
			if (AppTypeMap['LogwithApp'][self.logWithName])
				self.imageArea.append(AppTypeMap['LogwithApp'][self.logWithName]);
			return;
		}
		if (AppTypeMap[self.type])
			self.imageArea.append(AppTypeMap[self.type]);
	};
	this.scaleAnimate = function(){
		self.imageArea.addClass('scaleinAnimation');
		self.imageArea.one('webkitAnimationEnd oanimationend msAnimationEnd animationend', function(e){
			self.imageArea.removeClass('scaleinAnimation');
		});
	};
	this.hideWithAnimation = function(){
		self.qRoot.one('webkitAnimationEnd oanimationend msAnimationEnd animationend', function(e){
			self.qRoot.addClass('hide');
			self.qRoot.removeClass('flip-out-ver-left');
		});
		self.qRoot.addClass('flip-out-ver-left');
	}
};
