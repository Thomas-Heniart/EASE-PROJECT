var api = require('../utils/api');
var post_api = require('../utils/post_api');
import {deleteAppAction, fetchApp} from "./dashboardActions";

export function teamCreateMultiApp(app){
  return function (dispatch, getState){
    dispatch({type: 'TEAM_CREATE_MULTI_APP_PENDING'});
    return post_api.teamApps.createMultiApp(getState().common.ws_id, getState().team.id, app).then(response => {
      dispatch({type: 'TEAM_CREATE_MULTI_APP_FULFILLED', payload: response});
      return response;
    }).catch(err => {
      dispatch({type: 'TEAM_CREATE_MULTI_APP_REJECTED', payload: err});
      throw err;
    });
  }
}

export function teamCreateEnterpriseCard({team_id, channel_id, website_id, name, description, password_reminder_interval, receivers}){
  return (dispatch, getState) => {
    return post_api.teamApps.createEnterpriseCard({
      team_id: team_id,
      channel_id: channel_id,
      website_id: website_id,
      name: name,
      description: description,
      password_reminder_interval: password_reminder_interval,
      receivers: receivers,
      ws_id: getState().common.ws_id
    }).then(team_card => {
      dispatch(teamCardCreatedAction({
        team_card: team_card
      }));
      return team_card;
    }).catch(err => {
      throw err;
    });
  }
}

export function teamEditEnterpriseCard({team_id, team_card_id, name, description, password_reminder_interval}) {
  return (dispatch, getState) => {
    return post_api.teamApps.editEnterpriseCard({
      team_id: team_id,
      team_card_id: team_card_id,
      name: name,
      description: description,
      password_reminder_interval: password_reminder_interval,
      ws_id: getState().common.ws_id
    }).then(team_card => {
      dispatch({
        type: 'TEAM_CARD_CHANGED',
        payload: {
          team_card: team_card
        }
      });
      return team_card;
    }).catch(err => {
      throw err;
    });
  }
}

export function teamShareEnterpriseCard({team_id, team_card_id, team_user_id, account_information}) {
  return (dispatch, getState) => {
    return post_api.teamApps.shareEnterpriseCard({
      team_id: team_id,
      team_card_id: team_card_id,
      team_user_id: team_user_id,
      account_information: account_information,
      ws_id: getState().common.ws_id
    }).then(receiver => {
      dispatch(teamCardReceiverCreatedAction({
        receiver: receiver
      }));
      return receiver;
    }).catch(err => {
      throw err;
    });
  }
}

export function teamAcceptEnterpriseApp({team_id, shared_app_id, account_information, app_id}) {
  return (dispatch, getState) => {
    return post_api.teamApps.acceptEnterpriseApp({
      team_id: team_id,
      shared_app_id: shared_app_id,
      account_information: account_information,
      ws_id: getState().common.ws_id
    }).then(receiver => {
      console.log(receiver);
      dispatch({type: 'TEAM_CARD_RECEIVER_CHANGED', payload: {app_id: app_id, receiver: receiver}});
      return receiver;
    }).catch(err => {
      throw err;
    });
  }
}

export function teamJoinEnterpriseApp({team_id, app_id, account_information}){
  return (dispatch, getState) => {
    return post_api.teamApps.joinEnterpriseApp({
      team_id: team_id,
      app_id: app_id,
      account_information: account_information,
      ws_id: getState().common.ws_id
    }).then(app => {
      dispatch({type: 'TEAM_CARD_CHANGED', payload: {app: app}});
      return app;
    }).catch(err => {
      throw err;
    });
  }
}


export function teamEditEnterpriseCardReceiver({team_id, team_card_id, team_card_receiver_id, account_information}){
  return (dispatch, getState) => {
    return post_api.teamApps.editEnterpriseCardReceiver({
      team_id: team_id,
      team_card_id: team_card_id,
      team_card_receiver_id: team_card_receiver_id,
      account_information: account_information,
      ws_id: getState().common.ws_id
    }).then(receiver => {
      dispatch({
        type: 'TEAM_CARD_RECEIVER_CHANGED',
        payload: {
          receiver: receiver
        }
      });
      return receiver;
    }).catch(err => {
      throw err;
    });
  }
}

export function teamCardReceiverCreatedAction({receiver}) {
  return (dispatch, getState) => {
    const store = getState();
    const team = store.teams[receiver.team_id];
    dispatch({
      type: 'TEAM_CARD_RECEIVER_CREATED',
      payload: {
        receiver: receiver
      }
    });
    if (team.my_team_user_id === receiver.team_user_id){
      dispatch(fetchApp({app_id: receiver.app_id}));
    }
  }
}

export function teamCardCreatedAction({team_card}){
  return (dispatch, getState) => {
    dispatch({
      type: 'TEAM_CARD_CREATED',
      payload: {
        team_card: team_card
      }
    });
    const store = getState();
    const team = store.teams[team_card.team_id];
    const meReceiver = team_card.receivers.find(receiver => (receiver.team_user_id === team.my_team_user_id));
    if (!!meReceiver)
      dispatch(fetchApp({app_id: meReceiver.app_id}));
  }
}

export function teamCreateSingleApp({team_id, channel_id, website_id, name, description, password_change_interval, team_user_filler_id, account_information, receivers}) {
  return (dispatch, getState) => {
    return post_api.teamApps.createSingleApp({
      team_id: team_id,
      channel_id: channel_id,
      website_id:website_id,
      name: name,
      description: description,
      password_change_interval: password_change_interval,
      team_user_filler_id: team_user_filler_id,
      account_information: account_information,
      receivers: receivers,
      ws_id: getState().common.ws_id
    }).then(team_card => {
      dispatch(teamCardCreatedAction({team_card: team_card}));
      return team_card;
    }).catch(err => {
      throw err;
    });
  }
}

export function teamEditSingleApp({team_id, team_card_id, description, account_information, password_reminder_interval, name}){
  return function (dispatch, getState){
    return post_api.teamApps.editSingleApp({
      team_id: team_id,
      name: name,
      team_card_id: team_card_id,
      description: description,
      account_information: account_information,
      password_reminder_interval: password_reminder_interval,
      ws_id: getState().common.ws_id
    }).then(team_card => {
      dispatch({
        type: 'TEAM_CARD_CHANGED',
        payload: {
          team_card: team_card
        }
      });
      return team_card;
    }).catch(err => {
      throw err;
    });
  }
}

export function teamCardRequestCreatedAction({team_card_id, request}) {
  return {
    type: 'TEAM_CARD_REQUEST_CREATED',
    payload: {
      team_card_id: team_card_id,
      request: request
    }
  }
}

export function teamCardRequestRemovedAction({team_card_id, request_id}){
  return {
    type: 'TEAM_CARD_REQUEST_REMOVED',
    payload: {
      team_card_id: team_card_id,
      request_id: request_id
    }
  }
}

export function deleteTeamCardRequest({team_id, team_card_id, request_id}){
  return (dispatch, getState) => {
    return post_api.teamApps.deleteTeamCardRequest({
      team_id: team_id,
      team_card_id: team_card_id,
      request_id: request_id,
      ws_id: getState().common.ws_id
    }).then(response => {
      dispatch(teamCardRequestRemovedAction({
        team_card_id: team_card_id,
        request_id: request_id
      }));
      return response;
    }).catch(err => {
      throw err;
    });
  }
}

export function acceptTeamCardRequest({team_id, team_card_id, request_id}) {
  return (dispatch, getState) => {
    return post_api.teamApps.acceptTamCardRequest({
      team_id: team_id,
      team_card_id: team_card_id,
      request_id: request_id,
      ws_id: getState().common.ws_id
    }).then(receiver => {
      dispatch(teamCardReceiverCreatedAction({
        receiver: receiver
      }));
      return receiver;
    }).catch(err => {
      throw err;
    });
  }
}

export function requestTeamSingleCard({team_id, team_card_id}){
  return (dispatch, getState) => {
    return post_api.teamApps.requestTeamSingleCard({
      team_id: team_id,
      team_card_id: team_card_id,
      ws_id: getState().common.ws_id
    }).then(request => {
      dispatch(teamCardRequestCreatedAction({
        team_card_id: team_card_id,
        request: request
      }));
      return request;
    }).catch(err => {
      throw err;
    });
  };
}

export function requestTeamEnterpriseCard({team_id, team_card_id, account_information}) {
  return (dispatch, getState) => {
    return post_api.teamApps.requestTeamEnterpriseCard({
      team_id: team_id,
      team_card_id: team_card_id,
      account_information: account_information,
      ws_id: getState().common.ws_id
    }).then(request => {
      dispatch(teamCardRequestCreatedAction({
        team_card_id: team_card_id,
        request: request
      }));
      return request;
    }).catch(err => {
      throw err;
    });
  }
}

export function teamEditSingleAppReceiver({team_id, team_card_id, allowed_to_see_password, team_card_receiver_id}){
  return function (dispatch, getState){
    return post_api.teamApps.editSingleCardReceiver({
      team_id: team_id,
      team_card_id: team_card_id,
      team_card_receiver_id: team_card_receiver_id,
      allowed_to_see_password: allowed_to_see_password,
      ws_id: getState().common.ws_id
    }).then(receiver => {
      dispatch({
        type: 'TEAM_CARD_RECEIVER_CHANGED',
        payload: {
          receiver: receiver
        }
      });
      return receiver;
    }).catch(err => {
      throw err;
    });
  }
}

export function teamShareSingleCard({team_id, team_card_id, team_user_id, allowed_to_see_password}){
  return function (dispatch, getState){
    return post_api.teamApps.shareSingleApp({
      team_id: team_id,
      team_card_id: team_card_id,
      team_user_id: team_user_id,
      allowed_to_see_password: allowed_to_see_password,
      ws_id: getState().common.ws_id
    }).then(receiver => {
      dispatch(teamCardReceiverCreatedAction({
        receiver: receiver
      }));
      return receiver;
    }).catch(err => {
      throw err;
    });
  }
}

export function teamCreateLinkAppNew({team_id, channel_id, name, description, url, img_url, receivers}) {
  return (dispatch, getState) => {
    return post_api.teamApps.createLinkAppNew({
      team_id: team_id,
      channel_id: channel_id,
      name : name,
      description: description,
      url: url,
      img_url: img_url,
      receivers: receivers,
      ws_id: getState().common.ws_id
    }).then(app => {
      dispatch({type: 'TEAM_CARD_CREATED', payload: {team_card:app}});
      return app;
    }).catch(err => {
      throw err;
    });
  }
}

export function teamEditLinkAppNew({team_card_id, name, description, url, img_url}) {
  return (dispatch, getState) => {
    return post_api.teamApps.editLinkAppNew({
      team_card_id: team_card_id,
      name : name,
      description: description,
      url: url,
      img_url: img_url,
      ws_id: getState().common.ws_id
    }).then(app => {
      dispatch({type: 'TEAM_CARD_CHANGED', payload: {app: app}});
      return app;
    }).catch(err => {
      throw err;
    });
  }
}

export function teamPinLinkApp({team_id, app_id, app_name, profile_id})  {
  return (dispatch, getState) => {
    return post_api.teamApps.pinLinkApp({
      team_id: team_id,
      app_id: app_id,
      app_name:app_name,
      profile_id: profile_id,
      ws_id: getState().common.ws_id,
    }).then(receiver => {
      dispatch({type: 'TEAM_LINK_APP_RECEIVER_ADDED', payload: {app_id: app_id, receiver: receiver}});
      return receiver;
    }).catch(err => {
      throw err;
    });
  }
}

export function teamCreateLinkApp(app){
  return function (dispatch, getState){
    dispatch({type: 'TEAM_CREATE_LINK_APP_PENDING'});
    return post_api.teamApps.createLinkApp(getState().common.ws_id, getState().team.id, app).then(response => {
      dispatch({type: 'TEAM_CREATE_LINK_APP_FULFILLED', payload: response});
      return response;
    }).catch(err => {
      dispatch({type: 'TEAM_CREATE_LINK_APP_REJECTED', payload: err});
      throw err;
    });
  }
}

export function teamDeleteApp(app_id, team_id){
  return function (dispatch, getState){
    return post_api.teamApps.deleteApp(getState().common.ws_id, team_id, app_id).then(response => {
      dispatch({type: 'TEAM_APP_REMOVED', payload:{app_id: app_id}});
      return response;
    }).catch(err => {
      throw err;
    });
  }
}

export function teamShareApp(app_id, user_info){
  return function (dispatch, getState){
    dispatch({type: 'TEAM_SHARE_APP_PENDING'});
    return post_api.teamApps.shareApp(getState().common.ws_id, getState().team.id, app_id, user_info).then(response => {
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
    return post_api.teamApps.modifyApp(getState().common.ws_id, getState().team.id, app_id, app_info).then(response => {
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
    return post_api.teamApps.editReceiver(getState().common.ws_id, getState().team.id,user_app_id,receiver_info).then(response => {
      dispatch({type: 'TEAM_APP_EDIT_RECEIVER_FULFILLED', payload: {app_id: app_id, receiver_info: response}});
    }).catch(err => {
      dispatch({type: 'TEAM_APP_EDIT_RECEIVER_REJECTED', payload: err});
      throw err;
    })
  }
}

export function removeTeamCardReceiverAction({team_id, team_card_id, team_card_receiver_id}){
  return (dispatch, getState) => {
    const store = getState();
    const team = store.teams[team_id];
    const receiver = store.team_apps[team_card_id].receivers.find(item => (item.id === team_card_receiver_id));
    if (team.my_team_user_id === receiver.team_user_id){
      dispatch(deleteAppAction({
        app_id: receiver.app_id
      }));
    }
    dispatch({
      type: 'TEAM_CARD_RECEIVER_REMOVED',
      payload: {
        team_id: team_id,
        team_card_id: team_card_id,
        receiver: receiver
      }
    });
  }
}

export function removeTeamCardReceiver({team_id, team_card_id, team_card_receiver_id}) {
  return (dispatch, getState) => {
    return post_api.teamApps.removeTeamCardReceiver({
      team_id: team_id,
      team_card_id: team_card_id,
      team_card_receiver_id: team_card_receiver_id,
      ws_id: getState().common.ws_id
    }).then(response => {
      dispatch(removeTeamCardReceiverAction({
        team_id: team_id,
        team_card_id: team_card_id,
        team_card_receiver_id: team_card_receiver_id
      }));
      return response;
    }).catch(err => {
      throw err;
    });
  }
}

export function deleteTeamCardAction({team_id, team_card_id}){
  return (dispatch, getState) => {
    const store = getState();
    const app = store.team_apps[team_card_id];

    app.receivers.map(item => {
      dispatch(removeTeamCardReceiverAction({
        team_id: team_id,
        team_card_id: team_card_id,
        team_card_receiver_id: item.id
      }));
    });
    dispatch({
      type: 'TEAM_APP_REMOVED',
      payload: {
        app: app
      }
    });
  }
}

export function deleteTeamCard({team_id, team_card_id}) {
  return (dispatch, getState) => {
    return post_api.teamApps.deleteApp({
      team_id: team_id,
      team_card_id: team_card_id,
      ws_id: getState().common.ws_id
    }).then(response => {
      dispatch(deleteTeamCardAction({
        team_id: team_id,
        team_card_id: team_card_id
      }));
      return response;
    }).catch(err => {
      throw err;
    });
  }
}

export function teamAppDeleteReceiver({team_id, app_id, shared_app_id, team_user_id}){
  return function (dispatch, getState){
    return post_api.teamApps.deleteReceiver({
      team_id: team_id,
      app_id: app_id,
      shared_app_id: shared_app_id,
      ws_id: getState().common.ws_id
    }).then(response => {
      dispatch({type: 'TEAM_CARD_RECEIVER_REMOVED', payload: {app_id: app_id, team_user_id: team_user_id}});
    }).catch(err => {
      throw err;
    });
  }
}

export function teamAcceptSharedApp({team_id, app_id, shared_app_id}){
  return function(dispatch, getState){
    dispatch({type: 'TEAM_ACCEPT_SHARED_APP_PENDING'});
    return post_api.teamApps.acceptSharedApp({
      team_id: team_id,
      shared_app_id: shared_app_id,
      ws_id: getState().common.ws_id
    }).then(response => {
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
    return post_api.teamApps.transferOwnership(getState().common.ws_id, getState().team.id, app_id, team_user_id).then(response => {
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
    return post_api.teamApps.pinToDashboard(getState().common.ws_id, getState().team.id, shared_app_id, profile_id, app_name).then(response => {
      dispatch({type: 'TEAM_APP_PIN_TO_DASHBOARD_FULFILLED', payload: {shared_app_id: shared_app_id, profile_id: profile_id, app_id: app_id}});
      return response;
    }).catch(err => {
      dispatch({type: 'TEAM_APP_PIN_TO_DASHBOARD_REJECTED', payload: err});
      throw err;
    })
  }
}

export function joinTeamSingleCard({team_id, team_card_id}) {
  return function (dispatch, getState) {
    dispatch({type: 'TEAM_SINGLE_CARD_JOIN_PENDING'});
    return post_api.teamApps.joinTeamSingleCard(getState().common.ws_id, team_id, team_card_id).then(response => {
      dispatch({type: 'TEAM_SINGLE_CARD_JOIN_FULFILLED', payload: {team_card_id: team_card_id, team_user_id: response.team_user_id}});
      return response;
    }).catch(err => {
      dispatch({type: 'TEAM_SINGLE_CARD_JOIN_REJECTED', payload: err});
      throw err;
    })
  }
}

export function joinTeamEnterpriseCard(team_id, team_card_id, account_information) {
  return function (dispatch, getState) {
    dispatch({type: 'TEAM_ENTERPRISE_CARD_JOIN_PENDING'});
    return post_api.teamApps.joinTeamEnterpriseCard(getState().common.ws_id, team_id, team_card_id, account_information).then(response => {
      dispatch({type: 'TEAM_ENTERPRISE_CARD_JOIN_FULFILLED', payload: {team_card_id: team_card_id}});
      return response;
    }).catch(err => {
      dispatch({type: 'TEAM_ENTERPRISE_CARD_JOIN_REJECTED', payload: err});
      throw err;
    })
  }
}

export function askJoinTeamApp(app_id) {
  return function (dispatch, getState) {
    dispatch({type: 'TEAM_APP_ASK_JOIN_PENDING'});
    return post_api.teamApps.askJoinApp(getState().common.ws_id, getState().team.id, app_id).then(response => {
      dispatch({type: 'TEAM_APP_ASK_JOIN_FULFILLED', payload: {app_id: app_id, team_user_id: getState().team.myTeamUserId}});
      return response;
    }).catch(err => {
      dispatch({type: 'TEAM_APP_ASK_JOIN_REJECTED', payload: err});
      throw err;
    })
  }
}

export function deleteJoinAppRequest(app_id, team_user_id){
  return function (dispatch, getState){
    dispatch({type: 'DELETE_JOIN_APP_REQUEST_PENDING'});
    return post_api.teamApps.deleteJoinAppRequest(getState().common.ws_id, getState().team.id, app_id, team_user_id).then(r => {
      dispatch({type: 'DELETE_JOIN_APP_REQUEST_FULFILLED', payload: {app_id: app_id, team_user_id: team_user_id}});
      return r;
    }).catch(err => {
      dispatch({type: 'DELETE_JOIN_APP_REQUEST_REJECTED', payload:err});
      throw err;
    });
  }
}