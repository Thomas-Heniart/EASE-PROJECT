export default function reducer(state={
  id: null,
  name: null,
  myTeamUserId: null,
  teamMenuActive: false
}, action){
  switch (action.type){
    case "FETCH_TEAM_FULFILLED": {
      return {
        ...state,
        name: action.payload.name,
        id: action.payload.id,
        myTeamUserId:action.payload.myTeamUserId
      }
    }
    case 'SHOW_TEAM_MENU': {
      return {
        ...state,
        teamMenuActive: action.payload
      }
    }
    case 'EDIT_TEAM_NAME_FULFILLED': {
      return {
        ...state,
        name: action.payload.name
      }
    }
  }
  return state;
}