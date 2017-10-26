$(document).ready(function () {
    $(".ui.checkbox").checkbox();
    $(".ui.dropdown").dropdown();
    $("#website-requests-segment button").click(function () {
        var button = $(this);
        button.addClass("loading");
        var requests = $("#website-requests-manager-body tr.positive .checkbox input:checked").map(function (i, elem) {
            var jElem = $(elem).parent().parent().parent();
            return {
                email: $(".email", jElem).text(),
                id: parseInt(jElem.attr("website-id"))
            }
        });
        var all_requests = {};
        console.log(requests.toArray());
        requests.toArray().forEach(function (request) {
            var existing_request = all_requests[request.email];
            if (existing_request === undefined)
                existing_request = [];
            existing_request.push(request.id);
            all_requests[request.email] = existing_request;
        });
        ajaxHandler.post("/api/v1/admin/SendWebsitesIntegrated", {
            emailAndWebsiteIds: all_requests
        }, function () {

        }, function () {
            button.removeClass("loading");
            $("#website-requests-manager-body tr.positive .checkbox input:checked").parent().parent().parent().remove();
        });
    });
    $("#category-segment #add-category").submit(function (e) {
        e.stopPropagation();
        e.preventDefault();
        var input = $("input", $(this));
        input.parent().addClass("disabled");
        ajaxHandler.post("/api/v1/admin/AddCategory", {
            name: input.val()
        }, function () {

        }, function (category) {
            addCategoryRow(category).appendTo($("#category-manager-body"));
            input.parent().removeClass("disabled");
            input.val("");
        });
    });
    $(".ui.menu a.item").on("click", function () {
        $(this)
            .addClass("active")
            .siblings().removeClass("active");
        var target = $($(this).attr("data-target"));
        $(".segment").hide();
        $(".segment").addClass("loading");
        $(".segment table tbody tr").remove();
        target.show();
        if (target.hasClass("loading")) {
            switch (target.attr("id")) {
                case "team-segment":
                    ajaxHandler.get("/api/v1/admin/GetTeamsInformation", null, function () {
                    }, function (data) {
                        data.forEach(function (team) {
                            addTeamRow(team).appendTo($("#team-manager-body"));
                        });
                    });
                    target.removeClass("loading");
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
                            $("<div class='item' data-value='" + website.id + "'>" +
                                website.name + "</div>").appendTo($("#website-edition .connectWith .menu"));
                        });
                        target.removeClass("loading");
                    });
                    ajaxHandler.get("/api/v1/catalog/GetCategories", null, function () {

                    }, function (data) {
                        var categories = data.categories;
                        categories.sort(function (c1, c2) {
                            return c1.position - c2.position;
                        });
                        categories.forEach(function (category) {
                            $("<div class='item' data-value='" + category.id + "'>" +
                                category.name + "</div>").appendTo($("#website-edition .category .menu"));
                        });
                    });
                    break;
                case "category-segment":
                    ajaxHandler.get("/api/v1/catalog/GetCategories", null, function () {

                    }, function (data) {
                        var categories = data.categories;
                        categories.sort(function (c1, c2) {
                            return c1.position - c2.position;
                        });
                        categories.forEach(function (category) {
                            addCategoryRow(category).appendTo($("#category-manager-body"));
                        });
                        target.removeClass("loading");
                    });
                    break;
                case "website-requests-segment":
                    ajaxHandler.get("/api/v1/admin/GetWebsiteRequests", null, function () {

                    }, function (data) {
                        var requests = data.website_requests;
                        requests.sort(function (r1, r2) {
                            r2.date - r1.date;
                        });
                        requests.forEach(function (request) {
                            createRequestRow(request).appendTo($("#website-requests-manager-body"));
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

function createRequestRow(request) {
    var elem = $("<tr website-id='" + request.website_id + "'>" +
        "<td class='id'>" + request.id + "</td>" +
        "<td class='url'>" + request.url + "</td>" +
        "<td class='email'>" + request.email + "</td>" +
        "<td class='date'>" + new Date(request.date).toLocaleString() + "</a></td>" +
        '<td><div class="ui checkbox"><input type="checkbox"/><label></label></div></td>' +
        "<td><a href='#' class='delete'><i class='fa fa-trash'></i></a></td>" +
        "</tr>");
    if (request.integrated)
        elem.addClass("positive");
    else
        elem.addClass("negative");
    $("a.delete", elem).click(function () {
        $("a.delete", elem).click(function () {
            var modal = $("#request-delete");
            var button = $(".ok", modal);
            button.click(function () {
                button.addClass("loading");
                ajaxHandler.post("/api/v1/admin/DeleteWebsiteRequest", {
                    website_request_id: request.id,
                    website_id: request.website_id
                }, function () {
                }, function () {
                    button.removeClass("loading");
                    $(elem).remove();
                    modal.modal("hide");
                });
                button.off("click");
            });
            modal
                .modal({
                    onHide: function () {
                        button.off("click");
                    }
                })
                .modal("show");
        });
    });
    return elem;
}

function addCategoryRow(category) {
    var elem = $("<tr>" +
        "<td>" + category.id + "</td>" +
        "<td class='name'>" + category.name + "</td>" +
        "<td class='position'>" + category.position + "</td>" +
        "<td><a href='#' class='edit'><i class='fa fa-cog'></i></a></td>" +
        "<td><a href='#' class='delete'><i class='fa fa-trash'></i></a></td>" +
        "</tr>");
    $("a.edit", elem).click(function () {
        openCategoryEdit(category, elem);
    });
    $("a.delete", elem).click(function () {
        var modal = $("#category-delete");
        var button = $(".ok", modal);
        button.click(function () {
            button.addClass("loading");
            ajaxHandler.post("/api/v1/admin/DeleteCategory", {
                id: category.id
            }, function () {
            }, function () {
                button.removeClass("loading");
                $(elem).remove();
                modal.modal("hide");
            });
            button.off("click");
        });
        modal
            .modal({
                onHide: function () {
                    button.off("click");
                }
            })
            .modal("show");
    });
    return elem;
}

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
        + '<td><a href="#" class="delete"><i class="fa fa-trash"/></a></td>'
        + '<td><a href="#" class="merge-website">Merge</a></td>'
        + "<td class='login'>" + (website.website_credentials === undefined ? "" : website.website_credentials.login) + "</td>"
        + "<td class='password'>" + (website.website_credentials === undefined ? "" : website.website_credentials.password) + "</td>"
        + "<td class='publicKey'>" + (website.website_credentials === undefined ? "" : website.website_credentials.publicKey) + "</td>"
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
    $(".delete", elem).click(function () {
        var modal = $("#website-delete");
        var button = $(".ok", modal);
        button.click(function () {
            button.addClass("loading");
            ajaxHandler.post("/api/v1/admin/DeleteWebsite", {
                id: website.id
            }, function () {
            }, function () {
                $(elem).remove();
                modal.modal("hide");
            });
            button.off("click");
        });
        modal
            .modal({
                onHide: function () {
                    button.off("click");
                }
            })
            .modal("show");
    });
    return elem;
}

function openCategoryEdit(category, elem) {
    var modal = $("#category-modal");
    var edit_category = $("#category-edition", modal);
    var name = $("input[name='name']", edit_category);
    var position = $("input[name='position']", edit_category);
    name.val(category.name);
    position.val(category.position);
    edit_category.submit(function (e) {
        e.stopPropagation();
        e.preventDefault();
        var action = $(this).attr("action");
        ajaxHandler.post(action, {
            category_id: category.id,
            name: name.val(),
            position: parseInt(position.val())
        }, function () {

        }, function () {
            category.name = name.val();
            category.position = parseInt(position.val());
            $(".name", elem).text(category.name);
            $(".position", elem).text(category.position);
            modal.modal("hide");
        }, function () {
            modal.modal("hide");
        });
        edit_category.off("submit");
    });
    modal
        .modal({
            onHide: function () {
                edit_category.off("submit");
            }
        })
        .modal("show");
}

function openWebsiteIntegration(website, websiteElem) {
    var modal = $("#website-integration");
    var edit_website = $("#website-edition", modal);
    var name = $("input[name='name']", edit_website);
    var login_url = $("input[name='login_url']", edit_website);
    var landing_url = $("input[name='landing_url']", edit_website);
    var folder = $("input[name='folder']", edit_website);
    var integrated = $("#integration input[name='integrate']", edit_website);
    $("#website-upload input[name='website_id']", modal).val(website.id);
    name.val(website.name);
    login_url.val(website.login_url);
    landing_url.val(website.landing_url);
    folder.val(website.folder);
    if (website.integrated) {
        $("#integration", edit_website).addClass("checked");
        integrated.prop("checked", true);
    }
    $("input[name='team_id']", modal).val("");
    website.teams.forEach(function (team) {
        $(".teams .item[data-value='" + team.id + "']", modal).click();
    });
    $(".sso .item[data-value='" + website.sso + "']", modal).click();
    $(".category .item[data-value='" + website.category_id + "']", modal).click();
    $("input[name='connectWith_id']", modal).val("");
    website.connectWith.forEach(function (connectWith_id) {
        $(".connectWith .item[data-value='" + connectWith_id + "']", modal).click();
    });
    edit_website.submit(function (e) {
        e.stopPropagation();
        e.preventDefault();
        var action = $(this).attr("action");
        var teams = [];
        if ($("input[name='team_id']", modal).val() !== "")
            teams = $("input[name='team_id']", modal).val().split(",").map(function (x) {
                return parseInt(x);
            });
        var connectWith = [];
        if ($("input[name='connectWith_id']", modal).val() !== "") {
            connectWith = $("input[name='connectWith_id']", modal).val().split(",").map(function (x) {
                return parseInt(x);
            });
        }
        var sso_id = $("input[name='sso_id']", modal).val();
        var category_id = parseInt($("input[name='category_id']", modal).val());
        ajaxHandler.post(action, {
            id: website.id,
            name: name.val(),
            landing_url: landing_url.val(),
            login_url: login_url.val(),
            folder: folder.val(),
            integrated: integrated.is(":checked"),
            teams: teams,
            sso_id: sso_id,
            category_id: category_id,
            connectWith: connectWith
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
            website.category_id = category_id;
            website.teams = teams;
            website.connectWith = connectWith;
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
                $("input[name='connectWith_id']", modal).val("");
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
    var modal = $("#team-settings");
    var input = $("#send-money", modal);
    $("#current-credit", modal).text(team.credit);
    $("#card-number span", modal).text(team.card_number);
    $("#link-number span", modal).text(team.link_number);
    $("#single-number span", modal).text(team.single_number);
    $("#enterprise-number span", modal).text(team.enterprise_number);
    $("#card-with-password-reminder span", modal).text(team.card_with_password_reminder);
    $("i", input).click(function (e) {
        if (input.hasClass("loading"))
            return;
        var credit = $("input", input).val();
        if (credit.indexOf(".") === -1) {
            credit = parseInt(credit) * 100;
        } else {
            var entirePart = credit.split(".")[0];
            var decimalPart = credit.split(".")[1];
            if (decimalPart.length === 1)
                decimalPart += "0";
            credit = parseInt(entirePart) * 100 + parseInt(decimalPart);
        }
        input.addClass("loading");
        input.addClass("disabled");
        ajaxHandler.post("/api/v1/admin/SendLoveMoney", {
            team_id: team.id,
            credit: credit
        }, function () {
        }, function (data) {
            $("#current-credit", modal).text(data.credit);
            $("input", input).val("");
            input.removeClass("loading");
            input.removeClass("disabled");
        });
    });
    $("button.negative", modal).click(function () {
        var button = $(this);
        button.addClass("loading");
        ajaxHandler.post("/api/v1/admin/DeleteTeam", {
            team_id: team.id
        }, function() {

        }, function() {
            button.removeClass("loading");
            teamRow.remove();
            modal.modal("hide");
        })
    })
    modal
        .modal({
            onHide: function () {
                $("i", input).off("click");
                $("input", input).val("");
            }
        })
        .modal("show");
}