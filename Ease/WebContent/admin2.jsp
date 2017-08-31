<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.Ease.Dashboard.User.User" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/xhtml1-transitional.dtd">
<html xmlns="http://w3.org/1999/xhtml">
<head>
    <% User user = (User) (session.getAttribute("user"));
        if (session.getAttribute("user") == null || !((User) session.getAttribute("user")).isAdmin()) {
    %>
    <script>
        window.location.replace("/");
    </script>
    <% } %>
    <meta http-equiv="Content-Type" contentType="text/html; charset=UTF-8"/>
    <meta name="viewport" content="initial-scale=1, maximum-scale=1"/>
    <title>Ease.space admin</title>
    <link rel="icon" type="image/png" href="resources/icons/APPEASE.png"/>
    <link rel="manifest" href="manifest.json">
    <script src="jsMinified.v00015/jquery-3.1.0.js"></script>
    <script src="jsMinified.v00015/ajaxHandler.js"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/semantic-ui/2.2.10/semantic.min.css">
    <script src="https://cdn.jsdelivr.net/semantic-ui/2.2.10/semantic.min.js"></script>
    <link rel="stylesheet" type="text/css"
          href="css/lib/fonts/font-awesome-4.2.0/css/font-awesome.min.css"/>
    <script src="js/admin2.js"></script>
</head>
<body role="document" class="mainBody">
<table class="ui celled table">
    <thead>
    <tr>
        <th>id</th>
        <th>Name</th>
        <th>Logo</th>
        <th>Connection URL</th>
        <th>Landing URL</th>
        <th><i class="fa fa-eye-slash"/></th>
        <th>Edit</th>
        <th><i class="fa fa-trash-o"/></th>
    </tr>
    </thead>
    <tbody id="website-manager-body">

    </tbody>
</table>
</body>
</html>
