addBookmarkPopup = function(rootEl){
	var self = this;
	this.qRoot = $(rootEl);
	this.parentHandler = this.qRoot.closest('#easePopupsHandler');

	this.appNameInputHandler = this.qRoot.find('input#appName');
	this.urlInputHandler = this.qRoot.find("input[name='url']");

	this.appLogoHandler = this.qRoot.find('#appLogo img');

	this.errorRowHandler = this.qRoot.find('.errorHandler');

	this.infoRow = this.qRoot.find('.infoRow');

	this.submitButton = this.qRoot.find("button[type='submit']");
	this.goBackButtonHandler = this.qRoot.find("#goBack");

	this.currentApp = null;
	this.currentProfile = null;

	this.goBackButtonHandler.click(function(){
		self.close();
	});
	this.submitButton.click(function(){
		var url = self.urlInputHandler.val();
		var websiteId = self.currentApp.id;
		var profileId = self.currentProfile.id;
		var name = self.appNameInputHandler.val();
		self.submitButton.addClass('loading');
		self.errorRowHandler.removeClass('show');
		postHandler.post(
			'AddBookMark',
			{
				'link': url,
				'websiteId': websiteId,
				'profileId': profileId,
				'name': name
			},
			function(){
				self.submitButton.removeClass('loading');
			},
			function(msg){
				var app = new MyApp();
				app.init('', '', self.currentApp.id, name, msg, self.currentApp.ssoId, true, self.currentApp.imgSrc, url);
				self.currentProfile.addApp(app);
				app.scaleAnimate();
				self.close();
			},
			function(msg){
				self.errorRowHandler.find('p').text(msg);
				self.errorRowHandler.addClass('show');
			}
			);
	});
	this.reset = function(){
		self.infoRow.addClass('hide');
		self.appNameInputHandler.val('');
		self.urlInputHandler.val('');
		self.errorRowHandler.removeClass('show');
		this.currentApp = null;
		this.currentProfile = null;
	}
	this.open = function(app, profile, appName){
		currentEasePopup = self;
		self.reset();
		self.currentApp = app;
		self.currentProfile = profile;
		self.appLogoHandler.attr('src', app.imgSrc);
		if (!appName)
			self.appNameInputHandler.val(app.name);
		else
			self.appNameInputHandler.val(appName);
		self.urlInputHandler.val(app.url);
		self.parentHandler.addClass('myshow');
		self.qRoot.addClass('show');
	}
	this.close = function(){
		self.parentHandler.removeClass('myshow');
		self.qRoot.removeClass('show');
	}
}

var easeAddBookmarkPopup;
$(document).ready(function(){
	easeAddBookmarkPopup = new addBookmarkPopup($('#addBookmarkPopup'));
});