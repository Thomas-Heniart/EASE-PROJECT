function sendEvent(obj) {
    if (!($(obj).hasClass('waitingLinkImage'))) {
        var appId = $(obj).closest('.siteLinkBox').attr('id');
        var link = $(obj).closest('.siteLinkBox').attr('link');
        var logoImage = $(obj).find('.linkImage');
        var json = new Object();
        var event;
        
        mixpanel.track("App clicks");
        
        if (!($('#ease_extension').length)) {
            checkForExtension();
            return;
        }
        $(obj).addClass('waitingLinkImage');
        $(obj).addClass('scaleinAnimation');
        setTimeout(function() {
            $(obj).removeClass("waitingLinkImage");
            $(obj).removeClass('scaleinAnimation');
        }, 1000);
        if (typeof link !== typeof undefined && link !== false) {
            json.detail = {"url":link};
            json.detail.highlight = true;
            if (ctrlDown) json.detail.highlight = false;
            mixpanel.track("App successful clicks");
            mixpanel.track("link connections");
            event = new CustomEvent("NewLinkToOpen", json);
            document.dispatchEvent(event);
        } else {
        	postHandler.post("askInfo", {
        		appId : appId,
        	}, function() {
        	}, function(retMsg) {
        		json.detail = JSON.parse(retMsg);
        		json.detail.highlight = true;
        		if (ctrlDown) json.detail.highlight = false;
        		mixpanel.track("App successful clicks");
        		mixpanel.track(json.detail[json.detail.length - 1].website.name + " connections");
        		event = new CustomEvent("NewConnection", json);
        		document.dispatchEvent(event);
        	}, function(retMsg) {
        		showAlertPopup(retMsg, true);
        	}, 'text');
        }
    }
}

function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    var expires = "expires="+d.toUTCString();
    document.cookie = cname + "=" + cvalue + "; " + expires;
}

function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

function pad(num, totalChars) {
    var pad = '0';
    num = num + '';
    while (num.length < totalChars) {
        num = pad + num;
    }
    return num;
}

// Ratio is between 0 and 1
function changeColor(color, ratio, darker) {
    // Trim trailing/leading whitespace
    color = color.replace(/^\s*|\s*$/, '');

    // Expand three-digit hex
    color = color.replace(
        /^#?([a-f0-9])([a-f0-9])([a-f0-9])$/i,
        '#$1$1$2$2$3$3'
        );

    // Calculate ratio
    var difference = Math.round(ratio * 256) * (darker ? -1 : 1),
        // Determine if input is RGB(A)
        rgb = color.match(new RegExp('^rgba?\\(\\s*' +
            '(\\d|[1-9]\\d|1\\d{2}|2[0-4][0-9]|25[0-5])' +
            '\\s*,\\s*' +
            '(\\d|[1-9]\\d|1\\d{2}|2[0-4][0-9]|25[0-5])' +
            '\\s*,\\s*' +
            '(\\d|[1-9]\\d|1\\d{2}|2[0-4][0-9]|25[0-5])' +
            '(?:\\s*,\\s*' +
            '(0|1|0?\\.\\d+))?' +
            '\\s*\\)$'
            , 'i')),
        alpha = !!rgb && rgb[4] != null ? rgb[4] : null,

        // Convert hex to decimal
        decimal = !!rgb? [rgb[1], rgb[2], rgb[3]] : color.replace(
            /^#?([a-f0-9][a-f0-9])([a-f0-9][a-f0-9])([a-f0-9][a-f0-9])/i,
            function() {
                return parseInt(arguments[1], 16) + ',' +
                parseInt(arguments[2], 16) + ',' +
                parseInt(arguments[3], 16);
            }
            ).split(/,/),
        returnValue;

    // Return RGB(A)
    return !!rgb ?
    'rgb' + (alpha !== null ? 'a' : '') + '(' +
    Math[darker ? 'max' : 'min'](
        parseInt(decimal[0], 10) + difference, darker ? 0 : 255
        ) + ', ' +
    Math[darker ? 'max' : 'min'](
        parseInt(decimal[1], 10) + difference, darker ? 0 : 255
        ) + ', ' +
    Math[darker ? 'max' : 'min'](
        parseInt(decimal[2], 10) + difference, darker ? 0 : 255
        ) +
    (alpha !== null ? ', ' + alpha : '') +
    ')' :
        // Return hex
        [
        '#',
        pad(Math[darker ? 'max' : 'min'](
            parseInt(decimal[0], 10) + difference, darker ? 0 : 255
            ).toString(16), 2),
        pad(Math[darker ? 'max' : 'min'](
            parseInt(decimal[1], 10) + difference, darker ? 0 : 255
            ).toString(16), 2),
        pad(Math[darker ? 'max' : 'min'](
            parseInt(decimal[2], 10) + difference, darker ? 0 : 255
            ).toString(16), 2)
        ].join('');
    }

    function lighterColor(color, ratio) {
        return changeColor(color, ratio, false);
    }


    function darkerColor(color, ratio) {
        return changeColor(color, ratio, true);
    }

    function checkForExtension() {
        var ext = $('#ease_extension');

        if (!($('#ease_extension').length)) {
            $('#downloadExtension').css('display', 'block');
            $('#downloadExtension').find('#install-button').click(
                function() {
                    var NavigatorName = getUserNavigator();
                    if (NavigatorName == "Chrome") {
                        chrome.webstore
                        .install(
                            'https://chrome.google.com/webstore/detail/echjdhmhmgildgidlcdlepfkaledeokm',
                            function() {
                                window.location
                                .replace("index.jsp");
                            },
                            function() {
                                window.location
                                .replace("index.jsp");
                            })
                    }
                    else if (NavigatorName == "Safari"){
                        window.location.replace("https://ease.space/safariExtension/EaseExtension.safariextz");
                        $('#downloadExtension').find('.popupContent').hide();
                        $('#downloadExtension').find('.safariHelper').show();
                    }
                });
        }
    }

    $(document).click(function (e){
        var profile = $(e.target).closest('.ProfileControlPanel');
        var settingsButton = null;

        if (profile.length){
            settingsButton = profile.closest('.ProfileBox').find('.ProfileSettingsButton.settings-show');
        }

        $('.ProfileSettingsButton.settings-show').each(function(){
            if (!($(this).is($(settingsButton)))){
                $(this).click();
            }
        });
    }); 

    function setupAppSettingButtonPopup(elem){
 /*       $(elem).on('mouseover', function() {
            var subPopup = $(this).find('.appActionsPopup');
            var profileParent = $(this).closest('.siteLinkBox').parent();
            var str = '-';
            var scrollDist =  $(profileParent).scrollTop() + $(this).height();
            str += scrollDist + 'px';
            subPopup.css({
                'margin-top':str
            });
        }); */
    };
    $(document).on('mouseover', '.showAppActionsButton', function(evt){
            var subPopup = $(this).find('.appActionsPopup');
            var profileParent = $(this).closest('.siteLinkBox').parent();
            var str = '-';
            var scrollDist =  $(profileParent).scrollTop() + $(this).height();
            if ($(this).closest('.col-left'))
                scrollDist += $(this).closest('.col-left').scrollTop();
            str += scrollDist + 'px';
            subPopup.css({
                'margin-top':str
            });
    });
    $(document).ready(function() {
        $('.SitesContainer .showAppActionsButton').each(function(){
            setupAppSettingButtonPopup($(this));
        });
    });

