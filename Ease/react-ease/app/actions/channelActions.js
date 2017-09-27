var api = require('../utils/api');
var post_api = require('../utils/post_api');
import {selectChannelFromListById} from "../utils/helperFunctions";
import {autoSelectTeamItem} from "./commonActions";

export function selectTeamChannel(id){
  return function(dispatch, getState){
    dispatch({type: 'SELECT_TEAM_CHANNEL_PENDING'});
      var teamChannel = selectChannelFromListById(getState().channels.channels, id);
      return api.fetchTeamChannelApps(getState().team.id, id).then(response => {
        dispatch({type: 'SELECT_TEAM_CHANNEL_FULFILLED', payload: {channel: teamChannel, apps: response.reverse()}});
        return response;
      }).catch(err => {
      dispatch({type:'SELECT_TEAM_CHANNEL_REJECTED', payload:err});
      throw err;
    });
  }
}

export function fetchTeamChannelApps(id){
  return function(dispatch, getState){
    dispatch({type: 'FETCH_TEAM_CHANNEL_APPS_PENDING'});
    return api.fetchTeamChannelApps(getState().team.id, id).then(response => {
      dispatch({type: 'FETCH_TEAM_CHANNELS_APPS_FULFILLED', payload: {apps: response, type: 'channel', id:id}});
      return response;
    }).catch(err => {
      dispatch({type: 'FETCH_TEAM_CHANNEL_APPS_REJECTED', payload: err});
      throw err;
    });
  }
}

export function editRoomManager({team_id, channel_id, team_user_id}){
  return (dispatch, getState) => {
    return post_api.teamChannel.editRoomManager({
      team_id: team_id,
      channel_id: channel_id,
      team_user_id: team_user_id,
      ws_id: getState().common.ws_id
    }).then(response => {
      dispatch({type: 'TEAM_ROOM_CHANGED', payload: {channel: response}});
      return response;
    }).catch(err => {
      throw err;
    });
  }
}

export function fetchChannels(team_id) {
  return function(dispatch){
    dispatch({type: "FETCH_TEAM_CHANNELS_PENDING"});
    return api.fetchTeamChannels(team_id).then((response) => {
      dispatch({type: "FETCH_TEAM_CHANNELS_FULFILLED", payload: response});
    }).catch((err) => {
      dispatch({type:"FETCH_TEAM_CHANNELS_REJECTED", payload: err});
      throw err;
    });
  }
}

export function createTeamChannel(name, purpose){
  return function(dispatch, getState){
    dispatch({type: 'CREATE_TEAM_CHANNEL_PENDING'});
    return post_api.teamChannel.createChannel(getState().common.ws_id, getState().team.id, name, purpose).then(response => {
      dispatch({type: 'CREATE_TEAM_CHANNEL_FULFILLED', payload:response});
      return response;
    }).catch(err => {
      dispatch({type: 'CREATE_TEAM_CHANNEL_REJECTED', payload:err});
      throw err;
    });
  }
}

export function deleteTeamChannel(channel_id){
  return function (dispatch, getState){
    dispatch({type: 'DELETE_TEAM_CHANNEL_PENDING'});
    return post_api.teamChannel.deleteChannel(getState().common.ws_id, getState().team.id, channel_id).then(response => {
      //need to reselect existing channel
        return dispatch({type: 'DELETE_TEAM_CHANNEL_FULFILLED', payload: {channel_id: channel_id}});
    }).catch(err => {
      dispatch({type: 'DELETE_TEAM_CHANNEL_REJECTED', payload: err});
      throw err;
    });
  }
}

export function askJoinChannel(channel_id){
  return function(dispatch, getState){
    dispatch({type: 'ASK_JOIN_CHANNEL_PENDING'});
    return post_api.teamChannel.askJoinChannel(getState().common.ws_id, getState().team.id, channel_id).then(r => {
      dispatch({type: 'ASK_JOIN_CHANNEL_FULFILLED', payload: {channel_id: channel_id, team_user_id: getState().team.myTeamUserId}});
    }).catch(err => {
      dispatch({type: 'ASK_JOIN_CHANNEL_REJECTED', payload: err});
      throw err;
    });
  }
}

export function addTeamUserToChannel(channel_id, team_user_id){
  return function(dispatch, getState){
    dispatch({type: 'ADD_TEAM_USER_TO_CHANNEL_PENDING'});
    return post_api.teamChannel.addTeamUserToChannel(getState().common.ws_id, getState().team.id, channel_id, team_user_id).then(response => {
      dispatch({type: 'ADD_TEAM_USER_TO_CHANNEL_FULFILLED', payload:{channel_id: channel_id, team_user_id:team_user_id}});
      return response;
    }).catch(err => {
      dispatch({type: 'ADD_TEAM_USER_TO_CHANNEL_REJECTED', payload: err});
      throw err;
    });
  }
}

export function removeTeamUserFromChannel(channel_id, team_user_id){
  return function(dispatch, getState){
    dispatch({type: 'REMOVE_TEAM_USER_FROM_CHANNEL_PENDING'});
    return post_api.teamChannel.removeTeamUserFromChannel(getState().common.ws_id, getState().team.id, channel_id, team_user_id).then(response => {
      dispatch({type: 'REMOVE_TEAM_USER_FROM_CHANNEL_FULFILLED', payload:{channel_id: channel_id, team_user_id:team_user_id}});
    }).catch(err => {
      dispatch({type: 'REMOVE_TEAM_USER_FROM_CHANNEL_REJECTED', payload:err});
      throw err;
    })
  }
}

export function editTeamChannelName(channel_id, name){
  return function (dispatch, getState) {
    dispatch({type: 'EDIT_TEAM_CHANNEL_NAME_PENDING'});
    return post_api.teamChannel.editName(getState().common.ws_id, getState().team.id, channel_id, name).then(response => {
      dispatch({type:'EDIT_TEAM_CHANNEL_NAME_FULFILLED', payload: {id:channel_id, name: name}});
    }).catch(err => {
      dispatch({type:"EDIT_TEAM_CHANNEL_NAME_REJECTED", payload:err});
      throw err;
    });
  }
}

export function editTeamChannelPurpose(channel_id, purpose){
  return function (dispatch, getState) {
    dispatch({type: 'EDIT_TEAM_CHANNEL_PURPOSE_PENDING'});
    return post_api.teamChannel.editPurpose(getState().common.ws_id, getState().team.id, channel_id, purpose).then(response => {
      dispatch({type:'EDIT_TEAM_CHANNEL_PURPOSE_FULFILLED', payload: {id:channel_id, purpose: purpose}});
    }).catch(err => {
      dispatch({type:"EDIT_TEAM_CHANNEL_PURPOSE_REJECTED", payload:err});
    });
  }
}

export function deleteJoinChannelRequest(channel_id, team_user_id){
  return function (dispatch, getState){
    dispatch({type: 'DELETE_JOIN_CHANNEL_REQUEST_PENDING'});
    return post_api.teamChannel.deleteJoinChannelRequest(getState().common.ws_id, getState().team.id, channel_id, team_user_id).then(response => {
      dispatch({type: 'DELETE_JOIN_CHANNEL_REQUEST_FULFILLED', payload: {channel_id: channel_id, team_user_id: team_user_id}});
      return response;
    }).catch(err => {
      dispatch({type:'DELETE_JOIN_CHANNEL_REQUEST_REJECTED', payload: err});
    });
  }
}