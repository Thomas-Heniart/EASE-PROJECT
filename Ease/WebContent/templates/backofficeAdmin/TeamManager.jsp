<table class="ui compact small celled table">
    <thead>
    <tr>
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
        Current credit (TTC): <span id="current-credit"></span>
        <div id="send-money" class="ui icon input">
            <input name="love_money" placeholder="Add credit...">
            <i class="inverted circular stripe link icon"></i>
        </div>
    </div>
</div>