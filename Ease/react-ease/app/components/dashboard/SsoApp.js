import React, {Component} from "react";
import {
  EmptyAppIndicator, EmptyTeamAppIndicator, NewAppLabel, DisabledAppIndicator, WaitingTeamApproveIndicator,
  LoadingAppIndicator
} from "./utils";
import {showSsoAppSettingsModal} from "../../actions/modalActions";
import {AppConnection} from "../../actions/dashboardActions";
import {Loader, Input, Label,Icon} from 'semantic-ui-react';
import {connect} from "react-redux";

@connect(store => ({
  sso_groups: store.dashboard.sso_groups
}))
class SsoApp extends Component {
  constructor(props){
    super(props);
    this.state = {
      loading: false
    }
  };
  connect = (e) => {
    this.setState({loading: true});
    this.props.dispatch(AppConnection({
      app_id: this.props.app.id,
      keep_focus: e.ctrlKey
    })).then(response => {
      this.setState({loading: false});
    }).catch(err => {
      this.setState({loading: false});
    });
  };
  render(){
    const {app, dispatch} = this.props;
    const sso_group = this.props.sso_groups[app.sso_group_id];

    return (
        <div class='app'>
          <div class="logo_area">
            {this.state.loading &&
            <LoadingAppIndicator/>}
            {sso_group.empty &&
            <EmptyAppIndicator onClick={e => {
              dispatch(showSsoAppSettingsModal({active: true, app: app}))
            }}/>}
            {app.new &&
            <NewAppLabel/>}
            <div class="logo_handler">
              <img class="logo" src={app.logo} onClick={this.connect}/>
              <button class="settings_button" onClick={e => {dispatch(showSsoAppSettingsModal({active: true, app: app}))}}>
                Settings
              </button>
            </div>
          </div>
          <span class="app_name overflow-ellipsis">{app.name}</span>
        </div>
    )
  }
}

export default SsoApp;