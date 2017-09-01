var websites = [];

$(document).ready(function () {
    $(".ui.checkbox").checkbox();
    ajaxHandler.get("/api/v1/admin/GetWebsites", null, function () {
    }, function (data) {
        websites = data;
        websites.forEach(function (website) {
            addRow(website).appendTo($("#website-manager-body"));
        });
    });
});

function addRow(website) {
    var elem = $('<tr>'
        + '<td class="id">' + website.id + '</td>'
        + '<td class="name">' + website.name + '</td>'
        + '<td class="logo"><img class="ui mini image" src="' + website.logo + '" /> </td>'
        + '<td class="login_url">' + website.login_url + '</td>'
        + '<td class="landing_url">' + website.landing_url + '</td>'
        + '<td> <div class="ui checkbox public"><input type="checkbox"/><label></label></div></td>'
        + '<td><i class="fa fa-pencil edit-website"/></td>'
        + '<td> <div class="ui checkbox delete"><input type="checkbox"/><label></label></div></td>'
        + '</tr>');
    if (!website.integrated)
        elem.addClass("negative");
    if (!website.public) {
        $(".public", elem).addClass("checked");
        $(".public input", elem).prop("checked", true);
    }
    $(".public input", elem).click(function () {
        var checked = $(this).is(":checked");
        ajaxHandler.post("/api/v1/admin/ToggleWebsiteVisibility", {
            "id": website.id,
            "private": checked
        });
    });
    $(".edit-website", elem).click(function () {
        openWebsiteIntegration(website, elem);
    });
    return elem;
}

function openWebsiteIntegration(website, websiteElem) {
    var modal = $("#website-integration");
    var edit_website = $("#website-edition", modal);
    var name = $("input[name='name']", edit_website);
    var login_url = $("input[name='login_url']", edit_website);
    var landing_url = $("input[name='landing_url']", edit_website);
    var folder = $("input[name='folder']", edit_website);
    var integrated = $("#integration input[name='integrate']", edit_website);
    name.val(website.name);
    login_url.val(website.login_url);
    landing_url.val(website.landing_url);
    folder.val(website.folder);
    if (website.integrated) {
        $("#integration", edit_website).addClass("checked");
        integrated.prop("checked", true);
    }
    edit_website.submit(function (e) {
        e.stopPropagation();
        e.preventDefault();
        var action = $(this).attr("action");
        ajaxHandler.post(action, {
            id: website.id,
            name: name.val(),
            landing_url: landing_url.val(),
            login_url: login_url.val(),
            folder: folder.val(),
            integrated: integrated.is(":checked")
        }, function () {

        }, function () {
            website.name = name.val();
            website.landing_url = landing_url.val();
            website.login_url = login_url.val();
            website.folder = folder.val();
            website.integrated = integrated.is(":checked");
            if (!website.integrated)
                websiteElem.addClass(negative);
            $(".name", websiteElem).text(website.name);
            $(".landing_url", websiteElem).text(website.landing_url);
            $(".login_url", websiteElem).text(website.login_url);
            $(".logo img", websiteElem).attr("src", "/resources/websites/" + website.folder + "/logo.png");
        });
        edit_website.off("submit");
        modal.modal("hide");
    });
    modal.modal("show");
}