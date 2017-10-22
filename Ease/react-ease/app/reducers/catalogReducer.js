import createReducer from  "./createReducer";

export const catalog = createReducer({
  websites: [],
  categories: [],
  sso_list: [],
  fetching: false,
  loaded: false
}, {
  ['FETCH_CATALOG_PENDING'](state, action){
    return {
      ...state,
      fetching: true
    }
  },
  ['FETCH_CATALOG_FULFILLED'](state, action){
    return {
      ...state,
      websites: action.payload.websites.sort(function (a, b) {return b.integration_date - a.integration_date;}),
      categories: action.payload.categories,
      sso_list: action.payload.sso_list,
      fetching: false,
      loaded: true
    }
  },
  ['FETCH_CATALOG_REJECTED'](state, action){
    return {
      ...state,
      fetching: false
    }
  }
});