import React, {Component} from "react";
import {showTeamAskJoinMultiAppModal} from '../../actions/teamModalActions';
import {teamJoinEnterpriseApp} from "../../actions/appsActions";
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
      type={item.type}/>;
};

@connect(store => ({
  app: store.teamModals.teamAskJoinEnterpriseAppModal.app,
  user: store.teamModals.teamAskJoinEnterpriseAppModal.user,
  team_id: store.team.id
}))
class AskJoinEnterpriseAppModal extends Component {
  constructor(props){
    super(props);
    this.state = {
      loading: false,
      errorMessage: '',
      credentials: transformWebsiteInfoIntoList(this.props.app.website.information)
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
    this.props.dispatch(teamJoinEnterpriseApp({
      team_id: this.props.team_id,
      app_id: this.props.app.id,
      account_information: transformCredentialsListIntoObject(this.state.credentials)
    })).then(() => {
      this.props.dispatch(showTeamAskJoinMultiAppModal(false));
    }).catch(err => {
      this.setState({loading: false, errorMessage: err});
    });
  };
  render(){
    const app = this.props.app;
    return (
        <SimpleModalTemplate
            onClose={e => {this.props.dispatch(showTeamAskJoinMultiAppModal(false))}}
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

module.exports = AskJoinEnterpriseAppModal;