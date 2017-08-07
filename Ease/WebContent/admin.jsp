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
    <title>Ease.space admin</title>

    <link rel="icon" type="image/png" href="resources/icons/APPEASE.png"/>
    <link rel="manifest" href="manifest.json">

    <link rel="stylesheet" href="css/admin.css"/>
    <script src="jsMinified.v00014/jquery-3.1.0.js"></script>
    <script src="js/admin.js"></script>
</head>


<body role="document" class="mainBody">
<c:set var="groupManager" scope="session" value='${servletContext.getAttribute("groupManager")}'/>
<c:set var="websitesVisitedManager" scope="session" value='${servletContext.getAttribute("websitesVisitedManager")}'/>
<div class="ease-admin-menu">
    <a class="ease-admin-menu-anchor" href="#">Menu</a>
    <ol class="ease-admin-menu-dropdown">
        <li>Add a website to Ease</li>
        <li>Teams' owner</li>
    </ol>
</div>
<div class="ease-admin-content">

</div>
</body>
</html>
