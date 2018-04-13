import React, {Component} from "react";
import {
  DepartureDatePassedIndicator, UpdatePasswordLabel, EmptyTeamAppIndicator, NewAppLabel,
  DisabledAppIndicator, WaitingTeamApproveIndicator, LoadingAppIndicator, SettingsMenu, getPosition
} from "./utils";
import {showTeamAnySingleAppSettingsModal, showLockedTeamAppModal} from "../../actions/modalActions";
import {Icon} from 'semantic-ui-react';
import {teamUserDepartureDatePassed, needPasswordUpdate, copyTextToClipboard, transformWebsiteInfoIntoListAndSetValues} from "../../utils/utils";
import {validateApp, clickOnAppMetric, passwordCopied} from '../../actions/dashboardActions';
import extension from "../../utils/extension_api";
import {connect} from "react-redux";
import api from "../../utils/api";
import {moveTeamCard} from "../../actions/teamCardActions";
import {withRouter} from "react-router-dom";
import ReactCSSTransitionGroup from "react-addons-css-transition-group";

@connect(store => ({
  teams: store.teams,
  team_apps: store.team_apps,
  active: store.modals.teamAnySingleAppSettings.active,
  dnd: store.dashboard_dnd.dragging_app_id !== -1
}))
class TeamAnySingleApp extends Component {
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
  connect = () => {
    const {app} = this.props;
    const team_app = this.props.team_apps[app.team_card_id];

    if (app.new)
      this.props.dispatch(validateApp({app_id: app.id}));
    this.props.dispatch(clickOnAppMetric({app: app}));
    window.open(team_app.website.login_url);
    extension.fillActiveTab({app_id: app.id});
  };
  activateMenu = (e) => {
    e.preventDefault();
    const {app, teams} = this.props;
    const team_app = this.props.team_apps[app.team_card_id];
    const team = teams[team_app.team_id];
    const me = team.team_users[team.my_team_user_id];
    if (!me.disabled && !team_app.empty && team_app.team_user_filler_id !== me.id
      && !teamUserDepartureDatePassed(me.departure_date) && !this.props.dnd) {
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
    this.props.dispatch(showTeamAnySingleAppSettingsModal({active: true, app: this.props.app, remove: true}));
  };
  checkAndConnect = (e) => {
    const {app, teams, dispatch} = this.props;
    const team_app = this.props.team_apps[app.team_card_id];
    const team = teams[team_app.team_id];
    const me = team.team_users[team.my_team_user_id];

    if (!me.disabled && teamUserDepartureDatePassed(me.departure_date))
      return;
    if (!me.disabled && team_app.empty && team_app.team_user_filler_id !== me.id && !teamUserDepartureDatePassed(me.departure_date))
      return;
    if (me.disabled)
      dispatch(showLockedTeamAppModal({active: true, team_user_id: me.id}));
    else if (!me.disabled && team_app.empty && team_app.team_user_filler_id === me.id && !teamUserDepartureDatePassed(me.departure_date))
      this.clickOnSettings();
    else
      this.connect(e);
  };
  render() {
    const {app, teams, dispatch} = this.props;
    const team_app = this.props.team_apps[app.team_card_id];
    const team = teams[team_app.team_id];
    const me = team.team_users[team.my_team_user_id];
    const filler = team.team_users[team_app.team_user_filler_id];
    const password_update = !!filler && filler.id === me.id && !team_app.empty && !!team_app.password_reminder_interval && needPasswordUpdate(team_app.last_update_date, team_app.password_reminder_interval);
    const credentials = transformWebsiteInfoIntoListAndSetValues(team_app.website.information, team_app.account_information);
    const buttons = credentials.map((item,idx) => {
      if (this.state.copiedPassword !== item.priority && this.state.copiedOther !== item.priority) {
        if (item.name === 'password')
          return (
            <div className='container_button'>
              <button
                className="settings_button"
                onClick={e => this.copyPassword(item)}
                key={idx}>
                <Icon name='copy'/> • • • • • • • •
              </button>
            </div>
          );
        return (
          <div className='container_button'>
            <button
              key={idx}
              className="settings_button"
              onClick={e => this.copy(item)}>
              <Icon name='copy'/> {item.value}
            </button>
          </div>
        )
      }
      return (
        <div className='container_button'>
          <button
            key={idx}
            className="settings_button">
            Copied!
          </button>
        </div>
      )
    });
    return (
        <div className='app'>
          <div className={(me.disabled || team_app.empty || team_app.team_user_filler_id === me.id || teamUserDepartureDatePassed(me.departure_date)) ? 'logo_area'
              : this.state.menuActive ? 'logo_area active' : 'logo_area not_active'}
               onMouseEnter={!this.props.dnd ? this.activateMenu : null} onMouseLeave={!this.props.dnd ? this.deactivateMenu : null}>
            {this.state.loading &&
            <LoadingAppIndicator/>}
            {app.new &&
            <NewAppLabel/>}
            {password_update &&
            <UpdatePasswordLabel/>}
            {!me.disabled && teamUserDepartureDatePassed(me.departure_date) &&
            <DepartureDatePassedIndicator team_name={team.name} departure_date={me.departure_date}/>}
            {me.disabled &&
            <WaitingTeamApproveIndicator onClick={e => {
              dispatch(showLockedTeamAppModal({active: true, team_user_id: me.id}))
            }}/>}
            {!me.disabled && team_app.empty && team_app.team_user_filler_id === me.id && !teamUserDepartureDatePassed(me.departure_date) &&
            <EmptyTeamAppIndicator onClick={this.clickOnSettings}/>}
            {!me.disabled && team_app.empty && team_app.team_user_filler_id !== me.id && !teamUserDepartureDatePassed(me.departure_date) &&
            <DisabledAppIndicator filler_name={!!filler ? filler.username : 'Someone'} team_card_id={team_app.id}
                                  magic_link={!team_app.magic_link || team_app.magic_link === ''}/>}
            <ReactCSSTransitionGroup
              transitionName="settingsAnim"
              transitionEnter={true}
              transitionLeave={true}
              transitionEnterTimeout={1300}
              transitionLeaveTimeout={300}>
              {this.state.hover &&
            <SettingsMenu app={app}
                          teams={this.props.teams}
                          buttons={buttons}
                          remove={this.remove}
                          position={this.state.position}
                          clickOnSettings={this.clickOnSettings}/>}
            </ReactCSSTransitionGroup>
            <div className="logo_handler">
              <img className="logo" src={team_app.logo} onClick={this.connect}/>
            </div>
          </div>
          <span className="app_name overflow-ellipsis"
                onClick={this.checkAndConnect}>{app.name}</span>
        </div>
    )
  }
}

export default withRouter(TeamAnySingleApp);