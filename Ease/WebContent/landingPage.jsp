<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language"
       value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"
       scope="session"/>
<c:if test="${language ne 'en' and language ne 'fr_FR'}">
    <c:set var="language" value="en" scope="session"/>
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
    <link rel="stylesheet" type="text/css" href="/cssMinified.v00009/lib/fonts/museo-font.css"/>
    <link rel="stylesheet" href="/cssMinified.v00009/default_style.css"/>
    <link rel="stylesheet" href="/cssMinified.v00009/bootstrap.css"/>
    <link rel="stylesheet" href="/cssMinified.v00009/landingPage.css"/>
    <link rel="stylesheet" href="/cssMinified.v00009/lib/semantic.min.css" />
    <link rel="stylesheet" type="text/css"
          href="/cssMinified.v00009/lib/fonts/font-awesome-4.2.0/css/font-awesome.min.css"/>
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
                <li class="hidden">
                    <a href="#page-top"></a>
                </li>
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
                    <a href="/?skipLanding=true" id="connexionButton">
                        <fmt:message key="landing.header.connexion-link" />
                    </a>
                </li>
            </ul>
        </div>
    </div>
</nav>
<section style="margin-top: 20px;">
    <div class="container">
        <div class="ui stackable grid">
            <div class="one wide column"></div>

            <div class="six wide column">
                <p style="font-size: 48px;font-weight: 300;line-height: 1.22;color: #373b60;margin-bottom:15px;">Goodbye</br><span id="changing" style="font-weight: 300;">mots de passe</span>,</br>hello apps</p>
                <p style="font-size: 15px;font-weight: 300;line-height: 1.5;color: #949eb7;">
                    Une app est un compte web,</br>
                    ajouté par vous ou partagé au sein de votre équipe.</br>
                    Un clic suffit pour y accéder, directement connecté.</p>
                <div class="ui action fluid input" style="height: 50px;">
                    <input id="input" type="text" name="email" placeholder="Entrez votre adresse email">
                    <button type="submit" class="sendContactButton ui button" style="font-size: 15px!important;font-weight:300;">
                        ESSAYER
                    </button>
                </div>
                <p style="margin-top: 10px;font-size: 14px;font-weight: 300;color: #949eb7;">C’est gratuit, pas de CB requise</p>
            </div>
            <div class="rightLanding nine wide column">
                <div class="inTheMac">
                    <img class="ui fluid large image" src="/resources/images/screen.png"/>
                    <button id="discoverButton" class="ui small button">
                        Découvrir
                    </button>
                </div>
            </div>
        </div>
    </div>
</section>

<%@ include file="templates/landingPage/landingFooter.jsp" %>
<%@ include file="templates/landingPage/registrationPopup.jsp" %>
<script src="/jsMinified.v00015/jquery1.12.4.js"></script>
<script src="/jsMinified.v00015/jquery.complexify.js"></script>
<script src="/jsMinified.v00015/bootstrap.js" async></script>
<script src="/jsMinified.v00015/ajaxHandler.js" async></script>
<script src="/jsMinified.v00015/registrationPopup.js" async></script>
<%--<script src="/jsMinified.v00015/landingPage.js" async></script>--%>
<script src="/jsMinified.v00015/basic-utils.js" async></script>
<script src="/jsMinified.v00015/languageChooser.js" async></script>
<script src="/jsMinified.v00015/tracker.js" async></script>
<script type="text/javascript">

    $('button.sendContactButton').on('click', function() {
        if ($('#input').find("input[name='email']").val().length) {
            console.log('truc');
            document.location.href = "/teams#/registration?$('input[name=\'email\']')";
        }
        else {
            document.location.href = "/teams#/registration";
            console.log('truc');

        }
    })

    window.addEventListener('load', function () {

        $('.signUpButton').click(function () {
            easeTracker.trackEvent($(this).attr("trackEvent"));
            easeSignUpPopup.open();
        });


    });
</script>
<script>
    document.addEventListener('DOMContentLoaded',function(event){
        // array with texts to type in typewriter
        var dataText = ['mots de passe', 'comptes', 'identifiants', 'liens', 'post-its', 'fichiers excel'];

        // type one text in the typwriter
        // keeps calling itself until the text is finished
        function typeWriter(text, i, fnCallback) {
            // chekc if text isn't finished yet
            if (i < (text.length)) {
                // add next character to h1
                document.querySelector("#changing").innerHTML = text.substring(0, i+1) +'<span aria-hidden="true"></span>';

                // wait for a while and call this function again for next character
                setTimeout(function() {
                    typeWriter(text, i + 1, fnCallback)
                }, 100);
            }
            // text finished, call callback if there is a callback function
            else if (typeof fnCallback == 'function') {
                // call callback after timeout
                setTimeout(fnCallback, 1200);
            }
        }
        // start a typewriter animation for a text in the dataText array
        function StartTextAnimation(i) {
            if (typeof dataText[i] == 'undefined'){
                setTimeout(function() {
                    StartTextAnimation(0);
                }, 2000);
            }
            // check if dataText[i] exists
            if (i < dataText[i].length) {
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
            s.src = "/jsMinified.v00015/thirdParty/crisp.js";
            s.async = 1;
            d.getElementsByTagName("head")[0].appendChild(s);
        })();
    });
</script>
<script type="text/javascript">
    window.addEventListener('load', function () {
        (function (e, t) {
            var n = e.amplitude || {_q: [], _iq: {}};
            var r = t.createElement("script");
            r.type = "text/javascript";
            r.async = true;
            r.src = "/jsMinified.v00015/amplitude-analytics.js";
            r.onload = function () {
                e.amplitude.runQueuedFunctions()
            };
            var i = t.getElementsByTagName("script")[0];
            i.parentNode.insertBefore(r, i);
            function s(e, t) {
                e.prototype[t] = function () {
                    this._q.push([t].concat(Array.prototype.slice.call(arguments, 0)));
                    return this
                }
            }

            var o = function () {
                this._q = [];
                return this
            };
            var a = ["add", "append", "clearAll", "prepend", "set", "setOnce", "unset"];
            for (var u = 0; u < a.length; u++) {
                s(o, a[u])
            }
            n.Identify = o;
            var c = function () {
                this._q = [];
                return this;
            };
            var p = ["setProductId", "setQuantity", "setPrice", "setRevenueType", "setEventProperties"];
            for (var l = 0; l < p.length; l++) {
                s(c, p[l])
            }
            n.Revenue = c;
            var d = ["init", "logEvent", "logRevenue", "setUserId", "setUserProperties", "setOptOut", "setVersionName", "setDomain", "setDeviceId", "setGlobalUserProperties", "identify", "clearUserProperties", "setGroup", "logRevenueV2", "regenerateDeviceId"];

            function v(e) {
                function t(t) {
                    e[t] = function () {
                        e._q.push([t].concat(Array.prototype.slice.call(arguments, 0)));
                    }
                }

                for (var n = 0; n < d.length; n++) {
                    t(d[n])
                }
            }

            v(n);
            n.getInstance = function (e) {
                e = (!e || e.length === 0 ? "$default_instance" : e).toLowerCase();
                if (!n._iq.hasOwnProperty(e)) {
                    n._iq[e] = {_q: []};
                    v(n._iq[e])
                }
                return n._iq[e]
            };
            e.amplitude = n;
        })(window, document);

        /* Prod */
        //amplitude.getInstance().init("74f6ebfba0c7743a0c63012dc3a9fef0");

        /* Test */
        amplitude.getInstance().init("73264447f97c4623fb38d92b9e7eaeea");
        easeTracker.trackEvent("HomepageVisit");
    });
</script>
</body>
</html>