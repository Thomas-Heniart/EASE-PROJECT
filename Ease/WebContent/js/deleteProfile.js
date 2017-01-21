$(document).ready( function() {
	$('#PopupDeleteProfile').find('#close').click(function(){
		$('#PopupDeleteProfile').removeClass('md-show');
	});
	$('#PopupDeleteProfile').find('#password').keyup(function(event){
	    if(event.keyCode == 13){
	        $("#PopupDeleteProfile .md-content .buttonSet #accept").click();
	    }
	});

	$('#PopupDeleteProfile').find('#accept').click(function(){
		var parent = $(this).closest('.popup');
		
     	$('#loading').addClass("la-animate");
     	$(parent).find('.alertDiv').removeClass('show');
		postHandler.post(
			'RemoveProfile',
			{
				index: $(parent).find('#index').val(),
				password: $(parent).find('#password').val()
			},
			function(){$('#loading').removeClass("la-animate");},
	        function(retMsg) {
					easeTracker.trackEvent("DeleteProfile");
                  	window.location.replace("index.jsp");
			},
			function(retMsg){
				$(parent).find('.alertDiv').addClass('show');
      	  		$(parent).find('#password').val('');
        	  	showAlertPopup(retMsg, true);
			},
	        'text'
		);
	});
	
});