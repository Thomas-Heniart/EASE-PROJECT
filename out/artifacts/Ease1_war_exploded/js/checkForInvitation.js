$(document).ready(function(){
		$('#registrationForm').submit(function(e){
			e.preventDefault();
			var email = $("#registrationForm input[name='email']").val();
			$('.alertDiv').removeClass('show');
			var button = $('.registrationBlock .custom-button');
			button.addClass('loading');
			postHandler.post(
				$('#registrationForm').attr('action'),
				{
					email: email
				},
				function(){},
				function(retMsg){
					button.removeClass('loading');
					button.addClass('success');
					setTimeout(function(){
						button.removeClass('success');
					}, 5000);
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