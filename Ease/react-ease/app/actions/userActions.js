var api =require('../utils/api');
var post_api = require('../utils/post_api');

export function selectTeamUser(id){
  return function(dispatch, getState){
    dispatch({type: 'SELECT_USER_PENDING'});
    return api.fetchTeamUser(getState().team.id, id).then(response => {
      dispatch({type: 'SELECT_USER_FULFILLED', payload: response});
    }).catch(err => {
      dispatch({type:'SELECT_USER_REJECTED', payload:err});
      throw err;
    });
  }
}

export function fetchUsers(team_id){
  return function(dispatch, getState){
    dispatch({type: 'FETCH_USERS_PENDING'});
    return api.fetchTeamUsers(team_id).then((response) => {
      var myTeamUserId = getState().team.myTeamUserId;
      console.log(getState().team.myTeamUserId);
      var payload = {
        users : response,
        myTeamUserId : myTeamUserId
      };
      dispatch({type: "FETCH_USERS_FULFILLED", payload: payload});
    }).catch((err) => {
      dispatch({type:"FETCH_USERS_REJECTED", payload: err});
      throw err;
    })
  }
}

export function editTeamUserUsername(user_id, username){
  return function(dispatch, getState){
    dispatch({type: 'EDIT_TEAM_USER_USERNAME_PENDING'});
    return post_api.teamUser.editUsername(getState().team.id, user_id, username).then(response => {
      dispatch({type:'EDIT_TEAM_USER_USERNAME_FULFILLED', payload: {id: user_id, username: username}});
    }).catch(err => {
      dispatch({type: 'EDIT_TEAM_USER_USERNAME_REJECTED', payload: err});
      throw err;
    })
  }
}

export function editTeamUserFirstName(user_id, first_name){
  return function(dispatch, getState){
    dispatch({type: 'EDIT_TEAM_USER_FIRSTNAME_PENDING'});
    return post_api.teamUser.editFirstName(getState().team.id, user_id, first_name).then(response => {
      dispatch({type:'EDIT_TEAM_USER_FIRSTNAME_FULFILLED', payload: {id: user_id, first_name: first_name}});
    }).catch(err => {
      dispatch({type: 'EDIT_TEAM_USER_FIRSTNAME_REJECTED', payload: err});
      throw err;
    })
  }
}

export function editTeamUserLastName(user_id, last_name){
  return function(dispatch, getState){
    dispatch({type: 'EDIT_TEAM_USER_LASTNAME_PENDING'});
    return post_api.teamUser.editLastName(getState().team.id, user_id, last_name).then(response => {
      dispatch({type:'EDIT_TEAM_USER_LASTNAME_FULFILLED', payload: {id: user_id, last_name: last_name}});
    }).catch(err => {
      dispatch({type: 'EDIT_TEAM_USER_LASTNAME_REJECTED', payload: err});
      throw err;
    })
  }
}

export function editTeamUserRole(user_id, role){
  return function(dispatch, getState){
    dispatch({type: 'EDIT_TEAM_USER_ROLE_PENDING'});
    return post_api.teamUser.editRole(getState().team.id, user_id, role).then(response => {
      dispatch({type:'EDIT_TEAM_USER_ROLE_FULFILLED', payload: {id: user_id, role: role}});
    }).catch(err => {
      dispatch({type: 'EDIT_TEAM_USER_ROLE_REJECTED', payload: err});
      throw err;
    })
  }
}

export function editTeamUserDepartureDate(user_id, departure_date){
  return function(dispatch, getState){
    dispatch({type: 'EDIT_TEAM_USER_DEPARTUREDATE_PENDING'});
    return post_api.teamUser.editDepartureDate(getState().team.id, user_id, departure_date).then(response => {
      dispatch({type:'EDIT_TEAM_USER_DEPARTUREDATE_FULFILLED', payload: {id: user_id, departure_date: departure_date}});
    }).catch(err => {
      dispatch({type: 'EDIT_TEAM_USER_DEPARTUREDATE_REJECTED', payload: err});
      throw err;
    })
  }
}