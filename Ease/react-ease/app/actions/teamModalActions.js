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