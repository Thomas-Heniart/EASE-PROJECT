var api = require('../utils/api');
var post_api = require('../utils/post_api');

export function teamShareSingleApp(app){
  return function (dispatch, getState){
    dispatch({type: 'TEAM_SHARE_SINGLE_APP_PENDING'});
    return post_api.teamApps.shareSingleApp(getState().team.id, app).then(response => {
      dispatch({type: 'TEAM_SHARE_SINGLE_APP_FULFILLED', payload: app});
    }).catch(err => {
      dispatch({type: 'TEAM_SHARE_SINGLE_APP_REJECTED', payload: err});
      throw err;
    });
  }
}