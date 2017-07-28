const initialState = {
  user : null,
  authenticated : false,
  loginRedirectUrl: '',
  teamsTutorial: false,
  notifications: [
    {
      icon: '/resources/websites/Facebook/logo.png',
      content: 'This is my notification. And this is the second phrase of my notification.',
      id: 1,
      date: 5,
      isNew: true
    },
    {
      icon: '/resources/websites/Facebook/logo.png',
      content: 'This is my notification. And this is the second phrase of my notification.',
      id: 2,
      date: 5,
      isNew: false
    },
    {
      icon: '/resources/websites/Facebook/logo.png',
      content: 'This is my notification. And this is the second phrase of my notification.',
      id: 3,
      date: 5,
      isNew: true
    },
    {
      icon: '/resources/websites/Facebook/logo.png',
      content: 'This is my notification. And this is the second phrase of my notification.',
      id: 4,
      date: 5,
      isNew: true
    },
    {
      icon: '/resources/websites/Facebook/logo.png',
      content: 'This is my notification. And this is the second phrase of my notification.',
      id: 5,
      date: 5,
      isNew: true
    },
    {
      icon: '/resources/websites/Facebook/logo.png',
      content: 'This is my notification. And this is the second phrase of my notification.',
      id: 6,
      date: 5,
      isNew: true
    }
  ]
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
      return {
          ...state,
        teamsTutorial: action.payload
      }
    }
  }
  return state;
}