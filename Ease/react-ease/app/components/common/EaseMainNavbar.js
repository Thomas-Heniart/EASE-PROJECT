var React = require('react');
var classnames = require('classnames');
import ReactTooltip from 'react-tooltip';
import {withRouter} from "react-router-dom";
import {processLogout} from "../../actions/commonActions";
import { NavLink } from 'react-router-dom';
import {connect} from "react-redux";


class TeamsList extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      dropdown: false
    };
    this.onMouseDown = this.onMouseDown.bind(this);
    this.onMouseUp = this.onMouseUp.bind(this);
    this.pageClick = this.pageClick.bind(this);
  }
  onMouseDown(){
    this.mouseInDropDown = true;
  }
  onMouseUp(){
    this.mouseInDropDown = false;
  }
  pageClick(e){
    if (this.mouseInDropDown)
      return;
    this.setState({dropdown: false});
  }
  componentDidMount(){
    window.addEventListener('mousedown', this.pageClick, false);
  }
  componentWillUnmount(){
    window.removeEventListener('mousedown', this.pageClick, false);
  }
  render(){
    return (
        <button class="button-unstyle" id="teams_button"
                onClick={e => {this.setState({dropdown: true})}}
                onMouseDown={this.onMouseDown}
                onMouseUp={this.onMouseUp}>
          <img src="/resources/icons/users.svg" data-tip="Team Space"/>
          <div class={classnames('menu menu_arrow display_flex flex_direction_column', this.state.dropdown ? 'show': null)}>
            {this.props.user != null &&
            this.props.user.teams.map(function (item) {
              return (
                  <div class="menu_row team_select" key={item.id}>
                    <NavLink to={`/teams/${item.id}`} activeClassName="active">
                      <i class="fa fa-users icon"/>
                      {item.name}
                    </NavLink>
                  </div>
              )})}
            <div class="menu_row display_flex align_items_center" id="team_adder">
              <NavLink to={'/main/teamsPreview'}>
                <i class="fa fa-plus-square icon"/>
                Create a new team...
              </NavLink>
            </div>
          </div>
        </button>
    )
  }
}

class NotificationList extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      dropdown: false
    };
    this.onMouseDown = this.onMouseDown.bind(this);
    this.onMouseUp = this.onMouseUp.bind(this);
    this.pageClick = this.pageClick.bind(this);
  }
  onMouseDown(){
    this.mouseInDropDown = true;
  }
  onMouseUp(){
    this.mouseInDropDown = false;
  }
  pageClick(e){
    if (this.mouseInDropDown)
      return;
    this.setState({dropdown: false});
  }
  componentDidMount(){
    window.addEventListener('mousedown', this.pageClick, false);
  }
  componentWillUnmount(){
    window.removeEventListener('mousedown', this.pageClick, false);
  }
  render(){
    return (
        <button class="button-unstyle bordered_scrollbar notify" id="notification_button"
                onClick={e => {this.setState({dropdown: true})}}
                onMouseDown={this.onMouseDown}
                onMouseUp={this.onMouseUp}>
          <img src="/resources/icons/notification.svg" data-tip="Notifications"/>
          <div class={classnames('menu menu_arrow display_flex flex_direction_column ', this.state.dropdown ? 'show': null)}>
            {this.props.notifications.map(function (item) {
              return (
                  <div class="menu_row display_flex align_items_center" key={item.id}>
                    <div class={classnames("notification-card display-flex", item.isNew ? 'new': null)}>
                      <div class="squared_image_handler icon">
                        <img src={item.icon} alt="icon"/>
                      </div>
                      <div class="display-flex flex_direction_column">
                        <span>{item.content}</span>
                        <span class="date">il y a {item.date} heures</span>
                      </div>
                    </div>
                  </div>
              )
            }, this)}
          </div>
        </button>
    )
  }
}

@connect((store)=>{
  return {
    user: store.common.user,
    notifications: store.common.notifications
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
      window.location.href = "/login";
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
        <nav class="nav display_flex" id="ease_main_navbar">
          <button class="button-unstyle" id="settings_button" onClick={this.goHome} data-tip="Apps Dashboard">
            <span>{user !== null ? user.first_name : '...'}</span>
          </button>
          <button class="button-unstyle" id="logout_button" >
            <img src="/resources/icons/logout.svg" data-tip="Logout Menu"/>
            <div class="menu menu_arrow display_flex flex_direction_column">
              <div class="menu_row" id="simple_logout" onClick={this.processLogout}>
                Logout from Ease
              </div>
              <div class="menu_row" id="general_logout">
                Logout from all apps
              </div>
            </div>
          </button>
          <NotificationList notifications={this.props.notifications}/>
          <TeamsList user={this.props.user}/>
          <button class="button-unstyle action_button" id="catalog_button">
            <img src="/resources/icons/plus.svg" data-tip="Apps Catalogue"/>
          </button>
        </nav>
    )
  }
}

module.exports = withRouter(EaseMainNavbar);