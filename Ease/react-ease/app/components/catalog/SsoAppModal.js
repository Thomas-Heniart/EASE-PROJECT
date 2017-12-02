import React from 'react';
import { Image, Icon, Form, Button, Message, Checkbox, Segment, Input, Label, List, Grid } from 'semantic-ui-react';
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import InputModalCatalog from './InputModalCatalog';
import {reduxActionBinder} from "../../actions/index";
import {connect} from "react-redux";
import {dashboard} from "../../utils/post_api";
import {reflect, transformWebsiteInfoIntoList, credentialIconType, transformCredentialsListIntoObject} from "../../utils/utils";
import ChooseAppLocationModal from './ChooseAppLocationModal';
import ChooseTypeAppModal from './ChooseTypeAppModal';
import {createSsoGroup, createProfile} from "../../actions/dashboardActions";
import {catalogAddSsoApp, testCredentials} from "../../actions/catalogActions";

const CredentialInput = ({item, onChange}) => {
  return (
    <Form.Field>
      <label style={{ fontSize: '16px', fontWeight: '300', color: '#424242' }}>{item.placeholder}</label>
      <Input size="large"
             autoFocus={item.autoFocus}
             class="modalInput team-app-input"
             required
             autoComplete='on'
             name={item.name}
             onChange={onChange}
             label={<Label><Icon name={credentialIconType[item.name]}/></Label>}
             labelPosition="left"
             placeholder={item.placeholder}
             value={item.value}
             type={item.type}/>
    </Form.Field>
  )
};

class SecondStep extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      loading: false,
      errorMessage: ''
    }
  }
  render() {
    const {
      logo,
      name,
      toggleBookmark,
      ssoGroup,
      selectAccount,
      addGoogleAccount} = this.props;
    const mailList = ssoGroup.map(key => (
      <List.Item key={key.id} as="p" className="overflow-ellipsis">
        <a onClick={e => selectAccount(key)}>
          <Icon name='user circle'/>
          <span>{key.account_information.login}</span>
        </a>
      </List.Item>
    ));
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
    if (this.props.selectedProfile === 0) {
      this.props.dispatch(createProfile({name: this.props.profileName, column_index: this.props.chooseColumn()})).then(response => {
        const newProfile = response.id;
        if (this.props.accountGoogleSelected.id === 0) {
          this.props.dispatch(createSsoGroup({
            sso_id: this.props.accountGoogleSelected.sso_id,
            account_information: transformCredentialsListIntoObject(this.props.credentials)
          })).then(r => {
            const calls = this.props.ssoSelected.map(item => {
              return this.props.dispatch(catalogAddSsoApp({
                name: item.name,
                profile_id: newProfile,
                sso_group_id: r.id,
                website_id: item.website_id
              }));
            });
            const response = calls.filter(item => {
              if (item)
                return true
            });
            Promise.all(response.map(reflect)).then(r => {
              this.setState({loading: false});
              this.props.showCatalogAddSSOAppModal({active: false})
            }).catch(err => {
              this.setState({loading: false, errorMessage: err});
            });
          }).catch(err => {
            this.setState({loading: false, errorMessage: err});
          });
        }
        else {
          const calls = this.props.ssoSelected.map(item => {
            return this.props.dispatch(catalogAddSsoApp({
              name: item.name,
              profile_id: newProfile,
              sso_group_id: this.props.accountGoogleSelected.id,
              website_id: item.website_id
            }));
          });
          const response = calls.filter(item => {
            if (item)
              return true
          });
          Promise.all(response.map(reflect)).then(r => {
            this.setState({loading: false});
            this.props.showCatalogAddSSOAppModal({active: false})
          }).catch(err => {
            this.setState({loading: false, errorMessage: err});
          });
        }
      }).catch(err => {
        this.setState({loading: false, errorMessage: err});
      });
    }
    else {
      if (this.props.accountGoogleSelected.id === 0) {
        this.props.dispatch(createSsoGroup({
          sso_id: this.props.accountGoogleSelected.sso_id,
          account_information: transformCredentialsListIntoObject(this.props.credentials)
        })).then(r => {
          const calls = this.props.ssoSelected.map(item => {
            return this.props.dispatch(catalogAddSsoApp({
              name: item.name,
              profile_id: this.props.selectedProfile,
              sso_group_id: r.id,
              website_id: item.website_id
            }));
          });
          const response = calls.filter(item => {
            if (item)
              return true
          });
          Promise.all(response.map(reflect)).then(r => {
            this.setState({loading: false});
            this.props.showCatalogAddSSOAppModal({active: false})
          }).catch(err => {
            this.setState({loading: false, errorMessage: err});
          });
        }).catch(err => {
          this.setState({loading: false, errorMessage: err});
        });
      }
      else {
        const calls = this.props.ssoSelected.map(item => {
          return this.props.dispatch(catalogAddSsoApp({
            name: item.name,
            profile_id: this.props.selectedProfile,
            sso_group_id: this.props.accountGoogleSelected.id,
            website_id: item.website_id
          }));
        });
        const response = calls.filter(item => {
          if (item)
            return true
        });
        Promise.all(response.map(reflect)).then(r => {
          this.setState({loading: false});
          this.props.showCatalogAddSSOAppModal({active: false})
        }).catch(err => {
          this.setState({loading: false, errorMessage: err});
        });
      }
    }
  };
  render() {
    const {
      logo,
      name,
      accountGoogleSelected,
      ssoWebsites,
      back} = this.props;

    const ssoApps = accountGoogleSelected.sso_app_ids.map(item => {
      return this.props.dashboard.apps[item]
    });
    const gridSsoWebsites = ssoWebsites.map(item => {
      if (!ssoApps.filter(key => {
          if (key.website.id === item.id)
            return true
        })
          .length && this.props.modal.website.id !== item.id) {
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
      else if (ssoApps.filter(key => {
          if (key.website.id === item.id)
            return true
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
          <span class="app_name"><Input size="mini" type="text" placeholder="App name..."
                                        name="name"
                                        class="input_unstyle modal_input name_input"
                                        autoFocus={true}
                                        value={name}
                                        onChange={e => this.props.editSSO(e, this.props.modal.website)}/></span>
        </Form.Field>
        <Form.Field>
          <p className='backPointer' onClick={back}><Icon name='arrow left'/>Back</p>
        </Form.Field>
        <Form.Field>
          <Segment.Group className='connectWithGoogle'>
            <Segment className='first overflow-ellipsis'>
              <Icon name='google'/>
              {accountGoogleSelected.account_information.login}
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
      credentials,
      testCredentials,
      toggleBookmark,
      handleInput,
      handleCredentialInput,
      confirm} = this.props;
    const credentialsInputs = credentials.map(item => {
      return <CredentialInput key={item.priority} onChange={handleCredentialInput} item={item} />;
    });
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
        {credentialsInputs}
        <Message error content={this.state.errorMessage}/>
        <span id='test_credentials' onClick={testCredentials}>Test connection <Icon color='green' name='magic'/></span>
        <Button
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
    if (this.props.selectedProfile === 0) {
      this.props.dispatch(createProfile({name: this.props.profileName, column_index: this.props.chooseColumn()})).then(response => {
        const newProfile = response.id;
        this.props.catalogAddBookmark({
          name: this.props.name,
          profile_id: newProfile,
          url: this.state.url,
          img_url: this.props.logo
        }).then(r => {
          this.setState({loading: false});
          this.props.showCatalogAddSSOAppModal({active: false})
        }).catch(err => {
          this.setState({loading: false, errorMessage: err});
        });
      }).catch(err => {
        this.setState({loading: false, errorMessage: err});
      });
    }
    else {
      this.props.catalogAddBookmark({
        name: this.props.name,
        profile_id: this.props.selectedProfile,
        url: this.state.url,
        img_url: this.props.logo
      }).then(r => {
        this.setState({loading: false});
        this.props.showCatalogAddSSOAppModal({active: false})
      }).catch(err => {
        this.setState({loading: false, errorMessage: err});
      });
    }
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
      credentials: transformWebsiteInfoIntoList(this.props.modal.website.information),
      addBookmark: false,
      addGoogleAccount: false,
      accountGoogleSelected: [],
      profiles: [],
      profileName: '',
      profileAdded: false,
      selectedProfile: -1,
      selectedTeam: -1,
      selectedRoom: -1,
      addingProfile: false,
      view: 1,
      ssoSelected: [],
      ssoGroup: [],
      ssoWebsites: []
    }
  }
  componentWillMount() {
    this.setState({loading: true});
    const profiles = Object.keys(this.props.dashboard.profiles).map(id => (
      this.props.dashboard.profiles[id]
    ));
    this.setState({profiles: profiles});
    const ssoWebsites = this.props.catalog.websites.filter(item => {
      return item.sso_id === this.props.modal.website.sso_id;
    });
    const ssoGroup = Object.keys(this.props.dashboard.sso_groups).map(item => {
      if (this.props.dashboard.sso_groups[item].sso_id === this.props.modal.website.sso_id)
        return this.props.dashboard.sso_groups[item];
    });
    this.setState({ssoGroup: ssoGroup, ssoWebsites: ssoWebsites, loading: false});
  }
  handleInput = (e, {name, value}) => this.setState({[name]: value});
  handleCredentialInput = (e, {name, value}) => {
    let credentials = this.state.credentials.map(item => {
      if (name === item.name)
        item.value = value;
      return item;
    });
    this.setState({credentials: credentials});
  };
  toggleBookmark = () => {
    if (this.state.addBookmark)
      this.setState({addBookmark: false, errorMessage: ''});
    else
      this.setState({addBookmark: true, credentials: transformWebsiteInfoIntoList(this.props.modal.website.information), accountGoogleSelected: [], errorMessage: ''});
  };
  back = () => {
    if (this.state.ssoGroup.length)
      this.setState({
        accountGoogleSelected: [],
        view: 2,
        credentials: transformWebsiteInfoIntoList(this.props.modal.website.information),
        addGoogleAccount: false,
        errorMessage: '',
        ssoSelected: [{website_id: this.props.modal.website.id, name: this.state.name}]
      });
    else
      this.setState({
        accountGoogleSelected: [],
        view: 2,
        credentials: transformWebsiteInfoIntoList(this.props.modal.website.information),
        addGoogleAccount: true,
        errorMessage: '',
        ssoSelected: [{website_id: this.props.modal.website.id, name: this.state.name}]
      });
  };
  chooseColumn = () => {
    const columns = this.props.dashboard.columns.map((column, index) => {
      let apps = 0;
      column.map(item => {
        let tmp = this.props.dashboard.profiles[item].app_ids.length / 3;
        if (tmp <= Number(tmp.toFixed(0)))
          tmp = Number(tmp.toFixed(0)) + 1;
        else if (tmp > Number(tmp.toFixed(0)))
          tmp = Number(tmp.toFixed(0)) + 2;
        apps = apps + tmp;
      });
      if (apps > 0)
        return apps;
      else
        return 0;
    });
    let columnChoose = null;
    columns.map((column, index) => {
      let test = columns.slice();
      test.sort();
      if (column === test[0] && columnChoose === null)
        columnChoose = index;
    });
    return columnChoose;
  };
  selectAccount = (account) => {
    this.setState({
      accountGoogleSelected: account,
      view: 3,
      credentials: account.account_information
    });
  };
  addGoogleAccount = () => {
    this.setState({addGoogleAccount: true, accountGoogleSelected: []});
  };
  addMainAppToSSOSelected = () => {
    const newSelectedSSO = this.state.ssoSelected.slice();
    newSelectedSSO.push({website_id: this.props.modal.website.id, name: this.state.name});
    if (this.state.ssoGroup.length)
      this.setState({ssoSelected: newSelectedSSO, view: 2});
    else
      this.setState({ssoSelected: newSelectedSSO, view: 2, addGoogleAccount: true});
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
  showLogin = () => {
    const login = this.state.credentials.map(item => {
      if (item.name === "login")
        return item.value
    });
    return login;
  };
  showName = () => {
    let name = this.state.ssoSelected.filter(item => {
      return this.props.modal.website.id === item.website_id;
    });
    name = name.map(item => {
      return item.name;
    });
    return name;
  };
  confirm = () => {
    if (this.state.view === 1) {
      if (this.state.selectedProfile !== -1)
        this.addMainAppToSSOSelected();
      else if (this.state.selectedProfile === -1 && this.state.selectedRoom !== -1)
        this.setState({view: 4});
      else
        this.createProfile();
    }
    else if (this.state.addGoogleAccount) {
      this.setState({
        accountGoogleSelected: {
          id: 0,
          sso_app_ids: [],
          sso_id: this.props.modal.website.sso_id,
          account_information: {login: this.showLogin()}
        },
        view: 3,
        addGoogleAccount: false
      });
    }
  };
  testCredentials = () => {
    this.props.dispatch(testCredentials({
      account_information: transformCredentialsListIntoObject(this.state.credentials),
      website_id: this.state.website.id
    }));
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
            ssoGroup={this.state.ssoGroup}
            addGoogleAccount={this.addGoogleAccount}
            toggleBookmark={this.toggleBookmark}
            selectAccount={this.selectAccount} />}
        {this.state.view === 3 &&
          <ThirdStep
            {...this.props}
            logo={this.props.modal.website.logo}
            name={this.showName()}
            profileName={this.state.profileName}
            accountGoogleSelected={this.state.accountGoogleSelected}
            selectedProfile={this.state.selectedProfile}
            ssoWebsites={this.state.ssoWebsites}
            credentials={this.state.credentials}
            ssoSelected={this.state.ssoSelected}
            checkActive={this.checkActive}
            selectSSO={this.selectSSO}
            deselectSSO={this.deselectSSO}
            chooseColumn={this.chooseColumn}
            editSSO={this.editSSO}
            back={this.back}/> }
        {(this.state.addGoogleAccount && !this.state.addBookmark) &&
          <NewSsoAccount
            {...this.props}
            website={this.state.website}
            logo={this.props.modal.website.logo}
            name={this.props.modal.website.name}
            credentials={this.state.credentials}
            testCredentials={this.testCredentials}
            toggleBookmark={this.toggleBookmark}
            handleCredentialInput={this.handleCredentialInput}
            handleInput={this.handleInput}
            confirm={this.confirm} />}
        {this.state.addBookmark &&
          <AddBookmark
            {...this.props}
            landing_url={this.props.modal.website.landing_url}
            logo={this.props.modal.website.logo}
            name={this.props.modal.website.name}
            toggleBookmark={this.toggleBookmark}
            chooseColumn={this.chooseColumn}
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