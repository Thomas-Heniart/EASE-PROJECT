var api = require('../utils/api');
var post_api = require('../utils/post_api');
import {selectTeamChannel} from "./channelActions";
import {selectTeamUser} from "./userActions";
import {push} from "react-router-redux";

function getMyChannel(channels, myId){
  for (var i = 0; i < channels.length; i++){
    if (channels[i].userIds.indexOf(myId) !== -1)
      return channels[i].id;
  }
  return -1;
}

export function autoSelectTeamItem(){
  return function (dispatch, getState){
    const state = getState();
    const myId = state.team.myTeamUserId;
    const channels = state.channels.channels;
    const channelToSelect = getMyChannel(channels, myId);
    if (channelToSelect !== -1)
      dispatch(selectTeamChannel(channelToSelect));
    else
      dispatch(selectTeamUser(myId));
  }
}

export function fetchMyInformation(){
  return function(dispatch){
    dispatch({type: 'FETCH_MY_INFORMATION_PENDING'});
    return api.common.fetchMyInformation().then(response => {
      dispatch({type: 'FETCH_MY_INFORMATION_FULFILLED', payload: response});
      return response;
    }).catch(err => {
      dispatch({type: 'FETCH_MY_INFORMATION_REJECTED', payload: err});
      throw err;
    });
  }
}

export function processConnection(email, password){
  return function (dispatch){
    dispatch({type: 'CONNECTION_PENDING'});
    return post_api.common.connect(email, password).then(response => {
      dispatch({type: 'CONNECTION_FULFILLED', payload: response});
      return response;
    }).catch(err => {
      dispatch({type: 'CONNECTION_REJECTED', payload: err});
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

export function checkAuthentication(){
  return function(dispatch){
    dispatch({type: 'CHECK_CONNECTION_PENDING'});
    return api.common.checkAuthentication().then(response => {
      dispatch({type:'CHECK_CONNECTION_FULFILLED', payload: response});
      return response;
    }).catch(err => {
      dispatch({type: 'CHECK_CONNECTION_REJECTED', payload: err});
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

export function askEditEmail(password, new_email){
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

export function editEmail(password, new_email, digits){
    return function (dispatch){
        return post_api.common.editEmail(password, new_email, digits
        ).then(response => {
            dispatch({type: 'EDIT_EMAIL_FULFILLED', payload: response});
            return response;
        }).catch(err => {
            dispatch({type: 'EDIT_EMAIL_REJECTED', payload: err});
            throw err;
        })
    }
}

export function editPersonalUsername(username){
    return function (dispatch){
        return post_api.common.editPersonalUsername(username
        ).then(response => {
            dispatch({type: 'EDIT_USERNAME_FULFILLED', payload: response});
            return response;
        }).catch(err => {
            dispatch({type: 'EDIT_USERNAME_REJECTED', payload: err});
            throw err;
        })
    }
}

export function checkPassword(password){
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

export function editPassword(password, new_password){
    return function (dispatch){
        return post_api.common.editPassword(password, new_password)
            .then(response => {
            dispatch({type: 'EDIT_PASSWORD_FULFILLED', payload: response});
            return response;
        }).catch(err => {
            dispatch({type: 'EDIT_PASSWORD_REJECTED', payload: err});
            throw err;
        })
    }
}

export function setBackgroundPicture(active){
    return function (dispatch){
        return post_api.common.setBackgroundPicture(active).then(response => {
            dispatch({type: 'SET_BACKGROUND_FULFILLED', payload: response});
            return response;
        }).catch(err => {
            dispatch({type: 'SET_BACKGROUND_REJECTED', payload: err});
            throw err;
        })
    }
}

export function deleteAccount(password){
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