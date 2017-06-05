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

export function showTeamChannelAddUserModal(state){
  return {
    type: 'SHOW_TEAM_CHANNEL_ADD_USER_MODAL',
    payload: state
  }
}

export function showTeamDeleteUserModal(state){
  return {
    type: 'SHOW_TEAM_DELETE_USER_MODAL',
    payload: state
  }
}