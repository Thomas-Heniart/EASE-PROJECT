<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>

<div class="popupHandler" id="easePopupsHandler">
</div>
<script type="text/javascript">
	$(document).ready(function(){
		$.get('/templates/popups/addAppPopup.jsp').success(function(data)
		{
			$('#easePopupsHandler').append(data);
		});
		$.get('/templates/popups/addBookmarkPopup.jsp').success(function(data)
		{
			$('#easePopupsHandler').append(data);
		});
		$.get('/templates/popups/modifyAppPopup.jsp').success(function(data)
		{
			$('#easePopupsHandler').append(data);
		});
		$.get('/templates/popups/deleteProfilePopup.jsp').success(function(data)
		{
			$('#easePopupsHandler').append(data);
		});
		$.get('/templates/popups/deleteAccountPopup.jsp').success(function(data)
		{
			$('#easePopupsHandler').append(data);
		});		
	});
	var currentEasePopup = null;
	$("#easePopupsHandler").click(function(e){
		if ($(e.target).attr('id') == 'easePopupsHandler')
			currentEasePopup.close();
	});
</script>