<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>

<div class="popupHandler" id="easePopupsHandler">
</div>
<script type="text/javascript">
	$(document).ready(function(){
		asyncLoading.loadHtml({
			urls: ['/templates/popups/modifyAppPopup.html',
			'/templates/popups/addBookmarkPopup.html',
			'/templates/popups/addAppPopup.html',
			'/templates/popups/deleteProfilePopup.html',
			'/templates/popups/deleteAccountPopup.html'],
			appendTo: '#easePopupsHandler'
		});
		asyncLoading.loadScripts({
			urls: ['/js/popups/modifyAppPopup.js',
			'js/popups/addAppPopup.js',
			"js/popups/deleteAccountPopup.js",
			"js/popups/deleteProfilePopup.js",
			"js/popups/addBookmarkPopup.js",
			"js/popups/popupHandler.js"],
			async: true
		});
	});
</script>