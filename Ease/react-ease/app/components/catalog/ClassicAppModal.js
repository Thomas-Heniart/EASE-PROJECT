import React, {Component} from 'react';
import { Icon, Label, Form, Button, Message, Checkbox, Divider, Segment, List, Input } from 'semantic-ui-react';
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {dashboard} from "../../utils/post_api";
import {handleSemanticInput,
  transformCredentialsListIntoObject,
  transformWebsiteInfoIntoList,
  credentialIconType} from "../../utils/utils";
import {selectItemFromListById} from "../../utils/helperFunctions";
import {reduxActionBinder} from "../../actions/index";
import ChooseAppLocationModal from './ChooseAppLocationModal';
import ChooseTypeAppModal from './ChooseTypeAppModal';
import {connect} from "react-redux";
import {testCredentials} from "../../actions/catalogActions";
import {appAdded, createProfile} from "../../actions/dashboardActions";

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

class AddBookmarkForm extends Component {
  constructor(props){
    super(props);
    this.state = {
      loading: false,
      errorMessage: ''
    }
  }
  confirm = (e) => {
    e.preventDefault();
    let newProfile = this.props.profile_id;
    this.setState({loading: true, errorMessage: ''});
    if (newProfile === 0) {
      this.props.dispatch(createProfile({name: 'Me', column_index: this.props.chooseColumn()})).then(response => {
        newProfile = response.id;
        this.props.catalogAddBookmark({
          name: this.props.appName,
          profile_id: newProfile,
          url: this.props.url,
          img_url: this.props.website.logo
        }).then(resp => {
          this.props.dispatch(appAdded({
            app: resp,
            from: "Catalog"
          }));
          this.setState({loading: false});
          this.props.showCatalogAddAppModal({active: false});
        }).catch(err => {
          this.setState({loading: false, errorMessage: err});
        });
      }).catch(err => {
        this.setState({loading: false, errorMessage: err});
      });
    }
    else {
      this.props.catalogAddBookmark({
        name: this.props.appName,
        profile_id: newProfile,
        url: this.props.url,
        img_url: this.props.website.logo
      }).then(resp => {
        this.props.dispatch(appAdded({
          app: resp,
          from: "Catalog"
        }));
        this.setState({loading: false});
        this.props.showCatalogAddAppModal({active: false});
      }).catch(err => {
        this.setState({loading: false, errorMessage: err});
      });
    }
  };
  render(){
    const {url, handleInput} = this.props;
    return (
        <Form onSubmit={this.confirm} error={this.state.errorMessage.length > 0}>
          <Form.Field>
            <label style={{ fontSize: '16px', fontWeight: '300', color: '#424242' }}>Here is the link</label>
            <Input fluid
                   className="modalInput team-app-input"
                   size='large'
                   type='url'
                   autoFocus
                   name='url'
                   placeholder='Url'
                   onChange={handleInput}
                   required
                   label={{ icon: 'home' }}
                   labelPosition='left'
                   value={url} />
          </Form.Field>
          <Message error content={this.state.errorMessage}/>
          <Button
              type="submit"
              loading={this.state.loading}
              disabled={this.state.loading || url.length === 0}
              positive
              class="modal-button uppercase"
              content={'CONFIRM'} />
        </Form>
    )
  }
}

class AddClassicAppForm extends Component {
  constructor(props){
    super(props);
    this.state = {
      loading: false,
      errorMessage: '',
    }
  }
  confirm = (e) => {
    e.preventDefault();
    let newProfile = this.props.profile_id;
    this.setState({loading: true, errorMessage: ''});
    if (newProfile === 0) {
      this.props.dispatch(createProfile({name: 'Me', column_index: this.props.chooseColumn()})).then(response => {
        newProfile = response.id;
        this.props.catalogAddClassicApp({
          name: this.props.appName,
          website_id: this.props.website.id,
          profile_id: newProfile,
          account_information: transformCredentialsListIntoObject(this.props.credentials)
        }).then(app => {
          this.props.dispatch(appAdded({
            app: app,
            from: "Catalog"
          }));
          this.setState({loading: false});
          this.props.showCatalogAddAppModal({active: false});
        }).catch(err => {
          this.setState({loading: false, errorMessage: err});
        });
      }).catch(err => {
        this.setState({loading: false, errorMessage: err});
      });
    }
    else {
      this.props.catalogAddClassicApp({
        name: this.props.appName,
        website_id: this.props.website.id,
        profile_id: newProfile,
        account_information: transformCredentialsListIntoObject(this.props.credentials)
      }).then(app => {
        this.props.dispatch(appAdded({
          app: app,
          from: "Catalog"
        }));
        this.setState({loading: false});
        this.props.showCatalogAddAppModal({active: false});
      }).catch(err => {
        this.setState({loading: false, errorMessage: err});
      });
    }
  };
  render(){
    const {
      credentials,
      testCredentials,
      logWith_websites,
      chooseLogWith,
      handleCredentialInput} = this.props;
    const credentialsInputs = credentials.map(item => {
      return <CredentialInput key={item.priority} onChange={handleCredentialInput} item={item}/>;
    });
      const logWithButtons = logWith_websites.map(item => {
      const name = item.name.toLowerCase();
      return (
          <Form.Field key={item.id}>
            <Button fluid
                    onClick={chooseLogWith.bind(null, item.id)}
                    type="button"
                    className='buttonModalConnect'
                    color={name}
                    content={`Connect with ${item.name}`}
                    icon={`${name} square`}/>
          </Form.Field>
      )
    });
    return (
        <Form onSubmit={this.confirm} error={this.state.errorMessage.length > 0}>
          {credentialsInputs}
          {(logWithButtons.length > 0 && credentialsInputs.length > 0) &&
          <Form.Field>
            <Divider horizontal>Or</Divider>
          </Form.Field>}
          {logWithButtons}
          <Message error content={this.state.errorMessage}/>
          <span id='test_credentials' onClick={testCredentials}>Test connection <Icon color='green' name='magic'/></span>
          <Button
              type="submit"
              loading={this.state.loading}
              disabled={this.state.loading}
              positive
              class="modal-button uppercase"
              content={'CONFIRM'} />
        </Form>
    )
  }
}

class SecondStep extends Component {
  constructor(props){
    super(props);
    this.state = {
      bookmark: false,
    }
  }
  confirm = (e) => {
    e.preventDefault();
  };
  handleInput = handleSemanticInput.bind(this);
  render(){
    const {
      website,
      appName,
      handleInput
    } = this.props;
    return (
        <Form as="div" class="container">
          <Form.Field class="display-flex align_items_center" style={{marginBottom: '30px'}}>
            <div class="squared_image_handler">
              <img src={website.logo} alt="Website logo"/>
            </div>
            <span class="app_name"><Input size="mini" type="text" placeholder="App name..."
                                          name="name"
                                          class="input_unstyle modal_input name_input"
                                          autoFocus={true}
                                          value={appName}
                                          onChange={handleInput}/></span>
          </Form.Field>
          <Form.Field>
            <p style={{ display: 'inline-block', fontSize: '20px', color: '#414141' }}><strong>Add as Bookmark</strong></p>
            <Checkbox toggle name='bookmark' checked={this.state.bookmark} onClick={this.handleInput} style={{ marginLeft: '20px', marginBottom: '0' }} />
          </Form.Field>
          {this.state.bookmark &&
          <AddBookmarkForm {...this.props}/>}
          {!this.state.bookmark &&
          <AddClassicAppForm {...this.props}/>}
        </Form>
    )
  }
}

class AddLogWithAppForm extends Component {
  constructor(props){
    super(props);
    this.state = {
      loading: false,
      errorMessage: '',
      selectedAppId: -1
    }
  }
  confirm = (e) => {
    e.preventDefault();
    let newProfile = this.props.profile_id;
    this.setState({loading: true, errorMessage: ''});
    if (newProfile === 0) {
      this.props.dispatch(createProfile({name: 'Me', column_index: this.props.chooseColumn()})).then(response => {
        newProfile = response.id;
        this.props.catalogAddLogWithApp({
          name: this.props.appName,
          website_id: this.props.website.id,
          profile_id: newProfile,
          logWith_app_id: this.state.selectedAppId
        }).then(app => {
          this.props.dispatch(appAdded({
            app: app,
            from: "Catalog"
          }));
          this.setState({loading: false});
          this.props.showCatalogAddAppModal({active: false});
        }).catch(err => {
          this.setState({loading: false, errorMessage: err});
        });
      }).catch(err => {
        this.setState({loading: false, errorMessage: err});
      });
    }
    else {
      this.props.catalogAddLogWithApp({
        name: this.props.appName,
        website_id: this.props.website.id,
        profile_id: newProfile,
        logWith_app_id: this.state.selectedAppId
      }).then(app => {
        this.props.dispatch(appAdded({
          app: app,
          from: "Catalog"
        }));
        this.setState({loading: false});
        this.props.showCatalogAddAppModal({active: false});
      }).catch(err => {
        this.setState({loading: false, errorMessage: err});
      });
    }
  };
  selectApp = (id) => {
    this.setState({selectedAppId: id});
  };
  render(){
    const {website, appName, profile_id, goBack, logWithWebsite} = this.props;
    const logWithName = logWithWebsite.name.toLowerCase();
    const logWithSelectors = logWithWebsite.personal_apps.map(item => {
      if (!item.empty) {
        return (
          <List.Item key={item.id} as="p" active={this.state.selectedAppId === item.id}
                     onClick={this.selectApp.bind(null, item.id)}>
            <Icon name='user circle'/>
            <span>{item.account_information.login}</span>
          </List.Item>
        )
      }
      else {
        return (
          <List.Item key={item.id} as="p" style={{opacity:'.7'}}>
            <Icon name='user circle'/>
            <span>App with missing credentials</span>
          </List.Item>
        )
      }
    });
    return (
        <Form as="div" class="container" error={this.state.errorMessage.length > 0}>
          <Form.Field class="display-flex align_items_center" style={{marginBottom: '30px'}}>
            <div class="squared_image_handler">
              <img src={website.logo} alt="Website logo"/>
            </div>
            <span class="app_name">{appName}</span>
          </Form.Field>
          <Form.Field>
            <p className='backPointer' onClick={goBack}><Icon name='arrow left'/>Back</p>
          </Form.Field>
          <Form.Field>
            <Segment.Group class={`logwith_app_selectors ${logWithName}`}>
              <Segment className='first'>
                <Icon name={logWithName}/>
                Select your {logWithWebsite.name} account
              </Segment>
              <Segment>
                <List className="listCategory">
                  {logWithSelectors.length > 0 ?
                      logWithSelectors :
                      <p class="text-center errorMessage">
                        You don’t have a {logWithWebsite.name} Account setup yet. Please install one before all.
                      </p>}
                </List>
              </Segment>
            </Segment.Group>
          </Form.Field>
          <Message error content={this.state.errorMessage}/>
          <Button
              attached='bottom'
              type="submit"
              loading={this.state.loading}
              disabled={this.state.loading || this.state.selectedAppId === -1}
              positive
              onClick={this.confirm}
              class="modal-button uppercase"
              content={'CONFIRM'} />
        </Form>
    )
  }
}

@connect(store => ({
  dashboard: store.dashboard,
  catalog: store.catalog,
  modal: store.teamModals.catalogAddAppModal
}), reduxActionBinder)
class ClassicAppModal extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      view: 1,
      check: null,
      checkRoom: null,
      website: this.props.modal.website,
      name: this.props.modal.website.name,
      credentials: transformWebsiteInfoIntoList(this.props.modal.website.information),
      logWith_websites: [],
      choosenLogWithWebsite: null,
      url: this.props.modal.website.landing_url,
      profiles: [],
      selectedProfile: -1,
      profileName: '',
      selectedTeam: -1,
      selectedRoom: -1,
      loading: false,
    }
  }
  chooseLogWith = (logwithId) => {
    const logWithWebsite = selectItemFromListById(this.state.logWith_websites, logwithId);
    this.setState({view: 3, choosenLogWithWebsite:logWithWebsite});
  };
  componentWillMount(){
    this.setState({loading: true});
    const dashboard_apps = this.props.dashboard.apps;
    const profiles = Object.keys(this.props.dashboard.profiles).map(id => (
      this.props.dashboard.profiles[id]
    ));
    this.setState({profiles: profiles});
    let logwith = this.props.modal.website.connectWith_websites.map(item => {
      let website = selectItemFromListById(this.props.catalog.websites, item);
      let apps = [];
      profiles.map(item => {
        item.app_ids.map(id => {
          const app = dashboard_apps[id];
          if (app.type !== 'teamLinkApp' && app.type !== 'linkApp' && app.type !== 'softwareApp' && app.sub_type !== 'software') {
            if (app.website.id === website.id)
              apps.push(app);
          }
        }, this);
      }, this);
      website.personal_apps = apps;
      return website;
    });
    this.setState({logWith_websites: logwith, loading: false});
  }
  handleInput = handleSemanticInput.bind(this);
  handleCredentialInput = (e, {name, value}) => {
    let credentials = this.state.credentials.map(item => {
      if (name === item.name)
        item.value = value;
      return item;
    });
    this.setState({credentials: credentials});
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
  selectProfile = () => {
    let profileChoose = null;
    Object.keys(this.props.dashboard.profiles).map(item => {
      if (profileChoose === null && this.props.dashboard.profiles[item].team_id === -1)
        profileChoose = this.props.dashboard.profiles[item].id;
    });
    this.setState({
      selectedProfile: profileChoose !== null ? profileChoose : 0,
      selectedTeam: -1,
      selectedRoom: -1,
      check: 'newApp',
      error: ""
    });
  };
  selectRoom = (roomId) => {
    this.setState({selectedRoom: Number(roomId), checkRoom: roomId});
  };
  selectTeam = (teamId) => {
    this.setState({selectedTeam: teamId, check: teamId, selectedRoom: -1, checkRoom: null, selectedProfile: -1});
  };
  changeView = (view) => {
    if (this.state.selectedProfile !== -1)
      this.setState({view: view});
    else if (this.state.selectedProfile === -1 && this.state.selectedRoom !== -1)
      this.setState({view: 4});
  };
  testCredentials = () => {
    this.props.dispatch(testCredentials({
      account_information: transformCredentialsListIntoObject(this.state.credentials),
      website_id: this.state.website.id
    }));
  };
  close = () => {
    this.props.showCatalogAddAppModal({active: false});
  };
  render() {
    return (
        <SimpleModalTemplate
            onClose={this.close}
            headerContent={'Setup your App'}>
          {this.state.view === 1 &&
          <ChooseAppLocationModal
            check={this.state.check}
            appName={this.state.name}
            loading={this.state.loading}
            website={this.state.website}
            selectTeam={this.selectTeam}
            selectRoom={this.selectRoom}
            checkRoom={this.state.checkRoom}
            selectProfile={this.selectProfile}
            confirm={this.changeView.bind(null, 2)}
            logoLetter={this.props.modal.logoLetter} />}
          {this.state.view === 2 &&
          <SecondStep
              {...this.props}
              website={this.state.website}
              logWith_websites={this.state.logWith_websites}
              credentials={this.state.credentials}
              url={this.state.url}
              appName={this.state.name}
              handleInput={this.handleInput}
              profile_id={this.state.selectedProfile}
              profileName={this.state.profileName}
              testCredentials={this.testCredentials}
              handleCredentialInput={this.handleCredentialInput}
              chooseLogWith={this.chooseLogWith}
              chooseColumn={this.chooseColumn}
              changeView={this.changeView}/>}
          {this.state.view === 3 &&
          <AddLogWithAppForm
              {...this.props}
              website={this.state.website}
              appName={this.state.name}
              profileName={this.state.profileName}
              chooseColumn={this.chooseColumn}
              profile_id={this.state.selectedProfile}
              goBack={this.changeView.bind(null, 2)}
              logWithWebsite={this.state.choosenLogWithWebsite}/>}
          {this.state.view === 4 &&
          <ChooseTypeAppModal
              {...this.props}
              website={this.state.website}
              appName={this.state.name}
              team_id={this.state.selectedTeam}
              room_id={this.state.selectedRoom}
              subtype={'classic'}
              close={this.close} />}
        </SimpleModalTemplate>
    )
  }
}

export default ClassicAppModal;