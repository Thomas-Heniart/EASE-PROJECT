import api from "../utils/api";
import post_api from "../utils/post_api";

export function fetchDashboard(){
  return (dispatch, getState) => {
    const calls = [
      api.dashboard.fetchProfileList(),
      api.dashboard.fetchApps()
    ];
    dispatch({type: 'FETCH_DASHBOARD_PENDING'});
    return Promise.all(calls).then(response => {
      const columns = response[0];
      const apps = response[1];
      dispatch({type: 'FETCH_DASHBOARD_FULFILLED', payload: {
        columns: columns,
        apps: apps
      }});
      return response;
    }).catch(err => {
      dispatch({type: 'FETCH_DASHBOARD_REJECTED', payload: err});
      throw err;
    })
  }
}

export function moveApp({app_id, targetApp_id}) {
  return {
    type: 'INSERT_APP',
    payload: {
      app_id: app_id,
      targetApp_id: targetApp_id
    }
  }
}

export function insertAppInProfile({app_id, profile_id}){
  return (dispatch, getState) => {
    const store = getState();
    if (store.dashboard.apps[app_id].profile_id === profile_id)
      return;
    dispatch({
      type: 'INSERT_APP_IN_PROFILE',
      payload: {
        app_id:app_id,
        profile_id: profile_id
      }
    })
  }
}

export function beginAppDrag({app_id}){
  return {
    type: 'BEGIN_APP_DRAG',
    payload: {
      app_id: app_id
    }
  }
}

export function endAppDrag({app_id}){
  return {
    type: 'END_APP_DRAG',
    payload: {
      app_id: app_id
    }
  }
}

export function beginProfileDrag({profile_id}){
  return {
    type: 'BEGIN_PROFILE_DRAG',
    payload: {
      profile_id: profile_id
    }
  }
}

export function endProfileDrag({profile_id}){
  return {
    type: 'END_PROFILE_DRAG',
    payload: {
      profile_id: profile_id
    }
  }
}

export function moveProfile({profile_id, targetProfile_id, hoverClientY, hoverMiddleY}){
  return (dispatch, getState) => {
    const store = getState();
    const profile = store.dashboard.profiles[profile_id];
    const hoverProfile = store.dashboard.profiles[targetProfile_id];
    const dragIndex = store.dashboard.columns[profile.column_idx].indexOf(profile_id);
    const hoverIndex = store.dashboard.columns[hoverProfile.column_idx].indexOf(targetProfile_id);
    // Dragging downwards
    if (dragIndex < hoverIndex && hoverClientY < hoverMiddleY)
      return;
    // Dragging upwards
    if (dragIndex > hoverIndex && hoverClientY > hoverMiddleY)
      return;
    dispatch({
      type: 'INSERT_PROFILE',
      payload: {
        profile_id: profile_id,
        targetProfile_id: targetProfile_id
      }
    });
  };
}

export function insertProfileIntoColumn({profile_id, column_idx}){
  return {
    type: 'INSERT_PROFILE_IN_COLUMN',
    payload: {
      profile_id: profile_id,
      column_idx: column_idx
    }
  }
}

export function editClassicApp({app_id, name, account_information}){
  return (dispatch, getState) => {
    return post_api.dashboard.editClassicApp({
      app_id: app_id,
      name: name,
      account_information: account_information
    }).then(response => {
      dispatch({type: 'DASHBOARD_APP_CHANGED', payload: {app: response}});
      return response;
    }).catch(err =>  {
      throw err;
    })
  }
}

export function editLogWithApp({app_id, name, logWithApp_id}) {
  return (dispatch, getState) => {
    return post_api.dashboard.editLogWithApp({
      app_id: app_id,
      name: name,
      logWithApp_id:logWithApp_id
    }).then(response => {
      dispatch({type: 'DASHBOARD_APP_CHANGED', payload: {app: response}});
      return response;
    }).catch(err =>  {
      throw err;
    })
  }
}

export function editLinkApp({app_id, name, url, img_url}) {
  return (dispatch, getState) => {
    return post_api.dashboard.editLinkApp({
      app_id: app_id,
      name: name,
      url: url,
      img_url: img_url
    }).then(response=> {
      dispatch({type: 'DASHBOARD_APP_CHANGED', payload: {app: response}});
      return response;
    }).catch(err =>  {
      throw err;
    })
  }
}
