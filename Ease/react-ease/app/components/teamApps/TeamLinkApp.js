var React = require('react');
var classnames = require('classnames');
var TeamAppUserSelectDropdown = require('./TeamAppUserSelectDropdown');
var RequestAppButton = require('./RequestAppButton');
import * as appActions from "../../actions/appsActions";
import * as modalActions from "../../actions/teamModalActions"
import {
    selectUserFromListById,
    getChannelUsers,
    findMeInReceivers,
    getReceiverInList,
    isUserInList
} from "../../utils/helperFunctions"

function TeamLinkAppButtonSet(props) {
  const app = props.app;
  const me = props.me;
  const meReceiver = findMeInReceivers(app.receivers, me.id);
  const meSender = app.sender_id === me.id;

  return (
      <div class="team_app_actions_holder">
        <button class="button-unstyle team_app_requests"
                data-tip="User(s) would like to access this app"
                onClick={e => {props.dispatch(modalActions.showTeamManageAppRequestModal(true, app))}}>
          <i class="fa fa-user"/>
        </button>
        {meReceiver != null &&
        <button class="button-unstyle team_app_pin"
                data-tip="Pin App in your Personal space"
                onClick={e => {props.dispatch(modalActions.showPinTeamAppToDashboardModal(true, app))}}>
          <i class="fa fa-thumb-tack"/>
        </button>}
        {meReceiver != null &&
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

class TeamLinkApp extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      modifying : false,
      modifiedAppName: '',
      modifiedUrl: '',
      modifiedComment: '',
      selectedReceivers: [],
      receivers: []
    };
    this.setupModifying = this.setupModifying.bind(this);
    this.validateModifying = this.validateModifying.bind(this);
    this.handleAppNameInput = this.handleAppNameInput.bind(this);
    this.handleCommentInput = this.handleCommentInput.bind(this);
    this.handleUrlInput = this.handleUrlInput.bind(this);
    this.selectReceiver = this.selectReceiver.bind(this);
    this.deselectReceiver = this.deselectReceiver.bind(this);
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
  selfJoinApp(){
    this.props.dispatch(appActions.teamShareApp(this.props.app.id, {team_user_id:this.props.me.id}));
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

      this.props.app.receivers.map(function(item){
        var user = selectUserFromListById(receivers, item.team_user_id);
        user.selected = true;
        user.accepted = item.accepted;
        user.can_see_information = item.can_see_information;
        selectedReceivers.push(user);
      }, this);

      this.setState({
        modifying: state,
        modifiedAppName: this.props.app.name,
        modifiedUrl: this.props.app.url,
        modifiedComment: this.props.app.description,
        receivers: receivers,
        selectedReceivers: selectedReceivers
      });
    }else {
      this.setState({modifying: false});
    }
  }
  validateModifying(){
    console.log("validate modifying");
    var app_info = {
      name: this.state.modifiedAppName,
      description: this.state.modifiedComment,
      url: this.state.modifiedUrl
    };
    var addReceiverList = [];
    var deleteReceiverList = [];

    for (var i = 0; i < this.state.selectedReceivers.length; i++){
      var receiver = getReceiverInList(this.props.app.receivers, this.state.selectedReceivers[i].id);
      if (!receiver)
        addReceiverList.push(this.state.selectedReceivers[i]);
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
        return this.props.dispatch(appActions.teamShareApp(this.props.app.id, {team_user_id:item.id}));
      }, this);
      var concatCalls = deleteUsers.concat(addUsers);
      Promise.all(concatCalls).then(() => {
        this.setupModifying(false);
      });
    });
  }
  handleUrlInput(e){
    this.setState({modifiedUrl: e.target.value});
  }
  handleAppNameInput(e){
    this.setState({modifiedAppName: e.target.value});
  }
  handleCommentInput(e){
    this.setState({modifiedComment: e.target.value});
  }
  render(){
    const app = this.props.app;
    const senderUser = selectUserFromListById(this.props.users, app.sender_id);
    const me = this.props.me;
    const meReceiver = findMeInReceivers(app.receivers, me.id);

    return(
        <div class={classnames('team_app_holder', this.state.modifying ? "active":null)}>
          {!this.state.modifying &&
          <TeamLinkAppButtonSet
              app={app}
              me={me}
              setupModifying={this.setupModifying}
              dispatch={this.props.dispatch}/>
          }
          <div class="display-flex team_app_indicators">
            {!this.state.modifying && meReceiver !== null && meReceiver.profile_id !== -1 &&
            <span>
                    <i class="fa fa-thumb-tack"/>
                  </span>
            }
          </div>
          <div class="team_app_sender_info">
            <span class="team_app_sender_name">
              <i class="fa fa-user mrgnRight5"/>
              {senderUser.username}
              {me.id === senderUser.id && "(you)"}
            </span>
            <span>&nbsp;sent a Link App</span>
          </div>
          <div class="team_app">
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
                  <img src="/resources/icons/app_icon.svg" alt="logo"/>
                </div>
                <div class="credentials_holder">
                  <div class="credentials">
                    {!this.state.modifying && meReceiver === null && me.id !== app.sender_id && me.role === 1 &&
                    <RequestAppButton/>}
                    {!this.state.modifying && meReceiver === null && (me.role > 1 || me.id === app.sender_id) &&
                    <button class="button-unstyle joinAppBtn"
                            onClick={this.selfJoinApp}>
                      Join app
                    </button>}
                    {!this.state.modifying && meReceiver != null && meReceiver.accepted &&
                    <div class="credentials_line">
                      <i class="fa fa-home mrgnRight5"/>
                      <div class="credentials_value_holder">
                        <span class="credentials_value">
                              {app.url}
                            </span>
                      </div>
                    </div>}
                    {this.state.modifying &&
                    <div class="credentials_line">
                      <i class="fa fa-home mrgnRight5" data-tip="Link url"/>
                      <div class="credentials_value_holder">
                        <input autoComplete="off"
                               class="credentials_value_input value_input"
                               value={this.state.modifiedUrl}
                               onChange={this.handleUrlInput}
                               placeholder="Your url"
                               type="url"
                               name="url"/>
                      </div>
                    </div>}
                  </div>
                </div>
              </div>
              <div class="sharing_info full_flex">
                <div class="receivers_wrapper full_flex">
                  {!this.state.modifying ?
                      app.receivers.map(function (item) {
                        const user = selectUserFromListById(this.props.users, item.team_user_id);
                        return (
                            <div class={classnames("receiver", item.accepted ? "accepted": null)} key={item.team_user_id}>
                              <span class="receiver_name">
                              {user.username}
                                {me.id === user.id && "(you)"}
                              </span>
                            </div>
                        )
                      }, this)
                      :
                      <TeamAppUserSelectDropdown
                          receivers={this.state.receivers}
                          selectedReceivers={this.state.selectedReceivers}
                          myId={me.id}
                          selectFunc={this.selectReceiver}
                          deselectFunc={this.deselectReceiver}
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

module.exports = TeamLinkApp;