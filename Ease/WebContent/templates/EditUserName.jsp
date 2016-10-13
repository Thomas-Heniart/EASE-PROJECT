
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

    
<form oClass="EditUserNameForm" action="editUserName" id="ModifyNameForm">
	<input oClass="NoEmptyInput" type="text" name="firstName" placeholder="First name" />
	<input oClass="NoEmptyInput" type="text" name="lastName" placeholder="Last name" />
	<div>
	<button type="submit">Validate</button>
	<button type="button">Cancel</button>
	</div>
</form>

<script>
var formlala = new Form["EditUserNameForm"]($("#ModifyNameForm"));
</script>