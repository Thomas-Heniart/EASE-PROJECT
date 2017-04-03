// load css dynamycally
/*!function(){
    var loadDeferredStyles = function() {
        var addStylesNode = document.getElementById("deferred-styles");
        var replacement = document.createElement("div");
        replacement.innerHTML = addStylesNode.textContent;
        document.body.appendChild(replacement)
        addStylesNode.parentElement.removeChild(addStylesNode);
    };
    var raf = requestAnimationFrame || mozRequestAnimationFrame ||
    webkitRequestAnimationFrame || msRequestAnimationFrame;
    if (raf) raf(function() { window.setTimeout(loadDeferredStyles, 0); });
    else window.addEventListener('load', loadDeferredStyles);
}();*/

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

function getUserNavigator() {
    var ua = navigator.userAgent;
    var x = ua.indexOf("MSIE");
    var y = "MSIE";
    if (x == -1) {
        x = ua.indexOf("Firefox");
        y = "Firefox";
        if (x == -1) {
            if (x == -1) {
                x = ua.indexOf("Chrome");
                y = "Chrome";
                if (x == -1) {
                    x = ua.indexOf("Opera");
                    y = "Opera";
                    if (x == -1) {
                        x = ua.indexOf("Safari");
                        if (x != -1) {
                            x = ua.indexOf("Version");
                            y = "Safari";
                        } else {
                            y = "Unknown";
                        }
                    }
                }
            }
        }
    }
    return (y);
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

    $(document).on('mouseover', '.showAppActionsButton', function(evt){
        var subPopup = $(this).find('.appActionsPopup');
        var profileParent = $(this).closest('.siteLinkBox').parent();
        $(this).closest('.linkImage').addClass('settingsShow');
        var str = '-';
        var scrollDist =  $(profileParent).scrollTop() + $(this).height();
        if ($(this).closest('.col-left'))
            scrollDist += $(this).closest('.col-left').scrollTop();
        str += scrollDist + 'px';
        subPopup.css({
            'margin-top':str
        });
    });
    $(document).on('mouseleave', '.showAppActionsButton', function(evt){
        $(this).closest('.linkImage').removeClass('settingsShow');
    });

/*    $(document).ready(function(){
        var placeHolder = null;

        $('input:not([readonly]),textarea').focus(function(){
            placeHolder = $(this).attr('placeholder');
            $(this).attr('placeHolder', '');
            $(this).one('blur', function(){
                $(this).attr('placeholder', placeHolder);
            });
        });
    });*/

    /* js for checkable elements (search for .checkable) */
    $(document).click(function(e){
        var target = $(e.target).closest('.checkable');

        if (target.length){
            target.hasClass('checked') && target.removeClass('checked') || target.addClass('checked'); 
        }
    });

    $(document).ready(function(){
        $(document).on('click', ".showPassDiv", function(){
            var input = $(this).parent().find('input');
            if ($(this).hasClass('show')){
                input.attr('type', 'password');
                input.focus();
                $(this).removeClass('show');
            }else {
                input.attr('type', 'text');
                input.focus();
                $(this).addClass('show');
            }
        });        
    });


    var Animations = function(){
        this.animateOnce = function(elem, animationClass){
            $(elem).addClass(animationClass);

            $(elem).one('webkitAnimationEnd oanimationend msAnimationEnd animationend',
                function(e) {
                    $(elem).removeClass(animationClass);
                });
        }
        this.shake_anim = function(elem){
            $(elem).addClass('shake-anim');

            $(elem).one('webkitAnimationEnd oanimationend msAnimationEnd animationend',   
                function(e) {
                    $(elem).removeClass('shake-anim');
                });
        }
    }
    var easeAnimations = new Animations();

/*if ('serviceWorker' in navigator) {
  window.addEventListener('load', function() {
    navigator.serviceWorker.register('/sw.js').then(function(registration) {
      // Registration was successful
      console.log('ServiceWorker registration successful with scope: ', registration.scope);
    }).catch(function(err) {
      // registration failed :(
      console.log('ServiceWorker registration failed: ', err);
    });
  });
}*/