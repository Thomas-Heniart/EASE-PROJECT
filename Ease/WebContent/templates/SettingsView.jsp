<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@page import="com.Ease.Dashboard.User.User" %>
<% User user = (User) (session.getAttribute("user"));%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="session" scope="session" value="${pageContext.getSession()}"/>
<c:set var="user" scope="session" value='${session.getAttribute("user")}'/>

<script src="/jsMinified.v00015/SettingsView.js"></script>
<script src="/jsMinified.v00015/errorMsg.js"></script>
<script src="/jsMinified.v00015/input.js"></script>
<script src="/jsMinified.v00015/form.js"></script>
<script src="/jsMinified.v00015/popup.js"></script>
<script src="/jsMinified.v00015/emailSuggestions.js"></script>

<div class="settingsWindow">
    <div class="quit">
		<span class="fa-stack fa-lg">
			<i class="fa fa-circle fa-stack-1x" aria-hidden="true"></i>
			<i class="fa fa-times fa-stack-1x" aria-hidden="true"></i>
		</span>
    </div>
    <div class="title">
        <i class="fa fa-fw fa-cogs"></i>
        <p>Settings</p>
    </div>
    <div id="settingsTab">
        <div class="sectionHeader" id="specialOptions">
            <i class="fa fa-caret-right down" aria-hidden="true"></i>
            <p class="sectionDescription">Ease.space options</p>
            <div class="setting show">
                <%@include file="SpecialOptions.jsp" %>
            </div>
        </div>
        <div class="sectionHeader" id="nameSection">
            <i class="fa fa-caret-right down" aria-hidden="true"></i>
            <p class="sectionDescription">How would you like us to call you ? <span class="smallDescription">(no judgements)</span>
            </p>
            <%@ include file="EditUserName.jsp" %>
        </div>
        <div class="sectionHeader" id="emailSection">
            <i class="fa fa-caret-right down" aria-hidden="true"></i>
            <p class="sectionDescription">Emails</p>
            <%@ include file="EditVerifiedEmails.jsp" %>
        </div>
        <div class="sectionHeader" id="passwordSection">
            <i class="fa fa-caret-right" aria-hidden="true"></i>
            <p class="sectionDescription">Ease Password</p>
            <%@ include file="EditUserPassword.jsp" %>
        </div>
        <div class="sectionHeader" id="passwordSection">
            <i class="fa fa-caret-right" aria-hidden="true"></i>
            <p class="sectionDescription">Delete Ease Account</p>
            <%@ include file="DeleteAccount.jsp" %>
        </div>
    </div>
</div>