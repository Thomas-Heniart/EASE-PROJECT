export function showAddTeamUserModal(state){
  return {
    type:'SHOW_ADD_TEAM_USER_MODAL',
    payload: state
  }
}

export function showAddTeamChannelModal(state){
  return {
    type: 'SHOW_ADD_TEAM_CHANNEL_MODAL',
    payload: state
  }
}

export function showTeamDeleteUserModal(state, team_user_id){
  return {
    type: 'SHOW_TEAM_DELETE_USER_MODAL',
    payload: {
      active: state,
      team_user_id: team_user_id
    }
  }
}

export function showTeamDeleteChannelModal(state, channel_id){
  return {
    type: 'SHOW_TEAM_DELETE_CHANNEL_MODAL',
    payload: {
      active: state,
      channel_id: channel_id
    }
  }
}

export function showTeamDeleteUserFromChannelModal(state, channel_id, team_user_id){
  return {
    type: 'SHOW_TEAM_DELETE_USER_FROM_CHANNEL_MODAL',
    payload: {
      active: state,
      channel_id: channel_id,
      team_user_id: team_user_id
    }
  }
}

export function showTeamDeleteAppModal(state, teamApp){
  return {
    type: 'SHOW_TEAM_DELETE_APP_MODAL',
    payload: {
      active: state,
      app: teamApp
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

export function showTeamManageAppRequestModal(state, app){
  return {
    type: 'SHOW_TEAM_MANAGE_APP_REQUEST_MODAL',
    payload: {
      active: state,
      app:app
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

export function showTeamJoinMultiAppModal(state, user, app){
  return {
    type: 'SHOW_TEAM_JOIN_MULTI_APP_MODAL',
    payload: {
      active: state,
      user: user,
      app:app
    }
  }
}

export function showTeamEditEnterpriseAppModal(state, user, app){
  return {
    type: 'SHOW_TEAM_EDIT_ENTERPRISE_APP_MODAL',
    payload: {
      active: state,
      user: user,
      app:app
    }
  }
}

export function showTeamAskJoinMultiAppModal(state, user, app){
  return {
    type: 'SHOW_TEAM_ASK_JOIN_ENTERPRISE_APP_MODAL',
    payload: {
      active: state,
      user: user,
      app:app
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

export function showVerifyTeamUserModal(state, user){
  return {
    type: 'SHOW_VERIFY_TEAM_USER_MODAL',
    payload: {
      active: state,
      user: user
    }
  }
}

export function showReactivateTeamUserModal(state, user){
  return {
    type: 'SHOW_REACTIVATE_TEAM_USER_MODAL',
    payload: {
      active: state,
      user: user
    }
  }
}

export function showTeamTransferOwnershipModal(state, user){
  return {
    type: 'SHOW_TEAM_TRANSFER_OWNERSHIP_MODAL',
    payload: {
      active: state,
      user: user
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

export function showTeamAddMultipleUsersModal(state){
  return {
    type: 'SHOW_TEAM_ADD_MULTIPLE_USERS_MODAL',
    payload: {
      active: state
    }
  }
}

export function showUpgradeTeamPlanModal(state, feature_id) {
  return {
    type: 'SHOW_UPGRADE_TEAM_PLAN_MODAL',
    payload: {
      active: state,
      feature_id: feature_id
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

export function showDepartureDateEndModal(state, user) {
    return {
        type: 'DEPARTURE_DATE_END_MODAL',
        payload: {
            active: state,
            user: user
        }
    }
}