import React, {Component} from "react";
import {Icon} from "semantic-ui-react"
import {copyTextToClipboard, transformWebsiteInfoIntoListAndSetValues} from "../../utils/utils";
import {LoadingAppIndicator, EmptyAppIndicator, NewAppLabel, SettingsMenu, getPosition} from "./utils";
import {showSoftwareAppSettingsModal} from "../../actions/modalActions";
import {validateApp, clickOnAppMetric, passwordCopied} from '../../actions/dashboardActions';
import api from "../../utils/api";
import {connect} from "react-redux";
import ReactCSSTransitionGroup from "react-addons-css-transition-group";

@connect(store => ({
  active: store.modals.softwareAppSettings.active,
  dnd: store.dashboard_dnd.dragging_app_id !== -1
}))
class SoftwareApp extends Component {
  constructor(props) {
    super(props);
    this.state = {
      loading: false,
      copiedPassword: null,
      copiedOther: null,
      menuActive: false,
      hover: false,
      position: 'left'
    };
    this.password = '';
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
  activateMenu = (e) => {
    e.preventDefault();
    const {app} = this.props;
    if (!app.empty && !this.props.dnd) {
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
  clickOnSettings = (e) => {
    e.stopPropagation();
    this.props.dispatch(showSoftwareAppSettingsModal({active: true, app: this.props.app}));
  };
  copyPassword = (item) => {
    copyTextToClipboard(this.password);
    setTimeout(() => {
      this.setState({copiedPassword: item.priority});
    }, 1);
    setTimeout(() => {
      this.setState({copiedPassword: null});
    }, 1000);
    this.props.dispatch(passwordCopied({
      app: this.props.app
    }))
  };
  copy = (item) => {
    copyTextToClipboard(item.value);
    setTimeout(() => {
      this.setState({copiedOther: item.priority});
    }, 1);
    setTimeout(() => {
      this.setState({copiedOther: null});
    }, 1000);
  };
  remove = () => {
    this.props.dispatch(showSoftwareAppSettingsModal({active: true, app: this.props.app, remove: true}));
  };
  render() {
    const {app} = this.props;
    const credentials = transformWebsiteInfoIntoListAndSetValues(app.software.connection_information, app.account_information);
    const buttons = credentials.map((item, idx) => {
      if (this.state.copiedPassword !== item.priority && this.state.copiedOther !== item.priority) {
        if (item.name === 'password')
          return (
            <div className='container_button' key={idx}>
              <button className="settings_button" onClick={e => this.copyPassword(item)}>
                <Icon name='copy'/> • • • • • • • •
              </button>
            </div>
          );
        return (
          <div className='container_button' key={idx}>
            <button className="settings_button" onClick={e => this.copy(item)}>
              <Icon name='copy'/> {item.value}
            </button>
          </div>
        )
      }
      return (
        <div className='container_button' key={idx}>
          <button className="settings_button">
            Copied!
          </button>
        </div>
      )
    });
    return (
        <div className='app'>
          <div className={app.empty ? 'logo_area' : this.state.menuActive ? 'logo_area active' : 'logo_area not_active'}
               onMouseEnter={!this.props.dnd ? this.activateMenu : null} onMouseLeave={!this.props.dnd ? this.deactivateMenu : null}>
            {this.state.loading &&
            <LoadingAppIndicator/>}
            {app.empty &&
            <EmptyAppIndicator onClick={this.clickOnSettings}/>}
            {app.new &&
            <NewAppLabel/>}
            <ReactCSSTransitionGroup
              transitionName="settingsAnim"
              transitionEnter={true}
              transitionLeave={true}
              transitionEnterTimeout={1300}
              transitionLeaveTimeout={1}>
              {this.state.hover && !this.props.dnd &&
            <SettingsMenu
                app={app}
                buttons={buttons}
                remove={this.remove}
                position={this.state.position}
                clickOnSettings={this.clickOnSettings}/>}
            </ReactCSSTransitionGroup>
            <div className="logo_handler">
              <img className="logo" src={app.logo}/>
            </div>
          </div>
          <span className="app_name overflow-ellipsis">{app.name}</span>
        </div>
    )
  }
}

export default SoftwareApp;