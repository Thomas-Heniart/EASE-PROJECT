var selected_teams = [];
var account_graph;
var people_graph;
var click_graph;
let website_obj = {};
let website_arr = [];
let onboarding_rooms_obj = {};

$(document).ready(function () {
  ajaxHandler.get("/api/v1/admin/GetPublicWebsites", null, () => {
  }, (res) => {
    website_arr = res;
    website_arr.forEach((website) => {
      website_obj[website.id] = website;
      $("<div class='item' data-value='" + website.id + "'>" +
        "<img class='ui avatar image' src='" + website.logo + "' />" + website.name + "</div>").appendTo($("#onboarding_rooms_add_website .menu"));
    })
  });
  $(".ui.checkbox").checkbox();
  $(".ui.dropdown").dropdown();
  $(".ui.accordion").accordion();
  $(".united.modal").modal({
    allowMultiple: true
  });
  $("#select-all-team").change(function () {
    var checked = $(this).is(":checked");
    if (!checked)
      $(".select-team:checked").click();
    else
      $(".select-team").each(function (i, e) {
        if (!$(e).is(":checked"))
          $(e).click()
      })
  });
  $("#website-requests-segment button").click(function () {
    var button = $(this);
    button.addClass("loading");
    var requests = $("#website-requests-manager-body tr.positive .checkbox .send_email:checked").map(function (i, elem) {
      var jElem = $(elem).parent().parent().parent();
      return {
        email: $(".email", jElem).text(),
        id: parseInt(jElem.attr("website-id")),
        request_id: parseInt($(".id", jElem).text())
      }
    });
    var all_requests = {};
    requests.toArray().forEach(function (request) {
      var existing_request = all_requests[request.email];
      if (existing_request === undefined)
        existing_request = {website_ids: [], request_ids: []};
      existing_request.website_ids.push(request.id);
      existing_request.request_ids.push(request.request_id);
      all_requests[request.email] = existing_request;
    });
    ajaxHandler.post("/api/v1/admin/SendWebsitesIntegrated", {
      emailAndWebsiteIds: all_requests
    }, function () {

    }, function () {
      button.removeClass("loading");
      $("#website-requests-manager-body tr.positive .checkbox .send_email:checked").parent().parent().parent().remove();
    });
    var password_needed_requests = $("#website-requests-manager-body tr.negative .checkbox .send_email:checked").map(function (i, elem) {
      var jElem = $(elem).parent().parent().parent();
      return {
        email: $(".email", jElem).text(),
        id: parseInt(jElem.attr("website-id"))
      }
    });
    var all_password_needed_requests = {};
    password_needed_requests.toArray().forEach((request) => {
      var existing_request = all_password_needed_requests[request.email];
      if (existing_request === undefined)
        existing_request = [];
      existing_request.push(request.id);
      all_password_needed_requests[request.email] = existing_request;
    });
    ajaxHandler.post("/api/v1/admin/SendPasswordNeededEmails", {
      emailAndWebsiteIds: all_password_needed_requests
    }, function () {

    }, function () {
      button.removeClass("loading");
      $("#website-requests-manager-body tr.negative .checkbox .send_email:checked").prop("checked", false)
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
  $("#admin_menu a.item").on("click", function () {
    $(this)
      .addClass("active")
      .siblings().removeClass("active");
    var target = $($(this).attr("data-target"));
    $(".backOffice_part").hide();
    $(".backOffice_part").addClass("loading");
    $(".backOffice_part table tbody tr").remove();
    target.show();
    if (target.hasClass("loading")) {
      switch (target.attr("id")) {
        case "team-segment":
          ajaxHandler.get("/api/v1/admin/GetTeamsInformation", null, function () {
          }, function (data) {
            data.forEach(function (team, index) {
              addTeamRow(team, index).appendTo($("#team-manager-body"));
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
            websites = data.sort(function (w1, w2) {
              return w2.id - w1.id;
            });
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
              return r2.date - r1.date;
            });
            requests.forEach(function (request) {
              createRequestRow(request).appendTo($("#website-requests-manager-body"));
            });
            target.removeClass("loading");
          });
          break;
        case "website-failures-segment":
          ajaxHandler.get("/api/v1/admin/GetWebsiteFailures", null, function () {

          }, function (websiteFailures) {
            websiteFailures.forEach(function (websiteFailure) {
              createWebsiteFailureRow(websiteFailure).appendTo($("#website-failures-body"));
            });
            target.removeClass("loading");
          });
          break;
        case "teams-deleted-segment":
          ajaxHandler.get("/api/v1/admin/TeamsDeleted", null, () => {
          }, function (websiteFailures) {
            websiteFailures.forEach(function (websiteFailure) {
              createWebsiteFailureRow(websiteFailure).appendTo($("#website-failures-body"));
            });
            target.removeClass("loading");
          });
          break;
        case "onboarding-rooms-segment":
          $("#onboarding_rooms_menu .room_item").remove();
          $("#onboarding_rooms_tabs .tab").remove();
          ajaxHandler.get("/api/v1/admin/OnboardingRooms", null, () => {
          }, (onboarding_rooms) => {
            onboarding_rooms_obj = {};
            onboarding_rooms.forEach((onboarding_room) => {
              onboarding_rooms_obj[onboarding_room.id] = onboarding_room;
              let menu_elem = $('<a class="item room_item" data-id="' + onboarding_room.id + '">' +
                '<i class="edit icon"></i> ' +
                '<i class="trash icon"></i>' +
                '<span>' + onboarding_room.name + '</span>' +
                '<div class="ui label">' + onboarding_room.website_ids.length + '</div>' +
                '</a>');
              menu_elem.appendTo($("#onboarding_rooms_menu"))
              let tab_elem = $('<div class="tab" data-id="' + onboarding_room.id + '" style="display: none"></div>');
              let plus_elem = $("<div class='ui tiny spaced image'>" +
                "<img class='ui spaced image' src='/resources/icons/plus.png'/>" +
                "</div>");
              plus_elem.appendTo(tab_elem);
              plus_elem.click(() => openAddOnboardingRoomModal(onboarding_room, $(".label", menu_elem), tab_elem));
              onboarding_room.website_ids.forEach((website_id) => {
                let website = website_obj[website_id];
                let elem = $('<div class="ui tiny spaced image">' +
                  '<img class="visible content" src="' + website.logo + '"/>' +
                  '</div>');
                elem.hover(() => $("img", elem).attr("src", "/resources/icons/delete.png"), () => $("img", elem).attr("src", website.logo));
                elem.click(() => {
                  onboarding_room.website_ids.splice(onboarding_room.website_ids.indexOf(website.id), 1);
                  ajaxHandler.put("/api/v1/admin/OnboardingRooms", onboarding_room, () => {
                  }, () => {
                    elem.remove();
                    let count = parseInt($(".label", menu_elem).text()) - 1;
                    $(".label", menu_elem).text(count)
                  })
                });
                elem.appendTo(tab_elem);
              });
              tab_elem.appendTo($("#onboarding_rooms_tabs"))
            });
            target.removeClass("loading");
          });
          break;
        default:
          target.removeClass("loading");
          break;
      }
    }
  });
  $("#multiple_team_graph").click(function () {
    if (selected_teams.length > 0)
      openManyTeams();
  });
  $("input", $("#background-upload")).change(() => {
    if ($("input", $("#background-upload")).filter((i, elem) => elem.value === "").length < 7)
      $("button", $("#background-upload")).removeClass("disabled");
    else
      $("button", $("#background-upload")).addClass("disabled");
  });
  $("#background-upload").submit(function (e) {
    e.preventDefault();
    let backgrounds = {};
    var self = $(this);
    var submit_button = $("button", self);
    submit_button.addClass("loading");
    submit_button.addClass("disabled");
    let files = $("input", self)
      .filter((i, elem) => elem.value !== "")
      .map((i, elem) => new Promise((resolve) => resolve(getBase64(elem.files[0]).then((b64) => backgrounds[elem.getAttribute("name")] = b64)))).toArray();
    Promise.all(files).then(() => {
      ajaxHandler.post("/api/v1/admin/UploadBackground", {
        backgrounds: backgrounds
      }, function () {
        submit_button.removeClass("loading");
      }, function () {
        $("input", self).val("");
      }, function (data) {
        alert(data)
      })
    }).catch((error) => console.log(error))
  });
  $("#teams_table").tablesort();
  $("#teams_table th.number_data").data('sortBy', (th, td, tablesort) => parseInt(td.text()));
  $("#add_onboarding_room").click((e) => {
    e.stopPropagation();
    let modal = $("#onboarding_rooms_edit");
    $("form", modal).submit((e) => {
      e.preventDefault();
      let name = $("form input[name='name']", modal).val();
      let example = $("form input[name='example']", modal).val();
      let onboarding_room = {
        name: name,
        example: example,
        website_ids: []
      };
      ajaxHandler.post("/api/v1/admin/OnboardingRooms", onboarding_room, () => {
      }, (data) => {
        onboarding_room = data;
        onboarding_rooms_obj[onboarding_room.id] = onboarding_room;
        let menu_elem = $('<a class="item room_item" data-id="' + onboarding_room.id + '">' +
          '<i class="edit icon"></i> ' +
          '<i class="trash icon"></i>' +
          '<span>' + onboarding_room.name + '</span>' +
          '<div class="ui label">' + onboarding_room.website_ids.length + '</div>' +
          '</a>');
        menu_elem.appendTo($("#onboarding_rooms_menu"));
        let tab_elem = $('<div class="tab" data-id="' + onboarding_room.id + '" style="display: none"></div>');
        let plus_elem = $("<div class='ui tiny spaced image'>" +
          "<img class='ui spaced image' src='/resources/icons/plus.png'/>" +
          "</div>");
        plus_elem.appendTo(tab_elem);
        plus_elem.click(() => openAddOnboardingRoomModal(onboarding_room, $(".label", menu_elem), tab_elem));
        onboarding_room.website_ids.forEach((website_id) => {
          let website = website_obj[website_id];
          let elem = $('<div class="ui tiny spaced image">' +
            '<img class="visible content" src="' + website.logo + '"/>' +
            '</div>');
          elem.hover(() => $("img", elem).attr("src", "/resources/icons/delete.png"), () => $("img", elem).attr("src", website.logo));
          elem.click(() => {
            onboarding_room.website_ids.splice(onboarding_room.website_ids.indexOf(website.id), 1);
            ajaxHandler.put("/api/v1/admin/OnboardingRooms", onboarding_room, () => {
            }, () => {
              elem.remove();
              let count = parseInt($(".label", menu_elem).text()) - 1;
              $(".label", menu_elem).text(count)
            })
          });
          elem.appendTo(tab_elem);
        });
        tab_elem.appendTo($("#onboarding_rooms_tabs"));
        menu_elem.click();
        modal.modal("hide")
      })
    });
    modal
      .modal({
        onHide: function () {
          $("form", modal).off("submit");
        }
      })
      .modal("show");
  });
  $("#onboarding_rooms_menu").on('click', '.room_item .trash.icon', (e) => {
    e.stopPropagation();
    let modal = $("#onboarding_rooms_delete");
    let self = $(e.target);
    let id = self.parent().attr('data-id');
    $(".ok", modal).click(() => {
      $(".ok", modal).addClass("loading");
      $(".ok", modal).addClass("disabled");
      ajaxHandler.delete("/api/v1/admin/OnboardingRooms?id=" + id, null, () => {
      }, () => {
        $("#onboarding_rooms_tabs .tab[data-id='" + id + "']").remove();
        $("#onboarding_rooms_menu .room_item[data-id='" + id + "']").remove();
        modal.modal("hide");
      })
    });
    modal
      .modal({
        onHide: function () {
          $(".ok", modal).removeClass("loading");
          $(".ok", modal).removeClass("disabled");
          $(".ok", modal).off("click");
        }
      })
      .modal("show");
  });
  $("#onboarding_rooms_menu").on('click', '.room_item .edit.icon', (e) => {
    e.stopPropagation();
    let modal = $("#onboarding_rooms_edit");
    let self = $(e.target);
    let onboarding_room = onboarding_rooms_obj[self.parent().attr('data-id')];
    $("form input[name='name']", modal).val(onboarding_room.name);
    $("form input[name='example']", modal).val(onboarding_room.example);
    $("form", modal).submit((e) => {
      e.preventDefault();
      let name = $("form input[name='name']", modal).val();
      let example = $("form input[name='example']", modal).val();
      onboarding_room.name = name;
      onboarding_room.example = example;
      ajaxHandler.put("/api/v1/admin/OnboardingRooms", onboarding_room, () => {
      }, () => {
        $("span", self.parent()).text(name);
        modal.modal("hide")
      })
    });
    modal
      .modal({
        onHide: function () {
          $("form", modal).off("submit");
        }
      })
      .modal("show");
  });
  $("#onboarding_rooms_menu").on('click', '.room_item span', (e) => {
    e.stopPropagation();
    let self = $(e.target).parent();
    let onboarding_room = onboarding_rooms_obj[self.attr('data-id')];
    $("#onboarding_rooms_tabs .tab").hide();
    $("#onboarding_rooms_menu .item").removeClass("teal");
    $("#onboarding_rooms_menu .item").removeClass("active");
    self.addClass("teal");
    self.addClass("active");
    let tab = $("#onboarding_rooms_tabs .tab[data-id='" + onboarding_room.id + "']");
    tab.show();
  });
  $("#onboarding_rooms_menu").on('click', '.room_item', (e) => {
    let self = $(e.target);
    let onboarding_room = onboarding_rooms_obj[self.attr('data-id')];
    $("#onboarding_rooms_tabs .tab").hide();
    $("#onboarding_rooms_menu .item").removeClass("teal");
    $("#onboarding_rooms_menu .item").removeClass("active");
    self.addClass("teal");
    self.addClass("active");
    let tab = $("#onboarding_rooms_tabs .tab[data-id='" + onboarding_room.id + "']");
    tab.show();
  });
});

function createWebsiteFailureRow(websiteFailure) {
  var elem = $("<tr>" +
    "<td class='count'>" +
    websiteFailure.count +
    "</td>" +
    "<td class='url'>" +
    websiteFailure.url +
    "</td>" +
    "<td><a href='#' class='delete'><i class='fa fa-trash'></i></a></td>" +
    "</tr>");
  $("a.delete", elem).click(function () {
    $("a.delete", elem).click(function () {
      var modal = $("#website-failure-delete");
      var button = $(".ok", modal);
      button.click(function () {
        button.addClass("loading");
        ajaxHandler.post("/api/v1/admin/DeleteWebsiteFailure", {
          url: websiteFailure.url
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

function createRequestRow(request) {
  var elem = $("<tr website-id='" + request.website_id + "'>" +
    "<td class='id'>" + request.id + "</td>" +
    "<td class='url'>" + request.url + "</td>" +
    "<td class='email'>" + request.email + "</td>" +
    "<td class='date'>" + new Date(request.date).toLocaleString() + "</a></td>" +
    '<td><div class="ui read-only checkbox"><input class="credentials" type="checkbox" disabled/><label></label></div></td>' +
    '<td><div class="ui checkbox"><input class="send_email" type="checkbox"/><label></label></div></td>' +
    "<td><a href='#' class='delete'><i class='fa fa-trash'></i></a></td>" +
    "</tr>");
  if (request.integrated)
    elem.addClass("positive");
  else
    elem.addClass("negative");
  if (request.credentials) {
    $(".read-only", elem).addClass("active");
    $(".credentials", elem).prop("checked", true);
  }
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

function addTeamRow(team, index) {
  var elem = $("<tr>" +
    "<td>" + (index + 1) + "</td>" +
    '<td><div class="ui checkbox"><input class="select-team" type="checkbox"/><label></label></div></td>' +
    "<td>" + team.name + "</td>" +
    "<td>" + team.admin_first_name + " " + team.admin_last_name + "</td>" +
    "<td>" + team.admin_email + "</td>" +
    "<td>" + team.week_of_subscription + "</td>" +
    "<td>" + ((team.plan_id === 0) ? "Free" : "Pro") + "</td>" +
    "<td>" + team.card_entered + "</td>" +
    "<td>" + team.cards_with_tags + "</td>" +
    "<td>" + team.people_joined + "</td>" +
    "<td>" + team.people_joined_with_cards + "</td>" +
    "<td>" + team.people_click_on_app_once + "</td>" +
    "<td>" + team.people_click_on_app_three_times + "</td>" +
    "</tr>");
  $(".select-team", elem).click(function (e) {
    e.stopPropagation();
    if ($(this).is(":checked"))
      selected_teams.push(team.id);
    else
      selected_teams.splice(selected_teams.indexOf(team.id), 1);
  });
  elem.click(function () {
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
  if (!website.integrated) {
    elem.removeClass("positive");
    elem.addClass("negative");
  }
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
  var website_upload = $("#website-upload", modal);
  var name = $("input[name='name']", edit_website);
  var login_url = $("input[name='login_url']", edit_website);
  var landing_url = $("input[name='landing_url']", edit_website);
  var folder = $("input[name='folder']", edit_website);
  var integrated = $("#integration input[name='integrate']", edit_website);
  var alternative_urls = $("#alternative_urls", edit_website);
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
  var alternative_urls_content = $(".content .inputs", alternative_urls);
  website.alternative_urls.forEach((alternative_url) => {
    var elem = $("<div class='ui field'>" +
      "<div class='ui icon input'>" +
      "<input type='url' value='" + alternative_url.url + "' alternative_url_id='" + alternative_url.id + "' />" +
      "<i class='inverted circular close link icon'></i>" +
      "</div>" +
      "</div>");
    alternative_urls_content.prepend(elem);
    $(".close", elem).click(() => elem.remove());
  });
  var add_alternative_url = $("button", alternative_urls);
  add_alternative_url.click((e) => {
    e.preventDefault();
    var elem = $("<div class='ui field'>" +
      "<div class='ui icon input'>" +
      "<input type='url' />" +
      "<i class='inverted circular close link icon'></i>" +
      "</div>" +
      "</div>");
    alternative_urls_content.append(elem);
    $(".close", elem).click(() => elem.remove());
  });
  $("#alternative_urls_number", alternative_urls).text(website.alternative_urls.length);
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
    var new_alternative_urls = [];
    $("input", alternative_urls_content).each((i, elem) => {
      var jElem = $(elem);
      new_alternative_urls.push(jElem.val());
    });
    var add_alternative_url = $("button", alternative_urls);
    add_alternative_url.click((e) => {
      e.preventDefault();
      var elem = $("<div class='ui field'>" +
        "<div class='ui icon input'>" +
        "<input type='url' />" +
        "<i class='inverted circular close link icon'></i>" +
        "</div>" +
        "</div>");
      alternative_urls_content.append(elem);
      $(".close", elem).click(() => elem.remove());
    });
    $("#alternative_urls_number", alternative_urls).text(website.alternative_urls.length);
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
      connectWith: connectWith,
      alternative_urls: new_alternative_urls
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
      if (!website.integrated) {
        websiteElem.removeClass("positive");
        websiteElem.addClass("negative");
      } else {
        websiteElem.removeClass("negative");
        websiteElem.addClass("positive");
      }
      $(".name", websiteElem).text(website.name);
      $(".landing_url", websiteElem).text(website.landing_url);
      $(".login_url", websiteElem).text(website.login_url);
      $(".logo img", websiteElem).attr("src", "/resources/websites/" + website.folder + "/logo.png");
      integrated.prop("checked", false);
    });
    add_alternative_url.off("click");
    edit_website.off("submit");
    modal.modal("hide");
  });
  /* website_upload.submit(function(e) {
      e.preventDefault();
  }); */
  modal
    .modal({
      onHide: function () {
        edit_website.off("submit");
        //website_upload.off("submit");
        $("input[name='team_id']", modal).val("");
        $("input[name='connectWith_id']", modal).val("");
        $("a.ui.label.transition", modal).remove();
        $(".item.active")
          .removeClass("active")
          .removeClass("filtered");
        $("input.search").val("");
        $(".field", alternative_urls_content).remove();
      }
    })
    .modal("show");
}

function openAddOnboardingRoomModal(onboarding_room, onboarding_room_menu_item, onboarding_room_tab) {
  let modal = $("#onboarding_rooms_add_website");
  $(".form button", modal).click(() => {
    let website_id = $("input[name='website_id']", modal).val();
    if (onboarding_room.website_ids.indexOf(website_id) !== -1)
      return;
    onboarding_room.website_ids.push(website_id);
    ajaxHandler.put("/api/v1/admin/OnboardingRooms", onboarding_room, () => {
    }, () => {
      let website = website_obj[website_id];
      let elem = $('<div class="ui tiny spaced image">' +
        '<img class="visible content" src="' + website.logo + '"/>' +
        '</div>');
      elem.hover(() => $("img", elem).attr("src", "/resources/icons/delete.png"), () => $("img", elem).attr("src", website.logo));
      elem.click(() => {
        onboarding_room.website_ids.splice(onboarding_room.website_ids.indexOf(website.id), 1);
        ajaxHandler.put("/api/v1/admin/OnboardingRooms", onboarding_room, () => {
        }, () => {
          elem.remove();
          let count = parseInt($(".label", menu_elem).text()) - 1;
          $(".label", menu_elem).text(count)
        })
      });
      elem.appendTo(onboarding_room_tab);
      let count = parseInt(onboarding_room_menu_item.text()) + 1;
      onboarding_room_menu_item.text(count);
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

function openManyTeams() {
  var modal = $("#team-settings");
  var people_data = $("#people_data");
  var account_data = $("#account_data");
  var data_emails = $("#people_data_emails");
  var account_names = $("#account_data_names");
  var people_data_history = $("#people_data_history");
  var account_data_hisotry = $("#account_data_history");
  var team_settings_right = $("#team_settings_right");
  var team_settings_left = $("#team_settings_left");
  var click_average_graphic = $("#click_average_graphic");
  var show_graphic = $("#show_graphic");
  $("#credit").hide();
  $("#show_delete").hide();
  $(".header", modal).text("Many teams selected");
  var teams_data = undefined;
  selected_teams.forEach(function (team_id) {
    ajaxHandler.get("/api/v1/admin/GetTeam", {
      team_id: team_id
    }, function () {
    }, function (data) {
      if (teams_data === undefined)
        teams_data = data;
      else {
        for (var key in data) {
          if (data.hasOwnProperty(key)) {
            if (typeof data[key] === "string")
              teams_data[key] += (";" + data[key]);
            else
              teams_data[key] += data[key];
          }
        }
      }
    }, function () {
    }, false)
  });
  modal
    .modal({
      onHide: function () {
        $(".segment", modal).addClass("loading");
        $("button", people_data).off("click");
        $("button", account_data).off("click");
        show_graphic.off("click");
        data_emails.hide();
        account_names.hide();
        people_data_history.hide();
        account_data_hisotry.hide();
        click_average_graphic.hide();
        team_settings_left.show();
        team_settings_right.show();
        account_data.show();
        people_data.show();
        $("#credit").css("display", "inline");
        $("#show_delete").show();
        show_graphic.off("click");
      }
    })
    .modal("show");
  $(".segment", modal).removeClass("loading");
  people_data.find("span").each(function (index, element) {
    $(element).text(teams_data[element.id]);
    var anchor = $(element).next();
    anchor.click(function (e) {
      e.preventDefault();
      account_data.hide();
      var list = $("ul", data_emails);
      $("li", list).remove();
      teams_data[anchor.attr("id")].split(";").forEach(function (email) {
        $("<li>" + email + "</li>").appendTo(list);
      });
      data_emails.show();
      $("button", data_emails).click(function () {
        data_emails.hide();
        $("li", list).remove();
        $(this).off("click");
        account_data.show();
      })
    });
  });
  people_data.find("button").click(function () {
    account_data.hide();
    team_settings_right.addClass("loading");
    people_data_history.show();
    ajaxHandler.post("/api/v1/admin/RetrieveTeamsPeopleChartData", {
      team_ids: selected_teams
    }, function () {
    }, function (data) {
      team_settings_right.removeClass("loading");
      var ctx = document.getElementById("people_data_chart").getContext("2d");
      if (people_graph === undefined)
        people_graph = new Chart(ctx, data);
      else {
        people_graph.destroy();
        people_graph = new Chart(ctx, data);
      }
    });
    $("button", $("#people_data_history")).click(function () {
      people_data_history.hide();
      $(this).off("click");
      $("#account_data").show();
    })
  });
  account_data.find("button").click(function () {
    people_data.hide();
    click_average_graphic.hide();
    team_settings_left.addClass("loading");
    account_data_hisotry.show();
    ajaxHandler.post("/api/v1/admin/RetrieveTeamsAccountChartData", {
      team_ids: selected_teams
    }, function () {
    }, function (data) {
      team_settings_left.removeClass("loading");
      var ctx = $("#account_data_chart");
      if (account_graph === undefined)
        account_graph = new Chart(ctx, data);
      else {
        account_graph.destroy();
        account_graph = new Chart(ctx, data);
      }
    });
    $("button", account_data_hisotry).click(function () {
      account_data_hisotry.hide();
      $(this).off("click");
      people_data.show();
    })
  });
  account_data.find("span").each(function (index, element) {
    $(element).text(teams_data[element.id]);
    var anchor = $(element).next();
    anchor.click(function (e) {
      e.preventDefault();
      people_data.hide();
      var list = $("ul", account_names);
      $("li", list).remove();
      teams_data[anchor.attr("id")].split(";").forEach(function (email) {
        $("<li>" + email + "</li>").appendTo(list);
      });
      account_names.show();
      $("button", account_names).click(function () {
        account_names.hide();
        $("li", list).remove();
        $(this).off("click");
        people_data.show();
      })
    });
  });
  show_graphic.click(function () {
    team_settings_right.hide();
    team_settings_left.show();
    team_settings_left.addClass("loading");
    people_data.hide();
    click_average_graphic.show();
    ajaxHandler.post("/api/v1/admin/RetrieveTeamsClickChartData", {
      team_ids: selected_teams
    }, function () {
    }, function (data) {
      team_settings_left.removeClass("loading");
      var canvas = $("#click_average_canvas");
      if (click_graph === undefined)
        click_graph = new Chart(canvas, data);
      else {
        click_graph.destroy();
        click_graph = new Chart(canvas, data);
      }
    });
    $("button", click_average_graphic).click(function () {
      team_settings_right.show();
      people_data.show();
      click_average_graphic.hide();
      $(this).off("click");
    });
  })
}

function openTeamSettings(team, teamRow) {
  var modal = $("#team-settings");
  var send_money = $("#send-money", modal);
  var people_data = $("#people_data");
  var account_data = $("#account_data");
  var data_emails = $("#people_data_emails");
  var account_names = $("#account_data_names");
  var people_data_history = $("#people_data_history");
  var account_data_hisotry = $("#account_data_history");
  var team_settings_right = $("#team_settings_right");
  var team_settings_left = $("#team_settings_left");
  var click_average_graphic = $("#click_average_graphic");
  var show_graphic = $("#show_graphic");
  var delete_button = $("#show_delete");
  var delete_modal = $("#team_delete");
  var delete_modal_button = $("div.button.red", delete_modal);
  delete_modal_button.off("click");
  delete_modal_button.click(function () {
    $(this).addClass("loading");
    ajaxHandler.post("/api/v1/admin/DeleteTeam", {
      team_id: team.id
    }, function () {
    }, function () {
      delete_modal_button.removeClass("loading");
      delete_modal.modal("hide");
      teamRow.remove();
    })
  });
  $(".header", modal).text(team.name + " | " + team.admin_email + " " + team.phone_number + " | week of subscription: " + team.week_of_subscription + " | current week: " + team.week_now + " | " + ((team.plan_id === 0) ? "Free" : "Pro") + " | " + (team.card_entered ? "Card entered" : "No card"));
  modal
    .modal({
      onHide: function () {
        $(".segment", modal).addClass("loading");
        $("i", send_money).off("click");
        $("input", send_money).val("");
        $("button", people_data).off("click");
        $("button", account_data).off("click");
        delete_button.off("click");
        show_graphic.off("click");
        data_emails.hide();
        account_names.hide();
        people_data_history.hide();
        account_data_hisotry.hide();
        click_average_graphic.hide();
        team_settings_left.show();
        team_settings_right.show();
        account_data.show();
        people_data.show();
      }
    })
    .modal("show");
  ajaxHandler.get("/api/v1/admin/GetTeam", {
    team_id: team.id
  }, function () {
  }, function (data) {
    $(".segment", modal).removeClass("loading");
    people_data.find("span").each(function (index, element) {
      $(element).text(data[element.id]);
      var anchor = $(element).next();
      anchor.click(function (e) {
        e.preventDefault();
        account_data.hide();
        var list = $("ul", data_emails);
        $("li", list).remove();
        data[anchor.attr("id")].split(";").forEach(function (email) {
          $("<li>" + email + "</li>").appendTo(list);
        });
        data_emails.show();
        $("button", data_emails).click(function () {
          data_emails.hide();
          $("li", list).remove();
          $(this).off("click");
          $("#account_data").show();
        })
      });
    });
    people_data.find("button").click(function () {
      account_data.hide();
      team_settings_right.addClass("loading");
      people_data_history.show();
      ajaxHandler.get("/api/v1/admin/GetPeopleChartData", {
        team_id: team.id
      }, function () {
      }, function (data) {
        team_settings_right.removeClass("loading");
        var ctx = document.getElementById("people_data_chart").getContext("2d");
        if (people_graph === undefined)
          people_graph = new Chart(ctx, data);
        else {
          people_graph.destroy();
          people_graph = new Chart(ctx, data);
        }
      });
      $("button", $("#people_data_history")).click(function () {
        people_data_history.hide();
        $(this).off("click");
        $("#account_data").show();
      })
    });
    account_data.find("button").click(function () {
      people_data.hide();
      click_average_graphic.hide();
      team_settings_left.addClass("loading");
      account_data_hisotry.show();
      ajaxHandler.get("/api/v1/admin/GetAccountChartData", {
        team_id: team.id
      }, function () {
      }, function (data) {
        team_settings_left.removeClass("loading");
        var ctx = $("#account_data_chart");
        if (account_graph === undefined)
          account_graph = new Chart(ctx, data);
        else {
          account_graph.destroy();
          account_graph = new Chart(ctx, data);
        }
      });
      $("button", account_data_hisotry).click(function () {
        account_data_hisotry.hide();
        $(this).off("click");
        people_data.show();
      })
    });
    account_data.find("span").each(function (index, element) {
      $(element).text(data[element.id]);
      var anchor = $(element).next();
      anchor.click(function (e) {
        e.preventDefault();
        people_data.hide();
        var list = $("ul", account_names);
        $("li", list).remove();
        data[anchor.attr("id")].split(";").forEach(function (email) {
          $("<li>" + email + "</li>").appendTo(list);
        });
        account_names.show();
        $("button", account_names).click(function () {
          account_names.hide();
          $("li", list).remove();
          $(this).off("click");
          people_data.show();
        })
      });
    });
    $("#current-credit", modal).text(team.credit);
    $("i", send_money).click(function () {
      if (send_money.hasClass("loading"))
        return;
      var credit = $("input", send_money).val();
      if (credit.indexOf(".") === -1) {
        credit = parseInt(credit) * 100;
      } else {
        var entirePart = credit.split(".")[0];
        var decimalPart = credit.split(".")[1];
        if (decimalPart.length === 1)
          decimalPart += "0";
        credit = parseInt(entirePart) * 100 + parseInt(decimalPart);
      }
      send_money.addClass("loading");
      send_money.addClass("disabled");
      ajaxHandler.post("/api/v1/admin/SendLoveMoney", {
        team_id: team.id,
        credit: credit
      }, function () {
      }, function (data) {
        $("#current-credit", modal).text(data.credit);
        $("input", send_money).val("");
        send_money.removeClass("loading");
        send_money.removeClass("disabled");
      });
    });
    show_graphic.click(function () {
      team_settings_right.hide();
      team_settings_left.show();
      team_settings_left.addClass("loading");
      people_data.hide();
      click_average_graphic.show();
      ajaxHandler.get("/api/v1/teams/GetTeamClickChartData", {
        team_id: team.id
      }, function () {
      }, function (data) {
        team_settings_left.removeClass("loading");
        var canvas = $("#click_average_canvas");
        if (click_graph === undefined)
          click_graph = new Chart(canvas, data);
        else {
          click_graph.destroy();
          click_graph = new Chart(canvas, data);
        }
        $("body").css({
          "overlfow-y": "auto"
        });
      });
      $("button", click_average_graphic).click(function () {
        team_settings_right.show();
        people_data.show();
        click_average_graphic.hide();
        $(this).off("click");
      });
    });
    delete_button.click(function () {
      delete_modal.modal("show");
    });
  });
}

function getBase64(file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => resolve(reader.result.slice(reader.result.indexOf(",") + 1));
    reader.onerror = (error) => reject(error);
  });
}

populateTeamsDeleted = (data) => {
  let tableBody = $("#teams-deleted-body");
  data.forEach((elem, index) => {
    let jElem = $("<tr>" +
      "<td>" + (index + 1) + "</td>" +
      "<td>" + elem.name + "</td>" +
      "<td>" + elem.email + "</td>" +
      "<td>" + elem.phone_number + "</td>" +
      "</tr>");
    jElem.appendTo(tableBody);
  });
};