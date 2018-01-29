import React from 'react';
import {connect} from "react-redux";
import { Header, Grid, Image, Icon, Input, Dropdown } from 'semantic-ui-react';

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
    const {appsSelected, websites, currentRoom, rooms, selectApp} = this.props;
    const room = rooms.filter((item, idx) => {
      if (currentRoom === idx)
        return item;
    })[0];
    return (
      <React.Fragment>
        {currentRoom === 0 &&
        <Header as='h1'>Select tools everybody uses in your team</Header>}
        {currentRoom > 0 &&
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
    this.props.singleApps.map(id => {
      this.state.fourthField[id] = 0;
      this.state.seePassword[id] = false
    });
  }
  changeFourthField = (id, int) => {
    this.state.fourthField[id] = int;
    this.setState({fourthField: this.state.fourthField});
  };
  seePassword = (id) => {
    this.state.seePassword[id] = !this.state.seePassword[id];
    this.setState({seePassword: this.state.seePassword});
  };
  render() {
    const {singleApps, websites, credentialsSingleApps, users, handleAppInfo, deleteAccount} = this.props;
    const accounts = singleApps.map(id => (
      <div key={id} className='account'>
        <Icon name='remove circle' onClick={e => deleteAccount(id)}/>
        <img src={website[id].logo}/>
        <Input size='mini'
               name='name'
               type='text'
               onChange={e => handleAppInfo(id)}
               value={credentialsSingleApps[id].name}/>
        <Input size='mini'
               type='text'
               name='login'
               onChange={e => handleAppInfo(id)}
               value={credentialsSingleApps[id].login}
               disabled={this.state.fourthField[id] === 1}/>
        <Input size='mini'
               name='password'
               onChange={e => handleAppInfo(id)}
               value={credentialsSingleApps[id].password}
               disabled={this.state.fourthField[id] === 1}
               icon={<Icon name='eye' link onClick={e => this.seePassword(id)}/>}
               type={this.state.seePassword[id] === false ? 'password' : 'text'} />
        {(credentialsSingleApps[id].login === '' && credentialsSingleApps[id].password === '' && this.state.fourthField[id] === 0) &&
          <p onClick={e => this.changeFourthField(id, 1)}><Icon name='life ring'/>Ask connection info to...</p>}
        {(credentialsSingleApps[id].login !== '' && credentialsSingleApps[id].password === '' && this.state.fourthField[id] === 0) &&
          <p onClick={e => this.changeFourthField(id, 1)}><Icon name='life ring'/>Don't know the password?</p>}
        {this.state.fourthField[id] === 1 &&
          <React.Fragment>
            <Icon onClick={e => this.changeFourthField(id, 0)} name='remove circle' style={{cursor:'pointer',position:'relative',top:'14',left:'206',zIndex:'1',color:'#e0e1e2',margin:'0'}} />
            <Dropdown selection/>
          </React.Fragment>}
        {(credentialsSingleApps[id].login !== '' && credentialsSingleApps[id].password !== '') &&
          <p><Icon name='magic'/>Test this password</p>}
      </div>
    ));
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
      rooms,
      users,
      nextRoom,
      selectApp,
      singleApps,
      currentRoom,
      appsSelected,
      handleAppInfo,
      deleteAccount,
      roomsSelected,
      roomsWebsites,
      selectSingleApp,
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
            appsSelected={appsSelected}/>}
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
            users={users}
            singleApps={singleApps}
            websites={roomsWebsites}
            handleAppInfo={handleAppInfo}
            deleteAccount={deleteAccount}
            credentialsSingleApps={credentialsSingleApps}/>}
      </React.Fragment>
    )
  }
}

export default OnBoardingAccounts;