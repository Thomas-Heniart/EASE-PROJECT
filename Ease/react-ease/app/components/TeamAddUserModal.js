var React = require('react');
var classnames = require('classnames');
var api = require('../utils/api');
import {connect} from "react-redux";
import ReactCSSTransitionGroup from "react-addons-css-transition-group";
var SimpleAppSharingPreview = require('./teamAppsPreview/SimpleAppSharingPreview');
var LinkAppSharingPreview = require('./teamAppsPreview/LinkAppSharingPreview');
var MultiAppSharingPreview = require('./teamAppsPreview/MultiAppSharingPreview');
import * as userActions from "../actions/userActions"
import * as appActions from "../actions/appsActions"
import * as channelActions from "../actions/channelActions"
import {showAddTeamUserModal} from "../actions/teamModalActions"
import {selectChannelFromListById} from "../utils/helperFunctions"
import {renderRoomLabel} from "../utils/renderHelpers";
import { Header, Container, Segment, Checkbox, Form, Input, Select, Dropdown, Button, Message } from 'semantic-ui-react';

class FirstStepAddUser extends React.Component{
  constructor(props){
    super(props);
  }
  render() {
    return (
        <div class="contents" id="first_step">
          <Container>
            <Header as="h1">
              Invite a team member
            </Header>
            <Form onSubmit={this.props.validateStep} error={this.props.errorMessage.length > 0}>
              <Form.Group>
                <Form.Input label="Email address" type="email" name="email"
                            onChange={this.props.handleReactInput}
                            placeholder="name@company.com"
                            required
                            width={6}/>
                <Form.Input label="First name" type="text" name="fname"
                            onChange={this.props.handleReactInput}
                            placeholder="Optional"
                            width={5}/>
                <Form.Input label="Last name" type="text" name="lname"
                            onChange={this.props.handleReactInput}
                            placeholder="Optional" width={5}/>
              </Form.Group>
              <Form.Group>
                <Form.Input label="Username" type="text" name="username"
                            onChange={this.props.handleReactInput}
                            required
                            placeholder="Username" width={6}/>
                <Form.Select
                    style={{minWidth: '0px'}}
                    name="role"
                    defaultValue={1}
                    onChange={this.props.handleReactInput}
                    label="User role"
                    width={4}
                    options={[{key:1, value:1, text:'Member'}, {key:2, value:2, text:'Admin'}]}/>
                <Form.Input label="Departure date (optional)" type="date"
                            onChange={this.props.handleReactInput}
                            name="departure_date"
                            placeholder="Optional" width={6}/>
              </Form.Group>
              <Form.Dropdown
                  search={true}
                  fluid
                  selection={true}
                  multiple
                  options={this.props.options}
                  value={this.props.value}
                  onChange={this.props.dropdownChange}
                  renderLabel={renderRoomLabel}
                  placeholder="Choose room(s)"
                  label="Room(s)"/>
              <Message error content={this.props.errorMessage}/>
              <Form.Field>
                <Button
                    positive
                    floated='right'
                    loading={this.props.loading}
                    type="submit">
                  Next
                </Button>
                <Button floated='right' onClick={e => {this.props.dispatch(showAddTeamUserModal(false))}}>Cancel</Button>
              </Form.Field>
            </Form>
          </Container>
        </div>
    )
  }
}

class SecondStep extends React.Component {
  constructor(props){
    super(props);
  }
  render(){
    const username = this.props.username;
    const step = this.props.step;
    const maxStep = this.props.maxStep;
    const channels = this.props.selectedChannels;
    const channel = this.props.selectedChannels[step];

    return (
        <div class="contents" id="second_step">
          <div class="content_row">
            <span>
              You are adding <strong>{username}</strong> to&nbsp;
              {
                channels.map(function(item, idx){
                  if (idx === step)
                    return (
                        <strong key={item.id} style={{color:'#4990E2'}}>
                          #{item.name}
                          {!(idx === (channels.length - 1)) && ', '}
                        </strong>);
                  else
                    return (
                        <span key={item.id}>
                        #{item.name}
                          {!(idx === (channels.length - 1)) && ', '}
                      </span>);
                })
              }
              &nbsp;({step+1}/{maxStep})
            </span>
          </div>
          <div class="content_row">
            <h1 class="full_width" style={{marginBottom:0}}>Share apps from #{channel.name} to <strong>{username}</strong></h1>
          </div>
          <div class="content_row">
            <span>Select/deselect the ones you want to share/don't share.</span>
          </div>
          <div class="content_row flex_direction_column">
            {
              channel.apps.map(function(item){
                if (item.type === 'simple')
                  return (
                      <SimpleAppSharingPreview
                          key={item.id}
                          app={item}
                          users={this.props.users}
                          checkAppFunc={this.props.checkAppFunc.bind(null, channel.id)}
                      />
                  );
                else if (item.type === 'link')
                  return (
                      <LinkAppSharingPreview
                          key={item.id}
                          app={item}
                          users={this.props.users}
                          checkAppFunc={this.props.checkAppFunc.bind(null, channel.id)}
                      />
                  );
                else if (item.type === 'multi')
                  return (
                      <MultiAppSharingPreview
                          key={item.id}
                          app={item}
                          username={username}
                          users={this.props.users}
                          checkAppFunc={this.props.checkAppFunc.bind(null, channel.id)}
                          handleCredentialsInput={this.props.handleCredentialsInput.bind(null, channel.id)}
                      />
                  );
              }, this)
            }
          </div>
          <div class="content_row buttons_row">
            <div class="buttons_wrapper">
              {!channel.default &&
              <button class="button-unstyle action-text-button only-text-button"
                      onClick={this.props.incrementAndReject.bind(null, channel.id)}>
                <u>Do not add to #{channel.name}</u>
              </button>}
              <button class="button-unstyle positive_background action_text_button next_button"
                      onClick={this.props.incrementAndValidate.bind(null, channel.id)}>
                Next
              </button>
            </div>
          </div>
        </div>
    )
  }
}

@connect((store)=>{
  return {
    channels: store.channels.channels,
    users: store.users.users,
    team_id: store.team.id,
    me: store.users.me
  };
})
class TeamAddUserModal extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      user_id: null,
      email: '',
      fname: '',
      lname: '',
      role: 1,
      username: '',
      departure_date: '',
      selectedChannels: [],
      channels: [],
      options: [],
      defaultRooms: [],
      value: [],
      step: 0,
      firstStepLoading: false,
      firstStepErrorMessage: '',
      channelStep: 0,
      maxChannelStep:0
    };
    this.state.channels = this.props.channels.map(function (item) {
      item.selected = false;
      return item;
    });
    this.state.options = this.props.channels.map(item => {
      return {
        key: item.id,
        text: item.purpose.length > 0 ? `${item.name} - ${item.purpose}` : item.name,
        value: item.id,
        name: item.name
      }
    });
    this.state.value = this.props.channels.map(item => {
      if (item.default)
        return item.id;
      return null;
    }).filter(item => {
      return item !== null;
    });
    this.state.defaultRooms = this.state.value.map(item => (item));

    this.handleReactInput = this.handleReactInput.bind(this);
    this.selectChannel = this.selectChannel.bind(this);
    this.deselectChannel = this.deselectChannel.bind(this);
    this.validateFirstStep = this.validateFirstStep.bind(this);
    this.incrementStep = this.incrementStep.bind(this);
    this.decrementStep = this.decrementStep.bind(this);
    this.checkChannelApp = this.checkChannelApp.bind(this);
    this.handleCredentialsInput = this.handleCredentialsInput.bind(this);
    this.incrementStepAndRejectChannel = this.incrementStepAndRejectChannel.bind(this);
    this.incrementStepAndValidateChannel = this.incrementStepAndValidateChannel.bind(this);
    this.validateSecondStep = this.validateSecondStep.bind(this);
    this.dropdownChange = this.dropdownChange.bind(this);
  }
  handleReactInput(e, {name, value}){
    this.setState({[name]: value});
  }
  dropdownChange(e, {value}){
    let defaultRooms = this.state.defaultRooms;
    for (let i = 0; i < defaultRooms.length; i ++){
      if (value.indexOf(defaultRooms[i]) === -1)
        return;
    }
    this.setState({value: value});
  }
  validateFirstStep(e){
    e.preventDefault();
    this.setState({firstStepErrorMessage: ''});
    this.setState({firstStepLoading: true});
    let channels = this.state.value.map(item => {
      return selectChannelFromListById(this.state.channels, item);
    });
    let calls = channels.map(function(item){
      return api.fetchTeamChannelApps(this.props.team_id, item.id);
    }, this);
    let selectedChannels = [];
    this.props.dispatch(userActions.createTeamUser(this.state.fname, this.state.lname, this.state.email, this.state.username,this.state.departure_date, this.state.role)).then(response => {
      let user_id = response.id;
      Promise.all(calls).then(values => {
        selectedChannels = channels.map(function(item, idx){
          item.apps = values[idx].map(function(item){
            item.selected = false;
            if (item.type === 'multi'){
              item.credentials = {};
              Object.keys(item.website.information).map(function (info) {
                item.credentials[info] = '';
              });
            }
            return item;
          });
          item.confirmed = false;
          if (item.apps.length === 0)
            return null;
          return item;
        }).filter((item) => (item !== null));
        if (selectedChannels.length === 0){
          this.props.dispatch(showAddTeamUserModal(false));
          return;
        }
        this.setState({firstStepLoading: false});
        this.setState({step: 1, selectedChannels: selectedChannels, maxChannelStep: selectedChannels.length, user_id:user_id});
      });
    }).catch(err => {
      this.setState({firstStepLoading: false});
      this.setState({firstStepErrorMessage: err});
    });
  }
  handleCredentialsInput(channel_id, app_id, credentialName, credentialValue){
    var channels = this.state.selectedChannels.map(function (item) {
      if (channel_id === item.id){
        for (var i = 0; i < item.apps.length; i++){
          if (item.apps[i].id === app_id){
            item.apps[i].credentials[credentialName] = credentialValue;
            break;
          }
        }
      }
      return item;
    });
    this.setState({selectedChannels: channels});
  }
  checkChannelApp(channel_id, app_id){
    var channels = this.state.selectedChannels.map(function (item) {
      if (channel_id === item.id){
        for (var i = 0; i < item.apps.length; i++){
          if (item.apps[i].id === app_id){
            item.apps[i].selected = !item.apps[i].selected;
            break;
          }
        }
      }
      return item;
    });
    this.setState({selectedChannels: channels});
  }
  incrementStep(){
    const channelStep = this.state.channelStep + 1;
    if (channelStep === this.state.maxChannelStep){
      this.validateSecondStep();
      return;
    }
    if (this.state.step > 0){
      if (channelStep === this.state.maxChannelStep){
        this.props.dispatch(showAddTeamUserModal(false));
        console.log('validate user');
        return;
      }
      this.setState({channelStep: channelStep});
    }else {
      this.setState({step: this.state.step + 1});
    }
  }
  incrementStepAndValidateChannel(channel_id){
    const channels = this.state.selectedChannels.map(function(item){
      if (item.id === channel_id){
        item.confirmed = true;
      }
      return item;
    });
    this.setState({selectedChannels: channels});
    this.incrementStep();
  }
  incrementStepAndRejectChannel(channel_id){
    const channels = this.state.selectedChannels.map(function(item){
      if (item.id === channel_id){
        item.confirmed = false;
      }
      return item;
    });
    this.setState({selectedChannels: channels});
    this.incrementStep();
  }
  decrementStep(){
    if (this.state.channelStep > 0){
      this.setState({channelStep: this.state.channelStep - 1});
    }else{
      this.setState({step: this.state.step - 1});
    }
  }
  validateSecondStep(){
    var joinChannels = [];
    var shareApps = [];
    var app;
    console.log('validate second step');
    const channels = this.state.selectedChannels.filter(function(item){
      return item.confirmed;
    });
    joinChannels = channels.map(function(item){
      if (!item.default)
        return this.props.dispatch(channelActions.addTeamUserToChannel(item.id, this.state.user_id));
      return null;
    }, this).filter(item => (item !== null));
    Promise.all(joinChannels).then(() => {
      for (var i = 0; i < channels.length; i++){
        for (var j = 0; j < channels[i].apps.length; j++){
          app = channels[i].apps[j];
          if (app.selected){
            shareApps.push(this.props.dispatch(appActions.teamShareApp(app.id, {team_user_id: this.state.user_id, account_information: app.credentials})));
          }
        }
      }
      Promise.all(shareApps).then(() => {
        this.props.dispatch(showAddTeamUserModal(false));
      });
    });
  }
  deselectChannel(id){
    var selectedChannels = this.state.selectedChannels;
    var channels = this.state.channels.map(function (item) {
      if (item.id === id){
        selectedChannels.splice(selectedChannels.indexOf(item), 1);
        item.selected = false;
      }
      return item;
    });
    this.setState({channels: channels, selectedChannels: selectedChannels});
  }
  selectChannel(id){
    var selectedChannels = this.state.selectedChannels;
    var channels = this.state.channels.map(function(item){
      if (item.id === id) {
        if (item.selected)
          return item;
        item.selected = true;
        selectedChannels.push(item);
      }
      return item;
    });
    this.setState({channels: channels, selectedChannels: selectedChannels});
  }
  render(){
    return (
        <div class="ease_modal" id="add_user_modal">
          <div class="modal-background"></div>
          {this.state.channelStep > 0 &&
          <a id="ease_modal_back_btn" class="ease_modal_btn"
             onClick={this.decrementStep}>
            <i class="ease_icon fa fa-step-backward"/>
            <span class="key_label">back</span>
          </a>}
          <a id="ease_modal_close_btn" class="ease_modal_btn" onClick={e => {this.props.dispatch(showAddTeamUserModal(false))}}>
            <i class="ease_icon fa fa-times"/>
            <span class="key_label">close</span>
          </a>
          <div class="modal_contents_container">
            <ReactCSSTransitionGroup
                transitionName="slideLeft"
                transitionAppear={true}
                transitionAppearTimeout={200}
                transitionEnterTimeout={200}
                transitionLeaveTimeout={200}>
              {this.state.step === 0 &&
              <FirstStepAddUser key="1"
                                me={this.props.me}
                                email={this.state.email}
                                fname={this.state.fname}
                                lname={this.state.lname}
                                role={this.state.role}
                                username={this.state.username}
                                handleReactInput={this.handleReactInput}
                                departure_date={this.state.departure_date}
                                channels={this.state.channels}
                                selectedChannels={this.state.selectedChannels}
                                selectChannelFunc={this.selectChannel}
                                deselectChannelFunc={this.deselectChannel}
                                validateStep={this.validateFirstStep}
                                options={this.state.options}
                                value={this.state.value}
                                errorMessage={this.state.firstStepErrorMessage}
                                loading={this.state.firstStepLoading}
                                dropdownChange={this.dropdownChange}
                                dispatch={this.props.dispatch}/>
              }
              {this.state.step === 1 &&
              <SecondStep key="2"
                          step={this.state.channelStep}
                          maxStep={this.state.maxChannelStep}
                          username={this.state.username}
                          incrementAndValidate={this.incrementStepAndValidateChannel}
                          incrementAndReject={this.incrementStepAndRejectChannel}
                          selectedChannels={this.state.selectedChannels}
                          checkAppFunc={this.checkChannelApp}
                          handleCredentialsInput={this.handleCredentialsInput}
                          users={this.props.users}
                          dispatch={this.props.dispatch}/>
              }
            </ReactCSSTransitionGroup>
          </div>
        </div>
    )
  }
}

module.exports = TeamAddUserModal;