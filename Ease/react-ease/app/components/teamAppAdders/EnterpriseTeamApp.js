import React, {Component} from "react";
import classnames from "classnames";
import api from '../../utils/api';
import { Header, Popup, Grid, Label,List, Search,SearchResult, Container, Divider, Icon, Transition, TextArea, Segment, Checkbox, Form, Input, Select, Dropdown, Button, Message } from 'semantic-ui-react';
import {dashboardAndTeamAppSearch, fetchWebsiteInfo, getDashboardApp} from "../../utils/api";
import {setUserDropdownText,
  PasswordChangeHolder,
  renderSimpleAppUserLabel,
  PasswordChangeDropdown,
  PasswordChangeManagerLabel,
  PinAppButton,
  TeamAppActionButton,
  CopyPasswordButton,
  ExtendFillSwitch,
  SharingRequestButton} from "./common";
import * as modalActions from "../../actions/teamModalActions";
import {teamEditSingleApp, teamShareSingleApp, teamAppDeleteReceiver, teamEditSingleAppReceiver, askJoinTeamApp} from "../../actions/appsActions";
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
  getReceiverInList,
  isAdmin} from "../../utils/helperFunctions";
import {teamShareEnterpriseApp, teamEditEnterpriseAppReceiver, teamEditEnterpriseApp} from "../../actions/appsActions";
import {connect} from "react-redux";

const TeamEnterpriseAppButtonSet = ({app, me, dispatch, editMode, selfJoin, requestApp}) => {
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
        {(isAdmin(me.role) || meReceiver !== null) &&
        <TeamAppActionButton text='Edit App'
                             icon='pencil'
                             onClick={editMode}/>}
        {isAdmin(me.role) &&
        <TeamAppActionButton text='Delete App' icon='trash outline' onClick={e => {dispatch(modalActions.showTeamDeleteAppModal(true, app))}}/>}
      </div>
  )
};

const TeamAppCredentialInput = ({item, onChange, receiver_id, readOnly}) => {
  return <Input size="mini"
                class="team-app-input"
                readOnly={readOnly}
                name={item.name}
                onChange={onChange !== undefined ?(e, data) => {onChange(receiver_id, data)} : null}
                label={<Label><Icon name={credentialIconType[item.name]}/></Label>}
                labelPosition="left"
                placeholder={item.placeholder}
                value={item.value}
                type={item.type}/>;
};

const ReadOnlyTeamAppCredentialInput = ({item, onChange, receiver_id, readOnly, pwd_filled}) => {
  return <Input size="mini"
                class="team-app-input"
                readOnly={readOnly}
                name={item.name}
                label={<Label><Icon name={credentialIconType[item.name]}/></Label>}
                labelPosition="left"
                placeholder={item.placeholder}
                value={item.name === 'password' && pwd_filled ? 'abcdabcd' : item.value}
                type={item.type}/>;
};

const ReceiverCredentialsInput = ({receiver, onChange, onDelete}) => {
  return (
      <div class="receiver">
        <Label class="receiver-label" color="blue">
          <span>{receiver.username}</span>
          <Icon name="delete" link onClick={onDelete.bind(null, receiver.id)}/>
        </Label>
      </div>
  )
};

const ExtendedReceiverCredentialsInput = ({receiver, onChange, onDelete, readOnly}) => {
  return (
      <div class="receiver">
        <Label class="receiver-label" color="blue"><span>{receiver.username}</span> <Icon name="delete" link onClick={onDelete.bind(null, receiver.id)}/></Label>
        {
          receiver.credentials.map(item => {
            return <TeamAppCredentialInput readOnly={readOnly} receiver_id={receiver.id} key={item.priority} onChange={onChange} item={item}/>
          })
        }
      </div>
  )
};

const EnterpriseAppReceiverLabel = ({username, up_to_date, accepted}) => {
  return (
      <Popup size="mini"
             position="bottom center"
             inverted
             flowing
             hideOnScroll={true}
             trigger={
               <Label class={classnames('receiver-label', accepted ? 'accepted': null)}>
                 <span>{username}</span>
                 <Icon name="refresh"/>
               </Label>
             }
             header={<h5 class="mrgn0 text-center">User informations</h5>}
             content={
               <div>
                 {up_to_date &&
                 <span><Icon name='refresh'/> Password is up to date</span>}
                 {!up_to_date &&
                 <span><Icon name='refresh' color="red"/> Password is not up to date</span>}
                 <br/>
                 {accepted &&
                 <span><Icon name='circle' style={{color: '#949EB7'}}/> User accepted the app</span>}
                 {!accepted &&
                 <span><Icon name='circle' style={{color: '#D2DAE4'}}/> User didn't accept the app</span>}
               </div>}/>
  )
};

const SimpleCredentialsInput = ({receiver, me, team_id}) => {
  return (
      <div class="receiver align_items_center">
        <EnterpriseAppReceiverLabel username={receiver.username} up_to_date={!receiver.password_must_be_updated} accepted={receiver.accepted}/>
        {receiver.credentials.map(item => {
          return <ReadOnlyTeamAppCredentialInput pwd_filled={receiver.password_filled} readOnly={true} receiver_id={receiver.id} key={item.priority} onChange={null} item={item}/>
        })}
        {(isAdmin(me.role) || receiver.team_user_id === me.id) &&
        <CopyPasswordButton team_id={team_id} shared_app_id={receiver.shared_app_id}/>}
      </div>
  )
};

const StaticReceivers = ({receivers, me, team_id, expanded}) => {
  return (
      <div class="receivers">
        {receivers.map((item, idx) => {
          if (idx > 3 && !expanded)
            return null;
          return <SimpleCredentialsInput key={item.id} receiver={item} me={me} team_id={team_id}/>
        })}
      </div>
  )
};

const Receivers = ({receivers, onChange, onDelete, extended, myId}) => {
  return (
      <div class="receivers">
        {receivers.map(item => {
          if (extended || item.id === myId)
            return <ExtendedReceiverCredentialsInput key={item.id}
                                                     receiver={item}
                                                     onChange={onChange}
                                                     onDelete={onDelete}/>;
          return <ReceiverCredentialsInput key={item.id}
                                           receiver={item}
                                           onChange={onChange}
                                           onDelete={onDelete}/>
        })}
      </div>
  )
};

const AcceptRefuseAppHeader = ({onAccept, onRefuse}) => {
  return (
      <span style={{lineHeight: '1.7'}}>
        You was tagged in an Enterprise App,
        &nbsp;
        <button class="button-unstyle inline-text-button primary" type="button" onClick={onAccept}>Accept</button>
        &nbsp;or&nbsp;
        <button class="button-unstyle inline-text-button primary" type="button" onClick={onRefuse}>Refuse</button>
        &nbsp;it?
      </span>
  )
};

const ButtonShowMore = ({show_more, showMore}) => {
  if (show_more)
    return (
        <Button size="mini" type="button" class="fw-normal" onClick={showMore.bind(null, false)}>
          <Icon name="remove user"/>
          Show less
        </Button>
    );
  return (
      <Button size="mini" type="button" class="fw-normal" onClick={showMore.bind(null, true)}>
        <Icon name="add user"/>
        Show more
      </Button>
  )
};

const isDifferentCredentials = (first, second) => {
  let different = false;

  Object.keys(first).map(item => {
    if (first[item] !== second[item])
      different = true;
  });
  return different;
};

class EnterpriseTeamApp extends Component {
  constructor(props){
    super(props);
    this.state = {
      loading: false,
      edit: false,
      password_change_interval: 0,
      description: '',
      users: [],
      selected_users: [],
      fill_in_switch: this.props.app.fill_in_switch,
      expanded: false,
      show_more: false
    }
  }
  handleInput = handleSemanticInput.bind(this);
  changeFillInSwitch = (e, {checked}) => {
    if (this.props.app.fill_in_switch && checked)
      return;
    this.setState({fill_in_switch: !checked});
  };
  setShowMore = (state) => {
    this.setState({show_more: state});
  };
  deleteReceiver = (id) => {
    const selected_users = this.state.selected_users.filter(item => (item !== id));
    this.setState({selected_users: selected_users});
  };
  handleReceiverInput = (id, {name, value}) => {
    const users = this.state.users.map(user => {
      if (user.id === id){
        user.credentials.map(item => {
          if (item.name === name)
            item.value = value;
          return item;
        })
      }
      return user;
    });
    this.setState({users: users});
  };
  acceptRequest = (state) => {
    const meReceiver = findMeInReceivers(this.props.app.receivers, this.props.me.id);
    if (state) {
      this.props.dispatch(modalActions.showTeamAcceptMultiAppModal(true, this.props.me, this.props.app));
    } else {
      this.props.dispatch(teamAppDeleteReceiver({
        team_id: this.props.team_id,
        app_id: this.props.app.id,
        shared_app_id: meReceiver.shared_app_id,
        team_user_id: meReceiver.team_user_id
      }));
    }
  };
  selfJoinApp = () => {
    this.props.dispatch(modalActions.showTeamJoinMultiAppModal(true, this.props.me, this.props.app));
  };
  requestApp = () => {
    this.props.dispatch(modalActions.showTeamAskJoinMultiAppModal(true, this.props.me, this.props.app));
  };
  setupUsers = () =>  {
    const channel = selectItemFromListById(this.props.channels, this.props.app.origin.id);
    const app = this.props.app;
    let selected_users = [];

    const users = channel.userIds.map(item => {
      const user = selectItemFromListById(this.props.users, item);
      return {
        key: item,
        text: setUserDropdownText(user),
        value: item,
        id: item,
        username: user.username,
        user: user
      }
    }).sort((a, b) => {
      if (a.id === this.props.me.id)
        return -1000;
      return a.username.localeCompare(b.username);
    }).map(item => {
      const receiver = getReceiverInList(this.props.app.receivers, item.id);
      let credentials;
      if (receiver !== null) {
        credentials = transformWebsiteInfoIntoListAndSetValues(app.website.information, receiver.account_information);
        selected_users.push(item.id);
      } else
        credentials = transformWebsiteInfoIntoList(app.website.information);
      return {
        ...item,
        credentials: credentials
      }
    });
    this.setState({users: users, selected_users: selected_users});
  };
  setEdit = (state) => {
    if (state){
      if (!isAdmin(this.props.me.role)){
        this.props.dispatch(modalActions.showTeamEditEnterpriseAppModal(true, this.props.me, this.props.app));
        return;
      }
      const app = this.props.app;
      this.setupUsers();
      this.setState({password_change_interval: app.password_change_interval, description: app.description, fill_in_switch: app.fill_in_switch});
    }
    this.setState({edit: state, loading: false, show_more: false});
  };
  modify = (e) => {
    e.preventDefault();
    this.setState({loading: true});
    this.props.dispatch(teamEditEnterpriseApp({
      team_id: this.props.team_id,
      app_id: this.props.app.id,
      description: this.state.description,
      password_change_interval: this.state.password_change_interval,
      fill_in_switch:this.state.fill_in_switch
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
          sharing.push(this.props.dispatch(teamShareEnterpriseApp({
            team_id: this.props.team_id,
            app_id: app.id,
            team_user_id: item.id,
            account_information: transformCredentialsListIntoObject(item.credentials)})));
        if (selected && receiver !== null && isDifferentCredentials(transformCredentialsListIntoObject(item.credentials), receiver.account_information))
          edit.push(this.props.dispatch(teamEditEnterpriseAppReceiver({
            team_id: this.props.team_id,
            shared_app_id: receiver.shared_app_id,
            account_information: transformCredentialsListIntoObject(item.credentials),
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
  getUsers = () => {
    const app = this.props.app;
    if (this.state.edit){
      return this.state.selected_users.map(item => {
        return selectItemFromListById(this.state.users, item);
      });
    }
    return app.receivers.map(item => {
      const user = selectItemFromListById(this.props.users, item.team_user_id);
      return {
        password_must_be_updated: item.password_must_be_updated,
        accepted: item.accepted,
        id: item.team_user_id,
        password_filled: item.password_filled,
        shared_app_id: item.shared_app_id,
        credentials: transformWebsiteInfoIntoListAndSetValues(app.website.information, item.account_information),
        username: user.username
      }
    }).sort((a,b) => {
      if (a.id === this.props.me.id)
        return -1000;
      return a.username.localeCompare(b.username);
    });
  };
  render(){
    const app = this.props.app;
    const me = this.props.me;
    const meReceiver = getReceiverInList(app.receivers, me.id);
    const website = app.website;
    const users = this.getUsers();

    return (
        <Container fluid class="team-app mrgn0 enterprise-team-app" as="form" onSubmit={this.modify}>
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
            <TeamEnterpriseAppButtonSet app={app}
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
                  <div class="display-inline-flex">
                    {!this.state.edit ?
                        <PasswordChangeHolder value={app.password_change_interval}/> :
                        <PasswordChangeDropdown value={this.state.password_change_interval} onChange={this.handleInput}/>}
                    {this.state.edit &&
                    <ExtendFillSwitch value={this.state.fill_in_switch} onClick={this.changeFillInSwitch}/>}
                    {!this.state.edit && users.length > 4 &&
                    <ButtonShowMore show_more={this.state.show_more} showMore={this.setShowMore}/>}
                  </div>
                </div>
                {!this.state.edit &&
                <StaticReceivers receivers={users}
                                 extended={this.state.extended}
                                 expanded={this.state.show_more}
                                 me={me}
                                 team_id={this.props.team_id}/>}
                {this.state.edit &&
                <Receivers receivers={users}
                           onChange={this.handleReceiverInput}
                           onDelete={this.deleteReceiver}
                           extended={this.state.fill_in_switch}
                           myId={me.id}/>}
                {this.state.edit &&
                <Dropdown
                    class="mini users-dropdown"
                    search={true}
                    fluid
                    name="selected_users"
                    options={this.state.users}
                    onChange={this.handleInput}
                    value={this.state.selected_users}
                    selection={true}
                    multiple
                    placeholder="Tag your team members here..."/>}
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

module.exports = EnterpriseTeamApp;