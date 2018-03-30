import React, {Component} from 'react';
import {connect} from "react-redux";
import {withRouter} from "react-router-dom";
import {moveTeamCard} from "../../actions/teamCardActions";
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {showMoveAppModal} from "../../actions/teamModalActions";
import {addTeamUserToChannel} from "../../actions/channelActions";
import {Form, Button, Checkbox, Icon, Loader} from 'semantic-ui-react';
import {removeTeamCardReceiver, teamMoveCard} from "../../actions/appsActions";
import {reflect} from "../../utils/utils";

@connect(store => ({
  card: store.team_apps[store.teamModals.moveAppModal.app_id],
  team: store.teams[store.team_apps[store.teamModals.moveAppModal.app_id].team_id]
}))
class MoveAppUserLooseAccess extends Component {
  render() {
    const {
      next,
      back,
      loading,
      addInRoom,
      usersAdded,
      loadingAddInRoom
    } = this.props;
    const room = this.props.team.rooms[this.props.selectedRoom];
    return (
      <Form class="container" id="add_bookmark_form" onSubmit={next}>
        <p>The following users don’t have access to <strong>#{room.name}</strong>. In case you don’t add them, they <strong>will loose access</strong> to {this.props.card.name} and info related.</p>
        <p style={{cursor:'pointer'}} onClick={back} className="back_modal">
          <Icon name="arrow left"/>Back
        </p>
        <Form.Field className='choose_type_app'>
          {this.props.usersNotInRoom.map(user_id => {
            return (
              <div key={user_id} className='move_app'>
                <span>{this.props.team.team_users[user_id].username}</span>
                {usersAdded.find(userAddedId => (user_id === userAddedId)) ?
                  <Icon name='check'/>
                 : <Button type='button' onClick={e => loadingAddInRoom[user_id] ? null : addInRoom(user_id)}>
                    {loadingAddInRoom[user_id] ? <Loader size='mini' inverted active/> : 'Add'}
                    </Button>
                }
              </div>);
          })}
        </Form.Field>
        <Button
          onClick={next}
          negative
          type="submit"
          content="NEXT"
          attached='bottom'
          loading={loading}
          disabled={loading}
          className="modal-button"/>
      </Form>
    )
  }
}

@connect(store => ({
  card: store.team_apps[store.teamModals.moveAppModal.app_id],
  team: store.teams[store.team_apps[store.teamModals.moveAppModal.app_id].team_id]
}))
class MoveAppModal extends Component {
  constructor(props) {
    super(props);
    this.state = {
      view: 1,
      loading: false,
      usersAdded: [],
      checkRoom: null,
      selectedRoom: -1,
      usersNotInRoom: [],
      loadingAddInRoom: [],
    }
  }
  selectRoom = (roomId) => {
    this.setState({selectedRoom: Number(roomId), checkRoom: roomId});
  };
  back = () => {
    this.setState({view: 1, usersNotInRoom: [], selectedRoom: -1, checkRoom: null})
  };
  addInRoom = (user_id) => {
    const newUsersAdded = [...this.state.usersAdded];
    let loading = {...this.state.loadingAddInRoom};
    loading[user_id] = true;
    this.setState({loadingAddInRoom: loading});
    this.props.dispatch(addTeamUserToChannel({
      team_id: this.props.team.id,
      channel_id: this.state.selectedRoom,
      team_user_id: user_id
    })).then(() => {
      newUsersAdded.push(user_id);
      loading[user_id] = false;
      this.setState({loadingAddInRoom: loading, usersAdded: newUsersAdded});
    });
  };
  next = () => {
    this.setState({loading: true});
    const usersNotInRoom = [];
    this.props.card.receivers.filter(receiver => {
      if (!this.props.team.rooms[this.state.selectedRoom].team_user_ids.find(user_id => (user_id === receiver.team_user_id)))
        usersNotInRoom.push(receiver.team_user_id);
    });
    if (this.state.view === 1 && usersNotInRoom.length > 0) {
      let loadingAddInRoom = {};
      usersNotInRoom.map(user_id => {
        loadingAddInRoom[user_id] = false;
      });
      this.setState({view: 2, usersNotInRoom: usersNotInRoom, loadingAddInRoom: loadingAddInRoom, usersAdded: [], loading: false});
    }
    else {
      const calls = [];
      usersNotInRoom.map(user_id => {
        calls.push(this.props.dispatch(removeTeamCardReceiver({
          team_id:this.props.team.id,
          team_card_id: this.props.card.id,
          team_card_receiver_id: this.props.card.receivers.find(receiver => (receiver.team_user_id === user_id)).id
        })));
      });
      Promise.all(calls.map(reflect)).then(res => {
        this.props.dispatch(teamMoveCard({
          team_id: this.props.team.id,
          team_card_id: this.props.card.id,
          channel_id: this.state.selectedRoom
        })).then(response => {
          this.props.dispatch(moveTeamCard({card_id: Number(this.props.card.id)}));
          this.props.history.push(`/teams/${this.props.team.id}/${this.state.selectedRoom}?app_id=${this.props.card.id}`);
        });
      });
    }
  };
  close = () => {
    this.props.dispatch(showMoveAppModal({active: false}));
  };
  render() {
    const card = this.props.card;
    const website = card.software ? card.software : card.website ? card.website : card;
    const rooms = this.props.team.rooms;
    return (
      <SimpleModalTemplate
        onClose={this.close}
        headerContent={this.state.view === 1 ? 'Move App' : 'Some users can loose access'}>
        {this.state.view === 1 &&
        <Form class="container" id="add_bookmark_form" onSubmit={this.next}>
          <Form.Field class="display-flex align_items_center" style={{marginBottom: '30px'}}>
            <div className="squared_image_handler">
              <img src={website.logo} alt="Website logo"/>
            </div>
            <span className="app_name">{card.name}</span>
          </Form.Field>
          <Form.Field>
            <div style={{fontWeight: 'bold'}}>Move this app from #{rooms[card.channel_id].name} to:</div>
          </Form.Field>
          <Form.Field className='choose_type_app'>
            {Object.keys(rooms).sort((a, b) => {
              if (rooms[a].default)
                return -1000;
              else if (rooms[b].default)
                return 1000;
              return rooms[a].name.localeCompare(rooms[b].name);
            }).map(room_id => {
              if (room_id && rooms[room_id].team_user_ids.filter(id => (id === this.props.team.my_team_user_id)).length > 0 && Number(room_id) !== card.channel_id)
                return <Checkbox radio
                                 key={room_id}
                                 name={room_id}
                                 value={room_id}
                                 style={{margin: "0 0 10px 0"}}
                                 label={"#" + rooms[room_id].name}
                                 onChange={e => this.selectRoom(room_id)}
                                 checked={this.state.checkRoom === room_id}/>
            })}
          </Form.Field>
          <p>As room members might be different you’ll be able to setup the app as you want it to be.</p>
          <Button
            onClick={this.next}
            positive
            type="submit"
            content="NEXT"
            attached='bottom'
            className="modal-button"
            loading={this.state.loading}
            disabled={this.state.loading || this.state.selectedRoom === -1}/>
        </Form>}
        {this.state.view === 2 &&
        <MoveAppUserLooseAccess
          back={this.back}
          next={this.next}
          addInRoom={this.addInRoom}
          loading={this.state.loading}
          usersAdded={this.state.usersAdded}
          selectedRoom={this.state.selectedRoom}
          usersNotInRoom={this.state.usersNotInRoom}
          loadingAddInRoom={this.state.loadingAddInRoom}/>}
      </SimpleModalTemplate>
    )
  }
}

export default withRouter(MoveAppModal);