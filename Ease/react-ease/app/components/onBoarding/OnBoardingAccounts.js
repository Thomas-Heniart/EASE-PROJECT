import React from 'react';
import {connect} from "react-redux";
import { Header, Grid, Image, Icon, Input, Dropdown, Popup } from 'semantic-ui-react';

class ChoosePasswordManager extends React.Component {
  render() {
    const {selectPasswordManager, passwordManagerSelected} = this.props;
    return (
      <React.Fragment>
        <Header as='h1'>Add your accounts from:</Header>
        <div style={{display:'inline-flex',flexWrap:'wrap',overflowY:'auto'}}>
          <div
            onClick={e => selectPasswordManager(1)}
            className={passwordManagerSelected === 1 ? 'selected roomsSegment password_manager_segment' : 'roomsSegment password_manager_segment'}>
            <div className='password_manager'><img style={{marginTop:'7px'}} src="/resources/other/Excel.png"/><span>Excel or Google sheet</span></div>
          </div>
          <div
            onClick={e => selectPasswordManager(2)}
            className={passwordManagerSelected === 2 ? 'selected roomsSegment password_manager_segment' : 'roomsSegment password_manager_segment'}>
            <div className='password_manager'><img src="/resources/other/Chrome.png"/><span>Chrome</span></div>
          </div>
          <div
            onClick={e => selectPasswordManager(10)}
            className={passwordManagerSelected === 10 ? 'selected roomsSegment password_manager_segment' : 'roomsSegment password_manager_segment'}>
            <div className='password_manager'><img style={{marginTop:'7px'}} src="/resources/other/hand.png"/><span>I'll enter my accounts manually</span></div>
          </div>
          <div
            onClick={e => selectPasswordManager(3)}
            className={passwordManagerSelected === 3 ? 'selected roomsSegment password_manager_segment' : 'roomsSegment password_manager_segment'}>
            <div className='password_manager'><img src="/resources/other/Dashlane.png"/><span>Dashlane</span></div>
          </div>
          <div
            onClick={e => selectPasswordManager(4)}
            className={passwordManagerSelected === 4 ? 'selected roomsSegment password_manager_segment' : 'roomsSegment password_manager_segment'}>
            <div className='password_manager'><img src="/resources/other/Lastpass.png"/><span>LastPass</span></div>
          </div>
          <div
            onClick={e => selectPasswordManager(5)}
            className={passwordManagerSelected === 5 ? 'selected roomsSegment password_manager_segment' : 'roomsSegment password_manager_segment'}>
            <div className='password_manager'><img src="/resources/other/1password.png"/><span>1Password</span></div>
          </div>
          <div
            onClick={e => selectPasswordManager(6)}
            className={passwordManagerSelected === 6 ? 'selected roomsSegment password_manager_segment' : 'roomsSegment password_manager_segment'}>
            <div className='password_manager'><img src="/resources/other/Keepass.png"/><span>Keepass</span></div>
          </div>
          <div
            onClick={e => selectPasswordManager(7)}
            className={passwordManagerSelected === 7 ? 'selected roomsSegment password_manager_segment' : 'roomsSegment password_manager_segment'}>
            <div className='password_manager'><img src="/resources/other/roboform.png"/><span>Roboform</span></div>
          </div>
          <div
            onClick={e => selectPasswordManager(8)}
            className={passwordManagerSelected === 8 ? 'selected roomsSegment password_manager_segment' : 'roomsSegment password_manager_segment'}>
            <div className='password_manager'><img src="/resources/other/Zohovault.png"/><span>Zoho Vault</span></div>
          </div>
          <div
            onClick={e => selectPasswordManager(9)}
            className={passwordManagerSelected === 9 ? 'selected roomsSegment password_manager_segment' : 'roomsSegment password_manager_segment'}>
            <div className='password_manager'><img src="/resources/other/passpack.png"/><span>Passpack</span></div>
          </div>
        </div>
      </React.Fragment>
    )
  }
}

class ChooseApps extends React.Component {
  render() {
    const {appsSelected, websites, currentRoom, rooms, selectApp, allAppIdsSelected} = this.props;
    const room = rooms.filter((item, idx) => {
      if (currentRoom === idx)
        return item;
    })[0];
    let idx = 0;
    const website_ids = room.website_ids.filter(id => {
      if (idx < 20 && allAppIdsSelected.filter(appId => {return id === appId}).length === 0) {
        idx++;
        return id;
      }
    });
    return (
      <React.Fragment>
        {currentRoom === 0 &&
        <Header as='h1'>Select tools everybody uses in your team</Header>}
        {currentRoom > 0 &&
        <Header as='h1'>Select accounts used in #{room.name}</Header>}
        <Grid columns={4} className="logoCatalog">
          {website_ids.map(id => (
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

class ChooseSingleApps extends React.Component {
  render() {
    const {appsSelected, rooms, currentRoom, websites, singleApps, selectSingleApp} = this.props;
    const room = rooms.filter((item, idx) => {
      if (currentRoom === idx)
        return item;
    })[0];
    return (
      <React.Fragment>
        <Header as='h1'>Do people share common passwords?</Header>
        <p>Select the tool(s) where people share an account, meaning one login and one password together.</p>
        <Grid columns={4} className="logoCatalog">
          {appsSelected.map(id => (
            <Grid.Column key={id}
                         as='a'
                         className={singleApps[room.id].filter(appId => {return appId === id}).length > 0 ? "active showSegment" : "showSegment"}
                         onClick={e => selectSingleApp(id, room.id)}>
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

class CredentialsSingleApps extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      fourthField: {},
      seePassword: {}
    };
    Object.keys(this.props.singleApps).map(room_id => {
      this.props.singleApps[room_id].map(id => {
        this.state.fourthField[id] = 0;
        this.state.seePassword[id] = false
      });
    });
  }
  changeFourthField = (id, int) => {
    this.state.fourthField[id] = int;
    this.setState({fourthField: this.state.fourthField});
    if (int === 0)
      this.props.deleteFillerId(id);
  };
  seePassword = (id) => {
    this.state.seePassword[id] = !this.state.seePassword[id];
    this.setState({seePassword: this.state.seePassword});
  };
  render() {
    const {
      team,
      users,
      websites,
      singleApps,
      testPassword,
      handleAppInfo,
      roomsSelected,
      deleteSingleApp,
      credentialsSingleApps
    } = this.props;
    const myId = team.my_team_user_id;
    const filler = {};
    roomsSelected.map(room_id => {
      filler[room_id] = users.filter(item => {
        return item.room_ids.filter(id => {return room_id === id}).length > 0 && item.id !== myId;
      }).map(item => {
        return {
          text: item.username,
          value: item.id
        }
      });
    });
    const accounts = Object.keys(singleApps).map(room_id => {
      return singleApps[room_id].map(id => (
      <div key={id} className='account'>
        <Icon className='remove_account' name='remove circle' onClick={e => deleteSingleApp(room_id, id)}/>
        <img src={websites[id].logo}/>
        <Input size='mini'
               name='name'
               type='text'
               onChange={(e, name, value) => {
                 handleAppInfo(id, name, value)
               }}
               value={credentialsSingleApps[id].name}/>
        <Input size='mini'
               type='text'
               name='login'
               onChange={(e, name, value) => {
                 handleAppInfo(id, name, value)
               }}
               value={credentialsSingleApps[id].login}
               disabled={this.state.fourthField[id] === 1}/>
        <Input size='mini'
               name='password'
               onChange={(e, name, value) => {
                 handleAppInfo(id, name, value)
               }}
               value={credentialsSingleApps[id].password}
               disabled={this.state.fourthField[id] === 1}
               icon={<Icon name='eye' link onClick={e => this.seePassword(id)}/>}
               type={this.state.seePassword[id] === false ? 'password' : 'text'}/>
        {(credentialsSingleApps[id].login === '' && credentialsSingleApps[id].password === '' && this.state.fourthField[id] === 0 && filler[room_id].length !== 0) &&
        <Popup
          inverted
          trigger={
            <p className='underline_hover' onClick={e => this.changeFourthField(id, 1)}><Icon name='life ring'/>Ask password to...</p>}
          content='You can request logins and passwords from someone in your team.'/>}
        {this.state.fourthField[id] === 1 &&
        <div className='div_dropdown'>
          <Icon size='large' name='circle' className='remove_dropdown white'/>
          <Icon onClick={e => this.changeFourthField(id, 0)} name='remove circle' className='remove_dropdown'/>
          <Dropdown selection name='filler_id' options={filler[room_id]} onChange={(e, name, value) => {handleAppInfo(id, name, value)}}/>
        </div>}
        {(credentialsSingleApps[id].login !== '' || credentialsSingleApps[id].password !== '') &&
        <Popup
          inverted
          trigger={
            <p className='underline_hover' onClick={e => testPassword(id)}><Icon name='magic'/>Test this password</p>}
          content='It will open a new tab to verify if this password works or not.'/>}
      </div>))
    });
    return (
      <React.Fragment>
        <Header as='h1'>Enter shared accounts information</Header>
        <Grid id='accounts'>
          <Grid.Column>
            <div className='dropdown_fields'>
              <p>Name</p>
              <p>Login</p>
              <p>Password</p>
            </div>
            {accounts}
          </Grid.Column>
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
      team,
      rooms,
      users,
      selectApp,
      singleApps,
      currentRoom,
      appsSelected,
      testPassword,
      handleAppInfo,
      roomsSelected,
      roomsWebsites,
      deleteFillerId,
      selectSingleApp,
      deleteSingleApp,
      allAppIdsSelected,
      credentialsSingleApps,
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
            websites={roomsWebsites}
            currentRoom={currentRoom}
            appsSelected={appsSelected}
            allAppIdsSelected={allAppIdsSelected}/>}
        {view === 3 &&
          <ChooseSingleApps
            rooms={rooms}
            singleApps={singleApps}
            websites={roomsWebsites}
            currentRoom={currentRoom}
            appsSelected={appsSelected}
            selectSingleApp={selectSingleApp}/>}
        {view === 4 &&
          <CredentialsSingleApps
            team={team}
            users={users}
            singleApps={singleApps}
            websites={roomsWebsites}
            testPassword={testPassword}
            handleAppInfo={handleAppInfo}
            roomsSelected={roomsSelected}
            deleteFillerId={deleteFillerId}
            deleteSingleApp={deleteSingleApp}
            credentialsSingleApps={credentialsSingleApps}/>}
      </React.Fragment>
    )
  }
}

export default OnBoardingAccounts;