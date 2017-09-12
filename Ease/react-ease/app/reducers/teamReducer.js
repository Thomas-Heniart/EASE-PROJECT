import update from 'immutability-helper';

export default function reducer(state={
  id: null,
  name: null,
  myTeamUserId: null,
  teamMenuActive: false,
  payment: {
    data: {
      card : null,
      credit: 0,
      people_invited: false,
      business_vat_id: ""
    },
    loading: true
  },
  paymentDataLoading: false
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
  }
  return state;
}