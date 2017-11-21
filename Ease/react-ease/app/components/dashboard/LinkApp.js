import React, {Component} from "react";
import {EmptyAppIndicator, EmptyTeamAppIndicator, NewAppLabel, DisabledAppIndicator, WaitingTeamApproveIndicator} from "./utils";
import {showLinkAppSettingsModal} from "../../actions/modalActions";
import {Loader, Input, Label,Icon} from 'semantic-ui-react';

class LinkApp extends Component {
  constructor(props){
    super(props);
  }
  render(){
    const {app} = this.props;
    return (
        <div class='app'>
          <div class="logo_area">
            <NewAppLabel/>
            <div class="logo_handler">
              <img class="logo" src={app.logo}/>
              <button class="settings_button" onClick={e => {this.props.dispatch(showLinkAppSettingsModal({active: true, app:app}))}}>Settings</button>
            </div>
          </div>
          <span class="app_name overflow-ellipsis">{app.name}</span>
        </div>
    )
  }
}

export default LinkApp;