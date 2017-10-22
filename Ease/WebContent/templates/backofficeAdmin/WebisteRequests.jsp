<table class="ui compact small celled table">
    <thead>
    <tr>
        <th>id</th>
        <th>URL</th>
        <th>Email</th>
        <th>Date</th>
        <th><i class="fa fa-share"/></th>
        <th><i class="fa fa-trash-o"/></th>
    </tr>
    </thead>
    <tbody id="website-requests-manager-body"></tbody>
</table>
<button>Send email(s)</button>
<div class="ui modal" id="website-requests-modal">
    <i class="close icon"></i>
    <div class="header">Edit websites</div>
    <div class="content">
        <form action="/api/vi/admin/EmailWebsiteIntegration" class="ui form" id="website-integration">
            <h4 class="ui dividing header">Basics</h4>
            <div class="two fields">
                <p>Email to: <span id="user-email"></span></p>
                <div class="field">
                    <label>Name</label>
                    <textarea
                </div>
                <div class="field">
                    <label>Position</label>
                    <input name="position" placeholder="position">
                </div>
            </div>
            <button class="ui button">Edit</button>
        </form>
    </div>
</div>