import React, {Component} from "react";
import {showPinTeamAppToDashboardModal, showTeamAcceptMultiAppModal} from '../../actions/teamModalActions';
import {teamAcceptEnterpriseCard} from "../../actions/appsActions";
import api from "../../utils/api";
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {findMeInReceivers} from "../../utils/helperFunctions";
import {Button, Form, Input, Message} from 'semantic-ui-react';
import {transformCredentialsListIntoObject, transformWebsiteInfoIntoList} from "../../utils/utils";
import {connect} from "react-redux";

const CredentialInput = ({item, onChange, required}) => {
  return <Input
      class="team-app-input"
      name={item.name}
      autoFocus={item.autoFocus}
      onChange={onChange}
      required={required}
      placeholder={required ? item.placeholder : `${item.placeholder} (Optional)`}
      value={item.value}
      type={item.type}/>;
};

@connect(store => ({
  app: store.teamModals.teamAcceptMultiAppModal.app,
  user: store.teamModals.teamAcceptMultiAppModal.user,
    team_id: store.team.id,
    plan_id: store.team.plan_id
}))
class AcceptEnterpriseAppModal extends Component {
  constructor(props){
    super(props);
    this.state = {
      loading: false,
      errorMessage: '',
      credentials: transformWebsiteInfoIntoList(this.props.app.website.information)
    }
  };
  checkInputs = () => {
      if (this.props.plan_id > 0)
          return false;
    for (let i = 0; i < this.state.credentials.length; i++){
      if (this.state.credentials[i].value.length === 0)
        return true;
    }
    return false;
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
    const meReceiver = findMeInReceivers(this.props.app.receivers, this.props.user.id);
    this.props.dispatch(teamAcceptEnterpriseCard({
      team_id: this.props.team_id,
      app_id: this.props.app.id,
      shared_app_id: meReceiver.shared_app_id,
      account_information: transformCredentialsListIntoObject(this.state.credentials)
    })).then(() => {
      this.props.dispatch(showTeamAcceptMultiAppModal(false));
      this.props.dispatch(showPinTeamAppToDashboardModal(true, this.props.app));
    }).catch(err => {
      this.setState({loading: false, errorMessage: err});
    });
  };
  componentDidMount(){
    const meReceiver = findMeInReceivers(this.props.app.receivers, this.props.user.id);

    api.teamApps.getSharedAppPassword({team_id: this.props.team_id, shared_app_id: meReceiver.shared_app_id}).then(password => {
      const credentials = this.state.credentials.map(item => {
        if (item.name === 'password')
          item.value = password;
        else
          item.value = meReceiver.account_information[item.name];
        return item;
      });
      this.setState({credentials: credentials});
    });
  }
  render(){
    const app = this.props.app;
    return (
        <SimpleModalTemplate
            onClose={e => {this.props.dispatch(showTeamAcceptMultiAppModal(false))}}
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
                      <CredentialInput required={this.props.plan_id === 0} item={item}
                                       onChange={this.handleCredentialInput}/>
                  </Form.Field>
              )
            })}
            <Message error content={this.state.errorMessage}/>
            <Button
                attached='bottom'
                type="submit"
                loading={this.state.loading}
                disabled={this.checkInputs()}
                positive
                onClick={this.confirm}
                className="modal-button"
                content="CONFIRM"/>
          </Form>
        </SimpleModalTemplate>
    )
  }
}

module.exports = AcceptEnterpriseAppModal;