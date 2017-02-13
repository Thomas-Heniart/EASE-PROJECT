<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>

<div class="easePopup" id="addAppPopup">
	<div class="title">
		<p>Type your password for<br> the last time, ever.</p>
	</div>
	<div class="bodysHandler">
		<div class="popupBody show" id="addApp">
			<div class="handler">
				<div class="row text-center">
					<div class="logo" id="appLogo">
						<img src="/resources/websites/Facebook/logo.png"/>
					</div>
				</div>
				<div class="row text-center">
					<div class="input appName">
						<input id="appName" type="text" name="name" placeholder="App name" />
						<i class="fa fa-pencil placeholderIcon" aria-hidden="true"></i>
					</div>
				</div>
				<div class="row text-center titleRow">
					<p>
						How do you access your<br>
						<span>AppName</span> account ?
					</p>
				</div>
				<div class="row signInChooseRow">
					<button class="btn signInButton facebook" data="Facebook">
						<i class="fa fa-facebook" aria-hidden="true"></i>
						Sign in with Facebook
					</button>
					<button class="btn signInButton linkedIn" data="LinkedIn">
						<i class="fa fa-linkedin" aria-hidden="true"></i>
						Sign in with LinkedIn
					</button>
				</div>
				<div class="row text-center orDelimiter">
					<p class="post-title text-underlined">or</p>
				</div>
				<div class="row signInAccountSelectRow">
					<div class="selectionArea accountSelectArea">
						<div class="afterRowTitle">
							<p>Select your account</p>
							<div class="buttonBack">
								<span class="fa-stack fa-lg">
									<i class="fa fa-circle fa-stack-2x"></i>
									<i class="fa fa-chevron-left fa-stack-1x" aria-hidden="true"></i>
								</span>
								<p>Back</p>
							</div>
						</div>
						<div class="accounts">
							<div class="accountsHolder selectable">
							</div>
							<div class="SignInErrorHandler">
								No <span></span> accounts detected !
							</div>
						</div>
					</div>
				</div>
				<div class="row accountNameHelper">
					<div class="accountNameHandler">
						<div class="backButton">
							<span class="fa-stack fa-lg">
								<i class="fa fa-circle fa-stack-2x"></i>
								<i class="fa fa-chevron-left fa-stack-1x"></i>
							</span>
						</div>
						<div class="accountLine">
							<div class="accountLogo">
								<img src="/resources/websites/Facebook/logo.png"/>
							</div>
							<p class="accountName">sergisergio@gmail.com</p>
						</div>
					</div>
				</div>
				<div class="row ssoSelectAccountRow">
					<div class="selectionArea ssoSelectArea">
						<div class="afterRowTitle">
							<p>Select your account</p>
						</div>
						<div class="accounts">
							<div class="accountsHolder">
								<div class="accountLine newAccountAdder">
									<span class="fa-stack">
										<i class="fa fa-square fa-stack-2x"></i>
										<i class="fa fa-plus fa-stack-1x"></i>
									</span>
									<p class="accountName">Other <span>Google</span> account</p>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="row loginPasswordRow">
					<span class="input login">
						<i class="fa fa-user-o placeholderIcon" aria-hidden="true"></i>
						<input type="text" name="login" id="login" placeholder="Login"/>
					</span>
					<span class="input password">
						<i class="fa fa-lock placeholderIcon" aria-hidden="true"></i>
						<div class="showPassDiv">
							<i class="fa fa-eye centeredItem" aria-hidden="true"></i>
							<i class="fa fa-eye-slash centeredItem" aria-hidden="true"></i>
						</div>
						<input id="password" type="password" name="password" placeholder="Password">
					</span>
				</div>
				<div class="row sameSsoAppsRow">
					<div class="horizontalSelectDiv">
						<div class="afterRowTitle">
							You can have the same account with the following apps. Click to add them.
						</div>
						<div class="selectHandler">
						</div>
					</div>
				</div>
				<div class="row text-center errorText errorHandler">
					<p>
					</p>
				</div>
				<div class="row text-center">
					<button class="btn" type="submit">Add account</button>
				</div>
				<div class="row text-center titleRow">
					<p>
						Or just use without accounts
					</p>
				</div>
				<div class="row text-center">
					<button class="btn" id="shortcutLink">
						<i class="fa fa-link fa-flip-vertical" aria-hidden="true" style="margin-right: 5px"></i>
						Shortcut link</button>
					</div>
					<div class="row text-center">
						<a id="goBack" class="liteTextButton">Go back</a>
					</div>

				</div>
			</div>
		</div>
	</div>
	<script src="js/popups/addAppPopup.js"></script>