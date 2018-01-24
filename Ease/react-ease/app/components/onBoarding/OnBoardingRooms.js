import React from 'react';
import { Header } from 'semantic-ui-react';

class OnBoardingRooms extends React.Component {
  render() {
    const {rooms, roomsSelected, selectRoom} = this.props;
    const roomsList = rooms.map(item => (
      <div
        key={item.id}
        onClick={e => selectRoom(item.id)}
        className={roomsSelected.filter(id => {return id === item.id}).length > 0 ? 'selected roomsSegment' : 'roomsSegment'}>
        #{item.name}
        <span>{item.description}</span>
      </div>
    ));
    return (
      <React.Fragment>
        <Header as='h1'>What passwords your company uses?</Header>
        <p>Select at least 3 types of passwords your company has. You’ll be able to add the tools you want in it, as well as create your own # later.</p>
        <div style={{display:'inline-flex',flexWrap:'wrap'}}>
          {roomsList}
        </div>
      </React.Fragment>
    )
  }
}

export default OnBoardingRooms;