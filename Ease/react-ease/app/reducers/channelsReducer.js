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
    case 'CREATE_TEAM_USER_FULFILLED' : {
      const user = action.payload;
      let channels = state.channels.map(item => {
        if (item.default)
          item.userIds.push(user.id);
        return item;
      });
      return {
          ...state,
        channels: channels
      }
    }
    case 'ADD_TEAM_USER_TO_CHANNEL_FULFILLED': {
      var nChannels = state.channels.map(function(item){
        if (item.id === action.payload.channel_id) {
          item.userIds.push(action.payload.team_user_id);
          if (item.join_requests.indexOf(action.payload.team_user_id) !== -1)
            item.join_requests.splice(item.join_requests.indexOf(action.payload.team_user_id), 1);
        }
        return item;
      });
      return {
        ...state,
        channels: nChannels
      }
    }
    case 'REMOVE_TEAM_USER_FROM_CHANNEL_FULFILLED': {
      var nChannels = state.channels.map(item => {
        if (item.id === action.payload.channel_id) {
          const index = item.userIds.indexOf(action.payload.team_user_id);
          if (index !== -1)
            item.userIds.splice(index, 1);
        }
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
    case 'TEAM_USER_REMOVED': {
      var nChannels = state.channels.map(function(item){
        const index = item.userIds.indexOf(action.payload.user.id);
        if (index !== -1)
          item.userIds.splice(index, 1);
        return item;
      });
      return {
        ...state,
        channels: nChannels
      }
    }
    case 'DELETE_TEAM_USER_FULFILLED': {
      var nChannels = state.channels.map(function(item){
        const index = item.userIds.indexOf(action.payload.team_user_id);
        if (index !== -1)
          item.userIds.splice(index, 1);
        return item;
      });
      return {
          ...state,
        channels: nChannels
      }
    }
    case 'ASK_JOIN_CHANNEL_FULFILLED': {
      var nChannels = state.channels.map(item => {
        if (item.id === action.payload.channel_id && item.join_requests.indexOf(action.payload.team_user_id) === -1){
          item.join_requests.push(action.payload.team_user_id);
        }
        return item;
      });
      return {
          ...state,
          channels: nChannels
      }
    }
    case 'DELETE_JOIN_CHANNEL_REQUEST_FULFILLED': {
      var nChannels = state.channels.map(item => {
        if (item.id === action.payload.channel_id && item.join_requests.indexOf(action.payload.team_user_id) !== -1){
          item.join_requests.splice(item.join_requests.indexOf(action.payload.team_user_id), 1);
        }
        return item;
      });
      return {
        ...state,
        channels: nChannels
      }
    }
    case 'TEAM_ROOM_CHANGED': {
      var channels = state.channels.map(item => {
        if (item.id === action.payload.channel.id)
          return action.payload.channel;
        return item;
      });
      return {
        ...state,
        channels: channels
      }
    }
    case 'TEAM_ROOM_ADDED': {
      var channels = state.channels;
      if (selectUserFromListById(state.channels, action.payload.channel.id) !== null)
        break;
      users.push(action.payload.channel);
      return {
        ...state,
        channels: channels
      }
    }
    case 'TEAM_ROOM_REMOVED': {
      var channels = state.channels;

      for (var i = 0; i < channels.length; i++){
        if (channels[i].id === action.payload.channel.id){
          channels.splice(i, 1);
          return {
            ...state,
            channels: channels
          }
        }
      }
      break;
    }
  }
  return state;
}