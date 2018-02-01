var axios = require('axios');

//axios.defaults.withCredentials = true;

const basic_post = (url, params) => {
  return axios.post(url, params)
      .then(response => {
        return response.data;
      })
      .catch(err => {
        throw err.response.data;
      });
};

module.exports = {
  dashboard: {
    clickOnAppMetric: ({app_id}) => {
      return basic_post('/api/v1/metrics/ClickOnAppMetric', {
        app_id: app_id
      });
    },
    validateTutorial : () => {
      return basic_post('/api/v1/common/TutoDone');
    },
    createProfile : function({name, column_index, ws_id}) {
      return basic_post('/api/v1/dashboard/CreateProfile', {
        name: name,
        column_index: column_index,
        ws_id:ws_id
      });
    },
    editProfile: ({profile_id, name,ws_id}) => {
      return basic_post('/api/v1/dashboard/EditProfile', {
        profile_id: profile_id,
        name: name,
        ws_id:ws_id
      });
    },
    moveProfile: ({profile_id, column_index, position,ws_id}) => {
      return basic_post('/api/v1/dashboard/MoveProfile', {
        profile_id: profile_id,
        column_index: column_index,
        position: position,
        ws_id:ws_id
      });
    },
    deleteProfile: ({profile_id, ws_id}) => {
      return basic_post('/api/v1/dashboard/DeleteProfile', {
        profile_id: profile_id,
        ws_id:ws_id
      })
    },
    deleteApp: ({app_id, ws_id}) => {
      return basic_post('/api/v1/dashboard/DeleteApp', {
        app_id: app_id,
        ws_id:ws_id
      });
    },
    moveApp: ({app_id, profile_id, position, ws_id}) => {
      return basic_post('/api/v1/dashboard/MoveApp', {
        app_id: app_id,
        profile_id: profile_id,
        position: position,
        ws_id:ws_id
      });
    },
    validateApp: ({app_id,ws_id}) => {
      return basic_post('/api/v1/dashboard/ValidateApp', {
        app_id: app_id,
        ws_id:ws_id
      });
    },
    editClassicApp: ({app_id, name, account_information, ws_id}) => {
      Object.keys(account_information).map(item => {
        account_information[item] = cipher(account_information[item]);
      });
      return basic_post('/api/v1/dashboard/EditClassicApp', {
        app_id: app_id,
        name: name,
        account_information: account_information,
        ws_id:ws_id
      });
    },
    editLogWithApp: ({app_id, name, logWithApp_id, ws_id}) => {
      return basic_post('/api/v1/dashboard/EditLogWithApp', {
        app_id: app_id,
        name: name,
        logWithApp_id:logWithApp_id,
        ws_id:ws_id
      });
    },
    editLinkApp: ({app_id, name, url, img_url, ws_id}) => {
      return basic_post('/api/v1/dashboard/EditLinkApp', {
        app_id: app_id,
        name: name,
        url: url,
        img_url: img_url,
        ws_id:ws_id
      })
    },
    editAnyApp: ({app_id, name, url, img_url, account_information, connection_information, ws_id}) => {
      Object.keys(account_information).map(item => {
        account_information[item] = cipher(account_information[item]);
      });
      return basic_post('/api/v1/dashboard/EditAnyApp', {
        app_id: app_id,
        name: name,
        url: url,
        img_url: img_url,
        account_information: account_information,
        connection_information: connection_information,
        ws_id: ws_id
      });
    },
    editSoftwareApp: ({app_id, name, account_information, ws_id}) => {
      Object.keys(account_information).map(item => {
        account_information[item] = cipher(account_information[item]);
      });
      return basic_post('/api/v1/dashboard/EditSoftwareApp', {
        app_id: app_id,
        name: name,
        account_information: account_information,
        ws_id: ws_id
      });
    },
    editAppName: ({app_id, name,ws_id}) => {
      return basic_post('/api/v1/dashboard/EditAppName', {
        app_id: app_id,
        name: name,
        ws_id:ws_id
      });
    },
    editSsoGroup: ({sso_group_id, account_information, ws_id}) => {
      Object.keys(account_information).map(item => {
        account_information[item] = cipher(account_information[item]);
      });
      return basic_post('/api/v1/dashboard/EditSsoGroup', {
        sso_group_id: sso_group_id,
        account_information: account_information,
        ws_id:ws_id
      });
    },
    deleteSsoGroup: ({sso_group_id,ws_id}) => {
      return basic_post('/api/v1/dashboard/DeleteSsoGroup', {
        sso_group_id: sso_group_id,
        ws_id:ws_id
      });
    },
    createSsoGroup: ({sso_id, account_information,ws_id}) => {
      Object.keys(account_information).map(item => {
        account_information[item] = cipher(account_information[item]);
      });
      return basic_post('/api/v1/catalog/CreateSsoGroup', {
        sso_id: sso_id,
        account_information: account_information,
        ws_id:ws_id
      });
    }
  },
  catalog: {
    addSsoApp: ({name, profile_id, sso_group_id, website_id, ws_id}) => {
      return basic_post('/api/v1/catalog/AddSsoApp', {
        name: name,
        profile_id: profile_id,
        sso_group_id: sso_group_id,
        website_id: website_id,
        ws_id: ws_id
      });
    },
    addClassicApp: ({name, website_id, profile_id, account_information, ws_id}) => {
      Object.keys(account_information).map(item => {
        account_information[item] = cipher(account_information[item]);
      });
      return basic_post('/api/v1/catalog/AddClassicApp', {
        name: name,
        website_id: website_id,
        profile_id: profile_id,
        account_information: account_information,
        ws_id: ws_id
      });
    },
    addClassicAppSameAs: ({website_id, name, same_app_id, profile_id, ws_id}) => {
      return basic_post('/api/v1/catalog/AddClassicAppSameAs', {
        website_id: website_id,
        name: name,
        same_app_id: same_app_id,
        profile_id: profile_id,
        ws_id: ws_id
      });
    },
    addMultipleClassicApp: ({profile_id, apps_to_add, account_information, ws_id}) => {
      Object.keys(account_information).map(item => {
        account_information[item] = cipher(account_information[item]);
      });
      return basic_post('/api/v1/catalog/AddMultipleClassicApp', {
        profile_id: profile_id,
        apps_to_add: apps_to_add,
        account_information: account_information,
        ws_id: ws_id
      });
    },
    addBookmark: ({name, profile_id, url, img_url, ws_id}) => {
      return basic_post('/api/v1/catalog/AddBookmark', {
        name: name,
        profile_id: profile_id,
        url: url,
        img_url: img_url,
        ws_id: ws_id
      });
    },
    addLogWithApp: ({name, website_id, profile_id, logWith_app_id, ws_id}) => {
      return basic_post('/api/v1/catalog/AddLogWithApp', {
        name: name,
        website_id: website_id,
        profile_id: profile_id,
        logWith_app_id: logWith_app_id,
        ws_id: ws_id
      });
    },
    requestWebsite: ({url, account_information, ws_id}) => {
      if (account_information !== undefined)
        Object.keys(account_information).map(item => {
          account_information[item] = cipher(account_information[item]);
        });
      return basic_post('/api/v1/catalog/WebsiteRequest', {
        url: url,
        account_information: account_information,
        ws_id: ws_id
      });
    },
    addAnyApp: ({name, url, img_url, profile_id, account_information, connection_information, credentials_provided, ws_id}) => {
      Object.keys(account_information).map(item => {
        account_information[item] = cipher(account_information[item]);
      });
      return basic_post('/api/v1/catalog/AddAnyApp', {
        name: name,
        url: url,
        img_url: img_url,
        profile_id: profile_id,
        account_information: account_information,
        connection_information: connection_information,
        credentials_provided: credentials_provided,
        ws_id: ws_id
      });
    },
    addSoftwareApp: ({name, logo_url, profile_id, account_information, connection_information, ws_id}) => {
      Object.keys(account_information).map(item => {
        account_information[item] = cipher(account_information[item]);
      });
      return basic_post('/api/v1/catalog/AddSoftwareApp', {
        name: name,
        logo_url: logo_url,
        profile_id: profile_id,
        account_information: account_information,
        connection_information: connection_information,
        ws_id: ws_id
      });
    },
    importAccount: ({name, url, account_information, ws_id}) => {
      // Object.keys(account_information).map(item => {
      //   account_information[item] = cipher(account_information[item]);
      // });
      return basic_post('/api/v1/importedAccounts', {
        name: name,
        url: url,
        account_information: account_information,
        ws_id: ws_id
      });
    },
    modifyImportedAccount: ({id, name, url, website_id, account_information, ws_id}) => {
      // Object.keys(account_information).map(item => {
      //   account_information[item] = cipher(account_information[item]);
      // });
      return axios.put('/api/v1/importedAccounts', {
        id: id,
        name: name,
        url: url,
        website_id: website_id,
        account_information: account_information,
        ws_id: ws_id
      }).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
      });
    },
    deleteImportedAccount: ({id, ws_id}) => {
      return axios.delete('/api/v1/importedAccounts', {
        params: {
          id: id,
          ws_id: ws_id
        }
      }).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
      })
    }
  },
  teamChannel: {
    editName : function({ws_id, team_id, room_id, name}){
      return axios.post('/api/v1/teams/EditChannelName', {
        ws_id: ws_id,
        team_id: team_id,
        channel_id: room_id,
        name: name,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
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
    editRoomManager: function ({team_id, channel_id, team_user_id, ws_id}) {
      return axios.post('/api/v1/teams/EditRoomManager', {
        team_id: team_id,
        channel_id: channel_id,
        team_user_id: team_user_id,
        ws_id:ws_id,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
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
    createTeamUser: function(ws_id, team_id, first_name, last_name, email, username, departure_date, role, arrival_date){
      return axios.post('/api/v1/teams/StartTeamUserCreation', {
        ws_id: ws_id,
        team_id: team_id,
        first_name:first_name,
        last_name: last_name,
        email: email,
        username: username,
        departure_date: departure_date,
        arrival_date: arrival_date,
        role: role
      }).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
      });
    },
    sendTeamUserInvitation: function(ws_id, team_id, team_user_id){
      return axios.post('/api/v1/teams/SendTeamUserInvitation', {
        team_id: team_id,
        team_user_id: team_user_id,
        ws_id: ws_id
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
      }).catch(err => {
        throw err.response.data;
      });
    },
    editDepartureDate : function(ws_id, team_id, user_id, departure_date){
      return axios.post('/api/v1/teams/EditTeamUserDepartureDate', {
        ws_id: ws_id,
        team_id: team_id,
        team_user_id: user_id,
        departure_date: departure_date
      }).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
      });
    },
    editArrivalDate: ({ws_id, team_id, team_user_id, arrival_date}) => {
      return basic_post('/api/v1/teams/EditTeamUserArrivalDate', {
        ws_id: ws_id,
        team_id: team_id,
        team_user_id: team_user_id,
        arrival_date: arrival_date
      })
    },
    editFirstLastName: ({team_id, team_user_id, first_name, last_name, ws_id}) => {
      return basic_post('/api/v1/teams/EditTeamUserFirstAndLastName', {
        team_id: team_id,
        team_user_id: team_user_id,
        first_name: first_name,
        last_name: last_name,
        ws_id: ws_id
      })
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
    },
    sendActivationReminderToAdmin: ({team_user_id}) => {
      return basic_post('/api/v1/teams/SendPasswordLostReminderToAdmin', {
        team_user_id: team_user_id
      })
    }
  },
  teamApps: {
    createSingleCard: ({team_id, channel_id, website_id, name, description, password_reminder_interval, team_user_filler_id, account_information, receivers, ws_id}) => {
      Object.keys(account_information).map(item => {
        account_information[item] = cipher(account_information[item]);
      });
      return axios.post('/api/v1/teams/CreateTeamSingleCard', {
        team_id: team_id,
        channel_id: channel_id,
        website_id:website_id,
        name: name,
        password_reminder_interval: password_reminder_interval,
        team_user_filler_id: team_user_filler_id,
        account_information: account_information,
        description: description,
        receivers: receivers,
        ws_id: ws_id
      }).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
      });
    },
    createTeamAnySingleCard: ({team_id, channel_id, name, description, password_reminder_interval, url, img_url, connection_information, team_user_filler_id, account_information, receivers, credentials_provided, ws_id}) => {
      Object.keys(account_information).map(item => {
        account_information[item] = cipher(account_information[item]);
      });
      return basic_post('/api/v1/teams/CreateTeamAnySingleCard', {
        team_id: team_id,
        channel_id: channel_id,
        name: name,
        description: description,
        password_reminder_interval: password_reminder_interval,
        url: url,
        img_url: img_url,
        connection_information: connection_information,
        account_information: account_information,
        team_user_filler_id: team_user_filler_id,
        receivers: receivers,
        credentials_provided: credentials_provided,
        ws_id: ws_id
      });
    },
    createTeamSoftwareSingleCard: ({team_id, channel_id, name, description, password_reminder_interval, logo_url, team_user_filler_id, connection_information, account_information, receivers, ws_id}) => {
      Object.keys(account_information).map(item => {
        account_information[item] = cipher(account_information[item]);
      });
      return basic_post('/api/v1/teams/CreateTeamSoftwareSingleCard', {
        team_id: team_id,
        channel_id: channel_id,
        name: name,
        description: description,
        password_reminder_interval: password_reminder_interval,
        logo_url: logo_url,
        team_user_filler_id: team_user_filler_id,
        connection_information: connection_information,
        account_information: account_information,
        receivers: receivers,
        ws_id: ws_id
      });
    },
    sendSingleCardFillerReminder: ({team_card_id}) => {
      return basic_post('/api/v1/teams/SendFillerReminder', {
        team_card_id: team_card_id
      })
    },
    addTeamSingleCardReceiver: ({team_id, team_card_id, team_user_id, allowed_to_see_password, ws_id}) => {
      return basic_post('/api/v1/teams/AddTeamSingleCardReceiver', {
        team_id: team_id,
        team_card_id: team_card_id,
        team_user_id: team_user_id,
        allowed_to_see_password: allowed_to_see_password,
        ws_id: ws_id
      });
    },
    shareSingleCard: ({team_id, team_card_id, team_user_id, allowed_to_see_password, ws_id}) => {
      return axios.post('/api/v1/teams/AddTeamSingleCardReceiver', {
        team_id: team_id,
        team_card_id: team_card_id,
        team_user_id: team_user_id,
        allowed_to_see_password: allowed_to_see_password,
        ws_id: ws_id
      }).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
      });
    },
    editSingleCard: ({team_id, team_card_id, description, account_information, password_reminder_interval, name, ws_id}) => {
      Object.keys(account_information).map(item => {
        account_information[item] = cipher(account_information[item]);
      });
      return axios.post('/api/v1/teams/EditTeamSingleCard', {
        team_id: team_id,
        team_card_id: team_card_id,
        name: name,
        description: description,
        account_information: account_information,
        password_reminder_interval: password_reminder_interval,
        ws_id: ws_id
      }).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
      });
    },
    editAnySingleCard: ({team_card_id, description, connection_information, account_information, password_reminder_interval, url, img_url, name, ws_id}) => {
      Object.keys(account_information).map(item => {
        account_information[item] = cipher(account_information[item]);
      });
      return basic_post('/api/v1/teams/EditTeamAnySingleCard', {
        team_card_id: team_card_id,
        name: name,
        description: description,
        password_reminder_interval: password_reminder_interval,
        url: url,
        img_url: img_url,
        connection_information: connection_information,
        account_information: account_information,
        ws_id: ws_id
      });
    },
    editSoftwareSingleCard: ({team_card_id, description, account_information, connection_information, password_reminder_interval, name, ws_id}) => {
      Object.keys(account_information).map(item => {
        account_information[item] = cipher(account_information[item]);
      });
      return basic_post('/api/v1/teams/EditTeamSoftwareSingleCard', {
        team_card_id: team_card_id,
        name: name,
        description: description,
        account_information: account_information,
        connection_information: connection_information,
        password_reminder_interval: password_reminder_interval,
        ws_id: ws_id
      });
    },
    editSingleCardReceiver: ({team_id, team_card_id, team_card_receiver_id, allowed_to_see_password, ws_id}) => {
      return axios.post('/api/v1/teams/EditTeamSingleCardReceiver', {
        team_id: team_id,
        team_card_id: team_card_id,
        team_card_receiver_id: team_card_receiver_id,
        allowed_to_see_password: allowed_to_see_password,
        ws_id: ws_id
      }).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
      });
    },
    createEnterpriseCard: ({team_id, channel_id, website_id, name, description, password_reminder_interval, receivers, ws_id}) => {
      Object.keys(receivers).map(receiver => {
        Object.keys(receivers[receiver].account_information).map(item => {
          receivers[receiver].account_information[item] = cipher(receivers[receiver].account_information[item]);
        });
        return receivers[receiver];
      });
      return basic_post('/api/v1/teams/CreateTeamEnterpriseCard', {
        team_id: team_id,
        channel_id: channel_id,
        website_id: website_id,
        name: name,
        description: description,
        password_reminder_interval: password_reminder_interval,
        receivers: receivers,
        ws_id: ws_id
      });
    },
    createAnyEnterpriseCard: ({team_id, channel_id, name, description, password_reminder_interval, url, img_url, connection_information, receivers, ws_id}) => {
      Object.keys(receivers).map(receiver => {
        Object.keys(receivers[receiver].account_information).map(item => {
          receivers[receiver].account_information[item] = cipher(receivers[receiver].account_information[item]);
        });
        return receivers[receiver];
      });
      return basic_post('/api/v1/teams/CreateTeamAnyEnterpriseCard', {
        team_id: team_id,
        channel_id: channel_id,
        name: name,
        description: description,
        password_reminder_interval: password_reminder_interval,
        url: url,
        img_url: img_url,
        connection_information: connection_information,
        receivers: receivers,
        ws_id: ws_id
      });
    },
    createSoftwareEnterpriseCard: ({team_id, channel_id, name, description, password_reminder_interval, logo_url, connection_information, receivers, ws_id}) => {
      Object.keys(receivers).map(receiver => {
        Object.keys(receivers[receiver].account_information).map(item => {
          receivers[receiver].account_information[item] = cipher(receivers[receiver].account_information[item]);
        });
        return receivers[receiver];
      });
      return basic_post('/api/v1/teams/CreateTeamSoftwareEnterpriseCard', {
        team_id: team_id,
        channel_id: channel_id,
        name: name,
        description: description,
        password_reminder_interval: password_reminder_interval,
        logo_url: logo_url,
        receivers: receivers,
        connection_information: connection_information,
        ws_id: ws_id
      });
    },
    editEnterpriseCard: ({team_id, team_card_id, name, description, password_reminder_interval, ws_id}) => {
      return basic_post('/api/v1/teams/EditTeamEnterpriseCard', {
        team_id: team_id,
        team_card_id: team_card_id,
        name: name,
        description: description,
        password_reminder_interval: password_reminder_interval,
        ws_id: ws_id
      });
    },
    editAnyEnterpriseCard: ({team_card_id, name, description, password_reminder_interval, url, img_url, connection_information, ws_id}) => {
      return basic_post('/api/v1/teams/EditTeamAnyEnterpriseCard', {
        team_card_id: team_card_id,
        name: name,
        description: description,
        password_reminder_interval: password_reminder_interval,
        url: url,
        img_url: img_url,
        connection_information: connection_information,
        ws_id: ws_id
      });
    },
    editSoftwareEnterpriseCard: ({team_card_id, name, description, password_reminder_interval, ws_id}) => {
      return basic_post('/api/v1/teams/EditTeamSoftwareEnterpriseCard', {
        team_card_id: team_card_id,
        name: name,
        description: description,
        password_reminder_interval: password_reminder_interval,
        ws_id: ws_id
      });
    },
    shareEnterpriseCard: ({team_id, team_card_id, team_user_id, account_information, ws_id}) => {
      Object.keys(account_information).map(item => {
        account_information[item] = cipher(account_information[item]);
      });
      return basic_post('/api/v1/teams/AddTeamEnterpriseCardReceiver', {
        team_id: team_id,
        team_card_id: team_card_id,
        team_user_id: team_user_id,
        account_information: account_information,
        ws_id: ws_id
      });
    },
    joinEnterpriseApp : ({team_id, app_id, account_information, ws_id}) => {
      Object.keys(account_information).map(item => {
        account_information[item] = cipher(account_information[item]);
      });
      return axios.post('/api/v1/teams/JoinTeamEnterpriseCard', {
        team_id: team_id,
        team_card_id: app_id,
        account_information: account_information,
        ws_id: ws_id
      }).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
      });
    },
    acceptEnterpriseCard: ({team_id, shared_app_id, account_information, ws_id}) => {
      Object.keys(account_information).map(item => {
        account_information[item] = cipher(account_information[item]);
      });
      return axios.post('/api/v1/teams/AcceptEnterpriseApp', {
        team_id: team_id,
        shared_app_id: shared_app_id,
        account_information: account_information,
        ws_id: ws_id,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
      });
    },
    editEnterpriseCardReceiver: ({team_id, team_card_id, team_card_receiver_id, account_information, ws_id}) => {
      Object.keys(account_information).map(item => {
        account_information[item] = cipher(account_information[item]);
      });
      return basic_post('/api/v1/teams/EditTeamEnterpriseCardReceiver', {
        team_id: team_id,
        team_card_id: team_card_id,
        team_card_receiver_id: team_card_receiver_id,
        account_information: account_information,
        ws_id: ws_id
      });
    },
    createLinkCard: ({team_id, channel_id, name, description, url, img_url, ws_id, receivers}) => {
      return axios.post('/api/v1/teams/CreateTeamLinkCard', {
        team_id: team_id,
        channel_id: channel_id,
        name : name,
        description: description,
        url: url,
        img_url: img_url,
        ws_id: ws_id,
        receivers: receivers
      }).then(response => {
        return response.data;
      }).catch(err => {
        return err.response.data;
      });
    },
    addTeamLinkCardReceiver: ({team_card_id, team_user_id, ws_id}) => {
      return basic_post('/api/v1/teams/AddTeamLinkCardReceiver', {
        team_card_id: team_card_id,
        team_user_id: team_user_id,
        ws_id: ws_id
      });
    },
    editLinkCard: ({team_card_id, name, description, url, img_url, ws_id}) => {
      return basic_post('/api/v1/teams/EditTeamLinkCard', {
        team_card_id: team_card_id,
        name : name,
        description: description,
        url: url,
        img_url: img_url,
        ws_id: ws_id
      });
    },
    pinLinkApp : ({team_id, app_id, app_name, profile_id, ws_id}) => {
      return axios.post('/api/v1/teams/PinLinkApp', {
        team_id: team_id,
        app_id: app_id,
        app_name:app_name,
        profile_id: profile_id,
        ws_id: ws_id,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      }).catch(err => {
        return err.response.data;
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
    deleteCard: function ({team_id, team_card_id, ws_id}) {
      return basic_post('/api/v1/teams/DeleteTeamCard', {
        team_id: team_id,
        team_card_id: team_card_id,
        ws_id: ws_id
      });
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
    deleteReceiver: function({ws_id, team_id, app_id, shared_app_id}){
      return axios.post('/api/v1/teams/DeleteSharedApp', {
        ws_id: ws_id,
        team_id: team_id,
        app_id: app_id,
        shared_app_id: shared_app_id,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      });
    },
    removeTeamCardReceiver: ({team_id, team_card_id, team_card_receiver_id, ws_id}) => {
      return basic_post('/api/v1/teams/RemoveTeamCardReceiver', {
        team_id: team_id,
        team_card_id: team_card_id,
        team_card_receiver_id: team_card_receiver_id,
        ws_id: ws_id
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
    acceptSharedApp: function({ws_id, team_id, shared_app_id}){
      return axios.post('/api/v1/teams/AcceptJoinTeamCard', {
        ws_id: ws_id,
        team_id: team_id,
        team_card_id: shared_app_id
      }).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
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
    requestTeamSingleCard : function({ws_id, team_id, team_card_id}){
      return basic_post('/api/v1/teams/JoinTeamSingleCard', {
        ws_id: ws_id,
        team_id: team_id,
        team_card_id: team_card_id
      });
    },
    acceptTeamCardRequest: ({team_id, team_card_id, request_id, ws_id}) => {
      return basic_post('/api/v1/teams/AcceptJoinTeamCard', {
        team_id: team_id,
        team_card_id: team_card_id,
        request_id: request_id,
        ws_id: ws_id
      });
    },
    deleteTeamCardRequest: ({team_id, team_card_id, request_id, ws_id}) => {
      return basic_post('/api/v1/teams/DeleteJoinTeamCard', {
        team_id: team_id,
        team_card_id: team_card_id,
        request_id: request_id,
        ws_id: ws_id
      });
    },
    requestTeamEnterpriseCard : function({ws_id, team_id, team_card_id, account_information}){
      return basic_post('/api/v1/teams/JoinTeamEnterpriseCard', {
        ws_id: ws_id,
        team_id: team_id,
        team_card_id: team_card_id,
        account_information: account_information
      });
    },
    askJoinApp: function(ws_id, team_id, app_id){
      return axios.post('/api/v1/teams/AskJoinApp', {
        ws_id: ws_id,
        team_id: team_id,
        team_card_id: app_id
      }).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
      })
    },
    deleteJoinAppRequest: function(ws_id, team_id, app_id, team_user_id){
      return axios.post('/api/v1/teams/DeleteJoinTeamCard', {
        ws_id: ws_id,
        team_id: team_id,
        team_card_id: app_id,
        team_user_id: team_user_id
      }).then(r => {
        return r.data;
      }).catch(err => {
        throw err.response.data;
      })
    }
  },
  teams: {
    editTeamName : function({team_id, name, ws_id}){
      return axios.post('/api/v1/teams/EditTeamName', {
        team_id: team_id,
        name: name,
        ws_id: ws_id
      }).then(r => {
        return (r.data);
      }).catch(err => {
        throw err.response.data;
      })
    },
    createTeam: function({name, email, first_name, last_name, username, jobRole, jobDetails, digits,plan_id, ws_id}){
      return axios.post('/api/v1/teams/CreateTeam', {
        team_name: name,
        email: email,
        first_name: first_name,
        last_name: last_name,
        username: username,
        job_index: jobRole,
        job_details: jobDetails,
        digits: digits,
        plan_id: plan_id,
        ws_id:ws_id,
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
    addCreditCard: function({team_id, cardToken,ws_id}) {
      return axios.post('/api/v1/teams/AddCreditCard', {
        team_id: team_id,
        token: cardToken,
        ws_id:ws_id
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
    unsubscribe : function({team_id, password,ws_id}){
      return axios.post('/api/v1/teams/Unsubscribe', {
        team_id: team_id,
        password: password,
        ws_id: ws_id
      }).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
      });
    },
    upgradePlan: ({team_id, plan_id, ws_id}) => {
      return axios.post('/api/v1/teams/UpgradePlan', {
        team_id: team_id,
        plan_id: plan_id,
        ws_id:ws_id
      }).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
      });
    },
    askOwnerForBilling: ({team_id}) => {
      return axios.post('/api/v1/teams/AskOwnerForBilling', {
        team_id: team_id,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
      });
    },
    askOwnerToUpgrade: ({team_id}) => {
      return axios.post('/api/v1/teams/AskOwnerToUpgrade', {
        team_id: team_id,
        timestamp: new Date().getTime()
      }).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
      });
    }
  },
  common : {
    connect : function(email, password){
      return basic_post('/api/v1/common/Connection', {
        email: email,
        password: cipher(password)
      });
    },
    askEditEmail : function(password, new_email){
      return axios.post('/api/v1/common/AskEditEmail', {
            password: cipher(password),
            new_email: new_email
          }
      ).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
      })
    },
    passwordLost: ({email}) => {
      return basic_post('/passwordLost', {
        email: email
      });
    },
    renewPassword: ({email, code, password}) => {
      return basic_post('/api/v1/common/ResetPassword', {
        email: email,
        code: code,
        password: cipher(password)
      });
    },
    editEmail : function(password, new_email, digits){
      return axios.post('/api/v1/common/EditEmail', {
            password: cipher(password),
            new_email: new_email,
            digits: digits
          }
      ).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
      })
    },
    editPersonalUsername : function({username}){
      return basic_post('/api/v1/common/EditUsername', {
        username: username
      });
    },
    checkPassword : function(password){
      return axios.post('/api/v1/common/CheckPassword', {
            password: cipher(password)
          }
      ).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
      })
    },
    editPassword : function(password, new_password){
      return axios.post('/api/v1/common/EditPassword', {
            password: cipher(password),
            new_password: cipher(new_password)
          }
      ).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
      })
    },
    setBackgroundPicture : function({active}){
      return axios.post('/api/v1/common/SetBackgroundPicture', {
            active: active
          }
      ).then(response => {
        return response.data;
      }).catch(err => {
        throw err.response.data;
      })
    },
    deleteAccount : function(password){
      return axios.post('/api/v1/common/DeleteAccount', {
            password: cipher(password)
          }
      ).then(response => {
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
    },
    newFeatureSeen: function(){
      return axios.post('/api/v1/common/NewFeatureSeen', {})
          .then(response => {
            return response.data;
          }).catch(err => {
            throw err;
          })
    },
    tipDone: ({name}) => {
      return basic_post('/api/v1/common/TipDone', {
        name: name
      })
    }
  }
};