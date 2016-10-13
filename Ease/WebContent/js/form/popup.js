
var Popup = function (rootEl, form, closeButton) {
	var self = this;
	this.qRoot = rootEl;
	this.oForm = form;
	this.qCloseButton = closeButton;
	this.open = function () {
		self.oForm.reset();
		self.qRoot.addClass('md-show');
	};
	this.close = function () {
		self.qRoot.removeClass('md-show');
	};
	this.qCloseButton.onClick(function () {
		self.qRoot.close();
	});
}

var DeleteProfilePopup = function (rootEl, form, closeButton) {
	Popup.apply(this,arguments);
	var self = this;
	this.open = function (profileIndex) {
		self.oForm.reset();
		self.oForm.oInputs[0].val(profileIndex);
		self.qRoot.addClass('md-show');
	}
}