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
    <link rel="icon" type="image/png" href="resources/icons/APPEASE.png" />

    <link rel="stylesheet" type="text/css" href="//fonts.googleapis.com/css?family=Raleway:500,700" />
    <link rel="stylesheet" type="text/css" href="/cssMinified.v00050/lib/fonts/museo-font.css"/>
    <link rel="stylesheet" href="/cssMinified.v00053/default_style.css"/>
    <link rel="stylesheet" href="/cssMinified.v00053/bootstrap.css"/>
    <link rel="stylesheet" href="/cssMinified.v00053/landingPage.css"/>
    <link rel="stylesheet" href="/cssMinified.v00053/lib/fonts/font-awesome-4.2.0/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="/cssMinified.v00053/securityPage.css"/>

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
            <li><a href="#3" class="cc-active"><p>3.</p><p style="margin-left: 3px;"><fmt:message key="security.menu.title3"/></p></a></li>
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
                <a href="/#/teamCreation?plan_id=0" class="btn btn-xl signUpButton">
                    <fmt:message key="security.button.text"/>
                </a>
            </div>
        </div>
    </div>
</section>
<%@ include file="templates/landingPage/landingFooter.jsp" %>
<script src="/jsMinified.v00023/jquery1.12.4.js"></script>
<script src="/jsMinified.v00023/jquery.complexify.js"></script>
<script src="/jsMinified.v00023/bootstrap.js" async></script>
<script src="/jsMinified.v00023/ajaxHandler.js" async></script>
<script src="/jsMinified.v00023/landingPage.js" async></script>
<script src="/jsMinified.v00023/basic-utils.js" async></script>
<script src="/jsMinified.v00023/languageChooser.js" async></script>
<script src="/jsMinified.v00023/tracker.js" async></script>
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
</body>
</html>