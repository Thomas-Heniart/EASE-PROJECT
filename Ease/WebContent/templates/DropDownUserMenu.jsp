<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ page import="com.Ease.Dashboard.User.User"%>
<%
String UserName = ((User) (session.getAttribute("user"))).getFirstName();
%>
<div class='userSettingsContainer'>
	<a id="userSettingsButton"><i class="fa fa-fw fa-user"></i> <span><%=UserName%></span></a>
	<div class="userSettings">
		<div class="directSettings">
			<p class="settingsHeader">
				Quick setup
			</p>
			<div class="displayedByPlugin settingsRaw">
				<span>Homepage</span>
				<div class="infoCircle">
					<i class="fa fa-info-circle"></i>
					<div class="infoBubble">
						Set up Ease as your browser homepage to access your web easily.
						<div class="caretHelper"></div>
					</div>
				</div>
				<span class="onoffswitch"> <input
					type="checkbox" name="onoffswitch" class="onoffswitch-checkbox"
					id="homePageSwitch" /> <label class="onoffswitch-label"
					for="homePageSwitch"></label>
				</span>
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
			<div class="settingsRaw">
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
					id=personalComputerSetup /> <label class="onoffswitch-label"
					for="personalComputerSetup"></label>
				</span>
			</div>
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

<script type="text/javascript">
	$(document).ready(function(){
		extId = null;
		extensionDiv = null;
		setTimeout(function(){
			extensionDiv = $('#ease_extension');

			if (extensionDiv.length){
				extId = extensionDiv.attr('extensionId');
				postHandler.post(
					'IsPrivateExtension',
					{
						extensionId : extId
					},
					function(){

					},
					function (msg){
						$('#personalComputerSetup').prop('checked', msg);
						$("#personalComputerSetup").change(function() {
							var self = $(this);
							var isChecked = $('#personalComputerSetup').prop('checked');
							if (!isChecked) {
								postHandler.post(
									'AddUserExtension',
									{
										extensionId : extId
									},
									function (){

									},
									function (msg){
										self.prop('checked', true);
									},
									function (msg){

									},
									'text'
									);
							} else {
								postHandler.post(
									'RemoveUserExtension',
									{
										extensionId : extId
									},
									function (){

									},
									function (msg){
										self.prop('checked', false);
									},
									function (msg){

									},
									'text'
									);					
							}
						});
					},
					function (msg){
						$('#personalComputerSetup').closest('.settingsRaw').remove();
					},
					'text'
					);
			} else {
				$('#personalComputerSetup').closest('.settingsRaw').remove();
			}
		}, 1000);
	});
</script>
<script>
	$(document).ready(function() {
		$("#userSettingsButton").click(function() {
			$(".userSettings").toggleClass("show");
		});

		$(document).click(function(e) {
			if (!$(e.target).closest(".userSettings, #userSettingsButton").length)
				$(".userSettings").removeClass("show");
		});
		$('#ModifyUserButton').click(function() {
			$('.SettingsView').addClass('show');
			$('.col-left').removeClass('show');
			$('.MenuButtonSet').removeClass('show');
		});
		if($("body").hasClass("picBckgrnd")){
			$('#backgroundSwitch').prop("checked", true);
		} else {
			$('#backgroundSwitch').prop("checked", false);
		}
		$("#backgroundSwitch").change(function() {
			if($("body").hasClass("picBckgrnd")){
				$("body").switchClass("picBckgrnd", "logoBckgrnd");
			} else if($("body").hasClass("logoBckgrnd")){
				$("body").switchClass("logoBckgrnd", "picBckgrnd");
			}
			var self = $(this);
			postHandler.post(
				'changeUserBackground',
				{},
				function(){},
				function(retMsg){
					easeTracker.trackEvent("DailyPhotoSwitch");
					easeTracker.setDailyPhoto(self.is("checked"));
				},
				function(retMsg){
					showAlertPopup(retMsg, true);
				},
				"text"
				);
		});
	});

</script>
