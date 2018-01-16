export function addNotification({text}){
  return (dispatch, getState) => {
    const id = getState().notificationBox.lastId + 1;
    dispatch(addNotificationAction({
      id: id,
      text: text
    }));
    setTimeout(() => {
      dispatch(deleteNotificationAction({
        id: id
      }));
    }, 5000);
  }
}

export function addNotificationAction({id, text}) {
  return {
    type: 'ADD_NOTIFICATION_BOX',
    payload: {
      notification: {
        id: id,
        text: text
      }
    }
  }
}

export function deleteNotificationAction({id}) {
  return {
    type: 'DELETE_NOTIFICATION_BOX',
    payload: {
      notification_id: id
    }
  }
}