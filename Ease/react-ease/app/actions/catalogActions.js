import api from "../utils/api";
import post_api from "../utils/post_api";

export function fetchCatalog(){
  return (dispatch,getState) => {
    dispatch({type: 'FETCH_CATALOG_PENDING'});
    return Promise.all([
      api.catalog.getWebsites(),
      api.catalog.getCategories(),
      api.catalog.getSsoList(),
      api.catalog.getRequestsNumber()
    ]).then(values => {
      const websites = values[0].websites;
      const categories = values[1].categories.sort((a, b) => {
        return a.name.localeCompare(b.name);
      });
      const sso_list = values[2].ssoList;
      const requestsNumber = values[3].request_number;
      dispatch({type: 'FETCH_CATALOG_FULFILLED', payload:{
        websites : websites,
        categories: categories,
        sso_list: sso_list,
        requests_number: requestsNumber
      }});
      return values;
    }).catch(err => {
      dispatch({type: 'FETCH_CATALOG_REJECTED', payload: err});
      throw err;
    })
  }
}

export function catalogAddSsoApp({name, profile_id, sso_group_id, website_id}){
  return (dispatch, getState) => {
    post_api.catalog.addSsoApp({
      name: name,
      profile_id: profile_id,
      sso_group_id: sso_group_id,
      website_id: website_id
    }).then(response => {
      dispatch({
        type: 'DASHBOARD_APP_ADDED',
        app: response
      });
      return response;
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
      account_information: account_information
    }).then(response => {
      dispatch({type: 'CATALOG_ADD_CLASSIC_APP_FULFILLED', payload: {app: response}});
      return response;
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
      account_information: account_information
    }).then(apps => {
      dispatch({type: 'CATALOG_ADD_MULTIPLE_CLASSIC_APP', payload: {apps: apps}});
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
            profile_id: profile_id
        }).then(apps => {
            dispatch({type: 'CATALOG_ADD_CLASSIC_APP_SAME_AS', payload: {apps: apps}});
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
      logWith_app_id: logWith_app_id
    }).then(response => {
      dispatch({type: 'CATALOG_ADD_LOGWITH_APP_FULFILLED', payload: {app: response}});
      return response;
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
      img_url: img_url
    }).then(app => {
      dispatch({type: 'CATALOG_ADD_BOOKMARK_FULFILLED', payload: {app: app}});
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
      account_information: account_information
    }).then(response => {
      dispatch({type: 'CATALOG_REQUEST_WEBSITE_FULFILLED', payload: response});
      return response;
    }).catch(err => {
      throw err;
    });
  }
}

export function catalogAddBookmarkModal({name, url, img_url}){
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