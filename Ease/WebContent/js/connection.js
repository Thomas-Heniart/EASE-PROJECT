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
			easeTracker.setUserId(email);
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
					easeTracker.setUserId(email);
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


	$('.controls #passwordLost').click(function(){
		$('.controls').removeClass('show');
		$('.FormsContainer #connection').removeClass('show');
		$('.FormsContainer #passwordLost').addClass('show');
		$('.FormsContainer #passwordLost .alertDiv').removeClass('show');
		$('.FormsContainer #passwordLost input').val('');
	});

	$('.FormsContainer #passwordLost #goBack').click(function(){
		$('.controls').addClass('show');
		$('.FormsContainer #connection').addClass('show');
		$('.FormsContainer #passwordLost').removeClass('show');
	});
	$('form#passwordLost').submit(function(e){
		var self = $(this);
		e.preventDefault();
		self.find('.alertDiv').removeClass('show');
		self.find('button').removeClass('show');
		self.find('.sk-fading-circle').addClass('show');
		postHandler.post(
			$(this).attr('action'),
			{
				email : $(this).find("input[name='email']").val()
			},
			function(){

			},
			function(data){
				self.find('.alertDiv p').text(data);
				self.find('.alertDiv').addClass('show');
				self.find('button').addClass('show');
				self.find('.sk-fading-circle').removeClass('show');
			},
			function(data){
				self.find('.alertDiv p').text(data);
				self.find('.alertDiv').addClass('show');
				self.find('button').addClass('show');
				self.find('.sk-fading-circle').removeClass('show');
			},
			'text');
	});

	if ($('.savedUser').length) $('.savedUser #password').focus();
	$('.savedUser #password').keyup(function(event){
		if(event.keyCode == 13) $('.savedUser #savedUserButton').click();
	});
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
				loading.removeClass('show');
			},
			function(data){
				easeTracker.setUserId(email);
				easeTracker.trackEvent("Connect");
				window.location.reload();
			},
			function(data){
				self.closest('.landingPopup').addClass('show');
				errorDiv.find('p').text(data);
				errorDiv.addClass('show');
			},
			'text'
			);
	});
})