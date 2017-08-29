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
              <Icon name="add square"/>
              Create a new team...
            </Dropdown.Item>
          </Dropdown.Menu>
        </Dropdown>
    )
  }
}

const newNotificationIcon = () => (<Icon.Group><Icon name="bell" data-tip="Notifications"/><Icon corner color="red" size="massive" name='circle'/></Icon.Group>);
class NotificationList extends React.Component {
  constructor(props){
    super(props);
    this.executeNotification = this.executeNotification.bind(this);
    this.onClose = this.onClose.bind(this);
    this.onScroll = this.onScroll.bind(this);
    this.lastScrollTop = 0;
  }
  onClose(){
    if (checkForNewNotifications(this.props.notifications.notifications))
      this.props.dispatch(validateNotification());
  }
  onScroll(e){
    if (!this.props.notifications.fetching && this.lastScrollTop < e.target.scrollTop && (e.target.scrollTop > (e.target.scrollHeight - e.target.offsetHeight))){
      this.props.dispatch(fetchNotifications(this.props.notifications.notifications.length));
    }
    this.lastScrollTop = e.target.scrollTop;
  }
  executeNotification(notification){
    if (notification.url.length > 0){
      this.props.history.push(notification.url);
    }
  }
  render(){
    const newNotifs = checkForNewNotifications(this.props.notifications.notifications);
    return (
        <Dropdown class="bordered_scrollbar" icon={newNotifs ? newNotificationIcon() : <Icon name="bell" data-tip="Notifications"/>} item floating scrolling onClose={this.onClose} id="notifications_menu">
          <Dropdown.Menu onScroll={this.onScroll}>
            {this.props.notifications.notifications.length === 0 &&
            <Dropdown.Item>There isn't notifications yet</Dropdown.Item>}
            {this.props.notifications.notifications.map(function (item) {
              return (
                  <Dropdown.Item key={item.id} active={item.is_new} class="notification-card" onClick={this.executeNotification.bind(null, item)}>
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

@connect((store)=>{
  return {
    user: store.common.user,
    notifications: store.notifications
  };
})
class EaseMainNavbar extends React.Component {
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
          <NotificationList notifications={this.props.notifications} history={this.props.history} dispatch={this.props.dispatch}/>
          <TeamsList user={this.props.user}/>
          <Menu.Item id="catalog_button" data-tip="Apps Catalogue">
            <Icon name="plus"/>
          </Menu.Item>
        </Menu>
    )
  }
}

module.exports = withRouter(EaseMainNavbar);