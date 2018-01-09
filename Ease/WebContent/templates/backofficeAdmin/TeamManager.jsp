<table class="ui compact small selectable celled table">
    <thead>
    <tr>
        <th>count</th>
        <th>
            <div class="ui checkbox"><input id="select-all-team" type="checkbox"/><label></label></div>
        </th>
        <th>Name</th>
        <th>Admin name</th>
        <th>Admin email</th>
        <th>Week of sub</th>
        <th>Plan</th>
        <th>CC</th>
        <th>P. joined</th>
        <th>App w. tags</th>
        <th>P. w. apps</th>
        <th>P. click 1</th>
        <th>P. click 3</th>
    </tr>
    </thead>
    <tbody id="team-manager-body"></tbody>
</table>
<button id="multiple_team_graph" class="ui blue button">Show graph</button>
<div class="ui united fullscreen modal" id="team-settings">
    <i class="close icon"></i>
    <div class="header">Team settings</div>
    <div class="content">
        <div class="ui horizontal segments">
            <div id="team_settings_left" class="ui segment loading" style="width: 50%;">
                <div id="people_data" class="data_part">
                    <h2>People data</h2>
                    <p>People invited: <span id="people_invited"></span> <a href="#" id="people_invited_emails">(see
                        emails)</a></p>
                    <p>People who joined: <span id="people_joined"></span> <a href="#" id="people_joined_emails">(see
                        emails)</a></p>
                    <p>People with company apps: <span id="people_with_cards"></span> <a href="#"
                                                                                         id="people_with_cards_emails">(see
                        emails)</a>
                    </p>
                    <p>People with personal apps: <span id="people_with_personnal_apps"></span> <a href="#"
                                                                                                   id="people_with_personnal_apps_emails">(see
                        emails)</a></p>
                    <p>People who click on app 1 day: <span id="people_click_on_app_once"></span> <a href="#"
                                                                                                     id="people_click_on_app_once_emails">(see
                        emails)</a></p>
                    <p>People who click on app 3 days: <span id="people_click_on_app_three_times"></span> <a href="#"
                                                                                                             id="people_click_on_app_three_times_emails">(see
                        emails)</a></p>
                    <p>People who click on app 5 days: <span id="people_click_on_app_five_times"></span> <a href="#"
                                                                                                            id="people_click_on_app_five_times_emails">(see
                        emails)</a></p>
                    <button class="ui blue button">Show history</button>
                </div>
                <div id="click_average_graphic" style="display: none; max-width: 1050px; max-height: 400px">
                    <canvas id="click_average_canvas" width="1000" height="400"></canvas>
                    <button class="ui blue button">back</button>
                </div>
                <div id="account_data_history" style="display: none; max-width: 600px; max-height: 600px">
                    <canvas id="account_data_chart"></canvas>
                    <button class="ui blue button">back</button>
                </div>
                <div id="account_data_names" style="display: none">
                    <ul></ul>
                    <button class="ui blue button">back</button>
                </div>
            </div>
            <div id="team_settings_right" class="ui segment loading" style="width: 50%;">
                <div id="account_data" class="data_part">
                    <h2>Account data</h2>
                    <p>Rooms: <span id="rooms"></span> <span id="room_names"></span></p>
                    <p>Total apps: <span id="cards"></span> <a href="#" id="cards_names">(names)</a></p>
                    <p>Total apps with tags: <span id="cards_with_receiver"></span></p>
                    <p>Total apps with tags + PWP: <span id="cards_with_receiver_and_password_policy"></span></p>
                    <p>Single apps: <span id="single_cards"></span></p>
                    <p>Enterprise apps: <span id="enterprise_cards"></span></p>
                    <p>Link apps: <span id="link_cards"></span></p>
                    <button class="ui blue button">Show history</button>
                </div>
                <div id="people_data_emails" class="view_part" style="display: none;">
                    <ul></ul>
                    <button class="ui blue button">back</button>
                </div>
                <div id="people_data_history" class="view_part"
                     style="display: none; max-width: 600px; max-height: 600px">
                    <canvas id="people_data_chart"></canvas>
                    <button class="ui blue button">back</button>
                </div>
            </div>
        </div>
        <div class="ui segment">
            <div id="credit" style="display: inline">
                Current credit (TTC): <span id="current-credit"></span>
                <div id="send-money" class="ui icon input">
                    <input name="love_money" placeholder="Add credit...">
                    <i class="inverted circular stripe link icon"></i>
                </div>
            </div>
            <button id="show_graphic" class="ui button blue">Show graph</button>
            <button id="show_delete" class="ui button red">Delete</button>
        </div>
    </div>
</div>

<div class="ui modal basic" id="team_delete">
    <div class="ui icon header">
        Delete team
    </div>
    <div class="content">
        <p>Do you really want to delete this team</p>
    </div>
    <div class="actions">
        <div class="ui red basic inverted button">
            <i class="remove icon"></i>
            Yes
        </div>
        <div class="ui grey cancel inverted button">
            Cancel
        </div>
    </div>
</div>