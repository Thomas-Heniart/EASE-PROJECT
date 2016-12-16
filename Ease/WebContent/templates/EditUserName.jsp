<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<form oClass="EditUserNameForm" action="EditName"
	id="ModifyNameForm" class="setting show">
	<input type="text" name="fname" oClass="NoEmptyInput" value="<%=user.getFirstName()%>">
	<button type="submit">Go</button>
	<p class="errorMessage"></p>
</form>

<script>
	var formlala = new Form["EditUserNameForm"]($("#ModifyNameForm"));
</script>