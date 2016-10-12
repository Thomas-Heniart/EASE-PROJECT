
var Form = function (rootEl, inputs, button) {
	var self = this;
	this.qRoot = rootEl;
	this.qInputs = inputs;
	this.qButton = button;
	this.isEnabled = false;
	for (var i = 0; i < this.qInputs.length; ++i) {
		this.qInputs[i].listenBy(this.qRoot);
	}
	this.enable = function () {
		self.isEnabled = true;
		self.qButton.prop('disabled', false);
		self.qButton.addClass("Active");
	};
	this.disable = function () {
		self.isEnabled = false;
		self.qButton.prop('disabled', true);
		self.qButton.removeClass("Active");
	};
	this.qRoot.on("StateChanged", function () {
		self.checkInputs() && self.enable() || self.disable();
	});
	this.checkInputs = function () {
		for (var i = 0; i < self.qInputs.length; ++i) {
			if (self.qInput[i].isValid == false) {
				return false;
			}
		}
		return true;
	};
	this.reset = function () {
		self.disable();
		for (var i = 0; i < self.qInputs.length; ++i) {
			self.qInputs[i].reset();
		}
	};
}