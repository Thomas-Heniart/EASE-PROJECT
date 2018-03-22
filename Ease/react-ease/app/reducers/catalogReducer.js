import createReducer from  "./createReducer";

export const catalog = createReducer({
  websites: [],
  updates: [],
  categories: [],
  sso_list: [],
  requests_number: 0,
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
      websites: action.payload.websites.sort(function (a, b) {return b.ratio - a.ratio;}),
      categories: action.payload.categories,
      sso_list: action.payload.sso_list,
      requests_number: action.payload.requests_number,
      fetching: false,
      loaded: true
    }
  },
  ['FETCH_CATALOG_REJECTED'](state, action){
    return {
      ...state,
      fetching: false
    }
  },
  ['FETCH_UPDATES'](state, action){
    return {
      ...state,
      updates: action.payload,
    }
  },
  ['CATALOG_REQUEST_WEBSITE_FULFILLED'](state, action){
    return {
        ...state,
      requests_number: state.requests_number + 1
    }
  },
  ['DELETE_UPDATE'](state, action){
    return {
      ...state,
      updates: state.updates.filter(item => {return action.payload.update_id !== item.id})
    }
  }
});