<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<c:if test="${language ne 'en' and language ne 'fr_FR'}">
    <c:set var="language" value="fr_FR" scope="session"/>
</c:if>
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="com.Ease.Languages.text" />
<!DOCTYPE html>
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
    <link rel="stylesheet" type="text/css" href="/cssMinified.v00014/lib/fonts/museo-font.css"/>
    <link rel="stylesheet" href="/cssMinified.v00014/default_style.css"/>
    <link rel="stylesheet" href="/cssMinified.v00014/bootstrap.css"/>
    <link rel="stylesheet" href="/cssMinified.v00014/lib/fonts/font-awesome-4.2.0/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="/cssMinified.v00014/landingPage.css"/>
    <link rel="stylesheet" href="/cssMinified.v00014/securityPage.css"/>
    <link rel="stylesheet" href="/cssMinified.v00014/productPage.css"/>
    <link rel="stylesheet" href="semantic/dist/semantic.min.css">


    <link rel="manifest" href="manifest.json">
</head>
<body id="landingBody" style="margin:0;">
<nav id="mainNav" class="navbar navbar-default navbar-custom navbar-fixed-top">
    <div class="container">
        <div class="navbar-header page-scroll">
            <a id="ease-logo" class="navbar-brand page-scroll" href="/discover"></a>
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
        </div>
    </div>
</header>
<section style="background-color: white">
    <div class="container" style="margin:0;padding-left:0;padding-right:0;width:100%;">
        <div id="tableHello" class="product-tab">
            <div class="table">
                <div class="goodbye">
                    <h2 style="font-size:40px;margin-bottom:20px;">Goodbye</h2>
                    Les envois de mots de passe par email, les fichiers partagés, les post-its...
                </div>
            </div>
            <div class="tab-nav">
                <div class="hello">
                    <h2 style="font-size:40px;color:#373b60;margin-bottom:20px;">Hello!</h2>
                    Les apps, un clic suffit pour accéder au compte connecté.
                </div>
            </div>
        </div>
    </div>

    <div class="ui bottom attached tab" data-tab="first">
        <div class="container">
            <div class="ui grid discoverSpace">
                <div class="ui three column stackable row" style="display:inline-block;float:left">
                    <div class="column" style="display:inline-block;float:left">
                        <img src="/resources/images/appsIllimitees.png" height="150px" />
                        <h3>Apps illimitées</h3>
                        <p>Que vous ayez beaucoup de comptes ou nous. Nous intégrons tout, même les sites connectés avec Facebook, Linkedin…</p>
                    </div>
                    <div class="column" style="display:inline-block;float:left">
                        <img src="/resources/images/groupes.png" height="150px" />
                        <h3>Groupes illimités</h3>
                        <p>Ranger vos apps dans des groupes pour vous organiser en fonction de vos besoins.</p>
                    </div>
                    <div class="column" style="display:inline-block;float:left">
                        <img src="/resources/images/multiComptes.png" height="150px" />
                        <h3>Multi-comptes</h3>
                        <p>Vous avez plusieurs compte sur un même site ? Ease vous permet de passer de l’un à l’autre en un clic.</p>
                    </div>
                </div>
                <div class="ui three column stackable row" style="display:inline-block;float:left">
                    <div class="column" style="display:inline-block;float:left">
                        <img src="/resources/images/favoris.png" height="90px" />
                        <h3>Gérez vos favoris</h3>
                        <p>Intégrez vos favoris parmis votre dashboard d’Apps, pour y accéder facilement.</p>
                    </div>
                    <div class="column" style="display:inline-block;float:left">
                        <img src="/resources/images/updates.png" height="90px" />
                        <h3>Détection de vos identifiants</h3>
                        <p>Vous pouvez intégrer facilement les derniers comptes que vous avez créé par un simple Oui (ou Non).</p>
                    </div>
                    <div class="column" style="display:inline-block;float:left">
                        <img src="/resources/images/logout.png" height="90px" />
                        <h3>Log out général</h3>
                        <p>La porte d’entrée devient aussi la porte de sortie. En 1 clic déconnectez-vous de toutes les applications utilisées pendant la session.</p>
                    </div>
                </div>
            </div>
        </div>
        <div class="ui top attached tabular menu">
            <a class="item right" data-tab="second" style="font-size:20px;font-weight:300;line-height:1.35;color:#4990e2;"><u>Réduire</u></a>
        </div>
    </div>
    <div id="more" class="ui top attached tabular menu">
        <a class="item right" data-tab="first" style="font-size:20px;font-weight:300;line-height:1.35;color:#4990e2;"><u>En savoir plus</u></a>
    </div>
    <div class="container">
        <div class="ui two column stackable grid textDistrib">
            <div class="ui row" style="display:inline-block;float:left">
                <div class="four wide column" style="display:inline-block;float:left">
                    <h2>Team Space</h2>
                    <p>En entreprise : organisez qui a accès à quels outils et visualisez facilement qui peut accéder aux différents comptes.</p>
                </div>
                <div class="twelve wide column" style="display:inline-block;float:left">
                    <img class="ui huge image" src="/resources/images/teamSpace.png">
                </div>
            </div>
        </div>
        <div class="ui stackable two column grid textOrg">
            <div class="ui mobile reversed row" style="display:inline-block;float:left">
                <div class="five wide computer only column" style="display:inline-block;float:left">
                    <img class="ui small image" src="/resources/images/organisationApps.png">
                </div>
                <div class="eleven wide column" style="display:inline-block;float:left">
                    <h2>Organisation</h2>
                    <p>Créez des Rooms pour regrouper des membres par équipe, projet, client, en fonction de votre organisation.</p>
                    <p>Envoyez des Apps dans des Rooms et taggez les membres ayant besoin de l’outil, il recevront l’accès automatiquement.</p>
                     <p style="margin-top:-16px;">Choisissez d’envoyer des comptes a certaines personnes, lorsque d’autres n’ont pas besoin d’y accéder.</p>
                </div>
                <div class="five wide mobile only column" style="display:inline-block;float:left">
                    <img class="ui small image" src="/resources/images/organisationApps.png">
                </div>
            </div>
        </div>
        <div class="ui stackable two column grid textControl">
            <div class="ui row" style="display:inline-block;float:left">
                <div class="eleven wide column" style="display:inline-block;float:left">
                    <h2>Contrôle</h2>
                    <p>Donnez l’accès sans que le collaborateur ait besoin de connaître le mot de passe.</p>
                    <p>Programmez les départs des collaborateurs et révoquez leurs accès en un clic.</p>
                    <p>Visualisez qui accède à quels outils.</p>
                </div>
                <div class="five wide column" style="display:inline-block;float:left">
                    <img class="ui small right floated image" src="/resources/images/controlTagLouise.png">
                </div>
            </div>
        </div>
        <div class="ui stackable two column grid textShare">
            <div class="ui mobile reversed row" style="display:inline-block;float:left">
                <div class="five wide computer only column" style="display:inline-block;float:left">
                    <img class="ui small left floated image" src="/resources/images/partageApps.png">
                </div>
                <div class="eleven wide column" style="display:inline-block;float:left">
                    <h2>Partage</h2>
                    <p>Que les identifiants soient partagés entre plusieurs membres de l’équipe ou que chaque collaborateur ait ses propres identifiants...</p>
                    <p>Que ce soit un site sans identifiants, un intranet, un SSO, un blog...</p>
                    <p style="margin-top:0;">Vous pouvez les partager à votre équipe sur Ease.space.</p>
                </div>
                <div class="five wide mobile only column" style="display:inline-block;float:left">
                    <img class="ui small left floated image" src="/resources/images/partageApps.png">
                </div>
            </div>
        </div>
    </div>
    <div class="ui divider" style="margin: 80px 0;"></div>
    <div class="container">
        <div class="ui stackable two column grid textPlace">
            <div class="ui row" style="display:inline-block;float:left">
                <div class="nine wide column" style="display:inline-block;float:left">
                    <h2>Mise en place</h2>
                    <p>Invitez les membres de votre équipe.</p>
                    <p>Ajoutez les comptes web dont vous avez besoin.</p>
                    <p>Laissez vos collaborateurs ajouter des comptes eux même.</p>
                </div>
                <div class="seven wide column" style="display:inline-block;float:left">
                    <a class="ui right floated blue button" href="/pricing" style="margin-top:60px;color:#ffffff;font-size: 20px;font-weight: bold;">Découvrez nos plans</a>
                    <a class="ui right floated button" href="/companyContact" style="margin-top: 30px;font-size: 20px;font-weight: bold;">Contactez l'équipe commerciale</a>
                </div>
            </div>
        </div>
    </div>
</section>
<%@ include file="templates/landingPage/landingFooter.jsp" %>
<script src="/jsMinified.v00019/jquery1.12.4.js"></script>
<script src="/jsMinified.v00019/jquery.complexify.js"></script>
<script src="/jsMinified.v00019/bootstrap.js" async></script>
<script src="/jsMinified.v00019/ajaxHandler.js" async></script>
<script src="/jsMinified.v00019/landingPage.js" async></script>
<script src="/jsMinified.v00019/basic-utils.js" async></script>
<script src="/jsMinified.v00019/languageChooser.js" async></script>
<script src="/jsMinified.v00019/tracker.js" async></script>
<script>
    if (window.matchMedia("(max-width: 768px)").matches) {
        $('#tableHello').removeClass('product-tab');
    }
</script>
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
