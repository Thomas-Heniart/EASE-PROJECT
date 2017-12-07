import axios from "axios";

//axios.defaults.baseURL = 'https://ease.space/';
axios.defaults.baseURL = 'http://192.168.0.19:8080/';

const basic_get = (url, params) => {
  return axios.get(url, {params: params})
      .then(response => {
        console.log(response.data);
        return response.data;
      })
      .catch(err => {
        console.log(err.response.data);
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
      return basic_post('/api/rest/Connection', {
        email:email,
        password:password
      });
    }
  },
  get: {
    fetchMyInformation: () => {
      return basic_get('/api/v1/common/GetMyInformation');
    },
    fetchPersonalSpace: () => {
      return basic_get('/api/rest/GetProfiles');
    },
    getPersonalAndTeamSpace: () => {
      return basic_get('/api/rest/GetPersonalAndTeamSpace');
    },
    getTeamRoomApps: ({team_id, room_id}) => {
      return basic_get('/api/rest/GetRoomApps', {
        team_id: team_id,
        room_id: room_id
      });
    },
    getGroupApps: ({group_id}) => {
      return basic_get('/api/rest/GetGroupApps', {
        group_id: group_id
      });
    }
  }
};