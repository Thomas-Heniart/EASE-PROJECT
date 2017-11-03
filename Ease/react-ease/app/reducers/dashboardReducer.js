import createReducer from "./createReducer";

export const dashboard = createReducer({
  columns: [],
  profiles: {},
  apps: {}
}, {
  ['FETCH_DASHBOARD_FULFILLED'](state, action){
    let columns = [];
    let profiles = {};
    let apps = {};

    columns = action.payload.columns.map((item, idx) => {
      return item.map(profile => {
        profiles[profile.id] = profile;
        return profile.id;
      });
    });
    action.payload.apps.map(app => {
      apps[app.id] = app;
    });
    return {
        ...state,
      columns: columns,
      profiles: profiles,
      apps: apps
    }
  }
});