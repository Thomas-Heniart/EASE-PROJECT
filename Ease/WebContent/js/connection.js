$(document).ready(function() {
	if(window.location.search.split("?")[1]=="logout"){
		$(".logout-overlay").show();
		setTimeout(function(){
			$(".logout-overlay").addClass("transition");
			setTimeout(function(){
				$(".logout-overlay").hide();
			},1000);
		},3000);
	}
});

$(document).ready(function(){
	var loading = $('#loading');

	if ($(".otherAccountButton").length){
		$(".otherAccountButton").click(function(){
			$(".landingPopup.show").removeClass('show');
			$("#unknownUser").addClass('show');
		});
	}

	if ($(".knownAccountButton").length){
		$(".knownAccountButton").click(function(){
			$(".landingPopup.show").removeClass('show');
			$("#knownUser").addClass('show');			
		});
	}

	$(".pwdLostButton").click(function(){
		var lastPopup = $(this).closest('.landingPopup');
		easeTracker.trackEvent("PasswordLostVisit");
		$(".landingPopup.show").removeClass('show');
		$("#passwordLost").addClass('show');

		$("#passwordLost .backButton").one('click', function(){
			$("#passwordLost").removeClass('show');
			$(lastPopup).addClass('show');
		});	
	});

	$('#passwordLostForm').submit(function(e){
		e.preventDefault();
		var self = $(this);
		var errorDiv = $(this).find('.alertDiv');
		var email = $(this).find("input[name='email']").val();

		if (!email.length)
			return;
		errorDiv.removeClass('show');
		loading.addClass('show');
		self.closest('.landingPopup').removeClass('show');
		postHandler.post(
			$(this).attr('action'),
			{
				email: email
			},
			function(){
				loading.removeClass('show');
				self.closest('.landingPopup').addClass('show');
			},
			function(data){
				easeTracker.trackEvent("LoginpageLostPasswordSent");
				errorDiv.find('p').text(data);
				errorDiv.addClass('show');
			},
			function(data){
				errorDiv.find('p').text(data);
				errorDiv.addClass('show');
			},
			'text'
			);
	});
	$('#knownUserForm, #unknownUserForm').submit(function(e){
		e.preventDefault();
		var self = $(this);
		var errorDiv = $(this).find('.alertDiv');
		var email = $(this).find("input[name='email']").val();
		var password = $(this).find("input[name='password']").val();

		if (!email.length || !password.length)
			return;
		errorDiv.removeClass('show');
		loading.addClass("show");
		self.closest('.landingPopup').removeClass('show');
		postHandler.post(
			$(this).attr('action'),
			{
				email: email,
				password: password
			},
			function(){
			},
			function(data){
				easeTracker.setUserId(email);
				easeTracker.trackEvent("Connect");
				window.location.reload();
			},
			function(data){
				loading.removeClass('show');
				self.closest('.landingPopup').addClass('show');
				errorDiv.find('p').text(data);
				$("#knownUserForm, #unknownUserForm").find("input[name='password']").val("");
				errorDiv.addClass('show');
			},
			'text'
			);
	});
})