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
  },
  teamDeleteAppModal: {
    active: false,
    app: null
  },
  pinTeamAppToDashboardModal: {
    active: false,
    app: null
  },
  teamLeaveAppModal: {
    active: false,
    app: null,
    team_user_id: -1
  },
  teamManageAppRequestModal: {
    active: false,
    app: null
  },
  teamAcceptMultiAppModal: {
    active: false,
    user: null,
    app: null
  },
  teamJoinMultiAppModal: {
    active: false,
    user:null,
    app: null
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
    case 'SHOW_TEAM_DELETE_APP_MODAL': {
      return {
        ...state,
        teamDeleteAppModal: action.payload
      }
    }
    case 'SHOW_PIN_TEAM_APP_TO_DASHBOARD_MODAL': {
      return {
        ...state,
        pinTeamAppToDashboardModal: action.payload
      }
    }
    case 'SHOW_TEAM_LEAVE_APP_MODAL': {
      return {
          ...state,
        teamLeaveAppModal: action.payload
      }
    }
    case 'SHOW_TEAM_MANAGE_APP_REQUEST_MODAL': {
      return {
        ...state,
        teamManageAppRequestModal: action.payload
      }
    }
    case 'SHOW_TEAM_ACCEPT_MULTI_APP_MODAL': {
      return {
          ...state,
        teamAcceptMultiAppModal: action.payload
      }
    }
    case 'SHOW_TEAM_JOIN_MULTI_APP_MODAL': {
      return {
        ...state,
        teamJoinMultiAppModal: action.payload
      }
    }
  }
  return state;
}