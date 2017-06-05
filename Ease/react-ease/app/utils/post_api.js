var axios = require('axios');

module.exports = {
  teamChannel: {
    editName : function(team_id, channel_id, name){
      return axios.post('/api/v1/teams/EditChannelName', {
        team_id: team_id,
        channel_id: channel_id,
        name: name
      }).then(response => {
        return response.data;
      });
    },
    editPurpose: function(team_id, channel_id, name){
      return axios.post('/api/v1/teams/EditChannelPurpose',{
        team_id: team_id,
        channel_id: channel_id,
        name: name
      }).then(response => {
        return response.data;
      });
    }
  }
  ,
  teamUser: {
    editRole : function(team_id, user_id, role){
      return axios.post('/api/v1/teams/EditTeamUserRole', {
        team_id: team_id,
        team_user_id: user_id,
        role: role
      }).then(response => {
        return response.data;
      });
    },
    editDepartureDate : function(team_id, user_id, departure_date){
      return axios.post('/api/v1/teams/EditTeamUserDepartureDate', {
        team_id: team_id,
        team_user_id: user_id,
        departure_date: departure_date
      }).then(response => {
        return response.data;
      });
    },
    editFirstName : function(team_id, user_id, first_name){
      return axios.post('/api/v1/teams/EditTeamUserFirstName', {
        team_id: team_id,
        team_user_id: user_id,
        first_name: first_name
      }).then(response => {
        return response.data;
      });
    },
    editLastName : function(team_id, user_id, last_name){
      return axios.post('/api/v1/teams/EditTeamUserLastName', {
        team_id: team_id,
        team_user_id: user_id,
        last_name: last_name
      }).then(response => {
        return response.data;
      });
    },
    editUsername : function(team_id, user_id, username){
      return axios.post('/api/v1/teams/EditTeamUserUsername', {
        team_id: team_id,
        team_user_id: user_id,
        username: username
      }).then(response => {
        return response.data;
      });
    }
  },
  teamApps: {
    shareSingleApp: function(team_id, app){
      return axios.post('/api/v1/teams/CreateShareableSingleApp', {
        team_id: team_id,
        channel_id: app.channel_id,
        website_id: app.website_id,
        name: app.name,
        description: app.description,
        reminder_interval: app.reminder_interval,
        account_information: app.account_information
      }).then(response => {
        return response.data;
      });
    }
  }
};