<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<form oClass="EditUserNameForm" action="editUserName"
	id="ModifyNameForm" class="show">
	<input type="text" name="fname" oClass="NoEmptyInput" value="<%=user.getFirstName()%>">
	<button type="submit">Go</button>
</form>

<script>
	var formlala = new Form["EditUserNameForm"]($("#ModifyNameForm"));
</script>