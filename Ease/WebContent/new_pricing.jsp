<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<c:if test="${language ne 'en' and language ne 'fr_FR'}">
    <c:set var="language" value="en" scope="session"/>
</c:if>
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="languages/text" />
<html>
<head>
    <title> Ease.space | Pricing</title>
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
    <link rel="stylesheet" href="semantic/dist/semantic.min.css">
    <link rel="stylesheet" type="text/css" href="/css/lib/fonts/museo-font.css?cssv=8"/>
    <link rel="stylesheet" href="/css/default_style.css?cssv=8"/>
    <link rel="stylesheet" href="/css/bootstrap.css?cssv=8"/>
    <link rel="stylesheet" href="/css/landingPage.css?cssv=8"/>
    <link rel="stylesheet" href="/css/lib/fonts/font-awesome-4.2.0/css/font-awesome.min.css?cssv=8"/>
    <link rel="stylesheet" href="/css/team.css?cssv=8"/>
    <link rel="stylesheet" href="/css/pricingPage.css?cssv=8"/>


    <!-- Global site tag (gtag.js) - Google Analytics -->
    <script async src="https://www.googletagmanager.com/gtag/js?id=UA-75916041-5"></script>
    <script>
      window.dataLayer = window.dataLayer || [];
      function gtag(){dataLayer.push(arguments);}
      gtag('js', new Date());

      gtag('config', 'UA-75916041-5');
    </script>
    <link rel="manifest" href="manifest.json">
</head>
<body>
<%@ include file="templates/landingPage/landingHeader.jsp"%>
<section id="pricing" style="background-color: white">
    <div class="container" id="teamsPreview" style="overflow:unset;">
        <div class="content display-flex flex_direction_column step2">
            <h1 class="text-center" style="font-weight:normal;font-size:54px;margin:0 0 30px 0"><fmt:message key="pricing.title"/></h1>
            <span class="sub-title"><fmt:message key="pricing.sub-title"/></span>
            <div class="ui grid display-flex" style="justify-content:space-between;margin:55px 0 80px 0">
                <div class="team_plan_wrapper">
                    <div class="team_plan" id="starter_plan">
                        <h1 class="text-center title"><fmt:message key="pricing.free.title"/></h1>
                        <span class="text-center price" style="font-size:36px;">0 <span class="symbol">€HT</span></span>
                        <span class="price_divider" ><fmt:message key="pricing.sub-tip"/></span>
                        <div class="text-center">
                            <button class="button-unstyle big-button button">
                                <a href="/#/teamCreation?plan_id=0" class="link-unstyle" style="font-size: 18px;">
                                    <fmt:message key="pricing.free.button"/>
                                </a>
                            </button>
                        </div>
                        <span class="seats">
                            <fmt:message key="pricing.free.seats"/>
                        </span>
                    </div>
                    <div class="features display-flex flex_direction_column full_flex">
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span><fmt:message key="pricing.feature.1"/></span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span><fmt:message key="pricing.feature.2"/></span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span><fmt:message key="pricing.feature.3"/></span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span><fmt:message key="pricing.feature.4"/></span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span><fmt:message key="pricing.feature.5"/></span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span><fmt:message key="pricing.feature.6"/></span>
                        </div>
                    </div>
                </div>
                <div class="team_plan_wrapper">
                    <div class="team_plan" id="pro_team_plan">
                        <img src="/resources/other/illu.svg" alt="icon" class="styleImage"/>
                        <h1 class="text-center title greenText"><fmt:message key="pricing.pro.title"/></h1>
                        <span class="text-center price">59 <span class="symbol">€HT</span></span>
                        <span class="price_divider" ><fmt:message key="pricing.sub-tip"/></span>
                        <div class="text-center">
                            <button class="button-unstyle big-button button">
                                <a href="/#/teamCreation?plan_id=1" class="link-unstyle" style="font-size: 18px;">
                                    <fmt:message key="pricing.pro.button-text"/>
                                </a>
                            </button>
                        </div>
                        <span class="tip" style="margin: 5px 0 10px 0;"><fmt:message key="pricing.pro.sub-button"/></span>
                        <span class="seats" style="margin-top: 0">
                            <fmt:message key="pricing.pro.seats"/>
                        </span>
                    </div>
                    <div class="features first display-flex flex_direction_column full_flex">
                        <div class="feature title flex_direction_column">
                            <span>
                                <i class="fa fa-check tic"></i>
                                <span><fmt:message key="pricing.pro.features.title"/></span>
                            </span>
                            <i class="fa fa-plus-square plus"></i>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span><fmt:message key="pricing.feature.9"/></span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span><fmt:message key="pricing.feature.10"/></span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span><fmt:message key="pricing.feature.11"/></span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span><fmt:message key="pricing.feature.19"/></span>
                        </div>
                    </div>
                </div>
                <div class="team_plan_wrapper">
                    <div class="team_plan" id="enterprise_team_plan">
                        <img src="/resources/other/Saturn.svg" alt="icon" class="styleImage"/>
                        <h1 class="text-center title"><fmt:message key="pricing.premium.title"/></h1>
                        <span class="text-center price">299 <span class="symbol">€HT</span></span>
                        <span class="price_divider"><fmt:message key="pricing.sub-tip"/></span>
                        <div class="text-center">
                            <button class="button-unstyle big-button button">
                                <a href="/companyContact" class="link-unstyle" style="font-size: 18px;">
                                    <fmt:message key="pricing.premium.button"/>
                                </a>
                            </button>
                        </div>
                        <span class="seats">
                            <fmt:message key="pricing.premium.seats"/>
                        </span>
                    </div>
                    <div class="features first display-flex flex_direction_column full_flex">
                        <div class="feature title flex_direction_column">
                            <span>
                                <i class="fa fa-check tic"></i>
                                <span>
                                    <fmt:message key="pricing.premium.features.title"/>
                                </span>
                            </span>
                            <i class="fa fa-plus-square plus"></i>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span><fmt:message key="pricing.feature.12"/></span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span><fmt:message key="pricing.feature.13"/></span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span><fmt:message key="pricing.feature.14"/></span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span><fmt:message key="pricing.feature.16"/></span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span><fmt:message key="pricing.feature.17"/></span>
                        </div>
                        <div class="feature">
                            <i class="fa fa-check tic"></i>
                            <span><fmt:message key="pricing.feature.18"/></span>
                        </div>
                    </div>
                </div>
            </div>
            <span style="width:30%;border-top: 1px solid #979797; align-self: center"></span>
            <span style="font-size:36px;line-height: 1.83;text-align: center;color:#373B60;margin-top: 30px"><fmt:message key="pricing.user-rating.text"/></span>
            <img style="width: 500px; align-self: center;margin-top: 19px;" src="/resources/images/stars.png"/>
            <span style="font-size:36px;font-weight: bold;line-height: 1.83;text-align: center;color:#373B60;margin-top: 40px">4.92 / 5</span>
        </div>
    </div>
</section>
<%@ include file="templates/landingPage/landingFooter.jsp" %>
<script src="/jsMinified/jquery1.12.4.js"></script>
<script src="/jsMinified/jquery.complexify.js"></script>
<script src="/jsMinified/bootstrap.js" async></script>
<script src="/jsMinified/ajaxHandler.js" async></script>
<script src="/jsMinified/basic-utils.js" async></script>
<script src="/jsMinified/languageChooser.js" async></script>
<script src="/jsMinified/tracker.js" async></script>
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
    $('.feature-1').popup({
      on        : 'hover',
      position  : 'bottom center'
    });
    $('.feature-2').popup({
      on        : 'hover',
      position  : 'bottom center'
    });
    $('.feature-3').popup({
      on        : 'hover',
      position  : 'bottom center'
    });
    $('.feature-4').popup({
      on        : 'hover',
      position  : 'bottom center'
    });
    $('.feature-5').popup({
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
</body>
</html>
