<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="RightSideViewTab" id="StatisticsTab">
	<button id="quit">
		<i class="fa fa-times"></i>
	</button>
	<div class="loading-stats">
		<div class="sk-fading-circle">
			<div class="sk-circle1 sk-circle"></div>
			<div class="sk-circle2 sk-circle"></div>
			<div class="sk-circle3 sk-circle"></div>
			<div class="sk-circle4 sk-circle"></div>
			<div class="sk-circle5 sk-circle"></div>
			<div class="sk-circle6 sk-circle"></div>
			<div class="sk-circle7 sk-circle"></div>
			<div class="sk-circle8 sk-circle"></div>
			<div class="sk-circle9 sk-circle"></div>
			<div class="sk-circle10 sk-circle"></div>
			<div class="sk-circle11 sk-circle"></div>
			<div class="sk-circle12 sk-circle"></div>
		</div>
		<h3>Loading statistics data...</h3>
	</div>
	<div id="statisticsForm">
		<div class="stats-input-group">
			<input type="date" name="startDate" id="startDate" /> <input
				type="date" name="endDate" id="endDate" />
		</div>
		<div id="usersStats" class="stats-input-group">
			<div class="stats-input">
				<input type="checkbox" name="dailyConnections" id="dailyConnections" />
				<label for="dailyConnections">Daily connections</label>
			</div>
			<div class="stats-input">
				<input type="checkbox" name="registeredUsers" id="registeredUsers" />
				<label for="registeredUsers">Registered users</label>
			</div>
			<div class="stats-input">
				<input type="checkbox" name="registeredUsersWithTuto"
					id="registeredUsersWithTuto" /> <label
					for="registeredUsersWithTuto">Registered users with tuto
					done</label>
			</div>
		</div>
		<div id="connectionsStats" class="stats-input-group">
			<div class="stats-input">
				<input type="checkbox" name="avgSiteConnections"
					id="avgSiteConnections" /> <label for="avgSiteConnections">Sites
					connections average per daily user</label>
			</div>
			<div class="stats-input">
				<input type="checkbox" name="medianSiteConnections"
					id="medianSiteConnections" /> <label for="medianSiteConnections">Sites
					connections median per daily user</label>
			</div>
		</div>
		<div id="appsStats" class="stats-input-group">
			<div class="stats-input">
				<input type="checkbox" name="appsAdded" id="appsAdded" /> <label
					for="appsAdded">Apps added</label>
			</div>
			<div class="stats-input">
				<input type="checkbox" name="appsRemoved" id="appsRemoved" /> <label
					for="appsRemoved">Apps removed</label>
			</div>
		</div>
		<div class="stats-input-group">
			<div class="stats-input">
				<input type="checkbox" name="dailyUsers" id="dailyUsers" /> <label
					for="dailyUsers">Daily users for selected period</label>
			</div>
		</div>
		<a id='getStats'>Go !</a>
	</div>
	<div id="responses">
		<div class="general-values">
			<p>From: <span id="startDateSelected"></span> To: <span id="endDateSelected"></span></p>
			<p><span id="dailyUsersNumber"></span> daily users</p>
		</div>
		<div class="stats-graph" id="usersGraph">
			<canvas id="usersCanvas"></canvas>
		</div>
		<div class="stats-graph" id="connectionsGraph">
			<canvas id="connectionsCanvas"></canvas>
		</div>
		<div class="stats-graph" id="appsGraph">
			<canvas id="appsCanvas"></canvas>
		</div>
	</div>
</div>