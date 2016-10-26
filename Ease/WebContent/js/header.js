var Header = function(rootEl){
	var self = this;
	this.rootEl = rootEl;
	if(!this.rootEl.find("#backgroundSwitch").is(':checked')){
		this.rootEl.addClass("scrolling");
	}
	this.rootEl.find("#backgroundSwitch").change(function(){
		$(this).is(":checked") && self.rootEl.removeClass("scrolling") || self.rootEl.addClass("scrolling");
	});
}

var easeHeader;
$(document).ready(function(){
	easeHeader = new Header($('.header'));
});