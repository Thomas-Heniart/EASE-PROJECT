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
<fmt:setBundle basename="languages/text"/>
<html lang="${language}">
<head>
    <title> Ease.space | Le meilleur moyen de se connecter à ses sites préférés.</title>
    <meta name="google" content="notranslate" />
    <!-- Description shown in Google -->
    <!-- Facebook metadata -->
    <meta name="description"
          content="Mettez facilement en place une politique de sécurité au niveau de l’utilisation, le partage et le stockage des mots de passe de l’entreprise."/>
    <meta property="og:url" content="https://ease.space/"/>
    <meta property="og:type" content="website"/>
    <meta property="og:title" content="Ease.space - Prenez soin des mots de passe de votre entreprise"/>
    <meta property="og:image" content="https://ease.space/resources/metadescription.png"/>
    <meta property="og:logo" content="https://ease.space/resources/icons/APPEASE.png"/>
    <meta property="og:description"
          content="Mettez facilement en place une politique de sécurité au niveau de l’utilisation, le partage et le stockage des mots de passe de l’entreprise."/>
    <meta property="og:image" content="https://ease.space/resources/images/metadescription.png"/>
    <!-- Twitter metadata -->
    <meta name="twitter:card" content="summary_large_image"/>
    <meta name="twitter:site" content="@ease_space"/>
    <meta name="twitter:creator" content="@ease_space"/>
    <meta name="twitter:title" content="Ease.space - Prenez soin des mots de passe de votre entreprise"/>
    <meta name="twitter:description"
          content="Mettez facilement en place une politique de sécurité au niveau de l’utilisation, le partage et le stockage des mots de passe de l’entreprise."/>
    <meta name="twitter:image" content="https://ease.space/resources/images/metadescription.png"/>
    <meta http-equiv="Content-Type" contentType="text/html; charset=UTF-8"/>
    <meta name="viewport" content="initial-scale=1, maximum-scale=1"/>

    <link rel="icon" type="image/png" href="resources/icons/APPEASE.png"/>
    <link rel="manifest" href="manifest.json">
    <link rel="stylesheet" type="text/css" href="//fonts.googleapis.com/css?family=Raleway:500,700"/>
    <link rel="stylesheet" href="semantic/dist/semantic.min.css">
    <link rel="stylesheet" type="text/css" href="/cssMinified.v00052/lib/fonts/museo-font.css"/>
    <link rel="stylesheet" href="/cssMinified.v00052/default_style.css"/>
    <link rel="stylesheet" href="/cssMinified.v00052/bootstrap.css"/>
    <link rel="stylesheet" href="/cssMinified.v00052/landingPage.css"/>
    <link rel="stylesheet" type="text/css"
          href="/cssMinified.v00052/lib/fonts/font-awesome-4.2.0/css/font-awesome.min.css"/>
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
    <!-- Google Tag Manager -->
    <script>(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':
      new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0],
      j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=
      'https://www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);
    })(window,document,'script','dataLayer','GTM-T8BRML7');</script>
    <!-- End Google Tag Manager -->
</head>

<body id="landingBody" class="notranslate">
<!-- Google Tag Manager (noscript) -->
<noscript><iframe src="https://www.googletagmanager.com/ns.html?id=GTM-T8BRML7"
                  height="0" width="0" style="display:none;visibility:hidden"></iframe></noscript>
<!-- End Google Tag Manager (noscript) -->
<!-- Navigation -->
<nav id="mainNav" class="navbar navbar-default navbar-custom navbar-fixed-top affix" style="box-shadow:none;position:absolute;background-color:transparent;">
    <div class="container">
        <div class="navbar-header page-scroll">
            <a class="navbar-brand page-scroll" href="discover"><img src="resources/images/ease_logo_blue.svg" /></a>
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
                    <a href="/rgpd">
                        <fmt:message key="landing.header.gdpr" />
                    </a>
                </li>
                <li>
                    <a href="https://blog.ease.space">
                        <fmt:message key="landing.header.blog" />
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
<section class="sectionLanding">
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
<script src="/jsMinified.v00023/jquery1.12.4.js"></script>
<script src="/jsMinified.v00023/jquery.complexify.js"></script>
<script src="/jsMinified.v00023/bootstrap.js" async></script>
<script src="/jsMinified.v00023/ajaxHandler.js" async></script>
<script src="/jsMinified.v00023/basic-utils.js" async></script>
<script src="/jsMinified.v00023/languageChooser.js" async></script>
<script src="/jsMinified.v00023/tracker.js" async></script>
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
       document.location.href = "/#/teamCreation?plan_id=0&email="+$('#divInput').find("input[name='email']").val();
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
</body>
</html>