import React, {Component} from "react";
import {showTeamEditEnterpriseAppModal} from '../../actions/teamModalActions';
import {teamEditEnterpriseCardReceiver} from "../../actions/appsActions";
import api from "../../utils/api";
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {Label, Container, Icon, Form, Input, Popup, Button, Message } from 'semantic-ui-react';
import {
  credentialIconType,
  transformCredentialsListIntoObject,
  transformWebsiteInfoIntoListAndSetValues
} from "../../utils/utils";
import {connect} from "react-redux";
import {testCredentials} from "../../actions/catalogActions";

const CredentialInput = ({item, onChange}) => {
  return (
      <Form.Field>
        <label style={{fontSize: '16px', fontWeight: '300', color: '#424242'}}>{item.placeholder}</label>
        <Input size="large"
               autoFocus={item.autoFocus}
               class="modalInput team-app-input"
               required
               autoComplete='on'
               name={item.name ? item.name : item.information_name}
               onChange={onChange}
               label={<Label><Icon name={credentialIconType[item.name]}/></Label>}
               labelPosition="left"
               placeholder={item.placeholder}
               value={item.value}
               type={item.type ? item.type : item.information_type}/>
      </Form.Field>
  )
};

@connect(store => ({
  teams: store.teams,
  team_card: store.team_apps[store.teamModals.teamEditEnterpriseAppModal.team_card_id]
}))
class EditEnterpriseAppModal extends Component {
  constructor(props){
    super(props);
    this.state = {
      loading: false,
      errorMessage: '',
      team_card: this.props.team_card,
      my_id: this.props.teams[this.props.team_card.team_id].my_team_user_id,
      credentials: []
    };
    const team_card = this.props.team_card;
    const meReceiver = this.state.team_card.receivers.find(receiver => (receiver.team_user_id === this.state.my_id));
    this.state.credentials = transformWebsiteInfoIntoListAndSetValues(!!team_card.website ? team_card.website.information : team_card.software.connection_information, meReceiver.account_information);
  };
  handleCredentialInput = (e, {name, value}) => {
    const credentials = this.state.credentials.map(item => {
      if (name === item.name)
        item.value = value;
      return item;
    });
    this.setState({credentials: credentials});
  };
  testConnection = () => {
    this.props.dispatch(testCredentials({
      account_information: transformCredentialsListIntoObject(this.state.credentials),
      website_id: this.props.team_card.website.id
    }));
  };
  confirm = (e) => {
    e.preventDefault();
    this.setState({loading: true});
    const team_card = this.state.team_card;
    const meReceiver = team_card.receivers.find(receiver => (receiver.team_user_id === this.state.my_id));

    this.props.dispatch(teamEditEnterpriseCardReceiver({
      team_id:team_card.team_id,
      team_card_id: team_card.id,
      team_card_receiver_id: meReceiver.id,
      account_information: transformCredentialsListIntoObject(this.state.credentials)
    })).then(() => {
      this.close();
    }).catch(err => {
      this.setState({loading: false, errorMessage: err});
    });
  };
  componentWillMount(){
    const meReceiver = this.state.team_card.receivers.find(receiver => (receiver.team_user_id === this.state.my_id));

    api.dashboard.getAppPassword({
      app_id: meReceiver.app_id
    }).then(response => {
      const pwd = response.password;

      const credentials = this.state.credentials.map(item => {
        if (item.name === 'password')
          item.value = pwd;
        return item;
      });
      this.setState({credentials: credentials});
    });
  }
  close = () => {
    this.props.dispatch(showTeamEditEnterpriseAppModal({
      active: false
    }));
  };
  render(){
    const {team_card} = this.state;
    const credentialsInputs = this.state.credentials.map((item,idx) => {
      return <CredentialInput key={idx} onChange={this.handleCredentialInput} item={item}/>;
    });

    return (
        <SimpleModalTemplate
            onClose={this.close}
            headerContent={'App credentials'}>
          <Container>
            <div className="display-flex align_items_center" style={{marginBottom: '30px'}}>
              <div className="squared_image_handler">
                <img src={!!team_card.website ? team_card.website.logo : team_card.software.logo} alt="Website logo"/>
              </div>
              <span className="app_name">{team_card.name}</span>
            </div>
            <Form onSubmit={this.confirm} error={!!this.state.errorMessage.length}>
              {credentialsInputs}
              <Message error content={this.state.errorMessage}/>
              {this.props.team_card.sub_type === 'classic' &&
              <span id='test_credentials' onClick={this.testConnection}>Test connection <Icon color='green' name='magic'/></span>}
              <Button
                  type="submit"
                  disabled={this.state.loading}
                  loading={this.state.loading}
                  positive
                  className="modal-button uppercase"
                  content={'CONFIRM'}/>
            </Form>
          </Container>
        </SimpleModalTemplate>
    )
  }
}

module.exports = EditEnterpriseAppModal;