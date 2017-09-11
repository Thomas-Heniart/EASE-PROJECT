$(document).ready(function () {
    $(".ui.checkbox").checkbox();
    $(".ui.dropdown").dropdown();
    $(".ui.menu a.item").on("click", function () {
        $(this)
            .addClass("active")
            .siblings().removeClass("active");
        var target = $($(this).attr("data-target"));
        $(".segment").hide();
        target.show();
        if (target.hasClass("loading")) {
            switch (target.attr("id")) {
                case "team-segment":
                    ajaxHandler.get("/api/v1/admin/GetTeamsInformation", null, function () {
                    }, function (data) {
                        data.forEach(function (team) {
                            addTeamRow(team).appendTo($("#team-manager-body"));
                        });
                        target.removeClass("loading");
                    });
                    break;

                case "website-segment":
                    ajaxHandler.get("/api/v1/admin/GetTeams", null, function () {

                    }, function (data) {
                        data.forEach(function (team) {
                            $("<div class='item' data-value='" + team.id + "'>" +
                                team.name + "</div>").appendTo($("#website-edition .teams .menu"));

                        });
                    });
                    ajaxHandler.get("/api/v1/admin/GetSso", null, function () {

                    }, function (data) {
                        data.forEach(function (sso) {
                            $("<div class='item' data-value='" + sso.id + "'>" +
                                sso.name + "</div>").appendTo($("#website-edition .sso .menu"));
                        });
                    });
                    ajaxHandler.get("/api/v1/admin/GetWebsites", null, function () {
                    }, function (data) {
                        websites = data;
                        websites.forEach(function (website) {
                            addWebsiteRow(website).appendTo($("#website-manager-body"));
                            addResult(website).appendTo($("#website-merging .menu"));
                        });
                        target.removeClass("loading");
                    });
                    break;

                default:
                    break;
            }
        }
    });
});

function addTeamRow(team) {
    var elem = $("<tr>" +
        "<td>" + team.id + "</td>" +
        "<td>" + team.name + "</td>" +
        "<td>" + team.admin_first_name + "</td>" +
        "<td>" + team.admin_last_name + "</td>" +
        "<td>" + team.admin_email + "</td>" +
        "<td>" + team.team_users_size + "</td>" +
        "<td>" + team.active_team_users + "</td>" +
        "<td>" + team.phone_number + "</td>" +
        "<td><a href='#'><i class='fa fa-cog'></i></a></td>" +
        "</tr>");
    if (!team.is_active)
        elem.addClass("negative");
    $("a", elem).click(function () {
        openTeamSettings(team, elem);
    });
    return elem;
}

function addWebsiteRow(website) {
    var elem = $('<tr>'
        + '<td class="id">' + website.id + '</td>'
        + '<td class="name">' + website.name + '</td>'
        + '<td class="logo"><img class="ui mini image" src="' + website.logo + '" /> </td>'
        + '<td class="login_url">' + website.login_url + '</td>'
        + '<td class="landing_url">' + website.landing_url + '</td>'
        + '<td> <div class="ui checkbox public"><input type="checkbox"/><label></label></div></td>'
        + '<td><a href="#"><i class="fa fa-pencil edit-website"/></a></td>'
        + '<td> <div class="ui checkbox delete"><input type="checkbox"/><label></label></div></td>'
        + '<td><a href="#" class="merge-website">Merge</a></td>'
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
    $(".merge-website", elem).click(function () {
        openWebsiteMerging(website, elem);
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
    website.teams.forEach(function (team) {
        $(".teams .item[data-value='" + team.id + "']", modal).click();
    });
    $(".sso .item[data-value='" + website.sso + "']", modal).click();
    edit_website.submit(function (e) {
        e.stopPropagation();
        e.preventDefault();
        var action = $(this).attr("action");
        var teams = [];
        if ($("input[name='team_id']", modal).val() !== "")
            teams = $("input[name='team_id']", modal).val().split(",").map(parseInt);
        var sso_id = $("input[name='sso_id']", modal).val();
        ajaxHandler.post(action, {
            id: website.id,
            name: name.val(),
            landing_url: landing_url.val(),
            login_url: login_url.val(),
            folder: folder.val(),
            integrated: integrated.is(":checked"),
            teams: teams,
            sso_id: sso_id
        }, function () {

        }, function () {
            website.name = name.val();
            website.landing_url = landing_url.val();
            website.login_url = login_url.val();
            website.folder = folder.val();
            website.integrated = integrated.is(":checked");
            teams = teams.map(function (id) {
                var name = $(".teams .item[data-value='" + id + "']", modal).text();
                return {
                    id: id,
                    name: name
                };
            });
            if (sso_id !== "")
                website.sso = parseInt(sso_id);
            else
                website.sso = -1;
            website.teams = teams;
            if (!website.integrated)
                websiteElem.addClass("negative");
            $(".name", websiteElem).text(website.name);
            $(".landing_url", websiteElem).text(website.landing_url);
            $(".login_url", websiteElem).text(website.login_url);
            $(".logo img", websiteElem).attr("src", "/resources/websites/" + website.folder + "/logo.png");
        });
        edit_website.off("submit");
        modal.modal("hide");
    });
    modal
        .modal({
            onHide: function () {
                edit_website.off("submit");
                $("input[name='team_id']", modal).val("");
                $("a.ui.label.transition", modal).remove();
                $(".item.active")
                    .removeClass("active")
                    .removeClass("filtered");
                $("input.search").val("");
            }
        })
        .modal("show");
}

function openWebsiteMerging(website, websiteElem) {
    var modal = $("#website-merging");
    $(".form button", modal).click(function () {
        ajaxHandler.post("/api/v1/admin/MergeWebsite", {
            id: $("input[name='website_id']", modal).val(),
            id_to_merge: website.id
        }, function () {
        }, function () {
            $(".item[data-value='" + website.id + "']").remove();
            websiteElem.remove();
            modal.modal("hide");
        });
    });
    modal
        .modal({
            onHide: function () {
                $(".form button", modal).off("click");
            }
        })
        .modal("show");
}

function addResult(website) {
    var elem = $("<div class='item' data-value='" + website.id + "'>" +
        "<img class='ui avatar image' src='" + website.logo + "' />" + website.name + "</div>");
    return elem;
}

function openTeamSettings(team, teamRow) {
    var modal = $("#team-sttings");
    var input = $("#send-money", modal);
    $("button", input).click(function () {
        ajaxHandler.post("/api/v1/admin/SendLoveMoney", {
            team_id: team.id,
            credit: parseInt($("input", input).val())
        }, function () {
            $("button", input).off("click");
        }, function (data) {

        });
    });
    modal
        .modal({
            onHide: function () {
                $("button", input).off("click");
            }
        })
        .modal("show");
}