import React, {Component} from 'react';
import { Image, Icon, Label, Form, Button, Message, Checkbox, Divider, Segment, List, Container, Input } from 'semantic-ui-react';
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {dashboard} from "../../utils/post_api";
import {handleSemanticInput,
  transformCredentialsListIntoObject,
  transformWebsiteInfoIntoList,
  credentialIconType} from "../../utils/utils";
import {selectItemFromListById} from "../../utils/helperFunctions";
import {reduxActionBinder} from "../../actions/index";
import {connect} from "react-redux";
var api = require('../../utils/api');

class ProfileChooseStep extends Component {
  constructor(props){
    super(props);
    this.state = {
      profileName: '',
      addingProfile: false
    }
  }
  handleInput = handleSemanticInput.bind(this);
  createProfile = (e) => {
    e.preventDefault();
    e.stopPropagation();
    if (this.state.profileName.length === 0)
      return;
    this.setState({addingProfile: true});
    dashboard.createProfile({name: this.state.profileName}).then(response => {
      this.props.addProfile(response);
      this.setState({profileName: '', addingProfile: false});
    }).catch(err => {
      this.setState({addingProfile: false});
    });
  };
  render(){
    const {
      website,
      appName,
      handleInput,
      selectedProfile,
      confirm,
      selectProfile} = this.props;
    const profiles = this.props.profiles.map(profile => {
      return (
          <List.Item as="a"
                     key={profile.id}
                     class="display_flex"
                     active={selectedProfile === profile.id}
                     onClick={e => selectProfile(profile.id)}>
            <strong>{profile.name}</strong>
            &nbsp;&nbsp;
            <em class="overflow-ellipsis">
              {
                profile.apps.map(function(app, idx){
                  var ret = app.name;
                  ret += (idx === profile.apps.length - 1) ? '' : ', ';
                  return (ret)
                }, this)
              }
            </em>
          </List.Item>
      )
    });
    return (
        <Form class="container" id="add_bookmark_form" onSubmit={confirm}>
          <Form.Field class="display-flex align_items_center" style={{marginBottom: '30px'}}>
            <div class="squared_image_handler" style={{boxShadow:'none'}}>
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
            <div style={{marginBottom: '10px'}}>App location (you can always change it later)</div>
            <Container class="profiles">
              <List link>
                {profiles}
              </List>
              <form style={{marginBottom: 0}} onSubmit={this.createProfile}>
                <Input
                    loading={this.state.addingProfile}
                    value={this.state.profileName}
                    name="profileName"
                    required
                    transparent
                    onChange={this.handleInput}
                    class="create_profile_input"
                    icon={<Icon name="plus square" link onClick={this.createProfile}/>}
                    placeholder='Create new group'
                />
              </form>
            </Container>
          </Form.Field>
          <Button
              attached='bottom'
              type="submit"
              positive
              disabled={selectedProfile === -1 || appName.length === 0}
              onClick={confirm}
              className="modal-button"
              content="NEXT"/>
        </Form>
    )
  }
}

const CredentialInput = ({item, onChange}) => {
  return (
      <Form.Field>
        <label style={{ fontSize: '16px', fontWeight: '300', color: '#424242' }}>{item.placeholder}</label>
        <Input size="large"
               autoFocus={item.autoFocus}
               class="modalInput team-app-input"
               required
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
    this.setState({loading: true, errorMessage: ''});
    this.props.catalogAddBookmark({
      name: this.props.appName,
      profile_id: this.props.profile_id,
      url: this.props.url,
      img_url: this.props.website.logo
    }).then(resp => {
      this.setState({loading: false});
      this.props.showCatalogAddAppModal({active: false});
    }).catch(err => {
      this.setState({loading: false, errorMessage:err});
    });
  };
  render(){
    const {url, handleInput} = this.props;
    return (
        <Form onSubmit={this.confirm} error={this.state.errorMessage.length > 0}>
          <Form.Field>
            <label style={{ fontSize: '16px', fontWeight: '300', color: '#424242' }}>{this.props.nameLabel}</label>
            <Input fluid
                   className="modalInput team-app-input"
                   size='large'
                   type='url'
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
      errorMessage: ''
    }
  }
  confirm = (e) => {
    e.preventDefault();
    this.setState({loading: true});
    this.props.catalogAddClassicApp({
      name: this.props.appName,
      website_id: this.props.website.id,
      profile_id:this.props.profile_id,
      account_information: transformCredentialsListIntoObject(this.props.credentials)
    }).then(app => {
      this.setState({loading: false});
      this.props.showCatalogAddAppModal({active: false});
    }).catch(err => {
      this.setState({loading: false, errorMessage: err});
    });
  };
  render(){
    const {
      credentials,
      logWith_websites,
      chooseLogWith,
      handleCredentialInput} = this.props;
    const credentialsInputs = credentials.map(item => {
      return <CredentialInput key={item.priority} onChange={handleCredentialInput} item={item}/>
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
          {logWithButtons.length > 0 &&
          <Form.Field>
            <Divider horizontal>Or</Divider>
          </Form.Field>}
          {logWithButtons}
          <Message error content={this.state.errorMessage}/>
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
      bookmark: false
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
      credentials,
      url,
      handleInput,
      handleCredentialInput,
      changeView
    } = this.props;
    return (
        <Form as="div" class="container">
          <Form.Field>
            <Image src={website.logo} style={{ width:'80px', marginRight: '10px', display: 'inline-block', borderRadius: '5px'}}/>
            <p style={{ display: 'inline-block', fontSize: '20px', fontWeight: '300', color: '#939eb7' }}>{appName}</p>
          </Form.Field>
          <Form.Field>
            <p style={{ display: 'inline-block', fontSize: '20px', color: '#414141' }}><strong>Add just a Bookmark</strong></p>
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
    this.setState({loading: true, errorMessage: ''});
    this.props.catalogAddLogWithApp({
      name: this.props.appName,
      website_id: this.props.website.id,
      profile_id: this.props.profile_id,
      logWith_app_id: this.state.selectedAppId
    }).then(app => {
      this.setState({loading: false});
      this.props.showCatalogAddAppModal({active: false});
    }).catch(err => {
      this.setState({loading: false, errorMessage: err});
    });
  };
  selectApp = (id) => {
    this.setState({selectedAppId: id});
  };
  render(){
    const {website, appName, profile_id, goBack, logWithWebsite} = this.props;
    const logWithName = logWithWebsite.name.toLowerCase();
    const logWithSelectors = logWithWebsite.personal_apps.map(item => {
      return (
          <List.Item key={item.id} as="p" active={this.state.selectedAppId === item.id} onClick={this.selectApp.bind(null, item.id)}>
            <Icon name='user circle' />
            <span>{item.account_information.login}</span>
          </List.Item>
      )
    });
    return (
        <Form as="div" class="container" id="add_logwith_form" error={this.state.errorMessage.length > 0}>
          <Form.Field>
            <Image src={website.logo} style={{ width:'80px', marginRight: '10px', display: 'inline-block', borderRadius: '5px'}}/>
            <p style={{ display: 'inline-block', fontSize: '20px', fontWeight: '300', color: '#939eb7' }}>{appName}</p>
          </Form.Field>
          <Form.Field>
            <p className='backPointer' onClick={goBack}><Icon name='arrow left'/>Back</p>
          </Form.Field>
          <Form.Field>
            <Segment.Group className={logWithName}>
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
  catalog: store.catalog,
  modal: store.teamModals.catalogAddAppModal
}), reduxActionBinder)
class ClassicAppModal extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      website: this.props.modal.website,
      name: this.props.modal.website.name,
      credentials: transformWebsiteInfoIntoList(this.props.modal.website.information),
      logWith_websites: [],
      choosenLogWithWebsite: null,
      url: this.props.modal.website.landing_url,
      profiles: [],
      selectedProfile: -1,
      view: 1
    }
  }
  chooseLogWith = (logwithId) => {
    const logWithWebsite = selectItemFromListById(this.state.logWith_websites, logwithId);
    this.setState({view: 3, choosenLogWithWebsite:logWithWebsite});

  };
  componentWillMount(){
    api.dashboard.fetchProfiles().then(profiles => {
      this.setState({profiles: profiles});
      let logwith = this.props.modal.website.connectWith_websites.map(item => {
        let website = selectItemFromListById(this.props.catalog.websites, item);
        let apps = [];
        profiles.map(item => {
          item.apps.map(app => {
            if (app.website_id === website.id)
              apps.push(app);
          });
        });
        website.personal_apps = apps;
        return website;
      });
      this.setState({logWith_websites: logwith});
    });
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
  selectProfile = (id) => {
    this.setState({selectedProfile: id});
  };
  addProfile = (profile) => {
    let profiles = this.state.profiles.slice();
    profiles.push(profile);
    this.setState({profiles: profiles, selectedProfile: profile.id});
  };
  changeView = (view) => {
    this.setState({view: view});
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
          <ProfileChooseStep
              website={this.state.website}
              appName={this.state.name}
              profiles={this.state.profiles}
              handleInput={this.handleInput}
              selectedProfile={this.state.selectedProfile}
              addProfile={this.addProfile}
              confirm={this.changeView.bind(null, 2)}
              selectProfile={this.selectProfile}/>}
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
              handleCredentialInput={this.handleCredentialInput}
              chooseLogWith={this.chooseLogWith}
              changeView={this.changeView}/>}
          {this.state.view === 3 &&
          <AddLogWithAppForm
              {...this.props}
              website={this.state.website}
              appName={this.state.name}
              profile_id={this.state.selectedProfile}
              goBack={this.changeView.bind(null, 2)}
              logWithWebsite={this.state.choosenLogWithWebsite}/>}
        </SimpleModalTemplate>
    )
  }
}

export default ClassicAppModal;