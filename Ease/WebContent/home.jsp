<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags/dashboard" prefix="dashboard" %>
<%@ page import="com.Ease.Dashboard.User.SessionSave" %>
<%@ page import="com.Ease.Dashboard.User.User" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Base64" %>
<%@ page import="java.util.Date" %>

<% User user = (User) (session.getAttribute("user"));%>
<%
    if (user == null) {
%>
<c:redirect url="/"/>
<%}%>
<%
    SessionSave sessionSave = user.getSessionSave();
    Cookie sessionId = new Cookie("sId", sessionSave.getSessionId());
    Cookie sessionToken = new Cookie("sTk", sessionSave.getToken());
    Cookie skipLanding = new Cookie("skipLanding", "true");
    DateFormat dateFormat = new SimpleDateFormat("HH");
    Date date = new Date();
    int duration = 29 - Integer.parseInt(dateFormat.format(date));
    if (duration > 24) duration = duration - 24;
    duration = (duration * 60 - 30) * 60;
    sessionId.setMaxAge(duration);
    sessionToken.setMaxAge(duration);
    skipLanding.setMaxAge(60 * 60 * 24 * 14);
    response.addCookie(skipLanding);
    response.addCookie(sessionId);
    response.addCookie(sessionToken);
%>
<%
    Cookie fname = new Cookie("fname",
            Base64.getEncoder().encodeToString(user.getFirstName().getBytes(StandardCharsets.UTF_8)));
    Cookie email = new Cookie("email", user.getEmail());

    fname.setMaxAge(60 * 60 * 24 * 31);
    email.setMaxAge(60 * 60 * 24 * 31);
    response.addCookie(fname);
    response.addCookie(email);
%>
<c:set var="session" scope="session" value="${pageContext.getSession()}"/>
<c:set var="servletContext" scope="session" value="${session.getServletContext()}"/>
<c:set var="user" scope="session" value='${session.getAttribute("user")}'/>
<c:set var="colors" scope="session" value='${servletContext.getAttribute("Colors")}'/>
<c:set var="groupManager" scope="session" value='${servletContext.getAttribute("groupManager")}'/>
<c:set var="dashboard" scope="session" value='${user.getDashboardManager()}'/>
<c:set var="dashboardColumns" scope="session" value="${dashboard.getProfiles()}"/>
<c:set var="catalog" scope="session" value='${servletContext.getAttribute("catalog")}'/>
<c:set var="siteList" scope="session" value='${catalog.getWebsites()}'/>
<c:set var="tags" scope="session" value='${servletContext.getAttribute("Tags")}'/>
<c:set var="tagAndSiteMapping" scope="session" value='${servletContext.getAttribute("TagAndSiteMapping")}'/>
<c:set var="settingsOpen" scope="session" value="${param.openSettings}"/>

<!-- ========= DOCUMENT START ========= -->
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/xhtml1-transitional.dtd">
<html xmlns="http://w3.org/1999/xhtml" xmlns:og="http://ogp.me/ns#">
<head>
    <meta http-equiv="Content-Type" contentType="text/html; charset=UTF-8"/>
    <meta name="viewport" content="initial-scale=1, maximum-scale=1"/>
    <title> Ease.space</title>
    <!-- Description shown in Google -->
    <!-- Facebook metadata -->
    <meta name="description"
          content="Ease est la homepage qui vous débarrasse des mots de passe. En 1 clic, soyez connecté à vos sites web automatiquement."/>
    <meta property="og:url" content="https://ease.space/"/>
    <meta property="og:type" content="website"/>
    <meta property="og:title" content="Ease.space"/>
    <meta property="og:logo" content="https://ease.space/resources/icons/APPEASE.png"/>
    <meta property="og:description"
          content="Ease est la homepage qui vous débarrasse des mots de passe. En 1 clic, soyez connecté à vos sites web automatiquement."/>
    <meta property="og:image" content="https://ease.space/resources/images/fbmeta-fr.png"/>
    <!-- Twitter metadata -->
    <meta name="twitter:card" content="summary_large_image"/>
    <meta name="twitter:site" content="@Ease_app"/>
    <meta name="twitter:creator" content="@Ease_app"/>
    <meta name="twitter:title" content="Ease.space"/>
    <meta name="twitter:description"
          content="Ease est la homepage qui vous débarrasse des mots de passe. En 1 clic, soyez connecté à vos sites web automatiquement."/>
    <meta name="twitter:image" content="https://ease.space/resources/images/fbmeta-en.png"/>

    <link rel="chrome-webstore-item"
          href="https://chrome.google.com/webstore/detail/hnacegpfmpknpdjmhdmpkmedplfcmdmp"/>
    <link rel="icon" type="image/png" href="resources/icons/APPEASE.png"/>
    <link rel="stylesheet" type="text/css" href="/cssMinified.v00013/lib/fonts/museo-font.css"/>
    <link rel="stylesheet" type="text/css" href="//fonts.googleapis.com/css?family=Raleway:500,700"/>

    <link rel="stylesheet" href="/cssMinified.v00013/team.css"/>
    <link rel="stylesheet" href="/cssMinified.v00013/bootstrap.css"/>
    <link rel="stylesheet" href="/semantic/dist/semantic.min.css"/>
    <link rel="stylesheet" href="/cssMinified.v00013/default_style.css"/>
    <link rel="stylesheet" href="/cssMinified.v00013/lib/fonts/font-awesome-4.2.0/css/font-awesome.min.css"/>
    <script src="https://js.stripe.com/v3/"></script>

    <link rel="manifest" href="manifest.json">

</head>

<body role="document"
      class='<%= ((user.getOptions().isBackground_picked()) ? "mainBody picBckgrnd" : "mainBody logoBckgrnd") %>'>
<div id="userEmail" data-content=<%= user.getEmail() %>></div>
<div id="onMobile" style="display:none;">
    <%@ include file="/templates/Mobile.html" %>
</div>

<div id="onComputer">
    <div class="ease-header" id="app">
        <%--@ include file="/templates/Header.jsp"--%>
    </div>
    <script src="teams_bundle.js?00000004s"></script>
    <div id="loggedBody">
        <div class="col-left <c:if test='${settingsOpen eq null}'>show</c:if>" style="width: 100%; float:left">
            <%@ include file="/templates/ProfileView.jsp" %>
            <%--<div class="MenuButtonSet waiting-load">
                <button id="enterEditMode" state="off" class="button<c:if test="${param.catalogOpen}"> editMode</c:if>">
                    <img src="resources/icons/menu_icon.png"/>
                    <div class="openCatalogHelper"></div>
                </button>
            </div>--%>
            <div class="CatalogViewTab <c:if test="${param.catalogOpen}">show</c:if>">
            </div>
            <c:if test='${user.appsImported() && (user.getStatus().tutoIsDone() eq false)}'>
                <%@ include file="/templates/Tips.jsp" %>
            </c:if>
        </div>
        <div class="SettingsView <c:if test='${settingsOpen ne null}'>show</c:if>">
        </div>
    </div>
    <div class="popupHandler" id="easePopupsHandler">
    </div>
    <c:if test='${(user.appsImported() eq false) || (param.importAccounts)}'>
        <%@ include file="/templates/TutorialView.jsp" %>
        <script type="text/javascript">
            window.addEventListener('load', function () {
                asyncLoading.loadScriptsOneByOne(["/jsMinified.v00019/tutorialSteps.js"]);
            });
        </script>
    </c:if>
    <%@ include file="templates/new_extension.html" %>
</div>
<noscript id="deferred-styles">
    <link rel="stylesheet" href="/cssMinified.v00013/lib/niftyPopupWindow/component.css">
</noscript>
<script src="/jsMinified.v00019/jquery1.12.4.js"></script>
<script src="/jsMinified.v00019/moment.js"></script>
<script src="/jsMinified.v00019/jquery-ui-1.12.0.js" defer></script>
<script src="/jsMinified.v00019/Sortable.js" defer></script>
<script src="/jsMinified.v00019/app.js" defer></script>
<script src="/jsMinified.v00019/profiles.js" defer></script>
<script src="/jsMinified.v00019/dashboard.js" defer></script>
<script src="/jsMinified.v00019/websocket.js" async></script>
<script src="/jsMinified.v00019/tracker.js" async></script>
<script src="/jsMinified.v00019/asyncContentLoading.js" async></script>
<script src="/jsMinified.v00019/postHandler.js" async></script>
<script src="/jsMinified.v00019/basic-utils.js" async></script>
<script src="/jsMinified.v00019/extension.js" async></script>
<script src="/jsMinified.v00019/header.js" async></script>
<script src="/jsMinified.v00019/selectFx.js" async></script>
<script src="/jsMinified.v00019/isMobile.js" async></script>
<script src="/jsMinified.v00019/shortcut.js" async></script>
<script src="/jsMinified.v00019/logout.js" async></script>
<script src="/jsMinified.v00019/checkConnection.js" async></script>
<script src="/jsMinified.v00019/ajaxHandler.js" async></script>
<script src="/jsMinified.v00019/jsencrypt.js" async></script>
<script>
    var images = document.getElementsByClassName('logo');
    Array.prototype.forEach.call(images, function (element) {
        if (element.hasAttribute('lazy-src')) {
            var src = element.getAttribute('lazy-src');
            var myImage = new Image();
            myImage.onload = function () {
                element.setAttribute('src', myImage.src);
            };
            myImage.src = src;
        }
    });
</script>

<script type="text/javascript">
    var loadDeferredStyles = function () {
        var addStylesNode = document.getElementById("deferred-styles");
        var replacement = document.createElement("div");
        replacement.innerHTML = addStylesNode.textContent;
        document.body.appendChild(replacement);
        addStylesNode.parentElement.removeChild(addStylesNode);
    };
    window.addEventListener('load', loadDeferredStyles);
</script>
<script type="text/javascript">
    window.addEventListener('load', function () {
        setTimeout(function () {
            var event = new CustomEvent("NewEaseUser", {"detail": $("#userEmail").data("content")});
            document.dispatchEvent(event);
        }, 500)
    });
    window.addEventListener('load', function () {
        asyncLoading.loadHtml({
            urls: ["/templates/popups/modifyAppPopup.html",
                "/templates/popups/addBookmarkPopup.html",
                "/templates/popups/addAppPopup.html",
                "/templates/popups/deleteProfilePopup.html",
                "/templates/popups/deleteAccountPopup.html"
            ],
            appendTo: '#easePopupsHandler',
            callback: function () {
                asyncLoading.loadScriptsOneByOne(
                    [
                        "/jsMinified.v00019/modifyAppPopup.js",
                        "/jsMinified.v00019/addAppPopup.js",
                        "/jsMinified.v00019/deleteAccountPopup.js",
                        "/jsMinified.v00019/deleteProfilePopup.js",
                        "/jsMinified.v00019/addBookmarkPopup.js",
                        "/jsMinified.v00019/popupHandler.js"
                    ]
                );
            }
        });
        asyncLoading.loadHtml({
            urls: ['/templates/SettingsView.jsp'],
            appendTo: '#loggedBody .SettingsView'
        });
        asyncLoading.loadHtml({
            urls: ['/templates/catalog/catalogView.jsp'],
            appendTo: '.col-left .CatalogViewTab',
            callback: function () {
                asyncLoading.loadScriptsOneByOne(
                    ['/jsMinified.v00019/catalogApp.js',
                        '/jsMinified.v00019/catalog.js',
                        '/jsMinified.v00019/update.js',
                        '/jsMinified.v00019/updatesManager.js'
                    ],
                    function () {
                        $('.MenuButtonSet.waiting-load').removeClass('waiting-load');
                    });
            }
        });
        asyncLoading.loadHtml({
            urls: ['/templates/Footer.html'],
            appendTo: '#onComputer',
            callback: function () {
                asyncLoading.loadSingleScript("/jsMinified.v00019/footer.js");
            }
        });
    });
</script>
<script type="text/javascript">
    window.addEventListener('load', function () {
        (function (e, t) {
            var n = e.amplitude || {_q: [], _iq: {}};
            var r = t.createElement("script");
            r.type = "text/javascript";
            r.async = true;
            r.src = "/jsMinified.v00019/amplitude-analytics.js";
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

        amplitude.getInstance().init("73264447f97c4623fb38d92b9e7eaeea");

        var appCount = $(".SitesContainer .siteLinkBox").length;
        var verifiedEmailCount = $(".verifiedEmail").length;
        var unverifiedEmailCount = $(".unverifiedEmail").length;
        var emailCount = verifiedEmailCount + unverifiedEmailCount;
        easeTracker.setUserProperty("AppCount", appCount);
        easeTracker.setUserProperty("EmailCount", emailCount);
        easeTracker.setUserProperty("EmailVerifiedCount", verifiedEmailCount);
        easeTracker.setUserProperty("EmailNonVerifiedCount", unverifiedEmailCount);
        easeTracker.setDailyPhoto($('#backgroundSwitch').is("checked"));
    });
</script>

<!-- Amplitude script -->
<script type="text/javascript">
    window.addEventListener('load', function () {
        $crisp = [];
        CRISP_WEBSITE_ID = "6e9fe14b-66f7-487c-8ac9-5912461be78a";
        (function () {
            d = document;
            s = d.createElement("script");
            s.src = "/jsMinified.v00019/thirdParty/crisp.js";
            s.async = 1;
            d.getElementsByTagName("head")[0].appendChild(s);
        })();
    });
</script>
</body>
</html>