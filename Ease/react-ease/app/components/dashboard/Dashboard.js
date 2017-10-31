import React, {Component} from "react";
import {Loader, Input, Label,Icon} from 'semantic-ui-react';
import {showSimpleAppSettingsModal} from "../../actions/modalActions";
import {connect} from "react-redux";

@connect()
class Dashboard extends Component {
  constructor(props){
    super(props);
  }
  render(){
    return (
        <div id="dashboard">
          <div class="ui container fluid full_flex display_flex">
            <div class="display_flex flex_direction_column">
              <div class="app_group">
                <div class="app_group_name">
                  <Input placeholder="name" value="My Apps"/>
                </div>
                <div class="apps_container">
                  <div class="app display_flex flex_direction_column">
                    <div class="logo_area">
                      <div class="app_notification rounded_label"><Icon name="filter"/></div>
                      <div class="logo_handler">
                        <img src="/resources/websites/Facebook/logo.png"/>
                        <button class="settings_button" onClick={e => {this.props.dispatch(showSimpleAppSettingsModal({active: true}))}}>Settings</button>
                      </div>
                    </div>
                    <span class="app_name">Facebook</span>
                  </div>
                  <div class="app display_flex flex_direction_column">
                    <div class="logo_handler">
                      <img src="/resources/websites/Spotify/logo.png"/>
                    </div>
                    <span class="app_name">Spotify</span>
                  </div>
                  <div class="app display_flex flex_direction_column">
                    <div class="logo_handler">
                      <img src="/resources/websites/Slack/logo.png"/>
                    </div>
                    <span class="app_name">Slack</span>
                  </div>
                  <div class="app display_flex flex_direction_column">
                    <div class="logo_handler">
                      <img src="/resources/websites/Gmail/logo.png"/>
                    </div>
                    <span class="app_name">Gmail</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
    )
  }
}

export default Dashboard;