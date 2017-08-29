var React = require('react');
var classnames = require('classnames');
var TeamAppUserSelectDropdown = require('./TeamAppUserSelectDropdown');
var RequestAppButton = require('./RequestAppButton');
import AppReceiverTooltip from "../teams/AppReceiverTooltip";
import * as appActions from "../../actions/appsActions";
import * as modalActions from "../../actions/teamModalActions"
import {
    selectUserFromListById,
    getChannelUsers,
    findMeInReceivers,
    getReceiverInList,
    isUserInList,
    passwordChangeValues
} from "../../utils/helperFunctions"
import { Button, Icon } from 'semantic-ui-react';

function TeamSimpleAppButtonSet(props) {
  const app = props.app;
  const me = props.me;
  const meReceiver = findMeInReceivers(app.receivers, me.id);
  const meSender = app.sender_id === me.id;

  return (
      <div class="team_app_actions_holder">
        {app.sharing_requests.length > 0 &&
        <button class="button-unstyle team_app_requests"
                data-tip="User(s) would like to access this app"
                onClick={e => {
                  props.dispatch(modalActions.showTeamManageAppRequestModal(true, app))
                }}>
          <i class="fa fa-user"/>
        </button>}
        {meReceiver != null && meReceiver.accepted &&
        <button class="button-unstyle team_app_pin"
                data-tip="Pin App in your Personal space"
                onClick={e => {props.dispatch(modalActions.showPinTeamAppToDashboardModal(true, app))}}>
          <i class="fa fa-thumb-tack"/>
        </button>}
        {meReceiver != null && meReceiver.accepted &&
        <button class="button-unstyle team_app_leave"
                data-tip="Leave App"
                onClick={e => {props.dispatch(modalActions.showTeamLeaveAppModal(true, app, me.id))}}>
          <i class="fa fa-sign-out"/>
        </button>}
        {(meSender || me.role > 1) &&
        <button class="button-unstyle team_app_edit"
                data-tip="Edit App"
                onClick={props.setupModifying.bind(null, true)}>
          <i class="fa fa-pencil"/>
        </button>}
        {(meSender || me.role > 1) &&
        <button class="button-unstyle team_app_delete"
                data-tip="Delete App"
                onClick={e => {props.dispatch(modalActions.showTeamDeleteAppModal(true, app))}}>
          <i class="fa fa-trash"/>
        </button>}
      </div>
  )
}

function SimpleAppReceiverTag(props){
  return (
      <div class={classnames("receiver", props.item.accepted ? "accepted": null)}>
                              <span class="receiver_name">
                              {props.user.username}
                                {props.myId === props.user.id && "(you)"}
                              </span>
        <i class={classnames("fa", "mrgnLeft5", props.item.can_see_information ? "fa-eye": "fa-eye-slash")}/>
      </div>
  )
}

function SimpleAppReceiverTagDesc(props){
  const rcvr = props.receiver;
  return (
      <div class="app_receiver_desc display-flex flex_direction_column">
        <span class="title">User information</span>
        <div class="display-flex align_items_center">
          <i class={classnames('fa mrgnRight5 see_password_icon', rcvr.can_see_information ? 'fa-eye' : 'fa-eye-slash')}/>
          <span>User can{rcvr.can_see_information ? null: 'not'} see the password</span>
        </div>
        <div class="display-flex align_items_center">
          <i class={classnames('fa fa-circle mrgnRight5 accept_icon', rcvr.accepted ? 'accepted': null)}/>
          <span>
            {rcvr.accepted ?
                "User accepted the app"
                :
                "User didn't accept the app"}
          </span>
        </div>
      </div>
  )
}

class TeamSimpleApp extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      modifying : false,
      modifiedAppName: '',
      modifiedPasswordChangeInterval: '',
      modifiedComment: '',
      modifiedCredentials: {},
      selectedReceivers: [],
      receivers: []
    };

    this.setupModifying = this.setupModifying.bind(this);
    this.validateModifying = this.validateModifying.bind(this);
    this.handleCommentInput = this.handleCommentInput.bind(this);
    this.handlePasswordChangeIntervalInput = this.handlePasswordChangeIntervalInput.bind(this);
    this.handleAppNameInput = this.handleAppNameInput.bind(this);
    this.handleCredentialsInput = this.handleCredentialsInput.bind(this);
    this.selectReceiver = this.selectReceiver.bind(this);
    this.deselectReceiver = this.deselectReceiver.bind(this);
    this.handleReceiverCanSeePassword = this.handleReceiverCanSeePassword.bind(this);
    this.acceptRequest = this.acceptRequest.bind(this);
    this.selfJoinApp = this.selfJoinApp.bind(this);
  }
  handleReceiverCanSeePassword(id){
    var receivers = this.state.receivers;

    for (var i = 0; i < receivers.length; i++){
      if (receivers[i].id === id){
        receivers[i].can_see_information = !receivers[i].can_see_information;
        this.setState({
          receivers: receivers
        });
        return;
      }
    }
  }
  selfJoinApp(){
    this.props.dispatch(appActions.teamShareApp(this.props.app.id, {team_user_id:this.props.me.id, can_see_information: true})).then(response => {
      this.props.dispatch(appActions.teamAcceptSharedApp(this.props.app.id, response.shared_app_id)).then(() => {
        this.props.dispatch(modalActions.showPinTeamAppToDashboardModal(true, this.props.app));
      });
    });
  }
  selectReceiver(id){
    var selectedReceivers = this.state.selectedReceivers;
    var receivers = this.state.receivers;

    for (var i = 0; i < receivers.length; i++){
      if (receivers[i].id === id){
        if (receivers[i].selected)
          return;
        receivers[i].selected = true;
        if (receivers[i].can_see_information === undefined)
          receivers[i].can_see_information = false;
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
  handleCredentialsInput(e){
    var credentials = {...this.state.modifiedCredentials};
    credentials[e.target.name] = e.target.value;
    this.setState({modifiedCredentials : credentials});
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
  acceptRequest(state){
    const app = this.props.app;
    const me = this.props.me;
    const meReceiver = findMeInReceivers(app.receivers, me.id);
    if (state) {
      this.props.dispatch(appActions.teamAcceptSharedApp(app.id, meReceiver.shared_app_id)).then(() => {
        this.props.dispatch(modalActions.showPinTeamAppToDashboardModal(true, app));
      });
    }
    else
      this.props.dispatch(appActions.teamAppDeleteReceiver(app.id,meReceiver.shared_app_id,meReceiver.team_user_id));
  }
  validateModifying(){
    console.log("validate modifying");
    var app_info = {
      name: this.state.modifiedAppName,
      description: this.state.modifiedComment,
      password_change_interval: this.state.modifiedPasswordChangeInterval,
      account_information: {
        ...this.state.modifiedCredentials
      }
    };
    var addReceiverList = [];
    var deleteReceiverList = [];
    var modifyReceiverList = [];

    for (var i = 0; i < this.state.selectedReceivers.length; i++){
      var receiver = getReceiverInList(this.props.app.receivers, this.state.selectedReceivers[i].id);
      if (!receiver)
        addReceiverList.push(this.state.selectedReceivers[i]);
      else if (receiver.can_see_information != this.state.selectedReceivers[i].can_see_information)
        modifyReceiverList.push({...this.state.selectedReceivers[i], shared_app_id: receiver.shared_app_id});
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
        return this.props.dispatch(appActions.teamShareApp(this.props.app.id, {team_user_id:item.id, can_see_information: item.can_see_information}));
      }, this);
      var editUsers = modifyReceiverList.map(function(item){
        return this.props.dispatch(appActions.teamAppEditReceiver(this.props.app.id,item.shared_app_id, {can_see_information:item.can_see_information, team_user_id: item.id}))
      }, this);
      var concatCalls = deleteUsers.concat(addUsers, editUsers);
      Promise.all(concatCalls).then(() => {
        this.setupModifying(false);
      });
    });
  }
  setupModifying(state){
    if (state) {
      var modifiedCredentials = {...this.props.app.account_information};
      var receivers = [];
      var selectedReceivers = [];
      if (this.props.app.origin.type === 'channel'){
        receivers = getChannelUsers(this.props.channels, this.props.app.origin.id, this.props.users).map(function(item){
          return {...item};
        });
      } else {
        receivers = [{...selectUserFromListById(this.props.users, this.props.app.origin.id)}];
      }

      this.props.app.receivers.map(function(item){
        var user = selectUserFromListById(receivers, item.team_user_id);
        user.selected = true;
        user.accepted = item.accepted;
        user.can_see_information = item.can_see_information;
        selectedReceivers.push(user);
      });

      this.setState({
        modifying: state,
        modifiedAppName: this.props.app.name,
        modifiedComment: this.props.app.description,
        modifiedPasswordChangeInterval: this.props.app.password_change_interval.toString(),
        modifiedCredentials: modifiedCredentials,
        receivers: receivers,
        selectedReceivers: selectedReceivers
      });
    }else {
      this.setState({modifying: false});
    }
  }
  render(){
    const app = this.props.app;
    const senderUser = selectUserFromListById(this.props.users, app.sender_id);
    const me = this.props.me;
    const webInfo = app.website.information;
    const meReceiver = findMeInReceivers(app.receivers, me.id);
    const asked = app.sharing_requests.indexOf(me.id) !== -1;


    return(
        <div class={classnames('team_app_holder', this.state.modifying ? "active":null)}>
          {(!this.state.modifying && (meReceiver === null || meReceiver.accepted)) &&
          <TeamSimpleAppButtonSet
              app={app}
              me={me}
              setupModifying={this.setupModifying}
              dispatch={this.props.dispatch}/>}
          <div class="display-flex team_app_indicators">
            {!this.state.modifying && meReceiver !== null && meReceiver.profile_id !== -1 &&
            <span>
                    <i class="fa fa-thumb-tack"/>
                  </span>}
            {!this.state.modifying && app.sharing_requests.length > 0 && (me.id === app.sender_id || isAdmin(me.role)) &&
            <span>
              <i class="fa fa-user"/>
            </span>}
          </div>
          <div class="team_app_sender_info">
            <span class="team_app_sender_name">
              <i class="fa fa-user mrgnRight5"/>
              {senderUser.username}
              {me.id === senderUser.id && "(you)"}
            </span>
            {meReceiver != null && !meReceiver.accepted ?
                <span>
              &nbsp;tagged you in a Single App,&nbsp;
                  <button class="button-unstyle accept_app_btn" onClick={this.acceptRequest.bind(null, true)}>
                Accept
              </button>
                  &nbsp;or&nbsp;
                  <button class="button-unstyle accept_app_btn" onClick={this.acceptRequest.bind(null, false)}>
                Refuse
              </button>
                  &nbsp;it ?
            </span>
                :
                <span>&nbsp;sent a Single App</span>}
          </div>
          <div class="team_app">
            {meReceiver != null && !meReceiver.accepted &&
            <div class="custom-overlay"></div>}
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
                    {(meReceiver != null || this.state.modifying) &&
                    Object.keys(app.account_information).reverse().map(function(item){
                      return (
                          <div class="credentials_line" key={item}>
                            <div class="credentials_type_icon">
                              <i class={classnames('fa', webInfo[item].placeholderIcon)} data-tip={"App " + item}/>
                            </div>
                            <div class="credentials_value_holder">
                              {!this.state.modifying ?
                                  <span class="credentials_value">
                                      {item != 'password' ?
                                          app.account_information[item]
                                          :
                                          '********'}
                                  </span>
                                  :
                                  <input autoComplete="off"
                                         class="credentials_value_input value_input"
                                         value={this.state.modifiedCredentials[item]}
                                         onChange={this.handleCredentialsInput}
                                         placeholder={webInfo[item].placeholder}
                                         type={webInfo[item].type}
                                         name={item}/>
                              }
                            </div>
                          </div>
                      )
                    }, this)}
                    {!this.state.modifying && meReceiver === null && me.id !== app.sender_id && me.role === 1 && asked &&
                    <button class="button-unstyle requestAppButton">
                      <span class="onHover">Request sent</span>
                      <span class="default">Request sent</span>
                    </button>}
                    {!this.state.modifying && meReceiver === null && me.id !== app.sender_id && me.role === 1 && !asked &&
                    <button class="button-unstyle requestAppButton"
                            onClick={e => {this.props.dispatch(appActions.askJoinTeamApp(app.id))}}>
                      <span class="onHover">Ask to access</span>
                      <span class="default">This app does not concern you</span>
                    </button>}
                    {!this.state.modifying && meReceiver === null && (me.role > 1 || me.id === app.sender_id) &&
                    <button class="button-unstyle joinAppBtn"
                            onClick={this.selfJoinApp}>
                      Join app
                    </button>}
                  </div>
                  {meReceiver != null && meReceiver.accepted &&
                  <div class="password_change_remind">
                    <div class="password_change_icon">
                      <i class="fa fa-clock-o" data-tip="Password update frequency"/>
                    </div>
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
              <div class="sharing_info full_flex">
                <div class="receivers_wrapper full_flex">
                  {!this.state.modifying ?
                      app.receivers.map(function (item) {
                        const user = selectUserFromListById(this.props.users, item.team_user_id);
                        return (
                            <AppReceiverTooltip content={[<SimpleAppReceiverTagDesc receiver={item} key="1"/>]} key={item.team_user_id}>
                              <SimpleAppReceiverTag  item={item} user={user} myId={me.id} />
                            </AppReceiverTooltip>
                        )
                      }, this)
                      :
                      <TeamAppUserSelectDropdown
                          receivers={this.state.receivers}
                          selectedReceivers={this.state.selectedReceivers}
                          myId={me.id}
                          selectFunc={this.selectReceiver}
                          deselectFunc={this.deselectReceiver}
                          changeReceiverSeePasswordPermFunc={this.handleReceiverCanSeePassword}/>
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

module.exports = TeamSimpleApp;