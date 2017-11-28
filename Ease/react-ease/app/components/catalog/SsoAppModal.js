import React from 'react';
import { Image, Icon, Form, Button, Message, Checkbox, Segment, Input, Container, List, Grid } from 'semantic-ui-react';
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import InputModalCatalog from './InputModalCatalog';
import {selectItemFromListById} from "../../utils/helperFunctions";
import {reduxActionBinder} from "../../actions/index";
import {connect} from "react-redux";
import {dashboard} from "../../utils/post_api";
import {reflect} from "../../utils/utils";
import ChooseAppLocationModal from './ChooseAppLocationModal';
import ChooseTypeAppModal from './ChooseTypeAppModal';

class SecondStep extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      loading: false,
      errorMessage: ''
    }
  }
  checkDouble = (double, login) => {
    const doubleLogin = double.filter(account => {
      if (login === account)
        return true;
    });
    if (doubleLogin.length) {
      return false;
    }
    else {
      return true;
    }
  };
  render() {
    const {
      logo,
      name,
      toggleBookmark,
      logWith_websites,
      selectAccount,
      addGoogleAccount} = this.props;
    let double = [];
    const mailList = logWith_websites.map(item => {
      return item.personal_apps.map(key => {
        if (key.account_information) {
          if (this.checkDouble(double, key.account_information.login) === true) {
            double.push(key.account_information.login);
            return (
              <List.Item key={key.id} as="p"
                         className="overflow-ellipsis">
                <a
                  onClick={e => selectAccount(key.account_information.login, key.website_id, key.id)}>
                  <Icon name='user circle'/>
                  <span>{key.account_information.login}</span>
                </a>
              </List.Item>
            )
          }
        }
      })
    });
    return (
      <Form class="container" error={this.state.errorMessage.length > 0}>
        <Form.Field class="display-flex align_items_center" style={{marginBottom: '30px'}}>
          <div className="squared_image_handler">
            <Image src={logo} alt="Website logo"/>
          </div>
          <span className='app_name'>{name}</span>
        </Form.Field>
        <Form.Field>
          <p style={{display: 'inline-block', fontSize: '20px', color: '#414141'}}><strong>Add as
            Bookmark</strong></p>
          <Checkbox toggle onClick={toggleBookmark}
                    style={{marginLeft: '20px', marginBottom: '0'}}/>
        </Form.Field>
        <Form.Field>
          <Segment.Group className='connectWithGoogle'>
            <Segment className='first'>
              <Icon name='google'/>
              Sign in with your Google Account
            </Segment>
            <Segment>
              {mailList}
              <p>
                <a onClick={addGoogleAccount}>
                  <Icon name='add square'/>
                  Add a new Google Account</a>
              </p>
            </Segment>
          </Segment.Group>
        </Form.Field>
        <Message error content={this.state.errorMessage}/>
        <Button
          disabled
          type="submit"
          positive
          loading={this.state.loading}
          class="modal-button uppercase"
          content={'CONFIRM'}/>
      </Form>
    )
  }
}

class ThirdStep extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      loading: false,
      errorMessage: ''
    }
  }
  checkNameSSO = (item) => {
    let name = null;
    this.props.ssoSelected.filter(sso => {
      if (sso.website_id === item.id) {
        return name = sso.name;
      }
    });
    return name;
  };
  checkNameEmpty = () => {
    const empty = this.props.ssoSelected.map(item => {
      if (item.name === '')
        return item;
    });
    const response = empty.filter(item => {
      if (item)
        return true
    });
    if (response.length)
      return false;
    else
      return true;
  };
  confirm = () => {
    this.setState({loading: true, errorMessage: ''});
    if (this.state.appIdOfLoginSelected === null) {
      this.props.catalogAddMultipleClassicApp({
        profile_id: this.props.selectedProfile,
        apps_to_add: this.props.ssoSelected,
        account_information: {login: this.props.login, password: this.props.password}
      }).then(r => {
        this.setState({loading: false});
        this.props.showCatalogAddSSOAppModal({active: false})
      }).catch(err => {
        this.setState({loading: false});
        this.setState({errorMessage: err});
      });
    }
    else {
      const calls = this.props.ssoSelected.map(item => {
        return this.props.catalogAddClassicAppSameAs({
          website_id: item.website_id,
          name: item.name,
          same_app_id: this.props.appIdOfLoginSelected,
          profile_id: this.props.selectedProfile
        });
      });
      const response = calls.filter(item => {
        if (item)
          return true
      });
      Promise.all(response.map(reflect)).then(r => {
        this.setState({loading: false});
        this.props.showCatalogAddSSOAppModal({active: false})
      }).catch(err => {
        this.setState({loading: false});
        this.setState({errorMessage: err});
      });
    }
  };
  render() {
    const {
      logo,
      name,
      accountGoogleSelected,
      logWith_websites,
      back} = this.props;

    const gridSsoWebsites = logWith_websites.map(item => {
      if (!item.personal_apps.filter(key => {
          if (key.account_information) {
            if (key.account_information.login === this.state.login) return true
          }
        }).length && this.props.modal.website.id !== item.id) {
        return (
          <Grid.Column key={item.id} className="showSegment">
            <List.Item as='a' active={this.props.checkActive(item.id)}
                       onClick={e => this.props.selectSSO(item.id, item.name)}>
              <div className='appLogo' onClick={e => this.props.deselectSSO(item.id)}>
                <Image src={item.logo}/>
                <Icon className='iconCheck' name="check"/>
              </div>
              <Input disabled={!this.props.checkActive(item.id)}
                     focus={this.props.checkActive(item.id)}
                     value={this.checkNameSSO(item) === null ? item.name : this.checkNameSSO(item)}
                     name="ssoAppName"
                     transparent
                     onChange={e => this.props.editSSO(e, item)}
                     class='create_profile_input'
                     placeholder='Change name of your App'/>
              <Icon className='iconWrite' name="write"/>
            </List.Item>
          </Grid.Column>)
      }
      else if (item.personal_apps.filter(key => {
          if (key.account_information) {
            if (key.account_information.login === this.state.login)
              return true
          }
        }).length && this.props.modal.website.id !== item.id) {
        return (
          <Grid.Column key={item.id} className="showSegment">
            <List.Item as='a' active>
              <div className='appLogo'>
                <Image src={item.logo}/>
                <Icon className='iconCheck' name="check"/>
              </div>
              <Input disabled
                     value={item.name}
                     name="ssoAppName"
                     transparent
                     class="create_profile_input"
                     placeholder='Change name of your App'/>
            </List.Item>
          </Grid.Column>)
      }
    });
    return (
      <Form class="container" error={this.state.errorMessage.length > 0} onSubmit={this.confirm}>
        <Form.Field class="display-flex align_items_center" style={{marginBottom: '30px'}}>
          <div className="squared_image_handler">
            <Image src={logo} alt="Website logo"/>
          </div>
          <span className='app_name'>{name}</span>
        </Form.Field>
        <Form.Field>
          <p className='backPointer' onClick={back}><Icon name='arrow left'/>Back</p>
        </Form.Field>
        <Form.Field>
          <Segment.Group className='connectWithGoogle'>
            <Segment className='first overflow-ellipsis'>
              <Icon name='google'/>
              {accountGoogleSelected}
            </Segment>
          </Segment.Group>
        </Form.Field>
        <Form.Field>
          <p>You can connect your Google account with other apps. <strong>Click to add them</strong>.</p>
        </Form.Field>
        <Form.Field>
          <Segment className='pushable ssoListSegment'>
            <Grid columns={2} className='ssoListGrid'>
              {gridSsoWebsites}
            </Grid>
          </Segment>
        </Form.Field>
        <Message error content={this.state.errorMessage}/>
        <Button
          disabled={this.checkNameEmpty() === false}
          type="submit"
          positive
          loading={this.state.loading}
          class="modal-button uppercase"
          content={'CONFIRM'}/>
      </Form>
    )
  }
}

class NewSsoAccount extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      loading: false,
      errorMessage: ''
    }
  }
  render() {
    const {
      name,
      logo,
      toggleBookmark,
      handleInput,
      confirm} = this.props;
    return (
      <Form class="container" error={this.state.errorMessage.length > 0} onSubmit={confirm}>
        <Form.Field class="display-flex align_items_center" style={{marginBottom: '30px'}}>
          <div className="squared_image_handler">
            <Image src={logo} alt="Website logo"/>
          </div>
          <span className='app_name'>{name}</span>
        </Form.Field>
        <Form.Field>
          <p style={{display: 'inline-block', fontSize: '20px', color: '#414141'}}><strong>Add as
            Bookmark</strong></p>
          <Checkbox toggle onClick={toggleBookmark}
                    style={{marginLeft: '20px', marginBottom: '0'}}/>
        </Form.Field>
        <InputModalCatalog
          nameLabel='Login'
          nameInput='login'
          inputType='text'
          placeholderInput='Your login'
          handleInput={handleInput}
          iconLabel='user'/>
        <InputModalCatalog
          nameLabel='Password'
          nameInput='password'
          inputType='password'
          placeholderInput='Your password'
          handleInput={handleInput}
          iconLabel='lock'/>
        <Message error content={this.state.errorMessage}/>
        <Button
          disabled={!this.props.login || !this.props.password}
          type="submit"
          positive
          loading={this.state.loading}
          class="modal-button uppercase"
          content={'CONFIRM'}/>
      </Form>
    )
  }
}

class AddBookmark extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      url: this.props.landing_url,
      loading: false,
      errorMessage: ''
    }
  }
  handleInput = (e, {name, value}) => this.setState({[name]: value});
  confirm = () => {
    this.setState({loading: true, errorMessage: ''});
    this.props.catalogAddBookmark({
      name: this.props.name,
      profile_id: this.props.selectedProfile,
      url: this.state.url,
      img_url: this.props.logo
    }).then(r => {
      this.setState({loading: false});
      this.props.showCatalogAddSSOAppModal({active: false})
    }).catch(err => {
      this.setState({loading: false});
      this.setState({errorMessage: err});
    });
  };
  render() {
    const {
      name,
      logo,
      toggleBookmark} = this.props;
    return (
      <Form class="container" error={this.state.errorMessage.length > 0} onSubmit={this.confirm}>
        <Form.Field class="display-flex align_items_center" style={{marginBottom: '30px'}}>
          <div className="squared_image_handler">
            <Image src={logo} alt="Website logo"/>
          </div>
          <span className='app_name'>{name}</span>
        </Form.Field>
        <Form.Field>
          <p style={{display: 'inline-block', fontSize: '20px', color: '#414141'}}><strong>Add as
            Bookmark</strong></p>
          <Checkbox toggle checked onClick={toggleBookmark}
                    style={{marginLeft: '20px', marginBottom: '0'}}/>
        </Form.Field>
        <InputModalCatalog
          nameLabel='Here is the link'
          nameInput='url'
          inputType='url'
          placeholderInput='Url'
          handleInput={this.handleInput}
          iconLabel='home'
          valueInput={this.state.url} />
        <Message error content={this.state.errorMessage}/>
        <Button
          disabled={this.state.url.length < 1 || this.state.loading}
          type="submit"
          positive
          loading={this.state.loading}
          class="modal-button uppercase"
          content={'CONFIRM'}/>
      </Form>
    )
  }
}

@connect(store => ({
  catalog: store.catalog,
  modal: store.teamModals.catalogAddSSOAppModal,
  dashboard: store.dashboard
}), reduxActionBinder)
class SsoAppModal extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      website: this.props.modal.website,
      name: this.props.modal.website.name,
      login: '',
      password: '',
      addBookmark: false,
      addGoogleAccount: false,
      accountGoogleSelected: '',
      profiles: [],
      profileName: '',
      profileAdded: false,
      selectedProfile: -1,
      selectedTeam: -1,
      selectedRoom: -1,
      addingProfile: false,
      view: 1,
      websiteIdOfLoginSelected: null,
      appIdOfLoginSelected: null,
      logWith_websites: [],
      ssoSelected: []
    }
  }
  componentWillMount() {
    this.setState({loading: true});
    const profiles = Object.keys(this.props.dashboard.profiles).map(id => (
      this.props.dashboard.profiles[id]
    ));
    this.setState({profiles: profiles});
    const ssoList = this.props.catalog.websites.filter(item => {
      return item.sso_id === this.props.modal.website.sso_id;
    });
    let ssoWebsiteId = [];
    ssoList.map(item => {
      ssoWebsiteId.push(item.id);
    });
    let logwith = ssoWebsiteId.map(item => {
      let website = selectItemFromListById(this.props.catalog.websites, item);
      let apps = [];
      Object.keys(this.props.dashboard.apps).map(item => {
        if (this.props.dashboard.apps[item].type !== 'teamLinkApp' && this.props.dashboard.apps[item].type !== 'linkApp') {
          if (this.props.dashboard.apps[item].website.id === website.id)
            apps.push(this.props.dashboard.apps[item]);
        }
      });
      website.personal_apps = apps;
      return website;
    });
    this.setState({logWith_websites: logwith, loading: false});
  }
  handleInput = (e, {name, value}) => this.setState({[name]: value});
  toggleBookmark = () => {
    if (this.state.addBookmark)
      this.setState({addBookmark: false, errorMessage: ''});
    else
      this.setState({addBookmark: true, login: '', password: '', accountGoogleSelected: '', errorMessage: ''});
  };
  back = () => {
    if (this.checkAccountGoogle() === true)
      this.setState({
        accountGoogleSelected: '',
        view: 2,
        login: '',
        password: '',
        addGoogleAccount: false,
        errorMessage: '',
        ssoSelected: [{website_id: this.props.modal.website.id, name: this.state.name}]
      });
    else
      this.setState({
        accountGoogleSelected: '',
        view: 2,
        login: '',
        password: '',
        addGoogleAccount: true,
        errorMessage: '',
        ssoSelected: [{website_id: this.props.modal.website.id, name: this.state.name}]
      });
  };
  selectAccount = (account, website_id, app_id) => {
    this.setState({
      accountGoogleSelected: account,
      view: 3,
      login: account,
      websiteIdOfLoginSelected: website_id,
      appIdOfLoginSelected: app_id
    });
  };
  addGoogleAccount = () => {
    this.setState({addGoogleAccount: true, accountGoogleSelected: ''});
  };
  addMainAppToSSOSelected = () => {
    const newSelectedSSO = this.state.ssoSelected.slice();
    newSelectedSSO.push({website_id: this.props.modal.website.id, name: this.state.name});
    if (this.checkAccountGoogle() === true)
      this.setState({ssoSelected: newSelectedSSO, view: 2});
    else
      this.setState({ssoSelected: newSelectedSSO, view: 2, addGoogleAccount: true});
  };
  checkAccountGoogle = () => {
    const account = this.state.logWith_websites.map(item => {
      return item.personal_apps.map(key => {
        if (key.account_information) {
          return key;
        }
      }).filter(key2 => {
        if (key2)
          return true
      });
    });
    const response = account.filter(item => {
      if (item.length)
        return true
    });
    if (response.length)
      return true;
    else
      return false;
  };
  checkActive = (id) => {
    let check = false;
    this.state.ssoSelected.filter(item => {
      if (item.website_id === id)
        return check = true;
    });
    return check;
  };
  selectSSO = (id, name) => {
    const newSelectedSSO = this.state.ssoSelected.slice();
    if (!this.checkActive(id)) {
      newSelectedSSO.push({website_id: id, name: name});
      this.setState({ssoSelected: newSelectedSSO});
    }
  };
  deselectSSO = (id) => {
    const newSelectedSSO = this.state.ssoSelected.slice();
    if (this.checkActive(id)) {
      let key = 0;
      this.state.ssoSelected.filter((item, keyItem) => {
        if (item.website_id === id)
          return key = keyItem;
      });
      newSelectedSSO.splice(key, 1);
      this.setState({ssoSelected: newSelectedSSO});
    }
  };
  editSSO = (e, sso) => {
    const newSelectedSSO = this.state.ssoSelected.slice();
    let key = 0;
    this.state.ssoSelected.filter((item, keyItem) => {
      if (item.website_id === sso.id)
        return key = keyItem;
    });
    newSelectedSSO.splice(key, 1);
    newSelectedSSO.push({website_id: sso.id, name: e.target.value});
    this.setState({ssoSelected: newSelectedSSO});
  };
  selectProfile = (id) => {
    this.setState({selectedProfile: id, selectedTeam: -1, selectedRoom: -1});
  };
  selectRoom = (teamId, roomId) => {
    this.setState({selectedTeam: teamId, selectedRoom: roomId, selectedProfile: -1});
  };
  addProfile = (profile) => {
    let profiles = this.state.profiles.slice();
    profiles.push(profile);
    this.setState({profiles: profiles, selectedProfile: profile.id});
  };
  createProfile = () => {
    const newProfile = {id: 0, name: this.state.profileName};
    if (this.state.profileName.length === 0)
      return;
    this.addProfile(newProfile);
    this.setState({profileAdded: true});
  };
  confirm = () => {
    if (this.state.view === 1) {
      if (this.state.selectedProfile !== -1)
        this.addMainAppToSSOSelected();
      else
        this.setState({view: 4})
    }
    else if (this.state.addGoogleAccount) {
      this.setState({accountGoogleSelected: this.state.login, view: 3, addGoogleAccount: false});
    }
  };
  close = () => {
    this.props.showCatalogAddSSOAppModal({active: false});
  };
  render() {
    return (
      <SimpleModalTemplate
        onClose={e => {
          this.props.showCatalogAddSSOAppModal({active: false})
        }}
        headerContent={'Setup your App'}>
        {this.state.view === 1 &&
          <ChooseAppLocationModal
            website={this.state.website}
            appName={this.state.name}
            loading={this.state.loading}
            profiles={this.state.profiles}
            handleInput={this.handleInput}
            profileAdded={this.state.profileAdded}
            createProfile={this.createProfile}
            selectedProfile={this.state.selectedProfile}
            selectedRoom={this.state.selectedRoom}
            addProfile={this.addProfile}
            confirm={this.confirm}
            selectProfile={this.selectProfile}
            selectRoom={this.selectRoom}/>}
        {(this.state.view === 2 && !this.state.addBookmark && !this.state.addGoogleAccount) &&
          <SecondStep
            {...this.props}
            logo={this.props.modal.website.logo}
            name={this.props.modal.website.name}
            logWith_websites={this.state.logWith_websites}
            addGoogleAccount={this.addGoogleAccount}
            toggleBookmark={this.toggleBookmark}
            selectAccount={this.selectAccount} />}
        {this.state.view === 3 &&
          <ThirdStep
            {...this.props}
            logo={this.props.modal.website.logo}
            name={this.props.modal.website.name}
            accountGoogleSelected={this.state.accountGoogleSelected}
            logWith_websites={this.state.logWith_websites}
            selectedProfile={this.state.selectedProfile}
            login={this.state.login}
            password={this.state.password}
            appIdOfLoginSelected={this.state.appIdOfLoginSelected}
            ssoSelected={this.state.ssoSelected}
            checkActive={this.checkActive}
            selectSSO={this.selectSSO}
            deselectSSO={this.deselectSSO}
            editSSO={this.editSSO}
            back={this.back}/> }
        {(this.state.addGoogleAccount && !this.state.addBookmark) &&
          <NewSsoAccount
            {...this.props}
            logo={this.props.modal.website.logo}
            name={this.props.modal.website.name}
            login={this.state.login}
            password={this.state.password}
            toggleBookmark={this.toggleBookmark}
            handleInput={this.handleInput}
            confirm={this.confirm} />}
        {this.state.addBookmark &&
          <AddBookmark
            {...this.props}
            landing_url={this.props.modal.website.landing_url}
            logo={this.props.modal.website.logo}
            name={this.props.modal.website.name}
            toggleBookmark={this.toggleBookmark}
            selectedProfile={this.state.selectedProfile} />}
        {this.state.view === 4 &&
          <ChooseTypeAppModal
            {...this.props}
            website={this.state.website}
            appName={this.state.name}
            team_id={this.state.selectedTeam}
            room_id={this.state.selectedRoom}
            close={this.close}/>}
      </SimpleModalTemplate>
    )
  }
}

export default SsoAppModal;