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
  }
  return state;
}