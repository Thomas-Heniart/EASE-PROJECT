$(document).ready(function() {
	postHandler.post("GetUnregisteredEmails", {}, function() {
		$("#UnregisteredUsersTab #results div").remove()
	}, printEmailsFromJson, function(data) {
	});
	
	$("#UnregisteredUsersTab #results").on("click", ".delete-unregistered-email",
			function(e) {
				var self = $(this);
				var email = self.parent().attr("email");
				postHandler.post("DeleteUnregisteredEmail", {
					email : email
				}, function() {
				}, function(data) {
					self.parent().remove();
				}, function(data) {
				});
			});
});

function printEmailsFromJson(data) {
	var json = JSON.parse(data);
	json
			.forEach(function(elem) {
				$("#UnregisteredUsersTab #results")
						.append(
								"<div email='"
										+ elem.email
										+ "'>"
										+ "<i class='fa fa-times delete-unregistered-email'></i>"
										+ elem.date + " " + elem.email
										+ "</div>");
			});
}