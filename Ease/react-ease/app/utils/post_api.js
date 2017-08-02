var axios = require('axios');

module.exports = {
  teamChannel: {
    editName : function(team_id, channel_id, name){
      return axios.post('/api/v1/teams/EditChannelName', {
        team_id: team_id,
        channel_id: channel_id,
        name: name,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      });
    },
    editPurpose: function(team_id, channel_id, purpose){
      return axios.post('/api/v1/teams/EditChannelPurpose',{
        team_id: team_id,
        channel_id: channel_id,
        purpose: purpose,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      });
    },
    addTeamUserToChannel: function(team_id, channel_id, team_user_id){
      return axios.post('/api/v1/teams/AddTeamUserToChannel', {
        team_id: team_id,
        channel_id: channel_id,
        team_user_id: team_user_id,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      });
    },
    removeTeamUserFromChannel: function(team_id, channel_id, team_user_id){
      return axios.post('/api/v1/teams/RemoveUserFromChannel', {
        team_id: team_id,
        channel_id: channel_id,
        team_user_id: team_user_id,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      });
    },
    createChannel: function (team_id, name, purpose) {
      return axios.post('/api/v1/teams/CreateChannel', {
        team_id: team_id,
        name: name,
        purpose: purpose,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      });
    },
    deleteChannel: function (team_id, channel_id){
      return axios.post('/api/v1/teams/DeleteChannel', {
        team_id: team_id,
        channel_id: channel_id,
        timestamp: new Date().getTime()
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
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      });
    },
    deleteTeamUser: function(team_id, team_user_id){
      return axios.post('/api/v1/teams/DeleteTeamUser', {
        team_id: team_id,
        team_user_id: team_user_id,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      });
    },
    editRole : function(team_id, user_id, role){
      return axios.post('/api/v1/teams/EditTeamUserRole', {
        team_id: team_id,
        team_user_id: user_id,
        role: role,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      });
    },
    editDepartureDate : function(team_id, user_id, departure_date){
      return axios.post('/api/v1/teams/EditTeamUserDepartureDate', {
        team_id: team_id,
        team_user_id: user_id,
        departure_date: departure_date,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      });
    },
    editFirstName : function(team_id, user_id, first_name){
      return axios.post('/api/v1/teams/EditTeamUserFirstName', {
        team_id: team_id,
        team_user_id: user_id,
        first_name: first_name,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      });
    },
    editLastName : function(team_id, user_id, last_name){
      return axios.post('/api/v1/teams/EditTeamUserLastName', {
        team_id: team_id,
        team_user_id: user_id,
        last_name: last_name,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      });
    },
    editUsername : function(team_id, user_id, username){
      return axios.post('/api/v1/teams/EditTeamUserUsername', {
        team_id: team_id,
        team_user_id: user_id,
        username: username,
        timestamp: new Date().getTime()
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
        account_information: app.account_information,
        timestamp: new Date().getTime()
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
        description: app.description,
        timestamp: new Date().getTime()
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
        description:app.description,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      });
    },
    deleteApp: function (team_id, app_id) {
      return axios.post('/api/v1/teams/DeleteShareableApp', {
        team_id: team_id,
        app_id: app_id,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      })
    },
    shareMultiApp: function(team_id, app_id, user_info){
      return axios.post('/api/v1/teams/ShareApp', {
        team_id: team_id,
        app_id: app_id,
        team_user_id: user_info.user_id,
        account_information: user_info.account_information,
        adminHasAccess: user_info.adminHasAccess,
        timestamp: new Date().getTime()
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
        account_information: user_info.account_information,
        timestamp: new Date().getTime()
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
        account_information: app_info.account_information,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      });
    },
    deleteReceiver: function(team_id, app_id, team_user_id){
      return axios.post('/api/v1/teams/DeleteSharedApp', {
        team_id: team_id,
        app_id: app_id,
        team_user_id: team_user_id,
        timestamp: new Date().getTime()
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
        account_information: receiver_info.account_information,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      });
    },
    acceptSharedApp: function(team_id, shared_app_id){
      return axios.post('/api/v1/teams/AcceptSharedApp', {
        team_id: team_id,
        shared_app_id: shared_app_id,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      });
    },
    transferOwnership: function (team_id, app_id, team_user_id) {
      return axios.post('/api/v1/teams/TransferShareableAppOwner', {
        team_id: team_id,
        app_id: app_id,
        team_user_id: team_user_id,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      })
    },
    pinToDashboard: function(team_id, shared_app_id, profile_id, app_name){
      return axios.post('/api/v1/teams/PinAppToDashboard', {
        team_id: team_id,
        shared_app_id: shared_app_id,
        profile_id: profile_id,
        app_name: app_name,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      })
    }
  },
  teams: {
    editTeamName : function(team_id, name){
      return axios.post('/api/v1/teams/EditTeamName', {
        team_id: team_id,
        name: name,
        timestamp: new Date().getTime()
      }).then(r => {
        return (r.data);
      }).catch(err => {
        throw err.response.data;
      })
    },
    createTeam: function(name, email, first_name, last_name, username, jobRole, jobDetails, digits){
      return axios.post('/api/v1/teams/CreateTeam', {
        team_name: name,
        email: email,
        first_name: first_name,
        last_name: last_name,
        username: username,
        jobIndex: jobRole,
        jobDetails: jobDetails,
        digits: digits,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
      })
    },
    finalizeRegistration: function(fname, lname, username, jobRole, jobDetails, code){
      return axios.post('/api/v1/teams/FinalizeRegistration', {
        first_name: fname,
        last_name: lname,
        username: username,
        job_index: jobRole,
        job_details: jobDetails,
        code: code
      }).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
      });
    },
    askTeamCreation: function(email){
      return axios.post('/api/v1/teams/AskTeamCreation', {
        email: email,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
      })
    },
    checkTeamCreationDigits(email, digits){
      return axios.post('/api/v1/teams/CheckTeamCreationDigits', {
        email: email,
        digits: digits
      }).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
      })
    },
    inviteFriends: function (team_id, email1, email2,email3) {
      return axios.post('/api/v1/teams/InvitePeople', {
        team_id:team_id,
        email1: email1,
        email2: email2,
        email3: email3,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
      })
    },
    subscribeToPlan: function(teamId, stripeToken, vat_id, name, address_line1, address_line2, address_zip, address_state, address_country, address_city){
      return axios.post('/api/v1/teams/SubscribeToMonthPlan', {
        team_id: teamId,
        token: stripeToken,
        vat_id: vat_id,
        name: name,
        address_line1: address_line1,
        address_line2:address_line2,
        address_zip:address_zip,
        address_state:address_state,
        address_country:address_country,
        address_city: address_city,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      }).catch(err => {
        throw err;
      })
    }
  },
  common : {
    connect : function(email, password){
      return axios.post('/api/v1/common/Connection', {
        email: email,
        password: password
      }).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
      })
    },
    askRegistration: function(email){
      return axios.post('/api/v1/common/AskRegistration', {
        email: email
      }).then (response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
      })
    },
    checkRegistrationDigits: function(email, digits){
      return axios.post('/api/v1/common/CheckRegistrationDigits', {
        email: email,
        digits: digits
      }).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
      })
    },
    registration: function(email, username, password, digits, newsletter){
      return axios.post('/api/v1/common/Registration', {
        email: email,
        username: username,
        password: password,
        digits: digits,
        newsletter: newsletter,
        registration_date: new Date().getTime()
      }).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
      })
    }
  }
};