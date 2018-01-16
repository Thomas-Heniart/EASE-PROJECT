import React, {Component} from "react";
import {
  DepartureDatePassedIndicator, UpdatePasswordLabel, EmptyAppIndicator, EmptyTeamAppIndicator, NewAppLabel,
  DisabledAppIndicator, WaitingTeamApproveIndicator, LoadingAppIndicator
} from "./utils";
import {showTeamEnterpriseAppSettingsModal, showLockedTeamAppModal, showUpdateAppPasswordModal} from "../../actions/modalActions";
import {Loader, Input, Label,Icon} from 'semantic-ui-react';
import {teamUserDepartureDatePassed, needPasswordUpdate} from "../../utils/utils";
import {connect} from "react-redux";
import {AppConnection} from "../../actions/dashboardActions";

@connect(store => ({
  teams: store.teams,
  team_apps: store.team_apps
}))
class TeamEnterpriseApp extends Component {
  constructor(props){
    super(props);
    this.state = {
      loading: false
    }
  }
  connect = (e) => {
    this.setState({loading: true});
    this.props.dispatch(AppConnection({
      app_id: this.props.app.id,
      keep_focus: e.ctrlKey || e.metaKey
    })).then(response => {
      this.setState({loading: false});
    }).catch(err => {
      this.setState({loading: false});
    });
  };
  connectWithPasswordUpdate = (e) => {
    this.props.dispatch(showUpdateAppPasswordModal({
      active: true,
      app: this.props.app
    }));
  };
  render(){
    const {app, teams, dispatch} = this.props;
    const team_app = this.props.team_apps[app.team_card_id];
    const team = teams[team_app.team_id];
    const me = team.team_users[team.my_team_user_id];
    const meReceiver = team_app.receivers.find(item => (item.team_user_id === me.id));
    const room = teams[team_app.team_id].rooms[team_app.channel_id];
    const password_update = !meReceiver.empty && !!team_app.password_reminder_interval && needPasswordUpdate(meReceiver.last_update_date, team_app.password_reminder_interval);

    return (
        <div class='app'>
          <div class="logo_area">
            {this.state.loading &&
            <LoadingAppIndicator/>}
            {app.new &&
            <NewAppLabel/>}
            {password_update &&
            <UpdatePasswordLabel/>}
            {teamUserDepartureDatePassed(me.departure_date) &&
            <DepartureDatePassedIndicator team_name={team.name} departure_date={me.departure_date}/>}
            {me.disabled &&
            <WaitingTeamApproveIndicator onClick={e => {dispatch(showLockedTeamAppModal({active: true, team_user_id: me.id}))}}/>}
            {!me.disabled && meReceiver.empty &&
            <EmptyTeamAppIndicator onClick={e => {dispatch(showTeamEnterpriseAppSettingsModal({active: true, app: app}))}}/>}
            <div class="logo_handler">
              <img class="logo" src={team_app.logo} onClick={password_update ? this.connectWithPasswordUpdate : this.connect}/>
              <button class="settings_button" onClick={e => {dispatch(showTeamEnterpriseAppSettingsModal({active: true, app: app}))}}>
                Settings
              </button>
            </div>
          </div>
          <span class="app_name overflow-ellipsis">{app.name}</span>
        </div>
    )
  }
}

export default TeamEnterpriseApp;