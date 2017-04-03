<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ page import="com.Ease.Dashboard.User.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<% User user = (User) (session.getAttribute("user"));%>
<%
if (user == null){
%>
<jsp:forward page="/login" />
<%} else {%>
<jsp:forward page="/home" />
<%}%>
