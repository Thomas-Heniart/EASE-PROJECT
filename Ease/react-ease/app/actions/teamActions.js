import axios from "axios"
var api = require('../utils/api');
import * as UserActions from "./userActions"
import * as ChannelActions from "./channelActions"

export function fetchTeam(id) {
  return function(dispatch){
    dispatch({type:'FETCH_TEAM_PENDING', payload:id});
    return api.fetchTeam(id).then((response) => {
      dispatch({type: "FETCH_TEAM_FULFILLED", payload: response});
    }).catch((err) => {
      dispatch({type:"FETCH_TEAM_REJECTED", payload: err});
      throw err;
    });
  }
}

export function fetchTeamAndUsersAndChannels(team_id){
  return (dispatch) => {
    return dispatch(fetchTeam(team_id)).then(() => {
      return dispatch(UserActions.fetchUsers(team_id)).then(()=> {
        return dispatch(ChannelActions.fetchChannels(team_id));
      })
    })
  }
}

export function showTeamMenu(state){
  return {
    type: 'SHOW_TEAM_MENU',
    payload: state
  }
}