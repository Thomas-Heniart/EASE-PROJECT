import api from "../utils/api";
import axios from "axios";
import base64 from "base-64";

function parseJwt (token) {
  let base64Url = token.split('.')[1];
  let str = base64Url.replace('-', '+').replace('_', '/');
  return JSON.parse(base64.decode(str));
}

export function changeUsername({username}) {
  return {
    type: 'CHANGE_USERNAME',
    payload: username
  }
}

export function connection({email, password}) {
  return (dispatch, getState) => {
    return api.post.connection({
      email: email,
      password: password
    }).then(response => {
      const information = parseJwt(response.JWT);
      axios.defaults.headers.common['Authorization'] = 'JWT ' + response.JWT;
      dispatch({type: 'CONNECTION', payload: {user_info: information}});
      return information;
    }).catch(err => {
      throw err;
    });
  }
}

function lala(){
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      resolve(
          [
            {
              type:'link',
              id: 0,
              name: 'Facebook',
              logo: 'https://ease.space/resources/websites/Spotify/logo.png',
              url: 'https://www.bite.com'
            },
            {
              type: 'app',
              id:1,
              name: 'LinkedIn',
              logo:'https://ease.space/resources/websites/LinkedIn/logo.png',
              account_information: {
                login: 'FFFFfffff@fff.fff',
                password: 'ma bite'
              }
            }
          ]
      )
    }, 2000);
  })
}

export function fetchItemApps() {
  return (dispatch, getState) => {
    dispatch({type: 'FETCH_SELECTED_APPS_PENDING'});
    return lala().then(apps => {
      dispatch({type: 'FETCH_SELECTED_APPS_FULFILLED', payload: {apps: apps}});
    });
  }
}

export function selectItem({itemId, subItemId, name}){
  return (dispatch, getState) => {
    dispatch(fetchItemApps());
    dispatch({
      type: 'SELECT_ITEM',
      payload: {
        itemId: itemId,
        subItemId: subItemId,
        name: name
      }
    });
  };
}
