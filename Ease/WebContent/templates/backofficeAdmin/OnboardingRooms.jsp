<div class="ui grid">
    <div class="five wide column">
        <div class="ui vertical menu" id="onboarding_rooms_menu">
            <a id="add_onboarding_room" class="item">Add room</a>
        </div>
    </div>
    <div class="eleven wide  stretched column tabs" id="onboarding_rooms_tabs"></div>
</div>

<div class="ui modal" id="onboarding_rooms_add_website">
    <div class="header">Add a website</div>
    <div class="content">
        <div class="ui form">
            <div class="field">
                <div class="ui fluid search selection dropdown">
                    <input type="hidden" name="website_id"/>
                    <i class="dropdown icon"></i>
                    <input class="search" autocomplete="off" tabindex="0">
                    <div class="default text">Select website to add</div>
                    <div class="menu"></div>
                </div>
            </div>
            <button class="ui button green">Add</button>
        </div>
    </div>
</div>

<div class="ui modal" id="onboarding_rooms_edit">
    <div class="header">Edit room</div>
    <div class="content">
        <form class="ui form">
            <div class="field">
                <label>Name</label>
                <input type="text" name="name" placeholder="name"/>
            </div>
            <div class="field">
                <label>Example</label>
                <input type="text" name="example" placeholder="example"/>
            </div>
            <button class="ui button green">Submit</button>
        </form>
    </div>
</div>

<div class="ui modal basic" id="onboarding_rooms_delete">
    <div class="ui icon header">
        Delete onboarding room
    </div>
    <div class="content">
        <p>Do you really want to delete this onboarding room ?</p>
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