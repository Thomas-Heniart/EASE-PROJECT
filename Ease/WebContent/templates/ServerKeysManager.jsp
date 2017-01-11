<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="RightSideViewTab" id="ServerKeysManagerTab">
	<button id="quit">
		<i class="fa fa-times"></i>
	</button>

	<form method="post" style="margin-top:5%;" action="addServerCredentials">
		<p>CREATE NEW SERVER CREDENTIALS : </p>
		<div>
			<p>New server credentials :</p>
			<input name="newLogin" type="text" placeholder="Login"></input>
			<input name="newPassword" type="password" placeholder="Passwd"></input>
		</div>
		<div style="margin-top:1%;">
			<p>An admin must validate this new key :</p>
			<input name="login" type="text" placeholder="Login"></input>
			<input name="password" type="password" placeholder="Passwd"></input>
		</div>
		<button type="submit">Submit</button>
	</form>
	
	<form method="post" style="margin-top:5%;" action="removeServerCredentials">
		<p>REMOVE SERVER CREDENTIALS: </p>
		<div>
			<p>Enter login and password to remove your credentials :</p>
			<input name="login" type="text" placeholder="Login"></input>
			<input name="password" type="password" placeholder="Passwd"></input>
		</div>
		<button type="submit">Submit</button>
	</form>
</div>