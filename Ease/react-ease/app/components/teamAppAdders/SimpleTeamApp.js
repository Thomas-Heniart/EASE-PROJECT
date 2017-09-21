import React, {Component} from "react";
import classnames from "classnames";
import { Header, Popup, Grid, Label,List, Search,SearchResult, Container, Divider, Icon, Transition, TextArea, Segment, Checkbox, Form, Input, Select, Dropdown, Button, Message } from 'semantic-ui-react';
import {dashboardAndTeamAppSearch, fetchWebsiteInfo, getDashboardApp} from "../../utils/api";
import {setUserDropdownText,
  PasswordChangeHolder,
  renderSimpleAppUserLabel,
  PasswordChangeDropdown,
  PasswordChangeManagerLabel} from "./common";
import * as modalActions from "../../actions/teamModalActions";
import {teamEditSingleApp, teamShareSingleApp, teamAppDeleteReceiver, teamEditSingleAppReceiver} from "../../actions/appsActions";
import {handleSemanticInput,
  transformWebsiteInfoIntoList,
  transformCredentialsListIntoObject,
  transformWebsiteInfoIntoListAndSetValues,
  passwordChangeOptions,
  credentialIconType,
  passwordChangeValues,
  reflect
} from "../../utils/utils";
import {selectItemFromListById,
  findMeInReceivers,
  getReceiverInList} from "../../utils/helperFunctions";
import {requestWebsite} from "../../actions/teamModalActions";
import {teamCreateSingleApp} from "../../actions/appsActions";
import {closeAppAddUI} from "../../actions/teamAppsAddUIActions";
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
                placeholder={item.placeholder}
                value={item.value}
                type={item.type}/>;
};

const TeamSimpleAppButtonSet = ({app, me, dispatch, editMode}) => {
  const meReceiver = findMeInReceivers(app.receivers, me.id);
  const meSender = app.sender_id === me.id;
  return (
      <div class="team_app_actions_holder">
        {app.sharing_requests.length > 0 && (me.id === app.sender_id || isAdmin(me.role)) &&
        <button type="button" class="button-unstyle team_app_requests"
                data-tip="User(s) would like to access this app"
                onClick={e => {dispatch(modalActions.showTeamManageAppRequestModal(true, app))}}>
          <i class="fa fa-user"/>
        </button>}
        {meReceiver !== null && meReceiver.accepted &&
        <button class="button-unstyle team_app_pin"
                type="button"
                data-tip="Pin App in your Personal space"
                onClick={e => {dispatch(modalActions.showPinTeamAppToDashboardModal(true, app))}}>
          <i class="fa fa-thumb-tack"/>
        </button>}
        {meReceiver !== null && meReceiver.accepted &&
        <button class="button-unstyle team_app_leave"
                data-tip="Leave App"
                type="button"
                onClick={e => {dispatch(modalActions.showTeamLeaveAppModal(true, app, me.id))}}>
          <i class="fa fa-sign-out"/>
        </button>}
        {(meSender || me.role > 1) &&
        <button class="button-unstyle team_app_edit"
                data-tip="Edit App"
                type="button"
                onClick={editMode}>
          <i class="fa fa-pencil"/>
        </button>}
        {(meSender || me.role > 1) &&
        <button class="button-unstyle team_app_delete"
                data-tip="Delete App"
                type="button"
                onClick={e => {dispatch(modalActions.showTeamDeleteAppModal(true, app))}}>
          <i class="fa fa-trash"/>
        </button>}
      </div>
  )
};

const ReceiversLabelGroup = ({receivers, users}) => {
  return (
      <Label.Group>
        {receivers.map(item => {
          const user = selectItemFromListById(users, item.team_user_id);
          return (
              <Label key={item.team_user_id} class={classnames("user-label static", item.accepted ? 'accepted' : null)}>
                {user.username}
                <Icon name={item.can_see_information ? 'unhide' : 'hide'}/>
              </Label>
          )
        })}
      </Label.Group>
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
            team_user_id: item.id,
            app_id: app.id})));
        if (receiver === null && selected)
          sharing.push(this.props.dispatch(teamShareSingleApp({
            team_id: this.props.team_id,
            app_id: app.id,
            team_user_id: item.id,
            can_see_password: item.can_see_password})));
        if (selected && receiver !== null && item.can_see_information !== receiver.can_see_information)
          edit.push(this.props.dispatch(teamEditSingleAppReceiver({
            team_id: this.props.team_id,
            shared_app_id: receiver.shared_app_id,
            can_see_information: item.can_see_information,
            app_id: app.id})));
        const calls = deleting.concat(sharing, edit);
        Promise.all(reflect(calls)).then(response => {
          this.setEdit(false);
        });
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
      const receiver = getReceiverInList(this.props.app.receivers, user.id);
      const can_see_information = receiver !== null ? receiver.can_see_information : false;
      if (receiver !== null)
        selected_users.push(user.id);
      return {
        key: item,
        text: setUserDropdownText(user),
        value: item,
        id: item,
        username: user.username,
        can_see_information: can_see_information,
        toggleCanSeeInformation: this.toggleCanSeeInformation.bind(null, item)
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
    this.setState({edit: state});
  };
  render(){
    const app = this.props.app;
    const me = this.props.me;
    const website = app.website;
    const users = this.props.users;
    const credentials = !this.state.edit ?
        transformWebsiteInfoIntoListAndSetValues(website.information, app.account_information).map(item => {
          return <TeamAppCredentialInput key={item.priority}
                                         readOnly={true}
                                         onChange={this.handleCredentialInput}
                                         item={item}/>
        }) : this.state.credentials.map(item => {
          return <TeamAppCredentialInput key={item.priority}
                                         onChange={this.handleCredentialInput}
                                         item={item}/>
        });
    return (
        <Container fluid id="simple_team_app_add" class="team-app mrgn0" as="form" onSubmit={this.modify}>
          <div>
            <Segment>
              <Header as="h4">
                {website.website_name}
              </Header>
              {!this.state.edit &&
              <TeamSimpleAppButtonSet app={app} me={me} dispatch={this.props.dispatch} editMode={this.setEdit.bind(null, true)}/>}
              <div class="display_flex">
                <div class="logo_column">
                  <div class="logo">
                    <img src={website.logo}/>
                  </div>
                </div>
                <div class="main_column">
                  <div class="credentials">
                    {credentials}
                    <div class="display-inline-flex">
                      {!this.state.edit ?
                          <PasswordChangeHolder value={app.password_change_interval}/> :
                          <PasswordChangeDropdown value={this.state.password_change_interval} onChange={this.handleInput}/>}
                      {((!this.state.edit && app.password_change_interval !== 0) || (this.state.edit && this.state.password_change_interval !== 0)) &&
                      <PasswordChangeManagerLabel username={'johny'}/>}
                    </div>
                  </div>
                  <div>
                    {!this.state.edit ?
                        <ReceiversLabelGroup receivers={app.receivers} users={users}/> :
                        <Dropdown
                            class = "mini"
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
                           placeholder="What is this about? Any comment?"
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
          </div>
        </Container>
    )
  }
}

module.exports = SimpleTeamApp;