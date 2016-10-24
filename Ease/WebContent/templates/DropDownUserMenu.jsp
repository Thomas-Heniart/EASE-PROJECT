<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.Ease.session.User"%>
<%
	String UserName = ((User) (session.getAttribute("User"))).getFirstName();
%>
<div class='userSettingsContainer'>
	<a id="userSettingsButton"><i class="fa fa-fw fa-user"></i> <%=UserName%></a>
	<div class="userSettings">
		<div class="directSettings">
			<p>
				<span>Homepage</span><span class="onoffswitch"> <input
					type="checkbox" name="onoffswitch" class="onoffswitch-checkbox"
					id="homePageSwitch" /> <label class="onoffswitch-label"
					for="homePageSwitch"></label>
				</span>
			</p>
			<p>
				<span>Daily photo</span><span class="onoffswitch"> <input
					type="checkbox" name="onoffswitch" class="onoffswitch-checkbox"
					id=backgroundSwitch checked /> <label class="onoffswitch-label"
					for="backgroundSwitch"></label>
				</span>
			</p>
		</div>
		<a id="ModifyUserButton"><i class="fa fa-fw fa-cogs"></i> <span>Settings</span></a>
	</div>
</div>

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
	}
	$("#backgroundSwitch").change(function() {
		postHandler.post(
				'changeUserBackground',
				{},
				function(){},
				function(retMsg){
					if($("body").hasClass("picBckgrnd")){
						$("body").switchClass("picBckgrnd", "logoBckgrnd");
					} else if($("body").hasClass("logoBckgrnd")){
						$("body").switchClass("logoBckgrnd", "picBckgrnd");
					}
				},
				function(retMsg){
					showAlertPopup(retMsg, true);
				},
				"text"
			);
	});
});

</script>
