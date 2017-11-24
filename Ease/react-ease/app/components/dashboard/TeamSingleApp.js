import React, {Component} from "react";
import {EmptyAppIndicator, EmptyTeamAppIndicator, NewAppLabel, DisabledAppIndicator, WaitingTeamApproveIndicator} from "./utils";
import {showTeamSingleAppSettingsModal, showLockedTeamAppModal} from "../../actions/modalActions";
import {Loader, Input, Label,Icon} from 'semantic-ui-react';
import {teamUserDepartureDatePassed} from "../../utils/utils";
import {connect} from "react-redux";

@connect(store => ({
  teams: store.teams,
  team_apps: store.team_apps
}))
class TeamSingleApp extends Component {
  constructor(props){
    super(props);
  }
  render(){
    const {app, teams, dispatch} = this.props;
    const team_app = this.props.team_apps[app.team_card_id];
    const team = teams[team_app.team_id];
    const me = team.team_users[team.my_team_user_id];
    const filler = team.team_users[team_app.team_user_filler_id];
    const meReceiver = team_app.receivers.find(item => (item.team_user_id === me.id));
    const room = teams[team_app.team_id].rooms[team_app.channel_id];

    return (
        <div class='app'>
          <div class="logo_area">
            <NewAppLabel/>
            {(meReceiver.disabled || teamUserDepartureDatePassed(meReceiver.departure_date)) &&
            <WaitingTeamApproveIndicator onClick={e => {dispatch(showLockedTeamAppModal({active: true}))}}/>}
            {team_app.empty && team_app.team_user_filler_id === meReceiver.id &&
            <EmptyTeamAppIndicator onClick={e => {dispatch(showTeamSingleAppSettingsModal({active: true, app: app}))}}/>}
            {team_app.empty && team_app.team_user_filler_id !== meReceiver.id &&
            <DisabledAppIndicator filler_name={filler.username}/>}
            <div class="logo_handler">
              <img class="logo" src={team_app.logo}/>
              <button class="settings_button" onClick={e => {dispatch(showTeamSingleAppSettingsModal({active: true, app: app}))}}>
                Settings
              </button>
            </div>
          </div>
          <span class="app_name overflow-ellipsis">{app.name}</span>
        </div>
    )
  }
}

export default TeamSingleApp;