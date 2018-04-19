<div class="ui top attached tabular menu">
    <div class="item active" data-tab="recap">Recap</div>
    <div class="item" data-tab="users_cohort">Users cohort</div>
    <div class="item" data-tab="teams_cohort">Teams cohort</div>
    <div class="item" data-tab="click_repartition">Click provenance</div>
    <div class="item" data-tab="click_types">Click types</div>
    <div class="item" data-tab="click_history">Click history</div>
    <div class="item" data-tab="app_provenance">App provenance</div>
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
<div class="ui bottom attached tab segment" data-tab="users_cohort">
    <div id="main_users_cohort"></div>
    <form id="users_cohort_date_range">
        <label for="users_cohort_date_start">From: </label>
        <input type="date" id="users_cohort_date_start" />
        <label for="users_cohort_date_end">To: </label>
        <input type="date" id="users_cohort_date_end" />
        <label for="users_cohort_avg_clicks">Number of clicks to be active user</label>
        <input type="number" id="users_cohort_avg_clicks" value="1" min="1" />
        <button type="submit">Generate</button>
    </form>
</div>
<div class="ui bottom attached tab segment" data-tab="teams_cohort">
    <div id="main_teams_cohort"></div>
    <form id="teams_cohort_date_range">
        <label for="teams_cohort_date_start">From: </label>
        <input type="date" id="teams_cohort_date_start" />
        <label for="teams_cohort_date_end">To: </label>
        <input type="date" id="teams_cohort_date_end" />
        <label for="teams_cohort_avg_clicks">Number of clicks to be active user</label>
        <input type="number" id="teams_cohort_avg_clicks" value="1" min="1" />
        <button type="submit">Generate</button>
    </form>
</div>
<div class="ui bottom attached tab segment" data-tab="click_repartition">
    <form id="click_repartition_date_range">
        <label for="click_repartition_date_start">From: </label>
        <input type="date" id="click_repartition_date_start" />
        <label for="click_repartition_date_end">To: </label>
        <input type="date" id="click_repartition_date_end" />
        <button type="submit">Generate</button>
    </form>
    <div style="max-height: 600px; max-width: 1000px">
        <canvas id="clickRepartitionChart" width="800" height="500"></canvas>
    </div>
</div>
<div class="ui bottom attached tab segment" data-tab="click_types">
    <form id="click_types_date_range">
        <label for="click_types_date_start">From: </label>
        <input type="date" id="click_types_date_start" />
        <label for="click_types_date_end">To: </label>
        <input type="date" id="click_types_date_end" />
        <button type="submit">Generate</button>
    </form>
    <div style="max-height: 600px; max-width: 1000px">
        <canvas id="clickTypesChart" width="800" height="500"></canvas>
    </div>
</div>
<div class="ui bottom attached tab segment" data-tab="click_history">
    <form id="click_history_date_range">
        <label for="click_history_date_start">From: </label>
        <input type="date" id="click_history_date_start" />
        <label for="click_history_date_end">To: </label>
        <input type="date" id="click_history_date_end" />
        <button type="submit">Generate</button>
    </form>
    <div style="max-height: 600px; max-width: 1000px">
        <canvas id="clickHistoryChart" width="800" height="500"></canvas>
    </div>
</div>
<div class="ui bottom attached tab segment" data-tab="app_provenance">
    <form id="app_provenance_date_range">
        <label for="app_provenance_date_start">From: </label>
        <input type="date" id="app_provenance_date_start" />
        <label for="app_provenance_date_end">To: </label>
        <input type="date" id="app_provenance_date_end" />
        <button type="submit">Generate</button>
    </form>
    <div style="max-height: 600px; max-width: 1000px">
        <canvas id="appProvenanceChart" width="800" height="500"></canvas>
    </div>
</div>
<script src="js/backOffice/statistics.js?v=4" async></script>