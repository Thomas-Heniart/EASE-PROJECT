<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Base64" %>
<%@ page import="java.util.Date" %>

<c:redirect url="/"/>
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

    <link rel="chrome-webstore-item" href="https://chrome.google.com/webstore/detail/hnacegpfmpknpdjmhdmpkmedplfcmdmp"/>
    <link rel="icon" type="image/png" href="resources/icons/APPEASE.png"/>
    <link rel="stylesheet" type="text/css" href="/css/lib/fonts/museo-font.css?cssv=7"/>
    <link rel="stylesheet" type="text/css" href="//fonts.googleapis.com/css?family=Raleway:500,700"/>
    <link rel="stylesheet" href="/css/team.css?cssv=7"/>
    <link rel="stylesheet" href="/css/bootstrap.css?cssv=7"/>

    <link rel="stylesheet" href="/css/team.css?cssv=7"/>
    <link rel="stylesheet" href="/css/bootstrap.css?cssv=7"/>
    <link rel="stylesheet" href="/semantic/dist/semantic.min.css"/>
    <link rel="stylesheet" href="/css/default_style.css?cssv=7"/>
    <link rel="stylesheet" href="/css/lib/fonts/font-awesome-4.2.0/css/font-awesome.min.css?cssv=7"/>
    <link rel="manifest" href="manifest.json">

    <link rel="stylesheet" href="/css/default_style.css?cssv=7"/>
    <link rel="stylesheet" href="/css/lib/fonts/font-awesome-4.2.0/css/font-awesome.min.css?cssv=7"/>
    <script src="https://js.stripe.com/v3/"></script>
    <script src="/jsMinified/moment.js?jsv=2"></script>
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
    <script src="teams_bundle.js?00000014"></script>
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
            <c:if test='${user.getStatus().tutoIsDone() eq false}'>
                <%@ include file="/templates/Tips.jsp" %>
            </c:if>
        </div>
        <div class="SettingsView <c:if test='${settingsOpen ne null}'>show</c:if>">
        </div>
    </div>
    <div class="popupHandler" id="easePopupsHandler">
    </div>
    <%@ include file="templates/new_extension.html" %>
</div>
<noscript id="deferred-styles">
    <link rel="stylesheet" href="/css/lib/niftyPopupWindow/component.css?cssv=7">
</noscript>
<script src="/jsMinified/jquery1.12.4.js?jsv=2"></script>
<script src="/jsMinified/jquery-ui-1.12.0.js?jsv=2" defer></script>
<script src="/jsMinified/Sortable.js?jsv=2" defer></script>
<script src="/jsMinified/app.js?jsv=2" defer></script>
<script src="/jsMinified/profiles.js?jsv=2" defer></script>
<script src="/jsMinified/dashboard.js?jsv=2" defer></script>
<script src="/jsMinified/websocket.js?jsv=2" async></script>
<script src="/jsMinified/tracker.js?jsv=2" async></script>
<script src="/jsMinified/asyncContentLoading.js?jsv=2" async></script>
<script src="/jsMinified/postHandler.js?jsv=2" async></script>
<script src="/jsMinified/basic-utils.js?jsv=2" async></script>
<script src="/jsMinified/extension.js?jsv=2" async></script>
<script src="/jsMinified/header.js?jsv=2" async></script>
<script src="/jsMinified/selectFx.js?jsv=2" async></script>
<script src="/jsMinified/isMobile.js?jsv=2" async></script>
<script src="/jsMinified/shortcut.js?jsv=2" async></script>
<script src="/jsMinified/logout.js?jsv=2" async></script>
<script src="/jsMinified/checkConnection.js?jsv=2" async></script>
<script src="/jsMinified/ajaxHandler.js?jsv=2" async></script>
<script src="/jsMinified/jsencrypt.js?jsv=2" async></script>
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
                        "/jsMinified/modifyAppPopup.js",
                        "/jsMinified/addAppPopup.js",
                        "/jsMinified/deleteAccountPopup.js",
                        "/jsMinified/deleteProfilePopup.js",
                        "/jsMinified/addBookmarkPopup.js",
                        "/jsMinified/popupHandler.js"
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
                    ['/jsMinified/catalogApp.js',
                        '/jsMinified/catalog.js',
                        '/jsMinified/update.js',
                        '/jsMinified/updatesManager.js'
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
                asyncLoading.loadSingleScript("/jsMinified/footer.js");
            }
        });
    });
</script>
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
</script>
</body>
</html>