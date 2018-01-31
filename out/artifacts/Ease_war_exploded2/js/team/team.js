var team_view = new function () {
    var self = this;
    this.root = $("#team_view");
    this.team_id = null;
    this.me = null;
    this.team_name = null;
    this.channels = [];
    this.users = [];
    this.currentActiveSelector = null;
    this.active_element = null;

    //html area holders
    this.leftMainBar = $('.client_channels_container', this.root);
    this.header = $('#client_header', this.root);

    //header
    this.headerNamePlaceholder = $('#channel_name', this.header);
    this.headerIconHolder = $('.icon_wrapper', this.headerNamePlaceholder);
    this.headerChannelNameHolder = $('.name', this.headerNamePlaceholder);

    this.header_indicators = {
        'apps_number': $('#apps_number', self.header),
        'users_number': $('#users_number', self.header)
    };

    this.reset_header_indicators = function () {
        self.header_indicators['apps_number'].removeClass('active');
        self.header_indicators['users_number'].removeClass('active');
    };
    this.set_header_indicator = function (name, value) {
        self.header_indicators[name].addClass('active');
        $('.value', self.header_indicators[name]).text(value);
    };
    //team name + owner name
    this.ownerArea = $('#team_menu', this.leftMainBar);
    this.teamNameHandler = $('#team_name', this.ownerArea);
    this.ownerNameHandler = $('#team_client_user_name', this.ownerArea);

    //members
    this.usersArea = $('#team_members', this.leftMainBar);
    this.addUserButton = $('#new_member_button', this.usersArea);
    this.usersCounter = $('#members_header_count', this.usersArea);
    this.usersListContainer = $('.section-list', this.usersArea);

    //channels
    this.channelsArea = $('#team_channels', this.leftMainBar);
    this.addChannelButton = $('#new_channel_button', this.channelsArea);
    this.channelsCounter = $('#channel_header_count', this.channelsArea);
    this.channelsListContainer = $('.section-list', this.channelsArea);

    //apps
    this.appsArea = $('#team_apps_container', this.root);

    this.show_apps = function(apps){

    };

    //app adders section
    this.current_active_add_ui = null;
    this.app_adders_area = $('#app_add_actions', this.root);
    this.app_adder_buttons = {
        'simple_add': $('#simple_app_add_button', this.app_adders_area),
        'multiple_add': $('#multiple_app_add_button',this.app_adders_area),
        'link_add': $('#link_app_add_button', this.app_adders_area)
    };
    this.app_adder_uis = {
        'simple_add': new simple_app_add_interface($('#simple_app_adder'), self),
        'multiple_add': new multiple_app_add_interface($('#multiple_app_adder'), self),
        'link_add': new link_app_add_interface($('#link_app_adder'), self)
    };
    self.app_adder_buttons['simple_add'].click(function(){
        self.open_add_ui('simple_add');
    });
    self.app_adder_buttons['multiple_add'].click(function(){
        self.open_add_ui('multiple_add');
    });
    self.app_adder_buttons['link_add'].click(function(){
        self.open_add_ui('link_add');
    });
    this.close_active_add_ui = function () {
        self.app_adders_area.removeClass('active');
        self.current_active_add_ui && self.current_active_add_ui.close();
        self.current_active_add_ui = null;
    };
    this.open_add_ui = function (ui_name) {
        self.current_active_add_ui = self.app_adder_uis[ui_name];
        self.current_active_add_ui && self.current_active_add_ui.open();
        self.app_adders_area.addClass('active');
    };

    //flex panels (describers)
    this.flex_panel_open_button = $('#open_card_button', this.header);
    this.flex_panel = $('#col_flex', this.root);
    this.relative_flex_panel = null;

    this.flexpanels = {
        'user': new Flex_user_panel($('#team_user_tab', this.flex_panel)),
        'channel': new Flex_channel_panel($('#team_tab', this.flex_panel))
    };
    this.close_flex_panel = function () {
        self.relative_flex_panel && self.relative_flex_panel.close();
        self.flex_panel_open_button.removeClass('active');
    };
    this.flexpanels['user'].close_button.click(this.close_flex_panel);
    this.flexpanels['channel'].close_button.click(this.close_flex_panel);

    this.flex_panel_open_button.click(function(){
        if (self.relative_flex_panel == null)
            return;
        if (self.flex_panel_open_button.hasClass('active')){
            self.relative_flex_panel.close();
            self.flex_panel_open_button.removeClass('active');
        } else {
            self.relative_flex_panel.setup();
            self.relative_flex_panel.open();
            self.flex_panel_open_button.addClass('active');
        }
    });
    //flex panels end

    this.reset = function () {
        for (var i = 0; i < self.users.length;i++){
            self.users[i].remove();
        }
        self.users = [];
        for (var i = 0; i < self.channels.length;i++){
            self.channels[i].remove();
        }
        self.channels = [];
    };
    this.load = function(teamId){
        self.reset();
        postHandler.get(
            "/api/v1/teams/GetTeam",
            {team_id: teamId},
            function(){},
            function (data) {
                var jData = JSON.parse(data);
                var tmp = null;
                for (var i = 0;i < jData["teamUsers"].length; i++){
                    tmp = jData["teamUsers"][i];
                    self.addUser(new Team_user(tmp.id, tmp.username, tmp.firstName, tmp.lastName, tmp.email, tmp.role));
                }
                for (var i = 0;i < jData["channels"].length; i++){
                    tmp = jData["channels"][i];
                    self.addChannel(new Team_channel(tmp.id, tmp.name));
                }
                self.team_name = jData["name"];
                self.team_id = teamId;
                self.me = self.getUserById(jData["myTeamUserId"]);
                self.teamNameHandler.text(self.team_name);
                self.ownerNameHandler.text(self.me.selector.name);
                self.channels[0].selector.root.click();
            },
            function () {}
        )
    };

    this.setHeaderNamePlaceholder = function (icon, str) {
        self.headerIconHolder.html(icon);
        self.headerChannelNameHolder.html(str);
    };
    //channels interaction functions
    this.addChannel = function (channel) {
        self.channels.push(channel);
        self.channelsListContainer.append(channel.selector.root);
        self.channelsCounter.text(self.channels.length);
        channel.selector.root.click(function(e){
            self.active_element = channel;
            self.currentActiveSelector && self.currentActiveSelector.setActive(false);
            self.currentActiveSelector = channel.selector;
            self.setHeaderNamePlaceholder(icons['channel'],channel.selector.name);
            channel.load(function(){
                channel.selector.setActive(true);
                self.close_flex_panel();
                self.relative_flex_panel = self.flexpanels['channel'];
                self.relative_flex_panel.setTarget(channel);
                self.reset_header_indicators();
                self.set_header_indicator('apps_number',channel.apps.length);
                self.set_header_indicator('users_number',channel.users.length);
            });
        });
    };
    this.removeChannel = function (channel) {
        self.channels.splice(self.channels.indexOf(channel), 1);
        channel.remove();
    };
    this.getChannelById = function(id){
        for (var i = 0; i < self.channels.length; i++){
            if (self.channels[i].id === id)
                return self.channels[i];
        }
        return null;
    };

    // users interaction functions
    this.addUser = function (user) {
        self.users.push(user);
        self.usersListContainer.append(user.selector.root);
        self.usersCounter.text(self.users.length);
        user.selector.root.click(function(e){
            self.active_element = user;
            self.currentActiveSelector && self.currentActiveSelector.setActive(false);
            self.currentActiveSelector = user.selector;
            self.setHeaderNamePlaceholder(icons['user'],user.selector.name);
            user.load(function(){
                user.selector.setActive(true);
                self.close_flex_panel();
                self.relative_flex_panel = self.flexpanels['user'];
                self.relative_flex_panel.setTarget(user);
                self.reset_header_indicators();
                self.set_header_indicator('apps_number', user.apps.length);
            });
        });
    };
    this.removeUser = function (user) {
        self.users.splice(self.users.indexOf(user), 1);
        user.remove();
    };
    this.getUserById = function(id){
        for (var i = 0; i < self.users.length; i++){
            if (self.users[i].id === id)
                return self.users[i];
        }
        return null;
    };
};

var team_app_search = new function () {
    var self = this;
    this.search = function (q, team_id, callback) {
        postHandler.get(
            '/api/v1/catalog/SearchWebsite',
            {
                'search': q,
                'team_id': team_id
            },
            function () {},
            function (data) {
                if (callback)
                    callback(JSON.parse(data));
            },
            function (data) {
                console.log(data);
                callback(null);
            }
        );
    };
    this.get_app_info = function (app_id, callback) {
        postHandler.get(
            '/api/v1/catalog/GetWebsiteInformation',
            {
                'website_id': app_id
            },
            function () {},
            function (data) {
                callback(JSON.parse(data));
            },
            function (data) {
                console.log(data);
                callback(null);
            }
        );
    }
};

var team_div_creator = {
    simple_app_credentials_line : function (icon, name, type, placeholder) {
        var ret = $('<div class="credentials_line">'+
            '<i class="fa ' + icon + ' icon_handler credentials_type_icon" aria-hidden="true"></i>'+
            '<input placeholder="'+placeholder+'" readonly autocomplete="off" class="credentials_value_input value_input" type="'+type+'" name="'+name+'">'+
            '</div>'
        );
        $('input', ret).one('focus', function () {
            $(this).prop('readonly', false);
            $(this).focus();
        });
        return ret;
    },
    multi_app_input : function (name, type, placeholder) {
        var ret = $('<input readonly placeholder="'+placeholder+'" autocomplete="off" class="credentials_value_input value_input" type="'+type+'" name="'+name+'">');
        ret.one('focus', function () {
            $(this).prop('readonly', false);
            $(this).focus();
        });
        return ret;
    },
    app_selector : function (logoSrc, name) {
        return $('<div class="dropdown_row app_selector">'+
            '<img class="logo" src="'+ logoSrc +'"/>'+
            '<span class="app_name overflow-ellipsis">'+
            name +
            '</span>'+
            '</div>'
        );
    },
    user_selector : function (id, name, fname, lname){
        return $('<div class="user_selector" data-user-id="'+id+'">'+
            '<span class="username text_strong">'+ name +'</span>' +
            ' - ' +
            '<span class="fname">'+ fname +'</span>'+
            ' <span class="lname">'+ lname +'</span>'+
            '</div>');
    },
    simple_user_tag : function (name) {
        return $('<div class="user_token">' +
            '<span class="name_hodler overflow-ellipsis">'+
            name +
            '</span>'+
            '<button class="button_delete action_button button-unstyle">'+
            '<i class="fa fa-times" aria-hidden="true"></i>'+
            '</button>'+
            '</div>');
    }
};

function App_adder_search(root, adder) {
    var self = this;

    this.adder = adder;

    this.app_name_input_holder = root;
    this.app_name_input = $('input', this.app_name_input_holder);
    this.app_name_reset_button = $('.modify_app_button', this.app_name_input_holder);
    this.app_selectors = $('.app_selectors', this.app_name_input_holder);
    this.app_selectors_content = $('.dropdown_content', this.app_selectors);
    this.app_selectors_is_open = false;

    this.app_name_reset_button.click(function () {
        adder.reset();
        self.app_name_input.focus();
    });

    this.init_app_search_listeners = function () {
        self.app_name_input.on('focus',function () {
            if ($(this).val().length > 0){
                self.process_search_for_apps($(this).val());
            }
        });
        this.app_name_input.on('input',function () {
            if ($(this).val().length)
                self.process_search_for_apps($(this).val());
        });
    };
    this.destroy_app_search_listeners = function () {
        self.app_name_input.off();
    };
    this.show_app_search_results = function () {
        self.app_selectors.addClass('show');
        self.app_selectors_is_open = true;
        $(document).bind('click.simple_app_add', function (e) {
            if ($(e.target).closest('.app_name_input_wrapper').length == 0) {
                self.app_selectors.removeClass('show');
                self.app_selectors_is_open = false;
                $(document).unbind('click.simple_app_add');
            }
        });
    };
    this.process_search_for_apps = function (query) {
        if (!self.app_selectors_is_open)
            self.show_app_search_results();
        team_app_search.search(query,self.adder.team_view.team_id,
            function (res) {
                self.app_selectors_content.html('');
                if (!res.length)
                    self.app_selectors_content.append($('<div class="dropdown_row">No results...</div>'));
                res.forEach(function (item) {
                    var tmp = team_div_creator.app_selector(item.logo, item.name);
                    tmp.click(function () {
                        adder.choose_app(item);
                    });
                    self.app_selectors_content.append(tmp);
                });
            });
    };
    this.reset_app_search = function () {
        self.app_selectors.removeClass('show');
        self.app_selectors_content.html('');
        self.app_selectors_is_open = false;
        $(document).unbind('click.simple_app_add');
    };
}

function multiple_app_add_interface(root, team_view){
    var self = this;
    this.root = root;

    this.team_view = team_view;
    this.app_search = new App_adder_search($('.app_name_input_handler', this.root), self);
    this.choosen_app = null;

    this.app_info_div = $('.info_holder', this.root);
    this.app_comment_div = $('.comment_holder', this.root);

    this.logoImg = $('.logo_holder img', this.root);
    this.app_name_input_holder = $('.app_name_input_handler', this.root);
    this.app_name_input = $('input', this.app_name_input_holder);
    this.app_name_modify_button = $('.modify_app_button', this.app_name_input_holder);

    this.password_change_interval_input = $('.password_change_input', this.root);
    this.comment_input = $('.comment_input', this.root);

    this.credentials = $('.credentials', this.root);
    this.user_selectors = $('.user_selectors', this.root);

    this.close_button = $('.add_actions_holder .close_button', this.root);
    this.send_button = $('.add_actions_holder .send_button', this.root);

    this.user_choose_div = $('.user_choose', this.root);

    $('input',this.user_choose_div).focus(function () {
        if (self.user_choose_div.hasClass('list_visible'))
            return;
        self.user_choose_div.addClass('list_visible');
        $(document).bind('click.multiple_app_add',function (e) {
            if ($(e.target).closest('.user_choose').length == 0) {
                self.user_choose_div.removeClass('list_visible');
                $(document).unbind('click.multiple_app_add');
            }
        });
    });
    this.close_button.click(function () {
        team_view.close_active_add_ui();
    });

    this.choose_app = function (app) {
        self.choosen_app = {};
        self.choosen_app.info = app;
        team_app_search.get_app_info(app.single_id,
            function (data) {
                self.app_name_input_holder.addClass('locked');
                self.app_search.destroy_app_search_listeners();
                self.choosen_app.inputs = data.information;
                self.app_info_div.removeClass('hide');
                self.app_comment_div.removeClass('hide');
                self.app_name_input.val(app.name);
                self.logoImg.attr('src', app.logo);
                self.app_search.reset_app_search();
            });
    };

    this.create_credential_line = function (name) {
        var ret =$(
            '<div class="credentials_line">'+
            '<i class="fa fa-user icon_handler credentials_type_icon" aria-hidden="true"></i>'+
            '<div class="inputs_wrapper">'+
            '<div class="choosen_user">'+
            '<span class="value overflow-ellipsis">'+
            name +
            '</span>' +
            '<button class="button-unstyle button_delete">'+
            '<i class="fa fa-times" aria-hidden="true"></i>'+
            '</button>'+
            '</div>'+
            '</div>'+
            '</div>'
        );
        var wrapper = $('.inputs_wrapper', ret);
        self.choosen_app.inputs.forEach(function (item) {
            var input = team_div_creator.multi_app_input(item.name, item.type, item.placeholder);
            wrapper.append(input);
        });
        return ret;
    };
    this.users = [];

    this.reset_user_selectors = function () {
        for (var i = 0; i < self.users.length; i++){
            self.users[i].selector.remove();
            self.users[i].credentials && self.users[i].credentials.remove();
        }
    };
    this.reset = function () {
        self.app_info_div.addClass('hide');
        self.app_comment_div.addClass('hide');
        self.app_name_input_holder.removeClass('locked');
        self.app_search.init_app_search_listeners();
        self.choosen_app = null;
        self.logoImg.attr('src', '/resources/icons/app_icon.svg');
        self.reset_user_selectors();
        self.setup_user_selectors();
    };
    this.open = function () {
        self.app_name_input.val('');
        self.reset();
        self.root.addClass('active');
    };
    this.close = function () {
        self.root.removeClass('active');
        self.app_search.destroy_app_search_listeners();
        self.reset_user_selectors();
    };
    this.setup_user_selector = function (user) {
        var tmp = {};
        tmp.selector = team_div_creator.user_selector(user.id, user.username, user.first_name, user.last_name);
        tmp.isSelected = false;
        tmp.credentials = null;
        tmp.selector.click(function(e){
            if (tmp.isSelected)
                return;
            tmp.isSelected = true;
            tmp.selector.addClass('selected');
            tmp.credentials = self.create_credential_line(user.username);
            $('.button_delete', tmp.credentials).one('click', function (e) {
                tmp.selector.removeClass('selected');
                tmp.isSelected = false;
                tmp.credentials.remove();
                tmp.credentials = null;
            });
            self.credentials.append(tmp.credentials);
        });
        self.user_selectors.append(tmp.selector);
        self.users.push(tmp);
    };

    this.setup_user_selectors = function(){
        for (var i = 0; i < self.team_view.active_element.users.length; i++){
            self.setup_user_selector(self.team_view.active_element.users[i]);
        }
    };
}

function link_app_add_interface(root, team_view) {
    var self = this;

    this.root = root;
    this.team_view = team_view;
    this.user_selectors = new Simple_user_selectors($('.user_adding_container', this.root));

    this.app_info_div = $('.info_holder', this.root);
    this.app_comment_div = $('.comment_holder', this.root);

    this.logoImg = $('.logo_holder img', this.root);
    this.app_name_input_holder = $('.app_name_input_handler', this.root);
    this.app_name_input = $('input', this.app_name_input_holder);

    this.url_input = $('input[name=url]', this.root);

    this.comment_input = $('.comment_input', this.root);

    this.close_button = $('.add_actions_holder .close_button', this.root);
    this.send_button = $('.add_actions_holder .send_button', this.root);

    this.close_button.click(function () {
        self.team_view.close_active_add_ui();
    });

    $(self.tags_container_input).focus(function () {
        if (self.adding_container.hasClass('list_visible'))
            return;
        self.adding_container.addClass('list_visible');
        $(document).bind('click.simple_app_add',function (e) {
            if ($(e.target).closest('.user_adding_container').length == 0) {
                self.adding_container.removeClass('list_visible');
                $(document).unbind('click.simple_app_add');
            }
        });
    });
    this.reset = function () {
        self.app_name_input.val('');
        self.url_input.val('');
        self.logoImg.attr('src', '/resources/icons/app_icon.svg');
        self.user_selectors.reset_user_selectors();
        self.user_selectors.setup_user_selectors(self.team_view.active_element.users);
    };
    this.open = function () {
        self.reset();
        self.root.addClass('active');
    };
    this.close = function () {
        self.root.removeClass('active');
        self.user_selectors.reset_user_selectors();
    };
}

function Simple_user_selectors(root){
    var self = this;

    this.root = root;
    this.adding_container = root;
    this.tags_container_input = $('.true_input', this.adding_container);
    this.user_selectors = $('.user_selectors', this.root);

    this.users = [];

    $(self.tags_container_input).focus(function () {
        if (self.adding_container.hasClass('list_visible'))
            return;
        self.adding_container.addClass('list_visible');
        $(document).bind('click.simple_app_add',function (e) {
            if ($(e.target).closest('.user_adding_container').length == 0) {
                self.adding_container.removeClass('list_visible');
                $(document).unbind('click.simple_app_add');
            }
        });
    });

    this.reset_user_selectors = function () {
        for (var i = 0; i < self.users.length; i++){
            self.users[i].selector.remove();
            self.users[i].credentials && self.users[i].credentials.remove();
        }
    };
    this.setup_user_selector = function (user) {
        var tmp = {};
        tmp.selector = team_div_creator.user_selector(user.id, user.username, user.first_name, user.last_name);
        tmp.isSelected = false;
        tmp.credentials = null;
        tmp.selector.click(function(e){
            if (tmp.isSelected)
                return;
            tmp.isSelected = true;
            tmp.selector.addClass('selected');
            tmp.credentials = team_div_creator.simple_user_tag(user.username);
            $('.button_delete', tmp.credentials).one('click', function (e) {
                tmp.selector.removeClass('selected');
                tmp.isSelected = false;
                tmp.credentials.remove();
                tmp.credentials = null;
            });
            tmp.credentials.insertBefore(self.tags_container_input);
        });
        self.user_selectors.append(tmp.selector);
        self.users.push(tmp);
    };
    this.setup_user_selectors = function(users){
        for (var i = 0; i < users.length; i++){
            self.setup_user_selector(users[i]);
        }
    };
}

function simple_app_add_interface(root, team_view){
    var self = this;
    this.root = root;

    this.team_view = team_view;
    this.app_search = new App_adder_search($('.app_name_input_handler', this.root), self);
    this.user_selectors = new Simple_user_selectors($('.user_adding_container', this.root));
    this.choosen_app = null;
    this.inputs = [];

    this.app_info_div = $('.info_holder', this.root);
    this.app_comment_div = $('.comment_holder', this.root);

    this.logoImg = $('.logo_holder img', this.root);
    this.app_name_input_holder = $('.app_name_input_handler', this.root);
    this.app_name_input = $('input', this.app_name_input_holder);

    this.password_change_interval_input = $('.password_change_input', this.root);
    this.comment_input = $('.comment_input', this.root);

    this.credentials_div = $('.credentials', this.root);

    this.close_button = $('.add_actions_holder .close_button', this.root);
    this.send_button = $('.add_actions_holder .send_button', this.root);

    this.close_button.click(function () {
        self.team_view.close_active_add_ui();
    });

    this.choose_app = function (app) {
        self.choosen_app = {};
        self.choosen_app.info = app;
        team_app_search.get_app_info(app.single_id,
            function (data) {
                self.app_name_input_holder.addClass('locked');
                self.app_search.destroy_app_search_listeners();
                data.forEach(function (item) {
                    var tmp = team_div_creator.simple_app_credentials_line(item.placeholderIcon, item.name, item.type, item.placeholder);
                    self.inputs.push(tmp);
                    self.credentials_div.append(tmp);
                });
                self.choosen_app.inputs = data;
                self.app_info_div.removeClass('hide');
                self.app_comment_div.removeClass('hide');
                self.app_name_input.val(app.name);
                self.logoImg.attr('src', app.logo);
                self.app_search.reset_app_search();
            });
    };

    this.reset_credential_inputs = function () {
        self.inputs.forEach(function (item) {
            item.remove();
        });
        self.inputs = [];
    };
    this.reset = function () {
        self.app_info_div.addClass('hide');
        self.app_comment_div.addClass('hide');
        self.app_name_input_holder.removeClass('locked');
        self.app_search.init_app_search_listeners();
        self.reset_credential_inputs();
        self.choosen_app = null;
        self.logoImg.attr('src', '/resources/icons/app_icon.svg');
        self.user_selectors.reset_user_selectors();
        self.user_selectors.setup_user_selectors(self.team_view.active_element.users);
    };
    this.open = function () {
        self.app_name_input.val('');
        self.reset();
        self.root.addClass('active');
    };
    this.close = function () {
        self.root.removeClass('active');
        self.app_search.destroy_app_search_listeners();
        self.reset_credential_inputs();
        self.user_selectors.reset_user_selectors();
    };
}

function Flex_panel(root){
    var self = this;
    this.root = root;
    this.isOpen = false;
    this.close_button = $('.button_close_flexpanel', this.root);
    this.target = null;
    this.isSetup = false;

    this.close = function () {
        if (self.isOpen){
            self.isOpen = false;
            self.root.removeClass('active');
        }
    };
    this.open = function () {
        if (!self.isOpen){
            self.isOpen = true;
            self.root.addClass('active');
        }
    };
    this.setTarget = function (target) {
        if (self.target !== target)
            self.isSetup = false;
        self.target = target;
    };
}

function Flex_user_panel(root){
    Flex_panel.apply(this, arguments);
    var self = this;
    this.fname_holder = $('.name_holder .firstname', this.root);
    this.lname_holder = $('.name_holder .lastname', this.root);
    this.username_holder = $('.username_holder .username', this.root);
    this.email_holder = $('.email_holder .email', this.root);
    this.role_holder = $('.role_holder .role', this.root);
    this.join_date = $('.join_date_holder .join_date', this.root);
    this.leave_date = $('.leave_date_holder .leave_date', this.root);
    this.teams = $('.teams_holder .teams_list', this.root);
    this.using_apps = $('.using_apps_holder .using_apps', this.root);
    this.delete_button = $('.team_user_delete_button', this.root);

    this.setup = function () {
        if (self.target == null)
            return;
        self.fname_holder.text(self.target.first_name);
        self.lname_holder.text(self.target.last_name);
        self.username_holder.text(self.target.username);
        self.email_holder.text(self.target.email);
        self.role_holder.text(self.target.role);
        self.join_date.text(self.target.arrivalDate);
        self.leave_date.text(self.target.departureDate);
        //print teams
        self.using_apps.text(self.target.apps.length);
    };
}

function Flex_channel_panel(root){
    Flex_panel.apply(this, arguments);
    var self = this;
    this.name_handler = $('.name_holder .name', this.root);
    this.purpose_holder = $('.purpose_holder .purpose', this.root);
    this.number_apps_holder = $('.number_apps', this.root);
    this.user_list = $('.members_holder .members_list', this.root);
    this.delete_button = $('.team_delete_button', this.root);

    this.createMemberDiv = function (name) {
        return $('<span class="member_name_holder">' +
            '<span class="icon_wrapper">' +
            '<i class="fa fa-user" aria-hidden="true"></i>' +
            '</span>' +
            '<span class="member_name">' +
            name +
            '</span>' +
            '</span>');
    };
    this.setup = function (){
        this.user_list.html('');
        self.name_handler.text(self.target.name);
        self.purpose_holder.text(self.target.purpose);
        self.number_apps_holder.text(self.target.apps.length);
        for (var i = 0; i < self.target.users.length; i++){
            self.user_list.append(self.createMemberDiv(self.target.users[i].username));
        }
        self.delete_button.off();
        self.delete_button.click(function () {
            alert('wanna supp ' + self.target.name);
        });
    };
}

var teams = new function(){
    var self = this;
    this.teams_selectors = null;
    this.teams_infos = [];

    this.load = function () {
        self.teams_selectors = $('header #team_selectors');
        postHandler.get(
            '/api/v1/teams/GetTeams',
            {},
            function(){},
            function(data){
                var jData = JSON.parse(data);
                for (var i = 0; i < jData.length; i++){
                    self.teams_infos.push(new Team_info(jData[i].id, jData[i].name));
                }
                for (var i = 0; i < self.teams_infos.length; i++){
                    self.teams_selectors.append(self.teams_infos[i].selector);
                }
            },
            function(data){}
        );
    };
};

window.addEventListener('load',function() {
    teams.load();
});

function Team_info(id, name){
    var self = this;
    this.id = id;
    this.name = name;
    this.selector = $('<div class="team_select menu_row" data-team-id="' + id + '">' +
        '<div class="icon">' +
        '<i class="fa fa-superpowers" aria-hidden="true"></i>' +
        '</div>'+
        '<span class="team_name">' +
        name +
        '</span>' +
        '</div>');
    this.selector.click(function () {
        team_view.load(self.id);
    });
}

var icons = {
    channel: '<i class="fa fa-users" aria-hidden="true"></i>',
    user: '<i class="fa fa-user" aria-hidden="true"></i>'
};

function Team_channel(id, name){
    var self = this;
    this.id = id;
    this.name = name;
    this.purpose = null;
    this.apps = [];
    this.users = [];

    this.selector = new Team_channel_selector(id, name);

    this.remove = function () {
        self.selector.remove();
    };

    this.load = function (callback) {
        self.users = [];
        self.apps = [];
        postHandler.get(
            '/api/v1/teams/GetChannel',
            {
                'team_id': team_view.team_id,
                'channel_id': self.id
            },
            function () {},
            function (data) {
                var jData = JSON.parse(data);
                for (var i = 0; i < jData['userIds'].length; i++){
                    self.users.push(team_view.getUserById(jData['userIds'][i]));
                }
                self.apps = jData['apps'];
                self.purpose = jData['desc'];
                self.id = jData['id'];
                self.name = jData['name'];
                self.selector.setName(self.name);
                callback();
            },
            function (data) {
            }
        );
    };
}

function Team_user(id, name, fname, lname, email, role){
    var self = this;
    this.first_name = fname;
    this.last_name = lname;
    this.username = name;
    this.email = email;
    this.role = role;
    this.apps = [];
    this.channels = [];
    this.users = [self];
    this.arrivalDate = null;
    this.departureDate = null;
    this.id = id;

    this.selector = new Team_user_selector(id, name);

    this.remove = function () {
      self.selector.remove();
    };

    this.load = function (callback) {
        postHandler.get(
          '/api/v1/teams/GetTeamUser',
            {
                'team_id': team_view.team_id,
                'teamuser_id': self.id
            },
            function () {},
            function (data) {
                var jData = JSON.parse(data);
                self.first_name = jData['firstName'];
                self.last_name = jData['lastName'];
                self.role = jData['role'];
                self.email = jData['email'];
                self.username = jData['username'];
                self.id = jData['id'];
                self.arrivalDate = jData['arrivalDate'];
                self.selector.setName(self.username);
                callback();
            },
            function () {}
        );
    };
}

function Team_channel_selector(id, name){
    var self = this;
    this.id = id;
    this.name = name;
    this.isActive = false;

    this.root = $('<li class="section-list-item channel">' +
        '<div class="primary_action channel_name">' +
        '<i class="fa fa-users prefix"></i>' +
        '<span class="overflow-ellipsis"></span>' +
        '</div>' +
        '</li>');

    this.setActive = function (state) {
        self.isActive = state;
        state && this.root.addClass('active') || this.root.removeClass('active');
    };

    this.remove = function () {
        self.root.remove();
    };

    this.setName = function(name){
        self.name = name;
        $('span', self.root).text(self.name);
    };

    this.setName(this.name);
}

function Team_user_selector(id, username){
    var self = this;
    this.name = username;
    this.id = id;
    this.isActive = false;

    this.root = $('<li class="section-list-item member">' +
        '<div class="primary_action member_name">' +
        '<i class="fa fa-user prefix"></i>' +
        '<span class="overflow-ellipsis"></span>' +
        '</div>' +
        '</li>');

    this.setActive = function (state) {
        self.isActive = state;
        state && this.root.addClass('active') || this.root.removeClass('active');
    };

    this.setName = function(name){
        self.name = name;
        $('span', self.root).text(self.name);
    };

    this.remove = function () {
        self.root.remove();
    };
    this.setName(this.name);
}

team_view.load(2);