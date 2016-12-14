var tracker = function(){
	this.trackEvent = function(eventName){
		if (window.location.href.indexOf("ease.space") > -1)
	       amplitude.getInstance().logEvent(eventName);
	}
}

var easeTracker= new tracker();