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
    editPurpose: function(team_id, channel_id, purpose){
      return axios.post('/api/v1/teams/EditChannelPurpose',{
        team_id: team_id,
        channel_id: channel_id,
        purpose: purpose
      }).then(response => {
        return response.data;
      });
    },
    addTeamUserToChannel: function(team_id, channel_id, team_user_id){
      return axios.post('/api/v1/teams/AddTeamUserToChannel', {
        team_id: team_id,
        channel_id: channel_id,
        team_user_id: team_user_id
      }).then(response => {
        return response.data;
      });
    },
    removeTeamUserFromChannel: function(team_id, channel_id, team_user_id){
      return axios.post('/api/v1/teams/RemoveUserFromChannel', {
        team_id: team_id,
        channel_id: channel_id,
        team_user_id: team_user_id
      }).then(response => {
        return response.data;
      });
    },
    createChannel: function (team_id, name, purpose) {
      return axios.post('/api/v1/teams/CreateChannel', {
        team_id: team_id,
        name: name,
        purpose: purpose
      }).then(response => {
        return response.data;
      });
    },
    deleteChannel: function (team_id, channel_id){
      return axios.post('/api/v1/teams/DeleteChannel', {
        team_id: team_id,
        channel_id: channel_id
      }).then(response => {
        return response.data;
      });
    }
  }
  ,
  teamUser: {
    createTeamUser: function(team_id, first_name, last_name, email, username, departure_date, role){
      return axios.post('/api/v1/teams/StartTeamUserCreation', {
        team_id: team_id,
        first_name:first_name,
        last_name: last_name,
        email: email,
        username: username,
        departure_date: departure_date,
        role: role,
        arrival_date: new Date().getTime()
      }).then(response => {
        return response.data;
      });
    },
    deleteTeamUser: function(team_id, team_user_id){
      return axios.post('/api/v1/teams/DeleteTeamUser', {
        team_id: team_id,
        team_user_id: team_user_id
      }).then(response => {
        return response.data;
      });
    },
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
    createSingleApp: function(team_id, app){
      return axios.post('/api/v1/teams/CreateShareableSingleApp', {
        team_id: team_id,
        channel_id: app.channel_id,
        team_user_id: app.team_user_id,
        website_id: app.website_id,
        name: app.name,
        description: app.description,
        reminder_interval: app.reminder_interval,
        account_information: app.account_information
      }).then(response => {
        return response.data;
      });
    },
    createMultiApp: function(team_id, app){
      return axios.post('/api/v1/teams/CreateShareableMultiApp', {
        team_id: team_id,
        channel_id: app.channel_id,
        team_user_id: app.team_user_id,
        name: app.name,
        website_id: app.website_id,
        reminder_interval: app.reminder_interval,
        description: app.description
      }).then(response => {
        return response.data;
      });
    },
    createLinkApp: function(team_id, app){
      return axios.post('/api/v1/teams/CreateShareableLinkApp', {
        team_id: team_id,
        channel_id: app.channel_id,
        team_user_id: app.team_user_id,
        name: app.name,
        url: app.url,
        description:app.description
      }).then(response => {
        return response.data;
      });
    },
    shareMultiApp: function(team_id, app_id, user_info){
      return axios.post('/api/v1/teams/ShareApp', {
        team_id: team_id,
        app_id: app_id,
        team_user_id: user_info.user_id,
        account_information: user_info.account_information,
        adminHasAccess: user_info.adminHasAccess
      }).then(response => {
        return response.data;
      });
    },
    shareApp: function(team_id, app_id, user_info){
      return axios.post('/api/v1/teams/ShareApp', {
        team_id: team_id,
        app_id: app_id,
        team_user_id: user_info.team_user_id,
        can_see_information: user_info.can_see_information,
        account_information: user_info.account_information
      }).then(response => {
        return response.data;
      });
    },
    modifyApp: function(team_id, app_id, app_info){
      return axios.post('/api/v1/teams/EditShareableApp', {
        team_id: team_id,
        app_id: app_id,
        name: app_info.name,
        description: app_info.description,
        password_change_interval: app_info.password_change_interval,
        url: app_info.url,
        account_information: app_info.account_information
      }).then(response => {
        return response.data;
      });
    },
    deleteReceiver: function(team_id, app_id, team_user_id){
      return axios.post('/api/v1/teams/DeleteSharedApp', {
        team_id: team_id,
        app_id: app_id,
        team_user_id: team_user_id
      }).then(response => {
        return response.data;
      });
    },
    editReceiver: function(team_id, app_id, receiver_info){
      return axios.post('/api/v1/teams/EditSharedApp', {
        team_id: team_id,
        app_id: app_id,
        can_see_information: receiver_info.can_see_information,
        team_user_id: receiver_info.team_user_id,
        account_information: receiver_info.account_information
      }).then(response => {
        return response.data;
      });
    },
    transferOwnership: function (team_id, app_id, team_user_id) {
      return axios.post('/api/v1/teams/TransferShareableAppOwner', {
        team_id: team_id,
        app_id: app_id,
        team_user_id: team_user_id
      }).then(response => {
        return response.data;
      })
    }
  }
};