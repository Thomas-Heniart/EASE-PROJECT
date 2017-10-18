import createReducer from  "./createReducer";

export const apps = createReducer({
  apps: [],
  loading: false
}, {
  ['FETCH_APPS_PENDING'](state, action){
    return {
      apps: [],
      loading: true
    }
  },
  ['FETCH_APPS_FULFILLED'](state, action){
    return {
      apps: action.payload.apps,
      loading: false
    }
  },
  ['FETCH_APP_REJECTED'](state, action){
    return {
      apps: [],
      loading: false
    }
  }
});