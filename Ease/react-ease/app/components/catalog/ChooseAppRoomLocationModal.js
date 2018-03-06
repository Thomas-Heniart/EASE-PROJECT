import React from 'react';
import {connect} from "react-redux";
import {Form, Checkbox, Icon} from 'semantic-ui-react';

@connect(store => ({
  teams: store.teams
}))
class ChooseAppRoomLocationModal extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      team: this.props.teams[this.props.team_id],
      rooms: this.props.teams[this.props.team_id].rooms
    }
  }
  render() {
    const {
      back,
      checkRoom,
      handleChange
    } = this.props;
    return (
      <React.Fragment>
        <div>
          <p onClick={back}
             className="back_modal">
            <Icon name="arrow left"/>Back</p>
        </div>
        <Form.Field>
          <div style={{fontWeight: 'bold'}}>Where would you like to send this app?</div>
        </Form.Field>
        <Form.Field className='choose_type_app'>
          {Object.keys(this.state.rooms).sort((a, b) => {
            if (this.state.rooms[a].default)
              return -1000;
            else if (this.state.rooms[b].default)
              return 1000;
            return this.state.rooms[a].name.localeCompare(this.state.rooms[b].name);
          }).map(room_id => {
            if (room_id && this.state.rooms[room_id].team_user_ids.filter(id => (id === this.state.team.my_team_user_id)).length > 0)
              return <Checkbox radio
                               style={{margin: "0 0 10px 0"}}
                               name={room_id}
                               key={room_id}
                               value={room_id}
                               onChange={e => handleChange(room_id)}
                               label={"#" + this.state.rooms[room_id].name}
                               checked={checkRoom === room_id}/>
          })}
        </Form.Field>
      </React.Fragment>
    )
  }
}

export default ChooseAppRoomLocationModal;