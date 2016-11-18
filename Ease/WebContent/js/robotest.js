$(document).ready(function(){
	var majCode = 16;
	if(isMac) {
		if(getUserNavigator() == "Firefox") ctrlCode = 224;
		else if(getUserNavigator() == "Chrome" || getUserNavigator() == "Safari") ctrlCode = 91;
		$(".shortcutInfo").text("Hold cmd and click to open multiple apps.");
	}
	var textCtrl = $(".shortcutInfo").text();
	
	$("body").keydown(function(e){
		if(e.which == majCode) {
			$(".shortcutInfo").text("Hold shift and click to test an app.");
			$(".shortcutInfo").show();
			testApp = true;
		}
	});
	$(window).blur(function(e){
		$(".shortcutInfo").hide();
		$(".shortcutInfo").text(textCtrl);
		testApp = false;
	});
	$("body").keyup(function(e){
		if(e.which == majCode){
			$(".shortcutInfo").hide();
			$(".shortcutInfo").text(textCtrl);
			testApp = false;
		}
	});
});