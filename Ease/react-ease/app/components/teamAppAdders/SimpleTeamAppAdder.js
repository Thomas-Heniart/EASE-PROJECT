import React, {Component} from "react";
import {dashboardAndTeamAppSearch, fetchWebsiteInfo, getDashboardApp} from "../../utils/api";
import {handleSemanticInput,
  transformCredentialsListIntoObject,
  transformWebsiteInfoIntoList,
  credentialIconType} from "../../utils/utils";
import {selectUserFromListById, newSelectUserFromListById} from "../../utils/helperFunctions";
import {requestWebsite, showPinTeamAppToDashboardModal} from "../../actions/teamModalActions";
import {showChooseAppCredentialsModal} from "../../actions/modalActions";
import {teamCreateSingleApp} from "../../actions/appsActions";
import {closeAppAddUI} from "../../actions/teamAppsAddUIActions";
import {connect} from "react-redux";
import {
    setUserDropdownText, PasswordChangeDropdown, PasswordChangeManagerLabel,
    renderSimpleAppAddUserLabel
} from "./common";
import { Header, Popup, Grid, Label,List, Search,SearchResult, Container, Divider, Icon, Transition, TextArea, Segment, Checkbox, Form, Input, Select, Dropdown, Button, Message } from 'semantic-ui-react';
import {reduxActionBinder} from "../../actions/index";

const AppResultRenderer = ({name, logo, request, profile_name, login}) => {
  if (request)
    return (<div><Icon name="gift" color="red"/><strong>Didn't found your website? Request it!</strong></div>);
  return (
      <div>
        <img src={logo} class="logo"/>
        {name}
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
          item.name.toLowerCase().replace(/\s+/g, '').match(value.toLowerCase()) !== null
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
  myId: store.team.myTeamUserId,
  teams: store.teams,
  plan_id: store.team.plan_id,
  card: store.teamCard,
  modal: store.modals
}), reduxActionBinder)
class SimpleTeamAppAdder extends Component {
  constructor(props){
    super(props);
    this.state = {
      app_name: this.props.card.app.name,
      loading: false,
      app: this.props.card.app,
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
  chooseUser = (user) => {
    let selected = this.state.selected_users;
    selected.splice(selected.length + 1, 0, user.id);
    this.setState({ selected_users: selected });
  };
  componentDidMount(){
    let users = this.props.item.team_user_ids.map(item => {
      const user = newSelectUserFromListById(this.props.teams[this.props.card.team_id].team_users, item);
      return {
        key: item,
        text: setUserDropdownText(user),
        value: item,
        id: item,
        role: user.role,
        username: user.username,
        can_see_information: false,
        toggleCanSeeInformation: this.toggleCanSeeInformation.bind(null, item)
      }
    });
    const room_manager_name = newSelectUserFromListById(this.props.teams[this.props.card.team_id].team_users, this.props.item.room_manager_id).username;
    this.setState({users: users, room_manager_name: room_manager_name});
  }
  send = (e) => {
    e.preventDefault();
    // this.setState({loading: true});
    // const meReceiver = this.state.selected_users.indexOf(this.props.myId) !== -1;
    const receivers = this.state.users
        .filter(item => (this.state.selected_users.indexOf(item.id) !== -1));
        // .map(item => ({
        //   [item.id]: {allowed_to_see_password: item.can_see_information}
        // }));
    // const newReceivers = receivers.reduce(function(result, item) {
    //     result = Object.assign(result, item);
    //     return result;
    //   }, {});
    this.props.dispatch(showChooseAppCredentialsModal({
      active: true,
      card_name: this.state.app_name,
      receivers: receivers,
      description: this.state.description,
      password_change_interval: this.state.password_change_interval }));
      /*this.props.dispatch(teamCreateSingleApp({
        team_id: this.props.card.team_id,
        channel_id: this.props.item.id,
        website_id: this.state.app.id,
        description: this.state.description,
        password_change_interval: this.state.password_change_interval,
        // account_information: transformCredentialsListIntoObject(this.state.credentials),
        account_information: {login: 'salut', password: 'salut'},
        receivers: newReceivers
      })).then(response => {
        if (meReceiver)
          this.props.dispatch(showPinTeamAppToDashboardModal(true, response));
        this.setState({loading: false});
        this.close();
      });*/
  };
  close = () => {
    this.props.resetTeamCard();
    // this.props.dispatch(closeAppAddUI());
  };
  render(){
    const app = this.state.app;
    // const credentialsInputs = this.state.credentials.map(item => {
    //   return <TeamAppCredentialInput key={item.priority} onChange={this.handleCredentialInput} item={item}/>
    // });
    return (
        <Container fluid class="team-app team-app-adder mrgn0" as="form" onSubmit={this.send}>
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
                      <div class="display-inline-flex">
                        <PasswordChangeDropdown value={this.state.password_change_interval} onChange={this.handleInput}/>
                        {this.state.password_change_interval !== 0 &&
                        <PasswordChangeManagerLabel username={this.state.room_manager_name}/>}
                      </div>
                    </div>
                    <div>
                      <Dropdown
                          class="mini"
                          search
                          fluid
                          name="selected_users"
                          options={this.state.users}
                          onChange={this.handleInput}
                          value={this.state.selected_users}
                          selection
                          renderLabel={renderSimpleAppAddUserLabel}
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
              <Button
                  icon="arrow right"
                  content="Next"
                  loading={this.state.loading}
                  disabled={this.state.loading}
                  floated="right"
                  class="mrgn0 next_button"
                  positive size="mini"/>
            </div>}
          </Transition>
        </Container>
    )
  }
}

module.exports = SimpleTeamAppAdder;