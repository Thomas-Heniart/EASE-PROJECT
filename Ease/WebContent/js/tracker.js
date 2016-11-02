var tracker = function(){
	this.trackEvent = function(eventName){
		if (window.location.href.indexOf("ease.space") > -1)
	        mixpanel.track(eventName);
	}
}

var easeTracker;
$(document).ready(function(){
	easeTracker = new tracker();
});