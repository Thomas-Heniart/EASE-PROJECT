import React, {Component} from "react";
import {
  DepartureDatePassedIndicator, UpdatePasswordLabel, EmptyAppIndicator, EmptyTeamAppIndicator, NewAppLabel,
  DisabledAppIndicator, WaitingTeamApproveIndicator, LoadingAppIndicator
} from "./utils";
import {showLockedTeamAppModal, showUpdateAppPasswordModal, showTeamAnyEnterpriseAppSettingsModal} from "../../actions/modalActions";
import {Input, Label, Icon, Segment, Popup} from 'semantic-ui-react';
import {teamUserDepartureDatePassed, needPasswordUpdate, copyTextToClipboard, transformWebsiteInfoIntoListAndSetValues} from "../../utils/utils";
import {validateApp, clickOnAppMetric} from '../../actions/dashboardActions';
import api from "../../utils/api";
import {connect} from "react-redux";

@connect(store => ({
  teams: store.teams,
  team_apps: store.team_apps,
  active: store.modals.teamAnyEnterpriseAppSettings.active
}))
class TeamAnyEnterpriseApp extends Component {
  constructor(props){
    super(props);
    this.state = {
      loading: false,
      isOpen: false,
      copiedPassword: null,
      copiedOther: null
    };
    this.password = '';
  }
  handleOpenClose = () => {
    if (!this.props.active && !this.props.team_apps[this.props.app.team_card_id].empty) {
      if (this.state.isOpen === false) {
        if (this.props.app.new)
          this.props.dispatch(validateApp({app_id: this.props.app.id}));
        this.props.dispatch(clickOnAppMetric({app_id: this.props.app.id}));
        api.dashboard.getAppPassword({
          app_id: this.props.app.id
        }).then(response => {
          this.password = response.password;
        });
      }
      this.setState({isOpen: !this.state.isOpen});
    }
  };
  clickOnSettings = (e) => {
    e.stopPropagation();
    this.setState({isOpen: false});
    this.props.dispatch(showTeamAnyEnterpriseAppSettingsModal({active: true, app: this.props.app}));
  };
  copyPassword = (item) => {
    copyTextToClipboard(this.password);
    setTimeout(() => {
      this.setState({copiedPassword: item.priority});
    }, 1);
    setTimeout(() => {
      this.setState({copiedPassword: null});
    }, 1000);
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
  render(){
    const {app, teams, dispatch} = this.props;
    const team_app = this.props.team_apps[app.team_card_id];
    const team = teams[team_app.team_id];
    const me = team.team_users[team.my_team_user_id];
    const meReceiver = team_app.receivers.find(item => (item.team_user_id === me.id));
    const room = teams[team_app.team_id].rooms[team_app.channel_id];
    const password_update = !meReceiver.empty && !!team_app.password_reminder_interval && needPasswordUpdate(meReceiver.last_update_date, team_app.password_reminder_interval);
    const credentials = transformWebsiteInfoIntoListAndSetValues(team_app.website.information, meReceiver.account_information);
    const inputs = credentials.map((item,idx) => {
      if (this.state.copiedPassword !== item.priority && this.state.copiedOther !== item.priority) {
        if (item.name === 'password')
          return (
            <Input
              key={idx}
              disabled
              size='mini'
              type='password'
              name={item.name}
              placeholder='Password'
              value={'********'}
              label={
                <Label style={{backgroundColor: '#373b60', color: 'white', fontWeight: '300'}}
                       onClick={e => this.copyPassword(item)}>
                  Copy <Icon name='copy'/>
                </Label>}/>
          );
        return (
          <Input
            key={idx}
            disabled
            size='mini'
            type={item.type}
            name={item.name}
            placeholder={item.placeholder}
            value={item.value}
            label={
              <Label style={{backgroundColor: '#373b60', color: 'white', fontWeight: '300'}}
                     onClick={e => this.copy(item)}>
                Copy <Icon name='copy'/>
              </Label>}/>
        )
      }
      return (
        <Segment
          key={idx}
          size='mini'
          content={'Copied!'}/>
      )
    });
    return (
      <Popup
        size="tiny"
        className='dashboard_popup_soft_and_any'
        position="top center"
        on='click'
        open={this.props.active ? false : this.state.isOpen}
        onClose={this.handleOpenClose}
        onOpen={this.handleOpenClose}
        hideOnScroll
        trigger={
          <div className='app'>
            <div className="logo_area">
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
              <div className="logo_handler">
                <img className="logo" src={team_app.logo}/>
                <button className="settings_button" onClick={this.clickOnSettings}>
                  Settings
                </button>
              </div>
            </div>
            <span className="app_name overflow-ellipsis">{app.name}</span>
          </div>
        }
        content={
          <div>
            {inputs}
            <Input
              size='mini'
              placeholder='URL'
              disabled
              value={team_app.website.landing_url}
              label={
                <Label style={{backgroundColor:'#373b60',color:'white',fontWeight:'300'}}
                       onClick={e => window.open(team_app.website.landing_url)}>
                  Go to <Icon name='external'/>
                </Label>}/>
          </div>
        }/>
    )
  }
}

export default TeamAnyEnterpriseApp;