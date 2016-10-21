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
				<p>Name</p>
				<p class="directInfo"><%=user.getFirstName()%>
					<%=user.getLastName()%></p>
				<div class="iconEdit">
					<i class="fa fa-cog"></i>
					<p>Edit</p>
				</div>
			</div>
			<div class="sectionContent" id="contentName">
				<div>
					<%@ include file="EditUserName.jsp"%>
				</div>
			</div>
			<div class="sectionHeader" id="passwordSection">
				<p>Password</p>
				<p class="directInfo"></p>
				<div class="iconEdit">
					<i class="fa fa-cog"></i>
					<p>Edit</p>
				</div>
			</div>
			<div class="sectionContent" id="contentPassword">
				<%@ include file="EditUserPassword.jsp"%>
			</div>
			<div class="sectionHeader" id="styleSection">
				<p>Style</p>
				<p class="directInfo"/>
				<div class="iconEdit">
					<i class="fa fa-cog"></i>
					<p>Edit</p>
				</div>
			</div>
			<div class="sectionContent" id="contentStyle">
				<div>
					<p style="display:inline-block;">Beautiful background</p>
					<label style="display:inline-block; float:right; margin-right:30px;">
  						<input class="checkBox" type="checkbox"/>
  						<div class="slider"></div>
					</label>
				</div>
			</div>
			<div class="sectionHeader" id="styleSection">
				<p>Emails</p>
				<p class="directInfo"/>
				<div class="iconEdit">
					<i class="fa fa-cog"></i>
					<p>Edit</p>
				</div>
			</div>
			<div class="sectionContent" id="contentEmail">
				<div>
					<c:forEach items='${user.getEmails().keySet()}' var="item" varStatus="loop">
						<div class="userEmail">
							<p>${item }</p>
						</div>
					</c:forEach>
				</div>
			</div>
		</div>
	</div>
</div>