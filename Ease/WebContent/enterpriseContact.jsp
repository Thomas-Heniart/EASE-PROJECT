<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language"
       value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"
       scope="session"/>
<c:if test="${language ne 'en' and language ne 'fr_FR'}">
    <c:set var="language" value="fr_FR" scope="session"/>
</c:if>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="languages/text"/>
<html lang="${language}">
<head>
    <title> Ease.space | Enterprise</title>
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
    <meta http-equiv="Content-Type" contentType="text/html; charset=UTF-8"/>
    <meta name="viewport" content="initial-scale=1, maximum-scale=1"/>
    <meta name="viewport" content="width=device-width, user-scalable=no"/>

    <link rel="icon" type="image/png" href="resources/icons/APPEASE.png"/>
    <link rel="stylesheet" type="text/css" href="//fonts.googleapis.com/css?family=Raleway:500,700"/>
    <link rel="stylesheet" href="semantic/dist/semantic.min.css">
    <link rel="stylesheet" type="text/css" href="/cssMinified.v00058/lib/fonts/museo-font.css"/>
    <link rel="stylesheet" href="/cssMinified.v00058/default_style.css"/>
    <link rel="stylesheet" href="/cssMinified.v00058/bootstrap.css"/>
    <link rel="stylesheet" href="/cssMinified.v00058/landingPage.css"/>
    <link rel="stylesheet" href="/cssMinified.v00058/lib/fonts/font-awesome-4.2.0/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="/cssMinified.v00058/enterpriseContact.css"/>
    <link rel="manifest" href="manifest.json">

</head>

<body>
<%@ include file="templates/landingPage/landingHeader.jsp" %>
<section>
    <div class="container">
        <div class="row">
            <div class="col-lg-12 text-center">
                <h2 class="section-heading" style="margin-top:55px;font-size:36px">
                    <fmt:message key="companyContact.title"/>
                </h2>
                <h3 class="section-subheading text-muted"
                    style="font-size:24px;font-weight:300;line-height: 1.25;color: #373b60!important;">
                    <fmt:message key="companyContact.sub-title"/>
                </h3>
            </div>
        </div>
        <div class="contactSegment row ui segment" style="background-color:#f8f8f8;">
            <form class="ui form">
                <div class="field">
                    <div class="two fields">
                        <div class="field">
                            <label class="contactLabel"><fmt:message key="contact.onInput.name"/>*</label>
                            <input required type="text" name="name" placeholder="Richard Hendricks">
                        </div>
                        <div class="field">
                            <label class="contactLabel">Email*</label>
                            <input required type="email" name="email" placeholder="richard@piedpiper.com">
                        </div>
                    </div>
                </div>
                <div class="field">
                    <div class="two fields">
                        <div class="field">
                            <label class="contactLabel"><fmt:message key="contact.onInput.job"/></label>
                            <input type="text" name="role" placeholder="CEO">
                        </div>
                        <div class="field">
                            <label class="contactLabel"><fmt:message key="contact.onInput.company"/></label>
                            <input type="text" name="enterprise" placeholder="Piedpiper">
                        </div>
                    </div>
                </div>
                <div class="two fields">
                    <div class="field">
                        <label class="contactLabel"><fmt:message key="contact.onInput.phone"/></label>
                        <input type="text" name="phoneNumber" placeholder="<fmt:message key="contact.input.phone"/>">
                    </div>
                    <div class="ui field">
                        <label class="contactLabel"><fmt:message key="companyContact.onInput.employees"/></label>
                        <input type="text" name="collaborators" placeholder="">
                    </div>
                </div>
                <div class="field">
                    <label class="contactLabel">Message*</label>
                    <textarea required name="message" placeholder="<fmt:message key="contact.input.message"/>"></textarea>
                </div>
                <button class="sendContactButton fluid ui button"><fmt:message key="contact.button"/></button>
                <div class="ui positive message" style="display:none">
                    <p><fmt:message key="contact.sent"/></p>
                </div>
                <div class="ui negative message" style="display:none">
                    <p></p>
                </div>
            </form>
        </div>
    </div>
    <div style="text-align:center;margin-bottom:65px;">
        <strong>
            <fmt:message key="contact.assistance.title"/>
        </strong>
        <p>
            <fmt:message key="contact.assistance.text"/>
            <a href="mailto:victor@ease.space">
                <fmt:message key="contact.assistance.link"/>
            </a>
        </p>
    </div>
</section>

<%@ include file="templates/landingPage/landingFooter.jsp" %>
<script src="/jsMinified.v00023/jquery1.12.4.js"></script>
<script src="/jsMinified.v00023/enterpriseContact.js" defer></script>
<script src="/jsMinified.v00023/basic-utils.js" async></script>
<script src="/jsMinified.v00023/ajaxHandler.js" async></script>
<script src="/jsMinified.v00023/languageChooser.js" async></script>
<script src="/jsMinified.v00023/tracker.js" async></script>

<script type="text/javascript">
    window.addEventListener('load', function () {
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
    $('.contactSegment form').submit(function (e) {
        var self = $(this);
        e.preventDefault();
        var button = $("button.sendContactButton", self);
        button.addClass("loading");
        $(".message.negative").hide();
        $(".message.positive").hide();
        ajaxHandler.post(
            "/api/v1/common/PricingContact",
            {
                email: self.find("input[name='email']").val(),
                message: self.find("textarea[name='message']").val(),
                role: self.find("input[name='role']").val(),
                enterprise: self.find("input[name='enterprise']").val(),
                name: self.find("input[name='name']").val(),
                collaborators: self.find("input[name='collaborators']").val(),
                phoneNumber: self.find("input[name='phoneNumber']").val()
            },
            function () {
            },
            function () {
                button.hide();
                $(".message.positive").show();
            }, function (msg) {
                $(".message.negative p").text(msg);
                $(".message.negative").show();
                button.removeClass("loading");
            });
    });
</script>
</body>
</html>
