function showAddAppTuto(e){$("div#addAppTutorial").addClass("show"),$("div#addAppTutorial img").attr("src",$("div#simpleImportation div.appHandler div.app.selected:eq("+e+")").find("img").attr("src")),$("div#addAppTutorial input#name").val($("div#simpleImportation div.appHandler div.app.selected:eq("+e+")").find("p.name").text()),$("div#addAppTutorial p.post-title span").text($("div#simpleImportation div.appHandler div.app.selected:eq("+e+")").find("p.name").text()),$("div#addAppTutorial input#login").val(""),$("div#addAppTutorial input#login").focus(),$("div#addAppTutorial input#password").val("")}function goToNextStep(){addAppTutoCpt++,$("div#simpleImportation div.appHandler div.app.selected").length>addAppTutoCpt?showAddAppTuto(addAppTutoCpt):postHandler.post("TutoStep",{tutoStep:"apps_manually_added"},function(){},function(){easeTracker.trackEvent("TutoManoDone"),easeTracker.setUserProperty("TutoManoSelected",addAppTutoCpt),easeTracker.setUserProperty("TutoManoFilled",appsSelectedFilled),easeTracker.setUserProperty("TutoManoSkipped",appsSelectedSkiped),easeTracker.increaseAppCounter(addAppTutoCpt),location.reload()},function(){},"text")}function displayTutoApps(e,t){e.forEach(function(e){$("div#saving div.scrapedAppsContainer").append("<div class='appHandler'><div class='app selected' index="+appToAdd.length+"><div class='logo'><img src='"+e.img+"'/><img class='by' src='"+t.img+"'></div><p class='name'>"+e.name+"</p><p class='login'>"+(e.login?e.login:"")+"</p></div></div>"),t.profileId=e.profileId,appToAdd.push(e),-1!==t.img.indexOf("Chrome")?(chromeScrappingCount+=1,chromeSelectedCount+=1):-1!==t.img.indexOf("Facebook")?(facebookScrappingCount+=1,facebookSelectedCount+=1):(linkedInScrappingCount+=1,linkedInSelectedCount+=1)})}function showSavingPopup(e){scrappingFinished.forEach(function(t){t.websiteId&&appToAdd.push(t),displayTutoApps(e[t.id],t)}),$("div#saving div.scrapedAppsContainer div.appHandler").click(function(){var e=$(this).find("div.app"),t=e.find("img.by").attr("src");e.hasClass("selected")?(e.removeClass("selected"),-1!==t.indexOf("Chrome")?chromeSelectedCount-=1:-1!==t.indexOf("Facebook")?facebookSelectedCount-=1:linkedInSelectedCount-=1):(e.addClass("selected"),-1!==t.indexOf("Chrome")?chromeSelectedCount+=1:-1!==t.indexOf("Facebook")?facebookSelectedCount+=1:linkedInSelectedCount+=1)}),0==$("div#saving div.scrapedAppsContainer div.appHandler").length?sendTutoAddApp():($("#accountCredentials").removeClass("show"),$("div#importation").removeClass("show"),$("div#saving").addClass("show"))}function sendTutoAddApp(){$("#scrapping_done_submit").addClass("hide"),$("#add_app_progress").removeClass("hide");for(var e=[],t=0;t<appToAdd.length;++t)(0==$("div#saving div.scrapedAppsContainer div[index='"+t+"']").length||$("div#saving div.scrapedAppsContainer div[index='"+t+"']").hasClass("selected"))&&e.push(appToAdd[t]);postHandler.post("TutoAddApps",{scrapjson:JSON.stringify(e)},function(){},function(){postHandler.post("TutoStep",{tutoStep:"chrome_scrapping"},function(){},function(){easeTracker.trackEvent("TutoScrappingDone"),chromeScrappingCount>0&&chromeSelectedCount>=0&&(easeTracker.trackEvent("TutoScrapChrome"),easeTracker.setUserProperty("TutoScrapChromeCount",chromeScrappingCount),easeTracker.setUserProperty("TutoScrapChromeSelected",chromeSelectedCount)),facebookScrappingCount>0&&facebookSelectedCount>=0&&(easeTracker.trackEvent("TutoScrapFacebook"),easeTracker.setUserProperty("TutoScrapFacebookCount",facebookScrappingCount),easeTracker.setUserProperty("TutoScrapFacebookSelected",facebookSelectedCount)),linkedInScrappingCount>0&&linkedInSelectedCount>=0&&(easeTracker.trackEvent("TutoScrapLinkedIn"),easeTracker.setUserProperty("TutoScrapLinkedInCount",linkedInScrappingCount),easeTracker.setUserProperty("TutoLinkedInSelected",linkedInSelectedCount)),easeTracker.increaseAppCounter(chromeSelectedCount+facebookSelectedCount+linkedInSelectedCount),easeTracker.setUserProperty("TutoTotalAppsScrapped",chromeScrappingCount+facebookScrappingCount+linkedInScrappingCount),easeTracker.setUserProperty("TutoTotalAppsSelected",chromeSelectedCount+facebookSelectedCount+linkedInSelectedCount),location.reload()},function(){},"text")},function(){},"text")}function showAccountCredentials(e){$("#accountCredentials").addClass("show"),$("#accountCredentials p span").text(scrapping[0].name),$("#accountCredentials div.logo img").attr("src",scrapping[0].img),""==e?("Google Chrome"==scrapping[0].name&&window.chrome&&window.chrome.webstore?($("#accountCredentials #chromeUserEmailHelper").addClass("show"),event=new CustomEvent("GetChromeUser",{}),document.dispatchEvent(event),$(document).on("ChromeUserEmail",function(e){$("#accountCredentials input[name='email']").val(e.detail),$("#accountCredentials input[name='password']").focus()})):($("#accountCredentials input[name='email']").val(""),$("#accountCredentials input[name='email']").focus()),$("#accountCredentials div.errorText").removeClass("show")):($("#accountCredentials div.errorText p").text(e),$("#accountCredentials input[name='password']").focus(),$("#accountCredentials div.errorText").addClass("show")),$("#accountCredentials input[name='password']").val(""),$("#accountCredentials input[name='password']").change()}function showScrapingInfo(){$("#scrapingInfo").addClass("show"),$("#scrapingInfo p span").text(scrapping[0].name),$("#scrapingInfo div.logo img").attr("src",scrapping[0].img);var e="Scrap"+scrapping[0].id;event=new CustomEvent(e,{detail:{login:scrapping[0].login,password:scrapping[0].password}});var t=!1;document.dispatchEvent(event),$(document).on(e+"Result",function(e){0==t&&(t=!0,$("#scrapingInfo").removeClass("show"),0==e.detail.success?showAccountCredentials(e.detail.msg):(jsonscrap[scrapping[0].id]=e.detail.msg,ScrapingInfoFinished()))})}function ScrapingInfoFinished(){scrappingFinished.push(scrapping[0]),scrapping.splice(0,1),0==scrapping.length?postHandler.post("FilterScrap",{scrapjson:JSON.stringify(jsonscrap)},function(){},function(e){showSavingPopup(JSON.parse(e))},function(){},"text"):showAccountCredentials("")}function checkInputs(e){var t=!1,n=e.find("input");n.each(function(e,n){return""===$(n).val()?void(t=!0):void 0}),t?$("button[type='submit']",e).addClass("locked"):$("button[type='submit']",e).removeClass("locked")}$("#manualImportation").click(function(){$("#simpleImportation").addClass("show")});var addAppTutoCpt=0,appsSelectedFilled=0,appsSelectedSkiped=0;$("div#simpleImportation div.appHandler").click(function(){$(this).find("div.app").hasClass("selected")?$(this).find("div.app").removeClass("selected"):$(this).find("div.app").addClass("selected"),$("#simpleImportation .app.selected").length>=4?$("#simpleImportation button[type='submit']").removeClass("locked"):$("#simpleImportation button[type='submit']").addClass("locked")}),$("#simpleImportation .showMoreHelper").click(function(){$(this).css("display","none"),$("#simpleImportation .appHandler.hidden").removeClass("hidden")}),$("div#addAppTutorial #skipButton").click(function(){var e=$("div#addAppTutorial input#name").val(),t=$("div#simpleImportation div.appHandler div.app.selected:eq("+addAppTutoCpt+")").attr("id");postHandler.post("AddEmptyApp",{name:e,profileId:$("div#addAppTutorial input#profileId").val(),websiteId:t},function(){},function(){var n=null!=$(".catlogApp[idx='"+t+"']").attr("newApp");easeTracker.trackEvent("AddApp",{appType:"EmptyApp",appName:e,AppNewYN:n}),appsSelectedSkiped++},function(){},"text"),goToNextStep()}),$("div#addAppTutorial form").submit(function(e){e.preventDefault();var t=$("div#addAppTutorial input#name").val(),n=[],a=$("div#simpleImportation div.appHandler div.app.selected:eq("+addAppTutoCpt+")").attr("id");n.push(a),postHandler.post("AddClassicApp",{name:t,login:$("div#addAppTutorial input#login").val(),profileId:$("div#addAppTutorial input#profileId").val(),password:$("div#addAppTutorial input#password").val(),websiteIds:JSON.stringify(n)},function(){},function(){var e=null!=$(".catlogApp[idx='"+a+"']").attr("newApp");easeTracker.trackEvent("AddApp",{appType:"ClassicApp",appName:t,AppNewYN:e}),appsSelectedFilled++},function(){},"text"),""!=$("div#addAppTutorial input#login").val()&&""!=$("div#addAppTutorial input#password").val()&&""!=$("div#addAppTutorial input#name").val()&&goToNextStep()}),$("div#simpleImportation button").click(function(){$("div#simpleImportation div.appHandler div.app.selected").length>=4&&($("div#simpleImportation").removeClass("show"),showAddAppTuto(addAppTutoCpt))});var scrapping=[],appToAdd=[],jsonscrap={},scrappingFinished=[],profileIds=[],loadingStep=0,currentStep=0,maxSteps=0,chromeScrappingCount=0,facebookScrappingCount=0,linkedInScrappingCount=0,chromeSelectedCount=0,facebookSelectedCount=0,linkedInSelectedCount=0;$("div#saving div#selectScraping button").click(function(){sendTutoAddApp()}),$("div#importation div#selectScraping input:checkbox").click(function(){0==$("div#importation div#selectScraping input:checkbox:checked").length?$("div#importation div#selectScraping button").addClass("locked"):$("div#importation div#selectScraping button").removeClass("locked")}),$("div#importation div#selectScraping button").click(function(){$("div.account").each(function(){$(this).find("input:checkbox").is(":checked")&&scrapping.push({name:$(this).find("p.name").text(),websiteId:$(this).find("input:checkbox").attr("websiteId"),id:$(this).find("input:checkbox").attr("id"),img:$(this).find("img").attr("src"),login:"",password:""})}),scrapping.length>0&&($("div#importation div#selectScraping").removeClass("show"),showAccountCredentials(""))}),$("div#importation div#selectScraping a").click(function(){$("div#importation").removeClass("show"),$("div#simpleImportation").addClass("show")}),$("#accountCredentials input").keypress(function(e){var t=!1;13==e.which&&0==t&&($("#accountCredentials button").click(),t=!0,setTimeout(function(){t=!1},500))}),$("#accountCredentials input").keyup(function(){$(this).change()}),$("#accountCredentials input").change(function(){checkInputs($("#accountCredentials"))}),$("#accountCredentials a").click(function(){ScrapingInfoFinished(),$("#accountCredentials #chromeUserEmailHelper").removeClass("show")}),$("#accountCredentials button").click(function(){scrapping[0].login=$("#accountCredentials input[name='email']").val(),scrapping[0].password=$("#accountCredentials input[name='password']").val(),$("#accountCredentials #chromeUserEmailHelper").removeClass("show"),$("#accountCredentials").removeClass("show"),showScrapingInfo()});