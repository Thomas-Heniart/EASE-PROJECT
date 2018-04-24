import createReducer from  "./createReducer";

export const modals = createReducer({
  classicAppSettings: {
    active: false,
    app: null
  },
  linkAppSettings: {
    active: false,
    app: null
  },
  logWithAppSettings: {
    active: false,
    app: null
  },
  ssoAppSettings: {
    active: false,
    app: null
  },
  anyAppSettings: {
    active: false,
    app: null
  },
  softwareAppSettings: {
    active: false,
    app: null
  },
  teamSingleAppSettings: {
    active: false,
    app: null
  },
  teamAnySingleAppSettings: {
    active: false,
    app: null
  },
  teamSoftwareSingleAppSettings: {
    active: false,
    app: null
  },
  teamEnterpriseAppSettings: {
    active: false,
    app: null
  },
  teamAnyEnterpriseAppSettings: {
    active: false,
    app: null
  },
  teamSoftwareEnterpriseAppSettings: {
    active: false,
    app: null
  },
  teamLinkAppSettings: {
    active: false,
    app: null
  },
  extensionDownload: {
    active: false
  },
  passwordLostInformation: {
    active: false
  },
  lockedTeamApp: {
    active: false,
    team_user_id: -1
  },
  chooseAppCredentials: {
    active: false
  },
  chooseAnyAppCredentials: {
    active: false
  },
  chooseSoftwareAppCredentials: {
    active: false
  },
  updateAppPassword: {
    active: false,
    app: null
  },
  newFeature: {
    active: false
  },
  accountUpdate: {
    active: false,
    resolve: null,
    reject: null
  },
  passwordUpdate: {
    active: false,
    resolve: null,
    reject: null
  },
  newAccountUpdateLocation: {
      active: false,
      resolve: null,
      reject: null
  },
  newAccountUpdate: {
    active: false,
    resolve: null,
    reject: null
  },
  connectionDurationChooser: {
    active: false
  },
  magicLinkChooser: {
    active: false
  },
  passwordScoreUpgradeTeamPlan: {
    active: false,
    team_id: -1
  },
  appPasswordChangeAsk: {
    active: false,
    app: null,
    reason: ''
  },
  initializePasswordScoreFeature: {
    active: false,
    team_id: -1
  }
}, {
  ['SHOW_CLASSIC_APP_SETTINGS_MODAL'](state, action){
    return {
      ...state,
      classicAppSettings: action.payload
    }
  },
  ['SHOW_EXTENSION_DOWNLOAD_MODAL'](state, action){
    return {
      ...state,
      extensionDownload: action.payload
    }
  },
  ['SHOW_LINK_APP_SETTINGS_MODAL'](state, action){
    return {
      ...state,
      linkAppSettings: action.payload
    }
  },
  ['SHOW_PASSWORD_LOST_INFORMATION_MODAL'](state, action){
    return {
      ...state,
      passwordLostInformation: action.payload
    }
  },
  ['SHOW_LOCKED_TEAM_APP_MODAL'](state, action){
    return {
      ...state,
      lockedTeamApp:action.payload
    }
  },
  ['SHOW_LOG_WITH_APP_SETTINGS_MODAL'](state, action){
    return {
      ...state,
      logWithAppSettings: action.payload
    };
  },
  ['SHOW_SSO_APP_SETTINGS_MODAL'](state, action){
    return {
      ...state,
      ssoAppSettings: action.payload
    }
  },
  ['SHOW_ANY_APP_SETTINGS_MODAL'](state, action){
    return {
      ...state,
      anyAppSettings: action.payload
    }
  },
  ['SHOW_SOFTWARE_APP_SETTINGS_MODAL'](state, action){
    return {
      ...state,
      softwareAppSettings: action.payload
    }
  },
  ['SHOW_CHOOSE_APP_CREDENTIALS_MODAL'](state, action){
    return {
      ...state,
      chooseAppCredentials: action.payload
    };
  },
  ['SHOW_CHOOSE_ANY_APP_CREDENTIALS_MODAL'](state, action){
    return {
      ...state,
      chooseAnyAppCredentials: action.payload
    };
  },
  ['SHOW_CHOOSE_SOFTWARE_APP_CREDENTIALS_MODAL'](state, action){
    return {
      ...state,
      chooseSoftwareAppCredentials: action.payload
    };
  },
  ['SHOW_TEAM_LINK_APP_SETTINGS_MODAL'](state, action){
    return {
      ...state,
      teamLinkAppSettings: action.payload
    }
  },
  ['SHOW_TEAM_SINGLE_APP_SETTINGS_MODAL'](state, action){
    return {
      ...state,
      teamSingleAppSettings: action.payload
    }
  },
  ['SHOW_TEAM_ANY_SINGLE_APP_SETTINGS_MODAL'](state, action){
    return {
      ...state,
      teamAnySingleAppSettings: action.payload
    }
  },
  ['SHOW_TEAM_SOFTWARE_SINGLE_APP_SETTINGS_MODAL'](state, action){
    return {
      ...state,
      teamSoftwareSingleAppSettings: action.payload
    }
  },
  ['SHOW_TEAM_ENTERPRISE_APP_SETTINGS_MODAL'](state, action){
    return {
      ...state,
      teamEnterpriseAppSettings: action.payload
    }
  },
  ['SHOW_TEAM_ANY_ENTERPRISE_APP_SETTINGS_MODAL'](state, action){
    return {
      ...state,
      teamAnyEnterpriseAppSettings: action.payload
    }
  },
  ['SHOW_TEAM_SOFTWARE_ENTERPRISE_APP_SETTINGS_MODAL'](state, action){
    return {
      ...state,
      teamSoftwareEnterpriseAppSettings: action.payload
    }
  },
  ['SHOW_UPDATE_APP_PASSWORD_PASSWORD'](state, action){
    return {
      ...state,
      updateAppPassword: action.payload
    }
  },
  ['SHOW_NEW_FEATURE'](state, action){
    return {
      ...state,
      newFeature: action.payload
    }
  },
  ['SHOW_ACCOUNT_UPDATE_MODAL'](state, action){
    return {
      ...state,
      accountUpdate: action.payload
    }
  },
  ['SHOW_PASSWORD_UPDATE_MODAL'](state, action){
    return {
      ...state,
      passwordUpdate: action.payload
    }
  },
  ['SHOW_NEW_ACCOUNT_UPDATE_LOCATION_MODAL'](state, action){
      return {
          ...state,
          newAccountUpdateLocation: action.payload
      }
  },
  ['SHOW_NEW_ACCOUNT_UPDATE_MODAL'](state, action){
    return {
      ...state,
      newAccountUpdate: action.payload
    }
  },
  ['SHOW_CONNECTION_DURATION_CHOOSER_MODAL'](state, action){
    return {
        ...state,
      connectionDurationChooser: action.payload
    }
  },
  ['SHOW_MAGIC_LINK_CHOOSER_MODAL'](state, action){
    return {
        ...state,
      magicLinkChooser: action.payload
    }
  },
  ['SHOW_PASSWORD_SCORE_UPGRADE_TEAM_PLAN_MODAL'](state, action){
    return {
        ...state,
      passwordScoreUpgradeTeamPlan: action.payload
    }
  },
  ['SHOW_APP_PASSWORD_CHANGE_ASK_MODAL'](state, action){
    return {
        ...state,
      appPasswordChangeAsk: action.payload
    }
  },
  ['SHOW_INITIALIZE_PASSWORD_SCORE_FEATURE_MODAL'](state, action){
    return {
        ...state,
      initializePasswordScoreFeature: action.payload
    }
  }
});