<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<c:if test="${language ne 'en' and language ne 'fr_FR'}">
    <c:set var="language" value="fr_FR" scope="session"/>
</c:if>
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="com.Ease.Languages.text" />
<html>
<head>
    <title> Ease.space | Security</title>
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
    <link rel="stylesheet" href="/cssMinified.v00020/landingPage.css"/>
    <link rel="stylesheet" href="/cssMinified.v00020/lib/fonts/font-awesome-4.2.0/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="/cssMinified.v00020/securityPage.css"/>
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
    <link rel="manifest" href="manifest.json">
</head>
<body id="landingBody">
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
    <div class="container">
        <div class="intro-text">
            <div class="intro-heading">
                <fmt:message key="security.title"/>
            </div>
            <div class="intro-lead-in">
                <fmt:message key="security.sub-title"/>
            </div>
        </div>
    </div>
</header>
<section style="background-color:white;padding:0 0 170px 0;">
    <div class="container">
        <ul class="docs-nav">
            <li><a href="#1" class="cc-active">1. <fmt:message key="security.menu.title1"/></a></li>
            <li><a href="#2" class="cc-active">2. <fmt:message key="security.menu.title2"/></a></li>
            <li><a href="#3" class="cc-active"><p style="position: absolute;">3.</p><p style="margin-left: 18px;"><fmt:message key="security.menu.title3"/></p></a></li>
            <li><a href="#4" class="cc-active">4. <fmt:message key="security.menu.title4"/></a></li>
            <li><a href="#5" class="cc-active">5. <fmt:message key="security.menu.title5"/></a></li>
            <li><a href="#6" class="cc-active">6. <fmt:message key="security.menu.title6"/></a></li>
            <li><a href="#7" class="cc-active">7. <fmt:message key="security.menu.title7"/></a></li>
        </ul>
        <div id="docs-content" class="docs-content">
            <div class="doc_section" id="1" style="padding-top:70px;">
                <h1 class="doc_header">1. <fmt:message key="security.menu.title1"/></h1>
                <p><fmt:message key="security.anonymity.text1"/></p>
                <p><fmt:message key="security.anonymity.text2"/></p>
                <p><fmt:message key="security.anonymity.text3"/></p>
                <p><fmt:message key="security.anonymity.text4"/></p>
            </div>
            <div class="doc_section" id="2" style="padding-top:70px;">
                <h1 class="doc_header">2. <fmt:message key="security.menu.title2"/></h1>
                <p><fmt:message key="security.yoursecurity.text1"/></p>
                <p><fmt:message key="security.yoursecurity.text2"/></p>
                <p><fmt:message key="security.yoursecurity.text3"/></p>
                <p><fmt:message key="security.yoursecurity.text4"/></p>
                <p><fmt:message key="security.yoursecurity.text5"/></p>
                <p><fmt:message key="security.yoursecurity.text6"/></p>
                <p><fmt:message key="security.yoursecurity.text7"/></p>
                <p style="margin-bottom:0;"><fmt:message key="security.yoursecurity.text8"/></p>
                <p style="position: absolute;margin-top: 0;margin-left: 20px;">-</p>
                <p style="margin-top:0;margin-left:50px;margin-bottom:0;"><fmt:message key="security.yoursecurity.text8.first"/></p>
                <p style="position: absolute;margin-top: 0;margin-left: 20px;">-</p>
                <p style="margin-top:0;margin-left:50px;"><fmt:message key="security.yoursecurity.text8.second"/></p>
                <p><fmt:message key="security.yoursecurity.text9"/></p>
            </div>
            <div class="doc_section" id="3" style="padding-top:70px;">
                <h1 class="doc_header">3. <fmt:message key="security.menu.title3"/></h1>
                <p><fmt:message key="security.companysecurity.text1"/></p>
                <p><fmt:message key="security.companysecurity.text2"/></p>
                <p><fmt:message key="security.companysecurity.text3"/></p>
                <p><fmt:message key="security.companysecurity.text4"/></p>
                <p><fmt:message key="security.companysecurity.text5"/></p>
                <p><fmt:message key="security.companysecurity.text6"/></p>
                <p><fmt:message key="security.companysecurity.text7"/></p>
                <p><fmt:message key="security.companysecurity.text8"/></p>
                <p><fmt:message key="security.companysecurity.text9"/></p>
                <p><fmt:message key="security.companysecurity.text10"/></p>
                <p><fmt:message key="security.companysecurity.text11"/></p>
            </div>
            <div class="doc_section" id="4" style="padding-top:70px;">
                <h1 class="doc_header">4. <fmt:message key="security.menu.title4"/></h1>
                <p><fmt:message key="security.oursecurity.text1"/></p>
                <p><fmt:message key="security.oursecurity.text2"/></p>
                <p><fmt:message key="security.oursecurity.text3"/></p>
                <p><fmt:message key="security.oursecurity.text4"/></p>
                <p><fmt:message key="security.oursecurity.text5"/></p>
                <p><fmt:message key="security.oursecurity.text6"/></p>
                <p><fmt:message key="security.oursecurity.text7"/></p>
                <p><fmt:message key="security.oursecurity.text8"/></p>
                <p style="position:absolute;margin-left:20px;">1.</p>
                <p style="margin-left:50px;"><fmt:message key="security.oursecurity.text9"/></p>
                <p style="position:absolute;margin-left:20px;">2.</p>
                <p style="margin-left:50px;"><fmt:message key="security.oursecurity.text10"/></p>
                <p><fmt:message key="security.oursecurity.text11"/></p>
                <p class="sub_header">HTTPS</p>
                <p><fmt:message key="security.oursecurity.text12"/></p>
                <p><fmt:message key="security.oursecurity.text13"/></p>
                <p><fmt:message key="security.oursecurity.text14"/></p>
                <p class="sub_header"><fmt:message key="security.oursecurity.text15"/></p>
                <p><fmt:message key="security.oursecurity.text16"/></p>
                <p><fmt:message key="security.oursecurity.text17"/></p>
                <p><fmt:message key="security.oursecurity.text18"/></p>
                <p><fmt:message key="security.oursecurity.text19"/></p>
            </div>
            <div class="doc_section" id="5" style="padding-top:70px;">
                <h1 class="doc_header">5. <fmt:message key="security.menu.title5"/></h1>
                <p><fmt:message key="security.ourpolicy.text1"/></p>
                <p><fmt:message key="security.ourpolicy.text2"/></p>
                <p><fmt:message key="security.ourpolicy.text3"/></p>
                <p><fmt:message key="security.ourpolicy.text4"/></p>
                <p><fmt:message key="security.ourpolicy.text5"/></p>
            </div>
            <div class="doc_section" id="6" style="padding-top:70px;">
                <h1 class="doc_header">6. <fmt:message key="security.menu.title6"/></h1>
                <p><fmt:message key="security.faq.question1"/></p>
                <p><fmt:message key="security.faq.text1"/></p>
                <p><fmt:message key="security.faq.text2"/></p>
                <p><fmt:message key="security.faq.text3"/></p>
                <p style="position:absolute;margin-left:20px;">1.</p>
                <p style="margin-left:50px;"><fmt:message key="security.faq.text4"/></p>
                <p style="position:absolute;margin-left:20px;">2.</p>
                <p style="margin-left:50px;"><fmt:message key="security.faq.text5"/></p>
                <p style="margin-bottom:0;"><fmt:message key="security.faq.text6"/></p>
                <p style="position:absolute;margin-top:0;margin-left:20px;">-</p>
                <p style="margin-top:0;margin-left:50px;margin-bottom:0;"><fmt:message key="security.faq.text6.first"/></p>
                <p style="position:absolute;margin-top:0;margin-left:20px;">-</p>
                <p style="margin-top:0;margin-left:50px;"><fmt:message key="security.faq.text6.second"/></p>
                <p><fmt:message key="security.faq.text7"/></p>
                <p><fmt:message key="security.faq.question2"/></p>
                <p><fmt:message key="security.faq.text8"/></p>
                <p><fmt:message key="security.faq.question3"/></p>
                <p><fmt:message key="security.faq.text9"/></p>
                <p><fmt:message key="security.faq.question4"/></p>
                <p><fmt:message key="security.faq.text10"/></p>
                <p><fmt:message key="security.faq.text11"/></p>
            </div>
            <div class="doc_section" id="7" style="padding-top:70px;">
                <h1 class="doc_header">7. <fmt:message key="security.menu.title7"/></h1>
                <p><fmt:message key="security.whitepaper.text"/></p>
            </div>
            <div style="text-align: center">
                <a href="/teams#/registration" class="btn btn-xl signUpButton">
                    <fmt:message key="security.button.text"/>
                </a>
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
<script type="text/javascript">
    if (window.matchMedia("(max-width: 768px)").matches) {
        $('#docs-content').removeClass('docs-content');
        $('ul.docs-nav').addClass('docs-without-nav');
        $('ul.docs-nav').removeClass('docs-nav');
    }
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
    window.addEventListener('load',function(){
        easeTracker.trackEvent("SecurityPageVisit");
    });
</script>
</body>
</html>