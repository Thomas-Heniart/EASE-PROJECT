import React, {Component} from "react";
import {
  DepartureDatePassedIndicator, UpdatePasswordLabel, EmptyTeamAppIndicator, NewAppLabel,
  WaitingTeamApproveIndicator, LoadingAppIndicator, SettingsMenu, getPosition
} from "./utils";
import {
  showTeamEnterpriseAppSettingsModal,
  showLockedTeamAppModal,
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
import ReactCSSTransitionGroup from "react-addons-css-transition-group";

@connect(store => ({
  teams: store.teams,
  team_apps: store.team_apps,
  dnd: store.dashboard_dnd.dragging_app_id !== -1
}))
class TeamEnterpriseApp extends Component {
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
    this.setState({loading: true});
    this.props.dispatch(AppConnection({
      app_id: this.props.app.id,
      keep_focus: e.ctrlKey || e.metaKey
    })).then(response => {
      this.setState({loading: false});
    }).catch(err => {
      this.setState({loading: false});
    });
    this.props.dispatch(clickOnAppMetric({app: this.props.app}));
  };
  clickOnSettings = (e) => {
    e.stopPropagation();
    const {app} = this.props;
    const team_app = this.props.team_apps[app.team_card_id];
    this.props.dispatch(moveTeamCard({card_id: Number(team_app.id)}));
    this.props.history.push(`/teams/${app.team_id}/${team_app.channel_id}?app_id=${team_app.id}`);
  };
  activateMenu = (e) => {
    e.preventDefault();
    const {app, teams} = this.props;
    const team_app = this.props.team_apps[app.team_card_id];
    const team = teams[team_app.team_id];
    const me = team.team_users[team.my_team_user_id];
    const meReceiver = team_app.receivers.find(item => (item.team_user_id === me.id));
    if (!teamUserDepartureDatePassed(me.departure_date) && !me.disabled && !meReceiver.empty && !this.props.dnd) {
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
    this.props.dispatch(showTeamEnterpriseAppSettingsModal({active: true, app: this.props.app, remove: true}));
  };
  checkAndConnect = (e) => {
    const {app, teams, dispatch} = this.props;
    const team_app = this.props.team_apps[app.team_card_id];
    const team = teams[team_app.team_id];
    const me = team.team_users[team.my_team_user_id];
    const meReceiver = team_app.receivers.find(item => (item.team_user_id === me.id));
    const password_update = !meReceiver.empty && !!team_app.password_reminder_interval && needPasswordUpdate(meReceiver.last_update_date, team_app.password_reminder_interval);

    if (this.state.loading || teamUserDepartureDatePassed(me.departure_date))
      return;
    else if (me.disabled && !teamUserDepartureDatePassed(me.departure_date))
      return;
    else if (!me.disabled && meReceiver.empty && !teamUserDepartureDatePassed(me.departure_date))
      dispatch(showTeamEnterpriseAppSettingsModal({active: true, app: app}));
    else
      password_update ? this.connectWithPasswordUpdate() : this.connect(e);
  };
  render(){
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
            <div className='container_button' key={idx}>
              <button className="settings_button" onClick={e => this.copyPassword(item)}>
                <Icon name='copy'/> • • • • • • • •
              </button>
            </div>
          );
        return (
          <div className='container_button' key={idx}>
            <button className="settings_button" onClick={e => this.copy(item)}>
              <Icon name='copy'/> {item.value}
            </button>
          </div>
        )
      }
      return (
        <div className='container_button' key={idx}>
          <button className="settings_button">
            Copied!
          </button>
        </div>
      )
    });
    return (
        <div class='app'>
          <div className={(teamUserDepartureDatePassed(me.departure_date) || me.disabled || meReceiver.empty) ? 'logo_area'
              : this.state.menuActive ? 'logo_area active' : 'logo_area not_active'}
               onMouseEnter={!this.props.dnd ? this.activateMenu : null} onMouseLeave={!this.props.dnd ? this.deactivateMenu : null}>
            {this.state.loading &&
            <LoadingAppIndicator/>}
            {app.new &&
            <NewAppLabel/>}
            {password_update &&
            <UpdatePasswordLabel/>}
            {teamUserDepartureDatePassed(me.departure_date) &&
            <DepartureDatePassedIndicator team_name={team.name} departure_date={me.departure_date}/>}
            {me.disabled && !teamUserDepartureDatePassed(me.departure_date) &&
            <WaitingTeamApproveIndicator onClick={e => {dispatch(showLockedTeamAppModal({active: true, team_user_id: me.id}))}}/>}
            {!me.disabled && meReceiver.empty && !teamUserDepartureDatePassed(me.departure_date) &&
            <EmptyTeamAppIndicator onClick={e => {dispatch(showTeamEnterpriseAppSettingsModal({active: true, app: app}))}}/>}
            <ReactCSSTransitionGroup
              transitionName="settingsAnim"
              transitionEnter={true}
              transitionLeave={true}
              transitionEnterTimeout={1300}
              transitionLeaveTimeout={1}>
              {this.state.hover && !this.props.dnd &&
            <SettingsMenu app={app}
                          buttons={buttons}
                          remove={this.remove}
                          teams={this.props.teams}
                          position={this.state.position}
                          clickOnSettings={this.clickOnSettings}/>}
            </ReactCSSTransitionGroup>
            <div class="logo_handler">
              <img class="logo" src={team_app.logo} onClick={password_update ? this.connectWithPasswordUpdate : this.connect}/>
            </div>
          </div>
          <span class="app_name overflow-ellipsis"
                onClick={this.checkAndConnect}>{app.name}</span>
        </div>
    )
  }
}

export default withRouter(TeamEnterpriseApp);