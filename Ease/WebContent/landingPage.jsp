<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language"
       value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"
       scope="session"/>
<c:if test="${language ne 'en' and language ne 'fr_FR'}">
    <c:set var="language" value="fr_FR" scope="session"/>
</c:if>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="com.Ease.Languages.text"/>
<html lang="${language}">
<head>
    <title> Ease.space | Le meilleur moyen de se connecter à ses sites préférés.</title>
    <!-- Description shown in Google -->
    <meta name="description"
          content="Ease est la homepage qui vous débarrasse des mots de passe. En 1 clic, soyez connecté à vos sites web automatiquement."/>
    <!-- Facebook metadata -->
    <meta property="og:url" content="https://ease.space/"/>
    <meta property="og:title" content="Ease.space | Le meilleur moyen de se connecter à ses sites préférés."/>
    <meta property="og:description"
          content="Ease est la homepage qui vous débarrasse des mots de passe. En 1 clic, soyez connecté à vos sites web automatiquement."/>
    <meta property="og:image" content="https://ease.space/resources/images/fbmeta-fr.png"/>
    <meta property="og:type" content="website"/>
    <meta property="og:logo" content="https://ease.space/resources/icons/APPEASE.png"/>
    <!-- Twitter metadata -->
    <meta name="twitter:card" content="summary_large_image"/>
    <meta name="twitter:site" content="@Ease_app"/>
    <meta name="twitter:creator" content="@Ease_app"/>
    <meta name="twitter:title" content="Ease.space | Le meilleur moyen de se connecter à ses sites préférés."/>
    <meta name="twitter:description"
          content="Ease est la homepage qui vous débarrasse des mots de passe. En 1 clic, soyez connecté à vos sites web automatiquement."/>
    <meta name="twitter:image" content="https://ease.space/resources/images/fbmeta-en.png"/>
    <meta http-equiv="Content-Type" contentType="text/html; charset=UTF-8"/>
    <meta name="viewport" content="initial-scale=1, maximum-scale=1"/>
    <meta property="og:image"
          content="https://ease.space/resources/other/fb_letsgo_icon.jpg"/>
    <link rel="icon" type="image/png" href="resources/icons/APPEASE.png"/>

    <link rel="manifest" href="manifest.json">

    <link rel="stylesheet" type="text/css" href="//fonts.googleapis.com/css?family=Raleway:500,700"/>

    <link rel="stylesheet" href="semantic/dist/semantic.min.css">
    <link rel="stylesheet" type="text/css" href="/cssMinified.v00020/lib/fonts/museo-font.css"/>
    <link rel="stylesheet" href="/cssMinified.v00020/default_style.css"/>
    <link rel="stylesheet" href="/cssMinified.v00020/bootstrap.css"/>
    <link rel="stylesheet" href="/cssMinified.v00020/landingPage.css"/>
    <link rel="stylesheet" type="text/css"
          href="/cssMinified.v00020/lib/fonts/font-awesome-4.2.0/css/font-awesome.min.css"/>
    <style>
        .digit {
            border-radius: 4px;
            background-color: #ffffff;
            font-size: 40px;
            font-weight: 300;
            line-height: 1.65;
            text-align: center;
            color: #373b60;
            padding: 7px 20px;
            box-shadow: -6px 5px 10px rgba(0, 0, 0, 0.1);
            margin: 5px;
        }
    </style>
    <script type="text/javascript">
        (function(e,t){var n=e.amplitude||{_q:[],_iq:{}};var r=t.createElement("script")
        ;r.type="text/javascript";r.async=true
        ;r.src="/js/thirdParty/new_amplitude_analytics.js?0"
        ;r.onload=function(){if(e.amplitude.runQueuedFunctions){
            e.amplitude.runQueuedFunctions()}else{
            console.log("[Amplitude] Error: could not load SDK")}}
        ;var i=t.getElementsByTagName("script")[0];i.parentNode.insertBefore(r,i)
        ;function s(e,t){e.prototype[t]=function(){
            this._q.push([t].concat(Array.prototype.slice.call(arguments,0)));return this}}
            var o=function(){this._q=[];return this}
            ;var a=["add","append","clearAll","prepend","set","setOnce","unset"]
            ;for(var u=0;u<a.length;u++){s(o,a[u])}n.Identify=o;var c=function(){this._q=[]
                ;return this}
            ;var l=["setProductId","setQuantity","setPrice","setRevenueType","setEventProperties"]
            ;for(var p=0;p<l.length;p++){s(c,l[p])}n.Revenue=c
            ;var d=["init","logEvent","logRevenue","setUserId","setUserProperties","setOptOut","setVersionName","setDomain","setDeviceId","setGlobalUserProperties","identify","clearUserProperties","setGroup","logRevenueV2","regenerateDeviceId","logEventWithTimestamp","logEventWithGroups","setSessionId"]
            ;function v(e){function t(t){e[t]=function(){
                e._q.push([t].concat(Array.prototype.slice.call(arguments,0)))}}
                for(var n=0;n<d.length;n++){t(d[n])}}v(n);n.getInstance=function(e){
                e=(!e||e.length===0?"$default_instance":e).toLowerCase()
                ;if(!n._iq.hasOwnProperty(e)){n._iq[e]={_q:[]};v(n._iq[e])}return n._iq[e]}
            ;e.amplitude=n})(window,document);
        amplitude.getInstance().init(window.location.hostname === "ease.space" ? "73264447f97c4623fb38d92b9e7eaeea": "5f012a5e604acb0283ed11ed8da5414f");
    </script>
</head>

<body id="landingBody">
<!-- Navigation -->
<nav id="mainNav" class="navbar navbar-default navbar-custom navbar-fixed-top affix" style="box-shadow:none;position:absolute;background-color:transparent;">
    <div class="container">
        <div class="navbar-header page-scroll">
            <a class="navbar-brand page-scroll" href="discover"><img src="resources/landing/ease-logo.svg" /></a>
        </div>
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav navbar-right">
                <li>
                    <a href="/product">
                        <fmt:message key="landing.footer.section-product.title" />
                    </a>
                </li>
                <li>
                    <a href="/security">
                        <fmt:message key="landing.header.security-link" />
                    </a>
                </li>
                <li>
                    <a href="/pricing">
                        <fmt:message key="landing.header.price-link" />
                    </a>
                </li>
                <li>
                    <a href="/#/login?skipLanding=true" id="connexionButton">
                        <fmt:message key="landing.header.connexion-link" />
                    </a>
                </li>
            </ul>
        </div>
    </div>
</nav>
<section style="margin-top: 60px;">
    <div class="container">
        <div class="ui stackable grid">
            <div class="one wide column"></div>

            <div class="six wide column">
                <p style="font-size: 48px;font-weight: 300;line-height: 1.22;color: #373b60;margin-bottom:15px;">Goodbye</br><span id="changing" style="font-weight: 300;"></span></br>hello apps</p>
                <p style="font-size: 15px;font-weight: 300;line-height: 1.5;color: #949eb7;margin-bottom: 20px;">
                    <fmt:message key="landing.undervp"/>
                </p>
                <form id="formRedir" class="ui form" action="" method="post" onsubmit="return submitEmail()">
                    <div id="divInput" class="ui action fluid input" style="height: 50px;">
                        <input type="email" name="email" placeholder="<fmt:message key='landing.entermail'/>" style="font-size:14px;" required>
                        <button type="submit" class="sendContactButton ui button" style="font-size: 12px!important;font-weight:300;">
                            <fmt:message key="landing.entermail.button"/>
                        </button>
                    </div>
                </form>
                <p style="margin-top:10px;font-size:15px;font-weight:300;"><a href="/companyContact" style="color:#4fcb6c;"><fmt:message key="landing.underemail"/></a></p>
            </div>
            <div class="rightLanding nine wide column">
                <div class="inTheMac"></div>
                <a href="/product">
                    <button id="discoverButton" class="ui small button">
                        <fmt:message key="landing.discover"/>
                    </button>
                </a>
            </div>
            <div id="counter"  class="sixteen wide column" style="text-align:center;"></div>
            <div class="sixteen wide column" style="text-align:center;">
                <span id="popup2" style="font-size:30px;color: #949eb7;margin-top:15px;font-weight:300">
                    <u id="pop"><fmt:message key="landing.undercounter"/></u>
                    <div id="popupBefore" class="ui fluid popup top left transition">
                        <fmt:message key="landing.undercounter.popup"/>
                    </div>
                </span>
                <span style="font-size:30px;color: #949eb7;margin-top:15px;font-weight:300">
                    <fmt:message key="landing.undercounter.2"/>
                </span>
            </div>
        </div>
    </div>
</section>

<%@ include file="templates/landingPage/landingFooter.jsp" %>
<script src="/jsMinified.v00022/jquery1.12.4.js"></script>
<script src="/jsMinified.v00022/jquery.complexify.js"></script>
<script src="/jsMinified.v00022/bootstrap.js" async></script>
<script src="/jsMinified.v00022/ajaxHandler.js" async></script>
<script src="/jsMinified.v00022/basic-utils.js" async></script>
<script src="/jsMinified.v00022/languageChooser.js" async></script>
<script src="/jsMinified.v00022/tracker.js" async></script>
<script src="semantic/dist/semantic.min.js"></script>
<script type="text/javascript">
    if (window.matchMedia("(max-width: 768px)").matches) {
        $('.inTheMac img').addClass('big');
        $('.inTheMac img').removeClass('large');
    }
</script>
<script>
    $(document).ready(function() {
            $('#pop').popup({
                on        : 'hover',
                position  : 'bottom center',
                hoverable : true
            });
    });
</script>
<script type="text/javascript">
   function submitEmail() {
       document.location.href = "/#/registration?email="+$('#divInput').find("input[name='email']").val();
       return false;
    };
    $(document).ready(function () {
        var count;
        $.get("/api/v1/common/ConnectionNumber", function (data) {
            count = data.connections;
            setCounter(count);
        }, false);

        setInterval(function () {
            $.get("/api/v1/common/ConnectionNumber", function (data) {
                if (data.connections > count)
                    updateCounter(data.connections);
            });
        }, 60000);

        function updateCounter(new_count) {
            var step = 60000 / (new_count - count);
            var timer = setInterval(function () {
                var i;
                var l = $("#counter .digit").length;
                var countString = count.toString();
                if (countString.length > l) {
                    for (i = 0; i < countString.length - l; i++)
                        $("<span class='digit'>0</span>").prependTo($("#counter"));
                }
                for (i = 0; i < countString.length; i++) {
                    var c = $($("#counter .digit")[i]);
                    if (c.text() !== countString[i])
                        c.text(countString[i]);
                }
                if (count >= new_count) {
                    clearInterval(timer);
                    return;
                }
                count++;
            }, step);
        }

        function setCounter(count) {
            var l = $("#counter .digit").length;
            var countString = count.toString();
            if (countString.length > l) {
                for (var i = 0; i < countString.length - l; i++)
                    $("<span class='digit'>0</span>").prependTo($("#counter"));
            }
            for (var i = 0; i < countString.length; i++)
                $($("#counter .digit")[i]).text(countString[i]);
        }
    });
</script>
<script>
    document.addEventListener('DOMContentLoaded',function(event){
        // array with texts to type in typewriter
        var dataText = [<fmt:message key="landing.vp"/>];

        // type one text in the typwriter
        // keeps calling itself until the text is finished
        function typeWriter(text, i, fnCallback) {
            // check if text isn't finished yet
            if (i < (text.length)) {
                // add next character to h1
                document.querySelector("#changing").innerHTML = text.substring(0, i+1) +'<span aria-hidden="true"></span>';

                // wait for a while and call this function again for next character
                setTimeout(function() {
                    typeWriter(text, i + 1, fnCallback)
                }, 100);
            }
            // text finished, call callback if there is a callback function
            else if (typeof fnCallback === 'function') {
                // call callback after timeout
                setTimeout(fnCallback, 1200);
            }
        }
        // start a typewriter animation for a text in the dataText array
        function StartTextAnimation(i) {
            if (typeof dataText[i] === 'undefined'){
                setTimeout(function() {
                    StartTextAnimation(0);
                }, 2000);
            }
            // check if dataText[i] exists
            if (dataText[i] !== undefined && i < dataText[i].length) {
                // text exists! start typewriter animation
                typeWriter(dataText[i], 0, function(){
                    // after callback (and whole text has been animated), start next text
                    StartTextAnimation(i + 1);
                });
            }
        }
        // start the text animation
        StartTextAnimation(0);
    });
</script>
<script type="text/javascript">
    window.addEventListener('load', function () {
        $crisp = [];
        CRISP_WEBSITE_ID = "6e9fe14b-66f7-487c-8ac9-5912461be78a";
        (function () {
            d = document;
            s = d.createElement("script");
            s.src = "https://client.crisp.chat/l.js";
            s.async = 1;
            d.getElementsByTagName("head")[0].appendChild(s);
        })();
    });
</script>
<script type="text/javascript">
    window.addEventListener('load', function () {
        easeTracker.trackEvent("HomepageVisit");
    });
</script>
</body>
</html>