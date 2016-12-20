<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<link rel="stylesheet" href="css/Tutorial.css" />

<div class="popupHandler myshow" id="tutorial">
	<div class="easePopup" id="importation">
		<div class="title">
			<p>Accounts importation</p>
		</div>
		<div class="bodysHandler">
			<div class="popupBody show" id="selectScraping">
				<div class="handler">
					<div class="row text-center">
						<p class="sub-title">Build your ease plateform<br> in 1 minute.</p>
					</div>
					<div class="row text-center">
						<p class="post-title" style="text-decoration:underline;">Import your accounts from :</p>
					</div>
					<div class="row">
						<div class="account">
							<div class="logo">
								<img src="resources/images/Chrome.png"/>
							</div>
							<p class="name">Google Chrome</p>
							<div class="onoffswitch">
								<input type="checkbox" name="Chrome" class="onoffswitch-checkbox" id="Chrome">
								<label class="onoffswitch-label" for="Chrome"></label>
							</div>
						</div>
						<div class="account">
							<div class="logo">
								<img src="resources/websites/Linkedin/logo.png"/>
							</div>
							<p class="name">LinkedIn</p>
							<div class="onoffswitch">
								<input type="checkbox" name="Linkedin" class="onoffswitch-checkbox" id="Linkedin">
								<label class="onoffswitch-label" for="Linkedin"></label>
							</div>
						</div>
						<div class="account">
							<div class="logo">
								<img src="resources/websites/Facebook/logo.png"/>
							</div>
							<p class="name">Facebook</p>
							<div class="onoffswitch">
								<input type="checkbox" name="Facebook" class="onoffswitch-checkbox" id="Facebook">
								<label class="onoffswitch-label" for="Facebook"></label>
							</div>
						</div>
					</div>
					<div class="row text-center">
						<button class="btn" type="submit">Go!</button>
					</div>
					<div class="row text-center">
						<a>I prefere to import all my accounts 1 by 1</a>
					</div>
				</div>
			</div>
			<div class="popupBody" id="accountCredentials">
				<div class="handler">
					<div class="row text-center">
						<p class="sub-title">Your <span>Google Chrome</span> account is needed</p>
					</div>
					<div class="row text-center">
						<div class="logo">
							<div class="imageHandler">
								<img src="resources/images/Chrome.png"/>
							</div>
						</div>
					</div>
					<div class="row text-center">
						<p class="post-title">Type the info you use for this account</p>
					</div>
					<div class="row">
						<input type="text" name="email" placeholder="Email"/>
						<span class="input">
							<div class="showPassDiv">
								<i class="fa fa-eye centeredItem" aria-hidden="true"></i>
							</div>
							<input type="password" name="password" placeholder="Your password..." />
						</span>
					</div>
					<div class="row text-center">
						<button class="btn" type="submit">Go!</button>
					</div>
					<div class="row text-center">
						<a>Skip this step</a>
					</div>
				</div>
			</div>
			<div class="popupBody" id="scrapingInfo">
				<div class="handler">
					<div class="row text-center">
						<p class="sub-title"><span>Google Chrome</span> is being imported</p>
					</div>
					<div class="row text-center">
						<div class="logo">
							<div class="imageHandler">
								<img src="resources/images/Chrome.png"/>
							</div>
						</div>
					</div>
					<div class="row text-center">
						<p class="post-title">How it works</p>
					</div>
					<div class="row">
						<p class="info-text">Ease integrates your accounts by finding them in a new tab. Please do not close it.</p>
					</div>
					<div class="row">
						<p class="info-text">Once it is done, you can select what you keep on Ease.</p>
					</div>
					<div class="row text-center">
						<img class="loading" src="resources/other/facebook-loading.svg">
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript" src="js/Tutorial.js" > </script>