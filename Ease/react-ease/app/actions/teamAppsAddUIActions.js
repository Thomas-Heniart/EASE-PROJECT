export function showAppAddUIElement(type, state){
  return {
    type: 'SHOW_APP_ADD_UI_ELEMENT',
    payload: {
      type: type,
      state: state
    }
  }
}

export function closeAppAddUI(){
  return {
    type: 'CLOSE_APP_ADD_UI'
  }
}