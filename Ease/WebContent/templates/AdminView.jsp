<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="com.Ease.session.User" %>
<%@ page import="com.Ease.session.Profile" %>
<%@ page import="com.Ease.context.Site" %>
<%@ page import="com.Ease.context.SiteManager" %>
<%@ page import="com.Ease.context.Color" %>
<%@ page import="com.Ease.session.Account" %>
<%@ page import="com.Ease.session.LogWith" %>
<%@ page import="java.util.List" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
com.Ease.session.User user = (com.Ease.session.User)(session.getAttribute("User"));
%>

<div class="AdminMenu show">
	<%@ include file="AddUsers.jsp"%>
	<%@ include file="RequestedSitesView.jsp"%>
	<%@ include file="UploadWebsite.jsp" %>
	<%@ include file="TagsManager.jsp" %>
</div>