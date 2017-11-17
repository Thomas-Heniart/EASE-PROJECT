import React, {Component} from "react";
import {teamAppSearch, fetchWebsiteInfo, getDashboardApp} from "../../utils/api";
import {handleSemanticInput,
  transformCredentialsListIntoObject,
  transformWebsiteInfoIntoList,
  credentialIconType} from "../../utils/utils";
import {selectUserFromListById, newSelectUserFromListById} from "../../utils/helperFunctions";
import {requestWebsite, showPinTeamAppToDashboardModal, showUpgradeTeamPlanModal} from "../../actions/teamModalActions";
import {teamCreateSingleApp, teamCreateEnterpriseApp} from "../../actions/appsActions";
import {closeAppAddUI} from "../../actions/teamAppsAddUIActions";
import {connect} from "react-redux";
import {ExtendFillSwitch, setUserDropdownText, renderSimpleAppUserLabel, PasswordChangeDropdown, PasswordChangeManagerLabel} from "./common";
import { Header, Popup, Grid, Label,List, Search,SearchResult, Container, Divider, Icon, Transition, TextArea, Segment, Checkbox, Form, Input, Select, Dropdown, Button, Message } from 'semantic-ui-react';
import {reduxActionBinder} from "../../actions/index";

const AppResultRenderer = ({name, logo, request}) => {
  if (request)
    return (<div><Icon name="gift" color="red"/><strong>Didn't found your website? Request it!</strong></div>);
  return (
      <div>
        <img src={logo} class="logo"/>
        {name}
      </div>
  )
};

class TeamAppSearch extends Component {
  constructor(props){
    super(props);
    this.state = {
      allApps: [],
      apps: [],
      loading: true,
      value: ''
    }
  }
  handleInput = (e, {value}) => {
    this.setState({value: value});
    const apps = this.state.allApps.filter(item => {
      return (
          item.name.toLowerCase().replace(/\s+/g, '').match(value.toLowerCase()) !== null
      )
    });
    apps.push({request: true, key: -1});
    this.setState({apps: apps});
  };
  componentDidMount(){
    teamAppSearch(this.props.team_id, '').then(response => {
      const apps = response.map(item => {
        item.key = item.id;
        return item;
      }).sort(function(a,b){
        if (a.name < b.name)
          return -1;
        if (a.name > b.name)
          return 1;
        return 0;
      });
      this.setState({allApps: apps, apps: apps, loading: false});
    });
  }
  render(){
    return (
        <Search
            fluid
            minCharacters={0}
            autoFocus
            showNoResults={false}
            loading={this.state.loading}
            placeholder="Search websites here..."
            value={this.state.value}
            class="inverted full_flex bordered_scrollbar"
            onResultSelect={(e, data) => {this.props.select_app_func(data.result)}}
            resultRenderer={AppResultRenderer}
            onSearchChange={this.handleInput}
            size="mini"
            results={this.state.apps}/>
    )
  }
}

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

const ReceiverCredentialsInput = ({receiver, onChange, onDelete}) => {
  return (
      <div class="receiver">
        <Label class="receiver-label"><span>{receiver.username}</span> <Icon name="delete" link onClick={onDelete.bind(null, receiver.id)}/></Label>
      </div>
  )
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

const Receivers = ({receivers, onChange, onDelete, extended, myId}) => {
  return (
      <div class="receivers">
        {receivers.map(item => {
          if (extended || item.id === myId)
            return <ExtendedReceiverCredentialsInput key={item.id} isMe={item.id === myId} extended={extended} receiver={item} onChange={onChange} onDelete={onDelete}/>
          return <ReceiverCredentialsInput key={item.id} extended={extended} receiver={item} onChange={onChange} onDelete={onDelete}/>
        })}
      </div>
  )
};

@connect(store => ({
  team_id: store.team.id,
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
      app: null,
      password_change_interval: 0,
      description: '',
      users: [],
      selected_users: [],
      fill_in_switch: false
    }
  }
  componentWillMount() {
    this.setState({ app: this.props.card.app });
  };
  handleInput = handleSemanticInput.bind(this);
  changeFillInSwitch = (e, {checked}) => {
    if (this.props.plan_id === 0 && !checked){
      this.props.dispatch(showUpgradeTeamPlanModal(true, 2));
      return;
    }
    this.setState({fill_in_switch: !checked});
  };
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
  componentDidMount(){
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
  }
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
    const meReceiver = this.state.selected_users.indexOf(this.props.myId) !== -1;
    const receivers = this.state.users
        .filter(item => (this.state.selected_users.indexOf(item.id) !== -1))
        .map(item => ({
          team_user_id: item.id,
          account_information: transformCredentialsListIntoObject(item.credentials)
        }));
    this.props.dispatch(teamCreateEnterpriseApp({
      team_id: this.props.team_id,
      channel_id: this.props.item.id,
      website_id: this.state.app.id,
      description: this.state.description,
      password_change_interval: this.state.password_change_interval,
      fill_in_switch: this.state.fill_in_switch,
      receivers: receivers
    })).then(response => {
      if (meReceiver)
        this.props.dispatch(showPinTeamAppToDashboardModal(true, response));
      this.setState({loading: false});
      this.close();
    });
  };
  close = () => {
    this.props.resetTeamCard();
  };
  render(){
    const app = this.state.app;
    const selected_users = this.state.users.filter(item => (this.state.selected_users.indexOf(item.id) !== -1));

    return (
        <Container fluid id='enterprise-app-adder' class="team-app team-app-adder mrgn0" as="form" onSubmit={this.send}>
          <Transition visible={this.state.app !== null} unmountOnHide={true} mountOnShow={true} animation='scale' duration={300}>
            {this.state.app !== null &&
            <div>
              <Segment>
                <Header as="h4">
                  {app.name}
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
                        <PasswordChangeDropdown value={this.state.password_change_interval} onChange={this.handleInput}/>
                        <ExtendFillSwitch value={this.state.fill_in_switch} onClick={this.changeFillInSwitch}/>
                        {this.props.plan_id === 0 &&
                        <img style={{height: '18px'}} src="/resources/images/upgrade.png"/>}
                      </div>
                    </div>
                    <Receivers extended={this.state.fill_in_switch} myId={this.props.teams[this.props.card.team_id].my_team_user_id} receivers={selected_users} onDelete={this.onDeleteReceiver} onChange={this.handleReceiverInput}/>
                    <div>
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
                          placeholder="Tag your team members here..."/>
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
              <Button icon="send" content="Send" loading={this.state.loading} disabled={this.state.loading} floated="right" class="mrgn0" positive size="mini"/>
            </div>}
          </Transition>
        </Container>
    )
  }
}

module.exports = EnterpriseTeamAppAdder;