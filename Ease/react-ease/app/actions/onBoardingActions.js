import api from "../utils/api";
import post_api from "../utils/post_api";
import {addNotification} from "./notificationBoxActions";

export function fetchOnBoardingRooms() {
  return (dispatch, getState) => {
    return api.fetchOnBoardingRooms()
      .then(response => {
        dispatch({type: 'FETCH_ON_BOARDING_ROOMS', payload: response});
        return response;
      }).catch(err => {
        throw err;
      })
  }
}

export function askRegistration({email, newsletter}) {
  return (dispatch, getState) => {
    return post_api.common.askRegistration({
      email: email,
      newsletter: newsletter
    }).then(response => {
      return response
    }).catch(err => {
      throw err;
    });
  }
}

export function checkAskRegistration({email, digits}) {
  return (dispatch, getState) => {
    return post_api.common.checkRegistrationDigits(email, digits)
      .then(response => {
        return response;
      }).catch(err => {
        throw err;
      })
  }
}

export function onBoardingChangeStep({team_id, step}) {
  return (dispatch, getState) => {
    return post_api.onBoarding.onBoardingChangeStep({
      team_id: team_id,
      step: step,
      ws_id: getState().common.ws_id
    }).then(response => {
      dispatch({type: 'CHANGE_STEP', payload: {team_id: team_id, step: step}});
      return response
    }).catch(err => {
      throw err;
    });
  }
}

export function newRegistration({email, username, password, digits, code, phone_number, newsletter, first_name, last_name}) {
  return (dispatch, getState) => {
    return post_api.onBoarding.newRegistration({
      username: username,
      email: email,
      password: password,
      digits: digits,
      code: code,
      phone_number: phone_number,
      newsletter: newsletter,
      first_name: first_name,
      last_name: last_name
    }).then(response => {
      return response
    }).catch(err => {
      throw err;
    });
  }
}

export function joinTeamRegistration({email, password, code, phone_number, newsletter, first_name, last_name, access_code}) {
  return (dispatch, getState) => {
    return post_api.onBoarding.joinTeamRegistration({
      email: email,
      password: password,
      code: code,
      phone_number: phone_number,
      newsletter: newsletter,
      first_name: first_name,
      last_name: last_name,
      access_code: access_code
    }).then(response => {
      return response
    }).catch(err => {
      throw err;
    });
  }
}

export function editFirstNameAndLastName({first_name, last_name}) {
  return (dispatch, getState) => {
    return post_api.onBoarding.editFirstAndLastName({
      first_name: first_name,
      last_name: last_name
    }).then(response => {
      return response
    }).catch(err => {
      throw err;
    })
  }
}

export function getInfoClearbit({email}) {
  return (dispatch, getState) => {
    return api.getInfoClearbit({
      email: email
    }).then(response => {
      return response
    }).catch(err => {
      throw err;
    });
  }
}

export function createTeam({name, email, username, company_size, digits ,plan_id}) {
  return (dispatch, getState) => {
    return post_api.teams.createTeam({
      name: name,
      email: email,
      username: username,
      company_size: company_size,
      digits: digits,
      plan_id: plan_id,
      ws_id: getState().common.ws_id
    }).then(response => {
      response.team_users = {[response.my_team_user_id]: response.team_users[0]};
      response.rooms = {[response.rooms[0].id]: response.rooms[0]};
      dispatch({type: 'TEAM_CREATED', payload: {team: response}});
      return response;
    }).catch(err => {
      throw err;
    })
  }
}

export function createTeamProfile({team_id, team_users_and_channels}) {
  return (dispatch, getState) => {
    return post_api.onBoarding.createTeamProfile({
      team_id: team_id,
      team_users_and_channels: team_users_and_channels
    }).then(response => {
      if (response.profiles !== null) {
        response.profiles.forEach((profile) => {
          dispatch({
            type: 'DASHBOARD_PROFILE_CREATED',
            payload: {
              profile: profile
            }
          });
        });
      }
      return response.profiles;
    }).catch(err => {
      throw err;
    })
  }
}

export function onBoardingImportation({passwordManager, team_id}) {
  return (dispatch, getState) => {
    dispatch({type: 'GO_TO_ON_BOARDING_IMPORTATION',
      payload: {data: {passwordManager: passwordManager, team_id: team_id}}});
  }
}

export function resetOnBoardingImportation() {
  return (dispatch, getState) => {
    dispatch({type: 'RESET_ON_BOARDING_PASSWORD_MANAGER'});
  }
}

export function goToOnBoarding({passwordManager, team_id}) {
  return (dispatch, getState) => {
    dispatch({type: 'GO_TO_ON_BOARDING',
      payload: {team_id: team_id}
    });
  }
}

export function changeStep({team_id, step}) {
  return (dispatch, getState) => {
    return post_api.onBoarding.changeStep({
      team_id: team_id,
      step: step
    }).then(response => {
      dispatch({type: 'TEAM_CHANGE_STEP', payload: {team_id: team_id, step: step}});
      return response;
    }).catch(err => {
      throw err;
    })
  }
}