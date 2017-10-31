import createReducer from  "./createReducer";

export const modals = createReducer({
  simpleAppSettings: {
    active: false
  }
}, {
  ['SHOW_SIMPLE_APP_SETTINGS_MODAL'](state, action){
    return {
        ...state,
      simpleAppSettings: action.payload
    }
  }
});