const initialState = {
  addUserModalActive: false,
  addChannelModalActive: false,
  teamChannelAddUserModal: {
    active: false,
    channel_id: -1
  },
  teamDeleteUserModal: {
    active: false,
    team_user_id: -1
  },
  teamDeleteChannelModal: {
    active: false,
    channel_id: -1
  },
  teamDeleteUserFromChannelModal: {
    active: false,
    channel_id: -1,
    team_user_id: -1
  }
};
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
        teamChannelAddUserModal:action.payload
      }
    }
    case 'SHOW_TEAM_DELETE_USER_MODAL': {
      return {
          ...initialState,
          teamDeleteUserModal: action.payload
      }
    }
    case 'SHOW_TEAM_DELETE_CHANNEL_MODAL': {
      return {
          ...state,
          teamDeleteChannelModal: action.payload
      }
    }
    case 'SHOW_TEAM_DELETE_USER_FROM_CHANNEL_MODAL': {
      return {
        ...state,
        teamDeleteUserFromChannelModal: action.payload
      }
    }
  }
  return state;
}