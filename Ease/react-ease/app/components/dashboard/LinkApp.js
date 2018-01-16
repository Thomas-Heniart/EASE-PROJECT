import React, {Component} from "react";
import {EmptyAppIndicator, EmptyTeamAppIndicator, NewAppLabel, DisabledAppIndicator, WaitingTeamApproveIndicator} from "./utils";
import {showLinkAppSettingsModal} from "../../actions/modalActions";
import {Loader, Input, Label,Icon} from 'semantic-ui-react';
import {validateApp, clickOnAppMetric} from "../../actions/dashboardActions";

class LinkApp extends Component {
  constructor(props){
    super(props);
  }
  process = () => {
    const {app} = this.props;
    if (app.new)
      this.props.dispatch(validateApp({app_id: app.id}));
    this.props.dispatch(clickOnAppMetric({app_id: this.props.app.id}));
    window.open(app.url, '_blank');
  };
  render(){
    const {app, dispatch} = this.props;
    return (
        <div class='app'>
          <div class="logo_area">
            {app.new &&
            <NewAppLabel/>}
            <div class="logo_handler">
              <img class="logo" src={app.logo} onClick={this.process}/>
              <button class="settings_button" onClick={e => {dispatch(showLinkAppSettingsModal({active: true, app:app}))}}>Settings</button>
            </div>
          </div>
          <span class="app_name overflow-ellipsis">{app.name}</span>
        </div>
    )
  }
}

export default LinkApp;