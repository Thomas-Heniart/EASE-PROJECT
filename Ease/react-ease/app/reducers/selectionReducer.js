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
  }
  return state;
}