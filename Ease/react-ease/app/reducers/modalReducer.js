import createReducer from  "./createReducer";

export const modals = createReducer({
  simpleAppSettings: {
    active: false
  },
  linkAppSettings: {
    active: false
  },
  logWithAppSettings: {
    active: false
  },
  extensionDownload: {
    active: false
  },
  passwordLostInformation: {
    active: false
  },
  lockedTeamApp: {
    active: false
  }
}, {
  ['SHOW_SIMPLE_APP_SETTINGS_MODAL'](state, action){
    return {
        ...state,
      simpleAppSettings: action.payload
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
  }
});