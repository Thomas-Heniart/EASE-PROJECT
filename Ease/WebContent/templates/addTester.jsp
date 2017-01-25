<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="RightSideViewTab" id="AddTesterTab">
	<button id="quit">
		<i class="fa fa-times"></i>
	</button>
	
	<form id="addTester" method="POST" action="AddTesterWithInfra">
		<input type="text" id="email" placeholder="email" name="email" />
		<input type="text" id="name" placeholder="name" name="name" />
		<input type="text" id="infra" placeholder="infra" name="infra" />
		<input type="text" id="profileColor" placeholder="color" name="profileColor" />
		<button type="submit">Submit</button>
	</form>
</div>
<script>
	$("#addTester").submit(function(e) {
		e.preventDefault();
		var self = $(this);
		var email = $("#addTester #email");
		var name = $("#addTester #name");
		var infra = $("#addTester #infra");
		var profileColor = $("#addTester #profileColor");
		postHandler.post(self.attr("action"), function() {
			email: email,
			name: name,
			infra: infra,
			profileColor: profileColor
		},
		function(retMsg) {
			self.find("input").val("");
		},
		function(retMsg) {
			
		});
	});
</script>