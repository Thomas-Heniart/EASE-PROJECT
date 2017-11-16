import createReducer from './createReducer';

export const teamCard = createReducer({
    team_id: -1,
    channel_id: -1,
    app: {},
    type: ''
}, {
   ['RESET_TEAM_CARD'](state, action){
       return {
           ...state,
           team_id: action.payload.team_id,
           channel_id: action.payload.channel_id,
           app: action.payload.app,
           type: action.payload.type
       }
   },
   ['ADD_TEAM_CARD'](state, action){
       return {
           ...state,
           team_id: action.payload.team_id,
           channel_id: action.payload.channel_id,
           app: action.payload.app,
           type: action.payload.type
       }
   }
});