import createReducer from  "./createReducer";
import update from 'immutability-helper';

export const teams = createReducer({
}, {
  ['FETCH_TEAMS_FULFILLED'](state, action){
    return {
      ...action.payload.teams
    }
  },
  ['UPGRADE_TEAM_PLAN_FULFILLED'](state, action){
    const team = action.payload.team;

    return update(state, {
      [team.id]: {
        plan_id: {$set: team.plan_id},
        payment_required: {$set: team.payment_required}
      }
    });
  },
  ['TEAM_ADD_CREDIT_CARD_FULFILLED'](state, action){
    const {team_id} = action.payload;

    return update(state, {
      [team_id]: {
        payment_required: {$set: false}
      }
    });
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
      [team_id] : {$set: undefined}
    });
    return new_state;
  },
  ['TEAM_TRANSFER_OWNERSHIP'](state, action){
    const {team_id, team_user_id, owner_id} = action.payload;
    const team = state[team_id];
    const owner = team.team_users[owner_id];
    const user = team.team_users[team_user_id];

    return update(state, {
      [team_id]: {
        team_users: {
          [owner.id]: {
            role: {$set: team.plan_id === 1 ? 2 : 1}
          },
          [user.id]: {
            role: {$set: 3}
          }
        }
      }
    });
  },
  ['TEAM_ROOM_CHANGED'](state, action){
    const {room} = action.payload;
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
          [room.id]: {$set: room}
        }
      }
    });
  },
  ['TEAM_ROOM_REMOVED'](state, action){
    const {team_id,room_id} = action.payload;
    const room = state[team_id].rooms[room_id];

    let new_state = update(state, {
      [team_id]: {
        rooms: {$unset: [room_id]}
      }
    });
    room.team_user_ids.map(id => {
      let user = new_state[team_id].team_users[id];
      new_state[team_id].team_users[id] = update(user, {
        room_ids: {$splice: [[user.room_ids.indexOf(room.id), 1]]}
      });
    });
    return new_state;
  },
  ['TEAM_ROOM_REQUEST_CREATED'](state, action){
    const {team_id, room_id, team_user_id} = action.payload;

    return update(state, {
      [team_id]: {
        rooms: {
          [room_id]: {
            join_requests: {$push: [team_user_id]}
          }
        }
      }
    });
  },
  ['TEAM_ROOM_REQUEST_REMOVED'](state, action){
    const {team_id, room_id, team_user_id} = action.payload;

    const room = state[team_id].rooms[room_id];
    const request_idx = room.join_requests.indexOf(team_user_id);
    if (request_idx !== -1)
      return update(state, {
        [team_id]: {
          rooms: {
            [room_id]:{
              join_requests: {$splice: [[request_idx, 1]]}
            }
          }
        }
      });
    return state;
  },
  ['TEAM_USER_CHANGED'](state, action){
    const {team_user} = action.payload;
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
    const {team_user} = action.payload;
    const team_id = team_user.team_id;
    let new_state = update(state, {
      [team_id]: {
        team_users: {
          [team_user.id]: {$set: team_user}
        }
      }
    });
    Object.keys(new_state[team_id].rooms).map(id => {
      const room = new_state[team_id].rooms[id];
      if (room.default){
        new_state[team_id].rooms[id] = update(room, {
          team_user_ids: {$push: [team_user.id]}
        });
      }
    });
    return new_state;
  },
  ['TEAM_USER_REMOVED'](state, action){
    const {team_id, team_user_id} = action.payload;
    const team_user = state[team_id].team_users[team_user_id];

    let new_state = update(state, {
      [team_id]: {
        team_users: {$unset: [team_user_id]}
      }
    });
    team_user.room_ids.map(id => {
      let room = new_state[team_id].rooms[id];
      new_state[team_id].rooms[id] = update(room, {
        team_user_ids: {$splice: [[room.team_user_ids.indexOf(team_user_id), 1]]}
      });
    });
    return new_state;
  },
  ['TEAM_USER_INVITATION'](state, action){
    const {team_user, team_id} = action.payload;

    let new_state = update(state, {
      [team_id]: {
        team_users: {
          [team_user.id]: {$set: team_user}
        }
      }
    });
    Object.keys(new_state[team_id].rooms).map(id => {
      const room = new_state[team_id].rooms[id];
      if (room.default){
        new_state[team_id].rooms[id] = update(room, {
          team_user_ids: {$push: [team_user.id]}
        });
      }
    });
    return new_state;
  },
  ['TEAM_USER_CREATED_AND_INVITATION_SENT'](state, action){
    const {team_user, team_id} = action.payload;

    let new_state = update(state, {
      [team_id]: {
        team_users: {
          [team_user.id]: {$set: team_user}
        }
      }
    });
    Object.keys(new_state[team_id].rooms).map(id => {
      const room = new_state[team_id].rooms[id];
      if (room.default){
        new_state[team_id].rooms[id] = update(room, {
          team_user_ids: {$push: [team_user.id]}
        });
      }
    });
    return new_state;
  },
  ['TEAM_ROOM_MEMBER_CREATED'](state, action){
    const {team_id, team_user_id, room_id} = action.payload;
    let team_user = state[team_id].team_users[team_user_id];
    let room = state[team_id].rooms[room_id];

    team_user = update(team_user, {
      room_ids: {$push: [room_id]}
    });
    room = update(room, {
      team_user_ids: {$push: [team_user_id]},
      join_requests: {$splice: [[room.join_requests.indexOf(team_user_id), 1]]}
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
      team_user_ids: {$splice: [[room.team_user_ids.indexOf(team_user_id), 1]]}
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
  ['TEAM_CARD_RECEIVER_CREATED'](state, action){
    const {receiver} = action.payload;
    const team_id = receiver.team_id;
    const team_card_id = receiver.team_card_id;
    let team_user = state[team_id].team_users[receiver.team_user_id];

    team_user = update(team_user, {
      team_card_ids: {$push: [team_card_id]}
    });
    return update(state, {
      [team_id]:{
        team_users: {
          [team_user.id]: {$set: team_user}
        }
      }
    });
  },
  ['TEAM_CARD_RECEIVER_REMOVED'](state, action){
    const {receiver} = action.payload;
    const team_id = receiver.team_id;
    const team_card_id = receiver.team_card_id;

    let team_user = state[team_id].team_users[receiver.team_user_id];
    team_user = update(team_user, {
      team_card_ids: {$splice: [[team_user.team_card_ids.indexOf(team_card_id), 1]]}
    });
    return update(state, {
      [team_id]:{
        team_users: {
          [team_user.id]: {$set: team_user}
        }
      }
    });
  },
  ['TEAM_CARD_CREATED'](state, action){
    const {team_card} = action.payload;
    const team_id = team_card.team_id;
    const channel_id = team_card.channel_id;

    let new_state = update(state, {
      [team_id]: {
        rooms: {
          [channel_id]: {
            team_card_ids: {$push: [team_card.id]}
          }
        }
      }
    });
    team_card.receivers.map(item => {
      let user = new_state[team_id].team_users[item.team_user_id];
      new_state[team_id].team_users[item.team_user_id] = update(user, {
        team_card_ids: {$push: [team_card.id]}
      });
    });
    return new_state;
  },
  ['TEAM_CARD_REMOVED'](state, action){
    const app = action.payload.app;
    const app_id = app.id;
    const team_id = app.team_id;
    const room_id = app.channel_id;
    const room = state[team_id].rooms[room_id];

    return update(state, {
      [team_id]: {
        rooms: {
          [room_id]: {
            team_card_ids: {$splice: [[room.team_card_ids.indexOf(app_id), 1]]}
          }
        }
      }
    });
    /*    app.receivers.map(item => {
          let user = new_state[team_id].team_users[item.team_user_id];
          const idx = user.team_card_ids.indexOf(app.id);
          new_state[team_id].team_users[item.team_user_id] = update(user, {
            team_card_ids: {$splice: [[idx, 1]]}
          });
        });*/
//    return new_state;
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
  ['TEAM_CARD_RECEIVER_CREATED'](state, action){
    const {receiver} = action.payload;
    const team_card_id = receiver.team_card_id;
    if (!!state[team_card_id]){
      const team_card = state[team_card_id];
      const request = team_card.type !== 'teamLinkCard' ? team_card.requests.find(request => (request.team_user_id === receiver.team_user_id)) : false;
      let new_state = update(state, {
        [team_card_id]: {
          receivers: {$push: [receiver]}
        }
      });
      if (!!request)
        new_state = update(new_state, {
          [team_card_id]: {
            requests: {$splice: [[team_card.requests.indexOf(request), 1]]}
          }
        });
      return new_state;
    }
    return state;
  },
  ['TEAM_CARD_RECEIVER_REMOVED'](state, action){
    const {receiver} = action.payload;
    const team_card_id = receiver.team_card_id;

    if (!!state[team_card_id]){
      const app = state[team_card_id];
      const _receiver = app.receivers.find(item => (item.id === receiver.id));
      const idx = app.receivers.indexOf(_receiver);
      return update(state, {
        [team_card_id]: {
          receivers: {$splice: [[idx, 1]]}
        }
      });
    }
    return state;
  },
  ['TEAM_CARD_RECEIVER_CHANGED'](state, action){
    const {receiver} = action.payload;
    const app_id = receiver.team_card_id;
    if (!!state[app_id]){
      const app = state[app_id];
      const _receiver = app.receivers.find(item => (item.id === receiver.id));
      const idx = app.receivers.indexOf(_receiver);
      return update(state, {
        [app_id]: {
          receivers: {$splice: [[idx, 1], [idx, 0, receiver]]}
        }
      })
    }
  },
  ['TEAM_CARD_CREATED'](state, action){
    const {team_card} = action.payload;
    return update(state, {
      [team_card.id]: {$set: team_card}
    });
  },
  ['TEAM_CARD_CHANGED'](state, action){
    const team_card = action.payload.team_card;
    if (!!state[team_card.id])
      return update(state, {
        [team_card.id]: {$set: team_card}
      });
    return state;
  },
  ['TEAM_CARD_REMOVED'](state, action) {
    const app = action.payload.app;

    return update(state, {$unset: [app.id]});
  },
  ['TEAM_CARD_REQUEST_CREATED'](state, action){
    const {team_card_id, request} = action.payload;

    if (!!state[team_card_id]){
      return update(state, {
        [team_card_id]: {
          requests: {$push: [request]}
        }
      });
    }
    return state;
  },
  ['TEAM_CARD_REQUEST_REMOVED'](state, action){
    const {team_card_id, request_id} = action.payload;

    if (!!state[team_card_id]){
      const request = state[team_card_id].requests.find(request => (request_id === request.id));
      return update(state, {
        [team_card_id]: {
          requests: {$splice: [[state[team_card_id].requests.indexOf(request), 1]]}
        }
      });
    }
    return state;
  }
});

export const team_payments = createReducer({
  data: {
    card: null,
    credit: 0,
    people_invited: false,
    business_vat_id: ""
  },
  loading: true
}, {
  ['FETCH_TEAM_PAYMENT_INFORMATION_PENDING'](state,action) {
    return update(state, {
      loading: {$set: true}
    })
  },
  ["FETCH_TEAM_PAYMENT_INFORMATION_REJECTED"](state, action){
    return update(state, {
      loading: {$set: false}
    })
  },
  ['FETCH_TEAM_PAYMENT_INFORMATION_FULFILLED'](state, action){
    return update(state, {
      data: {$set: action.payload.data},
      loading: {$set: false}
    });
  },
  ['TEAM_UPDATE_BILLING_INFORMATION_FULFILLED'](state, action){
    return update(state, {
      data: {$set: action.payload.data}
    });
  },
  ['TEAM_ADD_CREDIT_CARD_FULFILLED'](state, action){
    return update(state, {
      data:{
        card: {$set: action.payload.card}
      }
    });
  },
  ['TEAM_INVITE_FRIENDS_FULFILLED'](state, action){
    return update(state, {
      data: {
        credit: {$set: action.payload.credit},
        people_invited: {$set: true}
      }
    });
  }
});

