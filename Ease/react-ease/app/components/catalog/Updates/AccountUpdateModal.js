import React from 'react';
import {connect} from "react-redux";
import CredentialInputs from "./CredentialInputs";
import {handleSemanticInput} from "../../../utils/utils";
import SimpleModalTemplate from "../../common/SimpleModalTemplate";
import {appAdded, editAppCredentials, updateAccepted} from "../../../actions/dashboardActions";
import { Container, Icon, Form, Message, Button, Checkbox } from 'semantic-ui-react';
import {deleteUpdate, newAccountUpdateModal, testCredentials} from "../../../actions/catalogActions";
import {teamEditEnterpriseCardReceiver, teamEditSingleCardCredentials} from "../../../actions/appsActions";

@connect(store => ({
  modal: store.modals.accountUpdate,
  dashboard: store.dashboard,
  teams: store.teams,
  team_apps: store.team_apps,
  sso_list: store.catalog.sso_list
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
  handleChange = (e, {value}) => this.setState({check: value});
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
  newAccount = () => {
    let website = this.props.modal.website;
    const item = this.props.modal.item;
    if (this.props.sso_list[0].websites[0].filter(website_id => (website_id === item.website_id)).length > 0) {
      website = {...this.props.sso_list[0]};
      website.logo = this.state.website.logo;
      website.id = item.website_id;
      website.update_id = item.id;
      website.sso_id = this.props.sso_list[0].id;
      website.sso_group_id = -1;
      website.information = {
        login: {name: 'login', placeholder: "Login", priority: 0, type: "text"},
        password: {name: 'password', placeholder: "Password", priority: 1, type: "password"}
      };
      website.app_name = website.name;
    }
    newAccountUpdateModal(
      this.props.dispatch,
      website,
      this.state.account_information
    );
    this.props.modal.reject();
  };
  edit = () => {
    this.setState({loading: true});
    let acc_info = {};
    Object.keys(this.state.account_information).map(item => {
      acc_info[item] = this.state.account_information[item];
    });
    if (this.state.check === 'Simple') {
      if (this.props.modal.item.team_card_id !== -1) {
        if (this.state.app.type === 'teamEnterpriseApp') {
          this.props.dispatch(teamEditEnterpriseCardReceiver({
            team_id: this.state.team.id,
            team_card_id: this.props.modal.item.team_card_id,
            team_card_receiver_id: this.props.team_apps[this.props.modal.item.team_card_id].receivers.filter(receiver => {
              return this.state.team.my_team_user_id === receiver.team_user_id
            })[0].id,
            account_information: acc_info
          })).then(response => {
            this.finish();
          });
        }
        else {
          this.props.dispatch(teamEditSingleCardCredentials({
            team_card: this.props.team_apps[this.props.modal.item.team_card_id],
            account_information: acc_info
          })).then(response => {
            this.finish();
          });
        }
      }
      else {
        this.props.dispatch(editAppCredentials({
          app: this.state.app,
          account_information: acc_info
        })).then(() => {
          this.finish();
        });
      }
    }
    else
      this.newAccount();
    this.props.dispatch(appAdded({
      app: this.props.app,
      from: "AccountUpdate"
    }));
    this.props.dispatch(updateAccepted({
      type: "AccountUpdate"
    }))
  };
  finish = () => {
    this.props.dispatch(deleteUpdate({id: this.props.modal.item.id})).then(() => {
      this.setState({loading: false});
      this.props.modal.resolve();
    });
  };
  render() {
    const item = this.props.modal.item;
    const website = this.state.website;
    return (
      <SimpleModalTemplate
          onClose={this.close}
          headerContent={"Account Update"}>
        <Container className="app_settings_modal">
          <div className="app_name_container display-flex align_items_center">
            <div className="squared_image_handler">
              <img src={website.logo} alt="Website logo"/>
            </div>
            <span className="app_name">{website.app_name}</span>
          </div>
          <Form onSubmit={this.edit} error={this.state.error.length > 0} id='add_bookmark_form'>
            <CredentialInputs
              url={(item.app_id !== -1 && this.state.app.type === 'anyApp')
              || (item.team_card_id !== -1
                && this.props.team_apps[item.team_card_id].sub_type === 'any') ? website.login_url : -1}
              edit={this.state.editCredentials}
              toggle={this.toggleCredentialEdit}
              seePassword={this.state.seePassword}
              handleChange={this.handleCredentialsInput}
              toggleSeePassword={this.toggleSeePassword}
              information={website.information}
              account_information={this.state.account_information}/>
            {((item.app_id !== -1 && this.state.app.type !== 'anyApp')
            || (item.team_card_id !== -1 && this.props.team_apps[item.team_card_id].sub_type !== 'any')) &&
            <span id='test_credentials' onClick={this.testConnection}>Test connection <Icon color='green' name='magic'/></span>}
            <Form.Field>
              <div style={{fontWeight:'bold'}}>Which App do you want to update with this account?</div>
            </Form.Field>
            <Form.Field className='choose_type_app'>
              <Checkbox radio
                        name='check'
                        value='Simple'
                        onChange={this.handleChange}
                        label={website.app_name + ', ' + this.state.team.name + ', #' + this.state.room.name}
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
              disabled={this.state.loading || this.state.check === ''}/>
          </Form>
        </Container>
      </SimpleModalTemplate>
    )
  }
}

export default AccountUpdateModal;