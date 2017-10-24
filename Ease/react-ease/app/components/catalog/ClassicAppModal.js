import React, {Component} from 'react';
import { Image, Icon, Label, Form, Button, Message, Checkbox, Divider, Segment, List, Container, Input } from 'semantic-ui-react';
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {dashboard} from "../../utils/post_api";
import {handleSemanticInput,
  transformCredentialsListIntoObject,
  transformWebsiteInfoIntoList,
  credentialIconType} from "../../utils/utils";
import {selectItemFromListById} from "../../utils/helperFunctions";
import InputModalCatalog from './InputModalCatalog';
import {reduxActionBinder} from "../../actions/index";
import {connect} from "react-redux";
import AddBookmarkModal from "./AddBookmarkModal";
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
    this.setState({loading: false, errorMessage: ''});
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
          <InputModalCatalog
              nameLabel='Here is the link'
              nameInput='url'
              inputType='url'
              placeholderInput=''
              handleInput={handleInput}
              iconLabel='home'
              valueInput={url}/>
          <Message error content={this.state.errorMessage}/>
          <Button
              attached='bottom'
              type="submit"
              loading={this.state.loading}
              disabled={this.state.loading || url.length === 0}
              positive
              onClick={this.confirm}
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
      handleCredentialInput} = this.props;
    const credentialsInputs = credentials.map(item => {
      return <CredentialInput key={item.priority} onChange={handleCredentialInput} item={item}/>
    });
    const logWithButtons = logWith_websites.map(item => {
      const name = item.name.toLowerCase();
      return (
          <Form.Field>
            <Button fluid
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
              attached='bottom'
              type="submit"
              loading={this.state.loading}
              disabled={this.state.loading}
              positive
              onClick={this.confirm}
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
  }
  render(){
    return (
        <Form as="div" class="container">
          <Form.Field>
            <Image src={website.logo} style={{ width:'80px', marginRight: '10px', display: 'inline-block', borderRadius: '5px'}}/>
            <p style={{ display: 'inline-block', fontSize: '20px', fontWeight: '300', color: '#939eb7' }}>{appName}</p>
          </Form.Field>
          <Form.Field>
            <p className='backPointer' onClick={this.back}><Icon name='arrow left'/>Back</p>
          </Form.Field>
          <Form.Field>
            <Segment.Group className='connectWithFacebook'>
              <Segment className='first'>
                <Icon name='facebook'/>
                Select your Facebook account
              </Segment>
              <Segment>
                <List link className="listCategory">
                  <List.Item activeClassName="active"><p><a onClick={e => this.selectAccount('victor_nivet@hotmail.fr')}><Icon name='user circle' />victor_nivet@hotmail.fr</a></p></List.Item>
                  <List.Item activeClassName="active"><p><a onClick={e => this.selectAccount('victor_nivet@gmail.com')}><Icon name='user circle' />victor_nivet@gmail.com</a></p></List.Item>
                  <List.Item activeClassName="active"><p><a onClick={e => this.selectAccount('victor_nivet@outlook.fr')}><Icon name='user circle' />victor_nivet@outlook.fr</a></p></List.Item>
                  <List.Item activeClassName="active"><p><a onClick={e => this.selectAccount('victor_nivet@free.fr')}><Icon name='user circle' />victor_nivet@free.fr</a></p></List.Item>
                </List>
              </Segment>
            </Segment.Group>
          </Form.Field>
          <Button
              attached='bottom'
              type="submit"
              loading={this.state.loading}
              disabled={this.state.loading}
              positive
              onClick={this.confirm}
              class="modal-button uppercase"
              content={'CONFIRM'} />
        </Form>
    )
  }
}

const tmp = (props) => {
  return (
      <Form class="container" error={this.state.errorMessage.length > 0} onSubmit={this.confirm}>
        <Form.Field>
          <Image src={this.props.modal.website.logo} style={{ width:'80px', marginRight: '10px', display: 'inline-block', borderRadius: '5px'}}/>
          <p style={{ display: 'inline-block', fontSize: '20px', fontWeight: '300', color: '#939eb7' }}>{this.props.modal.website.name}</p>
        </Form.Field>
        {!this.state.facebook && !this.state.linkedin ?
            <div>
              <Form.Field>
                <p style={{ display: 'inline-block', fontSize: '20px', color: '#414141' }}><strong>Add just a Bookmark</strong></p>
                <Checkbox toggle onClick={e => this.toggleBookmark()} style={{ marginLeft: '20px', marginBottom: '0' }} />
              </Form.Field>
              {!this.state.addBookmark ?
                  <div>
                    <InputModalCatalog
                        nameLabel='Login'
                        nameInput='login'
                        inputType='text'
                        placeholderInput='Your login'
                        handleInput={this.handleInput}
                        iconLabel='user' />
                    <InputModalCatalog
                        nameLabel='Password'
                        nameInput='password'
                        inputType='password'
                        placeholderInput='Your password'
                        handleInput={this.handleInput}
                        iconLabel='lock' />
                  </div>
                  :
                  <div>
                    <InputModalCatalog
                        nameLabel='Here is the link'
                        nameInput='url'
                        inputType='url'
                        placeholderInput=''
                        handleInput={this.handleInput}
                        iconLabel='home'
                        valueInput={!this.state.url ? this.props.modal.website.landing_url : this.state.url} />
                  </div>
              }
              {!this.state.addBookmark && this.props.modal.website.connectWith_websites.length ?
                  <div>
                    <Form.Field>
                      <Divider horizontal>Or</Divider>
                    </Form.Field>
                    <Form.Field>
                      <Button fluid
                              className='buttonModalConnect'
                              color='facebook'
                              content={'Connect with Facebook'}
                              icon='facebook'
                              onClick={e => this.connectWith('facebook')} />
                    </Form.Field>
                    <Form.Field>
                      <Button fluid
                              className='buttonModalConnect'
                              color='linkedin'
                              content={'Connect with Linkedin'}
                              icon='linkedin square'
                              onClick={e => this.connectWith('linkedin')} />
                    </Form.Field>
                  </div>
                  :
                  <div/>
              }
            </div>
            : this.state.facebook ?
                <div>
                  <Form.Field>
                    <p className='backPointer' onClick={this.back}><Icon name='arrow left'/>Back</p>
                  </Form.Field>
                  <Form.Field>
                    <Segment.Group className='connectWithFacebook'>
                      <Segment className='first'>
                        <Icon name='facebook'/>
                        Select your Facebook account
                      </Segment>
                      <Segment>
                        <List link className="listCategory">
                          <List.Item activeClassName="active"><p><a onClick={e => this.selectAccount('victor_nivet@hotmail.fr')}><Icon name='user circle' />victor_nivet@hotmail.fr</a></p></List.Item>
                          <List.Item activeClassName="active"><p><a onClick={e => this.selectAccount('victor_nivet@gmail.com')}><Icon name='user circle' />victor_nivet@gmail.com</a></p></List.Item>
                          <List.Item activeClassName="active"><p><a onClick={e => this.selectAccount('victor_nivet@outlook.fr')}><Icon name='user circle' />victor_nivet@outlook.fr</a></p></List.Item>
                          <List.Item activeClassName="active"><p><a onClick={e => this.selectAccount('victor_nivet@free.fr')}><Icon name='user circle' />victor_nivet@free.fr</a></p></List.Item>
                        </List>
                      </Segment>
                    </Segment.Group>
                  </Form.Field>
                </div>
                :
                <div>
                  <Form.Field>
                    <p className='backPointer' onClick={this.back}><Icon name='arrow left'/>Back</p>
                  </Form.Field>
                  <Form.Field>
                    <Segment.Group className='connectWithLinkedin'>
                      <Segment className='first'>
                        <Icon name='linkedin square'/>
                        Select your Linkedin account
                      </Segment>
                      <Segment>
                        <p><a><Icon name='user circle' />victor_nivet@hotmail.fr</a></p>
                        <p><a><Icon name='user circle' />victor_nivet@gmail.com</a></p>
                        <p><a><Icon name='user circle' />victor_nivet@outlook.fr</a></p>
                        <p><a><Icon name='user circle' />victor_nivet@free.fr</a></p>
                      </Segment>
                    </Segment.Group>
                  </Form.Field>
                </div>}
        <Message error content={this.state.errorMessage} />
        <Button
            disabled={!this.state.addBookmark && (!this.state.login || !this.state.password) && !this.state.accountFacebookSelected}
            attached='bottom'
            type="submit"
            positive
            loading={this.state.loading}
            onClick={this.confirm}
            class="modal-button uppercase"
            content={'CONFIRM'} />
      </Form>
  )
};

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
      errorMessage: '',
      url: this.props.modal.website.landing_url,
      login: '',
      password: '',
      loading: false,
      facebook: false,
      linkedin: false,
      addBookmark: false,
      accountFacebookSelected: '',
      profiles: [],
      selectedProfile: -1,
      view: 1
    }
  }
  componentWillMount(){
    api.dashboard.fetchProfiles().then(profiles => {
      this.setState({profiles: profiles});
      const logwith = this.props.modal.website.connectWith_websites.map(item => {
        let website = selectItemFromListById(this.props.catalog.websites, item);
        let apps = [];
        profiles.map(item => {
          item.apps.map(app => {
            if (app.website_id === website.id)
              apps.push(item);
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
  connectWith = (string) => {
    if (string === 'facebook')
      this.setState({ facebook: true, login: '', password: '' });
    else
      this.setState({ linkedin: true, login: '', password: '' });
  };

  toggleBookmark = () => {
    if (this.state.addBookmark)
      this.setState({ addBookmark: false, url: '' });
    else
      this.setState({ addBookmark: true, login: '', password: '' });
  };

  back = () => {
    this.setState({ facebook: false, linkedin: false, accountFacebookSelected: '' });
  };

  selectAccount = (account) => {
    this.setState({ accountFacebookSelected: account });
  };

  selectProfile = (id) => {
    this.setState({selectedProfile: id});
  };

  confirm = () => {
    console.log(this.state.name);
    if (this.state.view === 1) {
      this.setState({ view: 2 });
    }
    else {
      this.setState({loading: true, errorMessage: ''});
      if (this.state.addBookmark) {
        this.props.catalogAddBookmark({
          name: this.state.name,
          profile_id: this.state.selectedProfile,
          url: this.state.url,
          img_url: this.props.modal.website.img_url
        }).then(r => {
          this.setState({loading: false});
          this.props.showCatalogAddAppModal({active: false})
        }).catch(err => {
          this.setState({loading: false});
          this.setState({errorMessage: err});
        });
      }
      else {
        this.props.catalogAddClassicApp({
          name: this.state.name,
          website_id: this.props.modal.website.id,
          profile_id: this.state.selectedProfile,
          account_information: { login: this.state.login, password: this.state.password }
        }).then(r => {
          this.setState({loading: false});
          this.props.showCatalogAddAppModal({active: false})
        }).catch(err => {
          this.setState({loading: false});
          this.setState({errorMessage: err});
        });
      }
    }
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
              changeView={this.changeView}/>}
        </SimpleModalTemplate>
    )
  }
}

export default ClassicAppModal;