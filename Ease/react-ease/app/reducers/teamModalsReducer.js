const initialState = {
  addUserModal: {
    active: false,
    team_id: -1
  },
  addChannelModal: {
    active: false,
    team_id: -1
  },
  teamSettingsModalActive: false,
  teamAddMultipleUsersModal:{
    active: false,
    team_id: -1
  },
  teamDeleteUserModal: {
    active: false,
    team_user_id: -1,
    team_id: -1
  },
  teamDeleteChannelModal: {
    active: false,
    room_id: -1,
    team_id: -1
  },
  teamDeleteUserFromChannelModal: {
    active: false,
    room_id: -1,
    team_user_id: -1,
    team_id: -1
  },
  teamDeleteAppModal: {
    active: false,
    app_id: -1
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
    team_card_id: null
  },
  teamEditEnterpriseAppModal: {
    active: false,
    team_card_id: -1
  },
  teamAskJoinEnterpriseAppModal: {
    active: false,
    team_card_id: -1
  },
  teamAcceptMultiAppModal: {
    active: false,
    user: null,
    app: null
  },
  teamJoinEnterpriseAppModal: {
    active: false,
    team_card_id:-1
  },
  verifyTeamUserModal: {
    active: false,
    team_user_id: -1,
    team_id: -1
  },
  reactivateTeamUserModal: {
    active: false,
    team_user_id: -1,
    team_id: -1
  },
  teamTransferOwnershipModal: {
    active: false,
    team_id: -1,
    team_user_id: -1
  },
  teamPhoneNumberModal: {
    active: false,
    team_id: -1,
    team_user_id: -1
  },
  requestWebsiteModal: {
    active: false,
    resolve: null,
    reject: null
  },
  upgradeTeamPlanModal: {
    active: false,
    feature_id: -1,
    team_id: -1
  },
  teamUserInviteLimitReachedModal: {
    active: false,
    team_id: -1
  },
  freeTrialEndModal: {
    active: false
  },
  departureDateEndModal: {
    active: false,
    team_user_id: -1,
    team_id: -1
  },
  inviteTeamUsersModal: {
    active: false,
    team_id: -1
  },
  catalogAddBookmarkModal: {
    active: false,
    name: '',
    url: '',
    img_url: '',
    logoLetter: '',
    resolve: null,
    reject: null
  },
  catalogAddAppModal: {
    active: false,
    website: null
  },
  catalogAddSSOAppModal: {
    active: false,
    website: null
  },
  catalogAddAnyAppModal: {
    active: false,
    name: '',
    url: '',
    img_url: '',
    logoLetter: ''
  },
  catalogAddSoftwareAppModal: {
    active: false,
    name: '',
    img_url: '',
    logoLetter: ''
  }
};
export default function reducer(state=initialState, action){
  switch (action.type){
    case 'SHOW_ADD_TEAM_USER_MODAL': {
      return {
        ...initialState,
        addUserModal: action.payload
      }
    }
    case 'SHOW_ADD_TEAM_CHANNEL_MODAL': {
      return {
        ...initialState,
        addChannelModal: action.payload
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
    case 'SHOW_TEAM_JOIN_ENTERPRISE_APP_MODAL': {
      return {
        ...state,
        teamJoinEnterpriseAppModal: action.payload
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
    case 'SHOW_TEAM_USER_INVITE_LIMIT_REACHED_MODAL': {
      return {
          ...state,
        teamUserInviteLimitReachedModal: action.payload
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
    case 'INVITE_TEAM_USERS_MODAL': {
      return {
          ...state,
        inviteTeamUsersModal: action.payload
      }
    }
    case 'SHOW_CATALOG_ADD_BOOKMARK_MODAL': {
      return {
        ...state,
        catalogAddBookmarkModal: action.payload
      }
    }
    case 'SHOW_CATALOG_ADD_APP_MODAL': {
      return {
        ...state,
        catalogAddAppModal: action.payload
      }
    }
    case 'SHOW_CATALOG_ADD_SSO_APP_MODAL': {
      return {
          ...state,
        catalogAddSSOAppModal: action.payload
      }
    }
    case 'SHOW_CATALOG_ADD_ANY_APP_MODAL': {
      return {
        ...state,
        catalogAddAnyAppModal: action.payload
      }
    }
    case 'SHOW_CATALOG_ADD_SOFTWARE_APP_MODAL': {
      return {
        ...state,
        catalogAddSoftwareAppModal: action.payload
      }
    }
  }
  return state;
}