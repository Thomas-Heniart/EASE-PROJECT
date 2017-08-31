var websites = [];

$(document).ready(function () {
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
        + '<td>' + website.id + '</td>'
        + '<td>' + website.name + '</td>'
        + '<td><img class="ui mini image" src="' + website.logo + '" /> </td>'
        + '<td>' + website.login_url + '</td>'
        + '<td>' + website.landing_url + '</td>'
        + '<td> <div class="ui checkbox public"><input type="checkbox"/><label></label></div></td>'
        + '<td><i class="fa fa-pencil"/></td>'
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
    return elem;
}