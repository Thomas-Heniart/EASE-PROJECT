export function showClassicAppSettingsModal({active, app, remove}){
  return {
    type: 'SHOW_CLASSIC_APP_SETTINGS_MODAL',
    payload: {
      active: active,
      app:app,
      remove: remove
    }
  }
}

export function showLogWithAppSettingsModal({active, app, remove}){
  return {
    type:'SHOW_LOG_WITH_APP_SETTINGS_MODAL',
    payload: {
      active:active,
      app:app,
      remove: remove
    }
  }
}

export function showLinkAppSettingsModal({active, app, remove}){
  return {
    type: 'SHOW_LINK_APP_SETTINGS_MODAL',
    payload: {
      active: active,
      app:app,
      remove: remove
    }
  }
}

export function showTeamLinkAppSettingsModal({active, app, remove}){
  return {
    type: 'SHOW_TEAM_LINK_APP_SETTINGS_MODAL',
    payload: {
      active: active,
      app:app,
      remove: remove
    }
  }
}

export function showTeamSingleAppSettingsModal({active, app, remove}){
  return {
    type: 'SHOW_TEAM_SINGLE_APP_SETTINGS_MODAL',
    payload: {
      active: active,
      app:app,
      remove: remove
    }
  }
}

export function showTeamAnySingleAppSettingsModal({active, app, remove}){
  return {
    type: 'SHOW_TEAM_ANY_SINGLE_APP_SETTINGS_MODAL',
    payload: {
      active: active,
      app:app,
      remove: remove
    }
  }
}

export function showTeamSoftwareSingleAppSettingsModal({active, app, remove}){
  return {
    type: 'SHOW_TEAM_SOFTWARE_SINGLE_APP_SETTINGS_MODAL',
    payload: {
      active: active,
      app:app,
      remove: remove
    }
  }
}

export function showTeamEnterpriseAppSettingsModal({active, app, remove}){
  return {
    type: 'SHOW_TEAM_ENTERPRISE_APP_SETTINGS_MODAL',
    payload: {
      active: active,
      app:app,
      remove: remove
    }
  }
}

export function showTeamAnyEnterpriseAppSettingsModal({active, app, remove}){
  return {
    type: 'SHOW_TEAM_ANY_ENTERPRISE_APP_SETTINGS_MODAL',
    payload: {
      active: active,
      app:app,
      remove: remove
    }
  }
}

export function showTeamSoftwareEnterpriseAppSettingsModal({active, app, remove}){
  return {
    type: 'SHOW_TEAM_SOFTWARE_ENTERPRISE_APP_SETTINGS_MODAL',
    payload: {
      active: active,
      app:app,
      remove: remove
    }
  }
}

export function showSsoAppSettingsModal({active, app, remove}) {
  return {
    type: 'SHOW_SSO_APP_SETTINGS_MODAL',
    payload: {
      active: active,
      app:app,
      remove: remove
    }
  }
}

export function showAnyAppSettingsModal({active, app, remove}) {
  return {
    type: 'SHOW_ANY_APP_SETTINGS_MODAL',
    payload: {
      active: active,
      app:app,
      remove: remove
    }
  }
}

export function showSoftwareAppSettingsModal({active, app, remove}) {
  return {
    type: 'SHOW_SOFTWARE_APP_SETTINGS_MODAL',
    payload: {
      active: active,
      app:app,
      remove: remove
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

export function showChooseAnyAppCredentialsModal({active, card_name, receivers, description, url, img_url, logoLetter, subtype, password_reminder_interval}){
  return {
    type: 'SHOW_CHOOSE_ANY_APP_CREDENTIALS_MODAL',
    payload: {
      active: active,
      card_name: card_name,
      receivers: receivers,
      description: description,
      url: url,
      img_url: img_url,
      logoLetter: logoLetter,
      subtype: subtype,
      password_reminder_interval: password_reminder_interval
    }
  }
}

export function showChooseSoftwareAppCredentialsModal({active, card_name, receivers, description, url, img_url, logoLetter, subtype, password_reminder_interval}){
  return {
    type: 'SHOW_CHOOSE_SOFTWARE_APP_CREDENTIALS_MODAL',
    payload: {
      active: active,
      card_name: card_name,
      receivers: receivers,
      description: description,
      img_url: img_url,
      logoLetter: logoLetter,
      subtype: subtype,
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

export function showNewFeatureModal({active}) {
  return {
    type: 'SHOW_NEW_FEATURE',
    payload: {
      active: active
    }
  }
}

export function showConnectionDurationChooserModal({active}){
  return {
    type: 'SHOW_CONNECTION_DURATION_CHOOSER_MODAL',
    payload: {
      active: active
    }
  }
}