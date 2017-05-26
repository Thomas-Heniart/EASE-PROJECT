var axios = require('axios');

module.exports = {
    fetchTeams : function(){
        return axios.get('/api/v1/teams/GetTeams')
            .then(function(response){
                return JSON.parse(response.data.substring(response.data.indexOf(" ")+1, response.data.length));
            });
    },
    fetchTeam : function (team_id) {
        return axios.get('/api/v1/teams/GetTeam',{
            params : {
                'team_id': team_id
            }
        }).then(function (response) {
            return JSON.parse(response.data.substring(response.data.indexOf(" ")+1, response.data.length));
        });
    },
    fetchTeamChannel : function (team_id, channel_id) {
        return axios.get('/api/v1/teams/GetChannel',{
            params:{
                'team_id': team_id,
                'channel_id': channel_id
            }
        }).then(function (response) {
            return JSON.parse(response.data.substring(response.data.indexOf(" ")+1, response.data.length));
        });
    },
    fetchTeamUser : function (team_id, team_user_id) {
        return axios.get('/api/v1/teams/GetTeamUser',{
            params :{
                'team_id': team_id,
                'teamuser_id': team_user_id
            }
        }).then(function (response) {
            return JSON.parse(response.data.substring(response.data.indexOf(" ")+1, response.data.length));
        });
    },
    getDashboardApp : function(id){
      return axios.get('/api/v1/dashboard/GetDashboardApp',{
        params: {
          'id': id
        }
      }).then(function(response){
        return JSON.parse(response.data.substring(response.data.indexOf(" ")+1, response.data.length));
      });
    },
    dashboardAppSearch: function(query){
      return axios.get('/api/v1/dashboard/SearchDashboardApps', {
        params: {
          'q':query
        }
      }).then(function(response){
        return JSON.parse(response.data.substring(response.data.indexOf(" ")+1, response.data.length));
      });
    },
    teamAppSearch : function (team_id, query) {
        return axios.get('/api/v1/catalog/SearchTeamCatalogApps',{
            params :{
                'q': query,
                'team_id': team_id
            }
        }).then(function (response) {
            return JSON.parse(response.data.substring(response.data.indexOf(" ")+1, response.data.length));
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
            return JSON.parse(response.data.substring(response.data.indexOf(" ")+1, response.data.length));
        });
    },
    fetchPopularRepos: function (language) {
        var encodedURI = window.encodeURI('https://api.github.com/search/' +
            'repositories?q=stars:>1+language:' + language +'' +
            '&sort=stars&order=desc&type=Repositories');
        return axios.get(encodedURI)
            .then(function(response){
                return response.data.items;
            });
    }
};