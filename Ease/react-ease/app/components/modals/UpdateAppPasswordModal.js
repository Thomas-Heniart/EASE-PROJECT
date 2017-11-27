import React, {Component} from "react";
import api from "../../utils/api";
import TeamAppSettingsNameInput from "./TeamAppSettingsNameInput";
import {handleSemanticInput, isCredentialsMatch} from "../../utils/utils";
import {Image,List,Segment, Grid,Loader,Checkbox,Message, Input, Label,Form, Menu, Icon, Container, Button} from 'semantic-ui-react';
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {showUpdateAppPasswordModal} from "../../actions/modalActions";
import {AppSettingsMenu, ShareSection, RemoveSection, LabeledInput} from "./utils";
import {isAppInformationEmpty, transformCredentialsListIntoObject, transformWebsiteInfoIntoListAndSetValues, credentialIconType} from "../../utils/utils";
import {editAppName, editClassicApp, validateApp} from "../../actions/dashboardActions";
import {connect} from "react-redux";
import {copyTextToClipboard} from "../../utils/utils";

@connect(store => ({
  app: store.modals.updateAppPassword.app,
  team_apps: store.team_apps
}))
class UpdateAppPasswordModal extends Component {
  constructor(props){
    super(props);
    this.state = {
      loading: true,
      copyButtonText: 'Copy current password',
      errorMessage: ''
    };
    this.password = '';
  }
  process = (e) => {
    e.preventDefault();
  };
  close = () => {
    this.props.dispatch(showUpdateAppPasswordModal({active: false}));
  };
  copyPassword = () => {
    copyTextToClipboard(this.password);
    this.setState({copyButtonText: 'Copied!'});
    setTimeout(() => {
      this.setState({copyButtonText: 'Copy current password'});
    }, 2000);
  };
  componentWillMount(){
    api.dashboard.getAppPassword({
      app_id: this.props.app.id
    }).then(response => {
      this.password = response.password;
      this.setState({loading: false});
    }).catch(err => {
      this.setState({loading: false, errorMessage: err});
    });
  }
  render(){
    const {app} = this.props;
    const team_app = this.props.team_apps[app.team_card_id];

    return (
        <SimpleModalTemplate
            headerContent={'Your password is now obsolete!'}
            onClose={this.close}>
          <Form class="container" onSubmit={this.process} error={!!this.state.errorMessage.length}>
            <Form.Field>
              As requested by your administrator, the password of {app.name} needs to be updated each {team_app.password_reminder_interval} months.
              <br/>
              This time to go update your password!
            </Form.Field>
            <Form.Field>
              You might need your current password to proceed.
            </Form.Field>
            <Form.Field class="text-center">
              <Button type="button"
                      size="small"
                      loading={this.state.loading}
                      class="fw-normal"
                      style={{width: '62%'}}
                      primary
                      content={this.state.copyButtonText}
                      onClick={this.copyPassword}/>
            </Form.Field>
            <Message error content={this.state.errorMessage}/>
            <Button
                type="submit"
                positive
                className="modal-button"
                content="GO UPDATE MY PASSWORD"/>
          </Form>
        </SimpleModalTemplate>
    )
  }
}

export default UpdateAppPasswordModal;