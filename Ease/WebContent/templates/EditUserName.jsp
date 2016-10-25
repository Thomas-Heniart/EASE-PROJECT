
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

    
<form oClass="EditUserNameForm" action="editUserName" id="ModifyNameForm" class="settingsForm">
	<input oClass="NoEmptyInput" type="text" name="firstName" placeholder="First name" />
	<input oClass="NoEmptyInput" type="text" name="lastName" placeholder="Last name" />
	<div oClass="ClassicErrorMsg" class="alertDiv">
			<p></p>
		</div>
	<div class="buttonSet">
		<button type="submit">Validate</button>
		<button type="button" oClass="CloseButton">Cancel</button>
	</div>
</form>

<script>
	var formlala = new Form["EditUserNameForm"]($("#ModifyNameForm"));
</script>