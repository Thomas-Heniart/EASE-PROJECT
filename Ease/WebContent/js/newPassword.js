$(document).ready(function(){
		$("#goBack").click(function() {
			window.location = 'index.jsp';
		});
		
		$('#lostPasswordForm').submit(function(e){
			e.preventDefault();
			var password = $("#lostPasswordForm input[name='password']").val();
			var confirmPassword = $("#lostPasswordForm input[name='confirmPassword']").val();
			var linkCode = $("#lostPasswordForm input[name='linkCode']").val();
			$('.alertDiv').removeClass('show');
			var button = $('.lostPasswordBlock .custom-button');
			button.addClass('loading');
			$("#lostPasswordForm .input").removeClass("show");
			postHandler.post(
				$('#lostPasswordForm').attr('action'),
				{
					password: password,
					confirmPassword: confirmPassword,
					linkCode: linkCode
				},
				function(){},
				function(retMsg){
					button.removeClass('loading');
					button.addClass('success');
					setTimeout(function(){
						window.location = "index.jsp";
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