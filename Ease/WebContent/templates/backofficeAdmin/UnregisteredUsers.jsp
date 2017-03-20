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
			console.log(data);
			var json = JSON.parse(data);
			json.forEach(function(elem) {
				$("#UnregisteredUsersTab #results").append("<div>"
				+ elem.date
				+ " "
				+ elem.email
				+ "</div>");
			});
		}
	</script>
</div>