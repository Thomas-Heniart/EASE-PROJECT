currentAdminPopup = null;

$(document).ready(function () {

    $("#easePopupsHandler").click(function (e) {
        if ($(e.target).attr('id') === 'easePopupsHandler')
            currentAdminPopup.close();
    });

    /* Buttons behavior */
    $(".admin-menu button").click(openSideViewTab);
    $(".RightSideViewTab button .fa.fa-times").click(closeSideViewTab);
});

/* Interface functions */
function openSideViewTab(e) {
    $(".RightSideViewTab.show").removeClass("show");
    var target = $(e.target).closest("button").attr("target");
    $("#" + target).addClass("show");
}

function closeSideViewTab() {
    $(".RightSideViewTab.show").removeClass("show");
}