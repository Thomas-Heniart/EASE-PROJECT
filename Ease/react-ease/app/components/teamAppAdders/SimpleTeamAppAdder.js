import React, {Component} from "react";
import {dashboardAndTeamAppSearch, fetchWebsiteInfo, getDashboardApp} from "../../utils/api";
import {handleSemanticInput,
  transformCredentialsListIntoObject,
  transformWebsiteInfoIntoList,
  credentialIconType} from "../../utils/utils";
import {selectUserFromListById} from "../../utils/helperFunctions";
import {requestWebsite} from "../../actions/teamModalActions";
import {teamCreateSingleApp} from "../../actions/appsActions";
import {closeAppAddUI} from "../../actions/teamAppsAddUIActions";
import {connect} from "react-redux";
import {setUserDropdownText, renderSimpleAppUserLabel, PasswordChangeDropdown, PasswordChangeManagerLabel} from "./common";
import { Header, Popup, Grid, Label,List, Search,SearchResult, Container, Divider, Icon, Transition, TextArea, Segment, Checkbox, Form, Input, Select, Dropdown, Button, Message } from 'semantic-ui-react';

const AppResultRenderer = ({website_name, logo, request, profile_name, login}) => {
  if (request)
    return (<div><Icon name="gift" color="red"/><strong>Didn't found your website? Request it!</strong></div>);
  return (
      <div>
        <img src={logo} class="logo"/>
        {website_name}
        {profile_name !== undefined &&
        <span class="text-muted">&nbsp;- from {profile_name} - {login}</span>}
      </div>
  )
};

class SimpleTeamAppSearch extends Component {
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
          item.website_name.toLowerCase().replace(/\s+/g, '').match(value.toLowerCase()) !== null
      )
    });
    apps.push({request: true, key: -1});
    this.setState({apps: apps});
  };
  componentDidMount(){
    dashboardAndTeamAppSearch(this.props.team_id, '').then(response => {
      const apps = response.map(item => {
        item.key = item.id;
        return item;
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

const TeamAppCredentialInput = ({item, onChange}) => {
  return <Input size="mini"
                autoFocus={item.autoFocus}
                class="team-app-input"
                required
                name={item.name}
                onChange={onChange}
                label={<Label><Icon name={credentialIconType[item.name]}/></Label>}
                labelPosition="left"
                placeholder={item.placeholder}
                value={item.value}
                type={item.type}/>;
};


@connect(store => ({
  team_id: store.team.id,
  users: store.users.users
}))
class SimpleTeamAppAdder extends Component {
  constructor(props){
    super(props);
    this.state = {
      loading: false,
      app: null,
      credentials: [],
      password_change_interval: 0,
      description: '',
      room_manager_name: 'johny',
      users: [],
      selected_users: []
    };
  };
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
  setDashboardApp = (app) => {
    getDashboardApp(app.id).then(app => {
      fetchWebsiteInfo(app.website_id).then(info => {
        let credentials = transformWebsiteInfoIntoList(info.information).map(credential => {
          app.information.map(item => {
            if (item.info_name === credential.name)
              credential.value = item.info_value;
          });
          return credential;
        });
        this.setState({app: info, credentials: credentials});
      });
    });
  };
  setApp = (app) => {
    if (app.login !== undefined) {
      this.setDashboardApp(app);
      return;
    }
    if (app.request){
      requestWebsite(this.props.dispatch).then(app => {
        let credentials = transformWebsiteInfoIntoList(app.information);
        this.setState({app: app, credentials: credentials});
      }).catch(err => ({}));
      return;
    }
    fetchWebsiteInfo(app.id).then(app => {
      let credentials = transformWebsiteInfoIntoList(app.information);
      this.setState({app: app, credentials: credentials});
    });
  };
  componentDidMount(){
    let users = this.props.item.userIds.map(item => {
      const user = selectUserFromListById(this.props.users, item);
      return {
        key: item,
        text: setUserDropdownText(user),
        value: item,
        id: item,
        username: user.username,
        can_see_information: false,
        toggleCanSeeInformation: this.toggleCanSeeInformation.bind(null, item)
      }
    });
    const room_manager_name = selectUserFromListById(this.props.users, this.props.item.room_manager_id).username;
    this.setState({users: users, room_manager_name: room_manager_name});
  }
  send = (e) => {
    e.preventDefault();
    this.setState({loading: true});
    const receivers = this.state.users
        .filter(item => (this.state.selected_users.indexOf(item.id) !== -1))
        .map(item => ({
          team_user_id: item.id,
          can_see_information: item.can_see_information
        }));
    this.props.dispatch(teamCreateSingleApp({
      team_id: this.props.team_id,
      channel_id: this.props.item.id,
      website_id: this.state.app.id,
      description: this.state.description,
      password_change_interval: this.state.password_change_interval,
      account_information: transformCredentialsListIntoObject(this.state.credentials),
      receivers: receivers
    })).then(response => {
      this.setState({loading: false});
      this.close();
    });
  };
  close = () => {
    this.props.dispatch(closeAppAddUI());
  };
  render(){
    const app = this.state.app;
    const credentialsInputs = this.state.credentials.map(item => {
      return <TeamAppCredentialInput key={item.priority} onChange={this.handleCredentialInput} item={item}/>
    });

    return (
        <Container fluid class="team-app team-app-adder mrgn0" as="form" onSubmit={this.send}>
          {this.state.app === null &&
          <div class="display-flex align_items_center">
            <SimpleTeamAppSearch team_id={this.props.team_id} select_app_func={this.setApp}/>
            <Button type="button" icon="delete" style={{margin: '0 0 0 .6rem'}} size="mini" class="close" onClick={this.close} color="grey"/>
          </div>}
          <Transition visible={this.state.app !== null} unmountOnHide={true} mountOnShow={true} animation='scale' duration={300}>
            {this.state.app !== null &&
            <div>
              <Segment>
                <Header as="h4">
                  {app.website_name}
                </Header>
                <Button icon="delete" size="mini" class="close" onClick={this.close}/>
                <div class="display_flex">
                  <div class="logo_column">
                    <div class="logo">
                      <img src={app.logo}/>
                    </div>
                  </div>
                  <div class="main_column">
                    <div class="credentials">
                      {credentialsInputs}
                      <div class="display-inline-flex">
                        <PasswordChangeDropdown value={this.state.password_change_interval} onChange={this.handleInput}/>
                        {this.state.password_change_interval !== 0 &&
                        <PasswordChangeManagerLabel username={this.state.room_manager_name}/>}
                      </div>
                    </div>
                    <div>
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

module.exports = SimpleTeamAppAdder;