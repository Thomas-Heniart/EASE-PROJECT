$(document).ready(function() {
	/* Buttons behavior */
	$(".admin-menu button").click(openSideViewTab);
	$(".RightSideViewTab button .fa.fa-times").click(closeSideViewTab);

	/* Add Users behavior */
	$('#return').click(returnClick);
	$('#integrate').click(integrateUsers);

	/* Requested sites behavior */
	$("#enterRequestedWebsitesMode").click(function(){
		getRequestedSites();
	});
	
	$("#sendAllMails").click(function(){
		sendEmailsWebsiteIntegrated();
	});

	/* Tags manager behavior */
	$("#setTags").click(setTagsClick);
	
	$("#cleanSavedSessions").click(cleanSavedSessions);
	$("#buttonTestWebsites").click(testWebsites);
	/* Move websites positions in catalog */
	$(".goTop").click(function() {
		changePositionForm.setPostName("goTop");
		changePositionForm.setSiteToMove($(this).parent());
	});
	$(".goDown").click(function() {
		changePositionForm.setPostName("goDown");
		changePositionForm.setSiteToMove($(this).parent());
	});
	$(".top").click(function() {
		changePositionForm.setPostName("top");
		changePositionForm.setSiteToMove($(this).parent());
	});
	$(".down").click(function() {
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
		$('#progressStatus .progress').append("<p>" + email + " -> success : "+retMsg);
		callback();
	}, function(retMsg) {
		$('#progressStatus .progress').append(
				"<p>" + email + " -> error : " + retMsg);
		callback();
	}, 'text');
}

/* Requested sites */

function getRequestedSites() {
	$.get("WebsiteRequest").done(function(retMsg) {
		printRequestedWebsites(retMsg.substring(4, retMsg.length));
	});
}

function printRequestedWebsites(string) {
	var json = JSON.parse(string);
	json.forEach(function(element) {
		$('.requestedWebsitesView').append("<div class='requestedWebsite' website='" 
				+ element.site 
				+ "' email='" 
				+ element.email 
				+ "'><input type='checkbox' class='sendEmail'/><button class='quit'>X</button><p>"
				+ element.site
				+ " ("
				+ element.userName
				+ ", "
				+ element.date
				+ ")</p></div>");
	});
	$('.requestedWebsite .quit').click(function(){
		eraseWebsite($(this));
	});
}

function eraseWebsite(div) {
	var toErase = div.attr('website');
	var email = div.attr("email");
	postHandler.post('eraseRequestedWebsite', {
		email: email,
		toErase : toErase
	}, function() {
	}, function(retMsg) {
		div.remove();
	}, function(retMsg) {
	}, 'text');
}

function sendEmailsWebsiteIntegrated(){
	var toSend = {};
	$('.requestedWebsite .sendEmail').each(function(){
		if($(this).is(':checked')){
			var email = $(this).parent(".requestedWebsite").attr("email");
			if(!toSend[email])
				toSend[email]=[];
			toSend[email].push($(this).parent(".requestedWebsite").attr("website"));
		}
	});
	$("#PopupSendEmailWebsite").addClass("md-show");
	var emails=[];
	for (var email in toSend){
		emails.push(email);
	}
	nextPopup(0, emails, toSend);
	
	function nextPopup(i, emails, toSend){
		$("#PopupSendEmailWebsite #accept").unbind("click");
		$("#PopupSendEmailWebsite #close").unbind("click");
		$("#PopupSendEmailWebsite .md-content input").remove();
		var email = emails[i];
		var websites = toSend[email];
		$("#PopupSendEmailWebsite .title").text("Send an email to "+email+" for these websites ?");
		for(var webIndex in websites){
			$("<input value='"+websites[webIndex]+"'></input>").insertBefore("#PopupSendEmailWebsite .md-content .buttonSet");
		}
		$("#PopupSendEmailWebsite #close").click(function(e){
			if(emails[i+1])
				nextPopup(i+1, emails, toSend)
			else
				$("#PopupSendEmailWebsite").removeClass("md-show");
		});
		$("#PopupSendEmailWebsite #accept").click(function(e){
			var values = [];
			$("#PopupSendEmailWebsite .md-content input").each(function(){
				values.push($(this).val());
			});
			sendEmails(email, values, websites);
			if(emails[i+1])
				nextPopup(i+1, emails, toSend)
			else
				$("#PopupSendEmailWebsite").removeClass("md-show");
		});
	}
	
	function sendEmails(email, values, initialValues){
		var valuesToSend=JSON.stringify(values);
		postHandler.post(
				"SendWebsitesIntegrated",
				{email:email, websites:values},
				function(){},
				function(retMsg){
					$(".requestedWebsite").each(function(){
						if($(this).attr("email")==email && initialValues.indexOf($(this).attr("website"))!=-1)
							eraseWebsite($(this));
					});
				},
				function(retMsg){
					console.log("Could not send email to "+email+", message : retMsg");
				},
				'text'
		);
	}
}

/* Tags functions */

function setTagsClick() {
	var tags = $('#tagsContainer div').map(function() {
		return $(this).attr("tagId");
	}).get();
	tags = JSON.stringify(tags);
	var websiteId = $("#websiteSelector").val();
	postHandler.post("SetWebsiteTags", {
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
						"GetWebsiteTags",
						{
							websiteId : websiteId
						},
						function() {
							$('#completeForm').attr("style", "display:visible");
						},
						function(retMsg) {
							var tags = JSON.parse(retMsg);
							tags.forEach(function(tag) {
								$("#tagsContainer")
								.append(
										'<div tagId="'
												+ tag.singleId
												+ '" class="btn-group tags-group" style="margin-left: 1px; margin-top: 3px;">'
												+ '<a href="#" class="tag btn btn-default"'
												+ 'style="background-color: '
												+ tag.color
												+ '; border-color: '
												+ tag.color
												+ '; color: white;">'
												+ tag.name
												+ '</a>'
												+ '<a href="#" onClick="deleteTag('
												+ tag.singleId
												+ ')" class="btn btn-default delete-tag">X</a>'
												+ '</div>');
							});
						}, function(retMsg) {
						}, 'text');
	}
}

function addTag() {
	var websiteId = $("#websiteSelector").val();
	var tagId = $("#tagSelector").val()
	postHandler.post("AddWebsiteTag", {
		websiteId: websiteId,
		tagId: tagId
	}, function() {
		
	}, function(data) {
		var color = $("#tagSelector option[value=" + tagId + "]").attr("tag-color");
		var name = $("#tagSelector option[value=" + tagId + "]").text();
		$("#tagsContainer").append(
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
	}, function(data) {
		
	});
}

function deleteTag(tagId) {
	var websiteId = $("#websiteSelector").val();
	postHandler.post("RemoveWebsiteTag", {
		tagId: tagId,
		websiteId: websiteId
	}, function() {
		
	}, function(data) {
		$("#completeForm div div[tagId=" + tagId + "]").remove();
	}, function(data) {
		
	});
}

function cleanSavedSessions(){
	postHandler.post(
		"aspirateur",
		{},
		function(){},
		function(retMsg){
			console.log(retMsg);
		},
		function(retMsg){
			console.log(retMsg);
		},
		'text'
	);
}

function testWebsites(){
	postHandler.post(
		"testWebsites",
		{},
		function(){},
		function(retMsg){
			console.log(retMsg);
			var json = {};
			json.detail = JSON.parse(retMsg);
			event = new CustomEvent("MultipleTests", json);
    		document.dispatchEvent(event);
		},
		function(retMsg){
			console.log(retMsg);
		},
		'text'
	);
}