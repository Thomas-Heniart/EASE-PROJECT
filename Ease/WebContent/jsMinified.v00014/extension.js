function showExtensionPopup(){var e=$("#ease_extension"),n=$("#new_ease_extension");if(e.length){if(!waitForExtension)return"Safari"==getUserNavigator()?(!$("#ease_extension").attr("safariversion")||"2.2.5"!=$("#ease_extension").attr("safariversion"))&&($("#extension .title p").text("Update your extension"),$("#extension #download #line1").text("A new version of the extension is now available."),$("#extension #download #line2").text("We added new features and made it faster !"),$("#extension #download button").text("Update Ease Extension"),$("#extension").addClass("myshow"),$("#extension #download").addClass("show"),!0):($("#extension").addClass("myshow"),$("#extension #deleteExtension").addClass("show"),!0);setTimeout(function(){return showExtensionPopup()},200)}else{if(n.length)return!1;waitForExtension?setTimeout(function(){return showExtensionPopup()},200):($("#extension").addClass("myshow"),$("#extension #download").addClass("show"))}}function sendEvent(e){if(testApp){if(!$(e).hasClass("waitingLinkImage")){var n=$(e).closest(".siteLinkBox").attr("id"),t=$(e).closest(".siteLinkBox").attr("link"),o=($(e).find(".linkImage"),new Object);$(e).addClass("waitingLinkImage"),$(e).addClass("scaleinAnimation"),setTimeout(function(){$(e).removeClass("waitingLinkImage"),$(e).removeClass("scaleinAnimation")},1e3),void 0!==t&&!1!==t||postHandler.post("AskInfo",{appId:n},function(){},function(e){o.detail=JSON.parse(e),s=new CustomEvent("Test",o),document.dispatchEvent(s)},function(e){showAlertPopup(e,!0)},"text")}}else if(!$(e).hasClass("waitingLinkImage")){var s,n=$(e).closest(".siteLinkBox").attr("id"),t=$(e).closest(".siteLinkBox").attr("link"),o=($(e).find(".linkImage"),new Object);if(showExtensionPopup())return;$(e).addClass("waitingLinkImage"),$(e).addClass("scaleinAnimation"),setTimeout(function(){$(e).removeClass("waitingLinkImage"),$(e).removeClass("scaleinAnimation")},1e3),postHandler.post("AskInfo",{appId:n},function(){},function(e){o.detail=JSON.parse(e);var n="NewConnection";if(o.detail[0]&&o.detail[0].url)o.detail=o.detail[0],n="NewLinkToOpen",easeTracker.trackEvent("ClickOnApp",{type:"LinkApp",appName:o.detail.app_name});else{var t=o.detail[o.detail.length-1];easeTracker.trackEvent("ClickOnApp",{type:t.type,appName:t.app_name,websiteName:t.website_name})}var i=""+new Date;easeTracker.setOnce("TutoDateFirstClickOnApp",i),o.detail.highlight=!ctrlDown,s=new CustomEvent(n,o),document.dispatchEvent(s)},function(e){showAlertPopup(e,!0)},"text")}}var waitForExtension=!0;$(document).ready(function(){setTimeout(function(){waitForExtension=!1,showExtensionPopup(),showExtensionPopup()||$("#tutorial").addClass("myshow")},800)}),$(document).ready(function(){$("#homePageSwitch").change(function(){var e=$(this).is(":checked");easeTracker.setHomepage(e),easeTracker.trackEvent("HomepageSwitch");var n=e.toString();postHandler.post("HomepageSwitch",{homepageState:n},function(){},function(e){},function(e){})}),$("#chrome button[type='submit'], #safari button[type='submit']").click(function(){window.location="/"}),$("#extension #download #showExtensionInfo").click(function(){$("#extension #step1").removeClass("show"),$("#extension #extensionInfo").addClass("show")}),$("#extension #extensionInfo button").click(function(){$("#extension #extensionInfo").removeClass("show"),$("#extension #step1").addClass("show")}),$("#extension #download button[type='submit']").click(function(){$("#extension #step1 #download").removeClass("show"),"Chrome"==NavigatorName?window.open("https://chrome.google.com/webstore/detail/hnacegpfmpknpdjmhdmpkmedplfcmdmp","_blank").focus():"Safari"==NavigatorName?($("#extension #step1 #safari").addClass("show"),window.location.replace(location.protocol+"//"+location.hostname+"/safariExtension/EaseExtension.safariextz")):$("#extension #step1 #other").addClass("show")})});