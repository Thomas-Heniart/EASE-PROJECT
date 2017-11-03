import api from "../utils/api";

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