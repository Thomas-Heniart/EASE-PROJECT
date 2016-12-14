var tracker = function(){
	this.trackEvent = function(eventName){
		//if (window.location.href.indexOf("ease.space") > -1)
		console.log(eventName);
	       amplitude.getInstance().logEvent(eventName);
	}
}

var easeTracker= new tracker();