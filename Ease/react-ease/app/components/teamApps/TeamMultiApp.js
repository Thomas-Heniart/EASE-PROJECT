var React = require('react');
var classnames = require('classnames');
var TeamMultiAppUserSelect = require('./TeamMultiAppUserSelect');
var RequestAppButton = require('./RequestAppButton');
import * as appActions from "../../actions/appsActions";
import * as modalActions from "../../actions/teamModalActions";
import {
    selectUserFromListById,
    getChannelUsers,
    findMeInReceivers,
    getReceiverInList,
    isUserInList,
    passwordChangeValues
} from "../../utils/helperFunctions"

function TeamMultiAppButtonSet(props) {
  const app = props.app;
  const me = props.me;
  const meReceiver = findMeInReceivers(app.receivers, me.id);
  const meSender = app.sender_id === me.id;

  return (
      <div class="team_app_actions_holder">
        <button class="button-unstyle team_app_requests" onClick={e => {props.dispatch(modalActions.showTeamManageAppRequestModal(true, app))}}>
          <i class="fa fa-user"/>
        </button>
        {meReceiver != null &&
        <button class="button-unstyle team_app_leave" onClick={e => {props.dispatch(modalActions.showTeamLeaveAppModal(true, app, me.id))}}>
          <i class="fa fa-sign-out"/>
        </button>}
        {meReceiver != null &&
        <button class="button-unstyle team_app_pin" onClick={e => {props.dispatch(modalActions.showPinTeamAppToDashboardModal(true, app))}}>
          <i class="fa fa-thumb-tack"/>
        </button>}
        {(meSender || me.role > 1) &&
        <button class="button-unstyle team_app_edit" onClick={props.setupModifying.bind(null, true)}>
          <i class="fa fa-pencil"/>
        </button>}
        {(meSender || me.role > 1) &&
        <button class="button-unstyle team_app_delete" onClick={e => {props.dispatch(modalActions.showTeamDeleteAppModal(true, app))}}>
          <i class="fa fa-trash"/>
        </button>}
      </div>
  )
}

class TeamMultiApp extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      modifying : false,
      modifiedAppName: '',
      modifiedPasswordChangeInterval: '',
      modifiedComment: '',
      selectedReceivers: [],
      receivers: []
    };
    this.setupModifying = this.setupModifying.bind(this);
    this.validateModifying = this.validateModifying.bind(this);
    this.handleAppNameInput = this.handleAppNameInput.bind(this);
    this.handleCommentInput = this.handleCommentInput.bind(this);
    this.handlePasswordChangeIntervalInput = this.handlePasswordChangeIntervalInput.bind(this);
    this.handleUserCredentialInput = this.handleUserCredentialInput.bind(this);
    this.selectReceiver = this.selectReceiver.bind(this);
    this.deselectReceiver = this.deselectReceiver.bind(this);
    this.acceptRequest = this.acceptRequest.bind(this);
    this.selfJoinApp = this.selfJoinApp.bind(this);
  }
  selectReceiver(id){
    var selectedReceivers = this.state.selectedReceivers;
    var receivers = this.state.receivers;

    for (var i = 0; i < receivers.length; i++){
      if (receivers[i].id === id){
        if (receivers[i].selected)
          return;
        receivers[i].selected = true;
        selectedReceivers.push(receivers[i]);
        this.setState({
          receivers: receivers,
          selectedReceivers: selectedReceivers
        });
        return;
      }
    }
  }
  deselectReceiver(id){
    var selectedReceivers = this.state.selectedReceivers;
    var receivers = this.state.receivers;

    for (var i = 0; i < receivers.length; i++){
      if (receivers[i].id === id){
        if (!receivers[i].selected)
          return;
        receivers[i].selected = false;
        selectedReceivers.splice(selectedReceivers.indexOf(receivers[i]), 1);
        this.setState({
          receivers: receivers,
          selectedReceivers: selectedReceivers
        });
        return;
      }
    }
  }
  setupModifying(state){
    if (state) {
      var receivers = [];
      var selectedReceivers = [];
      if (this.props.app.origin.type === 'channel'){
        receivers = getChannelUsers(this.props.channels, this.props.app.origin.id, this.props.users).map(function(item){
          return {...item};
        });
      } else {
        receivers = [{...selectUserFromListById(this.props.users, this.props.app.origin.id)}];
      }
      receivers = receivers.map(function (receiver) {
        receiver.credentials = {};
        Object.keys(this.props.app.website.information).map(function (item) {
          receiver.credentials[item] = '';
        });
        return receiver;
      }, this);
      this.props.app.receivers.map(function(item){
        var user = selectUserFromListById(receivers, item.team_user_id);
        user.selected = true;
        user.accepted = item.accepted;
        user.credentials = {...item.account_information};
        selectedReceivers.push(user);
      }, this);

      this.setState({
        modifying: state,
        modifiedAppName: this.props.app.name,
        modifiedComment: this.props.app.description,
        modifiedPasswordChangeInterval: this.props.app.password_change_interval.toString(),
        receivers: receivers,
        selectedReceivers: selectedReceivers
      });
    }else {
      this.setState({modifying: false});
    }
  }
  acceptRequest(state){
    const app = this.props.app;
    const me = this.props.me;
    const meReceiver = findMeInReceivers(app.receivers, me.id);
    if (state)
      this.props.dispatch(modalActions.showTeamAcceptMultiAppModal(true, me, app));
    else
      this.props.dispatch(appActions.teamAppDeleteReceiver(app.id,meReceiver.shared_app_id,meReceiver.team_user_id));
  }
  validateModifying(){
    console.log("validate modifying");
    var app_info = {
      name: this.state.modifiedAppName,
      description: this.state.modifiedComment,
      password_change_interval: this.state.modifiedPasswordChangeInterval
    };
    var addReceiverList = [];
    var deleteReceiverList = [];
    var modifyReceiverList = [];

    for (var i = 0; i < this.state.selectedReceivers.length; i++){
      var receiver = getReceiverInList(this.props.app.receivers, this.state.selectedReceivers[i].id);
      if (!receiver)
        addReceiverList.push(this.state.selectedReceivers[i]);
      else {
        var keys = Object.keys(receiver.account_information);
        for (var j = 0; j < keys.length; j++){
          if (receiver.account_information[keys[j]] != this.state.selectedReceivers[i].credentials[keys[j]]){
            modifyReceiverList.push({...this.state.selectedReceivers[i], shared_app_id: receiver.shared_app_id});
            break;
          }
        }
      }
    }
    for (var i = 0; i < this.props.app.receivers.length; i++){
      if (!isUserInList(this.state.selectedReceivers, this.props.app.receivers[i].team_user_id))
        deleteReceiverList.push(this.props.app.receivers[i]);
    }
    this.props.dispatch(appActions.teamModifyAppInformation(this.props.app.id, app_info)).then(response => {
      var deleteUsers = deleteReceiverList.map(function(item){
        return this.props.dispatch(appActions.teamAppDeleteReceiver(this.props.app.id, item.shared_app_id, item.team_user_id));
      }, this);
      var addUsers = addReceiverList.map(function(item){
        return this.props.dispatch(appActions.teamShareApp(this.props.app.id, {account_information: item.credentials, team_user_id: item.id}));
      }, this);
      var editUsers = modifyReceiverList.map(function(item){
        return this.props.dispatch(appActions.teamAppEditReceiver(this.props.app.id,item.shared_app_id, {account_information: item.credentials, team_user_id: item.id}))
      }, this);
      var concatCalls = deleteUsers.concat(addUsers, editUsers);
      this.setupModifying(false);
    });
  }
  selfJoinApp(){
    this.props.dispatch(modalActions.showTeamJoinMultiAppModal(true,this.props.me, this.props.app));
  }
  handleUserCredentialInput(user_id, credentialName, value){
    var selectedReceivers = this.state.selectedReceivers;

    for (var i = 0; i < selectedReceivers.length; i++){
      if (selectedReceivers[i].id === user_id){
        selectedReceivers[i].credentials[credentialName] = value;
        this.setState({selectedReceivers: selectedReceivers});
      }
    }
  }
  handleAppNameInput(e){
    this.setState({modifiedAppName: e.target.value});
  }
  handleCommentInput(e){
    this.setState({modifiedComment: e.target.value});
  }
  handlePasswordChangeIntervalInput(e){
    this.setState({modifiedPasswordChangeInterval: e.target.value});
  }
  render(){
    const app = this.props.app;
    const senderUser = selectUserFromListById(this.props.users, app.sender_id);
    const me = this.props.me;
    const meReceiver = findMeInReceivers(app.receivers, me.id);
    const webInfo = app.website.information;

    return(
        <div class={classnames('team_app_holder', this.state.modifying ? "active":null)}>
          {!this.state.modifying &&
          <TeamMultiAppButtonSet
              app={app}
              me={me}
              setupModifying={this.setupModifying}
              dispatch={this.props.dispatch}/>
          }
          <div class="team_app_sender_info">
            <span class="team_app_sender_name">
              <i class="fa fa-user mrgnRight5"/>
              {senderUser.username}
              {me.id === senderUser.id && "(you)"}
            </span>
            <span>&nbsp;shared on&nbsp;{app.shared_date}
            </span>
          </div>
          <div class="team_app multiple_accounts_app">
            <div class="name_holder">
              {!this.state.modifying ?
                  app.name :
                  <input class="name_input" type="text" name="app_name"
                         value={this.state.modifiedAppName}
                         onChange={this.handleAppNameInput}/>
              }
            </div>
            <div class="info_holder">
              <div class="info">
                <div class="logo_holder">
                  <img src={app.website.logo} alt="logo"/>
                </div>
                <div class="credentials_holder">
                  <div class="credentials">
                    {meReceiver !== null &&
                    Object.keys(meReceiver.account_information).map(function(item){
                      return (
                          <div class="credentials_line" key={item}>
                            <div class="credentials_type_icon">
                              <i class={classnames('fa', webInfo[item].placeholderIcon)}/>
                            </div>
                            <div class="credentials_value_holder">
                                  <span class="credentials_value">
                                    {meReceiver.account_information[item]}
                                  </span>
                            </div>
                          </div>
                      )
                    }, this)}
                    {!this.state.modifying && meReceiver === null && me.id !== app.sender_id && me.role === 1 &&
                    <RequestAppButton/>}
                    {!this.state.modifying && meReceiver === null && (me.role > 1 || me.id === app.sender_id) &&
                    <button class="button-unstyle joinAppBtn"
                            onClick={this.selfJoinApp}>
                      Join app
                    </button>}
                    {meReceiver != null && !meReceiver.accepted &&
                    <div>
                      <button class="accept_request_btn button-unstyle action_text_button positive_background mrgnRight5"
                              onClick={this.acceptRequest.bind(null, true)}>
                        Accept
                      </button>
                      <button class="accept_request_btn button-unstyle action_text_button neutral_background"
                              onClick={this.acceptRequest.bind(null, false)}>
                        Refuse
                      </button>
                    </div>}
                  </div>
                  {meReceiver !== null && meReceiver.accepted &&
                  <div class="password_change_remind">
                    <div class="password_change_icon"><i class="fa fa-clock-o"/></div>
                    {!this.state.modifying ?
                        <div class="password_change_info">
                          {passwordChangeValues[app.password_change_interval]}
                        </div>
                        :
                        <select class="select_unstyle" value={this.state.modifiedPasswordChangeInterval} onChange={this.handlePasswordChangeIntervalInput}>
                          {
                            Object.keys(passwordChangeValues).map(function(item){
                              return (
                                  <option value={item} key={item}>{passwordChangeValues[item]}</option>
                              )
                            })
                          }
                        </select>
                    }
                  </div>}
                </div>
              </div>
              <div class="sharing_info display_flex full_flex flex_direction_column">
                <div class="receivers_wrapper full_flex">
                  {!this.state.modifying ?
                      app.receivers.map(function(item){
                        const user = selectUserFromListById(this.props.users, item.team_user_id);
                        return (
                            <div class="receiver_wrapper" key={item.team_user_id}>
                              <div class={classnames("receiver", item.accepted ? "accepted": null)}>
                              <span class="receiver_name">
                              {user.username}
                                {me.id === user.id && "(you)"}
                              </span>
                                <i class="fa fa-unlock-alt mrgnLeft5"/>
                              </div>
                              <div class="credentials">
                                {
                                  Object.keys(item.account_information).map(function(info){
                                    return (
                                        <div class="credential_container" key={info}>
                                          <i class={classnames('fa', 'mrgnRight5', webInfo[info].placeholderIcon)}/>
                                          <span class="value">
                                            {item.account_information[info]}
                                          </span>
                                        </div>
                                    )
                                  })
                                }
                              </div>
                            </div>
                        )
                      }, this)
                      :
                      <TeamMultiAppUserSelect
                          receivers={this.state.receivers}
                          selectedReceivers={this.state.selectedReceivers}
                          website_information={this.props.app.website.information}
                          selectUserFunc={this.selectReceiver}
                          deselectUserFunc={this.deselectReceiver}
                          handleUserCredentialInputFunc={this.handleUserCredentialInput}
                          myId={me.id}
                      />
                  }
                </div>
              </div>
            </div>
            <div class="comment_holder">
              <div class="comment_icon">
                <i class="fa fa-sticky-note-o"/>
              </div>
              <div class="comment">
                {!this.state.modifying ?
                    <span class="comment_value value">
                  {app.description.length > 0 ?
                      app.description :
                      "There is no comment for this app yet..."
                  }
                  </span>
                    :
                    <textarea class="comment_input" placeholder="Your comment..."
                              value={this.state.modifiedComment} onChange={this.handleCommentInput}/>
                }
              </div>
            </div>
            {this.state.modifying &&
            <div class="mrgnTop5">
              <button class="button-unstyle neutral_background action_text_button mrgnRight5"
                      onClick={this.setupModifying.bind(null, false)}>
                Cancel
              </button>
              <button class="button-unstyle positive_background action_text_button"
                      onClick={this.validateModifying}>
                Save changes
              </button>
            </div>
            }
          </div>
        </div>
    )
  }
}

module.exports = TeamMultiApp;