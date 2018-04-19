let beforeOnboardingCanvas = $("#beforeOnboardingChart");
let beforeOnboardingChart;

const buildBeforeOnboardingChart = (data) => {
  const options = {
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

$(document).ready(() => {
  ajaxHandler.get("/api/v1/admin/onboarding-before-creation-chart-data", {}, () => {
  }, (data) => {
    buildBeforeOnboardingChart(data);
  })
});