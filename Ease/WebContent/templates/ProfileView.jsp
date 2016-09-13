<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="com.Ease.session.User"%>
<%@ page import="com.Ease.session.Profile"%>
<%@ page import="com.Ease.context.Site"%>
<%@ page import="com.Ease.context.SiteManager"%>
<%@ page import="com.Ease.context.Color"%>
<%@ page import="com.Ease.session.Account"%>
<%@ page import="com.Ease.session.LogWith"%>
<%@ page import="java.util.List"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
	com.Ease.session.User user = (com.Ease.session.User) (session.getAttribute("User"));
	List<com.Ease.session.Profile> profiles = user.getProfiles();
	List<Site> sites = ((SiteManager) (session.getServletContext().getAttribute("Sites"))).getSitesList();
	List<Color> colors = (List<Color>) (session.getServletContext().getAttribute("Colors"));
	pageContext.setAttribute("member", user);
	pageContext.setAttribute("profiles", profiles);
	pageContext.setAttribute("siteList", sites);
	pageContext.setAttribute("colors", colors);
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
			var profileIndex = $(obj).closest('.owl-item').index();
			var json = new Object();
			var event;

			$(obj).addClass('waitingLinkImage');
			setTimeout(function() {
				$(obj).removeClass("waitingLinkImage");
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
							var index = $(this).closest('.owl-item').index();
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
							var index = $(this).closest('.owl-item').index();
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
					idx = $(this).closest(".owl-item").index();
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
				$(".MenuButtonSet").remove();
				var div = $('<div id="downloadExtension" class="centeredItem"></div>');
				div.append($('<p class="title">Pour utiliser Ease …</p>'));
				div
						.append($('<p class="info">Il ne te reste plus qu’à télécharger notre add-on !</p>'));
				div
						.append($('<button id="install-button">Install it !</button>'));
				$('.ProfilesView .owl-carousel').css('display', 'none');
				$('.ProfilesView').append(div);
				$('.ProfilesView')
						.find('#install-button')
						.click(
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
		<img class="centeredItem" id="dragAndDropHelper"
			src="resources/other/Arrow.png" style="display: none;" />
		<div class="owl-carousel">
			<c:forEach items='${profiles}' var="item">
				<div class="item" idx=<%=it%>>
					<div class="ProfileBox"
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
							<img class="Scaler" src="https://placehold.it/3x6"
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
												<input id="profileName" name="profileName" type="text"
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
								<c:forEach items='${item.getApps()}' var="account">
									<c:if test="${account.getType() eq 'Account'}">
										<div class="siteLinkBox" onclick="sendEvent(this)"
											login="${account.getLogin() }"
											webId="${account.getSite().getId()}"
											name="${account.getName()}">
									</c:if>
									<c:if test="${account.getType() eq 'LogWith'}">
										<div class="siteLinkBox" onclick="sendEvent(this)"
											webId="${account.getSite().getId()}"
											name="${account.getName()}">
									</c:if>
									<div class="linkImage">
										<div class="deleteAppButton"
											onclick="showConfirmDeleteAppPopup(this, event)">
											<i class="fa fa-times"></i>
										</div>
										<div class="modifyAppButton"
											onclick="showModifyAppPopup(this, event)">
											<i class="fa fa-cog" aria-hidden="true"></i>
										</div>
										<img
											src="<c:out value='${account.getSite().getFolder()}logo.png'/>" />
									</div>
									<div class="siteName">
										<c:choose>
											<c:when test="${account.getName().length() > 14}">
												<p>${account.getName().substring(0,14)}...
												<p>
											</c:when>
											<c:otherwise>
												<p>${account.getName()}</p>
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
			revertOnSpill: true
		}).on('drop', function(){
		});
		var drakeProfiles = dragula({
			isContainer: function(el){
				return el.classList.contains('owl-wrapper');
			},
  			moves: function (el, container, handle) {
    			return handle.classList.contains('ProfileName');
  			}
		});
	});
</script>
<%@ include file="catalogView.jsp"%>

</div>
<div id="boxHelper" style="display: none">
	<div class="siteLinkBox">
		<div class="linkImage">
			<div class="deleteAppButton"
				onclick="showConfirmDeleteAppPopup(this, event)">
				<i class="fa fa-times"></i>
			</div>
			<div class="modifyAppButton"
				onclick="showModifyAppPopup(this, event)">
				<i class="fa fa-cog"></i>
			</div>
			<img src="">
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
				<img class="Scaler" src="https://placehold.it/3x6"
					style="width: 100%; height: auto; visibility: hidden;" /> <i
					class="fa fa-plus-circle centeredItem addHelper" aria-hidden="true"></i>
				<!-- 				    <p>Create a new profile with drag and drop here</p>  -->
			</div>
		</div>
	</div>
</div>
<div id="profileHelper" style="display: none">
	<div class="item">
		<div class="ProfileBox" style="border-bottom: 5px solid #35a7ff;"
			color="#35a7ff">
			<div class="ProfileName" style="background-color: #35a7ff;">
				<p>@Default</p>
				<div class="ProfileSettingsButton">
					<i class="fa fa-fw fa-ellipsis-v"></i>
				</div>
			</div>
			<div class="ProfileContent">
				<img class="Scaler" src="https://placehold.it/3x6"
					style="width: 100%; height: auto; visibility: hidden;">
				<div class="content">
					<div class="ProfileControlPanel" index="0">

						<div class="profileSettingsTab">
							<div class="sectionHeader" id="NameSection">
								<p class="title">Profile name</p>
								<div class="directInfo">
									<p>Default</p>
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
			name : 'Default',
			color : '#35a7ff'
		}, function(data) {
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
		$('.owl-carousel').owlCarousel({
			items : 3,
			itemsCustom : false,
			itemsDesktop : [ 1199, 3 ],
			itemsDesktopSmall : [ 980, 3 ],
			itemsTablet : [ 768, 3 ],
			itemsTabletSmall : false,
			itemsMobile : [ 479, 1 ],
			singleItem : false,
			itemsScaleUp : false,
			pagination : false,
		    touchDrag: false,
		    mouseDrag: false
		});
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
									name : 'Default',
									color : '#35a7ff'
								}, function(data) {
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
								$('.owl-carousel').owlCarousel({
									items : 3,
									itemsCustom : false,
									itemsDesktop : [ 1199, 3 ],
									itemsDesktopSmall : [ 980, 3 ],
									itemsTablet : [ 768, 3 ],
									itemsTabletSmall : false,
									itemsMobile : [ 479, 1 ],
									singleItem : false,
									itemsScaleUp : false,
									pagination : false,
								    touchDrag: false,
								    mouseDrag: false
								});
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
<script>
	$(document).ready(function() {
		$('#testButton').click(function() {
			var owl = $(".owl-carousel").data('owlCarousel');
			setTimeout(function() {
				owl.reinit({
					items : 3,
					itemsCustom : false,
					itemsDesktop : [ 1199, 3 ],
					itemsDesktopSmall : [ 980, 3 ],
					itemsTablet : [ 768, 3 ],
					itemsTabletSmall : false,
					itemsMobile : [ 479, 1 ],
					singleItem : false,
					itemsScaleUp : false,
					pagination : false,
				    touchDrag: false,
				    mouseDrag: false,
					baseClass : "opened"
				});
			}, 400)
		});
	});
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

	function makeSettingsAccordion(elem) {
		$(elem).click(
				function() {
					var parent;
					parent = $(elem).closest(".ProfileBox");
					if ($(elem).hasClass("settings-show")) {
						$(elem).removeClass("settings-show");

						parent.find(".ProfileControlPanel").css("display",
								"none");
						parent.find(".SitesContainer").css("display", "block");
						$(elem).find("i").removeClass("fa-caret-down");
						$(elem).find("i").addClass("fa-bars");

					} else {

						$(elem).addClass("settings-show");
						parent.find(".ProfileControlPanel").css("display",
								"inline-block");
						parent.find(".SitesContainer").css("display", "none");
						$(elem).find("i").removeClass("fa-bars");
						$(elem).find("i").addClass("fa-caret-down");
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









