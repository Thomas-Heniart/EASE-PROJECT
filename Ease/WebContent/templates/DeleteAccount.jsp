<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div class="setting">
	<button id="deleteAccountButton">I want to delete my account</button>
</div>
<script>
	$("#deleteAccountButton").click(function(e) {
		e.stopPropagation();
		easeDeleteAccountPopup.open();
	});
</script>