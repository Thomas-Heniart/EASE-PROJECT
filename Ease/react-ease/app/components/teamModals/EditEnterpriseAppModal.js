import React, {Component} from "react";
import {showTeamEditEnterpriseAppModal, showPinTeamAppToDashboardModal} from '../../actions/teamModalActions';
import {teamEditEnterpriseCardReceiver} from "../../actions/appsActions";
import api from "../../utils/api";
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {findMeInReceivers} from "../../utils/helperFunctions";
import { Header, Label,List, Search,SearchResult, Container, Divider, Icon, Transition, TextArea, Segment, Checkbox, Form, Input, Select, Dropdown, Button, Message } from 'semantic-ui-react';
import {
  transformWebsiteInfoIntoList, transformCredentialsListIntoObject,
  transformWebsiteInfoIntoListAndSetValues
} from "../../utils/utils";
import {connect} from "react-redux";

const CredentialInput = ({item, onChange}) => {
  return <Input
      class="team-app-input"
      name={item.name}
      autoFocus={item.autoFocus}
      onChange={onChange}
      required
      placeholder={item.placeholder}
      value={item.value}
      type={item.type}/>;
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
    const meReceiver = this.state.team_card.receivers.find(receiver => (receiver.team_user_id === this.state.my_id));
    this.state.credentials = transformWebsiteInfoIntoListAndSetValues(this.state.team_card.website.information, meReceiver.account_information);
  };
  handleCredentialInput = (e, {name, value}) => {
    const credentials = this.state.credentials.map(item => {
      if (name === item.name)
        item.value = value;
      return item;
    });
    this.setState({credentials: credentials});
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
    const app = this.props.team_card;
    return (
        <SimpleModalTemplate
            onClose={this.close}
            headerContent={'Enter your info, last time ever'}>
          <Form class="container" onSubmit={this.confirm} error={this.state.errorMessage.length > 0}>
            <Form.Field class="display-flex align_items_center" style={{marginBottom: '35px'}}>
              <div class="squared_image_handler">
                <img src={app.website.logo} alt="Website logo"/>
              </div>
              <span style={{fontSize: "1.3rem"}}>{app.name}</span>
            </Form.Field>
            {this.state.credentials.map(item => {
              return (
                  <Form.Field key={item.priority}>
                    <label>
                      {item.placeholder}
                    </label>
                    <CredentialInput item={item} onChange={this.handleCredentialInput}/>
                  </Form.Field>
              )
            })}
            <Message error content={this.state.errorMessage}/>
            <Button
                type="submit"
                loading={this.state.loading}
                positive
                className="modal-button"
                content="CONFIRM"/>
          </Form>
        </SimpleModalTemplate>
    )
  }
}

module.exports = EditEnterpriseAppModal;