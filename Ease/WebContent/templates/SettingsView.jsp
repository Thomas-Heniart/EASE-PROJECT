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
					<div id="modifyNameForm">
						<input id="firstName" name="firstName" type="text"
							placeholder="First name" /> <input id="lastName" name="lastName"
							type="text" placeholder="Last name" />
						<div class="buttonSet">
							<button class="button" id="validate">Validate</button>
							<button class="button" id="cancel">Cancel</button>
						</div>
					</div>
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
				<div id="modifyPasswordForm">
					<p>For safety reasons, your password needs to be at least 8
						characters long,  including upper-case and lower-case letters,
						plus at least one numerical digit.</p>
					<input id="currentPassword" name="currentPassword" type="password"
						placeholder="Current password" /> <input id="newPassword"
						name="newPassword" type="password" placeholder="New password" /> <input
						id="confirmNewPassword" name="confirmNewPassword" type="password"
						placeholder="Confirm new password" />
					<div class="alertDiv">
						<p>Incorrect password or email !</p>
					</div>
					<div class="buttonSet">
						<button class="button" id="validate">Validate</button>
						<button class="button" id="cancel">Cancel</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>