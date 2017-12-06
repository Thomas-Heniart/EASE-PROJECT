import axios from "axios";

//axios.defaults.baseURL = 'https://ease.space/api/rest';
axios.defaults.baseURL = 'https://192.162.0.19:8443/api/rest';

const basic_get = (url, params) => {
  return axios.get(url, {params: params})
      .then(response => {
        return response.data;
      })
      .catch(err => {
        throw err.response.data;
      });
};

const basic_post = (url, params) => {
  return axios.post(url, params)
      .then(response => {
        console.log(response.data);
        return response.data;
      })
      .catch(err => {
        console.log(err.response.data);
        throw err.response.data;
      });
};

export default api = {
  post: {
    connection: ({email,password}) => {
      return basic_post('/Connection', {
        email:email,
        password:password,
        timestamp: new Date().getTime()
      });
    }
  },
  get: {
    getPersonalAndTeamSpace: () => {
      return basic_get('/GetPersonalAndTeamSpace');
    },
    getTeamRoomApps: ({team_id, room_id}) => {
      return basic_get('/GetRoomApps', {
        team_id: team_id,
        room_id: room_id
      });
    },
    getGroupApps: ({group_id}) => {
      return basic_get('/GetGroupApps', {
        group_id: group_id
      });
    }
  }
};