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

export function showTeamChannelAddUserModal(state, channel_id){
  return {
    type: 'SHOW_TEAM_CHANNEL_ADD_USER_MODAL',
    payload: {
      active: state,
      channel_id: channel_id
    }
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

export function showTeamBrowseChannelsModal(state){
  return {
    type: 'SHOW_TEAM_BROWSE_CHANNELS_MODAL',
    payload: {
      active: state
    }
  }
}