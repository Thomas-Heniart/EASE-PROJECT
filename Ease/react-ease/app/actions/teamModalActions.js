export function showAddTeamUserModal({active, team_id}){
  return {
    type:'SHOW_ADD_TEAM_USER_MODAL',
    payload: {
      active: active,
      team_id: team_id
    }
  }
}

export function showAddTeamChannelModal({active, team_id}){
  return {
    type: 'SHOW_ADD_TEAM_CHANNEL_MODAL',
    payload: {
      active: active,
      team_id: team_id
    }
  }
}

export function showTeamDeleteUserModal({active,team_id, team_user_id}){
  return {
    type: 'SHOW_TEAM_DELETE_USER_MODAL',
    payload: {
      active: active,
      team_user_id: team_user_id,
      team_id: team_id
    }
  }
}

export function showTeamDeleteChannelModal({active,team_id, room_id}){
  return {
    type: 'SHOW_TEAM_DELETE_CHANNEL_MODAL',
    payload: {
      active: active,
      room_id: room_id,
      team_id: team_id
    }
  }
}

export function showTeamDeleteUserFromChannelModal({active, team_id, room_id, team_user_id}){
  return {
    type: 'SHOW_TEAM_DELETE_USER_FROM_CHANNEL_MODAL',
    payload: {
      active: active,
      room_id: room_id,
      team_user_id: team_user_id,
      team_id: team_id
    }
  }
}

export function showTeamDeleteAppModal({active, app_id}){
  return {
    type: 'SHOW_TEAM_DELETE_APP_MODAL',
    payload: {
      active: active,
      app_id: app_id
    }
  }
}

export function showPinTeamAppToDashboardModal(state, teamApp){
  return {
    type: 'SHOW_PIN_TEAM_APP_TO_DASHBOARD_MODAL',
    payload: {
      active: state,
      app: teamApp
    }
  }
}

export function showTeamLeaveAppModal(state, app, team_user_id){
  return {
    type: 'SHOW_TEAM_LEAVE_APP_MODAL',
    payload: {
      active: state,
      app: app,
      team_user_id: team_user_id
    }
  }
}

export function showTeamManageAppRequestModal({active, team_card_id}){
  return {
    type: 'SHOW_TEAM_MANAGE_APP_REQUEST_MODAL',
    payload: {
      active: active,
      team_card_id:team_card_id
    }
  }
}

export function showTeamAcceptMultiAppModal(state, user, app){
  return {
    type: 'SHOW_TEAM_ACCEPT_MULTI_APP_MODAL',
    payload: {
      active: state,
      user: user,
      app: app
    }
  }
}

export function showTeamJoinEnterpriseAppModal({active, team_card_id}){
  return {
    type: 'SHOW_TEAM_JOIN_ENTERPRISE_APP_MODAL',
    payload: {
      active: active,
      team_card_id: team_card_id
    }
  }
}

export function showTeamEditEnterpriseAppModal({active, team_card_id}){
  return {
    type: 'SHOW_TEAM_EDIT_ENTERPRISE_APP_MODAL',
    payload: {
      active: active,
      team_card_id: team_card_id
    }
  }
}

export function showTeamAskJoinEnterpriseAppModal({active, team_card_id}) {
  return {
    type: 'SHOW_TEAM_ASK_JOIN_ENTERPRISE_APP_MODAL',
    payload: {
      active: active,
      team_card_id: team_card_id
    }
  }
}

export function showTeamSettingsModal(state){
  return {
    type: 'SHOW_TEAM_SETTINGS_MODAL',
    payload: {
      active: state
    }
  }
}

export function showVerifyTeamUserModal({active, team_user_id,team_id}){
  return {
    type: 'SHOW_VERIFY_TEAM_USER_MODAL',
    payload: {
      active: active,
      team_user_id: team_user_id,
      team_id: team_id
    }
  }
}

export function showReactivateTeamUserModal({active, team_id, team_user_id}){
  return {
    type: 'SHOW_REACTIVATE_TEAM_USER_MODAL',
    payload: {
      active: active,
      team_user_id: team_user_id,
      team_id: team_id
    }
  }
}

export function showTeamTransferOwnershipModal({active, team_id, team_user_id}){
  return {
    type: 'SHOW_TEAM_TRANSFER_OWNERSHIP_MODAL',
    payload: {
      active: active,
      team_id: team_id,
      team_user_id: team_user_id
    }
  }
}

export function showTeamPhoneNumberModal({active, team_id, team_user_id}) {
  return {
    type: 'SHOW_TEAM_PHONE_NUMBER_MODAL',
    payload: {
      active: active,
      team_id: team_id,
      team_user_id: team_user_id
    }
  }
}

export function requestWebsite(dispatch){
  return new Promise((resolve, reject) => {
    dispatch(showRequestWebsiteModal(true, resolve, reject));
  });
}

export function showRequestWebsiteModal(state, resolve, reject){
  return {
    type: 'SHOW_REQUEST_WEBSITE_MODAL',
    payload: {
      active: state,
      resolve: resolve,
      reject: reject
    }
  }
}

export function showTeamAddMultipleUsersModal({active, team_id}){
  return {
    type: 'SHOW_TEAM_ADD_MULTIPLE_USERS_MODAL',
    payload: {
      active: active,
      team_id: team_id
    }
  }
}

export function showUpgradeTeamPlanModal({active, feature_id,team_id}) {
  return {
    type: 'SHOW_UPGRADE_TEAM_PLAN_MODAL',
    payload: {
      active: active,
      feature_id: feature_id,
      team_id: team_id
    }
  }
}

export function showFreeTrialEndModal(state){
  return {
    type: 'SHOW_FREE_TRIAL_END_MODAL',
    payload: {
      active: state
    }
  }
}

export function showDepartureDateEndModal({active, team_user_id, team_id}) {
  return {
    type: 'DEPARTURE_DATE_END_MODAL',
    payload: {
      active: active,
      team_user_id: team_user_id,
      team_id: team_id
    }
  }
}