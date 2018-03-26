import update from 'immutability-helper';

const initialState = {
  user : null,
  ws_id: '-1',
  authenticated : false,
  loginRedirectUrl: '',
  teamsTutorial: false,
  homepage: false,
  generalLogoutModal: false,
  dailyLogout: false
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
          ...initialState,
        generalLogoutModal: state.generalLogoutModal
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
    case 'DASHBOARD_TUTORIAL_DONE': {
      return update(state, {
        user: {
          status: {
            tuto_done: {$set: true}
          }
        }
      })
    }
    case 'NEW_FEATURE_SEEN': {
      return update(state, {
        user: {
          new_feature_seen: {$set: true}
        }
      })
    }
    case 'SET_TIP_SEEN': {
      const {name} = action.payload;
      return update(state, {
        user: {
          status: {
            [name]: {$set: true}
          }
        }
      });
    }
    case 'SET_GENERAL_LOGOUT_MODAL_ACTIVE': {
      const {active} = action.payload;
      return update(state, {
        generalLogoutModal: {$set: active}
      })
    }
    case 'SET_DAILY_LOGOUT_ACTIVE': {
      const {active} = action.payload;

      return update(state, {
        dailyLogout: {$set: active}
      })
    }
  }
  return state;
}