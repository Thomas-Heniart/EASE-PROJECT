import React from 'react';
import {connect} from "react-redux";
import CredentialInputs from "./CredentialInputs";
import SimpleModalTemplate from "../../common/SimpleModalTemplate";
import {handleSemanticInput, logoLetter} from "../../../utils/utils";
import {editAppCredentials, updateAccepted} from "../../../actions/dashboardActions";
import {Container, Icon, Form, Message, Button, Checkbox} from 'semantic-ui-react';
import {deleteUpdate, sendUpdateToAdmin, testCredentials} from "../../../actions/catalogActions";
import {teamEditEnterpriseCardReceiver, teamEditSingleCardCredentials} from "../../../actions/appsActions";

@connect(store => ({
  apps: store.dashboard.apps,
  modal: store.modals.onlyPasswordUpdate,
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
      account_information: {},
      editCredentials: {},
      options: {}
    }
  }
  componentWillMount() {
    let edit = {};
    let acc_info = {};
    let options = {};
    Object.keys(this.props.modal.item.account_information).map(item => {
      acc_info[item] = this.props.modal.item.account_information[item];
    });
    Object.keys(acc_info).map(item => {
      if (item !== 'login')
        edit[item] = false
    });
    Object.keys(this.props.apps).filter(app_id => {
      const app = this.props.apps[app_id];
      if ((app.website.id === this.props.item.website_id) || (app.website.login_url === this.props.item.url))
        options[app_id] = {
          app: app,
          card: this.props.team_apps[app.team_card_id] ? this.props.team_apps[app.team_card_id] : null
        };
    });
    this.setState({options: options, editCredentials: edit, account_information: acc_info});
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
  edit = () => {
    this.setState({loading: true});
    let acc_info = {};
    Object.keys(this.state.account_information).map(item => {
      acc_info[item] = this.state.account_information[item];
    });
    if (this.props.modal.item.team_card_id !== -1) {
      if (this.props.team_apps[this.props.modal.item.team_card_id].type === 'teamEnterpriseCard') {
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
        if (this.state.team.team_users[this.state.team.my_team_user_id].role > 1
          || this.props.team_apps[this.state.app.team_card_id].team_user_filler_id === this.state.team.my_team_user_id) {
          this.props.dispatch(teamEditSingleCardCredentials({
            team_card: this.props.team_apps[this.props.modal.item.team_card_id],
            account_information: acc_info
          })).then(response => {
            this.finish();
          });
        }
        else
          this.props.dispatch(sendUpdateToAdmin({
            id: this.props.modal.item.id,
            account_information: acc_info
          })).then(() => {
            this.setState({loading: false});
            this.props.modal.resolve();
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
    this.props.dispatch(updateAccepted({
      type: "PasswordUpdate"
    }))
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
  render() {
    const item = this.props.modal.item;
    const website = this.state.website;
    return (
      <SimpleModalTemplate
        onClose={this.close}
        headerContent={"Password Update detected"}>
        <Container className="app_settings_modal">
          <div className="app_name_container display-flex align_items_center">
            {website.logo && website.logo !== '' ?
              <div className="squared_image_handler">
                <img src={website.logo} alt="Website logo"/>
              </div>
              :
              <div className="squared_image_handler" style={{backgroundColor:'#373b60',color:'white',fontSize:'24px',backgroundSize:'cover',display:'flex'}}>
                <div style={{margin:'auto'}}>
                  <p style={{margin:'auto'}}>{logoLetter(this.state.appName)}</p>
                </div>
              </div>}
            {/*<div className="squared_image_handler">*/}
              {/*<img src={website.logo} alt="Website logo"/>*/}
            {/*</div>*/}
            <div className="display_flex flex_direction_column team_app_settings_name">
              <span className="app_name">{website.app_name}</span>
            </div>
          </div>
          {/*{this.state.team !== -1 && this.state.team.team_users[this.state.team.my_team_user_id].role > 1 &&*/}
          {/*<p>Modifications will be applied to your Team.</p>}*/}
          {/*{this.state.team !== -1 && this.state.team.team_users[this.state.team.my_team_user_id].role < 2 &&*/}
          {/*<p>Modifications will be applied to you and suggested to the Admin of {website.app_name}.</p>}*/}
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
              onlyPassword={true}
              information={website.information}
              account_information={this.state.account_information}/>
            {((item.app_id !== -1 && this.state.app.type !== 'anyApp')
              || (item.team_card_id !== -1 && this.props.team_apps[item.team_card_id].sub_type !== 'any')) &&
            <span id='test_credentials' onClick={this.testConnection}>
              Test connection
              <Icon color='green' name='magic'/>
            </span>}
            <Message error content={this.state.error}/>
            <Form.Field className='choose_type_app'>
              {Object.keys(this.state.options).map(app_id => {
                const app = this.state.options[app_id].app;
                const card = this.state.options[app_id].card;
                const team = app.team_id ? this.props.teams[app.team_id] : null;
                const room = team ? team.rooms[card.channel_id] : null;
                return <Checkbox radio
                                 name='check'
                                 key={app_id}
                                 value={app_id}
                                 onChange={this.handleChange}
                                 style={{margin: "0 0 10px 0"}}
                                 checked={this.state.check === app_id}
                                 label={`${card ? card.name : app.name}${team ? ', ' + team.name : null}${room ? ', #' + room.name : null}, ${app.account_information.login}`}/>})}
              <Checkbox radio
                        name='check'
                        value='newApp'
                        label='Create a new app'
                        onChange={this.handleChange}
                        checked={this.state.check === 'newApp'}/>
            </Form.Field>
            <Button
              positive
              type="submit"
              className="modal-button"
              content={(this.state.team === -1 || this.state.team.team_users[this.state.team.my_team_user_id].role > 1
                || this.props.team_apps[this.state.app.team_card_id].team_user_filler_id === this.state.team.my_team_user_id
                || this.props.team_apps[this.state.app.team_card_id].type === 'teamEnterpriseCard') ? "SAVE NEW PASSWORD" : "SAVE AND SUGGEST NEW PASSWORD"}
              loading={this.state.loading}
              disabled={this.state.loading}/>
          </Form>
        </Container>
      </SimpleModalTemplate>
    )
  }
}

export default PasswordUpdateModal;