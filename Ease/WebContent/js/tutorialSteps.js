$("#manualImportation").click(function() {
	$("#simpleImportation").addClass("show");
});



/////////
/////////	Integrate apps
/////////

var addAppTutoCpt = 0;

function showAddAppTuto(i) {
	$("div#addAppTutorial").addClass("show");
	$("div#addAppTutorial img").attr("src", $("div#simpleImportation div.appHandler div.app.selected:eq(" + i + ")").find("img").attr("src"));	
	$("div#addAppTutorial input#name").val($("div#simpleImportation div.appHandler div.app.selected:eq(" + i + ")").find("p.name").text());
	$("div#addAppTutorial p.post-title span").text($("div#simpleImportation div.appHandler div.app.selected:eq(" + i + ")").find("p.name").text());
	$("div#addAppTutorial input#login").val("");
	$("div#addAppTutorial input#login").focus();
	$("div#addAppTutorial input#password").val("");
}

$("div#simpleImportation div.appHandler").click(function() {
	if ($(this).find("div.app").hasClass("selected")) {
		$(this).find("div.app").removeClass("selected");	
	} else {
		$(this).find("div.app").addClass("selected");	
	}
});

function goToNextStep() {
	addAppTutoCpt++;
	if ($("div#simpleImportation div.appHandler div.app.selected").length > addAppTutoCpt) {
		showAddAppTuto(addAppTutoCpt);
	} else {
		postHandler.post("TutoStep", {
			"tutoStep" : "apps_manually_added"
		}, function() {
			//always
		}, function(retMsg) {
			//success
			location.reload();
		}, function(retMsg) {
			//error
		}, 'text');
	}
}

$("div#addAppTutorial #skipButton").click(function() {
	postHandler.post("AddEmptyApp", {
		"name" : $("div#addAppTutorial input#name").val(),
		"profileId" : $("div#addAppTutorial input#profileId").val(),
		"websiteId" : $("div#simpleImportation div.appHandler div.app.selected:eq(" + addAppTutoCpt + ")").attr("id")
	}, function() {
		//always
	}, function(retMsg) {
		//success
	}, function(retMsg) {
		//error
	}, 'text');
	goToNextStep();
});

$('div#addAppTutorial form').submit(function (e) {
	e.preventDefault();
	postHandler.post('AddClassicApp', {
		"name" : $("div#addAppTutorial input#name").val(),
		"login" : $("div#addAppTutorial input#login").val(),
		"profileId" : $("div#addAppTutorial input#profileId").val(),
		"password" : $("div#addAppTutorial input#password").val(),
		"websiteId" : $("div#simpleImportation div.appHandler div.app.selected:eq(" + addAppTutoCpt + ")").attr("id")
	}, function() {
		//always
	}, function(retMsg) {
		//succes
		console.log(retMsg);
	}, function(retMsg) {
		//error
		console.log(retMsg);
	}, 'text');
	if ($("div#addAppTutorial input#login").val() != "" && $("div#addAppTutorial input#password").val() != "" && $("div#addAppTutorial input#name").val() != "") {
		addAppTutoCpt++;
		goToNextStep();
	}
});

$("div#simpleImportation button").click(function() {
	if ($("div#simpleImportation div.appHandler div.app.selected").length >= 4) {
		$("div#simpleImportation").removeClass("show");
		showAddAppTuto(addAppTutoCpt);
	}
});

/////////
/////////	Scrapping for apps
/////////

var scrapping = [];
var appToAdd = [];
var jsonscrap = {};
var scrappingFinished = [];
var profileIds = [];
var loadingStep = 0;
var currentStep = 0;
var maxSteps = 0;

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
	if (i >= appToAdd.length) {
		/*loadingStep = 0;
		currentStep = 0;
		maxSteps = 0;*/
		postHandler.post('TutoStep', {
			"tutoStep" : "chrome_scrapping"
		}, function() {
			//always
		}, function(retMsg) {
			//succes
			location.reload();
		}, function(retMsg) {
			//error
		}, 'text');
		return;
	}
	if ((!appToAdd[i].keyDate && appToAdd[i].password) || $("div#saving div.scrapedAppsContainer div[index='" + i + "']").hasClass("selected")) {
		goToNextLoadingStep();
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

function addTutoProfiles(i, profileId) {
	if (i >= appToAdd.length)
		addTutoApps(0, 0);
	else {
		if (i == 10 || i == 30) {
			postHandler.post('AddProfile', {
				"name" : "Choose name",
				"color" : "#FF0000"
			}, function() {
				//always
			}, function(retMsg) {
				//succes
				appToAdd[i].profileId = retMsg;
				addTutoProfiles(i + 1, retMsg);
			}, function(retMsg) {
				//error
			}, 'text');
		} else {
			if (i > 10) {
				appToAdd[i].profileId = profileId;
			}
			addTutoProfiles(i + 1, profileId);
		}
	}
}

function calculStep() {
	for (var i=0; i<appToAdd.length; i++) {
		if ($("div#saving div.scrapedAppsContainer div[index='" + i + "']").hasClass("selected"))
			maxSteps++
	}	
	return Math.round(100/maxSteps);
}

function goToNextLoadingStep() {
	currentStep++;
	var progressBar = $("#add_app_progress #progress_bar");
	$("#add_app_progress #currentStep").text(currentStep);
    var width = progressBar.width();
    var parentWidth = progressBar.offsetParent().width();
    var percent = 100*width/parentWidth;
    var nextPercent = percent + loadingStep;
    if (nextPercent > 100)
    	nextPercent = 100;
    progressBar.width(nextPercent + "%");
}

$('div#saving div#selectScraping button').click(function () {
	loadingStep = calculStep();
	$("#add_app_progress #maxStep").text(maxSteps);
	$("#scrapping_done_submit").addClass("hide");
	$("#add_app_progress").removeClass("hide");
	addTutoProfiles(0, 0);
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

$('div#importation div#selectScraping a').click(function () {
	$('div#importation').removeClass("show");
	$('div#simpleImportation').addClass("show");
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
