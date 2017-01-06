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
}

var easeTracker= new tracker();
