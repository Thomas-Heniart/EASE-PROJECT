import {teamUserState} from "../utils/utils";
import {selectUserFromListById} from "../utils/helperFunctions";

export default function reducer(state={
  users: [],
  me: null
},action){
  switch (action.type){
    case "FETCH_USERS_FULFILLED": {
      var users = action.payload.users;
      var myTeamId = action.payload.myTeamUserId;
      var myUser = null;
      for (var i = 0; i < users.length; i++){
        if (users[i].id === myTeamId){
          myUser = users[i];
          break;
        }
      }
      return {
        ...state,
        users: users,
        me: myUser
      }
    }
    case "EDIT_TEAM_USER_USERNAME_FULFILLED": {
      var nMe = {
          ...state.me
      };
      var nUsers = state.users.map(function(item){
        if (item.id === action.payload.id){
          item.username = action.payload.username;
        }
        return item;
      });
      if (state.me && state.me.id === action.payload.id){
        nMe.username = action.payload.username;
      }
      return {
          ...state,
        users: nUsers,
        me: nMe
      }
    }
    case "EDIT_TEAM_USER_FIRSTNAME_FULFILLED": {
      var nMe = {
        ...state.me
      };
      var nUsers = state.users.map(function(item){
        if (item.id === action.payload.id){
          item.first_name = action.payload.first_name;
        }
        return item;
      });
      if (state.me && state.me.id === action.payload.id){
        nMe.first_name = action.payload.first_name;
      }
      return {
        ...state,
        users: nUsers,
        me: nMe
      }
    }
    case "EDIT_TEAM_USER_LASTNAME_FULFILLED": {
      var nMe = {
        ...state.me
      };
      var nUsers = state.users.map(function(item){
        if (item.id === action.payload.id){
          item.last_name = action.payload.last_name;
        }
        return item;
      });
      if (state.me && state.me.id === action.payload.id){
        nMe.last_name = action.payload.last_name;
      }
      return {
        ...state,
        users: nUsers,
        me: nMe
      }
    }
    case 'CREATE_TEAM_USER_FULFILLED': {
      var users = state.users.map(function (item) {
        return item;
      });
      users.push(action.payload);
      return {
          ...state,
          users: users
      }
    }
    case 'DELETE_TEAM_USER_FULFILLED': {
      var users = state.users.map(function (item) {
        return item;
      });
      for (var i = 0; i < users.length; i++){
        if (users[i].id === action.payload.team_user_id){
          users.splice(i, 1);
          break;
        }
      }
      return {
          ...state,
        users: users
      }
    }
    case 'ADD_TEAM_USER_TO_CHANNEL_FULFILLED': {
      var users = state.users.map(function(item){
        if (item.id === action.payload.team_user_id)
          item.channel_ids.push(action.payload.channel_id);
        return item;
      });
      return {
        ...state,
        users: users
      }
    }
    case 'REMOVE_TEAM_USER_FROM_CHANNEL_FULFILLED': {
      var users = state.users.map(item => {
        if (item.id === action.payload.team_user_id) {
          item.channel_ids.splice(item.channel_ids.indexOf(action.payload.channel_id), 1);
        }
        return item;
      });
      return {
        ...state,
        users: users
      }
    }
    case 'VERIFY_TEAM_USER_ARRIVE_FULFILLED': {
      var users = state.users.map(item => {
        if (item.id === action.payload.team_user_id)
          item.state = teamUserState.accepted;
        return item;
      });
      return {
          ...state,
        users: users
      }
    }
    case 'TEAM_TRANSFER_OWNERSHIP_FULFILLED' : {
      var users = state.users.map(item => {
        if (item.id === action.payload.ownerId)
          item.role = '2';
        else if (item.id === action.payload.team_user_id)
          item.role = '3';
        return item;
      });
      return {
        ...state,
        users: users
      }
    }
    case 'EDIT_TEAM_USER_PHONE_FULFILLED': {
      var users = state.users.map(item => {
        if (item.id === action.payload.team_user_id)
          item.phone_number = action.payload.phone;
        return item;
      });
      return {
        ...state,
        users: users
      }
    }
    case 'TEAM_USER_CHANGED': {
      var users = state.users.map(item => {
        if (item.id === action.payload.user.id)
          return action.payload.user;
        return item;
      });
      return {
          ...state,
        users: users
      }
    }
    case 'TEAM_USER_ADDED': {
      var users = state.users;
      if (selectUserFromListById(state.users, action.payload.user.id) != null)
        break;
      users.push(action.payload.user);
      return {
          ...state,
        users: users
      }
    }
    case 'TEAM_USER_REMOVED': {
      var users = state.users;

      for (var i = 0; i < users.length; i++){
        if (users[i].id === action.payload.user.id){
          users.splice(i, 1);
          return {
              ...state,
            users: users
          }
        }
      }
    }
    case 'DELETE_TEAM_CHANNEL_FULFILLED': {
      var users = state.users.map(item => {
        const idx = item.channel_ids.indexOf(action.payload.channel_id);
        if (idx != -1)
          item.channel_ids.splice(idx, 1);
        return item;
      });
      return {
          ...state,
        users: users
      }
    }
  }
  return state;
}