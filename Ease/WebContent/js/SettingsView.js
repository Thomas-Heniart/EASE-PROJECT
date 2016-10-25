var emailToRemove = null;
function toggleClosestForm(jObj) {
	jObj.parent().find("form").toggleClass("show");
	jObj.parent().find("i.fa-caret-right").toggleClass("down");
}

$(document).ready(function() {
	$("#settingsTab .sectionHeader i.fa-caret-right").click(function() {
		toggleClosestForm($(this));
	});
	$("#settingsTab .sectionHeader p").click(function() {
		toggleClosestForm($(this));
	});
	$(".newEmail").click(function() {
		$(this).removeClass("show");
		$(".newEmailInput").addClass("show");
	});
	
	$(".unverifiedEmail").click(function() {
		//TODO
	});
	
	$(".removeEmail").click(function() {
		emailToRemove = $(this).parent().parent().find("input").val();
		deleteEmailPopup.setEmail(emailToRemove);
		console.log(deleteEmailPopup.oForm.oInputs[0].getVal());
		deleteEmailPopup.open();
		
	});
	$(document).click(function(e) {
		if (!$(e.target).closest('md-content, .removeEmail, .md-modal, .md-overlay').length)
			deleteEmailPopup.close();
	});
});

$(function() {
	$('.quit').click(function() {
		$('.SettingsView').removeClass('show');
		$('.col-left').addClass('show');
	});
});