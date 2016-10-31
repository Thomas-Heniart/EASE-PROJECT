var ctrlDown = false;
var isMac = false;
if (navigator.appVersion.indexOf("Mac")!=-1) isMac= true;

$(document).ready(function(){
	var ctrlCode = 17;
	if(isMac) {
		if(getUserNavigator() == "Firefox") ctrlCode = 224;
		else if(getUserNavigator() == "Chrome" || getUserNavigator() == "Safari") ctrlCode = 91;
		$(".shortcutInfo").text("Hold cmd and click to open multiple apps.");
	}
	
	$("body").keydown(function(e){
		if(e.which == ctrlCode) {
			$(".shortcutInfo").show();
			ctrlDown = true;
		}
	});
	$("body").keyup(function(e){
		if(e.which == ctrlCode){
			$(".shortcutInfo").hide();
			ctrlDown = false;
		}
	});
});