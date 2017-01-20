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
	this.setHomepage = function (checked) {
		 var identify = new amplitude.Identify().set('homepage', checked);
		 amplitude.getInstance().identify(identify);
	}
	this.setDailyPhoto = function (checked) {
		var identify = new amplitude.Identify().set('dailyPhoto', checked);
		 amplitude.getInstance().identify(identify);
	}
	this.increaseAppCounter = function (count) {
		var identify = new amplitude.Identify().add('appCounter', (count == null ? 1 : count));
		amplitude.getInstance().identify(identify);
	}
	this.decreaseAppCounter = function (count) {
		var identify = new amplitude.Identify().add('appCounter', (count == null ? -1 : count));
		amplitude.getInstance().identify(identify);
	}
	
	this.setUserProperty = function(property, value) {
		var identify = new amplitude.Identify().add(property, value);
		amplitude.getInstance().identify(identify);
	}
	
	this.logout = function() {
		amplitude.getInstance().setUserId(null);
		//amplitude.getInstance().regenerateDeviceId();
	}
}

var easeTracker= new tracker();
