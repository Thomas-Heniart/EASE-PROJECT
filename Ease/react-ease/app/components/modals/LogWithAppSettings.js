import React, {Component} from "react";
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {connect} from "react-redux";
import {List, Segment, Message, Form, Icon, Container, Button} from 'semantic-ui-react';
import {showLogWithAppSettingsModal} from "../../actions/modalActions";
import {handleSemanticInput} from "../../utils/utils";
import AppSettingsNameInput from "./AppSettingsNameInput";
import {editLogWithApp, deleteApp, validateApp} from "../../actions/dashboardActions";
import {AppSettingsMenu, ShareSection, RemoveSection} from "./utils";
import {addNotification} from "../../actions/notificationBoxActions";

@connect(store => ({
  app: store.modals.logWithAppSettings.app,
  apps: store.dashboard.apps,
  remove: store.modals.logWithAppSettings.remove
}))
class LogWithAppSettings extends Component {
  constructor(props){
    super(props);
    this.state = {
      view: this.props.remove ? 'Remove' : 'Account',
      appName: this.props.app.name,
      lw_app_id: this.props.app.logWithApp_id,
      lw_apps: [],
      loading: false,
      errorMessage: ''
    }
  }
  handleInput = handleSemanticInput.bind(this);
  selectApp = (id) => {
    this.setState({lw_app_id: id});
  };
  close = () => {
    this.props.dispatch(showLogWithAppSettingsModal({active: false}));
  };
  remove = () => {
    return this.props.dispatch(deleteApp({
      app_id: this.props.app.id
    })).then(response => {
      this.props.dispatch(addNotification({
        text: `${this.props.app.name} has been successfully removed!`
      }));
      this.close();
    }).catch(err => {
      throw err;
    });
  };
  edit = (e) => {
    e.preventDefault();
    this.setState({loading: true, errorMessage:''});
    this.props.dispatch(editLogWithApp({
      app_id: this.props.app.id,
      name: this.state.appName,
      logWithApp_id: this.state.lw_app_id
    })).then(response => {
      this.setState({loading: false});
      this.props.dispatch(addNotification({
        text: `${this.props.app.name} has been successfully modified!`
      }));
      this.close();
    }).catch(err => {
      this.setState({loading: false, errorMessage: err});
    });
  };
  componentWillMount(){
    const {app, apps} = this.props;
    const lw_apps = Object.keys(apps).map(item => {
      return apps[item];
    }).filter(item => {
      return !!item.website && item.website.id === app.logWith_website.id;
    });
    if (this.props.app.new)
      this.props.dispatch(validateApp({
        app_id: this.props.app.id
      }));
    this.setState({lw_apps: lw_apps});
  };
  render(){
    const {app} = this.props;
    const {view} = this.state;
    const lw_name = app.logWith_website.name.toLowerCase();
    const app_list = this.state.lw_apps.map(app => {
      return (
          <List.Item
              key={app.id}
              as="p"
              disabled={app.empty}
              active={app.id === this.state.lw_app_id}
              onClick={this.selectApp.bind(null, app.id)}>
            <Icon name='user circle' />
            <span>{!app.empty ? app.account_information.login : `App with missing credentials`}</span>
          </List.Item>
      )
    });
    return (
        <SimpleModalTemplate
            headerContent={'App settings'}
            onClose={this.close}>
          <Container class="app_settings_modal">
            <div class="app_name_container display-flex align_items_center">
              <div class="squared_image_handler">
                <img src={app.logo} alt="Website logo"/>
              </div>
              <AppSettingsNameInput value={this.state.appName} onChange={this.handleInput}/>
            </div>
            <AppSettingsMenu view={view} onChange={this.handleInput}/>
            {view === 'Account' &&
            <Form onSubmit={this.edit} error={!!this.state.errorMessage.length}>
              <Form.Field>
                <Segment.Group class={`logwith_app_selectors ${lw_name}`}>
                  <Segment className='first'>
                    <Icon name={lw_name}/>
                    Select your {app.logWith_website.name} account
                  </Segment>
                  <Segment>
                    <List className="listCategory">
                      {!!app_list.length ?
                          app_list :
                          <p class="text-center errorMessage">
                            You don’t have a {app.logWith_website.name} Account setup yet. Please install one before all.
                          </p>}
                    </List>
                  </Segment>
                </Segment.Group>
              </Form.Field>
              <Message error content={this.state.errorMessage}/>
              <Button
                  type="submit"
                  positive
                  disabled={this.state.lw_app_id === -1}
                  loading={this.state.loading}
                  className="modal-button"
                  content="CONFIRM"/>
            </Form>}
            {view === 'Remove' &&
            <RemoveSection onRemove={this.remove}/>}
            {view === 'Share' &&
            <ShareSection/>}
          </Container>
        </SimpleModalTemplate>
    )
  }
}

export default LogWithAppSettings;