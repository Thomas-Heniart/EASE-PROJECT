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
        teams[team.id] = team;
      });
      dispatch({type: 'FETCH_TEAMS_FULFILLED', payload: {teams: teams}});
      return response;
    }).catch(err => {
      dispatch({type: 'FETCH_TEAMS_REJECTED'});
      throw err;
    });
  }
}

export function fetchTeam(id) {
  return function(dispatch){
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

export function teamInviteFriends(email1, email2, email3){
  return (dispatch, getState) => {
    dispatch({type:'TEAM_INVITE_FRIENDS_PENDING'});
    return post_api.teams.inviteFriends(getState().team.id, email1, email2, email3).then(response => {
      dispatch({type: 'TEAM_INVITE_FRIENDS_FULFILLED', payload: response});
      return response;
    }).catch(err => {
      dispatch({type: 'TEAM_INVITE_FRIENDS_REJECTED', payload:err});
      throw err;
    });
  }
}

export function teamAddCreditCard({cardToken}){
  return (dispatch, getState) => {
    dispatch({type: 'TEAM_ADD_CREDIT_CARD_PENDING'});
    return post_api.teams.addCreditCard({team_id: getState().team.id, cardToken: cardToken}).then(response => {
      dispatch({type: 'TEAM_ADD_CREDIT_CARD_FULFILLED', payload: {card: response}});
    }).catch(err => {
      dispatch({type: 'TEAM_ADD_CREDIT_CARD_REJECTED', payload: err});
      throw err;
    });
  }
}

export function teamUpdateBillingInformation({address_city, address_country, address_line1, address_line2, address_state, address_zip, business_vat_id}){
  return (dispatch, getState) => {
    dispatch({type: 'TEAM_UPDATE_BILLING_INFORMATION_PENDING'});
    return post_api.teams.updateBillingInformation({
      team_id: getState().team.id,
      address_city: address_city,
      address_country: address_country,
      address_line1: address_line1,
      address_line2:address_line2,
      address_state: address_state,
      address_zip: address_zip,
      business_vat_id: business_vat_id
    }).then(response => {
      dispatch({type: 'TEAM_UPDATE_BILLING_INFORMATION_FULFILLED', payload:{data: response}});
    }).catch(err => {
      dispatch({type: 'TEAM_UPDATE_BILLING_INFORMATION_REJECTED', payload:err});
      throw err;
    });
  }
}

export function fetchTeamPaymentInformation(){
  return (dispatch, getState) => {
    dispatch({type: 'FETCH_TEAM_PAYMENT_INFORMATION_PENDING'});
    return api.teams.getTeamPaymentInformation({team_id: getState().team.id}).then(r => {
      dispatch({type: 'FETCH_TEAM_PAYMENT_INFORMATION_FULFILLED', payload: {data: r}});
      return r;
    }).catch(err => {
      dispatch({type: 'FETCH_TEAM_PAYMENT_INFORMATION_REJECTED', payload: err});
    });
  }
}

export function unsubscribe(password){
  return (dispatch, getState) => {
    dispatch({type: 'TEAM_UNSUBSCRIBE_PENDING'});
    return post_api.teams.unsubscribe({team_id: getState().team.id, password: password}).then(response => {
      dispatch({type:'TEAM_UNSUBSCRIBE_FULFILLED', payload: {team_id: getState().team_id}});
      return response;
    }).catch(err => {
      dispatch({type: 'TEAM_UNSUBSCRIBE_REJECTED', payload: err});
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

export function editTeamName(name){
  return function(dispatch, getState){
    dispatch ({type: 'EDIT_TEAM_NAME_PENDING'});
    return post_api.teams.editTeamName(getState().team.id, name).then(r => {
      dispatch({type: 'EDIT_TEAM_NAME_FULFILLED', payload: {name:name}});
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