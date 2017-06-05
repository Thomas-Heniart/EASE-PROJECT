export default function reducer(state={
  channels: []
}, action){
  switch (action.type){
    case 'FETCH_TEAM_CHANNELS_FULFILLED': {
      return {
          ...state,
        channels: action.payload
      }
    }
    case 'FETCH_TEAM_CHANNELS_REJECTED': {
      break;
    }
    case 'EDIT_TEAM_CHANNEL_NAME_FULFILLED': {
      var nChannels = state.channels.map(function(item){
        if (item.id === action.payload.id){
          item.name = action.payload.name;
        }
        return item;
      });
      return {
          ...state,
        channels: nChannels
      }
    }
    case 'EDIT_TEAM_CHANNEL_PURPOSE_FULFILLED': {
      var nChannels = state.channels.map(function(item){
        if (item.id === action.payload.id){
          item.purpose = action.payload.purpose;
        }
        return item;
      });
      return {
        ...state,
        channels: nChannels
      }
    }
  }
  return {
      ...state
  };
}