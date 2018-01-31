<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="RightSideViewTab" id="AddUsersTab">
	<button id="quit">
		<i class="fa fa-times"></i>
	</button>

	<div class="addUsersView">
		<div class="addUsersHeader">
			<p>Add Users to EasE</p>
		</div>

		<div class="addUsers">
			<div class="DivHeader">
				<p>Copy paste a list of emails separated by ';' to add them to
					Ease</p>
			</div>
			<div id="addUsersForm">
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

		<div class="addUsersProgress hidden">
			<div class="DivHeader">
				<p>Sending new users to database ...</p>
			</div>
			<div id="progressStatus">
				<div class="progress"></div>
				<div class="buttonSet">
					<button id="return">
						<div style="display: inline-block">OK</div>
						<i class="fa fa-arrow-circle-right"></i>
					</button>
					<i class="fa fa-check-circle hidden"></i>
				</div>
			</div>
		</div>
	</div>
</div>