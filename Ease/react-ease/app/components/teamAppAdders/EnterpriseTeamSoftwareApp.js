import React, {Component} from "react";
import classnames from "classnames";
import {Button, Container, Dropdown, Header, Icon, Input, Label, Popup, Segment} from 'semantic-ui-react';
import {
  EmptyCredentialsEnterpriseAppIndicator,
  PasswordChangeDropdownEnterprise,
  PasswordChangeHolderEnterprise,
  setUserDropdownText,
  SharingRequestButton,
  TeamAppActionButton
} from "./common";
import * as modalActions from "../../actions/teamModalActions";
import {showUpgradeTeamPlanModal} from "../../actions/teamModalActions";
import {
  teamEditSoftwareEnterpriseCard,
  teamEditEnterpriseCardReceiver,
  teamShareEnterpriseCard,
  removeTeamCardReceiver
} from "../../actions/appsActions";
import {
  copyTextToClipboard,
  credentialIconType,
  handleSemanticInput,
  reflect,
  transformCredentialsListIntoObject,
  transformWebsiteInfoIntoList,
  transformWebsiteInfoIntoListAndSetValues,
  needPasswordUpdate
} from "../../utils/utils";
import {getReceiverInList, isAdmin, selectItemFromListById} from "../../utils/helperFunctions";
import {reduxActionBinder} from "../../actions/index";
import {connect} from "react-redux";
import {addNotification} from "../../actions/notificationBoxActions";
import * as api from "../../utils/api";

const TeamEnterpriseAppButtonSet = ({app, me, dispatch, editMode, selfJoin, requestApp}) => {
  const meReceiver = app.receivers.find(receiver => (receiver.team_user_id === me.id));
  const asked = !!app.requests.find(request => (request.team_user_id === me.id));

  return (
      <div class="team_app_actions_holder">
        {!meReceiver &&
        <TeamAppActionButton text={isAdmin(me.role) ? 'Join App' : asked ? 'Request Sent' : 'Ask to join'}
                             onClick={isAdmin(me.role) ? selfJoin : asked ? null : requestApp}
                             icon="pointing up"
                             disabled={asked}/>}
        {(isAdmin(me.role) || !!meReceiver) &&
        <TeamAppActionButton text='Edit App'
                             icon='pencil'
                             onClick={editMode}/>}
        {isAdmin(me.role) &&
        <TeamAppActionButton text='Delete App' icon='trash outline' onClick={e => {dispatch(modalActions.showTeamDeleteAppModal({active: true, app_id: app.id}))}}/>}
      </div>
  )
};

const EnterpriseAppEditReceiverLabel = ({receiver, reminder_interval, onDelete}) => {
  const up_to_date =  !!receiver.receiver ? !needPasswordUpdate(receiver.receiver.last_update_date, reminder_interval) : true;
  return (
      <Popup size="mini"
             position="bottom center"
             inverted
             hideOnScroll={true}
             trigger={
               <Label class={classnames("receiver-label", (!!receiver.receiver && !receiver.empty) ? 'accepted': null)}>
                 <span>{receiver.user.username}</span>
                 {/*<Icon name="mobile" style={{marginRight: 0}}/>*/}
                 {!!reminder_interval &&
                 <Icon name="refresh" class="mrgn0" color={up_to_date ? null : 'red'}/>}
                 <Icon name="delete"
                       link
                       onClick={onDelete.bind(null, receiver.user.id)}/>
               </Label>
             }
             content={
               <div>
                 This person can access the account on desktop and mobile.
                 {/*<span>Mobile access: on</span>
                 <br/>
                 <span>Password copy: on</span>
                 <br/>
                 {!!receiver.receiver && !!reminder_interval && up_to_date &&
                 <span>Password is up to date</span>}*/}
                 {!!receiver.receiver && !!reminder_interval && !up_to_date &&
                 <span>Password <span style={{ textDecorationLine: 'underline' }}>is not up to date</span></span>}
               </div>}/>
  )
};

const EnterpriseAppReceiverLabel = ({receiver, reminder_interval}) => {
  const up_to_date =  !needPasswordUpdate(receiver.receiver.last_update_date, reminder_interval);
  return (
      <Popup size="mini"
             position="bottom center"
             inverted
             hideOnScroll={true}
             trigger={
               <Label class={classnames("receiver-label", !receiver.receiver.empty ? 'accepted': null)}>
                 <span>{receiver.user.username}</span>
                 {!!reminder_interval &&
                 <Icon name="refresh" class="mrgn0" color={up_to_date ? null : 'red'}/>}
                 {/*<Icon name="mobile"/>*/}
               </Label>
             }
             content={
               <div>
                 This person can access the account on desktop and mobile.
                 {/*<span>Mobile access: on</span>
                 <br/>
                 <span>Password copy: on</span>
                 <br/>
                 {up_to_date &&
                 <span>Password is up to date</span>}*/}
                 {!up_to_date &&
                 <span>Password <span style={{ textDecorationLine: 'underline' }}>is not up to date</span></span>}
               </div>}/>
  )
};

class CopyPasswordButton extends Component {
  constructor(props){
    super(props);
    this.state = {
      state: 0,
      open: false,
      pwd: ''
    }
  }
  copyPassword = () => {
    copyTextToClipboard(this.state.pwd);
    this.setState({state: 3, open: true});
    setTimeout(() => {
      this.setState({state: 0, open: false});
    }, 2000);
  };
  fetchPassword = () => {
    this.setState({state: 1, open: true});
    api.dashboard.getAppPassword({
      app_id: this.props.app_id
    }).then(response => {
      this.setState({pwd: response.password, state: 2, open: true});
    }).catch(err => {
      this.setState({state: 4, open: true});
      setTimeout(() => {
        this.setState({state: 0, open: false});
      }, 2000);
    });
  };
  render(){
    const content = <div>
      {this.state.state === 0 &&
      'Copy password'}
      {this.state.state === 1 &&
      <div><Icon name="asterisk" loading/> decrypting password locally</div>}
      {this.state.state === 2 &&
      <Button size="mini" positive onClick={this.copyPassword} content={'Click to copy'}/>}
      {this.state.state === 3 &&
      'Copied!'}
      {this.state.state === 4 &&
      'Error'}
    </div>;
    return (
        <Popup size="mini"
               position="top center"
               open={this.state.state > 0 ? true : undefined}
               inverted
               hoverable
               trigger={
                 <Icon name="copy" class="copy_pwd_button" link onClick={this.fetchPassword}/>
               }
               content={content}/>
    )
  }
};

const StaticReceivers = ({receivers, me, expanded, password_reminder_interval, dispatch}) => {
  const meAdmin = isAdmin(me.role);
  return (
      <div class="receivers">
        {receivers.map((receiver, idx) => {
          if (idx > 2 && !expanded)
            return null;
          return (
              <div class="receiver align_items_center" key={receiver.user.id}>
                <EnterpriseAppReceiverLabel
                    receiver={receiver}
                    reminder_interval={password_reminder_interval}/>
                {receiver.receiver.empty ?
                    <EmptyCredentialsEnterpriseAppIndicator
                        receiver={receiver.receiver}
                        me={me}
                        dispatch={dispatch}
                        team_user={receiver.user}
                        meAdmin={meAdmin}/> :
                    receiver.credentials.map(item => {
                      return <Input size="mini"
                                    key={item.name}
                                    class="team-app-input"
                                    readOnly={true}
                                    name={item.name}
                                    label={<Label><Icon name={credentialIconType[item.name] ? credentialIconType[item.name] : 'wait'}/></Label>}
                                    labelPosition="left"
                                    placeholder={item.placeholder}
                                    value={(item.name === 'password' && !receiver.receiver.empty) ? 'abcdabcd' : item.value}
                                    type={item.information_type}/>;                })}
                {receiver.user.id === me.id && !receiver.receiver.empty &&
                <CopyPasswordButton app_id={receiver.receiver.app_id}/>}
              </div>
          );
        })}
      </div>
  )
};

const TeamAppCredentialInput = ({item, onChange, receiver, myId}) => {
  const isRequired = receiver.user.id === myId && item.name !== 'password';
  const label = <Label><Icon name={credentialIconType[item.name] ? credentialIconType[item.name] : 'wait'}/></Label>;
  let placeholder = item.placeholder;
  if (item.name === 'password' && !receiver.empty)
    placeholder = '••••••••';
  if (receiver.user.id !== myId && receiver.empty)
    placeholder = `${placeholder} (Optional)`;

  return <Input size="mini"
                class="team-app-input"
                name={item.name}
                required={isRequired}
                onChange={(e, data) => {onChange(receiver.user.id, data)}}
                label={label}
                labelPosition="left"
                placeholder={placeholder}
                value={item.value}
                type={item.information_type}/>;
};

const ExtendedReceiverCredentialsInput = ({receiver, onChange, onDelete, myId, password_reminder_interval}) => {
  return (
      <div class={classnames('receiver', receiver.empty ? 'empty':null)}>
        <EnterpriseAppEditReceiverLabel
            receiver={receiver}
            reminder_interval={password_reminder_interval}
            onDelete={onDelete}/>
        {
          receiver.credentials.map(item => {
            return <TeamAppCredentialInput
                empty={receiver.empty}
                myId={myId}
                receiver={receiver}
                key={item.priority}
                onChange={onChange}
                item={item}/>
          })
        }
      </div>
  )
};

const Receivers = ({receivers, onChange, onDelete, myId, password_reminder_interval}) => {
  return (
      <div class="receivers">
        {receivers.map(item => {
          return <ExtendedReceiverCredentialsInput key={item.user.id}
                                                   password_reminder_interval={password_reminder_interval}
                                                   myId={myId}
                                                   receiver={item}
                                                   onChange={onChange}
                                                   onDelete={onDelete}/>;
        })}
      </div>
  )
};

const ButtonShowMore = ({number_of_users, show_more, showMore}) => {
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
        {number_of_users}&nbsp;users
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

@connect(store => ({
  teams: store.teams
}), reduxActionBinder)
class EnterpriseTeamSoftwareApp extends Component {
  constructor(props){
    super(props);
    this.state = {
      loading: false,
      edit: false,
      name: '',
      password_reminder_interval: 0,
      description: '',
      users: [],
      selected_users: [],
      show_more: false
    }
  }
  handleInput = handleSemanticInput.bind(this);
  changeFillInSwitch = (e, {checked}) => {
    if (this.props.plan_id === 0 && !checked){
      this.props.dispatch(showUpgradeTeamPlanModal(true, 2));
      return;
    }
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
      if (user.user.id === id){
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
  selfJoinApp = () => {
    this.props.dispatch(modalActions.showTeamJoinEnterpriseAppModal({
      active: true,
      team_card_id: this.props.app.id
    }));
  };
  requestApp = () => {
    this.props.dispatch(modalActions.showTeamAskJoinEnterpriseAppModal({
      active: true,
      team_card_id: this.props.app.id
    }));
  };
  setupUsers = () =>  {
    const channel = selectItemFromListById(this.props.channels, this.props.app.channel_id);
    const app = this.props.app;
    let selected_users = [];
    const users = channel.team_user_ids.map(item => {
      const user = this.props.users.find(user => (user.id === item));
      return {
        key: item,
        text: setUserDropdownText(user),
        value: item,
        username: user.username,
        user: user
      }
    }).sort((a, b) => {
      if (a.user.id === this.props.me.id)
        return -1000;
      if (b.user.id === this.props.me.id)
        return 1000;
      return a.username.localeCompare(b.username);
    }).map(item => {
      const receiver = this.props.app.receivers.find(receiver => (receiver.team_user_id === item.user.id));
      let credentials;
      if (!!receiver) {
        credentials = transformWebsiteInfoIntoListAndSetValues(app.software.connection_information, receiver.account_information);
        selected_users.push(item.user.id);
      } else
        credentials = transformWebsiteInfoIntoList(app.software.connection_information);
      return {
        ...item,
        credentials: credentials,
        empty: !!receiver && receiver.empty,
        receiver: receiver
      }
    });
    this.setState({users: users, selected_users: selected_users});
  };
  setEdit = (state) => {
    if (state){
      if (!isAdmin(this.props.me.role)){
        this.props.dispatch(modalActions.showTeamEditEnterpriseAppModal({
          active: true,
          team_card_id: this.props.app.id
        }));
        return;
      }
      const app = this.props.app;
      this.setupUsers();
      this.setState({password_reminder_interval: app.password_reminder_interval, description: app.description, name: app.name});
    }
    this.setState({edit: state, loading: false, show_more: false});
  };
  modify = (e) => {
    e.preventDefault();
    this.setState({loading: true});
    this.props.dispatch(teamEditSoftwareEnterpriseCard({
      team_card_id: this.props.app.id,
      name: this.state.name,
      description: this.state.description,
      password_reminder_interval: this.state.password_reminder_interval
    })).then(response => {
      const app = response;
      const receivers = app.receivers;
      let deleting = [];
      let edit = [];
      let sharing = [];
      this.state.users.map(item => {
        const selected = this.state.selected_users.indexOf(item.user.id) !== -1;
        const receiver = receivers.find(receiver => (receiver.team_user_id === item.user.id));
        if (!selected && !!receiver)
          deleting.push(this.props.dispatch(removeTeamCardReceiver({
            team_id:app.team_id,
            team_card_id: app.id,
            team_card_receiver_id: receiver.id})));
        if (!receiver && selected)
          sharing.push(this.props.dispatch(teamShareEnterpriseCard({
            team_id: app.team_id,
            team_card_id: app.id,
            team_user_id: item.user.id,
            account_information: transformCredentialsListIntoObject(item.credentials)})));
        if (selected && !!receiver && isDifferentCredentials(transformCredentialsListIntoObject(item.credentials), receiver.account_information))
          edit.push(this.props.dispatch(teamEditEnterpriseCardReceiver({
            team_id: app.team_id,
            team_card_id: app.id,
            team_card_receiver_id: receiver.id,
            account_information: transformCredentialsListIntoObject(item.credentials)})));
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
  getUsers = () => {
    const app = this.props.app;
    if (this.state.edit){
      return this.state.selected_users.map(item => {
        return this.state.users.find(user => (user.user.id === item));
      });
    }
    return app.receivers.map(receiver => {
      const user = this.props.users.find(user => (user.id === receiver.team_user_id));
      return {
        user: user,
        receiver: receiver,
        credentials: transformWebsiteInfoIntoListAndSetValues(app.software.connection_information, receiver.account_information),
      }
    }).sort((a,b) => {
      if (a.user.id === this.props.me.id)
        return -1000;
      if (b.user.id === this.props.me.id)
        return 1000;
      return a.user.username.localeCompare(b.user.username);
    });
  };
  render(){
    const app = this.props.app;
    const me = this.props.me;
    const team = this.props.teams[app.team_id];
    const meReceiver = getReceiverInList(app.receivers, me.id);
    const website = app.software;
    const users = this.getUsers();
    const room_manager = this.props.teams[this.props.team_id].team_users[selectItemFromListById(this.props.channels, app.channel_id).room_manager_id];
    return (
        <Container fluid
                   id={`app_${app.id}`}
                   class="team-app mrgn0 enterprise-team-app"
                   as="form"
                   onSubmit={this.modify}>
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
                  <div class="display-inline-flex align_items_center">
                    {!this.state.edit ?
                        <PasswordChangeHolderEnterprise
                            team={team}
                            value={app.password_reminder_interval}
                            roomManager={room_manager.username}/> :
                        <PasswordChangeDropdownEnterprise
                            team={team}
                            dispatch={this.props.dispatch}
                            value={this.state.password_reminder_interval}
                            onChange={this.handleInput}
                            roomManager={room_manager.username}/>}
                  </div>
                </div>
                {!this.state.edit &&
                <StaticReceivers receivers={users}
                                 dispatch={this.props.dispatch}
                                 password_reminder_interval={app.password_reminder_interval}
                                 expanded={this.state.show_more}
                                 me={me}/>}
                <div>
                  {!this.state.edit && users.length > 3 &&
                  <ButtonShowMore
                      number_of_users={users.length - 3}
                      show_more={this.state.show_more}
                      showMore={this.setShowMore}/>}
                </div>
                {this.state.edit &&
                <Receivers receivers={users}
                           password_reminder_interval={this.state.password_reminder_interval}
                           onChange={this.handleReceiverInput}
                           onDelete={this.deleteReceiver}
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
                    noResultsMessage='No more results found'
                    placeholder="Tag your team members here..."/>}
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

module.exports = EnterpriseTeamSoftwareApp;