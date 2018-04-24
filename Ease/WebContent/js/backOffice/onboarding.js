let beforeOnboardingCanvas = $("#beforeOnboardingChart");
let beforeOnboardingChart;

let afterOnboardingCanvas = $("#afterOnboardingChart");
let afterOnboardingChart;

const buildBeforeOnboardingChart = (data) => {
  const options = {
    legend: {
      display: false
    },
    scales: {
      yAxes: [{
        ticks: {
          beginAtZero: true
        }
      }]
    }
  };
  let datasets = [];
  datasets.push({
    backgroundColor: [
      'rgba(255, 99, 132, 0.2)',
      'rgba(54, 162, 235, 0.2)',
      'rgba(255, 206, 86, 0.2)',
      'rgba(75, 192, 192, 0.2)',
      'rgba(153, 102, 255, 0.2)'
    ],
    borderColor: [
      'rgba(255,99,132,1)',
      'rgba(54, 162, 235, 1)',
      'rgba(255, 206, 86, 1)',
      'rgba(75, 192, 192, 1)',
      'rgba(153, 102, 255, 1)'
    ],
    borderWidth: 1,
    data: data
  });
  if (!!beforeOnboardingChart)
    beforeOnboardingChart.destroy();
  beforeOnboardingChart = new Chart(beforeOnboardingCanvas, {
    type: 'bar',
    label: 'Before creation',
    data: {
      labels: [
        'Page visited',
        'Email submit',
        'Digits submit',
        'Company information',
        'Personal information'
      ],
      datasets: datasets
    },
    options: options
  })
};

const buildAfterOnboardingChart = (data) => {
  const options = {
    legend: {
      display: false
    },
    scales: {
      yAxes: [{
        ticks: {
          beginAtZero: true
        }
      }]
    }
  };
  let datasets = [];
  datasets.push({
    backgroundColor: [
      'rgba(175, 210, 233, 0.7)',
      'rgba(154, 113, 151, 0.7)',
      'rgba(157, 150, 184, 0.7)',
      'rgba(157, 150, 184, 0.7)',
      'rgba(157, 150, 184, 0.7)',
      'rgba(157, 150, 184, 0.7)'
    ],
    borderColor: [
      'rgba(175, 210, 233, 1)',
      'rgba(154, 113, 151, 1)',
      'rgba(157, 150, 184, 1)',
      'rgba(157, 150, 184, 1)',
      'rgba(157, 150, 184, 1)',
      'rgba(157, 150, 184, 1)',
      'rgba(157, 150, 184, 1)'
    ],
    borderWidth: 1,
    data: data
  });
  if (!!afterOnboardingChart)
    afterOnboardingChart.destroy();
  afterOnboardingChart = new Chart(afterOnboardingCanvas, {
    type: 'bar',
    label: 'After creation',
    data: {
      labels: [
        'Teams created',
        'Active teams',
        '0->10 tools',
        '11->20 tools',
        '21->30 tools',
        '30+ tools'
      ],
      datasets: datasets
    },
    options: options
  })
};


$(document).ready(() => {
  ajaxHandler.get("/api/v1/admin/onboarding-before-creation-chart-data", {}, () => {
  }, (data) => {
    $(".segment[data-tab=before_onboarding]").removeClass("loading");
    buildBeforeOnboardingChart(data);
  });
  ajaxHandler.get("/api/v1/admin/onboarded-teams-chart-data", {}, () => {
  }, (data) => {
    $(".segment[data-tab=after_onboarding]").removeClass("loading");
    buildAfterOnboardingChart(data);
  })
});