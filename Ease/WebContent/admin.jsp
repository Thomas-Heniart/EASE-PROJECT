<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.Ease.Dashboard.User.User" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/xhtml1-transitional.dtd">
<html xmlns="http://w3.org/1999/xhtml">
<head>
    <% User user = (User) (session.getAttribute("user")); %>
    <%
        if (session.getAttribute("user") == null || !((User) session.getAttribute("user")).isAdmin()) {
    %>
    <script>
        window.location.replace("index.jsp");
    </script>
    <% } %>
    <meta http-equiv="Content-Type" contentType="text/html; charset=UTF-8"/>
    <meta name="viewport" content="initial-scale=1, maximum-scale=1"/>
    <title>EASE.space</title>
    <link rel="chrome-webstore-item"
          href="https://chrome.google.com/webstore/detail/hnacegpfmpknpdjmhdmpkmedplfcmdmp">

    <link rel="icon" type="image/png" href="resources/icons/APPEASE.png"/>
    <link rel="stylesheet" href="css/default_style.css"/>
    <link href='https://fonts.googleapis.comcss?family=Source+Sans+Pro'
          rel='stylesheet' type='textcss'/>
    <link rel="stylesheet"
          href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"/>
    <link rel="manifest" href="manifest.json">

    <script src="jsMinified.v00022/jquery-3.1.0.js"></script>
    <script src="js/postHandler.js"></script>
    <script src="js/admin.js"></script>
</head>


<body role="document" class="mainBody">
<div id="loggedBody">
    <div class="col-left show" style="width: 100%; float: left">
        <%@ include file="templates/AdminView.jsp" %>
    </div>
    <div class="md-overlay"></div>
</div>
<%@ include file="templates/Footer.html" %>
<div class="la-anim-10" id="loading"></div>
<%@ include file="templates/ChatButton.jsp" %>
</body>
</html>