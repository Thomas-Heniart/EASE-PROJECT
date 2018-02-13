import api from "../utils/api";
import post_api from "../utils/post_api";
import delete_api from "../utils/delete_api";
import {addNotification} from "./notificationBoxActions";
import extension from "../utils/extension_api";

export function fetchCatalog(){
  return (dispatch,getState) => {
    dispatch({type: 'FETCH_CATALOG_PENDING'});
    return Promise.all([
      api.catalog.getWebsites(),
      api.catalog.getCategories(),
      api.catalog.getSsoList(),
      api.catalog.getRequestsNumber(),
      api.catalog.getUpdates()
    ]).then(values => {
      const websites = values[0].websites;
      const categories = values[1].categories.sort((a, b) => {
        return a.name.localeCompare(b.name);
      });
      const sso_list = values[2].ssoList;
      const requestsNumber = values[3].request_number;
      const updates = values[4];
      dispatch({type: 'FETCH_CATALOG_FULFILLED', payload:{
        websites : websites,
        categories: categories,
        sso_list: sso_list,
        requests_number: requestsNumber,
        updates: updates
      }});
      return values;
    }).catch(err => {
      dispatch({type: 'FETCH_CATALOG_REJECTED', payload: err});
      throw err;
    })
  }
}

export function getImportedAccounts() {
  return (dispatch, getState) => {
    return api.catalog.getImportation()
      .then(response => {
        dispatch({type: 'FETCH_IMPORTED_ACCOUNTS', payload: response});
        return response;
      }).catch(err => {
        throw err;
    })
  }
}

export function testCredentials({account_information, website_id}) {
  return (dispatch, getState) => {
    extension.test_website_connection({
      website_id: website_id,
      account_information: account_information
    });
    api.getWebsiteConnection({account_information, website_id})
      .then(response => {
        dispatch({type: 'TEST_CREDENTIALS'});
      }).catch(err => {
        console.log(err);
    });
  }
}

export function catalogAddAnyApp({name, url, img_url, profile_id, account_information, connection_information, credentials_provided}){
  return (dispatch, getState) => {
    return post_api.catalog.addAnyApp({
      name: name,
      url: url,
      img_url: img_url,
      profile_id: profile_id,
      account_information: account_information,
      connection_information: connection_information,
      credentials_provided: credentials_provided,
      ws_id: getState().common.ws_id
    }).then(app => {
      dispatch({
        type: 'DASHBOARD_APP_CREATED',
        payload: {
          app: app
        }
      });
      const profile = getState().dashboard.profiles[profile_id];
      dispatch(addNotification({
        text: `${app.name} successfully sent to ${profile.name}!`
      }));
      return app;
    }).catch(err => {
      throw err;
    });
  }
}

export function catalogAddSoftwareApp({name, logo_url, profile_id, account_information, connection_information}){
  return (dispatch, getState) => {
    return post_api.catalog.addSoftwareApp({
      name: name,
      logo_url: logo_url,
      profile_id: profile_id,
      account_information: account_information,
      connection_information: connection_information,
      ws_id: getState().common.ws_id
    }).then(app => {
      dispatch({
        type: 'DASHBOARD_APP_CREATED',
        payload: {
          app: app
        }
      });
      const profile = getState().dashboard.profiles[profile_id];
      dispatch(addNotification({
        text: `${app.name} successfully sent to ${profile.name}!`
      }));
      return app;
    }).catch(err => {
      throw err;
    });
  }
}

export function catalogAddSsoApp({name, profile_id, sso_group_id, website_id}){
  return (dispatch, getState) => {
    post_api.catalog.addSsoApp({
      name: name,
      profile_id: profile_id,
      sso_group_id: sso_group_id,
      website_id: website_id,
      ws_id: getState().common.ws_id
    }).then(app => {
      dispatch({
        type: 'DASHBOARD_APP_CREATED',
        payload:{
          app:app
        }
      });
      const profile = getState().dashboard.profiles[profile_id];
      dispatch(addNotification({
        text: `${app.name} successfully sent to ${profile.name}!`
      }));
      return app;
    }).catch(err => {
      throw err;
    });
  }
}

export function catalogAddClassicApp({name, website_id, profile_id, account_information}) {
  return (dispatch, getState) => {
    return post_api.catalog.addClassicApp({
      name: name,
      website_id: website_id,
      profile_id: profile_id,
      account_information: account_information,
      ws_id: getState().common.ws_id
    }).then(app => {
      dispatch({
        type: 'DASHBOARD_APP_CREATED',
        payload:{
          app:app
        }
      });
      const profile = getState().dashboard.profiles[profile_id];
      dispatch(addNotification({
        text: `${app.name} successfully sent to ${profile.name}!`
      }));
      return app;
    }).catch(err => {
      throw err;
    })
  }
}

export function catalogAddMultipleClassicApp({profile_id, apps_to_add, account_information}) {
  return (dispatch, getState) => {
    return post_api.catalog.addMultipleClassicApp({
      profile_id: profile_id,
      apps_to_add: apps_to_add,
      account_information: account_information,
      ws_id: getState().common.ws_id
    }).then(apps => {
      dispatch({
        type: 'DASHBOARD_APP_CREATED',
        payload:{
          app:apps
        }
      });
      return apps;
    }).catch(err => {
      throw err;
    });
  }
}

export function catalogAddClassicAppSameAs({website_id, name, same_app_id, profile_id}) {
  return (dispatch, getState) => {
    return post_api.catalog.addClassicAppSameAs({
      website_id: website_id,
      name: name,
      same_app_id: same_app_id,
      profile_id: profile_id,
      ws_id: getState().common.ws_id
    }).then(apps => {
      dispatch({
        type: 'DASHBOARD_APP_CREATED',
        payload: {
          app: apps
        }
      });
      return apps;
    }).catch(err => {
      throw err;
    });
  }
}

export function catalogAddLogWithApp({name, website_id, profile_id, logWith_app_id}) {
  return (dispatch, getState) => {
    return post_api.catalog.addLogWithApp({
      name: name,
      website_id: website_id,
      profile_id: profile_id,
      logWith_app_id: logWith_app_id,
      ws_id: getState().common.ws_id
    }).then(app => {
      dispatch({
        type: 'DASHBOARD_APP_CREATED',
        payload:{
          app:app
        }
      });
      const profile = getState().dashboard.profiles[profile_id];
      dispatch(addNotification({
        text: `${app.name} successfully sent to ${profile.name}!`
      }));
      return app;
    }).catch(err => {
      throw err;
    })
  }
}

export function catalogAddBookmark({name, profile_id, url, img_url}){
  return (dispatch, getState) => {
    return post_api.catalog.addBookmark({
      name: name,
      profile_id: profile_id,
      url: url,
      img_url: img_url,
      ws_id: getState().common.ws_id
    }).then(app => {
      dispatch({
        type: 'DASHBOARD_APP_CREATED',
        payload:{
          app:app
        }
      });
      const profile = getState().dashboard.profiles[profile_id];
      dispatch(addNotification({
        text: `${app.name} successfully added as Bookmark in ${profile.name}!`
      }));
      return app;
    }).catch(err => {
      throw err;
    })
  }
}

export function catalogRequestWebsite({url, account_information}){
  return (dispatch, getState) => {
    return post_api.catalog.requestWebsite({
      url: url,
      account_information: account_information,
      ws_id: getState().common.ws_id
    }).then(response => {
      dispatch({type: 'CATALOG_REQUEST_WEBSITE_FULFILLED', payload: response});
      return response;
    }).catch(err => {
      throw err;
    });
  }
}

export function importAccount({name, url, account_information}){
  return (dispatch, getState) => {
    return post_api.catalog.importAccount({
      name: name,
      url: url,
      account_information: account_information,
      ws_id: getState().common.ws_id
    }).then(response => {
      dispatch({type: 'CATALOG_IMPORT_ACCOUNT', payload: response});
      return response;
    }).catch(err => {
      throw err;
    });
  }
}

export function modifyImportedAccount({id, name, url, website_id, account_information}){
  return (dispatch, getState) => {
    return post_api.catalog.modifyImportedAccount({
      id: id,
      name: name,
      url: url,
      website_id: website_id,
      account_information: account_information,
      ws_id: getState().common.ws_id
    }).then(response => {
      dispatch({type: 'CATALOG_MODIFY_IMPORTED_ACCOUNT', payload: response});
      return response;
    }).catch(err => {
      throw err;
    });
  }
}

export function deleteImportedAccount({id}){
  return (dispatch, getState) => {
    return post_api.catalog.deleteImportedAccount({
      id: id,
      ws_id: getState().common.ws_id
    }).then(response => {
      dispatch({type: 'CATALOG_DELETE_IMPORTED_ACCOUNT', payload: response});
      return response;
    }).catch(err => {
      throw err;
    })
  }
}

export function catalogAddBookmarkModal({name, url, img_url, logoLetter}){
  return (dispatch, getState) => {
    return new Promise((resolve, reject) => {
      dispatch({
        type: 'SHOW_CATALOG_ADD_BOOKMARK_MODAL',
        payload: {
          active: true,
          name: name,
          url: url,
          img_url: img_url,
          resolve: resolve,
          reject: reject
        }
      })
    })
  }
}

export function showCatalogAddBookmarkModal({active, name, url, img_url, resolve, reject}){
  return {
    type: 'SHOW_CATALOG_ADD_BOOKMARK_MODAL',
    payload: {
      active: active,
      name: name,
      url: url,
      img_url: img_url,
      resolve: resolve,
      reject: reject
    }
  }
}

export function showCatalogAddAppModal({active, website}){
  return {
    type: 'SHOW_CATALOG_ADD_APP_MODAL',
    payload: {
      active: active,
      website: website
    }
  }
}

export function showCatalogAddSSOAppModal({active, website}){
  return {
    type: 'SHOW_CATALOG_ADD_SSO_APP_MODAL',
    payload: {
      active: active,
      website: website
    }
  }
}

export function catalogAddAnyAppModal({name, url, img_url, logoLetter}){
  return (dispatch, getState) => {
    return new Promise((resolve, reject) => {
      dispatch({
        type: 'SHOW_CATALOG_ADD_ANY_APP_MODAL',
        payload: {
          active: true,
          name: name,
          url: url,
          img_url: img_url,
          logoLetter: logoLetter,
          resolve: resolve,
          reject: reject
        }
      })
    })
  }
}

export function showCatalogAddAnyAppModal({active, name, url, img_url, logoLetter, resolve, reject}){
  return {
    type: 'SHOW_CATALOG_ADD_ANY_APP_MODAL',
    payload: {
      active: active,
      name: name,
      url: url,
      img_url: img_url,
      logoLetter: logoLetter,
      resolve: resolve,
      reject: reject
    }
  }
}

export function catalogAddSoftwareAppModal({name, img_url, logoLetter}){
  return (dispatch, getState) => {
    return new Promise((resolve, reject) => {
      dispatch({
        type: 'SHOW_CATALOG_ADD_SOFTWARE_APP_MODAL',
        payload: {
          active: true,
          name: name,
          img_url: img_url,
          logoLetter: logoLetter,
          resolve: resolve,
          reject: reject
        }
      })
    })
  }
}

export function showCatalogAddSoftwareAppModal({active, name, img_url, logoLetter, resolve, reject}){
  return {
    type: 'SHOW_CATALOG_ADD_SOFTWARE_APP_MODAL',
    payload: {
      active: active,
      name: name,
      img_url: img_url,
      logoLetter: logoLetter,
      resolve: resolve,
      reject: reject
    }
  }
}

export function accountUpdateModal(dispatch, website, account_information, team, room){
  return new Promise((resolve, reject) => {
    dispatch(showAccountUpdateModal({
      state: true,
      website: website,
      account_information: account_information,
      team: team,
      room: room,
      resolve: resolve,
      reject: reject
    }));
  }).then(response => {
    dispatch(showAccountUpdateModal({state: false}));
    return response;
  }).catch(err => {
    dispatch(showAccountUpdateModal({state: false}));
  });
}

export function showAccountUpdateModal({state, resolve, reject, website, account_information, team, room}){
  return {
    type: 'SHOW_ACCOUNT_UPDATE_MODAL',
    payload: {
      active: state,
      website: website,
      account_information: account_information,
      team: team,
      room: room,
      resolve: resolve,
      reject: reject
    }
  }
}

export function passwordUpdateModal(dispatch, website, account_information, team, room, team_user_id){
  return new Promise((resolve, reject) => {
    dispatch(showPasswordUpdateModal({
      state: true,
      website: website,
      account_information: account_information,
      team: team,
      room: room,
      team_user_id: team_user_id,
      resolve: resolve,
      reject: reject
    }));
  }).then(response => {
    dispatch(showPasswordUpdateModal({state: false}));
    return response;
  }).catch(err => {
    dispatch(showPasswordUpdateModal({state: false}));
  });
}

export function showPasswordUpdateModal({state, resolve, reject, website, account_information, team, room, team_user_id}){
  return {
    type: 'SHOW_PASSWORD_UPDATE_MODAL',
    payload: {
      active: state,
      website: website,
      account_information: account_information,
      team: team,
      room: room,
      team_user_id: team_user_id,
      resolve: resolve,
      reject: reject
    }
  }
}

export function newAccountUpdateModal(dispatch, website, account_information){
    return new Promise((resolve, reject) => {
        dispatch(showNewAccountUpdateModal({
            state: true,
            website: website,
            account_information: account_information,
            resolve: resolve,
            reject: reject
        }));
    }).then(response => {
        console.log('resolve');
        dispatch(showNewAccountUpdateModal({state: false}));
        console.log('la réponse :',response);
        return response;
    }).catch(err => {
        dispatch(showNewAccountUpdateModal({state: false}));
    });
}

export function showNewAccountUpdateModal({state, resolve, reject, website, account_information}){
    return {
        type: 'SHOW_NEW_ACCOUNT_UPDATE_MODAL',
        payload: {
            active: state,
            website: website,
            account_information: account_information,
            resolve: resolve,
            reject: reject
        }

    }
}

export function deleteUpdate({id}){
  return (dispatch, getState) => {
    return delete_api.catalog.deleteUpdate({
      id: id,
      ws_id: getState().common.ws_id
    }).then(response => {
      dispatch({type: 'DELETE_UPDATE', payload: {update_id: id}});
      return response;
    }).catch(err => {
      throw err;
    })
  }
}