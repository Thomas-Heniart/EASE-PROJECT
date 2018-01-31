import createReducer from  "./createReducer";

export const onBoarding = createReducer({
  team_id: null,
  passwordManager: null
}, {
  ['GO_TO_ON_BOARDING_IMPORTATION'](state, action){
    return {
      ...state,
      team_id: action.payload.data.team_id,
      passwordManager: action.payload.data.passwordManager
    }
  },
  ['RESET_ON_BOARDING_PASSWORD_MANAGER'](state, action){
    return {
      ...state,
      team_id: null,
      passwordManager: null
    }
  },
  ['GO_TO_ON_BOARDING'](state, action){
    return {
      ...state,
      team_id: action.payload.team_id
    }
  }
});