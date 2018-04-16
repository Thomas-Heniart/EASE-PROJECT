import React, {Component} from "react";
import {connect} from "react-redux";
import {getPosition, NewAppLabel, SettingsMenu} from "./utils";
import {showLinkAppSettingsModal} from "../../actions/modalActions";
import {validateApp, clickOnAppMetric} from "../../actions/dashboardActions";
import ReactCSSTransitionGroup from "react-addons-css-transition-group";

@connect(store => ({
  dnd: store.dashboard_dnd.dragging_app_id !== -1
}))
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
    if (!this.props.dnd)
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
               onMouseEnter={!this.props.dnd ? this.activateMenu : null} onMouseLeave={!this.props.dnd ? this.deactivateMenu : null}>
            {app.new &&
            <NewAppLabel/>}
            <ReactCSSTransitionGroup
              transitionName="settingsAnim"
              transitionEnter={true}
              transitionLeave={true}
              transitionEnterTimeout={1300}
              transitionLeaveTimeout={300}>
              {this.state.hover && !this.props.dnd &&
                <SettingsMenu
                  app={app}
                  remove={this.remove}
                  position={this.state.position}
                  clickOnSettings={e => dispatch(showLinkAppSettingsModal({active: true, app: this.props.app}))}/>}
            </ReactCSSTransitionGroup>
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