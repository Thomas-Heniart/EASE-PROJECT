$(document).ready(function () {
    $(".ui.checkbox").checkbox();
    $(".ui.dropdown").dropdown();
    ajaxHandler.get("/api/v1/admin/GetTeams", null, function () {

    }, function (data) {
        data.forEach(function (team) {
            $("<div class='item' data-value='" + team.id + "'>" +
                team.name + "</div>").appendTo($("#website-edition .teams .menu"));

        });
    });
});