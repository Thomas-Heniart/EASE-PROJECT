import update from 'immutability-helper';

export default function reducer(state={
  id: null,
  name: null,
  myTeamUserId: null,
  plan_id: 0,
  payment_required: false,
  teamMenuActive: false,
  payment: {
    data: {
      card: null,
      credit: 0,
      people_invited: false,
      business_vat_id: ""
    },
    loading: true
  }
}, action){
  switch (action.type){
    case "FETCH_TEAM_FULFILLED": {
      return {
        ...state,
        name: action.payload.name,
        id: action.payload.id,
        myTeamUserId:action.payload.myTeamUserId,
        plan_id: action.payload.plan_id,
        payment_required: action.payload.payment_required
      }
    }
    case "FETCH_TEAM_PAYMENT_INFORMATION_PENDING": {
      let new_state = {...state};

      new_state.payment.loading = true;
      return new_state;
    }
    case "FETCH_TEAM_PAYMENT_INFORMATION_REJECTED": {
      let new_state = {...state};

      new_state.payment.loading = false;
      return new_state;
    }
    case "FETCH_TEAM_PAYMENT_INFORMATION_FULFILLED" : {
      const new_state = update(state, {
        payment: {
          data: {$set: action.payload.data},
          loading: {$set: false}
        }
      });
      return new_state;
    }
    case 'TEAM_UPDATE_BILLING_INFORMATION_FULFILLED' : {
      const new_state = update(state, {
        payment: {
          data: {$set: action.payload.data}
        }
      });
      return new_state;
    }
    case 'TEAM_ADD_CREDIT_CARD_FULFILLED': {
      const new_state = update(state, {
        payment_required: {$set: false},
        payment: {
          data: {
            card: {$set: action.payload.card}
          }
        }
      });
      return new_state;
    }
    case 'TEAM_INVITE_FRIENDS_FULFILLED' : {
      const new_state = update(state, {
        payment: {
          data: {
            credit: {$set: action.payload.credit},
            people_invited: {$set: true}
          }
        }
      });
      return new_state;
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
    case 'UPGRADE_TEAM_PLAN_FULFILLED': {
      const team = action.payload.team;

      const new_state = update(state, {
        plan_id: {$set: team.plan_id},
        payment_required: {$set: team.payment_required}
      });
      return new_state;
    }
    case 'TEAM_CHANGED' : {
      const team = action.payload;
      return {
        ...state,
        name: team.name,
        id: team.id,
        myTeamUserId:team.myTeamUserId,
        plan_id: team.plan_id,
        payment_required: team.payment_required
      }
    }
  }
  return state;
}