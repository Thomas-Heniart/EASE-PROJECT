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
    case 'CREATE_TEAM_CHANNEL_FULFILLED': {
      var nChannels = state.channels.map(function(item){
        return item;
      });

      nChannels.push(action.payload);
      return {
          ...state,
        channels: nChannels
      }
    }
    case 'ADD_TEAM_USER_TO_CHANNEL_FULFILLED': {
      var nChannels = state.channels.map(function(item){
        if (item.id === action.payload.channel_id)
          item.userIds.push(action.payload.team_user_id);
        return item;
      });
      return {
        ...state,
        channels: nChannels
      }
    }
    case 'DELETE_TEAM_CHANNEL_FULFILLED': {
      var nChannels = state.channels.map(function(item){
          return item;
      });
      for (var i = 0; i < nChannels.length; i++){
        if (nChannels[i].id === action.payload.channel_id){
          nChannels.splice(i,1);
          break;
        }
      }
      return {
        ...state,
        channels: nChannels
      }
    }
    case 'DELETE_TEAM_USER_FULFILLED': {
      var nChannels = state.channels.map(function(item){
        return item;
      });
      for (var i = 0; i < nChannels.length; i++){
          nChannels[i].userIds.splice(nChannels[i].userIds.indexOf(action.payload.team_user_id), 1);
      }
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