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
    this.state = {
      dropdown: false
    };
    this.onMouseDown = this.onMouseDown.bind(this);
    this.onMouseUp = this.onMouseUp.bind(this);
    this.pageClick = this.pageClick.bind(this);
  }
  onMouseDown(){
    this.mouseInDropDown = true;
  }
  onMouseUp(){
    this.mouseInDropDown = false;
  }
  pageClick(e){
    if (this.mouseInDropDown)
      return;
    this.setState({dropdown: false});
  }
  componentDidMount(){
    window.addEventListener('mousedown', this.pageClick, false);
  }
  componentWillUnmount(){
    window.removeEventListener('mousedown', this.pageClick, false);
  }
  render() {
    return (
        <div class="contents" id="first_step">
          <div class="content_row">
            <h1 class="full_width">Invite a team member</h1>
          </div>
          <div class="content_row">
            <div class="signed_input">
              <label htmlFor="email_input">Email address</label>
              <input value={this.props.email}
                     onChange={(e) => {this.props.handleInputs(e.target.name, e.target.value)}}
                     id="email_input" name="email" type="email" placeholder="name@company.com" class="full_width modal_input"/>
            </div>
            <div class="signed_input">
              <label htmlFor="fname_input">First name</label>
              <input value={this.props.fname}
                     onChange={(e) => {this.props.handleInputs(e.target.name, e.target.value)}}
                     id="fname_input" name="fname" type="text" placeholder="Optional" class="full_width modal_input"/>
            </div>
            <div class="signed_input">
              <label htmlFor="lname_input">Last name</label>
              <input value={this.props.lname}
                     onChange={(e) => {this.props.handleInputs(e.target.name, e.target.value)}}
                     id="lname_input" name="lname" type="text" placeholder="Optional" class="full_width modal_input"/>
            </div>
          </div>
          <div class="content_row">
            <div class="signed_input">
              <label htmlFor="username_input">Username</label>
              <input value={this.props.username}
                     onChange={(e) => {this.props.handleInputs(e.target.name, e.target.value)}}
                     id="username_input" name="username" type="text" class="full_width modal_input" placeholder="Username"/>
            </div>
            <div class="signed_input">
              <label htmlFor="user_role_select">User role</label>
              <select value={this.props.role}
                      onChange={(e) => {this.props.handleInputs(e.target.name, e.target.value)}}
                      id="user_role_select" name='role' class="full_width select_unstyle modal_input">
                <option value="1">Member</option>
                <option value="2">Admin</option>
              </select>
            </div>
            <div class="signed_input">
              <label htmlFor="departure_date_input">Departure date (optional)</label>
              <input value={this.props.departure_date}
                     onChange={(e) => {this.props.handleInputs(e.target.name, e.target.value)}}
                     id="departure_date_input" name="departure_date" type="date" class="full_width modal_input" placeholder="Optional"/>
            </div>
          </div>
          <div class="content_row">
            <div class="tagged_input_container full_width">
              <label htmlFor="teams_input">Team(s)</label>
              <div class="modal_input_wrapper full_width item_list"
                   onMouseDown={this.onMouseDown}
                   onMouseUp={this.onMouseUp}>
                {
                  this.props.selectedChannels.map(function(item){
                    return (
                        <div class="input_tag" key={item.id}>
                          <span>{item.name}</span>
                          <button class="button-unstyle" onClick={this.props.deselectChannelFunc.bind(null, item.id)}>
                            <i class="fa fa-times"/>
                          </button>
                        </div>
                    )
                  }, this)
                }
                <input id="teams_input" class="full_width input_unstyle" name="teams" type="text" placeholder="Search by name"
                       onFocus={(e) => {this.setState({dropdown: true})}}/>
                <div class={classnames("floating_dropdown", this.state.dropdown ? "show" : null)}>
                  <div class="dropdown_content">
                    {
                      this.props.channels.map(function (item) {
                        return (
                            <div onClick={this.props.selectChannelFunc.bind(null, item.id)} class={classnames("dropdown_row selectable", item.selected ? "selected": null)} key={item.id}>
                              <span class="main_value">{item.name}</span>{item.purpose.length && <span class="text-muted">&nbsp;- {item.purpose}</span>}
                            </div>
                        )
                      }, this)
                    }
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="content_row buttons_row">
            <div class="buttons_wrapper">
              <button class="button-unstyle neutral_background action_text_button cancel_button"
                      onClick={e => {this.props.dispatch(showAddTeamUserModal(false))}}>
                Cancel
              </button>
              <button class="button-unstyle positive_background action_text_button next_button"
                      onClick={this.props.validateStep}>
                Next
              </button>
            </div>
          </div>
        </div>
    )
  }
}

class FirstStepAddUser2 extends React.Component{
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
                    options={[{key:1, value:1, text:'Member'}, {key:2, value:2, text:'Admin'}]}
                />
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
              <button class="button-unstyle action-text-button only-text-button"
                      onClick={this.props.incrementAndReject.bind(null, channel.id)}>
                <u>Do not add to #{channel.name}</u>
              </button>
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
      value: [],
      step: 0,
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
    this.handleInputs = this.handleInputs.bind(this);
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
    this.setState({value: value});
  }
  validateFirstStep(e){
    e.preventDefault();
    this.setState({firstStepErrorMessage: ''});
    var channels = this.state.value.map(item => {
      return selectChannelFromListById(this.state.channels, item);
    });
    if (!channels.length){
      this.props.dispatch(userActions.createTeamUser(this.state.fname, this.state.lname, this.state.email, this.state.username,this.state.departure_date, this.state.role)).then(response => {
        this.props.dispatch(showAddTeamUserModal(false));
      }).catch(err => {
        this.setState({firstStepErrorMessage: err});
      });
      return;
    }
    var calls = channels.map(function(item){
      return api.fetchTeamChannelApps(this.props.team_id, item.id);
    }, this);
    var selectedChannels = [];
    this.props.dispatch(userActions.createTeamUser(this.state.fname, this.state.lname, this.state.email, this.state.username,this.state.departure_date, this.state.role)).then(response => {
      var user_id = response.id;
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
          return item;
        });
        this.setState({step: 1, selectedChannels: selectedChannels, maxChannelStep: selectedChannels.length, user_id:user_id});
      });
    }).catch(err => {
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
      return this.props.dispatch(channelActions.addTeamUserToChannel(item.id, this.state.user_id));
    }, this);
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
  handleInputs(name, value){
    this.setState({[name]: value});
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
              <FirstStepAddUser2 key="1"
                                 me={this.props.me}
                                 email={this.state.email}
                                 fname={this.state.fname}
                                 lname={this.state.lname}
                                 role={this.state.role}
                                 username={this.state.username}
                                 handleReactInput={this.handleReactInput}
                                 departure_date={this.state.departure_date}
                                 handleInputs={this.handleInputs}
                                 channels={this.state.channels}
                                 selectedChannels={this.state.selectedChannels}
                                 selectChannelFunc={this.selectChannel}
                                 deselectChannelFunc={this.deselectChannel}
                                 validateStep={this.validateFirstStep}
                                 options={this.state.options}
                                 value={this.state.value}
                                 errorMessage={this.state.firstStepErrorMessage}
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