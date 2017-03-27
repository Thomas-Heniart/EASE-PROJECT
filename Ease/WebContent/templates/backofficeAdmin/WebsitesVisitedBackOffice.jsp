<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="RightSideViewTab" id="WebsitesVisitedTab">
	<button id="quit">
		<i class="fa fa-times"></i>
	</button>
	<div class="presentation">
		<h1>Websites visited and done</h1>
	</div>
	<div class="flex-row">
		<div class="centered-row">
			<h3 class="module-presentation">Blacklist</h3>
			<div id="blacklist"></div>
		</div>
		<div class="centered-row">
			<h3 class="module-presentation">Waiting</h3>
			<div id="results"></div>
		</div>
	</div>
	<div class="flex-row">
		<div class="centered-row">
			<h3 class="module-presentation">Broken</h3>
			<div id="websitesBroken"></div>
		</div>
		<div class="centered-row">
			<h3 class="module-presentation">Integrated</h3>
			<div id="websitesDone"></div>
		</div>
	</div>
</div>