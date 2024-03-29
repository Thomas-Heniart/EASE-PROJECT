import React, {Component} from "react";
import TeamAppSettingsNameInput from "./TeamAppSettingsNameInput";
import {handleSemanticInput, isCredentialsMatch} from "../../utils/utils";
import {Message, Input, Label,Form, Icon, Container, Button} from 'semantic-ui-react';
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {showTeamEnterpriseAppSettingsModal} from "../../actions/modalActions";
import {AppSettingsMenu, ShareSection, TeamAppRemoveSection} from "./utils";
import {transformCredentialsListIntoObject, transformWebsiteInfoIntoListAndSetValues, credentialIconType} from "../../utils/utils";
import {editAppName, validateApp} from "../../actions/dashboardActions";
import {isOwner} from "../../utils/helperFunctions";
import {connect} from "react-redux";
import {CopyPasswordIcon} from "../dashboard/utils";
import {removeTeamCardReceiver, teamEditEnterpriseCardReceiver} from "../../actions/appsActions";
import {addNotification} from "../../actions/notificationBoxActions";
import {testCredentials} from "../../actions/catalogActions";
import * as api from "../../utils/api";

@connect(store => ({
  teams: store.teams,
  team_apps: store.team_apps,
  app: store.modals.teamEnterpriseAppSettings.app,
  remove: store.modals.teamEnterpriseAppSettings.remove
}))
class TeamEnterpriseAppSettingsModal extends Component {
  constructor(props){
    super(props);
    this.state = {
      appName: this.props.app.name,
      view: this.props.remove ? 'Remove' : 'Account',
      credentials: [],
      team_app: this.props.team_apps[this.props.app.team_card_id],
      isEmpty: false,
      loading: false,
      errorMessage:''
    };
    const team_app = this.state.team_app;
    const team = this.props.teams[team_app.team_id];
    const me = team.team_users[team.my_team_user_id];
    const meReceiver = team_app.receivers.find(item => (item.team_user_id === me.id));
    this.state.source_credentials = meReceiver.account_information;
    this.state.isEmpty = meReceiver.empty;
  }
  handleInput = handleSemanticInput.bind(this);
  handleCredentialInput = (e, {name, value}) => {
    const credentials = this.state.credentials.map(item => {
      if (item.name === name)
        item.value = value;
      return item;
    });
    this.setState({credentials: credentials});
  };
  toggleCredentialEdit = (name) => {
    const credentials = this.state.credentials.map(item => {
      if (item.name === name)
        item.edit = !item.edit;
      if (!item.edit)
        item.value = this.state.source_credentials[item.name];
      return item;
    });
    this.setState({credentials: credentials});
  };
  testConnection = () => {
    let account_information = transformCredentialsListIntoObject(this.state.credentials);
    if (!this.state.isEmpty) {
      api.dashboard.getAppPassword({
        app_id: this.props.app.id
      }).then(response => {
        if (this.state.credentials.filter(item => {return item.name === 'password' && !item.edit}).length > 0)
          account_information.password = response.password;
        this.props.dispatch(testCredentials({
          account_information: account_information,
          website_id: this.props.app.website.id
        }));
      });
    }
    else {
      this.props.dispatch(testCredentials({
        account_information: account_information,
        website_id: this.props.app.website.id
      }));
    }
  };
  close = () => {
    this.props.dispatch(showTeamEnterpriseAppSettingsModal({active: false}));
  };
  remove = () => {
    return this.props.dispatch(removeTeamCardReceiver({
      team_id: this.props.app.team_id,
      team_card_id: this.props.app.team_card_id,
      team_card_receiver_id: this.props.app.team_card_receiver_id
    })).then(response => {
      this.props.dispatch(addNotification({
        text: `${this.props.app.name} has been successfully removed!`
      }));
      this.close();
    }).catch(err => {
      throw err;
    });
  };
  edit = (e) => {
    e.preventDefault();
    const team_app = this.state.team_app;
    const team = this.props.teams[team_app.team_id];
    const me = team.team_users[team.my_team_user_id];
    const meReceiver = team_app.receivers.find(item => (item.team_user_id === me.id));
    const account_information = transformCredentialsListIntoObject(this.state.credentials);

    this.setState({errorMessage: '', loading:true});
    let calls = [];
    if (this.state.appName !== this.props.app.name)
      calls.push(this.props.dispatch(editAppName({
        app_id: this.props.app.id,
        name: this.state.appName
      })));
    if (!isCredentialsMatch(this.state.source_credentials, account_information))
      calls.push(this.props.dispatch(teamEditEnterpriseCardReceiver({
        team_id: team_app.team_id,
        team_card_id: team_app.id,
        team_card_receiver_id: meReceiver.id,
        account_information: account_information
      })));
    Promise.all(calls).then(response => {
      this.setState({loading: false});
      this.props.dispatch(addNotification({
        text: `${this.props.app.name} has been successfully modified!`
      }));
      this.close();
    }).catch(err => {
      this.setState({errorMessage: err, loading: false});
    });
  };
  componentWillMount(){
    const {app, teams} = this.props;
    const team_app = this.state.team_app;
    const team = teams[team_app.team_id];
    const me = team.team_users[team.my_team_user_id];
    const meReceiver = team_app.receivers.find(item => (item.team_user_id === me.id));
    const credentials = transformWebsiteInfoIntoListAndSetValues(team_app.website.information, meReceiver.account_information).map(item => {
      return {
        ...item,
        edit: false
      }
    });
    if (app)
      this.props.dispatch(validateApp({
        app_id: app.id
      }));
    this.setState({credentials: credentials});
  }
  render(){
    const {view, credentials} = this.state;
    const {app, teams} = this.props;
    const team_app = this.props.team_apps[app.team_card_id];
    const team = teams[team_app.team_id];
    const senderUser = team.team_users[team_app.team_user_sender_id];
    const room = teams[team_app.team_id].rooms[team_app.channel_id];
    const inputs = credentials.map((item, idx) => {
      if (item.name === 'password')
        return (
            <Form.Field key={idx}>
              <label>{item.placeholder}</label>
              <div class="display_flex align_items_center">
                <Input
                    fluid
                    icon
                    disabled={!item.edit && !this.state.isEmpty}
                    className="modalInput team-app-input"
                    size='large'
                    required={this.state.isEmpty}
                    type={item.type}
                    name={item.name}
                    onChange={this.handleCredentialInput}
                    value={(item.edit || this.state.isEmpty) ? item.value : '********'}
                    placeholder={item.placeholder}
                    labelPosition='left'>
                  <Label><Icon name="lock"/></Label>
                  <input/>
                  {!this.state.isEmpty &&
                  <CopyPasswordIcon app_id={app.id}/>}
                </Input>
                {!this.state.isEmpty &&
                <Icon
                    name="pencil"
                    onClick={this.toggleCredentialEdit.bind(null, item.name)}
                    fitted
                    link
                    style={{paddingLeft: '15px'}}/>}
              </div>
            </Form.Field>
        );
      return (
          <Form.Field key={idx}>
            <label>{item.placeholder}</label>
            <div class="display_flex align_items_center">
              <Input
                  fluid
                  disabled={!item.edit && !this.state.isEmpty}
                  className="modalInput team-app-input"
                  size='large'
                  required={this.state.isEmpty}
                  type={item.type}
                  name={item.name}
                  onChange={this.handleCredentialInput}
                  label={{ icon: credentialIconType[item.name]}}
                  value={item.value}
                  placeholder={item.placeholder}
                  labelPosition='left'/>
              {!this.state.isEmpty &&
              <Icon
                  name="pencil"
                  onClick={this.toggleCredentialEdit.bind(null, item.name)}
                  fitted
                  link
                  style={{paddingLeft: '15px'}}/>}
            </div>
          </Form.Field>
      )
    });
    return (
        <SimpleModalTemplate
            onClose={this.close}
            headerContent={"App settings"}>
          <Container class="app_settings_modal">
            <div class="app_name_container display-flex align_items_center">
              <div class="squared_image_handler">
                <img src={this.props.app.logo} alt="Website logo"/>
              </div>
              <TeamAppSettingsNameInput
                  team_name={team.name}
                  room_name={room.name}
                  value={this.state.appName}
                  onChange={this.handleInput}/>
            </div>
            {!this.state.isEmpty &&
            <AppSettingsMenu
                view={view}
                onChange={this.handleInput}/>}
            {view === 'Account' &&
            <Form onSubmit={this.edit} error={!!this.state.errorMessage.length}>
              {this.state.isEmpty &&
              <Form.Field>
                <Icon name="wrench" style={{color: '#ff9a00'}}/> {!!senderUser ? senderUser.username : `${team_app.name}'s manager`} asked you to enter your connection information.
              </Form.Field>}
              {inputs}
              <Message error content={this.state.errorMessage}/>
              {(this.state.credentials.filter(item => {return item.edit}).length > 0 || this.state.isEmpty) &&
              <span id='test_credentials' onClick={this.testConnection}>Test connection <Icon color='green' name='magic'/></span>}
              <Button
                  type="submit"
                  positive
                  loading={this.state.loading}
                  className="modal-button"
                  content="CONFIRM"/>
            </Form>}
            {view === 'Remove' &&
            <TeamAppRemoveSection onRemove={this.remove}/>}
            {view === 'Share' &&
            <ShareSection/>}
          </Container>
        </SimpleModalTemplate>
    )
  }
}

export default TeamEnterpriseAppSettingsModal;
