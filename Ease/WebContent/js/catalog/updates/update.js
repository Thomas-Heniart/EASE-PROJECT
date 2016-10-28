
var Update = function(rootEl, updateManager, index) {
	var self = this;
	this.qRoot = rootEl;
	this.oManager = updateManager;
	this.index = index;
	this.isAnimate = false;
	
	this.remove = function() {
		self.qRoot.remove();
	};
	this.animate = function () {
		self.qRoot.addClass('decaleUpdate');
		self.isAnimate = true;
	}
	this.unanimate = function () {
		self.qRoot.removeClass('decaleUpdate');
		self.isAnimate = false;
	}
	this.qRoot.find(".cancel").on("click", function () {
		if (self.isAnimate == false) {
			self.qRoot.addClass('deletingUpdate');
			self.oManager.launchAnim(self.index);
			setTimeout(function () {
				self.remove();
				self.oManager.stopAnim(self.index);
			}, 300);
		}
	});
}