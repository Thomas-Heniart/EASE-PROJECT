var axios = require('axios');

//axios.defaults.withCredentials = true;

const basic_delete = (url, params) => {
  return axios.delete(url, params)
    .then(response => {
      return response.data;
    })
    .catch(err => {
      throw err.response.data;
    });
};

module.exports = {
  catalog: {
    deleteUpdate: ({id, ws_id}) => {
      return basic_delete('/api/v1/updates', {
        params: {
          id: id,
          ws_id: ws_id
        }
      })
    }
  }
};