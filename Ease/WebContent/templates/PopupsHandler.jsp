<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>

<div class="popupHandler" id="easePopupsHandler">
	<div class="easePopup" id="addUpdatePopup">
		<div class="title">
			<p>Add this web update</p>
		</div>
		<div class="bodysHandler">
			<div class="popupBody show" id="addUpdate">
				<div class="handler">
					<div class="row text-center">
						<div class="logo">
							<img src="/resources/websites/Facebook/logo.png"/>
						</div>
					</div>
					<div class="row text-center">
						<p class="appName post-title">AppName</p>
					</div>
					<div class="emailSendingDiv">
						<div class="row text-center errorText show">
							<p>
								The email used for this<br>account isn’t validated yet.
							</p>
						</div>
						<div class="row text-center">
							<button class="btn emailSend">Send verification email & add the app</button>
						</div>
						<div class="row lineBehind text-center">
							<p class="post-title">
							Or enter account password<br>to add now.
							</p>
						</div>
					</div>
					<form action="lala" method="POST">
						<div class="row">
							<input type="text" name="login" id="login" placeholder="Login" readonly/>
							<span class="input">
								<div class="showPassDiv">
									<i class="fa fa-eye centeredItem" aria-hidden="true"></i>
									<i class="fa fa-eye-slash centeredItem" aria-hidden="true"></i>
								</div>
								<input id="password" type="password" name="password" placeholder="Password">
							</span>
						</div>
						<div class="row text-center errorText errorHandler">
							<p>
							</p>
						</div>
						<div class="row text-center">
							<button class="btn" type="submit">Add this app</button>
						</div>
					</form>
					<div class="row text-center">
						<a id="goBack" class="liteTextButton">Go back</a>
					</div>
				</div>
			</div>
			<div class="popupBody" id="informEmailSent">
				<div class="handler">
					<div class="row text-center">
						<div class="logo">
							<img src="/resources/websites/Facebook/logo.png"/>
						</div>
					</div>
					<div class="row text-center">
						<p class="appName post-title">Facebook</p>
					</div>
					<div class="row text-center">
						<p class="simpleText">
							Click the link in the email<br>you just received ! ;)
						</p>
					</div>
					<div class="row text-center">
						<button class="btn">OK !</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>