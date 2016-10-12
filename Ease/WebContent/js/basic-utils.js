function sendEvent(email, password, userSelector, passSelector, buttonSelector, urlLogin, urlHome){ //dispatch an Event to warn the plugin it has to connect to the websit\e
    var event = new CustomEvent("NewConnection",
     {'detail':{"user": email,
     "pass":password,
     "userField":userSelector,
     "passField":passSelector,
     "button": buttonSelector,
     "urlLogin": urlLogin,
     "urlHome": urlHome}});
    document.dispatchEvent(event);
    console.log("Ease : go to new page");
    console.log(event.detail);
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

    $(document).ready(function() {
/*  $(".ease-button").hover(function(event) {
    var darkColor = darkerColor($(event.target).css("background-color"), 0.3);
    $(event.target).css("background-color", darkColor);
  }, function(event) {
    var lightColor = lighterColor($(event.target).css("background-color"), 0.3);
    $(event.target).css("background-color", lightColor);
});*/
});
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
    $(document).ready(function() {
        $(".profileSettingsTab").accordion({
            active : 10,
            collapsible : true,
            autoHeight : false,
            heightStyle : "content"
        });
    });

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
    function makeSettingsAccordion(elem) {
        $(elem).click(
            function(e) {
                var parent;
                var button = $(this);
                e.stopPropagation();
                $('.ProfileSettingsButton.settings-show').each(function(){
                    if (!($(this).is($(button)))){
                        $(this).click();
                    }
                });             
                parent = $(elem).closest(".ProfileBox");
                if ($(elem).hasClass("settings-show")) {
                    $(elem).removeClass("settings-show");

                    elem.removeClass('fa-rotate-90');                       
                    parent.find(".ProfileControlPanel").css("display","none");

                } else {

                    elem.addClass('fa-rotate-90');
                    $(elem).addClass("settings-show");
                    parent.find(".ProfileControlPanel").css("display","inline-block");
                }
            });
    }

    $(document).ready(function() {
        $('.ProfileSettingsButton').each(function(index, el) {
            makeSettingsAccordion($(this));
        });
        $('.ProfilesHandler .item').each(function(index, el) {
            setupProfileSettings($(this));
        });

    });

    $(document).ready(function() {
        $('.ModifyProfileButton').each(function(index, el) {
            $(this).click(function() {
                var parent;
                var index;
                var name;
                var popup;

                parent = $(this).closest(".ProfileBox");
                index = parent.find(".ProfileControlPanel").attr("index");
                name = parent.find(".ProfileName").children("p").text();
                popup = $('#PopupModifyProfile');

                popup.find("#index").val(index);
                popup.find("#profilName").parent().addClass("input--filled");
                popup.find("#profilName").val(name.substring(1));
                $('#PopupModifyProfile').addClass("md-show");
            });
        });
    });
    function setupAppSettingButtonPopup(elem){
        $(elem).on('mouseover', function() {
            var subPopup = $(this).find('.appActionsPopup');
            var profileParent = (this).closest('.content');
            var str = '-';
            var scrollDist = $('.col-left').scrollTop() + $(profileParent).scrollTop() + $(this).height();
            str += scrollDist + 'px';
            subPopup.css({
                'margin-top':str
            });
        }); 
    }
    $(document).ready(function() {
        $('.SitesContainer .showAppActionsButton').each(function(){
            setupAppSettingButtonPopup($(this));
        });
    });
