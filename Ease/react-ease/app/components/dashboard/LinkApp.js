import React, {Component} from "react";
import {getPosition, NewAppLabel, SettingsMenu} from "./utils";
import {showLinkAppSettingsModal} from "../../actions/modalActions";
import {validateApp, clickOnAppMetric} from "../../actions/dashboardActions";

class LinkApp extends Component {
  constructor(props){
    super(props);
    this.state = {
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
  activateMenu = () => {
    this.setState({hover: true, position: getPosition(this.props.app.id)});
  };
  deactivateMenu = () => {
    this.setState({menuActive: false, hover: false});
  };
  process = () => {
    const {app} = this.props;
    if (app.new)
      this.props.dispatch(validateApp({app_id: app.id}));
    this.props.dispatch(clickOnAppMetric({app: this.props.app}));
    window.open(app.url, '_blank');
  };
  remove = () => {
    this.props.dispatch(showLinkAppSettingsModal({active: true, app: this.props.app, remove: true}));
  };
  render(){
    const {app, dispatch} = this.props;
    return (
        <div class='app'>
          <div className={this.state.menuActive ? 'logo_area active' : 'logo_area not_active'}
               onMouseEnter={this.activateMenu} onMouseLeave={this.deactivateMenu}>
            {app.new &&
            <NewAppLabel/>}
            <SettingsMenu
              app={app}
              remove={this.remove}
              position={this.state.position}
              clickOnSettings={e => dispatch(showLinkAppSettingsModal({active: true, app: this.props.app}))}/>
            <div class="logo_handler">
              <img class="logo" src={app.logo} onClick={this.process}/>
            </div>
          </div>
          <span class="app_name overflow-ellipsis" onClick={this.process}>{app.name}</span>
        </div>
    )
  }
}

export default LinkApp;