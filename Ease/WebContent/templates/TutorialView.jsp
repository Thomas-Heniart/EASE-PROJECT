<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<link rel="stylesheet" href="/cssMinified.v00039/Tutorial.css"/>
<div id="ohehcool"></div>
<div class="popupHandler" id="tutorial">
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
                            <p class="name">Google Chrome (Currently unavailable)</p>
                            <div class="onoffswitch">
                                <!-- <input type="checkbox" name="Chrome" class="onoffswitch-checkbox" id="Chrome" checked>
                                <label class="onoffswitch-label" for="Chrome"></label> -->
                                <label class="onoffswitch-label onoffswitch-label-readonly-disable"></label>
                            </div>
                        </div>
                        <div class="account" style='display: none'>
                            <div class="logo">
                                <img src="resources/websites/LinkedIn/logo.png"/>
                            </div>
                            <p class="name">LinkedIn</p>
                            <div class="onoffswitch">
                                <input type="checkbox" name="LinkedIn" class="onoffswitch-checkbox" id="LinkedIn"
                                       websiteId="${catalog.getWebsiteWithName("LinkedIn").getDb_id()}">
                                <label class="onoffswitch-label" for="LinkedIn"></label>
                            </div>
                        </div>
                        <div class="account">
                            <div class="logo">
                                <img src="resources/websites/Facebook/logo.png"/>
                            </div>
                            <p class="name">Facebook</p>
                            <div class="onoffswitch">
                                <input type="checkbox" name="Facebook" class="onoffswitch-checkbox" id="Facebook"
                                       checked websiteId="${catalog.getWebsiteWithName("Facebook").getDb_id()}">
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
                        <input autocomplete="new-password" type="text" name="email" placeholder="Email"/>
                        <span class="input">
						<div class="showPassDiv">
							<i class="fa fa-eye centeredItem" aria-hidden="true"></i>
							<i class="fa fa-eye-slash centeredItem" aria-hidden="true"></i>
						</div>
						<input autocomplete="new-password" type="password" name="password"
                               placeholder="Your password..."/>
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
                    <div class="row text-center" style="margin-bottom: 0.5vw">
                        <i class="fa fa-chevron-up" aria-hidden="true"></i>
                    </div>
                    <div class="row text-center">
                        <p>Click on your name to see<br>your Chrome account email.</p>
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
                        <p class="info-text">Ease integrates your accounts by finding them in a new tab. Please do not
                            close it.</p>
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
                                <div class="app" id="${catalog.getWebsiteWithName('Facebook').getDb_id()}">
                                    <div class="logo">
                                        <img src="resources/websites/Facebook/logo.png">
                                    </div>
                                    <p class="name">Facebook</p>
                                </div>
                            </div>
                            <div class="appHandler">
                                <div class="app" id="${catalog.getWebsiteWithName('LinkedIn').getDb_id()}">
                                    <div class="logo">
                                        <img src="resources/websites/LinkedIn/logo.png">
                                    </div>
                                    <p class="name">LinkedIn</p>
                                </div>
                            </div>
                            <div class="appHandler">
                                <div class="app" id="${catalog.getWebsiteWithName('Gmail').getDb_id()}">
                                    <div class="logo">
                                        <img src="resources/websites/Gmail/logo.png">
                                    </div>
                                    <p class="name">Gmail</p>
                                </div>
                            </div>
                            <div class="appHandler">
                                <div class="app" id="${catalog.getWebsiteWithName('Youtube').getDb_id()}">
                                    <div class="logo">
                                        <img src="resources/websites/Youtube/logo.png">
                                    </div>
                                    <p class="name">Youtube</p>
                                </div>
                            </div>
                            <div class="appHandler">
                                <div class="app" id="${catalog.getWebsiteWithName('Google Drive').getDb_id()}">
                                    <div class="logo">
                                        <img src="resources/websites/GoogleDrive/logo.png">
                                    </div>
                                    <p class="name">Google Drive</p>
                                </div>
                            </div>
                            <div class="appHandler">
                                <div class="app" id="${catalog.getWebsiteWithName('Twitter').getDb_id()}">
                                    <div class="logo">
                                        <img src="resources/websites/Twitter/logo.png">
                                    </div>
                                    <p class="name">Twitter</p>
                                </div>
                            </div>
                            <div class="appHandler">
                                <div class="app" id="${catalog.getWebsiteWithName('Instagram').getDb_id()}">
                                    <div class="logo">
                                        <img src="resources/websites/Instagram/logo.png">
                                    </div>
                                    <p class="name">Instagram</p>
                                </div>
                            </div>
                            <div class="appHandler">
                                <div class="app" id="${catalog.getWebsiteWithName('Airbnb').getDb_id()}">
                                    <div class="logo">
                                        <img src="resources/websites/Airbnb/logo.png">
                                    </div>
                                    <p class="name">Airbnb</p>
                                </div>
                            </div>
                            <div class="appHandler">
                                <div class="app" id="${catalog.getWebsiteWithName('BlaBlaCar').getDb_id()}">
                                    <div class="logo">
                                        <img src="resources/websites/BlaBlaCar/logo.png">
                                    </div>
                                    <p class="name">BlaBlaCar</p>
                                </div>
                            </div>
                            <div class="appHandler">
                                <div class="app" id="${catalog.getWebsiteWithName('Messenger').getDb_id()}">
                                    <div class="logo">
                                        <img src="resources/websites/Messenger/logo.png">
                                    </div>
                                    <p class="name">Messenger</p>
                                </div>
                            </div>
                            <div class="appHandler">
                                <div class="app" id="${catalog.getWebsiteWithName('Suite Office').getDb_id()}">
                                    <div class="logo">
                                        <img src="resources/websites/Office365/logo.png">
                                    </div>
                                    <p class="name">Office365 Mail</p>
                                </div>
                            </div>
                            <div class="appHandler">
                                <div class="app" id="${catalog.getWebsiteWithName('Skype').getDb_id()}">
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
                                <div class="app" id="${catalog.getWebsiteWithName('Vente-privée').getDb_id()}">
                                    <div class="logo">
                                        <img src="resources/websites/VentePrivee/logo.png">
                                    </div>
                                    <p class="name">Vente Privée</p>
                                </div>
                            </div>
                            <div class="appHandler hidden">
                                <div class="app" id="${catalog.getWebsiteWithName('Amazon').getDb_id()}">
                                    <div class="logo">
                                        <img src="resources/websites/Amazon/logo.png">
                                    </div>
                                    <p class="name">Amazon</p>
                                </div>
                            </div>
                            <div class="appHandler hidden">
                                <div class="app" id="${catalog.getWebsiteWithName('Dropbox').getDb_id()}">
                                    <div class="logo">
                                        <img src="resources/websites/Dropbox/logo.png">
                                    </div>
                                    <p class="name">Dropbox</p>
                                </div>
                            </div>
                            <div class="appHandler hidden">
                                <div class="app" id="${catalog.getWebsiteWithName('Deliveroo').getDb_id()}">
                                    <div class="logo">
                                        <img src="resources/websites/Deliveroo/logo.png">
                                    </div>
                                    <p class="name">Deliveroo</p>
                                </div>
                            </div>
                            <div class="appHandler hidden">
                                <div class="app" id="${catalog.getWebsiteWithName('Leboncoin').getDb_id()}">
                                    <div class="logo">
                                        <img src="resources/websites/LeBonCoin/logo.png">
                                    </div>
                                    <p class="name">Leboncoin</p>
                                </div>
                            </div>
                            <div class="appHandler hidden">
                                <div class="app" id="${catalog.getWebsiteWithName('Voyages SNCF').getDb_id()}">
                                    <div class="logo">
                                        <img src="resources/websites/VoyageSNCF/logo.png">
                                    </div>
                                    <p class="name">Voyages SNCF</p>
                                </div>
                            </div>
                            <div class="appHandler hidden">
                                <div class="app" id="${catalog.getWebsiteWithName('Yahoo Mail').getDb_id()}">
                                    <div class="logo">
                                        <img src="resources/websites/YahooMail/logo.png">
                                    </div>
                                    <p class="name">Yahoo Mail</p>
                                </div>
                            </div>
                            <div class="appHandler hidden">
                                <div class="app" id="${catalog.getWebsiteWithName('PayPal').getDb_id()}">
                                    <div class="logo">
                                        <img src="resources/websites/Paypal/logo.png">
                                    </div>
                                    <p class="name">PayPal</p>
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
                            <input id="name" type="text" name="name" placeholder="Name"/>
                            <input id="profileId" type="hidden" name="profileId"
                                   value="${dashboard.getProfilesList().get(0).getDBid() }"/>
                        </div>
                        <div class="row text-center lineBehind">
                            <p class="post-title">How do you access your <span>Facebook</span> account ?</p>
                        </div>
                        <div class="row">
                            <input autocomplete="new-password" type="text" name="login" id="login" placeholder="Login"/>
                            <span class="input">
							<div class="showPassDiv">
								<i class="fa fa-eye centeredItem" aria-hidden="true"></i>
								<i class="fa fa-eye-slash centeredItem" aria-hidden="true"></i>
							</div>
							<input autocomplete="new-password" id="password" type="password" name="password"
                                   placeholder="Your password..."/>
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