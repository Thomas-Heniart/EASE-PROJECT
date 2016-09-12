<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="MenuButtonSet bottomLeft">
	<button id="enterAddSiteMode" state="off" class="button">
		<img src="resources/icons/add_website_icon.png" />
	</button>
</div>

<div class="RightSideViewTab" id="AddSiteTab">
	<button id="quit">
		<i class="fa fa-times"></i>
	</button>

	<div class="addSiteView">
		<div class="addSiteHeader">
			<p>Add a website to Ease</p>
		</div>

		<div class="addSite">
			<form method="post" id="addSiteForm" action="uploadWebsite"
				enctype="multipart/form-data">
				<input type="text" name="siteName" class="form-control" />
				<input type="file" class="form-control" name="uploadFile" />
				<input type="submit" class="btn btn-default btn-primary" value="Upload" />
			</form>

		</div>
	</div>
</div>
<script>
function enterAddSiteMode() {
	$('#AddSiteTab').addClass('show');
}

function leaveAddSiteMode() {
	$('#AddSiteTab').removeClass('show');
}

$(document).ready(function() {
	$('#enterAddSiteMode').click(function() {
		enterAddSiteMode();
	});
});

$(document).ready(function() {
	$('#AddSiteTab #quit').click(function() {
		leaveAddSiteMode();
	});
});
</script>