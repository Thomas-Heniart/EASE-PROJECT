import React, {Component} from "react";
import api from "../../utils/api";
import {copyTextToClipboard} from "../../utils/utils";
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

export const EmptyTeamAppIndicator = (props) => {
  return (
      <div class="app_overlay_indicator red" {...props}>
        <Icon name="wrench" fitted size="large"/>
      </div>
  )
};

export const DisabledAppIndicator = (props) => {
  return (
      <Popup size="mini"
             position="top center"
             inverted
             trigger={
               <div class="app_overlay_indicator" {...props}>
                 <Icon name="hourglass half" fitted size="large"/>
               </div>
             }
             content={`${props.filler_name} will setup this app soon.`}/>
  )
};

export const NewAppLabel = (props) => {
  return (
      <Label circular class="new_app_label">New</Label>
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