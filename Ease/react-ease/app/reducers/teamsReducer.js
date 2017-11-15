import createReducer from  "./createReducer";
import update from 'immutability-helper';

export const teams = createReducer({
}, {
  ['FETCH_TEAMS_FULFILLED'](state, action){
    return {
      ...action.payload.teams
    }
  },
  ['EDIT_TEAM_NAME_FULFILLED'](state, action){
    const teamId = action.payload.team_id;
    const name = action.payload.name;
    const new_state = update(state, {
      [teamId]: {
        name: {$set: name}
      }
    });
    return new_state;
  },
  ['TEAM_CHANGED'](state, action){
    const team = action.payload.team;

    if (state[team.id]){
      const new_state = update(state, {
        [team.id]: {$set: team}
      });
      return new_state;
    }
  },
  ['TEAM_CREATED'](state, action){
    const team = action.payload.team;
    const new_state = update(state, {
      [team.id]: {$set: team}
    });
    return new_state;
  },
  ['TEAM_REMOVED'](state, action){
    const team_id = action.payload.team_id;
    const new_state = update(state, {
      [team_id] : undefined
    });
    return new_state;
  },
  ['TEAM_ROOM_CHANGED'](state, action){
    const room = action.payload.room;
    const team_id = room.team_id;

    return update(state, {
      [team_id]: {
        rooms: {
          [room.id]: {$set: room}
        }
      }
    });
  },
  ['TEAM_ROOM_CREATED'](state, action){
    const room = action.payload.room;
    const team_id = room.team_id;

    return update(state, {
      [team_id]: {
        rooms: {
          [room.id]: room
        }
      }
    });
  },
  ['TEAM_ROOM_REMOVED'](state, action){
    const {team_id,room_id} = action.payload;

    return  update(state, {
      [team_id]: {
        rooms: {
          [room_id]: {$set: undefined}
        }
      }
    });
  },
  ['TEAM_USER_CHANGED'](state, action){
    const team_user = action.payload.user;
    const team_id = team_user.team_id;


    return update(state, {
      [team_id]: {
        team_users: {
          [team_user.id]: {$set: team_user}
        }
      }
    });
  },
  ['TEAM_USER_CREATED'](state, action){
    const team_user = action.payload.user;
    const team_id = team_user.team_id;

    return update(state, {
      [team_id]: {
        team_users: {
          [team_user.id]: {$set: team_user}
        }
      }
    });
  },
  ['TEAM_USER_REMOVED'](state, action){
    const team_user = action.payload.user;
    const team_id = team_user.team_id;

    return update(state, {
      [team_id]: {
        team_users: {
          [team_user.id]: {$set: undefined}
        }
      }
    });
  },
  ['TEAM_ROOM_MEMBER_CREATED'](state, action){
    const {team_id, team_user_id, room_id} = action.payload;
    let team_user = state[team_id].team_users[team_user_id];
    let room = state[team_id].rooms[room_id];

    team_user = update(team_user, {
      room_ids: {$push: room_id}
    });
    room = update(room, {
      user_ids: {$push: team_user_id}
    });
    return update(state, {
      [team_id]: {
        rooms: {
          [room_id]: {$set: room}
        },
        team_users: {
          [team_user_id]: {$set: team_user}
        }
      }
    });
  },
  ['TEAM_ROOM_MEMBER_REMOVED'](state, action){
    const {team_id, team_user_id, room_id} = action.payload;
    let team_user = state[team_id].team_users[team_user_id];
    let room = state[team_id].rooms[room_id];

    team_user = update(team_user, {
      room_ids: {$splice: [[team_user.room_ids.indexOf(room_id), 1]]}
    });
    room = update(room, {
      user_ids: {$splice: [[room.user_ids.indexOf(team_user_id), 1]]}
    });
    return update(state, {
      [team_id]: {
        rooms: {
          [room_id]: {$set: room}
        },
        team_users: {
          [team_user_id]: {$set: team_user}
        }
      }
    });
  },
  ['TEAM_APP_RECEIVER_CREATED'](state, action){
    const {team_id, app_id, receiver} = action.payload;
    let team_user = state[team_id].team_users[receiver.teamUser_id];

    team_user = update(team_user, {
      app_ids: {$push: app_id}
    });
    return update(state, {
      [team_id]:{
        team_users: {
          [team_user.id]: {$set: team_user}
        }
      }
    });
  },
  ['TEAM_APP_RECEIVER_REMOVED'](state, action){
    const {team_id, app_id, receiver} = action.payload;

    let team_user = state[team_id].team_users[receiver.teamUser_id];
    team_user = update(team_user, {
      app_ids: {$splice: [[team_user.app_ids.indexOf(app_id), 1]]}
    });
    return update(state, {
      [team_id]:{
        team_users: {
          [team_user.id]: {$set: team_user}
        }
      }
    });
  },
  ['TEAM_APP_ADDED'](state, action){
    const {app} = action.payload;
    const team_id = app.team_id;
    const channel_id = app.channel_id;


    let new_state = update(state, {
      [team_id]: {
        rooms: {
          [channel_id]: {
            team_card_ids: {$push: [app.id]}
          }
        }
      }
    });
    app.receivers.map(item => {
      let user = new_state[team_id].team_users[item.team_user_id];
      new_state[team_id].team_users[item.team_user_id] = update(user, {
        team_card_ids: {$push: [app.id]}
      });
    });
    return new_state;
  },
  ['TEAM_APP_REMOVED'](state, action){
    const {team_id, room_id, app} = action.payload;
    const app_id = app.id;
    const room = state[team_id].rooms[room_id];

    let new_state = update(state, {
      [team_id]: {
        rooms: {
          [room_id]: {
            app_ids: {$splice: [[room.app_ids.indexOf(app_id), 1]]}
          }
        }
      }
    });
    app.receivers.map(item => {
      let user = new_state[team_id].team_users[item.teamUser_id];
      const idx = user.app_ids.indexOf(app.id);
      new_state[team_id].team_users[item.teamUser_id] = update(user, {
        app_ids: {$splice: [[idx, 1]]}
      });
    });
    return new_state;
  }
});

export const team_apps = createReducer({

}, {
  ['FETCH_TEAM_APP_FULFILLED'](state,action){
    const app = action.payload.app;
    return {
      ...state,
      [app.id]: app
    }
  },
  ['TEAM_APP_RECEIVER_CREATED'](state, action){
    const {team_id, app_id, receiver} = action.payload;

    if (!!state[app_id]){
      return update(state, {
        [app_id]: {
          receivers: {$push: receiver}
        }
      })
    }
    return state;
  },
  ['TEAM_APP_RECEIVER_REMOVED'](state, action){
    const {team_id, app_id, receiver} = action.payload;

    if (!!state[app_id]){
      const app = state[app_id];
      const _receiver = app.receivers.find(item => (item.id === receiver.id));
      const idx = app.receivers.indexOf(_receiver);
      return update(state, {
        [app_id]: {
          receivers: {$splice: [[idx, 1]]}
        }
      });
    }
    return state;
  },
  ['TEAM_APP_RECEIVER_CHANGED'](state, action){
    const {team_id, app_id, receiver} = action.payload;

    if (!!state[app_id]){
      const app = state[app_id];
      const _receiver = app.receivers.find(item => (item.id === receiver.id));
      const idx = app.receivers.indexOf(_receiver);
      return update(state, {
        [app_id]: {
          receivers: {$splice: [[idx, 1], [idx, 0, _receiver]]}
        }
      })
    }
  },
  ['TEAM_APP_ADDED'](state, action){
    const app = action.payload.app;
    return {
      ...state,
      [app.id]: app
    }
  },
  ['TEAM_APP_CHANGED'](state, action){
    const app = action.payload.app;
    if (state[app.id] !== undefined)
      return {
        ...state,
        [app.id]: app
      };
    return state;
  },
  ['TEAM_APP_REMOVED'](state, action) {
    const app = action.payload.app;
    if (state[app.id] !== undefined)
      return {
        ...state,
        [app.id]: undefined
      };
    return state;
  }
});

const default_payment = {
  data: {
    card: null,
    credit: 0,
    people_invited: false,
    business_vat_id: ""
  },
  loading: true
};

export const team_payments = createReducer({

}, {
  ['FETCH_TEAMS_FULFILLED'](state, action) {
    const teams = action.payload.teams;
    let payments = {};
    Object.keys(teams).map(id => {
      payments[id] = {
        ...default_payment
      }
    });
    return payments;
  },
  ['FETCH_TEAM_PAYMENT_INFORMATION_PENDING'](state,action) {

  }
});

