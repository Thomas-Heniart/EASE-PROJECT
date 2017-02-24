<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="RightSideViewTab" id="AddTesterTab">
	<button id="quit">
		<i class="fa fa-times"></i>
	</button>
	
	<form id="addTester" action="AddTesterWithInfra">
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
	var email = $("#email", self).val();
	var name = $("#name", self).val();
	var infra = $("#infra", self).val();
	var profileColor = $("#profileColor", self).val();
	console.log("ici");
	var action = self.attr("action");
	console.log(email + " " + name + " " + infra + " " + profileColor);
	postHandler.post("AddTesterWithInfra", {
		email: email,
		name: name,
		infra: infra,
		profileColor: profileColor
		},
		function() {},
		function(retMsg) {
			$("input", self).val("");
		},
		function(retMsg) {}, 'text');
	console.log("ici2");
});
</script>