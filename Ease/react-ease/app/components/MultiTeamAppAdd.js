var React = require('react');
var api = require('../utils/api');
var classnames = require('classnames');
import {requestWebsite, showPinTeamAppToDashboardModal} from "../actions/teamModalActions";
import {closeAppAddUI} from "../actions/teamAppsAddUIActions"
import {teamCreateMultiApp, teamShareApp} from "../actions/appsActions"

class TeamAppSearch extends React.Component{
  constructor(props){
    super(props);
    this.state = {
      apps : []
    };
    this.updateList = this.updateList.bind(this);
  }
  updateList(team_id, query){
    api.teamAppSearch(team_id, query).then(function(data){
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
                  this.state.apps.map(function (item) {
                    return (
                        <div className="dropdown_row app_selector"
                             key={item.id}
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

class MultiTeamAppAdd extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      choosenApp: null,
      appName: '',
      passwordRemind : '0',
      comment: '',
      selectedUsers: [],
      users: [],
      dropdown: false
    };
    this.handleAppNameChange = this.handleAppNameChange.bind(this);
    this.chooseApp = this.chooseApp.bind(this);
    this.resetApp = this.resetApp.bind(this);
    this.requestWebsite = this.requestWebsite.bind(this);
    this.handlePasswordRemind = this.handlePasswordRemind.bind(this);
    this.handleComment = this.handleComment.bind(this);
    this.handleUserSelect = this.handleUserSelect.bind(this);
    this.handleUserDeselect = this.handleUserDeselect.bind(this);
    this.handleUserInput = this.handleUserInput.bind(this);
    this.showDropdown = this.showDropdown.bind(this);
    this.hideDropdown = this.hideDropdown.bind(this);
    this.shareApp = this.shareApp.bind(this);
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
    }
  }
  shareApp(){
    let meSelected = false;

    if (this.state.selectedUsers.length === 0)
      return;
    var app = {
      website_id: this.state.choosenApp.info.id,
      name: this.state.appName,
      description: this.state.comment,
      reminder_interval: this.state.passwordRemind
    };
    if (this.props.selectedItem.type === 'channel')
      app.channel_id = this.props.item.id;
    else
      app.team_user_id = this.props.item.id;
    var selectedUsers = this.state.selectedUsers;
    this.props.dispatch(teamCreateMultiApp(app)).then(response => {
      var id = response.id;
      var sharing = selectedUsers.map(function(user){
        if (this.props.my_id === user.id)
          meSelected = true;
        var user_info = {
          team_user_id: user.id,
          account_information: user.credentials,
          adminHasAccess: false
        };
        return this.props.dispatch(teamShareApp(id, user_info));
      }, this);
      Promise.all(sharing).then(() => {
        if (meSelected)
          this.props.dispatch(showPinTeamAppToDashboardModal(true, response));
        this.props.dispatch(closeAppAddUI());
      });
    });
  }
  hideDropdown(){
    if (this.state.dropdown)
      this.setState({dropdown: false});
  }
  showDropdown(){
    if (!this.state.dropdown)
      this.setState({dropdown: true});
  }
  handleAppNameChange(event){
    this.setState({appName: event.target.value});
  }
  handleUserInput(userId, inputName, value){
    var selectedUsers = this.state.selectedUsers;
    for (var i = 0; i < selectedUsers.length;i++){
      if (selectedUsers[i].id === userId) {
        selectedUsers[i].credentials[inputName] = value;
        this.setState({selectedUsers: selectedUsers});
        break;
      }
    }
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
    var user;
    for (var i = 0; i < users.length; i++){
      if (users[i].id === id){
        if (!users[i].selected){
          users[i].selected = true;
          user = users[i];
          user.credentials = {};
          Object.keys(this.state.choosenApp.inputs).map(function(item){
            user.credentials[item] = '';
          });
          selectedUsers.push(users[i]);
        }
        break;
      }
    }
    this.setState({users: users, selectedUsers: selectedUsers});
  }
  requestWebsite(){
    requestWebsite(this.props.dispatch).then(website => {
      this.chooseApp(website);
    }).catch(err => {
      //do nothing :/
    });
  }
  chooseApp(app){
    api.fetchWebsiteInfo(app.id).then(function(data){
      var credentials = {};
      Object.keys(data.information).map(function (item) {
        credentials[item] = '';
      });
      this.setState({choosenApp: {info : app, inputs: data.information}, appName : app.website_name, credentials: credentials});
      if (this.props.selectedItem.type === 'user')
        this.handleUserSelect(this.state.users[0].id);
    }.bind(this));
  }
  handleComment(event){
    this.setState({comment: event.target.value});
  }
  handlePasswordRemind(event){
    this.setState({passwordRemind: event.target.value});
  }
  resetApp(){
    this.setState({choosenApp: null});
  }
  render(){
    return (
        <div className="add_content_container full_flex team_app active" id="multiple_app_adder">
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
              <TeamAppSearch
                  team_id={this.props.team_id}
                  query={this.state.appName}
                  requestWebsite={this.requestWebsite}
                  selectFunc={this.chooseApp}/>}
            </div>
            {this.state.choosenApp &&
            <button className="button-unstyle action_button modify_app_button" onClick={this.resetApp}>
              <i className="fa fa-times"/>
            </button>}
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
                      this.state.selectedUsers.map(function (user) {
                        return (
                            <div className="credentials_line" key={user.id}>
                              <i className="fa fa-user icon_handler credentials_type_icon"/>
                              <div className="inputs_wrapper">
                                <div className="choosen_user">
                                    <span className="value overflow-ellipsis">
                                      {user.username}
                                    </span>
                                  {user.removable === true &&
                                  <button className="button-unstyle button_delete" onClick={this.handleUserDeselect.bind(null, user.id)}>
                                    <i className="fa fa-times"/>
                                  </button>}
                                </div>
                                {
                                  Object.keys(this.state.credentials).reverse().map(function(item){
                                    return (
                                          <input
                                                 key={item}
                                                 placeholder={`${this.state.choosenApp.inputs[item].placeholder}(Optional)`}
                                                 autoComplete="off"
                                                 className="credentials_value_input value_input"
                                                 type={this.state.choosenApp.inputs[item].type}
                                                 name={item}
                                                 value={user.credentials[item.name]}
                                                 onChange={(e) => {this.handleUserInput(user.id, e.target.name, e.target.value)}}/>
                                    )
                                  }, this)
                                }
                              </div>
                            </div>
                        )
                      }, this)
                    }
                  </div>
                  <div className={classnames("user_choose", this.state.dropdown ? "list_visible": null)}>
                    {this.state.dropdown && <div className="popover_mask" onClick={this.hideDropdown}/>}
                    <input type="text" className="input_unstyle" placeholder="Search for people..."
                           onFocus={this.showDropdown}/>
                    <div className="floating_dropdown user_selectors">
                      {
                        this.state.users.map(function (item) {
                          return (
                              <div className={classnames('user_selector', item.selected ? 'selected':null)} key={item.id} onClick={this.handleUserSelect.bind(null, item.id)}>
                                  <span className="username text_strong">
                                    {item.username}
                                  </span>
                                &nbsp;-&nbsp;
                                <span className="fname">{item.first_name}</span>&nbsp;
                                <span className="lname">{item.last_name}</span>
                              </div>
                          )
                        }, this)
                      }
                    </div>
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

module.exports = MultiTeamAppAdd;