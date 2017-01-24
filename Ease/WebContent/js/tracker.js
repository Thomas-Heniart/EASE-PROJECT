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
		 var identify = new amplitude.Identify().set('HomepageYN', checked);
		 amplitude.getInstance().identify(identify);
	}
	this.setDailyPhoto = function (checked) {
		var identify = new amplitude.Identify().set('DailyPhotoYN', checked);
		 amplitude.getInstance().identify(identify);
	}
	this.increaseAppCounter = function (count) {
		var identify = new amplitude.Identify().add('AppCount', (count == null ? 1 : count));
		amplitude.getInstance().identify(identify);
	}
	this.decreaseAppCounter = function (count) {
		var identify = new amplitude.Identify().add('AppCount', (count == null ? -1 : count));
		amplitude.getInstance().identify(identify);
	}
	this.increaseEmailCount = function () {
		var identify = new amplitude.Identify().add('EmailCount', 1);
		amplitude.getInstance().identify(identify);
		var identify2 = new amplitude.Identify().add('EmailNonVerifiedCount', 1);
		amplitude.getInstance().identify(identify2);
	}
	
	this.decreaseEmailCount = function () {
		var identify = new amplitude.Identify().add('EmailCount', -1);
		amplitude.getInstance().identify(identify);
		var identify2 = new amplitude.Identify().add('EmailNonVerifiedCount', -1);
		amplitude.getInstance().identify(identify2);
	}
	
	this.setEmailCount = function(count) {
		var identify = new amplitude.Identify().set('EmailCount', -1);
		amplitude.getInstance().identify(identify);
	}
	
	this.setUserProperty = function(property, value) {
		var identify = new amplitude.Identify().set(property, value);
		amplitude.getInstance().identify(identify);
	}
	
	this.increaseUpdateAcceptedCount = function() {
		var identify = new amplitude.Identify().set("UpdateAcceptedCount", 1);
		amplitude.getInstance().identify(identify);
	}
	
	this.increaseUpdateRejectedCount = function() {
		var identify = new amplitude.Identify().set("UpdateRejectedCount", 1);
		amplitude.getInstance().identify(identify);
	}
	
	this.increaseUpdateCount = function() {
		var identify = new amplitude.Identify().set("UpdateCount", 1);
		amplitude.getInstance().identify(identify);
	}
	
	this.logout = function() {
		amplitude.getInstance().setUserId(null);
		//amplitude.getInstance().regenerateDeviceId();
	}
}

var easeTracker= new tracker();
