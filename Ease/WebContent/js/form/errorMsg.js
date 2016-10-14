
var constructorErrorMsg = function (rootEl) {
	var self = this;
	this.qRoot = rootEl;
	this.qMsg = this.qRoot.find("p");
	this.display = function (msg) {
		self.qMsg.addClass("show");
		self.qMsg.innerHTML(msg);
	}
	this.reset = function () {
		self.qMsg.innerHTML("");
		self.qMsg.removeClass("show");
	}
}

var ErrorMsg = {
	ClassicErrorMsg : function () {
		constructorErrorMsg.apply(this, arguments);
		var self = this;
	}
}