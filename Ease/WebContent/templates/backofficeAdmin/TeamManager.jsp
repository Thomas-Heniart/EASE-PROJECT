<table class="ui compact small celled table">
    <thead>
    <tr>
        <th>count</th>
        <th>id</th>
        <th>Name</th>
        <th>Admin first name</th>
        <th>Admin last name</th>
        <th>Admin email</th>
        <th>Users</th>
        <th>Active users</th>
        <th>Phone number</th>
        <th></th>
    </tr>
    </thead>
    <tbody id="team-manager-body"></tbody>
</table>
<div class="ui modal" id="team-settings">
    <i class="close icon"></i>
    <div class="header">Team settings</div>
    <div class="content">
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
    </div>
</div>