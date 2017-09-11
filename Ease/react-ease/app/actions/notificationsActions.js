var post_api = require("../utils/post_api");
var api = require("../utils/api");

const sound = new Audio('/resources/notification_sound.mp3');

export function fetchNotifications(offset){
  return function (dispatch, getState){
    dispatch({type: 'FETCH_NOTIFICATIONS_PENDING'});
    return api.common.getNotifications(offset).then(r => {
      dispatch({type: 'FETCH_NOTIFICATIONS_FULFILLED', payload: {notifications: r}});
      return r;
    }).catch(err => {
      dispatch({type: 'FETCH_NOTIFICATIONS_REJECTED'});
      throw err;
    });
  }
}

export function validateNotification(){
  return function(dispatch, getState){
    return post_api.common.validateNotifications().then(r => {
      dispatch({type: 'VALIDATE_NOTIFICATIONS_FULFILLED'});
      return r;
    });
  }
}

export function newNotification(notification){
  sound.play();
  return {
    type: 'NEW_NOTIFICATION',
    payload: {
      notification: notification
    }
  }
}