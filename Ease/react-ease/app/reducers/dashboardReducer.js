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

    columns = action.payload.columns.map(item => {
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
  },
  ['DASHBOARD_APP_CHANGED'](state, action){
    const app = action.payload.app;
    return update(state, {
      apps: {
        [app.id]: {$set: app}
      }
    });
  },
  ['DASHBOARD_APP_REMOVED'](state, action){
    const app_id = action.payload.app_id;
    const app = state.apps[app_id];

    return update(state, {
      apps: {$unset: [app_id]},
      profiles: {
        [app.profile_id]: {
          app_ids: {$splice: [[state.profiles[app.profile_id].app_ids.indexOf(app_id), 1]]}
        }
      }
    });
  },
  ['DASHBOARD_APP_ADDED'](state, action){
    const {app} = action.payload;

    return update(state, {
      apps: {
        [app.id]: {$set: app}
      },
      profiles: {
        [app.profile_id]: {
          app_ids: {$push: [app.id]}
        }
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
    const insertIdx = state.columns[targetProfile.column_index].indexOf(targetProfile_id);
    const sourceIdx = state.columns[profile.column_index].indexOf(profile_id);

    if (profile.column_index === targetProfile.column_index){
      return update(state, {
        columns: {
          [profile.column_index] : {$splice: [[sourceIdx, 1], [insertIdx, 0, profile_id]]}
        }
      });
    }
    return update(state, {
      columns: {
        [profile.column_index]: {$splice: [[sourceIdx, 1]]},
        [targetProfile.column_index]: {$splice: [[insertIdx, 0, profile_id]]}
      },
      profiles: {
        [profile_id]:{
          column_index: {$set: targetProfile.column_index}
        }
      }
    });
  },
  ['INSERT_PROFILE_IN_COLUMN'](state, action){
    const {profile_id, column_index} = action.payload;
    const profile = state.profiles[profile_id];
    const sourceIdx = state.columns[profile.column_index].indexOf(profile_id);
    const insertIdx = state.columns[column_index].length;

    return update(state, {
      columns: {
        [profile.column_index]: {$splice: [[sourceIdx, 1]]},
        [column_index]: {$splice: [[insertIdx, 0, profile.id]]}
      },
      profiles: {
        [profile_id]: {
          column_index: {$set: column_index}
        }
      }
    });
  }
});

export const dashboard_dnd = createReducer({
  dragging_app_id: -1,
  dragging_app_source_profile_id: -1,
  dragging_profile_id: -1
}, {
  ['BEGIN_APP_DRAG'](state, action){
    return update(state, {
      dragging_app_id: {$set: action.payload.app_id},
      dragging_app_source_profile_id: {$set: action.payload.profile_id}
    })
  },
  ['END_APP_DRAG'](state, action){
    return update(state, {
      dragging_app_id: {$set: -1},
      dragging_app_source_profile_id: {$set: -1}
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