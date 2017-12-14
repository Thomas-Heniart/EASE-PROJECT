import api from "../utils/api";
import axios from "axios";
import base64 from "base-64";
import {AsyncStorage} from "react-native";
import { Toast } from 'native-base';

function parseJwt (token) {
  let base64Url = token.split('.')[1];
  let str = base64Url.replace('-', '+').replace('_', '/');
  return JSON.parse(base64.decode(str));
}

export function connectionChanged(connectionInfo){
  return (dispatch, getState) => {
    if (connectionInfo.type === 'unknown')
      return;
    const network = connectionInfo.type !== 'none';
    dispatch({
      type: 'NET_CONNECTION_CHANGED',
      payload: {
        network: network
      }
    })
  }
}

export function changeUsername({username}) {
  return {
    type: 'CHANGE_USERNAME',
    payload: username
  }
}

export function connectionWithJWTToken({token}){
  return (dispatch, getState) => {
    return new Promise((resolve, reject) => {
      const information = parseJwt(token);
      if (information.exp < new Date().getTime())
        reject();
      AsyncStorage.setItem('JWTToken', token);
      axios.defaults.headers.common['Authorization'] = token;
      dispatch({type: 'CONNECTION', payload: {user_info: information}});
      resolve();
    });
  }
}

export function connection({email, password}) {
  return (dispatch, getState) => {
    return api.post.connection({
      email: email,
      password: password
    }).then(response => {
      AsyncStorage.setItem('JWTToken', response.JWT);
      axios.defaults.headers.common['Authorization'] = response.JWT;
      dispatch({
        type: 'CONNECTION',
        payload: {
          username: response.first_name,
          email: response.email
        }
      });
      return response;
    }).catch(err => {
      throw err;
    });
  }
}

export function setUserInformation({username, email}) {
  return {
    type: 'SET_USER_INFORMATION',
    payload: {
      username: username,
      email: email
    }
  }
}

export function fetchMyInformation() {
  return (dispatch, getState) => {
    return api.get.fetchMyInformation().then(response => {
      const user = response.user;
      AsyncStorage.setItem('LastEmail', user.email);
      dispatch(setUserInformation({
        username: user.first_name,
        email: user.email
      }));
      return response;
    }).catch(err => {
      throw err;
    })
  }
}

export function fetchPersonalSpace() {
  return (dispatch, getState) => {
    return api.get.fetchPersonalSpace().then(response => {
      const store = getState();
      const selectedItem = store.selectedItem.itemId;
      const profiles = response.profiles;
      dispatch({
        type: 'FETCH_PERSONAL_SPACE',
        payload: response
      });
      if (!profiles[selectedItem])
        dispatch(selectItem({
          itemId: Object.keys(profiles)[0]
        }));
      return response;
    }).catch(err => {
      throw err;
    });
  }
}

export function fetchCriticalInformation(){
  return (dispatch, getState) => {
    const calls = [
      dispatch(fetchMyInformation()),
      dispatch(fetchPersonalSpace())
    ];
    return Promise.all(calls).then((response) => {
      return response;
    }).catch(err => {
      throw err;
    });
  }
}

export function logout() {
  return (dispatch, getState) => {
    return AsyncStorage.removeItem('JWTToken').then(() => {
      axios.defaults.headers.common['Authorization'] = null;
      dispatch({type: 'LOGOUT'});
    }).catch(err => {
      throw err;
    })
  };
}

export function fetchSpaces(){
  return (dispatch, getState) => {
    return api.get.getPersonalAndTeamSpace()
        .then(response => {
          dispatch({type: 'FETCH_SPACES_FULFILLED', payload:{spaces: response}});
          return response;
        })
        .catch(err => {
          throw err;
        });
  }
}

export function fetchTeamRoomApps({team_id, room_id}){
  return (dispatch, getState) => {
    dispatch({type: 'FETCH_APPS_PENDING'});
    return api.get.getTeamRoomApps({
      team_id: team_id,
      room_id: room_id
    }).then(response => {
      const apps = response.apps;
      dispatch({type: 'FETCH_APPS_FULFILLED', payload: {apps: apps}});
      return apps;
    }).catch(err => {
      dispatch({type: 'FETCH_APP_REJECTED'});
      throw err;
    });
  }
}

export function fetchGroupApps({group_id}){
  return (dispatch, getState) => {
    dispatch({type: 'FETCH_APPS_PENDING'});
    return api.get.getGroupApps({
      group_id: group_id
    }).then(response => {
      const apps = response.apps;
      dispatch({type: 'FETCH_APPS_FULFILLED', payload: {apps: apps}});
      return apps;
    }).catch(err => {
      dispatch({type: 'FETCH_APP_REJECTED'});
      throw err;
    })
  }
}

export function selectItemAndFetchApps({itemId, subItemId, name}){
  return (dispatch, getState) => {
    dispatch(selectItem({
      itemId: itemId,
      subItemId: subItemId,
      name: name
    }));
    if (itemId === -1)
      return dispatch(fetchGroupApps({group_id: subItemId}));
    else
      return dispatch(fetchTeamRoomApps({team_id: itemId, room_id: subItemId}));
  }
}

export function selectItem({itemId}) {
  return {
    type: 'SELECT_ITEM',
    payload: {
      itemId: itemId
    }
  }
}