<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/xhtml1-transitional.dtd">
<html xmlns="http://w3.org/1999/xhtml">
<head>
    <%
        if (session.getAttribute("is_admin") == null || !((Boolean) session.getAttribute("is_admin"))) {
    %>
    <script>
        window.location.replace("/");
    </script>
    <% } %>
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
    <title>Ease.space admin</title>
    <link rel="icon" type="image/png" href="resources/icons/APPEASE.png"/>
    <link rel="manifest" href="manifest.json">
    <script src="jsMinified/jquery-3.1.0.js?jsv=1"></script>
    <script src="js/ajaxHandler.js"></script>
    <script src="js/backOffice/admin.js?9"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/semantic-ui/2.2.10/semantic.min.css">
    <script src="https://cdn.jsdelivr.net/semantic-ui/2.2.10/semantic.min.js"></script>
    <script src="https://semantic-ui.com/javascript/library/tablesort.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.1/Chart.bundle.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.1/Chart.min.js"></script>
    <link rel="stylesheet" href="js/backOffice/cornelius.css"/>
    <script type="application/javascript" src="js/backOffice/cornelius.js"></script>
    <link rel="stylesheet" type="text/css"
          href="css/lib/fonts/font-awesome-4.2.0/css/font-awesome.min.css"/>
    <style>
        .ui.segment {
            overflow: auto;
            max-height: 600px;
            max-width: 99%;
        }

        .ui.celled.table tr td, .ui.celled.table tr th {
            max-width: 250px;
            overflow: auto;
        }

        #onboarding_rooms_tabs div.image {
            margin-bottom: 0.5em;
        }

        #onboarding_rooms_menu {
            max-width: 100%;
            width: auto;
        }
    </style>
</head>
<body role="document" class="mainBody">
<div class="ui grid">
    <div class="four wide column">
        <div id="admin_menu" class="ui vertical fluid tabular menu">
            <a class="item" data-target="#team-segment">Teams</a>
            <a class="item" data-target="#website-segment">Websites</a>
            <a class="item" data-target="#category-segment">Categories</a>
            <a class="item" data-target="#website-requests-segment">Requests</a>
            <a class="item" data-target="#website-failures-segment">Website failures</a>
            <a class="item" data-target="#background-upload-segment">Background upload</a>
            <a class="item" data-target="#onboarding-rooms-segment">Onboarding rooms</a>
            <a class="item" data-target="#teams-deleted-segment">Teams deleted</a>
            <a class="item" data-target="#statistics">Statistics</a>
        </div>
    </div>
    <div class="twelve wide stretched column tabs">
        <div id="team-segment" class="ui segment loading backOffice_part" style="display: none">
            <%@ include file="templates/backofficeAdmin/TeamManager.jsp" %>
        </div>
        <div id="website-segment" class="ui segment loading backOffice_part" style="display: none;">
            <%@include file="templates/backofficeAdmin/WebsiteManager.jsp" %>
        </div>
        <div id="category-segment" class="ui segment loading backOffice_part" style="display: none;">
            <%@include file="templates/backofficeAdmin/CategoryManager.jsp" %>
        </div>
        <div id="website-requests-segment" class="ui segment loading backOffice_part" style="display: none;">
            <%@include file="templates/backofficeAdmin/WebisteRequests.jsp" %>
        </div>
        <div id="website-failures-segment" class="ui segment loading backOffice_part" style="display: none;">
            <%@include file="templates/backofficeAdmin/WebisteFailures.jsp" %>
        </div>
        <div id="background-upload-segment" class="ui segment loading backOffice_part" style="display: none">
            <%@include file="templates/backofficeAdmin/BackgorundUpload.jsp" %>
        </div>
        <div id="teams-deleted-segment" class="ui segment loading backOffice_part" style="display: none">
            <%@include file="templates/backofficeAdmin/TeamsDeleted.jsp" %>
        </div>
        <div id="onboarding-rooms-segment" class="ui segment loading backOffice_part" style="display: none">
            <%@include file="templates/backofficeAdmin/OnboardingRooms.jsp" %>
        </div>
        <div id="statistics" class="ui segment loading backOffice_part" style="display: none">
            <%@include file="templates/backofficeAdmin/Statistics.jsp" %>
        </div>
    </div>
</div>
</body>
</html>
