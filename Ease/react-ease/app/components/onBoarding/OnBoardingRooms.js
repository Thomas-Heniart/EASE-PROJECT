import React from 'react';
import { Header } from 'semantic-ui-react';

class OnBoardingRooms extends React.Component {
  render() {
    const {rooms, roomsSelected, selectRoom} = this.props;
    const roomsWithoutOpenSpace = rooms.filter(item => {return item.name !== 'openspace'});
    const roomsList = roomsWithoutOpenSpace.map(item => (
      <div
        key={item.id}
        onClick={e => selectRoom(item.id)}
        className={roomsSelected.filter(id => {return id === item.id}).length > 0 ? 'selected roomsSegment' : 'roomsSegment'}>
        #{item.name}
        <span>{item.example}</span>
      </div>
    ));
    return (
      <React.Fragment>
        <Header as='h1'>What passwords does your company uses?</Header>
        <p>Select at least 3 types of passwords your company has. You’ll be able to add the tools you want in it, as well as create your own # later.</p>
        <div style={{display:'inline-flex',flexWrap:'wrap'}}>
          {roomsList}
        </div>
      </React.Fragment>
    )
  }
}

export default OnBoardingRooms;