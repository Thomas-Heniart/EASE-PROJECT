const users_cohort_date_range = $("#users_cohort_date_range");
const users_cohort_date_start = $("#users_cohort_date_start");
const users_cohort_date_end = $("#users_cohort_date_end");

users_cohort_date_range.submit((e) => {
  e.preventDefault();
  ajaxHandler.get("/api/v1/admin/GetUsersCohortData", {
    start_week_ms: users_cohort_date_start[0].valueAsDate.getTime(),
    end_week_ms: users_cohort_date_end[0].valueAsDate.getTime()
  }, () => {
  }, (data) => {
    let initialDate = new Date(2018, 2, 11);
    Cornelius.draw({
      initialDate: initialDate,
      container: document.getElementById('main_users_cohort'),
      cohort: data,
      title: "Users cohort",
      timeInterval: 'weekly'
    })
  })
});