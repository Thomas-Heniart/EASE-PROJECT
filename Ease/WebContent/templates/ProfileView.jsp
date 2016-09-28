<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="com.Ease.session.User"%>
<%@ page import="com.Ease.session.Profile"%>
<%@ page import="com.Ease.context.Site"%>
<%@ page import="com.Ease.context.SiteManager"%>
<%@ page import="com.Ease.context.Color"%>
<%@ page import="com.Ease.context.Tag"%>
<%@ page import="com.Ease.context.TagAndSiteMap"%>
<%@ page import="com.Ease.session.App"%>
<%@ page import="com.Ease.session.ClassicAccount"%>
<%@ page import="com.Ease.session.LogWithAccount"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.LinkedList"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
com.Ease.session.User user = (com.Ease.session.User) (session.getAttribute("User"));
List<com.Ease.session.Profile> profiles = user.getProfiles();
List<Site> sites = ((SiteManager) (session.getServletContext().getAttribute("siteManager"))).getSitesList();
List<Color> colors = (List<Color>) (session.getServletContext().getAttribute("Colors"));
List<Tag> tags = (List<Tag>) (session.getServletContext().getAttribute("Tags"));
List<TagAndSiteMap> tagAndSiteMapping = (List<TagAndSiteMap>) (session.getServletContext().getAttribute("TagAndSiteMapping"));
SiteManager siteManager = (SiteManager) (session.getServletContext().getAttribute("siteManager"));
pageContext.setAttribute("session", session);
pageContext.setAttribute("member", user);
pageContext.setAttribute("profiles", profiles);
pageContext.setAttribute("siteList", sites);
pageContext.setAttribute("colors", colors);
session.getServletContext().setAttribute("tags", tags);
pageContext.setAttribute("siteManager", siteManager);
pageContext.setAttribute("selectedTags", new LinkedList<Tag>());
pageContext.setAttribute("tagAndSiteMapping", tagAndSiteMapping);
int it = 0;

Cookie fname = new Cookie("fname",
	Base64.getEncoder().encodeToString(user.getFirstName().getBytes(StandardCharsets.UTF_8)));
Cookie lname = new Cookie("lname",
	Base64.getEncoder().encodeToString(user.getLastName().getBytes(StandardCharsets.UTF_8)));
Cookie email = new Cookie("email", user.getEmail());

fname.setMaxAge(60 * 60 * 24 * 31);
lname.setMaxAge(60 * 60 * 24 * 31);
email.setMaxAge(60 * 60 * 24 * 31);
response.addCookie(fname);
response.addCookie(lname);
response.addCookie(email);
%>


<script>
	function sendEvent(obj) {
		if (!($(obj).hasClass('waitingLinkImage'))) {
			var appIdx = $(obj).closest('.siteLinkBox').index();
			var logoImage = $(obj).find('.linkImage');
			var profileIndex = $(obj).closest('.owl-item').index();
			var json = new Object();
			var event;

			$(obj).addClass('waitingLinkImage');
			$(obj).addClass('scaleinAnimation');
			setTimeout(function() {
				$(obj).removeClass("waitingLinkImage");
				$(obj).removeClass('scaleinAnimation');				
			}, 1000);
			$.post("askInfo", {
				profileIndex : profileIndex,
				appIndex : appIdx,

			}, function(data) {
				if (data[0] == 's') {
					json.detail = JSON.parse(data.substring(8, data.length));
					event = new CustomEvent("NewConnection", json);
					document.dispatchEvent(event);
				} else {
					if (data[0] != 'e') {
						document.location.reload(true);
					} else {
						showAlertPopup(null, true);
					}
				}
			}, 'text');
		}
	}

	function setupProfileSettings(profile) {
		$(profile).find('.ProfileControlPanel #cancel').click(function() {
			var Accordion = $(this).closest('.ui-accordion');

			$(Accordion).find('input').val('');
			$(Accordion).find('.color.selected').removeClass('selected');
			$(Accordion).accordion("option", "active", 10);

		});
		$(profile)
		.find(
			'.ProfileControlPanel #modifyNameForm .buttonSet #validate')
		.click(
			function() {
				var index = $(this).closest('.item').attr('id');
				var name = $(this).closest('#modifyNameForm').find(
					'#profileName').val();
				var button = $(this);
				var closeSectionButton = $(this).closest(
					'.buttonSet').find('#cancel');

				$('#loading').addClass('la-animate');
				$
				.post(
					'editProfileName',
					{
						name : name,
						index : index
					},
					function(data) {
						$('#loading').removeClass(
							'la-animate');
						$(button).closest(
							'#modifyNameForm')
						.find('#profileName')
						.val('');

						if (data[0] == 's') {
							$(closeSectionButton)
							.click();
							$(button)
							.closest(
								'.ProfileBox')
							.find(
								'.ProfileName p')
							.text('@' + name);
							$(button)
							.closest(
								'.profileSettingsTab')
							.find(
								'.sectionHeader .directInfo p')
							.text(name);
							showAlertPopup(
								'Modifications successfully applied !',
								false);
						} else {
							if (data[0] != 'e') {
								document.location
								.reload(true);
							} else {
								showAlertPopup(null,
									true);
							}
						}
					}, 'text');
			});

		$(profile).find(".colorChooser .color").click(function() {
			var parent = $(this).closest('.colorChooser');
			$(parent).find('.selected').removeClass('selected');
			$(parent).find('#color').val($(this).attr('color'));
			$(this).addClass('selected');
		});

		$(profile)
		.find(
			'.ProfileControlPanel #modifyColorForm .buttonSet #validate')
		.click(
			function() {
				var index = $(this).closest('.item').attr('id');
				var color = $(this).closest('#modifyColorForm')
				.find('#color').val();
				var button = $(this);
				var closeSectionButton = $(this).closest(
					'.buttonSet').find('#cancel');

				$('#loading').addClass('la-animate');
				$
				.post(
					'editProfileColor',
					{
						color : color,
						index : index
					},
					function(data) {
						$('#loading').removeClass(
							'la-animate');
						if (data[0] == 's') {
							$(closeSectionButton)
							.click();
							$(button)
							.closest(
								'.ProfileBox')
							.find(
								'.ProfileName')
							.css(
								'background-color',
								color);
							$(button).closest(
								'.ProfileBox')
							.attr('color',
								color);
							var string = '5px solid '
							+ color;
							$(button).closest(
								'.ProfileBox').css(
								'border-bottom',
								string);
								$(button)
								.closest(
									'.profileSettingsTab')
								.find(
									'#ColorSection .directInfo')
								.css(
									'background-color',
									color);
								showAlertPopup(
									'Modifications successfully applied !',
									false);
							} else {
								if (data[0] != 'e') {
									document.location
									.reload(true);
								} else {
									showAlertPopup(null,
										true);
								}
							}
						}, 'text');
			});
		$(profile).find('#deleteProfileForm .buttonSet #validate').click(
			function() {
				var idx;
				var name;
				var popup;

				parent = $(this).closest(".ProfileBox");
				idx = $(this).closest(".item").attr('id');
				popup = $('#PopupDeleteProfile');

				popup.find("#index").val(idx);
				popup.find('#password').val('');
				popup.find('span').text(
					$(parent).find('.ProfileName p').text());
				$('#PopupDeleteProfile').addClass("md-show");
				setTimeout(function() {
					$(popup).find('#password').focus();
				}, 100);
			});
	}
</script>
<div class="ProfilesView show">
	<%@ include file="MenuButtonSet.jsp"%>

	<c:if test="${member.getTuto() == '0'}">
		<%@ include file="Tutorial.jsp"%>
	</c:if>

<script type="text/javascript">
	function checkForExtension() {
		var ext = $('#ease_extension');

		if (!($('#ease_extension').length)) {
			$('#downloadExtension').css('display', 'block');
			$('#downloadExtension').find('#install-button').click(
				function() {
					if (getUserNavigator() == "Chrome") {
						chrome.webstore
						.install(
							'https://chrome.google.com/webstore/detail/echjdhmhmgildgidlcdlepfkaledeokm',
							function() {
								window.location
								.replace("index.jsp");
							},
							function() {
								window.location
								.replace("index.jsp");
							})
					}
				});
		}
	}
	$(document).ready(function() {
		if (!($('#tutorialView').length)) {
			setTimeout(function() {
				checkForExtension();
			}, 1000);
		}
	});
	function closeAllSettingsTabs() {
		$('.ProfileSettingsButton.settings-show').click();
	}
</script>
<div class="ProfilesHandler">
	<div class="owl-carousel">
		<c:forEach items='${profiles}' var="item">
		<div class="item" id='${item.getProfileId()}' idx=<%=it%>>
			<div class="ProfileBox" custom="${item.isCustom()}"
			style="border-bottom: 5px solid ${item.getColor()};"
			color="${item.getColor()}">
			<div class="ProfileName"
			style="background-color: ${item.getColor()};">
			<p>
				@
				<c:out value='${item.getName()}' />
			</p>
			<div class="ProfileSettingsButton">
				<i class="fa fa-fw fa-ellipsis-v"></i>
			</div>
		</div>
		<div class="ProfileContent">
			<img class="Scaler" src="resources/other/placeholder-36.png"
			style="width: 100%; height: auto; visibility: hidden;" />
			<div class=content>
				<div class="ProfileControlPanel" index=<%=it%>>
					<%
					it = it + 1;
					%>
					<div class="profileSettingsTab">
						<div class="sectionHeader" id="NameSection">
							<p class="title">Profile name</p>
							<div class="directInfo">
								<p>${item.getName() }</p>
							</div>
						</div>
						<div class="sectionContent" id="contentName">
							<div id="modifyNameForm">
								<input id="profileName" name="profileName" type="text"  maxlength="20"
								placeholder="Profile name..." />
								<div class="buttonSet">
									<button class="button" id="validate">Validate</button>
									<button class="button" id="cancel">Cancel</button>
								</div>
							</div>
						</div>
						<div class="sectionHeader" id="ColorSection">
							<p class="title">Color</p>
							<div class="directInfo"
							style="background-color: ${item.getColor()}"></div>
						</div>
						<div class="sectionContent" id="contentColor">
							<p>Choose your color</p>
							<div id="modifyColorForm">
								<div class="colorChooser">
									<input name="color" type="hidden" id="color" />
									<%
									int itr = 0;
									%>
									<c:forEach items='${colors}' var="color">
									<%
									if ((itr % 5) == 0) {
									%>
									<div class="lineColor">
										<%
									}
									%>
									<div class="color" color="${color.getColor()}"
									style="background-color: ${color.getColor()}"></div>
									<%
									itr++;
									%>
									<%
									if ((itr % 5) == 0) {
									%>
								</div>
								<%
							}
							%>
						</c:forEach>
						<%
						if (itr % 5 != 0) {
						%>
					</div>
					<%
				}
				%>
			</div>
			<div class="buttonSet">
				<button class="button" id="validate">Validate</button>
				<button class="button" id="cancel">Cancel</button>
			</div>
		</div>
	</div>
	<div class="sectionHeader" id="DeleteProfilSection">
		<p class="title">Delete profile</p>
	</div>
	<div class="sectionContent" id="contentDeleteProfil">
		<div id="deleteProfileForm">
			<p>By deleting your profile you will lose all related
				information and associated accounts</p>
				<div class="buttonSet">
					<button class="button" id="validate">Validate</button>
					<button class="button" id="cancel">Cancel</button>
				</div>
			</div>
		</div>
	</div>
</div>
<div id="profile-<%=it - 1%>" class="SitesContainer">
	<c:forEach items='${item.getApps()}' var="app">

	<c:choose>
		<c:when test="${app.getType() eq 'NoAccount'}">
			<div class="siteLinkBox emptyApp"
				 login=""
				webId="${app.getSite().getId()}"
				name="${app.getName()}"
				id="${app.getAppId()}">
				<div class="linkImage" onclick="sendEvent(this)">
					<div class="emptyAppIndicator" onclick="showModifyAppPopup(this, event)">
						<img src="resources/other/raise-your-hand-to-ask.svg" />
					</div>
					<div class="showAppActionsButton" >
						<i class="fa fa-cog"></i>
						<div class="appActionsPopup">
							<div class="buttonsContainer">
								<div class="modifyAppButton menu-item"
									onclick="showModifyAppPopup(this, event)">
									<p>Modify</p>
								</div>
								<c:if test="${app.havePerm('DELETE', session.getServletContext())}">
								<div class="deleteAppButton menu-item"
									onclick="showConfirmDeleteAppPopup(this, event)">
									<p>Delete</p>
								</div>
								</c:if>
							</div>
						</div>
					</div>
					<img class="logo" src="<c:out value='${app.getSite().getFolder()}logo.png'/>" />
				</div>
		</c:when>
		<c:otherwise>
			<c:if test="${app.getType() eq 'ClassicAccount'}">
				<div class="siteLinkBox"
					 login="${app.getLogin()}"
					webId="${app.getSite().getId()}"
					name="${app.getName()}"
					id="${app.getAppId()}"
					ssoId="${app.getSite().getSso()}"
					logwith="false">
			</c:if>
			<c:if test="${app.getType() eq 'LogWithAccount'}">
				<div class="siteLinkBox"
				webId="${app.getSite().getId()}"
				name="${app.getName()}"
				id="${app.getAppId()}"
				logwith="${app.getAccount().getLogWithApp(member).getAppId()}">
			</c:if>
			<div class="linkImage" onclick="sendEvent(this)">
				<div class="showAppActionsButton">
					<i class="fa fa-cog"></i>
					<div class="appActionsPopup">
						<div class="buttonsContainer">
							<div class="modifyAppButton menu-item"
								onclick="showModifyAppPopup(this, event)">
							<p>Modify</p>
							</div>
							<c:if test="${app.havePerm('DELETE', session.getServletContext())}">
								<div class="deleteAppButton menu-item"
									onclick="showConfirmDeleteAppPopup(this, event)">
									<p>Delete</p>
								</div>
							</c:if>
					</div>
				</div>
			</div>
			<img class="logo" src="<c:out value='${app.getSite().getFolder()}logo.png'/>" />
		</div>
	</c:otherwise>
</c:choose>
<div class="siteName">
	<c:choose>
	<c:when test="${app.getName().length() > 14}">
	<p>${app.getName().substring(0,14)}...
		</p>
		</c:when>
		<c:otherwise>
		<p>${app.getName()}</p>
	</c:otherwise>
</c:choose>
</div>
</div>
</c:forEach>
</div>
</div>
</div>
</div>
</div>
</c:forEach>
</div>
</div>
<script type="text/javascript">
	$(document).ready(function(){
		var drake = dragula({
			isContainer: function(el){
				return  el.classList.contains('SitesContainer');
			},
			revertOnSpill: true,
			moves: function(el, container){
				return !($(el).hasClass('noMove'));
			}
		}).on('drop', function(el, target, source, sibling){
			$.post(
				"moveApp",
				{
					appId: $(el).attr('id'),
					profileId: $(el).closest('.item').attr('id') ,
					index: $(el).index()
				},
				function (data){

				},
				'text'
				);
		}).on('drag', function(el, source){
			$('.owl-carousel .siteLinkBox').each(function(){
				var rand = Math.random();

				rand = '-' + rand + 's';
				$(this).css('animation-delay', rand);
				$(this).addClass('shake-ease');
				$(this).addClass('shake-constant');
				$(el).removeClass('shake-ease');
				$(el).removeClass('shake-constant');
			});
		}).on('dragend', function(el){
			$('.owl-carousel .siteLinkBox').removeClass('shake-ease');			
			$('.owl-carousel .siteLinkBox').removeClass('shake-constant');
		});	
		var drakeProfiles = dragula({
			isContainer: function(el){
				return el.classList.contains('owl-wrapper');
			},
			moves: function (el, container, handle) {
				return handle.classList.contains('ProfileName');
			},
			direction: 'horizontal'
		}).on('drop', function(el, target, source, sibling){
			$.post(
				"moveProfile",
				{
					profileId: $(el).find('.item').attr('id'),
					index: $(el).index()
				},
				function (data){

				},
				'text'
				);
		}).on('drag', function(el, source){
			document.body.style.cursor = "move";
		}).on('dragend', function(el){
			document.body.style.cursor = "default";
		});
	});
</script>
<%@ include file="catalogView.jsp"%>

</div>
<div id="boxHelper" style="display: none">
	<div class="siteLinkBox">
		<div class="linkImage">
			<div class="showAppActionsButton">
				<i class="fa fa-cog"></i>
				<div class="appActionsPopup">
					<!--												<div class="caretHelper"><i class="fa fa-caret-up" aria-hidden="true"></i></div>-->
					<div class="buttonsContainer">
						<div class="modifyAppButton menu-item"
						onclick="showModifyAppPopup(this, event)">
						<p>Modify</p>
					</div>
					<div class="deleteAppButton menu-item"
					onclick="showConfirmDeleteAppPopup(this, event)">
					<p>Delete</p>
				</div>
			</div>
		</div>
	</div>
	<img class="logo" src="">
</div>
<div class="siteName">
	<p></p>
</div>
</div>
</div>

<div id="addProfileHelper" style="display: none;">
	<div class="item">
		<div class="AddProfileView">
			<div class="MobilePreviewHeader">
				<p></p>
			</div>
			<div class="scalerContainer">
				<img class="Scaler" src="resources/other/placeholder-36.png"
				style="width: 100%; height: auto; visibility: hidden;" /> <i
				class="fa fa-plus-circle centeredItem addHelper" aria-hidden="true"></i>
				<p>Drop an app (or click) to create new profile</p>
			</div>
		</div>
	</div>
</div>
<div id="profileHelper" style="display: none">
	<div class="item">
		<div class="ProfileBox" style="border-bottom: 5px solid #35a7ff;"
		color="#35a7ff">
		<div class="ProfileName" style="background-color: #35a7ff;">
			<p>@Profile name</p>
			<div class="ProfileSettingsButton">
				<i class="fa fa-fw fa-ellipsis-v"></i>
			</div>
		</div>
		<div class="ProfileContent">
			<img class="Scaler" src="resources/other/placeholder-36.png"
			style="width: 100%; height: auto; visibility: hidden;">
			<div class="content">
				<div class="ProfileControlPanel" index="0">

					<div class="profileSettingsTab">
						<div class="sectionHeader" id="NameSection">
							<p class="title">Profile name</p>
							<div class="directInfo">
								<p>Profile name</p>
							</div>
						</div>
						<div class="sectionContent" id="contentName">
							<div id="modifyNameForm">
								<input id="profileName" name="profileName" type="text"
								placeholder="Profile name...">
								<div class="buttonSet">
									<button class="button" id="validate">Validate</button>
									<button class="button" id="cancel">Cancel</button>
								</div>
							</div>
						</div>
						<div class="sectionHeader" id="ColorSection">
							<p class="title">Color</p>
							<div class="directInfo" style="background-color: #35a7ff"></div>
						</div>
						<div class="sectionContent" id="contentColor">
							<p>Choose your color</p>
							<div id="modifyColorForm">
								<div class="colorChooser">
									<input name="color" type="hidden" id="color">
									<div class="lineColor">
										<div class="color" color="#ffe74c"
										style="background-color: #ffe74c"></div>
										<div class="color" color="#35a7ff"
										style="background-color: #35a7ff"></div>
										<div class="color" color="#6bf178"
										style="background-color: #6bf178"></div>
										<div class="color" color="#ec555b"
										style="background-color: #ec555b"></div>
										<div class="color" color="#805b9b"
										style="background-color: #805b9b"></div>
									</div>
									<div class="lineColor">
										<div class="color" color="#ff974f"
										style="background-color: #ff974f"></div>
										<div class="color" color="#373b60"
										style="background-color: #373b60"></div>
										<div class="color" color="#ff618a"
										style="background-color: #ff618a"></div>
									</div>
								</div>
								<div class="buttonSet">
									<button class="button" id="validate">Validate</button>
									<button class="button" id="cancel">Cancel</button>
								</div>
							</div>
						</div>
						<div class="sectionHeader" id="DeleteProfilSection">
							<p class="title">Delete profile</p>
						</div>
						<div class="sectionContent" id="contentDeleteProfil">
							<div id="deleteProfileForm">
								<p>By deleting your profile you will lose all related
									information and associated accounts</p>
									<div class="buttonSet">
										<button class="button" id="validate">Validate</button>
										<button class="button" id="cancel">Cancel</button>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="SitesContainer"></div>
				</div>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	$(document).ready(function() {
		$('.SitesContainer').each(function() {
			makeViewDroppable($(this));
		});
	});

	function addProfileView(elem) {
		var profile = $($('#profileHelper').html());
		var container = $(profile).find('.SitesContainer');

		var parent = $(elem).closest('.item');
		var owl = $(".owl-carousel").data('owlCarousel');

		$.post('addProfile', {
			name : 'Profile name',
			color : '#35a7ff'
		}, function(data) {
			$(profile).attr('id', data.substring(9, data.length));
		}, 'text');
		makeViewDroppable($(container));
		setupProfileSettings($(profile));
		makeSettingsAccordion($($(profile).find(".ProfileSettingsButton")));
		$(profile).find('.profileSettingsTab').accordion({
			active : 10,
			collapsible : true,
			autoHeight : false,
			heightStyle : "content"
		});
		owl.destroy();
		$('.owl-carousel').append($(profile));
		$('.owl-carousel').append($(parent));
		var nbProfiles = $('.owl-carousel > *').length;
		if (nbProfiles > 3) {
			var addProfileHelper = $(elem).closest('.item');
			$('#addProfileHelper').append($(addProfileHelper));
		}
		setupOwlCarousel();
	}
	$(document).ready(function() {
		$('.AddProfileView .scalerContainer').click(function() {
			addProfileView($(this));
		});
	});
	$(document).ready(
		function() {
			$('.AddProfileView .scalerContainer').droppable(
			{
				accept : ".catalogApp",

				drop : function(event, ui) {
					event.preventDefault();
					event.stopPropagation();
					$(this).css('border', 'none');
					var nbProfiles = $('.owl-wrapper > *').length;
					var profile = $($('#profileHelper').html());
					var container = $(profile).find(
						'.SitesContainer');
					var parent = $(this).closest('.item');
					var owl = $(".owl-carousel")
					.data('owlCarousel');

					$.post('addProfile', {
						name : 'Profile name',
						color : '#35a7ff'
					}, function(data) {
						if (data[0] == 's'){
							$(profile).attr('id', data.substring(9, data.length));
						}
					}, 'text');
					makeViewDroppable($(container));
					setupProfileSettings($(profile));
					makeSettingsAccordion($($(profile).find(
						".ProfileSettingsButton")));
					$(profile).find('.profileSettingsTab')
					.accordion({
						active : 10,
						collapsible : true,
						autoHeight : false,
						heightStyle : "content"
					});
					owl.destroy();
					$('.owl-carousel').append($(profile));
					$('.owl-carousel').append($(parent));
					var nbProfiles = $('.owl-carousel > *').length;
					if (nbProfiles > 3) {
						var addProfileHelper = $(this).closest(
							'.item');
						$('#addProfileHelper').append(
							$(addProfileHelper));
					}
					setupOwlCarousel();
					showAddAppPopup($(container), $(ui.helper));

				},
				over : function(event, ui) {
					event.preventDefault();
					event.stopPropagation();
					$(this).css('border', '1px solid #35a7ff');
				},

				out : function(event, ui) {
					event.preventDefault();
					event.stopPropagation();
					$(this).css('border', 'none');
				}
			});
		});

	function makeViewDroppable(v) {
		var parent = $(v).closest('.ProfileBox');

		$(v).droppable({
			accept : ".catalogApp",

			drop : function(event, ui) {
				event.preventDefault();
				event.stopPropagation();
				$(this).css('border', 'none');

				showAddAppPopup($(this), $(ui.helper));

			},
			over : function(event, ui) {
				event.preventDefault();
				event.stopPropagation();
				$(this).css('border', '1px solid ' + $(parent).attr('color'));
			},

			out : function(event, ui) {
				event.preventDefault();
				event.stopPropagation();
				$(this).css('border', 'none');
			}
		});
	}
</script>
<script type="text/javascript">
	$(document).ready(function() {
		$(".profileSettingsTab").accordion({
			active : 10,
			collapsible : true,
			autoHeight : false,
			heightStyle : "content"
		});
	});

	$(document).click(function (e){
		var profile = $(e.target).closest('.ProfileControlPanel');
		var settingsButton = null;

		if (profile.length){
			settingsButton = profile.closest('.ProfileBox').find('.ProfileSettingsButton.settings-show');
		}

		$('.ProfileSettingsButton.settings-show').each(function(){
			if (!($(this).is($(settingsButton)))){
				$(this).click();
			}
		});
	});	
	function makeSettingsAccordion(elem) {
		$(elem).click(
			function(e) {
				var parent;
				var button = $(this);
				e.stopPropagation();
				$('.ProfileSettingsButton.settings-show').each(function(){
					if (!($(this).is($(button)))){
						$(this).click();
					}
				});				
				parent = $(elem).closest(".ProfileBox");
				if ($(elem).hasClass("settings-show")) {
					$(elem).removeClass("settings-show");

					elem.removeClass('fa-rotate-90');						
					parent.find(".ProfileControlPanel").css("display","none");

				} else {

					elem.addClass('fa-rotate-90');
					$(elem).addClass("settings-show");
					parent.find(".ProfileControlPanel").css("display","inline-block");
				}
			});
	}

	$(document).ready(function() {
		$('.ProfileSettingsButton').each(function(index, el) {
			makeSettingsAccordion($(this));
		});
		$('.ProfilesHandler .item').each(function(index, el) {
			setupProfileSettings($(this));
		});

	});

	$(document).ready(function() {
		$('.ModifyProfileButton').each(function(index, el) {
			$(this).click(function() {
				var parent;
				var index;
				var name;
				var popup;

				parent = $(this).closest(".ProfileBox");
				index = parent.find(".ProfileControlPanel").attr("index");
				name = parent.find(".ProfileName").children("p").text();
				popup = $('#PopupModifyProfile');

				popup.find("#index").val(index);
				popup.find("#profilName").parent().addClass("input--filled");
				popup.find("#profilName").val(name.substring(1));
				$('#PopupModifyProfile').addClass("md-show");
			});
		});
	});
</script>

<script type="text/javascript">
	function setupAppSettingButtonPopup(elem){
		$(elem).on('mouseover', function() {
			var subPopup = $(this).find('.appActionsPopup');
			var profileParent = (this).closest('.content');
			var str = '-';
			var scrollDist = $('.col-left').scrollTop() + $(profileParent).scrollTop() + $(this).height();
			str += scrollDist + 'px';
			subPopup.css({
				'margin-top':str
			});
		});	
	}
	$(document).ready(function() {
		$('.SitesContainer .showAppActionsButton').each(function(){
			setupAppSettingButtonPopup($(this));
		});
	});
</script>







