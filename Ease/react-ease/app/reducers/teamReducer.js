export default function reducer(state={
  id: null,
  name: null,
  myTeamUserId: null
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
    case "FETCH_TEAM_REJECTED": {

    }
  }
  return state;
}