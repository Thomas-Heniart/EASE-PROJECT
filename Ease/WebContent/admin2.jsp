<table class="ui compact small celled table">
    <thead>
    <tr>
        <th>id</th>
        <th>Name</th>
        <th>Logo</th>
        <th>Connection URL</th>
        <th>Landing URL</th>
        <th><i class="fa fa-eye-slash"/></th>
        <th>Edit</th>
        <th><i class="fa fa-trash-o"/></th>
        <th>Merge</th>
    </tr>
    </thead>
    <tbody id="website-manager-body"></tbody>
</table>
<div class="ui modal" id="website-integration">
    <i class="close icon"></i>
    <div class="header">Edit websites</div>
    <div class="content">
        <form action="/api/v1/admin/EditWebsite" class="ui form" id="website-edition">
            <h4 class="ui dividing header">Basics</h4>
            <div class="four fields">
                <div class="field">
                    <label>Name</label>
                    <input name="name" placeholder="Name...">
                </div>
                <div class="field">
                    <label>Login url</label>
                    <input name="login_url" placeholder="Login url...">
                </div>
                <div class="field">
                    <label>Landing url</label>
                    <input name="landing_url" placeholder="Landing url...">
                </div>
                <div class="field">
                    <label>Folder</label>
                    <input name="folder" placeholder="Folder...">
                </div>
            </div>
            <h4 class="ui dividing header">Complementary</h4>
            <div class="two fields">
                <div class="field">
                    <div class="ui fluid search selection dropdown sso">
                        <input type="hidden" name="sso_id"/>
                        <i class="dropdown icon"></i>
                        <input class="search" autocomplete="off" tabindex="0">
                        <div class="default text">Select sso...</div>
                        <div class="menu">
                            <div class="item" data-value="-1">No sso</div>
                        </div>
                    </div>
                </div>
                <div class="field">
                    <div class="ui fluid multiple search selection dropdown teams">
                        <input type="hidden" name="team_id"/>
                        <i class="dropdown icon"></i>
                        <input class="search" autocomplete="off" tabindex="0">
                        <div class="default text">Select team...</div>
                        <div class="menu"></div>
                    </div>
                </div>
            </div>
            <h4 class="ui dividing header">Integration</h4>
            <div class="field">
                <div id="integration" class="ui checkbox">
                    <input name="integrate" type="checkbox" tabindex="0" class="hidden">
                    <label>Integrate</label>
                </div>
            </div>
            <button class="ui button">Edit</button>
        </form>
        <form class="ui form" method="POST" action="uploadWebsite" enctype="multipart/form-data">
            <h4 class="ui dividing header"></h4>
            <div class="filed">
                <label>Connection file</label>
                <input type="file"/>
            </div>
            <div class="field">
                <label>Logo</label>
                <input type="file"/>
            </div>
        </form>
    </div>
</div>

<div class="ui modal" id="website-merging">
    <div class="header">Website merge</div>
    <div class="content">
        <div class="ui form">
            <div class="field">
                <div class="ui fluid search selection dropdown">
                    <input type="hidden" name="website_id"/>
                    <i class="dropdown icon"></i>
                    <input class="search" autocomplete="off" tabindex="0">
                    <div class="default text">Select website to merge with...</div>
                    <div class="menu"></div>
                </div>
            </div>
            <button class="ui button">Merge</button>
        </div>
    </div>
</div>
