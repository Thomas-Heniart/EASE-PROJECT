<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<c:if test="${language ne 'en' and language ne 'fr_FR'}">
    <c:set var="language" value="en" scope="session"/>
</c:if>
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="com.Ease.Languages.text" />
<html>
<head>
    <title> Ease.space | Product</title>
    <meta name="viewport" content="initial-scale=1, maximum-scale=1" />
    <!-- Description shown in Google -->
    <meta name="description" content="Ease est la homepage qui vous débarrasse des mots de passe. En 1 clic, soyez connecté à vos sites web automatiquement." />
    <!-- Facebook metadata -->
    <meta property="og:url" content="https://ease.space/" />
    <meta property="og:title" content="Ease.space | Le meilleur moyen de se connecter à ses sites préférés." />
    <meta property="og:description" content="Ease est la homepage qui vous débarrasse des mots de passe. En 1 clic, soyez connecté à vos sites web automatiquement." />
    <meta property="og:image" content="https://ease.space/resources/images/fbmeta-fr.png" />
    <meta property="og:type" content="website" />
    <!-- Twitter metadata -->
    <meta name="twitter:card" content="summary_large_image" />
    <meta name="twitter:site" content="@Ease_app" />
    <meta name="twitter:creator" content="@Ease_app" />
    <meta name="twitter:title" content="Ease.space | Le meilleur moyen de se connecter à ses sites préférés." />
    <meta name="twitter:description" content="Ease est la homepage qui vous débarrasse des mots de passe. En 1 clic, soyez connecté à vos sites web automatiquement." />
    <meta name="twitter:image" content="https://ease.space/resources/images/fbmeta-en.png" />
    <meta http-equiv="Content-Type" contentType="text/html; charset=UTF-8" />
    <meta name="viewport" content="initial-scale=1, maximum-scale=1" />
    <meta property="og:image"
          content="https://ease.space/resources/other/fb_letsgo_icon.jpg" />
    <link rel="icon" type="image/png" href="resources/icons/APPEASE.png" />

    <link rel="stylesheet" type="text/css" href="//fonts.googleapis.com/css?family=Raleway:500,700" />
    <link rel="stylesheet" type="text/css" href="/cssMinified.v00009/lib/fonts/museo-font.css" />
    <link rel="stylesheet" href="/cssMinified.v00009/default_style.css" />
    <link rel="stylesheet" href="/cssMinified.v00009/bootstrap.css" />
    <link rel="stylesheet" href="/cssMinified.v00009/lib/fonts/font-awesome-4.2.0/css/font-awesome.min.css" />
    <link rel="stylesheet" href="/cssMinified.v00009/landingPage.css" />
    <link rel="stylesheet" href="/cssMinified.v00009/securityPage.css" />
    <link rel="stylesheet" href="/scss/productPage.scss" />
    <link rel="stylesheet" href="/cssMinified.v00009/lib/semantic.min.css" />

    <link rel="manifest" href="manifest.json">
</head>
<body id="landingBody" style="margin:0;">
<nav id="mainNav" class="navbar navbar-default navbar-custom navbar-fixed-top">
    <div class="container">
        <div class="navbar-header page-scroll">
            <a id="ease-logo" class="navbar-brand page-scroll" href="/"></a>
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
<header>
    <div class="container" style="margin:0;padding-left:0;padding-right:0;width:100%;">
        <div class="intro-text">
            <div class="intro-heading">
                Le gestionnaire de mots de passe</br>sécurisé et intuitif
            </div>
            <div class="intro-lead-in">
                Gérer les mots de passe de son entreprise, c’est distribuer les outils aux bonnes</br>personnes au bon moment, et leur permettre d’accéder facilement et rapidement</br>aux différents comptes.
            </div>
        </div>
    </div>
</header>
<section style="background-color: white">
    <div class="container" style="margin:0;padding-left:0;padding-right:0;width:100%;">
        <div class="product-tab">
            <div class="table">
                <div class="goodbye">
                    <h2 style="font-size:40px;margin-bottom:20px;">Goodbye</h2>
                    Les envois de mots de passe par email, les fichiers partagés désorganisés, les post-its…
                </div>
            </div>
            <div class="tab-nav">
                <div class="hello">
                    <h2 style="font-size:40px;color:#373b60;margin-bottom:20px;">Hello!</h2>
                    Les Apps, un clic et nous vous amènons directement connecté sur votre compte, instantannément.
                </div>
            </div>
        </div>
    </div>

    <div class="ui bottom attached tab" data-tab="first">
        <div class="container">
            <div class="introDiscover">
                <h2>Découvrez le Personal Space</h2>
                Il s’agit d’un dashboard d’Apps. Quelque soit les circonstances, lorsque vous cliquez sur une</br>App, notre robot vous amène directement connecté sur le compte correspondant (si vous ne</br>l’êtiez pas déjà), en remplissant tous les champs et cliquant sur tout les bontons necessaire.
            </div>
            <div class="ui grid discoverSpace">
                <div class="ui three column stackable row">
                    <div class="column">
                        <img src="/resources/images/appsIllimitées.png" height="150px" />
                        <h3>Apps illimitées</h3>
                        Que vous ayez beaucoup de comptes</br>ou nous. Nous intégrons tout, même</br>les sites connectés avec Facebook,</br>Linkedin…
                    </div>
                    <div class="column">
                        <img src="/resources/images/groupes.png" height="150px" />
                        <h3>Groupes illimités</h3>
                        Rangez vos Apps dans des groupes,</br>pour vous organiser au mieux, en</br>fonction de  vos besoins.
                    </div>
                    <div class="column">
                        <img src="/resources/images/multiComptes.png" height="150px" />
                        <h3>Multi-comptes</h3>
                        Vous avez plusieurs compte sur un</br>même site ? Ease vous permet de</br>passer de l’un à l’autre en un clic.
                    </div>
                </div>
                <div class="ui three column stackable row">
                    <div class="column">
                        <img src="/resources/images/favoris.png" height="90px" />
                        <h3>Gérez vos favoris</h3>
                        Intégrez vos favoris parmis votre</br>dashboard d’Apps, pour y accéder</br>facilement.
                    </div>
                    <div class="column">
                        <img src="/resources/images/updates.png" height="90px" />
                        <h3>Détection de vos identifiants</h3>
                        Vous pouvez intégrer facilement les</br>derniers comptes que vous avez créé</br>par un simple Oui (ou Non).
                    </div>
                    <div class="column">
                        <img src="/resources/images/logout.png" height="90px" />
                        <h3>Log out général</h3>
                        La porte d’entrée devient aussi la porte</br>de sortie. En 1 clic déconnectez-vous</br>de toutes les applications utilisées</br>pendant la session.
                    </div>
                </div>
            </div>
        </div>
        <div class="ui top attached tabular menu">
            <a class="item right" data-tab="second" style="font-size:20px;font-weight:300;line-height:1.35;color:#4990e2;">Réduire</a>
        </div>
    </div>
    <div id="more" class="ui top attached tabular menu">
        <a class="item right" data-tab="first" style="font-size:20px;font-weight:300;line-height:1.35;color:#4990e2;">En savoir plus</a>
    </div>
    <div class="container">
        <div class="titleDistrib"><h2>Distribuez les bons outils aux bonnes personnes</h2></div>
        <div class="ui two column stackable grid textDistrib">
            <div class="ui row">
                <div class="four wide column">
                    <h2>Team Space</h2>
                    <p>Les entreprise ont des dizaines de mots de passe à gérer. Chaque employé n’a pas besoin d’avoir accès à tous les comptes de la société. En plus, les besoins d’accès changent dans le temps.</p>
                </div>
                <div class="twelve wide column">
                    <img class="ui huge image" src="/resources/images/teamSpace.png" style="margin-left:35px;border-radius:5px;box-shadow: -5px 5px 10px 0 rgba(0, 0, 0, 0.15);">
                </div>
            </div>
        </div>
        <div class="ui stackable mobile reversed two column grid textOrg">
            <div class="ui row">
                <div class="five wide column">
                    <img class="ui small image" src="/resources/images/organisationApps.png" style="width:300px;height:260px;margin-top:40px;">
                </div>
                <div class="eleven wide column">
                    <h2>Organisation</h2>
                    <p>Créer des Rooms regroupant les membres de votre équipe, par projet, thème, client, ou autre, à l’image de votre entreprise.</p>
                    <p>En envoyant des apps dans des groupes, taggez les membres ayant besoin de l’outil.</p>
                     <p>Vous pouvez aussi envoyer un app à une seule personne.</p>
                </div>
            </div>
        </div>
        <div class="ui segment textControl" style="border:none;box-shadow:none;">
            <img class="ui small right floated image" src="/resources/images/controlTagLouise.png" style="width:360px;height:200px;margin-top:100px;">
            <h2>Contrôle</h2>
            <p>Distribuez les accès automatiquement à une ou plusieurs personnes.</p>
            <p>Notre technologie permet de donner les accès sans que les collaborateurs n’aient besoin de connaître les mots de passe.</p>
             <p>Programmez les arrivées et départs des collaborateurs et révoquez les accès en un clic.</p>
        </div>
        <div class="ui segment textShare" style="border:none;box-shadow:none;">
            <img class="ui small left floated image" src="/resources/images/partageApps.png" style="width:350px;height:350px;margin-top:30px;">
            <h2>Partage</h2>
            <p>Qu’un compte soit partagé entre plusieurs personnes, ou que chaque utilisateur ait ses propres identifiants, ou que ce soit un intranet, un SSO ou un site sans identifiants, vous pouvez le partager.</p>
            <p>Vous pouvez différencier chaque compte et de les distribuer facilement à l’ensemble de l’équipe ou juste certaines  personnes.</p>
             <p>L’outil est collaboratif, les membres peuvent partager des outils au sein des Rooms dont ils font partie.</p>
        </div>
    </div>
    <div class="ui divider"></div>
    <div class="container">
        <div class="ui clearing segment textPlace" style="border:none;box-shadow:none;">
            <a class="ui right floated blue button" href="/pricing" style="margin-top:80px;margin-right:30px;color:#ffffff;font-size: 20px;font-weight: bold;">Découvrez nos plans</a>
            <h2>Mise en place</h2>
            <p>Invitez vos membres et ajoutez les comptes web dont vous avez besoin, ou laissez vos collaborateurs le faire eux même.</p>
             <p>La distribution des comptes se fait automatiquement.</p>
            <a class="ui right floated button" href="/" style="margin-top:-80px;margin-right:30px;font-size: 20px;font-weight: bold;">Contactez l'équipe commerciale</a>
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
<script src="/jsMinified.v00015/landingPage.js" async></script>
<script src="/jsMinified.v00015/basic-utils.js" async></script>
<script src="/jsMinified.v00015/languageChooser.js" async></script>
<script src="/jsMinified.v00015/tracker.js" async></script>
<script type="text/javascript">
    $(document).ready(function(){
        $('.ui.menu').on('click', function() {
            $('#more').toggleClass('hidden');
            $('.ui.tab').toggleClass('active');
            return false;
        });
    });
</script>
<script type="text/javascript">
    $(document).ready(function () {
        var AFFIX_TOP_LIMIT = 560;
        var AFFIX_OFFSET = 80;

        var $menu = $("#menu"),
            $btn = $("#menu-toggle");

        $("#menu-toggle").on("click", function () {
            $menu.toggleClass("open");
            return false;
        });

//        $('.ui.button').on('click', function() {
//                // programmatically activating tab
//                $.tab('change tab', 'tab-name');
//        });
//        $('.top.menu .item').tab();


        $(".docs-nav").each(function () {
            var $affixNav = $(this),
                $container = $affixNav.parent(),
                affixNavfixed = false,
                originalClassName = this.className,
                current = null,
                $links = $affixNav.find("a");

            function getClosestHeader(top) {
                var last = $links.first();

                if (top < AFFIX_TOP_LIMIT) {
                    return last;
                }

                for (var i = 0; i < $links.length; i++) {
                    var $link = $links.eq(i),
                        href = $link.attr("href");

                    if (href.charAt(0) === "#" && href.length > 1) {
                        var $anchor = $(href).first();

                        if ($anchor.length > 0) {
                            var offset = $anchor.offset();

                            if (top < offset.top - AFFIX_OFFSET) {
                                return last;
                            }

                            last = $link;
                        }
                    }
                }
                return last;
            }


            $(window).on("scroll", function (evt) {
                var top = window.scrollY,
                    height = $affixNav.outerHeight(),
                    max_bottom = $container.offset().top + $container.outerHeight(),
                    bottom = top + height + AFFIX_OFFSET;

                if (affixNavfixed) {
                    if (top <= AFFIX_TOP_LIMIT) {
                        $affixNav.removeClass("fixed");
                        $affixNav.css("top", 0);
                        affixNavfixed = false;
                    } else if (bottom > max_bottom) {
                        $affixNav.css("top", (max_bottom - height) - top);
                    } else {
                        $affixNav.css("top", AFFIX_OFFSET);
                    }
                } else if (top > AFFIX_TOP_LIMIT) {
                    $affixNav.addClass("fixed");
                    affixNavfixed = true;
                }

                var $current = getClosestHeader(top);

                if (current !== $current) {
                    $affixNav.find(".active").removeClass("active");
                    $current.addClass("active");
                    current = $current;
                }
            });
        });
    });
</script>
<script type="text/javascript">
    window.addEventListener('load',function(){
        $('.signUpButton').click(function(){
            easeTracker.trackEvent($(this).attr("trackEvent"));
            easeSignUpPopup.open();
        });
    });
</script>
<script type="text/javascript">
    window.addEventListener('load',function(){
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
    window.addEventListener('load',function(){
        (function(e,t){var n=e.amplitude||{_q:[],_iq:{}};var r=t.createElement("script");r.type="text/javascript";
            r.async = true;
            r.src = "/jsMinified.v00015/amplitude-analytics.js";
            r.onload=function(){e.amplitude.runQueuedFunctions()};var i=t.getElementsByTagName("script")[0];
            i.parentNode.insertBefore(r,i);function s(e,t){e.prototype[t]=function(){this._q.push([t].concat(Array.prototype.slice.call(arguments,0)));
                return this}}var o=function(){this._q=[];return this};var a=["add","append","clearAll","prepend","set","setOnce","unset"];
            for(var u=0;u<a.length;u++){s(o,a[u])}n.Identify=o;var c=function(){this._q=[];return this;
            };var p=["setProductId","setQuantity","setPrice","setRevenueType","setEventProperties"];
            for(var l=0;l<p.length;l++){s(c,p[l])}n.Revenue=c;var d=["init","logEvent","logRevenue","setUserId","setUserProperties","setOptOut","setVersionName","setDomain","setDeviceId","setGlobalUserProperties","identify","clearUserProperties","setGroup","logRevenueV2","regenerateDeviceId"];
            function v(e){function t(t){e[t]=function(){e._q.push([t].concat(Array.prototype.slice.call(arguments,0)));
            }}for(var n=0;n<d.length;n++){t(d[n])}}v(n);n.getInstance=function(e){e=(!e||e.length===0?"$default_instance":e).toLowerCase();
                if(!n._iq.hasOwnProperty(e)){n._iq[e]={_q:[]};v(n._iq[e])}return n._iq[e]};e.amplitude=n;
        })(window,document);

        /* Prod */
        //amplitude.getInstance().init("74f6ebfba0c7743a0c63012dc3a9fef0");

        /* Test */
        amplitude.getInstance().init("73264447f97c4623fb38d92b9e7eaeea");
        easeTracker.trackEvent("SecurityPageVisit");
    });
</script>
</body>
</html>
