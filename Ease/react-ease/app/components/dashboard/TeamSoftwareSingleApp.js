import React, {Component} from "react";
import {
  DepartureDatePassedIndicator, UpdatePasswordLabel, EmptyTeamAppIndicator, NewAppLabel,
  DisabledAppIndicator, WaitingTeamApproveIndicator, LoadingAppIndicator
} from "./utils";
import {showTeamSoftwareSingleAppSettingsModal, showLockedTeamAppModal} from "../../actions/modalActions";
import {Popup, Input, Label, Icon, Segment} from 'semantic-ui-react';
import {teamUserDepartureDatePassed, needPasswordUpdate, copyTextToClipboard, transformWebsiteInfoIntoListAndSetValues} from "../../utils/utils";
import {validateApp, clickOnAppMetric} from '../../actions/dashboardActions';
import {connect} from "react-redux";
import api from "../../utils/api";

@connect(store => ({
  teams: store.teams,
  team_apps: store.team_apps,
  active: store.modals.teamSoftwareSingleAppSettings.active
}))
class TeamSoftwareSingleApp extends Component {
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
    this.props.dispatch(showTeamSoftwareSingleAppSettingsModal({active: true, app: this.props.app}));
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
    const filler = team.team_users[team_app.team_user_filler_id];
    const password_update = !!filler && filler.id === me.id && !team_app.empty && !!team_app.password_reminder_interval && needPasswordUpdate(team_app.last_update_date, team_app.password_reminder_interval);
    const credentials = transformWebsiteInfoIntoListAndSetValues(team_app.software.connection_information, team_app.account_information);
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
        open={this.props.active || teamUserDepartureDatePassed(me.departure_date) ? false : this.state.isOpen}
        onClose={this.handleOpenClose}
        onOpen={!teamUserDepartureDatePassed(me.departure_date) ? this.handleOpenClose : null}
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
              {!me.disabled && teamUserDepartureDatePassed(me.departure_date) &&
              <DepartureDatePassedIndicator team_name={team.name} departure_date={me.departure_date}/>}
              {me.disabled &&
              <WaitingTeamApproveIndicator onClick={e => {
                dispatch(showLockedTeamAppModal({active: true, team_user_id: me.id}))
              }}/>}
              {!me.disabled && team_app.empty && team_app.team_user_filler_id === me.id && !teamUserDepartureDatePassed(me.departure_date) &&
              <EmptyTeamAppIndicator onClick={this.clickOnSettings}/>}
              {!me.disabled && team_app.empty && team_app.team_user_filler_id !== me.id && !teamUserDepartureDatePassed(me.departure_date) &&
              <DisabledAppIndicator filler_name={!!filler ? filler.username : 'Someone'} team_card_id={team_app.id} magic_link={!team_app.magic_link || team_app.magic_link === ''}/>}
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
        content={inputs}/>
    )
  }
}

export default TeamSoftwareSingleApp;