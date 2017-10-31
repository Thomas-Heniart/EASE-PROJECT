export function showSimpleAppSettingsModal({active}){
  return {
    type: 'SHOW_SIMPLE_APP_SETTINGS_MODAL',
    payload: {
      active: active
    }
  }
}