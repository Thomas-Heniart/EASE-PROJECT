import React from 'react';
import {connect} from "react-redux";
import CredentialInputs from "./CredentialInputs";
import {handleSemanticInput} from "../../../utils/utils";
import {testCredentials} from "../../../actions/catalogActions";
import SimpleModalTemplate from "../../common/SimpleModalTemplate";
import { Container, Icon, Form, Message, Button, Label } from 'semantic-ui-react';

@connect(store => ({
  modal: store.modals.passwordUpdate
}))
class PasswordUpdateModal extends React.Component {
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
    this.props.modal.resolve({account_information: this.state.account_information});
  };
  render() {
    return (
      <SimpleModalTemplate
        onClose={this.close}
        headerContent={"Password Update"}>
        <Container className="app_settings_modal">
          <div className="app_name_container display-flex align_items_center">
            <div className="squared_image_handler">
              <img src={this.state.website.logo} alt="Website logo"/>
            </div>
            <div className="display_flex flex_direction_column team_app_settings_name">
              <span className="app_name">{this.state.website.app_name}</span>
              {this.props.modal.team !== -1 &&
                <React.Fragment>
                  <div>
                    <Label className="team_name" icon={<Icon name="users" class="mrgnRight5"/>} size="tiny" content={this.props.modal.team.name}/>
                  </div>
                  <span className="room_name"># {this.props.modal.room.name}</span>
                </React.Fragment>}
            </div>
          </div>
          {this.props.modal.team_user_id !== -1 &&
            <div>
              <p>Password suggested by: </p>
              <div>{this.props.modal.team.team_users[this.props.modal.team_user_id].name}</div>
            </div>}
          {this.props.modal.team !== -1 && this.props.modal.team.team_users[this.props.modal.team.my_team_user_id].role > 1 &&
          <p>Modifications will be applied to your Team.</p>}
          {this.props.modal.team !== -1 && this.props.modal.team.team_users[this.props.modal.team.my_team_user_id].role < 2 &&
          <p>Modifications will be applied to you and suggested to the Admin of {this.state.website.app_name}.</p>}
          <Form onSubmit={this.edit} error={this.state.error.length > 0} id='add_bookmark_form'>
            <CredentialInputs
              toggle={this.toggleCredentialEdit}
              seePassword={this.state.seePassword}
              handleChange={this.handleCredentialsInput}
              toggleSeePassword={this.toggleSeePassword}
              information={this.state.website.information}
              account_information={this.state.account_information}/>
            <span id='test_credentials' onClick={this.testConnection}>Test connection <Icon color='green' name='magic'/></span>
            <Message error content={this.state.error}/>
            <Button
              positive
              type="submit"
              className="modal-button"
              content="SAVE NEW PASSWORD"
              loading={this.state.loading}
              disabled={this.state.loading}/>
          </Form>
        </Container>
      </SimpleModalTemplate>
    )
  }
}

export default PasswordUpdateModal;