$(document).ready(function() {
	$(".login-group-input input[name='login']").on('focus', function() {
		$(".suggested-emails").addClass("show");
	});
	$(".login-group-input + input[type='password']").on('focus', function() {
		$(".suggested-emails").removeClass("show");
	})
	$('.login-group-input .email-suggestions').click(function() {
		$(".suggested-emails").toggleClass("show");
	});
	$(".suggested-emails .email-suggestion").click(function() {
		$(".login-group-input input[name='login']").val($(this).find("span").html());
		$(".login-group-input input[name='login']").change();
		$(".suggested-emails").removeClass("show");
		$(".login-group-input + input[name='password']").click();
		$(".login-group-input + input[name='password']").focus();
	});
});