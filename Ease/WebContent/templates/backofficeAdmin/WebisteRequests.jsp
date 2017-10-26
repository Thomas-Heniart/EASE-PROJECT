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
<button class="ui primary button">Send email(s)</button>

<div class="ui modal basic" id="request-delete">
    <div class="ui icon header">
        Delete category
    </div>
    <div class="content">
        <p>Do you really want to delete this request</p>
    </div>
    <div class="actions">
        <div class="ui red basic ok inverted button">
            <i class="remove icon"></i>
            Yes
        </div>
        <div class="ui grey cancel inverted button">
            Cancel
        </div>
    </div>
</div>