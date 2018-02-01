<table class="ui compact small celled table">
    <thead>
    <tr>
        <th>id</th>
        <th>Name</th>
        <th>Position</th>
        <th>Edit</th>
        <th><i class="fa fa-trash-o"/></th>
    </tr>
    </thead>
    <tbody id="category-manager-body"></tbody>
</table>
<form id="add-category">
    <div class="ui input"><input placeholder="Add category..."/></div>
</form>
<div class="ui modal" id="category-modal">
    <i class="close icon"></i>
    <div class="header">Edit websites</div>
    <div class="content">
        <form action="/api/v1/admin/EditCategory" class="ui form" id="category-edition">
            <h4 class="ui dividing header">Basics</h4>
            <div class="two fields">
                <div class="field">
                    <label>Name</label>
                    <input name="name" placeholder="Name...">
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
<div class="ui modal basic" id="category-delete">
    <div class="ui icon header">
        Delete category
    </div>
    <div class="content">
        <p>Do you really want to delete this category</p>
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