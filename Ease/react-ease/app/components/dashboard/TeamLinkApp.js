import React, {Component} from "react";
import {DepartureDatePassedIndicator, EmptyAppIndicator, EmptyTeamAppIndicator, NewAppLabel, DisabledAppIndicator, WaitingTeamApproveIndicator} from "./utils";
import {showTeamLinkAppSettingsModal, showLockedTeamAppModal} from "../../actions/modalActions";
import {Loader, Input, Label,Icon} from 'semantic-ui-react';
import {teamUserDepartureDatePassed} from "../../utils/utils";
import {connect} from "react-redux";
import {validateApp,clickOnAppMetric} from "../../actions/dashboardActions";

@connect(store => ({
  teams: store.teams,
  team_apps: store.team_apps
}))
class TeamLinkApp extends Component {
  constructor(props){
    super(props);
  }
  process = () => {
    const {app} = this.props;
    const team_app = this.props.team_apps[app.team_card_id];
    if (app.new)
      this.props.dispatch(validateApp({
        app_id: this.props.app.id
      }));
    this.props.dispatch(clickOnAppMetric({app_id: this.props.app.id}));
    window.open(team_app.url, '_blank');
  };
  render(){
    const {app, teams, dispatch} = this.props;
    const team_app = this.props.team_apps[app.team_card_id];
    const team = teams[team_app.team_id];
    const me = team.team_users[team.my_team_user_id];
    const meReceiver = team_app.receivers.find(item => (item.team_user_id === me.id));
    const room = teams[team_app.team_id].rooms[team_app.channel_id];

    return (
        <div class='app'>
          <div class="logo_area">
            {app.new &&
            <NewAppLabel/>}
            {teamUserDepartureDatePassed(me.departure_date) &&
            <DepartureDatePassedIndicator team_name={team.name} departure_date={me.departure_date}/>}
            {me.disabled &&
            <WaitingTeamApproveIndicator onClick={e => {dispatch(showLockedTeamAppModal({active: true, team_user_id: me.id}))}}/>}
            <div class="logo_handler">
              <img class="logo" src={team_app.logo} onClick={this.process}/>
              <button class="settings_button" onClick={e => {dispatch(showTeamLinkAppSettingsModal({active: true, app: app}))}}>
                Settings
              </button>
            </div>
          </div>
          <span class="app_name overflow-ellipsis">{app.name}</span>
        </div>
    )
  }
}

export default TeamLinkApp;