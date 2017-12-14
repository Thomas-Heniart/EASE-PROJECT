import createReducer from  "./createReducer";
import update from 'immutability-helper';

export const notificationBox = createReducer({
  lastId: -1,
  notifications: []
}, {
  ['ADD_NOTIFICATION_BOX'](state, action){
    const {notification} = action.payload;

    return update(state, {
      notifications: {$push: [notification]},
      lastId: {$set: notification.id}
    });
  },
  ['DELETE_NOTIFICATION_BOX'](state, action){
    const {notification_id} = action.payload;
    const notification = state.notifications.find(n => (n.id === notification_id));
    if (!!notification) {
      const idx = state.notifications.indexOf(notification);
      return update(state, {
        notifications: {$splice: [[idx, 1]]}
      });
    }
    return state;
  }
});