import React, {Component} from 'react';
import {connect} from "react-redux";
import {moveTeamCard} from "../../actions/teamCardActions";
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {showMoveAppModal} from "../../actions/teamModalActions";
import {Form, Button, Checkbox} from 'semantic-ui-react';

@connect(store => ({
  card: store.team_apps[store.teamModals.moveAppModal.app_id],
  teams: store.teams
}))
class MoveAppModal extends Component {
  constructor(props) {
    super(props);
    this.state = {
      team: this.props.teams[this.props.card.team_id],
      loading: false,
      checkRoom: null,
      selectedRoom: -1
    }
  }
  selectRoom = (roomId) => {
    this.setState({selectedRoom: Number(roomId), checkRoom: roomId});
  };
  next = () => {
    this.props.dispatch(moveTeamCard({card_id: Number(this.props.card.id)}));
    // this.props.history.push(`/teams/${this.state.team.id}/${this.state.selectedRoom}`);
    this.close();
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
        headerContent={'Move App'}>
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
        </Form>
      </SimpleModalTemplate>
    )
  }
}

export default MoveAppModal;