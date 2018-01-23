var api = require('../utils/api');
var post_api = require('../utils/post_api');
import {dashboardAppRemovedAction, deleteAppAction, fetchApp} from "./dashboardActions";
import {addNotification} from "./notificationBoxActions";

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
      const room = getState().teams[team_id].rooms[channel_id];
      dispatch(addNotification({
        text: `${team_card.name} successfully sent to ${room.name}`
      }));
      return team_card;
    }).catch(err => {
      throw err;
    });
  }
}

export function teamCreateAnyEnterpriseCard({team_id, channel_id, name, description, password_reminder_interval, url, img_url, connection_information, receivers}){
  return (dispatch, getState) => {
    return post_api.teamApps.createAnyEnterpriseCard({
      team_id: team_id,
      channel_id: channel_id,
      name: name,
      description: description,
      password_reminder_interval: password_reminder_interval,
      url: url,
      img_url: img_url,
      connection_information: connection_information,
      receivers: receivers,
      ws_id: getState().common.ws_id
    }).then(team_card => {
      dispatch(teamCardCreatedAction({
        team_card: team_card
      }));
      const room = getState().teams[team_id].rooms[channel_id];
      dispatch(addNotification({
        text: `${team_card.name} successfully sent to ${room.name}`
      }));
      return team_card;
    }).catch(err => {
      throw err;
    });
  }
}

export function teamCreateSoftwareEnterpriseCard({team_id, channel_id, name, description, password_reminder_interval, logo_url, connection_information, receivers}){
  return (dispatch, getState) => {
    return post_api.teamApps.createSoftwareEnterpriseCard({
      team_id: team_id,
      channel_id: channel_id,
      name: name,
      description: description,
      password_reminder_interval: password_reminder_interval,
      logo_url: logo_url,
      receivers: receivers,
      connection_information: connection_information,
      ws_id: getState().common.ws_id
    }).then(team_card => {
      dispatch(teamCardCreatedAction({
        team_card: team_card
      }));
      const room = getState().teams[team_id].rooms[channel_id];
      dispatch(addNotification({
        text: `${team_card.name} successfully sent to ${room.name}`
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
      dispatch(teamCardChangedAction({
        team_card: team_card
      }));
      return team_card;
    }).catch(err => {
      throw err;
    });
  }
}

export function teamEditAnyEnterpriseCard({team_card_id, name, description, password_reminder_interval, url, img_url, connection_information}) {
  return (dispatch, getState) => {
    return post_api.teamApps.editAnyEnterpriseCard({
      team_card_id: team_card_id,
      name: name,
      description: description,
      password_reminder_interval: password_reminder_interval,
      url: url,
      img_url: img_url,
      connection_information: connection_information,
      ws_id: getState().common.ws_id
    }).then(team_card => {
      dispatch(teamCardChangedAction({
        team_card: team_card
      }));
      return team_card;
    }).catch(err => {
      throw err;
    });
  }
}

export function teamEditSoftwareEnterpriseCard({team_card_id, name, description, password_reminder_interval}) {
  return (dispatch, getState) => {
    return post_api.teamApps.editSoftwareEnterpriseCard({
      team_card_id: team_card_id,
      name: name,
      description: description,
      password_reminder_interval: password_reminder_interval,
      ws_id: getState().common.ws_id
    }).then(team_card => {
      dispatch(teamCardChangedAction({
        team_card: team_card
      }));
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

export function teamEditEnterpriseCardReceiver({team_id, team_card_id, team_card_receiver_id, account_information}){
  return (dispatch, getState) => {
    return post_api.teamApps.editEnterpriseCardReceiver({
      team_id: team_id,
      team_card_id: team_card_id,
      team_card_receiver_id: team_card_receiver_id,
      account_information: account_information,
      ws_id: getState().common.ws_id
    }).then(receiver => {
      dispatch(teamCardReceiverChangedAction({
        receiver: receiver
      }));
      return receiver;
    }).catch(err => {
      throw err;
    });
  }
}

export function teamCreateSingleApp({team_id, channel_id, website_id, name, description, password_reminder_interval, team_user_filler_id, account_information, receivers}) {
  return (dispatch, getState) => {
    return post_api.teamApps.createSingleCard({
      team_id: team_id,
      channel_id: channel_id,
      website_id:website_id,
      name: name,
      description: description,
      password_reminder_interval: password_reminder_interval,
      team_user_filler_id: team_user_filler_id,
      account_information: account_information,
      receivers: receivers,
      ws_id: getState().common.ws_id
    }).then(team_card => {
      dispatch(teamCardCreatedAction({team_card: team_card}));
      const room = getState().teams[team_id].rooms[channel_id];
      dispatch(addNotification({
        text: `${team_card.name} successfully sent to ${room.name}`
      }));
      return team_card;
    }).catch(err => {
      throw err;
    });
  }
}

export function teamCreateAnySingleCard({team_id, channel_id, name, description, password_reminder_interval, url, img_url, connection_information, team_user_filler_id, account_information, receivers}) {
  return (dispatch, getState) => {
    return post_api.teamApps.createTeamAnySingleCard({
      team_id: team_id,
      channel_id: channel_id,
      name: name,
      description: description,
      password_reminder_interval: password_reminder_interval,
      url: url,
      img_url: img_url,
      connection_information: connection_information,
      team_user_filler_id: team_user_filler_id,
      account_information: account_information,
      receivers: receivers,
      ws_id: getState().common.ws_id
    }).then(team_card => {
      dispatch(teamCardCreatedAction({team_card: team_card}));
      const room = getState().teams[team_id].rooms[channel_id];
      dispatch(addNotification({
        text: `${team_card.name} successfully sent to ${room.name}`
      }));
      return team_card;
    }).catch(err => {
      throw err;
    });
  }
}

export function teamCreateSoftwareSingleCard({team_id, channel_id, name, description, password_reminder_interval, logo_url, team_user_filler_id, connection_information, account_information, receivers}) {
  return (dispatch, getState) => {
    return post_api.teamApps.createTeamSoftwareSingleCard({
      team_id: team_id,
      channel_id: channel_id,
      name: name,
      description: description,
      password_reminder_interval: password_reminder_interval,
      logo_url: logo_url,
      team_user_filler_id: team_user_filler_id,
      connection_information: connection_information,
      account_information: account_information,
      receivers: receivers,
      ws_id: getState().common.ws_id
    }).then(team_card => {
      dispatch(teamCardCreatedAction({team_card: team_card}));
      const room = getState().teams[team_id].rooms[channel_id];
      dispatch(addNotification({
        text: `${team_card.name} successfully sent to ${room.name}`
      }));
      return team_card;
    }).catch(err => {
      throw err;
    });
  }
}

export function teamEditSingleApp({team_id, team_card_id, description, account_information, password_reminder_interval, name}){
  return function (dispatch, getState){
    return post_api.teamApps.editSingleCard({
      team_id: team_id,
      name: name,
      team_card_id: team_card_id,
      description: description,
      account_information: account_information,
      password_reminder_interval: password_reminder_interval,
      ws_id: getState().common.ws_id
    }).then(team_card => {
      dispatch(teamCardChangedAction({
        team_card: team_card
      }));
      return team_card;
    }).catch(err => {
      throw err;
    });
  }
}

export function teamEditAnySingleApp({team_card_id, description, url, img_url, connection_information, account_information, password_reminder_interval, name}){
  return function (dispatch, getState){
    return post_api.teamApps.editAnySingleCard({
      name: name,
      team_card_id: team_card_id,
      description: description,
      url: url,
      img_url: img_url,
      connection_information: connection_information,
      account_information: account_information,
      password_reminder_interval: password_reminder_interval,
      ws_id: getState().common.ws_id
    }).then(team_card => {
      dispatch(teamCardChangedAction({
        team_card: team_card
      }));
      return team_card;
    }).catch(err => {
      throw err;
    });
  }
}

export function teamEditSoftwareSingleApp({team_card_id, description, account_information, connection_information, password_reminder_interval, name}){
  return function (dispatch, getState){
    return post_api.teamApps.editSoftwareSingleCard({
      name: name,
      team_card_id: team_card_id,
      description: description,
      account_information: account_information,
      connection_information: connection_information,
      password_reminder_interval: password_reminder_interval,
      ws_id: getState().common.ws_id
    }).then(team_card => {
      dispatch(teamCardChangedAction({
        team_card: team_card
      }));
      return team_card;
    }).catch(err => {
      throw err;
    });
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
    return post_api.teamApps.acceptTeamCardRequest({
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

export function teamEditSingleCardReceiver({team_id, team_card_id, allowed_to_see_password, team_card_receiver_id}){
  return function (dispatch, getState){
    return post_api.teamApps.editSingleCardReceiver({
      team_id: team_id,
      team_card_id: team_card_id,
      team_card_receiver_id: team_card_receiver_id,
      allowed_to_see_password: allowed_to_see_password,
      ws_id: getState().common.ws_id
    }).then(receiver => {
      dispatch(teamCardReceiverChangedAction({
        receiver: receiver
      }));
      return receiver;
    }).catch(err => {
      throw err;
    });
  }
}

export function teamShareSingleCard({team_id, team_card_id, team_user_id, allowed_to_see_password}){
  return function (dispatch, getState){
    return post_api.teamApps.shareSingleCard({
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

export function teamCreateLinkCard({team_id, channel_id, name, description, url, img_url, receivers}) {
  return (dispatch, getState) => {
    return post_api.teamApps.createLinkCard({
      team_id: team_id,
      channel_id: channel_id,
      name : name,
      description: description,
      url: url,
      img_url: img_url,
      receivers: receivers,
      ws_id: getState().common.ws_id
    }).then(team_card => {
      dispatch(teamCardCreatedAction({team_card: team_card}));
      const room = getState().teams[team_id].rooms[channel_id];
      dispatch(addNotification({
        text: `${team_card.name} successfully sent to ${room.name}`
      }));
      return app;
    }).catch(err => {
      throw err;
    });
  }
}

export function teamShareLinkCard({team_card_id, team_user_id}){
  return function (dispatch, getState){
    return post_api.teamApps.addTeamLinkCardReceiver({
      team_card_id: team_card_id,
      team_user_id: team_user_id,
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

export function teamEditLinkCard({team_card_id, name, description, url, img_url}) {
  return (dispatch, getState) => {
    return post_api.teamApps.editLinkCard({
      team_card_id: team_card_id,
      name : name,
      description: description,
      url: url,
      img_url: img_url,
      ws_id: getState().common.ws_id
    }).then(team_card => {
      dispatch(teamCardChangedAction({
        team_card: team_card
      }));
      return team_card;
    }).catch(err => {
      throw err;
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
      const store = getState();
      const team_card = store.team_apps[team_card_id];
      const team_user_id = team_card.receivers.find(receiver => (receiver.id === team_card_receiver_id)).team_user_id;
      dispatch(teamCardReceiverRemovedAction({
        team_id: team_id,
        team_card_id: team_card_id,
        team_user_id: team_user_id
      }));
      return response;
    }).catch(err => {
      throw err;
    });
  }
}

export function deleteTeamCard({team_id, team_card_id}) {
  return (dispatch, getState) => {
    return post_api.teamApps.deleteCard({
      team_id: team_id,
      team_card_id: team_card_id,
      ws_id: getState().common.ws_id
    }).then(response => {
      const team_card = getState().team_apps[team_card_id];
      dispatch(addNotification({
        text: `${team_card.name} successfully deleted!`
      }));
      dispatch(teamCardRemovedAction({
        team_id: team_id,
        team_card_id: team_card_id
      }));
      return response;
    }).catch(err => {
      throw err;
    });
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

export function teamCardChangedAction({team_card}) {
  return {
    type: 'TEAM_CARD_CHANGED',
    payload: {
      team_card: team_card
    }
  }
}

export function teamCardRemovedAction({team_id, team_card_id}){
  return (dispatch, getState) => {
    const store = getState();
    const apps = store.dashboard.apps;
    Object.keys(apps).map(app_id => {
      const app = apps[app_id];
      if (app.team_card_id === team_card_id)
        dispatch(dashboardAppRemovedAction({
          app_id: app.id
        }));
    });
    dispatch({
      type: 'TEAM_CARD_REMOVED',
      payload: {
        team_id: team_id,
        team_card_id:team_card_id
      }
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

export function teamCardReceiverChangedAction({receiver}) {
  return {
    type: 'TEAM_CARD_RECEIVER_CHANGED',
    payload: {
      receiver: receiver
    }
  }
}

export function teamCardReceiverRemovedAction({team_id, team_card_id, team_user_id}) {
  return (dispatch, getState) => {
    const store = getState();
    const team = store.teams[team_id];

    if (team.my_team_user_id === team_user_id){
      const team_card = store.team_apps[team_card_id];
      const receiver = team_card.receivers.find(receiver => (receiver.team_user_id === team_user_id));
      dispatch(deleteAppAction({
        app_id: receiver.app_id
      }));
    }
    dispatch({
      type: 'TEAM_CARD_RECEIVER_REMOVED',
      payload: {
        team_id: team_id,
        team_card_id: team_card_id,
        team_user_id: team_user_id
      }
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
