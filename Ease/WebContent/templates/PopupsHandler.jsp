<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>

<div class="popupHandler" id="easePopupsHandler">
	<%@ include file="popups/addUpdatePopup.jsp" %>
	<%@ include file="popups/addAppPopup.jsp" %>
	<%@ include file="popups/addBookmarkPopup.jsp" %>
	<%@ include file="popups/deleteProfilePopup.jsp" %>
	<%@ include file="popups/modifyAppPopup.jsp" %>
</div>
<script type="text/javascript">
	var currentEasePopup = null;
	$("#easePopupsHandler").click(function(e){
		if ($(e.target).attr('id') == 'easePopupsHandler')
			currentEasePopup.close();
	});
</script>