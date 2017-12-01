import api from './api';
export const passwordRegexp = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\S+$).{8,}$/;
export const emailRegexp = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
export const urlRegexp = /^(https?:\/\/)?([\da-z\.-]+)\.([a-z\.]{2,6})([\/\w \.-]*)*\/?$/;
export const usernameRegexp = /^[a-z0-9_\-]{3,21}$/;
export const roomNameRegexp = /^[a-z0-9_\-]{1,21}$/;
export const monthInMs = 2629746000;
export const userNameRuleString = 'Please choose a username that is all lowercase, containing only letters, numbers, periods, hyphens and underscores. From 3 to 22 characters.';

export function isUrl(url){
  return url.match(urlRegexp) !== null;
}

export function reflect(promise){
  return promise.then(function(v){ return {data:v, error: false }},
      function(e){ return {data:e, error: true }});
}

export function needPasswordUpdate(last_update, interval){
  if (!interval)
    return false;
  return last_update + monthInMs * interval < new Date().getTime();
}

export function teamUserDepartureDatePassed(departure_date){
  return !!departure_date && new Date().getTime() > departure_date;
}

export function isAppInformationEmpty(information){
  let empty_params = 0;
  Object.keys(information).map(item => {
    if (!information[item].length)
      empty_params++;
  });
  return empty_params > 1;
}

export function handleSemanticInput(e, {name, value, checked}){
  if (checked !== undefined){
    this.setState({[name]: !checked});
    return;
  }
  this.setState({[name]: value});
}

export function objectToList(obj){
  return Object.keys(obj).map(id => {
    return obj[id]
  });
}

export function transformCredentialsListIntoObject(credentials){
  return credentials.reduce((prev, curr) =>{
    return {...prev, [curr.name]: curr.value}
  }, {});
}

export function transformWebsiteInfoIntoList(informations){
  return Object.keys(informations)
      .sort((a,b) => (informations[a].priority - informations[b].priority))
      .map((item, idx) => {
        return {
          ...informations[item],
          value: '',
          name: item,
          autoFocus: idx === 0
        };
      });
}

export function transformWebsiteInfoIntoListAndSetValues(information, values){
  return transformWebsiteInfoIntoList(information).map(item => {
    item.value = !!values[item.name] ? values[item.name] : '';
    return item;
  });
}

export function isCredentialsMatch(prev, next){
  let match = true;
  Object.keys(prev).map(key => {
    if (prev[key] !== next[key])
      match = false;
  });
  if (match){
    Object.keys(next).map(key => {
      if (next[key] !== prev[key])
        match = false;
    });
  }
  return match;
}

export function getTeamAppPasswordAndCopyToClipboard({team_id, shared_app_id}){
  let loading = true;
  let password = '';
  let interval = null;
  return new Promise((resolve, reject) => {
    api.teamApps.getSharedAppPassword({team_id: team_id, shared_app_id: shared_app_id}).then(response => {
      loading = false;
      password = response;
    }).catch(err => {
      if (interval !== null)
        window.clearInterval(interval);
      reject(err);
    });
    interval = window.setInterval(() => {
      if (!loading){
        let worked = copyTextToClipboard(password);
        worked ? resolve(password) : reject();
        window.clearInterval(interval);
      }
    }, 10);
  });
}

export function copyTextToClipboard(str){
  let dummy = document.createElement("input");
  dummy.setAttribute('id', 'copy-password');
  dummy.style.position = 'absolute';
  dummy.style.left = '-150000px';
  document.body.appendChild(dummy);
  dummy.value = str;
  dummy.select();
  let worked = document.execCommand('copy');
  document.body.removeChild(dummy);
  return worked;
}

export function basicDateFormat(timestamp) {
  return moment(timestamp).format('MMMM Do YYYY');
}

export function checkTeamUsernameErrors(username){
  let value = {
    error: false,
    message: ''
  };
  if (username.length < 3 || username.length > 21)
    value.message = "Sorry, usernames must be greater than 2 characters and fewer than 22 characters.";
  else if (username.match(usernameRegexp) === null)
    value.message = "Sorry, usernames must contain only lowercase characters.";
  if (value.message.length > 0)
    value.error = true;
  return value;
}

export const jobRoles = [
  'Administrative/Facilities',
  'Accounting/Finance',
  'Business Development',
  'Business Owner',
  'Customer Support',
  'Data/Analytics/Business Intelligence',
  'Design',
  'Engineering (Software)',
  'Marketing',
  'Media/Communications',
  'Operations',
  'Product Management',
  'Program/Project Management',
  'Research',
  'Sales',
  'Other'
];
export const teamUserState = {
  invited: 0,
  registered: 1,
  accepted: 2
};

export const teamUserRoles = {
  1: 'Member',
  2: 'Admin',
  3: 'Owner'
};

export const passwordChangeValues = {
  0: 'No password update reminder',
  1: "Every 1 month",
  3: "Every 3 months",
  6: "Every 6 months",
  12: "Every 12 months"
};

export const credentialIconType = {
  password: 'lock',
  login: 'user',
  team: 'slack',
  storeName: 'shopping bag',
  subdomain: 'linkify'
};

export const passwordChangeOptions = [
  {key: 0, text: 'No password update reminder', value: 0},
  {key: 1, text: 'Every 1 month', value: 1},
  {key: 3, text: 'Every 3 months', value: 3},
  {key: 6, text: 'Every 6 months', value: 6},
  {key: 12, text: 'Every 12 months', value: 12},
];

export const teamUserRoleValues = [
  {key: 1, text: 'member', value:1},
  {key: 2, text: 'admin', value:2},
  {key: 3, text: 'owner', value:3}
];