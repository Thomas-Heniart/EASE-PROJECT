import React from 'react';
import {setWSId} from '../../actions/commonActions';
import {newNotification} from "../../actions/notificationsActions";
import Websocket from 'react-websocket';
import {connect} from "react-redux";
import * as dashboardActions from "../../actions/dashboardActions";
import * as teamActions from "../../actions/teamActions";
import * as roomActions from "../../actions/channelActions";
import * as teamUserActions from "../../actions/userActions";
import * as teamCardActions from "../../actions/appsActions";

const update_client_actions = {
  'DASHBOARD_APP_CREATED': dashboardActions.dashboardAppCreatedAction,
  'DASHBOARD_APP_CHANGED': dashboardActions.dashboardAppChangedAction,
  'DASHBOARD_APP_REMOVED': dashboardActions.dashboardAppRemovedAction,
  'DASHBOARD_PROFILE_CREATED': dashboardActions.dashboardProfileCreatedAction,
  'DASHBOARD_PROFILE_CHANGED': dashboardActions.dashboardProfileChangedAction,
  'DASHBOARD_PROFILE_REMOVED': dashboardActions.dashboardProfileRemovedAction,
  'SSO_GROUP_CREATED': dashboardActions.ssoGroupCreatedAction,
  'SSO_GROUP_CHANGED': dashboardActions.ssoGroupChangedAction,
  'SSO_GROUP_REMOVED': dashboardActions.ssoGroupRemovedAction,
  'MOVE_APP': dashboardActions.moveAppAction,
  'MOVE_PROFILE': dashboardActions.moveProfileAction,
  'TEAM_CREATED': teamActions.teamCreatedAction,
  'TEAM_CHANGED': teamActions.teamChangedAction,
  'TEAM_REMOVED': teamActions.teamRemovedAction,
  'TEAM_ROOM_CREATED': roomActions.teamRoomCreatedAction,
  'TEAM_ROOM_CHANGED': roomActions.teamRoomChangedAction,
  'TEAM_ROOM_REMOVED': roomActions.teamRoomRemovedAction,
  'TEAM_USER_CREATED': teamUserActions.teamUserCreatedAction,
  'TEAM_USER_CHANGED': teamUserActions.teamUserChangedAction,
  'TEAM_USER_REMOVED': teamUserActions.teamUserRemovedAction,
  'TEAM_ROOM_REQUEST_CREATED': roomActions.teamRoomRequestCreatedAction,
  'TEAM_ROOM_REQUEST_REMOVED': roomActions.teamRoomRequestRemovedAction,
  'TEAM_ROOM_MEMBER_CREATED': roomActions.teamRoomMemberCreated,
  'TEAM_ROOM_MEMBER_REMOVED': roomActions.teamRoomMemberRemoved,
  'TEAM_CARD_CREATED': teamCardActions.teamCardCreatedAction,
  'TEAM_CARD_CHANGED': teamCardActions.teamCardChangedAction,
  'TEAM_CARD_REMOVED': teamCardActions.teamCardRemovedAction,
  'TEAM_CARD_RECEIVER_CREATED': teamCardActions.teamCardReceiverCreatedAction,
  'TEAM_CARD_RECEIVER_CHANGED': teamCardActions.teamCardReceiverChangedAction,
  'TEAM_CARD_RECEIVER_REMOVED': teamCardActions.teamCardRequestRemovedAction,
  'TEAM_CARD_REQUEST_CREATED': teamCardActions.teamCardRequestCreatedAction,
  'TEAM_CARD_REQUEST_REMOVED': teamCardActions.teamCardRequestRemovedAction
};

function teamAppDispatcher(action, app, target){
  return function (dispatch, getState){
    const state = getState();
    if (state.selection.id === target.id && state.selection.type === target.type && state.team.id === target.team_id)
      dispatch({type: 'TEAM_APP_' + action, payload: {app: app, app_id: app.id, target: target}});
  }
}

function teamUserDispatcher(action, user, target){
  return function (dispatch, getState){
    const state = getState();
    if (action === 'REMOVED' && state.team.myTeamUserId === user.id)
      window.location.href = '/';
    if (state.team.id === target.team_id)
      dispatch({type: 'TEAM_USER_' + action, payload: {user: user, target: target}});
  }
}

function teamChannelDispatcher(action, channel, target){
  return function (dispatch, getState){
    const state = getState();
    if (state.team.id === target.team_id) {
      dispatch({type: 'TEAM_ROOM_' + action, payload: {channel: channel, target: target}});
    }
  }
}

function teamDispatcher(action, team) {
  return function (dispatch, getState){
    dispatch({type: 'TEAM_' + action, payload: {team: team}});
  }
}

@connect()
class WebsocketClient extends React.Component {
  constructor(props){
    super(props);
    this.listeners = {
      'TEAM_APP': teamAppDispatcher,
      'TEAM_USER': teamUserDispatcher,
      'TEAM_ROOM': teamChannelDispatcher,
      'TEAM': teamDispatcher
    };
    this.onMessage = this.onMessage.bind(this);
  }
  onMessage(event) {
    var mess = JSON.parse(event);
    var type = mess.type;
    var data = mess.data;
    console.log(mess);
    switch (type){
      case 'UPDATE_CLIENT':
        this.props.dispatch(update_client_actions[mess.action](mess.data));
//        this.props.dispatch(this.listeners[data.type](data.action, data.data, data.target));
        break;
      case 'CONNECTION_ID':
        this.props.dispatch(setWSId(data));
        break;
      case 'NEW_NOTIFICATION':
        this.props.dispatch(newNotification(data));
        break;
    }
  }
  render() {
    const webServerUrl = `wss://${window.location.host}/webSocketServer`;

    return <Websocket url={webServerUrl} onMessage={this.onMessage}/>
  }
}

module.exports = WebsocketClient;