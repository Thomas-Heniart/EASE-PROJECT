import createReducer from  "./createReducer";

export const username = createReducer('fisun_s', {
  ['CHANGE_USERNAME'](state, action){
    return action.payload;
  }
});

export const auth = createReducer({
  email: '',
  expiration: 0,
  username: '',
  token: ''
}, {
  ['CONNECTION'](state, action){
    const user_info = action.payload.user_info;

    return {
      email: user_info.email,
      expiration: user_info.exp,
      username: user_info.name,
      token: user_info.tok
    }
  },
  ['LOGOUT'](state, action){
    return {
      email: '',
      expiration: 0,
      username: '',
      token: ''
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
  personal_space: [],
  teams: []
}, {
  ['FETCH_SPACES_FULFILLED'](state, action){
    return {
        ...state,
      personal_space: action.payload.spaces.personal_space,
      teams: action.payload.spaces.teams
    }
  }
});