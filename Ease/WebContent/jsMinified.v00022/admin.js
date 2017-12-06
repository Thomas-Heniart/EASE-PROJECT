function createWebsiteFailureRow(e){var a=$("<tr><td class='count'>"+e.count+"</td><td class='url'>"+e.url+"</td><td><a href='#' class='delete'><i class='fa fa-trash'></i></a></td></tr>");return $("a.delete",a).click(function(){$("a.delete",a).click(function(){var t=$("#website-failure-delete"),i=$(".ok",t);i.click(function(){i.addClass("loading"),ajaxHandler.post("/api/v1/admin/DeleteWebsiteFailure",{url:e.url},function(){},function(){i.removeClass("loading"),$(a).remove(),t.modal("hide")}),i.off("click")}),t.modal({onHide:function(){i.off("click")}}).modal("show")})}),a}function createRequestRow(e){var a=$("<tr website-id='"+e.website_id+"'><td class='id'>"+e.id+"</td><td class='url'>"+e.url+"</td><td class='email'>"+e.email+"</td><td class='date'>"+new Date(e.date).toLocaleString()+"</a></td><td><div class=\"ui checkbox\"><input type=\"checkbox\"/><label></label></div></td><td><a href='#' class='delete'><i class='fa fa-trash'></i></a></td></tr>");return e.integrated?a.addClass("positive"):a.addClass("negative"),$("a.delete",a).click(function(){$("a.delete",a).click(function(){var t=$("#request-delete"),i=$(".ok",t);i.click(function(){i.addClass("loading"),ajaxHandler.post("/api/v1/admin/DeleteWebsiteRequest",{website_request_id:e.id,website_id:e.website_id},function(){},function(){i.removeClass("loading"),$(a).remove(),t.modal("hide")}),i.off("click")}),t.modal({onHide:function(){i.off("click")}}).modal("show")})}),a}function addCategoryRow(e){var a=$("<tr><td>"+e.id+"</td><td class='name'>"+e.name+"</td><td class='position'>"+e.position+"</td><td><a href='#' class='edit'><i class='fa fa-cog'></i></a></td><td><a href='#' class='delete'><i class='fa fa-trash'></i></a></td></tr>");return $("a.edit",a).click(function(){openCategoryEdit(e,a)}),$("a.delete",a).click(function(){var t=$("#category-delete"),i=$(".ok",t);i.click(function(){i.addClass("loading"),ajaxHandler.post("/api/v1/admin/DeleteCategory",{id:e.id},function(){},function(){i.removeClass("loading"),$(a).remove(),t.modal("hide")}),i.off("click")}),t.modal({onHide:function(){i.off("click")}}).modal("show")}),a}function addTeamRow(e,a){var t=$("<tr><td>"+(a+1)+"</td><td>"+e.id+"</td><td>"+e.name+"</td><td>"+e.admin_first_name+" "+e.admin_last_name+"</td><td>"+e.admin_email+"</td><td>"+e.phone_number+"</td><td>"+(0===e.plan_id?"Free":"Pro")+"</td><td>"+e.card_entered+"</td><td>"+e.team_users_size+"</td><td>"+e.active_team_users+"</td><td>"+e.people_click_on_app_three_days+"</td><td><a href='#'><i class='fa fa-cog'></i></a></td></tr>");return e.is_active||t.addClass("negative"),$("a",t).click(function(){openTeamSettings(e,t)}),t}function addWebsiteRow(e){var a=$('<tr><td class="id">'+e.id+'</td><td class="name">'+e.name+'</td><td class="logo"><img class="ui mini image" src="'+e.logo+'" /> </td><td class="login_url">'+e.login_url+'</td><td class="landing_url">'+e.landing_url+'</td><td> <div class="ui checkbox public"><input type="checkbox"/><label></label></div></td><td><a href="#"><i class="fa fa-pencil edit-website"/></a></td><td><a href="#" class="delete"><i class="fa fa-trash"/></a></td><td><a href="#" class="merge-website">Merge</a></td><td class=\'login\'>'+(void 0===e.website_credentials?"":e.website_credentials.login)+"</td><td class='password'>"+(void 0===e.website_credentials?"":e.website_credentials.password)+"</td><td class='publicKey'>"+(void 0===e.website_credentials?"":e.website_credentials.publicKey)+"</td></tr>");return e.integrated||a.addClass("negative"),e.public||($(".public",a).addClass("checked"),$(".public input",a).prop("checked",!0)),$(".public input",a).click(function(){var a=$(this).is(":checked");ajaxHandler.post("/api/v1/admin/ToggleWebsiteVisibility",{id:e.id,private:a})}),$(".edit-website",a).click(function(){openWebsiteIntegration(e,a)}),$(".merge-website",a).click(function(){openWebsiteMerging(e,a)}),$(".delete",a).click(function(){var t=$("#website-delete"),i=$(".ok",t);i.click(function(){i.addClass("loading"),ajaxHandler.post("/api/v1/admin/DeleteWebsite",{id:e.id},function(){},function(){$(a).remove(),t.modal("hide")}),i.off("click")}),t.modal({onHide:function(){i.off("click")}}).modal("show")}),a}function openCategoryEdit(e,a){var t=$("#category-modal"),i=$("#category-edition",t),n=$("input[name='name']",i),d=$("input[name='position']",i);n.val(e.name),d.val(e.position),i.submit(function(o){o.stopPropagation(),o.preventDefault();var s=$(this).attr("action");ajaxHandler.post(s,{category_id:e.id,name:n.val(),position:parseInt(d.val())},function(){},function(){e.name=n.val(),e.position=parseInt(d.val()),$(".name",a).text(e.name),$(".position",a).text(e.position),t.modal("hide")},function(){t.modal("hide")}),i.off("submit")}),t.modal({onHide:function(){i.off("submit")}}).modal("show")}function openWebsiteIntegration(e,a){var t=$("#website-integration"),i=$("#website-edition",t),n=$("input[name='name']",i),d=$("input[name='login_url']",i),o=$("input[name='landing_url']",i),s=$("input[name='folder']",i),l=$("#integration input[name='integrate']",i);$("#website-upload input[name='website_id']",t).val(e.id),n.val(e.name),d.val(e.login_url),o.val(e.landing_url),s.val(e.folder),e.integrated&&($("#integration",i).addClass("checked"),l.prop("checked",!0)),$("input[name='team_id']",t).val(""),e.teams.forEach(function(e){$(".teams .item[data-value='"+e.id+"']",t).click()}),$(".sso .item[data-value='"+e.sso+"']",t).click(),$(".category .item[data-value='"+e.category_id+"']",t).click(),$("input[name='connectWith_id']",t).val(""),e.connectWith.forEach(function(e){$(".connectWith .item[data-value='"+e+"']",t).click()}),i.submit(function(r){r.stopPropagation(),r.preventDefault();var c=$(this).attr("action"),u=[];""!==$("input[name='team_id']",t).val()&&(u=$("input[name='team_id']",t).val().split(",").map(function(e){return parseInt(e)}));var m=[];""!==$("input[name='connectWith_id']",t).val()&&(m=$("input[name='connectWith_id']",t).val().split(",").map(function(e){return parseInt(e)}));var f=$("input[name='sso_id']",t).val(),p=parseInt($("input[name='category_id']",t).val());ajaxHandler.post(c,{id:e.id,name:n.val(),landing_url:o.val(),login_url:d.val(),folder:s.val(),integrated:l.is(":checked"),teams:u,sso_id:f,category_id:p,connectWith:m},function(){},function(){e.name=n.val(),e.landing_url=o.val(),e.login_url=d.val(),e.folder=s.val(),e.integrated=l.is(":checked"),u=u.map(function(e){return{id:e,name:$(".teams .item[data-value='"+e+"']",t).text()}}),e.sso=""!==f?parseInt(f):-1,e.category_id=p,e.teams=u,e.connectWith=m,e.integrated||a.addClass("negative"),$(".name",a).text(e.name),$(".landing_url",a).text(e.landing_url),$(".login_url",a).text(e.login_url),$(".logo img",a).attr("src","/resources/websites/"+e.folder+"/logo.png")}),i.off("submit"),t.modal("hide")}),t.modal({onHide:function(){i.off("submit"),$("input[name='team_id']",t).val(""),$("input[name='connectWith_id']",t).val(""),$("a.ui.label.transition",t).remove(),$(".item.active").removeClass("active").removeClass("filtered"),$("input.search").val("")}}).modal("show")}function openWebsiteMerging(e,a){var t=$("#website-merging");$(".form button",t).click(function(){ajaxHandler.post("/api/v1/admin/MergeWebsite",{id:$("input[name='website_id']",t).val(),id_to_merge:e.id},function(){},function(){$(".item[data-value='"+e.id+"']").remove(),a.remove(),t.modal("hide")})}),t.modal({onHide:function(){$(".form button",t).off("click")}}).modal("show")}function addResult(e){return $("<div class='item' data-value='"+e.id+"'><img class='ui avatar image' src='"+e.logo+"' />"+e.name+"</div>")}function openTeamSettings(e,a){var t=$("#team-settings");ajaxHandler.get("/api/v1/admin/GetTeam",{team_id:e.id},function(){},function(i){var n=$("#send-money",t);$("#current-credit",t).text(e.credit),$("#card-number span",t).text(i.card_number),$("#link-number span",t).text(i.link_card_number),$("#single-number span",t).text(i.single_card_number),$("#enterprise-number span",t).text(i.enterprise_card_number),$("#card-with-password-reminder span",t).text(i.card_with_receiver_and_password_reminder_number),$("i",n).click(function(a){if(!n.hasClass("loading")){var i=$("input",n).val();if(-1===i.indexOf("."))i=100*parseInt(i);else{var d=i.split(".")[0],o=i.split(".")[1];1===o.length&&(o+="0"),i=100*parseInt(d)+parseInt(o)}n.addClass("loading"),n.addClass("disabled"),ajaxHandler.post("/api/v1/admin/SendLoveMoney",{team_id:e.id,credit:i},function(){},function(e){$("#current-credit",t).text(e.credit),$("input",n).val(""),n.removeClass("loading"),n.removeClass("disabled")})}}),$("button.negative",t).click(function(){var i=$(this);i.addClass("loading"),ajaxHandler.post("/api/v1/admin/DeleteTeam",{team_id:e.id},function(){},function(){i.removeClass("loading"),a.remove(),t.modal("hide")})}),t.modal({onHide:function(){$("i",n).off("click"),$("input",n).val("")}}).modal("show")})}$(document).ready(function(){$(".ui.checkbox").checkbox(),$(".ui.dropdown").dropdown(),$("#website-requests-segment button").click(function(){var e=$(this);e.addClass("loading");var a=$("#website-requests-manager-body tr.positive .checkbox input:checked").map(function(e,a){var t=$(a).parent().parent().parent();return{email:$(".email",t).text(),id:parseInt(t.attr("website-id"))}}),t={};console.log(a.toArray()),a.toArray().forEach(function(e){var a=t[e.email];void 0===a&&(a=[]),a.push(e.id),t[e.email]=a}),ajaxHandler.post("/api/v1/admin/SendWebsitesIntegrated",{emailAndWebsiteIds:t},function(){},function(){e.removeClass("loading"),$("#website-requests-manager-body tr.positive .checkbox input:checked").parent().parent().parent().remove()})}),$("#category-segment #add-category").submit(function(e){e.stopPropagation(),e.preventDefault();var a=$("input",$(this));a.parent().addClass("disabled"),ajaxHandler.post("/api/v1/admin/AddCategory",{name:a.val()},function(){},function(e){addCategoryRow(e).appendTo($("#category-manager-body")),a.parent().removeClass("disabled"),a.val("")})}),$(".ui.menu a.item").on("click",function(){$(this).addClass("active").siblings().removeClass("active");var e=$($(this).attr("data-target"));if($(".segment").hide(),$(".segment").addClass("loading"),$(".segment table tbody tr").remove(),e.show(),e.hasClass("loading"))switch(e.attr("id")){case"team-segment":ajaxHandler.get("/api/v1/admin/GetTeamsInformation",null,function(){},function(e){e.forEach(function(e,a){addTeamRow(e,a).appendTo($("#team-manager-body"))})}),e.removeClass("loading");break;case"website-segment":ajaxHandler.get("/api/v1/admin/GetTeams",null,function(){},function(e){e.forEach(function(e){$("<div class='item' data-value='"+e.id+"'>"+e.name+"</div>").appendTo($("#website-edition .teams .menu"))})}),ajaxHandler.get("/api/v1/admin/GetSso",null,function(){},function(e){e.forEach(function(e){$("<div class='item' data-value='"+e.id+"'>"+e.name+"</div>").appendTo($("#website-edition .sso .menu"))})}),ajaxHandler.get("/api/v1/admin/GetWebsites",null,function(){},function(a){websites=a,websites.forEach(function(e){addWebsiteRow(e).appendTo($("#website-manager-body")),addResult(e).appendTo($("#website-merging .menu")),$("<div class='item' data-value='"+e.id+"'>"+e.name+"</div>").appendTo($("#website-edition .connectWith .menu"))}),e.removeClass("loading")}),ajaxHandler.get("/api/v1/catalog/GetCategories",null,function(){},function(e){var a=e.categories;a.sort(function(e,a){return e.position-a.position}),a.forEach(function(e){$("<div class='item' data-value='"+e.id+"'>"+e.name+"</div>").appendTo($("#website-edition .category .menu"))})});break;case"category-segment":ajaxHandler.get("/api/v1/catalog/GetCategories",null,function(){},function(a){var t=a.categories;t.sort(function(e,a){return e.position-a.position}),t.forEach(function(e){addCategoryRow(e).appendTo($("#category-manager-body"))}),e.removeClass("loading")});break;case"website-requests-segment":ajaxHandler.get("/api/v1/admin/GetWebsiteRequests",null,function(){},function(a){var t=a.website_requests;t.sort(function(e,a){a.date,e.date}),t.forEach(function(e){createRequestRow(e).appendTo($("#website-requests-manager-body"))}),e.removeClass("loading")});break;case"website-failures-segment":ajaxHandler.get("/api/v1/admin/GetWebsiteFailures",null,function(){},function(a){a.forEach(function(e){createWebsiteFailureRow(e).appendTo($("#website-failures-body"))}),e.removeClass("loading")})}})});