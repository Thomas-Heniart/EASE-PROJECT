<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<div class="MenuButtonSet megaBottomLeft">
	<button id="enterStatisticsMode" state="off" class="button adminButton">
		<img src="resources/icons/ascendant-bars-graphic.png" />
	</button>
</div>

<div class="RightSideViewTab" id="StatisticsTab">
	<button id="quit">
		<i class="fa fa-times"></i>
	</button>
	<div id="statisticsForm">
		<input type="date" name="startDate" id="startDate" />
		<input type="date" name="endDate" id="endDate" />
		<div id="usersStats">
			<input type="checkbox" name="dailyConnections" id="dailyConnections" />
			<label for="dailyConnections">Daily connections</label>
			<input type="checkbox" name="registeredUsers" id="registeredUsers" />
			<label for="registeredUsers">Registered users</label>
			<input type="checkbox" name="registeredUsersWithTuto" id="registeredUsersWithTuto" />
			<label for="registeredUsersWithTuto">Registered users with tuto done</label>
		</div>
		<div id="connectionsStats">
			<input type="checkbox" name="avgSiteConnections" id="avgSiteConnections"/>
			<label for="avgSiteConnections">Sites connections average per daily user</label>
			<input type="checkbox" name="medianSiteConnections" id="medianSiteConnections"/>
			<label for="medianSiteConnections">Sites connections median per daily user</label>
		</div>
		<div id="appsStats">
			<input type="checkbox" name="appsAdded" id="appsAdded"/>
			<label for="appsAdded">Apps added</label>
			<input type="checkbox" name="appsRemoved" id="appsRemoved"/>
			<label for="appsRemoved">Apps removed</label>
		</div>
		<button id='getStats'>Go !</button>
	</div>
	<div id="responses">
		<div>
			<canvas id="usersCanvas"></canvas>
		</div>
		<div>
			<canvas id="connectionsCanvas"></canvas>
		</div>
		<div>
			<canvas id="appsCanvas"></canvas>
		</div>
	</div>
</div>