import React, {Component} from "react";
import {showTeamJoinEnterpriseAppModal} from '../../actions/teamModalActions';
import {teamShareEnterpriseCard} from "../../actions/appsActions";
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import { Header, Label,List, Search,SearchResult, Container, Divider, Icon, Transition, TextArea, Segment, Checkbox, Form, Input, Select, Dropdown, Button, Message } from 'semantic-ui-react';
import {transformWebsiteInfoIntoList, transformCredentialsListIntoObject} from "../../utils/utils";
import {connect} from "react-redux";

const CredentialInput = ({item, onChange}) => {
  return <Input
      class="team-app-input"
      name={item.name}
      autoFocus={item.autoFocus}
      onChange={onChange}
      placeholder={item.placeholder}
      value={item.value}
      required
      type={item.type}/>;
};

@connect(store => ({
  teams: store.teams,
  team_card: store.team_apps[store.teamModals.teamJoinEnterpriseAppModal.team_card_id]
}))
class JoinEnterpriseAppModal extends Component {
  constructor(props){
    super(props);
    this.state = {
      loading: false,
      errorMessage: '',
      team_card: this.props.team_card,
      my_id: this.props.teams[this.props.team_card.team_id].my_team_user_id,
      credentials: transformWebsiteInfoIntoList(this.props.team_card.website.information),
    }
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
    this.props.dispatch(teamShareEnterpriseCard({
      team_id: this.state.team_card.team_id,
      team_card_id: this.state.team_card.id,
      team_user_id: this.state.my_id,
      account_information: transformCredentialsListIntoObject(this.state.credentials)
    })).then(() => {
      this.close();
    }).catch(err => {
      this.setState({loading: false, errorMessage: err});
    });
  };
  close = () => {
    this.props.dispatch(showTeamJoinEnterpriseAppModal({active: false}));
  };
  render(){
    const team_card = this.state.team_card;
    return (
        <SimpleModalTemplate
            onClose={this.close}
            headerContent={'Enter your info, last time ever'}>
          <Form class="container" onSubmit={this.confirm} error={!!this.state.errorMessage.length}>
            <Form.Field class="display-flex align_items_center" style={{marginBottom: '35px'}}>
              <div class="squared_image_handler">
                <img src={team_card.website.logo} alt="Website logo"/>
              </div>
              <span style={{fontSize: "1.3rem"}}>{team_card.name}</span>
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
                attached='bottom'
                type="submit"
                loading={this.state.loading}
                positive
                onClick={this.confirm}
                className="modal-button"
                content="CONFIRM"/>
          </Form>
        </SimpleModalTemplate>
    )
  }
}

module.exports = JoinEnterpriseAppModal;