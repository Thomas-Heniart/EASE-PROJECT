<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<form oClass="EditUserPasswordForm" action="EditPassword" id="ModifyPasswordForm" class="setting">
	<p class="smallDescription">For safety reasons, your password needs to be at least 8
						characters long,  including upper-case and lower-case letters,
						plus at least one numerical digit.</p>
	<input oClass="PasswordInput" type="password" name="oldPassword" placeholder="Current password" />
	<input oClass="PasswordInput" type="password" name="password" placeholder="New password" />
	<input oClass="PasswordInput" type="password" name="confirmPassword" placeholder="Confirm new password" />
	<button type="submit">Go</button>
	<p class="response"></p>
</form>

<script>
	var formla = new Form["EditUserPasswordForm"]($("#ModifyPasswordForm"));
</script>