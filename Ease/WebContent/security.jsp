<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<c:if test="${language ne 'en' and language ne 'fr_FR'}">
    <c:set var="language" value="fr_FR" scope="session"/>
</c:if>
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="languages/text" />
<html>
<head>
    <title> Ease.space | Security</title>
    <meta name="viewport" content="initial-scale=1, maximum-scale=1" />
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
    <meta http-equiv="Content-Type" contentType="text/html; charset=UTF-8" />
    <meta name="viewport" content="initial-scale=1, maximum-scale=1" />
    <link rel="icon" type="image/png" href="resources/icons/APPEASE.png" />

    <link rel="stylesheet" type="text/css" href="//fonts.googleapis.com/css?family=Raleway:500,700" />
    <link rel="stylesheet" type="text/css" href="/cssMinified.v00056/lib/fonts/museo-font.css"/>
    <link rel="stylesheet" href="/cssMinified.v00056/default_style.css"/>
    <link rel="stylesheet" href="/cssMinified.v00056/bootstrap.css"/>
    <link rel="stylesheet" href="/cssMinified.v00056/landingPage.css"/>
    <link rel="stylesheet" href="/cssMinified.v00056/lib/fonts/font-awesome-4.2.0/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="/cssMinified.v00056/securityPage.css"/>

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
        <div class="row" style="margin-top: 40px;" id="contact">
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
<%@ include file="templates/landingPage/registrationPopup.jsp" %>

<script src="/jsMinified.v00023/jquery1.12.4.js"></script>
<script src="/jsMinified.v00023/jquery.complexify.js"></script>
<script src="/jsMinified.v00023/bootstrap.js" async></script>
<script src="/jsMinified.v00023/ajaxHandler.js" async></script>
<script src="/jsMinified.v00023/registrationPopup.js" async></script>
<script src="/jsMinified.v00023/languageChooser.js" async></script>
<script src="/jsMinified.v00023/basic-utils.js" async></script>
<script src="/jsMinified.v00023/tracker.js" async></script>
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
</body>
</html>
