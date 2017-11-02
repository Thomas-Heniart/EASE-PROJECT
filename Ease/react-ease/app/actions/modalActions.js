export function showSimpleAppSettingsModal({active}){
  return {
    type: 'SHOW_SIMPLE_APP_SETTINGS_MODAL',
    payload: {
      active: active
    }
  }
}

export function showLogWithAppSettingsModal({active}){
  return {
    type:'SHOW_LOG_WITH_APP_SETTINGS_MODAL',
    payload: {
      active:active
    }
  }
}

export function showExtensionDownloadModal({active}){
  return {
    type: 'SHOW_EXTENSION_DOWNLOAD_MODAL',
    payload: {
      active: active
    }
  }
}

export function showLinkAppSettingsModal({active}){
  return {
    type: 'SHOW_LINK_APP_SETTINGS_MODAL',
    payload: {
      active: active
    }
  }
}

export function showPasswordLostInformationModal({active}){
  return {
    type: 'SHOW_PASSWORD_LOST_INFORMATION_MODAL',
    payload: {
      active: active
    }
  }
}

export function showLockedTeamAppModal({active}){
  return {
    type: 'SHOW_LOCKED_TEAM_APP_MODAL',
    payload: {
      active: active
    }
  }
}