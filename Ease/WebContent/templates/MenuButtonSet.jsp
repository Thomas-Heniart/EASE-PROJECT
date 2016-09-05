<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div class="MenuButtonSet">
<!-- 	<button id="addProfileButton" class="button button--sacnite button--round-l"><i class="button__icon icon icon-plus"></i><span>User</span></button>
	<button class="button button--sacnite button--round-l"><i class="button__icon icon icon-bookmark"></i><span>Bookmark</span></button>
 -->	<button id="enterEditMode" state="off" class="button"><img src="resources/icons/menu_icon.png"/></button>
</div>

<script>
$(document).ready(function(){
	$('#enterEditMode').click(function(){
		enterEditMode();
//		$('.CatalogViewTab').addClass('show');
	});
});
</script>