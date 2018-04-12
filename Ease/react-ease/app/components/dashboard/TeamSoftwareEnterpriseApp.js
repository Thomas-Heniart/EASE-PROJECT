import React, {Component} from "react";
import {
  DepartureDatePassedIndicator, UpdatePasswordLabel, EmptyTeamAppIndicator, NewAppLabel,
  WaitingTeamApproveIndicator, LoadingAppIndicator, SettingsMenu, getPosition
} from "./utils";
import {showLockedTeamAppModal, showTeamSoftwareEnterpriseAppSettingsModal} from "../../actions/modalActions";
import {Icon} from 'semantic-ui-react';
import {teamUserDepartureDatePassed, needPasswordUpdate, copyTextToClipboard, transformWebsiteInfoIntoListAndSetValues} from "../../utils/utils";
import {validateApp, clickOnAppMetric, passwordCopied} from '../../actions/dashboardActions';
import api from "../../utils/api";
import {connect} from "react-redux";
import {moveTeamCard} from "../../actions/teamCardActions";
import {withRouter} from "react-router-dom";
import ReactCSSTransitionGroup from "react-addons-css-transition-group";

@connect(store => ({
  teams: store.teams,
  team_apps: store.team_apps,
  active: store.modals.teamSoftwareEnterpriseAppSettings.active
}))
class TeamSoftwareEnterpriseApp extends Component {
  constructor(props) {
    super(props);
    this.state = {
      loading: false,
      copiedPassword: null,
      copiedOther: null,
      menuActive: false,
      hover: false,
      position: 'left'
    };
    this.password = '';
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
  activateMenu = (e) => {
    e.preventDefault();
    const {app, teams} = this.props;
    const team_app = this.props.team_apps[app.team_card_id];
    const team = teams[team_app.team_id];
    const me = team.team_users[team.my_team_user_id];
    const meReceiver = team_app.receivers.find(item => (item.team_user_id === me.id));
    if (!teamUserDepartureDatePassed(me.departure_date) && !me.disabled && !meReceiver.empty) {
      this.setState({hover: true, position: getPosition(app.id)});
      if (this.password === '')
        api.dashboard.getAppPassword({
          app_id: this.props.app.id
        }).then(response => {
          this.password = response.password;
        });
    }
  };
  deactivateMenu = () => {
    this.setState({menuActive: false, hover: false});
  };
  clickOnSettings = (e) => {
    e.stopPropagation();
    const {app} = this.props;
    const team_app = this.props.team_apps[app.team_card_id];
    this.props.dispatch(moveTeamCard({card_id: Number(team_app.id)}));
    this.props.history.push(`/teams/${app.team_id}/${team_app.channel_id}?app_id=${team_app.id}`);
  };
  copyPassword = (item) => {
    copyTextToClipboard(this.password);
    setTimeout(() => {
      this.setState({copiedPassword: item.priority});
    }, 1);
    setTimeout(() => {
      this.setState({copiedPassword: null});
    }, 1000);
    this.props.dispatch(passwordCopied({
      app: this.props.app
    }))
  };
  copy = (item) => {
    copyTextToClipboard(item.value);
    setTimeout(() => {
      this.setState({copiedOther: item.priority});
    }, 1);
    setTimeout(() => {
      this.setState({copiedOther: null});
    }, 1000);
  };
  remove = () => {
    this.props.dispatch(showTeamSoftwareEnterpriseAppSettingsModal({active: true, app: this.props.app, remove: true}));
  };
  render() {
    const {app, teams, dispatch} = this.props;
    const team_app = this.props.team_apps[app.team_card_id];
    const team = teams[team_app.team_id];
    const me = team.team_users[team.my_team_user_id];
    const meReceiver = team_app.receivers.find(item => (item.team_user_id === me.id));
    const password_update = !meReceiver.empty && !!team_app.password_reminder_interval && needPasswordUpdate(meReceiver.last_update_date, team_app.password_reminder_interval);
    const credentials = transformWebsiteInfoIntoListAndSetValues(team_app.software.connection_information, meReceiver.account_information);
    const buttons = credentials.map((item,idx) => {
      if (this.state.copiedPassword !== item.priority && this.state.copiedOther !== item.priority) {
        if (item.name === 'password')
          return (
            <button
              className="settings_button"
              onClick={e => this.copyPassword(item)}
              key={idx}>
              <Icon name='copy'/> • • • • • • • •
            </button>
          );
        return (
          <button
            key={idx}
            className="settings_button"
            onClick={e => this.copy(item)}>
            <Icon name='copy'/> {item.value}
          </button>
        )
      }
      return (
        <button
          key={idx}
          className="settings_button">
          Copied!
        </button>
      )
    });
    return (
      <div className='app'>
        <div className={(teamUserDepartureDatePassed(me.departure_date) || me.disabled || meReceiver.empty) ? 'logo_area'
          : this.state.menuActive ? 'logo_area active' : 'logo_area not_active'}
             onMouseEnter={this.activateMenu} onMouseLeave={this.deactivateMenu}>
          {this.state.loading &&
          <LoadingAppIndicator/>}
          {app.new &&
          <NewAppLabel/>}
          {password_update &&
          <UpdatePasswordLabel/>}
          {teamUserDepartureDatePassed(me.departure_date) &&
          <DepartureDatePassedIndicator team_name={team.name} departure_date={me.departure_date}/>}
          {me.disabled && !teamUserDepartureDatePassed(me.departure_date) &&
          <WaitingTeamApproveIndicator onClick={e => {
            dispatch(showLockedTeamAppModal({active: true, team_user_id: me.id}))
          }}/>}
          {!me.disabled && meReceiver.empty && !teamUserDepartureDatePassed(me.departure_date) &&
          <EmptyTeamAppIndicator onClick={this.clickOnSettings}/>}
          <ReactCSSTransitionGroup
            transitionName="settingsAnim"
            transitionEnter={true}
            transitionLeave={true}
            transitionEnterTimeout={1300}
            transitionLeaveTimeout={300}>
            {this.state.hover &&
          <SettingsMenu app={app}
                        buttons={buttons}
                        remove={this.remove}
                        teams={this.props.teams}
                        position={this.state.position}
                        clickOnSettings={this.clickOnSettings}/>}
          </ReactCSSTransitionGroup>
          <div className="logo_handler">
            <img className="logo" src={team_app.logo}/>
          </div>
        </div>
        <span className="app_name overflow-ellipsis">{app.name}</span>
      </div>
    )
  }
}

export default withRouter(TeamSoftwareEnterpriseApp);