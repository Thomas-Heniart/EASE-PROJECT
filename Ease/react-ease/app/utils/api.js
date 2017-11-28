var axios = require('axios');
import {reflect} from "./utils";

const basic_get = (url, params) => {
  return axios.get(url, {params: params})
      .then(response => {
        return response.data;
      })
      .catch(err => {
        throw err.response.data;
      });
};

module.exports = {
  catalog: {
    getWebsites: () => {
      return basic_get('/api/v1/catalog/GetWebsites');
    },
    getCategories: () => {
      return basic_get('/api/v1/catalog/GetCategories');
    },
    getSsoList: () => {
      return basic_get('/api/v1/catalog/GetSsoList');
    },
    getRequestsNumber: () => {
      return basic_get('/api/v1/catalog/GetRequestNumber')
    }
  },
  getClearbitLogo : function (url) {
    const l = document.createElement("a");
    l.href = url;
    const img_url = "https://logo.clearbit.com/" + l.hostname;
    return axios.get("https://logo.clearbit.com/" + l.hostname).then(response => {
      if (img_url.endsWith(window.location.hostname) && url.indexOf(window.location.hostname) === -1)
        return '/resources/icons/link_app.png';
      return img_url;
    }).catch(err => {
      throw err;
    })
  },
  getLogo: function({url: url}) {
    const l = document.createElement("a");
    l.href = url;
    const img_url = "https://logo.clearbit.com/" + l.hostname;
    return reflect(axios.get("https://logo.clearbit.com/" + l.hostname)).then(response => {
      if (response.error)
        return '/resources/icons/link_app.png';
      return img_url;
    });
  },
  fetchTeams : function(){
    return axios.get('/api/v1/teams/GetTeams',{
      params: {
        timestamp: new Date().getTime()
      }
    }).then(function(response){
      return response.data;
    });
  },
  fetchTeam : function (team_id) {
    return axios.get('/api/v1/teams/GetTeam',{
      params : {
        'team_id': team_id,
        timestamp: new Date().getTime()
      }
    }).then(function (response) {
      return response.data;
    });
  },
  fetchTeamChannels: function(team_id){
    return axios.get('/api/v1/teams/GetChannels',{
      params: {
        team_id: team_id,
        timestamp: new Date().getTime()
      }
    }).then(response => {
      const channels = response.data.sort((a,b) => {
        if (a.default)
          return -1;
        return 1;
      });
      return channels;
    });
  },
  fetchTeamUsers: function (team_id) {
    return axios.get('/api/v1/teams/GetTeamUsers', {
      params: {
        team_id: team_id,
        timestamp: new Date().getTime()
      }
    }).then((response) => {
      return response.data;
    });
  },
  fetchTeamChannel : function (team_id, channel_id) {
    return axios.get('/api/v1/teams/GetChannel',{
      params:{
        'team_id': team_id,
        'channel_id': channel_id,
        timestamp: new Date().getTime()
      }
    }).then(function (response) {
      return response.data;
    });
  },
  fetchTeamChannelApps : function(team_id, channel_id){
    return axios.get('/api/v1/teams/GetChannelApps', {
      params :{
        'team_id': team_id,
        'channel_id': channel_id,
        timestamp: new Date().getTime()
      }
    }).then(response => {
      return response.data;
    })
  },
  fetchTeamUser : function (team_id, team_user_id) {
    return axios.get('/api/v1/teams/GetTeamUser',{
      params :{
        'team_id': team_id,
        'team_user_id': team_user_id,
        timestamp: new Date().getTime()
      }
    }).then(function (response) {
      return response.data;
    });
  },
  fetchTeamUserShareableAppsInChannel: function (team_id, channel_id, team_user_id) {
    return axios.get('/api/v1/teams/GetUserShareableAppsInChannel', {
      params:{
        team_id: team_id,
        channel_id: channel_id,
        team_user_id: team_user_id,
        timestamp: new Date().getTime()
      }
    }).then(response => {
      return response.data;
    })
  },
  fetchTeamUserShareableApps: function(team_id, team_user_id){
    return axios.get('/api/v1/teams/GetUserShareableApps', {
      params: {
        team_id: team_id,
        team_user_id: team_user_id,
        timestamp: new Date().getTime()
      }
    }).then(response => {
      return response.data;
    })
  },
  fetchTeamUserApps: function (team_id, team_user_id) {
    return axios.get('/api/v1/teams/GetTeamUserApps',{
      params :{
        'team_id': team_id,
        'team_user_id': team_user_id,
        timestamp: new Date().getTime()
      }
    }).then(function (response) {
      return response.data;
    });
  },
  getDashboardApp : function(id){
    return axios.get('/api/v1/dashboard/GetDashboardApp',{
      params: {
        'id': id,
        timestamp: new Date().getTime()
      }
    }).then(function(response){
      return response.data;
    });
  },
  dashboardAppSearch: function(query){
    return axios.get('/api/v1/dashboard/SearchDashboardApps', {
      params: {
        'q':query,
        timestamp: new Date().getTime()
      }
    }).then(function(response){
      return response.data;
    });
  },
  teamAppSearch : function (team_id, query) {
    return axios.get('/api/v1/catalog/SearchTeamCatalogApps',{
      params :{
        'q': query,
        'team_id': team_id,
        timestamp: new Date().getTime()
      }
    }).then(function (response) {
      return response.data;
    });
  },
  dashboardAndTeamAppSearch: function(team_id, query){
    return axios.all([module.exports.dashboardAppSearch(query), module.exports.teamAppSearch(team_id, query)])
        .then(axios.spread(function(dashboard, teams){
          let apps = dashboard.concat(teams);
          apps.sort(function(a,b){
            if (a.name < b.name)
              return -1;
            if (a.name > b.name)
              return 1;
            return 0;
          });
          return apps;
        }));
  },
  fetchWebsiteInfo: function (website_id) {
    return axios.get('/api/v1/catalog/GetWebsiteInformation',{
      params:{
        id: website_id,
        timestamp: new Date().getTime()
      }
    }).then(function (response) {
      return response.data;
    });
  },
  getWebsitesCatalog: function() {
    return axios.get('/api/v1/catalog/GetWebsites')
        .then(function (response) {
          return response.data;
        });
  },
  getCategories: function() {
    return axios.get('/api/v1/catalog/GetCategories')
        .then(function (response) {
          return response.data
        });
  },
  dashboard: {
    fetchProfiles: function(){
      return axios.get('/api/v1/dashboard/GetProfiles').then(response => {
        return response.data;
      });
    },
    fetchProfileList: function () {
      return basic_get('/api/v1/dashboard/GetProfileList');
    },
    fetchApps: function() {
      return basic_get('/api/v1/dashboard/GetApps');
    },
    fetchSsoGroups: () => {
      return basic_get('/api/v1/dashboard/GetSsoGroups');
    },
    getAppPassword: ({app_id}) => {
      return basic_get('/api/v1/dashboard/GetAppPassword', {
        app_id: app_id
      });
    },
    getAppConnectionInformation: ({app_id}) => {
      return basic_get('/api/v1/dashboard/GetConnection', {
        app_id: app_id
      });
    }
  },
  teamApps: {
    getSharedAppPassword: function({team_id, shared_app_id}){
      return axios.get('/api/v1/teams/GetSharedAppPassword', {
        params: {
          team_id: team_id,
          shared_app_id: shared_app_id,
          timestamp: new Date().getTime()
        }
      }).then(response => {
        return decipher(response.data.password);
      }).catch(err => {
        throw err.response.data;
      });
    },
    getSingleAppPassword: function ({team_id, app_id}) {
      return axios.get('/api/v1/teams/GetSingleAppPassword', {
        params: {
          team_id: team_id,
          app_id: app_id,
          timestamp: new Date().getTime()
        }
      }).then(response => {
        return decipher(response.data.password);
      }).catch(err => {
        throw err.response.data;
      });
    }
  },
  teams: {
    fetchTeams: () => {
      return basic_get('/api/v1/teams/GetTeams', {
        timestamp: new Date().getTime()
      });
    },
    fetchTeamApp: ({team_id, app_id}) => {
      return basic_get('/api/v1/teams/GetTeamCard', {
        team_id: team_id,
        team_card_id: app_id,
      })
    },
    getInvitationInformation : function({code}){
      return axios.get('/api/v1/teams/GetInvitationInformation', {
        params : {
          code: code
        }
      }).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
      });
    },
    finalizeRegistration: function(code){
      return axios.get('/api/v1/teams/FinalizeRegistration', {
        params: {
          code: code,
          timestamp: new Date().getTime()
        }
      }).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
      });
    },
    getTeamPaymentInformation: function({team_id}){
      return axios.get('/api/v1/teams/GetTeamPaymentInformation', {
        params: {
          team_id: team_id
        }
      }).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
      })
    }
  },
  common: {
    bz : function(){
      return axios.post('/bz').then(response => {
        return response.data.connected;
      }).catch(err => {
        throw err.response.data;
      });
    },
    checkAuthentication : function(){
      return axios.get('/api/v1/common/checkAuthentication').then(response => {
        return response.data;
      });
    },
    logout: function(){
      return axios.get('/api/v1/common/Logout').then(response => {
        return response.data;
      });
    },
    fetchMyInformation : function () {
      return axios.get('/api/v1/common/GetMyInformation', {
        params: {
          timestamp:new Date().getTime()
        }
      }).then(response => {
        return response.data;
      });
    },
    getNotifications: function (offset) {
      return axios.get('/api/v1/common/GetNotifications', {
        params: {
          offset: offset
        }
      }).then(r => {
        return r.data;
      }).catch(err => {
        throw err.response.data;
      })
    }
  }
};