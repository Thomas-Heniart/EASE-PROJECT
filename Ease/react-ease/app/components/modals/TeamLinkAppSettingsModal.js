import React, {Component} from "react";
import TeamAppSettingsNameInput from "./TeamAppSettingsNameInput";
import {handleSemanticInput} from "../../utils/utils";
import {getLogo} from "../../utils/api";
import {Image,List,Segment, Grid,Loader,Checkbox,Message, Input, Label,Form, Menu, Icon, Container, Button} from 'semantic-ui-react';
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {showTeamLinkAppSettingsModal} from "../../actions/modalActions";
import {AppSettingsMenu, ShareSection, RemoveSection, LabeledInput} from "./utils";
import {isAppInformationEmpty, transformCredentialsListIntoObject, transformWebsiteInfoIntoListAndSetValues, credentialIconType} from "../../utils/utils";
import {connect} from "react-redux";

@connect(store => ({
  app: store.modals.teamLinkAppSettings.app,
  teams: store.teams,
  team_apps: store.team_apps
}))
class TeamLinkAppSettingsModal extends Component {
  constructor(props){
    super(props);
    this.state = {
      view: 'Account',
      url: this.props.app.url,
      appName: this.props.app.name,
      logo: this.props.app.logo,
      loading: false,
      edit: false,
      errorMessage: ''
    }
  }
  handleInput = handleSemanticInput.bind(this);
  linkInput = (e, {value}) => {
    this.setState({url: value});
    getLogo({url: value}).then(response => {
      this.setState({logo:response});
    });
  };
  toggleEdit = () => {
    this.setState({edit: !this.state.edit});
  };
  close = () => {
    this.props.dispatch(showTeamLinkAppSettingsModal({active: false}));
  };
  edit = (e) => {
    e.preventDefault();
  };
  render(){
    const {app, teams} = this.props;
    const {view, logo} = this.state;
    const team_app = this.props.team_apps[app.team_card_id];
    const team = teams[team_app.team_id];
    const me = team.team_users[team.my_team_user_id];
    const room = teams[team_app.team_id].rooms[team_app.channel_id];

    return (
        <SimpleModalTemplate
          headerContent={"App settings"}
          onClose={this.close}>
          <Container class="app_settings_modal">
            <div class="display-flex align_items_center">
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
                  <Icon name="pencil" fitted link style={{paddingLeft:'15px'}} onClick={this.toggleEdit}/>
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
            <RemoveSection onRemove={this.remove}/>}
            {view === 'Share' &&
            <ShareSection/>}
          </Container>
        </SimpleModalTemplate>
    )
  }
}

export default TeamLinkAppSettingsModal;