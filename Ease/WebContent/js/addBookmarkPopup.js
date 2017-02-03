addBookmarkPopup = function(rootEl){
	var self = this;
	this.qRoot = $(rootEl);
	this.parentHandler = this.qRoot.closest('#easePopupsHandler');
	this.errorRowHandler = this.qRoot.find('.errorHandler');

	this.currentApp = null;
	this.currentProfile = null;

	this.reset = function(){

	}
	this.open = function(app, profile){
		self.currentApp = app;
		self.currentProfile = profile;
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