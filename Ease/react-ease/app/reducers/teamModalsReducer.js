const initialState = {
  addUserModalActive: false,
  addChannelModalActive: false,
  teamSettingsModalActive: false,
  teamAddMultipleUsersModal:{
    active: false
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
  teamEditEnterpriseAppModal: {
    active: false,
    user: null,
    app: null
  },
  teamAskJoinEnterpriseAppModal: {
    active: false,
    user: null,
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
  },
  verifyTeamUserModal: {
    active: false,
    user: null
  },
  reactivateTeamUserModal: {
    active: false,
    user: null
  },
  teamTransferOwnershipModal: {
    active: false,
    user: null
  },
  teamPhoneNumberModal: {
    active: false
  },
  requestWebsiteModal: {
    active: false,
    resolve: null,
    reject: null
  },
  upgradeTeamPlanModal: {
    active: false,
    feature_id: null
  },
  freeTrialEndModal: {
    active: false
  },
  departureDateEndModal: {
    active: false,
    user: null
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
    case 'SHOW_TEAM_ASK_JOIN_ENTERPRISE_APP_MODAL': {
      return {
        ...state,
        teamAskJoinEnterpriseAppModal: action.payload
      }
    }
    case 'SHOW_TEAM_EDIT_ENTERPRISE_APP_MODAL': {
      return {
          ...state,
        teamEditEnterpriseAppModal: action.payload
      }
    }
    case 'SHOW_TEAM_JOIN_MULTI_APP_MODAL': {
      return {
        ...state,
        teamJoinMultiAppModal: action.payload
      }
    }
    case 'SHOW_TEAM_SETTINGS_MODAL': {
      return {
          ...state,
        teamSettingsModalActive: action.payload.active
      }
    }
    case 'SHOW_VERIFY_TEAM_USER_MODAL': {
      return {
          ...state,
        verifyTeamUserModal: action.payload
      }
    }
    case 'SHOW_REACTIVATE_TEAM_USER_MODAL': {
      return {
          ...state,
        reactivateTeamUserModal: action.payload
      }
    }
    case 'SHOW_TEAM_TRANSFER_OWNERSHIP_MODAL': {
      return {
          ...state,
        teamTransferOwnershipModal: action.payload
      }
    }
    case 'SHOW_TEAM_PHONE_NUMBER_MODAL': {
      return {
          ...state,
        teamPhoneNumberModal: action.payload
      }
    }
    case 'SHOW_REQUEST_WEBSITE_MODAL': {
      return {
          ...state,
        requestWebsiteModal: action.payload
      }
    }
    case 'SHOW_TEAM_ADD_MULTIPLE_USERS_MODAL': {
      return {
          ...state,
        teamAddMultipleUsersModal: action.payload
      }
    }
    case 'SHOW_UPGRADE_TEAM_PLAN_MODAL' : {
      return {
          ...state,
        upgradeTeamPlanModal: action.payload
      }
    }
    case 'SHOW_FREE_TRIAL_END_MODAL': {
      return {
          ...state,
        freeTrialEndModal: action.payload
      }
    }
    case 'DEPARTURE_DATE_END_MODAL': {
      return {
          ...state,
        departureDateEndModal: action.payload
      }
    }
  }
  return state;
}