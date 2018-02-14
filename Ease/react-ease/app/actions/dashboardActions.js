import api from "../utils/api";
import post_api from "../utils/post_api";
import extension from "../utils/extension_api";
import {fetchTeamApp} from "./teamActions";
import {showExtensionDownloadModal} from "./modalActions";

export function fetchDashboard(){
  return (dispatch, getState) => {
    const calls = [
      api.dashboard.fetchProfileList(),
      api.dashboard.fetchApps(),
      api.dashboard.fetchSsoGroups()
    ];
    dispatch({type: 'FETCH_DASHBOARD_PENDING'});
    return Promise.all(calls).then(response => {
      const columns = response[0];
      const apps = response[1];
      const sso_groups = response[2];
      const team_app_calls = [];

      easeTracker.setUserProperty("AppCount", Object.keys(apps).length);

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
          apps: apps,
          sso_groups: sso_groups
        }});
        return response;
      });
    }).catch(err => {
      dispatch({type: 'FETCH_DASHBOARD_REJECTED', payload: err});
      throw err;
    })
  }
}

export function dashboardAppCreatedAction({app}) {
  return {
    type: 'DASHBOARD_APP_CREATED',
    payload: {
      app: app
    }
  }
}

export function dashboardAppChangedAction({app}) {
  return {
    type: 'DASHBOARD_APP_CHANGED',
    payload: {
      app: app
    }
  }
}

export function dashboardAppRemovedAction({app_id}){
  return (dispatch, getState) => {
    const store = getState();
    const app = store.dashboard.apps[app_id];
    dispatch({
      type: 'DASHBOARD_APP_REMOVED',
      payload: {
        app_id: app.id,
        ws_id: getState().common.ws_id
      }
    });
    dispatch(checkIfProfileEmpty({
      profile_id: app.profile_id
    }));
    if (app.type === 'ssoApp'){
      const sso_group = store.dashboard.sso_groups[app.sso_group_id];
      if (sso_group.sso_app_ids.length === 1)
        dispatch(ssoGroupRemovedAction({
          sso_group_id: sso_group.id
        }));
    }
  };
}

export function dashboardProfileCreatedAction({profile}){
  return {
    type: 'DASHBOARD_PROFILE_CREATED',
    payload: {
      profile: profile
    }
  }
}

export function dashboardProfileChangedAction({profile}){
  return {
    type: 'DASHBOARD_PROFILE_CHANGED',
    payload: {
      profile: profile
    }
  }
}

export function dashboardProfileRemovedAction({profile_id}){
  return {
    type: 'DASHBOARD_PROFILE_REMOVED',
    payload: {
      profile_id: profile_id
    }
  }
}

export function moveAppAction({app_id, profile_id, index}){
  return (dispatch, getState) => {
    const store = getState();
    const app = store.dashboard.apps[app_id];
    const source_profile = store.dashboard.profiles[app.profile_id];
    dispatch({
      type: 'MOVE_APP',
      payload: {
        app_id: app_id,
        profile_id: profile_id,
        index: index
      }
    });
    dispatch(checkIfProfileEmpty({
      profile_id: source_profile.id
    }));
  }
}

export function moveProfileAction({profile_id, column_index, index}){
  return {
    type: 'MOVE_PROFILE',
    payload: {
      profile_id: profile_id,
      column_index: column_index,
      index: index
    }
  }
}

export function ssoGroupCreatedAction({sso_group}) {
  return {
    type: 'SSO_GROUP_CREATED',
    payload: {
      sso_group: sso_group
    }
  }
}

export function ssoGroupRemovedAction({sso_group_id}) {
  return {
    type: 'SSO_GROUP_REMOVED',
    payload: {
      sso_group_id: sso_group_id
    }
  }
}

export function ssoGroupChangedAction({sso_group}) {
  return {
    type: 'SSO_GROUP_CHANGED',
    payload: {
      sso_group: sso_group
    }
  }
}

export function fetchProfile({profile_id}) {
  return (dispatch, getState) => {
    return api.dashboard.fetchProfile({
      profile_id: profile_id
    }).then(profile => {
      dispatch(dashboardProfileCreatedAction({
        profile: profile
      }));
      return profile;
    }).catch(err => {
      throw err;
    });
  }
}

export function fetchApp({app_id}){
  return (dispatch, getState) => {
    return api.dashboard.fetchApp({
      app_id:app_id
    }).then(app => {
      const store = getState();
      const profile = store.dashboard.profiles[app.profile_id];

      if (!profile) {
        dispatch({
          type: 'FETCH_DASHBOARD_APP',
          payload: {app: app}
        });
        return dispatch(fetchProfile({
          profile_id: app.profile_id
        })).then(response => {
          return app;
        });
      }
      dispatch(dashboardAppCreatedAction({app: app}));
      return app;
    }).catch(err => {
      throw err;
    });
  }
}

export function validateApp({app_id}) {
  return (dispatch, getState) => {
    post_api.dashboard.validateApp({
      app_id: app_id,
      ws_id: getState().common.ws_id
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
    return post_api.dashboard.deleteApp({
      app_id: app_id,
      ws_id: getState().common.ws_id
    }).then(response => {
      dispatch(deleteAppAction({
        app_id: app_id
      }));
      return response;
    }).catch(err => {
      throw err;
    });
  }
}

export function deleteSsoApp({app_id}){
  return (dispatch, getState) => {
    const store = getState();
    const app = store.dashboard.apps[app_id];
    const sso_group = store.dashboard.sso_groups[app.sso_group_id];
    return post_api.dashboard.deleteApp({
      app_id: app_id,
      ws_id: getState().common.ws_id
    }).then(response => {
      dispatch(deleteAppAction({
        app_id: app_id
      }));
      if (sso_group.sso_app_ids.length === 1)
        dispatch(deleteSsoGroupAction({
          sso_group_id: sso_group.id
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

    dispatch({
      type: 'END_APP_DRAG',
      payload: {
        app_id: app_id
      }
    });
    dispatch(checkIfProfileEmpty({profile_id: source_profile_id}));
    post_api.dashboard.moveApp({
      app_id: app_id,
      profile_id: app.profile_id,
      position: index,
      ws_id: getState().common.ws_id
    });
    easeTracker.trackEvent("MoveApp");
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
      position: index,
      ws_id: getState().common.ws_id
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
      column_index: column_index,
      ws_id: getState().common.ws_id
    }).then(profile => {
      easeTracker.trackEvent("NewGroup");
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
      name: name,
      ws_id: getState().common.ws_id
    }).then(response => {
      dispatch({type: 'DASHBOARD_PROFILE_CHANGED', payload: {profile: response}});
      return response;
    }).catch(err => {
      throw err;
    });
  }
}

export function deleteAppAction({app_id}){
  return (dispatch, getState) => {
    const store = getState();
    const app = store.dashboard.apps[app_id];
    dispatch({
      type: 'DASHBOARD_APP_REMOVED',
      payload: {
        app_id: app.id
      }
    });
    dispatch(checkIfProfileEmpty({
      profile_id: app.profile_id
    }));
  }
}

export function checkIfProfileEmpty({profile_id}){
  return (dispatch, getState) => {
    const store = getState();
    if (!store.dashboard.profiles[profile_id].app_ids.length){
      /*      post_api.dashboard.deleteProfile({
              profile_id: profile_id
            });*/
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

export function editAppCredentials({account_information, app}) {
  if (app.type === 'classicApp')
    return editClassicApp({
      app_id: app.id,
      name: app.name,
      account_information: account_information
    });
  else if (app.type === 'softwareApp')
    return editSoftwareApp({
      app_id: app.id,
      name: app.name,
      account_information: account_information
    });
  else
    return editAnyApp({
      app_id: app.id,
      name: app.name,
      url: app.website.login_url,
      img_url: app.website.logo,
      account_information: account_information,
      connection_information: app.website.information
    })
}

export function editClassicApp({app_id, name, account_information}){
  return (dispatch, getState) => {
    return post_api.dashboard.editClassicApp({
      app_id: app_id,
      name: name,
      account_information: account_information,
      ws_id: getState().common.ws_id
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
      logWithApp_id:logWithApp_id,
      ws_id: getState().common.ws_id
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
      img_url: img_url,
      ws_id: getState().common.ws_id
    }).then(response=> {
      dispatch({type: 'DASHBOARD_APP_CHANGED', payload: {app: response}});
      return response;
    }).catch(err =>  {
      throw err;
    })
  }
}

export function editAnyApp({app_id, name, url, img_url, account_information, connection_information}) {
  return (dispatch, getState) => {
    return post_api.dashboard.editAnyApp({
      app_id: app_id,
      name: name,
      url: url,
      img_url: img_url,
      account_information: account_information,
      connection_information: connection_information,
      ws_id: getState().common.ws_id
    }).then(response=> {
      dispatch({type: 'DASHBOARD_APP_CHANGED', payload: {app: response}});
      return response;
    }).catch(err =>  {
      throw err;
    })
  }
}

export function editSoftwareApp({app_id, name, account_information}) {
  return (dispatch, getState) => {
    return post_api.dashboard.editSoftwareApp({
      app_id: app_id,
      name: name,
      account_information: account_information,
      ws_id: getState().common.ws_id
    }).then(response=> {
      dispatch({type: 'DASHBOARD_APP_CHANGED', payload: {app: response}});
      return response;
    }).catch(err =>  {
      throw err;
    })
  }
}

export function editSsoGroup({sso_group_id, account_information}) {
  return (dispatch, getState) => {
    return post_api.dashboard.editSsoGroup({
      sso_group_id: sso_group_id,
      account_information: account_information,
      ws_id: getState().common.ws_id
    }).then(response => {
      dispatch({
        type: 'SSO_GROUP_CHANGED',
        payload: {
          sso_group: response
        }
      });
      return response;
    }).catch(err => {
      throw err;
    });
  }
}


export function deleteSsoGroupAction({sso_group_id}){
  return {
    type: 'SSO_GROUP_REMOVED',
    payload: {
      sso_group_id: sso_group_id
    }
  }
};

export function createSsoGroup({sso_id, account_information}){
  return (dispatch, getState) => {
    return post_api.dashboard.createSsoGroup({
      sso_id:sso_id,
      account_information: account_information,
      ws_id: getState().common.ws_id
    }).then(response => {
      dispatch({
        type: 'SSO_GROUP_CREATED',
        payload: {
          sso_group: response
        }
      });
      return response;
    }).catch(err => {
      throw err;
    });
  }
}

export function editAppName({app_id, name}) {
  return (dispatch, getState) => {
    return post_api.dashboard.editAppName({
      app_id: app_id,
      name: name,
      ws_id: getState().common.ws_id
    }).then(app => {
      dispatch({
        type: 'DASHBOARD_APP_CHANGED',
        payload: {
          app: app
        }
      });
    }).catch(err => {
      throw err;
    });
  }
}

export function clickOnAppMetric({app_id}) {
  return (dispatch, getState) => {
    return post_api.dashboard.clickOnAppMetric({
      app_id: app_id
    }).catch(err => {
      throw err;
    });
  }
}

export function validateTutorial() {
  return (dispatch, getState) => {
    return post_api.dashboard.validateTutorial().then(response => {
      easeTracker.trackEvent("EaseOnboardingPersonalTuto");
      dispatch({
        type: 'DASHBOARD_TUTORIAL_DONE'
      });
    }).catch(err => {
      throw err;
    });
  }
}

export function AppConnection({app_id, keep_focus}){
  return (dispatch, getState) => {
    if (!document.querySelector('#new_ease_extension')){
      dispatch(showExtensionDownloadModal({active: true}));
      return new Promise((resolve, reject) => {reject('Need extension.')});
    }
    const app = getState().dashboard.apps[app_id];
    extension.app_connection({
      app_id:app_id,
      active_tab: !keep_focus,
      website: app.website
    });
    return api.dashboard.getAppConnectionInformation({
      app_id: app_id
    }).then(response => {
      let json = {};
      json.detail = response;
      json.detail = json.detail.map(item => {
        if (!!item.user)
          Object.keys(item.user).map(id => {
            item.user[id] = decipher(item.user[id]);
          });
        return item;
      });
      json.detail.highlight = !keep_focus;
      if (app.new)
        dispatch(validateApp({app_id: app_id}));
      document.dispatchEvent(new CustomEvent('NewConnection', json));
      easeTracker.trackEvent("ClickOnApp", {
        type: app.type,
        appName: app.name,
        websiteName: app.website.name
      });
      return json;
    }).catch(err => {
      throw err;
    });
  }
}