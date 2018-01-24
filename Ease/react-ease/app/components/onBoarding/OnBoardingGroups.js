import React from 'react';
import { Header, Dropdown } from 'semantic-ui-react';
import {renderUserLabel} from "../../utils/renderHelpers";

class OnBoardingGroups extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      value: [1]
    }
  }
  dropdownChange = (e, {value}) => {
    if (value.indexOf(this.props.team.my_team_user_id) === -1)
      return;
    this.setState({value: value});
  };
  render() {
    const {rooms, roomsSelected, users} = this.props;
    const roomsList = rooms.map(item => (
      <div key={item.id} style={{marginBottom:'25px'}}>
        <p style={{fontWeight:'bold'}}>Who uses #{item.name} passwords?</p>
        <Dropdown
          fluid
          search
          multiple
          selection
          options={users}
          value={this.state.value}
          placeholder="Select people"
          renderLabel={renderUserLabel}
          onChange={this.dropdownChange}/>
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