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
		
		$('#helloButton').click(function() {
			var parent = $(this).closest('form');
			var email = $(parent).find("#email").val();
			var password = $(parent).find("#password").val();
			$(parent).find('.alertDiv').removeClass('show');
			$(parent).hide();
			$(".ease-logo").hide();
			$("#loadingUnknownUser").css("display","block");
			$("#back").hide();
			postHandler.post('connection',
					{
						email : email,
						password : password
					},
					function(){},
					function(retMsg) {
						easeTracker.trackEvent("Connect");
						window.location.reload();
					}, 
					function(retMsg) {
						$("#back").show();
						$(parent).show();
						$(".ease-logo").show();
						$("#loadingUnknownUser").css("display","none");
						$(parent).find('.alertDiv p').text(retMsg);
						$(parent).find('.alertDiv').addClass('show');
						$(parent).find('#password').val('');
					},
					'text'
				);
		});
		
		$("#loginForm").submit(function(e) {
			e.preventDefault();
		});
		
		$('.savedUser #savedUserButton').click(function(){
			var parent = $(this).closest('.savedUser');
			var email = getCookie('email');
			var password = parent.find('#password').val();

			email = email.substring(1, email.length - 1);
			//console.log("Connection for email "+email+" requested.");
			$(parent).find('.alertDiv').removeClass('show');
			$(parent).closest(".forget-password").hide();
			$(parent).hide();
			$(".ease-logo").hide();
			$("#loadingKnownUser").css("display","block");
			$("#changeAccount").hide();
		    postHandler.post(
	                'connection', 
	                {
	                    email : email, 
	                    password : password
	                },
	                function(){},
	                function(retMsg){
						easeTracker.trackEvent("Connect");
	                    window.location.reload();
	                },
	                function(retMsg){
	                	$("#changeAccount").show();
	                	$(parent).closest(".forget-password").show();
	        			$(parent).show();
	        			$(".ease-logo").show();
	        			$("#loadingKnownUser").css("display","none");
	                	$(parent).find('.alertDiv p').text(retMsg);
	                	$(parent).find('.alertDiv').addClass('show');
	                	$(parent).find('#password').val('');
	                },
	                'text' // Nous souhaitons recevoir "Success" ou "Failed", donc on indique text !
		    	);
		});
		
		var changeAccButton = $('#changeAccount');
		var opened = true;

		if (changeAccButton){
			var knownUser = $('#knownUser');
			var newUser = $('#unknownUser');
			var name = $('#userName').text();

			$('#userName').remove();
			changeAccButton.click(function(){
				if (opened){
					knownUser.removeClass('show');
					newUser.addClass('show');
					changeAccButton.text(name + "'s account");
				}else {
					knownUser.addClass('show');
					newUser.removeClass('show');
					changeAccButton.text("Other account");
				}
				opened = !opened;
			});
		}

/*		var nbForms = $('.FormsContainer > *').length;
		if (nbForms > 1){
			$('#changeAccount').click(function(){
				$('#loginForm span input').val("");
				$('#password').val("");
				$('#unknownUser').css('visibility', 'visible');
				$('#knownUser').css('visibility', 'hidden');;
				$('#back').click(function(){
					$('#knownUser').css('visibility', 'visible');
					$('#unknownUser').css('visibility', 'hidden');;
				});
			});
		}*/
		
		if ($('.savedUser').length) $('.savedUser #password').focus();
		$('.savedUser #password').keyup(function(event){ 
			if(event.keyCode == 13) $('.savedUser #savedUserButton').click();
		});	
});