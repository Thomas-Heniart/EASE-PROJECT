export default function reducer(state={
  notifications: [],
  fetching: false
}, action){
  switch (action.type){
    case 'FETCH_NOTIFICATIONS_PENDING': {
      return {
          ...state,
        fetching: true
      }
    }
    case 'FETCH_NOTIFICATIONS_REJECTED': {
      return {
          ...state,
        fetching: false
      }
    }
    case 'FETCH_NOTIFICATIONS_FULFILLED': {
      var n = state.notifications.concat(action.payload.notifications);
      return {
        ...state,
        notifications: n,
        fetching: false
      }
    }
    case 'VALIDATE_NOTIFICATIONS_FULFILLED': {
      var n = state.notifications.map(item => {
        if (item.is_new)
          item.is_new = false;
        return item;
      });
      console.log('notifications validated');
      return {
          ...state,
        notifications: n
      }
    }
    case 'NEW_NOTIFICATION': {
      var n = state.notifications;
      n.unshift(action.payload.notification);
      return {
          ...state,
        notifications: n
      }
    }
  }
  return state;
}