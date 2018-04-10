import React, {Component} from "react";
import {connect} from "react-redux";
import * as api from "../../utils/api";
import {Icon} from "semantic-ui-react";
import {showSsoAppSettingsModal} from "../../actions/modalActions";
import {AppConnection, passwordCopied} from "../../actions/dashboardActions";
import {copyTextToClipboard, transformWebsiteInfoIntoListAndSetValues} from "../../utils/utils";
import {EmptyAppIndicator, NewAppLabel, LoadingAppIndicator, SettingsMenu, getPosition} from "./utils";

@connect(store => ({
  sso_groups: store.dashboard.sso_groups
}))
class SsoApp extends Component {
  constructor(props){
    super(props);
    this.state = {
      loading: false,
      menuActive: false,
      hover: false,
      position: 'left'
    };
    this.password = '';
  };
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
    const sso_group = this.props.sso_groups[app.sso_group_id];
    if (!sso_group.empty) {
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
    this.props.dispatch(showSsoAppSettingsModal({active: true, app: this.props.app, remove: true}));
  };
  checkAndConnect = (e) => {
    const {app} = this.props;
    const sso_group = this.props.sso_groups[app.sso_group_id];

    if (this.state.loading || sso_group.empty)
      return;
    this.connect(e);
  };
  render(){
    const {app, dispatch} = this.props;
    const sso_group = this.props.sso_groups[app.sso_group_id];
    const credentials = transformWebsiteInfoIntoListAndSetValues(app.website.information, this.props.sso_groups[app.sso_group_id].account_information);
    const buttons = credentials.map((item,idx) => {
      if (this.state.copiedPassword !== item.priority && this.state.copiedOther !== item.priority) {
        if (item.name === 'password')
          return (
              <button
                  className="settings_button"
                  onClick={e => this.copyPassword(item)}
                  key={idx}>
                <Icon name='copy'/> • • • • • • • •
              </button>
          );
        return (
            <button
                key={idx}
                className="settings_button"
                onClick={e => this.copy(item)}>
              <Icon name='copy'/> {item.value}
            </button>
        )
      }
      return (
          <button
              key={idx}
              className="settings_button">
            Copied!
          </button>
      )
    });
    return (
        <div class='app'>
          <div className={sso_group.empty ? 'logo_area' : this.state.menuActive ? 'logo_area active' : 'logo_area not_active'}
               onMouseEnter={this.activateMenu} onMouseLeave={this.deactivateMenu}>
            {this.state.loading &&
            <LoadingAppIndicator/>}
            {sso_group.empty &&
            <EmptyAppIndicator onClick={e => {dispatch(showSsoAppSettingsModal({active: true, app: app}))}}/>}
            {app.new &&
            <NewAppLabel/>}
            <SettingsMenu
                app={app}
                buttons={buttons}
                remove={this.remove}
                position={this.state.position}
                clickOnSettings={e => dispatch(showSsoAppSettingsModal({active: true, app: app}))}/>
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

export default SsoApp;