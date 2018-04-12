<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language"
       value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"
       scope="session"/>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="languages/text"/>
<html lang="${language}">
<head>

    <title>Ease.space | Contact</title>
    <!-- Description shown in Google -->
    <meta name="description"
          content="We are there to answer your questions. If you do want to use passwords anymore, feel free.">

    <meta http-equiv="Content-Type" contentType="text/html; charset=UTF-8"/>
    <meta name="viewport" content="initial-scale=1, maximum-scale=1"/>
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
    <link rel="icon" type="image/png" href="resources/icons/APPEASE.png"/>
    <link rel="stylesheet" type="text/css" href="//fonts.googleapis.com/css?family=Raleway:500,700"/>
    <link rel="manifest" href="manifest.json">

    <script src="/jsMinified/jquery1.12.4.js?jsv=2"></script>
    <script src="/jsMinified/bootstrap.js?jsv=2"></script>
    <script src="/jsMinified/basic-utils.js?jsv=2"></script>
    <script src="/jsMinified/ajaxHandler.js?jsv=2"></script>
    <script src="/jsMinified/languageChooser.js?jsv=2"></script>
    <script src="/jsMinified/tracker.js?jsv=2"></script>

    <link rel="stylesheet" href="semantic/dist/semantic.min.css">
    <link rel="stylesheet" href="/css/default_style.css?cssv=7"/>
    <link rel="stylesheet" href="/css/bootstrap.css?cssv=7"/>
    <link rel="stylesheet" href="/css/landingPage.css?cssv=7"/>
    <link rel="stylesheet" href="/css/teamBody.css?cssv=7"/>
    <link rel="stylesheet" href="/css/lib/fonts/font-awesome-4.2.0/css/font-awesome.min.css?cssv=7"/>
    <script type="text/javascript">$crisp = [];
    CRISP_WEBSITE_ID = "6e9fe14b-66f7-487c-8ac9-5912461be78a";
    (function () {
        d = document;
        s = d.createElement("script");
        s.src = "/jsMinified/crisp.js?jsv=2";
        s.async = 1;
        d.getElementsByTagName("head")[0].appendChild(s);
    })();</script>
    <script type="text/javascript" src="jsMinified/tracker.js?jsv=2"></script>
    <!-- Global site tag (gtag.js) - Google Analytics -->
    <script async src="https://www.googletagmanager.com/gtag/js?id=UA-75916041-5"></script>
    <script>
      window.dataLayer = window.dataLayer || [];
      function gtag(){dataLayer.push(arguments);}
      gtag('js', new Date());
      gtag('config', 'UA-75916041-5');
    </script>
</head>

<body>
<!-- Navigation -->
<%@ include file="templates/landingPage/landingHeader.jsp" %>

<section>
    <div class="container">
        <div class="row">
            <div class="col-lg-12 text-center">
                <h2 class="section-heading" style="margin-top:55px;font-size:36px">
                    <fmt:message key="contact.title"/>
                </h2>
                <h3 class="section-subheading text-muted"
                    style="font-size:24px;font-weight:300;line-height: 1.25;color: #373b60!important;">
                    <fmt:message key="contact.underTitle"/>
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
                        <label class="contactLabel"><fmt:message key="contact.onInput.typeOfDemand"/>*</label>
                        <select required class="ui fluid dropdown" name="demand_type"
                                placeholder="Pouvons-nous vous aider ?"
                                style="height:59%;">
                            <option class="item" value="1"><fmt:message key="contact.input.typeOfDemand.1"/></option>
                            <option class="item" value="2"><fmt:message key="contact.input.typeOfDemand.2"/></option>
                            <option class="item" value="3"><fmt:message key="contact.input.typeOfDemand.3"/></option>
                            <option class="item" value="4"><fmt:message key="contact.input.typeOfDemand.4"/></option>
                            <option class="item" value="5"><fmt:message key="contact.input.typeOfDemand.5"/></option>
                        </select>
                    </div>
                </div>
                <div class="field">
                    <label class="contactLabel">Message*</label>
                    <textarea required name="message" placeholder="<fmt:message key="contact.input.message"/>"></textarea>
                </div>
                <button class="sendContactButton fluid ui button"><fmt:message key="contact.button"/></button>
                <div class="ui positive message" style="display: none">
                    <p><fmt:message key="contact.sent"/></p>
                </div>
                <div class="ui negative message" style="display: none">
                    <p></p>
                </div>
            </form>
        </div>
    </div>
    <div style="text-align: center">
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
<script type="text/javascript">
    $('.ui.dropdown').dropdown();
</script>
<script type="text/javascript">
    $('.contactSegment form').submit(function (e) {
        var self = $(this);
        e.preventDefault();
        var button = $("button.sendContactButton", self);
        button.addClass("loading");
        $(".message.negative").hide();
        $(".message.positive").hide();
        ajaxHandler.post(
            "/api/v1/common/ContactUs",
            {
                email: self.find("input[name='email']").val(),
                message: self.find("textarea[name='message']").val(),
                role: self.find("input[name='role']").val(),
                enterprise: self.find("input[name='enterprise']").val(),
                name: self.find("input[name='name']").val(),
                demand_type: self.find("select[name='demand_type']").val(),
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
<%@ include file="templates/landingPage/landingFooter.jsp" %>
</body>
</html>