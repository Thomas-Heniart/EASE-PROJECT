var axios = require('axios');

module.exports = {
  teamChannel: {
    editName : function(ws_id, team_id, channel_id, name){
      return axios.post('/api/v1/teams/EditChannelName', {
        ws_id: ws_id,
        team_id: team_id,
        channel_id: channel_id,
        name: name,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      });
    },
    editPurpose: function(ws_id, team_id, channel_id, purpose){
      return axios.post('/api/v1/teams/EditChannelPurpose',{
        ws_id: ws_id,
        team_id: team_id,
        channel_id: channel_id,
        purpose: purpose,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      });
    },
    addTeamUserToChannel: function(ws_id, team_id, channel_id, team_user_id){
      return axios.post('/api/v1/teams/AddTeamUserToChannel', {
        ws_id: ws_id,
        team_id: team_id,
        channel_id: channel_id,
        team_user_id: team_user_id,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      });
    },
    removeTeamUserFromChannel: function(ws_id, team_id, channel_id, team_user_id){
      return axios.post('/api/v1/teams/RemoveUserFromChannel', {
        ws_id: ws_id,
        team_id: team_id,
        channel_id: channel_id,
        team_user_id: team_user_id,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
      });
    },
    createChannel: function (ws_id, team_id, name, purpose) {
      return axios.post('/api/v1/teams/CreateChannel', {
        ws_id: ws_id,
        team_id: team_id,
        name: name,
        purpose: purpose,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
      })
    },
    deleteChannel: function (ws_id, team_id, channel_id){
      return axios.post('/api/v1/teams/DeleteChannel', {
        ws_id: ws_id,
        team_id: team_id,
        channel_id: channel_id,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
      });
    },
    askJoinChannel: function (ws_id, team_id, channel_id){
      return axios.post('/api/v1/teams/AskJoinChannel', {
        ws_id: ws_id,
        team_id: team_id,
        channel_id: channel_id,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      })
    },
    deleteJoinChannelRequest: function(ws_id, team_id, channel_id, team_user_id){
      return axios.post('/api/v1/teams/DeleteJoinChannelRequest', {
        ws_id: ws_id,
        team_id: team_id,
        channel_id: channel_id,
        team_user_id: team_user_id,
        timestamp: new Date().getTime()
      }).then(r => {
          return r.data;
      }).catch(err => {
        throw err.response.data;
      })
    }
  },
  teamUser: {
    createTeamUser: function(ws_id, team_id, first_name, last_name, email, username, departure_date, role){
      return axios.post('/api/v1/teams/StartTeamUserCreation', {
        ws_id: ws_id,
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
      }).catch(err => {
        throw err.response.data;
      });
    },
    deleteTeamUser: function(ws_id, team_id, team_user_id){
      return axios.post('/api/v1/teams/DeleteTeamUser', {
        ws_id: ws_id,
        team_id: team_id,
        team_user_id: team_user_id,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
      });
    },
    verifyTeamUser: function(ws_id, team_id, team_user_id){
      return axios.post('/api/v1/teams/VerifyTeamUser', {
        ws_id: ws_id,
        team_id: team_id,
        team_user_id: team_user_id,
        timestamp: new Date().getTime()
      }).then(r => {
        return r.data;
      }).catch(err => {
        throw err.response.data;
      })
    },
    reactivateTeamUser: function ({team_id, team_user_id, ws_id}) {
      return axios.post('/api/v1/teams/ReactivateTeamUser', {
        team_id: team_id,
        ws_id: ws_id,
        team_user_id: team_user_id,
        timestamp: new Date().getTime()
      }).then(r => {
        return r.data;
      }).catch(err => {
        throw err.response.data;
      });
    },
    editRole : function(ws_id, team_id, user_id, role){
      return axios.post('/api/v1/teams/EditTeamUserRole', {
        ws_id: ws_id,
        team_id: team_id,
        team_user_id: user_id,
        role: role,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      });
    },
    editDepartureDate : function(ws_id, team_id, user_id, departure_date){
      return axios.post('/api/v1/teams/EditTeamUserDepartureDate', {
        ws_id: ws_id,
        team_id: team_id,
        team_user_id: user_id,
        departure_date: departure_date,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      });
    },
    editFirstName : function(ws_id, team_id, user_id, first_name){
      return axios.post('/api/v1/teams/EditTeamUserFirstName', {
        ws_id: ws_id,
        team_id: team_id,
        team_user_id: user_id,
        first_name: first_name,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      });
    },
    editLastName : function(ws_id, team_id, user_id, last_name){
      return axios.post('/api/v1/teams/EditTeamUserLastName', {
        ws_id: ws_id,
        team_id: team_id,
        team_user_id: user_id,
        last_name: last_name,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      });
    },
    editUsername : function(ws_id, team_id, user_id, username){
      return axios.post('/api/v1/teams/EditTeamUserUsername', {
        ws_id: ws_id,
        team_id: team_id,
        team_user_id: user_id,
        username: username,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      });
    },
    transferTeamOwnership : function (ws_id, team_id, password, team_user_id){
      return axios.post('/api/v1/teams/TransferOwnership', {
        ws_id: ws_id,
        team_id: team_id,
        password: password,
        team_user_id: team_user_id,
        timestamp: new Date().getTime()
      }).then(r => {
        return r.data;
      }).catch(err => {
        throw err.response.data;
      });
    },
    editPhoneNumber: function (ws_id, team_id, team_user_id, phone){
      return axios.post('/api/v1/teams/EditTeamUserPhoneNumber', {
        ws_id: ws_id,
        team_id: team_id,
        team_user_id: team_user_id,
        phone_number: phone,
        timestamp: new Date().getTime()
      }).then(r => {
        return r.data;
      }).catch(err => {
        throw err.response.data;
      })
    }
  },
  teamApps: {
    createSingleApp: function(ws_id, team_id, app){
      return axios.post('/api/v1/teams/CreateShareableSingleApp', {
        ws_id: ws_id,
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
    createMultiApp: function(ws_id, team_id, app){
      return axios.post('/api/v1/teams/CreateShareableMultiApp', {
        ws_id: ws_id,
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
    createLinkApp: function(ws_id, team_id, app){
      return axios.post('/api/v1/teams/CreateShareableLinkApp', {
        ws_id: ws_id,
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
    deleteApp: function (ws_id, team_id, app_id) {
      return axios.post('/api/v1/teams/DeleteShareableApp', {
        ws_id: ws_id,
        team_id: team_id,
        app_id: app_id,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      })
    },
    shareMultiApp: function(ws_id, team_id, app_id, user_info){
      return axios.post('/api/v1/teams/ShareApp', {
        ws_id: ws_id,
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
    shareApp: function(ws_id, team_id, app_id, user_info){
      return axios.post('/api/v1/teams/ShareApp', {
        ws_id: ws_id,
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
    modifyApp: function(ws_id, team_id, app_id, app_info){
      return axios.post('/api/v1/teams/EditShareableApp', {
        ws_id: ws_id,
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
    deleteReceiver: function(ws_id, team_id, app_id, team_user_id){
      return axios.post('/api/v1/teams/DeleteSharedApp', {
        ws_id: ws_id,
        team_id: team_id,
        app_id: app_id,
        team_user_id: team_user_id,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      });
    },
    editReceiver: function(ws_id, team_id, app_id, receiver_info){
      return axios.post('/api/v1/teams/EditSharedApp', {
        ws_id: ws_id,
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
    acceptSharedApp: function(ws_id, team_id, shared_app_id){
      return axios.post('/api/v1/teams/AcceptSharedApp', {
        ws_id: ws_id,
        team_id: team_id,
        shared_app_id: shared_app_id,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      });
    },
    transferOwnership: function (ws_id, team_id, app_id, team_user_id) {
      return axios.post('/api/v1/teams/TransferShareableAppOwner', {
        ws_id: ws_id,
        team_id: team_id,
        app_id: app_id,
        team_user_id: team_user_id,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      })
    },
    pinToDashboard: function(ws_id, team_id, shared_app_id, profile_id, app_name){
      return axios.post('/api/v1/teams/PinAppToDashboard', {
        ws_id: ws_id,
        team_id: team_id,
        shared_app_id: shared_app_id,
        profile_id: profile_id,
        app_name: app_name,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      })
    },
    askJoinApp: function(ws_id, team_id, app_id){
      return axios.post('/api/v1/teams/AskJoinApp', {
        ws_id: ws_id,
        team_id: team_id,
        app_id: app_id,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
      })
    },
    deleteJoinAppRequest: function(ws_id, team_id, app_id, team_user_id){
      return axios.post('/api/v1/teams/DeleteJoinAppRequest', {
        ws_id: ws_id,
        team_id: team_id,
        app_id: app_id,
        team_user_id: team_user_id,
        timestamp: new Date().getTime()
      }).then(r => {
        return r.data;
      }).catch(err => {
        throw err.response.data;
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
        job_index: jobRole,
        job_details: jobDetails,
        digits: digits,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
      })
    },
    finalizeRegistration: function(ws_id, fname, lname, username, jobRole, jobDetails, code){
      return axios.post('/api/v1/teams/FinalizeRegistration', {
        ws_id: ws_id,
        first_name: fname,
        last_name: lname,
        username: username,
        job_index: jobRole,
        job_details: jobDetails,
        code: code,
        timestamp: new Date().getTime()
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
        throw err.response.data;
      })
    },
    validateTutorial : function(){
      return axios.post('/api/v1/teams/TutoDone', {
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
      })
    },
    addCreditCard: function({team_id, cardToken}) {
      return axios.post('/api/v1/teams/AddCreditCard', {
        team_id: team_id,
        token: cardToken
      }).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
      });
    },
    updateBillingInformation: function({team_id, address_city, address_country, address_line1, address_line2, address_state, address_zip, business_vat_id}){
      return axios.post('/api/v1/teams/UpdateBillingInformation', {
        team_id: team_id,
        address_city: address_city,
        address_country: address_country,
        address_line1: address_line1,
        address_line2: address_line2,
        address_state: address_state,
        address_zip: address_zip,
        business_vat_id: business_vat_id
      }).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
      });
    },
    unsubscribe : function({team_id, password}){
      return axios.post('/api/v1/teams/Unsubscribe', {
        team_id: team_id,
        password: password
      }).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
      });
    }
  },
  dashboard: {
    createProfile : function({name}){
      return axios.post('/api/v1/dashboard/CreateProfile', {
        name: name
      }).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
      });
    }
  },
  common : {
    connect : function(email, password){
      return axios.post('/api/v1/common/Connection', {
        email: email,
          password: cipher(password)
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
    registration: function(email, username, password, digits, code, newsletter){
      return axios.post('/api/v1/common/Registration', {
        email: email,
        username: username,
        password: password,
        digits: digits,
        code: code,
        newsletter: newsletter,
        registration_date: new Date().getTime()
      }).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
      })
    },
    validateNotifications: function () {
      return axios.post('/api/v1/common/ValidateNotifications', {}).then(r => {
        return r.data;
      }).catch(err => {
        throw err;
      })
    },
    requestWebsite: function({team_id, url, is_public, login, password}){
      return axios.post('/api/v1/teams/AskWebsite', {
        team_id: team_id,
        url:url,
        is_public: is_public,
          login: cipher(login),
          password: cipher(password),
        timestamp: new Date().getTime()
      }).then(r => {
        return r.data;
      }).catch(err => {
        throw err.response.data;
      });
    }
  }
};