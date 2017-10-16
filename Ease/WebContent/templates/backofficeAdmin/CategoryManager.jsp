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