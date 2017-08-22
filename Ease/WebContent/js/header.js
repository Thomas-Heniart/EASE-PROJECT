var Header = function(rootEl){
	var self = this;
	this.rootEl = rootEl;
}

var easeHeader;
$(document).ready(function(){
	easeHeader = new Header($('.header'));
});

function goToSettings(){
	$('.SettingsView').addClass('show');
	$('.col-left').removeClass('show');
	$('.MenuButtonSet').removeClass('show');
}

$(document).ready(function() {
	$("#userSettingsButton").click(function() {
		$(".userSettings").toggleClass("show");
	});

	$(document).click(function(e) {
		if (!$(e.target).closest(".userSettings, #userSettingsButton").length)
			$(".userSettings").removeClass("show");
	});
	$('#ModifyUserButton').click(function() {
		easeTracker.trackEvent("ClickSettingsFromDashboard");
		goToSettings();
	});
	if($("body").hasClass("picBckgrnd")){
		$('#backgroundSwitch').prop("checked", true);
	} else {
		$('#backgroundSwitch').prop("checked", false);
	}
	$("#backgroundSwitch").change(function() {
		if($("body").hasClass("picBckgrnd")){
			$("body").switchClass("picBckgrnd", "logoBckgrnd");
		} else if($("body").hasClass("logoBckgrnd")){
			$("body").switchClass("logoBckgrnd", "picBckgrnd");
		}
		var self = $(this);
		postHandler.post(
			'changeUserBackground',
			{},
			function(){},
			function(retMsg){
				easeTracker.trackEvent("DailyPhotoSwitch");
				easeTracker.setDailyPhoto(self.is("checked"));
			},
			function(retMsg){
				showAlertPopup(retMsg, true);
			},
			"text"
			);
	});
});