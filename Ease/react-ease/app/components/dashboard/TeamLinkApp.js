import React, {Component} from "react";
import {
  DepartureDatePassedIndicator,
  getPosition,
  NewAppLabel,
  SettingsMenu,
  WaitingTeamApproveIndicator
} from "./utils";
import {showTeamLinkAppSettingsModal, showLockedTeamAppModal} from "../../actions/modalActions";
import {teamUserDepartureDatePassed} from "../../utils/utils";
import {connect} from "react-redux";
import {validateApp,clickOnAppMetric} from "../../actions/dashboardActions";
import {moveTeamCard} from "../../actions/teamCardActions";
import {withRouter} from "react-router-dom";

@connect(store => ({
  teams: store.teams,
  team_apps: store.team_apps
}))
class TeamLinkApp extends Component {
  constructor(props){
    super(props);
    this.state = {
      menuActive: false,
      hover: false,
      position: 'left'
    }
  }
  componentDidMount() {
    document.addEventListener('contextmenu', this._handleContextMenu);
  };
  componentWillUnmount() {
    document.removeEventListener('contextmenu', this._handleContextMenu);
  }
  _handleContextMenu = (event) => {
    event.preventDefault();
    if (this.state.hover)
      this.setState({ menuActive: true });
  };
  clickOnSettings = (e) => {
    e.stopPropagation();
    const {app} = this.props;
    const team_app = this.props.team_apps[app.team_card_id];
    this.setState({isOpen: false});
    this.props.dispatch(moveTeamCard({card_id: Number(team_app.id)}));
    this.props.history.push(`/teams/${app.team_id}/${team_app.channel_id}?app_id=${team_app.id}`);
  };
  process = () => {
    const {app} = this.props;
    const team_app = this.props.team_apps[app.team_card_id];
    if (app.new)
      this.props.dispatch(validateApp({
        app_id: this.props.app.id
      }));
    this.props.dispatch(clickOnAppMetric({app: this.props.app}));
    window.open(team_app.url, '_blank');
  };
  activateMenu = () => {
    this.setState({hover: true, position: getPosition(this.props.app.id)});
  };
  deactivateMenu = () => {
    this.setState({menuActive: false, hover: false});
  };
  remove = () => {
    this.props.dispatch(showTeamLinkAppSettingsModal({active: true, app: this.props.app, remove: true}));
  };
  render(){
    const {app, teams, dispatch} = this.props;
    const team_app = this.props.team_apps[app.team_card_id];
    const team = teams[team_app.team_id];
    const me = team.team_users[team.my_team_user_id];
    return (
        <div class='app'>
          <div className={(me.disabled || teamUserDepartureDatePassed(me.departure_date)) ? 'logo_area'
            : this.state.menuActive ? 'logo_area active' : 'logo_area not_active'}
               onMouseLeave={this.deactivateMenu} onMouseEnter={this.activateMenu}>
            {app.new &&
            <NewAppLabel/>}
            {teamUserDepartureDatePassed(me.departure_date) &&
            <DepartureDatePassedIndicator team_name={team.name} departure_date={me.departure_date}/>}
            {me.disabled && !teamUserDepartureDatePassed(me.departure_date) &&
            <WaitingTeamApproveIndicator onClick={e => {dispatch(showLockedTeamAppModal({active: true, team_user_id: me.id}))}}/>}
            <SettingsMenu app={app}
                          teams={this.props.teams}
                          remove={this.remove}
                          position={this.state.position}
                          clickOnSettings={this.clickOnSettings}/>
            <div class="logo_handler">
              <img class="logo" src={team_app.logo} onClick={this.process}/>
            </div>
          </div>
          <span class="app_name overflow-ellipsis">{app.name}</span>
        </div>
    )
  }
}

export default withRouter(TeamLinkApp);