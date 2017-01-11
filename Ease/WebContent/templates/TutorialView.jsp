<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.Ease.Context.Catalog.Catalog"%>

<link rel="stylesheet" href="css/Tutorial.css" />
<div id="ohehcool"></div>
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
						
						<c:forEach items='${user.getInfras()}' var="infra">
						<div class="account">
							<div class="logo">
								<img src="${infra.getLogoPath()}"/>
							</div>
							<p class="name">${infra.getName()}</p>
							<div class="onoffswitch">
								<label class="onoffswitch-label onoffswitch-label-readonly"></label>
							</div>
						</div>	
					</c:forEach>

					<div class="account">
						<div class="logo">
							<img src="resources/images/Chrome.png"/>
						</div>
						<p class="name">Google Chrome</p>
						<div class="onoffswitch">
							<input type="checkbox" name="Chrome" class="onoffswitch-checkbox" id="Chrome" checked>
							<label class="onoffswitch-label" for="Chrome"></label>
						</div>
					</div>
					<div class="account">
						<div class="logo">
							<img src="resources/websites/Linkedin/logo.png"/>
						</div>
						<p class="name">LinkedIn</p>
						<div class="onoffswitch">
							<input type="checkbox" name="Linkedin" class="onoffswitch-checkbox" id="Linkedin" checked websiteId="${catalog.getWebsiteWithName("Linkedin").getSingleId()}">
							<label class="onoffswitch-label" for="Linkedin"></label>
						</div>
					</div>
					<div class="account">
						<div class="logo">
							<img src="resources/websites/Facebook/logo.png"/>
						</div>
						<p class="name">Facebook</p>
						<div class="onoffswitch">
							<input type="checkbox" name="Facebook" class="onoffswitch-checkbox" id="Facebook" checked websiteId="${catalog.getWebsiteWithName("Facebook").getSingleId()}">
							<label class="onoffswitch-label" for="Facebook"></label>
						</div>
					</div>
				</div>
				<div class="row text-center">
					<button class="btn" type="submit">Go!</button>
				</div>
				<div class="row text-center">
					<a id="manualImportation">I prefere to import my accounts 1 by 1</a>
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
					<button class="btn locked" type="submit">Go!</button>
				</div>
				<div class="row text-center">
					<a>Skip this step</a>
				</div>
			</div>
			<div id="chromeUserEmailHelper">
				<div class="row text-center">
					Click on your name to see your Chrome account email.
				</div>
				<div class="row text-center">
					<img src="/resources/icons/chromeEmailHelper.png"/>
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
				<div class="row" style="margin-bottom:1.5vw;">
					<div class="scrapedAppsContainer">

					</div>
				</div>
				<div class="row text-center">
					<div id="scrapping_done_submit">
						<button class="btn" type="submit">I am done!</button>
					</div>
					<div class="row text-center">
						<img class="loading hide" id="add_app_progress" src="resources/other/facebook-loading.svg">
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="easePopup" id="simpleImportation">
	<div class="title">
		<p>Start with apps you use frequently</p>
		<p class="sub-title">Click on at least 4 icons</p>
	</div>
	<div class="bodysHandler">
		<div class="popupBody show">
			<div class="handler">
				<div class="row">
					<div class="appsContainer">
						<div class="appHandler">
							<div class="app" id="${catalog.getWebsiteWithName('Facebook').getSingleId()}">
								<div class="logo">
									<img src="resources/websites/Facebook/logo.png">
								</div>
								<p class="name">Facebook</p>
							</div>
						</div>
						<div class="appHandler">
							<div class="app" id="${catalog.getWebsiteWithName('Linkedin').getSingleId()}">
								<div class="logo">
									<img src="resources/websites/Linkedin/logo.png">
								</div>
								<p class="name">Linkedin</p>
							</div>
						</div>
						<div class="appHandler">
							<div class="app"  id="${catalog.getWebsiteWithName('Gmail').getSingleId()}">
								<div class="logo">
									<img src="resources/websites/Gmail/logo.png">
								</div>
								<p class="name">Gmail</p>
							</div>
						</div>
						<div class="appHandler">
							<div class="app" id="${catalog.getWebsiteWithName('Youtube').getSingleId()}">
								<div class="logo">
									<img src="resources/websites/Youtube/logo.png">
								</div>
								<p class="name">Youtube</p>
							</div>
						</div>
						<div class="appHandler">
							<div class="app"  id="${catalog.getWebsiteWithName('Google Drive').getSingleId()}">
								<div class="logo">
									<img src="resources/websites/GoogleDrive/logo.png">
								</div>
								<p class="name">Google Drive</p>
							</div>
						</div>
						<div class="appHandler">
							<div class="app"  id="${catalog.getWebsiteWithName('Twitter').getSingleId()}">
								<div class="logo">
									<img src="resources/websites/Twitter/logo.png">
								</div>
								<p class="name">Twitter</p>
							</div>
						</div>
						<div class="appHandler">
							<div class="app" id="${catalog.getWebsiteWithName('Instagram').getSingleId()}">
								<div class="logo">
									<img src="resources/websites/Instagram/logo.png">
								</div>
								<p class="name">Instagram</p>
							</div>
						</div>
						<div class="appHandler">
							<div class="app" id="${catalog.getWebsiteWithName('Airbnb').getSingleId()}">
								<div class="logo">
									<img src="resources/websites/Airbnb/logo.png">
								</div>
								<p class="name">Airbnb</p>
							</div>
						</div>
						<div class="appHandler">
							<div class="app" id="${catalog.getWebsiteWithName('BlaBlaCar').getSingleId()}">
								<div class="logo">
									<img src="resources/websites/BlaBlaCar/logo.png">
								</div>
								<p class="name">BlaBlaCar</p>
							</div>
						</div>
						<div class="appHandler">
							<div class="app" id="${catalog.getWebsiteWithName('Messenger').getSingleId()}">
								<div class="logo">
									<img src="resources/websites/Messenger/logo.png">
								</div>
								<p class="name">Messenger</p>
							</div>
						</div>
						<div class="appHandler">
							<div class="app"  id="${catalog.getWebsiteWithName('Suite Office').getSingleId()}">
								<div class="logo">
									<img src="resources/websites/Office365/logo.png">
								</div>
								<p class="name">Office365 Mail</p>
							</div>
						</div>
						<div class="appHandler">
							<div class="app" id="${catalog.getWebsiteWithName('Skype').getSingleId()}">
								<div class="logo">
									<img src="resources/websites/Skype/logo.png">
								</div>
								<p class="name">Skype</p>
							</div>
						</div>
						<div class="showMoreHelper">
							<p class="showMoreButton">
								Show few more
							</p>
						</div>
						<div class="appHandler hidden">
							<div class="app" id="${catalog.getWebsiteWithName('Vente Privee').getSingleId()}">
								<div class="logo">
									<img src="resources/websites/VentePrivee/logo.png">
								</div>
								<p class="name">Vente Privée</p>
							</div>
						</div>
						<div class="appHandler hidden">
							<div class="app" id="${catalog.getWebsiteWithName('Amazon').getSingleId()}">
								<div class="logo">
									<img src="resources/websites/Amazon/logo.png">
								</div>
								<p class="name">Amazon</p>
							</div>
						</div>
						<div class="appHandler hidden">
							<div class="app" id="${catalog.getWebsiteWithName('Dropbox').getSingleId()}">
								<div class="logo">
									<img src="resources/websites/Dropbox/logo.png">
								</div>
								<p class="name">Dropbox</p>
							</div>
						</div>
						<div class="appHandler hidden">
							<div class="app" id="${catalog.getWebsiteWithName('Deliveroo').getSingleId()}">
								<div class="logo">
									<img src="resources/websites/Deliveroo/logo.png">
								</div>
								<p class="name">Deliveroo</p>
							</div>
						</div>
						<div class="appHandler hidden">
							<div class="app" id="${catalog.getWebsiteWithName('LeBonCoin').getSingleId()}">
								<div class="logo">
									<img src="resources/websites/LeBonCoin/logo.png">
								</div>
								<p class="name">LeBonCoin</p>
							</div>
						</div>
						<div class="appHandler hidden">
							<div class="app" id="${catalog.getWebsiteWithName('Voyages SNCF').getSingleId()}">
								<div class="logo">
									<img src="resources/websites/VoyageSNCF/logo.png">
								</div>
								<p class="name">Voyages SNCF</p>
							</div>
						</div>
						<div class="appHandler hidden">
							<div class="app" id="${catalog.getWebsiteWithName('YahooMail').getSingleId()}">
								<div class="logo">
									<img src="resources/websites/YahooMail/logo.png">
								</div>
								<p class="name">YahooMail</p>
							</div>
						</div>
						<div class="appHandler hidden">
							<div class="app" id="${catalog.getWebsiteWithName('Paypal').getSingleId()}">
								<div class="logo">
									<img src="resources/websites/Paypal/logo.png">
								</div>
								<p class="name">Paypal</p>
							</div>
						</div>
					</div>
				</div>
				<div class="row text-center">
					<button class="btn locked" type="submit">I am done!</button>
				</div>
				<div class="row text-center">
					<a>If you have more than one account on a website, you’ll be able to add it later.</a>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="easePopup" id="addAppTutorial">
	<div class="title">
		<p>Type your password for<br>the last time, ever.</p>
	</div>		
	<div class="bodysHandler">
		<div class="popupBody show">
			<div class="handler">
				<div class="row text-center">
					<div class="logo">
						<img src="resources/websites/Facebook/logo.png"/>
					</div>
				</div>
				<form action="addClassicApp" method="POST">
					<div class="row text-center">
						<input id="name" type="text" name="name" placeholder="Name" />
						<input id="profileId" type="hidden" name="profileId" value="${user.getProfilesList().get(0).getSingleId() }"/>
					</div>
					<div class="row text-center lineBehind">
						<p class="post-title">How do you access your <span>Facebook</span> account ?</p>
					</div>
					<div class="row">
						<input type="text" name="login" id="login" placeholder="Login" />
						<span class="input">
							<div class="showPassDiv">
								<i class="fa fa-eye centeredItem" aria-hidden="true"></i>
							</div>
							<input id="password" type="password" name="password" placeholder="Your password..." />
						</span>
					</div>
					<div class="row text-center">
						<button class="btn" type="submit">Add this app</button>
					</div>
				</form>
				<div class="row text-center">
					<a id="skipButton">Skip</a>
				</div>
			</div>
		</div>
	</div>
</div>
</div>

<script type="text/javascript" src="js/tutorialSteps.js" > </script>