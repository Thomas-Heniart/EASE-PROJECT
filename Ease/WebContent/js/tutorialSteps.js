/* Transitions */

$("#manualImportation").click(function () {
    $("#simpleImportation").addClass("show");
});


/////////
/////////	Integrate apps
/////////

var addAppTutoCpt = 0;
var appsSelectedFilled = 0;
var appsSelectedSkiped = 0;

function showAddAppTuto(i) {
    $("div#addAppTutorial").addClass("show");
    $("div#addAppTutorial img").attr("src", $("div#simpleImportation div.appHandler div.app.selected:eq(" + i + ")").find("img").attr("src"));
    $("div#addAppTutorial input#name").val($("div#simpleImportation div.appHandler div.app.selected:eq(" + i + ")").find("p.name").text());
    $("div#addAppTutorial p.post-title span").text($("div#simpleImportation div.appHandler div.app.selected:eq(" + i + ")").find("p.name").text());
    $("div#addAppTutorial input#login").val("");
    $("div#addAppTutorial input#login").focus();
    $("div#addAppTutorial input#password").val("");
}

$("div#simpleImportation div.appHandler").click(function () {
    if ($(this).find("div.app").hasClass("selected")) {
        $(this).find("div.app").removeClass("selected");
    } else {
        $(this).find("div.app").addClass("selected");
    }
    if ($("#simpleImportation .app.selected").length >= 4) {
        $("#simpleImportation button[type='submit']").removeClass('locked');
    } else {
        $("#simpleImportation button[type='submit']").addClass('locked');
    }
});

$("#simpleImportation .showMoreHelper").click(function () {
    //easeTracker.trackEvent("");
    $(this).css('display', 'none');
    $("#simpleImportation .appHandler.hidden").removeClass('hidden');
});

function goToNextStep() {
    addAppTutoCpt++;
    if ($("div#simpleImportation div.appHandler div.app.selected").length > addAppTutoCpt) {
        showAddAppTuto(addAppTutoCpt);
    } else {
        postHandler.post("TutoStep", {
            "tutoStep": "apps_manually_added"
        }, function () {
            //always
        }, function (retMsg) {
            //success
            easeTracker.trackEvent("TutoManoDone");
            easeTracker.setUserProperty("TutoManoSelected", addAppTutoCpt);
            easeTracker.setUserProperty("TutoManoFilled", appsSelectedFilled);
            easeTracker.setUserProperty("TutoManoSkipped", appsSelectedSkiped);
            easeTracker.increaseAppCounter(addAppTutoCpt);
            location.reload();
        }, function (retMsg) {
            //error
        }, 'text');
    }
}

$("div#addAppTutorial #skipButton").click(function () {

    var appName = $("div#addAppTutorial input#name").val();
    var websiteId = $("div#simpleImportation div.appHandler div.app.selected:eq(" + addAppTutoCpt + ")").attr("id");
    postHandler.post("AddEmptyApp", {
        "name": appName,
        "profileId": $("div#addAppTutorial input#profileId").val(),
        "websiteId": websiteId
    }, function () {
        //always
    }, function (retMsg) {
        //success
        var newWebsite = $(".catlogApp[idx='" + websiteId + "']").attr("newApp") != null;
        easeTracker.trackEvent("AddApp", {"appType": "EmptyApp", "appName": appName, "AppNewYN": newWebsite});
        appsSelectedSkiped++;
    }, function (retMsg) {
        //error
    }, 'text');
    goToNextStep();
});

$('div#addAppTutorial form').submit(function (e) {
    e.preventDefault();
    var appName = $("div#addAppTutorial input#name").val();
    var websiteIds = [];
    var websiteId = $("div#simpleImportation div.appHandler div.app.selected:eq(" + addAppTutoCpt + ")").attr("id");
    websiteIds.push(websiteId);
    postHandler.post('AddClassicApp', {
        "name": appName,
        "login": $("div#addAppTutorial input#login").val(),
        "profileId": $("div#addAppTutorial input#profileId").val(),
        "password": $("div#addAppTutorial input#password").val(),
        "websiteIds": (JSON.stringify(websiteIds))
    }, function () {
        //always
    }, function (retMsg) {
        //succes
        var newWebsite = $(".catlogApp[idx='" + websiteId + "']").attr("newApp") != null;
        easeTracker.trackEvent("AddApp", {"appType": "ClassicApp", "appName": appName, "AppNewYN": newWebsite});
        appsSelectedFilled++;
    }, function (retMsg) {
        //error
    }, 'text');
    if ($("div#addAppTutorial input#login").val() != "" && $("div#addAppTutorial input#password").val() != "" && $("div#addAppTutorial input#name").val() != "") {
        goToNextStep();
    }
});

$("div#simpleImportation button").click(function () {
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
var chromeScrappingCount = 0;
var facebookScrappingCount = 0;
var linkedInScrappingCount = 0;
var chromeSelectedCount = 0;
var facebookSelectedCount = 0;
var linkedInSelectedCount = 0;

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
        if (by.img.indexOf("Chrome") !== -1) {
            chromeScrappingCount += 1;
            chromeSelectedCount += 1;
        } else if (by.img.indexOf("Facebook") !== -1) {
            facebookScrappingCount += 1;
            facebookSelectedCount += 1;
        } else {
            linkedInScrappingCount += 1;
            linkedInSelectedCount += 1;
        }
    });
}

function showSavingPopup(filterJson) {
    scrappingFinished.forEach(function (element) {
        if (element.websiteId)
            appToAdd.push(element);
        displayTutoApps(filterJson[element.id], element);
    });
    $('div#saving div.scrapedAppsContainer div.appHandler').click(function () {
        var self = $(this).find("div.app");
        var bySrc = self.find("img.by").attr("src");
        if (self.hasClass("selected")) {
            self.removeClass("selected");
            if (bySrc.indexOf("Chrome") !== -1) {
                chromeSelectedCount -= 1;
            } else if (bySrc.indexOf("Facebook") !== -1) {
                facebookSelectedCount -= 1;
            } else {
                linkedInSelectedCount -= 1;
            }
        } else {
            self.addClass("selected");
            if (bySrc.indexOf("Chrome") !== -1) {
                chromeSelectedCount += 1;
            } else if (bySrc.indexOf("Facebook") !== -1) {
                facebookSelectedCount += 1;
            } else {
                linkedInSelectedCount += 1;
            }
        }
    });
    if ($('div#saving div.scrapedAppsContainer div.appHandler').length == 0) {
        sendTutoAddApp();
    } else {
        $('#accountCredentials').removeClass("show");
        $('div#importation').removeClass("show");
        $('div#saving').addClass("show");
    }
}

function sendTutoAddApp() {
    $("#scrapping_done_submit").addClass("hide");
    $("#add_app_progress").removeClass("hide");
    var appToAddFilter = [];
    for (var i = 0; i < appToAdd.length; ++i) {
        if ($("div#saving div.scrapedAppsContainer div[index='" + i + "']").length == 0 || $("div#saving div.scrapedAppsContainer div[index='" + i + "']").hasClass("selected")) {
            appToAddFilter.push(appToAdd[i]);
        }
    }

    postHandler.post('TutoAddApps', {
        "scrapjson": JSON.stringify(appToAddFilter)
    }, function () {
        //always
    }, function (retMsg) {
        //succes
        postHandler.post('TutoStep', {
            "tutoStep": "chrome_scrapping"
        }, function () {
            //always
        }, function (retMsg) {
            //succes
            easeTracker.trackEvent("TutoScrappingDone");
            if (chromeScrappingCount > 0 && chromeSelectedCount >= 0) {
                easeTracker.trackEvent("TutoScrapChrome");
                easeTracker.setUserProperty("TutoScrapChromeCount", chromeScrappingCount);
                easeTracker.setUserProperty("TutoScrapChromeSelected", chromeSelectedCount);
            }
            if (facebookScrappingCount > 0 && facebookSelectedCount >= 0) {
                easeTracker.trackEvent("TutoScrapFacebook");
                easeTracker.setUserProperty("TutoScrapFacebookCount", facebookScrappingCount);
                easeTracker.setUserProperty("TutoScrapFacebookSelected", facebookSelectedCount);
            }
            if (linkedInScrappingCount > 0 && linkedInSelectedCount >= 0) {
                easeTracker.trackEvent("TutoScrapLinkedIn");
                easeTracker.setUserProperty("TutoScrapLinkedInCount", linkedInScrappingCount);
                easeTracker.setUserProperty("TutoLinkedInSelected", linkedInSelectedCount);
            }
            easeTracker.increaseAppCounter(chromeSelectedCount + facebookSelectedCount + linkedInSelectedCount);
            easeTracker.setUserProperty("TutoTotalAppsScrapped", (chromeScrappingCount + facebookScrappingCount + linkedInScrappingCount));
            easeTracker.setUserProperty("TutoTotalAppsSelected", (chromeSelectedCount + facebookSelectedCount + linkedInSelectedCount));
            location.reload();
        }, function (retMsg) {
            //error
        }, 'text');
    }, function (retMsg) {
        //error
    }, 'text');
}

$('div#saving div#selectScraping button').click(function () {
    sendTutoAddApp();
});

function showAccountCredentials(retMsg) {
    $('#accountCredentials').addClass("show");
    $('#accountCredentials p span').text(scrapping[0].name);
    $('#accountCredentials div.logo img').attr("src", scrapping[0].img);
    if (retMsg == "") {
        if (scrapping[0].name == "Google Chrome" && !!window.chrome && !!window.chrome.webstore) {
            $("#accountCredentials #chromeUserEmailHelper").addClass("show");
            event = new CustomEvent("GetChromeUser", {});
            document.dispatchEvent(event);
            $(document).on("ChromeUserEmail", function (e) {
                $("#accountCredentials input[name='email']").val(e.detail);
                $("#accountCredentials input[name='password']").focus();
            });
        } else {
            $("#accountCredentials input[name='email']").val("");
            $("#accountCredentials input[name='email']").focus();
        }
        $('#accountCredentials div.errorText').removeClass("show");
    } else {
        $('#accountCredentials div.errorText p').text(retMsg);
        $("#accountCredentials input[name='password']").focus();
        $('#accountCredentials div.errorText').addClass("show");
    }
    $("#accountCredentials input[name='password']").val("");
    $("#accountCredentials input[name='password']").change();
}

function showScrapingInfo() {
    $('#scrapingInfo').addClass("show");
    $('#scrapingInfo p span').text(scrapping[0].name);
    $('#scrapingInfo div.logo img').attr("src", scrapping[0].img);
    var eventName = "Scrap" + scrapping[0].id;
    event = new CustomEvent(eventName, {"detail": {"login": scrapping[0].login, "password": scrapping[0].password}});
    var receive = false;
    document.dispatchEvent(event);
    $(document).on((eventName + "Result"), function (e) {
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
            "scrapjson": JSON.stringify(jsonscrap)
        }, function () {
            //always
        }, function (retMsg) {
            //succes
            showSavingPopup(JSON.parse(retMsg));

        }, function (retMsg) {
            //error
        }, 'text');
    } else {
        showAccountCredentials("");
    }
}

$('div#importation div#selectScraping input:checkbox').click(function () {
    /* if ($('div#importation div#selectScraping input:checkbox:checked').length == 0) {
     $('div#importation div#selectScraping button').addClass("locked");
     } else {
     $('div#importation div#selectScraping button').removeClass("locked");
     } */
});

$('div#importation div#selectScraping button').click(function () {
    $('div.account').each(function (index) {
        if ($(this).find("input:checkbox").is(":checked")) {
            if ($(this).find("p.name").text != "Google Chrome")
                scrapping.push({
                    "name": $(this).find("p.name").text(),
                    "websiteId": $(this).find("input:checkbox").attr("websiteId"),
                    "id": $(this).find("input:checkbox").attr("id"),
                    "img": $(this).find("img").attr("src"),
                    "login": "",
                    "password": ""
                });
        }
    });
    if (scrapping.length > 0) {
        $('div#importation div#selectScraping').removeClass("show");
        showAccountCredentials("");
    } else {
        $('div#importation').removeClass("show");
        $("#simpleImportation").addClass("show");
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
        setTimeout(function () {
            used = false
        }, 500);
    }
});

$('#accountCredentials input').keyup(function () {
    $(this).change();
})

$('#accountCredentials input').change(function () {
    checkInputs($("#accountCredentials"));
});

function checkInputs(contextElement) {
    var shouldBeLocked = false;
    var inputs = contextElement.find("input");
    inputs.each(function (index, element) {
        if ($(element).val() === "") {
            shouldBeLocked = true;
            return;
        }
    });
    if (shouldBeLocked)
        $("button[type='submit']", contextElement).addClass("locked");
    else
        $("button[type='submit']", contextElement).removeClass("locked");

}

$('#accountCredentials a').click(function () {
    ScrapingInfoFinished();
    $("#accountCredentials #chromeUserEmailHelper").removeClass("show");
});

$('#accountCredentials button').click(function () {
    scrapping[0].login = $("#accountCredentials input[name='email']").val();
    scrapping[0].password = $("#accountCredentials input[name='password']").val();
    $("#accountCredentials #chromeUserEmailHelper").removeClass("show");
    $('#accountCredentials').removeClass("show");
    showScrapingInfo();
});

