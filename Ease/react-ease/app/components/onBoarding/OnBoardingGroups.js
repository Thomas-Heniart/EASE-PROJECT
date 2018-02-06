import React from 'react';
import { Header, Dropdown } from 'semantic-ui-react';
import {renderUserLabel} from "../../utils/renderHelpers";

class OnBoardingGroups extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      options: []
    };
  }
  render() {
    const {rooms, roomsSelected, users, dropdownChange, value, team} = this.props;
    const myId = team.my_team_user_id;
    this.state.options = users.filter(user => {
      return user.id !== myId;
    }).map(item => {
      return {
        key: item.id,
        text: item.username + ' - ' + item.email,
        username: item.username,
        value: item.id
      }
    });
    const roomsToShow = rooms.filter(item => {
      return item.id && item.name !== 'openspace' && roomsSelected.filter(room_id => {return item.id === room_id}).length > 0;});
    const roomsList = roomsToShow.map(item => (
      <div key={item.id} className='group'>
        <p>Who uses #{item.name} passwords?</p>
        <Dropdown
          fluid
          search
          multiple
          selection
          id={item.id}
          value={value[item.id]}
          onChange={dropdownChange}
          placeholder="Select people"
          options={this.state.options}
          renderLabel={renderUserLabel}/>
      </div>
    ));
    return (
      <React.Fragment>
        <Header as='h1'>Who's in which Room?</Header>
        <div className='groups'>
          {roomsList}
        </div>
      </React.Fragment>
    )
  }
}

export default OnBoardingGroups;