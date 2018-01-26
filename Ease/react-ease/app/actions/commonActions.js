var api = require('../utils/api');
var post_api = require('../utils/post_api');
import {fetchDashboard} from "./dashboardActions";
import {fetchTeams} from "./teamActions";
import {fetchNotifications} from "./notificationsActions";
import {addNotification} from "./notificationBoxActions";

export function fetchMyInformation(){
  return function(dispatch){
    dispatch({type: 'FETCH_MY_INFORMATION_PENDING'});
    return api.common.fetchMyInformation().then(response => {
      if (response.user !== undefined)
        easeTracker.setUserId(response.user.email);
      dispatch({type: 'FETCH_MY_INFORMATION_FULFILLED', payload: response});
      return response;
    }).catch(err => {
      dispatch({type: 'FETCH_MY_INFORMATION_REJECTED', payload: err});
      throw err;
    });
  }
}

export function fetchCriticalParts(){
  return (dispatch) => {
    dispatch(fetchNotifications(0));
    return Promise.all([
      dispatch(fetchTeams()),
      dispatch(fetchDashboard())
    ]).then(response => {
      return response;
    }).catch(err => {
      throw err;
    });
  }
}

export function processConnection({email, password}){
  return (dispatch) => {
    return post_api.common.connect(email, password).then(infos => {
      return dispatch(fetchCriticalParts()).then(response => {
        easeTracker.setUserId(infos.email);
        dispatch({type: 'CONNECTION_FULFILLED', payload: {user:infos}});
        return infos;
      });
    }).catch(err => {
      throw err;
    })
  }
}

export function processLogout(){
  return function (dispatch){
    dispatch({type: 'LOGOUT_PENDING'});
    return api.common.logout().then(response => {
      dispatch({type: 'LOGOUT_FULFILLED', payload: response});
      return response;
    }).catch(err => {
      dispatch({type: 'LOGOUT_REJECTED', payload: err});
      throw err;
    })
  }
}

export function setLoginRedirectUrl(url){
  return {
    type: 'SET_LOGIN_REDIRECT_URL',
    payload: {
      url: url
    }
  }
}

export function setTeamsTutorial(state) {
  if (state)
    return (dispatch) => {
      return post_api.teams.validateTutorial().then(r => {
        dispatch({type: 'SET_TEAMS_TUTORIAL', payload: state});
      });
    };
  return {
    type: 'SET_TEAMS_TUTORIAL',
    payload: state
  }
}

export function setWSId(id){
  return {
    type: 'SET_WS_ID',
    payload: {
      ws_id: id
    }
  }
}

export function askEditEmail({password, new_email}){
  return function (dispatch){
    return post_api.common.askEditEmail(password, new_email
    ).then(response => {
      dispatch({type: 'ASK_EDIT_EMAIL_FULFILLED', payload: response});
      return response;
    }).catch(err => {
      dispatch({type: 'ASK_EDIT_EMAIL_REJECTED', payload: err});
      throw err;
    })
  }
}

export function editEmail({password, new_email, digits}){
  return function (dispatch){
    return post_api.common.editEmail(password, new_email, digits).then(response => {
      dispatch({type: 'EDIT_EMAIL_FULFILLED', payload: response});
      dispatch(addNotification({
        text: "Your email has been successfully modified!"
      }));
      return response;
    }).catch(err => {
      dispatch({type: 'EDIT_EMAIL_REJECTED', payload: err});
      throw err;
    })
  }
}

export function editPersonalUsername({username}){
  return function (dispatch){
    return post_api.common.editPersonalUsername({
      username: username
    }).then(response => {
      dispatch({type: 'EDIT_USERNAME_FULFILLED', payload: response});
      dispatch(addNotification({
        text: `Your username has been changed to ${username}`
      }));
      return response;
    }).catch(err => {
      dispatch({type: 'EDIT_USERNAME_REJECTED', payload: err});
      throw err;
    })
  }
}

export function checkPassword({password}){
  return function (dispatch){
    return post_api.common.checkPassword(password).then(response => {
      dispatch({type: 'CHECK_PASSWORD_FULFILLED', payload: response});
      return response;
    }).catch(err => {
      dispatch({type: 'CHECK_PASSWORD_REJECTED', payload: err});
      throw err;
    })
  }
}

export function editPassword({password, new_password}){
  return function (dispatch){
    return post_api.common.editPassword(password, new_password)
        .then(response => {
          dispatch({type: 'EDIT_PASSWORD_FULFILLED', payload: response});
          dispatch(addNotification({
            text: "Your password has been successfully modified!"
          }));
          return response;
        }).catch(err => {
          dispatch({type: 'EDIT_PASSWORD_REJECTED', payload: err});
          throw err;
        })
  }
}

export function setBackgroundPicture({active}){
  return function (dispatch){
    return post_api.common.setBackgroundPicture({
      active: active
    }).then(response => {
      dispatch({
        type: 'SET_BACKGROUND_FULFILLED',
        payload: {
          background_picture: active
        }
      });
      return response;
    }).catch(err => {
      throw err;
    })
  }
}

export function deleteAccount({password}){
  return function (dispatch){
    return post_api.common.deleteAccount(password).then(response => {
      dispatch({type: 'DELETE_ACCOUNT_FULFILLED', payload: response});
      return response;
    }).catch(err => {
      dispatch({type: 'DELETE_ACCOUNT_REJECTED', payload: err});
      throw err;
    })
  }
}

export function setHomepage({homepage}){
  return {
    type: 'SET_HOMEPAGE', payload: {homepage: homepage}
  }
}

export function newFeatureSeen() {
  return (dispatch, getState) => {
    return post_api.common.newFeatureSeen().then(response => {
      dispatch({
        type: 'NEW_FEATURE_SEEN'
      });
    }).catch(err => {
      throw err;
    });
  }
}

export function setTipSeen({name}) {
  return (dispatch) => {
    return post_api.common.tipDone({
      name: name
    }).then(response => {
      dispatch({
        type: 'SET_TIP_SEEN',
        payload: {
          name: name
        }
      });
      return response;
    }).catch(err => {
      throw err;
    });
  }
}