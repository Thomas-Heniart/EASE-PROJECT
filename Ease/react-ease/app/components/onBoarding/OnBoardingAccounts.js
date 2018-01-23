import React from 'react';
import {connect} from "react-redux";
import { Header, Grid, Image, Icon } from 'semantic-ui-react';

class ChoosePasswordManager extends React.Component {
  render() {
    const {selectPasswordManager, passwordManagerSelected} = this.props;
    return (
      <React.Fragment>
        <div style={{display:'inline-flex',flexWrap:'wrap'}}>
          <div
            onClick={e => selectPasswordManager(1)}
            className={passwordManagerSelected === 1 ? 'selected roomsSegment' : 'roomsSegment'}>
            <img src="/resources/other/Excel.png"/>Excel or Google sheet
          </div>
          <div
            onClick={e => selectPasswordManager(2)}
            className={passwordManagerSelected === 2 ? 'selected roomsSegment' : 'roomsSegment'}>
            <img src="/resources/other/Chrome.png"/>Chrome
          </div>
          <div
            onClick={e => selectPasswordManager(3)}
            className={passwordManagerSelected === 3 ? 'selected roomsSegment' : 'roomsSegment'}>
            <img src="/resources/other/Excel.png"/>I'll enter my accounts manually
          </div>
          <div
            onClick={e => selectPasswordManager(4)}
            className={passwordManagerSelected === 4 ? 'selected roomsSegment' : 'roomsSegment'}>
            <img src="/resources/other/Dashlane.png"/>Dashlane
          </div>
          <div
            onClick={e => selectPasswordManager(5)}
            className={passwordManagerSelected === 5 ? 'selected roomsSegment' : 'roomsSegment'}>
            <img src="/resources/other/Lastpass.png"/>LastPass
          </div>
          <div
            onClick={e => selectPasswordManager(6)}
            className={passwordManagerSelected === 6 ? 'selected roomsSegment' : 'roomsSegment'}>
            <img src="/resources/other/1password.png"/>1Password
          </div>
          <div
            onClick={e => selectPasswordManager(7)}
            className={passwordManagerSelected === 7 ? 'selected roomsSegment' : 'roomsSegment'}>
            <img src="/resources/other/Keepass.png"/>Keepass
          </div>
          <div
            onClick={e => selectPasswordManager(8)}
            className={passwordManagerSelected === 8 ? 'selected roomsSegment' : 'roomsSegment'}>
            <img src="/resources/other/roboform.png"/>Roboform
          </div>
          <div
            onClick={e => selectPasswordManager(9)}
            className={passwordManagerSelected === 9 ? 'selected roomsSegment' : 'roomsSegment'}>
            <img src="/resources/other/Zohovault.png"/>Zoho Vault
          </div>
          <div
            onClick={e => selectPasswordManager(10)}
            className={passwordManagerSelected === 10 ? 'selected roomsSegment' : 'roomsSegment'}>
            <img src="/resources/other/passpack.png"/>Passpack
          </div>
        </div>
      </React.Fragment>
    )
  }
}

class ChooseApps extends React.Component {
  render() {
    const {passwordManagerSelected, websites, room} = this.props;
    return (
      <React.Fragment>
        <Grid columns={4} className="logoCatalog">
          {websites.map((item) =>
            <Grid.Column key={item.id} as='a' className="showSegment" onClick={e => console.log('chooose')}>
              <Image src={item.logo}/>
              <p>{item.name}</p>
              <Icon name="add square"/>
            </Grid.Column>
          )}
        </Grid>
      </React.Fragment>
    )
  }
}

@connect(store => ({
  websites: store.catalog.websites
}))
class OnBoardingAccounts extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      room: 1
    }
  }
  render() {
    const {view, selectPasswordManager, passwordManagerSelected} = this.props;
    return (
      <React.Fragment>
        <Header as='h1'>Who's in which Room?</Header>
        {view === 1 &&
          <ChoosePasswordManager
            selectPasswordManager={selectPasswordManager}
            passwordManagerSelected={passwordManagerSelected}/>}
        {view === 2 &&
          <ChooseApps
            room={this.state.room}
            websites={this.props.websites}
            passwordManagerSelected={passwordManagerSelected}/>}
      </React.Fragment>
    )
  }
}

export default OnBoardingAccounts;