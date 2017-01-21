$(document).ready(function(){
		$("#goBack").click(function() {
			window.location = 'index.jsp';
		});
		
		$("#custom-confirm button").click(function(e) {
			$("#custom-confirm").removeClass("show");
			$("#security").removeClass("show");
			$(".custom-button").addClass("show");
			$("#enterEmail").addClass("show");
			$(".input").addClass("show");
		});
		
		$('#lostPasswordForm').submit(function(e){
			e.preventDefault();
			var email = $("#lostPasswordForm input[name='email']").val();
			$('.alertDiv').removeClass('show');
			var button = $('.lostPasswordBlock .custom-button');
			button.addClass('loading');
			postHandler.post(
				$('#lostPasswordForm').attr('action'),
				{
					email: email
				},
				function(){},
				function(retMsg){
					button.removeClass('loading');
					button.addClass('success');
					easeTracker.trackEvent("PasswordLostSent");
					setTimeout(function(){
						window.location = "index.jsp";
					}, 3000);
				},
				function(retMsg){
					var str = retMsg;
					$('.alertDiv').css('color', 'red');
					$('.alertDiv').text(str);
					$('.alertDiv').addClass('show');
					button.removeClass('loading');
				},
				'text'
			);
		});
	});