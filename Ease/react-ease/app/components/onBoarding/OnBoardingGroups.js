import React from 'react';
import { Header, Dropdown } from 'semantic-ui-react';
import {renderUserLabel} from "../../utils/renderHelpers";

class OnBoardingGroups extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      options: []
    };
    this.state.options = this.props.users.map(item => {
      return {
        key: item.id,
        text: item.username + ' - ' + item.email,
        username: item.username,
        value: item.id
      }
    });
    this.props.rooms.map(item => {
      if (item.name !== 'openspace')
        this.props.value[item.id] = [this.props.team.my_team_user_id];
    });
  }
  render() {
    const {rooms, roomsSelected, users, dropdownChange, value} = this.props;
    const roomsToShow = rooms.filter(item => {
      return item.id && item.name !== 'openspace' && roomsSelected.filter(room_id => {return item.id === room_id}).length > 0;});
    const roomsList = roomsToShow.map(item => (
      <div key={item.id} style={{marginBottom:'25px'}}>
        <p style={{fontWeight:'bold'}}>Who uses #{item.name} passwords?</p>
        <Dropdown
          fluid
          search
          multiple
          selection
          id={item.id}
          value={value[item.id]}
          placeholder="Select people"
          options={this.state.options}
          renderLabel={renderUserLabel}
          onChange={dropdownChange}/>
      </div>
    ));
    return (
      <React.Fragment>
        <Header as='h1'>Who's in which Room?</Header>
        <div style={{height:'450px', overflowY:'auto',paddingRight:'20px'}}>
          {roomsList}
        </div>
      </React.Fragment>
    )
  }
}

export default OnBoardingGroups;