import React, {Component} from "react";
import TeamAppSettingsNameInput from "./TeamAppSettingsNameInput";
import {handleSemanticInput, isCredentialsMatch} from "../../utils/utils";
import {Image,List,Segment, Grid,Loader,Checkbox,Message, Input, Label,Form, Menu, Icon, Container, Button} from 'semantic-ui-react';
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {showTeamEnterpriseAppSettingsModal} from "../../actions/modalActions";
import {AppSettingsMenu, ShareSection, RemoveSection, LabeledInput} from "./utils";
import {isAppInformationEmpty, transformCredentialsListIntoObject, transformWebsiteInfoIntoListAndSetValues, credentialIconType} from "../../utils/utils";
import {editAppName, editClassicApp, validateApp} from "../../actions/dashboardActions";
import {connect} from "react-redux";
import {CopyPasswordIcon} from "../dashboard/utils";
import {removeTeamCardReceiver, teamEditEnterpriseCardReceiver} from "../../actions/appsActions";

@connect(store => ({
  teams: store.teams,
  team_apps: store.team_apps,
  app: store.modals.teamEnterpriseAppSettings.app
}))
class TeamEnterpriseAppSettingsModal extends Component {
  constructor(props){
    super(props);
    this.state = {
      appName: this.props.app.name,
      view: 'Account',
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
  close = () => {
    this.props.dispatch(showTeamEnterpriseAppSettingsModal({active: false}));
  };
  remove = () => {
    return this.props.dispatch(removeTeamCardReceiver({
      team_id: this.props.app.team_id,
      team_card_id: this.props.app.team_card_id,
      team_card_receiver_id: this.props.app.team_card_receiver_id
    })).then(response => {
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
    const me = team.team_users[team.my_team_user_id];
    const meReceiver = team_app.receivers.find(item => (item.team_user_id === me.id));
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
                  icon
                  disabled={!item.edit && !this.state.isEmpty}
                  className="modalInput team-app-input"
                  size='large'
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
            <div class="display-flex align_items_center">
              <div class="squared_image_handler">
                <img src={this.props.app.logo} alt="Website logo"/>
              </div>
              <TeamAppSettingsNameInput
                  team_name={team.name}
                  room_name={room.name}
                  value={this.state.appName}
                  onChange={this.handleInput}/>
            </div>
            <AppSettingsMenu
                view={view}
                onChange={this.handleInput}/>
            {view === 'Account' &&
            <Form onSubmit={this.edit} error={!!this.state.errorMessage.length}>
              {this.state.isEmpty &&
              <Form.Field>
                <Icon name="wrench" style={{color: '#ff9a00'}}/> your admin asked you to enter the credentials.
              </Form.Field>}
              {inputs}
              <Message error content={this.state.errorMessage}/>
              <Button
                  type="submit"
                  positive
                  loading={this.state.loading}
                  className="modal-button"
                  content="CONFIRM"/>
            </Form>}
            {view === 'Remove' &&
            <RemoveSection onRemove={this.remove}/>}
            {view === 'Share' &&
            <ShareSection/>}
          </Container>
        </SimpleModalTemplate>
    )
  }
}

export default TeamEnterpriseAppSettingsModal;
