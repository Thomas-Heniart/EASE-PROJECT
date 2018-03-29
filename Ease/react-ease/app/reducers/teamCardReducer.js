import createReducer from './createReducer';

export const teamCard = createReducer({
  team_id: -1,
  channel_id: -1,
  app: {},
  name: '',
  url: '',
  type: '',
  subtype: '',
  edit: -1
}, {
  ['RESET_TEAM_CARD'](state, action){
    return {
      ...state,
      team_id: -1,
      channel_id: -1,
      app: {},
      name: '',
      url: '',
      type: '',
      subtype: '',
      edit: -1
    }
  },
  ['ADD_TEAM_CARD'](state, action){
    return {
      ...state,
      team_id: action.payload.team_id,
      channel_id: action.payload.channel_id,
      app: action.payload.app,
      name: action.payload.name,
      url: action.payload.url,
      type: action.payload.type,
      subtype: action.payload.subtype,
      account_information: action.payload.account_information
    }
  },
  ['MOVE_TEAM_CARD'](state, action){
    return {
      ...state,
      edit: action.payload.card_id
    }
  }
});