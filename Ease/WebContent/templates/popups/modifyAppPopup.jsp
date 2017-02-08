<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>

<div class="easePopup" id="modifyAppPopup">
	<div class="title">
		<p>App settings</p>
	</div>
	<div class="bodysHandler">
		<div class="popupBody show" id="modifyApp">
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
				<div class="row fragmentsRow">
					<ul class="tabChooser">
						<li><a href="#tabInfo">Info</a></li>
						<li><a href="#tabDelete">Delete app</a></li>
					</ul>
					<div class="row" id="tabInfo">
						<div class="row">
							<p class="post-title">
								Account info
							</p>
						</div>
						<!-- sign in choose -->
						<div class="row signInChooseRow">
							<button class="btn signInButton facebook" data="Facebook">
								<i class="fa fa-facebook" aria-hidden="true"></i>
								Sign in with Facebook
							</button>
							<button class="btn signInButton linkedIn" data="Linkedin">
								<i class="fa fa-linkedin" aria-hidden="true"></i>
								Sign in with LinkedIn
							</button>
						</div>
						<div class="row signInAccountSelectRow">
							<div class="selectionArea accountSelectArea">
								<div class="afterRowTitle">
									<p>Select your account</p>
<!--									<div class="buttonBack">
										<span class="fa-stack fa-lg">
											<i class="fa fa-circle fa-stack-2x"></i>
											<i class="fa fa-chevron-left fa-stack-1x" aria-hidden="true"></i>
										</span>
										<p>Back</p>
									</div>-->
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
						<!-- login + password row -->
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
								<input id="password" type="password" name="password" placeholder="Password"/>
							</span>
						</div>
						<div class="row sameSsoAppsRow">
							<div class="horizontalSelectDiv">
								<div class="afterRowTitle">
									Login or password modfications<br>will also apply to :
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
							<button class="btn" type="submit">Modify</button>
						</div>
					</div>
					<div class="row" id="tabDelete">
						<div class="row infoRow">
							<p class="simpleText">
								Are you sure you want to <span>remove this app</span> from your space ?
							</p>
							<p class="simpleText">
								The corresponding data will be <span>lost permanently</span>.
							</p>
						</div>
						<div class="row text-center errorText errorHandler">
							<p>
							</p>
						</div>
						<div class="row text-center">
							<button class="btn" type="submit">Delete</button>
						</div>
					</div>
				</div>
				<div class="row text-center">
					<a id="goBack" class="liteTextButton">Go back</a>
				</div>
			</div>
		</div>
	</div>
</div>

<script src="js/popups/modifyAppPopup.js"></script>