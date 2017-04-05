var UpdateManager=function(t){var e=this;this.qRoot=t,this.updatesHandler=this.qRoot.find(".contentList"),this.loading=this.qRoot.find(".updateLoadingHelper"),this.titleHandler=this.qRoot.find(".catalogHeader p"),this.updates=[],this.isShown=!1,this.showLoading=function(){e.updatesHandler.addClass("hide"),e.loading.addClass("show")},this.hideLoading=function(){e.updatesHandler.removeClass("hide"),e.loading.removeClass("show")},this.show=function(){e.qRoot.addClass("show"),e.isShown=!0},this.hide=function(){e.qRoot.removeClass("show"),e.isShown=!1},this.hideWithAnimation=function(){e.qRoot.height(e.qRoot.height()),e.qRoot.css({transition:"height .3s, opacity .3s",height:e.qRoot.height(),opacity:"1"}),e.qRoot.css({height:"0",opacity:"0"}),e.qRoot.one("transitionend",function(){e.hide(),e.qRoot.attr("style",""),catalog.onResize()})},this.updateTitle=function(){e.titleHandler.text(e.updates.length+" Update"+(1==e.updates.length?"":"s")+" available")},this.checkUpdates=function(){e.showLoading(),postHandler.post("GetUpdates",{},function(){},function(t){var i,o=JSON.parse(t);if(e.updates.length!=o.length){for(var n=0;n<e.updates.length;n++)e.removeUpdate(e.updates[n]);e.updates=[];for(var n=0;n<o.length;n++)i=o[n],"newClassicApp"==o[n].type?e.addUpdate(new newClassicApp(i.singleId,i.login,i.passwordLength,i.websiteImg,i.websiteName,i.email,i.websiteId)):"newPassword"==o[n].type?e.addUpdate(new updatePassword(i.singleId,i.appId,i.login,i.passwordLength,i.websiteImg,i.websiteName,i.email,i.websiteId)):"newLogWithApp"==o[n].type&&e.addUpdate(new newLogWithApp(i.singleId,i.websiteName,i.logWithId,i.login,i.logWithImg,i.logWithName,i.websiteImg,i.websiteId))}e.updateTitle(),easeUpdateCount.setCount(e.updates.length),e.hideLoading(),e.updates.length||e.hideWithAnimation()},function(){e.hideLoading()},"text")},this.onCatalogOpen=function(){0!=easeUpdateCount.count&&(e.show(),e.checkUpdates())},this.addUpdate=function(t){var i=t.websiteName,o=t.type;easeTracker.trackEvent("UpdateNew",{UpdateType:o,appName:i}),easeTracker.increaseUpdateCount(),e.updates.push(t),e.updatesHandler.append(t.qRoot)},this.removeUpdate=function(t){t.removeWithAnimation();for(var i=e.updates.indexOf(t)+1;i<e.updates.length;i++)e.updates[i].slideLeftAnimation();e.updates.splice(e.updates.indexOf(t),1),e.updateTitle(),easeUpdateCount.setCount(e.updates.length),e.updates.length||e.hideWithAnimation()}},updatePassword=function(t,e,i,o,n,s,a,d){var r=this;for(this.qRoot=$('<div class="updateBox"><div class="logo"><img src="" /></div><div class="infos">		<div class="errorMessage">		 <p></p>		</div>	<div class="handler"><div class="title"><p> New Password</p></div>	<div class="credentials">		<div class="credentialsHandler">			<p class="login"></p>			<p class="password"></p>		</div>	</div>	<div class="buttonsHandler">	<button class="acceptButton btn">Accept</button>	<button class="rejectButton btn">Reject</button>	</div></div></div></div>'),this.type="updatePassword",this.updateId=t,this.websiteName=s,this.appId=e,this.login=i,this.imageSrc=n,this.isVerified=a,this.catalogId=d,this.pwdString="",this.imageHandler=this.qRoot.find(".logo"),this.errorMessageHandler=this.qRoot.find(".errorMessage"),this.infosHandler=this.qRoot.find(".infos .handler"),this.buttonsHandler=this.qRoot.find(".buttonsHandler"),this.acceptButton=this.qRoot.find(".acceptButton"),this.rejectButton=this.qRoot.find(".rejectButton");o-->0;)this.pwdString+="•";this.qRoot.find(".login").text(this.login),this.qRoot.find(".password").text(this.pwdString),this.qRoot.find(".logo img").attr("src",this.imageSrc),this.showErrorMessage=function(t){r.errorMessageHandler.find("p").text(t),r.errorMessageHandler.addClass("show"),r.infosHandler.addClass("hide"),setTimeout(function(){r.errorMessageHandler.find("p").text(""),r.errorMessageHandler.removeClass("show"),r.infosHandler.removeClass("hide")},2500)},this.removeWithAnimation=function(){r.qRoot.addClass("deletingUpdate"),r.qRoot.one("webkitAnimationEnd oanimationend msAnimationEnd animationend",function(){r.remove()})},this.slideLeftAnimation=function(){r.qRoot.addClass("decaleUpdate"),r.qRoot.one("webkitAnimationEnd oanimationend msAnimationEnd animationend",function(){r.qRoot.removeClass("decaleUpdate")})},this.remove=function(){r.qRoot.remove()},this.startLogoAnimation=function(){r.imageHandler.addClass("infiniteScaleAnimation"),r.buttonsHandler.css("pointer-events","none")},this.stopLogoAnimation=function(){r.imageHandler.removeClass("infiniteScaleAnimation"),r.buttonsHandler.css("pointer-events","")},this.acceptButton.click(function(){"verified"==r.isVerified?(r.startLogoAnimation(),postHandler.post("AcceptUpdate",{profileId:profiles[profiles.length-1].id,updateId:r.updateId},function(){r.stopLogoAnimation()},function(){catalog.oUpdate.removeUpdate(r),easeTracker.trackEvent("UpdateAccepted"),easeTracker.increaseUpdateAcceptedCount()},function(t){r.showErrorMessage(t)},"text")):easeAddUpdatePopup.open(r,profiles[profiles.length-1].id)}),this.rejectButton.click(function(){r.startLogoAnimation(),postHandler.post("RejectUpdate",{updateId:r.updateId},function(){r.stopLogoAnimation()},function(){easeTracker.trackEvent("UpdateRejected"),easeTracker.increaseUpdateRejectedCount(),catalog.oUpdate.removeUpdate(r)},function(t){r.showErrorMessage(t)},"text")})},newClassicApp=function(t,e,i,o,n,s,a){var d=this;for(this.qRoot=$('<div class="updateBox">	<div class="logo">	<img src="" />	</div>	<div class="infos">		<div class="errorMessage">		 <p></p>		</div>	<div class="handler">		<div class="title">			<p> New Account</p>		</div>		<div class="credentials">			<div class="credentialsHandler">				<p class="login"></p>				<p class="password"></p>			</div>		</div>		<div class="buttonsHandler">		<button class="acceptButton btn">Accept</button>			<button class="rejectButton btn">Reject</button>			</div>		</div>		</div>	</div>'),this.type="newClassicApp",this.updateId=t,this.websiteName=n,this.login=e,this.imageSrc=o,this.catalogId=a,this.isVerified=s,this.pwdString="",this.imageHandler=this.qRoot.find(".logo"),this.errorMessageHandler=this.qRoot.find(".errorMessage"),this.infosHandler=this.qRoot.find(".infos .handler"),this.buttonsHandler=this.qRoot.find(".buttonsHandler"),this.acceptButton=this.qRoot.find(".acceptButton"),this.rejectButton=this.qRoot.find(".rejectButton");i-->0;)this.pwdString+="•";this.qRoot.find(".login").text(this.login),this.qRoot.find(".password").text(this.pwdString),this.qRoot.find(".logo img").attr("src",this.imageSrc),this.showErrorMessage=function(t){d.errorMessageHandler.find("p").text(t),d.errorMessageHandler.addClass("show"),d.infosHandler.addClass("hide"),setTimeout(function(){d.errorMessageHandler.find("p").text(""),d.errorMessageHandler.removeClass("show"),d.infosHandler.removeClass("hide")},2500)},this.removeWithAnimation=function(){d.qRoot.addClass("deletingUpdate"),d.qRoot.one("webkitAnimationEnd oanimationend msAnimationEnd animationend",function(){d.remove()})},this.slideLeftAnimation=function(){d.qRoot.addClass("decaleUpdate"),d.qRoot.one("webkitAnimationEnd oanimationend msAnimationEnd animationend",function(){d.qRoot.removeClass("decaleUpdate")})},this.remove=function(){d.qRoot.remove()},this.startLogoAnimation=function(){d.imageHandler.addClass("infiniteScaleAnimation"),d.buttonsHandler.css("pointer-events","none")},this.stopLogoAnimation=function(){d.imageHandler.removeClass("infiniteScaleAnimation"),d.buttonsHandler.css("pointer-events","")},this.acceptButton.click(function(){"verified"==d.isVerified?(d.startLogoAnimation(),postHandler.post("AcceptUpdate",{profileId:profiles[profiles.length-1].id,updateId:d.updateId},function(){d.stopLogoAnimation()},function(t){var e=new MyApp;e.init(!1,d.login,d.catalogId,d.websiteName,t,0,!0,d.imageSrc),profiles[profiles.length-1].addApp(e),e.scaleAnimate(),catalog.oUpdate.removeUpdate(d),easeTracker.trackEvent("UpdateAccepted"),easeTracker.increaseUpdateAcceptedCount()},function(t){d.showErrorMessage(t)},"text")):easeAddUpdatePopup.open(d,profiles[profiles.length-1].id,-1)}),this.rejectButton.click(function(){d.startLogoAnimation(),postHandler.post("RejectUpdate",{updateId:d.updateId},function(){d.stopLogoAnimation()},function(){easeTracker.trackEvent("UpdateRejected"),easeTracker.increaseUpdateRejectedCount(),catalog.oUpdate.removeUpdate(d)},function(t){d.showErrorMessage(t)},"text")})},newLogWithApp=function(t,e,i,o,n,s,a,d){var r=this;this.qRoot=$('<div class="updateBox">	<div class="logo">		<img src="" />	</div>		<div class="infos">		<div class="errorMessage">		 <p></p>		</div>	<div class="handler">			<div class="title">				<p> New Account</p>			</div>			<div class="credentials">				<div class="logWithLogo">					<img src="" />				</div>				<div class="credentialsHandler">					<p class="login">Sign-in with <span></span></p>					<p class="password">(<span></span>)</p>				</div>			</div>			<div class="buttonsHandler">			<button class="acceptButton btn">Accept</button>			<button class="rejectButton btn">Reject</button>		</div>			</div>		</div>	</div>'),this.type="newLogWith",this.updateId=t,this.websiteName=e,this.logWithLogin=o,this.logWithImageSrc=n,this.logWithAppName=s,this.websiteImageSrc=a,this.catalogId=d,this.logWithId=i,this.imageHandler=this.qRoot.find(".logo"),this.errorMessageHandler=this.qRoot.find(".errorMessage"),this.infosHandler=this.qRoot.find(".infos .handler"),this.buttonsHandler=this.qRoot.find(".buttonsHandler"),this.acceptButton=this.qRoot.find(".acceptButton"),this.rejectButton=this.qRoot.find(".rejectButton"),this.qRoot.find(".logo img").attr("src",this.websiteImageSrc),this.qRoot.find(".logWithLogo img").attr("src",this.logWithImageSrc),this.qRoot.find(".login span").text(this.logWithAppName),this.qRoot.find(".password span").text(this.logWithLogin),this.showErrorMessage=function(t){r.errorMessageHandler.find("p").text(t),r.errorMessageHandler.addClass("show"),r.infosHandler.addClass("hide"),setTimeout(function(){r.errorMessageHandler.find("p").text(""),r.errorMessageHandler.removeClass("show"),r.infosHandler.removeClass("hide")},2500)},this.removeWithAnimation=function(){r.qRoot.addClass("deletingUpdate"),r.qRoot.one("webkitAnimationEnd oanimationend msAnimationEnd animationend",function(){r.remove()})},this.slideLeftAnimation=function(){r.qRoot.addClass("decaleUpdate"),r.qRoot.one("webkitAnimationEnd oanimationend msAnimationEnd animationend",function(){r.qRoot.removeClass("decaleUpdate")})},this.remove=function(){r.qRoot.remove()},this.startLogoAnimation=function(){r.imageHandler.addClass("infiniteScaleAnimation"),r.buttonsHandler.css("pointer-events","none")},this.stopLogoAnimation=function(){r.imageHandler.removeClass("infiniteScaleAnimation"),r.buttonsHandler.css("pointer-events","")},this.acceptButton.click(function(){r.startLogoAnimation(),postHandler.post("AcceptUpdate",{profileId:profiles[profiles.length-1].id,updateId:r.updateId},function(){r.stopLogoAnimation()},function(t){var e=new MyApp;e.init(r.logWithId,null,r.catalogId,r.websiteName,t,0,!0,r.websiteImageSrc),profiles[profiles.length-1].addApp(e),e.scaleAnimate(),catalog.oUpdate.removeUpdate(r),easeTracker.trackEvent("UpdateAccepted"),easeTracker.increaseUpdateAcceptedCount()},function(t){r.showErrorMessage(t)},"text")}),this.rejectButton.click(function(){r.startLogoAnimation(),postHandler.post("RejectUpdate",{updateId:r.updateId},function(){r.stopLogoAnimation()},function(){easeTracker.trackEvent("UpdateRejected"),easeTracker.increaseUpdateRejectedCount(),catalog.oUpdate.removeUpdate(r)},function(t){r.showErrorMessage(t)},"text")})},easeAddUpdatePopup,addUpdatePopup=function(t){var e=this;this.qRoot=$(t),this.parentHandler=this.qRoot.closest("#easePopupsHandler"),this.logoImgHandler=this.qRoot.find(".logo img"),this.appNameHandler=this.qRoot.find(".appName"),this.emailSendingArea=this.qRoot.find(".emailSendingDiv"),this.sendEmailButtonHandler=this.qRoot.find("button.emailSend"),this.sentEmailOkButton=this.qRoot.find("#informEmailSent button.btn"),this.ajaxFormHandler=this.qRoot.find("form"),this.loginInputHandler=this.qRoot.find("input[name='login']"),this.passwordInputHandler=this.qRoot.find("input[name='password']"),this.errorMessageHandler=this.qRoot.find(".errorHandler"),this.submitButtonHandler=this.ajaxFormHandler.find("button[type='submit']"),this.goBackButtonHandler=this.qRoot.find("#goBack"),this.mainBody=this.qRoot.find("#addUpdate"),this.sentEmailBody=this.qRoot.find("#informEmailSent"),this.profileId=-1,this.appIdx=-1,this.update=null,this.showEmailSendingArea=function(){e.emailSendingArea.addClass("show")},this.hideEmailSendingArea=function(){e.emailSendingArea.removeClass("show")},this.reset=function(){e.loginInputHandler.val(""),e.passwordInputHandler.val(""),e.profileId=-1,e.appIdx=-1,e.updateId=-1,e.submitButtonHandler.addClass("locked"),e.sentEmailBody.removeClass("show"),e.mainBody.addClass("show")},this.open=function(t,i,o){currentEasePopup=e,e.reset(),e.update=t,e.appIdx=o,e.profileId=i,e.loginInputHandler.val(t.login),e.appNameHandler.text(t.websiteName),e.logoImgHandler.attr("src",t.imageSrc),"unverified"==t.isVerified&&(e.showEmailSendingArea(),e.sendEmailButtonHandler.one("click",function(){e.sendEmailButtonHandler.addClass("loading"),postHandler.post("AskVerificationEmail",{email:e.update.login},function(){e.sendEmailButtonHandler.removeClass("loading")},function(){e.showSentEmailFeedbackArea()},function(){},"text")})),e.parentHandler.addClass("myshow"),e.qRoot.addClass("show")},this.close=function(){e.qRoot.removeClass("show"),e.parentHandler.removeClass("myshow"),e.errorMessageHandler.removeClass("show"),e.sendEmailButtonHandler.off(),e.hideEmailSendingArea()},this.showSentEmailFeedbackArea=function(){e.mainBody.removeClass("show"),e.sentEmailBody.addClass("show")},this.sentEmailOkButton.click(function(){e.close()}),this.passwordInputHandler.on("input",function(){$(this).val().length>0&&e.submitButtonHandler.removeClass("locked")||e.submitButtonHandler.addClass("locked")}),this.goBackButtonHandler.click(function(){e.close()}),this.ajaxFormHandler.submit(function(t){t.preventDefault(),e.errorMessageHandler.removeClass("show"),e.submitButtonHandler.addClass("loading"),postHandler.post("AcceptUpdate",{profileId:e.profileId,updateId:e.update.updateId,password:e.passwordInputHandler.val()},function(){e.submitButtonHandler.removeClass("loading")},function(t){if("newClassicApp"==e.update.type){var i=new MyApp;i.init(!1,e.update.login,e.update.catalogId,e.update.websiteName,t,0,!0,e.update.imageSrc),profiles[profiles.length-1].addApp(i),i.scaleAnimate()}catalog.oUpdate.removeUpdate(e.update),e.close()},function(t){e.errorMessageHandler.find("p").text(t),e.errorMessageHandler.addClass("show")},"text")})};$(document).ready(function(){easeAddUpdatePopup=new addUpdatePopup($("#addUpdatePopup"))});var updateCount=function(t){var e=this;this.qRoot=$(t),this.count=parseInt(this.qRoot.text(),10),e.count&&e.qRoot.css("display","block")||e.qRoot.css("display","none"),this.setCount=function(t){e.qRoot.html(t),e.count=t,e.count&&e.qRoot.css("display","block")||e.qRoot.css("display","none")}},easeUpdateCount;$(document).ready(function(){});