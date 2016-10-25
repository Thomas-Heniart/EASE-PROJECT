<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="SettingsView">
	<div class="settingsWindow">
		<div class="quit">
			<i class="fa fa-times" aria-hidden="true"></i>
		</div>
		<div class="title">
			<i class="fa fa-fw fa-cogs"></i>
			<p>Settings</p>
		</div>
		<div id="settingsTab">
			<div class="sectionHeader" id="nameSection">
				<i class="fa fa-caret-right" aria-hidden="true"></i><p class="sectionDescription">How would you like us to call you ? <span class="smallDescription">(no judgements)</span></p>
				<%@ include file="EditUserName.jsp"%>
			</div>
			<div class="sectionHeader" id="emailSection">
				<i class="fa fa-caret-right" aria-hidden="true"></i><p class="sectionDescription">Emails</p>
				<%@ include file="EditVerifiedEmails.jsp" %>
			</div>
			<div class="sectionHeader" id="passwordSection">
				<i class="fa fa-caret-right" aria-hidden="true"></i><p class="sectionDescription">Ease Password</p>
				<%@ include file="EditUserPassword.jsp" %>
			</div>
		</div>
	</div>
</div>