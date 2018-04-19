const users_cohort_date_range = $("#users_cohort_date_range");
const users_cohort_date_start = $("#users_cohort_date_start");
const users_cohort_date_end = $("#users_cohort_date_end");
const users_cohort_avg_clicks = $("#users_cohort_avg_clicks");

const teams_cohort_date_range = $("#teams_cohort_date_range");
const teams_cohort_date_start = $("#teams_cohort_date_start");
const teams_cohort_date_end = $("#teams_cohort_date_end");
const teams_cohort_avg_clicks = $("#teams_cohort_avg_clicks");

const clickRepartitionCanvas = $("#clickRepartitionChart");
const click_repartition_date_range = $("#click_repartition_date_range");
const click_repartition_date_start = $("#click_repartition_date_start");
const click_repartition_date_end = $("#click_repartition_date_end");
let clickRepartionChart;

const clickTypesCanvas = $("#clickTypesChart");
const click_types_date_range = $("#click_types_date_range");
const click_types_date_start = $("#click_types_date_start");
const click_types_date_end = $("#click_types_date_end");
let clickTypesChart;

const clickHistoryCanvas = $("#clickHistoryChart");
const click_history_date_range = $("#click_history_date_range");
const click_history_date_start = $("#click_history_date_start");
const click_history_date_end = $("#click_history_date_end");
let clickHistoryChart;

const appProvenanceCanvas = $("#appProvenanceChart");
const app_provenance_date_range = $("#app_provenance_date_range");
const app_provenance_date_start = $("#app_provenance_date_start");
const app_provenance_date_end = $("#app_provenance_date_end");
let appProvenanceChart;

users_cohort_date_range.submit((e) => {
  e.preventDefault();
  ajaxHandler.get("/api/v1/admin/GetUsersCohortData", {
    start_week_ms: users_cohort_date_start[0].valueAsDate.getTime(),
    end_week_ms: users_cohort_date_end[0].valueAsDate.getTime(),
    avg_clicks: parseInt(users_cohort_avg_clicks.val())
  }, () => {
  }, (data) => {
    let initialDate = new Date(users_cohort_date_start[0].valueAsDate.getTime());
    Cornelius.draw({
      initialDate: initialDate,
      container: document.getElementById('main_users_cohort'),
      cohort: data,
      title: "Users cohort",
      timeInterval: 'weekly'
    })
  })
});

teams_cohort_date_range.submit((e) => {
  e.preventDefault();
  ajaxHandler.get("/api/v1/admin/GetTeamsCohortData", {
    start_week_ms: teams_cohort_date_start[0].valueAsDate.getTime(),
    end_week_ms: teams_cohort_date_end[0].valueAsDate.getTime(),
    avg_clicks: parseInt(teams_cohort_avg_clicks.val())
  }, () => {
  }, (data) => {
    let initialDate = new Date(teams_cohort_date_start[0].valueAsDate.getTime());
    Cornelius.draw({
      initialDate: initialDate,
      container: document.getElementById('main_teams_cohort'),
      cohort: data,
      title: "Teams cohort",
      timeInterval: 'weekly'
    })
  })
});

click_repartition_date_range.submit((e) => {
  e.preventDefault();
  ajaxHandler.get("/api/v1/admin/GetPasswordUsedStatistics", {
    start_week_ms: click_repartition_date_start[0].valueAsDate.getTime(),
    end_week_ms: click_repartition_date_end[0].valueAsDate.getTime(),
  }, () => {
  }, (data) => {
    buildClickChart(data)
  })
});

click_types_date_range.submit((e) => {
  e.preventDefault();
  ajaxHandler.get("/api/v1/admin/GetAppTypesStatistics", {
    start_week_ms: click_types_date_start[0].valueAsDate.getTime(),
    end_week_ms: click_types_date_end[0].valueAsDate.getTime(),
  }, () => {
  }, (data) => {
    buildAppTypesChart(data)
  })
});

click_history_date_range.submit((e) => {
  e.preventDefault();
  ajaxHandler.get("/api/v1/admin/GetClickHistoryStatistics", {
    start_week_ms: click_history_date_start[0].valueAsDate.getTime(),
    end_week_ms: click_history_date_end[0].valueAsDate.getTime(),
  }, () => {
  }, (data) => {
    buildClickHistoryChart(data)
  })
});

app_provenance_date_range.submit((e) => {
  e.preventDefault();
  ajaxHandler.get("/api/v1/admin/GetAppProvenanceStatistics", {
    start_week_ms: app_provenance_date_start[0].valueAsDate.getTime(),
    end_week_ms: app_provenance_date_end[0].valueAsDate.getTime(),
  }, () => {
  }, (data) => {
    buildAppProvenanceChart(data)
  })
});

$(document).ready(() => {
  ajaxHandler.get("/api/v1/admin/GetPasswordUsedStatistics", {}, () => {
  }, (data) => {
    buildClickChart(data)
  });
  ajaxHandler.get("/api/v1/admin/GetAppTypesStatistics", {}, () => {
  }, (data) => {
    buildAppTypesChart(data)
  });
  ajaxHandler.get("/api/v1/admin/GetClickHistoryStatistics", {}, () => {
  }, (data) => {
    buildClickHistoryChart(data)
  });
  ajaxHandler.get("/api/v1/admin/GetAppProvenanceStatistics", {}, () => {
  }, (data) => {
    buildAppProvenanceChart(data)
  })
});

const buildClickChart = (data) => {
  const options = {
    scales: {
      xAxes: [{
        stacked: true
      }],
      yAxes: [{
        stacked: true,
        ticks: {
          beginAtZero: true,
          max: 100
        }
      }]
    }
  };
  const labels = data.labels;
  let datasets = [];
  datasets.push({
    backgroundColor: '#27053A',
    label: 'DashboardClick',
    data: data.fromDashboardClick
  });
  datasets.push({
    backgroundColor: '#E56EBD',
    label: 'Extension',
    data: data.fromExtension
  });
  datasets.push({
    backgroundColor: '#8CB8FF',
    label: 'FillIn',
    data: data.fromFillIn
  });
  datasets.push({
    backgroundColor: '#D6A0AD',
    label: 'Copy',
    data: data.fromCopy
  });
  if (!!clickRepartionChart)
    clickRepartionChart.destroy();
  clickRepartionChart = new Chart(clickRepartitionCanvas, {
    type: 'bar',
    data: {
      labels: labels,
      datasets: datasets
    },
    options: options
  })
};

const buildAppTypesChart = (data) => {
  const options = {
    scales: {
      xAxes: [{
        stacked: true
      }],
      yAxes: [{
        stacked: true,
        ticks: {
          beginAtZero: true,
          max: 100
        }
      }]
    }
  };
  const labels = data.labels;
  let datasets = [];
  datasets.push({
    backgroundColor: '#E56EBD',
    label: 'Classic',
    data: data.classicApps
  });
  datasets.push({
    backgroundColor: '#8CB8FF',
    label: 'Any',
    data: data.anyApps
  });
  if (!!clickTypesChart)
    clickTypesChart.destroy();
  clickTypesChart = new Chart(clickTypesCanvas, {
    type: 'bar',
    data: {
      labels: labels,
      datasets: datasets
    },
    options: options
  })
};

const buildClickHistoryChart = (data) => {
  const options = {
    scales: {
      yAxes: [{
        ticks: {
          beginAtZero: true,
        }
      }]
    }
  };
  const labels = data.labels;
  let datasets = [];
  datasets.push({
    backgroundColor: '#8CB8FF',
    label: 'Clicks',
    data: data.clicks
  });
  if (!!clickHistoryChart)
    clickHistoryChart.destroy();
  clickHistoryChart = new Chart(clickHistoryCanvas, {
    type: 'line',
    data: {
      labels: labels,
      datasets: datasets
    },
    options: options
  })
};

const buildAppProvenanceChart = (data) => {
  const options = {
    scales: {
      xAxes: [{
        stacked: true
      }],
      yAxes: [{
        stacked: true,
        ticks: {
          beginAtZero: true,
          max: 100
        }
      }]
    }
  };
  const labels = data.labels;
  let datasets = [];
  datasets.push({
    backgroundColor: '#051937',
    label: 'Single',
    data: data.single,
    stack: 1
  });
  datasets.push({
    backgroundColor: '#A8EB12',
    label: 'Enterprise',
    data: data.enterprise,
    stack: 1
  });
  datasets.push({
    backgroundColor: '#0363F2',
    label: 'Pro',
    data: data.pro,
    stack: 2
  });
  datasets.push({
    backgroundColor: '#D0C11A',
    label: 'Perso',
    data: data.perso,
    stack: 2
  });
  if (!!appProvenanceChart)
    appProvenanceChart.destroy();
  appProvenanceChart = new Chart(appProvenanceCanvas, {
    type: 'bar',
    data: {
      labels: labels,
      datasets: datasets
    },
    options: options
  })
};