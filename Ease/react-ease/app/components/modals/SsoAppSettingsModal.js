import React, {Component} from "react";
import AppSettingsNameInput from "./AppSettingsNameInput";
import {handleSemanticInput} from "../../utils/utils";
import {Image,List,Segment, Grid,Loader,Checkbox,Message, Input, Label,Form, Menu, Icon, Container, Button} from 'semantic-ui-react';
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {showSsoAppSettingsModal} from "../../actions/modalActions";
import {AppSettingsMenu, ShareSection, RemoveSection, LabeledInput} from "./utils";
import {isAppInformationEmpty, transformCredentialsListIntoObject, transformWebsiteInfoIntoListAndSetValues, credentialIconType} from "../../utils/utils";
import {editClassicApp, deleteSsoApp, validateApp} from "../../actions/dashboardActions";
import {CopyPasswordIcon} from "../dashboard/utils";
import {connect} from "react-redux";

@connect(store => ({
  app: store.modals.ssoAppSettings.app,
  sso_groups: store.dashboard.sso_groups,
  apps: store.dashboard.apps
}))
class SsoAppSettingsModal extends Component{
  constructor(props){
    super(props);
    this.state = {
      appName: this.props.app.name,
      view: 'Account',
      sso_group: this.props.sso_groups[this.props.app.sso_group_id],
      other_apps: [],
      credentials: [],
      loading: false,
      errorMessage: ''
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
        item.value = this.state.sso_group.account_information[item.name];
      return item;
    });
    this.setState({credentials: credentials});
  };
  close = () => {
    this.props.dispatch(showSsoAppSettingsModal({active: false}));
  };
  remove = () => {
    return this.props.dispatch(deleteSsoApp({
      app_id: this.props.app.id
    })).then(response => {
      this.close();
    }).catch(err => {
      throw err;
    });
  };
  edit = (e) => {
    e.preventDefault();
  };
  componentWillMount(){
    const {app} = this.props;
    const other_apps = [];
    this.state.sso_group.sso_app_ids.map(id => {
      if (id !== app.id)
        other_apps.push(this.props.apps[id]);
    });
    const credentials = transformWebsiteInfoIntoListAndSetValues(app.website.information, this.state.sso_group.account_information).map(item => {
      return {
        ...item,
        edit: false
      }
    });
    if (this.props.app.new)
      this.props.dispatch(validateApp({
        app_id: this.props.app.id
      }));
    this.setState({credentials: credentials, other_apps: other_apps});
  }
  render(){
    const {view, credentials, other_apps} = this.state;
    const app = this.props.app;
    const inputs = credentials.map((item,idx) => {
      if (item.name === 'password')
        return (
            <Form.Field key={idx}>
              <label>{item.placeholder}</label>
              <div class="display_flex align_items_center">
                <Input
                    fluid
                    icon
                    disabled={!item.edit && !this.state.sso_group.empty}
                    className="modalInput team-app-input"
                    size='large'
                    type={item.type}
                    name={item.name}
                    onChange={this.handleCredentialInput}
                    value={(item.edit || this.state.sso_group.empty) ? item.value : '********'}
                    placeholder={item.placeholder}
                    labelPosition='left'>
                  <Label><Icon name="lock"/></Label>
                  <input/>
                  {!this.state.sso_group.empty &&
                  <CopyPasswordIcon app_id={app.id}/>}
                </Input>
                {!this.state.sso_group.empty &&
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
                  icon
                  disabled={!item.edit && !this.state.sso_group.empty}
                  className="modalInput team-app-input"
                  size='large'
                  type={item.type}
                  name={item.name}
                  onChange={this.handleCredentialInput}
                  label={{ icon: credentialIconType[item.name]}}
                  value={item.value}
                  placeholder={item.placeholder}
                  labelPosition='left'/>
              {!this.state.sso_group.empty &&
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
            <div class="display-flex align_items_center">
              <div class="squared_image_handler">
                <img src={app.website.logo} alt="Website logo"/>
              </div>
              <AppSettingsNameInput value={this.state.appName} onChange={this.handleInput}/>
            </div>
            <AppSettingsMenu view={view} onChange={this.handleInput}/>
            {view === 'Account' &&
            <Form onSubmit={this.edit} error={!!this.state.errorMessage.length}>
              {inputs}
              {!!other_apps.length &&
              <Form.Field>
                <span>Modifications will also apply to:</span>
                <Segment class="pushable ssoListSegment">
                  <Grid columns={2} class="ssoListGrid">
                    {other_apps.map(item => {
                      return (
                          <Grid.Column class="showSegment" key={item.id}>
                            <div class="display_flex align_items_center">
                              <img class="appLogo" src={item.website.logo}/>
                              <span class="overflow-ellipsis">{item.name}</span>
                            </div>
                          </Grid.Column>
                      )
                    })}
                  </Grid>
                </Segment>
              </Form.Field>}
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

export default SsoAppSettingsModal;