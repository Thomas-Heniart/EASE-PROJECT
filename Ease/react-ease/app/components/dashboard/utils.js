import React, {Component} from "react";
import api from "../../utils/api";
import post_api from "../../utils/post_api";
import {copyTextToClipboard, basicDateFormat} from "../../utils/utils";
import {Label,Icon, Popup} from 'semantic-ui-react';
import {testCredentials} from "../../actions/catalogActions";
import {passwordCopied} from "../../actions/dashboardActions";
import {connect} from "react-redux";

export const EmptyAppIndicator = (props) => {
  return (
      <div class="app_overlay_indicator" {...props}>
        <Icon name="exclamation triangle" fitted/>
      </div>
  )
};

export const LoadingAppIndicator = (props) => {
  return (
      <div class="app_overlay_indicator white" style={{zIndex: '2'}}>
      </div>
  )
};

export const WaitingTeamApproveIndicator = (props) => {
  return (
      <div class="app_overlay_indicator" {...props}>
        <Icon name="hourglass half" fitted/>
      </div>
  )
};

export const UpdatePasswordIndicator = (props) => {
  return (
      <div class="app_overlay_indicator red" {...props}>
        <Icon name="unlock alternate" fitted/>
      </div>
  )
};

export const DepartureDatePassedIndicator = ({team_name, departure_date}) => {
  return (
      <Popup size="mini"
             position="top center"
             inverted
             trigger={
               <div class="app_overlay_indicator grey"/>
             }
             content={`Your departure date from ${team_name} has passed on ${basicDateFormat(departure_date)}.`}
      />
  )
};

export const EmptyTeamAppIndicator = (props) => {
  return (
      <div class="app_overlay_indicator red" {...props}>
      </div>
  )
};

export class DisabledAppIndicator extends Component {
  constructor(props){
    super(props);
    this.state = {
      buttonText: 'Send reminder'
    }
  }
  send = () => {
    post_api.teamApps.sendSingleCardFillerReminder({
      team_card_id: this.props.team_card_id
    }).then(response => {
      this.setState({buttonText: 'Reminder sent!'});
      setTimeout(() => {
        this.setState({buttonText: 'Send reminder'});
      }, 2000);
    }).catch(err => {
      this.setState({buttonText: 'Error :('});
      setTimeout(() => {
        this.setState({buttonText: 'Send reminder'});
      }, 2000);
    });
  };
  render(){
    const {filler_name} = this.props;
    return (
        <Popup size="mini"
               position="top center"
               inverted
               hoverable
               trigger={
                 <div class="app_overlay_indicator">
                   <Icon name="hourglass half" fitted/>
                 </div>
               }
               content={
                 <div>
                   {`${filler_name} will setup this app soon.`}
                   {this.props.magic_link &&
                   <div class="text-center">
                     <button onClick={this.send} class="button-unstyle inline-text-button fw-normal">
                       {this.state.buttonText}
                     </button>
                   </div>}
                 </div>
               }/>
    )
  }
};

export const NewAppLabel = (props) => {
  return (
      <Label circular class="app_corner_indicator">New</Label>
  )
};

export const UpdatePasswordLabel = (props)=> {
  return (
      <Label circular class="app_corner_indicator">
        <Icon name="unlock alternate"/>
      </Label>
  )
};

export const getPosition = (app_id) => {
  const el = document.getElementById(`app_${app_id}`);
  const window = document.getElementById('dashboard');
  let position = 'left';
  if (el)
    position = el.getBoundingClientRect().x + 245 > window.offsetWidth ? 'right' : 'left';
  return position;
};

export const SettingsMenu = ({app, clickOnSettings, remove, buttons, teams, position}) => {
  const team = app.team_id ? teams[app.team_id] : null;
  const role = team ? team.team_users[team.my_team_user_id].role : null;
  return (
    <div id={`app_${app.id}_menu`} className={`settings_buttons ${position ? position : 'left'}`}>
      {app.type === 'logWithApp' &&
      <button className="settings_button" onClick={clickOnSettings}>
        <Icon name={`${app.logWith_website.name.toLowerCase()} square`}/> {app.login}
      </button>}
      {(app.type === 'anyApp' || app.sub_type === 'any') &&
      <button className="settings_button" onClick={e => window.open(app.website.landing_url)}>
        <Icon name='external'/> {app.website.landing_url}
      </button>}
      {buttons}
      <button className={(role < 2 && (app.type === 'teamSingleApp' || app.type === 'teamLinkApp')) ? 'settings_button not_allowed' : 'settings_button'}
              onClick={(role < 2 && (app.type === 'teamSingleApp' || app.type === 'teamLinkApp')) ? null : clickOnSettings}>
        <Icon name='setting'/> Settings {(app.type === 'teamSingleApp' || app.type === 'teamLinkApp') ? '(Admins only)' : null}
      </button>
      <button className='settings_button' onClick={remove}>
        <Icon name='trash'/> Remove
      </button>
    </div>
  )
};

@connect(store => ({
  apps: store.dashboard.apps
}))
export class CopyPasswordIcon extends Component {
  constructor(props){
    super(props);
    this.state = {
      loading: false,
      popupText: 'loading...',
      app_id: this.props.app_id,
      open: undefined
    };
    this.password = '';
  }
  componentWillMount(){
    api.dashboard.getAppPassword({
      app_id: this.state.app_id
    }).then(response => {
      this.password = response.password;
      this.setState({loading: false, popupText: 'Copy'});
    });
  }
  copyPassword = () => {
    copyTextToClipboard(this.password);
    this.setState({popupText: 'Copied!', open: true});
    setTimeout(() => {
      this.setState({popupText: 'Copy', open: undefined});
    }, 1000);
    this.props.dispatch(passwordCopied({
      app: this.props.apps[this.state.app_id]
    }))
  };
  render(){
    return (
        <Popup size="mini"
               position="top center"
               inverted
               open={this.state.open}
               trigger={
                 <Icon name='copy' loading={this.state.loading} link onClick={this.state.loading ? null : this.copyPassword}/>
               }
               content={this.state.popupText}/>
    )
  }
}
