import React, {Component} from "react";
import api from "../../utils/api";
import post_api from "../../utils/post_api";
import {copyTextToClipboard, basicDateFormat} from "../../utils/utils";
import {Loader, Input, Label,Icon, Popup} from 'semantic-ui-react';

export const EmptyAppIndicator = (props) => {
  return (
      <div class="app_overlay_indicator" {...props}>
        <Icon name="exclamation triangle" fitted size="large"/>
      </div>
  )
};

export const WaitingTeamApproveIndicator = (props) => {
  return (
      <div class="app_overlay_indicator grey" {...props}/>
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
        <Icon name="wrench" fitted size="large"/>
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
                   <Icon name="hourglass half" fitted size="large"/>
                 </div>
               }
               content={
                 <div>
                   {`${filler_name} will setup this app soon.`}
                   <div class="text-center">
                     <button onClick={this.send} class="button-unstyle inline-text-button fw-normal">
                       {this.state.buttonText}
                     </button>
                   </div>
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