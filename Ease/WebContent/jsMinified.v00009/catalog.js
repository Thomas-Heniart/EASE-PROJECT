function getAndSites(t){var a=t.filter(function(t,a){return"and"==t[1]});return $(".catalogApp").filter(function(t,e){a.includes($(e).attr("idx").toString())})}function sortSites(t){var a=t.map(function(t,a){return{index:a,value:$(t).attr("name").toLowerCase()}});return a.sort(function(t,a){return+(t>a)||+(t===a)-1}),a.map(function(a){return t[a.index]})}function refreshCatalogContent(t){if("["==t[0]){var a=JSON.parse(t);if($(".catalogApp").hide(),a.length>0){$(".catalogContainer .no-result-search").hide();$(".catalogApp").filter(function(t,e){return a.includes($(e).attr("idx"))}).show()}else $(".catalogContainer .no-result-search").show()}}function updateCatalogWith(t,a){var e=[],n="";a.each(function(t,o){e.push($(o).attr("tagid")),n+=$(o).text(),t<a.length&&(n+=" ")});var o=JSON.stringify(e);postHandler.post("SearchApp",{tags:o,search:t},function(){},function(a){easeTracker.trackEvent("CatalogSearch",{SearchContent:t,SelectedTags:n}),refreshCatalogContent(a)},function(t){},"text")}function newButtonGroup(t){return $(".selectedTagsContainer").append("<div tagid='"+t+"' class='btn-group btn-group-xs tags-group'></div>"),$("div[tagid='"+t+"'].tags-group")}function newCrossButton(t){return"<a href='#' tagid='"+t+"' class='btn btn-default delete-tag'>x</button>"}function setNewCrossCss(t){var a=$("a[tagid='"+t+"'].tag");$("a[tagid='"+t+"'].delete-tag").css("background-color",lighterColor(a.css("background-color"),.3)),$("a[tagid='"+t+"'].delete-tag").css("color",a.css("color"))}function addActionOnCrossButton(t){$("a[tagid='"+t+"'].delete-tag").click(function(t){t.stopPropagation();var a=$(t.target).parent().find("a.tag");$(a).removeClass("tag-active"),$(a).addClass("hvr-grow"),updateCatalogFront($(a))})}function updateTagsInSearchBar(){var t=0,a=parseInt($(".catalogSearchbar i.fa-search").css("width"));$(".tags-group").each(function(a,e){t+=parseInt($(e).css("width"))}),t+=a+7,t<25?$(".catalogSearchbar input").css("padding-left",25):$(".catalogSearchbar input").css("padding-left",t)}function updateCatalogFront(t){var a=t.attr("tagid");if(t.hasClass("tag-active")){var e=newButtonGroup(a);e.append(t),e.append(newCrossButton(a)),e.addClass("scaleinAnimation"),setNewCrossCss(a),addActionOnCrossButton(a)}else{var e=t.parent();$(".tagContainer .tags").prepend(t),e.remove()}updateTagsInSearchBar(),updateCatalogWith($(".catalogSearchbar input").val(),$(".selectedTagsContainer .tag")),$(".catalogApp").filter(function(){return"none"!=this.style.display}).length}function removeLastSelectedTag(){var t=$(".selectedTagsContainer .tag").last();t.removeClass("tag-active"),updateCatalogFront(t)}function addTagIfExists(t){var a;a=" "==t.val().slice(-1)?t.val().toLowerCase().slice(0,-1):t.val().toLowerCase();var e=$(".tagContainer").find('.tag[name="'+a+'"]'),n=$(".tag-active");0!=e.length&&(0==n.length?(e.addClass("tag-active"),updateCatalogFront(e),t.val("")):(n.toggleClass("hvr-grow"),n.toggleClass("tag-active"),updateCatalogFront(n),e.addClass("tag-active"),updateCatalogFront(e),t.val("")))}function updateVerfiedEmailsCount(){var t=$(".verifiedEmail").length;t>1?$(".integrated-emails-count span").html(t+" validated emails"):$(".integrated-emails-count span").html(t+" validated email")}function removeActiveTagFromFront(t){var a=t.parent();$(".tagContainer .tags").prepend(t),a.remove()}function getTagsFullWidth(){var t=0;return $(".tag").each(function(a,e){t+=$(e).outerWidth()}),t}function scroll(){$(".tagContainer .tags").animate({scrollLeft:amount},200,"linear")}$(document).ready(function(){$("#catalog-quit").click(function(t){t.stopPropagation(),leaveEditMode()}),$(".tag").click(function(t){t.stopPropagation();var a=$(".selectedTagsContainer .tag-active");a.length&&removeActiveTagFromFront(a),$(t.target).hasClass("tag-active")||($(t.target).toggleClass("tag-active"),$(t.target).toggleClass("hvr-grow")),updateCatalogFront($(t.target));var e=$(t.target).text();easeTracker.trackEvent("ClickOnTag",{TagName:e})}),$("input[name='catalogSearch']").keydown(function(t){8==t.keyCode&&(""==$(t.target).val()&&removeLastSelectedTag(),updateCatalogWith($(t.target).val(),$(".selectedTagsContainer .tag")))}),$("input[name='catalogSearch']").keyup(function(t){8==t.keyCode&&(t.preventDefault(),t.stopPropagation()),32!=t.keyCode&&13!=t.keyCode||addTagIfExists($(t.target)),updateCatalogWith($(t.target).val(),$(".selectedTagsContainer .tag"))}),$(".tagContainer i.fa-angle-right").click(function(){amount="+="+$(".tagContainer .tags").css("width"),scroll(),$(".tagContainer i.fa-angle-left").show(),$(".tagContainer .tags").scrollLeft()+parseInt($(".tagContainer .tags").width())>=getTagsFullWidth()?$(".tagContainer i.fa-angle-right").hide():$(".tagContainer i.fa-angle-right").show()}),$(".tagContainer i.fa-angle-left").click(function(){amount="-="+$(".tagContainer .tags").css("width"),$(".tagContainer i.fa-angle-right").css("display","inline-block"),scroll(),$(".tagContainer .tags").scrollLeft()-parseInt($(".tagContainer .tags").css("width"))<=0&&$(".tagContainer i.fa-angle-left").css("display","none")}),updateVerfiedEmailsCount(),$(".integrated-emails-count span").click(function(){$("#ModifyUserButton").click()})});var amount="",ssoObject=function(t,a,e){this.name=t,this.id=a,this.imgSrc=e},catalog,Catalog=function(t){var a=this;this.qRoot=t,this.isOpen=!1,this.quitButton=this.qRoot.find("#quit"),this.appsHolder=this.qRoot.find(".scaleContainerView"),this.appsArea=this.qRoot.find("#catalog"),this.searchBar=this.qRoot.find(".catalogSearchbar"),this.tagContainer=this.qRoot.find(".tagContainer"),this.integrateAppArea=this.qRoot.find(".helpIntegrateApps"),this.apps=[],this.ssos=[],postHandler.post("GetCatalogApps",{},function(){},function(t){for(var e,n=JSON.parse(t),o=0;o<n.length;o++)e=n[o],a.addApp(new catalogApp(e.name,e.singleId,e.logo,e.loginWith,e.ssoId,e.url,e.inputs,e.isNew,e.count))},function(t){},"text"),postHandler.post("GetSso",{},function(){},function(t){for(var e=JSON.parse(t),n=0;n<e.length;n++)a.ssos.push(new ssoObject(e[n].name,e[n].singleId,e[n].imgSrc))},function(t){},"text"),this.getSsoById=function(t){for(var e=0;e<a.ssos.length;e++)if(a.ssos[e].id==t)return a.ssos[e];return null},this.getAppByName=function(t){for(var e=0;e<a.apps.length;e++)if(a.apps[e].name==t)return a.apps[e]},this.getAppById=function(t){for(var e=0;e<a.apps.length;e++)if(a.apps[e].id==t)return a.apps[e]},this.getAppsBySsoId=function(t){for(var e=[],n=0;n<a.apps.length;n++)a.apps[n].ssoId==t&&e.push(a.apps[n]);return e},this.addApp=function(t){a.apps.push(t),a.appsArea.append(t.qRoot)},this.open=function(){a.qRoot.addClass("show"),a.isOpen=!0,easeTracker.trackEvent("OpenCatalog"),a.onResize()},this.close=function(){a.qRoot.removeClass("show"),a.isOpen=!1},this.onResize=function(){a.appsHolder.height(a.qRoot.outerHeight(!0)-(a.qRoot.find(".catalogHeader.title").outerHeight(!0)+a.searchBar.outerHeight(!0)+a.tagContainer.outerHeight(!0)+a.tagContainer.outerHeight(!0)/2+a.integrateAppArea.outerHeight(!0)));var t=0;a.apps.forEach(function(a){0==t&&(t=a.qRoot.width()),a.qRoot.height(t)})},lastLineViewed=0,$(".catalogContainer").scroll(function(t){var a=$(".catalogApp").height(),e=$(this).scrollTop(),n=Math.floor(e/a);n>lastLineViewed&&(lastLineViewed=n,easeTracker.trackEvent("CatalogScroll",{CatalogLinesViewed:lastLineViewed}))}),this.onResize(),this.quitButton.click(function(){leaveEditMode()}),$(window).resize(function(){a.onResize()}),this.haveThisUrl=function(t){for(var e=[],n=0;n<a.apps.length;++n)a.apps[n].url.indexOf(t)>-1&&e.push(a.apps[n]);return e}};$(document).ready(function(){catalog=new Catalog($(".CatalogViewTab")),catalog.qRoot.hasClass("show")&&catalog.open(),$(".helpIntegrateApps #integrateAppForm #integrateApp").keyup(function(t){13==t.keyCode&&$(".helpIntegrateApps #integrateAppForm #integrate").trigger("click")}),$(".helpIntegrateApps #integrateAppForm #integrate").click(function(t){t.preventDefault();var a=$(this).closest("#integrateAppForm"),e=$(a).find("#integrateApp").val();postHandler.post("WebsiteRequest",{ask:e},function(){$(a).find(".inputs input").val(""),$(a).find(".inputs").hide(),$(a).find(".confirmation").show().delay(1e3).fadeOut(function(){$(a).find(".inputs").show()})},function(t){easeTracker.trackEvent("RequestWebsite",{AskedWebsiteName:e})},function(t){},"text")})});