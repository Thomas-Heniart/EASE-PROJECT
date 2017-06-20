var axios = require('axios');

module.exports = {
  fetchTeams : function(){
    return axios.get('/api/v1/teams/GetTeams')
        .then(function(response){
          return response.data;
        });
  },
  fetchTeam : function (team_id) {
    return axios.get('/api/v1/teams/GetTeam',{
      params : {
        'team_id': team_id
      }
    }).then(function (response) {
      return response.data;
    });
  },
  fetchTeamChannels: function(team_id){
    return axios.get('/api/v1/teams/GetChannels',{
      params: {
        team_id: team_id
      }
    }).then((response) => {
      return response.data;
    });
  },
  fetchTeamUsers: function (team_id) {
    return axios.get('/api/v1/teams/GetTeamUsers', {
      params: {
        team_id: team_id
      }
    }).then((response) => {
      return response.data;
    });
  },
  fetchTeamChannel : function (team_id, channel_id) {
    return axios.get('/api/v1/teams/GetChannel',{
      params:{
        'team_id': team_id,
        'channel_id': channel_id
      }
    }).then(function (response) {
      return response.data;
    });
  },
  fetchTeamChannelApps : function(team_id, channel_id){
    return axios.get('/api/v1/teams/GetChannelApps', {
      params :{
        'team_id': team_id,
        'channel_id': channel_id
      }
    }).then(response => {
      return response.data;
    })
  },
  fetchTeamUser : function (team_id, team_user_id) {
    return axios.get('/api/v1/teams/GetTeamUser',{
      params :{
        'team_id': team_id,
        'team_user_id': team_user_id
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
        team_user_id: team_user_id
      }
    }).then(response => {
      return response.data;
    })
  },
  fetchTeamUserShareableApps: function(team_id, team_user_id){
    return axios.get('/api/v1/teams/GetUserShareableApps', {
      params: {
        team_id: team_id,
        team_user_id: team_user_id
      }
    }).then(response => {
      return response.data;
    })
  },
  fetchTeamUserApps: function (team_id, team_user_id) {
    return axios.get('/api/v1/teams/GetTeamUserApps',{
      params :{
        'team_id': team_id,
        'team_user_id': team_user_id
      }
    }).then(function (response) {
      return response.data;
    });
  },
  getDashboardApp : function(id){
    return axios.get('/api/v1/dashboard/GetDashboardApp',{
      params: {
        'id': id
      }
    }).then(function(response){
      return response.data;
    });
  },
  dashboardAppSearch: function(query){
    return axios.get('/api/v1/dashboard/SearchDashboardApps', {
      params: {
        'q':query
      }
    }).then(function(response){
      return response.data;
    });
  },
  teamAppSearch : function (team_id, query) {
    return axios.get('/api/v1/catalog/SearchTeamCatalogApps',{
      params :{
        'q': query,
        'team_id': team_id
      }
    }).then(function (response) {
      return response.data;
    });
  },
  dashboardAndTeamAppSearch: function(team_id, query){
    return axios.all([this.dashboardAppSearch(query), this.teamAppSearch(team_id, query)])
        .then(axios.spread(function(dashboard, teams){
          var apps = dashboard.concat(teams);
          apps.sort(function(a,b){
            if (a.website_name < b.website_name)
              return -1;
            if (a.website_name > b.website_name)
              return 1;
            return 0;
          });
          return apps;
        }));
  },
  fetchWebsiteInfo: function (website_id) {
    return axios.get('/api/v1/catalog/GetWebsiteInformation',{
      params:{
        'id': website_id
      }
    }).then(function (response) {
      return response.data;
    });
  }
};