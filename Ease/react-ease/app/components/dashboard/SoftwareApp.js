import React, {Component} from "react";
import {Popup, Input, Icon, Label, Segment} from "semantic-ui-react"
import {copyTextToClipboard, transformWebsiteInfoIntoListAndSetValues} from "../../utils/utils";
import {LoadingAppIndicator, EmptyAppIndicator, NewAppLabel} from "./utils";
import {showSoftwareAppSettingsModal} from "../../actions/modalActions";
import {validateApp} from '../../actions/dashboardActions';
import api from "../../utils/api";
import {connect} from "react-redux";

@connect(store => ({
  active: store.modals.softwareAppSettings.active
}))
class SoftwareApp extends Component {
  constructor(props){
    super(props);
    this.state = {
      loading: false,
      isOpen: false,
      copiedPassword: null,
      copiedOther: null
    };
    this.password = '';
  }
  handleOpenClose = () => {
    if (!this.props.active) {
      if (this.state.isOpen === false) {
        if (this.props.app.new)
          this.props.dispatch(validateApp({app_id: this.props.app.id}));
        api.dashboard.getAppPassword({
          app_id: this.props.app.id
        }).then(response => {
          this.password = response.password;
        });
      }
      this.setState({isOpen: !this.state.isOpen});
    }
  };
  clickOnSettings = (e) => {
    e.stopPropagation();
    this.setState({isOpen: false});
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
  render(){
    const {app} = this.props;
    const credentials = transformWebsiteInfoIntoListAndSetValues(app.software.connection_information, app.account_information);
    const inputs = credentials.map((item,idx) => {
      if (this.state.copiedPassword !== item.priority && this.state.copiedOther !== item.priority) {
        if (item.name === 'password')
          return (
            <Input
              key={idx}
              size='mini'
              type='password'
              disabled
              name={item.name}
              placeholder='Password'
              value={'********'}
              label={
                <Label style={{backgroundColor: '#373b60', color: 'white', fontWeight: '300'}}
                       onClick={e => this.copyPassword(item)}>
                  Copy <Icon name='copy'/>
                </Label>}/>
          );
        return (
          <Input
            key={idx}
            size='mini'
            disabled
            type={item.type}
            name={item.name}
            placeholder={item.placeholder}
            value={item.value}
            label={
              <Label style={{backgroundColor: '#373b60', color: 'white', fontWeight: '300'}}
                     onClick={e => this.copy(item)}>
                Copy <Icon name='copy'/>
              </Label>}/>
        )
      }
      return (
        <Segment
          key={idx}
          size='mini'
          content={'Copied!'}/>
      )
    });
    return (
      <Popup
        size="tiny"
        className='dashboard_popup_soft_and_any'
        position="top center"
        on='click'
        open={(this.props.active || app.empty) ? false : this.state.isOpen}
        onClose={this.handleOpenClose}
        onOpen={this.handleOpenClose}
        hideOnScroll
        trigger={
          <div className='app'>
            <div className="logo_area">
              {this.state.loading &&
              <LoadingAppIndicator/>}
              {app.empty &&
              <EmptyAppIndicator onClick={this.clickOnSettings}/>}
              {app.new &&
              <NewAppLabel/>}
              <div className="logo_handler">
                <img className="logo" src={app.logo}/>
                <button className="settings_button" onClick={this.clickOnSettings}>
                  Settings
                </button>
              </div>
            </div>
            <span className="app_name overflow-ellipsis">{app.name}</span>
          </div>}
        content={inputs}/>
    )
  }
}

export default SoftwareApp;