import React, {Component} from "react";
import {EmptyAppIndicator, NewAppLabel, LoadingAppIndicator} from "./utils";
import {showLogWithAppSettingsModal} from "../../actions/modalActions";
import {AppConnection} from "../../actions/dashboardActions";

class LogWithApp extends Component {
  constructor(props){
    super(props);
    this.state = {
      loading: false
    }
  }
  connect = (e) => {
    this.setState({loading: true});
    this.props.dispatch(AppConnection({
      app_id: this.props.app.id,
      keep_focus: e.ctrlKey || e.metaKey
    })).then(response => {
      this.setState({loading: false});
    }).catch(err => {
      this.setState({loading: false});
    });
  };
  render(){
    const {app, dispatch} = this.props;
    const isEmpty = app.logWithApp_id === -1;

    return (
        <div class='app'>
          <div class="logo_area">
            {this.state.loading &&
            <LoadingAppIndicator/>}
            {app.new &&
            <NewAppLabel/>}
            {isEmpty &&
            <EmptyAppIndicator onClick={e => {dispatch(showLogWithAppSettingsModal({active: true, app: app}))}}/>}
            <div class="logo_handler">
              <img class="logo" src={app.logo} onClick={this.connect}/>
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