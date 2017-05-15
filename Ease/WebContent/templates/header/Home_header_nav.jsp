<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.Ease.Dashboard.User.User"%>
<%
    String UserName = ((User) (session.getAttribute("user"))).getFirstName();
%>
<nav class="nav display_flex">
    <button class="button-unstyle" id="settings_button">
        <span><%=UserName%></span> (Click to modify)
    </button>
    <button class="button-unstyle" id="logout_button">
        <img src="/resources/icons/logout.svg">
        <div class="menu menu_arrow display_flex flex_direction_column">
            <div class="menu_row" id="simple_logout">
                Logout from Ease
            </div>
            <div class="menu_row" id="general_logout">
                Logout from all apps
            </div>
        </div>
    </button>
    <button class="button-unstyle notify" id="notification_button">
        <img src="/resources/icons/notification.svg">
    </button>
    <button class="button-unstyle" id="team_button">
        <img src="/resources/icons/users.svg">
        <div class="menu menu_arrow display_flex flex_direction_column">
            <div class="menu_row teams_container" id="team_selectors">
            </div>
            <div class="menu_row display_flex align_items_center" id="team_adder">
                <i class="fa fa-plus-square icon" aria-hidden="true"></i>
                Create a new team...
            </div>
        </div>
    </button>
    <button class="button-unstyle action_button" id="catalog_button">
        <img src="/resources/icons/plus.svg">
    </button>
</nav>