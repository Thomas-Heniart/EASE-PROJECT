import axios from "axios"
var api = require('../utils/api');
var post_api = require('../utils/post_api');
import * as UserActions from "./userActions"
import * as ChannelActions from "./channelActions"
import {closeAppAddUI} from "./teamAppsAddUIActions";

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
      });
      dispatch({type: 'FETCH_TEAMS_FULFILLED', payload: {teams: teams}});
      return response;
    }).catch(err => {
      dispatch({type: 'FETCH_TEAMS_REJECTED'});
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
        if (team_apps[id] === undefined)
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

export function fetchTeam(id) {
  return (dispatch) => {
    dispatch({type:'FETCH_TEAM_PENDING', payload:id});
    return api.fetchTeam(id).then((response) => {
      dispatch({type: "FETCH_TEAM_FULFILLED", payload: response});
    }).catch((err) => {
      dispatch({type:"FETCH_TEAM_REJECTED", payload: err});
      throw err;
    });
  }
}

export function fetchTeamAndUsersAndChannels(team_id){
  return (dispatch) => {
    return dispatch(fetchTeam(team_id)).then(() => {
      return dispatch(UserActions.fetchUsers(team_id)).then(()=> {
        return dispatch(ChannelActions.fetchChannels(team_id));
      })
    })
  }
}

export function teamInviteFriends({team_id, email1, email2, email3}){
  return (dispatch, getState) => {
    return post_api.teams.inviteFriends(team_id, email1, email2, email3).then(response => {
      dispatch({type: 'TEAM_INVITE_FRIENDS_FULFILLED', payload: response});
      return response;
    }).catch(err => {
      throw err;
    });
  }
}

export function teamAddCreditCard({team_id, cardToken}){
  return (dispatch, getState) => {
    return post_api.teams.addCreditCard({team_id: team_id, cardToken: cardToken}).then(response => {
      dispatch({type: 'TEAM_ADD_CREDIT_CARD_FULFILLED', payload: {card: response, team_id: team_id}});
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
      return response;
    }).catch(err => {
      throw err;
    });
  }
}

export function fetchTeamPaymentInformation({team_id}){
  return (dispatch, getState) => {
    dispatch({type: 'FETCH_TEAM_PAYMENT_INFORMATION_PENDING', payload:{team_id: team_id}});
    return api.teams.getTeamPaymentInformation({team_id: team_id}).then(r => {
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
    return post_api.teams.unsubscribe({team_id: team_id, password: password}).then(response => {
      dispatch({type:'TEAM_REMOVED', payload: {team_id: team_id}});
      return response;
    }).catch(err => {
      throw err;
    });
  }
}

export function fetchTeamItemApps(itemId){
  return function (dispatch, getState){
    dispatch(closeAppAddUI());
    if (itemId[0] !== '@')
      return dispatch(ChannelActions.fetchTeamChannelApps(Number(itemId)));
    else
      return dispatch(UserActions.fetchTeamUserApps(Number(itemId.slice(1, itemId.length))));
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
    return post_api.teams.editTeamName(team_id, name).then(r => {
      dispatch({type: 'EDIT_TEAM_NAME_FULFILLED', payload: {team_id:team_id,name:name}});
      return name;
    }).catch(err => {
      dispatch({type: 'EDIT_TEAM_NAME_REJECTED', payload: err});
      throw err;
    });
  }
}

export function upgradePlan({team_id, plan_id}){
  return (dispatch, getState) => {
    return post_api.teams.upgradePlan({
      team_id: team_id,
      plan_id: plan_id
    }).then(team => {
      dispatch({type: 'UPGRADE_TEAM_PLAN_FULFILLED', payload: {team: team}});
      return team;
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
  return {
    type: 'TEAM_REMOVED',
    payload: {
      team_id: team_id
    }
  }
}