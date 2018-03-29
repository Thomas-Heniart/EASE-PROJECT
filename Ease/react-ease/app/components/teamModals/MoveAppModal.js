import React, {Component} from 'react';
import {connect} from "react-redux";
import {withRouter} from "react-router-dom";
import {moveTeamCard} from "../../actions/teamCardActions";
import {Form, Button, Checkbox, Icon} from 'semantic-ui-react';
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {showMoveAppModal} from "../../actions/teamModalActions";
import {addTeamUserToChannel} from "../../actions/channelActions";

@connect(store => ({
  card: store.team_apps[store.teamModals.moveAppModal.app_id],
  teams: store.teams
}))
class MoveAppUserLooseAccess extends Component {
  constructor(props){
    super(props);
    this.state = {
      loadingAddInRoom: false,
      team: this.props.teams[this.props.card.team_id],
      room: this.props.teams[this.props.card.team_id].rooms[this.props.selectedRoom]
    }
  }
  addInRoom = (user_id) => {
    this.setState({loadingAddInRoom: true});
    this.props.dispatch(addTeamUserToChannel({
      team_id: this.state.team.id,
      channel_id: this.props.selectedRoom,
      team_user_id: user_id
    })).then(() => {
      this.setState({loadingAddInRoom: false});
    });
  };
  render() {
    const {
      next,
      back,
      loading,
      usersAdded
    } = this.props;
    return (
      <Form class="container" id="add_bookmark_form" onSubmit={next}>
        <p>The following users don’t have access to <strong>#{this.state.room.name}</strong>. In case you don’t add them, they <strong>will loose access</strong> to {this.props.card.name} and info related.</p>
        <p style={{cursor:'pointer'}} onClick={back} className="back_modal">
          <Icon name="arrow left"/>Back
        </p>
        <Form.Field className='choose_type_app'>
          {this.props.usersNotInRoom.map(user_id => {
              return <Checkbox radio
                               key={user_id}
                               name={user_id}
                               value={user_id}
                               style={{margin: "0 0 10px 0"}}
                               label={this.state.team.team_users[user_id].username}/>
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
  teams: store.teams
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
      team: this.props.teams[this.props.card.team_id]
    }
  }
  selectRoom = (roomId) => {
    this.setState({selectedRoom: Number(roomId), checkRoom: roomId});
  };
  back = () => {
    this.setState({view: 1, usersNotInRoom: [], selectedRoom: -1, checkRoom: null})
  };
  next = () => {
    const usersNotInRoom = [];
    this.props.card.receivers.filter(receiver => {
      if (!this.state.team.rooms[this.state.selectedRoom].team_user_ids.find(user_id => (user_id === receiver.team_user_id)))
        usersNotInRoom.push(receiver.team_user_id);
    });
    if (this.state.view === 1 && usersNotInRoom.length > 0)
      this.setState({view: 2, usersNotInRoom: usersNotInRoom});
    else {
      // this.props.dispatch(moveTeamCard({card_id: Number(this.props.card.id)}));
      // this.props.history.push(`/teams/${this.state.team.id}/${this.state.selectedRoom}?app_id=${this.props.card.id}`);
      this.props.dispatch(moveTeamCard({card_id: 30990}));
      this.props.history.push(`/teams/${this.state.team.id}/434?app_id=30990`);
      this.close();
    }
  };
  close = () => {
    this.props.dispatch(showMoveAppModal({active: false}));
  };
  render() {
    const card = this.props.card;
    const website = card.software ? card.software : card.website ? card.website : card;
    const rooms = this.state.team.rooms;
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
              if (room_id && rooms[room_id].team_user_ids.filter(id => (id === this.state.team.my_team_user_id)).length > 0 && Number(room_id) !== card.channel_id)
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
          loading={this.state.loading}
          usersAdded={this.state.usersAdded}
          selectedRoom={this.state.selectedRoom}
          usersNotInRoom={this.state.usersNotInRoom}/>}
      </SimpleModalTemplate>
    )
  }
}

export default withRouter(MoveAppModal);