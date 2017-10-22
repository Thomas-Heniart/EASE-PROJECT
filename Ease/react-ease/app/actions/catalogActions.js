import api from "../utils/api";
import post_api from "../utils/post_api";

export function fetchCatalog(){
  return (dispatch,getState) => {
    dispatch({type: 'FETCH_CATALOG_PENDING'});
    return Promise.all([
        api.catalog.getWebsites(),
        api.catalog.getCategories(),
        api.catalog.getSsoList()
    ]).then(values => {
      const websites = values[0].websites;
      const categories = values[1].categories;
      const sso_list = values[2].sso_list;
      dispatch({type: 'FETCH_CATALOG_FULFILLED', payload:{
        websites : websites,
        categories: categories,
        sso_list: sso_list
      }});
      return values;
    }).catch(err => {
      dispatch({type: 'FETCH_CATALOG_REJECTED', payload: err});
      throw err;
    })
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

export function catalogAddBookmark({name, profile_id, url, img_url}) {
  return (dispatch, getState) => {
    return post_api.catalog.addBookmark({
      name: name,
      profile_id: profile_id,
      url: url,
      img_url: img_url
    }).then(response => {
      dispatch({type: 'CATALOG_ADD_BOOKMARK_FULFILLED', payload: {app: response}});
      return response;
    }).catch(err => {
      throw err;
    })
  }
}

export function catalogRequestWebsite({url, acount_information}){
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