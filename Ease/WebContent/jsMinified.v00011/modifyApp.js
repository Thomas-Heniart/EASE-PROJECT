function resetDisabledInput(t){$(t).find("input").prop("disabled",!0),$(t).find(".activateInput").css("display","block"),"password"==$(t).find("input").attr("name")&&$(t).find("input").attr("placeholder","Click on the wheel to modify password")}function enableDisabledInput(t){$(t).find("input").prop("disabled",!1),$(t).find(".activateInput").css("display","none"),"password"==$(t).find("input").attr("name")&&$(t).find("input").attr("placeholder","Password")}function showModifyAppPopup(t,i){i.preventDefault(),i.stopPropagation(),modifyAppPopup.open();var o=$("#PopupModifyApp"),n=($(t).closest(".owl-item"),$(t).closest(".siteLinkBox"));modifyAppPopup.setApp($(n)),o.find(".logoApp").attr("src",$(n).find("img.logo").attr("src"));$(n).find(".linkImage");if($(n).hasClass("emptyApp")){o.find(".disabledInput").each(function(){enableDisabledInput($(this))}),o.find(".loginWithButton").removeClass("locked"),o.find(".classicLogin").addClass("show"),o.find(".or").css("display","block"),o.find(".loginAppChooser .ChooserContent").empty(),o.find(".loginAppChooser").css("display","none"),o.find("p.title").html("Finalize <span></span>.</br>Type your info for the last time");var a,s=$("#PopupModifyApp .loginWithChooser");if(a=$(".catalogContainer .catalogApp[idx='"+$(n).attr("webid")+"']").attr("data-login"))var e=a.split(",");else var e=[];if(s.addClass("hidden"),s.find(".loginWithButton").addClass("hidden"),""!=$(".catalogContainer .catalogApp[idx='"+$(n).attr("webid")+"']").attr("data-login")){s.removeClass("hidden");for(var d=0;d<e.length;d++)s.find(".loginWithButton[name='"+$(".catalogApp[idx='"+e[d]+"']").attr("name")+"']").removeClass("hidden")}$("#PopupModifyApp .buttonBack").unbind("click"),$("#PopupModifyApp .buttonBack").click(function(){var t=$(this).closest(".md-content");t.find(".loginWithButton").removeClass("locked"),t.find(".loginAppChooser .ChooserContent").empty(),t.find(".loginAppChooser").css("display","none"),"true"!=$(".catalogContainer .catalogApp[idx='"+$(n).attr("webid")+"']").attr("data-nologin")&&(t.find(".classicLogin").addClass("show"),t.find(".or").css("display","block"))}),$("#PopupModifyApp .popupHeader .title span").text($(n).attr("name"))}else{o.find(".disabledInput").each(function(){resetDisabledInput($(this))});var s=$("#PopupModifyApp .loginWithChooser");if(o.find("p.title").html("Modify informations related to <span></span>"),"false"!=n.attr("logwith")){var p=n.attr("logwith"),l=$(".siteLinkBox[id='"+p+"']").attr("webid");s.removeClass("hidden"),o.find(".loginAppChooser").css("display","block"),s.find(".loginWithButton").addClass("hidden"),s.find(".loginWithButton[name='"+$(".catalogApp[idx='"+l+"']").attr("name")+"']").removeClass("hidden"),o.find(".loginWithButton[name='"+$(".catalogApp[idx='"+l+"']").attr("name")+"']").click(),o.find(".AccountApp[aid='"+p+"']").click(),o.find(".classicLogin").removeClass("show")}else{o.find(".classicLogin").addClass("show"),s.addClass("hidden"),s.find(".loginWithButton").addClass("hidden"),o.find(".loginAppChooser").css("display","none");var s=$("#PopupModifyApp .loginWithChooser")}$("#PopupModifyApp .popupHeader .title span").text($(n).attr("name"))}}$(document).ready(function(){$(".disabledInput .activateInput").click(function(){var t=$(this).closest(".disabledInput").find("input");1==t.prop("disabled")&&(t.prop("disabled",!1),"password"==t.attr("name")&&t.attr("placeholder","Password")),$(this).css("display","none"),$(t).focus()}),$("#PopupModifyApp .loginWithButton").click(function(){var t=$(this).closest(".md-content"),i=$(this).closest(".md-content").find(".loginAppChooser .ChooserContent"),o=$(".catalogApp[name='"+$(this).attr("name")+"']").attr("idx"),n=$("<div class='AccountApp'><div class='imageHandler'><img src='' /></div><p></p></div>");i.empty();var a;t.find(".loginWithButton").removeClass("locked"),$(this).addClass("locked"),t.find(".loginAppChooser").css("display","block"),t.find(".or").css("display","none"),t.find(".classicLogin").removeClass("show");var s=$(".siteLinkBox[webid='"+o+"']");if(0==s.length){var e=$(".catalogApp[idx='"+o+"']").attr("name");e&&$(".loginAppChooser p").text("No "+e+" account detected")}for(var d=0;d<s.length;d++)n.attr("aId",$(s[d]).attr("id")),n.find("p").text($(s[d]).attr("login")),n.find("img").attr("src",$(s[d]).find("img.logo").attr("src")),a=$(n).clone(),a.click(function(){$(t).find(".AccountApp.selected").removeClass("selected"),$(this).addClass("selected")}),0==d&&($(a).addClass("selected"),$("#PopupModifyApp .buttonSet button[type='submit']").addClass("Active"),$("#PopupModifyApp .buttonSet button[type='submit']").prop("disabled",!1)),i.append(a)})});