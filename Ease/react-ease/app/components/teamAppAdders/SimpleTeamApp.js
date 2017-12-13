import React, {Component} from "react";
import classnames from "classnames";
import {Button, Container, Dropdown, Header, Icon, Input, Label, Popup, Segment} from 'semantic-ui-react';
import * as modalActions from "../../actions/teamModalActions";
import {
  SingleAppCopyPasswordButton,
  PasswordChangeDropdown,
  PasswordChangeHolder,
  renderSimpleAppEditUserLabel,
  setUserDropdownText,
  SharingRequestButton,
  TeamAppActionButton
} from "./common";
import {
  removeTeamCardReceiver, requestTeamSingleCard,
  teamEditSingleApp,
  teamEditSingleCardReceiver,
  teamShareSingleCard
} from "../../actions/appsActions";
import {
  credentialIconType,
  handleSemanticInput,
  reflect,
  transformCredentialsListIntoObject,
  transformWebsiteInfoIntoListAndSetValues
} from "../../utils/utils";
import {
  getReceiverInList,
  isAdmin,
  selectItemFromListById,
  sortReceiversAndMap
} from "../../utils/helperFunctions";
import {addNotification} from "../../actions/notificationBoxActions";
import {connect} from "react-redux";

const TeamAppCredentialInput = ({item, onChange, disabled, readOnly}) => {
  return <Input size="mini"
                class="team-app-input"
                required={item.name !== 'password'}
                readOnly={readOnly}
                disabled={disabled}
                name={item.name}
                onChange={onChange}
                label={<Label><Icon name={credentialIconType[item.name]}/></Label>}
                labelPosition="left"
                placeholder={item.name === 'password' ? '••••••••' : item.placeholder}
                value={item.name === 'password' && readOnly ? 'abcdabcd' : item.value}
                type={item.type}>
  </Input>
};

const TeamSimpleAppButtonSet = ({app, me, dispatch, editMode, selfJoin, requestApp}) => {
  const meReceiver = app.receivers.find(receiver => (receiver.team_user_id === me.id));
  const asked = !!app.requests.find(request => (request.team_user_id === me.id));

  return (
      <div class="team_app_actions_holder">
        {!meReceiver &&
        <TeamAppActionButton text={isAdmin(me.role) ? 'Join App' : asked ? 'Request Sent' : 'Ask to join'}
                             onClick={isAdmin(me.role) ? selfJoin : asked ? null : requestApp}
                             icon="pointing up"
                             disabled={asked}/>}
        {isAdmin(me.role) &&
        <TeamAppActionButton text='Edit App' icon='pencil' onClick={editMode}/>}
        {isAdmin(me.role) &&
        <TeamAppActionButton text='Delete App' icon='trash outline' onClick={e => {dispatch(modalActions.showTeamDeleteAppModal({active: true, app_id: app.id}))}}/>}
      </div>
  )
};

const TeamAppReceiverLabel = ({admin, username, accepted, can_see_information}) => {
  return (
      <Popup size="mini"
             position="bottom center"
             inverted
             flowing
             hideOnScroll={true}
             trigger={
               <Label class={classnames("user-label static accepted", can_see_information ? 'can_see_information' : null)}>
                 {username}
                 {admin &&
                 <Icon name={can_see_information ? 'unhide' : 'hide'}/>}&nbsp;
                 {can_see_information &&
                 <Icon name='mobile'/>}
               </Label>
             }
             content={
               <div>
                 {can_see_information &&
                 <span>Mobile access: on</span>}
                 {!can_see_information &&
                 <span>Mobile access: off</span>}
                 <br/>
                 {can_see_information &&
                 <span>Password copy: on</span>}
                 {!can_see_information &&
                 <span>Password copy: off</span>}
               </div>}/>
  )
};

class ReceiversLabelGroup extends Component {
  constructor(props){
    super(props);
    this.state = {
      show_all: false
    }
  }
  showAll = (state) => {
    this.setState({show_all: state});
  };
  render() {
    const receivers = this.props.receivers;
    return (
        <Label.Group>
          {this.props.receivers.map((item, idx) => {
            if (!this.state.show_all && idx > 14)
              return null;
            const user = item.user;
            const receiver = item.receiver;
            return (
                <TeamAppReceiverLabel key={receiver.team_user_id}
                                      admin={this.props.meAdmin}
                                      username={user.username}
                                      can_see_information={receiver.allowed_to_see_password}
                                      accepted={receiver.accepted}/>
            )
          })}
          {receivers.length > 15 && !this.state.show_all &&
          <Button size="mini" type="button" class="label fw-normal" onClick={this.showAll.bind(null, true)}>
            <Icon name="add user"/>
            {receivers.length - 15}&nbsp;users
          </Button>}
          {receivers.length > 15 && this.state.show_all &&
          <Button size="mini" type="button"  class="label fw-normal" onClick={this.showAll.bind(null, false)}>
            <Icon name="remove user"/>
            Show less
          </Button>}
        </Label.Group>
    )
  }
};

const AcceptRefuseAppHeader = ({pinneable, onAccept, onRefuse}) => {
  if (pinneable)
    return (
        <span style={{lineHeight: '1.7'}}>
          You received a Single App,
          &nbsp;
          <button class="button-unstyle inline-text-button primary" type="button" onClick={onAccept}>Accept</button>
          &nbsp;or&nbsp;
          <button class="button-unstyle inline-text-button primary" type="button" onClick={onRefuse}>Refuse</button>
          &nbsp;it?
        </span>
    );
  else
    return (
        <span style={{lineHeight: '1.7'}}>
          This app is new to our robot, we are processing the integration. It will be ready in few hours.
        </span>
    )
};

@connect(store => ({
  teams: store.teams
}))
class SimpleTeamApp extends Component {
  constructor(props){
    super(props);
    this.state = {
      loading: false,
      edit: false,
      name: '',
      credentials: [],
      password_reminder_interval: 0,
      description: '',
      users: [],
      selected_users: []
    }
  }
  handleInput = handleSemanticInput.bind(this);
  toggleCanSeeInformation = (id) => {
    let users = this.state.users.map(item => {
      return {
          ...item,
        can_see_information: item.id === id ? (isAdmin(item.user.role) ? item.can_see_information : !item.can_see_information) : item.can_see_information
      }
    });
    this.setState({users: users});
  };
  handleCredentialInput = (e, {name, value}) => {
    let credentials = this.state.credentials.map(item => {
      if (name === item.name)
        item.value = value;
      return item;
    });
    this.setState({credentials: credentials});
  };
  modify = (e) => {
    e.preventDefault();
    this.setState({loading: true});
    this.props.dispatch(teamEditSingleApp({
      name: this.state.name,
      team_id: this.props.app.team_id,
      team_card_id: this.props.app.id,
      description: this.state.description,
      password_reminder_interval: this.state.password_reminder_interval,
      account_information: transformCredentialsListIntoObject(this.state.credentials)
    })).then(response => {
      const app = response;
      const receivers = app.receivers;
      let deleting = [];
      let edit = [];
      let sharing = [];
      this.state.users.map(item => {
        const selected = this.state.selected_users.indexOf(item.id) !== -1;
        const receiver = getReceiverInList(receivers, item.id);
        if (!selected && !!receiver)
          deleting.push(this.props.dispatch(removeTeamCardReceiver({
            team_id:this.props.app.team_id,
            team_card_id: this.props.app.id,
            team_card_receiver_id: receiver.id})));
        if (!receiver && selected)
          sharing.push(this.props.dispatch(teamShareSingleCard({
            team_id: this.props.app.team_id,
            team_card_id: app.id,
            team_user_id: item.id,
            allowed_to_see_password: item.can_see_information})));
        if (selected && !!receiver && item.can_see_information !== receiver.allowed_to_see_password)
          edit.push(this.props.dispatch(teamEditSingleCardReceiver({
            team_id: this.props.app.team_id,
            team_card_id: this.props.app.id,
            team_card_receiver_id: receiver.id,
            allowed_to_see_password: item.can_see_information})));
      });
      const calls = deleting.concat(sharing, edit);
      Promise.all(calls.map(reflect)).then(response => {
        this.props.dispatch(addNotification({
          text: `${app.name} successfully modified!`
        }));
        this.setEdit(false);
      });
    }).catch(err => {
      console.log(err);
    });
  };
  setupUsers = () => {
    const channel = selectItemFromListById(this.props.channels, this.props.app.channel_id);
    let selected_users = [];
    const users = channel.team_user_ids.map(item => {
      const user = selectItemFromListById(this.props.users, item);
      return {
        key: item,
        text: setUserDropdownText(user),
        value: item,
        id: item,
        username: user.username,
        user: user,
        toggleCanSeeInformation: this.toggleCanSeeInformation.bind(null, item)
      }
    }).sort((a, b) => {
      if (a.id === this.props.me.id)
        return -1000;
      else if (b.id === this.props.me.id)
        return 1000;
      return a.username.localeCompare(b.username);
    }).map(item => {
      const receiver = getReceiverInList(this.props.app.receivers, item.id);
      const can_see_information = !!receiver ? receiver.allowed_to_see_password : !!isAdmin(item.user.role);
      if (!!receiver)
        selected_users.push(item.id);
      return {
        ...item,
        can_see_information: can_see_information,
        receiver: receiver
      }
    });
    this.setState({users: users, selected_users:selected_users});
  };
  setEdit = (state) => {
    if (state){
      const app = this.props.app;
      let credentials = transformWebsiteInfoIntoListAndSetValues(app.website.information, app.account_information);
      this.setupUsers();
      this.setState({credentials:credentials, password_reminder_interval: app.password_reminder_interval, description: app.description, name: app.name});
    }
    this.setState({edit: state, loading: false});
  };
  selfJoinApp = () => {
    const team_card = this.props.app;
    const me = this.props.me;

    this.props.dispatch(teamShareSingleCard({
      team_id: team_card.team_id,
      team_card_id: team_card.id,
      team_user_id: me.id,
      allowed_to_see_password: true
    }));
  };
  requestApp = () => {
    const team_card = this.props.app;
    this.props.dispatch(requestTeamSingleCard({
      team_id: team_card.team_id,
      team_card_id: team_card.id
    }));
  };
  render(){
    const app = this.props.app;
    const me = this.props.me;
    const room_manager = selectItemFromListById(this.props.users, selectItemFromListById(this.props.channels, app.channel_id).room_manager_id);
    const meReceiver = getReceiverInList(app.receivers, me.id);
    const userReceiversMap = sortReceiversAndMap(app.receivers, this.props.users, me.id);
    const website = app.website;
    const credentials = !this.state.edit ?
        transformWebsiteInfoIntoListAndSetValues(website.information, app.account_information).map(item => {
          return <TeamAppCredentialInput key={item.priority}
                                         readOnly={true}
                                         item={item}/>
        }) : this.state.credentials.map(item => {
          return <TeamAppCredentialInput key={item.priority}
                                         onChange={this.handleCredentialInput}
                                         item={item}/>
        });
    return (
        <Container fluid id={`app_${app.id}`} class="team-app mrgn0 simple-team-app" as="form" onSubmit={this.modify}>
          <Segment>
            <Header as="h4">
              {!this.state.edit ?
                  app.name :
                  <Input size="mini"
                         class="team-app-input"
                         onChange={this.handleInput}
                         name="name"
                         value={this.state.name}
                         placeholder="Card name..."
                         type="text"
                         required/>}
              {app.requests.length > 0 && isAdmin(me.role) &&
              <SharingRequestButton onClick={e => {this.props.dispatch(modalActions.showTeamManageAppRequestModal({active: true, team_card_id: app.id}))}}/>}
            </Header>
            {!this.state.edit &&
            <TeamSimpleAppButtonSet app={app}
                                    me={me}
                                    selfJoin={this.selfJoinApp}
                                    requestApp={this.requestApp}
                                    dispatch={this.props.dispatch}
                                    editMode={this.setEdit.bind(null, true)}/>}
            <div class="display_flex">
              <div class="logo_column">
                <div class="logo">
                  <img src={website.logo}/>
                </div>
              </div>
              <div class="main_column">
                <div class="credentials">
                  {credentials}
                  {((!!meReceiver && meReceiver.allowed_to_see_password) || me.id === room_manager.id) &&
                  <SingleAppCopyPasswordButton team_card_id={app.id}/>}
                  <div class="display-inline-flex">
                    {!this.state.edit ?
                        <PasswordChangeHolder value={app.password_reminder_interval} roomManager={room_manager.username} /> :
                        <PasswordChangeDropdown value={this.state.password_reminder_interval} onChange={this.handleInput} roomManager={room_manager.username}/>}
                  </div>
                </div>
                <div>
                  {!this.state.edit ?
                      <ReceiversLabelGroup meAdmin={isAdmin(me.role)} receivers={userReceiversMap}/> :
                      <Dropdown
                          class="mini"
                          search={true}
                          fluid
                          name="selected_users"
                          options={this.state.users}
                          onChange={this.handleInput}
                          value={this.state.selected_users}
                          selection={true}
                          renderLabel={renderSimpleAppEditUserLabel}
                          multiple
                          noResultsMessage='No more results found'
                          placeholder="Tag your team members here..."/>}
                </div>
                {(this.state.description || app.description || this.state.edit) &&
                <div>
                  <Input size="mini"
                         fluid
                         class="team-app-input"
                         onChange={this.handleInput}
                         name="description"
                         readOnly={!this.state.edit}
                         value={this.state.edit ? this.state.description : app.description}
                         placeholder="You can add a comment here"
                         type="text"
                         label={<Label><Icon name="sticky note"/></Label>}
                         labelPosition="left"/>
                </div>}
              </div>
            </div>
          </Segment>
          {this.state.edit &&
          <div>
            <Button content="Save" floated="right" positive size="mini" loading={this.state.loading} disabled={this.state.loading}/>
            <Button content="Cancel" type="button" floated="right" onClick={this.setEdit.bind(null, false)} size="mini"/>
          </div>}
        </Container>
    )
  }
}

module.exports = SimpleTeamApp;