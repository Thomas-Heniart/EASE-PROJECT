<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<script src="js/addApp.js"></script>
<div oClass="AddAppPopup" class="md-modal md-effect-15 popup" id="PopupAddApp">
	<div class="md-content">
		<div class="popupHeader">
			<img class="logoApp" src="" />
			<div class="textContent">
				<p>Type your password<br>for the last time ;)</p>
			</div>
		</div>
		<%@ include file="AddAppForm.jsp" %>
	</div>
</div>
<script type="text/javascript">
	var popupAddApp = new Popup["AddAppPopup"]($("#PopupAddApp"));
</script>
