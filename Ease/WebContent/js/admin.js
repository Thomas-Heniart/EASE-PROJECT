$(document).ready(function() {
	/* Buttons behavior */
	$(".admin-menu button").click(openSideViewTab);
	$(".RightSideViewTab button .fa.fa-times").click(closeSideViewTab);

	/* Add Users behavior */
	$('#return').click(returnClick);
	$('#integrate').click(integrateUsers);

	/* Requested sites behavior */
	postHandler.post('requestedWebsites', {}, function() {
	}, function(retMsg) {
		printRequestedWebsites(retMsg);
	}, function(retMsg) {
	}, 'text');

	/* Tags manager behavior */
	$("#setTags").click(setTagsClick);
});

/* Interface functions */
function openSideViewTab(e) {
	$(".RightSideViewTab.show").removeClass("show");
	var target = $(e.target).closest("button").attr("target");
	console.log($("#" + target));
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

	for ( var email in emailsList) {
		var tab = emailsList[email].split(":");
		sendInvitation(tab[0], tab[1], function() {
			emailsSent++;
			$('#sent').text(emailsSent + "/" + emailsToSend + " emails sent.");
		});
	}
}

function sendInvitation(email, group_id, callback) {
	postHandler.post('createInvitation', {
		email : email,
		groupId : group_id
	}, function() {
	}, function(retMsg) {
		$('#progressStatus .progress').append("<p>" + email + " -> success");
		callback();
	}, function(retMsg) {
		$('#progressStatus .progress').append(
				"<p>" + email + " -> error : " + retMsg);
		callback();
	}, 'text');
}

/* Requested sites */

function printRequestedWebsites(string) {
	var requests = string.split(";");
	for ( var i in requests) {
		if (i > 0) {
			var email = requests[i].split("-SENTBY-")[1];
			var website = requests[i].split("-SENTBY-")[0];
			$('.requestedWebsitesView').append(
					"<div class='requestedWebsite' website='"
							+ requests[i].split("-SENTBY-")[0]
							+ "'><button class='quit'>X</button><p>Website : "
							+ requests[i].split("-SENTBY-")[0]
							+ " 		ASKED BY email : "
							+ requests[i].split("-SENTBY-")[1] + "</p></div>");
		}
	}
	$('.requestedWebsite .quit').click(eraseWebsites);
}

function eraseWebsites() {
	var div = $(this).parent();
	var toErase = div.attr('website');
	postHandler.post('eraseRequestedWebsite', {
		toErase : toErase
	}, function() {
	}, function(retMsg) {
		div.remove();
	}, function(retMsg) {
	}, 'text');
}

/* Tags functions */

function setTagsClick() {
	var tags = $('#tagsContainer div').map(function() {
		return $(this).attr("tagId");
	}).get();
	tags = JSON.stringify(tags);
	var websiteId = $("#websiteSelector").val();
	postHandler.post("setTags", {
		websiteId : websiteId,
		tagsId : tags
	}, function() {
	}, function(retMsg) {
		$('#setTags').val("Success");
		$('#setTags').prop('disabled', true);
		setTimeout(function() {
			$('#setTags').val("Validate");
			$('#setTags').prop('disabled', false);
		}, 1000);
	}, function(retMsg) {
		$('#setTags').val(retMsg);
		$('#setTags').prop('disabled', true);
		setTimeout(function() {
			$('#setTags').val("Validate");
			$('#setTags').prop('disabled', false);
		}, 1000);
	}, 'text');
}

function showFormTags() {

	var websiteId = $("#websiteSelector").val();
	$('#tagsContainer').empty();

	if (websiteId == 0) {
		$('#completeForm').attr("style", "display:none");

	} else {
		postHandler
				.post(
						"getTags",
						{
							websiteId : websiteId
						},
						function() {
							$('#completeForm').attr("style", "display:visible");
						},
						function(retMsg) {
							var tags = JSON.parse(retMsg);
							for ( var i in tags) {
								$("#tagsContainer")
										.append(
												'<div tagId="'
														+ tags[i].id
														+ '" class="btn-group tags-group" style="margin-left: 1px; margin-top: 3px;">'
														+ '<a href="#" class="tag btn btn-default"'
														+ 'style="background-color: '
														+ tags[i].color
														+ '; border-color: '
														+ tags[i].color
														+ '; color: white;">'
														+ tags[i].name
														+ '</a>'
														+ '<a href="#" onClick="deleteTag('
														+ tags[i].id
														+ ')" class="btn btn-default delete-tag">X</a>'
														+ '</div>');
							}
						}, function(retMsg) {
						}, 'text');
	}
}

function addTag() {
	var tagId = $("#tagSelector").val();
	if (tagId != 0) {
		if ($("#tagsContainer div[tagId=" + tagId + "]").length == 0) {

			var color = $("#tagSelector option[value=" + tagId + "]").attr(
					"tag-color");
			var name = $("#tagSelector option[value=" + tagId + "]").text();

			$("#tagsContainer")
					.append(
							'<div tagId="'
									+ tagId
									+ '" class="btn-group tags-group" style="margin-left: 1px; margin-top: 3px;">'
									+ '<a href="#" class="tag btn btn-default"'
									+ 'style="background-color: '
									+ color
									+ '; border-color: '
									+ color
									+ '; color: white;">'
									+ name
									+ '</a>'
									+ '<a href="#" onClick="deleteTag('
									+ tagId
									+ ')" class="btn btn-default delete-tag">X</a>'
									+ '</div>');
		}
	}
}

function deleteTag(tagId) {
	$("#completeForm div div[tagId=" + tagId + "]").remove();
}