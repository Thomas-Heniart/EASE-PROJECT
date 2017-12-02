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
    <title> Ease.space | Pricing</title>
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

    <link rel="stylesheet" href="semantic/dist/semantic.min.css">
    <link rel="stylesheet" type="text/css" href="/cssMinified.v00020/lib/fonts/museo-font.css"/>
    <link rel="stylesheet" href="/cssMinified.v00020/default_style.css"/>
    <link rel="stylesheet" href="/cssMinified.v00020/bootstrap.css"/>
    <link rel="stylesheet" href="/cssMinified.v00020/landingPage.css"/>
    <link rel="stylesheet" href="/cssMinified.v00020/lib/fonts/font-awesome-4.2.0/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="/cssMinified.v00020/team.css"/>
    <link rel="stylesheet" href="/cssMinified.v00020/pricingPage.css"/>
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
<body>
<%@ include file="templates/landingPage/landingHeader.jsp"%>
<section id="pricing" style="background-color: white">
    <div class="container" id="teamsPreview" style="overflow:unset;">
        <div class="content display-flex flex_direction_column step2">
            <h1 class="text-center" style="margin:0 0 10px 0"><fmt:message key="pricing.title"/></h1>
            <span class="sub-title"><fmt:message key="pricing.sub-title"/></span>
            <div class="ui grid display-flex" style="margin:55px 0 37px 0">
                <div class="team_plan" id="starter_plan" style="margin:15px 10px;overflow:unset;">
                    <%--<img src="/resources/other/Spaceship.svg" alt="icon" class="styleImage" style="background-size:400px 400px;width:400px;height:400px;" />--%>
                    <h1 class="text-center title"><fmt:message key="pricing.free.title"/></h1>
                        <span class="text-center price" style="font-size:36px;">0 <span class="symbol">€HT</span></span>
                        <span class="tip" style="margin:0 0 15px 0"><fmt:message key="pricing.sub-tip"/></span>
                        <div class="text-center" style="margin-bottom:26px">
                            <button class="button-unstyle big-button button">
                                <a href="/teams#/registration" class="link-unstyle" style="font-size: 18px;">
                                    <fmt:message key="pricing.free.button"/>
                                </a>
                            </button>
                        </div>
                    <div class="display-flex flex_direction_column full_flex">
                        <div class="feature">
                            <fmt:message key="pricing.free.features.title.1"/>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span><fmt:message key="pricing.free.features.1"/></span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span><fmt:message key="pricing.free.features.2"/></span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span><fmt:message key="pricing.free.features.3"/></span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <fmt:message key="pricing.free.features.4"/>
                        </div>
                        <div class="feature">
                            <fmt:message key="pricing.free.features.title.2"/>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span><fmt:message key="pricing.free.features.5"/></span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span><fmt:message key="pricing.free.features.6"/></span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span><fmt:message key="pricing.free.features.7"/></span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span><fmt:message key="pricing.free.features.8"/></span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span><fmt:message key="pricing.free.features.9"/></span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span><fmt:message key="pricing.free.features.10"/></span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span><fmt:message key="pricing.free.features.11"/></span>
                        </div>
                    </div>
                    <div class="text-center" style="margin: 60px 0 0 0">
                        <button class="button-unstyle big-button button signUpButton" style="font-size: 18px;">
                            <a href="/#/registration" class="link-unstyle" style="font-size: 18px;">
                                <fmt:message key="pricing.free.button"/>
                            </a>
                        </button>
                    </div>
                </div>
                <div class="team_plan" id="pro_team_plan" style="margin: 15px 10px;overflow:unset">
                    <img src="/resources/other/illu.svg" alt="icon" class="styleImage"/>
                    <h1 class="text-center title greenText"><fmt:message key="pricing.pro.title"/></h1>
                    <span class="text-center price">3,99 <span class="symbol">€HT</span></span>
                    <span class="tip" style="margin:0 0 15px 0"><fmt:message key="pricing.sub-tip"/></span>
                    <div class="text-center">
                        <button class="button-unstyle big-button button">
                            <a href="/#/teamCreation?plan_id=1" class="link-unstyle" style="font-size: 18px;">
                                <fmt:message key="pricing.pro.button-text"/>
                            </a>
                        </button>
                    </div>
                    <span class="tip" style="margin: 10px 0 0 0;"><fmt:message key="pricing.pro.sub-button"/></span>
                    <div class="display-flex flex_direction_column full_flex">
                        <div class="feature">
                            <span class="info"><fmt:message key="pricing.pro.features.title"/></span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span><fmt:message key="pricing.pro.features.1"/></span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span><fmt:message key="pricing.pro.features.2"/></span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span><fmt:message key="pricing.pro.features.3"/></span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span><fmt:message key="pricing.pro.features.4"/></span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span><fmt:message key="pricing.pro.features.5"/></span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span><fmt:message key="pricing.pro.features.6"/></span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span><fmt:message key="pricing.pro.features.7"/></span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <fmt:message key="pricing.pro.features.8"/>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <fmt:message key="pricing.pro.features.9"/>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <fmt:message key="pricing.pro.features.10"/>
                        </div>
                    </div>
                    <div class="text-center" style="margin: 60px 0 10px 0">
                        <button class="button-unstyle big-button button">
                            <a href="/#/teamCreation?plan_id=1" class="link-unstyle" style="font-size: 18px;">
                                <fmt:message key="pricing.pro.button-text"/>
                            </a>
                        </button>
                    </div>
                    <span class="tip" style="font-size:14px;margin-bottom:1px;"><fmt:message key="pricing.pro.sub-button"/></span>
                </div>
                <div class="team_plan" id="enterprise_team_plan" style="margin: 15px 10px;">>
                    <img src="/resources/other/Saturn.svg" alt="icon" class="styleImage"/>
                    <div class="plan_header display-flex flex_direction_column text-center" style="height:261px;margin-top:-30px;margin-bottom:26px">
                        <h1 class="text-center title"><fmt:message key="pricing.premium.title"/></h1>
                        <span class="text-center price">7,89 <span class="symbol">€HT</span></span>
                        <span class="tip" style="margin:0 0 15px 0;color:white;"><fmt:message key="pricing.sub-tip"/></span>
                    </div>
                    <div class="display-flex flex_direction_column full_flex">
                        <div class="feature">
                            <span class="info"><fmt:message key="pricing.premium.features.title"/></span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span><fmt:message key="pricing.premium.features.1"/></span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span><fmt:message key="pricing.premium.features.2"/></span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span><fmt:message key="pricing.premium.features.3"/></span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span><fmt:message key="pricing.premium.features.4"/></span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span><fmt:message key="pricing.premium.features.5"/></span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span><fmt:message key="pricing.premium.features.6"/></span>
                        </div>
                    </div>
                    <div class="text-center">
                        <button class="button-unstyle big-button button">
                            <a href="/companyContact" class="link-unstyle" style="font-size: 18px;">
                                <fmt:message key="pricing.premium.button"/>
                            </a>
                        </button>
                    </div>
                </div>
            </div>
            <span class="sub-title" style="font-size: 1rem"><fmt:message key="pricing.endsentence"/></span>
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
<script>
    $(document).ready(function() {
        $('#starter-title-1').popup({
            on        : 'hover',
            position  : 'bottom center',
            target    : '#starter-title-1-icon'
        });
        $('#starter-title-1-icon').popup({
            on        : 'hover',
            position  : 'bottom center'
        });
        $('#starter-title-2').popup({
            on        : 'hover',
            position  : 'bottom center',
            target    : '#starter-title-2-icon'
        });
        $('#starter-title-2-icon').popup({
            on        : 'hover',
            position  : 'bottom center'
        });
        $('#free-feature-1').popup({
            on        : 'hover',
            position  : 'bottom center'
        });
        $('#free-feature-2').popup({
            on        : 'hover',
            position  : 'bottom center'
        });
        $('#free-feature-3').popup({
            on        : 'hover',
            position  : 'bottom center'
        });
        $('#free-feature-4').popup({
            on        : 'hover',
            position  : 'bottom center'
        });
        $('#free-feature-8').popup({
            on        : 'hover',
            position  : 'bottom center'
        });
        $('#free-feature-10').popup({
            on        : 'hover',
            position  : 'bottom center'
        });
        $('#free-feature-11').popup({
            on        : 'hover',
            position  : 'bottom center'
        });
        $('#pro-feature-1').popup({
            on        : 'hover',
            position  : 'bottom center'
        });
        $('#pro-feature-4').popup({
            on        : 'hover',
            position  : 'bottom center'
        });
        $('#pro-feature-6').popup({
            on        : 'hover',
            position  : 'bottom center'
        });
        $('#premium-feature-1').popup({
            on        : 'hover',
            position  : 'bottom center'
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
        easeTracker.trackEvent("PricingVisit");
    });
</script>
</body>
</html>