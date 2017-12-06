<table class="ui compact small selectable celled table">
    <thead>
    <tr>
        <th>count</th>
        <th>Name</th>
        <th>Admin name</th>
        <th>Admin email</th>
        <th>Phone number</th>
        <th>Week of sub</th>
        <th>Week now</th>
        <th>Plan</th>
        <th>CC</th>
    </tr>
    </thead>
    <tbody id="team-manager-body"></tbody>
</table>
<div class="ui united modal" id="team-settings">
    <i class="close icon"></i>
    <div class="header">Team settings</div>
    <div class="content">
        <div class="ui horizontal segments">
            <div id="team_settings_left" class="ui segment loading">
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
            </div>
            <div id="team_settings_right" class="ui segment loading">
                <div id="account_data" class="data_part">
                    <h2>Account data</h2>
                    <p>Rooms: <span id="rooms"></span> <span id="room_names"></span></p>
                    <p>Total apps: <span id="cards"></span></p>
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
                <div id="people_data_history" class="view_part" style="display: none;">
                    <canvas id="people_data_chart" width="400" height="400"></canvas>
                    <button class="ui blue button">back</button>
                </div>
            </div>
        </div>
        <div class="ui segment">
            Current credit (TTC): <span id="current-credit"></span>
            <div id="send-money" class="ui icon input">
                <input name="love_money" placeholder="Add credit...">
                <i class="inverted circular stripe link icon"></i>
            </div>
            <button class="ui button blue">Show graph</button>
        </div>
    </div>
    <!-- <div class="content">
        <div class="ui one column centered grid">
            <div class="column">
                Current credit (TTC): <span id="current-credit"></span>
                <div id="send-money" class="ui icon input">
                    <input name="love_money" placeholder="Add credit...">
                    <i class="inverted circular stripe link icon"></i>
                </div>
            </div>
            <div class="column" id="card-number">
                Number of cards: <span></span>
            </div>
            <div class="column" id="link-number">
                Number of link apps: <span></span>
            </div>
            <div class="column" id="single-number">
                Number of single apps: <span></span>
            </div>
            <div class="column" id="enterprise-number">
                Number of enterprise apps: <span></span>
            </div>
            <div class="column" id="card-with-password-reminder">
                Number of cards with password modification: <span></span>
            </div>
            <div class="column">
                <button class="ui button negative">Delete</button>
            </div>
        </div>
    </div> -->
</div>