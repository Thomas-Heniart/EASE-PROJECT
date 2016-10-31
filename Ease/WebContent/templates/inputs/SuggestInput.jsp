<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="login-group-input">
	<input oClass="NoEmptyInput" type="text" name="login"
		placeholder="Login" value="" /><i
		class="fa fa-caret-down email-suggestions" aria-hidden="true"></i>
	<div class="suggested-emails">
		<c:forEach items='${user.getEmails().keySet()}' var="item">
			<p class="email-suggestion" email="${item}">
				<span>${item}</span>
			</p>
		</c:forEach>
	</div>
</div>