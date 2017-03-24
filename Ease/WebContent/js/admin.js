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
	
	$("#cleanSavedSessions").click(cleanSavedSessions);
	$("#buttonTestWebsites").click(testWebsites);
	
	/* Site integration */
	$("#AddSiteTab #addSiteForm #addInfo").click(addWebsiteInfo);
	
	
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
		var websiteIntegrated = element.alreadyIntegrated ? "integrated" : "pending";
		$('.requestedWebsitesView').append("<div class='requestedWebsite' website='" 
				+ element.site 
				+ "' email='" 
				+ element.email 
				+ "'>" 
				+ (element.alreadyIntegrated ? "<input type='checkbox' class='sendEmail'/>" : "")
				+ "<button class='quit'>X</button><p class='"
				+ websiteIntegrated
				+ "'><span class='websiteUrl'>"
				+ element.site
				+ "</span> <span>("
				+ element.userName
				+ ", "
				+ element.date
				+ ")</span> <input style='display: none' class='editRequestUrl' type='text' placeholder='Set url' /></p></div>");
	});
	$('.requestedWebsite .quit').click(function(){
		eraseWebsite($(this).parent());
	});
	$(".requestedWebsite .pending .websiteUrl").click(function(){
		$(".editRequestUrl", $(this).parent()).show();
	});
	$(".editRequestUrl").keypress(function(e) {
		if (e.which == 13) {
			var newUrl = $(this).val();
			var self = $(this);
			console.log(newUrl);
			var oldUrl = $(this).parent().parent().attr("website");
			console.log(oldUrl);
			postHandler.post("EditRequestWebsiteUrl", {
				newUrl: newUrl,
				oldUrl: oldUrl
			}, function() {
				
			}, function(retMsg) {
				$(".requestedWebsite[website='" + oldUrl + "']").attr("website", newUrl);
				$(".requestedWebsite[website='" + newUrl + "'] .websiteUrl").text(newUrl);
				$(".requestedWebsite[website='" + newUrl + "'] p").removeClass("pending");
				if (retMsg == "integrated" && ! $(".requestedWebsite[website='" + newUrl + "'] p").hasClass("integrated")) {
					$(".requestedWebsite[website='" + newUrl + "']").prepend("<input type='checkbox' class='sendEmail'/>");
				}
				$(".requestedWebsite[website='" + newUrl + "'] p").addClass(retMsg);
				self.val("");
				self.hide();
			}, function(retMsg) {
				
			});
		}
	});
}

function eraseWebsite(div) {
	var toErase = div.attr('website');
	var email = div.attr("email");
	console.log(div)
	postHandler.post('EraseRequestedWebsite', {
		email: email,
		websiteUrl : toErase
	}, function() {
	}, function(retMsg) {
		$(".requestedWebsite[website='" + toErase + "'][email='" + email + "']").remove();
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
				{
					email:email, 
					websites:valuesToSend
				},
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

function addWebsiteInfo() {
	$("#AddSiteTab #addSiteForm #websiteInformations").append("<div class='websiteInfo'>"
						+ "<input name='infoName' type='text' class='form-control' placeholder='Info name'/>"
						+ "<input name='infoType' type='text' class='form-control' placeholder='Info type'/>"
						+ "<input name='infoPlaceholder' type='text' class='form-control' placeholder='Placeholder'/>"
						+ "<input name='infoPlaceholderIcon' type='text' class='form-control' placeholder='FA icon'/>"
					+ "</div>");
}