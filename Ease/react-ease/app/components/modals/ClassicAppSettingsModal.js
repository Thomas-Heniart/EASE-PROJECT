import React, {Component} from "react";
import AppSettingsNameInput from "./AppSettingsNameInput";
import {handleSemanticInput} from "../../utils/utils";
import {Image,List,Segment, Grid,Loader,Checkbox,Message, Input, Label,Form, Menu, Icon, Container, Button} from 'semantic-ui-react';
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {showClassicAppSettingsModal} from "../../actions/modalActions";
import {AppSettingsMenu, ShareSection, RemoveSection, LabeledInput} from "./utils";
import {isAppInformationEmpty, transformCredentialsListIntoObject, transformWebsiteInfoIntoListAndSetValues, credentialIconType} from "../../utils/utils";
import {editClassicApp} from "../../actions/dashboardActions";
import {connect} from "react-redux";

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
      isEmpty: isAppInformationEmpty(this.props.app.account_information),
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
  close = () => {
    this.props.dispatch(showClassicAppSettingsModal({active: false}));
  };
  remove = () => {
    return new Promise((resolve, reject) => {
      resolve();
      this.close();
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
    this.setState({credentials: credentials});
  }
  render(){
    const {view, credentials} = this.state;
    const inputs = credentials.map((item,idx) => {
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
                    type={item.type}
                    name={item.name}
                    onChange={this.handleCredentialInput}
                    value={(item.edit || this.state.isEmpty) ? item.value : '********'}
                    placeholder={item.placeholder}
                    labelPosition='left'>
                  <Label><Icon name="lock"/></Label>
                  <input/>
                  <Icon name="copy" link/>
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
                  icon
                  disabled={!item.edit && !this.state.isEmpty}
                  className="modalInput team-app-input"
                  size='large'
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
            <div class="display-flex align_items_center">
              <div class="squared_image_handler">
                <img src={this.props.app.logo} alt="Website logo"/>
              </div>
              <AppSettingsNameInput value={this.state.appName} onChange={this.handleInput}/>
            </div>
            <AppSettingsMenu view={view} onChange={this.handleInput}/>
            {view === 'Account' &&
            <Form onSubmit={this.edit} error={!!this.state.errorMessage.length}>
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

const ssoSection = () => {
  return (
      <Form.Field>
        <span>Modifications will also apply to:</span>
        <Segment class="pushable ssoListSegment">
          <Grid columns={2} class="ssoListGrid">
            <Grid.Column class="showSegment">
              <div class="display_flex align_items_center">
                <img class="appLogo" src={'/resources/websites/Facebook/logo.png'}/>
                <span class="overflow-ellipsis">Facebook</span>
              </div>
            </Grid.Column>
            <Grid.Column class="showSegment">
              <div class="display_flex align_items_center">
                <img class="appLogo" src={'/resources/websites/Facebook/logo.png'}/>
                <span class="overflow-ellipsis">Facebook</span>
              </div>
            </Grid.Column>
            <Grid.Column class="showSegment">
              <div class="display_flex align_items_center">
                <img class="appLogo" src={'/resources/websites/Facebook/logo.png'}/>
                <span class="overflow-ellipsis">Facebook</span>
              </div>
            </Grid.Column>
          </Grid>
        </Segment>
      </Form.Field>
  )
};

export default ClassicAppSettingsModal;