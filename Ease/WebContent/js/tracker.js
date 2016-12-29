var tracker = function(){
	this.setUserId = function (email) {
		amplitude.getInstance().setUserId(email);
	}
	this.trackEvent = function(eventName){
		//if (window.location.href.indexOf("ease.space") > -1)
	       amplitude.getInstance().logEvent(eventName);
	}
	this.resetUserId = function () {
		amplitude.getInstance().setUserId(null);
	}
}

var easeTracker= new tracker();
