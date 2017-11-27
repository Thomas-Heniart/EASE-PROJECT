var React = require('react');
import {connect} from "react-redux";
import {createTeamUser, createTeamUserNow} from "../../actions/userActions";
import {handleSemanticInput, reflect} from "../../utils/utils";
import {showTeamAddMultipleUsersModal, showUpgradeTeamPlanModal} from "../../actions/teamModalActions";
import { Header, Label, Container, Divider, Icon, Transition, TextArea, Segment, Checkbox, Form, Input, Select, Dropdown, Button, Message } from 'semantic-ui-react';

class PreviewStep extends React.Component {
  constructor(props){
    super(props);
  };
  submit = (e) => {
    e.preventDefault();
    this.props.validate();
  };
  render(){
    const fields = this.props.invitations.map((item, idx) => {
      return (
          <Form.Field key={idx} error={item.error.length > 0}>
          {/*<Form.Group style={{marginBottom: 0}}>*/}
            <Input
                action={<Button icon="delete" type='button' onClick={this.props.removeField.bind(null, idx)}/>}
                actionPosition="left"
                name="email"
                type="email"
                value={item.email}
                placeholder="Email"
                onChange={(e, values) => {this.props.editField(idx, values)}}/>
            {/*<Form.Input width={7}
                        type="text"
                        name="username"
                        placeholder="Username"
                        value={item.username}
                        onChange={(e, values) => {this.props.editField(idx, values)}}/>*/}
          {/*</Form.Group>*/}
            {item.error.length > 0 &&
            <Label pointing class="fluid" style={{textAlign: 'center'}} content={item.error} color="red" basic/>}
          </Form.Field>
      )
    });
    return (
        <Form onSubmit={this.submit}>
          <Divider hidden clearing/>
          <Form.Group>
            <Form.Field><label>Email address</label></Form.Field>
          </Form.Group>
          {fields}
          <Form.Field>
            <Icon name="add circle" color="blue" size='large'/>
            <button class="button-unstyle inline-text-button primary" type="button" onClick={this.props.addField}>Add another</button>
            &nbsp;or&nbsp;
            <button class="button-unstyle inline-text-button primary" type="button" onClick={this.props.changeStep}>add a list of users</button>
          </Form.Field>
          {this.props.errorMessage !== null &&
              <Message color="red">
                {this.props.errorMessage}
              </Message>}
          <Form.Group id='invitationButton' class="overflow-hidden">
            <Form.Button
              basic color='green'
              positive
              floated='right'
              loading={this.props.loading}
              type="submit"
              disabled={!this.props.invitationsReady()}
              width={8}>
              Send invitation <u><strong>later</strong></u>
            </Form.Button>
            <Form.Button
              disabled={!this.props.invitationsReady()}
              color='green'
              floated='right'
              type="button"
              onClick={this.props.sendInvitationsNow}
              width={8}>
              Send invitation <u><strong>now</strong></u>
            </Form.Button>
          </Form.Group>
          {/*<Form.Field class="overflow-hidden">*/}
            {/*<Button floated="right" positive  loading={this.props.loading}>{this.props.validateButtonText}</Button>*/}
            {/*<Button floated="right">*/}
              {/*{this.props.cancelButtonText}*/}
            {/*</Button>*/}
          {/*</Form.Field>*/}
        </Form>
    )
  }
}

class EmailListStep extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      value: ''
    }
  }
  handleInput = handleSemanticInput.bind(this);
  process = (e) => {
    e.preventDefault();
    let filterSpacesValue = this.state.value.replace(/\s+/g, '');
    // this.props.addFields(filterSpacesValue);
    this.props.multipleAddLater(filterSpacesValue);
  };
  processNow = (e) => {
    e.preventDefault();
    let filterSpacesValue = this.state.value.replace(/\s+/g, '');
    // this.props.addFields(filterSpacesValue);
    this.props.multipleAddNow(filterSpacesValue);
  };
  render(){
    return (
        <Form onSubmit={this.process}>
          <Form.Field>
            Enter multiple email addresses, separated with commas
          </Form.Field>
          <Form.Field>
            <TextArea rows={7} name="value" onChange={this.handleInput} placeholder="Write emails here..."/>
          </Form.Field>
          <Form.Group id='invitationButton' class="overflow-hidden">
            <Form.Button
              basic color='green'
              positive
              disabled={!this.state.value}
              floated='right'
              loading={this.props.loading}
              type="submit"
              width={8}>
              Send invitation <u><strong>later</strong></u>
            </Form.Button>
            <Form.Button
              color='green'
              floated='right'
              disabled={!this.state.value}
              type="button"
              onClick={this.processNow}
              width={8}>
              Send invitation <u><strong>now</strong></u>
            </Form.Button>
          </Form.Group>
          {/*<Form.Field>
            <Button floated="right" primary>Next</Button>
            <Button floated="right" type="button" onClick={this.props.changeStep}>Cancel</Button>
          </Form.Field>*/}
        </Form>
    )
  }
}

@connect(store => ({
  teams: store.teams,
  team_id: store.teamModals.teamAddMultipleUsersModal.team_id
}))
class TeamAddMultipleUsersModal extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      view: 'main',
      invitations: [
        {email: '', username: '', error: ''},
        {email: '', username: '', error: ''},
        {email: '', username: '', error: ''}],
      loading: false,
      validateButtonText: 'Send invitations',
      cancelButtonText: 'Cancel',
      errorMessage: null
    }
  }
  showUpgradeModal = () => {
    this.props.dispatch(showUpgradeTeamPlanModal(true, 4));
  };
  invitationsReady = () => {
    return this.state.invitations.filter(item => (item.email.length > 0 && item.username.length > 0)).length > 0;
  };
  sendInvitations = () => {
    let invitations = this.state.invitations.slice();
    const team = this.props.teams[this.props.team_id];
    const users_length = Object.keys(team.team_users).length;
    invitations = invitations.filter(item => (item.email.length > 0 || item.username.length > 0));
    if (invitations.length + users_length > 30 && team.plan_id === 0){
      this.setState({
        errorMessage:
            <span>
              You are adding {invitations.length} people to your team but unfortunately you only have {30 - this.props.users.length} spots remaining to stay in the Basic plan. <button onClick={this.showUpgradeModal} class="button-unstyle inline-text-button" type="button">Upgrade to Pro</button> or add less people.
            </span>
      });
      return;
    }
    let calls = invitations.map(item => {
      return this.props.dispatch(createTeamUser({
        team_id: this.props.team_id,
        first_name: '',
        last_name: '',
        email: item.email,
        username: item.username,
        departure_date: null,
        role: 1
      }));
    });
    this.setState({loading: true, errorMessage: null});
    Promise.all(calls.map(reflect)).then(results => {
      results.map((item, idx) => {
        if (item.error)
          invitations[idx].error = item.data;
        else
          invitations[idx].error = '';
      });
      invitations = invitations.filter(item => (item.error.length > 0));
      if (invitations.length === 0){
        this.props.dispatch(showTeamAddMultipleUsersModal({active: false}));
        return;
      }
      this.setState({loading: false, invitations: invitations, validateButtonText:'Resend invitations', cancelButtonText: "Ok, I'm done"});
    });
  };
  sendInvitationsNow = () => {
    let invitations = this.state.invitations.slice();
    const team = this.props.teams[this.props.team_id];
    const users_length = Object.keys(team.team_users).length;
    invitations = invitations.filter(item => (item.email.length > 0 || item.username.length > 0));
    if (invitations.length + users_length > 30 && team.plan_id === 0){
      this.setState({
        errorMessage:
          <span>
              You are adding {invitations.length} people to your team but unfortunately you only have {30 - this.props.users.length} spots remaining to stay in the Basic plan. <button onClick={this.showUpgradeModal} class="button-unstyle inline-text-button" type="button">Upgrade to Pro</button> or add less people.
            </span>
      });
      return;
    }
    let calls = invitations.map(item => {
      return this.props.dispatch(createTeamUserNow({
        team_id: this.props.team_id,
        first_name: '',
        last_name: '',
        email: item.email,
        username: item.username,
        departure_date: null,
        role: 1
      }));
    });
    this.setState({loading: true, errorMessage: null});
    Promise.all(calls.map(reflect)).then(results => {
      results.map((item, idx) => {
        if (item.error)
          invitations[idx].error = item.data;
        else
          invitations[idx].error = '';
      });
      invitations = invitations.filter(item => (item.error.length > 0));
      if (invitations.length === 0){
        this.props.dispatch(showTeamAddMultipleUsersModal({active: false}));
        return;
      }
      this.setState({loading: false, invitations: invitations, validateButtonText:'Resend invitations', cancelButtonText: "Ok, I'm done"});
    });
  };
  changeView = (view) => {
    this.setState({view: view});
  };
  editField = (idx, {name, value}) => {
    let invitations = this.state.invitations.slice();
    invitations[idx][name] = value;
    if (name === 'email')
      invitations[idx]['username'] = value.split('@')[0];
    this.setState(() => ({invitations: invitations}));
  };
  addMultipleFields = (value) => {
    let invitations = [];
    let emails = value.split(',');
    emails.map(item => {
      let username = item.split('@')[0];
      invitations.push({email: item, username: username, error: ''});
    });
    this.setState({invitations: invitations});
  };
  multipleAddLater = (value) => {
    // this.addMultipleFields(invitations);
    let invitations = [];
    let emails = value.split(',');
    emails.map(item => {
      let username = item.split('@')[0];
      invitations.push({email: item, username: username, error: ''});
    });
    this.setState({invitations: invitations}, this.sendInvitations);
  };
  multipleAddNow = (value) => {
    // this.addMultipleFields(invitations);
    let invitations = [];
    let emails = value.split(',');
    emails.map(item => {
      let username = item.split('@')[0];
      invitations.push({email: item, username: username, error: ''});
    });
    this.setState({invitations: invitations}, this.sendInvitationsNow);
  };
  addField = () => {
    let invitations = this.state.invitations.slice();
    invitations.push({email: '', username: '', error: ''});
    this.setState(() => ({invitations: invitations}));
  };
  removeField = (idx) => {
    let invitations = this.state.invitations.slice();
    invitations.splice(idx, 1);
    this.setState({invitations: invitations});
  };
  render(){
    return (
        <div class="ease_modal">
          <div class="modal-background"/>
          <a id="ease_modal_close_btn" class="ease_modal_btn" onClick={e => {this.props.dispatch(showTeamAddMultipleUsersModal(false))}}>
            <i class="ease_icon fa fa-times"/>
            <span class="key_label">close</span>
          </a>
          <div class="modal_contents_container">
            <div class="contents">
              <Container>
                <Header as="h1">
                  Create several users at once
                </Header>
                {this.state.view === 'main' &&
                <PreviewStep
                    dispatch={this.props.dispatch}
                    changeStep={this.changeView.bind(null, 'emailList')}
                    invitations={this.state.invitations}
                    removeField={this.removeField}
                    editField={this.editField}
                    loading={this.state.loading}
                    validate={this.sendInvitations}
                    sendInvitationsNow={this.sendInvitationsNow}
                    errorMessage={this.state.errorMessage}
                    cancelButtonText={this.state.cancelButtonText}
                    validateButtonText={this.state.validateButtonText}
                    invitationsReady={this.invitationsReady}
                    addField={this.addField}/>}
                {this.state.view === 'emailList' &&
                <EmailListStep
                    // addFields={this.addMultipleFields}
                    changeStep={this.changeView.bind(null, 'main')}
                    validate={this.sendInvitations}
                    // sendInvitationsNow={this.sendInvitationsNow}
                    multipleAddNow={this.multipleAddNow}
                    multipleAddLater={this.multipleAddLater}/>}
              </Container>
            </div>
          </div>
        </div>
    )
  }
}

module.exports = TeamAddMultipleUsersModal;