import React, {Component, Fragment} from "react";
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import { Form, Button, Message, Label, Input, Icon, Segment, Checkbox, Container } from 'semantic-ui-react';
import {transformWebsiteInfoIntoList, credentialIconType, transformCredentialsListIntoObject} from "../../utils/utils";
import {showFillSimpleCardCredentialsModal} from "../../actions/teamModalActions";
import {teamEditSingleCardFiller, teamEditSingleCardCredentials} from "../../actions/appsActions";
import {teamUserRoles} from "../../utils/utils";
import {connect} from "react-redux";

const CredentialInput = ({item, onChange}) => {
  return (
      <Form.Field>
        <label style={{fontSize: '16px', fontWeight: '300', color: '#424242'}}>{item.placeholder}</label>
        <Input size="large"
               autoFocus={item.autoFocus}
               class="modalInput team-app-input"
               required
               autoComplete='on'
               name={item.name}
               onChange={onChange}
               label={<Label><Icon name={credentialIconType[item.name]}/></Label>}
               labelPosition="left"
               placeholder={item.placeholder}
               value={item.value}
               type={item.type}/>
      </Form.Field>
  )
};

@connect(store => ({
  team_card: store.teamModals.fillSimpleCardCredentialsModal.team_card
}))
class FillSimpleCardCredentialsModal extends Component {
  constructor(props){
    super(props);
    this.state = {
      credentials: [],
      loading: false,
      errorMessage: ''
    };
    const team_card = this.props.team_card;
    this.state.credentials = transformWebsiteInfoIntoList(!!team_card.website ? team_card.website.information : team_card.software.connection_information);
  }
  handleCredentialInput = (e, {name, value}) => {
    let credentials = this.state.credentials.map(item => {
      if (name === item.name)
        item.value = value;
      return item;
    });
    this.setState({credentials: credentials});
  };
  close = () => {
    this.props.dispatch(showFillSimpleCardCredentialsModal({
      active: false
    }));
  };
  submit = (e) => {
    e.preventDefault();
    const {team_card} = this.props;
    this.setState({loading: true, errorMessage:''});
    this.props.dispatch(teamEditSingleCardCredentials({
      team_card: team_card,
      account_information: transformCredentialsListIntoObject(this.state.credentials)
    })).then(response => {
      this.props.closeModal();
    }).catch(err => {
      this.setState({loading: false, errorMessage: err});
    })
  };
  render(){
    const {team_card} = this.props;
    const credentialsInputs = this.state.credentials.map((item,idx) => {
      return <CredentialInput key={idx} onChange={this.handleCredentialInput} item={item}/>;
    });

    return (
        <SimpleModalTemplate
            headerContent={'App credentials'}
            onClose={this.close}>
          <Container>
            <div className="display-flex align_items_center" style={{marginBottom: '30px'}}>
              <div className="squared_image_handler">
                <img src={!!team_card.website ? team_card.website.logo : team_card.software.logo} alt="Website logo"/>
              </div>
              <span className="app_name">{team_card.name}</span>
            </div>
            <Form onSubmit={this.submit} error={!!this.state.errorMessage.length}>
              {credentialsInputs}
              <Message error content={this.state.errorMessage}/>
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

export default FillSimpleCardCredentialsModal;