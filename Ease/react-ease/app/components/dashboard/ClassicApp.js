import React, {Component} from "react";
import {EmptyAppIndicator} from "./utils";
import {showClassicAppSettingsModal} from "../../actions/modalActions";
import {isAppInformationEmpty} from "../../utils/utils";
import {Loader, Input, Label,Icon} from 'semantic-ui-react';

class ClassicApp extends Component {
  constructor(props){
    super(props);
  }
  render(){
    const {app, dispatch} = this.props;
    const isEmpty = isAppInformationEmpty(app.account_information);

    return (
        <div class='app'>
          <div class="logo_area">
            {isEmpty &&
            <div style={{display: 'none'}} class="app_notification rounded_label">
              <Icon name="filter"/>
            </div>}
            <EmptyAppIndicator onClick={e => {dispatch(showClassicAppSettingsModal({active: true, app: app}))}}/>
            <div class="logo_handler">
              <img class="logo" src={app.logo}/>
              <button class="settings_button" onClick={e => {dispatch(showClassicAppSettingsModal({active: true, app: app}))}}>
                Settings
              </button>
            </div>
          </div>
          <span class="app_name overflow-ellipsis">{app.name}</span>
        </div>
    )
  }
}

export default ClassicApp;