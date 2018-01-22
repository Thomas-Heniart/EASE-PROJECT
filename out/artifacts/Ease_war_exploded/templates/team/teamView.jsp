<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div class="team_view" id="team_view">
    <div class="client_channels_container">
        <div id="team_menu">
            <div class="team_name_container">
            <span id="team_name">
            </span>
            </div>
            <div class="team_client_user">
                <i class="fa fa-square" id="team_client_user_presence" aria-hidden="true"></i>
                <span id="team_client_user_name">
            </span>
            </div>
        </div>
        <div id="col_channels">
            <div id="col_channels_scroller">
                <div class="section-holder" id="team_channels">
                    <button class="heading-button button-unstyle" id="new_channel_button">
                        <i class="fa fa-plus-square-o" aria-hidden="true"></i>
                    </button>
                    <div class="section-header">
                <span>
                    Teams
                </span>
                        <span class="header-count">(<span id="channel_header_count">-</span>)</span>
                    </div>
                    <div class="section-list">
                    </div>
                </div>
                <div class="section-holder" id="team_members">
                    <button class="heading-button button-unstyle" id="new_member_button">
                        <i class="fa fa-plus-square-o" aria-hidden="true"></i>
                    </button>
                    <div class="section-header">
                <span>
                    Members
                </span>
                        <span class="header-count">(<span id="members_header_count">-</span>)</span>
                    </div>
                    <div class="section-list">
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="client_main_container">
        <header id="client_header">
            <div class="channel_header">
                <div class="tab_header">
                    <div class="channel_title">
                        <div id="channel_name_container" class="channel_name_container">
                            <span id="channel_name" class="channel_name">
                                <div class="icon_wrapper">
                                </div>
                                <div class="name">
                                </div>
                            </span>
                        </div>
                        <div class="channel_header_info" >
                            <div class="channel_header_info_item" id="apps_number">
                                <button class="button-unstyle">
                                    <span class="ease_tip">

                                    </span>
                                    <i class="fa fa-share-alt-square" aria-hidden="true"></i>
                                    <span class="value">-</span>
                                </button>
                            </div>
                            <div class="channel_header_info_item" id="users_number">
                                <button class="button-unstyle">
                                    <i class="fa fa-user-o" aria-hidden="true"></i>
                                    <span class="value">-</span>
                                </button>
                            </div>
                        </div>
                    </div>
                    <div class="channel_title_info">
                        <button class="button-unstyle ease_tipped" id="open_card_button">
                            <span class="ease_tip">Card</span>
                            <i class="fa fa-id-card-o" aria-hidden="true"></i>
                        </button>
                    </div>
                </div>
            </div>
        </header>
        <div class="team_client_body">
            <div id="col_main">
                <div class="add_actions_container" id="app_add_actions">
                    <div class="add_buttons_wrapper">
                        <button class="button-unstyle" id="simple_app_add_button">
                            <div class="icon_holder">
                                <i class="fa fa-square" aria-hidden="true"></i>
                            </div>
                            <span class="title">
                        Simple app
                    </span>
                        </button>
                        <button class="button-unstyle" id="multiple_app_add_button">
                            <div class="icon_holder">
                                <i class="fa fa-sitemap" aria-hidden="true"></i>
                            </div>
                            <span class="title">
                        Per user account
                    </span>
                        </button>
                        <button class="button-unstyle" id="link_app_add_button">
                            <div class="icon_holder">
                                <i class="fa fa-link" aria-hidden="true"></i>
                            </div>
                            <span class="title">
                        Link url
                    </span>
                        </button>
                    </div>
                    <%@ include file="/templates/team/team_app_adders.html" %>
                </div>
                <div class="apps_container" >
                    <div class="apps_scroller_div" id="team_apps_container">
                        <div class="team_app_holder">
                            <div class="team_app_actions_holder">
                                <button class="button-unstyle team_app_pin">
                                    <i class="fa fa-thumb-tack" aria-hidden="true"></i>
                                </button>
                                <button class="button-unstyle team_app_edit">
                                    <i class="fa fa-pencil" aria-hidden="true"></i>
                                </button>
                                <button class="button-unstyle team_app_delete">
                                    <i class="fa fa-trash" aria-hidden="true"></i>
                                </button>
                            </div>
                            <div class="team_app_edit_actions">
                                <button class="button-unstyle confirm">
                                    <i class="fa fa-check" aria-hidden="true"></i>
                                </button>
                                <button class="button-unstyle cancel">
                                    <i class="fa fa-times" aria-hidden="true"></i>
                                </button>
                            </div>
                            <div class="team_app_sender_info">
                            <span>
                                <span class="team_app_sender_type_icon">
                                    <i class="fa fa-user" aria-hidden="true"></i>
                                </span>
                                <span class="team_app_sender_name">
                                    Victor
                                </span>
                            </span>
                                shared on
                                <span class="team_app_send_date">
                                April 3rd, 10:32 AM
                            </span>
                            </div>
                            <div class="team_app multiple_accounts_app">
                                <div class="name_holder">
                                <span class="name_value value">
                                    Mailchimp
                                </span>
                                    <input class="name_input value_input" placeholder="App name..."/>
                                </div>
                                <div class="info_holder">
                                    <div class="info">
                                        <div class="logo_holder">
                                            <img src="/resources/websites/Mailchimp/logo.png"/>
                                        </div>
                                        <div class="credentials_holder">
                                            <div class="credentials">
                                                <div class="credentials_line" data-type="email">
                                                    <div class="credentials_type_icon">
                                                        <i class="fa fa-envelope-o" aria-hidden="true"></i>
                                                    </div>
                                                    <div class="credentials_value_holder">
                                                    <span class="credentials_value value">
                                                        victor@ease.space
                                                    </span>
                                                        <input autocomplete="off"
                                                               class="credentials_value_input value_input" type="email"
                                                               name="email"/>
                                                    </div>
                                                </div>
                                                <div class="credentials_line" data-type="email">
                                                    <div class="credentials_type_icon">
                                                        <i class="fa fa-lock" aria-hidden="true"></i>
                                                    </div>
                                                    <div class="credentials_value_holder">
                                                    <span class="credentials_value value">
                                                        ••••••••
                                                    </span>
                                                        <input placeholder="Leave blank to not change"
                                                               class="credentials_value_input value_input" type="email"
                                                               name="email"/>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="password_change_remind">
                                                <div class="password_change_icon">
                                                    <i class="fa fa-refresh" aria-hidden="true"></i>
                                                </div>
                                                <div class="password_change_info">
                                                <span class="password_change_value value">
                                                    never
                                                </span>
                                                    <select name="password_change_interval"
                                                            class="password_change_input value_input">
                                                        <option value="0">never</option>
                                                        <option value="1">1 months</option>
                                                        <option value="3">3 months</option>
                                                        <option value="6">6 months</option>
                                                        <option value="12">12 months</option>
                                                    </select>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="sharing_info display_flex full_flex flex_direction_column">
                                        <div class="receivers_wrapper full_flex">
                                            <div class="receiver_wrapper">
                                                <div class="receiver">
                                                    <span class="receiver_name">Alexandra</span>
                                                    <span class="view_password_perm">
                                            <i class="fa fa-eye" aria-hidden="true"></i>
                                        </span>
                                                    <span class="password_modification_counter">
                                            <i class="fa fa-unlock-alt" aria-hidden="true"></i>
                                        </span>
                                                </div>
                                                <div class="credentials">
                                                    <div class="credential_container">
                                                        <i class="fa fa-user-o icon_wrapper" aria-hidden="true"></i>
                                                        <span class="value">alexandra@gmail.com</span>
                                                        <input class="value_input input_unstyle" placeholder="Email" type="text" name="user_name">
                                                    </div>
                                                    <div class="credential_container">
                                                        <i class="fa fa-lock icon_wrapper" aria-hidden="true"></i>
                                                        <span class="value">********</span>
                                                        <input class="value_input input_unstyle" placeholder="Password" type="text" name="user_name">
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="receiver_wrapper">
                                                <div class="receiver">
                                                    <span class="receiver_name">Alexandra</span>
                                                    <span class="view_password_perm">
                                            <i class="fa fa-eye" aria-hidden="true"></i>
                                        </span>
                                                    <span class="password_modification_counter">
                                            <i class="fa fa-unlock-alt" aria-hidden="true"></i>
                                        </span>
                                                </div>
                                                <div class="credentials">
                                                    <div class="credential_container">
                                                        <i class="fa fa-user-o icon_wrapper" aria-hidden="true"></i>
                                                        <span class="value">alexandra@gmail.com</span>
                                                        <input class="value_input input_unstyle" placeholder="Email" type="text" name="user_name">
                                                    </div>
                                                    <div class="credential_container">
                                                        <i class="fa fa-lock icon_wrapper" aria-hidden="true"></i>
                                                        <span class="value">********</span>
                                                        <input class="value_input input_unstyle" placeholder="Password" type="text" name="user_name">
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="receiver_wrapper">
                                                <div class="receiver">
                                                    <span class="receiver_name">Alexandra</span>
                                                    <span class="view_password_perm">
                                            <i class="fa fa-eye" aria-hidden="true"></i>
                                        </span>
                                                    <span class="password_modification_counter">
                                            <i class="fa fa-unlock-alt" aria-hidden="true"></i>
                                        </span>
                                                </div>
                                                <div class="credentials">
                                                    <div class="credential_container">
                                                        <i class="fa fa-user-o icon_wrapper" aria-hidden="true"></i>
                                                        <span class="value">alexandra@gmail.com</span>
                                                        <input class="value_input input_unstyle" placeholder="Email" type="text" name="user_name">
                                                    </div>
                                                    <div class="credential_container">
                                                        <i class="fa fa-lock icon_wrapper" aria-hidden="true"></i>
                                                        <span class="value">********</span>
                                                        <input class="value_input input_unstyle" placeholder="Password" type="text" name="user_name">
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="user_adding_container">
                                            <div class="receivers_wrapper">
                                                    <div class="receiver_wrapper">
                                                        <div class="receiver_container">
                                                        <div class="receiver">
                                                            <span class="receiver_name">Alexandra</span>
                                                            <span class="view_password_perm">
                                            <i class="fa fa-eye" aria-hidden="true"></i>
                                        </span>
                                                            <span class="password_modification_counter">
                                            <i class="fa fa-unlock-alt" aria-hidden="true"></i>
                                        </span>
                                                        </div>
                                                            <input class="value_input user_name_input" type="text" placeholder="Username" name="user_name">
                                                        </div>
                                                        <div class="credentials">
                                                            <div class="credential_container">
                                                                <i class="fa fa-user-o icon_wrapper" aria-hidden="true"></i>
                                                                <span class="value">alexandra@gmail.com</span>
                                                                <input class="value_input input_unstyle" placeholder="Email" type="text" name="user_name">
                                                            </div>
                                                            <div class="credential_container">
                                                                <i class="fa fa-lock icon_wrapper" aria-hidden="true"></i>
                                                                <span class="value">********</span>
                                                                <input class="value_input input_unstyle" placeholder="Password" type="text" name="user_name">
                                                            </div>
                                                        </div>
                                                        <button class="button-unstyle action_button delete_button">
                                                            <i class="fa fa-times icon_wrapper" aria-hidden="true"></i>
                                                        </button>
                                                    </div>
                                            </div>
                                            <button class="button-unstyle add_field_button action_button">
                                                Add another field
                                            </button>
                                        </div>
                                    </div>
                                </div>
                                <div class="comment_holder">
                                    <div class="comment_icon">
                                        <i class="fa fa-sticky-note-o" aria-hidden="true"></i>
                                    </div>
                                    <div class="comment">
                                    <span class="comment_value value">
                                        Pour ce qui ont besoin d'utiliser mon compte pour envoyer des emails marketing à nos abonnés, ca se passe sur Mailchimp !
                                    </span>
                                        <textarea class="comment_input value_input"
                                                  placeholder="Your comment..."></textarea>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="team_app_holder">
                            <div class="team_app_actions_holder">
                                <button class="button-unstyle team_app_pin">
                                    <i class="fa fa-thumb-tack" aria-hidden="true"></i>
                                </button>
                                <button class="button-unstyle team_app_edit">
                                    <i class="fa fa-pencil" aria-hidden="true"></i>
                                </button>
                                <button class="button-unstyle team_app_delete">
                                    <i class="fa fa-trash" aria-hidden="true"></i>
                                </button>
                            </div>
                            <div class="team_app_sender_info">
                            <span>
                                <span class="team_app_sender_type_icon">
                                    <i class="fa fa-user" aria-hidden="true"></i>
                                </span>
                                <span class="team_app_sender_name">
                                    Victor
                                </span>
                            </span>
                                shared on
                                <span class="team_app_send_date">
                                April 3rd, 10:32 AM
                            </span>
                            </div>
                            <div class="team_app">
                                <div class="name_holder">
                                <span class="name_value">
                                    Youtube
                                </span>
                                </div>
                                <div class="info_holder">
                                    <div class="info">
                                        <div class="logo_holder">
                                            <img src="/resources/websites/Youtube/logo.png"/>
                                        </div>
                                        <div class="credentials_holder">
                                            <div class="credentials">
                                                <div class="credentials_line" data-type="email">
                                                    <div class="credentials_type_icon">
                                                        <i class="fa fa-envelope-o" aria-hidden="true"></i>
                                                    </div>
                                                    <div class="credentials_value_holder">
                                                    <span class="credentials_value">
                                                        victor@ease.space
                                                    </span>
                                                    </div>
                                                </div>
                                                <div class="credentials_line" data-type="email">
                                                    <div class="credentials_type_icon">
                                                        <i class="fa fa-lock" aria-hidden="true"></i>
                                                    </div>
                                                    <div class="credentials_value_holder">
                                                    <span class="credentials_value">
                                                        ••••••••
                                                    </span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="password_change_remind">
                                                <div class="password_change_icon">
                                                    <i class="fa fa-refresh" aria-hidden="true"></i>
                                                </div>
                                                <div class="password_change_info">
                                                        <span class="password_change_value">
                                                            never
                                                        </span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="sharing_info">
                                        <div class="receivers_wrapper full_flex">
                                            <div class="receiver">
                                                <span class="receiver_name">Alexandra</span>
                                                <span class="view_password_perm">
                                            <i class="fa fa-eye" aria-hidden="true"></i>
                                        </span>
                                                <span class="password_modification_counter">
                                            <i class="fa fa-unlock-alt" aria-hidden="true"></i>
                                        </span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="comment_holder">
                                    <div class="comment_icon">
                                        <i class="fa fa-sticky-note-o" aria-hidden="true"></i>
                                    </div>
                                    <div class="comment">
                                    <span>
                                        Pour ce qui ont besoin d'utiliser mon compte pour envoyer des emails marketing à nos abonnés, ca se passe sur Mailchimp !
                                    </span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div id="col_flex">
                <div id="flex_contents">
                    <div class="flex_contents_panel" id="team_tab">
                        <div class="tab_heading">
                            <div class="heading_row">
                            <span class="heading_text">
                                Team's information
                            </span>
                                <button class="button-unstyle button_close_flexpanel">
                                    <i class="fa fa-times" aria-hidden="true"></i>
                                </button>
                            </div>
                        </div>
                        <div class="tab_content_body">
                            <div class="tab_content_row">
                                <h2 class="name_holder">
                                    <strong class="name">Marketing</strong>
                                </h2>
                            </div>
                            <div class="tab_content_row">
                               <span class="purpose_holder">
                                <strong>Purpose: </strong>
                                <span class="purpose">
                                    cette chanel est faite pour tous les srab qui font du sale
                                    </span>
                                </span>
                            </div>
                            <div class="tab_content_row">
                                    <span class="number_apps_holder">
                                        <strong>Shared apps: </strong>
                                        <span class="number_apps">
                                            38
                                        </span>
                                    </span>
                            </div>
                            <div class="tab_content_row">
                                <div class="members_holder">
                                    <strong class="heading">Members:</strong>
                                    <div class="members_list">
                                    <span class="member_name_holder">
                                        <span class="icon_wrapper">
                                            <i class="fa fa-user" aria-hidden="true"></i>
                                        </span>
                                        <span class="member_name">
                                            general
                                        </span>
                                    </span>
                                        <span class="member_name_holder">
                                        <span class="icon_wrapper">
                                            <i class="fa fa-user" aria-hidden="true"></i>
                                        </span>
                                        <span class="member_name">
                                            market&com
                                        </span>
                                    </span>
                                    </div>
                                </div>
                            </div>
                            <div class="tab_content_row">
                                <button class="button-unstyle team_delete_button">
                                    <u>Delete team</u>
                                </button>
                            </div>
                        </div>
                    </div>
                    <div class="flex_contents_panel" id="team_user_tab">
                        <div class="tab_heading">
                            <div class="heading_row">
                            <span class="heading_text">
                                User's information
                            </span>
                                <button class="button-unstyle button_close_flexpanel">
                                    <i class="fa fa-times" aria-hidden="true"></i>
                                </button>
                            </div>
                        </div>
                        <div class="tab_content_body">
                            <div class="tab_content_row">
                                <h2 class="name_holder">
                                    <strong class="firstname">Victor</strong>
                                    <strong class="lastname">Nivet</strong>
                                </h2>
                            </div>
                            <div class="tab_content_row">
                            <span class="username_holder">
                                @<span class="username">victor</span>
                            </span>
                            </div>
                            <div class="tab_content_row">
                            <span class="email_holder">
                                <strong>Email: </strong>
                                <span class="email">
                                    victor@ease.space
                                </span>
                                </span>
                            </div>
                            <div class="tab_content_row">
                            <span class="role_holder">
                                <strong>Role: </strong>
                                <span class="role">Member</span>
                            </span>
                            </div>
                            <div class="tab_content_row">
                            <span class="join_date_holder">
                                <strong>First connection:</strong>
                                <span class="join_date">
                                    22/02/2017
                                </span>
                            </span>
                            </div>
                            <div class="tab_content_row">
                            <span class="leave_date_holder">
                                <strong>Departure planned:</strong>
                                <span class="leave_date">
                                    not set up
                                </span>
                            </span>
                            </div>
                            <div class="tab_content_row">
                                <div class="teams_holder">
                                    <strong class="heading">Teams:</strong>
                                    <div class="teams_list">
                                    <span class="team_name_holder">
                                        #
                                        <span class="team_name">
                                            general
                                        </span>
                                    </span>
                                        <span class="team_name_holder">
                                        #
                                        <span class="team_name">
                                            market&com
                                        </span>
                                    </span>
                                    </div>
                                </div>
                            </div>
                            <div class="tab_content_row">
                            <span class="using_apps_holder">
                                <strong>Apps used:</strong>
                                <span class="using_apps">
                                    24
                                </span>
                            </span>
                            </div>
                            <div class="tab_content_row">
                                <button class="button-unstyle team_user_delete_button">
                                    <u>Desactivate account</u>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="item_select_box" style="display: none">
    <div class="item">
        <div class="icon_wrapper">
            <i class="fa fa-user" aria-hidden="true"></i>
        </div>
        <span class="item_value">
            Mojo
        </span>
    </div>
    <div class="item">
        <div class="icon_wrapper">
            <i class="fa fa-user" aria-hidden="true"></i>
        </div>
        <span class="item_value">
            Mojo
        </span>
    </div>
    <div class="item">
        <div class="icon_wrapper">
            <i class="fa fa-user" aria-hidden="true"></i>
        </div>
        <span class="item_value">
            Mojo
        </span>
    </div>
    <div class="item">
        <div class="icon_wrapper">
            <i class="fa fa-user" aria-hidden="true"></i>
        </div>
        <span class="item_value">
            Mojo
        </span>
    </div>
    <div class="item">
        <div class="icon_wrapper">
            <i class="fa fa-user" aria-hidden="true"></i>
        </div>
        <span class="item_value">
            Mojo
        </span>
    </div>
    <div class="item">
        <div class="icon_wrapper">
            <i class="fa fa-user" aria-hidden="true"></i>
        </div>
        <span class="item_value">
            Mojo
        </span>
    </div>
    <div class="item">
        <div class="icon_wrapper">
            <i class="fa fa-user" aria-hidden="true"></i>
        </div>
        <span class="item_value">
            Mojo
        </span>
    </div>
    <div class="item">
        <div class="icon_wrapper">
            <i class="fa fa-user" aria-hidden="true"></i>
        </div>
        <span class="item_value">
            Mojo
        </span>
    </div>
</div>
<%@ include file="/templates/team/user_add_modal.html" %>
<!--<div class="profile_chooser_wrapper menu">
    <div class="popover"></div>
    <div class="menu_content">
        <div class="menu_items_scroller">
            <div class="menu_body">
                <header>
                    Append to :
                </header>
                <div class="main_body">
                    <div class="profile_name_holder overflow-ellipsis">
                        <div class="icon_wrapper">
                            <i class="fa fa-th-large" aria-hidden="true"></i>
                        </div>
                        <div class="profile_name">
                            Perso
                        </div>
                    </div>
                    <div class="profile_name_holder overflow-ellipsis">
                        <div class="icon_wrapper">
                            <i class="fa fa-th-large" aria-hidden="true"></i>
                        </div>
                        <div class="profile_name">
                            Boite
                        </div>
                    </div>
                    <div class="profile_name_holder selected overflow-ellipsis">
                        <div class="icon_wrapper">
                            <i class="fa fa-ban" aria-hidden="true"></i>
                        </div>
                        <div class="profile_name">
                            No profile
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>-->