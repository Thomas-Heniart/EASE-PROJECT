const initialState = {
  user : null,
  ws_id: -1,
  authenticated : false,
  loginRedirectUrl: '',
  teamsTutorial: false
};

export default function reducer(state=initialState, action) {
  switch (action.type){
    case 'FETCH_MY_INFORMATION_FULFILLED': {
      return {
          ...state,
          user : action.payload.user,
          authenticated : action.payload.user !== null
      }
    }
    case 'CONNECTION_FULFILLED': {
      return {
        ...state,
        user : action.payload.user,
        authenticated : true
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
  }
  return state;
}