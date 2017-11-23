import api from "../utils/api";
import post_api from "../utils/post_api";
import {fetchTeamApp} from "./teamActions";

export function fetchDashboard(){
  return (dispatch, getState) => {
    const calls = [
      api.dashboard.fetchProfileList(),
      api.dashboard.fetchApps()
    ];
    dispatch({type: 'FETCH_DASHBOARD_PENDING'});
    return Promise.all(calls).then(response => {
      const columns = response[0];
      const apps = response[1];
      const team_app_calls = [];
      apps.map(app => {
        if (!!app.team_id)
          team_app_calls.push(dispatch(fetchTeamApp({
            team_id: app.team_id,
            app_id: app.team_card_id
          })));
      });
      return Promise.all(team_app_calls).then(response => {
        dispatch({type: 'FETCH_DASHBOARD_FULFILLED', payload: {
          columns: columns,
          apps: apps
        }});
        return response;
      });
    }).catch(err => {
      dispatch({type: 'FETCH_DASHBOARD_REJECTED', payload: err});
      throw err;
    })
  }
}

export function validateApp({app_id}) {
  return (dispatch, getState) => {
    post_api.dashboard.validateApp({
      app_id: app_id
    }).then(app => {
      dispatch({
        type: 'DASHBOARD_APP_CHANGED',
        payload: {
          app: app
        }
      });
      return app;
    }).catch(err => {
      throw err;
    });
  }
}

export function deleteApp({app_id}){
  return (dispatch, getState) => {
    const store = getState();
    const app = store.dashboard.apps[app_id];
    const profile = store.dashboard.profiles[app.profile_id];
    const isEmpty = profile.app_ids.length === 1;
    return post_api.dashboard.deleteApp({
      app_id: app_id
    }).then(response => {
      dispatch({
        type: 'DASHBOARD_APP_REMOVED',
        payload: {
          app_id: app_id
        }
      });
      if (isEmpty)
        dispatch(removeProfile({
          profile_id: profile.id
        }));
      return response;
    }).catch(err => {
      throw err;
    });
  }
}

export function moveApp({app_id, targetApp_id}) {
  return {
    type: 'INSERT_APP',
    payload: {
      app_id: app_id,
      targetApp_id: targetApp_id
    }
  }
}

export function insertAppInProfile({app_id, profile_id}){
  return (dispatch, getState) => {
    const store = getState();
    const app = store.dashboard.apps[app_id];
    if (app.profile_id === profile_id)
      return;
    dispatch({
      type: 'INSERT_APP_IN_PROFILE',
      payload: {
        app_id:app_id,
        profile_id: profile_id
      }
    });
  }
}

export function beginAppDrag({app_id}){
  return (dispatch, getState) => {
    const store = getState();
    const app = store.dashboard.apps[app_id];
    dispatch({
      type: 'BEGIN_APP_DRAG',
      payload: {
        app_id: app_id,
        profile_id: app.profile_id
      }
    });
  };
}

export function endAppDrag(){
  return (dispatch, getState) => {
    const store = getState();
    const app_id = store.dashboard_dnd.dragging_app_id;
    const app = store.dashboard.apps[app_id];
    const index = store.dashboard.profiles[app.profile_id].app_ids.indexOf(app_id);
    const source_profile_id = store.dashboard_dnd.dragging_app_source_profile_id;
    const last_profile = store.dashboard.profiles[source_profile_id];
    const is_last_profile_empty = !last_profile.app_ids.length;

    dispatch({
      type: 'END_APP_DRAG',
      payload: {
        app_id: app_id
      }
    });
    if (is_last_profile_empty)
      dispatch({
        type: 'DASHBOARD_PROFILE_REMOVED',
        payload: {
          profile_id: source_profile_id
        }
      });
    post_api.dashboard.moveApp({
      app_id: app_id,
      profile_id: app.profile_id,
      position: index
    }).then(response => {
      if (is_last_profile_empty)
        post_api.dashboard.deleteProfile({
          profile_id: source_profile_id
        });
    });
  };
}

export function beginProfileDrag({profile_id}){
  return {
    type: 'BEGIN_PROFILE_DRAG',
    payload: {
      profile_id: profile_id
    }
  }
}

export function endProfileDrag({profile_id}){
  return (dispatch, getState) => {
    const store = getState();
    const profile = store.dashboard.profiles[profile_id];
    const index = store.dashboard.columns[profile.column_index].indexOf(profile_id);
    post_api.dashboard.moveProfile({
      profile_id: profile_id,
      column_index: profile.column_index,
      position: index
    });
    dispatch({
      type: 'END_PROFILE_DRAG',
      payload: {
        profile_id: profile_id
      }
    });
  };
}

export function moveProfile({profile_id, targetProfile_id, hoverClientY, hoverMiddleY}){
  return (dispatch, getState) => {
    const store = getState();
    const profile = store.dashboard.profiles[profile_id];
    const hoverProfile = store.dashboard.profiles[targetProfile_id];
    const dragIndex = store.dashboard.columns[profile.column_index].indexOf(profile_id);
    const hoverIndex = store.dashboard.columns[hoverProfile.column_index].indexOf(targetProfile_id);
    // Dragging downwards
    if (dragIndex < hoverIndex && hoverClientY < hoverMiddleY)
      return;
    // Dragging upwards
    if (dragIndex > hoverIndex && hoverClientY > hoverMiddleY)
      return;
    dispatch({
      type: 'INSERT_PROFILE',
      payload: {
        profile_id: profile_id,
        targetProfile_id: targetProfile_id
      }
    });
  };
}

export function insertProfileIntoColumn({profile_id, column_index}){
  return (dispatch, getState) => {
    const store = getState();
    if (store.dashboard.profiles[profile_id].column_index === column_index)
      return;
    dispatch({
      type: 'INSERT_PROFILE_IN_COLUMN',
      payload: {
        profile_id: profile_id,
        column_index: column_index
      }
    })
  }
}

export function createProfileAndInsertApp({column_index, name, app_id}){
  return (dispatch, getState) => {
    dispatch(createProfile({
      column_index: column_index,
      name: name
    })).then(profile => {
      dispatch(insertAppInProfile({
        app_id: app_id,
        profile_id: profile.id
      }));
      dispatch(endAppDrag());
    });
  };
}

export function createProfile({column_index, name}) {
  return (dispatch, getState) => {
    return post_api.dashboard.createProfile({
      name: name,
      column_index: column_index
    }).then(profile => {
      dispatch({
        type: 'DASHBOARD_PROFILE_CREATED',
        payload: {
          profile: profile
        }
      });
      return profile;
    });
  };
}

export function editProfile({profile_id, name}) {
  return (dispatch, getState) => {
    return post_api.dashboard.editProfile({
      profile_id: profile_id,
      name: name
    }).then(response => {
      dispatch({type: 'DASHBOARD_PROFILE_CHANGED', payload: {profile: response}});
      return response;
    }).catch(err => {
      throw err;
    });
  }
}

export function checkIfProfileEmpty({profile_id}){
  return (dispatch, getState) => {
    const store = getState();
    if (!store.dashboard.profiles[profile_id].app_ids.length){
      post_api.dashboard.deleteProfile({
        profile_id: profile_id
      });
      dispatch({
        type: 'DASHBOARD_PROFILE_REMOVED',
        payload: {
          profile_id:profile_id
        }
      })
    }
  }
}

export function removeProfile({profile_id}){
  return (dispatch, getState) => {
    dispatch({
      type: 'DASHBOARD_PROFILE_REMOVED',
      payload: {
        profile_id: profile_id
      }
    });
    return post_api.dashboard.deleteProfile({
      profile_id: profile_id
    });
  }
}

export function editClassicApp({app_id, name, account_information}){
  return (dispatch, getState) => {
    return post_api.dashboard.editClassicApp({
      app_id: app_id,
      name: name,
      account_information: account_information
    }).then(response => {
      dispatch({type: 'DASHBOARD_APP_CHANGED', payload: {app: response}});
      return response;
    }).catch(err =>  {
      throw err;
    })
  }
}

export function editLogWithApp({app_id, name, logWithApp_id}) {
  return (dispatch, getState) => {
    return post_api.dashboard.editLogWithApp({
      app_id: app_id,
      name: name,
      logWithApp_id:logWithApp_id
    }).then(response => {
      dispatch({type: 'DASHBOARD_APP_CHANGED', payload: {app: response}});
      return response;
    }).catch(err =>  {
      throw err;
    })
  }
}

export function editLinkApp({app_id, name, url, img_url}) {
  return (dispatch, getState) => {
    return post_api.dashboard.editLinkApp({
      app_id: app_id,
      name: name,
      url: url,
      img_url: img_url
    }).then(response=> {
      dispatch({type: 'DASHBOARD_APP_CHANGED', payload: {app: response}});
      return response;
    }).catch(err =>  {
      throw err;
    })
  }
}
