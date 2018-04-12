import React, {Component} from "react";
import {connect} from "react-redux";
import {EmptyAppIndicator, NewAppLabel, LoadingAppIndicator, SettingsMenu, getPosition} from "./utils";
import {showLogWithAppSettingsModal} from "../../actions/modalActions";
import {AppConnection} from "../../actions/dashboardActions";
import * as api from "../../utils/api";
import ReactCSSTransitionGroup from "react-addons-css-transition-group";

@connect(store => ({
  apps: store.dashboard.apps,
  dnd: store.dashboard_dnd.dragging_app_id !== -1
}))
class LogWithApp extends Component {
  constructor(props){
    super(props);
    this.state = {
      loading: false,
      menuActive: false,
      hover: false,
      position: 'left'
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
    if (!isEmpty && !this.props.dnd) {
      this.setState({hover: true, position: getPosition(app.id)});
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
  checkAndConnect = () => {
    const {app} = this.props;

    if (this.state.loading || app.logWithApp_id === -1)
      return;
    this.connect();
  };
  render(){
    const {app, dispatch} = this.props;
    const isEmpty = app.logWithApp_id === -1;
    let appModified = {...app};
    appModified.login = this.props.apps[app.logWithApp_id].account_information.login;
    return (
        <div class='app'>
          <div className={isEmpty ? 'logo_area' : this.state.menuActive ? 'logo_area active' : 'logo_area not_active'}
               onMouseEnter={!this.props.dnd ? this.activateMenu : null} onMouseLeave={!this.props.dnd ? this.deactivateMenu : null}>
            {this.state.loading &&
            <LoadingAppIndicator/>}
            {app.new &&
            <NewAppLabel/>}
            {isEmpty &&
            <EmptyAppIndicator onClick={e => {dispatch(showLogWithAppSettingsModal({active: true, app: app}))}}/>}
            <ReactCSSTransitionGroup
              transitionName="settingsAnim"
              transitionEnter={true}
              transitionLeave={true}
              transitionEnterTimeout={1300}
              transitionLeaveTimeout={300}>
              {this.state.hover &&
            <SettingsMenu
              app={appModified}
              remove={this.remove}
              position={this.state.position}
              clickOnSettings={e => dispatch(showLogWithAppSettingsModal({active: true, app: app}))}/>}
            </ReactCSSTransitionGroup>
            <div class="logo_handler">
              <img class="logo" src={app.logo} onClick={this.connect}/>
            </div>
          </div>
          <span class="app_name overflow-ellipsis"
              onClick={this.checkAndConnect}>{app.name}</span>
        </div>
    )
  }
}

export default LogWithApp;