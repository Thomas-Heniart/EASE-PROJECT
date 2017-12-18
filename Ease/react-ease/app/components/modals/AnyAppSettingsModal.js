import React, {Component} from "react";
import AppSettingsNameInput from "./AppSettingsNameInput";
import {handleSemanticInput} from "../../utils/utils";
import {Message, Input, Label,Form, Icon, Container, Button} from 'semantic-ui-react';
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {showAnyAppSettingsModal} from "../../actions/modalActions";
import {AppSettingsMenu, ShareSection, RemoveSection} from "./utils";
import {transformCredentialsListIntoObject, transformWebsiteInfoIntoListAndSetValues, credentialIconType} from "../../utils/utils";
import {editAnyApp, deleteApp, validateApp} from "../../actions/dashboardActions";
import {CopyPasswordIcon} from "../dashboard/utils";
import { getClearbitLogo } from "../../utils/api";
import {connect} from "react-redux";
import {addNotification} from "../../actions/notificationBoxActions";

@connect(store => ({
  app: store.modals.anyAppSettings.app
}))
class AnyAppSettingsModal extends Component {
  constructor(props){
    super(props);
    this.state = {
      appName: this.props.app.name,
      view: 'Account',
      credentials: [],
      isEmpty: this.props.app.empty,
      loading: false,
      errorMessage:'',
      url: this.props.app.website.landing_url,
      editUrl: false
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
  getLogo = () => {
    getClearbitLogo(this.state.url).then(response => {
      this.setState({img_url: response});
    }).catch(err => {
      this.setState({img_url: ''});
    });
  };
  changeUrl = (e, {value}) => {
    this.setState({url: value}, this.getLogo);
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
  toggleUrlEdit = () => {
    this.setState({editUrl: !this.state.editUrl});
  };
  close = () => {
    this.props.dispatch(showAnyAppSettingsModal({active: false}));
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
    this.props.dispatch(editAnyApp({
      app_id: this.props.app.id,
      name: this.state.appName,
      url: this.state.url,
      img_url: this.state.img_url,
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
  render(){
    const {view, credentials} = this.state;
    const app = this.props.app;
    const inputs = credentials.map((item,idx) => {
      if (item.name === 'password')
        return (
          <Form.Field key={idx}>
            <label>{item.placeholder}</label>
            <div className="display_flex align_items_center">
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
          <div className="display_flex align_items_center">
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
              label={{ icon: credentialIconType[item.name]}}
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
          <div className="app_name_container display-flex align_items_center">
            <div className="squared_image_handler">
              <img src={this.state.img_url ? this.state.img_url : app.website.logo} alt="Website logo"/>
            </div>
            <AppSettingsNameInput value={this.state.appName} onChange={this.handleInput}/>
          </div>
          <AppSettingsMenu view={view} onChange={this.handleInput}/>
          {view === 'Account' &&
          <Form onSubmit={this.edit} error={!!this.state.errorMessage.length}>
            <Form.Field>
              <label>URL</label>
              <div className="display_flex align_items_center">
                <Input
                  fluid
                  icon
                  disabled={!this.state.editUrl && !this.state.isEmpty}
                  className="modalInput team-app-input"
                  size='large'
                  required={this.state.isEmpty}
                  type='url'
                  name='url'
                  onChange={this.changeUrl}
                  label={{ icon: 'home'}}
                  value={this.state.url}
                  placeholder='URL'
                  labelPosition='left'/>
                {!this.state.isEmpty &&
                <Icon
                  name="pencil"
                  onClick={this.toggleUrlEdit}
                  fitted link
                  style={{paddingLeft: '15px'}}/>}
              </div>
            </Form.Field>
            {inputs}
            <Message error content={this.state.errorMessage}/>
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

export default AnyAppSettingsModal;