import update from 'immutability-helper';

const initialState = {
  user : null,
  ws_id: '-1',
  authenticated : false,
  loginRedirectUrl: '',
  teamsTutorial: false,
  homepage: false
};

export default function reducer(state=initialState, action) {
  switch (action.type){
    case 'FETCH_MY_INFORMATION_FULFILLED': {
      return {
          ...state,
          user : action.payload.user,
          authenticated : !!action.payload.user
      }
    }
    case 'CONNECTION_FULFILLED': {
      return {
        ...state,
        user : action.payload.user,
        authenticated : true
      }
    }
    case 'CHECK_PASSWORD_FULFILLED': {
      return {
        ...state,
        password : action.payload.password
      }
    }
    case 'SET_LOGIN_REDIRECT_URL': {
      return {
          ...state,
        loginRedirectUrl: action.payload.url
      }
    }
    case 'LOGOUT_FULFILLED': {
      return {
          ...initialState
      }
    }
    case 'SET_TEAMS_TUTORIAL': {
      var user = state.user;
      user.status.team_tuto_done = action.payload;
      return {
          ...state,
        user: user
      }
    }
    case 'SET_WS_ID': {
      return {
          ...state,
        ws_id: action.payload.ws_id
      }
    }
    case 'TEAM_ADDED': {
      if (!state.user)
        break;
      let user = state.user;
      user.teams.push(action.payload.team);
      return {
        ...state,
        user: user
      }
    }
/*    case 'TEAM_REMOVED': {
      if (!state.user)
        break;
      let user = state.user;
      for (let i = 0; i < user.teams.length; i++){
        if (user.teams[i].id === action.payload.team.id){
          user.teams.splice(i, 1);
          return {
            ...state,
            user: user
          }
        }
      }
      break;
    }*/
    case 'TEAM_CHANGED': {
      if (!state.user)
        break;
      let user = state.user;
      user.teams = user.teams.map(item => {
        if (item.id === action.payload.team.id)
          return action.payload.team;
        return item;
      });
      return {
          ...state,
        user: user
      }
    }
    case 'SET_BACKGROUND_FULFILLED': {
      const {background_picture} = action.payload;
      return update(state, {
        user: {
          background_picture: {$set: background_picture}
        }
      })
    }
    case 'SET_HOMEPAGE': {
      return {
        ...state,
        homepage: action.payload.homepage
      }
    }
  }
  return state;
}