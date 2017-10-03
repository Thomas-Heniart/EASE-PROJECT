import React, {Component} from "react";
import classnames from "classnames";
import {Button, Container, Dropdown, Header, Icon, Input, Label, Popup, Segment} from 'semantic-ui-react';
import * as modalActions from "../../actions/teamModalActions";
import {showUpgradeTeamPlanModal} from "../../actions/teamModalActions";
import {
    CopyPasswordButton,
    PasswordChangeDropdown,
    PasswordChangeHolder,
    PasswordChangeManagerLabel,
    PinAppButton,
    renderSimpleAppUserLabel,
    setUserDropdownText,
    SharingRequestButton,
    TeamAppActionButton
} from "./common";
import {
    askJoinTeamApp,
    teamAcceptSharedApp,
    teamAppDeleteReceiver,
    teamEditSingleApp,
    teamEditSingleAppReceiver,
    teamShareSingleApp
} from "../../actions/appsActions";
import {
    credentialIconType,
    handleSemanticInput,
    reflect,
    transformCredentialsListIntoObject,
    transformWebsiteInfoIntoListAndSetValues
} from "../../utils/utils";
import {
    findMeInReceivers,
    getReceiverInList,
    isAdmin,
    selectItemFromListById,
    sortReceiversAndMap
} from "../../utils/helperFunctions";

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
  const meReceiver = findMeInReceivers(app.receivers, me.id);
  const asked = app.sharing_requests.indexOf(me.id) !== -1;
  return (
      <div class="team_app_actions_holder">
        {meReceiver === null &&
        <TeamAppActionButton text={isAdmin(me.role) ? 'Join App' : asked ? 'Request Sent' : 'Ask to join'}
                             onClick={isAdmin(me.role) ? selfJoin : asked ? null : requestApp}
                             icon="pointing up"
                             disabled={asked}/>}
        {meReceiver !== null && meReceiver.accepted &&
        <TeamAppActionButton text='Leave App' icon='sign out' onClick={e => {dispatch(modalActions.showTeamLeaveAppModal(true, app, me.id))}}/>}
        {isAdmin(me.role) &&
        <TeamAppActionButton text='Edit App' icon='pencil' onClick={editMode}/>}
        {isAdmin(me.role) &&
        <TeamAppActionButton text='Delete App' icon='trash outline' onClick={e => {dispatch(modalActions.showTeamDeleteAppModal(true, app))}}/>}
      </div>
  )
};

const TeamAppReceiverLabel = ({username, accepted, can_see_information}) => {
  return (
      <Popup size="mini"
             position="bottom center"
             inverted
             flowing
             hideOnScroll={true}
             trigger={
               <Label class={classnames("user-label static", accepted ? 'accepted' : null)}>
                 {username}
                 <Icon name={can_see_information ? 'unhide' : 'hide'}/>
               </Label>
             }
             header={<h5 class="mrgn0 text-center">User informations</h5>}
             content={
               <div>
                 {can_see_information &&
                 <span><Icon name='unhide'/> User can see the password</span>}
                 {!can_see_information &&
                 <span><Icon name='hide'/> User cannot see the password</span>}
                 <br/>
                 {accepted &&
                 <span><Icon name='circle' style={{color: '#949EB7'}}/> User accepted the app</span>}
                 {!accepted &&
                 <span><Icon name='circle' style={{color: '#D2DAE4'}}/> User didn't accept the app</span>}
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
            if (!this.state.show_all && idx > 15)
              return null;
            const user = item.user;
            const receiver = item.receiver;
            return (
                <TeamAppReceiverLabel key={receiver.team_user_id}
                                      username={user.username}
                                      can_see_information={receiver.can_see_information}
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

const AcceptRefuseAppHeader = ({onAccept, onRefuse}) => {
  return (
      <span style={{lineHeight: '1.7'}}>
        You received a Single App,
        &nbsp;
        <button class="button-unstyle inline-text-button primary" type="button" onClick={onAccept}>Accept</button>
        &nbsp;or&nbsp;
        <button class="button-unstyle inline-text-button primary" type="button" onClick={onRefuse}>Refuse</button>
        &nbsp;it?
      </span>
  )
};

class SimpleTeamApp extends Component {
  constructor(props){
    super(props);
    this.state = {
      loading: false,
      edit: false,
      credentials: [],
      password_change_interval: 0,
      description: '',
      users: [],
      selected_users: []
    }
  }
  handleInput = handleSemanticInput.bind(this);
  toggleCanSeeInformation = (id) => {
    if (this.props.plan_id === 0){
      this.props.dispatch(showUpgradeTeamPlanModal(true, 1));
      return;
    }
    let users = this.state.users.map(item => {
      return {
        ...item,
        can_see_information: item.id === id ? !item.can_see_information : item.can_see_information
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
      app_id: this.props.app.id,
      description: this.state.description,
      password_change_interval: this.state.password_change_interval,
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
        if (!selected && receiver !== null)
          deleting.push(this.props.dispatch(teamAppDeleteReceiver({
            team_id:this.props.team_id,
            shared_app_id: receiver.shared_app_id,
            team_user_id: item.id,
            app_id: app.id})));
        if (receiver === null && selected)
          sharing.push(this.props.dispatch(teamShareSingleApp({
            team_id: this.props.team_id,
            app_id: app.id,
            team_user_id: item.id,
            can_see_information: item.can_see_information})));
        if (selected && receiver !== null && item.can_see_information !== receiver.can_see_information)
          edit.push(this.props.dispatch(teamEditSingleAppReceiver({
            team_id: this.props.team_id,
            shared_app_id: receiver.shared_app_id,
            can_see_information: item.can_see_information,
            app_id: app.id})));
      });
      const calls = deleting.concat(sharing, edit);
      Promise.all(calls.map(reflect)).then(response => {
        this.setEdit(false);
      });
    }).catch(err => {
      console.log(err);
    });
  };
  setupUsers = () => {
    const channel = selectItemFromListById(this.props.channels, this.props.app.origin.id);
    let selected_users = [];
    const users = channel.userIds.map(item => {
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
      return a.username.localeCompare(b.username);
    }).map(item => {
      const receiver = getReceiverInList(this.props.app.receivers, item.id);
      const can_see_information = receiver !== null ? receiver.can_see_information : false;
      if (receiver !== null)
        selected_users.push(item.id);
      return {
        ...item,
        can_see_information: can_see_information
      }
    });
    this.setState({users: users, selected_users:selected_users});
  };
  setEdit = (state) => {
    if (state){
      const app = this.props.app;
      let credentials = transformWebsiteInfoIntoListAndSetValues(app.website.information, app.account_information);
      this.setupUsers();
      this.setState({credentials:credentials, password_change_interval: app.password_change_interval, description: app.description});
    }
    this.setState({edit: state, loading: false});
  };
  selfJoinApp = () => {
    this.props.dispatch(teamShareSingleApp({
      team_id : this.props.team_id,
      app_id: this.props.app.id,
      team_user_id: this.props.me.id,
      can_see_information:true
    })).then(() => {
      this.props.dispatch(modalActions.showPinTeamAppToDashboardModal(true, this.props.app));
    });
  };
  acceptRequest = (state) => {
    const app = this.props.app;
    const me = this.props.me;
    const meReceiver = findMeInReceivers(app.receivers, me.id);
    if (state) {
      this.props.dispatch(teamAcceptSharedApp({
        team_id: this.props.team_id,
        app_id: app.id,
        shared_app_id: meReceiver.shared_app_id
      })).then(() => {
        this.props.dispatch(modalActions.showPinTeamAppToDashboardModal(true, app));
      });
    }
    else
      this.props.dispatch(teamAppDeleteReceiver({
        team_id: this.props.team_id,
        app_id: app.id,
        shared_app_id: meReceiver.shared_app_id,
        team_user_id: meReceiver.team_user_id
      }));
  };
  render(){
    const app = this.props.app;
    const me = this.props.me;
    const room_manager_name = selectItemFromListById(this.props.users, selectItemFromListById(this.props.channels, app.origin.id).room_manager_id).username;
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
          {meReceiver !== null && !meReceiver.accepted &&
          <AcceptRefuseAppHeader onAccept={this.acceptRequest.bind(null, true)} onRefuse={this.acceptRequest.bind(null, false)}/>}
          <Segment>
            <Header as="h4">
              {website.website_name}
              {meReceiver !== null && meReceiver.accepted &&
              <PinAppButton is_pinned={meReceiver.profile_id !== -1} onClick={e => {this.props.dispatch(modalActions.showPinTeamAppToDashboardModal(true, app))}}/>}
              {app.sharing_requests.length > 0 && isAdmin(me.role) &&
              <SharingRequestButton onClick={e => {this.props.dispatch(modalActions.showTeamManageAppRequestModal(true, app))}}/>}
            </Header>
            {meReceiver !== null && !meReceiver.accepted &&
            <div class="overlay"/>}
            {!this.state.edit &&
            <TeamSimpleAppButtonSet app={app}
                                    me={me}
                                    selfJoin={this.selfJoinApp}
                                    requestApp={e => {this.props.dispatch(askJoinTeamApp(app.id))}}
                                    dispatch={this.props.dispatch} editMode={this.setEdit.bind(null, true)}/>}
            <div class="display_flex">
              <div class="logo_column">
                <div class="logo">
                  <img src={website.logo}/>
                </div>
              </div>
              <div class="main_column">
                <div class="credentials">
                  {credentials}
                  {(meReceiver !== null && meReceiver.can_see_information) &&
                  <CopyPasswordButton team_id={this.props.team_id} shared_app_id={meReceiver.shared_app_id}/>}
                  <div class="display-inline-flex">
                    {!this.state.edit ?
                        <PasswordChangeHolder value={app.password_change_interval}/> :
                        <PasswordChangeDropdown value={this.state.password_change_interval} onChange={this.handleInput}/>}
                    {((!this.state.edit && app.password_change_interval !== 0) || (this.state.edit && this.state.password_change_interval !== 0)) &&
                    <PasswordChangeManagerLabel username={room_manager_name}/>}
                  </div>
                </div>
                <div>
                  {!this.state.edit ?
                      <ReceiversLabelGroup receivers={userReceiversMap}/> :
                      <Dropdown
                          class="mini"
                          search={true}
                          fluid
                          name="selected_users"
                          options={this.state.users}
                          onChange={this.handleInput}
                          value={this.state.selected_users}
                          selection={true}
                          renderLabel={renderSimpleAppUserLabel}
                          multiple
                          placeholder="Tag your team members here..."/>}
                </div>
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
                </div>
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