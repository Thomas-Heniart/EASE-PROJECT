import React, {Component} from "react";
import {LoadingAppIndicator, EmptyAppIndicator, NewAppLabel} from "./utils";
import {showClassicAppSettingsModal} from "../../actions/modalActions";
import {AppConnection} from "../../actions/dashboardActions";

class ClassicApp extends Component {
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

    return (
        <div class='app classic'>
          <div class="logo_area">
            {this.state.loading &&
            <LoadingAppIndicator/>}
            {app.empty &&
            <EmptyAppIndicator onClick={e => {
              dispatch(showClassicAppSettingsModal({active: true, app: app}))
            }}/>}
            {app.new &&
            <NewAppLabel/>}
            <div class="logo_handler">
              <img class="logo" src={app.logo} onClick={this.connect}/>
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