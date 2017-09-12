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
    <link rel="stylesheet" type="text/css" href="/cssMinified.v00011/lib/fonts/museo-font.css"/>
    <link rel="stylesheet" href="/cssMinified.v00011/default_style.css"/>
    <link rel="stylesheet" href="/cssMinified.v00011/bootstrap.css"/>
    <link rel="stylesheet" href="/cssMinified.v00011/landingPage.css"/>
    <link rel="stylesheet" href="/cssMinified.v00011/lib/fonts/font-awesome-4.2.0/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="/cssMinified.v00011/team.css"/>
    <link rel="stylesheet" href="/cssMinified.v00011/pricingPage.css"/>

    <link rel="manifest" href="manifest.json">
</head>
<body>
<%@ include file="templates/landingPage/landingHeader.jsp"%>
<section id="pricing" style="background-color: white">
    <div class="container" id="teamsPreview">
        <div class="content display-flex flex_direction_column step2">
            <h1 class="text-center" style="margin:0 0 10px 0">Un prix juste et transparent</h1>
            <span class="sub-title">Offre <u>sans engagement</u>, facturée <u>mensuellement</u> par utilisateur <u>actif</u>*</span>
            <div class="ui grid display-flex" style="margin:55px 0 37px 0">
                <div class="team_plan" id="starter_plan" style="margin: 15px 10px;">
                    <%--<img src="/resources/other/Spaceship.svg" alt="icon" class="styleImage" style="background-size:400px 400px;width:400px;height:400px;" />--%>
                    <h1 class="text-center title">Starter</h1>
                    <span class="text-center price" style="margin-bottom: 33px">Gratuit</span>
                    <span class="tip" style="margin: 15px 0 35px 0">Pour les utilisateurs solo souhaitant essayer Ease.space pour une periode de temps illimitée</span>
                    <div class="display-flex flex_direction_column full_flex">
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span>Accès au Personal Space</span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span>Apps illimitées</span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span>Groupes illimitées</span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span>Mutli-comptes illimités</span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span>Gestion de Favoris</span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span>Intégration d’Apps, connectées avec Facebook et Linkedin</span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span>Intégration automatique de nouveaux mots de passe</span>
                            <img src="/resources/icons/Soon.png" style="width:50px;height:17px;margin-top:15px;"/>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span>Déconnexion de tous vos comptes en 1 clic</span>
                        </div>
                    </div>
                    <div class="text-center" style="margin: 60px 0 0 0">
                        <button class="button-unstyle big-button button signUpButton" style="font-size: 18px;">
                                Utiliser gratuitement
                        </button>
                    </div>
                </div>
                <div class="team_plan" id="pro_team_plan" style="margin: 15px 10px;">
                    <img src="/resources/other/illu.svg" alt="icon" class="styleImage"/>
                    <h1 class="text-center title greenText">Team</h1>
                    <span class="text-center price" style="margin-bottom: 33px">3,99 <span class="symbol">€HT</span></span>
                    <div class="text-center">
                        <button class="button-unstyle big-button button">
                            <a href="/teams#/teamCreation" class="link-unstyle" style="font-size: 18px;">
                                Essayez 1 mois gratuit
                            </a>
                        </button>
                    </div>
                    <span class="tip" style="margin: 10px 0 0 0;font-size:14px;">Pas de CB requise</span>
                    <span class="info">L'integralité de Starter, et</span>
                    <div class="display-flex flex_direction_column full_flex">
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span>Rooms illimitées</span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span>Partage d'accès web avec affichage ou obstruction des mots de passe</span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span>Partage d'outils SaaS avec personnalisation des accès pour chaque utilisateur</span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span>Partage d'identifiants pour softwares locaux</span>
                            <img src="/resources/icons/Soon.png" style="width:50px;height:17px;margin-top:15px;"/>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span>Partage temporaire d'identifiants</span>
                            <img src="/resources/icons/Soon.png" style="width:50px;height:17px;margin-left:50px;margin-top:15px;"/>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span>Retrait automatique des accès à date paramétrée</span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span>Plusieurs Admins et 1 Owner</span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span>Demandes d’intégrations de sites prioritaires</span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span>Service client prioritaire</span>
                        </div>
                    </div>
                    <div class="text-center" style="margin: 60px 0 10px 0">
                        <button class="button-unstyle big-button button">
                            <a href="/teams#/teamCreation" class="link-unstyle" style="font-size: 18px;">
                                Essayez 1 mois gratuit
                            </a>
                        </button>
                    </div>
                    <span class="tip" style="font-size:14px;margin-bottom:1px;">Pas de CB requise</span>
                </div>
                <div class="team_plan" id="enterprise_team_plan" style="margin: 15px 10px;">>
                    <img src="/resources/other/Saturn.svg" alt="icon" class="styleImage"/>
                    <div class="plan_header display-flex flex_direction_column text-center" style="height: 240px;margin-top:-30px;">
                        <h1 class="text-center title">Entreprise</h1>
                        <span style="font-size: 0.9rem">à partir de</span>
                        <span class="text-center price" style="margin-bottom: 33px">7,89 <span class="symbol">€HT</span></span>
                    </div>
                    <span class="info" style="margin-top:40px;">L'integralité de team<br/>et des fonctionnalités à la demande</span>
                    <div class="display-flex flex_direction_column full_flex">
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span>Intégration Single Sign-on (SSO)</span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span>Authentification à double facteur</span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span>Déploiment applicatif et base de données en interne</span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span>Synchronisation temps-réel avec votre Active Directory</span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span>Conservation et gestion d'archives</span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span>Temps de fonctionnement disponible (SLA): 99%</span>
                        </div>
                    </div>
                    <div class="text-center">
                        <button class="button-unstyle big-button button">
                            <a href="/companyContact" class="link-unstyle" style="font-size: 18px;">
                                Contactez-nous
                            </a>
                        </button>
                    </div>
                </div>
            </div>
            <span class="sub-title" style="font-size: 1rem">* un utilisateur est actif à partir du moment où il a reçu ou envoyé au moins une App dans votre Team Space.</span>
        </div>
    </div>
</section>
<%@ include file="templates/landingPage/landingFooter.jsp" %>
<%@ include file="templates/landingPage/registrationPopup.jsp" %>

<script src="/jsMinified.v00016/jquery1.12.4.js"></script>
<script src="/jsMinified.v00016/jquery.complexify.js"></script>
<script src="/jsMinified.v00016/bootstrap.js" async></script>
<script src="/jsMinified.v00016/ajaxHandler.js" async></script>
<script src="/jsMinified.v00016/registrationPopup.js" async></script>
<script src="/jsMinified.v00016/basic-utils.js" async></script>
<script src="/jsMinified.v00016/languageChooser.js" async></script>
<script src="/jsMinified.v00016/tracker.js" async></script>
<script type="text/javascript">
    window.addEventListener('load',function(){
        $('.signUpButton').click(function(){
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
            s.src = "/jsMinified.v00016/thirdParty/crisp.js";
            s.async = 1;
            d.getElementsByTagName("head")[0].appendChild(s);
        })();
    });
</script>
<script type="text/javascript">
    window.addEventListener('load',function(){
        (function(e,t){var n=e.amplitude||{_q:[],_iq:{}};var r=t.createElement("script");r.type="text/javascript";
            r.async = true;
            r.src = "/jsMinified.v00016/amplitude-analytics.js";
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
        easeTracker.trackEvent("HomepageVisit");
    });
</script>
</body>
</html>