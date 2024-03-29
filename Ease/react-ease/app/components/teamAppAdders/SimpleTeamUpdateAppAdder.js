import React, {Component} from "react";
import {logoLetter, transformWebsiteInfoIntoListAndSetValues} from "../../utils/utils";
import {fetchWebsiteInfo, getDashboardApp, getClearbitLogo, getClearbitLogoAutoComplete} from "../../utils/api";
import {credentialIconType, handleSemanticInput, transformWebsiteInfoIntoList, transformCredentialsListIntoObject} from "../../utils/utils";
import {newSelectUserFromListById} from "../../utils/helperFunctions";
import {cardAdded, requestWebsite} from "../../actions/teamModalActions";
import {connect} from "react-redux";
import { setUserDropdownText, PasswordChangeDropdown, renderSimpleAppAddUserLabel} from "./common";
import {
  Header, Label, Container, Icon, Transition, Segment, Input, Dropdown, Button,
  TextArea
} from 'semantic-ui-react';
import {reduxActionBinder} from "../../actions/index";
import {teamCreateAnySingleCard, teamCreateSingleApp} from "../../actions/appsActions";
import {deleteUpdate} from "../../actions/catalogActions";
import {updateAccepted} from "../../actions/dashboardActions";

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

@connect(store => ({
  teams: store.teams,
  card: store.teamCard,
  modal: store.modals,
  updates: store.catalog.updates
}), reduxActionBinder)
class SimpleTeamUpdateAppAdder extends Component {
  constructor(props) {
    super(props);
    this.state = {
      app_name: this.props.card.name,
      app_url: this.props.card.url,
      loading: false,
      app: this.props.card.app,
      credentials: [],
      password_reminder_interval: 0,
      description: '',
      room_manager_name: 'johny',
      users: [],
      selected_users: [],
      img_url: this.props.card.app.logo
    };
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
  getLogo = () => {
    if (this.props.card.subtype === 'AnyApp')
      getClearbitLogo(this.state.app_url).then(response => {
        this.setState({img_url: response});
      }).catch(err => {
        this.setState({img_url: ''});
      });
    else if (this.props.card.subtype === 'softwareApp') {
      const name = this.state.app_name.replace(/\s+/g, '').toLowerCase();
      if (name === '')
        this.setState({img_url: ''});
      else
        getClearbitLogoAutoComplete(name).then(response => {
          this.setState({img_url: response});
        }).catch(err => {
          this.setState({img_url: ''});
        });
    }
  };
  handleInputName = (e, {value}) => {
    this.setState({app_name: value});
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
  imgNone = (e) => {
    e.preventDefault();
    this.setState({img_url:''});
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
  componentWillMount(){
    let users = this.props.item.team_user_ids.map(item => {
      const user = newSelectUserFromListById(this.props.teams[this.props.card.team_id].team_users, item);
      if (user.role > 1 || this.props.card.subtype === 'softwareApp' || this.props.card.subtype === 'AnyApp') {
        return {
          key: item,
          text: setUserDropdownText(user),
          value: item,
          id: item,
          role: user.role,
          username: user.username,
          can_see_information: true
        }
      }
      else {
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
      }
    });
    const room_manager_name = this.props.teams[this.props.card.team_id].team_users[this.props.item.room_manager_id].username;
    this.setState({users: users, room_manager_name: room_manager_name});
  };
  componentDidMount() {
    this.chooseAllUsers();
    let credentials = transformWebsiteInfoIntoListAndSetValues(this.props.card.app.information, this.props.card.account_information);
      credentials.map(item => {
      this.state.credentials.push(item);
    });
  }
  send = (e) => {
    e.preventDefault();
    const receivers = this.state.users
      .filter(item => (this.state.selected_users.indexOf(item.id) !== -1))
      .map(item => ({
        [item.id]: {allowed_to_see_password: item.can_see_information}
      }));
    const newReceivers = receivers.reduce(function (result, item) {
      result = Object.assign(result, item);
      return result;
    }, {});
    if (!this.props.card.app.url) {
      this.props.dispatch(teamCreateSingleApp({
        team_id: this.props.card.team_id,
        channel_id: this.props.card.channel_id,
        website_id: this.props.card.app.id,
        name: this.props.card.name,
        description: this.state.description,
        password_reminder_interval: this.state.password_reminder_interval,
        account_information: transformCredentialsListIntoObject(this.state.credentials),
        receivers: newReceivers
      })).then(response => {
        this.props.dispatch(cardAdded({
          card: response
        }));
        this.finish();
      });
    } else {
      const connection_information = this.state.credentials.reduce((prev, curr) => {
        return {...prev, [curr.name]: {type: curr.type, priority: curr.priority, placeholder: curr.placeholder}}
      }, {});
      this.props.dispatch(teamCreateAnySingleCard({
        team_id: this.props.card.team_id,
        channel_id: this.props.card.channel_id,
        name: this.props.card.name,
        description: this.state.description,
        password_reminder_interval: this.state.password_reminder_interval,
        url: this.props.card.app.url,
        img_url: this.props.card.app.information.logo,
        connection_information: connection_information,
        account_information: transformCredentialsListIntoObject(this.state.credentials),
        credentials_provided: false,
        receivers: newReceivers
      })).then(response => {
        this.finish();
      });
    }
  };
  finish = () => {
    this.props.dispatch(updateAccepted({
      type: this.props.updates.find((update) => update.id === this.props.card.app.update_id).type
    }));
    this.props.dispatch(deleteUpdate({id: this.props.card.app.update_id})).then(() => {
      this.setState({loading: false});
      this.close();
      this.props.resetTeamCard();
    });
  };
  close = () => {
    this.props.resetTeamCard();
  };
  render(){
   const credentials = this.state.credentials.map(item => {
      return <TeamAppCredentialInput key={item}
                                     onChange={this.handleCredentialInput}
                                     item={item}/>
    });
    const app = this.state.app;
    const room_manager = this.props.teams[this.props.card.team_id].team_users[this.props.item.room_manager_id];
    const team = this.props.teams[this.props.item.team_id];
    return (
      <Container fluid class="team-app team-app-adder mrgn0" as="form" onSubmit={this.send}>
        <Transition visible={app !== null} unmountOnHide={true} mountOnShow={true} animation='scale' duration={300}>
          {app !== null &&
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
                           onChange={this.handleInputName}
                           size="mini"
                           labelPosition="left"
                           required/>
                  </div>
                </div>
              </Header>
              <Button icon="delete" type="button" size="mini" class="close" onClick={this.close}/>
              <div class="display_flex">
                <div class="logo_column">
                  <div class="logo">
                    {this.state.img_url ?
                      <div style={{backgroundImage:`url('${this.state.img_url}')`}}>
                        {(this.state.subtype === 'AnyApp' || this.state.subtype === 'softwareApp') &&
                        <button className="button-unstyle action_button close_button" onClick={this.imgNone}>
                          <Icon name="close" class="mrgn0" link/>
                        </button>}
                      </div>
                      : this.state.app_name ?
                        <div style={{backgroundColor:'#373b60',color:'white'}}>
                          <p style={{margin:'auto'}}>{logoLetter(this.state.app_name)}</p>
                        </div>
                        :
                        <div style={{backgroundColor:'white',color: '#dededf'}}>
                          <Icon name='wait' style={{margin:'auto'}}/>
                        </div>}
                  </div>
                </div>
                <div class="main_column">
                  {this.props.card.app.url &&
                  <Input size="mini"
                         class="team-app-input"
                         readOnly
                         name={this.props.card.app.url}
                         label={<Label><Icon name="home"/></Label>}
                         labelPosition="left"
                         value={this.props.card.app.url} />}
                  <div class="credentials">
                    <div class="display-inline-flex">
                        {credentials}
                      <PasswordChangeDropdown
                        team={team}
                        dispatch={this.props.dispatch}
                        value={this.state.password_reminder_interval}
                        onChange={this.handleInput}
                        roomManager={room_manager.username}/>
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
                      noResultsMessage='No more results found'
                      placeholder="Tag your team members here..."/>
                  </div>
                  <div class="ui form description display_flex">
                    <div class="label">
                      <Icon name="sticky note" fitted/>
                    </div>
                    <TextArea size="mini"
                              fluid
                              class="team-app-input"
                              onChange={this.handleInput}
                              name="description"
                              rows={1}
                              value={this.state.description}
                              placeholder="You can add a comment here"
                              type="text"/>
                  </div>
                </div>
              </div>
            </Segment>
            <Button
              icon="arrow right"
              content="Done"
              type='submit'
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

module.exports = SimpleTeamUpdateAppAdder;