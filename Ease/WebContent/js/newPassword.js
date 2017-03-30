$(document).ready(function(){
		$("#goBack").click(function() {
			window.location = '/';
		});
		
		$('#lostPasswordForm').submit(function(e){
			e.preventDefault();
			var password = $("#lostPasswordForm input[name='password']").val();
			var confirmPassword = $("#lostPasswordForm input[name='confirmPassword']").val();
			var linkCode = $("#lostPasswordForm input[name='linkCode']").val();
			var email = $("#lostPasswordForm input[name='email']").val();
			$('.alertDiv').removeClass('show');
			var button = $('.lostPasswordBlock .custom-button');
			button.addClass('loading');
			$("#lostPasswordForm .input").removeClass("show");
			postHandler.post(
				$('#lostPasswordForm').attr('action'),
				{
					email: email,
					password: password,
					confirmPassword: confirmPassword,
					linkCode: linkCode
				},
				function(){},
				function(retMsg){
					button.removeClass('loading');
					button.addClass('success');
					easeTracker.trackEvent("LoginpageNewPasswordSetup");
					setTimeout(function(){
						window.location = "/";
					}, 3000);
				},
				function(retMsg){
					$('.alertDiv').css('color', 'red');
					$('.alertDiv p').text(retMsg);
					$('.alertDiv').addClass('show');
					$("#lostPasswordForm .input").addClass("show");
					button.removeClass('loading');
				},
				'text'
			);
		});
	});