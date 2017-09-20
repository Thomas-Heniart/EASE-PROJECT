import React, {Component} from "react";
import classnames from 'classnames';
import {dashboardAndTeamAppSearch, fetchWebsiteInfo} from "../../utils/api";
import {handleSemanticInput,
  transformWebsiteInfoIntoList,
  passwordChangeOptions,
  credentialIconType} from "../../utils/utils";
import {selectUserFromListById} from "../../utils/helperFunctions";
import {requestWebsite} from "../../actions/teamModalActions";
import {connect} from "react-redux";
import { Header, Grid, Label,List, Search,SearchResult, Container, Divider, Icon, Transition, TextArea, Segment, Checkbox, Form, Input, Select, Dropdown, Button, Message } from 'semantic-ui-react';

const AppResultRenderer = ({website_name, logo, request}) => {
  if (request)
    return (<div><Icon name="gift" color="red"/><strong>Didn't found your website? Request it!</strong></div>);
  return (<div><img src={logo} class="logo"/>{website_name}</div>)
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
            loading={this.state.loading}
            placeholder="Search websites here..."
            value={this.state.value}
            class="inverted"
            onResultSelect={(e, data) => {this.props.select_app_func(data.result)}}
            resultRenderer={AppResultRenderer}
            onSearchChange={this.handleInput}
            size="mini"
            results={this.state.apps}/>
    )
  }
}

const PasswordChangeDropdown = ({value, onChange}) => {
  return (
      <Dropdown class="mini icon"
                value={value}
                onChange={onChange}
                button
                icon="refresh"
                labeled
                options={passwordChangeOptions}/>
  )
};

const PasswordChangeManagerLabel = ({username})=> {
  return (
      <Label class="pwd-manager">{username}&nbsp;&nbsp;<Icon name="eye"/></Label>
  )
};

const TeamAppCredentialInput = ({item, onChange}) => {
  return <Input size="mini"
                class="team-app-input"
                name={item.name}
                onChange={onChange}
                label={<Label><Icon name={credentialIconType[item.name]}/></Label>}
                labelPosition="left"
                placeholder={item.placeholder}
                value={item.value}
                type={item.type}/>;
};

const setUserDropdownText = (user) => {
  return user.username + user.first_name.length > 0 || user.last_name > 0 ? ` - ${user.first_name} ${user.last_name}` : null;
};

@connect(store => ({
  team_id: store.team.id,
  users: store.users.users
}))
class SimpleTeamAppAdder extends Component {
  constructor(props){
    super(props);
    this.state = {
      app: null,
      credentials: [],
      receivers: [],
      description: '',
      users: []
    };
  };
  handleInput = handleSemanticInput.bind(this);
  handleCredentialInput = (e, {name, value}) => {
    let credentials = this.state.credentials.map(item => {
      if (name === item.name)
        item.value = value;
      return item;
    });
    this.setState({credentials: credentials});
  };
  setApp = (app) => {
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
        value: item
      }
    });
    this.setState({users: users});
  }
  render(){
    const app = this.state.app;
    const credentialsInputs = this.state.credentials.map(item => {
      return <TeamAppCredentialInput key={item.priority} onChange={this.handleCredentialInput} item={item}/>
    });

    return (
        <Container fluid id="simple_team_app_add" class="team-app" as="form">
          {this.state.app === null &&
          <SimpleTeamAppSearch team_id={this.props.team_id} select_app_func={this.setApp}/>}
          {this.state.app !== null &&
          <Segment>
            <Header as="h4">
              {app.website_name}
            </Header>
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
                    <PasswordChangeDropdown value={0} />
                    <PasswordChangeManagerLabel username="johny"/>
                  </div>
                </div>
                <div>
                  <Dropdown
                      class="mini"
                      search={true}
                      fluid
                      options={[]}
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
                         placeholder="What is this about? Any comment?"
                         type="text"
                         label={<Label><Icon name="sticky note"/></Label>}
                         labelPosition="left"/>
                </div>
              </div>
            </div>
          </Segment>}
        </Container>
    )
  }
}

module.exports = SimpleTeamAppAdder;