var LoadingIndicator = function(rootEl){
	var self = this;
	this.qRoot = rootEl;
	this.show = function(){
		self.qRoot.addClass('la-animate');
	};
	this.hide = function(){
		self.qRoot.removeClass('la-animate');
	}
};

var easeLoadingIndicator;
$(document).ready(function(){
	easeLoadingIndicator = new LoadingIndicator($('#loading'));
});
