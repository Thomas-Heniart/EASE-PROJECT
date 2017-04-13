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
    <link rel="stylesheet" href="/cssMinified.v00006/default_style.css" />
    <link rel="stylesheet" href="/cssMinified.v00006/bootstrap.css" />
    <link rel="stylesheet" href="/cssMinified.v00006/landingPage.css" />
    <link rel="stylesheet" href="/cssMinified.v00006/lib/fonts/font-awesome-4.2.0/css/font-awesome.min.css" />
    <link rel="stylesheet" href="/cssMinified.v00006/securityPage.css" />

    <link rel="manifest" href="manifest.json">
</head>
<body id="landingBody">
<%@ include file="templates/landingPage/landingHeader.jsp"%>

<section id="security">
    <div class="container">
        <div class="row">
            <div class="col-lg-12 text-center">
                <h1>
                    <fmt:message key="security.title"/>
                </h1>
            </div>
        </div>
    </div>
    <div class="container" id="securityBody">
        <div class="row">
            <div class="col-lg-12">
                <h4 class="text-center">
                    <fmt:message key="security.sub-title"/>
                </h4>
            </div>
        </div>
        <div class="row" id="masterKey">
            <div class="col-lg-12">
                <h2 class="text-center">
                    <fmt:message key="security.masterKey.title"/>
                </h2>
                <div class="center-block imageHandler">
                    <img src="/resources/landing/ligneCadenas.png"/>
                </div>
                <div class="row">
                    <div class="col-lg-4 col-md-4 col-xs-4">
                        <p class="text-center">
                            <fmt:message key="security.masterKey.tip1"/>
                        </p>
                    </div>
                    <div class="col-lg-4 col-md-4 col-xs-4">
                        <p class="text-center">
                            <fmt:message key="security.masterKey.tip2"/>
                        </p>
                    </div>
                    <div class="col-lg-4 col-md-4 col-xs-4">
                        <p class="text-center">
                            <fmt:message key="security.masterKey.tip3"/>
                        </p>
                    </div>
                </div>
            </div>
        </div>
        <div class="row" id="funcs">
            <div class="row">
                <div class="col-lg-6 col-md-6 col-xs-12 image">
                    <div class="imageHandler center-block">
                        <img src="/resources/landing/coffreFort.png"/>
                    </div>                    
                </div>
                <div class="col-lg-6 col-md-6 col-xs-12">
                    <h4>
                        <fmt:message key="security.funcs.tip1.title"/>
                    </h4>
                    <p>
                        <fmt:message key="security.funcs.tip1.text"/>
                    </p>
                </div>
            </div>
            <div class="row">
                <div class="col-lg-6 col-md-6 col-xs-12">
                    <h4>
                        <fmt:message key="security.funcs.tip2.title"/>
                    </h4>
                    <p>
                        <fmt:message key="security.funcs.tip2.text"/>
                    </p>
                </div>
                <div class="col-lg-6 col-md-6 col-xs-12 image">
                    <div class="imageHandler center-block">
                        <img src="/resources/landing/Cadenas.png"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-lg-6 col-md-6 col-xs-12 image">
                    <div class="imageHandler center-block">
                        <img src="/resources/landing/Securisation.png"/>
                    </div>
                </div>
                <div class="col-lg-6 col-md-6 col-xs-12">
                    <h4>
                        <fmt:message key="security.funcs.tip3.title"/>
                    </h4>
                    <p>
                        <fmt:message key="security.funcs.tip3.text"/>
                    </p>
                </div>
            </div>
            <div class="row">
                <div class="col-lg-6 col-md-6 col-xs-12">
                    <h4>
                        <fmt:message key="security.funcs.tip4.title"/>
                    </h4>
                    <p>
                        <fmt:message key="security.funcs.tip4.text"/>
                    </p>
                </div>
                <div class="col-lg-6 col-md-6 col-xs-12 image">
                    <div class="imageHandler center-block">
                        <img src="/resources/landing/Cloud.png"/>
                    </div>
                </div>
            </div>
        </div>
        <div class="row" id="anonymity">
            <h2 class="text-center">
                <fmt:message key="security.anonymity.title"/>
            </h2>
            <div class="row">
                <div class="col-lg-1 col-md-2 col-xs-12">
                    <img src="/resources/landing/Folder.png" class="center-block"/>
                </div>
                <div class="col-lg-11 col-md-10 col-xs-12">
                    <p>
                        <fmt:message key="security.anonymity.tip1"/>
                    </p>
                </div>
            </div>
            <div class="row">
                <div class="col-lg-1 col-md-2 col-xs-12">
                    <img src="/resources/landing/Institution.png" class="center-block"/>
                </div>
                <div class="col-lg-11 col-md-10 col-xs-12">
                    <p>
                        <fmt:message key="security.anonymity.tip2"/>
                    </p>
                </div>
            </div>
            <div class="row">
                <div class="col-lg-1 col-md-2 col-xs-12 ">
                    <img src="/resources/landing/Paper.png" class="center-block"/>
                </div>
                <div class="col-lg-11 col-md-10 col-xs-12">
                    <p>
                        <fmt:message key="security.anonymity.tip3"/>
                    </p>
                </div>
            </div>
        </div>
        <div class="row" id="faq">
            <h2 class="text-center">
                <fmt:message key="security.faq.title"/>
            </h2>
            <div class="row">
                <h4>
                    <fmt:message key="security.faq.tip1.title"/>
                </h4>
                <p>
                    <fmt:message key="security.faq.tip1.text"/>
                </p>
            </div>
            <div class="row">
                <h4>
                    <fmt:message key="security.faq.tip2.title"/>
                </h4>
                <p>
                    <fmt:message key="security.faq.tip2.text"/>
                </p>
            </div>
            <div class="row">
                <h4>
                    <fmt:message key="security.faq.tip3.title"/>
                </h4>
                <p>
                    <fmt:message key="security.faq.tip3.text"/>
                </p>
            </div>
            <div class="row">
                <h4>
                    <fmt:message key="security.faq.tip4.title"/>
                </h4>
                <p>
                    <fmt:message key="security.faq.tip4.text"/>
                </p>
            </div>
            <div class="row">
                <h4>
                    <fmt:message key="security.faq.tip5.title"/>
                </h4>
                <p>
                    <fmt:message key="security.faq.tip5.text"/>
                </p>
            </div>
        </div>
        <div class="row" style="margin-top: 40px;">
            <div class="row">
                <div class="col-lg-12">
                <p>
                    <strong>
                        <fmt:message key="security.contact-us"/>
                    </strong>
                </p>
                </div>
            </div>
        </div>
    </div>
</section>

<%@ include file="templates/landingPage/landingFooter.jsp" %>

<script	src="/jsMinified.v00007/jquery1.12.4.js"></script>
<script src="/jsMinified.v00007/languageChooser.js" async></script>
<script src="/jsMinified.v00007/basic-utils.js" async></script>
<script src="/jsMinified.v00007/postHandler.js" async></script>
<script src="/jsMinified.v00007/tracker.js" async></script>
<script type="text/javascript">
    window.addEventListener('load',function(){
        $crisp=[];CRISP_WEBSITE_ID="6e9fe14b-66f7-487c-8ac9-5912461be78a";(function(){d=document;s=d.createElement("script");s.src="/jsMinified.v00007/crisp.js";s.async=1;d.getElementsByTagName("head")[0].appendChild(s);})();
    });
</script>
<script type="text/javascript">
    window.addEventListener('load',function(){
        (function(e,t){var n=e.amplitude||{_q:[],_iq:{}};var r=t.createElement("script");r.type="text/javascript";
            r.async=true;r.src="/jsMinified.v00007/amplitude-analytics.js";
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
