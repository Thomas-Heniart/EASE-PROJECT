function showAddAppPopup(o,t){var p=$("#PopupAddApp"),d=$($("#boxHelper").html());popupAddApp.open(),popupAddApp.appId(null),popupAddApp.setHelper($(t)),popupAddApp.setAppsContainer($(o)),popupAddApp.setNewAppItem(d),popupAddApp.setVal($(t).attr("name")),p.find(".loginWithButton").removeClass("locked"),p.find(".or").css("display","block"),p.find(".loginAppChooser .ChooserContent").empty(),p.find(".loginAppChooser").css("display","none"),p.find(".loginSsoChooser .ChooserContent").empty(),p.find(".loginSsoChooser").css("display","none"),p.find("button.Active").prop("disabled",!0),p.find("button.Active").removeClass("Active"),d.attr("name",$(t).attr("name")),d.find("img.logo").attr("src",$(t).find("img").attr("src")),d.find(".siteName p").text($(t).attr("name")),p.find(".logoApp").attr("src",$(t).find("img").attr("src"));var e=$("#PopupAddApp .loginWithChooser"),n=$(t).attr("data-login").split(",");if(e.addClass("hidden"),e.find(".loginWithButton").addClass("hidden"),""!=$(t).attr("data-login")){e.removeClass("hidden");for(var s=0;s<n.length;s++)e.find(".loginWithButton[name='"+$(".catalogApp[idx='"+n[s]+"']").attr("name")+"']").removeClass("hidden")}var i=$(".loginSsoChooser .ChooserContent"),a=$("<div class='AccountApp'><div class='imageHandler'><img src='' /></div><p></p></div>"),l=$("#PopupAddApp").find(".loginSsoChooser"),r=$(t).attr("data-sso");if(""!=r&&"0"!=r){var c=$(".siteLinkBox[ssoId='"+r+"']"),A=[];0!=c.length&&l.css("display","block");for(var s=0;s<c.length;s++)A.includes($(c[s]).attr("login"))||""==$(c[s]).attr("login")||null==$(c[s]).attr("login")||(a.find("p").text($(c[s]).attr("login")),a.attr("aId",$(c[s]).attr("id")),a.find("img").attr("src","resources/sso/"+r+".png"),SsoHelperCloned=$(a).clone(),SsoHelperCloned.click(function(){$(this).closest(".ChooserContent").find(".AccountApp.selected").removeClass("selected"),$(this).addClass("selected"),popupAddApp.appId(a.attr("aid")),popupAddApp.setPostName("addAppWithSso")}),i.append(SsoHelperCloned),A.push($(c[s]).attr("login")))}"true"==$(t).attr("data-nologin")&&(p.find(".classicLogin").removeClass("show"),$(".or").css("display","none")),$("#PopupAddApp .buttonBack").unbind("click"),$("#PopupAddApp .buttonBack").click(function(){var o=$(this).closest(".md-content");o.find(".loginWithButton").removeClass("locked"),o.find(".loginAppChooser .ChooserContent").empty(),o.find(".loginAppChooser").css("display","none"),"true"!=$(t).attr("data-nologin")&&(popupAddApp.appId(null),o.find(".classicLogin").addClass("show"),o.find(".or").css("display","block"))})}$(document).ready(function(){$("#PopupAddApp").find(".loginWithButton").click(function(){$(".loginAppChooser p").text("Select your account");var o=$(this).closest(".md-content"),t=$(this).closest(".md-content").find(".loginAppChooser .ChooserContent"),p=$(".catalogApp[name='"+$(this).attr("name")+"']").attr("idx"),d=$("<div class='AccountApp'><div class='imageHandler'><img src='' /></div><p></p></div>");t.empty();var e;o.find(".loginWithButton").removeClass("locked"),$(this).addClass("locked"),o.find(".loginAppChooser").css("display","block"),o.find(".classicLogin").removeClass("show"),o.find(".or").css("display","none");var n=$(".siteLinkBox[webid='"+p+"']");if(0==n.length){var s=$(".catalogApp[idx='"+p+"']").attr("name");s&&$(".loginAppChooser p").text("No "+s+" account detected")}for(var i=0;i<n.length;i++)d.attr("aId",$(n[i]).attr("id")),d.find("p").text($(n[i]).attr("login")),d.find("img").attr("src",$(n[i]).find("img.logo").attr("src")),e=$(d).clone(),e.click(function(){$(o).find(".AccountApp.selected").removeClass("selected"),$(this).addClass("selected"),popupAddApp.appId($(this).attr("aid")),popupAddApp.setPostName("AddLogwithApp")}),0==i&&($(e).addClass("selected"),popupAddApp.appId($(e).attr("aid")),popupAddApp.setPostName("AddLogwithApp")),t.append(e)})});