var scrapping = [];
var appToAdd = [];
var jsonscrap = {};
var scrappingFinished = [];

function displayTutoApps(apps, by) {
	apps.forEach(function (element) {
		$('div#saving div.scrapedAppsContainer').append(
				"<div class='appHandler'>" +
					"<div class='app selected' index=" + appToAdd.length + ">" +
						"<div class='logo'>" +
							"<img src='" + element.img + "'/>" +
							"<img class='by' src='" + by.img + "'>" +
						"</div>" +
						"<p class='name'>" + element.name + "</p>" +
						"<p class='login'>" + ((element.login) ? element.login : "") + "</p>" +
					"</div>" +
				"</div>");
		by.profileId = element.profileId;
		appToAdd.push(element);
	});
}

function showSavingPopup(filterJson) {
	scrappingFinished.forEach(function(element) {
		if (element.websiteId)
			appToAdd.push(element);
		displayTutoApps(filterJson[element.id], element);
	});
	$('div#saving div.scrapedAppsContainer div.appHandler').click(function() {
		if ($(this).find("div.app").hasClass("selected")) {
			$(this).find("div.app").removeClass("selected");
		} else {
			$(this).find("div.app").addClass("selected");
		}
	});
	$('#accountCredentials').removeClass("show");
	$('div#importation').removeClass("show");
	$('div#saving').addClass("show");
}

function addTutoApps(logwithId, i) {
	if (i >= appToAdd.length)
		return ;
	if ((!appToAdd[i].keyDate && appToAdd[i].password) || $("div#saving div.scrapedAppsContainer div[index='" + i + "']").hasClass("selected")) {
		if (appToAdd[i].keyDate) {
			postHandler.post('AddClassicApp', {
				"name" : appToAdd[i].name,
				"websiteId" : appToAdd[i].websiteId,
				"profileId" : appToAdd[i].profileId,
				"password" : appToAdd[i].password,
				"login" : appToAdd[i].login,
				"keyDate" : appToAdd[i].keyDate
			}, function() {
				//always
			}, function(retMsg) {
				//succes
				addTutoApps(logwithId, i + 1);
			}, function(retMsg) {
				//error
			}, 'text');
		} else {
			if (appToAdd[i].password) {
				postHandler.post('AddClassicApp', {
					"name" : appToAdd[i].id,
					"websiteId" : appToAdd[i].websiteId,
					"profileId" : appToAdd[i].profileId,
					"password" : appToAdd[i].password,
					"login" : appToAdd[i].login
				}, function() {
					//always
				}, function(retMsg) {
					//succes
					addTutoApps(retMsg, i + 1);
				}, function(retMsg) {
					//error
				}, 'text');
			} else {
				postHandler.post('AddLogwithApp', {
					"name" : appToAdd[i].name,
					"websiteId" : appToAdd[i].websiteId,
					"profileId" : appToAdd[i].profileId,
					"logwithId" : logwithId
				}, function() {
					//always
				}, function(retMsg) {
					//succes
					addTutoApps(logwithId, i + 1);
				}, function(retMsg) {
					//error
				}, 'text');
			}
		}
	} else {
		addTutoApps(logwithId, i + 1);
	}
}

$('div#saving div#selectScraping button').click(function () {
	addTutoApps(0, 0);
});

function showAccountCredentials(retMsg) {
	$('#accountCredentials').addClass("show");
	$('#accountCredentials p span').text(scrapping[0].name);
	$('#accountCredentials img').attr("src", scrapping[0].img);
	if (retMsg == "") {
		$("#accountCredentials input[name='email']").val("");
		$("#accountCredentials input[name='email']").focus();
		$('#accountCredentials div.errorText').removeClass("show");
	} else {
		$('#accountCredentials div.errorText p').text(retMsg);
		$("#accountCredentials input[name='password']").focus();
		$('#accountCredentials div.errorText').addClass("show");
	}
	$("#accountCredentials input[name='password']").val("");
}

function showScrapingInfo() {
	$('#scrapingInfo').addClass("show");
	$('#scrapingInfo p span').text(scrapping[0].name);
	$('#scrapingInfo div.logo img').attr("src", scrapping[0].img);
	event = new CustomEvent("Scrap" + scrapping[0].id, {"detail": {"login" : scrapping[0].login, "password" : scrapping[0].password}});
	var receive = false;
    document.dispatchEvent(event);
	$(document).on("Scrap" + scrapping[0].id + "Result", function (e) {
		if (receive == false) {
			receive = true;
			$('#scrapingInfo').removeClass("show");
			if (e.detail.success == false) {
				showAccountCredentials(e.detail.msg);
			} else {
				jsonscrap[scrapping[0].id] = e.detail.msg;
				ScrapingInfoFinished();
			}
		}
	});
}

function ScrapingInfoFinished() {
	scrappingFinished.push(scrapping[0]);
	scrapping.splice(0, 1);
	if (scrapping.length == 0) {
		postHandler.post('FilterScrap', {
			"scrapjson" : JSON.stringify(jsonscrap)
		}, function() {
			//always
		}, function(retMsg) {
			//succes
			filterJson = retMsg;
			showSavingPopup(JSON.parse(retMsg));
			
		}, function(retMsg) {
			//error
		}, 'text');			
	} else {
		showAccountCredentials("");
	}
}

$('div#importation div#selectScraping input:checkbox').click(function() {
	
	if ($('div#importation div#selectScraping input:checkbox:checked').length == 0) {
		$('div#importation div#selectScraping button').addClass("locked");
	} else {
		$('div#importation div#selectScraping button').removeClass("locked");
	}
});

$('div#importation div#selectScraping button').click(function () {
	$('div.account').each(function (index) {
		if ($(this).find("input:checkbox").is(":checked")) {
			scrapping.push({"name" : $(this).find("p.name").text(), "websiteId" : $(this).find("input:checkbox").attr("websiteId") ,"id" : $(this).find("input:checkbox").attr("id"), "img" : $(this).find("img").attr("src"), "login" : "", "password" : ""});
		}
	});
	if (scrapping.length > 0) {
		$('div#importation div#selectScraping').removeClass("show");
		showAccountCredentials("");
	}
});

$('#accountCredentials input').keypress(function (e) {
	var used = false;
	if (e.which == 13 && used == false) {
		$('#accountCredentials button').click();
		used = true;
		setTimeout(function() {used = false}, 500);
	}
});

$('#accountCredentials a').click(function () {
	ScrapingInfoFinished();
});

$('#accountCredentials button').click(function () {
	scrapping[0].login = $("#accountCredentials input[name='email']").val();
	scrapping[0].password = $("#accountCredentials input[name='password']").val();
	
    $('#accountCredentials').removeClass("show");
	showScrapingInfo();
});
