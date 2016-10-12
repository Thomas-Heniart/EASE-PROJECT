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
