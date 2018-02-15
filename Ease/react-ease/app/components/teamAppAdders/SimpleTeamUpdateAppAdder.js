import React, {Component} from "react";
import {fetchWebsiteInfo, getDashboardApp, getClearbitLogo, getClearbitLogoAutoComplete} from "../../utils/api";
import {credentialIconType, handleSemanticInput, transformWebsiteInfoIntoList} from "../../utils/utils";
import {newSelectUserFromListById} from "../../utils/helperFunctions";
import {requestWebsite} from "../../actions/teamModalActions";
import {connect} from "react-redux";
import { setUserDropdownText, PasswordChangeDropdown, renderSimpleAppAddUserLabel} from "./common";
import { Header, Label, Container, Icon, Transition, Segment, Input, Dropdown, Button } from 'semantic-ui-react';
import {reduxActionBinder} from "../../actions/index";

@connect(store => ({
  teams: store.teams,
  card: store.teamCard,
  modal: store.modals
}), reduxActionBinder)
class SimpleTeamUpdateAppAdder extends Component {
  constructor(props){
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
  changeUrl = (e, {value}) => {
    this.setState({app_url: value}, this.getLogo);
  };
  handleInputName = (e, {value}) => {
    this.setState({app_name: value}, this.getLogo);
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
  logoLetter = () => {
    let first = '';
    let second = '';
    let space = false;
    for (let letter = 0; letter < this.state.app_name.length; letter++) {
      if (first.length < 1 && this.state.app_name[letter] !== ' ')
        first = this.state.app_name[letter];
      else if (first.length > 0 && second.length < 1 && this.state.app_name[letter] !== ' ' && space === true)
        second = this.state.app_name[letter];
      else if (this.state.app_name[letter] === ' ')
        space = true;
    }
    if (second !== '')
      return first.toUpperCase() + second.toUpperCase();
    else
      return first.toUpperCase();
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
  componentDidMount(){
    this.chooseAllUsers();
  }
  send = (e) => {
    console.log('nope');
  };
  
  close = () => {
    this.props.resetTeamCard();
    // this.props.dispatch(closeAppAddUI());
  };
  render(){
    const app = this.state.app;
    const room_manager = this.props.teams[this.props.card.team_id].team_users[this.props.item.room_manager_id];
    const team = this.props.teams[this.props.item.team_id];

    console.log('*******************************************');
    console.log('*******************************************');
    console.log('*******************************************');
    console.log('PROPS', this.props);
    console.log('STATE', this.state);
    console.log('*******************************************');
    console.log('*******************************************');
    console.log('*******************************************');
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
                           onChange={this.handleInputName}
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
                    {this.state.img_url ?
                      <div style={{backgroundImage:`url('${this.state.img_url}')`}}>
                        {(this.state.subtype === 'AnyApp' || this.state.subtype === 'softwareApp') &&
                        <button className="button-unstyle action_button close_button" onClick={this.imgNone}>
                          <Icon name="close" class="mrgn0" link/>
                        </button>}
                      </div>
                      : this.state.app_name ?
                        <div style={{backgroundColor:'#373b60',color:'white'}}>
                          <p style={{margin:'auto'}}>{this.logoLetter()}</p>
                        </div>
                        :
                        <div style={{backgroundColor:'white',color: '#dededf'}}>
                          <Icon name='wait' style={{margin:'auto'}}/>
                        </div>}
                  </div>
                </div>
                <div class="main_column">
                  <div class="credentials">
                    <div class="display-inline-flex">
                      {this.props.card.subtype === 'AnyApp' &&
                      <Input className="team-app-input any_app"
                             placeholder="Website URL"
                             name="app_url"
                             value={this.state.app_url}
                             autoComplete="off"
                             onChange={this.changeUrl}
                             size="mini"
                             label={<Label><Icon name="home"/></Label>}
                             labelPosition="left"
                             required/>}

                      {/*<Input size="mini"*/}
                             {/*class="team-app-input"*/}
                             {/*required={item.name !== 'password'}*/}
                             {/*readOnly={readOnly}*/}
                             {/*disabled={disabled}*/}
                             {/*name={item.name}*/}
                             {/*onChange={onChange}*/}
                             {/*label={<Label><Icon name={credentialIconType[item.name]}/></Label>}*/}
                             {/*labelPosition="left"*/}
                             {/*placeholder={item.name === 'password' ? '••••••••' : item.placeholder}*/}
                             {/*value={item.name === 'password' && readOnly ? 'abcdabcd' : item.value}*/}
                             {/*type={item.type}>*/}
                      {/*</Input>*/}
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