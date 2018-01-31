<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ page import="com.Ease.Dashboard.User.User"%>
<%
String UserName = ((User) (session.getAttribute("user"))).getFirstName();
%>
<div class='userSettingsContainer'>
	<a id="userSettingsButton"><i class="fa fa-fw fa-user"></i> <span><%=UserName%></span></a>
	<div class="userSettings <c:if test='${param.showSettings}'>show</c:if>">
		<div class="directSettings">
			<p class="settingsHeader">
				Quick setup
			</p>
			<div class="displayedByPlugin settingsRaw">
				<span>Homepage</span>
				<span class="onoffswitch"> <input
						type="checkbox" name="onoffswitch" class="onoffswitch-checkbox"
						id="homePageSwitch"/> <label class="onoffswitch-label"
													 for="homePageSwitch"></label>
				</span>
				<div class="infoCircle">
					<i class="fa fa-info-circle"></i>
					<div class="infoBubble">
						Set up Ease as your browser homepage to access your web easily.
						<div class="caretHelper"></div>
					</div>
				</div>
			</div>
			<div class="settingsRaw">
				<span>Ease Photo</span>
				<div class="infoCircle">
					<i class="fa fa-info-circle"></i>
					<div class="infoBubble">
						Would you like a background photo on your space every day?
						<div class="caretHelper"></div>
					</div>
				</div>
				<span class="onoffswitch"> <input
					type="checkbox" name="onoffswitch" class="onoffswitch-checkbox"
					id=backgroundSwitch checked /> <label class="onoffswitch-label"
					for="backgroundSwitch"></label>
				</span>
			</div>
			<!-- <div class="settingsRaw">
				<span>My Computer</span>
				<div class="infoCircle">
					<i class="fa fa-info-circle"></i>
					<div class="infoBubble">
						Get more web updates when you are on your personal computer!
						<div class="caretHelper"></div>
					</div>
				</div>
				<span class="onoffswitch"> <input
					type="checkbox" name="onoffswitch" class="onoffswitch-checkbox"
					id="personalComputerSetup" /> <label class="onoffswitch-label"
					for="personalComputerSetup"></label>
				</span>
			</div> -->
		</div>
		<a id="ModifyUserButton" class="dropDownButton">
			<span class="fa-stack fa-lg icon">
				<i class="fa fa-square fa-stack-2x"></i>
				<i class="fa fa-sliders fa-stack-1x fa-rotate-90"></i>
			</span>
			<span>My settings</span>
		</a>
	</div>
</div>

