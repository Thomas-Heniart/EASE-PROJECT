<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="MenuButtonSet bottomLeft">
	<button id="enterAddWebsiteMode" state="off" class="button">
		<img src="resources/icons/add_website_icon.png" />
	</button>
</div>

<div class="RightSideViewTab" id="AddSiteTab">
	<button id="quit">
		<i class="fa fa-times"></i>
	</button>

	<div class="addSiteView">
		<div class="addSiteHeader">
			<p>Add a website to Ease</p>
		</div>

		<div class="addSite">
			
			<form id="addUsersForm">
				<div class="inputs">
					<textarea id="integrateUsers" cols="120" rows="20" name="name"
						placeholder="Add users here !"></textarea>
					<p class="hidden">Sending to database ...</p>
				</div>
				<div class="buttonSet">
					<button id="integrate">
						<div style="display: inline-block">Add these users</div>
						<i class="fa fa-arrow-circle-right"></i>
					</button>
					<i class="fa fa-check-circle hidden"></i>
				</div>
			</div>
		</div>
	</div>
</div>