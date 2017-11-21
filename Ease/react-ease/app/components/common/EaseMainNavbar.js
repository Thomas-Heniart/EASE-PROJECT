import React from 'react';
import {fetchNotifications, validateNotification} from "../../actions/notificationsActions";
import {checkForNewNotifications} from "../../utils/helperFunctions";
import {NavLink, withRouter} from "react-router-dom";
import {processLogout} from "../../actions/commonActions";
import {connect} from "react-redux";
import {Dropdown, Icon, Menu, Popup} from 'semantic-ui-react';

@connect(store => ({
  teams: store.teams
}))
class TeamsList extends React.Component {
  constructor(props) {
    super(props);
  }
  render() {
    return (
        <Dropdown icon={<Icon name="users" data-tip="Team Space"/>} item floating id="teams_list">
            <Dropdown.Menu>
              {Object.keys(this.props.teams).map(id => {
                const team = this.props.teams[id];
                const me = team.team_users[team.my_team_user_id];
                if (me.disabled)
                  return (
                      <Popup key={team.id}
                             size="mini"
                             position="left center"
                             trigger={
                                 <Dropdown.Item style={{opacity: '0.45'}}>
                                     <Icon name="users"/>
                                   {team.name}
                                 </Dropdown.Item>
                             }
                             content='You need to wait until an admin accepts you.'/>
                  );
                return (
                    <Dropdown.Item key={team.id} as={NavLink} to={`/teams/${team.id}`} activeClassName="active">
                        <Icon name="users"/>
                      {team.name}
                    </Dropdown.Item>
                )
              })}
                <Dropdown.Item as={NavLink} to={'/main/teamsPreview'}>
                    <Icon name="add square"/>
                    Create a new team...
                </Dropdown.Item>
            </Dropdown.Menu>
        </Dropdown>
    )
  }
}

const newNotificationIcon = () => (
    <Icon.Group><Icon name="bell" data-tip="Notifications"/><Icon corner color="red" size="massive"
                                                                  name='circle'/></Icon.Group>);

@connect(store => ({
  notifications: store.notifications
}))
class NotificationList extends React.Component {
  constructor(props) {
    super(props);
    this.lastScrollTop = 0;
  }
  onClose = () => {
    if (checkForNewNotifications(this.props.notifications.notifications))
      this.props.dispatch(validateNotification());
  };
  onScroll = (e) => {
    if (!this.props.notifications.fetching && this.lastScrollTop < e.target.scrollTop && (e.target.scrollTop > (e.target.scrollHeight - e.target.offsetHeight))) {
      this.props.dispatch(fetchNotifications(this.props.notifications.notifications.length));
    }
    this.lastScrollTop = e.target.scrollTop;
  };
  executeNotification = (notification) => {
    if (!!notification.url.length) {
      this.props.history.push(notification.url);
    }
  };
  render() {
    const newNotifs = checkForNewNotifications(this.props.notifications.notifications);
    return (
        <Dropdown class="bordered_scrollbar"
                  icon={newNotifs ? newNotificationIcon() : <Icon name="bell" data-tip="Notifications"/>} item
                  floating scrolling onClose={this.onClose} id="notifications_menu">
            <Dropdown.Menu onScroll={this.onScroll}>
              {!this.props.notifications.notifications.length &&
              <Dropdown.Item>There isn't notifications yet</Dropdown.Item>}
              {this.props.notifications.notifications.map(function (item) {
                return (
                    <Dropdown.Item key={item.id} active={item.is_new} class="notification-card"
                                   onClick={this.executeNotification.bind(null, item)}>
                        <div class="squared_image_handler icon">
                            <img src={item.icon} alt="icon"/>
                        </div>
                        <div class="display-flex flex_direction_column">
                            <span>{item.content}</span>
                            <span class="date">{moment(item.date).fromNow()}</span>
                        </div>
                    </Dropdown.Item>
                )
              }, this)}
            </Dropdown.Menu>
        </Dropdown>
    )
  }
}

@connect((store) => ({
  user: store.common.user
}))
class EaseMainNavbar extends React.Component {
  constructor(props) {
    super(props);
  }
  processLogout = () => {
    this.props.dispatch(processLogout()).then(response => {
      window.location.href = "/#/login";
//      this.props.history.push('/login');
    })
  };
  logoutFromAllApps = () => {
    this.props.dispatch(processLogout()).then(response => {
      let event = new CustomEvent("Logout");
      document.dispatchEvent(event);
      window.location.href = "/login";
    });
  };
  render() {
    const user = this.props.user;
    return (
        <Menu id="main_navbar">
            <Menu.Item as={NavLink} to='/main/dashboard' data-tip="Apps Dashboard">
              {user !== null ? user.first_name : '...'}
            </Menu.Item>
            <Dropdown icon={<Icon name="log out" data-tip="Logout Menu"/>} item floating id="logout_button">
                <Dropdown.Menu>
                    <Dropdown.Item text="Logout from Ease" onClick={this.processLogout}/>
                    <Dropdown.Item text="Logout from all apps" onClick={this.logoutFromAllApps}/>
                </Dropdown.Menu>
            </Dropdown>
            <Menu.Item data-tip="Settings" as={NavLink} to={`/main/settings`} activeClassName="active">
                <Icon name="setting"/>
            </Menu.Item>
            <NotificationList history={this.props.history}/>
            <TeamsList user={this.props.user}/>
            <Menu.Item as={NavLink} data-tip="Apps Catalogue" id="catalog_button" to={'/main/catalog'}>
                <Icon name="plus"/>
            </Menu.Item>
        </Menu>
    )
  }
}

module.exports = withRouter(EaseMainNavbar);