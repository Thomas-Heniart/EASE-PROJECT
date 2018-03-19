<div class="ui top attached tabular menu">
    <div class="item active" data-tab="recap">Recap</div>
    <div class="item" data-tab="onboarding">Onboarding</div>
    <div class="item" data-tab="users_cohort">Users cohort</div>
</div>
<div class="ui bottom attached tab active segment" data-tab="recap">
    <table class="ui compact small selectable sortable celled table" id="stats_table">
        <thead>
        <tr>
            <th>Week</th>
            <th class="number_data">New teams</th>
            <th class="number_data">New users</th>
            <th class="number_data">New apps</th>
            <th class="number_data">New team apps</th>
            <th class="number_data">Passwords killed</th>
            <th class="number_data">Active users</th>
            <th class="number_data">Active teams</th>
        </tr>
        </thead>
        <tbody id="stats_body"></tbody>
    </table>
</div>
<div class="ui bottom attached tab segment" data-tab="onboarding">
    <canvas id="onboardingChart" width="400" height="400"></canvas>
</div>
<div class="ui bottom attached tab segment" data-tab="users_cohort">
    <div id="main_users_cohort"></div>
    <form id="users_cohort_date_range">
        <label for="users_cohort_date_start">From: </label>
        <input type="date" id="users_cohort_date_start" />
        <label for="users_cohort_date_end">To: </label>
        <input type="date" id="users_cohort_date_end" />
        <button type="submit">Generate</button>
    </form>
    <script src="js/backOffice/statistics.js" async></script>
</div>