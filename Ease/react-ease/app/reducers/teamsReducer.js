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
    if (state[room.team_id]){
      const new_state = update(state, {
        [room.team_id]: {
          rooms: {
            [room.id]: {$set: room}
          }
        }
      });
      return new_state;
    }
  },
  ['TEAM_ROOM_ADDED'](state, action){
    const room = action.payload.room;
    if (state[room.team_id]){
      const new_state = update(state, {
        [room.team_id]: {
          rooms: {
            [room.id]: room
          }
        }
      });
      return new_state;
    }
  },
  ['TEAM_ROOM_REMOVED'](state, action){
    const room = action.payload.room;
    if (state[room.team_id]){
      const new_state = update(state, {
        [room.team_id]: {
          rooms: {
            [room.id]: undefined
          }
        }
      });
      return new_state;
    }
  },
  ['TEAM_USER_CHANGED'](state, action){
    const team_user = action.payload.user;
    if (state[team_user.team_id]) {
      const new_state = update(state, {
        [team_user.team_id]: {
          team_users: {
            [team_user.id]: team_user
          }
        }
      });
      return new_state;
    }
  },
  ['TEAM_USER_ADDED'](state, action){
    const team_user = action.payload.user;
    if (state[team_user.team_id]){
      const new_state = update(state, {
        [team_user.team_id]: {
          team_users: {
            [team_user.id]: team_user
          }
        }
      });
      return new_state;
    }
  },
  ['TEAM_USER_REMOVED'](state, action){
    const team_user = action.payload.user;
    if (state[team_user.team_id]){
      const new_state = update(state, {
        [team_user.team_id]: {
          team_users: {
            [team_user.id]: undefined
          }
        }
      });
      return new_state;
    }
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
      }
  },
  ['TEAM_APP_REMOVED'](state, action){
    const app = action.payload.app;
    if (state[app.id] !== undefined)
      return {
        ...state,
        [app.id]: undefined
      }
  }
});

const default_payment = {
    data: {
      card: null,
      credit: 0,
      people_invited: false,
      business_vat_id: ""
    }
  ,
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

