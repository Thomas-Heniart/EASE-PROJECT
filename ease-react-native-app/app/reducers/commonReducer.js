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
  personal_space: [
    {name: 'Me', id:1},
    {name: 'Google tools', id:2},
    {name: 'PP Admin', id:3},
    {name: 'PP Customer', id:4}
  ],
  teams: [
    {
      name: 'PIED PIPER',
      id:1,
      rooms: [
        {
          name: 'openspace',
          id:2
        },
        {
          name: 'administrators',
          id:3
        },
        {
          name: 'tech',
          id:4
        },
        {
          name: 'communication',
          id:5
        }
      ]
    },
    {
      name: 'PIED PIPER',
      id:3,
      rooms: [
        {
          name: 'openspace',
          id:2
        },
        {
          name: 'administrators',
          id:3
        },
        {
          name: 'tech',
          id:4
        },
        {
          name: 'communication',
          id:5
        }
      ]
    },
    {
      name: 'PIED PIPER',
      id:2,
      rooms: [
        {
          name: 'openspace',
          id:2
        },
        {
          name: 'administrators',
          id:3
        },
        {
          name: 'tech',
          id:4
        },
        {
          name: 'communication',
          id:5
        }
      ]
    }
  ]
}, {

});