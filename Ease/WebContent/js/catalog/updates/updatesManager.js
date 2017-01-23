
var UpdateManager = function (rootEl) {
	var self = this;
	this.qRoot = rootEl;
	this.updatesHandler = this.qRoot.find('.contentList');
	this.loading = this.qRoot.find('.updateLoadingHelper');
	this.titleHandler = this.qRoot.find('.catalogHeader p');
	this.updates = [];

	this.showLoading = function(){
		self.updatesHandler.addClass('hide');
		self.loading.addClass('show');
	};
	this.hideLoading = function(){
		self.updatesHandler.removeClass('hide');
		self.loading.removeClass('show');		
	};
	this.show = function(){
		self.qRoot.addClass('show');
	};
	this.hide = function(){
		self.qRoot.removeClass('show');
	};
	this.updateTitle = function(){
		self.titleHandler.text(self.updates.length + ' Update' + ((self.updates.length == 1) ? '' : 's') + ' available');
	};
	this.checkUpdates = function(){
		postHandler.post(
			'GetUpdates',
			{},
			function(){

			},
			function(msg){
				var updates = JSON.parse(msg);
				var u;
				if (self.updates.length != updates.length){
					for (var i = 0; i < self.updates.length; i++) {
						self.removeUpdate(self.updates[i]);
					}
					self.updates = [];
					for (var i = 0; i < updates.length; i++) {
						u = updates[i];
						if (updates[i].type == 'newClassicApp'){
							self.addUpdate(new newClassicApp(u.singleId, u.login, u.passwordLength, u.websiteImg, u.websiteName, u.email, u.websiteId));
						} else if (updates[i].type == 'newPassword'){
							self.addUpdate(new updatePassword(u.singleId, u.appId, u.login, u.passwordLength, u.websiteImg, u.websiteName, u.email, u.websiteId));
						} else if (updates[i].type == 'newLogWithApp'){
							self.addUpdate(new logWithApp(u.singleId, u.websiteName, u.logWithId, u.login, u.logWithImg, u.logWithName, u.websiteImg, u.websiteId));
						}
					}
				}
				self.updateTitle();
			},
			function(msg){

			},
			'text'
			);
	};
	this.onCatalogOpen = function(){
		self.showLoading();
		self.checkUpdates();
		self.hideLoading();
	};
	this.addUpdate = function(update){
		self.updates.push(update);
		self.updatesHandler.append(update.qRoot);
	};
	this.removeUpdate = function(update){
		update.removeWithAnimation();
		for (var i = self.updates.indexOf(update) + 1; i < self.updates.length; i++) {
			self.updates[i].slideLeftAnimation();
		}
		self.updates.splice(self.updates.indexOf(update), 1);
	};
}


var MyApp = function(){
	var self = this;
	this.qRoot = $($('#boxHelper').html());
	this.logoHandler = this.qRoot.find('img.logo');
	this.appNameHandler = this.qRoot.find('.siteName p');
	this.modifyAppButton = this.qRoot.find('.modifyAppButton');
	this.deleteAppButton = this.qRoot.find('.deleteAppButton');
	this.imageArea = this.qRoot.find('.linkImage');

	this.init = function(logWith, login, catalogId, name, id, ssoId, canMove, imageSrc){
		this.qRoot.attr('logwith', logWith);
		this.qRoot.attr('login', login);
		this.qRoot.attr('webid', catalogId);
		this.qRoot.attr('id', id);
		this.qRoot.attr('ssoid', ssoId);
		this.qRoot.attr('move', canMove);
		this.qRoot.attr('name', name);
		this.logoHandler.attr('src', imageSrc);
		this.appNameHandler.text(name);
		this.imageArea.click(function(){
			sendEvent(this);
		});
		this.modifyAppButton.click(function(e){
			showModifyAppPopup(this, e);
		});
		this.deleteAppButton.click(function(e){
			showConfirmDeleteAppPopup(this, e);
		});
	};
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

var updatePassword = function(updateId, appId, login, pwdLength, imageSrc, websiteName, isVerified, catalogId) {
	var self = this;
	this.qRoot = $(
		'<div class="updateBox">'
		+'<div class="logo">'
		+'<img src="" />'
		+'</div>'
		+'<div class="infos">'
		+'		<div class="errorMessage">'
		+'		 <p></p>'
		+'		</div>'
		+'	<div class="handler">'
		+'<div class="title">'
		+'<p> New Password</p>'
		+'</div>'
		+'	<div class="credentials">'
		+'		<div class="credentialsHandler">'
		+'			<p class="login"></p>'
		+'			<p class="password"></p>'
		+'		</div>'
		+'	</div>'
		+'	<div class="buttonsHandler">'
		+'	<button class="acceptButton btn">Accept</button>'
		+'	<button class="rejectButton btn">Reject</button>'
		+'	</div>'
		+'</div>'
		+'</div>'
		+'</div>'
		);
	this.type = 'updatePassword';
	this.updateId = updateId;
	this.websiteName = websiteName;
	this.appId = appId;
	this.login = login;
	this.imageSrc = imageSrc;
	this.isVerified = isVerified;
	this.catalogId = catalogId;
	this.pwdString = '';
	this.imageHandler = this.qRoot.find('.logo');
	this.errorMessageHandler = this.qRoot.find('.errorMessage');
	this.infosHandler = this.qRoot.find('.infos .handler');
	this.buttonsHandler = this.qRoot.find('.buttonsHandler');
	this.acceptButton = this.qRoot.find('.acceptButton');
	this.rejectButton = this.qRoot.find('.rejectButton');

	while (pwdLength-- > 0)this.pwdString += '•';
	this.qRoot.find('.login').text(this.login);
	this.qRoot.find('.password').text(this.pwdString);
	this.qRoot.find('.logo img').attr('src', this.imageSrc);

	this.showErrorMessage = function(msg){
		self.errorMessageHandler.find('p').text(msg);
		self.errorMessageHandler.addClass('show');
		self.infosHandler.addClass('hide');
		setTimeout(function(){
			self.errorMessageHandler.find('p').text('');
			self.errorMessageHandler.removeClass('show');
			self.infosHandler.removeClass('hide');			
		}, 2500);
	}

	this.removeWithAnimation = function(){
		self.qRoot.addClass('deletingUpdate');
		self.qRoot.one('webkitAnimationEnd oanimationend msAnimationEnd animationend', function(e){
			self.remove();
		});
	}
	this.slideLeftAnimation = function(){
		self.qRoot.addClass('decaleUpdate');
		self.qRoot.one('webkitAnimationEnd oanimationend msAnimationEnd animationend', function(e){
			self.qRoot.removeClass('decaleUpdate');
		});
	}
	this.remove = function(){
		self.qRoot.remove();
	}
	this.startLogoAnimation = function() {
		self.imageHandler.addClass('infiniteScaleAnimation');
		self.buttonsHandler.css('pointer-events', 'none');
	}
	this.stopLogoAnimation = function() {
		self.imageHandler.removeClass('infiniteScaleAnimation');
		self.buttonsHandler.css('pointer-events','');
	}

	this.acceptButton.click(function(){
		if (self.isVerified == 'verified'){
			self.startLogoAnimation();
			postHandler.post(
				'acceptUpdate',
				{
					profileId: profiles[profiles.length - 1].id,
					updateId: self.updateId
				},
				function(){
					self.stopLogoAnimation();
				},
				function(msg){
					catalog.oUpdate.removeUpdate(self);
				},
				function(msg){
					self.showErrorMessage(msg);
				},
				'text'
				);
		} else {
			easeAddUpdatePopup.open(self, profiles[profiles.length - 1].id);
		}
	});
	this.rejectButton.click(function(){
		self.startLogoAnimation();
		postHandler.post(
			'RejectUpdate',
			{
				updateId:self.updateId
			},
			function(){
				self.stopLogoAnimation();
			},
			function(msg){
				catalog.oUpdate.removeUpdate(self);
			},
			function(msg){
				self.showErrorMessage(msg);
			},
			'text');
	});
};

var newClassicApp = function(updateId, login, pwdLength, imageSrc, websiteName, isVerified, catalogId){
	var self = this;
	this.qRoot = $(
		'<div class="updateBox">'
		+'	<div class="logo">'
		+'	<img src="" />'
		+'	</div>'
		+'	<div class="infos">'
		+'		<div class="errorMessage">'
		+'		 <p></p>'
		+'		</div>'
		+'	<div class="handler">'
		+'		<div class="title">'
		+'			<p> New Account</p>'
		+'		</div>'
		+'		<div class="credentials">'
		+'			<div class="credentialsHandler">'
		+'				<p class="login"></p>'
		+'				<p class="password"></p>'
		+'			</div>'
		+'		</div>'
		+'		<div class="buttonsHandler">'
		+'		<button class="acceptButton btn">Accept</button>'
		+'			<button class="rejectButton btn">Reject</button>'
		+'			</div>'
		+'		</div>'
		+'		</div>'
		+'	</div>'
		);
	this.type = 'newClassicApp';
	this.updateId = updateId;
	this.websiteName = websiteName;
	this.login = login;
	this.imageSrc = imageSrc;
	this.catalogId = catalogId;
	this.isVerified = isVerified;
	this.pwdString = '';
	this.imageHandler = this.qRoot.find('.logo');
	this.errorMessageHandler = this.qRoot.find('.errorMessage');
	this.infosHandler = this.qRoot.find('.infos .handler');
	this.buttonsHandler = this.qRoot.find('.buttonsHandler');
	this.acceptButton = this.qRoot.find('.acceptButton');
	this.rejectButton = this.qRoot.find('.rejectButton');

	while (pwdLength-- > 0)this.pwdString += '•';
	this.qRoot.find('.login').text(this.login);
	this.qRoot.find('.password').text(this.pwdString);
	this.qRoot.find('.logo img').attr('src', this.imageSrc);

	this.showErrorMessage = function(msg){
		self.errorMessageHandler.find('p').text(msg);
		self.errorMessageHandler.addClass('show');
		self.infosHandler.addClass('hide');
		setTimeout(function(){
			self.errorMessageHandler.find('p').text('');
			self.errorMessageHandler.removeClass('show');
			self.infosHandler.removeClass('hide');			
		}, 2500);
	}
	this.removeWithAnimation = function(){
		self.qRoot.addClass('deletingUpdate');
		self.qRoot.one('webkitAnimationEnd oanimationend msAnimationEnd animationend', function(e){
			self.remove();
		});
	}
	this.slideLeftAnimation = function(){
		self.qRoot.addClass('decaleUpdate');
		self.qRoot.one('webkitAnimationEnd oanimationend msAnimationEnd animationend', function(e){
			self.qRoot.removeClass('decaleUpdate');
		});
	}
	this.remove = function(){
		self.qRoot.remove();
	}
	this.startLogoAnimation = function() {
		self.imageHandler.addClass('infiniteScaleAnimation');
		self.buttonsHandler.css('pointer-events', 'none');
	}
	this.stopLogoAnimation = function() {
		self.imageHandler.removeClass('infiniteScaleAnimation');
		self.buttonsHandler.css('pointer-events', '');
	}

	this.acceptButton.click(function(){
		if (self.isVerified == 'verified'){
			self.startLogoAnimation();
			postHandler.post(
				'acceptUpdate',
				{
					profileId: profiles[profiles.length - 1].id,
					updateId: self.updateId
				},
				function(){
					self.stopLogoAnimation();
				},
				function(msg){
					var app = new MyApp();
					app.init(false, self.login, self.catalogId, self.websiteName, msg, 0, true, self.imageSrc);
					profiles[profiles.length - 1].addApp(app);
					app.scaleAnimate();
					catalog.oUpdate.removeUpdate(self);
				},
				function(msg){
					self.showErrorMessage(msg);
				},
				'text'
				);
		} else {
			easeAddUpdatePopup.open(self, profiles[profiles.length - 1].id, -1);
		}
	});
	this.rejectButton.click(function(){
		self.startLogoAnimation();
		postHandler.post(
			'RejectUpdate',
			{
				updateId:self.updateId
			},
			function(){
				self.stopLogoAnimation();
			},
			function(msg){
				catalog.oUpdate.removeUpdate(self);
			},
			function(msg){
				self.showErrorMessage(msg);
			},
			'text');
	});
};

var newLogWithApp = function(updateId, websiteName, logWithId, logWithLogin, logWithImageSrc, logWithAppName, websiteImageSrc, catalogId){
	var self = this;
	this.qRoot = $(
		'<div class="updateBox">'
		+'	<div class="logo">'
		+'		<img src="" />'
		+'	</div>'
		+'		<div class="infos">'
		+'		<div class="errorMessage">'
		+'		 <p></p>'
		+'		</div>'
		+'	<div class="handler">'
		+'			<div class="title">'
		+'				<p> New Account</p>'
		+'			</div>'
		+'			<div class="credentials">'
		+'				<div class="logWithLogo">'
		+'					<img src="" />'
		+'				</div>'
		+'				<div class="credentialsHandler">'
		+'					<p class="login">Sign-in with <span></span></p>'
		+'					<p class="password">(<span></span>)</p>'
		+'				</div>'
		+'			</div>'
		+'			<div class="buttonsHandler">'
		+'			<button class="acceptButton btn">Accept</button>'
		+'			<button class="rejectButton btn">Reject</button>'
		+'		</div>'
		+'			</div>'
		+'		</div>'
		+'	</div>'
		);
	this.type = 'newLogWith';
	this.updateId = updateId;
	this.websiteName = websiteName;
	this.logWithLogin = logWithLogin;
	this.logWithImageSrc = logWithImageSrc;
	this.logWithAppName = logWithAppName;
	this.websiteImageSrc = websiteImageSrc;
	this.catalogId = catalogId;
	this.logWithId = logWithId;
	this.imageHandler = this.qRoot.find('.logo');
	this.errorMessageHandler = this.qRoot.find('.errorMessage');
	this.infosHandler = this.qRoot.find('.infos .handler');
	this.buttonsHandler = this.qRoot.find('.buttonsHandler');
	this.acceptButton = this.qRoot.find('.acceptButton');
	this.rejectButton = this.qRoot.find('.rejectButton');

	this.qRoot.find('.logo img').attr('src', this.websiteImageSrc);
	this.qRoot.find('.logWithLogo img').attr('src', this.logWithImageSrc);
	this.qRoot.find('.login span').text(this.logWithAppName);
	this.qRoot.find('.password span').text(this.logWithLogin);

	this.showErrorMessage = function(msg){
		self.errorMessageHandler.find('p').text(msg);
		self.errorMessageHandler.addClass('show');
		self.infosHandler.addClass('hide');
		setTimeout(function(){
			self.errorMessageHandler.find('p').text('');
			self.errorMessageHandler.removeClass('show');
			self.infosHandler.removeClass('hide');			
		}, 2500);
	}
	this.removeWithAnimation = function(){
		self.qRoot.addClass('deletingUpdate');
		self.qRoot.one('webkitAnimationEnd oanimationend msAnimationEnd animationend', function(e){
			self.remove();
		});
	}
	this.slideLeftAnimation = function(){
		self.qRoot.addClass('decaleUpdate');
		self.qRoot.one('webkitAnimationEnd oanimationend msAnimationEnd animationend', function(e){
			self.qRoot.removeClass('decaleUpdate');
		});
	}
	this.remove = function(){
		self.qRoot.remove();
	}
	this.startLogoAnimation = function() {
		self.imageHandler.addClass('infiniteScaleAnimation');
		self.buttonsHandler.css('pointer-events', 'none');
	}
	this.stopLogoAnimation = function() {
		self.imageHandler.removeClass('infiniteScaleAnimation');
		self.buttonsHandler.css('pointer-events','');
	}

	this.acceptButton.click(function(){
		self.startLogoAnimation();
		postHandler.post(
			'acceptUpdate',
			{
				profileId: profiles[profiles.length - 1].id,
				updateId: self.updateId
			},
			function(){
				self.stopLogoAnimation();
			},
			function(msg){
				var app = new MyApp();
				app.init(self.logWithId, null, self.catalogId, self.websiteName, msg, 0, true, self.imageSrc);
				profiles[profiles.length - 1].addApp(app);
				app.scaleAnimate();
				catalog.oUpdate.removeUpdate(self);
			},
			function(msg){
				self.showErrorMessage(msg);
			},
			'text'
			);
	});
	this.rejectButton.click(function(){
		self.startLogoAnimation();
		postHandler.post(
			'RejectUpdate',
			{
				updateId:self.updateId
			},
			function(){
				self.stopLogoAnimation();
			},
			function(msg){
				catalog.oUpdate.removeUpdate(self);
			},
			function(msg){
				self.showErrorMessage(msg);
			},
			'text');
	});
};

var easeAddUpdatePopup;
var addUpdatePopup = function(rootEl){
	var self = this;
	this.qRoot = $(rootEl);
	this.parentHandler = this.qRoot.closest('#easePopupsHandler');
	this.logoImgHandler = this.qRoot.find('.logo img');
	this.appNameHandler = this.qRoot.find('.appName');
	this.emailSendingArea = this.qRoot.find('.emailSendingDiv');
	this.sendEmailButtonHandler = this.qRoot.find('button.emailSend');
	this.sentEmailOkButton = this.qRoot.find('#informEmailSent button.btn');
	this.ajaxFormHandler = this.qRoot.find('form');
	this.loginInputHandler = this.qRoot.find("input[name='login']");
	this.passwordInputHandler = this.qRoot.find("input[name='password']");
	this.submitButtonHandler = this.ajaxFormHandler.find("button[type='submit']");
	this.goBackButtonHandler = this.qRoot.find("#goBack");

	this.mainBody = this.qRoot.find('#addUpdate');
	this.sentEmailBody = this.qRoot.find('#informEmailSent');

	this.profileId = -1;
	this.appIdx = -1;
	this.update = null;
	this.showEmailSendingArea = function(){
		self.emailSendingArea.addClass('show');
	};
	this.hideEmailSendingArea = function () {
		self.emailSendingArea.removeClass('show');
	}
	this.reset = function(){
		self.loginInputHandler.val('');
		self.passwordInputHandler.val('');
		self.profileId = -1;
		self.appIdx = -1;
		self.updateId = -1;
		self.submitButtonHandler.addClass('locked');
		self.sentEmailBody.removeClass('show');
		self.mainBody.addClass('show');
	};
	this.open = function(update, profileId, appIdx){
		self.reset();
		self.update = update;
		self.appIdx = appIdx;
		self.profileId = profileId;
		self.loginInputHandler.val(update.login);
		self.appNameHandler.text(update.websiteName);
		self.logoImgHandler.attr('src', update.imageSrc);
		if (update.isVerified == 'unverified'){
			self.showEmailSendingArea();
			self.sendEmailButtonHandler.one('click', function(){
				postHandler.post(
					'AskVerificationEmail',
					{
						email: self.update.login
					},
					function(){
					},
					function(msg){
						self.showSentEmailFeedbackArea();
					},
					function(msg){

					},'text'
					);
			});
		}
		self.parentHandler.addClass('myshow');
		self.qRoot.addClass('show');
	};
	this.close = function(){
		self.qRoot.removeClass('show');
		self.parentHandler.removeClass('myshow');
		self.sendEmailButtonHandler.off();
		self.hideEmailSendingArea();
	};
	this.showSentEmailFeedbackArea = function(){
		self.mainBody.removeClass('show');
		self.sentEmailBody.addClass('show');
	};
	this.sentEmailOkButton.click(function(){
		self.close();
	});
	this.passwordInputHandler.on('input',function(){
		($(this).val().length > 0)
		&& self.submitButtonHandler.removeClass('locked')
		|| self.submitButtonHandler.addClass('locked');
	});
	this.goBackButtonHandler.click(function(){
		self.close();
	});
	this.ajaxFormHandler.submit(function(e){
		e.preventDefault();
		postHandler.post(
			'AcceptUpdate',
			{
				profileId: self.profileId,
				updateId: self.update.updateId,
				password:self.passwordInputHandler.val()
			},
			function(){

			},
			function(msg){
				if (self.update.type == 'newClassicApp'){
					var app = new MyApp();
					app.init(false, self.update.login, self.update.catalogId, self.update.websiteName, msg, 0, true, self.update.imageSrc);
					profiles[profiles.length - 1].addApp(app);
					app.scaleAnimate();
				}
				catalog.oUpdate.removeUpdate(self.update);
				self.close();
			},
			function(msg){

			},
			'text'
			);
	});
};

$(document).ready(function(){
	easeAddUpdatePopup = new addUpdatePopup($('#addUpdatePopup'));
});