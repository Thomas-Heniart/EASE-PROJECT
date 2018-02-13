import React from 'react';
import {connect} from "react-redux";
import CredentialInputs from "./CredentialInputs";
import {testCredentials} from "../../../actions/catalogActions";
import SimpleModalTemplate from "../../common/SimpleModalTemplate";
import {handleSemanticInput} from "../../../utils/utils";
import { Container, Icon, Form, Message, Button, Checkbox } from 'semantic-ui-react';

@connect(store => ({
  modal: store.modals.accountUpdate
}))
class AccountUpdateModal extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      error: '',
      check: '',
      loading: false,
      seePassword: false,
      website: this.props.modal.website,
      account_information: this.props.modal.account_information
    }
  }
  handleInput = handleSemanticInput.bind(this);
  handleChange = (e, {value}) => this.setState({check: value});
  handleCredentialsInput = (e, {name, value}) => {
    let account_information = {...this.state.account_information};
    account_information[name] = value;
    this.setState({account_information: account_information});
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
  toggleSeePassword = () => {
    this.setState({seePassword: !this.state.seePassword});
  };
  testConnection = () => {
    this.props.dispatch(testCredentials({
      account_information: this.state.account_information,
      website_id: this.state.website.id
    }));
  };
  close = () => {
    this.props.modal.reject();
  };
  edit = () => {
    this.props.modal.resolve({account_information: this.state.account_information, check: this.state.check});
  };
  render() {
    return (
      <SimpleModalTemplate
          onClose={this.close}
          headerContent={"Account Update"}>
        <Container className="app_settings_modal">
          <div className="app_name_container display-flex align_items_center">
            <div className="squared_image_handler">
              <img src={this.state.website.logo} alt="Website logo"/>
            </div>
            <span className="app_name">{this.state.website.app_name}</span>
          </div>
          <Form onSubmit={this.edit} error={this.state.error.length > 0} id='add_bookmark_form'>
            <CredentialInputs
              toggle={this.toggleCredentialEdit}
              seePassword={this.state.seePassword}
              handleChange={this.handleCredentialsInput}
              toggleSeePassword={this.toggleSeePassword}
              information={this.state.website.information}
              account_information={this.state.account_information}/>
            <span id='test_credentials' onClick={this.testConnection}>Test connection <Icon color='green' name='magic'/></span>
            <Form.Field>
              <div style={{fontWeight:'bold'}}>Which App do you want to update with this account?</div>
            </Form.Field>
            <Form.Field className='choose_type_app'>
              <Checkbox radio
                        name='check'
                        value='Simple'
                        onChange={this.handleChange}
                        label={this.state.website.app_name + ', ' + this.props.modal.team.name + ', #' + this.props.modal.room.name}
                        checked={this.state.check === 'Simple'}/>
              <Checkbox radio
                        name='check'
                        value='newApp'
                        label='Create a new app'
                        onChange={this.handleChange}
                        checked={this.state.check === 'newApp'}/>
            </Form.Field>
            <Message error content={this.state.error}/>
            <Button
              positive
              type="submit"
              className="modal-button"
              content="SAVE ACCOUNT..."
              loading={this.state.loading}
              disabled={this.state.loading}/>
          </Form>
        </Container>
      </SimpleModalTemplate>
    )
  }
}

export default AccountUpdateModal;