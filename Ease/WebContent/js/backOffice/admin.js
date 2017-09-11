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
            ajaxHandler.get("/api/v1/admin/GetTeamsInformation", null, function () {
            }, function (data) {
                data.forEach(function (team) {
                    addTeamRow(team).appendTo($("#team-manager-body"));
                });
                target.removeClass("loading");
            });
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
        "</tr>");
    if (!team.is_active)
        elem.addClass("negative");
    return elem;
}