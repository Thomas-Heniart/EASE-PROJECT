<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="MenuButtonSet toptopLeft">
	<button id="enterChangeBackMode" class="button adminButton">
		<img src="resources/icons/upload_back.png" />
	</button>
</div>

<div class="RightSideViewTab" id="ChangeBackTab">
	<button id="quit">
		<i class="fa fa-times"></i>
	</button>

	<div>
		<div>
			<p>Change background of Ease homepage</p>
		</div>

		<div>
			<form method="post" id="changeBackForm" action="changeBackground"
				enctype="multipart/form-data">
				<h4 style="margin-left: 25%; font-size: 1em">Upload a picture for Ease homepage</h4>
				<input id="bckgrnd" type="file" class="form-control" name="uploadBackground" />
				<input type="submit" class="btn btn-default btn-primary" value="Upload" />
			</form>

		</div>
	</div>
</div>
<script>
function enterChangeBackMode() {
	$('#ChangeBackTab').addClass('show');
}

function leaveChangeBackMode() {
	$('#ChangeBackTab').removeClass('show');
}

$(document).ready(function() {
	$('#enterChangeBackMode').click(function() {
		leaveAddUsersMode();
		leaveTagsManagerMode();
		leaveRequestedWebsitesMode();
		leaveAddSiteMode();
		enterChangeBackMode();
	});
});

$(document).ready(function() {
	$('#ChangeBackTab #quit').click(function() {
		leaveChangeBackMode();
	});
});
</script>