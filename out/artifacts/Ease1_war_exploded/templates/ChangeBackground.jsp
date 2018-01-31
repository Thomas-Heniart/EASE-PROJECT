<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

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