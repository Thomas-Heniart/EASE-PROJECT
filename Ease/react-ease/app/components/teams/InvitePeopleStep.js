import React from "react";
import {handleSemanticInput, reflect, isEmail} from "../../utils/utils";
import { Header, Label, Divider, Icon, TextArea, Segment, Form, Button } from 'semantic-ui-react';
import {sendTeamUserInvitation, createTeamUserNow, createTeamUser} from "../../actions/userActions";

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
              <Form.Input
                  width={16}
                  action={<Button icon="delete" type="button" onClick={this.props.removeField.bind(null, idx)}/>}
                  actionPosition="left"
                  name="email"
                  type="email"
                  value={item.email}
                  placeholder="Email"
                  onChange={(e, values) => {this.props.editField(idx, values)}}/>
            {item.error.length > 0 &&
            <Label pointing fluid class="fluid" style={{textAlign: 'center'}} content={item.error} color="red" basic/>}
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
            <button class="button-unstyle inline-text-button primary" type="button" onClick={this.props.addField}>Add another field</button>
            &nbsp;or&nbsp;
            <button class="button-unstyle inline-text-button primary" type="button" onClick={this.props.changeStep}>Paste a list of emails</button>
          </Form.Field>
          <Form.Group id='invitationButton' class="overflow-hidden">
            <Form.Button
              basic color='green'
              positive
              floated='right'
              disabled={!this.props.invitationsReady() || this.props.loading}
              loading={this.props.loading}
              type="submit"
              width={8}>
              Send invitation <u><strong>later</strong></u>
            </Form.Button>
            <Form.Button
              color='green'
              floated='right'
              type="button"
              loading={this.props.loadingInvitationsNow}
              disabled={!this.props.invitationsReady() || this.props.loadingInvitationsNow}
              onClick={this.props.sendInvitationsNow}
              width={8}>
              Send invitation <u><strong>now</strong></u>
            </Form.Button>
          </Form.Group>
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
    this.props.multipleAddLater(filterSpacesValue);
  };
  processNow = (e) => {
    e.preventDefault();
    let filterSpacesValue = this.state.value.replace(/\s+/g, '');
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
              floated='right'
              disabled={!this.state.value || this.props.loading}
              loading={this.props.loading}
              type="submit"
              width={8}>
              Send invitation <u><strong>later</strong></u>
            </Form.Button>
            <Form.Button
              color='green'
              floated='right'
              type="button"
              loading={this.props.loadingInvitationsNow}
              disabled={!this.state.value || this.props.loadingInvitationsNow}
              onClick={this.processNow}
              width={8}>
              Send invitation <u><strong>now</strong></u>
            </Form.Button>
          </Form.Group>
        </Form>
    )
  }
}

class InvitePeopleStep extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      view: 'main',
      invitations: [
        {email: '', username: '', error: ''},
        {email: '', username: '', error: ''},
        {email: '', username: '', error: ''}],
      loading: false,
      loadingInvitationsNow: false,
      validateButtonText: 'Send invitations',
      cancelButtonText: 'Skip for now'
    }
  }
  invitationsReady = () => {
    return this.state.invitations.filter(item => (isEmail(item.email))).length > 0;
  };
  sendInvitations = () => {
    let invitations = this.state.invitations.slice();
    this.setState({loading: true});
    invitations = invitations.filter(item => (item.email.length > 0 || item.username.length > 0));
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
    Promise.all(calls.map(reflect)).then(results => {
      results.map((item, idx) => {
        if (item.error)
          invitations[idx].error = item.data;
        else
          invitations[idx].error = '';
      });
      invitations = invitations.filter(item => (item.error.length > 0));
      if (invitations.length === 0){
        this.props.onStepValidated();
        return;
      }
      this.setState({loading: false, invitations: invitations, validateButtonText:'Resend invitations', cancelButtonText: "Ok, I'm done"});
    });
  };
  sendInvitationsNow = () => {
    let invitations = this.state.invitations.slice();
    this.setState({loadingInvitationsNow: true});
    invitations = invitations.filter(item => (item.email.length > 0 || item.username.length > 0));
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
    Promise.all(calls.map(reflect)).then(results => {
      results.map((item, idx) => {
        if (item.error)
          invitations[idx].error = item.data;
        else
          invitations[idx].error = '';
      });
      invitations = invitations.filter(item => (item.error.length > 0));
      if (invitations.length === 0){
        this.props.onStepValidated();
        return;
      }
      this.setState({loadingInvitationsNow: false, invitations: invitations, validateButtonText:'Resend invitations', cancelButtonText: "Ok, I'm done"});
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
    let invitations = [];
    let emails = value.split(',');
    emails.map(item => {
      let username = item.split('@')[0];
      invitations.push({email: item, username: username, error: ''});
    });
    this.setState({invitations: invitations}, this.sendInvitations);
  };
  multipleAddNow = (value) => {
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
  render (){
    return (
        <div class="contents">
          <Segment>
            <Header as="h1">
              Who is in your team?
              <Header.Subheader>
                You can setup your members and choose to send them invitations <strong>later</strong> so everything will be ready before they arrive..
              </Header.Subheader>
            </Header>
            {this.state.view === 'main' &&
            <PreviewStep
                dispatch={this.props.dispatch}
                changeStep={this.changeView.bind(null, 'emailList')}
                invitations={this.state.invitations}
                removeField={this.removeField}
                editField={this.editField}
                loading={this.state.loading}
                loadingInvitationsNow={this.state.loadingInvitationsNow}
                validate={this.sendInvitations}
                sendInvitationsNow={this.sendInvitationsNow}
                invitationsReady={this.invitationsReady}
                onStepValidated={this.props.onStepValidated}
                cancelButtonText={this.state.cancelButtonText}
                validateButtonText={this.state.validateButtonText}
                addField={this.addField}/>}
            {this.state.view === 'emailList' &&
            <EmailListStep
              changeStep={this.changeView.bind(null, 'main')}
              validate={this.sendInvitations}
              loading={this.state.loading}
              loadingInvitationsNow={this.state.loadingInvitationsNow}
              multipleAddNow={this.multipleAddNow}
              multipleAddLater={this.multipleAddLater}/>}
          </Segment>
        </div>
    )
  }
}

module.exports = InvitePeopleStep;