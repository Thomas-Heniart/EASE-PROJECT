import React, {Component} from "react";
import {connect} from "react-redux";
import * as api from "../../utils/api";
import {Icon} from 'semantic-ui-react';
import {withRouter} from "react-router-dom";
import extension from "../../utils/extension_api";
import {moveTeamCard} from "../../actions/teamCardActions";
import {validateApp, clickOnAppMetric, passwordCopied} from '../../actions/dashboardActions';
import {showLockedTeamAppModal, showTeamAnyEnterpriseAppSettingsModal} from "../../actions/modalActions";
import {
  DepartureDatePassedIndicator, UpdatePasswordLabel, EmptyTeamAppIndicator, NewAppLabel,
  WaitingTeamApproveIndicator, LoadingAppIndicator, SettingsMenu
} from "./utils";
import {
  teamUserDepartureDatePassed, needPasswordUpdate,
  copyTextToClipboard, transformWebsiteInfoIntoListAndSetValues
} from "../../utils/utils";

@connect(store => ({
  teams: store.teams,
  team_apps: store.team_apps,
  active: store.modals.teamAnyEnterpriseAppSettings.active
}))
class TeamAnyEnterpriseApp extends Component {
  constructor(props) {
    super(props);
    this.state = {
      loading: false,
      isOpen: false,
      copiedPassword: null,
      copiedOther: null,
      menuActive: false,
      hover: false
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
  connect = () => {
    const {app} = this.props;
    const team_app = this.props.team_apps[app.team_card_id];

    if (app.new)
      this.props.dispatch(validateApp({app_id: app.id}));
    this.props.dispatch(clickOnAppMetric({app: app}));
    window.open(team_app.website.login_url);
    extension.fillActiveTab({app_id: app.id});
  };
  handleOpenClose = () => {
    if (!this.props.active && !this.props.team_apps[this.props.app.team_card_id].empty) {
      if (this.state.isOpen === false) {
        if (this.props.app.new)
          this.props.dispatch(validateApp({app_id: this.props.app.id}));
        this.props.dispatch(clickOnAppMetric({app: this.props.app}));
        api.dashboard.getAppPassword({
          app_id: this.props.app.id
        }).then(response => {
          this.password = response.password;
        });
      }
      this.setState({isOpen: !this.state.isOpen});
    }
  };
  activateMenu = (e) => {
    e.preventDefault();
    const {app, teams} = this.props;
    const team_app = this.props.team_apps[app.team_card_id];
    const team = teams[team_app.team_id];
    const me = team.team_users[team.my_team_user_id];
    const meReceiver = team_app.receivers.find(item => (item.team_user_id === me.id));
    if (!teamUserDepartureDatePassed(me.departure_date) && !me.disabled && !meReceiver.empty) {
      this.setState({hover: true});
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
    this.setState({isOpen: false});
    // this.props.dispatch(showTeamAnyEnterpriseAppSettingsModal({active: true, app: this.props.app}));
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
    this.props.dispatch(showTeamAnyEnterpriseAppSettingsModal({active: true, app: this.props.app, remove: true}));
  };
  render() {
    const {app, teams, dispatch} = this.props;
    const team_app = this.props.team_apps[app.team_card_id];
    const team = teams[team_app.team_id];
    const me = team.team_users[team.my_team_user_id];
    const meReceiver = team_app.receivers.find(item => (item.team_user_id === me.id));
    const password_update = !meReceiver.empty && !!team_app.password_reminder_interval && needPasswordUpdate(meReceiver.last_update_date, team_app.password_reminder_interval);
    const credentials = transformWebsiteInfoIntoListAndSetValues(team_app.website.information, meReceiver.account_information);
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
          <SettingsMenu app={app}
                        buttons={buttons}
                        remove={this.remove}
                        teams={this.props.teams}
                        clickOnSettings={this.clickOnSettings}/>
          <div className="logo_handler">
            <img className="logo" src={team_app.logo} onClick={this.connect}/>
          </div>
        </div>
        <span className="app_name overflow-ellipsis">{app.name}</span>
      </div>
    )
  }
}

export default withRouter(TeamAnyEnterpriseApp);