export function showClassicAppSettingsModal({active, app}){
  return {
    type: 'SHOW_CLASSIC_APP_SETTINGS_MODAL',
    payload: {
      active: active,
      app: app
    }
  }
}

export function showLogWithAppSettingsModal({active, app}){
  return {
    type:'SHOW_LOG_WITH_APP_SETTINGS_MODAL',
    payload: {
      active:active,
      app: app
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

export function showLinkAppSettingsModal({active, app}){
  return {
    type: 'SHOW_LINK_APP_SETTINGS_MODAL',
    payload: {
      active: active,
      app: app
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

export function showChooseAppCredentialsModal({active, card_name, receivers, description, password_change_interval}){
  return {
    type: 'SHOW_CHOOSE_APP_CREDENTIALS_MODAL',
    payload: {
      active: active,
      card_name: card_name,
      receivers: receivers,
      description: description,
      password_change_interval: password_change_interval
    }
  }
}