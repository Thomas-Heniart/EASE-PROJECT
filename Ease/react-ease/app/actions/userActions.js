var api =require('../utils/api');
var post_api = require('../utils/post_api');
import {showVerifyTeamUserModal, showReactivateTeamUserModal} from "./teamModalActions";
import {teamUserState} from "../utils/utils";
import {selectUserFromListById,  isAdmin} from "../utils/helperFunctions";

export function selectTeamUser(id){
  return function(dispatch, getState){
    dispatch({type: 'SELECT_USER_PENDING'});
    var teamUser = selectUserFromListById(getState().users.users, id);
    return api.fetchTeamUserApps(getState().team.id, id).then(response => {
      dispatch({type: 'SELECT_USER_FULFILLED', payload: {user:teamUser, apps: response}});
      if (teamUser.state === teamUserState.registered)
        dispatch(showVerifyTeamUserModal(true, teamUser));
    }).catch(err => {
      dispatch({type:'SELECT_USER_REJECTED', payload:err});
      throw err;
    });
  }
}

export function fetchTeamUserApps(id){
  return function(dispatch, getState){
    dispatch({type: 'FETCH_TEAM_USER_APPS_PENDING'});
    const teamUser = selectUserFromListById(getState().users.users, id);
    const me = selectUserFromListById(getState().users.users, getState().team.myTeamUserId);

    return api.fetchTeamUserApps(getState().team.id, id).then(response => {
      dispatch({type: 'FETCH_TEAM_USER_APPS_FULFILLED', payload: {apps: response, type: 'user', id: id}});
      if (teamUser.state === teamUserState.registered && isAdmin(me.role) && teamUser.id !== me.id)
        dispatch(showVerifyTeamUserModal(true, teamUser));
      else if (teamUser.disabled && isAdmin(me.role) && teamUser.id !== me.id)
        dispatch(showReactivateTeamUserModal(true, teamUser));
      return response;
    }).catch(err => {
      dispatch({type:'FETCH_TEAM_USER_APPS_REJECTED', payload:err});
      throw err;
    });
  }
}

export function fetchUsers(team_id){
  return function(dispatch, getState){
    dispatch({type: 'FETCH_USERS_PENDING'});
    return api.fetchTeamUsers(team_id).then((response) => {
      var myTeamUserId = getState().team.myTeamUserId;
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

export function getTeamUserShareableApps(team_user_id){
  return function(dispatch, getState){
    dispatch({type: 'GET_TEAM_USER_SHAREABLE_APPS_PENDING'});
    return api.fetchTeamUserShareableApps(getState().team.id, team_user_id).then(response => {
      dispatch({type: 'GET_TEAM_USER_SHAREABLE_APPS_FULFILLED', payload:{team_user_id:team_user_id, apps:response}});
      return response;
    }).catch(err => {
      dispatch({type: 'GET_TEAM_USER_SHAREABLE_APPS_REJECTED'});
      throw err;
    });
  }
}

export function createTeamUser(first_name, last_name, email, username, departure_date, role){
  return function(dispatch, getState){
    dispatch({type:'CREATE_TEAM_USER_PENDING'});
    return post_api.teamUser.createTeamUser(getState().common.ws_id, getState().team.id, first_name, last_name, email, username, departure_date, role).then(response => {
      dispatch({type: 'CREATE_TEAM_USER_FULFILLED', payload: response});
      return response;
    }).catch(err => {
      dispatch({type: 'CREATE_TEAM_USER_REJECTED'});
      throw err;
    });
  }
}

export function deleteTeamUser(team_user_id){
  return function(dispatch, getState){
    dispatch({type: 'DELETE_TEAM_USER_PENDING'});
    return post_api.teamUser.deleteTeamUser(getState().common.ws_id, getState().team.id, team_user_id).then(response => {
        return dispatch({type: 'DELETE_TEAM_USER_FULFILLED', payload:{team_user_id: team_user_id}});
    }).catch(err => {
      dispatch({type: 'DELETE_TEAM_USER_REJECTED', payload: err});
      throw err;
    })
  }
}

export function editTeamUserUsername(user_id, username){
  return function(dispatch, getState){
    dispatch({type: 'EDIT_TEAM_USER_USERNAME_PENDING'});
    return post_api.teamUser.editUsername(getState().common.ws_id, getState().team.id, user_id, username).then(response => {
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
    return post_api.teamUser.editFirstName(getState().common.ws_id, getState().team.id, user_id, first_name).then(response => {
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
    return post_api.teamUser.editLastName(getState().common.ws_id, getState().team.id, user_id, last_name).then(response => {
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
    return post_api.teamUser.editRole(getState().common.ws_id, getState().team.id, user_id, role).then(response => {
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
    return post_api.teamUser.editDepartureDate(getState().common.ws_id, getState().team.id, user_id, departure_date).then(response => {
      dispatch({type:'EDIT_TEAM_USER_DEPARTUREDATE_FULFILLED', payload: {id: user_id, departure_date: departure_date}});
    }).catch(err => {
      dispatch({type: 'EDIT_TEAM_USER_DEPARTUREDATE_REJECTED', payload: err});
      throw err;
    })
  }
}

export function verifyTeamUserArrive(team_user_id){
  return function (dispatch, getState){
    dispatch({type: 'VERIFY_TEAM_USER_ARRIVE_PENDING'});
    return post_api.teamUser.verifyTeamUser(getState().common.ws_id, getState().team.id, team_user_id).then(response => {
      dispatch({type: 'VERIFY_TEAM_USER_ARRIVE_FULFILLED', payload: {team_user_id: team_user_id}});
    }).catch(err => {
      dispatch({type: 'VERIFY_TEAM_USER_ARRIVE_REJECTED', payload: {err}});
      throw err;
    });
  }
}

export function reactivateTeamUser({team_user_id}){
  return (dispatch, getState) => {
    dispatch({type: 'REACTIVATE_TEAM_USER_PENDING'});
    return post_api.teamUser.reactivateTeamUser({team_id: getState().team.id, team_user_id: team_user_id, ws_id: getState().common.ws_id}).then(r => {
      dispatch({type: 'REACTIVATE_TEAM_USER_FULFILLED', payload: {team_user_id: team_user_id}});
      return r;
    }).catch(err => {
      dispatch({type: 'REACTIVATE_TEAM_USER_FULFILLED', payload: err});
      throw err;
    });
  }
}

export function transferTeamOwnership(password, team_user_id){
  return function (dispatch, getState){
    dispatch({type: 'TEAM_TRANSFER_OWNERSHIP_PENDING'});
    return post_api.teamUser.transferTeamOwnership(getState().common.ws_id, getState().team.id, password, team_user_id).then(r => {
      dispatch({type: 'TEAM_TRANSFER_OWNERSHIP_FULFILLED', payload: {ownerId: getState().team.myTeamUserId, team_user_id: team_user_id}});
    }).catch(err => {
      dispatch({type: 'TEAM_TRANSFER_OWNERSHIP_REJECTED', payload: err});
      throw err;
    });
  }
}

export function editTeamUserPhone(team_user_id, phone_number){
  return function(dispatch, getState){
    dispatch({type: 'EDIT_TEAM_USER_PHONE_PENDING'});
    return post_api.teamUser.editPhoneNumber(getState().common.ws_id, getState().team.id, team_user_id, phone_number).then(r => {
      dispatch({type: 'EDIT_TEAM_USER_PHONE_FULFILLED', payload: {team_user_id: team_user_id, phone: phone_number}});
    }).catch(err => {
      dispatch({type: 'EDIT_TEAM_USER_PHONE_REJECTED', payload: err});
      throw err;
    });
  }
}