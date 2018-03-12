import React, {Component} from "react";
import AppSettingsNameInput from "./AppSettingsNameInput";
import {handleSemanticInput} from "../../utils/utils";
import {Message, Input, Label,Form, Icon, Container, Button} from 'semantic-ui-react';
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {showClassicAppSettingsModal} from "../../actions/modalActions";
import {AppSettingsMenu, ShareSection, RemoveSection} from "./utils";
import {transformCredentialsListIntoObject, transformWebsiteInfoIntoListAndSetValues, credentialIconType} from "../../utils/utils";
import {editClassicApp, deleteApp, validateApp} from "../../actions/dashboardActions";
import {CopyPasswordIcon} from "../dashboard/utils";
import {connect} from "react-redux";
import {addNotification} from "../../actions/notificationBoxActions";
import {testCredentials} from "../../actions/catalogActions";
import * as api from "../../utils/api";

@connect(store => ({
  app: store.modals.classicAppSettings.app
}))
class ClassicAppSettingsModal extends Component {
  constructor(props){
    super(props);
    this.state = {
      appName: this.props.app.name,
      view: 'Account',
      credentials: [],
      isEmpty: this.props.app.empty,
      loading: false,
      errorMessage:''
    };
  }
  handleInput = handleSemanticInput.bind(this);
  handleCredentialInput = (e, {name, value}) => {
    const credentials = this.state.credentials.map(item => {
      if (item.name === name)
        item.value = value;
      return item;
    });
    this.setState({credentials: credentials});
  };
  toggleCredentialEdit = (name) => {
    const credentials = this.state.credentials.map(item => {
      if (item.name === name)
        item.edit = !item.edit;
      if (!item.edit)
        item.value = this.props.app.account_information[item.name];
      return item;
    });
    this.setState({credentials: credentials});
  };
  testConnection = () => {
    let account_information = transformCredentialsListIntoObject(this.state.credentials);
    if (!this.state.isEmpty) {
      api.dashboard.getAppPassword({
        app_id: this.props.app.id
      }).then(response => {
        if (this.state.credentials.filter(item => {return item.name === 'password' && !item.edit}).length > 0)
          account_information.password = response.password;
        this.props.dispatch(testCredentials({
          account_information: account_information,
          website_id: this.props.app.website.id
        }));
      });
    }
    else {
      this.props.dispatch(testCredentials({
        account_information: account_information,
        website_id: this.props.app.website.id
      }));
    }
  };
  close = () => {
    this.props.dispatch(showClassicAppSettingsModal({active: false}));
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
    this.setState({loading: true, errorMessage: ''});
    this.props.dispatch(editClassicApp({
      app_id: this.props.app.id,
      name: this.state.appName,
      account_information: transformCredentialsListIntoObject(this.state.credentials)
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
    const {app} = this.props;
    const credentials = transformWebsiteInfoIntoListAndSetValues(app.website.information, app.account_information).map(item => {
      return {
        ...item,
        edit: false
      }
    });
    if (this.props.app.new)
      this.props.dispatch(validateApp({
        app_id: this.props.app.id
      }));
    this.setState({credentials: credentials});
  }
  render() {
    const {view, credentials} = this.state;
    const app = this.props.app;
    const inputs = credentials.map((item, idx) => {
      if (item.name === 'password')
        return (
          <Form.Field key={idx}>
            <label>{item.placeholder}</label>
            <div class="display_flex align_items_center">
              <Input
                fluid
                icon
                disabled={!item.edit && !this.state.isEmpty}
                className="modalInput team-app-input"
                size='large'
                required={this.state.isEmpty}
                type={item.type}
                name={item.name}
                onChange={this.handleCredentialInput}
                value={(item.edit || this.state.isEmpty) ? item.value : '********'}
                placeholder={item.placeholder}
                labelPosition='left'>
                <Label><Icon name="lock"/></Label>
                <input/>
                {!app.empty &&
                <CopyPasswordIcon app_id={app.id}/>}
              </Input>
              {!this.state.isEmpty &&
              <Icon
                name="pencil"
                onClick={this.toggleCredentialEdit.bind(null, item.name)}
                fitted link
                style={{paddingLeft: '15px'}}/>}
            </div>
          </Form.Field>
        );
      return (
        <Form.Field key={idx}>
          <label>{item.placeholder}</label>
          <div class="display_flex align_items_center">
            <Input
              fluid
              disabled={!item.edit && !this.state.isEmpty}
              className="modalInput team-app-input"
              size='large'
              required={this.state.isEmpty}
              type={item.type}
              name={item.name}
              onChange={this.handleCredentialInput}
              label={{icon: credentialIconType[item.name]}}
              value={item.value}
              placeholder={item.placeholder}
              labelPosition='left'/>
            {!this.state.isEmpty &&
            <Icon
              name="pencil"
              onClick={this.toggleCredentialEdit.bind(null, item.name)}
              fitted link
              style={{paddingLeft: '15px'}}/>}
          </div>
        </Form.Field>
      )
    });
    return (
      <SimpleModalTemplate
        onClose={this.close}
        headerContent={"App settings"}>
        <Container class="app_settings_modal">
          <div class="app_name_container display-flex align_items_center">
            <div class="squared_image_handler">
              <img src={app.website.logo} alt="Website logo"/>
            </div>
            <AppSettingsNameInput value={this.state.appName} onChange={this.handleInput}/>
          </div>
          <AppSettingsMenu view={view} onChange={this.handleInput}/>
          {view === 'Account' &&
          <Form onSubmit={this.edit} error={!!this.state.errorMessage.length}>
            {inputs}
            <Message error content={this.state.errorMessage}/>
            {this.state.credentials.filter(item => {return item.edit}).length > 0 &&
            <span id='test_credentials' onClick={this.testConnection}>Test connection <Icon color='green' name='magic'/></span>}
            <Button
              type="submit"
              positive
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

export default ClassicAppSettingsModal;