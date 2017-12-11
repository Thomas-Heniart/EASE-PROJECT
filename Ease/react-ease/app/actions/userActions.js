var api =require('../utils/api');
var post_api = require('../utils/post_api');
import {showVerifyTeamUserModal, showReactivateTeamUserModal, showDepartureDateEndModal} from "./teamModalActions";
import {teamRemovedAction} from "./teamActions";
import {teamUserState, teamUserRoles} from "../utils/utils";
import {selectUserFromListById,  isAdmin} from "../utils/helperFunctions";
import {teamCardReceiverRemovedAction, teamCardReceiverRemovedAction2} from "./appsActions";
import {addNotification} from "./notificationBoxActions";

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
      else if (teamUser.departure_date !== null && new Date().getTime() > teamUser.departure_date && isAdmin(me.role) && teamUser.id !== me.id)
        dispatch(showDepartureDateEndModal(true, teamUser));
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

export function createTeamUserNow({team_id, first_name, last_name, email, username, departure_date, role}){
  return function(dispatch, getState){
    return post_api.teamUser.createTeamUser(getState().common.ws_id, team_id, first_name, last_name, email, username, departure_date, role).then(team_user => {
      return post_api.teamUser.sendTeamUserInvitation(getState().common.ws_id, team_id, team_user.id).then(response => {
        dispatch(teamUserCreatedAction({team_user: response}));
        return response;
      }).catch(err => {
        throw err;
      });
    }).catch(err => {
      throw err;
    });
  }
}

export function createTeamUser({team_id, first_name, last_name, email, username, departure_date, role}){
  return function(dispatch, getState){
    return post_api.teamUser.createTeamUser(getState().common.ws_id, team_id, first_name, last_name, email, username, departure_date, role).then(response => {
      dispatch(teamUserCreatedAction({team_user: response}));
      return response;
    }).catch(err => {
      throw err;
    });
  }
}

export function sendTeamUserInvitation({team_id, team_user_id}){
  return function(dispatch, getState){
    return post_api.teamUser.sendTeamUserInvitation(getState().common.ws_id, team_id, team_user_id).then(response => {
      dispatch(teamUserCreatedAction({team_user: response}));
      return response;
    }).catch(err => {
      throw err;
    });
  }
}

export function deleteTeamUser({team_id,team_user_id}){
  return function(dispatch, getState){
    return post_api.teamUser.deleteTeamUser(getState().common.ws_id, team_id, team_user_id).then(response => {
      const team_user = getState().teams[team_id].team_users[team_user_id];
      dispatch(addNotification({
        text: `${team_user.username} has been successfully removed from your team`
      }));
      dispatch(teamUserRemovedAction({
        team_id: team_id,
        team_user_id: team_user_id
      }));
      return response;
    }).catch(err => {
      throw err;
    })
  }
}

export function editTeamUserUsername({team_id, team_user_id, username}){
  return function(dispatch, getState){
    return post_api.teamUser.editUsername(getState().common.ws_id, team_id, team_user_id, username).then(team_user => {
      dispatch({
        type: 'TEAM_USER_CHANGED',
        payload: {
          team_id: team_id,
          team_user: team_user
        }
      });
      dispatch(addNotification({
        text: "User's information successfully modified!"
      }));
      return team_user;
    }).catch(err => {
      throw err;
    })
  }
}

export function editTeamUserFirstName({team_id, team_user_id, first_name}){
  return function(dispatch, getState){
    return post_api.teamUser.editFirstName(getState().common.ws_id, team_id, team_user_id, first_name).then(response => {
      dispatch({
        type: 'TEAM_USER_CHANGED',
        payload: {
          team_id: team_id,
          team_user: response
        }
      });
      return response;
    }).catch(err => {
      throw err;
    })
  }
}

export function editTeamUserLastName({team_id, team_user_id, last_name}){
  return function(dispatch, getState){
    return post_api.teamUser.editLastName(getState().common.ws_id, team_id, team_user_id, last_name).then(response => {
      dispatch({
        type: 'TEAM_USER_CHANGED',
        payload: {
          team_id: team_id,
          team_user: response
        }
      });
      return response;
    }).catch(err => {
      throw err;
    })
  }
}

export function editTeamUserFirstLastName({team_id, team_user_id, first_name, last_name}) {
  return (dispatch, getState) => {
    return post_api.teamUser.editFirstLastName({
      team_id: team_id,
      team_user_id: team_user_id,
      first_name: first_name,
      last_name: last_name,
      ws_id: getState().common.ws_id
    }).then(team_user => {
      dispatch(teamUserChangedAction({
        team_user: team_user
      }));
      dispatch(addNotification({
        text: "User's information successfully modified!"
      }));
      return team_user;
    }).catch(err => {
      throw err;
    });
  }
}

export function editTeamUserRole({team_id, team_user_id, role}){
  return function(dispatch, getState){
    return post_api.teamUser.editRole(getState().common.ws_id, team_id, team_user_id, role).then(team_user => {
      dispatch({
        type: 'TEAM_USER_CHANGED',
        payload: {
          team_id: team_id,
          team_user: team_user
        }
      });
      dispatch(addNotification({
          text: `${team_user.username} role successfully changed to ${teamUserRoles[team_user.role]}`
      }));
      return team_user;
    }).catch(err => {
      throw err;
    })
  }
}

export function editTeamUserDepartureDate({team_id, team_user_id, departure_date}){
  return function(dispatch, getState){
    return post_api.teamUser.editDepartureDate(getState().common.ws_id, team_id, team_user_id, departure_date).then(team_user => {
      dispatch({
        type: 'TEAM_USER_CHANGED',
        payload: {
          team_id: team_id,
          team_user: team_user
        }
      });
      dispatch(addNotification({
        text: `Departure date for ${team_user.username}, successfully changed!`
      }));
      return team_user;
    }).catch(err => {
      throw err;
    })
  }
}

export function verifyTeamUserArrive({team_id, team_user_id}){
  return function (dispatch, getState){
    return post_api.teamUser.verifyTeamUser(getState().common.ws_id, team_id, team_user_id).then(response => {
      dispatch({
        type: 'TEAM_USER_CHANGED',
        payload: {
          team_user: response,
          team_id: team_id
        }
      });
      return response;
    }).catch(err => {
      throw err;
    });
  }
}

export function reactivateTeamUser({team_id, team_user_id}){
  return (dispatch, getState) => {
    return post_api.teamUser.reactivateTeamUser({
      team_id: team_id,
      team_user_id: team_user_id,
      ws_id: getState().common.ws_id
    }).then(response => {
      dispatch({
        type: 'TEAM_USER_CHANGED',
        payload: {
          team_user: response,
          team_id: team_id
        }
      });
      return response;
    }).catch(err => {
      throw err;
    });
  }
}

export function transferTeamOwnership({team_id, password, team_user_id}){
  return function (dispatch, getState){
    const my_id = getState().teams[team_id].my_team_user_id;
    return post_api.teamUser.transferTeamOwnership(getState().common.ws_id, team_id, password, team_user_id).then(r => {
      dispatch({
        type: 'TEAM_TRANSFER_OWNERSHIP',
        payload: {
          team_id: team_id,
          team_user_id: team_user_id,
          owner_id: my_id
        }
      });
    }).catch(err => {
      throw err;
    });
  }
}

export function editTeamUserPhone({team_id, team_user_id, phone_number}){
  return function(dispatch, getState){
    return post_api.teamUser.editPhoneNumber(getState().common.ws_id, team_id, team_user_id, phone_number).then(response => {
      dispatch({
        type: 'TEAM_USER_CHANGED',
        payload: {
          team_id: team_id,
          team_user: response
        }
      });
      return response;
    }).catch(err => {
      throw err;
    });
  }
}

export function teamUserCreatedAction({team_user}) {
  return {
    type: 'TEAM_USER_CREATED',
    payload: {
      team_user: team_user
    }
  }
}

export function teamUserChangedAction({team_user}) {
  return {
    type: 'TEAM_USER_CHANGED',
    payload: {
      team_user: team_user
    }
  }
}

export function teamUserRemovedAction({team_id, team_user_id}) {
  return (dispatch, getState) => {
    const store = getState();
    const team = store.teams[team_id];
    if (team.my_team_user_id === team_user_id){
      dispatch(teamRemovedAction({team_id: team_id}));
      return;
    }
    const team_user = team.team_users[team_user_id];
    team_user.team_card_ids.map(team_card_id => {
      dispatch(teamCardReceiverRemovedAction({
        team_id: team_id,
        team_card_id: team_card_id,
        team_user_id: team_user_id
      }));
    });
    dispatch({
      type: 'TEAM_USER_REMOVED',
      payload: {
        team_id: team_id,
        team_user_id: team_user_id
      }
    })
  };
}