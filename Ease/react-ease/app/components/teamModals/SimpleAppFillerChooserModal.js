import React, {Component, Fragment} from "react";
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import { Form, Button, Message, Label, Input, Icon, Segment, Checkbox, Container } from 'semantic-ui-react';
import {transformWebsiteInfoIntoList, credentialIconType, transformCredentialsListIntoObject} from "../../utils/utils";
import {showSimpleAppFillerChooserModal} from "../../actions/teamModalActions";
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

class ChooseMemberStep extends Component {
  constructor(props){
    super(props);
    this.state = {
      team_user_id: this.props.team_card.team_user_filler_id,
      loading: false,
      errorMessage: ''
    }
  }
  validate = (e) => {
    e.preventDefault();
    const {team_card, team} = this.props;
    const me = team.team_users[team.my_team_user_id];

    if (this.state.team_user_id !== me.id){
      this.setState({loading: true, errorMessage:''});
      this.props.dispatch(teamEditSingleCardFiller({
        team_id: team.id,
        team_card_id: team_card.id,
        filler_id:this.state.team_user_id
      })).then(response => {
        this.props.closeModal();
      }).catch(err => {
        this.setState({loading: false, errorMessage:err});
      });
      return;
    }
    this.props.chooseFiller(this.state.team_user_id);
  };
  selectUser = (id) => {
    this.setState({team_user_id: id});
  };
  render(){
    const {team_card, team} = this.props;
    const team_users = team.team_users;
    const me = team.team_users[team.my_team_user_id];
    const selected_user_id = this.state.team_user_id;
    const receivers = team_card.receivers.sort((a,b) => {
      return team_users[a.team_user_id].username.localeCompare(team_users[b.team_user_id].username);
    });
    return (
        <Form onSubmit={this.validate} error={!!this.state.errorMessage.length}>
          <Form.Field>
            Who will enter app credentials?
          </Form.Field>
          <Segment class="pushable">
            <Checkbox radio
                      label={`${me.username} (${teamUserRoles[me.role]})`}
                      name='checkboxRadioGroup'
                      value={me.id}
                      onClick={this.selectUser.bind(null, me.id)}
                      checked={selected_user_id === me.id}/>
            {receivers.map(receiver => {
              if (receiver.team_user_id === me.id)
                return null;
              const team_user = team_users[receiver.team_user_id];
              return (
                  <Checkbox radio
                            key={team_user.id}
                            label={`${team_user.username} (${teamUserRoles[team_user.role]})`}
                            name='checkboxRadioGroup'
                            value={team_user.id}
                            onClick={this.selectUser.bind(null, team_user.id)}
                            checked={selected_user_id === team_user.id}/>
              )
            })}
          </Segment>
          <Message error content={this.state.errorMessage}/>
          <Button
              type="submit"
              disabled={this.state.team_user_id === -1 || this.state.loading}
              loading={this.state.loading}
              positive
              className="modal-button uppercase"
              content={'CONFIRM'}/>
        </Form>
    )
  }
}

class SetupCredentialsStep extends Component {
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
  submit = (e) => {
    e.preventDefault();
    const {team_card} = this.props;
    this.setState({loading: true, errorMessage:''});
    this.props.dispatch(teamEditSingleCardCredentials({
      team_card: team_card,
      account_information: transformCredentialsListIntoObject(this.state.credentials)
    })).then(response => {
      this.close();
    }).catch(err => {
      this.setState({loading: false, errorMessage: err});
    })
  };
  render(){
    const credentialsInputs = this.state.credentials.map((item,idx) => {
      return <CredentialInput key={idx} onChange={this.handleCredentialInput} item={item}/>;
    });
    return (
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
    )
  }
}

@connect(store => ({
  team: store.teams[store.teamModals.simpleAppFillerChooserModal.team_card.team_id],
  team_card: store.teamModals.simpleAppFillerChooserModal.team_card
}))
class SimpleAppFillerChooserModal extends Component {
  constructor(props){
    super(props);
    this.state = {
      team: this.props.team,
      team_card: this.props.team_card,
      team_user_id: -1
    }
  }
  chooseFiller = (id) => {
    this.setState({team_user_id: id});
  };
  close = () => {
    this.props.dispatch(showSimpleAppFillerChooserModal({
      active: false
    }));
  };
  render(){
    const {team_card, team} = this.state;

    return (
        <SimpleModalTemplate
            onClose={this.close}
            headerContent={'App credentials'}>
          <Container id="popup_team_single_card">
            <div className="display-flex align_items_center" style={{marginBottom: '30px'}}>
              <div className="squared_image_handler">
                <img src={!!team_card.website ? team_card.website.logo : team_card.software.logo} alt="Website logo"/>
              </div>
              <span className="app_name">{team_card.name}</span>
            </div>
            {this.state.team_user_id === -1 &&
            <ChooseMemberStep
                dispatch={this.props.dispatch}
                chooseFiller={this.chooseFiller}
                closeModal={this.close}
                team_card={team_card}
                team={team}/>}
            {this.state.team_user_id !== -1 &&
            <SetupCredentialsStep
                dispatch={this.props.dispatch}
                closeModal={this.close}
                team_card={team_card}/>}
          </Container>
        </SimpleModalTemplate>
    )
  }
}

export default SimpleAppFillerChooserModal;