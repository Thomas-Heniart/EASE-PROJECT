import React, {Component} from "react";
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
            <div style={{display:'none'}} class="app_notification rounded_label"><Icon name="filter"/></div>
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