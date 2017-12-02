import React, {Component} from "react";
import {teamAppSearch, fetchWebsiteInfo, getDashboardApp} from "../../utils/api";
import {handleSemanticInput,
  transformCredentialsListIntoObject,
  transformWebsiteInfoIntoList,
  credentialIconType} from "../../utils/utils";
import {selectUserFromListById, newSelectUserFromListById} from "../../utils/helperFunctions";
import {requestWebsite, showPinTeamAppToDashboardModal, showUpgradeTeamPlanModal} from "../../actions/teamModalActions";
import {teamCreateSingleApp, teamCreateEnterpriseCard} from "../../actions/appsActions";
import {closeAppAddUI} from "../../actions/teamAppsAddUIActions";
import {connect} from "react-redux";
import {ExtendFillSwitch, setUserDropdownText, renderSimpleAppUserLabel, PasswordChangeDropdown, PasswordChangeManagerLabel} from "./common";
import { Header, Popup, Grid, Label,List, Search,SearchResult, Container, Divider, Icon, Transition, TextArea, Segment, Checkbox, Form, Input, Select, Dropdown, Button, Message } from 'semantic-ui-react';
import {reduxActionBinder} from "../../actions/index";

const TeamAppCredentialInput = ({item, onChange, receiver_id, readOnly, isMe}) => {
  return <Input size="mini"
                class="team-app-input"
                readOnly={readOnly}
                name={item.name}
                required={isMe}
                onChange={(e, data) => {onChange(receiver_id, data)}}
                label={<Label><Icon name={credentialIconType[item.name]}/></Label>}
                labelPosition="left"
                placeholder={isMe ? item.placeholder : `${item.placeholder} (Optional)`}
                value={item.value}
                type={item.type}/>;
};

const ExtendedReceiverCredentialsInput = ({receiver, onChange, onDelete, readOnly, isMe}) => {
  return (
      <div class="receiver">
        <Label class="receiver-label"><span>{receiver.username}</span> <Icon name="delete" link onClick={onDelete.bind(null, receiver.id)}/></Label>
        {
          receiver.credentials.map(item => {
            return <TeamAppCredentialInput readOnly={readOnly} receiver_id={receiver.id} isMe={isMe} key={item.priority} onChange={onChange} item={item}/>
          })
        }
      </div>
  )
};

const Receivers = ({receivers, onChange, onDelete, myId}) => {
  return (
      <div class="receivers">
        {receivers.map(item => {
          // if (item.id === myId)
            return <ExtendedReceiverCredentialsInput key={item.id} isMe={item.id === myId} receiver={item} onChange={onChange} onDelete={onDelete}/>;
          // return <ReceiverCredentialsInput key={item.id} receiver={item} onChange={onChange} onDelete={onDelete}/>
        })}
      </div>
  )
};

@connect(store => ({
  team_id: store.teamCard.team_id,
  users: store.users.users,
  myId: store.team.myTeamUserId,
  plan_id: store.team.plan_id,
  teams: store.teams,
  card: store.teamCard
}), reduxActionBinder)
class EnterpriseTeamAppAdder extends Component {
  constructor(props){
    super(props);
    this.state = {
      loading: false,
      app: this.props.card.app,
      app_name: this.props.card.app.name,
      password_reminder_interval: 0,
      description: '',
      users: [],
      selected_users: [],
      fill_in_switch: false
    }
  }
  handleInput = handleSemanticInput.bind(this);
  onDeleteReceiver = (id) => {
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
  chooseAllUsers = () => {
    let selected = [];
    this.state.users.map(user => {
      if (selected.length) {
        selected.splice(selected.length + 1, 0, user.id);
      }
      else {
        selected.splice(0, 0, user.id);
      }
    });
    this.setState({ selected_users: selected });
  };
  setApp = (app) => {
    if (app.request){
      requestWebsite(this.props.dispatch).then(app => {
        this.setUsers(app);
        this.setState({app: app});
      }).catch(err => ({}));
      return;
    }
    fetchWebsiteInfo(app.id).then(app => {
      this.setUsers(app);
      this.setState({app: app});
    });
  };
  componentWillMount(){
    let users = this.props.item.team_user_ids.map(item => {
      const user = newSelectUserFromListById(this.props.teams[this.props.card.team_id].team_users, item);
      return {
        key: item,
        text: setUserDropdownText(user),
        value: item,
        id: item,
        credentials: transformWebsiteInfoIntoList(this.props.card.app.information),
        username: user.username
      }
    });
    this.setState({users: users});
  };
  componentDidMount(){
    this.chooseAllUsers();
  };
  setUsers = (app) => {
    let users = this.props.item.user_ids.map(item => {
      const user = newSelectUserFromListById(this.props.teams[this.props.card.team_id].team_users, item);
      return {
        key: item,
        text: setUserDropdownText(user),
        value: item,
        id: item,
        credentials: transformWebsiteInfoIntoList(app.information),
        username: user.username
      }
    });
    this.setState({users: users});
  };
  send = (e) => {
    e.preventDefault();
    this.setState({loading: true});
    const receivers = this.state.users
      .filter(item => (this.state.selected_users.indexOf(item.id) !== -1))
      .map(item => ({
        [item.id]: {account_information: transformCredentialsListIntoObject(item.credentials)}
      }));
    const newReceivers = receivers.reduce(function (result, item) {
      result = Object.assign(result, item);
      return result;
    }, {});
    this.props.dispatch(teamCreateEnterpriseCard({
      team_id: this.props.team_id,
      channel_id: this.props.item.id,
      website_id: this.state.app.id,
      name: this.state.app_name,
      description: this.state.description,
      password_reminder_interval: this.state.password_reminder_interval,
      receivers: newReceivers
    })).then(response => {
      this.setState({loading: false});
      this.close();
      this.props.resetTeamCard();
    });
  };
  close = () => {
    this.props.resetTeamCard();
  };
  render(){
    const app = this.state.app;
    const selected_users = this.state.users.filter(item => (this.state.selected_users.indexOf(item.id) !== -1));
    const room_manager = this.props.teams[this.props.card.team_id].team_users[this.props.teams[this.props.card.team_id].rooms[this.props.card.channel_id].room_manager_id];
    return (
        <Container fluid id='enterprise-app-adder' class="team-app team-app-adder mrgn0" as="form" onSubmit={this.send}>
          <Transition visible={this.state.app !== null} unmountOnHide={true} mountOnShow={true} animation='scale' duration={300}>
            {this.state.app !== null &&
            <div>
              <Segment>
                <Header as="h5">
                  <div className="display_flex margin_b5rem">
                    <div>
                      <Input className="team-app-input"
                             placeholder="Name your card"
                             name="app_name"
                             value={this.state.app_name}
                             autoComplete="off"
                             onChange={this.handleInput}
                             size="mini"
                             label={<Label><Icon name="home"/></Label>}
                             labelPosition="left"
                             required/>
                    </div>
                  </div>
                </Header>
                <Button icon="delete" type="button" size="mini" class="close" onClick={this.close}/>
                <div class="display_flex">
                  <div class="logo_column">
                    <div class="logo">
                      <img src={app.logo}/>
                    </div>
                  </div>
                  <div class="main_column">
                    <div class="credentials">
                      <div class="display-inline-flex align_items_center">
                        <PasswordChangeDropdown value={this.state.password_reminder_interval} onChange={this.handleInput} roomManager={room_manager.username}/>
                      </div>
                    </div>
                    <Receivers myId={this.props.teams[this.props.card.team_id].my_team_user_id} receivers={selected_users} onDelete={this.onDeleteReceiver} onChange={this.handleReceiverInput}/>
                    <div>
                      {this.state.selected_users.length !== this.state.users.length &&
                      <Dropdown
                          class="mini users-dropdown"
                          search
                          fluid
                          name="selected_users"
                          options={this.state.users}
                          onChange={this.handleInput}
                          value={this.state.selected_users}
                          selection
                          multiple
                          noResultsMessage='No more results found'
                          placeholder="Tag your team members here..."/>}
                    </div>
                    <div>
                      <Input size="mini"
                             fluid
                             class="team-app-input"
                             onChange={this.handleInput}
                             name="description"
                             value={this.state.description}
                             placeholder="You can add a comment here"
                             type="text"
                             label={<Label><Icon name="sticky note"/></Label>}
                             labelPosition="left"/>
                    </div>
                  </div>
                </div>
              </Segment>
              <Button
                icon="send"
                content="Send"
                loading={this.state.loading}
                disabled={this.state.loading}
                floated="right"
                class="mrgn0"
                positive
                size="mini"/>
            </div>}
          </Transition>
        </Container>
    )
  }
}

module.exports = EnterpriseTeamAppAdder;