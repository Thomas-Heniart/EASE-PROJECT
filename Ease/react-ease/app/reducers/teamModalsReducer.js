const initialState = {
  addUserModalActive: false,
  addChannelModalActive: false,
  teamChannelAddUserModalActive: false,
  teamDeleteUserModalActive: false
}
export default function reducer(state=initialState, action){
  switch (action.type){
    case 'SHOW_ADD_TEAM_USER_MODAL': {
      return {
          ...initialState,
        addUserModalActive: action.payload
      }
    }
    case 'SHOW_ADD_TEAM_CHANNEL_MODAL': {
      return {
          ...initialState,
        addChannelModalActive: action.payload
      }
    }
    case 'SHOW_TEAM_CHANNEL_ADD_USER_MODAL': {
      return {
          ...initialState,
        teamChannelAddUserModalActive:action.payload
      }
    }
    case 'SHOW_TEAM_DELETE_USER_MODAL': {
      return {
          ...initialState,
          teamDeleteUserModalActive: action.payload
      }
    }
  }
  return state;
}