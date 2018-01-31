var addInput = function(rootEl, parent, name, type, placeholder, placeholderIcon) {
	var self = this;
	this.qRoot = $(rootEl);
	this.name = name;
	this.qRoot.append('<span class="input ' + name + '">'
						+ '<i class="fa ' + placeholderIcon + ' placeholderIcon" aria-hidden="true"></i>'
					+ '</span>');
	if (type === 'password') {
		$("span.input." + name, this.qRoot).append('<div class="showPassDiv">'
				+ '<i class="fa fa-eye centeredItem" aria-hidden="true"></i>'
				+ '<i class="fa fa-eye-slash centeredItem" aria-hidden="true"></i>'
			+ '</div>');
	}
	$("span.input." + name, this.qRoot).append('<input autocomplete="new-password" type="' + type + '" name="' + name + '" id="' + name + '" placeholder="' + placeholder + '"/>');
	this.inputField = $("#" + name, this.qRoot);
	this.inputField.keyup(function(e){
		if (e.which == 13)
			parent.submitButton.click();
	});
	this.val = function() {
		return self.inputField.val();
	}
};	

addAppPopup = function(rootEl){
	var self = this;
	this.qRoot = $(rootEl);
	this.parentHandler = this.qRoot.closest('#easePopupsHandler');
	this.errorRowHandler = this.qRoot.find('.errorHandler');

	this.goBackButtonHandler = this.qRoot.find("#goBack");
	this.submitButton = this.qRoot.find("button[type='submit']");
	this.shortcutLinkButton = this.qRoot.find('#shortcutLink');

	this.appNameHolder = this.qRoot.find('input#appName');
	this.appNameInTitle = this.qRoot.find('.titleRow p span');

	this.appLogoHandler = this.qRoot.find('#appLogo img');

	this.loginPasswordRow = this.qRoot.find('.loginPasswordRow');
	this.loginInput = this.loginPasswordRow.find("input[name='login']");
	this.passwordInput = this.loginPasswordRow.find("input[name='password']");

	this.orDelimiter = this.qRoot.find('.orDelimiter');

	this.signInChooseRow = this.qRoot.find('.signInChooseRow');
	this.signInAccountSelectRow = this.qRoot.find('.signInAccountSelectRow');
	this.signInDetectionErrorHandler = this.signInAccountSelectRow.find('.SignInErrorHandler');

	this.signInAccountSelectButtonBack = this.signInAccountSelectRow.find('.buttonBack');

	this.sameSsoAppsRow = this.qRoot.find('.sameSsoAppsRow');
	this.ssoSelectAccountRow = this.qRoot.find('.ssoSelectAccountRow');
	this.ssoNewAccountButton = this.ssoSelectAccountRow.find('.newAccountAdder');

	this.accountNameHelper = this.qRoot.find('.accountNameHelper');
	this.accountNameHelperImg = this.accountNameHelper.find('.accountLogo img');
	this.accountNameHelperName = this.accountNameHelper.find('.accountName');
	this.accountNameHelperBackButton = this.accountNameHelper.find('.backButton');

	this.currentProfile = null;
	this.currentApp = null;


	this.shortcutLinkButton.click(function(){
		self.close();
		easeAddBookmarkPopup.open(self.currentApp, self.currentProfile, self.appNameHolder.val());
		easeAddBookmarkPopup.goBackButtonHandler.one('click',function(){
			self.parentHandler.addClass('myshow');
			self.qRoot.addClass('show');
		});
	});

	/* Sign in interactions */
	this.signInAccountSelectRow.find('.selectable').selectable({
		classes: {
			"ui-selected": "selected"
		}
	});

	this.signInAccountSelectButtonBack.click(function(){
		self.resetSignInAccounts();
		self.resetSimpleInputs();
		self.submitButton.addClass('locked');
		self.signInAccountSelectRow.addClass('hide');
		for (var i = 0; i < self.activeSections.length; i++) {
			self.activeSections[i].removeClass('hide');
		}
	});

	this.choosenSignInName = "";
	this.signInChooseRow.find('.signInButton').click(function(){
		if ($(this).hasClass('selected'))
			return;
		else {
			self.signInChooseRow.find('.selected').removeClass('selected');
			$(this).addClass('selected');
		}
		var catalogApp = catalog.getAppByName($(this).attr('data'));
		self.choosenSignInName = $(this).attr('data');
		self.signInDetectionErrorHandler.removeClass('show');
		self.signInDetectionErrorHandler.find('span').text(self.choosenSignInName);
		self.showSignInAccounts(catalogApp.id);
	});

	this.createSignInSelectorDiv = function(app){
		var ret = $('<div class="accountLine" appId=' + app.id +'>'
			+'<div class="checkBoxInput">'
			+'<span class="fa-stack">'
			+'<i class="fa fa-square-o fa-stack-2x"></i>'
			+'<i class="fa fa-check fa-stack-1x"></i>'
			+'</span>'
			+'</div>'
			+'<p class="accountName">' + app.getAccountInformationValue("login") + '</p>'
			+'</div>');
		return ret;
	}

	this.showSignInButtons = function(websitesIdTab){
		self.signInChooseRow.find('.signInButton').removeClass('show');
		var name;
		for (var i = 0; i < websitesIdTab.length; i++) {
			name = catalog.getAppById(websitesIdTab[i]).name;
			self.signInChooseRow.find('.signInButton[data=' + name + ']').addClass('show');
		}
	}

	this.showSignInAccounts = function(websiteId){
		for (var i = 0; i < self.activeSections.length; i++) {
			self.activeSections[i].addClass('hide');
		}
		var accountsHolder = self.signInAccountSelectRow.find('.accountsHolder');
		accountsHolder.find('.accountLine').remove();
		var apps = easeAppsManager.getAppsByWebsiteIdNotEmpty(websiteId);
		var accountLine;
		for (var i = 0; i < apps.length; i++) {
			accountLine = self.createSignInSelectorDiv(apps[i]);
			if (!i){
				accountLine.addClass('selected');
				accountLine.addClass('ui-selected');
				accountLine.addClass('ui-selectee');
			}
			accountsHolder.append(accountLine);
		}
		if (apps.length)
			self.submitButton.removeClass('locked');
		else{
			self.submitButton.addClass('locked');
			self.signInDetectionErrorHandler.addClass('show');
		}
		self.signInAccountSelectRow.removeClass('hide');
	}
	/* Sign in interactions end */

	this.activeSections = [];

	/* sso interactions */

	this.createSsoAccountSelectDiv = function(ssoId, login, imgSrc){
		var ret = $(
			'<div class="accountLine">'
			+'<div class="accountLogo">'
			+'<img src="' + imgSrc + '"/>'
			+'</div>'
			+'<p class="accountName">' + login + '</p>'
			+'</div>'
			);
		return ret;
	}

	/* obj.name, obj.ssoId, obj.imgSrc, obj.websiteId, obj.qRoot */
	this.sameAccountsVar = [];

	this.createSameAccountDiv = function(imgSrc, name){
		var ret = $(
			'<div class="appHandler checkable">'
			+'<div class="appLogo">'
			+'<img src="' + imgSrc +'">'
			+'</div>'
			+'<div class="appName">'
			+'<p>' + name + '</p>'
			+'</div>'
			+'</div>'
			);
		return ret;
	}

	this.resetSameAccountsRow = function(){
		for (var i = 0; i < self.sameAccountsVar.length; i++) {
			self.sameAccountsVar[i].qRoot.remove();
		}
		this.sameAccountsVar = [];		
	}
	this.resetSimpleInputs = function(){
		self.loginInput.val('');
		self.passwordInput.val('');
	}
	this.resetSignInAccounts = function(){
		self.signInAccountSelectRow.find('.accountLine').remove();
		self.signInDetectionErrorHandler.removeClass('show');
		self.signInChooseRow.find('.selected').removeClass('selected');
	}
	this.resetPasswordShows = function(){
		self.qRoot.find("input[name='password']").attr('type', 'password');
		self.qRoot.find('.showPassDiv.show').removeClass('show');
	}
	this.setupSameAccountsDiv = function(ssoId){
		var apps = catalog.getAppsBySsoId(ssoId);
		var obj;
		self.resetSameAccountsRow();
		var toPut = self.sameSsoAppsRow.find('.selectHandler');
		toPut.find('.appHandler').remove();
		for (var i = apps.length - 1; i >= 0; i--) {
			if (apps[i].id != self.currentApp.id){
				obj = {};
				obj.name = apps[i].name;
				obj.ssoId = apps[i].ssoId;
				obj.imgSrc = apps[i].imgSrc;
				obj.websiteId = apps[i].id;
				obj.qRoot = self.createSameAccountDiv(obj.imgSrc, obj.name);
				self.sameAccountsVar.push(obj);
			}
		}
		for (var i = 0; i < self.sameAccountsVar.length; i++) {
			toPut.append(self.sameAccountsVar[i].qRoot);
		}
	}

	this.choosenSsoAccountLogin = null;
	this.selectExistingSsoAccounts = function(){
		var apps = easeAppsManager.getAppsByLoginAndSsoId(self.choosenSsoAccountLogin, self.currentApp.ssoId);
		for (var i = 0; i < self.sameAccountsVar.length; i++) {
			self.sameAccountsVar[i].qRoot.removeClass('checked');
			self.sameAccountsVar[i].qRoot.addClass('checkable');
			for (var j = 0; j < apps.length; j++) {
				if (self.sameAccountsVar[i].websiteId == apps[j].websiteId){
					self.sameAccountsVar[i].qRoot.addClass('checked');
					self.sameAccountsVar[i].qRoot.removeClass('checkable');
					break;
				}
			}
		}
	}

	this.accountNameHelperBackButton.click(function(){
		self.resetSimpleInputs();
		self.choosenSsoAccountLogin = null;
		self.submitButton.addClass('locked');
		self.accountNameHelper.addClass('hide');
		self.sameSsoAppsRow.addClass('hide');
		self.ssoSelectAccountRow.removeClass('hide');
	});
	this.ssoNewAccountButton.click(function(){
		self.ssoSelectAccountRow.addClass('hide');
		self.initializeInputsRow();
		//self.loginPasswordRow.removeClass('hide');
		
		for (var i = 0; i < self.sameAccountsVar.length; i++) {
			self.sameAccountsVar[i].qRoot.addClass('checkable');
			self.sameAccountsVar[i].qRoot.removeClass('checked');
		}
		self.sameSsoAppsRow.removeClass('hide');
	});
	this.setupSsoAccountChooseRow = function(app){
		var accountHandler = self.ssoSelectAccountRow.find('.accountsHolder');
		var lastButton = accountHandler.find('.newAccountAdder');
		accountHandler.find('.accountLine:not(.newAccountAdder)').remove();
		var loginList = self.getLoginList(easeAppsManager.getAppsBySsoId(app.ssoId));
		var ssoAcc = catalog.getSsoById(self.currentApp.ssoId);
		var createdDiv;
		for (var i = 0; i < loginList.length; i++) {
			createdDiv = self.createSsoAccountSelectDiv(app.ssoId, loginList[i], ssoAcc != null ? ssoAcc.imgSrc : "");
			createdDiv.click(function(){
				self.choosenSsoAccountLogin = $(this).find('.accountName').text();
				self.accountNameHelperName.text(self.choosenSsoAccountLogin).val(self.choosenSsoAccountLogin);
				self.accountNameHelperImg.attr('src', $(this).find('.accountLogo img').attr('src'));
				self.accountNameHelper.removeClass('hide');
				self.setupSameAccountsDiv(app.ssoId);
				self.selectExistingSsoAccounts();
				self.sameSsoAppsRow.removeClass('hide');
				self.ssoSelectAccountRow.addClass('hide');
				self.submitButton.removeClass('locked');
			});
			lastButton.before(createdDiv);
		}
	}
	/* sso interactions end*/

	this.getLoginList = function(appList){
		var loginList = [];
		for (var i = 0; i < appList.length; i++) {
			if (loginList.indexOf(appList[i].getAccountInformationValue("login")) == -1){
				loginList.push(appList[i].getAccountInformationValue("login"));
			}
		}
		return loginList;
	}

	this.open = function(app, profile){
		currentEasePopup = self;
		self.reset();
		self.currentApp = app;
		self.currentProfile = profile;

		self.setName(app.name);
		self.appLogoHandler.attr('src', app.imgSrc);

		if (app.canLoginWith.length){
			self.showSignInButtons(app.canLoginWith);
			self.signInChooseRow.removeClass('hide');
			self.orDelimiter.removeClass('hide');
			self.activeSections.push(self.orDelimiter);
		}
		if (app.ssoId == -1){
			self.loginPasswordRow.removeClass('hide');
			self.initializeInputsRow();
			self.activeSections.push(self.loginPasswordRow);
		}else {
			self.setupSameAccountsDiv(app.ssoId);
			if (!(easeAppsManager.getAppsBySsoId(app.ssoId).length)){
				self.loginPasswordRow.removeClass('hide');
				self.initializeInputsRow();
				self.sameSsoAppsRow.removeClass('hide');
				self.activeSections.push(self.loginPasswordRow);
			} else {
				self.setupSsoAccountChooseRow(app);
				self.ssoSelectAccountRow.removeClass('hide');
				self.activeSections.push(self.ssoSelectAccountRow);
			}
		}
		self.parentHandler.addClass('myshow');
		self.qRoot.addClass('show');
	}
	this.currentInputs = [];
	this.initializeInputsRow = function() {
		self.loginPasswordRow.removeClass('hide');
		self.currentApp.inputs.forEach(function(input) {
			self.currentInputs.push(new addInput(self.loginPasswordRow, self, input.name, input.type, input.placeholder, input.placeholderIcon));
		});
		$("input", self.loginPasswordRow).on('input', function() {
			if (self.checkInputsRow())
				self.submitButton.removeClass('locked');
			else
				self.submitButton.addClass('locked');
		});
	}
	this.resetInputsRow = function() {
		$(".input", self.loginPasswordRow).remove();
		self.currentInputs = [];
	}
	this.checkInputsRow = function() {
		for (var i=0; i < self.currentInputs.length; i++) {
			if (!self.currentInputs[i].val().length)
				return false;
		}
		return true;
	}
	this.close = function(){
		self.qRoot.removeClass('show');
		self.parentHandler.removeClass('myshow');
	}

	this.reset = function(){
		self.currentProfile = null;
		self.currentApp = null;
		self.activeSections = [];
		self.choosenSsoAccountLogin = null;
		self.resetSimpleInputs();
		self.resetSameAccountsRow();
		self.resetSignInAccounts();
		self.resetPasswordShows();
		self.submitButton.addClass('locked');
		self.errorRowHandler.removeClass('show');
		self.loginPasswordRow.addClass('hide');
		self.signInChooseRow.addClass('hide');
		self.signInAccountSelectRow.addClass('hide');
		self.sameSsoAppsRow.addClass('hide');
		self.ssoSelectAccountRow.addClass('hide');
		self.orDelimiter.addClass('hide');
		self.accountNameHelper.addClass('hide');
		self.resetInputsRow();
	}

	this.setName = function(name){
		self.appNameHolder.val(name);
		self.appNameInTitle.text(name);
	}

	this.submitButton.click(function(){
		self.errorRowHandler.removeClass('show');
		var name = self.appNameHolder.val();
		var profileId = self.currentProfile.id;

		var logwithApp = self.signInAccountSelectRow.find('.selected');
		var logwithId = logwithApp.length ? $(logwithApp[0]).attr('appid') : "";

		var websiteId = [];
		var parametersToKeep = [];
		var sameApp = easeAppsManager.getAppByLoginAndSsoId(self.choosenSsoAccountLogin, self.currentApp.ssoId);
		if (sameApp != null)
			parametersToKeep.push({"name" : "login", "value" : self.choosenSsoAccountLogin});
		var appId = sameApp != null ? sameApp.id : "";
		websiteId.push(self.currentApp.id.toString());
		for (var i = 0; i < self.sameAccountsVar.length; i++) {
			if (self.sameAccountsVar[i].qRoot.hasClass('checkable') && self.sameAccountsVar[i].qRoot.hasClass('checked'))
				websiteId.push(self.sameAccountsVar[i].websiteId.toString());
		}
		var submitUrl = "AddClassicApp";
		var appType = "ClassicApp";
		if (logwithId.length){
			submitUrl = "AddLogwithApp";
			appType = "LogwithApp";
		}
		if (self.choosenSsoAccountLogin != null) {
			submitUrl = "AddClassicAppSameAs";
		}
		if (submitUrl == "AddClassicApp" && (!self.checkInputsRow()))
			return;
		self.submitButton.addClass('loading');
		var websiteIdJson = JSON.stringify(websiteId);
		var parameters = {
				'name' : name,
				'profileId' : profileId,
				'websiteIds' : websiteIdJson,
				'logwithId' : logwithId,
				'appId' : appId
			};
		self.currentInputs.forEach(function(input) {
			parameters[input.name] = input.val();
			if (input.name != "password") {
				var obj = {};
				obj.name = input.name;
				obj.value = input.val();
				parametersToKeep.push(obj);
			}
		});
		postHandler.post(
			submitUrl,
			parameters,
			function(){
				self.submitButton.removeClass('loading');
			},
			function(msg){
				var ids = JSON.parse(msg);
				var catalogApp;
				for (var i = 0; i < ids.length; ++i) {
					catalogApp = catalog.getAppById(websiteId[i]);
					var app = new MyApp();
					var accountInformations = [];
					app.init(logwithId, parametersToKeep, catalogApp.id, i == 0 ? name : catalogApp.name, ids[i], catalogApp.ssoId, true, catalogApp.imgSrc, '', appType);
					catalogApp.increaseCount();
					self.currentProfile.addApp(app);
					app.scaleAnimate();
				}
				self.close();
			},
			function (msg){
				self.errorRowHandler.find('p').text(msg);
				self.errorRowHandler.addClass('show');
				setTimeout(function(){
					self.errorRowHandler.removeClass('show');
				}, 3000);
			},
			'text'
			);
	});

	this.goBackButtonHandler.click(function(){
		self.close();
	});
};

var easeAddAppPopup;
$(document).ready(function(){
	easeAddAppPopup = new addAppPopup($('#addAppPopup'));
});