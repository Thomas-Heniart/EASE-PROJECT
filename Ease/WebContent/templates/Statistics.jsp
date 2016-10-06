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
		<input type="checkbox" name="registerAndTutoDone" id="registerAndTutoDone" />
		<label for="registerAndTutoDone">People connected and who achieve tutorial</label>
		<button id='getStats'>Try</button>
	</div>
	<div id="response">
		<div name="registerAndTutoDone">
			<p></p>
		</div>
	</div>
</div>

<script>
	function enterStatisticsMode() {
		$('#StatisticsTab').addClass('show');
	}

	function leaveStatisticsMode() {
		$('#StatisticsTab').removeClass('show');
	}

	$(document).ready(function() {
		
		$('#enterStatisticsMode').click(function() {
			leaveTagsManagerMode();
			leaveRequestedWebsitesMode();
			leaveAddSiteMode();
			leaveChangeBackMode();
			leaveAddUsersMode();
			enterStatisticsMode();
		});
	});

	$(document).ready(function() {
		$("#getStats").click(function() {
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();
			var registerAndTutoDone = $("#registerAndTutoDone").val();
			$.post(
				'Statistics',
				{
					startDate : startDate,
					endDate : endDate,
					registerAndTutoDone : registerAndTutoDone
				},
				function(data) {
					/* JSONArray composed by JSONObjects (key => name of the stats, value => value) */
					var json = JSON.parse(data.substring(9));
					console.log(json);
					for (var key in json) {
						console.log(key + " : " + json);
					}
				}, 'text'
			);
		});
		$('#StatisticsTab #quit').click(function() {
			leaveStatisticsMode();
		});
	});
</script>