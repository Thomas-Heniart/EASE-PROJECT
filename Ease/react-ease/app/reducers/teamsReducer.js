import createReducer from  "./createReducer";

export const teams = createReducer({
  teams: {}
}, {
  ['FETCH_TEAMS_FULFILLED'](state, action){
    return {
        ...state,
      teams: action.payload.teams
    }
  }
});