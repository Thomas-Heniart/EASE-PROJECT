import createReducer from "./createReducer";
import update from 'immutability-helper';

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
        profile.column_idx = idx;
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
  },
  ['DASHBOARD_APP_CHANGED'](state, action){
    const app = action.payload.app;
    return update(state, {
      apps: {
        [app.id]: {$set: app}
      }
    });
  },
  ['DASHBOARD_PROFILE_REMOVED'](state, action){
    const profile_id = action.payload.profile_id;
    const profile = state.profiles[profile_id];
    const index = state.columns[profile.column_index].indexOf(profile_id);

    return update(state, {
      columns: {
        [profile.column_index]: {$splice: [[index, 1]]}
      }
    });
  },
  ['DASHBOARD_PROFILE_CREATED'](state, action){
    const profile = action.payload.profile;

    return update(state, {
      profiles: {
        [profile.id]: {$set: profile}
      },
      columns: {
        [profile.column_index]: {$push: [profile.id]}
      }
    });
  },
  ['DASHBOARD_PROFILE_CHANGED'](state, action){
    const profile = action.payload.profile;
    return update(state, {
      profiles: {
        [profile.id]: {$set: profile}
      }
    });
  },
  ['INSERT_APP'](state, action){
    const {app_id, targetApp_id} = action.payload;
    const app = state.apps[app_id];
    const targetApp = state.apps[targetApp_id];
    const profileId = targetApp.profile_id;
    const index = state.profiles[profileId].app_ids.indexOf(targetApp.id);
    const sourceIdx = state.profiles[app.profile_id].app_ids.indexOf(app.id);

    if (app.profile_id === profileId)
      return  update(state, {
        profiles: {
          [app.profile_id]: {
            app_ids: {$splice: [[sourceIdx, 1], [index, 0, app.id]]}
          },
        }
      });
  },
  ['INSERT_APP_IN_PROFILE'](state, action){
    const {app_id, profile_id} = action.payload;
    const app = state.apps[app_id];
    const insert_idx = state.profiles[profile_id].app_ids.length;
    const sourceIdx = state.profiles[app.profile_id].app_ids.indexOf(app_id);

    return update(state, {
      profiles: {
        [app.profile_id]: {
          app_ids: {$splice: [[sourceIdx, 1]]}
        },
        [profile_id]: {
          app_ids: {$splice: [[insert_idx, 0, app_id]]}
        }
      },
      apps: {
        [app_id]: {
          profile_id: {$set: profile_id}
        }
      }
    })
  },
  ['INSERT_PROFILE'](state, action){
    const {profile_id, targetProfile_id} = action.payload;
    const profile = state.profiles[profile_id];
    const targetProfile = state.profiles[targetProfile_id];
    const insertIdx = state.columns[targetProfile.column_idx].indexOf(targetProfile_id);
    const sourceIdx = state.columns[profile.column_idx].indexOf(profile_id);

    if (profile.column_idx === targetProfile.column_idx){
      return update(state, {
        columns: {
          [profile.column_idx] : {$splice: [[sourceIdx, 1], [insertIdx, 0, profile_id]]}
        }
      });
    }
    return update(state, {
      columns: {
        [profile.column_idx]: {$splice: [[sourceIdx, 1]]},
        [targetProfile.column_idx]: {$splice: [[insertIdx, 0, profile_id]]}
      },
      profiles: {
        [profile_id]:{
          column_idx: {$set: targetProfile.column_idx}
        }
      }
    });
  },
  ['INSERT_PROFILE_IN_COLUMN'](state, action){
    const {profile_id, column_idx} = action.payload;
    const profile = state.profiles[profile_id];
    const sourceIdx = state.columns[profile.column_idx].indexOf(profile_id);
    const insertIdx = state.columns[column_idx].length;

    return update(state, {
      columns: {
        [profile.column_idx]: {$splice: [[sourceIdx, 1]]},
        [column_idx]: {$splice: [[insertIdx, 0, profile.id]]}
      },
      profiles: {
        [profile_id]: {
          column_idx: {$set: column_idx}
        }
      }
    });
  }
});

export const dashboard_dnd = createReducer({
  dragging_app_id: -1,
  dragging_profile_id: -1
}, {
  ['BEGIN_APP_DRAG'](state, action){
    return update(state, {
      dragging_app_id: {$set: action.payload.app_id}
    })
  },
  ['END_APP_DRAG'](state, action){
    return update(state, {
      dragging_app_id: {$set: -1}
    })
  },
  ['BEGIN_PROFILE_DRAG'](state, action){
    return update(state, {
      dragging_profile_id: {$set:action.payload.profile_id}
    })
  },
  ['END_PROFILE_DRAG'](state, action){
    return update(state, {
      dragging_profile_id: {$set: -1}
    })
  }
});