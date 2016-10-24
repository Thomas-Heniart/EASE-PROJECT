var Header = function(rootEl){
	var self = this;
	this.rootEl = rootEl;
	
}

var easeHeader;
$(document).ready(function(){
	easeHeader = new Header($('.header'));
});