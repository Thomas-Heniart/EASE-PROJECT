import React from 'react';
import {connect} from "react-redux";
import {
  catalogAddAnyApp, catalogAddBookmark, catalogAddClassicApp,
  getImportedAccounts
} from "../../actions/catalogActions";
import {importAccount, modifyImportedAccount, deleteImportedAccount} from "../../actions/catalogActions";
import {handleSemanticInput, isEmail, reflect, credentialIconType} from "../../utils/utils";
import {teamCreateSingleApp, teamCreateAnySingleCard, teamCreateLinkCard} from "../../actions/appsActions";
import {createProfile} from "../../actions/dashboardActions";
import {createTeamChannel, addTeamUserToChannel} from "../../actions/channelActions";
import {getLogo} from "../../utils/api"
import { Segment, Button, Icon, TextArea, Dropdown, Form, Menu, Message, Input, Loader, Grid, Label} from 'semantic-ui-react';
import Joyride from "react-joyride";
import {setTipSeen} from "../../actions/commonActions";

function json(fields, separator, csv, dispatch) {
  const array = csv.split('\n');
  let calls = [];
  for (let i = 0; i < array.length; i++) {
    let separatorCounter = 0;
    const object = Object.keys(fields);
    let url = false;
    object.map(item => {
      if (fields[item] === 'url')
        url = true;
    });
    for (let j = 0; j < array[i].length; j++) {
      if (array[i][j] === separator)
        separatorCounter++;
    }
    if (separatorCounter >= 3 && url === true) {
      let item = {};
      const field = array[i].split(separator);
      let l = 0;
      for (let k = 0; k < field.length; k++) {
        if (fields[object[k]] === 'login' && field[k + 1] && isEmail(field[k + 1].replace(/^["]+|["]+$/g, '')))
          k++;
        item[fields[object[l++]]] = field[k].replace(/^["]+|["]+$/g, '');
      }
      if (!item.url.startsWith('http://') && !item.url.startsWith('https://') && item.url !== '')
        item.url = "https://" + item.url;
      if (item.url !== '' && item.url.match(/^(https?:\/\/)?([\da-z.-]+)\.([a-z.]{2,6})(\/?)/) !== null) {
        calls.push(dispatch(importAccount({
          name: item.name ? item.name : '',
          url: item.url,
          account_information: {
            login: {name: "login", value: item.login ? item.login : ''},
            password: {name: "password", value: item.password ? item.password : ''}
          }
        })))
      }
    }
  }
  if (calls.length)
    return calls;
  else if (separator === ',')
    return json(fields, '\t', csv, dispatch);
  else
    return null;
}

class ChromeFirstStep extends React.Component {
  render() {
    const {
      login,
      password,
      error,
      back,
      next,
      onChange
    } = this.props;
    return (
      <Form className='chromeForm' error={error !== ''} onSubmit={next}>
        <Segment id='chromeSteps'>
          <p className='title'><img src="/resources/other/Chrome.png"/> Import your passwords from Chrome</p>
          <div className='inline'>
            <p>Enter below the information of your Chrome account</p>
            <img src="/resources/images/agathe_chrome.png"/>
          </div>
          <Form.Field>
            <label>Login</label>
            <Input size="large"
                   autoFocus
                   class="modalInput team-app-input"
                   required
                   autoComplete='on'
                   name='chromeLogin'
                   onChange={onChange}
                   label={<Label><Icon name={credentialIconType['login']}/></Label>}
                   labelPosition="left"
                   placeholder='Your login'
                   value={login}
                   type='text'/>
          </Form.Field>
          <Form.Field>
            <label>Password</label>
            <Input size="large"
                   class="modalInput team-app-input"
                   required
                   autoComplete='on'
                   name='chromePassword'
                   onChange={onChange}
                   label={<Label><Icon name={credentialIconType['password']}/></Label>}
                   labelPosition="left"
                   placeholder='Your password'
                   value={password}
                   type='password'/>
          </Form.Field>
          <Form.Field>
            <Message error size="mini" content={error}/>
          </Form.Field>
        </Segment>
        <Button className={'left'} onClick={back} type='button'>
          <Icon name='arrow left'/> Back
        </Button>
        <Button className={'right'} positive onClick={next} type='submit' disabled={login === '' || password === ''}>
          Next <Icon name='arrow right'/>
        </Button>
      </Form>
    )
  }
}

class ChromeSecondStep extends React.Component {
  render() {
    return (
      <React.Fragment>
        <Segment id='chromeSteps'>
          <p className='title'><img src="/resources/other/Chrome.png"/> Google Chrome is being imported</p>
          <div style={{margin:'45px 0'}}>
            <Loader active style={{position:'relative',transform:'translateX(-50%)',bottom:'50%'}}/>
          </div>
          <p>Ease.space integrates your accounts by finding them in a new tab. Please, do not close it.</p>
          <p>Once it’s done, you will be able to select what you keep on Ease.space.</p>
          <p>It will take approximately 45sec, be patient ;)</p>
        </Segment>
      </React.Fragment>
    )
  }
}

class ChoosePasswordManager extends React.Component {
  render() {
    const {
      next,
      choosePasswordManager,
      passwordManager
    } = this.props;
    return (
      <React.Fragment>
        <p className='title center'>Where is your importation coming from</p>
        <Segment.Group>
          <Segment onClick={e => choosePasswordManager(1)} className={passwordManager === 1 ? 'selected' : null}>
            <img src="/resources/other/Excel.png"/>Excel or Google sheet</Segment>
          <Segment onClick={e => choosePasswordManager(2)} className={passwordManager === 2 ? 'selected' : null}>
            <img src="/resources/other/Chrome.png"/>Chrome</Segment>
          <Segment onClick={e => choosePasswordManager(3)} className={passwordManager === 3 ? 'selected' : null}>
            <img src="/resources/other/Dashlane.png"/>Dashlane</Segment>
          <Segment onClick={e => choosePasswordManager(4)} className={passwordManager === 4 ? 'selected' : null}>
            <img src="/resources/other/Lastpass.png"/>LastPass</Segment>
          <Segment onClick={e => choosePasswordManager(5)} className={passwordManager === 5 ? 'selected' : null}>
            <img src="/resources/other/1password.png"/>1Password</Segment>
          <Segment onClick={e => choosePasswordManager(6)} className={passwordManager === 6 ? 'selected' : null}>
            <img src="/resources/other/Keepass.png"/>Keepass</Segment>
          <Segment onClick={e => choosePasswordManager(7)} className={passwordManager === 7 ? 'selected' : null}>
            <img src="/resources/other/roboform.png"/>Roboform</Segment>
          <Segment onClick={e => choosePasswordManager(8)} className={passwordManager === 8 ? 'selected' : null}>
            <img src="/resources/other/Zohovault.png"/>Zoho Vault</Segment>
          <Segment onClick={e => choosePasswordManager(9)} className={passwordManager === 9 ? 'selected' : null}>
            <img src="/resources/other/passpack.png"/>Passpack</Segment>
        </Segment.Group>
        <Button
          className={'right'}
          disabled={passwordManager === 0}
          positive
          onClick={next}>
          Next <Icon name='arrow right'/>
        </Button>
      </React.Fragment>
    )
  }
}

class Explication extends React.Component {
  render() {
    const {
      next,
      back,
      passwordManager
    } = this.props;
    return (
      <React.Fragment>
        {passwordManager === 2 &&
        <React.Fragment>
          <p className='title'><img src="/resources/other/Chrome.png"/> Import your passwords from Chrome</p>
          <p><a  target='_blank'/></p>
          <p>2. Once you get the CSV, click on Next bellow :)</p>
        </React.Fragment>}
        {passwordManager === 3 &&
        <React.Fragment>
          <p className='title'><img src="/resources/other/Dashlane.png"/> Import your passwords from Dashlane</p>
          <p className='no_margin'>&emsp;1. Export your passwords from Dashlane to a <strong>CSV file</strong>.</p>
          <ul>
            <li>Open your Dashlane desktop application.</li>
            <li>Click the “<strong>File</strong>” tab</li>
            <li>Select “<strong>Export</strong>”, then choose “<strong>Export to CSV</strong>”</li>
          </ul>
          <p className='no_margin'>&emsp;2. Your almost done!</p>
          <ul>
            <li>Open the downloaded file</li>
            <li>Copy the whole content of the file, then click on “<strong>Next</strong>” bellow :)</li>
          </ul>
        </React.Fragment>}
        {passwordManager === 4 &&
        <React.Fragment>
          <p className='title'><img src="/resources/other/Lastpass.png"/> Import your passwords from LastPass</p>
          <p className='no_margin'>&emsp;1. Export your passwords from Lastpass to a <strong>CSV file</strong>.</p>
          <ul>
            <li>Open Lastpass, click on “<strong>More Options</strong>” > “<strong>Advanced</strong>” > “<strong>Export</strong>”.</li>
          </ul>
          <p className='no_margin'>&emsp;2. Your almost done!</p>
          <ul>
            <li>Copy the whole text containing your account names, logins and passwords.</li>
            <li>Click on “<strong>Next</strong>” bellow :)</li>
          </ul>
        </React.Fragment>}
        {passwordManager === 5 &&
        <React.Fragment>
          <p className='title'><img src="/resources/other/1password.png"/> Import your passwords from 1Password</p>
          <p className='no_margin'>&emsp;1. Export your passwords from 1Password to a <strong>CSV file</strong>.</p>
          <ul>
            <li>Open 1Password desktop application</li>
            <li>Select a Vault (you cannot export several Vaults at once)</li>
            <li>Click the “<strong>File</strong>” tab > “<strong>Export</strong>” > “<strong>All elements</strong>”</li>
            <li>Enter your 1Password master password</li>
            <li>As File Format chose <strong>Comma Separated Values (.csv)</strong></li>
            <li>Leave other default settings as displayed, then click “<strong>Save</strong>”</li>
          </ul>
          <p className='no_margin'>&emsp;2. Your almost done!</p>
          <ul>
            <li>Open the downloaded file.</li>
            <li>Copy the whole content of the file, then click on “<strong>Next</strong>” bellow :)</li>
          </ul>
        </React.Fragment>}
        {passwordManager === 6 &&
        <React.Fragment>
          <p className='title'><img src="/resources/other/Keepass.png"/> Import your passwords from Keepass</p>
          <p>&emsp;1. Export your passwords from Keepass to a <strong>CSV file</strong>.
            <a href='https://keepass.info/help/base/importexport.html' target='_blank'>
              Check how here
            </a>
          </p>
          <p className='no_margin'>&emsp;2. Your almost done!</p>
          <ul>
            <li>Open the downloaded file.</li>
            <li>Copy the whole content of the file, then click on “<strong>Next</strong>” bellow :)</li>
          </ul>
        </React.Fragment>}
        {passwordManager === 7 &&
        <React.Fragment>
          <p className='title'><img src="/resources/other/roboform.png"/> Import your passwords from RoboForm</p>
          <p>&emsp;1. Export your passwords from Roboform to a <strong>CSV file</strong>.
            <a href='https://help.roboform.com/hc/en-us/articles/230425008-How-to-export-your-RoboForm-logins-' target='_blank'>
              Check how here
            </a>
          </p>
          <p className='no_margin'>&emsp;2. Your almost done!</p>
          <ul>
            <li>Open the downloaded file.</li>
            <li>Copy the whole content of the file, then click on “<strong>Next</strong>” bellow :)</li>
          </ul>
        </React.Fragment>}
        {passwordManager === 8 &&
        <React.Fragment>
          <p className='title'><img src="/resources/other/Zohovault.png"/> Import your passwords from Zoho Vault</p>
          <p className='no_margin'>&emsp;1. Export your passwords from Zoho Vault</p>
          <ul>
            <li>Open Zoho Vault.</li>
            <li>Click the “<strong>Tools</strong>” tab. Choose “<strong>Export Secrets</strong>” button on the left panel.</li>
            <li>You can either export all the secrets OR export only the ones that belong to a particular secret type.</li>
            <li>Select “<strong>General CSV</strong>”</li>
            <li>Click on “<strong>Export Secrets</strong>”</li>
          </ul>
          <p className='no_margin'>&emsp;2. Your almost done!</p>
          <ul>
            <li>Copy the whole text containing your account names, logins and passwords.</li>
            <li>Click on “<strong>Next</strong>” bellow :)</li>
          </ul>
        </React.Fragment>}
        {passwordManager === 9 &&
        <React.Fragment>
          <p className='title'><img src="/resources/other/passpack.png"/> Import your passwords from Passpack</p>
          <p>&emsp;1. Export your passwords from Passpack to a <strong>Comma Separated Values (CSV) file</strong>.
            <a href='https://help.roboform.com/hc/en-us/articles/230425008-How-to-export-your-RoboForm-logins-' target='_blank'>
              Check how here
            </a>
          </p>
          <p className='no_margin'>&emsp;2. Your almost done!</p>
          <ul>
            <li>Open the downloaded file.</li>
            <li>Copy the whole content of the file, then click on “<strong>Next</strong>” bellow :)</li>
          </ul>
        </React.Fragment>}
        <Button className={'left'} onClick={back}>
          <Icon name='arrow left'/> Back
        </Button>
        <Button className={'right'} positive onClick={next}>
          Next <Icon name='arrow right'/>
        </Button>
      </React.Fragment>
    )
  }
}

class PasteStep extends React.Component {
  render() {
    const {
      next,
      back,
      onChange,
      onChangeField,
      fields,
      pasted,
      passwordManager
    } = this.props;
    const separator = [
      {text: 'Comma (,)', value: ','},
      {text: 'Tab', value: '\t'},
      {text: 'Semicolon (;)', value: ';'},
      {text: 'Space', value: ' '}];
    const order = [
      {text: 'Name', value: 'name'},
      {text: 'URL', value: 'url'},
      {text: 'User ID', value: 'login'},
      {text: 'Password', value: 'password'},
      {text: 'Note', value: 'note'},
      {text: 'Tag', value: 'tag'},
      {text: '-', value: '-'}];
    return (
      <React.Fragment>
        <Form id='step3' error={this.props.error !== ''}>
          <p className='title'>Paste here the content of your file.</p>
          <TextArea name='paste' onChange={onChange} className={(passwordManager > 2 && passwordManager < 9) ? 'alone' : null} autoFocus/>
          {(passwordManager < 3 || passwordManager > 8) &&
          <React.Fragment>
            <p className='question'>1. How is your file structured?</p>
            <Menu className='menu_fields'>
              <Dropdown pointing className='link item' value={fields.field1} options={order} name='field1' onChange={onChangeField}/>
              <Dropdown pointing className='link item' value={fields.field2} options={order} name='field2' onChange={onChangeField}/>
              <Dropdown pointing className='link item' value={fields.field3} options={order} name='field3' onChange={onChangeField}/>
              <Dropdown pointing className='link item' value={fields.field4} options={order} name='field4' onChange={onChangeField}/>
              <Dropdown pointing className='link item' value={fields.field5} options={order} name='field5' onChange={onChangeField}/>
              <Dropdown pointing className='link item' value={fields.field6} options={order} name='field6' onChange={onChangeField}/>
            </Menu>
            <p>It should be as above if your file is ordered like: Linkedin, https://www.linkedin.com/, elon@spacex.com, ElonPassword75, personal account.</p>
            <div id='div_separator'>
              <p className='question'>2. How is the data separated in your file?</p>
              <Dropdown selection name='separator' defaultValue={','} options={separator} onChange={onChange}/>
            </div>
            <p>Ex: if your first row is "Website URL", "Login", "Password"; then choose Comma (,)</p>
          </React.Fragment>}
          <Message error content={this.props.error}/>
        </Form>
        <Button className={'left'} onClick={back}>
          <Icon name='arrow left'/> Back
        </Button>
        <Button className={'right'} positive onClick={next} disabled={!pasted}>
          Next <Icon name='arrow right'/>
        </Button>
      </React.Fragment>
    )
  }
}

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
  logoLetter = (name) => {
    let first = '';
    let second = '';
    let space = false;
    for (let letter = 0; letter < name.length; letter++) {
      if (first.length < 1 && name[letter] !== ' ')
        first = name[letter];
      else if (first.length > 0 && second.length < 1 && name[letter] !== ' ' && space === true)
        second = name[letter];
      else if (name[letter] === ' ')
        space = true;
    }
    if (second !== '')
      return first.toUpperCase() + second.toUpperCase();
    else
      return first.toUpperCase();
  };
  render() {
    const {
      onChange,
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
        <strong className="overflow-ellipsis"><Icon name='user'/>Personal Space</strong>
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
          {((team.plan_id === 1 || (team.plan_id === 0 && Object.keys(team.rooms).length < 4)) && roomAdded[team.id] === false && team.team_users[team.my_team_user_id].role > 1) &&
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
        <Icon name='remove circle' onClick={e => deleteAccount(item.id)}/>
        {(item.logo && item.logo.length > 0) && <img src={item.logo}/>}
        {(!item.logo || item.logo.length < 1) &&
        <div className='logo_letter'>
          <p style={{margin: 'auto'}}>{this.logoLetter(item.name)}</p>
        </div>}
        {Object.keys(fields).map(field => (
          <Input idapp={item.id}
                 key={fields[field]}
                 size='mini'
                 name={fields[field]}
                 error={this.props.fieldProblem.id === item.id && this.props.fieldProblem.name === fields[field]}
                 value={item[fields[field]]}
                 onChange={this.props.handleAppInfo}
                 disabled={fields[field] === 'url' && item.website_id !== -1}
                 icon={fields[field] === 'password' && <Icon name='eye' link onClick={e => this.seePassword(item.id)}/>}
                 type={fields[field] === 'password' ? this.state.seePassword[item.id] : 'text'} />
        ))}
        <Icon name='arrow circle right' size='large' onClick={e => toPending(item.id)}/>
      </div>
    ));
    const listPending = accountsPending.map(item => (
        <div key={item.id} className='div_account'>
          <Icon name='arrow circle left' onClick={e => cancelPending(item.id)}/>
          {(item.logo && item.logo.length > 0) && <img src={item.logo}/>}
          {(!item.logo || item.logo.length < 1) &&
          <div className='logo_letter pending'>
            <p>{this.logoLetter(item.name)}</p>
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
              <div class="display_flex align_items_center" style={{justifyContent: 'space-between'}}>
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

class ErrorAccounts extends React.Component {
  logoLetter = (name) => {
    let first = '';
    let second = '';
    let space = false;
    for (let letter = 0; letter < name.length; letter++) {
      if (first.length < 1 && name[letter] !== ' ')
        first = name[letter];
      else if (first.length > 0 && second.length < 1 && name[letter] !== ' ' && space === true)
        second = name[letter];
      else if (name[letter] === ' ')
        space = true;
    }
    if (second !== '')
      return first.toUpperCase() + second.toUpperCase();
    else
      return first.toUpperCase();
  };
  render() {
    const {
      errorAccounts,
      handleErrorAppInfo,
      importErrorAccounts,
      deleteErrorAccount,
      fields
    } = this.props;
    const accounts = errorAccounts.map(item => (
      <div key={item.id} className='account'>
        <Icon name='remove circle' onClick={e => deleteErrorAccount(item.id)}/>
        {(item.logo && item.logo.length > 0) && <img src={item.logo}/>}
        {(!item.logo || item.logo.length < 1) &&
        <div className='logo_letter'>
          <p style={{margin: 'auto'}}>{this.logoLetter(item.name)}</p>
        </div>}
        {Object.keys(fields).map(field => (
          <Input idapp={item.id}
                 key={fields[field]}
                 size='mini'
                 name={fields[field]}
                 value={item[fields[field]]}
                 disabled
                 icon={fields[field] === 'password' && <Icon name='eye' link onClick={e => this.seePassword(item.id)}/>}
                 type={fields[field] === 'password' ? this.state.seePassword[item.id] : 'text'} />
        ))}
        <Input size='mini'
               idapp={item.id}
               name='thirdField'
               error={item.thirdField.value === ''}
               placeholder={item.thirdField.name}
               value={item.thirdField.value}
               onChange={handleErrorAppInfo} />
      </div>
    ));
    return (
      <React.Fragment>
        <p style={{color:'#eb555c',marginBottom: '50px', marginLeft: '70px', marginTop: '10px'}}>Some errors detected for the following account(s):</p>
        <Grid id='accounts'>
          <div className='dropdown_fields'>
            <Dropdown value={fields.field1}/>
            <Dropdown value={fields.field2}/>
            <Dropdown value={fields.field3}/>
            <Dropdown value={fields.field4}/>
            <Dropdown value={'Third field'}/>
          </div>
          {accounts}
        </Grid>
        <Button className={'right'} content='Done!' positive onClick={importErrorAccounts}/>
      </React.Fragment>
    )
  }
}

@connect(store => ({
  fetching: store.catalog.fetching,
  websites: store.catalog.websites,
  dashboard: store.dashboard,
  profiles: store.dashboard.profiles,
  teams: store.teams,
  user: store.common.user
}))
class Importations extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      view: 1,
      passwordManager: 0,
      chromeLogin: '',
      chromePassword: '',
      separator: ',',
      paste: '',
      error: '',
      location: '',
      loading: false,
      loadingDelete: false,
      loadingSending: false,
      fields: {},
      importedAccounts: [],
      accountsPending: [],
      errorAccounts: [],
      profiles: {},
      selectedProfile: -1,
      teamsInState: {},
      roomName: {},
      roomAdded: false,
      selectedTeam: -1,
      selectedRoom: -1,
      fieldProblem: {name: '', id: -1}
    };
  }
  componentWillMount() {
    this.setState({loading: true});
    const profiles = {...this.props.profiles};
    let newTeams = {};
    Object.keys(this.props.teams).map(item => {
      const team  = this.props.teams[item];
      newTeams[item] = {...team};
      newTeams[item].rooms = {};
      Object.keys(team.rooms).map(room_id =>  {
        newTeams[item].rooms[room_id] = {...team.rooms[room_id]}
      });
    });
    const keys = Object.keys(newTeams);
    const roomName = {};
    const roomAdded = {};
    keys.map(item => {
      roomName[item] = "";
      roomAdded[item] = false;
      return item;
    });
    this.props.dispatch(getImportedAccounts())
      .then(response => {
        if (response.length) {
          const accounts = response.map(item => {
            return {
              id: item.id,
              url: item.url,
              name: item.name,
              login: item.account_information.login.value,
              password: item.account_information.password.value,
              website_id: item.website_id,
            }
          }).sort((a, b) => {
            return a.url.localeCompare(b.url);
          });
          this.setState({
            importedAccounts: accounts,
            view: 4,
            fields: {field1: 'url', field2: 'name', field3: 'login', field4: 'password'}
          });
        }
        this.setState({loading: false, profiles: profiles, teamsInState: newTeams, roomName: roomName, roomAdded: roomAdded});
      }).catch(err => {
        this.setState({error: err, loading: false});
    });
  }
  getLogo = () => {
    const importedAccounts = this.state.importedAccounts.map(item => {
      if (item.website_id === -1) {
        getLogo({url: item.url}).then(response => {
          if (response !== '/resources/icons/link_app.png')
            item.logo = response;
          else if (item.login && item.password && response === '/resources/icons/link_app.png')
            item.logo = '';
          else
            item.logo = response;
          return item;
        });
      }
      else {
        item.logo = this.props.websites.filter(website => {
          if (item.website_id === website.id)
            return website;
        }).map(website => {
          return website.logo;
        })[0];
      }
      return item;
    });
    this.setState({importedAccounts: importedAccounts});
  };
  handleInput = handleSemanticInput.bind(this);
  handleRoomName = (e, team_id) => {
    if (e.target.value.match(/[a-zA-Z0-9\s_\-]/g) !== null && e.target.value.length <= 21) {
      const roomName = Object.assign({}, this.state.roomName);
      roomName[team_id] = e.target.value.replace(" ", "_").toLowerCase();
      this.setState({roomName: roomName});
    }
    else {
      this.setState({error: 'Room names can’t contain uppercases, spaces, periods or most punctuation and must be shorter than 21 characters.'})
    }
  };
  handleAppInfo = (e, {name, value, idapp}) => {
    const importedAccounts = this.state.importedAccounts.map(item => {
      if (item.id === idapp)
        item[name] = value;
      return item;
    });
    this.setState({importedAccounts: importedAccounts});
  };
  handleErrorAppInfo = (e, {name, value, idapp}) => {
    const errorAccounts = this.state.errorAccounts.map(item => {
      if (item.id === idapp && name !== 'thirdField')
        item[name] = value;
      else if (item.id === idapp && name === 'thirdField')
        item.thirdField.value = value;
      return item;
    });
    this.setState({errorAccounts: errorAccounts});
  };
  createRoom = (id) => {
    if (this.state.roomName[id].length === 0)
      return;
    let newTeams = {...this.state.teamsInState};
    let roomAdded = Object.assign({}, this.state.roomAdded);
    newTeams[id].rooms.newRoom = {id: 0, name: this.state.roomName[id], team_user_ids:[newTeams[id].my_team_user_id]};
    roomAdded[id] = true;
    this.setState({roomAdded: roomAdded, teamsInState: newTeams, selectedRoom: 0, selectedTeam: id, location: `#${this.state.roomName[id]}`});
  };
  toPending = (id) => {
    if (this.state.selectedProfile === -1 && this.state.selectedRoom === -1) {
      this.setState({error:"Choose where you want to put your apps!"})
    }
    else {
      const accountsPending = this.state.accountsPending.slice();
      const importedAccounts = [];
      this.state.importedAccounts.map(item => {
        if (item.id !== id)
          importedAccounts.push(item);
        else if (item.id === id && item.url.match(/^(https?:\/\/)?([\da-z.-]+)\.([a-z.]{2,6})(\/?)/) !== null && item.name !== '') {
          accountsPending.push(item);
          this.setState({error: ''});
        }
        else if (item.id === id && item.url.match(/^(https?:\/\/)?([\da-z.-]+)\.([a-z.]{2,6})(\/?)/) !== null && item.name === '') {
          importedAccounts.push(item);
          this.setState({error: 'Please name the account before importing it.', fieldProblem: {name: 'name', id: item.id}});
        }
        else {
          importedAccounts.push(item);
          this.setState({error: 'It is not an URL', fieldProblem: {name: 'url', id: item.id}});
        }
      });
      this.setState({importedAccounts: importedAccounts, accountsPending: accountsPending});
    }
  };
  cancelPending = (id) => {
    const accountsPending = [];
    const importedAccounts = this.state.importedAccounts.slice();
    this.state.accountsPending.map(item => {
      if (item.id === id)
        importedAccounts.push(item);
      else
        accountsPending.push(item);
    });
    this.setState({importedAccounts: importedAccounts, accountsPending: accountsPending});
  };
  selectProfile = () => {
    this.setState({
      selectedProfile: Object.keys(this.props.profiles).length > 0 ? 1 : 0,
      selectedTeam: -1,
      selectedRoom: -1,
      location: 'Personal Apps',
      error: ""
    });
  };
  selectRoom = (teamId, roomId, name) => {
    this.setState({ selectedTeam: teamId, selectedRoom: roomId, selectedProfile: -1, location: `#${name}`, error:""});
  };
  changeOrderField = (e, {name, value}) => {
    let fields = this.state.fields;
    if (value !== '-') {
      Object.keys(fields).map(item => {
        if (fields[item] === value)
          fields[item] = fields[name];
      });
    }
    Object.keys(fields).map(item => {
      if (item === name)
        fields[item] = value;
    });
    this.setState({fields: fields});
  };
  changeOrderFieldStep4 = (e, {name, value}) => {
    let fields = Object.assign({}, this.state.fields);
    Object.keys(fields).map(item => {
      if (fields[item] === value)
        fields[item] = fields[name];
    });
    Object.keys(fields).map(item => {
      if (item === name)
        fields[item] = value;
    });
    const accounts = this.state.importedAccounts.map(item => {
      return {
        [fields.field1]: item[this.state.fields.field1],
        [fields.field2]: item[this.state.fields.field2],
        [fields.field3]: item[this.state.fields.field3],
        [fields.field4]: item[this.state.fields.field4],
        id: item.id,
        website_id: item.website_id,
        logo: (item.logo && item.logo.length > 0) ? item.logo : ''
      }
    });
    this.setState({fields: fields, importedAccounts: accounts});
  };
  eventListener = event => {
    if (event.detail.success === true) {
      let calls = [];
      event.detail.msg.map((item, idx) => {
         calls.push(this.props.dispatch(importAccount({
          id: idx,
          name: '',
          url:  item.website.startsWith("http") === false && item.website !== '' ? "https://" + item.website : item.website,
          website_id: -1,
          account_information: {
            login: {name:"login", value: item.login},
            password: {name:"password", value: item.pass}
          }
         })));
      });
      Promise.all(calls.map(reflect)).then(response => {
        const json = response.filter(item => {
          if (item.error === false)
            return item;
        }).map(item => {
          return item.data;
        });
        if (json.length) {
          const acc = json.map(item => {
            return {
              id: item.id,
              url: item.url,
              name: item.name,
              login: item.account_information.login.value,
              password: item.account_information.password.value,
              website_id: item.website_id,
            }
          }).sort((a, b) => {
            return a.url.localeCompare(b.url);
          });
          this.setState({
            importedAccounts: acc,
            view: 4,
            error: '',
            fields: {field1: 'url', field2: 'name', field3: 'login', field4: 'password'}
          });
        }
      }).catch(err => {
      });
    }
    else
      this.setState({view: 2, error: event.detail.msg});
  };
  changeView = () => {
    this.setState({loading: true});
    if (this.state.view === 1 && this.state.passwordManager === 1)
      this.setState({view: 3, loading: false, error: ''});
    else if (this.state.view === 2 && this.state.passwordManager === 2) {
      this.setState({view: 3, loading: false});
      document.dispatchEvent(new CustomEvent("ScrapChrome", {detail:{login:this.state.chromeLogin,password:this.state.chromePassword}}));
      document.addEventListener("ScrapChromeResult", (event) => this.eventListener(event));
    }
    else if (this.state.view === 3 && this.state.paste !== '') {
      const calls = json(this.state.fields, this.state.separator, this.state.paste, this.props.dispatch);
      if (calls === null)
        this.setState({error: 'Darn, that didn’t work! Make sure the text pasted contains logins and passwords properly separated.', loading: false});
      else {
        Promise.all(calls.map(reflect)).then(response => {
          const json = response.filter(item => {
            if (item.error === false)
              return item;
          }).map(item => {
            return item.data;
          });
          if (json.length) {
            const accounts = json.map(item => {
              return {
                id: item.id,
                url: item.url,
                name: item.name,
                login: item.account_information.login.value,
                password: item.account_information.password.value,
                website_id: item.website_id,
              }
            }).sort((a, b) => {
              return a.url.localeCompare(b.url);
            });
            this.setState({
              importedAccounts: accounts,
              view: 4,
              error: '',
              fields: {field1: 'url', field2: 'name', field3: 'login', field4: 'password'},
              loading: false
            });
          }
        }).catch(err => {
        });
      }
    }
    else
      this.setState({view: this.state.view + 1, error: '', loading: false});
  };
  deleteAccount = (id) => {
    if (!this.state.loadingDelete) {
      this.setState({loadingDelete: true});
      this.props.dispatch(deleteImportedAccount({id}))
        .then(response => {
          const accounts = this.state.importedAccounts.filter(item => {
            if (item.id !== id)
              return item;
          });
          this.setState({importedAccounts: accounts, loadingDelete: false});
          if (accounts.length < 1 && this.state.accountsPending.length < 1)
            this.setState({
              passwordManager: 0,
              view: 1,
              error: '',
              location: '',
              selectedTeam: -1,
              selectedRoom: -1,
              selectedProfile: -1,
              separator: ','
            });
        });
    }
  };
  deleteErrorAccount = (id) => {
    if (!this.state.loading) {
      this.props.dispatch(deleteImportedAccount({id}))
        .then(response => {
          const accounts = this.state.errorAccounts.filter(item => {
            if (item.id !== id)
              return item;
          });
          this.setState({errorAccounts: accounts});
          if (accounts.length < 1)
            this.setState({
              passwordManager: 0,
              view: 1,
              error: '',
              selectedTeam: -1,
              selectedRoom: -1,
              selectedProfile: -1,
              location: '',
              separator: ','
            });
        });
    }
  };
  back = () => {
    if (this.state.passwordManager === 1)
      this.setState({view: 1, error: '', separator: ',',});
    else
      this.setState({view: this.state.view - 1, error: ''});
  };
  choosePasswordManager = (int) => {
    if (int === 4)
      this.setState({
        passwordManager: int,
        fields:{
          field1: 'url',
          field2: 'login',
          field3: 'password',
          field4: 'extra',
          field5: 'name'
        }
      });
    else if (int === 5)
      this.setState({
        passwordManager: int,
        fields:{
          field1: 'password',
          field2: 'login',
          field3: 'note',
          field4: 'name',
          field5: 'type',
          field6: 'url'
        }
      });
    else if (int === 6)
      this.setState({
        passwordManager: int,
        fields:{
          field1: 'name',
          field2: 'login',
          field3: 'password',
          field4: 'url'
        }
      });
    else if (int === 8)
      this.setState({
        passwordManager: int,
        fields:{
          field1: 'name',
          field2: 'url',
          field3: 'note',
          field4: 'login',
          field5: 'password'
        }
      });
    else
      this.setState({
        passwordManager: int,
        fields: {
          field1: 'name',
          field2: 'url',
          field3: 'login',
          field4: 'password',
          field5: '-',
          field6: '-'
        }
      });
  };
  checkThirdField = (website_id, id) => {
    const websiteThirdField = this.props.websites.filter(item => {
        return item.id === website_id && item.information.length > 2;
    });
    if (websiteThirdField.length > 0) {
      const keys = Object.keys(websiteThirdField[0].information);
      const accounts = this.state.errorAccounts.slice();
      const accountsPending = [];
      this.state.accountsPending.map(item => {
        if (item.id !== id)
          accountsPending.push(item);
        else if (item.id === id) {
          item.thirdField = {name: keys[2], value: ""};
          if (this.state.selectedProfile !== -1)
            item.locationProfile = this.state.selectedProfile;
          else
            item.locationRoom = {team_id: this.state.selectedTeam, room_id: this.state.selectedRoom};
          accounts.push(item);
        }
      });
      this.setState({accountsPending: accountsPending, errorAccounts: accounts});
    }
  };
  importErrorAccounts = () => {
    let calls = [];
    this.state.errorAccounts.map(app => {
      if (app.locationProfile) {
        calls.push(this.props.dispatch(catalogAddClassicApp({
          name: app.name,
          website_id: app.website_id,
          profile_id: app.locationProfile,
          account_information: {login: app.login, password: app.password, [app.thirdField.name]: app.thirdField.value}
        })));
      }
      else {
        const receivers = this.props.teams[app.locationRoom.team_id].rooms[app.locationRoom.room_id].team_user_ids.map(item => {
          return {id: item, allowed_to_see_password: this.props.teams[app.locationRoom.team_id].team_users[item].role > 1}
        }).reduce((prev, curr) => {
          return {...prev, [curr.id]: {allowed_to_see_password: curr.allowed_to_see_password}}
        }, {});
        calls.push(this.props.dispatch(teamCreateSingleApp({
          team_id: app.locationRoom.team_id,
          channel_id: app.locationRoom.room_id,
          name: app.name,
          website_id: app.website_id,
          password_reminder_interval: 0,
          account_information: {login: app.login, password: app.password, [app.thirdField.name]: app.thirdField.value},
          description: '',
          receivers: receivers
        })));
      }
      calls.push(this.props.dispatch(deleteImportedAccount({
        id: app.id
      })));
    });
    Promise.all(calls.map(reflect)).then(response => {
      this.setState({errorAccounts: [], view: 1, separator: ',',});
    }).catch(err => {
      this.setState({error: err});
    });
  };
  importAccounts = () => {
    this.setState({loadingSending: true});
    let calls = [];
    if (this.state.selectedProfile > 0) {
      this.state.accountsPending.map(app => {
        let thirdField = false;
        if (app.website_id !== -1 && app.login !== '' && app.password !== '') {
          // if (this.checkThirdField(app.website_id, app.id) === false) {
            calls.push(this.props.dispatch(catalogAddClassicApp({
              name: app.name,
              website_id: app.website_id,
              profile_id: Object.keys(this.props.profiles)[0],
              account_information: {login: app.login, password: app.password}
            })));
          // }
          // else
          //   thirdField = true;
        }
        else {
          if (app.login !== '' && app.password !== '') {
            calls.push(this.props.dispatch(catalogAddAnyApp({
              name: app.name,
              url: app.url,
              img_url: app.logo,
              profile_id: Object.keys(this.props.profiles)[0],
              account_information: {login: app.login, password: app.password},
              connection_information: {
                login: {type: "text", priority: 0, placeholder: "Login"},
                password: {type: "password", priority: 1, placeholder: "Password"}
              },
              credentials_provided: false
            })));
          }
          else {
            calls.push(this.props.dispatch(catalogAddBookmark({
              name: app.name,
              url: app.url,
              img_url: app.logo !== '' ? app.logo : '/resources/icons/link_app.png',
              profile_id: Object.keys(this.props.profiles)[0]
            })));
          }
        }
        if (!thirdField)
          calls.push(this.props.dispatch(deleteImportedAccount({
            id: app.id
          })));
      });
    }
    else if (this.state.selectedProfile === -1 && this.state.selectedRoom > 0)
      calls = this.importAccountsRoom();
    else if (this.state.selectedProfile === 0)
      calls = this.importAccountsNewProfile();
    else if (this.state.selectedRoom === 0)
      calls = this.importAccountsNewRoom();
      let teams = {};
      Object.keys(this.props.teams).map(item => {
        const team = this.props.teams[item];
        teams[item] = {...team};
        teams[item].rooms = {};
        Object.keys(team.rooms).map(room_id => {
          teams[item].rooms[room_id] = {...team.rooms[room_id]}
        });
      });
      const keys = Object.keys(teams);
      const roomName = {};
      const roomAdded = {};
      keys.map(item => {
        roomName[item] = "";
        roomAdded[item] = false;
        return item;
      });
      return Promise.all(calls.map(reflect)).then(response => {
        this.setState({
          accountsPending: [],
          selectedProfile: -1,
          selectedRoom: -1,
          selectedTeam: -1,
          loadingSending: false,
          roomAdded: roomAdded,
          error: '',
          roomName: roomName,
          location: '',
          profiles: Object.assign({}, this.props.profiles),
          teamsInState: teams
        });
        if (this.state.importedAccounts.length < 1)
          this.setState({view: 1, separator: ',',});
      }).catch(err => {
        this.setState({error: err, loadingSending: false});
      });
  };
  importAccountsNewProfile = () => {
    let calls = [];
    this.props.dispatch(createProfile({
      name: 'Me',
      column_index: 1
    })).then(response => {
      this.state.accountsPending.map(app => {
        let thirdField = false;
        if (app.website_id !== -1 && app.login !== '' && app.password !== '') {
          // if (this.checkThirdField(app.website_id, app.id) === false) {
            calls.push(this.props.dispatch(catalogAddClassicApp({
              name: app.name,
              website_id: app.website_id,
              profile_id: response.id,
              account_information: {login: app.login, password: app.password}
            })));
          // }
          // else
          //   thirdField = true;
        }
        else {
          if (app.login !== '' && app.password !== '') {
            calls.push(this.props.dispatch(catalogAddAnyApp({
              name: app.name,
              url: app.url,
              img_url: app.logo,
              profile_id: response.id,
              account_information: {login: app.login, password: app.password},
              connection_information: {
                login: {type: "text", priority: 0, placeholder: "Login"},
                password: {type: "password", priority: 1, placeholder: "Password"}
              },
              credentials_provided: false
            })));
          }
          else {
            calls.push(this.props.dispatch(catalogAddBookmark({
              name: app.name,
              url: app.url,
              img_url: app.logo !== '' ? app.logo : '/resources/icons/link_app.png',
              profile_id: response.id
            })));
          }
        }
        if (!thirdField)
          calls.push(this.props.dispatch(deleteImportedAccount({
            id: app.id
          })));
      });
      let newTeams = {};
      Object.keys(this.props.teams).map(item => {
        const team  = this.props.teams[item];
        newTeams[item] = {...team};
        newTeams[item].rooms = {};
        Object.keys(team.rooms).map(room_id =>  {
          newTeams[item].rooms[room_id] = {...team.rooms[room_id]}
        });
      });
      const keys = Object.keys(newTeams);
      const roomName = {};
      const roomAdded = {};
      keys.map(item => {
        roomName[item] = "";
        roomAdded[item] = false;
        return item;
      });
      return Promise.all(calls.map(reflect)).then(response => {
        this.setState({
          accountsPending: [],
          selectedProfile: -1,
          selectedRoom: -1,
          selectedTeam: -1,
          loadingSending: false,
          roomAdded: roomAdded,
          error: '',
          roomName: roomName,
          location: '',
          profiles: Object.assign({}, this.props.profiles),
          teamsInState: newTeams
        });
        if (this.state.importedAccounts.length < 1)
          this.setState({view: 1, separator: ',',});
      }).catch(err => {
        this.setState({error: err, loadingSending: false});
      });
    });
  };
  importAccountsRoom = async () => {
    const receivers = this.props.teams[this.state.selectedTeam].rooms[this.state.selectedRoom].team_user_ids.map(item => {
      return {id: item, allowed_to_see_password: this.props.teams[this.state.selectedTeam].team_users[item].role > 1}
    }).reduce((prev, curr) => {
      return {...prev, [curr.id]: {allowed_to_see_password: curr.allowed_to_see_password}}
    }, {});
    const receiversAnyApp = this.props.teams[this.state.selectedTeam].rooms[this.state.selectedRoom].team_user_ids.map(item => {
      return {id: item, allowed_to_see_password: true}
    }).reduce((prev, curr) => {
      return {...prev, [curr.id]: {allowed_to_see_password: curr.allowed_to_see_password}}
    }, {});
    const receiversLink = this.props.teams[this.state.selectedTeam].rooms[this.state.selectedRoom].team_user_ids;
    for (let i = 0; i < this.state.accountsPending.length; i++) {
      const app = this.state.accountsPending[i];
      if (app.website_id !== -1 && app.login !== '' && app.password !== '') {
        await this.props.dispatch(teamCreateSingleApp({
          team_id: this.state.selectedTeam,
          channel_id: this.state.selectedRoom,
          name: app.name,
          website_id: app.website_id,
          password_reminder_interval: 0,
          account_information: {login: app.login, password: app.password},
          description: '',
          receivers: receivers
        }));
      }
      else {
        if (app.login !== '' && app.password !== '') {
          await this.props.dispatch(teamCreateAnySingleCard({
            team_id: this.state.selectedTeam,
            channel_id: this.state.selectedRoom,
            name: app.name,
            description: '',
            password_reminder_interval: 0,
            url: app.url,
            img_url: app.logo,
            account_information: {login: app.login, password: app.password},
            connection_information: {
              login: {type: "text", priority: 0, placeholder: "Login"},
              password: {type: "password", priority: 1, placeholder: "Password"}
            },
            credentials_provided: false,
            receivers: receiversAnyApp
          }));
        }
        else {
          await this.props.dispatch(teamCreateLinkCard({
            team_id: this.state.selectedTeam,
            channel_id: this.state.selectedRoom,
            name: app.name,
            description: '',
            url: app.url,
            img_url: app.logo,
            receivers: receiversLink
          }));
        }
      }
      await this.props.dispatch(deleteImportedAccount({
        id: app.id
      }));
    }
    let newTeams = {};
    Object.keys(this.props.teams).map(item => {
      const team  = this.props.teams[item];
      newTeams[item] = {...team};
      newTeams[item].rooms = {};
      Object.keys(team.rooms).map(room_id =>  {
        newTeams[item].rooms[room_id] = {...team.rooms[room_id]}
      });
    });
    const keys = Object.keys(newTeams);
    const roomName = {};
    const roomAdded = {};
    keys.map(item => {
      roomName[item] = "";
      roomAdded[item] = false;
      return item;
    });
    this.setState({
      accountsPending: [],
      selectedProfile: -1,
      selectedRoom: -1,
      selectedTeam: -1,
      loadingSending: false,
      roomAdded: roomAdded,
      error: '',
      roomName: roomName,
      location: '',
      profiles: Object.assign({}, this.props.profiles),
      teamsInState: newTeams
    });
    if (this.state.importedAccounts.length < 1)
      this.setState({view: 1, separator: ',',})
  };
  importAccountsNewRoom = async () => {
    const receivers = {[this.props.teams[this.state.selectedTeam].my_team_user_id]: {allowed_to_see_password: true}};
    const receiversLink = [this.props.teams[this.state.selectedTeam].my_team_user_id];

    const response = await this.props.dispatch(createTeamChannel({
      team_id: this.state.selectedTeam,
      name: this.state.roomName[this.state.selectedTeam],
      purpose: ''
    }));
    const resp2 = await  this.props.dispatch(addTeamUserToChannel({
      team_id: this.state.selectedTeam,
      channel_id: response.id,
      team_user_id: this.props.teams[this.state.selectedTeam].my_team_user_id
    }));
    for (let i = 0; i < this.state.accountsPending.length; i++) {
      const app = this.state.accountsPending[i];
      if (app.website_id !== -1 && app.login !== '' && app.password !== '') {
        await this.props.dispatch(teamCreateSingleApp({
          team_id: this.state.selectedTeam,
          channel_id: response.id,
          name: app.name,
          website_id: app.website_id,
          password_reminder_interval: 0,
          account_information: {login: app.login, password: app.password},
          description: '',
          receivers: receivers
        }));
      }
      else {
        if (app.login !== '' && app.password !== '') {
          await this.props.dispatch(teamCreateAnySingleCard({
            team_id: this.state.selectedTeam,
            channel_id: response.id,
            name: app.name,
            description: '',
            password_reminder_interval: 0,
            url: app.url,
            img_url: app.logo,
            account_information: {login: app.login, password: app.password},
            connection_information: {
              login: {type: "text", priority: 0, placeholder: "Login"},
              password: {type: "password", priority: 1, placeholder: "Password"}
            },
            credentials_provided: false,
            receivers: receivers
          }));
        }
        else {
          await this.props.dispatch(teamCreateLinkCard({
            team_id: this.state.selectedTeam,
            channel_id: response.id,
            name: app.name,
            description: '',
            url: app.url,
            img_url: app.logo,
            receivers: receiversLink
          }));
        }
      }
      await this.props.dispatch(deleteImportedAccount({
        id: app.id
      }));
    }
    let newTeams = {};
    Object.keys(this.props.teams).map(item => {
      const team  = this.props.teams[item];
      newTeams[item] = {...team};
      newTeams[item].rooms = {};
      Object.keys(team.rooms).map(room_id =>  {
        newTeams[item].rooms[room_id] = {...team.rooms[room_id]}
      });
    });
    const keys = Object.keys(newTeams);
    const roomName = {};
    const roomAdded = {};
    keys.map(item => {
      roomName[item] = "";
      roomAdded[item] = false;
      return item;
    });
    this.setState({
      accountsPending: [],
      selectedProfile: -1,
      selectedRoom: -1,
      selectedTeam: -1,
      loadingSending: false,
      roomAdded: roomAdded,
      error: '',
      roomName: roomName,
      location: '',
      profiles: Object.assign({}, this.props.profiles),
      teamsInState: newTeams
    });
    if (this.state.importedAccounts.length < 1)
      this.setState({view: 1, separator: ',',})
  };
  render() {
    return (
      <div id='importations' className={this.state.view === 4 ? 'step4' : null}>
        {(this.state.loading === true || this.props.fetching) && <Loader active />}
        {(this.state.view === 1 && this.state.loading === false && !this.props.fetching) &&
          <ChoosePasswordManager
            {...this.props}
            passwordManager={this.state.passwordManager}
            next={this.changeView}
            choosePasswordManager={this.choosePasswordManager}/>}
        {(this.state.view === 2 && this.state.passwordManager !== 2 && this.state.loading === false) &&
          <Explication
            back={this.back}
            next={this.changeView}
            passwordManager={this.state.passwordManager}/>}
        {(this.state.view === 2 && this.state.passwordManager === 2 && this.state.loading === false) &&
          <ChromeFirstStep
            login={this.state.chromeLogin}
            password={this.state.chromePassword}
            onChange={this.handleInput}
            error={this.state.error}
            back={this.back}
            next={this.changeView}/>}
        {(this.state.view === 3 && this.state.passwordManager !== 2 && this.state.loading === false) &&
          <PasteStep
            back={this.back}
            next={this.changeView}
            onChange={this.handleInput}
            onChangeField={this.changeOrderField}
            pasted={this.state.paste !== ''}
            error={this.state.error}
            fields={this.state.fields}
            passwordManager={this.state.passwordManager}/>}
        {(this.state.view === 3 && this.state.passwordManager === 2 && this.state.loading === false) &&
          <ChromeSecondStep
            back={this.back}
            next={this.changeView}
            error={this.state.error}/>}
        {(this.state.view === 4 && this.state.importedAccounts && !this.props.fetching && this.state.loading === false) &&
          <DisplayAccounts
            {...this.props}
            getLogo={this.getLogo}
            onChange={this.handleInput}
            onChangeRoomName={this.handleRoomName}
            handleAppInfo={this.handleAppInfo}
            toPending={this.toPending}
            roomName={this.state.roomName}
            profilesInState={this.state.profiles}
            selectProfile={this.selectProfile}
            teamsInState={this.state.teamsInState}
            selectRoom={this.selectRoom}
            createRoom={this.createRoom}
            roomAdded={this.state.roomAdded}
            importAccounts={this.importAccounts}
            cancelPending={this.cancelPending}
            onChangeField={this.changeOrderFieldStep4}
            deleteAccount={this.deleteAccount}
            loadingSending={this.state.loadingSending}
            error={this.state.error}
            fields={this.state.fields}
            fieldProblem={this.state.fieldProblem}
            location={this.state.location}
            importedAccounts={this.state.importedAccounts}
            accountsPending={this.state.accountsPending}
            selectedProfile={this.state.selectedProfile}/>}
        {(this.state.view === 5 && this.state.errorAccounts && this.state.loading === false) &&
          <ErrorAccounts
            errorAccounts={this.state.errorAccounts}
            handleErrorAppInfo={this.handleErrorAppInfo}
            importErrorAccounts={this.importErrorAccounts}
            deleteErrorAccount={this.deleteErrorAccount}
            fields={this.state.fields}/>}
      </div>
    )
  }
}

export default Importations;