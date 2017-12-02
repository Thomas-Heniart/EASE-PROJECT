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

export function showLinkAppSettingsModal({active, app}){
  return {
    type: 'SHOW_LINK_APP_SETTINGS_MODAL',
    payload: {
      active: active,
      app: app
    }
  }
}

export function showTeamLinkAppSettingsModal({active, app}){
  return {
    type: 'SHOW_TEAM_LINK_APP_SETTINGS_MODAL',
    payload: {
      active: active,
      app:app
    }
  }
}

export function showTeamSingleAppSettingsModal({active, app}){
  return {
    type: 'SHOW_TEAM_SINGLE_APP_SETTINGS_MODAL',
    payload: {
      active: active,
      app:app
    }
  }
}

export function showTeamEnterpriseAppSettingsModal({active, app}){
  return {
    type: 'SHOW_TEAM_ENTERPRISE_APP_SETTINGS_MODAL',
    payload: {
      active: active,
      app:app
    }
  }
}

export function showSsoAppSettingsModal({active, app}) {
  return {
    type: 'SHOW_SSO_APP_SETTINGS_MODAL',
    payload: {
      active: active,
      app:app
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


export function showPasswordLostInformationModal({active}){
  return {
    type: 'SHOW_PASSWORD_LOST_INFORMATION_MODAL',
    payload: {
      active: active
    }
  }
}

export function showLockedTeamAppModal({active, team_user_id}){
  return {
    type: 'SHOW_LOCKED_TEAM_APP_MODAL',
    payload: {
      active: active,
      team_user_id: team_user_id
    }
  }
}

export function showChooseAppCredentialsModal({active, card_name, receivers, description, password_reminder_interval}){
  return {
    type: 'SHOW_CHOOSE_APP_CREDENTIALS_MODAL',
    payload: {
      active: active,
      card_name: card_name,
      receivers: receivers,
      description: description,
      password_reminder_interval: password_reminder_interval
    }
  }
}

export function showUpdateAppPasswordModal({active, app}) {
  return {
    type: 'SHOW_UPDATE_APP_PASSWORD_PASSWORD',
    payload: {
      active: active,
      app: app
    }
  }
}