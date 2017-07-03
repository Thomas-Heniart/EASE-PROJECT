var api = require('../utils/api');
var post_api = require('../utils/post_api');

export function teamCreateMultiApp(app){
  return function (dispatch, getState){
    dispatch({type: 'TEAM_CREATE_MULTI_APP_PENDING'});
    return post_api.teamApps.createMultiApp(getState().team.id, app).then(response => {
      dispatch({type: 'TEAM_CREATE_MULTI_APP_FULFILLED', payload: response});
      return response;
    }).catch(err => {
      dispatch({type: 'TEAM_CREATE_MULTI_APP_REJECTED', payload: err});
      throw err;
    });
  }
}

export function teamShareMultiApp(app_id, user_info){
  return function (dispatch, getState){
    dispatch({type: 'TEAM_SHARE_APP_PENDING'});
    return post_api.teamApps.shareMultiApp(getState().team.id, app_id, user_info).then(response => {
      dispatch({type: 'TEAM_SHARE_APP_FULFILLED', payload:{user_info:response, app_id:app_id}});
    }).catch(err => {
      dispatch({type: 'TEAM_SHARE_APP_REJECTED', payload:err});
      throw err;
    });
  }
}
export function teamCreateSingleApp(app){
  return function (dispatch, getState){
    dispatch({type: 'TEAM_CREATE_SINGLE_APP_PENDING'});
    return post_api.teamApps.createSingleApp(getState().team.id, app).then(response => {
      dispatch({type: 'TEAM_CREATE_SINGLE_APP_FULFILLED', payload: response});
      return response;
    }).catch(err => {
      dispatch({type: 'TEAM_CREATE_SINGLE_APP_REJECTED', payload: err});
      throw err;
    });
  }
}

export function teamCreateLinkApp(app){
  return function (dispatch, getState){
    dispatch({type: 'TEAM_CREATE_LINK_APP_PENDING'});
    return post_api.teamApps.createLinkApp(getState().team.id, app).then(response => {
      dispatch({type: 'TEAM_CREATE_LINK_APP_FULFILLED', payload: response});
      return response;
    }).catch(err => {
      dispatch({type: 'TEAM_CREATE_LINK_APP_REJECTED', payload: err});
      throw err;
    });
  }
}

export function teamDeleteApp(app_id){
  return function (dispatch, getState){
    dispatch({type: 'TEAM_DELETE_APP_PENDING'});
    return post_api.teamApps.deleteApp(getState().team.id, app_id).then(response => {
      dispatch({type: 'TEAM_DELETE_APP_FULFILLED', payload:{app_id: app_id}});
      return response;
    }).catch(err => {
      dispatch({type: 'TEAM_DELETE_APP_REJECTED', payload: err});
      throw err;
    });
  }
}

export function teamShareApp(app_id, user_info){
  return function (dispatch, getState){
    dispatch({type: 'TEAM_SHARE_APP_PENDING'});
    return post_api.teamApps.shareApp(getState().team.id, app_id, user_info).then(response => {
      dispatch({type: 'TEAM_SHARE_APP_FULFILLED', payload:{user_info:response, app_id:app_id}});
      return response;
    }).catch(err => {
      dispatch({type: 'TEAM_SHARE_APP_REJECTED', payload:err});
      throw err;
    });
  }
}

export function teamModifyAppInformation(app_id, app_info){
  return function(dispatch, getState){
    dispatch({type: 'TEAM_MODIFY_APP_INFORMATION_PENDING'});
    return post_api.teamApps.modifyApp(getState().team.id, app_id, app_info).then(response => {
      dispatch({type: 'TEAM_MODIFY_APP_INFORMATION_FULFILLED',payload:{app_id:app_id, app:response}});
    }).catch( err => {
      dispatch({type: 'TEAM_MODIFY_APP_INFORMATION_REJECTED', payload: err});
      throw err;
    })
  }
}

export function teamAppEditReceiver(app_id,user_app_id, receiver_info){
  return function(dispatch, getState){
    dispatch({type: 'TEAM_APP_EDIT_RECEIVER_PENDING'});
    return post_api.teamApps.editReceiver(getState().team.id,user_app_id,receiver_info).then(response => {
      dispatch({type: 'TEAM_APP_EDIT_RECEIVER_FULFILLED', payload: {app_id: app_id, receiver_info: response}});
    }).catch(err => {
      dispatch({type: 'TEAM_APP_EDIT_RECEIVER_REJECTED', payload: err});
    })
  }
}

export function teamAppDeleteReceiver(app_id,user_app_id, team_user_id){
  return function(dispatch, getState){
    dispatch({type: 'TEAM_APP_DELETE_RECEIVER_PENDING'});
    return post_api.teamApps.deleteReceiver(getState().team.id, user_app_id, team_user_id).then(response => {
      dispatch({type: 'TEAM_APP_DELETE_RECEIVER_FULFILLED', payload: {app_id: app_id, team_user_id: team_user_id}});
    }).catch(err => {
      dispatch({type: 'TEAM_APP_DELETE_RECEIVER_REJECTED', payload: err});
    });
  }
}

export function teamAcceptSharedApp(app_id, shared_app_id){
  return function(dispatch, getState){
    dispatch({type: 'TEAM_ACCEPT_SHARED_APP_PENDING'});
    return post_api.teamApps.acceptSharedApp(getState().team.id, shared_app_id).then(response => {
      dispatch({type: 'TEAM_ACCEPT_SHARED_APP_FULFILLED', payload: {app_id: app_id, shared_app_id: shared_app_id}});
      return response;
    }).catch(err => {
      dispatch({type: 'TEAM_ACCEPT_SHARED_APP_REJECTED'});
      throw err;
    });
  }
}

export function teamAppTransferOwnership(app_id, team_user_id){
  return function(dispatch, getState){
    dispatch({type: 'TEAM_APP_TRANSFER_OWNERSHIP_PENDING'});
    return post_api.teamApps.transferOwnership(getState().team.id, app_id, team_user_id).then(response => {
      dispatch({type: 'TEAM_APP_TRANSFER_OWNERSHIP_FULFILLED', payload:{app_id: app_id, team_user_id: team_user_id}});
    }).catch(err => {
      dispatch({type:'TEAM_APP_TRANSFER_OWNERSHIP_REJECTED', payload: err});
      throw err;
    });
  }
}

export function teamAppPinToDashboard(shared_app_id, profile_id, app_name, app_id){
  return function (dispatch, getState) {
    dispatch({type: 'TEAM_APP_PIN_TO_DASHBOARD_PENDING'});
    return post_api.teamApps.pinToDashboard(getState().team.id, shared_app_id, profile_id, app_name).then(response => {
      dispatch({type: 'TEAM_APP_PIN_TO_DASHBOARD_FULFILLED', payload: {shared_app_id: shared_app_id, profile_id: profile_id, app_id: app_id}});
      return response;
    }).catch(err => {
      dispatch({type: 'TEAM_APP_PIN_TO_DASHBOARD_REJECTED', payload: err});
      throw err;
    })
  }
}