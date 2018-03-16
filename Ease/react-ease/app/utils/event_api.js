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
        name:'AppClick',
        data: {
          id: app.id,
          type: app.type,
          sub_type: app.sub_type,
          from: 'dashboard'
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
    updateAccepted: (({type}) => {
      return basic_post('/api/v1/trackEvent', {
        name: 'UpdateAccepted',
        data: {
          type: type
        }
      })
    })
  }
};