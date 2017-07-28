var api = require('../utils/api');
var post_api = require('../utils/post_api');

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
  return {
    type: 'SET_TEAMS_TUTORIAL',
    payload: state
  }
}
