import React from 'react';
import {setWSId} from '../../actions/commonActions';
import {newNotification} from "../../actions/notificationsActions";
import Websocket from 'react-websocket';
import {connect} from "react-redux";

function teamAppDispatcher(action, app, target){
  return function (dispatch, getState){
      dispatch({type: 'TEAM_APP_' + action, payload: {app: app, target: target}});
  }
}

function teamUserDispatcher(action, user, target){
  return function (dispatch, getState){
    const state = getState();
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
        this.props.dispatch(this.listeners[data.type](data.action, data.data, data.target));
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