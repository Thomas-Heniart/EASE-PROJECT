import React from 'react';
import {connect} from "react-redux";
import CredentialInputs from "./CredentialInputs";
import {handleSemanticInput} from "../../../utils/utils";
import SimpleModalTemplate from "../../common/SimpleModalTemplate";
import {editAppCredentials} from "../../../actions/dashboardActions";
import {deleteUpdate, sendUpdateToAdmin, testCredentials} from "../../../actions/catalogActions";
import { Container, Icon, Form, Message, Button, Label } from 'semantic-ui-react';
import {teamEditEnterpriseCardReceiver, teamEditSingleCardCredentials} from "../../../actions/appsActions";

@connect(store => ({
  modal: store.modals.passwordUpdate,
  dashboard: store.dashboard,
  teams: store.teams,
  team_apps: store.team_apps
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
      account_information: this.props.modal.item.account_information,
      app: this.props.dashboard.apps[this.props.modal.item.app_id],
      team: this.props.modal.item.team_id !== -1 ? this.props.teams[this.props.modal.item.team_id] : -1,
      room: this.props.modal.item.team_id !== -1 ? this.props.teams[this.props.modal.item.team_id].rooms[this.props.team_apps[this.props.modal.item.team_card_id].channel_id] : -1,
      editCredentials: {}
    }
  }
  componentWillMount() {
    let edit = {};
    Object.keys(this.state.account_information).map(item => {
      if (item !== 'login')
        edit[item] = false
    });
    this.setState({editCredentials: edit});
  }
  handleInput = handleSemanticInput.bind(this);
  handleCredentialsInput = (e, {name, value}) => {
    let account_information = {...this.state.account_information};
    account_information[name] = value;
    this.setState({account_information: account_information});
  };
  toggleCredentialEdit = (name) => {
    const editCredentials = {};
    Object.keys(this.state.editCredentials).map(item => {
      if (item === name)
        editCredentials[item] = !this.state.editCredentials[item];
      else
        editCredentials[item] = this.state.editCredentials[item];
    });
    this.setState({editCredentials: editCredentials});
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
  finish = () => {
    this.props.dispatch(deleteUpdate({id: this.props.modal.item.id})).then(() => {
      this.setState({loading: false});
      this.props.modal.resolve();
    });
  };
  edit = () => {
    this.setState({loading: true});
    if (this.props.modal.item.team_card_id !== -1) {
      if (this.state.app.type === 'teamEnterpriseApp') {
        this.props.dispatch(teamEditEnterpriseCardReceiver({
          team_id: this.state.team.id,
          team_card_id: this.props.modal.item.team_card_id,
          team_card_receiver_id: this.props.team_apps[this.props.modal.item.team_card_id].receivers.filter(receiver => {
            return this.state.team.my_team_user_id === receiver.team_user_id
          })[0].id,
          account_information: this.state.account_information
        })).then(response => {
          this.finish();
        });
      }
      else {
        if (this.state.team.team_users[this.state.team.my_team_user_id].role > 1
          || this.props.team_apps[this.state.app.team_card_id].team_user_filler_id === this.state.team.my_team_user_id) {
          this.props.dispatch(teamEditSingleCardCredentials({
            team_card: this.props.team_apps[this.props.modal.item.team_card_id],
            account_information: this.state.account_information
          })).then(response => {
            this.finish();
          });
        }
        else
          this.props.dispatch(sendUpdateToAdmin({
            id: this.props.modal.item.id,
            account_information: this.state.account_information
          })).then(() => {
            this.finish();
          });
      }
    }
    else {
      this.props.dispatch(editAppCredentials({
        app: this.state.app,
        account_information: this.state.account_information
      })).then(() => {
        this.finish();
      });
    }
  };
  render() {
    return (
      <SimpleModalTemplate
        onClose={this.close}
        headerContent={"Password Update detected"}>
        <Container className="app_settings_modal">
          <div className="app_name_container display-flex align_items_center">
            <div className="squared_image_handler">
              <img src={this.state.website.logo} alt="Website logo"/>
            </div>
            <div className="display_flex flex_direction_column team_app_settings_name">
              <span className="app_name">{this.state.website.app_name}</span>
              {this.state.team !== -1 &&
                <React.Fragment>
                  <div>
                    <Label className="team_name" icon={<Icon name="users" class="mrgnRight5"/>} size="tiny" content={this.state.team.name}/>
                  </div>
                  <span className="room_name"># {this.state.room.name}</span>
                </React.Fragment>}
            </div>
          </div>
          {this.props.modal.item.team_user_id !== -1 &&
            <div className='ui labels' style={{display:'inline-flex'}}>
              <p style={{marginTop:'4px', fontWeight:'bold'}}>Password suggested by: </p>
              <Label className='user-label'>{this.state.team.team_users[this.props.modal.item.team_user_id].username}</Label>
            </div>}
          {this.state.team !== -1 && this.state.team.team_users[this.state.team.my_team_user_id].role > 1 &&
          <p>Modifications will be applied to your Team.</p>}
          {this.state.team !== -1 && this.state.team.team_users[this.state.team.my_team_user_id].role < 2 &&
          <p>Modifications will be applied to you and suggested to the Admin of {this.state.website.app_name}.</p>}
          <Form onSubmit={this.edit} error={this.state.error.length > 0} id='add_bookmark_form'>
            <CredentialInputs
              edit={this.state.editCredentials}
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