<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language"
       value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"
       scope="session"/>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="com.Ease.Languages.text"/>
<html lang="${language}">
<head>

    <title>Ease.space | Contact</title>
    <!-- Description shown in Google -->
    <meta name="description"
          content="We are there to answer your questions. If you do want to use passwords anymore, feel free.">

    <meta http-equiv="Content-Type" contentType="text/html; charset=UTF-8"/>
    <meta name="viewport" content="initial-scale=1, maximum-scale=1"/>
    <meta property="og:image"
          content="https://ease.space/resources/other/fb_letsgo_icon.jpg"/>
    <link rel="icon" type="image/png" href="resources/icons/APPEASE.png"/>
    <link rel="stylesheet" type="text/css" href="//fonts.googleapis.com/css?family=Raleway:500,700"/>

    <link rel="manifest" href="manifest.json">

    <script src="/jsMinified.v00017/jquery1.12.4.js"></script>
    <script src="/jsMinified.v00017/bootstrap.js"></script>
    <script src="/jsMinified.v00017/basic-utils.js"></script>
    <script src="/jsMinified.v00017/ajaxHandler.js"></script>
    <script src="/jsMinified.v00017/languageChooser.js"></script>
    <script src="/jsMinified.v00017/tracker.js"></script>

    <link rel="stylesheet" href="semantic/dist/semantic.min.css">
    <link rel="stylesheet" href="/cssMinified.v00012/default_style.css"/>
    <link rel="stylesheet" href="/cssMinified.v00012/bootstrap.css"/>
    <link rel="stylesheet" href="/cssMinified.v00012/landingPage.css"/>
    <link rel="stylesheet" href="/cssMinified.v00012/teamBody.css"/>
    <link rel="stylesheet" href="/cssMinified.v00012/lib/fonts/font-awesome-4.2.0/css/font-awesome.min.css"/>
    <script type="text/javascript">$crisp = [];
    CRISP_WEBSITE_ID = "6e9fe14b-66f7-487c-8ac9-5912461be78a";
    (function () {
        d = document;
        s = d.createElement("script");
        s.src = "/jsMinified.v00017/crisp.js";
        s.async = 1;
        d.getElementsByTagName("head")[0].appendChild(s);
    })();</script>
    <script type="text/javascript">
        (function (e, t) {
            var n = e.amplitude || {_q: [], _iq: {}};
            var r = t.createElement("script");
            r.type = "text/javascript";
            r.async = true;
            r.src = "/jsMinified.v00017/amplitude-analytics.js";
            r.onload = function () {
                e.amplitude.runQueuedFunctions()
            };
            var i = t.getElementsByTagName("script")[0];
            i.parentNode.insertBefore(r, i);

            function s(e, t) {
                e.prototype[t] = function () {
                    this._q.push([t].concat(Array.prototype.slice.call(arguments, 0)));
                    return this
                }
            }

            var o = function () {
                this._q = [];
                return this
            };
            var a = ["add", "append", "clearAll", "prepend", "set", "setOnce", "unset"];
            for (var u = 0; u < a.length; u++) {
                s(o, a[u])
            }
            n.Identify = o;
            var c = function () {
                this._q = [];
                return this;
            };
            var p = ["setProductId", "setQuantity", "setPrice", "setRevenueType", "setEventProperties"];
            for (var l = 0; l < p.length; l++) {
                s(c, p[l])
            }
            n.Revenue = c;
            var d = ["init", "logEvent", "logRevenue", "setUserId", "setUserProperties", "setOptOut", "setVersionName", "setDomain", "setDeviceId", "setGlobalUserProperties", "identify", "clearUserProperties", "setGroup", "logRevenueV2", "regenerateDeviceId"];

            function v(e) {
                function t(t) {
                    e[t] = function () {
                        e._q.push([t].concat(Array.prototype.slice.call(arguments, 0)));
                    }
                }

                for (var n = 0; n < d.length; n++) {
                    t(d[n])
                }
            }

            v(n);
            n.getInstance = function (e) {
                e = (!e || e.length === 0 ? "$default_instance" : e).toLowerCase();
                if (!n._iq.hasOwnProperty(e)) {
                    n._iq[e] = {_q: []};
                    v(n._iq[e])
                }
                return n._iq[e]
            };
            e.amplitude = n;
        })(window, document);

        /* Prod */
        //amplitude.getInstance().init("74f6ebfba0c7743a0c63012dc3a9fef0");

        /* Test */
        amplitude.getInstance().init("73264447f97c4623fb38d92b9e7eaeea");
        easeTracker.trackEvent("HomepageContactVisit");
    </script>
</head>

<body>
<!-- Navigation -->
<%@ include file="templates/landingPage/landingHeader.jsp" %>

<section>
    <div class="container">
        <div class="row">
            <div class="col-lg-12 text-center">
                <h2 class="section-heading">
                    Contact
                </h2>
                <h3 class="section-subheading text-muted"
                    style="font-size:24px;font-weight:300;line-height: 1.25;color: #373b60!important;">
                    Nous nous tenons à votre disposition pour discuter de notre collaboration
                </h3>
            </div>
        </div>
        <div class="contactSegment row ui segment" style="background-color:#f8f8f8;">
            <form class="ui form">
                <div class="field">
                    <div class="two fields">
                        <div class="field">
                            <label class="contactLabel">Prénom et Nom*</label>
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
                            <label class="contactLabel">Intitulé du poste</label>
                            <input type="text" name="role" placeholder="CEO">
                        </div>
                        <div class="field">
                            <label class="contactLabel">Société</label>
                            <input type="text" name="enterprise" placeholder="Piedpiper">
                        </div>
                    </div>
                </div>
                <div class="two fields">
                    <div class="field">
                        <label class="contactLabel">Téléphone</label>
                        <input type="text" name="phoneNumber" placeholder="06">
                    </div>
                    <div class="ui field">
                        <label class="contactLabel">Type de demande*</label>
                        <select required class="ui fluid dropdown" name="demand_type"
                                placeholder="Pouvons-nous vous aider ?"
                                style="height:59%;">
                            <option class="item" value="1">À propos du produit</option>
                            <option class="item" value="2">À propos de la sécurité</option>
                            <option class="item" value="3">Jobs</option>
                            <option class="item" value="4">Presse</option>
                            <option class="item" value="5">Autre demande</option>
                        </select>
                    </div>
                </div>
                <div class="field">
                    <label class="contactLabel">Message*</label>
                    <textarea required name="message" placeholder="Votre message."></textarea>
                </div>
                <button class="sendContactButton fluid ui button">Envoyer</button>
                <div class="ui positive message" style="display: none">
                    <p>Message envoyé</p>
                </div>
            </form>
        </div>
    </div>
    <div style="text-align: center">
        <strong>
            <fmt:message key="companyContact.help1.title"/>
        </strong>
        <p>
            <fmt:message key="companyContact.help1.text"/> <a href="mailto:victor@ease.space"><fmt:message
                key="companyContact.help1.link-text"/></a>
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
        $("button", self).addClass("loading");
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
                $("button", self).addClass("loading");
            },
            function () {
                $("button", self).hide();
                $(".message.positive").show();
                easeTracker.trackEvent("HomepageContactSubmit");
            });
    });
</script>
<%@ include file="templates/landingPage/landingFooter.jsp" %>
</body>
</html>