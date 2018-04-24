import React, {Component} from "react";
import {
  DepartureDatePassedIndicator, UpdatePasswordLabel, EmptyTeamAppIndicator, NewAppLabel,
  DisabledAppIndicator, WaitingTeamApproveIndicator, LoadingAppIndicator, SettingsMenu, getPosition,
  UpdatePasswordIndicator
} from "./utils";
import {
  showTeamSingleAppSettingsModal, showLockedTeamAppModal,
  showUpdateAppPasswordModal
} from "../../actions/modalActions";
import {
  teamUserDepartureDatePassed,
  needPasswordUpdate,
  transformWebsiteInfoIntoListAndSetValues, copyTextToClipboard
} from "../../utils/utils";
import {Icon} from 'semantic-ui-react';
import {connect} from "react-redux";
import {AppConnection, clickOnAppMetric, passwordCopied} from "../../actions/dashboardActions";
import api from "../../utils/api";
import {moveTeamCard} from "../../actions/teamCardActions";
import {withRouter} from "react-router-dom";

@connect(store => ({
  teams: store.teams,
  team_apps: store.team_apps
}))
class TeamSingleApp extends Component {
  constructor(props){
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
  connect = (e) => {
    const {app, teams, dispatch} = this.props;
    const team_app = this.props.team_apps[app.team_card_id];
    const team = teams[team_app.team_id];
    const me = team.team_users[team.my_team_user_id];
    const room = team.rooms[team_app.channel_id];
    const roomManager = team.team_users[room.room_manager_id];
    const password_update = !!roomManager && roomManager.id === me.id && !team_app.empty && !!team_app.password_reminder_interval && needPasswordUpdate(team_app.last_update_date, team_app.password_reminder_interval);

    this.setState({loading: true});
    this.props.dispatch(AppConnection({
      app_id: this.props.app.id,
      keep_focus: e.ctrlKey || e.metaKey,
      passwordChangeReminder: password_update,
      appName: team_app.name,
      login: team_app.account_information.login
    })).then(response => {
      this.setState({loading: false});
    }).catch(err => {
      this.setState({loading: false});
    });
    this.props.dispatch(clickOnAppMetric({app: app}));
  };
  activateMenu = (e) => {
    e.preventDefault();
    const {app, teams} = this.props;
    const team_app = this.props.team_apps[app.team_card_id];
    const team = teams[team_app.team_id];
    const me = team.team_users[team.my_team_user_id];
    if (!me.disabled && !team_app.empty && team_app.team_user_filler_id !== me.id && !teamUserDepartureDatePassed(me.departure_date)) {
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
    this.setState({isOpen: false});
    // this.props.dispatch(showTeamSingleAppSettingsModal({active: true, app: app}));
    this.props.dispatch(moveTeamCard({card_id: Number(team_app.id)}));
    this.props.history.push(`/teams/${app.team_id}/${team_app.channel_id}?app_id=${team_app.id}`);
  };
  connectWithPasswordUpdate = (e) => {
    this.props.dispatch(showUpdateAppPasswordModal({
      active: true,
      app: this.props.app
    }));
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
    this.props.dispatch(showTeamSingleAppSettingsModal({active: true, app: this.props.app, remove: true}));
  };
  checkAndConnect = (e) => {
    const {app, teams, dispatch} = this.props;
    const team_app = this.props.team_apps[app.team_card_id];
    const team = teams[team_app.team_id];
    const me = team.team_users[team.my_team_user_id];

    if (this.state.loading || teamUserDepartureDatePassed(me.departure_date))
      return;
    else if (me.disabled)
      dispatch(showLockedTeamAppModal({active: true, team_user_id: me.id}));
    else if (!me.disabled && team_app.empty && team_app.team_user_filler_id === me.id && !teamUserDepartureDatePassed(me.departure_date))
      dispatch(showTeamSingleAppSettingsModal({active: true, app: app}));
    else if (!me.disabled && team_app.empty && team_app.team_user_filler_id !== me.id && !teamUserDepartureDatePassed(me.departure_date))
      return;
    else
      this.connect(e);
  };
  render(){
    const {app, teams, dispatch} = this.props;
    const team_app = this.props.team_apps[app.team_card_id];
    const team = teams[team_app.team_id];
    const me = team.team_users[team.my_team_user_id];
    const filler = team.team_users[team_app.team_user_filler_id];
    const room = team.rooms[team_app.channel_id];
    const roomManager = team.team_users[room.room_manager_id];
    const password_update = !!roomManager && roomManager.id === me.id && !team_app.empty && !!team_app.password_reminder_interval && needPasswordUpdate(team_app.last_update_date, team_app.password_reminder_interval);
    const credentials = transformWebsiteInfoIntoListAndSetValues(team_app.website.information, team_app.account_information);
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
        <div class='app classic'>
          <div className={(me.disabled || team_app.empty || team_app.team_user_filler_id === me.id || teamUserDepartureDatePassed(me.departure_date)) ? 'logo_area'
              : this.state.menuActive ? 'logo_area active' : 'logo_area not_active'}
               onMouseEnter={this.activateMenu} onMouseLeave={this.deactivateMenu}>
            {this.state.loading &&
            <LoadingAppIndicator/>}
            {app.new &&
            <NewAppLabel/>}
            {password_update &&
            <UpdatePasswordIndicator onClick={this.connect}/>}
            {!me.disabled && teamUserDepartureDatePassed(me.departure_date) &&
            <DepartureDatePassedIndicator team_name={team.name} departure_date={me.departure_date}/>}
            {me.disabled &&
            <WaitingTeamApproveIndicator onClick={e => {dispatch(showLockedTeamAppModal({active: true, team_user_id: me.id}))}}/>}
            {!me.disabled && team_app.empty && team_app.team_user_filler_id === me.id && !teamUserDepartureDatePassed(me.departure_date) &&
            <EmptyTeamAppIndicator onClick={e => {dispatch(showTeamSingleAppSettingsModal({active: true, app: app}))}}/>}
            {!me.disabled && team_app.empty && team_app.team_user_filler_id !== me.id && !teamUserDepartureDatePassed(me.departure_date) &&
            <DisabledAppIndicator filler_name={!!filler ? filler.username : 'Someone'} team_card_id={team_app.id} magic_link={!team_app.magic_link || team_app.magic_link === ''}/>}
            <SettingsMenu app={app}
                          buttons={buttons}
                          teams={this.props.teams}
                          remove={this.remove}
                          position={this.state.position}
                          clickOnSettings={this.clickOnSettings}/>
            <div class="logo_handler">
              <img class="logo" src={team_app.logo} onClick={this.connect}/>
            </div>
          </div>
          <span class="app_name overflow-ellipsis"
                onClick={this.checkAndConnect}>{app.name}</span>
        </div>
    )
  }
}

export default withRouter(TeamSingleApp);