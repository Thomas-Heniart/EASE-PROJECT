function showExtensionPopup() {
    var ease_extension = $("#ease_extension");
    var new_ease_extension = $("#new_ease_extension");
    if (ease_extension.length) {
        if (!waitForExtension) {
            if (getUserNavigator() == "Safari") {
                if (!$('#ease_extension').attr("safariversion") || $('#ease_extension').attr("safariversion") != "2.2.9") {
                    $('#extension .title p').text("Update your extension");
                    $('#extension #download #line1').text("A new version of the extension is now available.");
                    $('#extension #download #line2').text("We added new features and made it faster !");
                    $('#extension #download button').text("Update Ease Extension");
                    $('#extension').addClass("myshow");
                    $('#extension #download').addClass("show");
                    return true;
                }
                return false;
            } else {
                $("#extension").addClass("myshow");
                $("#extension #deleteExtension").addClass("show");
            }
            return true;
        } else {
            setTimeout(function () {
                return showExtensionPopup();
            }, 200);
        }

    } else if (!new_ease_extension.length) {
        if (!waitForExtension) {
            $("#extension").addClass("myshow");
            $("#extension #download").addClass("show");
        }
        else
            setTimeout(function () {
                return showExtensionPopup();
            }, 200);
    } else {
        return false;
    }
}

function sendEvent(obj) {
    if (testApp) {
        if (!($(obj).hasClass('waitingLinkImage'))) {
            var appId = $(obj).closest('.siteLinkBox').attr('id');
            var link = $(obj).closest('.siteLinkBox').attr('link');
            var logoImage = $(obj).find('.linkImage');
            var json = new Object();
            var event;
            $(obj).addClass('waitingLinkImage');
            $(obj).addClass('scaleinAnimation');
            setTimeout(function () {
                $(obj).removeClass("waitingLinkImage");
                $(obj).removeClass('scaleinAnimation');
            }, 1000);
            if (typeof link !== typeof undefined && link !== false) {
            } else {
                postHandler.post("AskInfo", {
                    appId: appId
                }, function () {
                }, function (retMsg) {
                    json.detail = JSON.parse(retMsg);
                    event = new CustomEvent("Test", json);
                    document.dispatchEvent(event);
                }, function (retMsg) {
                }, 'text');
            }
        }
    } else {
        if (!($(obj).hasClass('waitingLinkImage'))) {
            var appId = $(obj).closest('.siteLinkBox').attr('id');
            var link = $(obj).closest('.siteLinkBox').attr('link');
            var logoImage = $(obj).find('.linkImage');
            var json = new Object();
            var event;

            if (showExtensionPopup())
                return;
            else {
                $(obj).addClass('waitingLinkImage');
                $(obj).addClass('scaleinAnimation');
                setTimeout(function () {
                    $(obj).removeClass("waitingLinkImage");
                    $(obj).removeClass('scaleinAnimation');
                }, 1000);
                postHandler.post("AskInfo", {
                    appId: appId,
                }, function () {
                }, function (retMsg) {
                    json.detail = JSON.parse(retMsg);
                    var message = "NewConnection";
                    if (json.detail[0] && json.detail[0].url) {
                        json.detail = json.detail[0];
                        message = "NewLinkToOpen";
                    } else {
                        json.detail.forEach(function (detail) {
                            if (typeof detail.user !== "undefined") {
                                for (var key in detail.user) {
                                    if (detail.user.hasOwnProperty(key))
                                        detail.user[key] = decipher(detail.user[key]);
                                }
                            }
                        });
                        var jsonDetail = json.detail[json.detail.length - 1];
                    }
                    var now = "" + new Date;
                    json.detail.highlight = !ctrlDown;
                    event = new CustomEvent(message, json);
                    document.dispatchEvent(event);
                }, function (retMsg) {
                    return;
                }, 'text');
            }
        }
    }
}

$(document).ready(function () {
    $('#homePageSwitch').change(function () {
        var homepageState = $(this).is(":checked");
        var stateString = homepageState.toString();
        postHandler.post("HomepageSwitch", {
            homepageState: stateString
        }, function () {

        }, function (data) {

        }, function (date) {

        });
    });

    // init extension popup 
    $("#chrome button[type='submit'], #safari button[type='submit']").click(function () {
        window.location = "/";
    });
    $("#extension #download #showExtensionInfo").click(function () {
        $('#extension #step1').removeClass('show');
        $('#extension #extensionInfo').addClass('show');
    });
    $("#extension #extensionInfo button").click(function () {
        $('#extension #extensionInfo').removeClass('show');
        $('#extension #step1').addClass('show');
    });
    $("#extension #download button[type='submit']").click(function () {
        $("#extension #step1 #download").removeClass('show');
        if (NavigatorName == "Chrome") {
            var win = window.open('https://chrome.google.com/webstore/detail/hnacegpfmpknpdjmhdmpkmedplfcmdmp', '_blank');
            win.focus();
        } else if (NavigatorName == "Safari") {
            $("#extension #step1 #safari").addClass('show');
            window.location.replace(window.location.protocol + "//" + window.location.host + "/safariExtension/EaseExtension.safariextz");
        } else {
            $("#extension #step1 #other").addClass('show');
        }
    });
});
