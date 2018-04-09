import React, {Component, Fragment} from "react";
import {Dropdown, Icon, Menu, Popup} from 'semantic-ui-react';
import {connect} from "react-redux";
import classnames from "classnames";
import {teamUserDepartureDatePassed} from "../../utils/utils";
import extension from "../../utils/extension_api";
import {processLogout, setGeneralLogoutModal} from "../../actions/commonActions";
import {NavLink, withRouter} from "react-router-dom";
import {fetchNotifications, validateNotification} from "../../actions/notificationsActions";
import {checkForNewNotifications} from "../../utils/helperFunctions";
import {setDashboardFooterState} from "../../actions/dashboardActions";

const HeaderButtonPopup = ({trigger, content}) => {
  return (
      <Popup size="mini"
             id="header_button_popup"
             inverted
             position="right center"
             trigger={trigger}
             content={content}/>
  )
};

@connect(store => ({
  notifications: store.notifications
}))
class NotificationList extends Component {
  constructor(props){
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
      window.location.href = notification.url;
//      this.props.history.push(notification.url);
    }
  };
  render(){
    const newNotifs = checkForNewNotifications(this.props.notifications.notifications);

    return (
        <Dropdown class="bordered_scrollbar header_button"
                  id="notifications_menu"
                  onOpen={this.onOpen}
                  onClose={this.onClose}
                  icon={
                    <HeaderButtonPopup
                        trigger={
                          <Icon name="bell"
                                color={newNotifs ? 'red' : null}
                                fitted/>
                        }
                        content="Notifications"
                    />
                  }
                  scrolling>
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

const SettingsButton = (props) => {
  return (
      <Dropdown class="header_button"
                icon={
                  <HeaderButtonPopup
                      trigger={
                        <Icon name="cogs" fitted/>
                      }
                      content="Settings"
                  />
                }>
        <Dropdown.Menu>
          <Dropdown.Item as={NavLink}
                         to={'/main/settings'}
                         text="Personal settings"
                         icon="setting"/>
          <Dropdown.Item as={NavLink}
                         icon="add square"
                         text="Create a new team..."
                         to={'/main/teamsPreview'}>
          </Dropdown.Item>
        </Dropdown.Menu>
      </Dropdown>
  )
};

class LogoutButton extends Component {
  constructor(props){
    super(props);
  }
  processLogout = () => {
    this.props.dispatch(processLogout()).then(response => {
      extension.easeLogout();
      this.props.history.push('/login');
    })
  };
  logoutFromAllApps = () => {
    this.props.dispatch(setGeneralLogoutModal({
      active: true
    }));
    this.props.dispatch(processLogout()).then(response => {
      extension.general_logout();
      extension.easeLogout();
      this.props.history.push('/login');
    });
  };
  render(){
    return (
        <Dropdown class="header_button"
                  icon={
                    <HeaderButtonPopup
                        trigger={<Icon name="power" fitted/>}
                        content={'Logout'}
                    />
                  }>
          <Dropdown.Menu>
            <Dropdown.Item icon="log out" text="Ease.space Logout" onClick={this.processLogout}/>
            <Popup size="mini"
                   inverted
                   position="right center"
                   trigger={
                     <Dropdown.Item icon="power" text="General Logout" onClick={this.logoutFromAllApps}/>
                   }
                   content='It will logout accounts you connected to, using Ease.space.'/>
          </Dropdown.Menu>
        </Dropdown>
    )
  }
}

@connect(store => ({
  teams: store.teams
}))
class TeamsList extends Component {
  constructor(props){
    super(props);
  }
  render(){
    return (
        <Fragment>
          {Object.keys(this.props.teams).map(id => {
            const team = this.props.teams[id];
            const me = team.team_users[team.my_team_user_id];
            const buttonText = team.name.replace(/\s+/g, '').substring(0, 2).toUpperCase();

            if (teamUserDepartureDatePassed(me.departure_date))
              return (
                  <HeaderButtonPopup
                      key={team.id}
                      trigger={
                        <NavLink class="header_button team_button">
                          {buttonText}
                        </NavLink>
                      }
                      content='Your departure date has passed! Your access to the team is now over.'
                  />
              );
            else if (me.disabled || (team.onboarding_step !== 5 && me.role !== 3))
              return (
                  <HeaderButtonPopup
                      key={team.id}
                      trigger={
                        <NavLink class="header_button team_button">
                          {buttonText}
                        </NavLink>
                      }
                      content='You need to wait until an admin accepts you.'
                  />
              );
            return (
                <HeaderButtonPopup
                    key={team.id}
                    trigger={
                      <NavLink class="header_button team_button"
                               location={this.props.location}
                               activeClassName="active"
                               to={`/teams/${team.id}`}>
                        {buttonText}
                      </NavLink>
                    }
                    content={`${team.name} team`}
                />
            )
          })}
        </Fragment>
    )
  }
}

@connect(store => ({
  background_picture: store.common.user.background_picture
}))
class HeaderSidebar extends Component {
  constructor(props){
    super(props);
  }
  openFooter = () => {
    this.props.dispatch(setDashboardFooterState(({
      active: true
    })));
  };
  render(){
    return (
        <div id="header_sidebar" class={(this.props.background_picture && window.location.hash.includes('dashboard')) ? 'background_picture' : null}>
          <div style={{marginBottom: '10px'}}>
            <NotificationList history={this.props.history}/>
            <NavLink to={'/main/catalog'}
                     id="catalog_button"
                     class="header_button">
              <HeaderButtonPopup
                  trigger={
                    <Icon name="plus" fitted/>
                  }
                  content="Add new app"
              />
            </NavLink>
            <NavLink to={'/main/dashboard'}
                     class="header_button">
              <HeaderButtonPopup
                  trigger={
                    <Icon name="grid layout" fitted/>
                  }
                  content="Dashboard"
              />
            </NavLink>
          </div>
          <div class="full_flex teams_list_section">
            <TeamsList location={this.props.location}/>
          </div>
          <div>
            <SettingsButton/>
            <LogoutButton history={this.props.history}
                          dispatch={this.props.dispatch}/>
            <a class="header_button" onClick={this.openFooter}>
              <HeaderButtonPopup
                  trigger={<img style={{height:25, width:25}} src="/resources/icons/ease_logo_blue.svg"/>}
                  content="Our links"/>
            </a>
          </div>
        </div>
    )
  }
}

export default withRouter(HeaderSidebar);