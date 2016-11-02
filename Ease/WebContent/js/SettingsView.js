var emailToRemove = null;
function toggleClosestForm(jObj) {
	jObj.parent().find(".setting").toggleClass("show");
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
	
	$(".removeEmail").click(function() {
		emailToRemove = $(this).parent().parent().find("input").val();
		deleteEmailPopup.open();
		deleteEmailPopup.setEmail(emailToRemove);
		
	});
	$(".sendVerificationEmail").click(function() {
		emailToVerify = $(".emailLine").has($(this)).find("input").val();
		emailConfirmationForm.setEmail(emailToVerify);
		$("#SendVerificationEmail button[type='submit']").click();
	});
	$(document).click(function(e) {
		if (!$(e.target).closest("#DeleteEmailPopup .md-content, .removeEmail").length)
			deleteEmailPopup.close();
	});
});

$(function() {
	$('.quit').click(function() {
		$('.SettingsView').removeClass('show');
		$('.col-left').addClass('show');
	});
});