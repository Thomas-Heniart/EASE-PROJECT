export default function reducer(state={
  type:null,
  item:null
}, action){
  switch (action.type) {
    case 'SELECT_USER_FULFILLED': {
      return {
          ...state,
        type: 'user',
        item:action.payload
      }
    }
    case 'SELECT_TEAM_CHANNEL_FULFILLED': {
      return {
          ...state,
        type:'channel',
        item: action.payload
      }
    }
    case 'EDIT_TEAM_CHANNEL_NAME_FULFILLED': {
      if (state.type === 'channel' && state.item.id === action.payload.id){
        return {
            ...state,
            item: {
                ...state.item,
                name: action.payload.name
            }
        }
      }
      break;
    }
    case 'EDIT_TEAM_CHANNEL_PURPOSE_FULFILLED': {
      if (state.type === 'channel' && state.item.id === action.payload.id){
        return {
          ...state,
          item: {
            ...state.item,
            purpose: action.payload.purpose
          }
        }
      }
      break;
    }
    case "EDIT_TEAM_USER_USERNAME_FULFILLED": {
      if (state.type === 'user' && state.item.id === action.payload.id){
        return {
            ...state,
          item: {
              ...state.item,
              username: action.payload.username
          }
        }
      }
      break;
    }
    case "EDIT_TEAM_USER_FIRSTNAME_FULFILLED": {
      if (state.type === 'user' && state.item.id === action.payload.id){
        return {
          ...state,
          item: {
            ...state.item,
            firstName: action.payload.first_name
          }
        }
      }
      break;
    }
    case "EDIT_TEAM_USER_LASTNAME_FULFILLED": {
      if (state.type === 'user' && state.item.id === action.payload.id){
        return {
          ...state,
          item: {
            ...state.item,
            lastName: action.payload.last_name
          }
        }
      }
      break;
    }
    case "EDIT_TEAM_USER_ROLE_FULFILLED": {
      if (state.type === 'user' && state.item.id === action.payload.id){
        return {
          ...state,
          item: {
            ...state.item,
            role: action.payload.role
          }
        }
      }
      break;
    }
    case "EDIT_TEAM_USER_DEPARTUREDATE_FULFILLED": {
      if (state.type === 'user' && state.item.id === action.payload.id){
        return {
          ...state,
          item: {
            ...state.item,
            departureDate: action.payload.departure_date
          }
        }
      }
      break;
    }
    case "TEAM_CREATE_SINGLE_APP_FULFILLED": {
      if(state.type === action.payload.origin.type && state.item.id === action.payload.origin.id){
        var state = {
            ...state
        };
        state.item.apps.unshift(action.payload);
        return state;
      }
    }
    case "TEAM_CREATE_LINK_APP_FULFILLED": {
      if(state.type === action.payload.origin.type && state.item.id === action.payload.origin.id){
        var state = {
          ...state
        };
        state.item.apps.unshift(action.payload);
        return state;
      }
    }
    case "TEAM_CREATE_MULTI_APP_FULFILLED": {
      if(state.type === action.payload.origin.type && state.item.id === action.payload.origin.id){
        var state = {
          ...state
        };
        state.item.apps.unshift(action.payload);
        return state;
      }
    }
    case "TEAM_SHARE_APP_FULFILLED": {
      var nState = {
          ...state
      };
      for (var i = 0; i < nState.item.apps.length;i++){
        if (nState.item.apps[i].id === action.payload.app_id){
          nState.item.apps[i].receivers.push(action.payload.user_info);
          return nState;
        }
      }
      break;
    }
    case 'TEAM_MODIFY_APP_INFORMATION_FULFILLED': {
      var nState = {
          ...state
      };
      for (var i = 0; i < nState.item.apps.length;i++){
        if (nState.item.apps[i].id === action.payload.app_id){
          nState.item.apps[i] = action.payload.app;
          return nState;
        }
      }
      break;
    }
    case 'TEAM_APP_EDIT_RECEIVER_FULFILLED' : {
      var nState = {
          ...state
      };
      var app;
      for (var i = 0; i < nState.item.apps.length;i++){
        if (nState.item.apps[i].id === action.payload.app_id){
          app = nState.item.apps[i];
          for (var j = 0; j < app.receivers.length; j++){
            if (app.receivers[j].team_user_id === action.payload.receiver_info.team_user_id){
              app.receivers[j] = action.payload.receiver_info;
              return nState;
            }
          }
        }
      }
      break;
    }
    case 'TEAM_APP_DELETE_RECEIVER_FULFILLED' : {
      var nState = {
        ...state
      };
      var app;
      for (var i = 0; i < nState.item.apps.length;i++){
        if (nState.item.apps[i].id === action.payload.app_id){
          app = nState.item.apps[i];
          for (var j = 0; j < app.receivers.length; j++){
            if (app.receivers[j].team_user_id === action.payload.team_user_id){
              app.receivers.splice(j, 1);
              return nState;
            }
          }
        }
      }
      break;
    }
    case 'DELETE_TEAM_CHANNEL_FULFILLED': {
      if (state.item.id === action.payload.channel_id && state.type === 'channel'){
        return {
            ...state,
          type:null,
          item:null
        }
      }
      break;
    }
    case 'DELETE_TEAM_USER_FULFILLED': {
      if (state.item.id === action.payload.team_user_id && state.type === 'user'){
        return {
          ...state,
          type:null,
          item:null
        }
      }
      break;
    }
    case 'TEAM_APP_TRANSFER_OWNERSHIP_FULFILLED': {
      var item = {
          ...state.item
      };
      var apps = item.apps;

      for (var i = 0; i < apps.length; i++){
        if (apps[i].id === action.payload.app_id){
          apps[i].sender_id = action.payload.team_user_id;
        }
      }
      return {
          ...state,
        item: item
      }
    }
    case 'TEAM_DELETE_APP_FULFILLED': {
      var item = {...state.item};

      for (var i = 0; i < item.apps.length; i++){
        if (item.apps[i].id === action.payload.app_id){
          item.apps.splice(i, 1);
          break;
        }
      }
      return {
          ...state,
        item: item
      }
    }
    case 'TEAM_ACCEPT_SHARED_APP_FULFILLED': {
      var item = {...state.item};
      const apps = item.apps;

      for (var i = 0; i < apps.length; i++){
        if (apps[i].id === action.payload.app_id){
          for (var j = 0; j < apps[i].receivers.length; j++){
            if (apps[i].receivers[j].shared_app_id === action.payload.shared_app_id){
              apps[i].receivers[j].accepted = true;
              break;
            }
          }
          break;
        }
      }
      return {
        ...state,
        item: item
      }
    }
  }
  return state;
}