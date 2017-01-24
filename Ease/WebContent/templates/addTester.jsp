<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="RightSideViewTab" id="AddTesterTab">
	<button id="quit">
		<i class="fa fa-times"></i>
	</button>
	
	<form id="addTester" method="POST" action="AddTesterWithInfra">
		<input type="text" placeholder="email" name="email" />
		<input type="text" placeholder="name" name="name" />
		<input type="text" placeholder="infra" name="infra" />
		<input type="text" placeholder="color" name="profileColor" />
		<button type="submit">Submit</button>
	</form>
</div>
<script>
	$("#addTester").submit(function(e) {
		e.preventDefault();
		var self = $(this);
		postHandler.post(self.attr("action"), function() {
			
		},
		function(retMsg) {
			self.find("input").val("");
		},
		function(retMsg) {
			
		}):
	});
</script>