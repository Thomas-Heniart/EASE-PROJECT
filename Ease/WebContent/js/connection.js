$(document).ready(function() {
		$('#helloButton').click(function() {
			$('#loading').addClass("la-animate");
			var parent = $(this).closest('form');
			var email = $(parent).find("#email").val();
			var password = $(parent).find("#password").val();

			$(parent).find('.alertDiv').removeClass('show');
			
			postHandler.post('connection',
					{
						email : email,
						password : password
					},
					function(){},
					function(retMsg) {
						$('#loading').removeClass("la-animate");
						window.location.replace("index.jsp");
					}, 
					function(retMsg) {
						$('#loading').removeClass("la-animate");
						$(parent).find('.alertDiv').addClass('show');
						$(parent).find('#password').val('');
					},
					'text');
		});
		
		$("#loginForm").submit(function(e) {
			e.preventDefault();
		});
		
		$('.savedUser #savedUserButton').click(function(){
			var parent = $(this).closest('.form');
			var email = getCookie('email');
			var password = parent.find('#password').val();

			email = email.substring(1, email.length - 1);
	  		$('#loading').addClass("la-animate");
		    postHandler.post(
	                'connection', 
	                {
	                    email : email, 
	                    password : password
	                },
	                function(){},
	                function(retMsg){
	                	$('#loading').removeClass("la-animate");
	                    window.location.replace("index.jsp");
	                },
	                function(retMsg){
	                	$('#loading').removeClass("la-animate");
	                	$(parent).find('.alertDiv').html(retMsg);
	                	$(parent).find('.alertDiv').addClass('show');
	                	$(parent).find('#password').val('');
	                },
	                'text' // Nous souhaitons recevoir "Success" ou "Failed", donc on indique text !
		    	);
		});
	});