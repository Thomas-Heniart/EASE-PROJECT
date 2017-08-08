currentAdminPopup = null;

$(document).ready(function () {

    $("#easePopupsHandler").click(function (e) {
        if ($(e.target).attr('id') == 'easePopupsHandler')
            currentAdminPopup.close();
    });

    /* Buttons behavior */
    $(".admin-menu button").click(openSideViewTab);
    $(".RightSideViewTab button .fa.fa-times").click(closeSideViewTab);

    /* Add Users behavior */
    $('#return').click(returnClick);
    $('#integrate').click(integrateUsers);

    $("#sendAllMails").click(function () {
        sendEmailsWebsiteIntegrated();
    });

    $("#cleanSavedSessions").click(cleanSavedSessions);
    $("#buttonTestWebsites").click(testWebsites);

    /* Site integration */
    $("#AddSiteTab #addSiteForm #addInfo").click(addWebsiteInfo);


    /* Move websites positions in catalog */
    $(".goTop").click(function () {
        changePositionForm.setPostName("goTop");
        changePositionForm.setSiteToMove($(this).parent());
    });
    $(".goDown").click(function () {
        changePositionForm.setPostName("goDown");
        changePositionForm.setSiteToMove($(this).parent());
    });
    $(".top").click(function () {
        changePositionForm.setPostName("top");
        changePositionForm.setSiteToMove($(this).parent());
    });
    $(".down").click(function () {
        changePositionForm.setPostName("down");
        changePositionForm.setSiteToMove($(this).parent());
    });
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

/* Add users */

function returnClick() {
    $(".addUsersProgress").addClass('hidden');
    $(".addUsers").removeClass('hidden');
    $('#progressStatus .progress p').remove();
}

function integrateUsers() {
    $(".addUsers").addClass('hidden');
    $('#progressStatus .progress').append("<p>Sending to database ... </p>");
    $(".addUsersProgress").removeClass('hidden');
    $('#progressStatus .progress').append(
        "<p id='sent'>" + emailsSent + "/" + emailsToSend
        + " emails sent. </p>");
    var form = $(this).closest('#addUsersForm');
    var emailsList = $(form).find('#integrateUsers').val().replace(/ /g, '')
        .split(";");

    var emailsToSend = emailsList.length;
    var emailsSent = 0;

    for (var email in emailsList) {
        var tab = emailsList[email].split(":");
        sendInvitation(tab[0], tab[1], function () {
            emailsSent++;
            $('#sent').text(emailsSent + "/" + emailsToSend + " emails sent.");
        });
    }
}

function sendInvitation(email, group_id, callback) {
    postHandler.post('createInvitation', {
        email: email,
        groupId: group_id
    }, function () {
    }, function (retMsg) {
        $('#progressStatus .progress').append("<p>" + email + " -> success : " + retMsg);
        callback();
    }, function (retMsg) {
        $('#progressStatus .progress').append(
            "<p>" + email + " -> error : " + retMsg);
        callback();
    }, 'text');
}

function cleanSavedSessions() {
    postHandler.post(
        "aspirateur",
        {},
        function () {
        },
        function (retMsg) {
            console.log(retMsg);
        },
        function (retMsg) {
            console.log(retMsg);
        },
        'text'
    );
}

function testWebsites() {
    postHandler.post(
        "testWebsites",
        {},
        function () {
        },
        function (retMsg) {
            var json = {};
            json.detail = JSON.parse(retMsg);
            event = new CustomEvent("MultipleTests", json);
            document.dispatchEvent(event);
        },
        function (retMsg) {
            console.log(retMsg);
        },
        'text'
    );
}

function addWebsiteInfo() {
    $("#AddSiteTab #addSiteForm #websiteInformations").append("<div class='websiteInfo'>"
        + "<input name='infoName' type='text' class='form-control' placeholder='Info name'/>"
        + "<input name='infoType' type='text' class='form-control' placeholder='Info type'/>"
        + "<input name='infoPlaceholder' type='text' class='form-control' placeholder='Placeholder'/>"
        + "<input name='infoPlaceholderIcon' type='text' class='form-control' placeholder='FA icon'/>"
        + "</div>");
}