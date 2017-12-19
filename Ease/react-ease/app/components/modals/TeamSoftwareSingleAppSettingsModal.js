import React, {Component} from "react";
import TeamAppSettingsNameInput from "./TeamAppSettingsNameInput";
import {handleSemanticInput} from "../../utils/utils";
import {Message, Input, Label,Form, Icon, Container, Button} from 'semantic-ui-react';
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {showTeamSoftwareSingleAppSettingsModal} from "../../actions/modalActions";
import {AppSettingsMenu, ShareSection, TeamAppRemoveSection, LabeledInput} from "./utils";
import {isAdmin} from "../../utils/helperFunctions";
import {isCredentialsMatch, isAppInformationEmpty, transformCredentialsListIntoObject, transformWebsiteInfoIntoListAndSetValues, credentialIconType} from "../../utils/utils";
import {removeTeamCardReceiver, teamEditSoftwareSingleApp} from "../../actions/appsActions";
import {editAppName, validateApp} from "../../actions/dashboardActions";
import {CopyPasswordIcon} from "../dashboard/utils";
import {connect} from "react-redux";
import {addNotification} from "../../actions/notificationBoxActions";

@connect(store => ({
  teams: store.teams,
  team_apps: store.team_apps,
  app: store.modals.teamSoftwareSingleAppSettings.app
}))
class TeamSoftwareSingleAppSettingsModal extends Component{
  constructor(props){
    super(props);
    this.state = {
      appName: this.props.app.name,
      view: 'Account',
      credentials: [],
      isEmpty: false,
      app: this.props.app,
      team_app: this.props.team_apps[this.props.app.team_card_id],
      loading: false,
      errorMessage:''
    };
    this.state.isEmpty = this.state.team_app.empty;
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
    const team_app = this.state.team_app;
    const credentials = this.state.credentials.map(item => {
      if (item.name === name)
        item.edit = !item.edit;
      if (!item.edit)
        item.value = team_app.account_information[item.name];
      return item;
    });
    this.setState({credentials: credentials});
  };
  toggleUrlEdit = () => {
    this.setState({editUrl: !this.state.editUrl});
  };
  close = () => {
    this.props.dispatch(showTeamSoftwareSingleAppSettingsModal({active: false}));
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
    const account_information = transformCredentialsListIntoObject(this.state.credentials);
    this.setState({errorMessage: '', loading:true});
    let calls = [];
    if (this.state.appName !== this.state.app.name)
      calls.push(this.props.dispatch(editAppName({
        app_id: this.state.app.id,
        name: this.state.appName
      })));
    if (!isCredentialsMatch(this.state.team_app.account_information, account_information))
      calls.push(this.props.dispatch(teamEditSoftwareSingleApp({
        name: team_app.name,
        team_card_id: team_app.id,
        description: team_app.description,
        url: this.state.url,
        img_url: this.state.img_url ? this.state.img_url : this.props.app.logo,
        connection_information: team_app.software.connection_information,
        account_information: account_information,
        password_reminder_interval: team_app.password_reminder_interval,
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
    const team_app = this.state.team_app;
    const credentials = transformWebsiteInfoIntoListAndSetValues(team_app.software.connection_information, team_app.account_information).map(item => {
      return {
        ...item,
        edit: false
      }
    });
    if (this.state.app.new)
      this.props.dispatch(validateApp({
        app_id: this.state.app.id
      }));
    this.setState({credentials: credentials});
  }
  render(){
    const {app, teams} = this.props;
    const {view, credentials} = this.state;
    const team_app = this.state.team_app;
    const team = teams[team_app.team_id];
    const me = team.team_users[team.my_team_user_id];
    const meReceiver = team_app.receivers.find(item => (item.team_user_id === me.id));
    const meAdmin = isAdmin(me.role);
    const room = team.rooms[team_app.channel_id];
    const inputs = credentials.map((item, idx) => {
      if (item.name === 'password')
        return (
          <Form.Field key={idx}>
            <label>{item.placeholder}</label>
            <div className="display_flex align_items_center">
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
                {!app.empty && (meReceiver.allowed_to_see_password || meAdmin) &&
                <CopyPasswordIcon app_id={app.id}/>}
              </Input>
              {!this.state.isEmpty &&
              <Icon
                name="pencil"
                onClick={this.toggleCredentialEdit.bind(null, item.name)}
                fitted
                link={meAdmin}
                disabled={!meAdmin}
                style={{paddingLeft: '15px'}}/>}
            </div>
          </Form.Field>
        );
      return (
        <Form.Field key={idx}>
          <label>{item.placeholder}</label>
          <div className="display_flex align_items_center">
            <Input
              fluid
              icon
              disabled={!item.edit && !this.state.isEmpty}
              className="modalInput team-app-input"
              size='large'
              type={item.type}
              name={item.name}
              required={this.state.isEmpty}
              onChange={this.handleCredentialInput}
              label={{icon: credentialIconType[item.name] ? credentialIconType[item.name] : 'wait'}}
              value={item.value}
              placeholder={item.placeholder}
              labelPosition='left'/>
            {!this.state.isEmpty &&
            <Icon
              name="pencil"
              onClick={this.toggleCredentialEdit.bind(null, item.name)}
              fitted
              link={meAdmin}
              disabled={!meAdmin}
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
          <div className="app_name_container display-flex align_items_center">
            <div className="squared_image_handler">
              <img src={this.state.img_url ? this.state.img_url: this.props.app.logo} alt="Website logo"/>
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
          <Form onSubmit={this.edit} error={!!this.state.errorMessage}>
            {(isAdmin(me.role) || team_app.team_user_filler_id !== me.id) &&
            <Form.Field>
              Modifications will be applied to your Team.
            </Form.Field>}
            {this.state.isEmpty && me.id === team_app.team_user_filler_id &&
            <Form.Field>
              <Icon name="wrench" style={{color: '#ff9a00'}}/> your admin asked you to enter the credentials.
            </Form.Field>}
            {!isAdmin(me.role) && me.id !== team_app.team_user_filler_id &&
            <Message content={'This app is shared with your team, you’re not allowed to modify it.'}/>}
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
          <TeamAppRemoveSection onRemove={this.remove}/>}
          {view === 'Share' &&
          <ShareSection/>}
        </Container>
      </SimpleModalTemplate>
    )
  }
}

export default TeamSoftwareSingleAppSettingsModal;