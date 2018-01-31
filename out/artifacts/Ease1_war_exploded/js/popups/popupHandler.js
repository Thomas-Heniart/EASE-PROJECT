var currentEasePopup = null;
$("#easePopupsHandler").click(function(e){
	if ($(e.target).attr('id') == 'easePopupsHandler')
		currentEasePopup.close();
});
