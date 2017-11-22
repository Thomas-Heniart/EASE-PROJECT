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
  teamSingleAppSettings: {
    active: false,
    app: null
  },
  teamEnterpriseAppSettings: {
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
    active: false
  },
  chooseAppCredentials: {
    active: false
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
  ['SHOW_CHOOSE_APP_CREDENTIALS_MODAL'](state, action){
    return {
      ...state,
      chooseAppCredentials: action.payload
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
  ['SHOW_TEAM_ENTERPRISE_APP_SETTINGS_MODAL'](state, action){
    return {
      ...state,
      teamEnterpriseAppSettings: action.payload
    }
  }
});