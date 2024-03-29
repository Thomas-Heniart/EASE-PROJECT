const axios = require('axios');

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
  track: {
    appClick: (({app}) => {
      return basic_post('/api/v1/trackEvent', {
        name:'PasswordUsed',
        data: {
          id: app.id,
          type: app.type,
          sub_type: app.sub_type,
          from: 'DashboardClick'
        }
      })
    }),
    appAdded: (({app, from}) => {
      return basic_post('/api/v1/trackEvent', {
        name: 'AppAdded',
        data: {
          id: app.id,
          type: app.type,
          sub_type: app.sub_type,
          from: from
        }
      })
    }),
    cardAdded: (({card, from}) => {
      return basic_post('/api/v1/trackEvent', {
        name: 'CardAdded',
        data: {
          id: card.id,
          type: card.type,
          filled: !card.empty,
          ask_someone: card.team_user_filler_id !== -1,
          ask_magic_link: card.magic_link !== '',
          from: from
        }
      })
    }),
    updateAccepted: (({type}) => {
      return basic_post('/api/v1/trackEvent', {
        name: 'UpdateAccepted',
        data: {
          type: type
        }
      })
    }),
    passwordCopied: (({app}) => {
      return basic_post('/api/v1/trackEvent', {
        name: 'PasswordUsed',
        data: {
          id: app.id,
          type: app.type,
          sub_type: app.sub_type,
          from: 'CopyFromDashboard'
        }
      })
    })
  }
};