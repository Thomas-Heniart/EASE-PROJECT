var React = require('react');
var classnames = require('classnames');
var api = require('../utils/api');
var TeamAppAdderButtons = require('./TeamAppAdderButtons');
var MultiTeamAppAdd = require('./MultiTeamAppAdd');
import {requestWebsite, showPinTeamAppToDashboardModal} from "../actions/teamModalActions";
import {connect} from "react-redux";
import {selectUserFromListById} from "../utils/helperFunctions";
import * as appActions from "../actions/appsActions";
import {closeAppAddUI} from "../actions/teamAppsAddUIActions"
import SimpleTeamAppAdder from "./teamAppAdders/SimpleTeamAppAdder";
import LinkTeamAppAdder from "./teamAppAdders/LinkTeamAppAdder";

class DashboardAndTeamAppSearch extends React.Component{
  constructor(props){
    super(props);
    this.state = {
      apps : []
    };
    this.updateList = this.updateList.bind(this);
  }
  updateList(team_id, query){
    api.dashboardAndTeamAppSearch(team_id, query).then(function(data){
      this.setState({apps: data});
    }.bind(this));
  }
  componentWillReceiveProps(props){
    props !== this.props && this.updateList(props.team_id, props.query);
  }
  componentDidMount(){
    this.updateList(this.props.team_id, this.props.query);
  }
  render() {
    return (
        <div className="floating_dropdown app_selectors show">
          <div className="dropdown_content">
            {
              this.state.apps.length ?
                  this.state.apps.map(function (item, idx) {
                    if (item.login)
                      return (
                          <div className="dropdown_row app_selector"
                               key={idx}
                               onClick={this.props.dashboardSelectFunc.bind(null, item)}>
                            <img className="logo" src={item.logo}/>
                            <span className="app_name overflow-ellipsis">{item.website_name}</span>
                            <span className="overflow-ellipsis text-muted">&nbsp;- from {item.profile_name} - {item.login}</span>
                          </div>
                      );
                    return (
                        <div className="dropdown_row app_selector"
                             key={idx}
                             onClick={this.props.selectFunc.bind(null, item)}>
                          <img className="logo" src={item.logo}/>
                          <span className="app_name overflow-ellipsis">{item.website_name}</span>
                        </div>
                    )
                  }, this) :
                  <div>No results</div>
            }
          </div>
          <div class="attached" onClick={this.props.requestWebsite}>
            <i class="fa fa-plus"/>
            Request a website
          </div>
        </div>
    )
  }
}

class SimpleUserSelect extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      dropdown: false
    };
    this.closeDropdown = this.closeDropdown.bind(this);
    this.openDropdown = this.openDropdown.bind(this);
  }
  closeDropdown(){
    this.setState({dropdown: false});
  }
  openDropdown(){
    this.setState({dropdown : true});
  }
  render(){
    return (
        <div className="user_adding_container">
          {this.state.dropdown && <div className="popover_mask" onClick={this.closeDropdown}/>}
          <div className="icon_wrapper">
            <i className="fa fa-plus"/>
          </div>
          <div className="add_container full_flex display_flex">
            {
              this.props.selectedUsers.map(function(item){
                return (
                    <div className="user_token" key={item.id}>
                      <span className="name_hodler overflow-ellipsis">{item.username}</span>
                      {item.can_see_information !== undefined &&
                        <button class="action_button button-unstyle mrgnLeft5" onClick={this.props.switchCanSeeInformationFunc.bind(null, item.id)}>
                        <i class={classnames('fa', item.can_see_information ? 'fa-eye' : 'fa-eye-slash')}/>
                      </button>}
                      {item.removable === true &&
                      <button className="button_delete action_button button-unstyle" onClick={this.props.deselectFunc.bind(null, item.id)}>
                        <i className="fa fa-times"/>
                      </button>}
                    </div>
                )
              }, this)
            }
            <input className="true_input input_unstyle full_flex" placeholder="Tag users here..." type="text" onFocus={this.openDropdown}/>
          </div>
          {this.state.dropdown && <div className="floating_dropdown user_selectors show">
            {
              this.props.users.map(function(item){
                return (
                    <div className={classnames('user_selector', item.selected ? 'selected':null)} key={item.id} onClick={this.props.selectFunc.bind(null, item.id)}>
                                  <span className="username text_strong">
                                    {item.username}
                                  </span>
                      -
                      <span className="fname">{item.first_name}</span>
                      <span className="lname">{item.last_name}</span>
                    </div>
                )
              }, this)
            }
          </div>}
        </div>
    )
  }
}

class LinkTeamAppAdd extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      appName: '',
      url: '',
      logoSrc: '/resources/icons/link_app.png',
      comment: '',
      selectedUsers: [],
      users: []
    };
    this.handleAppNameChange = this.handleAppNameChange.bind(this);
    this.handleUrlInput= this.handleUrlInput.bind(this);
    this.handleUserSelect = this.handleUserSelect.bind(this);
    this.handleUserDeselect = this.handleUserDeselect.bind(this);
    this.shareApp = this.shareApp.bind(this);
    this.handleComment = this.handleComment.bind(this);
  }
  componentDidMount(){
    if (this.props.selectedItem.type === 'channel'){
      this.props.item.userIds.map(function(item){
        var user = this.props.userSelectFunc(item);
        user.selected = false;
        user.removable = true;
        this.state.users.push(user);
      }, this);
    } else {
      var item = this.props.item;
      item.selected = false;
      item.removable = false;
      this.state.users.push(item);
      this.handleUserSelect(item.id);
    }
  }
  shareApp(){
    let meSelected = false;
    var app = {
      name: this.state.appName,
      url: this.state.url,
      description: this.state.comment
    };
    if (this.props.selectedItem.type === 'channel')
      app.channel_id = this.props.item.id;
    else
      app.team_user_id = this.props.item.id;
    var selectedUsers = this.state.selectedUsers;
    this.props.dispatch(appActions.teamCreateLinkApp(app)).then(response => {
      var id = response.id;
      var sharing = selectedUsers.map(function (item) {
        if (this.props.my_id === item.id)
          meSelected = true;
        return this.props.dispatch(appActions.teamShareApp(id, {team_user_id: item.id}));
      }, this);
      Promise.all(sharing).then(() => {
        if (meSelected)
          this.props.dispatch(showPinTeamAppToDashboardModal(true, response));
        this.props.dispatch(closeAppAddUI());
      });
    });
  }
  handleComment(event){
    this.setState({comment: event.target.value});
  }
  handleAppNameChange(event){
    this.setState({appName: event.target.value});
  }
  handleUrlInput(event){
    this.setState({url: event.target.value});
  }
  handleUserDeselect(id){
    var users = this.state.users;
    var selectedUsers = this.state.selectedUsers;
    for (var i = 0; i < users.length; i++){
      if (users[i].id === id){
        if (users[i].selected){
          users[i].selected = false;
          selectedUsers.splice(selectedUsers.indexOf(users[i]), 1);
        }
        break;
      }
    }
    this.setState({users: users, selectedUsers: selectedUsers});
  }
  handleUserSelect(id){
    var users = this.state.users;
    var selectedUsers = this.state.selectedUsers;
    for (var i = 0; i < users.length; i++){
      if (users[i].id === id){
        if (!users[i].selected){
          users[i].selected = true;
          selectedUsers.push(users[i]);
        }
        break;
      }
    }
    this.setState({users: users, selectedUsers: selectedUsers});
  }
  render(){
    return (
        <div className="add_content_container full_flex team_app active" id="simple_app_adder">
          <div className="add_actions_holder">
            <button className="button-unstyle send_button action_text_button positive_background" onClick={this.shareApp}>
              Send
            </button>
            <button className="button-unstyle action_text_button alert_background close_button" onClick={e => {this.props.dispatch(closeAppAddUI())}}>
              Cancel
            </button>
          </div>
          <div className="app_name_input_handler display_flex locked">
            <i className="fa fa-search icon_holder"/>
            <div className="app_name_input_wrapper">
              <input className="true_input input_unstyle width100"
                     placeholder="App name..."
                     name="app_name"
                     value={this.state.appName}
                     onChange={this.handleAppNameChange}/>
            </div>
          </div>
          <div>
            <div className="info_holder">
              <div className="info">
                <div className="logo_holder">
                  <img src={this.state.logoSrc} alt="website logo"/>
                </div>
                <div className="credentials_holder">
                  <div className="credentials">
                    <div className="credentials_line">
                      <i className="fa fa-home icon_handler credentials_type_icon"/>
                      <input className="credentials_value_input value_input"
                        placeholder="Your url..."
                        autoComplete="off"
                        type="text"
                        name="url"
                        value={this.state.url}
                        onChange={this.handleUrlInput}/>
                      </div>
                  </div>
                </div>
              </div>
              <div className="sharing_info full_flex">
                <SimpleUserSelect
                    users={this.state.users}
                    selectedUsers={this.state.selectedUsers}
                    selectFunc={this.handleUserSelect}
                    deselectFunc={this.handleUserDeselect}/>
              </div>
            </div>
            <div className="comment_holder">
              <div className="comment_icon">
                <i className="fa fa-sticky-note-o"/>
              </div>
              <div className="comment">
                <textarea value={this.state.comment} onChange={this.handleComment} className="comment_input value_input" placeholder="Your comment..."/>
              </div>
            </div>
          </div>
        </div>
    )
  }
}

class SimpleTeamAppAdd extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      choosenApp: null,
      appName: '',
      credentials: {},
      passwordRemind : '0',
      comment: '',
      selectedUsers: [],
      users: []
    };
    this.handleAppNameChange = this.handleAppNameChange.bind(this);
    this.chooseApp = this.chooseApp.bind(this);
    this.requestWebsite = this.requestWebsite.bind(this);
    this.resetApp = this.resetApp.bind(this);
    this.handleCredentialInput = this.handleCredentialInput.bind(this);
    this.handlePasswordRemind = this.handlePasswordRemind.bind(this);
    this.handleComment = this.handleComment.bind(this);
    this.handleUserSelect = this.handleUserSelect.bind(this);
    this.handleUserDeselect = this.handleUserDeselect.bind(this);
    this.chooseDashboardApp = this.chooseDashboardApp.bind(this);
    this.switchUserCanSeeInformation = this.switchUserCanSeeInformation.bind(this);
    this.shareApp = this.shareApp.bind(this);
  }
  componentDidMount(){
    if (this.props.selectedItem.type === 'channel'){
      this.props.item.userIds.map(function(item){
        var user = {...this.props.userSelectFunc(item)};
        user.selected = false;
        user.removable = true;
        user.can_see_information = false;
        this.state.users.push(user);
      }, this);
    } else {
      var item = {...this.props.item};
      item.removable = false;
      item.selected = false;
      item.can_see_information = true;
      this.state.users.push(item);
      this.handleUserSelect(item.id);
    }
  }
  requestWebsite(){
    requestWebsite(this.props.dispatch).then(website => {
      this.chooseApp(website);
    }).catch(err => {
      //do nothing :/
    });
  }
  shareApp(){
    let credentials = Object.keys(this.state.credentials);
    let meSelected = false;

    for (let i = 0; i < credentials.length; i++) {
      if (this.state.credentials[credentials[i]].length === 0)
        return;
    }
    var app = {
      website_id: this.state.choosenApp.info.id,
      name: this.state.appName,
      description: this.state.comment,
      reminder_interval: this.state.passwordRemind,
      account_information: []
    };
    if (this.props.selectedItem.type === 'channel')
      app.channel_id = this.props.item.id;
    else
      app.team_user_id = this.props.item.id;
    Object.keys(this.state.credentials).map(function(item){
      app.account_information.push({info_name: item, info_value: this.state.credentials[item]});
    }, this);
    var selectedUsers = this.state.selectedUsers;
    this.props.dispatch(appActions.teamCreateSingleApp(app)).then(response => {
      var id = response.id;
      var sharing = selectedUsers.map(function (item) {
        if (this.props.my_id === item.id)
          meSelected = true;
        return this.props.dispatch(appActions.teamShareApp(id, {team_user_id: item.id, can_see_information: item.can_see_information}));
      }, this);
      Promise.all(sharing).then(() => {
        if (meSelected)
          this.props.dispatch(showPinTeamAppToDashboardModal(true, response));
        this.props.dispatch(closeAppAddUI());
      });
    });
  };
  switchUserCanSeeInformation(user_id){
    var users = this.state.users;
    for (var i = 0; i < users.length; i++){
      if (users[i].id === user_id){
        users[i].can_see_information = !users[i].can_see_information;
        this.setState({users: users});
        return;
      }
    }
  }
  handleAppNameChange(event){
    this.setState({appName: event.target.value});
  }
  handleUserDeselect(id){
    var users = this.state.users;
    var selectedUsers = this.state.selectedUsers;
    for (var i = 0; i < users.length; i++){
      if (users[i].id === id){
        if (users[i].selected){
          users[i].selected = false;
          selectedUsers.splice(selectedUsers.indexOf(users[i]), 1);
        }
        break;
      }
    }
    this.setState({users: users, selectedUsers: selectedUsers});
  }
  handleUserSelect(id){
    var users = this.state.users;
    var selectedUsers = this.state.selectedUsers;
    for (var i = 0; i < users.length; i++){
      if (users[i].id === id){
        if (!users[i].selected){
          users[i].selected = true;
          selectedUsers.push(users[i]);
        }
        break;
      }
    }
    this.setState({users: users, selectedUsers: selectedUsers});
  }
  chooseDashboardApp(app){
    api.getDashboardApp(app.id).then(function(data){
      var info = data.information;
      app.id = data.website_id;
      api.fetchWebsiteInfo(data.website_id).then(function(data){
        var credentials = {};
        info.map(function(item){
          credentials[item.info_name] = item.info_value;
        });
        this.setState({
          choosenApp: {info: app, inputs:data.information},
          appName:app.website_name,
          credentials: credentials
        });
      }.bind(this));
    }.bind(this));
  }
  chooseApp(app){
    api.fetchWebsiteInfo(app.id).then(function(data){
      var credentials = {};
      Object.keys(data.information).map(function (item) {
        credentials[item] = '';
      });
      this.setState({choosenApp: {info : app, inputs: data.information}, appName : app.website_name, credentials: credentials});
    }.bind(this));
  }
  handleComment(event){
    this.setState({comment: event.target.value});
  }
  handlePasswordRemind(event){
    this.setState({passwordRemind: event.target.value});
  }
  handleCredentialInput(event){
    var state = this.state.credentials;
    state[event.target.name] = event.target.value;
    this.setState({credentials: state});
  }
  resetApp(){
    this.setState({choosenApp: null});
  }
  render(){
    return (
        <div className="add_content_container full_flex team_app active" id="simple_app_adder">
          <div className="add_actions_holder">
            <button className="button-unstyle send_button action_text_button positive_background" onClick={this.shareApp}>
              Send
            </button>
            <button className="button-unstyle action_text_button alert_background close_button" onClick={e => {this.props.dispatch(closeAppAddUI())}}>
              Cancel
            </button>
          </div>
          <div className="app_name_input_handler display_flex locked">
            <i className="fa fa-search icon_holder"/>
            <div className="app_name_input_wrapper">
              <input className="true_input input_unstyle width100"
                     placeholder="App name..."
                     name="app_name"
                     value={this.state.appName}
                     onChange={this.handleAppNameChange}
              />
              {!this.state.choosenApp && this.state.appName.length > 0 &&
              <DashboardAndTeamAppSearch
                  team_id={this.props.team_id}
                  query={this.state.appName}
                  selectFunc={this.chooseApp}
                  requestWebsite={this.requestWebsite}
                  dashboardSelectFunc={this.chooseDashboardApp}/>}
            </div>
            {this.state.choosenApp &&
            <button className="button-unstyle action_button modify_app_button" onClick={this.resetApp}>
              <i className="fa fa-times"/>
            </button>
            }
          </div>
          {this.state.choosenApp  &&
          <div>
            <div className="info_holder">
              <div className="info">
                <div className="logo_holder">
                  <img src={this.state.choosenApp.info.logo} alt="website logo"/>
                </div>
                <div className="credentials_holder">
                  <div className="credentials">
                    {
                      Object.keys(this.state.credentials).reverse().map(function(item){
                        return (
                            <div className="credentials_line" key={item}>
                              <i className={classnames("fa icon_handler credentials_type_icon", this.state.choosenApp.inputs[item].placeholderIcon)}/>
                              <input placeholder={this.state.choosenApp.inputs[item].placeholder}
                                     autoComplete="off"
                                     className="credentials_value_input value_input"
                                     type={this.state.choosenApp.inputs[item].type}
                                     name={item}
                                     value={this.state.credentials[item]}
                                     onChange={this.handleCredentialInput}/>
                            </div>
                        )
                      }, this)
                    }
                  </div>
                  <div className="password_change_remind">
                    <div className="password_change_icon">
                      <i className="fa fa-refresh"/>
                    </div>
                    <span className="text_helper">Password change reminder:</span>
                    <div className="password_change_info">
                      <select name="password_change_interval"
                              className="password_change_input value_input"
                              value={this.state.passwordRemind}
                              onChange={this.handlePasswordRemind}>
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
              <div className="sharing_info full_flex">
                <SimpleUserSelect
                    users={this.state.users}
                    selectedUsers={this.state.selectedUsers}
                    switchCanSeeInformationFunc={this.switchUserCanSeeInformation}
                    selectFunc={this.handleUserSelect}
                    deselectFunc={this.handleUserDeselect}
                />
              </div>
            </div>
            <div className="comment_holder">
              <div className="comment_icon">
                <i className="fa fa-sticky-note-o"/>
              </div>
              <div className="comment">
                <textarea value={this.state.comment} onChange={this.handleComment} className="comment_input value_input" placeholder="Your comment..."/>
              </div>
            </div>
          </div>}
        </div>
    )
  }
}

@connect((store)=>{
  return {
    selectedItem: store.selection,
    team_id: store.team.id,
    addAppUI: store.teamAppsAddUI,
    users: store.users.users,
    my_id: store.team.myTeamUserId
  };
})
class TeamAppAddingUi extends React.Component {
  constructor(props){
    super(props);
  }
  render(){
    const item = this.props.selection;
    return (
        <div className="add_actions_container" id="app_add_actions">
          {this.props.addAppUI.TeamSimpleAppAddActive &&
                   <SimpleTeamAppAdder item={item}/>
            /*<SimpleTeamAppAdd
              team_id={this.props.team_id}
              selectedItem={this.props.selectedItem}
              item={item}
              my_id={this.props.my_id}
              userSelectFunc={selectUserFromListById.bind(null, this.props.users)}
              dispatch={this.props.dispatch}/>*/}
          {this.props.addAppUI.TeamLinkAppAddActive &&
              <LinkTeamAppAdder item={item} dispatch={this.props.dispatch}/>
              /*<LinkTeamAppAdd
                  team_id={this.props.team_id}
                  selectedItem={this.props.selectedItem}
                  item={item}
                  my_id={this.props.my_id}
                  userSelectFunc={selectUserFromListById.bind(null, this.props.users)}
                  dispatch={this.props.dispatch}/>*/}
          {this.props.addAppUI.TeamMultiAppAddActive &&
            <MultiTeamAppAdd
              team_id={this.props.team_id}
              selectedItem={this.props.selectedItem}
              item={item}
              my_id={this.props.my_id}
              userSelectFunc={selectUserFromListById.bind(null, this.props.users)}
              dispatch={this.props.dispatch}/>}
        </div>
    )
  }
}

module.exports = TeamAppAddingUi;