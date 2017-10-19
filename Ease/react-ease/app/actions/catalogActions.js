import api from "../utils/api";

export function fetchCatalog(){
  return (dispatch,getState) => {
    dispatch({type: 'FETCH_CATALOG_PENDING'});
    Promise.all([
        api.catalog.getWebsites(),
        api.catalog.getCategories(),
        api.catalog.getSsoList()
    ]).then(values => {
      const websites = values[0];
      const categories = values[1];
      const sso_list = values[2];
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