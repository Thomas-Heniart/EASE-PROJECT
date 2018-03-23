import React from 'react';
import Joyride from "react-joyride";
import {logoLetter} from "../../../utils/utils";
import {setTipSeen} from "../../../actions/commonActions";
import {testCredentials} from "../../../actions/catalogActions";
import {Segment, Button, Icon, Dropdown, Message, Input, Grid, Loader, Popup} from 'semantic-ui-react';

class DisplayAccounts extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      accounts: [],
      accountsNumber: 0,
      seePassword: {},
      dropdownOpened: false
    }
  }
  componentWillMount(){
    this.setState(this.props.getLogo());
  }
  componentDidMount(){
    const seePassword = this.props.importedAccounts.map(item => {
      return {type: 'password', id: item.id};
    }).reduce((prev, curr) =>{
      return {...prev, [curr.id]: curr.type}
    }, {});
    this.setState({accountsNumber: this.props.importedAccounts.length, seePassword: seePassword});
  }
  openDropdown = () => {
    if (this.props.loadingSending)
      return;
    if(!this.props.user.status.tip_importation_seen) {
      this.props.dispatch(setTipSeen({
        name: 'tip_importation_seen'
      }));
    }
    this.setState({dropdownOpened: !this.state.dropdownOpened});
  };
  closeOnBlur = () => {
    this.setState({dropdownOpened: false});
  };
  seePassword = (id) => {
    const seePassword = this.state.seePassword;
    if (seePassword[id] === 'password')
      seePassword[id] = 'text';
    else
      seePassword[id] = 'password';
    this.setState({seePassword: seePassword});
  };
  testConnection = (item) => {
    this.props.dispatch(testCredentials({
      account_information: {login: item.login, password: item.password},
      website_id: item.website_id
    }));
  };
  render() {
    const {
      onChange,
      loadingLogo,
      onChangeRoomName,
      roomName,
      toPending,
      cancelPending,
      onChangeField,
      deleteAccount,
      selectProfile,
      createRoom,
      selectRoom,
      roomAdded,
      loadingSending,
      importAccounts,
      error,
      fields,
      importedAccounts,
      accountsPending,
      selectedProfile,
      selectedRoom
    } = this.props;
    const order = [
      {text: 'Name', value: 'name'},
      {text: 'URL', value: 'url'},
      {text: 'User ID', value: 'login'},
      {text: 'Password', value: 'password'}];
    const profiles =
      <Dropdown.Item as="a"
                     class="display_flex"
                     active={selectedProfile > -1}
                     onClick={selectProfile}>
        <strong className="overflow-ellipsis"><Icon name='user'/>Personal Apps</strong>
        &nbsp;&nbsp;
      </Dropdown.Item>;
    const teamsList = Object.entries(this.props.teamsInState).map((teams, i) => (
      teams.map((team) => (
        team.rooms &&
        <React.Fragment key={team.id}>
          <Dropdown.Header><Icon name='users'/>{team.name}</Dropdown.Header>
          {Object.entries(team.rooms).map(rooms => (
            rooms.map(room => (
              (room.team_user_ids && room.team_user_ids.filter(id => (id === team.my_team_user_id)).length > 0) &&
              <Dropdown.Item
                as="a"
                class="display_flex"
                active={selectedRoom === room.id}
                onClick={e => selectRoom(team.id, room.id, room.name)}
                key={room.id}>
                <strong className='overflow-ellipsis'># {room.name}</strong>
                &nbsp;&nbsp;
              </Dropdown.Item>
            ))
          ))}
          {(roomAdded[team.id] === false && team.team_users[team.my_team_user_id].role > 1) &&
          <Dropdown.Item>
            <form style={{marginBottom: 0}} onSubmit={e => createRoom(team.id)}>
              <Input
                style={{fontSize: '14px'}}
                name="roomName"
                required
                transparent
                value={roomName[team.id]}
                onChange={e => onChangeRoomName(e, team.id)}
                class="create_profile_input"
                icon={<Icon name="plus square" link onClick={e => createRoom(team.id)}/>}
                placeholder='New Room' />
            </form>
          </Dropdown.Item>}
        </React.Fragment>
      ))
    ));
    const accounts = importedAccounts.map(item => (
      <div key={item.id} className='account'>
        <Loader active={loadingLogo[item.id]} inline='centered' size='tiny'/>
          {!loadingLogo[item.id] &&
            <React.Fragment>
              <Icon name='remove circle' onClick={e => deleteAccount(item.id)}/>
              {(item.logo && item.logo.length > 0) && <img src={item.logo}/>}
              {(!item.logo || item.logo.length < 1) &&
                <div className='logo_letter'>
                  <p style={{margin: 'auto'}}>{logoLetter(item.name)}</p>
                </div>}
                {Object.keys(fields).map(field => (
                  <div key={field}>
                  <Input idapp={item.id}
                         key={fields[field]}
                         size='mini'
                         name={fields[field]}
                         error={this.props.fieldProblem.id === item.id && this.props.fieldProblem.name === fields[field]}
                         value={item[fields[field]]}
                         onChange={this.props.handleAppInfo}
                         disabled={fields[field] === 'url' && item.website_id !== -1}
                         icon={fields[field] === 'password' &&
                         <Icon name='eye' link onClick={e => this.seePassword(item.id)}/>}
                         type={fields[field] === 'password' ? this.state.seePassword[item.id] : 'text'}/>
                    {(fields[field] === 'password') &&
                    <Popup
                      inverted
                      trigger={
                        <p
                          className={item.website_id !== -1 ? 'underline_hover test_connection' : 'underline_hover test_connection disabled'}
                          onClick={item.website_id !== -1 ? e => this.testConnection(item) : null}>
                          <Icon name='magic'/>Test this password
                        </p>}
                      content={item.website_id !== -1 ? 'We will open a new tab to test if the password works or not.'
                        : 'Testing this password is not available for this website'}/>}
                  </div>
                ))}
                <Icon name='arrow circle right' size='large' onClick={e => toPending(item.id)}/>
              </React.Fragment>}
        </div>
    ));
    const listPending = accountsPending.map(item => (
      <div key={item.id} className='div_account'>
        <Icon name='arrow circle left' onClick={e => cancelPending(item.id)}/>
        {(item.logo && item.logo.length > 0) && <img src={item.logo}/>}
        {(!item.logo || item.logo.length < 1) &&
        <div className='logo_letter pending'>
          <p>{logoLetter(item.name)}</p>
        </div>}
        <p>{item.name} - {item.login && item.password ? item.login : 'Add as a bookmark'}</p>
      </div>
    ));
    return (
      <React.Fragment>
        <p style={{marginBottom: '50px', marginLeft: '70px', marginTop: '10px'}}>
          <span style={{color:'#45c997',fontWeight:'bold'}}>{this.state.accountsNumber} accounts detected! </span>
          Make sure your data are put in the right columns, then import.
        </p>
        <Grid id='accounts'>
          <Grid.Column width={10}>
            <div className='dropdown_fields'>
              <p className='import'>Logo</p>
              <Dropdown pointing value={fields.field1} options={order} name='field1' onChange={onChangeField}/>
              <Dropdown pointing value={fields.field2} options={order} name='field2' onChange={onChangeField}/>
              <Dropdown pointing value={fields.field3} options={order} name='field3' onChange={onChangeField}/>
              <Dropdown pointing value={fields.field4} options={order} name='field4' onChange={onChangeField}/>
            </div>
            {accounts}
          </Grid.Column>
          <Grid.Column width={6}>
            <Segment className='segment_pending'>
              <div className="display_flex align_items_center" style={{justifyContent: 'space-between'}}>
                <p className='import'>Import selection to:</p>
                <Dropdown open={this.state.dropdownOpened}
                          floating item name='location'
                          onOpen={this.openDropdown}
                          onClose={this.openDropdown}
                          onBlur={this.closeOnBlur}
                          text={this.props.location}
                          error={error !== ''}
                          id="importation_dropdown">
                  <Dropdown.Menu>
                    {teamsList}
                    <Dropdown.Divider />
                    {profiles}
                  </Dropdown.Menu>
                </Dropdown>
                {!this.props.user.status.tip_importation_seen &&
                <Joyride
                  steps={[{
                    title: 'Organize your apps by sending them where you want to!',
                    isFixed: true,
                    selector:"#importation_dropdown",
                    position: 'bottom',
                    style: {
                      beacon: {
                        inner: '#45C997',
                        outer: '#45C997'
                      }
                    }
                  }]}
                  locale={{ back: 'Back', close: 'Got it!', last: 'Got it!', next: 'Next', skip: 'Skip the tips' }}
                  disableOverlay={true}
                  run={true}
                  allowClicksThruHole={true}
                  callback={(action) => {
                    if (action.type === 'finished')
                      this.props.dispatch(setTipSeen({
                        name: 'tip_importation_seen'
                      }));
                  }}
                />}
              </div>
              <div className='div_accounts'>
                <Message error hidden={error === ''} content={error} size='mini'/>
                {listPending}
              </div>
              <Button
                id="import_button"
                positive
                loading={loadingSending}
                disabled={accountsPending.length < 1 || (selectedProfile === -1 && selectedRoom === -1) || loadingSending}
                content={this.props.location ? `Import to ${this.props.location}` : "Import and encrypt"}
                onClick={importAccounts}/>
            </Segment>
          </Grid.Column>
        </Grid>
      </React.Fragment>
    )
  }
}

export default DisplayAccounts;