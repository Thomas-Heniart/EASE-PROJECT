var EditInput = function(rootEl, parent, name, type, value, placeholder, placeholderIcon) {
	var self = this;
	this.qRoot = $(rootEl);
	this.name = name;
	this.value = value;
	this.qRoot.append('<span class="input ' + name + '">'
						+ '<i class="fa ' + placeholderIcon + ' placeholderIcon" aria-hidden="true"></i>'
					+ '</span>');
	if (type === 'password') {
		$("span.input." + name, this.qRoot).append('<div class="showPassDiv">'
				+ '<i class="fa fa-eye centeredItem" aria-hidden="true"></i>'
				+ '<i class="fa fa-eye-slash centeredItem" aria-hidden="true"></i>'
				+ '</div>'
				+ '<div class="inputUnlocker">'
				+ '<i class="fa fa-cog centeredItem" aria-hidden="true"></i>'
			+ '</div>');
	}
	$("span.input." + name, this.qRoot).append('<input type="' + type + '" name="' + name + '" id="' + name + '" value="' + value + '" placeholder="' + placeholder + '"/>');
	
	if (type === 'password') {
		var passwordDiv = $("span.input." + name, this.qRoot);
		$("input", passwordDiv).attr("placeholder", "click the wheel to update");
		$('.inputUnlocker', passwordDiv).click(function(){
			parent.unlockPasswordInput(passwordDiv);
		});
		passwordDiv.addClass("locked");
	}
	
	$('.showPassDiv', this.qRoot).click(function(){
        var input = $(this).parent().find('input');
        if ($(this).hasClass('show')){
            input.attr('type', 'password');
            input.focus();
            $(this).removeClass('show');
        } else {
            input.attr('type', 'text');
            input.focus();
            $(this).addClass('show');
        }
    });
	this.inputField = $("#" + name, this.qRoot);
	this.inputField.keyup(function(e){
		if (e.which == 13)
			parent.tabInfoSubmitButton.click();
	});
	this.val = function() {
		return self.inputField.val();
	}
}

modifyAppPopup = function(rootEl){
	var self = this;
	this.qRoot = $(rootEl);
	this.parentHandler = this.qRoot.closest('#easePopupsHandler');
	this.goBackButtonHandler = this.qRoot.find("#goBack");

	this.appNameHolder = this.qRoot.find('input#appName');
	this.appLogoHandler = this.qRoot.find('#appLogo img');

	this.urlInputHandler = this.qRoot.find("input[name='url']");
	this.urlRow = this.qRoot.find('.urlRowHandler');


	/*----------- tab info vars -----------*/
	this.tabInfo = this.qRoot.find('#tabInfo');
	this.tabInfoErrorRowHandler = this.tabInfo.find('.errorHandler');
	this.tabInfoSubmitButton = this.tabInfo.find("button[type='submit']");

	this.loginPasswordRow = this.tabInfo.find('.loginPasswordRow');
	
	this.qRoot.find(".row.fragmentsRow").tabs({
		show: { effect: "fadeIn", duration: 300 }
	});

	this.signInChooseRow = this.tabInfo.find('.signInChooseRow');
	this.signInAccountSelectRow = this.tabInfo.find('.signInAccountSelectRow');
	this.signInDetectionErrorHandler = this.signInAccountSelectRow.find('.SignInErrorHandler');

	this.sameSsoAppsRow = this.tabInfo.find('.sameSsoAppsRow');
	this.sameSsoAppsObjectHandler = this.sameSsoAppsRow.find('.selectHandler');
	/* Sign in interactions */
	this.signInAccountSelectRow.find('.selectable').selectable({
		classes: {
			"ui-selected": "selected"
		}
	});
	this.signInButtons = [];
	this.choosenSignInName = "";
	this.basicSignInName = "";

	this.lockPasswordInput = function(obj){
		//obj.addClass('locked');
		//obj.find('input').attr('placeholder', 'click the wheel to update');
	}
	this.unlockPasswordInput = function(obj){
		obj.removeClass('locked');
		obj.find('input').attr('placeholder', 'Password');
		self.tabInfoSubmitButton.addClass('locked');
	}
	this.signInChooseRow.find('.signInButton').each(function(index, elem){
		var tmp = new Object();
		tmp.name = $(elem).attr('data');
		tmp.qRoot = $(elem);
		tmp.qRoot.click(function(){
			if ($(this).hasClass('selected'))
				return;
			else {
				self.signInChooseRow.find('.selected').removeClass('selected');
				$(this).addClass('selected');
			}
			self.loginPasswordRow.addClass('hide');
			var catalogApp = catalog.getAppByName($(this).attr('data'));
			self.choosenSignInName = $(this).attr('data');
			self.signInDetectionErrorHandler.removeClass('show');
			self.signInDetectionErrorHandler.find('span').text(self.choosenSignInName);
			self.showSignInAccounts(catalogApp.id);			
		});
		self.signInButtons.push(tmp);
	});
	this.createSignInSelectorDiv = function(app){
		var ret = $('<div class="accountLine" appId=' + app.id +'>'
			+'<div class="checkBoxInput">'
			+'<span class="fa-stack">'
			+'<i class="fa fa-square-o fa-stack-2x"></i>'
			+'<i class="fa fa-check fa-stack-1x"></i>'
			+'</span>'
			+'</div>'
			+'<p class="accountName">' + app.login + '</p>'
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
		var accountsHolder = self.signInAccountSelectRow.find('.accountsHolder');

		accountsHolder.find('.accountLine').remove();
		var apps = easeAppsManager.getAppsByWebsiteIdNotEmpty(websiteId);
		var accountLine;
		for (var i = 0; i < apps.length; i++) {
			accountLine = self.createSignInSelectorDiv(apps[i]);
			if (apps[i].id == self.currentApp.logWith){
				accountLine.addClass('selected');
				accountLine.addClass('ui-selected');
				accountLine.addClass('ui-selectee');
			}else if (self.choosenSignInName != self.basicSignInName && !i){
				accountLine.addClass('selected');
				accountLine.addClass('ui-selected');
				accountLine.addClass('ui-selectee');
			}
			accountsHolder.append(accountLine);
		}
		if (apps.length)
			self.tabInfoSubmitButton.removeClass('locked');
		else{
			self.tabInfoSubmitButton.addClass('locked');
			self.signInDetectionErrorHandler.addClass('show');
		}
		self.signInAccountSelectRow.removeClass('hide');
	}
	/* Sign in interactions end */

	/* SSO interactions */
	// object : object.app (easeApp), object.qRoot (jquery div)
	this.sameSsoAccountsVar = [];

	this.createSameAccountDiv = function(imgSrc, name){
		var ret = $(
			'<div class="appHandler">'
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

	this.setupSameSsoAccountsDiv = function(){
		var tmpObject;
		var ssoApps = easeAppsManager.getAppsByLoginAndSsoId(self.currentApp.login, self.currentApp.ssoId);
		ssoApps.splice(ssoApps.indexOf(self.currentApp), 1);
		for (var i = 0; i < ssoApps.length; i++) {
			tmpObject = new Object();
			tmpObject.app = ssoApps[i];
			tmpObject.qRoot = self.createSameAccountDiv(ssoApps[i].imgSrc, ssoApps[i].name);
			self.sameSsoAccountsVar.push(tmpObject);
			self.sameSsoAppsObjectHandler.append(tmpObject.qRoot);
		}
	}
	this.resetSameAccountsRow = function(){
		for (var i = 0; i < self.sameSsoAccountsVar.length; i++) {
			self.sameSsoAccountsVar[i].qRoot.remove();
		}
		this.sameSsoAccountsVar = [];		
	}
	/* SSO interactions end */

	/* ------------  tab delete vars ------------*/
	this.tabDelete = this.qRoot.find('#tabDelete');
	this.tabDeleteErrorRowHandler = this.tabDelete.find('.errorHandler');
	this.tabDeleteSubmitButton = this.tabDelete.find("button[type='submit']");
	this.tabDeleteSubmitButton.click(function(){
		self.tabDeleteErrorRowHandler.removeClass('show');
		self.tabDeleteSubmitButton.addClass('loading');
		postHandler.post(
			'RemoveApp',
			{
				appId: self.currentApp.id
			},
			function(){
				self.tabDeleteSubmitButton.removeClass('loading');
			},
			function(msg){
				self.currentApp.currentProfile.removeApp(self.currentApp);
				self.currentApp.remove();
				self.close();
			},
			function(msg){
				self.tabDeleteErrorRowHandler.find('p').text(msg);
				self.tabDeleteErrorRowHandler.addClass('show');
			},
			'text'
			);
	});

	this.currentApp = null;
	this.relatedCatalogApp = null;
	this.currentInputs = [];

	this.resetSimpleInputs = function(){
		//self.loginInput.val('');
		//self.passwordInput.val('');
	}
	this.resetPasswordShows = function(){
		self.qRoot.find("input[name='password']").attr('type', 'password');
		self.qRoot.find('.showPassDiv.show').removeClass('show');
	}
	this.resetSignInAccounts = function(){
		self.signInAccountSelectRow.find('.accountLine').remove();
		self.signInDetectionErrorHandler.removeClass('show');
		self.signInChooseRow.find('.selected').removeClass('selected');
	}
	this.tabInfoSubmitButton.click(function(){
		self.tabInfoErrorRowHandler.removeClass('show');
		self.tabInfoSubmitButton.addClass('loading');

		var name = self.appNameHolder.val();
		var linkUrl = self.urlInputHandler.val();

		var logwithApp = self.signInAccountSelectRow.find('.selected');
		var logwithId = logwithApp.length ? $(logwithApp[0]).attr('appid') : "";

		var appsId = [];
		appsId.push(self.currentApp.id.toString());
		for (var i = 0; i < self.sameSsoAccountsVar.length; i++) {
			appsId.push(self.sameSsoAccountsVar[i].app.id.toString());
		}
		var submitUrl = "EditClassicApp";
		if (self.currentApp.logWith.length){
			submitUrl = "EditLogwithApp";
		} else if (self.currentApp.url.length){
			submitUrl = "EditBookMark";
		}
		if (self.currentApp.isEmpty){
			if (logwithId.length)
				submitUrl = "WebsiteAppToLogwithApp";
			else
				submitUrl = "WebsiteAppToClassicApp";
		}
		var appsIdJson = JSON.stringify(appsId);
		var parameters = {
				'name': name,
				'logwithId': logwithId,
				'link': linkUrl, 
				'appIds': appsIdJson
		};
		for(var i=0; i < self.currentInputs.length; i++) {
			var input = self.currentInputs[i]
			parameters[input.name] = input.val();
		}
		postHandler.post(
			submitUrl,
			parameters,
			function(){
				self.tabInfoSubmitButton.removeClass('loading');
			},
			function(msg){
				self.currentApp.changeName(name);
				self.currentApp.url = linkUrl;
				if (self.currentApp.isEmpty){
					self.currentApp.qRoot.find('.emptyAppIndicator').remove();
					self.currentApp.qRoot.removeClass('emptyApp');
					self.currentApp.isEmpty = false;
				}
				if (parameters.login != self.currentApp.getAccountInformationValue('login') || parameters.password.length){
					for (var i = 0; i < self.sameSsoAccountsVar.length; i++) {
						self.sameSsoAccountsVar[i].app.setAccountInformationValue("login", parameters.login);
						self.sameSsoAccountsVar[i].app.scaleAnimate();
					}
				}
				self.currentApp.setAccountInformationValue("login", parameters.login);
				self.currentApp.logWith = logwithId;
				self.currentApp.scaleAnimate();
				self.close();
			},
			function(msg){
				self.tabInfoErrorRowHandler.find('p').text(msg);
				self.tabInfoErrorRowHandler.addClass('show');
				setTimeout(function(){
					self.tabInfoErrorRowHandler.removeClass('show');
				}, 3000);
			},
			'text'
			);
	});
	this.reset = function(){
		this.currentApp = null;
		this.relatedCatalogApp = null;
		self.resetSimpleInputs();
		self.resetSameAccountsRow();
		this.resetSignInAccounts();
		self.resetPasswordShows();
		self.lockPasswordInput(self.passwordInputDiv);
		this.qRoot.find(".row.fragmentsRow").tabs("option", "active", 0);
		self.tabDeleteErrorRowHandler.removeClass('show');
		self.tabInfoErrorRowHandler.removeClass('show');
		self.signInChooseRow.addClass('hide');
		self.urlRow.addClass('hide');
		self.urlInputHandler.val('');
		self.signInAccountSelectRow.addClass('hide');
		self.loginPasswordRow.addClass('hide');
		self.sameSsoAppsRow.addClass('hide');
		self.resetInputs();
	}
	this.initializeCurrentInputs = function() {
		for (var i=0; i < self.relatedCatalogApp.inputs.length; i++) {
			var input = self.relatedCatalogApp.inputs[i];
			var inputValue = self.currentApp.getAccountInformationValue(input.name);
			self.currentInputs.push(new EditInput(self.loginPasswordRow, self, input.name, input.type, inputValue, input.placeholder, input.placeholderIcon));
		}
		$("input", self.loginPasswordRow).on('input', self.checkInputs);
	}
	this.checkInputs = function() {
		for(var i=0; i< self.currentInputs.length; i++) {
			var input = self.currentInputs[i];
			if (!input.inputField.parent().hasClass("locked")) {
				if (!input.val().length) {
					self.tabInfoSubmitButton.addClass('locked');
					return;
				}
			}
		}
		self.tabInfoSubmitButton.removeClass('locked');
	}
	this.resetInputs = function() {
		$(".input", self.loginPasswordRow).remove();
		self.currentInputs = [];
	}
	this.open = function(app){
		currentEasePopup = self;
		self.reset();
		this.currentApp = app;
		self.appNameHolder.val(app.name);
		self.relatedCatalogApp = catalog.getAppById(app.websiteId);
		console.log(self.relatedCatalogApp);
		if (self.relatedCatalogApp != undefined)
			self.initializeCurrentInputs();
		if (app.isEmpty){
			self.unlockPasswordInput($("span.input.password"));
			if (self.relatedCatalogApp.canLoginWith.length){
				self.showSignInButtons(self.relatedCatalogApp.canLoginWith);
				self.signInChooseRow.removeClass('hide');
			}
			self.loginPasswordRow.removeClass('hide');
		}
		else if (app.url.length){
			self.urlInputHandler.val(app.url);
			self.urlRow.removeClass('hide');
		}
		else if (app.logWith.length){
			self.showSignInButtons(self.relatedCatalogApp.canLoginWith);
			self.signInChooseRow.removeClass('hide');
			self.basicSignInName = catalog.getAppById(easeAppsManager.getAppById(app.logWith).websiteId).name;
			for (var i = 0; i < self.signInButtons.length; i++) {
				if (self.signInButtons[i].name == self.basicSignInName){
					self.signInButtons[i].qRoot.click();
					break;
				}
			}
		}
		else if (self.currentInputs.length){
			self.loginPasswordRow.removeClass('hide');
			//self.loginInput.val(app.login);
		}
		if (!app.isEmpty && !app.url.length && app.ssoId != -1){
			self.setupSameSsoAccountsDiv();
			if (self.sameSsoAccountsVar.length)
				self.sameSsoAppsRow.removeClass('hide');
		}
		self.appLogoHandler.attr('src', app.logoHandler.attr('src'));
		self.parentHandler.addClass('myshow');
		self.qRoot.addClass('show');
	}
	this.close = function(){
		self.qRoot.removeClass('show');
		self.parentHandler.removeClass('myshow');
	}
	this.goBackButtonHandler.click(function(){
		self.close();
	});
}

var easeModifyAppPopup;
$(document).ready(function(){
	easeModifyAppPopup = new modifyAppPopup($('#modifyAppPopup'));
});