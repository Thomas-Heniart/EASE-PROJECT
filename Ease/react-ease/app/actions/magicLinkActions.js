import * as api from '../utils/api';
import * as post_api from '../utils/post_api'
import {teamCardChangedAction} from "./appsActions";
import {showManageMagicLinkModal} from "./teamModalActions";

export function getTeamCardFromMagicLink({card_id, uuid}) {
  return (dispatch, getState) => {
    return api.magicLink.getTeamCardFromMagicLink({
      card_id: card_id,
      uuid: uuid
    }).then(response => {
      return response;
    }).catch(error => {
      throw error;
    });
  }
}

export function sendCredentialsToTeam({card_id, uuid, account_information, type, url, img_url, connection_information}) {
  return (dispatch, getState) => {
    return post_api.teamApps.sendCredentialsToTeam({
      card_id: card_id,
      uuid: uuid,
      account_information: account_information,
      type: type,
      url: url,
      img_url: img_url,
      connection_information: connection_information,
      ws_id: getState().common.ws_id
    }).then(response => {
      return response;
    }).catch(error => {
      throw error;
    });
  }
}

export function renewMagicLink({team_id, team_card_id}) {
  return (dispatch, getState) => {
    return post_api.magicLink.renewMagicLink({
      team_id: team_id,
      team_card_id: team_card_id,
      ws_id: getState().common.ws_id
    }).then(response => {
      dispatch(teamCardChangedAction({
        team_card: response
      }));
      dispatch(showManageMagicLinkModal({
        active: true,
        team_card: response
      }));
      return response
    }).catch(error => {})
  }
}