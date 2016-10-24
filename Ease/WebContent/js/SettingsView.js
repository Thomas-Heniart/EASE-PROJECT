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
			
});

$(function() {
	$('.quit').click(function() {
		$('.SettingsView').removeClass('show');
		$('.col-left').addClass('show');
	});
	/*$("#settingsTab").accordion({
		active : 10,
		collapsible : true,
		autoHeight : false,
		heightStyle : "content"
	});
	$('#settingsTab #cancel').click(function() {
		var Accordion = $(this).closest('.ui-accordion');

		$(Accordion).find('input').val('');
		$(Accordion).accordion("option", "active", 10);

	});*/
});