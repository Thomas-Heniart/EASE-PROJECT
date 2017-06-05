var api = require('../utils/api');
var post_api = require('../utils/post_api');

export function selectTeamChannel(id){
  return function(dispatch, getState){
    dispatch({type: 'SELECT_TEAM_CHANNEL_PENDING'});
    return api.fetchTeamChannel(getState().team.id, id).then(response => {
      dispatch({type: 'SELECT_TEAM_CHANNEL_FULFILLED', payload: response});
    }).catch(err => {
      dispatch({type:'SELECT_TEAM_CHANNEL_REJECTED', payload:err});
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

export function editTeamChannelName(channel_id, name){
  return function (dispatch, getState) {
    dispatch({type: 'EDIT_TEAM_CHANNEL_NAME_PENDING'});
    return post_api.teamChannel.editName(getState().team.id, channel_id, name).then(response => {
      dispatch({type:'EDIT_TEAM_CHANNEL_NAME_FULFILLED', payload: {id:channel_id, name: name}});
    }).catch(err => {
      dispatch({type:"EDIT_TEAM_CHANNEL_NAME_REJECTED", payload:err});
    });
  }
}

export function editTeamChannelPurpose(channel_id, purpose){
  return function (dispatch, getState) {
    dispatch({type: 'EDIT_TEAM_CHANNEL_PURPOSE_PENDING'});
    return post_api.teamChannel.editPurpose(getState().team.id, channel_id, purpose).then(response => {
      dispatch({type:'EDIT_TEAM_CHANNEL_PURPOSE_FULFILLED', payload: {id:channel_id, purpose: purpose}});
    }).catch(err => {
      dispatch({type:"EDIT_TEAM_CHANNEL_PURPOSE_REJECTED", payload:err});
    });
  }
}