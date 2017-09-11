$(document).ready(function () {
    $(".ui.checkbox").checkbox();
    $(".ui.dropdown").dropdown();
    ajaxHandler.get("/api/v1/admin/GetTeamsInformation", null, function () {

    }, function (data) {
        data.forEach(function (team) {
            addRow(team).appendTo($("#team-manager-body"));

        });
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