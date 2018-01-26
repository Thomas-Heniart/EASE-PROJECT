import React from 'react';
import {connect} from "react-redux";
import { Header, Grid, Image, Icon } from 'semantic-ui-react';

class ChoosePasswordManager extends React.Component {
  render() {
    const {selectPasswordManager, passwordManagerSelected} = this.props;
    return (
      <React.Fragment>
        <Header as='h1'>Add your accounts from:</Header>
        <div style={{display:'inline-flex',flexWrap:'wrap'}}>
          <div
            onClick={e => selectPasswordManager(1)}
            className={passwordManagerSelected === 1 ? 'selected roomsSegment password_manager_segment' : 'roomsSegment password_manager_segment'}>
            <div className='password_manager'><img src="/resources/other/Excel.png"/><span>Excel or Google sheet</span></div>
          </div>
          <div
            onClick={e => selectPasswordManager(2)}
            className={passwordManagerSelected === 2 ? 'selected roomsSegment password_manager_segment' : 'roomsSegment password_manager_segment'}>
            <div className='password_manager'><img src="/resources/other/Chrome.png"/><span>Chrome</span></div>
          </div>
          <div
            onClick={e => selectPasswordManager(3)}
            className={passwordManagerSelected === 3 ? 'selected roomsSegment password_manager_segment' : 'roomsSegment password_manager_segment'}>
            <div className='password_manager'><img src="/resources/other/hand.png"/><span>I'll enter my accounts manually</span></div>
          </div>
          <div
            onClick={e => selectPasswordManager(4)}
            className={passwordManagerSelected === 4 ? 'selected roomsSegment password_manager_segment' : 'roomsSegment password_manager_segment'}>
            <div className='password_manager'><img src="/resources/other/Dashlane.png"/><span>Dashlane</span></div>
          </div>
          <div
            onClick={e => selectPasswordManager(5)}
            className={passwordManagerSelected === 5 ? 'selected roomsSegment password_manager_segment' : 'roomsSegment password_manager_segment'}>
            <div className='password_manager'><img src="/resources/other/Lastpass.png"/><span>LastPass</span></div>
          </div>
          <div
            onClick={e => selectPasswordManager(6)}
            className={passwordManagerSelected === 6 ? 'selected roomsSegment password_manager_segment' : 'roomsSegment password_manager_segment'}>
            <div className='password_manager'><img src="/resources/other/1password.png"/><span>1Password</span></div>
          </div>
          <div
            onClick={e => selectPasswordManager(7)}
            className={passwordManagerSelected === 7 ? 'selected roomsSegment password_manager_segment' : 'roomsSegment password_manager_segment'}>
            <div className='password_manager'><img src="/resources/other/Keepass.png"/><span>Keepass</span></div>
          </div>
          <div
            onClick={e => selectPasswordManager(8)}
            className={passwordManagerSelected === 8 ? 'selected roomsSegment password_manager_segment' : 'roomsSegment password_manager_segment'}>
            <div className='password_manager'><img src="/resources/other/roboform.png"/><span>Roboform</span></div>
          </div>
          <div
            onClick={e => selectPasswordManager(9)}
            className={passwordManagerSelected === 9 ? 'selected roomsSegment password_manager_segment' : 'roomsSegment password_manager_segment'}>
            <div className='password_manager'><img src="/resources/other/Zohovault.png"/><span>Zoho Vault</span></div>
          </div>
          <div
            onClick={e => selectPasswordManager(10)}
            className={passwordManagerSelected === 10 ? 'selected roomsSegment password_manager_segment' : 'roomsSegment password_manager_segment'}>
            <div className='password_manager'><img src="/resources/other/passpack.png"/><span>Passpack</span></div>
          </div>
        </div>
      </React.Fragment>
    )
  }
}

class ChooseApps extends React.Component {
  render() {
    const {passwordManagerSelected, appsSelected, websites, currentRoom, rooms, selectApp} = this.props;
    const room = rooms.filter((item, idx) => {
      if (currentRoom === idx)
        return item;
    })[0];
    return (
      <React.Fragment>
        {currentRoom === 1 &&
        <Header as='h1'>Select tools everybody uses in your team</Header>}
        {currentRoom > 1 &&
        <Header as='h1'>Select accounts used in #{room.name}</Header>}
        <Grid columns={4} className="logoCatalog">
          {room.website_ids.map(id => (
            <Grid.Column key={id}
                         as='a'
                         className={appsSelected.filter(appId => {return appId === id}).length > 0 ? "active showSegment" : "showSegment"}
                         onClick={e => selectApp(id)}>
              <div className='appLogo'>
                <Image src={websites[id].logo}/>
                <Icon className='iconCheck' name="check"/>
              </div>
                <p>{websites[id].name}</p>
              </Grid.Column>
          ))}
        </Grid>
      </React.Fragment>
    )
  }
}

@connect(store => ({
  websites: store.catalog.websites
}))
class OnBoardingAccounts extends React.Component {
  render() {
    const {
      view,
      rooms,
      nextRoom,
      selectApp,
      currentRoom,
      appsSelected,
      roomsSelected,
      roomsWebsites,
      selectPasswordManager,
      passwordManagerSelected} = this.props;
    return (
      <React.Fragment>
        {view === 1 &&
          <ChoosePasswordManager
            selectPasswordManager={selectPasswordManager}
            passwordManagerSelected={passwordManagerSelected}/>}
        {view === 2 &&
          <ChooseApps
            rooms={rooms}
            selectApp={selectApp}
            appsSelected={appsSelected}
            currentRoom={currentRoom}
            roomsSelected={roomsSelected}
            websites={roomsWebsites}
            passwordManagerSelected={passwordManagerSelected}/>}
      </React.Fragment>
    )
  }
}

export default OnBoardingAccounts;