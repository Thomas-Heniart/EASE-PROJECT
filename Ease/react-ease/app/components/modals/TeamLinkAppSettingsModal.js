import React, {Component} from "react";
import TeamAppSettingsNameInput from "./TeamAppSettingsNameInput";
import {handleSemanticInput} from "../../utils/utils";
import {getLogo} from "../../utils/api";
import {Message, Form, Icon, Container, Button} from 'semantic-ui-react';
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {showTeamLinkAppSettingsModal} from "../../actions/modalActions";
import {AppSettingsMenu, ShareSection, TeamAppRemoveSection, LabeledInput} from "./utils";
import {connect} from "react-redux";
import {removeTeamCardReceiver, teamEditLinkCard} from "../../actions/appsActions";
import {isAdmin} from "../../utils/helperFunctions";
import {editAppName, validateApp} from "../../actions/dashboardActions";
import {addNotification} from "../../actions/notificationBoxActions";

@connect(store => ({
  app: store.modals.teamLinkAppSettings.app,
  teams: store.teams,
  team_apps: store.team_apps,
  remove: store.modals.teamLinkAppSettings.remove
}))
class TeamLinkAppSettingsModal extends Component {
  constructor(props){
    super(props);
    this.state = {
      view: this.props.remove ? 'Remove' : 'Account',
      url: this.props.app.url,
      appName: this.props.app.name,
      logo: '',
      team_app: this.props.team_apps[this.props.app.team_card_id],
      loading: false,
      edit: false,
      errorMessage: ''
    };
    this.state.logo = this.state.team_app.logo
  }
  handleInput = handleSemanticInput.bind(this);
  linkInput = (e, {value}) => {
    this.setState({url: value});
    getLogo({url: value}).then(response => {
      this.setState({logo:response});
    });
  };
  toggleEdit = () => {
    const edit = !this.state.edit;
    this.setState({edit: !this.state.edit});
    if (!edit)
      this.linkInput(null, {value: this.props.app.url});
  };
  close = () => {
    this.props.dispatch(showTeamLinkAppSettingsModal({active: false}));
  };
  edit = (e) => {
    e.preventDefault();
    const team_app = this.state.team_app;
    let calls = [];
    if (this.state.appName !== this.props.app.name)
      calls.push(this.props.dispatch(editAppName({
        app_id: this.props.app.id,
        name: this.state.appName
      })));
    if (this.state.url !== this.props.app.url)
      calls.push(this.props.dispatch(teamEditLinkCard({
        team_card_id: team_app.id,
        name:team_app.name,
        description: team_app.description,
        url: this.state.url,
        img_url: this.state.logo
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
  componentWillMount() {
    if (this.props.app.new)
      this.props.dispatch(validateApp({
        app_id: this.props.app.id
      }));
  }
  render(){
    const {app, teams} = this.props;
    const {view, logo} = this.state;
    const team_app = this.state.team_app;
    const team = teams[team_app.team_id];
    const me = team.team_users[team.my_team_user_id];
    const meAdmin = isAdmin(me.role);
    const room = teams[team_app.team_id].rooms[team_app.channel_id];
    return (
        <SimpleModalTemplate
            headerContent={"App settings"}
            onClose={this.close}>
          <Container class="app_settings_modal">
            <div class="app_name_container display-flex align_items_center">
              <div class="squared_image_handler">
                <img src={logo} alt="Website logo"/>
              </div>
              <TeamAppSettingsNameInput
                  team_name={team.name}
                  room_name={room.name}
                  value={this.state.appName}
                  onChange={this.handleInput}/>
            </div>
            <AppSettingsMenu view={view} onChange={this.handleInput}/>
            {view === 'Account' &&
            <Form onSubmit={this.edit} error={!!this.state.errorMessage.length}>
              {isAdmin(me.role) &&
              <Form.Field>
                Modifications will be applied to your Team.
              </Form.Field>}
              {!isAdmin(me.role) &&
              <Message content={'This app is shared with your team, you’re not allowed to modify it.'}/>}
              <Form.Field>
                <label>Here is the link</label>
                <div class="display_flex align_items_center">
                  <LabeledInput
                      value={this.state.url}
                      disabled={!this.state.edit}
                      name="url"
                      icon="home"
                      placeholder="https://ease.space"
                      onChange={this.linkInput}/>
                  <Icon
                      name="pencil"
                      link={meAdmin}
                      disabled={!meAdmin}
                      fitted
                      style={{paddingLeft:'15px'}}
                      onClick={this.toggleEdit}/>
                </div>
              </Form.Field>
              <Message error content={this.state.errorMessage}/>
              <Button
                  type="submit"
                  loading={this.state.loading}
                  positive
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

export default TeamLinkAppSettingsModal;