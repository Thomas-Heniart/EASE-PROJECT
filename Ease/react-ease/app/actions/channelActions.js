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

export function editRoomManager({team_id, room_id, team_user_id}){
  return (dispatch, getState) => {
    return post_api.teamChannel.editRoomManager({
      team_id: team_id,
      channel_id: room_id,
      team_user_id: team_user_id,
      ws_id: getState().common.ws_id
    }).then(response => {
      dispatch({
        type: 'TEAM_ROOM_CHANGED',
        payload: {
          room: response
        }
      });
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

export function createTeamChannel({team_id, name, purpose}){
  return function(dispatch, getState){
    return post_api.teamChannel.createChannel(getState().common.ws_id, team_id, name, purpose).then(response => {
      dispatch({type: 'TEAM_ROOM_CREATED', payload:{room:response}});
      return response;
    }).catch(err => {
      throw err;
    });
  }
}

export function deleteTeamChannel({team_id, room_id}){
  return function (dispatch, getState){
    return post_api.teamChannel.deleteChannel(getState().common.ws_id, team_id, room_id).then(response => {
      dispatch({
        type: 'TEAM_ROOM_REMOVED',
        payload: {
          team_id: team_id,
          room_id: room_id
        }
      });
      return response;
    }).catch(err => {
      throw err;
    });
  }
}

export function askJoinChannel({team_id, room_id}){
  return function(dispatch, getState){
    const store = getState();
    const my_id = store.teams[team_id].my_team_user_id;
    return post_api.teamChannel.askJoinChannel(getState().common.ws_id, team_id, room_id).then(r => {
      dispatch({
        type: 'TEAM_ROOM_REQUEST_CREATED',
        payload: {
          team_id: team_id,
          room_id: room_id,
          team_user_id: my_id
        }
      });
    }).catch(err => {
      throw err;
    });
  }
}

export function addTeamUserToChannel({team_id, channel_id, team_user_id}){
  return function(dispatch, getState){
    return post_api.teamChannel.addTeamUserToChannel(getState().common.ws_id, team_id, channel_id, team_user_id).then(response => {
      dispatch({type: 'TEAM_ROOM_MEMBER_CREATED', payload:{
        team_id: team_id,
        team_user_id: team_user_id,
        room_id: channel_id
      }});
      return response;
    }).catch(err => {
      throw err;
    });
  }
}

export function removeTeamUserFromChannel({team_id, room_id, team_user_id}){
  return function(dispatch, getState){
    return post_api.teamChannel.removeTeamUserFromChannel(getState().common.ws_id, team_id, room_id, team_user_id).then(response => {
      dispatch({
        type: 'TEAM_ROOM_MEMBER_REMOVED',
        payload: {
          team_id: team_id,
          team_user_id: team_user_id,
          room_id: room_id
        }
      });
      return response;
    }).catch(err => {
      throw err;
    })
  }
}

export function editTeamChannelName({team_id, room_id, name}){
  return function (dispatch, getState) {
    return post_api.teamChannel.editName({
      ws_id: getState().common.ws_id,
      team_id: team_id,
      room_id: room_id,
      name: name
    }).then(response => {
      dispatch({type:'TEAM_ROOM_CHANGED', payload: {room: response}});
    }).catch(err => {
      throw err;
    });
  }
}

export function editTeamChannelPurpose({team_id, room_id, purpose}){
  return function (dispatch, getState) {
    return post_api.teamChannel.editPurpose(getState().common.ws_id, team_id, room_id, purpose).then(response => {
      dispatch({
        type: 'TEAM_ROOM_CHANGED',
        payload: {
          room: response
        }
      })
    }).catch(err => {
      throw err;
    });
  }
}

export function deleteJoinChannelRequest({team_id, room_id, team_user_id}){
  return function (dispatch, getState){
    return post_api.teamChannel.deleteJoinChannelRequest(getState().common.ws_id, team_id, room_id, team_user_id).then(response => {
      dispatch({
        type: 'TEAM_ROOM_REQUEST_REMOVED',
        payload: {
          team_id: team_id,
          room_id: room_id,
          team_user_id: team_user_id
        }
      });
      return response;
    }).catch(err => {
      throw err;
    });
  }
}

export function teamRoomCreatedAction({room}) {
  return {
    payload: {
      type: 'TEAM_ROOM_CREATED',
      payload: {
        room: room
      }
    }
  }
}

export function teamRoomChangedAction({room}) {
  return {
    payload: {
      type: 'TEAM_ROOM_CHANGED',
      payload: {
        room: room
      }
    }
  }
}

export function teamRoomRemovedAction({team_id, room_id}) {
  return {
    payload: {
      type: 'TEAM_ROOM_REMOVED',
      payload: {
        team_id:team_id,
        room_id: room_id
      }
    }
  }
}

export function teamRoomRequestCreatedAction({team_id, room_id, team_user_id}) {
  return {
    type: 'TEAM_ROOM_REQUEST_CREATED',
    payload: {
      team_id: team_id,
      room_id: room_id,
      team_user_id: team_user_id
    }
  }
}

export function teamRoomRequestRemovedAction({team_id, room_id, team_user_id}) {
  return {
    type: 'TEAM_ROOM_REQUEST_REMOVED',
    payload: {
      team_id: team_id,
      room_id: room_id,
      team_user_id: team_user_id
    }
  }
}

export function teamRoomMemberCreated({team_id, room_id, team_user_id}) {
  return {
    type: 'TEAM_ROOM_MEMBER_CREATED',
    payload: {
      team_id: team_id,
      room_id: room_id,
      team_user_id: team_user_id
    }
  }
}

export function teamRoomMemberRemoved({team_id, room_id, team_user_id}) {
  return {
    type: 'TEAM_ROOM_MEMBER_REMOVED',
    payload: {
      team_id: team_id,
      room_id: room_id,
      team_user_id: team_user_id
    }
  }
}
