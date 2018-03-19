const users_cohort_date_range = $("#users_cohort_date_range");
const users_cohort_date_start = $("#users_cohort_date_start");
const users_cohort_date_end = $("#users_cohort_date_end");
const users_cohort_avg_clicks = $("#users_cohort_avg_clicks");

const teams_cohort_date_range = $("#teams_cohort_date_range");
const teams_cohort_date_start = $("#teams_cohort_date_start");
const teams_cohort_date_end = $("#teams_cohort_date_end");
const teams_cohort_avg_clicks = $("#teams_cohort_avg_clicks");

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