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
    <link rel="stylesheet" type="text/css" href="/cssMinified.v00020/lib/fonts/museo-font.css"/>
    <link rel="stylesheet" href="/cssMinified.v00020/default_style.css"/>
    <link rel="stylesheet" href="/cssMinified.v00020/bootstrap.css"/>
    <link rel="stylesheet" href="/cssMinified.v00020/lib/fonts/font-awesome-4.2.0/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="/cssMinified.v00020/landingPage.css"/>
    <link rel="stylesheet" href="/cssMinified.v00020/securityPage.css"/>
    <link rel="stylesheet" href="/cssMinified.v00020/productPage.css"/>
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
                <fmt:message key="product.title"/>
            </div>
            <button class="button-product big centered ui button" onclick="location.href='/teams#/registration'">
                <fmt:message key="product.button"/>
            </button>
        </div>
    </div>
</header>
<section style="background-color: white">
    <div class="container" style="margin:0;padding-left:0;padding-right:0;width:100%;">
        <div id="tableHello" class="product-tab">
            <div class="table">
                <div class="goodbye">
                    <h2 style="font-size:40px;margin-bottom:20px;">Goodbye</h2>
                    <fmt:message key="product.goodbye"/>
                </div>
            </div>
            <div class="tab-nav">
                <div class="hello">
                    <h2 style="font-size:40px;color:#373b60;margin-bottom:20px;">Hello!</h2>
                    <fmt:message key="product.hello"/>
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
                        <h3><fmt:message key="product.dropDown.title.1"/></h3>
                        <p><fmt:message key="product.dropDown.text.1"/></p>
                    </div>
                    <div class="column" style="display:inline-block;float:left">
                        <img src="/resources/images/groupes.png" height="150px" />
                        <h3><fmt:message key="product.dropDown.title.2"/></h3>
                        <p><fmt:message key="product.dropDown.text.2"/></p>
                    </div>
                    <div class="column" style="display:inline-block;float:left">
                        <img src="/resources/images/multiComptes.png" height="150px" />
                        <h3><fmt:message key="product.dropDown.title.3"/></h3>
                        <p><fmt:message key="product.dropDown.text.3"/></p>
                    </div>
                </div>
                <div class="ui three column stackable row" style="display:inline-block;float:left">
                    <div class="column" style="display:inline-block;float:left">
                        <img src="/resources/images/favoris.png" height="90px" />
                        <h3><fmt:message key="product.dropDown.title.4"/></h3>
                        <p><fmt:message key="product.dropDown.text.4"/></p>
                    </div>
                    <div class="column" style="display:inline-block;float:left">
                        <img src="/resources/images/updates.png" height="90px" />
                        <h3><fmt:message key="product.dropDown.title.5"/></h3>
                        <p><fmt:message key="product.dropDown.text.5"/></p>
                    </div>
                    <div class="column" style="display:inline-block;float:left">
                        <img src="/resources/images/logout.png" height="90px" />
                        <h3><fmt:message key="product.dropDown.title.6"/></h3>
                        <p><fmt:message key="product.dropDown.text.6"/></p>
                    </div>
                </div>
            </div>
        </div>
        <div class="ui top attached tabular menu">
            <a class="item right" data-tab="second" style="font-size:20px;font-weight:300;line-height:1.35;color:#4990e2;"><u><fmt:message key="product.showLess"/></u></a>
        </div>
    </div>
    <div id="more" class="ui top attached tabular menu">
        <a class="item centered" data-tab="first" style="margin-top:10px;font-size:20px;font-weight:bold;line-height:1.35;color:#4990e2;"><u><fmt:message key="product.showMore"/></u></a>
    </div>
    <div class="container">
        <div class="ui two column stackable grid textDistrib">
            <div class="ui row" style="display:inline-block;float:left">
                <div class="four wide column" style="display:inline-block;float:left">
                    <h2><fmt:message key="product.teamSpace.title"/></h2>
                    <p><fmt:message key="product.teamSpace.text"/></p>
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
                    <p><fmt:message key="product.organisation.text.1"/></p>
                    <p><fmt:message key="product.organisation.text.2"/></p>
                     <p style="margin-top:-16px;"><fmt:message key="product.organisation.text.3"/></p>
                </div>
                <div class="five wide mobile only column" style="display:inline-block;float:left">
                    <img class="ui small image" src="/resources/images/organisationApps.png">
                </div>
            </div>
        </div>
        <div class="ui stackable two column grid textControl">
            <div class="ui row" style="display:inline-block;float:left">
                <div class="eleven wide column" style="display:inline-block;float:left">
                    <h2><fmt:message key="product.control.title"/></h2>
                    <p><fmt:message key="product.control.text.1"/></p>
                    <fmt:message key="product.control.text.2"/>
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
                    <h2><fmt:message key="product.share.title"/></h2>
                    <p><fmt:message key="product.share.text.1"/></p>
                    <p><fmt:message key="product.share.text.2"/></p>
                    <p style="margin-top:0;"><fmt:message key="product.share.text.3"/></p>
                </div>
                <div class="five wide mobile only column" style="display:inline-block;float:left">
                    <img class="ui small left floated image" src="/resources/images/partageApps.png">
                </div>
            </div>
        </div>
        <div class="ui stackable two column grid textNotifications">
            <div class="ui row" style="display:inline-block;float:left">
                <div class="eleven wide column" style="display:inline-block;float:left">
                    <h2><fmt:message key="product.notifications.title"/></h2>
                    <p><fmt:message key="product.notifications.text.1"/></p>
                    <p><fmt:message key="product.notifications.text.2"/></p>
                </div>
                <div class="five wide column" style="display:inline-block;float:left">
                    <img class="ui small right floated image" src="/resources/images/notifications_product.png">
                </div>
            </div>
        </div>
        <div class="ui stackable two column grid textMobile">
            <div class="ui mobile reversed row" style="display:inline-block;float:left">
                <div class="five wide computer only column" style="display:inline-block;float:left">
                    <img class="ui small left floated image" src="/resources/images/black_phone.png">
                </div>
                <div class="eleven wide column" style="display:inline-block;float:left">
                    <h2><fmt:message key="product.mobile.title"/></h2>
                    <p><fmt:message key="product.mobile.text.1"/></p>
                    <p class="last"><fmt:message key="product.mobile.text.2"/></p>
                    <img class="apple" src="/resources/icons/app_store.badge.svg"/>
                    <img class="google" src="/resources/icons/google-play-badge.png"/>
                </div>
                <div class="five wide mobile only column" style="display:inline-block;float:left">
                    <img class="ui small left floated image" src="/resources/images/black_phone.png">
                </div>
            </div>
        </div>
    </div>
    <div class="ui divider" style="margin:0;"></div>
    <div class="container">
        <div class="comparativeTable">
            <h2><fmt:message key="product.table.title"/></h2>
            <table class="ui unstackable celled table" style="color:#949eb7">
                <thead>
                <tr style="background-color:#f8f8f8;">
                    <th class="center aligned" style="color:#949eb7;font-weight:bold;text-align:left;"><fmt:message key="product.table.feature"/></th>
                    <th class="center aligned" style="color:#4fcb6c;font-weight:bold;">EASE.SPACE</th>
                    <th class="center aligned" style="color:#949eb7;font-weight:bold;">DASHLANE</th>
                    <th class="center aligned" style="color:#949eb7;font-weight:bold;">LASTPASS</th>
                    <th class="center aligned" style="color:#949eb7;font-weight:bold;">1PASSWORD</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td><fmt:message key="product.table.feature.1"/></td>
                    <td class="center aligned" style="color:#4fcb6c;font-size:20px;"><i class="check circle icon"></i></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
                <tr style="background-color: #f8f8f8;">
                    <td><fmt:message key="product.table.feature.2"/></td>
                    <td class="center aligned" style="color:#4fcb6c;font-size:20px;"><i class="check circle icon"></i></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td><fmt:message key="product.table.feature.3"/></td>
                    <td class="center aligned" style="color:#4fcb6c;font-size:20px;"><i class="check circle icon"></i></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
                <tr style="background-color: #f8f8f8;">
                    <td><fmt:message key="product.table.feature.4"/></td>
                    <td class="center aligned" style="color:#4fcb6c;font-size:20px;"><i class="check circle icon"></i></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td><fmt:message key="product.table.feature.5"/></td>
                    <td class="center aligned" style="color:#4fcb6c;font-size:20px;"><i class="check circle icon"></i></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
                <tr style="background-color: #f8f8f8;">
                    <td><fmt:message key="product.table.feature.6"/></td>
                    <td class="center aligned" style="color:#4fcb6c;font-size:20px;"><i class="check circle icon"></i></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td><fmt:message key="product.table.feature.7"/></td>
                    <td class="center aligned" style="color:#4fcb6c;font-size:20px;"><i class="check circle icon"></i></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
                <tr style="background-color: #f8f8f8;">
                    <td><fmt:message key="product.table.feature.8"/></td>
                    <td class="center aligned" style="color:#4fcb6c;font-size:20px;"><i class="check circle icon"></i></td>
                    <td></td>
                    <td class="center aligned" style="color:#4fcb6c;font-size:20px;"><i class="check circle icon"></i></td>
                    <td></td>
                </tr>
                <tr>
                    <td><fmt:message key="product.table.feature.9"/></td>
                    <td class="center aligned" style="color:#4fcb6c;font-size:20px;"><i class="check circle icon"></i></td>
                    <td class="center aligned" style="color:#4fcb6c;font-size:20px;"><i class="check circle icon"></i></td>
                    <td></td>
                    <td></td>
                </tr>
                <tr style="background-color: #f8f8f8;">
                    <td><fmt:message key="product.table.feature.10"/></td>
                    <td class="center aligned" style="color:#4fcb6c;font-size:20px;"><i class="check circle icon"></i></td>
                    <td></td>
                    <td class="center aligned" style="color:#4fcb6c;font-size:20px;"><i class="check circle icon"></i></td>
                    <td></td>
                </tr>
                <tr>
                    <td><fmt:message key="product.table.feature.11"/></td>
                    <td class="center aligned" style="color:#4fcb6c;font-size:20px;"><i class="check circle icon"></i></td>
                    <td class="center aligned" style="color:#4fcb6c;font-size:20px;"><i class="check circle icon"></i></td>
                    <td></td>
                    <td></td>
                </tr>
                <tr style="background-color: #f8f8f8;">
                    <td><fmt:message key="product.table.feature.12"/></td>
                    <td class="center aligned" style="color:#4fcb6c;font-size:20px;"><i class="check circle icon"></i></td>
                    <td class="center aligned" style="color:#4fcb6c;font-size:20px;"><i class="check circle icon"></i></td>
                    <td class="center aligned" style="color:#4fcb6c;font-size:20px;"><i class="check circle icon"></i></td>
                    <td class="center aligned" style="color:#4fcb6c;font-size:20px;"><i class="check circle icon"></i></td>
                </tr>
                <tr>
                    <td><fmt:message key="product.table.feature.13"/></td>
                    <td class="center aligned" style="color:#4fcb6c;font-size:20px;"><i class="check circle icon"></i></td>
                    <td class="center aligned" style="color:#4fcb6c;font-size:20px;"><i class="check circle icon"></i></td>
                    <td class="center aligned" style="color:#4fcb6c;font-size:20px;"><i class="check circle icon"></i></td>
                    <td class="center aligned" style="color:#4fcb6c;font-size:20px;"><i class="check circle icon"></i></td>
                </tr>
                <tr style="background-color: #f8f8f8;">
                    <td><fmt:message key="product.table.feature.14"/></td>
                    <td class="center aligned" style="color:#4fcb6c;font-size:20px;"><i class="check circle icon"></i></td>
                    <td class="center aligned" style="color:#4fcb6c;font-size:20px;"><i class="check circle icon"></i></td>
                    <td class="center aligned" style="color:#4fcb6c;font-size:20px;"><i class="check circle icon"></i></td>
                    <td class="center aligned" style="color:#4fcb6c;font-size:20px;"><i class="check circle icon"></i></td>
                </tr>
                <tr>
                    <td><fmt:message key="product.table.feature.15"/></td>
                    <td class="center aligned" style="color:#4fcb6c;font-size:20px;"><i class="check circle icon"></i></td>
                    <td class="center aligned" style="color:#4fcb6c;font-size:20px;"><i class="check circle icon"></i></td>
                    <td class="center aligned" style="color:#4fcb6c;font-size:20px;"><i class="check circle icon"></i></td>
                    <td class="center aligned" style="color:#4fcb6c;font-size:20px;"><i class="check circle icon"></i></td>
                </tr>
                <tr style="background-color: #f8f8f8;">
                    <td><fmt:message key="product.table.feature.16"/></td>
                    <td class="center aligned" style="color:#4fcb6c;font-size:20px;"><i class="check circle icon"></i></td>
                    <td class="center aligned" style="color:#4fcb6c;font-size:20px;"><i class="check circle icon"></i></td>
                    <td class="center aligned" style="color:#4fcb6c;font-size:20px;"><i class="check circle icon"></i></td>
                    <td class="center aligned" style="color:#4fcb6c;font-size:20px;"><i class="check circle icon"></i></td>
                </tr>
                <tr>
                    <td><fmt:message key="product.table.feature.17"/></td>
                    <td class="center aligned">AES 256</td>
                    <td class="center aligned">AES 256</td>
                    <td class="center aligned">AES-CGM</td>
                    <td class="center aligned">AES 256</td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="ui stackable two column grid textPlace">
            <div class="ui row" style="display:inline-block;float:left">
                <div class="nine wide column" style="display:inline-block;float:left">
                    <h2><fmt:message key="product.setUp.title"/></h2>
                    <p><fmt:message key="product.setUp.text.1"/></p>
                    <p><fmt:message key="product.setUp.text.2"/></p>
                    <p><fmt:message key="product.setUp.text.3"/></p>
                </div>
                <div class="seven wide column" style="display:inline-block;float:left">
                    <a class="ui right floated blue button" href="/pricing" style="margin-top:60px;color:#ffffff;font-size: 20px;font-weight: bold;"><fmt:message key="product.setUp.button.1"/></a>
                    <a class="ui right floated button" href="/companyContact" style="margin-top: 30px;font-size: 20px;font-weight: bold;"><fmt:message key="product.setUp.button.2"/></a>
                </div>
            </div>
        </div>
    </div>
</section>
<%@ include file="templates/landingPage/landingFooter.jsp" %>
<script src="/jsMinified.v00022/jquery1.12.4.js"></script>
<script src="/jsMinified.v00022/jquery.complexify.js"></script>
<script src="/jsMinified.v00022/bootstrap.js" async></script>
<script src="/jsMinified.v00022/ajaxHandler.js" async></script>
<script src="/jsMinified.v00022/landingPage.js" async></script>
<script src="/jsMinified.v00022/basic-utils.js" async></script>
<script src="/jsMinified.v00022/languageChooser.js" async></script>
<script src="/jsMinified.v00022/tracker.js" async></script>
<script src="semantic/dist/semantic.min.js"></script>
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
        $('#table-general').popup({
            on        : 'hover',
            position  : 'bottom center',
        });
        $('#table-switch').popup({
            on        : 'hover',
            position  : 'bottom center'
        });
        $('#table-notification').popup({
            on        : 'hover',
            position  : 'bottom center',
        });
        $('#table-activeUser').popup({
            on        : 'hover',
            position  : 'bottom center'
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

        if (location.hostname === "ease.space")
            amplitude.getInstance().init("73264447f97c4623fb38d92b9e7eaeea");
        else
            amplitude.getInstance().init("5f012a5e604acb0283ed11ed8da5414f");
        easeTracker.trackEvent("SecurityPageVisit");
    });
</script>
</body>
</html>
