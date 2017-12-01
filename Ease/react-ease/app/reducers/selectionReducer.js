import {selectAppFromListById} from '../utils/helperFunctions';

export default function reducer(state={
  type:null,
  id:null,
  apps: [],
  loading: false
}, action){
  switch (action.type) {
    case 'SELECT_USER_FULFILLED': {
      return {
        ...state,
        type: 'user',
        id:action.payload.user.id,
        apps:action.payload.apps.reverse(),
        loading: false
      }
    }
    case "FETCH_TEAM_FULFILLED": {
      return {
        ...state,
        type: null,
        id: null,
        apps: []
      }
    }
    case 'FETCH_TEAM_USER_APPS_FULFILLED' : {
      return {
        ...state,
        apps: action.payload.apps.reverse(),
        type: action.payload.type,
        id: action.payload.id,
        loading: false
      }
    }
    case 'FETCH_TEAM_CHANNELS_APPS_FULFILLED' : {
      return {
        ...state,
        apps: action.payload.apps.reverse(),
        type: action.payload.type,
        id: action.payload.id,
        loading: false
      }
    }
    case 'FETCH_TEAM_USER_APPS_PENDING': {
      return {
          ...state,
        loading: true
      }
    }
    case 'FETCH_TEAM_CHANNEL_APPS_PENDING': {
      return {
          ...state,
        loading: true
      }
    }
    case 'SELECT_TEAM_CHANNEL_FULFILLED': {
      return {
        ...state,
        type:'channel',
        id: action.payload.channel.id,
        apps: action.payload.apps.reverse(),
        loading: false
      }
    }
    case "TEAM_CREATE_SINGLE_APP_FULFILLED": {
      if(state.type === action.payload.app.type && state.id === action.payload.app.id){
        var state = {
          ...state
        };
        state.apps.unshift(action.payload);
        return state;
      }
    }
    case "TEAM_CREATE_LINK_APP_FULFILLED": {
      if(state.type === action.payload.app.type && state.id === action.payload.app.id){
        var state = {
          ...state
        };
        state.apps.unshift(action.payload);
        return state;
      }
    }
    case "TEAM_CREATE_MULTI_APP_FULFILLED": {
      if(state.type === action.payload.app.type && state.id === action.payload.app.id){
        var state = {
          ...state
        };
        state.apps.unshift(action.payload);
        return state;
      }
    }
    case "TEAM_SHARE_APP_FULFILLED": {
      var nState = {
        ...state
      };
      for (var i = 0; i < nState.apps.length;i++){
        if (nState.apps[i].id === action.payload.app_id){
          nState.apps[i].receivers.push(action.payload.user_info);
          if (nState.apps[i].sharing_requests.indexOf(action.payload.user_info.team_user_id) !== -1)
            nState.apps[i].sharing_requests.splice(nState.apps[i].sharing_requests.indexOf(action.payload.user_info.team_user_id), 1);
          return nState;
        }
      }
      break;
    }
    case 'TEAM_MODIFY_APP_INFORMATION_FULFILLED': {
      var nState = {
        ...state
      };
      for (var i = 0; i < nState.apps.length;i++){
        if (nState.apps[i].id === action.payload.app_id){
          nState.apps[i] = action.payload.app;
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
      for (var i = 0; i < nState.apps.length;i++){
        if (nState.apps[i].id === action.payload.app_id){
          app = nState.apps[i];
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
      for (var i = 0; i < nState.apps.length;i++){
        if (nState.apps[i].id === action.payload.app_id){
          app = nState.apps[i];
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
      if (state.id === action.payload.channel_id && state.type === 'channel'){
        return {
          ...state,
          type:null,
          item:null
        }
      }
      break;
    }
    case 'REMOVE_TEAM_USER_FROM_CHANNEL_FULFILLED' : {
      const channel_id = action.payload.channel_id;
      const user_id = action.payload.team_user_id;
      if (state.id === channel_id && state.type === 'channel'){
        let apps = state.apps.map(item => {
          item.receivers = item.receivers.filter(item => {
            return item.team_user_id !== user_id;
          });
          item.sharing_requests = item.sharing_requests.filter(item => {
            return item !== user_id;
          });
          return item;
        });
        return {
          ...state,
          apps: apps
        }
      }
      break;
    }
    case 'DELETE_TEAM_USER_FULFILLED': {
      const user_id = action.payload.team_user_id;
      let apps = state.apps.map(item => {
        item.receivers = item.receivers.filter(item => {
          return item.team_user_id !== user_id;
        });
        item.sharing_requests = item.sharing_requests.filter(item => {
          return item !== user_id;
        });
        return item;
      });
      return {
        ...state,
        apps: apps
      };
    }
    case 'TEAM_APP_TRANSFER_OWNERSHIP_FULFILLED': {
      var apps = state.apps;

      for (var i = 0; i < apps.length; i++){
        if (apps[i].id === action.payload.app_id){
          apps[i].sender_id = action.payload.team_user_id;
        }
      }
      return {
        ...state,
        apps: apps
      }
    }
    case 'TEAM_DELETE_APP_FULFILLED': {
      var apps = state.apps;

      for (var i = 0; i < apps.length; i++){
        if (apps[i].id === action.payload.app_id){
          apps.splice(i, 1);
          break;
        }
      }
      return {
        ...state,
        apps: apps
      }
    }
    case 'TEAM_ACCEPT_SHARED_APP_FULFILLED': {
      var apps = state.apps;

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
        apps: apps
      }
    }
    case 'TEAM_APP_PIN_TO_DASHBOARD_FULFILLED': {
      var apps = state.apps;
      for (var i = 0; i < apps.length; i++){
        if (apps[i].id === action.payload.app_id){
          for (var j = 0; j < apps[i].receivers.length; j++){
            if (apps[i].receivers[j].id === action.payload.id){
              apps[i].receivers[j].profile_id = action.payload.profile_id;
              break;
            }
          }
          break;
        }
      }
      return {
        ...state,
        apps: apps
      }
    }
    case 'TEAM_SINGLE_CARD_JOIN_FULFILLED': {
      var apps = state.apps;
      var app = selectAppFromListById(apps, action.payload.team_card_id);

      if (app !== null && app.requests.indexOf(action.payload.team_user_id) === -1){
        app.requests.push(action.payload.team_user_id);
        return {
          ...state,
          apps: apps
        }
      }
    }
    case 'TEAM_ENTERPRISE_CARD_JOIN_FULFILLED': {
      var apps = state.apps;
      var app = selectAppFromListById(apps, action.payload.team_card_id);

      if (app !== null && app.requests.indexOf(action.payload.team_user_id) === -1){
        app.requests.push(action.payload.team_user_id);
        return {
          ...state,
          apps: apps
        }
      }
    }
    case 'TEAM_APP_ASK_JOIN_FULFILLED': {
      var apps = state.apps;
      var app = selectAppFromListById(apps, action.payload.app_id);

      if (app !== null && app.sharing_requests.indexOf(action.payload.team_user_id) === -1){
        app.sharing_requests.push(action.payload.team_user_id);
        return {
          ...state,
          apps: apps
        }
      }
    }
    case 'DELETE_JOIN_APP_REQUEST_FULFILLED': {
      var apps = state.apps;
      var app = selectAppFromListById(apps, action.payload.app_id);

      if (app !== null && app.sharing_requests.indexOf(action.payload.team_user_id) !== -1){
        app.sharing_requests.splice(app.sharing_requests.indexOf(action.payload.team_user_id), 1);
        return {
          ...state,
          apps: apps
        }
      }
    }
    case 'TEAM_USER_REMOVED': {
      const user_id = action.payload.team_user_id;
      let apps = state.apps.map(item => {
        item.receivers = item.receivers.filter(item => {
          return item.team_user_id !== user_id;
        });
        item.requests = item.requests.filter(item => {
          return item.team_user_id !== user_id;
        });
        return item;
      });
      return {
        ...state,
        apps: apps
      };
    }
    case 'TEAM_APP_CHANGED' : {
      let apps = state.apps;

      for (let i = 0; i < apps.length; i++){
        if (apps[i].id === action.payload.app.id){
          apps[i] = action.payload.app;
          return {
            ...state,
            apps: apps
          }
        }
      }
      break;
    }
    case 'TEAM_APP_ADDED' : {
      let apps = state.apps;
      const app = action.payload.app;
      if(state.type === app.type && state.id === app.id) {
        apps.unshift(app);
        return {
          ...state,
          apps: apps
        }
      }
    }
    case 'TEAM_CARD_REMOVED' : {
      const apps = state.apps.filter(app => (app.id !== action.payload.team_card_id));
      return {
        ...state,
        apps: apps
      }
    }
    case 'TEAM_APP_RECEIVER_CHANGED' : {
      const app_id = action.payload.app_id;
      const receiver = action.payload.receiver;
      const apps = state.apps.map(app => {
        if (app.id === app_id)
          app.receivers = app.receivers.map(item => {
            if (item.team_user_id === receiver.team_user_id)
              return receiver;
            return item;
          });
        return app;
      });
      return {
        ...state,
        apps: apps
      }
    }
    case 'TEAM_APP_RECEIVER_ADDED' : {
      const app_id = action.payload.app_id;
      const receiver = action.payload.receiver;
      const apps = state.apps.map(app => {
        if (app.id === app_id) {
          app.sharing_requests = app.sharing_requests.filter(req => (req !== receiver.team_user_id));
          app.receivers.push(receiver);
        }
        return app;
      });
      return {
        ...state,
        apps: apps
      }
    }
    case 'TEAM_APP_RECEIVER_REMOVED' : {
      const app_id = action.payload.app_id;
      const team_user_id = action.payload.team_user_id;
      const apps = state.apps.map(app => {
        if (app.id === app_id)
          app.receivers = app.receivers.filter(item => (item.team_user_id !== team_user_id));
        return app;
      });
      return {
        ...state,
        apps:apps
      }
    }
    case 'TEAM_LINK_APP_RECEIVER_ADDED' : {
      const app_id = action.payload.app_id;
      const receiver = action.payload.receiver;
      let changed = false;

      const apps = state.apps.map(app => {
        if (app.id === app_id){
          app.receivers = app.receivers.map(item => {
            if (item.team_user_id === receiver.team_user_id){
              changed = true;
              return receiver;
            }
            return item;
          });
          if (!changed)
            app.receivers.push(receiver);
        }
        return app;
      });
      return {
        ...state,
        apps:apps
      }
    }
  }
  return state;
}