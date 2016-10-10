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
			var dailyConnections = $("#dailyConnections:checked").val();
      var registeredUsers = $("#registeredUsers:checked").val();
      var registeredUsersWithTuto = $("#registeredUsersWithTuto:checked").val();
      var websitesConnections = $("#websitesConnections:checked").val();
      var appsAdded = $("#appsAdded:checked").val();
			var appsRemoved = $("#appsRemoved:checked").val();
      var medianAppsAdded = $("#medianAppsAdded:checked").val();
			var dailyUsers = $("#dailyUsers:checked").val();
			$("#statisticsForm").hide();
			$(".loading-stats").show();
      $.post(
				'Statistics',
				{
					startDate : startDate,
					endDate : endDate,
					dailyConnections : dailyConnections,
          registeredUsers : registeredUsers,
          registeredUsersWithTuto : registeredUsersWithTuto,
					websitesConnections : websitesConnections,
          appsAdded : appsAdded,
          appsRemoved : appsRemoved,
					dailyUsers : dailyUsers
				},
				function(data) {
					var json = JSON.parse(data.substring(4));
					var usersCtx = $("#usersCanvas");
          var connectionsCtx = $("#connectionsCanvas");
          var appsCtx = $("#appsCanvas");
          var key;
          var dates = null;
          var usersDatasets = [];
          var connectionsDatasets = [];
          var appsDatasets = [];
					var dailyUsers = null;
          for (key in json) {
            var tmpObj = json[key];
            if (key != "dates" && key != "dailyUsers") {
              switch (tmpObj.chart) {
                case "usersChart":
                  pushObjInDatasets(tmpObj, usersDatasets);
                  break;
                case "connectionsChart":
                  pushObjInDatasets(tmpObj, connectionsDatasets);
                  break;
                case "appsChart":
                  pushObjInDatasets(tmpObj, appsDatasets);
                  break;
                default:
                  break;

              }
            } else if (key == "dates")
              dates = tmpObj;
						else {
							console.log(tmpObj);
							dailyUsers = tmpObj;
						}
          }
          var usersChartData = {
            labels : dates,
            datasets : usersDatasets
          };
          var connectionsChartData = {
            labels : dates,
            datasets : connectionsDatasets
          };
          var appsChartData = {
            labels : dates,
            datasets : appsDatasets
          };
          if (usersChartData.datasets.length > 0) {
					     var usersChart = createNewChart(usersCtx, usersChartData, 'line');
							 $("#usersGraph").addClass("show");
					}
          if (connectionsChartData.datasets.length > 0) {
            var connectionsChart = createNewChart(connectionsCtx, connectionsChartData, 'line');
						$("#connectionsGraph").addClass("show");
					}
          if (appsChartData.datasets.length > 0) {
            var appsChart = createNewChart(appsCtx, appsChartData, 'line');
						$("#appsGraph").addClass("show");
					}
					if (dailyUsers != null) {
						$("#dailyUsersNumber").text(dailyUsers);
						$("#startDateSelected").text(dates[0]);
						$("#endDateSelected").text(dates[dates.length - 1]);
						$(".general-values").addClass("show");
					}
					$(".loading-stats").hide();
				}, 'text'
			);
		});
		$('#StatisticsTab #quit').click(function() {
			leaveStatisticsMode();
		});
	});

function pushObjInDatasets(jsonObj, datasets) {
  datasets.push({
    label : jsonObj.label,
    data : jsonObj.values,
    backgroundColor : jsonObj.backgroundColor,
    borderColor : jsonObj.borderColor
  });
}

function createNewChart(ctx, data, type) {
  return (new Chart(ctx, {
    type : type,
    responsive : true,
    maintainAspectRatio : false,
    data: data
  }))
}
