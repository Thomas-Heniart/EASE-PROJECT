<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div class="MenuButtonSet">
	<button id="enterEditMode" state="off" class="button"><img src="resources/icons/menu_icon.png"/></button>
	<div class="openCatalogHelper"></div>
</div>

<script>
$(document).ready(function(){
	$('#enterEditMode').click(function(){
		enterEditMode();
//		enterEditMode();
//		$('.CatalogViewTab').addClass('show');
	});
});
</script>