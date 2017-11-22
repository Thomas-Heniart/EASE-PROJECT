import React, {Component} from "react";
import {EmptyAppIndicator, EmptyTeamAppIndicator, NewAppLabel, DisabledAppIndicator, WaitingTeamApproveIndicator} from "./utils";
import {showLogWithAppSettingsModal} from "../../actions/modalActions";
import {Loader, Input, Label,Icon} from 'semantic-ui-react';

class LogWithApp extends Component {
  constructor(props){
    super(props);
  }
  render(){
    const {app, dispatch} = this.props;
    const isEmpty = app.logWithApp_id === -1;

    return (
        <div class='app'>
          <div class="logo_area">
            {app.new &&
            <NewAppLabel/>}
            {isEmpty &&
            <EmptyAppIndicator onClick={e => {dispatch(showLogWithAppSettingsModal({active: true, app: app}))}}/>}
            <div class="logo_handler">
              <img class="logo" src={app.logo}/>
              <button class="settings_button" onClick={e => {dispatch(showLogWithAppSettingsModal({active: true, app: app}))}}>
                Settings
              </button>
            </div>
          </div>
          <span class="app_name overflow-ellipsis">{app.name}</span>
        </div>
    )
  }
}

export default LogWithApp;