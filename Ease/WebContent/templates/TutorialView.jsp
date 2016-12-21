<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.Ease.Context.Catalog.Catalog"%>


<link rel="stylesheet" href="css/Tutorial.css" />


<div class="popupHandler myshow" id="tutorial">
	<div class="easePopup show" id="importation">
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
								<input type="checkbox" name="Linkedin" class="onoffswitch-checkbox" id="Linkedin" websiteId="${catalog.getWebsiteWithName("Linkedin").getSingleId()}">
								<label class="onoffswitch-label" for="Linkedin"></label>
							</div>
						</div>
						<div class="account">
							<div class="logo">
								<img src="resources/websites/Facebook/logo.png"/>
							</div>
							<p class="name">Facebook</p>
							<div class="onoffswitch">
								<input type="checkbox" name="Facebook" class="onoffswitch-checkbox" id="Facebook" websiteId="${catalog.getWebsiteWithName("Facebook").getSingleId()}">
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
					<div class="row text-center errorText">
						<p>The email or password is incorrect. Please try again.</p>
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


	<div class="easePopup" id="saving">
		<div class="title">
			<p>What do you want to keep ?</p>
			<p class="sub-title">Anything you are not keeping, we are not keeping.</p>
		</div>
		<div class="bodysHandler">
			<div class="popupBody show" id="selectScraping">
				<div class="handler">
					<div class="row">
						<div class="scrapedAppsContainer">

						</div>
					</div>
					<div id="scrapping_done_submit" class="row text-center">
						<button class="btn" type="submit">I am done!</button>
					</div>
					<div id="add_app_progress hide">
  						<div id="progress_bar"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript" src="js/tutorialSteps.js" > </script>