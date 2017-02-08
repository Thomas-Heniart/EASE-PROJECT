deleteProfilePopup = function(rootEl){
	var self = this;
	this.qRoot = $(rootEl);
	this.parentHandler = this.qRoot.closest('#easePopupsHandler');

	this.errorRowHandler = this.qRoot.find('.errorHandler');

	this.passwordInputHander = this.qRoot.find("input[name='password']");

	this.formHandler = this.qRoot.find("#deleteProfileForm");
	this.submitButton = this.qRoot.find("button[type='submit']");
	this.goBackButtonHandler = this.qRoot.find("#goBack");

	this.goBackButtonHandler.click(function(){
		self.close();
	});
	this.passwordInputHander.on('input', function(){
		if ($(this).val().length)
			self.submitButton.removeClass('locked');
		else
			self.submitButton.addClass('locked');
	});
	this.formHandler.submit(function(e){
		e.preventDefault();
		if (!(self.passwordInputHander.val().length))
			return;
		self.errorRowHandler.removeClass('show');
		self.submitButton.addClass('loading');
		postHandler.post(
			'RemoveProfile',
			{
				profileId: self.currentProfile.id,
				password: self.passwordInputHander.val()
			},
			function(){
				self.submitButton.removeClass('loading');
			},
			function(msg){
				easeTracker.trackEvent('DeleteProfile');
				self.currentProfile.remove();
				self.close();
			},
			function(msg){
				self.errorRowHandler.find('p').text(msg);
				self.errorRowHandler.addClass('show');
			},
			'text'
		);
	});
	this.currentProfile = null;
	this.reset = function(){
		self.passwordInputHander.val('');
		self.submitButton.addClass('locked');
		self.errorRowHandler.removeClass('show');
	}
	this.open = function(profile){
		currentEasePopup = self;
		self.reset();
		self.currentProfile = profile;
		self.parentHandler.addClass('myshow');
		self.qRoot.addClass('show');
	}
	this.close = function(){
		self.qRoot.removeClass('show');
		self.parentHandler.removeClass('myshow');
	}
}

var easeDeleteProfilePopup;
$(document).ready(function(){
	easeDeleteProfilePopup = new deleteProfilePopup($('#deleteProfilePopup'));
});