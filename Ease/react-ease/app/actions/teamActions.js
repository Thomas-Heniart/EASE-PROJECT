var api = require('../utils/api');
var post_api = require('../utils/post_api');
import {dashboardAppRemovedAction} from "./dashboardActions";
import * as UserActions from "./userActions"
import * as ChannelActions from "./channelActions"
import {closeAppAddUI} from "./teamAppsAddUIActions";
import {addNotification} from "./notificationBoxActions";

export function fetchTeams(){
  return (dispatch, getState) => {
    dispatch({type: 'FETCH_TEAMS_PENDING'});
    return api.teams.fetchTeams().then(response => {
      let teams = {};
      response.map(team => {
        let rooms = {};
        let team_users = {};
        team.rooms = team.rooms.map(room => {
          rooms[room.id] = room;
        });
        team.team_users = team.team_users.map(team_user => {
          team_users[team_user.id] = team_user
        });
        teams[team.id] = team;
        teams[team.id].rooms = rooms;
        teams[team.id].team_users = team_users;
        dispatch(fetchTeamCardsPasswordStrength({
          team_id: team.id
        }));
      });
      dispatch({type: 'FETCH_TEAMS_FULFILLED', payload: {teams: teams}});
      return response;
    }).catch(err => {
      dispatch({type: 'FETCH_TEAMS_REJECTED'});
      throw err;
    });
  }
}

export function fetchTeamCardsPasswordStrength({team_id}) {
  return (dispatch, getState) => {
    return api.teams.getTeamPasswordStrengthScore({
      team_id: team_id
    }).then(description => {
      dispatch({
        type: 'FETCH_TEAM_CARDS_PASSWORD_STRENGTH_DESCRIPTION',
        payload: {
          team_id: team_id,
          description: description
        }
      });
      return description;
    }).catch(err => {
      throw err;
    });
  }
}

export function fetchTeamAppList({team_id, ids}){
  return (dispatch, getState) => {
    return new Promise((resolve, reject) => {
      const team_apps = getState().team_apps;
      let calls = [];
      ids.map(id => {
        if (!team_apps[id])
          calls.push(dispatch(fetchTeamApp({
            team_id: team_id,
            app_id: id
          })));
      });
      Promise.all(calls).then(response => {
        resolve();
      }).catch(err => {
        reject();
      })
    })
  }
}

export function fetchTeamApp({team_id, app_id}){
  return (dispatch, getState) => {
    return api.teams.fetchTeamApp({
      team_id: team_id,
      app_id: app_id
    }).then(app => {
      dispatch({type: 'FETCH_TEAM_APP_FULFILLED', payload: {app: app}});
      return app;
    }).catch(err => {
      throw err;
    });
  }
}

export function teamInviteFriends({team_id, email1, email2, email3}){
  return (dispatch, getState) => {
    return post_api.teams.inviteFriends(team_id, email1, email2, email3).then(response => {
      dispatch({type: 'TEAM_INVITE_FRIENDS_FULFILLED', payload: response});
      dispatch(addNotification({
        text: "Friends successfully invited! +15€ received!"
      }));
      return response;
    }).catch(err => {
      throw err;
    });
  }
}

export function teamAddCreditCard({team_id, cardToken}){
  return (dispatch, getState) => {
    return post_api.teams.addCreditCard({
      team_id: team_id,
      cardToken: cardToken,
      ws_id: getState().common.ws_id
    }).then(response => {
      dispatch({type: 'TEAM_ADD_CREDIT_CARD_FULFILLED', payload: {card: response, team_id: team_id}});
      dispatch(addNotification({
        text: "New payment method successfully setup!"
      }));
      return response;
    }).catch(err => {
      throw err;
    });
  }
}

export function teamUpdateBillingInformation({team_id, address_city, address_country, address_line1, address_line2, address_state, address_zip, business_vat_id}){
  return (dispatch, getState) => {
    return post_api.teams.updateBillingInformation({
      team_id: team_id,
      address_city: address_city,
      address_country: address_country,
      address_line1: address_line1,
      address_line2:address_line2,
      address_state: address_state,
      address_zip: address_zip,
      business_vat_id: business_vat_id
    }).then(response => {
      dispatch({type: 'TEAM_UPDATE_BILLING_INFORMATION_FULFILLED', payload:{data: response}});
      dispatch(addNotification({
        text: "Billing information successfully setup!"
      }));
      return response;
    }).catch(err => {
      throw err;
    });
  }
}

export function fetchTeamPaymentInformation({team_id}){
  return (dispatch, getState) => {
    dispatch({type: 'FETCH_TEAM_PAYMENT_INFORMATION_PENDING', payload:{team_id: team_id}});
    return api.teams.getTeamPaymentInformation({
      team_id: team_id
    }).then(r => {
      dispatch({type: 'FETCH_TEAM_PAYMENT_INFORMATION_FULFILLED', payload: {data: r}});
      return r;
    }).catch(err => {
      dispatch({type: 'FETCH_TEAM_PAYMENT_INFORMATION_REJECTED', payload: err});
      throw err;
    });
  }
}

export function unsubscribe({team_id,password}){
  return (dispatch, getState) => {
    return post_api.teams.unsubscribe({
      team_id: team_id,
      password: password,
      ws_id: getState().common.ws_id
    }).then(response => {
      dispatch(teamRemovedAction({team_id: team_id}));
      return response;
    }).catch(err => {
      throw err;
    });
  }
}

export function showTeamMenu(state){
  return {
    type: 'SHOW_TEAM_MENU',
    payload: state
  }
}

export function editTeamName({team_id, name}){
  return function(dispatch, getState){
    dispatch ({type: 'EDIT_TEAM_NAME_PENDING'});
    return post_api.teams.editTeamName({
      team_id: team_id,
      name: name,
      ws_id: getState().common.ws_id
    }).then(r => {
      const team = getState().teams[team_id];
      dispatch(addNotification({
        text: `${name} is now the new name for ${team.name}`
      }));
      dispatch({type: 'EDIT_TEAM_NAME_FULFILLED', payload: {team_id:team_id,name:name}});
      return name;
    }).catch(err => {
      throw err;
    });
  }
}

export function upgradePlan({team_id, plan_id}){
  return (dispatch, getState) => {
    return post_api.teams.upgradePlan({
      team_id: team_id,
      plan_id: plan_id,
      ws_id: getState().common.ws_id
    }).then(team => {
      dispatch({type: 'UPGRADE_TEAM_PLAN_FULFILLED', payload: {team: team}});
      dispatch(addNotification({
        text: `Your team ${team.name} has been upgraded for free for 1 month!`
      }));
      return team;
    }).catch(err => {
      throw err;
    });
  }
}

export function inviteFriend({team_id, email}) {
  return (dispatch, getState) => {
    return post_api.teams.inviteFriend({
      ws_id: getState().common.ws_id,
      team_id: team_id,
      email: email
    }).then(response => {
      dispatch({
        type: 'TEAM_INVITE_FRIEND_FULFILLED',
        payload: {
          team_id: team_id
        }
      });
      dispatch(addNotification({
        text: 'Invitation sent! Thank you!'
      }));
    }).catch(err => {
      throw err;
    });
  }
}

export function teamCreatedAction({team}) {
  return {
    type: 'TEAM_CREATED',
    payload: {
      team: team
    }
  }
}

export function teamChangedAction({team}) {
  return {
    type: 'TEAM_CHANGED',
    payload: {
      team: team
    }
  }
}

export function teamRemovedAction({team_id}) {
  return (dispatch, getState) => {
    const store = getState();
    const apps = store.dashboard.apps;
    Object.keys(apps).map(id => {
      const app = apps[id];
      if (app.team_id === team_id)
        dispatch(dashboardAppRemovedAction({app_id: app.id}));
    });
    dispatch({
      type: 'TEAM_REMOVED',
      payload: {
        team_id: team_id
      }
    });
  };
}

export function teamPasswordScoreAlert({team_id}) {
  return (dispatch, getState) => {
    return post_api.teamApps.passwordScoreAlert({
      team_id: team_id
    }).then(last_alert_date => {
      dispatch({
        type: 'TEAM_PASSWORD_SCORE_ALERT',
        payload: {
          team_id: team_id,
          last_alert_date: last_alert_date
        }
      });
      return last_alert_date;
    }).catch(err => {
      throw err;
    });
  }
}

export function updateTeamPasswordsAmount({team_id, diff}){
  return {
    type: 'TEAM_CARDS_PASSWORDS_TOTAL_CHANGED',
    payload: {
      team_id: team_id,
      diff: diff
    }
  }
}

export function updateTeamStrongPasswordsAmount({team_id, diff}){
  return {
    type: 'TEAM_CARDS_STRONG_PASSWORDS_TOTAL_CHANGED',
    payload: {
      team_id: team_id,
      diff: diff
    }
  }
}