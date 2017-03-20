<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="RightSideViewTab" id="UnregisteredUsersTab">
	<button id="quit">
		<i class="fa fa-times"></i>
	</button>
	<div class="presentation">
		<h1>Unregistered emails</h1>
	</div>
	<div class="flex-row">
		<div class="centered-row" id="results"></div>
	</div>
	<script>
		postHandler.post("GetUnregisteredEmails", {},
			function(){ $("#UnregisteredUsersTab #results div").remove() },
			printEmailsFromJson,
			function(data) {
				
			});
		
		function printEmailsFromJson(data) {
			var json = JSON.parse(data);
			json.forEach(function(elem) {
				$("#UnregisteredUsersTab #results").append("<div email='"
				+ elem.email
				+ "'>"
				+ "<i class='fa fa-times delete-unregistered-email'></i>"
				+ elem.date
				+ " "
				+ elem.email
				+ "</div>");
			});
		}

		$(document).on('click', function(e) {
			var target = $(e.target);
			if (target.hasClass('delete-unregistered-email')) {
				var email = target.parent().attr("email");
				postHandler.post("DeleteUnregisteredEmail", {
					email: email
				}, function() {
					
				},
				function(data) {
					target.parent().remove();
				},
				function(data){});	
			}
		});
	</script>
</div>