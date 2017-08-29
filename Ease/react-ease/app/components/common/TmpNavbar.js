var React = require('react');
var classnames = require('classnames');
import {fetchNotifications, validateNotification} from "../../actions/notificationsActions";
import {checkForNewNotifications} from "../../utils/helperFunctions";
import ReactTooltip from 'react-tooltip';
import {withRouter} from "react-router-dom";
import {processLogout} from "../../actions/commonActions";
import { NavLink } from 'react-router-dom';
import {connect} from "react-redux";
import { Header, Container, Menu, Segment, Popup, Checkbox, Form, Input,Divider, Icon, List, Select, Dropdown, Button, Grid, Message, Label,Transition } from 'semantic-ui-react';

class TeamsList extends React.Component {
  constructor(props){
    super(props);
  }
  render(){
    return (
          <Dropdown icon={<Icon name="users" data-tip="Team Space"/>} item floating id="teams_list">
            <Dropdown.Menu>
              {this.props.user != null &&
              this.props.user.teams.map(function (item) {
                return (
                    <Dropdown.Item key={item.id} as={NavLink} to={`/teams/${item.id}`} activeClassName="active">
                        <Icon name="users"/>
                        {item.name}
                    </Dropdown.Item>
                )})}
              <Dropdown.Item as={NavLink} to={'/main/teamsPreview'}>
                  <Icon name="users"/>
                  Create a new team...
              </Dropdown.Item>
            </Dropdown.Menu>
          </Dropdown>
    )
  }
}

class NotificationList extends React.Component {
  constructor(props){
    super(props);
    this.onClose = this.onClose.bind(this);
    this.onScroll = this.onScroll.bind(this);
  }
  onClose(){
    if (checkForNewNotifications(this.props.notifications.notifications))
      this.props.dispatch(validateNotification());
  }
  onScroll(e){
    if (!this.props.notifications.fetching && (e.target.scrollTop > (e.target.scrollHeight - e.target.offsetHeight))){
      this.props.dispatch(fetchNotifications(this.props.notifications.notifications.length));
    }
  }
  render(){
    const newNotifs = checkForNewNotifications(this.props.notifications.notifications);
    return (
        <Dropdown class="bordered_scrollbar" icon={<Icon name="bell" data-tip="Notifications"/>} item floating scrolling onClose={this.onClose} id="notifications_menu">
          <Dropdown.Menu onScroll={this.onScroll}>
            {this.props.notifications.notifications.map(function (item) {
              return (
                  <Dropdown.Item key={item.id} active={item.is_new} class="notification-card">
                        <div class="squared_image_handler icon">
                          <img src={item.icon} alt="icon"/>
                        </div>
                        <div class="display-flex flex_direction_column">
                          <span>{item.content}</span>
                          <span class="date">il y a {item.date} heures</span>
                        </div>
                  </Dropdown.Item>
              )
            }, this)}
          </Dropdown.Menu>
        </Dropdown>
    )
  }
}

@connect((store)=>{
  return {
    user: store.common.user,
    notifications: store.notifications
  };
})
class TmpNavbar extends React.Component {
  constructor(props){
    super(props);
    this.processLogout = this.processLogout.bind(this);
    this.goHome = this.goHome.bind(this);
  }
  processLogout(){
    this.props.dispatch(processLogout()).then(response => {
      window.location.href = "/#/login";
//      this.props.history.push('/login');
    })
  }
  goHome(){
    window.location.href = "/home";
  }
  componentDidMount(){
    ReactTooltip.rebuild();
  }
  render(){
    const user = this.props.user;
    return (
        <Menu id="main_navbar">
          <Menu.Item onClick={this.goHome} data-tip="Apps Dashboard" >
            {user !== null ? user.first_name : '...'}
          </Menu.Item>
          <Dropdown icon={<Icon name="log out" data-tip="Logout Menu"/>} item floating id="logout_button">
            <Dropdown.Menu>
              <Dropdown.Item text="Logout from Ease" onClick={this.processLogout}/>
              <Dropdown.Item text="Logout from all apps"/>
            </Dropdown.Menu>
          </Dropdown>
          <NotificationList notifications={this.props.notifications} dispatch={this.props.dispatch}/>
          <TeamsList user={this.props.user}/>
          <Menu.Item id="catalog_button">
            <Icon name="plus"/>
          </Menu.Item>
        </Menu>
    )
  }
}

module.exports = withRouter(TmpNavbar);