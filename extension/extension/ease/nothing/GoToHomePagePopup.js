I.AddWebsitePopup = function(text) {
	var self = this;

	var style = "<style type='text/css'>.AddWebsitePopup.ui-dialog{z-index:10000;}</style>";
	var body = "<link rel='stylesheet' href='//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css'><div id='GoToHomePagePopup' title='Add a website'><p>" + text + "</p><button id='done'>Done</button></div>";
	
	var onDoneCallback = undefined;

	$('body').append(style + body);
	$('#GoToHomePagePopup').dialog({position: { my: "left top", at: "left top", of: window }, dialogClass:"AddWebsitePopup"});
	this.onDone = function(callback) {
		console.log($('#GoToHomePagePopup #done').length);
		$('#GoToHomePagePopup #done')[0].removeEventListener("mouseup", onDoneCallback);
		onDoneCallback = callback;
		$('#GoToHomePagePopup #done')[0].addEventListener("mouseup", onDoneCallback);
	}

	this.getLoggedPage = function() {
		$('#GoToHomePagePopup p').text("Please log-in");
	}
	this.getLoggedOutPage = function() {
		$('#GoToHomePagePopup p').text("Please log-out");
	}
	this.remove = function() {
		$('#GoToHomePagePopup').dialog('destroy').remove()
	}
}