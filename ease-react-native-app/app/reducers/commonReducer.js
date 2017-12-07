import createReducer from  "./createReducer";

export const auth = createReducer({
  email: '',
  username: '',
  network: true
}, {
  ['NET_CONNECTION_CHANGED'](state, action){
    return {
      ...state,
      network: action.payload.network
    }
  },
  ['SET_USER_INFORMATION'](state, action){
    const {email, username} = action.payload;

    return {
      ...state,
      email: email,
      username: username
    }
  },
  ['CONNECTION'](state, action){
    const {email, username} = action.payload;

    return {
      ...state,
      email: email,
      username: username
    }
  },
  ['LOGOUT'](state, action){
    return {
      ...state,
      email: '',
      username: ''
    }
  }
});

export const selectedItem = createReducer({
  itemId: -1,
  subItemId: -1,
  name: ''
}, {
  ['SELECT_ITEM'](state, action){
    return {
      ...state,
      itemId: action.payload.itemId,
      subItemId: action.payload.subItemId,
      name: action.payload.name
    }
  }
});

export const spaces = createReducer({
  profiles: {},
  apps: {}
}, {
  ['FETCH_PERSONAL_SPACE'](state, action){
    const {profiles, apps} = action.payload;
    return {
      ...state,
      profiles: profiles,
      apps: apps
    }
  },
  ['FETCH_SPACES_FULFILLED'](state, action){
    return {
      ...state,
      personal_space: action.payload.spaces.personal_space,
      teams: action.payload.spaces.teams
    }
  }
});