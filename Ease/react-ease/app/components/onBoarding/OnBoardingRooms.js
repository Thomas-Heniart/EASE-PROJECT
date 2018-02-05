import React from 'react';
import { Header, Message } from 'semantic-ui-react';

class OnBoardingRooms extends React.Component {
  render() {
    const {rooms, roomsSelected, selectRoom, error} = this.props;
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
        <p>Select at least 2 Rooms, you can add any password in them, and you can create more Rooms later.</p>
        <div style={{display:'inline-flex',flexWrap:'wrap',overflowY:'auto',overflowX:'hidden'}}>
          {roomsList}
        </div>
        <Message error content={error} className='rooms'/>
      </React.Fragment>
    )
  }
}

export default OnBoardingRooms;