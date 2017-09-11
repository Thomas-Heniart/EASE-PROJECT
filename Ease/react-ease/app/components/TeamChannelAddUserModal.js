var React = require('react');
var classnames = require('classnames');
var api = require('../utils/api');
import {showTeamChannelAddUserModal} from "../actions/teamModalActions"
import {addTeamUserToChannel} from "../actions/channelActions"
var SimpleAppSharingPreview = require('./teamAppsPreview/SimpleAppSharingPreview');
var LinkAppSharingPreview = require('./teamAppsPreview/LinkAppSharingPreview');
var MultiAppSharingPreview = require('./teamAppsPreview/MultiAppSharingPreview');
import {selectChannelFromListById} from "../utils/helperFunctions"
import {teamShareApp} from "../actions/appsActions"
import {connect} from "react-redux"

class FirstStep extends React.Component {
  constructor(props){
    super(props);
  }
  render(){
    const users = this.props.users;
    return (
        <div class="contents" id="first_step">
          <div class="content_row">
            <h1>Browse people</h1>
          </div>
          {users.length > 0 ?
              <div class="content_row flex_direction_column">
                <div class="modal_input_wrapper">
                  <i class="fa fa-search input_icon"/>
                  <input type="text" value="" class="input_unstyle" id="name_input"
                         placeholder="Search user name or email" name="name"/>
                </div>
                <div class="user_selecting_list width100 mrgnTop5">
                  {
                    users.map(function (item) {
                      return (
                          <div class="user display-flex flex_direction_column" key={item.id}
                                onClick={this.props.selectUser.bind(null, item.id)}>
                            {
                              item.first_name !== null && item.first_name.length > 0 ?
                                  <strong class="name">{item.first_name} {item.last_name}</strong>
                                  :
                                  <strong class="name"><em>User name not setup yet</em></strong>
                            }
                            <span>@{item.username} - {item.email}</span>
                          </div>
                      )
                    }, this)
                  }
                </div>
              </div>
              :
              <div class="content_row">
                    <span>
                      This group already contain all members of this team.
                    </span>
              </div>
          }
        </div>
    )
  }
}
class SecondStep extends React.Component {
  constructor(props){
    super(props);
  }
  render(){
    const username = this.props.username;
    const channel = this.props.channel;

    return (
        <div class="contents" id="second_step">
          <div class="content_row">
            <span>
              You are adding <strong>{username}</strong> to #{channel.name}
            </span>
          </div>
          <div class="content_row">
            <h1 class="full_width" style={{marginBottom:0}}>Share apps from #{channel.name} to <strong>{username}</strong></h1>
          </div>
          <div class="content_row">
            <span>Select/deselect the ones you want to share/don't share.</span>
          </div>
          <div class="content_row flex_direction_column">
            {
              channel.apps.map(function(item){
                if (item.type === 'simple')
                  return (
                      <SimpleAppSharingPreview
                          key={item.id}
                          app={item}
                          users={this.props.users}
                          checkAppFunc={this.props.checkAppFunc}
                      />
                  );
                else if (item.type === 'link')
                  return (
                      <LinkAppSharingPreview
                          key={item.id}
                          app={item}
                          users={this.props.users}
                          checkAppFunc={this.props.checkAppFunc}
                      />
                  );
                else if (item.type === 'multi')
                  return (
                      <MultiAppSharingPreview
                          key={item.id}
                          app={item}
                          username={username}
                          users={this.props.users}
                          checkAppFunc={this.props.checkAppFunc}
                          handleCredentialsInput={this.props.handleCredentialsInput}
                      />
                  );
              }, this)
            }
          </div>
          <div class="content_row buttons_row">
            <div class="buttons_wrapper">
              <button class="button-unstyle action_text_button neutral_background mrgnRight5"
                      onClick={e => {this.props.dispatch(showTeamChannelAddUserModal(false))}}>
                Cancel
              </button>
              <button class="button-unstyle positive_background action_text_button next_button"
                      onClick={this.props.validate}>
                Next
              </button>
            </div>
          </div>
        </div>
    )
  }
}

@connect((store)=>{
  return {
    selectedChannelId: store.selection.id,
    users: store.users.users,
    channels: store.channels.channels,
    modal : store.teamModals.teamChannelAddUserModal,
    team_id: store.team.id
  };
})
class TeamChannelAddUserModal extends React.Component{
  constructor(props){
    super(props);
    this.state = {
      users: [],
      channel: null,
      choosenUser: null
    };

    const channel = selectChannelFromListById(this.props.channels, this.props.selectedChannelId);
    this.state.users = this.props.users.filter(function(item){
      return channel.userIds.indexOf(item.id) < 0;
    }.bind(this));
    this.state.channel = selectChannelFromListById(this.props.channels, this.props.modal.channel_id);
    this.stepBack = this.stepBack.bind(this);
    this.selectUser = this.selectUser.bind(this);
    this.checkApp = this.checkApp.bind(this);
    this.handleCredentialsInput = this.handleCredentialsInput.bind(this);
    this.validateModal = this.validateModal.bind(this);
  }
  validateModal(){
    var shareApps = [];
    const channel = this.state.channel;
    const user = this.state.choosenUser;
    const apps = channel.apps.filter(function(item){
      return item.selected;
    });

    this.props.dispatch(addTeamUserToChannel(channel.id, user.id)).then(() => {
      shareApps = apps.map(function(item){
        return this.props.dispatch(teamShareApp(item.id, {team_user_id: user.id, account_information: item.credentials}));
      }, this);
      Promise.all(shareApps).then(values => {
        this.props.dispatch(showTeamChannelAddUserModal(false));
      });
    });
  }
  handleCredentialsInput(app_id, credentialName, credentialValue){
    var channel = {...this.state.channel};
        for (var i = 0; i < channel.apps.length; i++){
          if (channel.apps[i].id === app_id){
            channel.apps[i].credentials[credentialName] = credentialValue;
            break;
          }
        }
    this.setState({channel: channel});
  }
  checkApp(app_id){
    var channel = {...this.state.channel};

    for (var i = 0; i < channel.apps.length; i++) {
      if (channel.apps[i].id === app_id) {
        channel.apps[i].selected = !channel.apps[i].selected;
        break;
      }
    }
    this.setState({channel: channel});
  }
  stepBack(){
    this.setState({choosenUser: null});
  }
  selectUser(id){
    var channel = {
        ...this.state.channel
    };
    var user = null;

    for (var i = 0; i < this.state.users.length; i++){
      if (this.state.users[i].id === id){
        user = this.state.users[i];
        break;
      }
    }
    api.fetchTeamChannelApps(this.props.team_id, this.state.channel.id).then(response => {
      var apps = response.map(function(item){
        item.selected = false;
        if (item.type === 'multi'){
          item.credentials = {};
          Object.keys(item.website.information).map(function (info) {
            item.credentials[info] = '';
          });
        }
        return item;
      });
      channel.apps = apps;
      this.setState({channel: channel,choosenUser:user});
    });
  }
  render(){
    return (
        <div className="ease_modal" id="team_channel_add_user_modal">
          <div className="modal-background"></div>
          {this.state.choosenUser != null &&
          <a id="ease_modal_back_btn" class="ease_modal_btn"
             onClick={this.stepBack}>
            <i class="ease_icon fa fa-step-backward"/>
            <span class="key_label">back</span>
          </a>
          }
          <a id="ease_modal_close_btn" className="ease_modal_btn" onClick={e => {this.props.dispatch(showTeamChannelAddUserModal(false))}}>
            <i className="ease_icon fa fa-times"/>
            <span className="key_label">close</span>
          </a>
          <div className="modal_contents_container">
            {this.state.choosenUser === null ?
                <FirstStep
                  users={this.state.users}
                  selectUser={this.selectUser}/>
                :
                <SecondStep
                  channel={this.state.channel}
                  username={this.state.choosenUser.username}
                  checkAppFunc={this.checkApp}
                  users={this.props.users}
                  handleCredentialsInput={this.handleCredentialsInput}
                  validate={this.validateModal}
                  dispatch={this.props.dispatch}/>
            }
          </div>
        </div>
    )
  }
}

module.exports = TeamChannelAddUserModal;