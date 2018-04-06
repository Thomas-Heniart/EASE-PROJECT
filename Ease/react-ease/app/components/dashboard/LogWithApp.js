import React, {Component} from "react";
import {connect} from "react-redux";
import {EmptyAppIndicator, NewAppLabel, LoadingAppIndicator, SettingsMenu} from "./utils";
import {showLogWithAppSettingsModal} from "../../actions/modalActions";
import {AppConnection} from "../../actions/dashboardActions";
import * as api from "../../utils/api";

@connect(store => ({
  apps: store.dashboard.apps
}))
class LogWithApp extends Component {
  constructor(props){
    super(props);
    this.state = {
      loading: false,
      menuActive: false,
      hover: false
    }
  }
  componentDidMount() {
    document.addEventListener('contextmenu', this._handleContextMenu);
  };
  componentWillUnmount() {
    document.removeEventListener('contextmenu', this._handleContextMenu);
  }
  _handleContextMenu = (event) => {
    event.preventDefault();
    if (this.state.hover)
      this.setState({ menuActive: true });
  };
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
  activateMenu = (e) => {
    e.preventDefault();
    const {app} = this.props;
    const isEmpty = app.logWithApp_id === -1;
    if (!isEmpty) {
      this.setState({hover: true});
      if (this.password === '')
        api.dashboard.getAppPassword({
          app_id: this.props.app.id
        }).then(response => {
          this.password = response.password;
        });
    }
  };
  deactivateMenu = () => {
    this.setState({menuActive: false, hover: false});
  };
  remove = () => {
    this.props.dispatch(showLogWithAppSettingsModal({active: true, app: this.props.app, remove: true}));
  };
  render(){
    const {app, dispatch} = this.props;
    const isEmpty = app.logWithApp_id === -1;
    let appModified = {...app};
    appModified.login = this.props.apps[app.logWithApp_id].account_information.login;
    return (
        <div class='app'>
          <div className={isEmpty ? 'logo_area' : this.state.menuActive ? 'logo_area active' : 'logo_area not_active'}
               onMouseEnter={this.activateMenu} onMouseLeave={this.deactivateMenu}>
            {this.state.loading &&
            <LoadingAppIndicator/>}
            {app.new &&
            <NewAppLabel/>}
            {isEmpty &&
            <EmptyAppIndicator onClick={e => {dispatch(showLogWithAppSettingsModal({active: true, app: app}))}}/>}
            <SettingsMenu
              app={appModified}
              remove={this.remove}
              clickOnSettings={e => dispatch(showLogWithAppSettingsModal({active: true, app: app}))}/>
            <div class="logo_handler">
              <img class="logo" src={app.logo} onClick={this.connect}/>
            </div>
          </div>
          <span class="app_name overflow-ellipsis">{app.name}</span>
        </div>
    )
  }
}

export default LogWithApp;