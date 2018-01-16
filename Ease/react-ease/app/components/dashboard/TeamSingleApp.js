import React, {Component} from "react";
import {
  DepartureDatePassedIndicator, UpdatePasswordLabel, EmptyAppIndicator, EmptyTeamAppIndicator, NewAppLabel,
  DisabledAppIndicator, WaitingTeamApproveIndicator, LoadingAppIndicator
} from "./utils";
import {
  showTeamSingleAppSettingsModal, showLockedTeamAppModal,
  showUpdateAppPasswordModal
} from "../../actions/modalActions";
import {Loader, Input, Label,Icon} from 'semantic-ui-react';
import {teamUserDepartureDatePassed, needPasswordUpdate} from "../../utils/utils";
import {connect} from "react-redux";
import {AppConnection} from "../../actions/dashboardActions";

@connect(store => ({
  teams: store.teams,
  team_apps: store.team_apps
}))
class TeamSingleApp extends Component {
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
    const filler = team.team_users[team_app.team_user_filler_id];
    const meReceiver = team_app.receivers.find(item => (item.team_user_id === me.id));
    const room = team.rooms[team_app.channel_id];
    const roomManager = team.team_users[room.room_manager_id];
    const password_update = !!roomManager && roomManager.id === me.id && !team_app.empty && !!team_app.password_reminder_interval && needPasswordUpdate(team_app.last_update_date, team_app.password_reminder_interval);

    return (
<<<<<<< HEAD
        <div class='app'>
          <div class="logo_area">
            {this.state.loading &&
            <LoadingAppIndicator/>}
            {app.new &&
            <NewAppLabel/>}
            {password_update &&
            <UpdatePasswordLabel/>}
            {!me.disabled && teamUserDepartureDatePassed(me.departure_date) &&
            <DepartureDatePassedIndicator team_name={team.name} departure_date={me.departure_date}/>}
            {me.disabled &&
            <WaitingTeamApproveIndicator onClick={e => {dispatch(showLockedTeamAppModal({active: true, team_user_id: me.id}))}}/>}
            {!me.disabled && team_app.empty && team_app.team_user_filler_id === me.id &&
            <EmptyTeamAppIndicator onClick={e => {dispatch(showTeamSingleAppSettingsModal({active: true, app: app}))}}/>}
            {!me.disabled && team_app.empty && team_app.team_user_filler_id !== me.id &&
            <DisabledAppIndicator filler_name={!!filler ? filler.username : 'Someone'} team_card_id={team_app.id}/>}
            <div class="logo_handler">
              <img class="logo" src={team_app.logo} onClick={password_update ? this.connectWithPasswordUpdate : this.connect}/>
              <button class="settings_button" onClick={e => {dispatch(showTeamSingleAppSettingsModal({active: true, app: app}))}}>
                Settings
              </button>
            </div>
=======
      <div class='app'>
        <div class="logo_area">
          {this.state.loading &&
          <LoadingAppIndicator/>}
          {app.new &&
          <NewAppLabel/>}
          {password_update &&
          <UpdatePasswordLabel/>}
          {!me.disabled && teamUserDepartureDatePassed(me.departure_date) &&
          <DepartureDatePassedIndicator team_name={team.name} departure_date={me.departure_date}/>}
          {me.disabled &&
          <WaitingTeamApproveIndicator onClick={e => {dispatch(showLockedTeamAppModal({active: true, team_user_id: me.id}))}}/>}
          {!me.disabled && team_app.empty && team_app.team_user_filler_id === me.id &&
          <EmptyTeamAppIndicator onClick={e => {dispatch(showTeamSingleAppSettingsModal({active: true, app: app}))}}/>}
          {!me.disabled && team_app.empty && team_app.team_user_filler_id !== me.id &&
          <DisabledAppIndicator filler_name={!!filler ? filler.username : 'Someone'} team_card_id={team_app.id}/>}
          <div class="logo_handler">
            <img class="logo" src={team_app.logo} onClick={this.connect}/>
            <button class="settings_button" onClick={e => {dispatch(showTeamSingleAppSettingsModal({active: true, app: app}))}}>
              Settings
            </button>
>>>>>>> dashboard
          </div>
        </div>
        <span class="app_name overflow-ellipsis">{app.name}</span>
      </div>
    )
  }
}

export default TeamSingleApp;