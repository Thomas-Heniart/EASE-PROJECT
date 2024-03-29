import {teamShareCard} from "../../actions/appsActions";

var React = require('react');
import {connect} from "react-redux";
import {createTeamUser, createTeamUserNow, sendInvitationToTeamUserList} from "../../actions/userActions";
import {handleSemanticInput, reflect, isEmail} from "../../utils/utils";
import {showTeamAddMultipleUsersModal, showUpgradeTeamPlanModal} from "../../actions/teamModalActions";
import { Header, Label, Container, Divider, Icon, TextArea, Form, Input, Button, Message } from 'semantic-ui-react';
import {addNotification} from "../../actions/notificationBoxActions";
import {withRouter} from "react-router-dom";
import {createTeamProfile} from "../../actions/onBoardingActions";

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
            <Input
                action={<Button icon="delete" type='button' onClick={this.props.removeField.bind(null, idx)}/>}
                actionPosition="left"
                name="email"
                type="email"
                value={item.email}
                placeholder="Email"
                onChange={(e, values) => {this.props.editField(idx, values)}}/>
            {item.error.length > 0 &&
            <Label pointing class="fluid" style={{textAlign: 'center'}} content={item.error} color="red" basic/>}
          </Form.Field>
      )
    });
    return (
        <Form onSubmit={this.submit} error={this.props.errorMessage.length > 0} id='add_user_modal'>
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
          <Message error content={this.props.errorMessage}/>
          <label className='check_tag_user_label'>Setting</label>
          <Form.Checkbox
            toggle
            name='checkTagUser'
            className='check_tag_user'
            onChange={this.props.handleInput}
            checked={this.props.checkTagUser}
            label="Add these users in all apps of #openspace"/>
          <Form.Group id='invitationButton' class="overflow-hidden">
            <Form.Button
                basic color='green'
                positive
                floated='right'
                loading={this.props.loading}
                type="submit"
                disabled={!this.props.invitationsReady() || this.props.loading}
                width={8}>
              Send invitation <u><strong>later</strong></u>
            </Form.Button>
            <Form.Button
                disabled={!this.props.invitationsReady() || this.props.loadingInvitationsNow}
                loading={this.props.loadingInvitationsNow}
                color='green'
                floated='right'
                type="button"
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
    this.props.addMultipleFields(this.state.value);
    this.props.changeStep();
    /*    let filterSpacesValue = this.state.value.replace(/\s+/g, '');
        this.props.multipleAddLater(filterSpacesValue);*/
  };
  /*  processNow = (e) => {
      e.preventDefault();
      let filterSpacesValue = this.state.value.replace(/\s+/g, '');
      this.props.multipleAddNow(filterSpacesValue);
    };*/
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
                onClick={this.props.changeStep}
                type="button"
                floated='right'
                width={8}>
              Cancel
            </Form.Button>
            <Form.Button
                color='green'
                floated='right'
                disabled={!this.state.value}
                type="submit"
                width={8}>
              Next
            </Form.Button>
          </Form.Group>
        </Form>
    )
  }
}

@connect(store => ({
  teams: store.teams,
  team_id: store.teamModals.teamAddMultipleUsersModal.team_id,
  team_apps: store.team_apps
}))
class TeamAddMultipleUsersModal extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      view: 'main',
      invitations: [
        {email: '', error: ''},
        {email: '', error: ''},
        {email: '', error: ''}],
      loading: false,
      loadingInvitationsNow: false,
      validateButtonText: 'Send invitations',
      cancelButtonText: 'Cancel',
      errorMessage: '',
      checkTagUser: false
    }
  }
  handleInput = handleSemanticInput.bind(this);
  showUpgradeModal = () => {
    this.props.dispatch(showUpgradeTeamPlanModal(true, 4));
  };
  invitationsReady = () => {
    return this.state.invitations.filter(item => (isEmail(item.email))).length > 0;
  };
  sendInvitations = () => {
    let invitations = this.state.invitations.slice();
    const team = this.props.teams[this.props.team_id];
    const users_length = Object.keys(team.team_users).length;
    invitations = invitations.filter(item => (item.email.length > 0));
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
    this.setState({loading: true, errorMessage: ''});
    let lastInvitedUser = null;
    Promise.all(calls.map(reflect)).then(results => {
      let newUsers = [];
      results.map((item, idx) => {
        if (item.error)
          invitations[idx].error = item.data;
        else {
          invitations[idx].error = '';
          newUsers.push(item.data.id);
          lastInvitedUser = item.data;
        }
      });
      invitations = invitations.filter(item => (item.error.length > 0));
      if (this.state.checkTagUser) {
        newUsers.map(user_id => {
          let openspaceId = null;
          Object.keys(team.rooms).map(room_id => {
            if (team.rooms[room_id].default)
              openspaceId = room_id;
          });
          team.rooms[openspaceId].team_card_ids.map(card_id => {
            calls.push(this.props.dispatch(teamShareCard({
              type: this.props.team_apps[card_id].type,
              team_id: team.id,
              team_card_id: card_id,
              team_user_id: user_id,
              account_information: {}
            })));
          });
        });
      }
      if (!!lastInvitedUser)
        this.props.history.replace(`/teams/${team.id}/@${lastInvitedUser.id}`);
      if (calls.length !== invitations.length){
        this.props.dispatch(addNotification({
          text: "New team user(s) successfully created!"
        }));
      }
      if (invitations.length === 0){
        this.props.dispatch(showTeamAddMultipleUsersModal({active: false}));
        return;
      }
      this.setState({loading: false, invitations: invitations, validateButtonText:'Resend invitations', cancelButtonText: "Ok, I'm done"});
    });
  };
  sendInvitationsNow = async () => {
    let invitations = this.state.invitations.slice();
    const team = this.props.teams[this.props.team_id];
    this.setState({
      loadingInvitationsNow: true,
      errorMessage: ''});
    const users_length = Object.keys(team.team_users).length;
    invitations = invitations.filter(item => (item.email.length > 0));
    /*    if (invitations.length + users_length > 30 && team.plan_id === 0){
          this.setState({
            errorMessage:
                <span>
                  You are adding {invitations.length} people to your team but unfortunately you only have {30 - users_length} spots remaining to stay in the Basic plan. <button onClick={this.showUpgradeModal} class="button-unstyle inline-text-button" type="button">Upgrade to Pro</button> or add less people.
                </span>
          });
          return;
        }*/
    let calls = invitations.map(item => {
      return this.props.dispatch(createTeamUser({
        team_id: this.props.team_id,
        first_name: '',
        last_name: '',
        email: item.email,
        username: item.username,
        departure_date: null,
        arrival_date: null,
        role: 1
      }));
    });
    const invitationsToSend = [];
    let lastInvitedUser = null;
    let callback = [];
    Promise.all(calls.map(reflect)).then(async results => {
      results.map((item, idx) => {
        if (item.error)
          invitations[idx].error = item.data;
        else {
          invitations[idx].error = '';
          invitationsToSend.push(item.data.id);
          lastInvitedUser = item.data;
        }
      });
      if (this.state.checkTagUser) {
        let openspaceId = null;
        Object.keys(team.rooms).map(room_id => {
          if (team.rooms[room_id].default)
            openspaceId = room_id;
        });
        invitationsToSend.map(user_id => {
          team.rooms[openspaceId].team_card_ids.map(card_id => {
            callback.push(this.props.dispatch(teamShareCard({
              type: this.props.team_apps[card_id].type,
              team_id: team.id,
              team_card_id: card_id,
              team_user_id: user_id,
              account_information: {}
            })));
          });
        });
        await Promise.all(callback.map(reflect)).then(res => {});
      }
      invitations = invitations.filter(item => (item.error.length > 0));
      if (!!invitationsToSend.length)
        await this.props.dispatch(sendInvitationToTeamUserList({
          team_id: team.id,
          team_user_id_list: invitationsToSend
        }));
      if (!!lastInvitedUser)
        this.props.history.replace(`/teams/${team.id}/@${lastInvitedUser.id}`);
      if (calls.length !== invitations.length){
        this.props.dispatch(addNotification({
          text: "New team user(s) successfully created!"
        }));
      }
      if (invitations.length === 0){
        this.props.dispatch(showTeamAddMultipleUsersModal({active: false}));
        return;
      }
      this.setState({
        loadingInvitationsNow: false,
        invitations: invitations,
        validateButtonText:'Resend invitations',
        cancelButtonText: "Ok, I'm done"
      });
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
    let emails = value.split(/[ ,\n\t]+/);
    emails = emails.filter(item => {
      return isEmail(item);
    });
    emails = Array.from(new Set(emails));
    emails.map(item => {
      invitations.push({email: item, error: ''});
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
                  Add several users at once
                </Header>
                {this.state.view === 'main' &&
                <PreviewStep
                    handleInput={this.handleInput}
                    checkTagUser={this.state.checkTagUser}
                    dispatch={this.props.dispatch}
                    changeStep={this.changeView.bind(null, 'emailList')}
                    invitations={this.state.invitations}
                    removeField={this.removeField}
                    editField={this.editField}
                    loading={this.state.loading}
                    loadingInvitationsNow={this.state.loadingInvitationsNow}
                    validate={this.sendInvitations}
                    sendInvitationsNow={this.sendInvitationsNow}
                    errorMessage={this.state.errorMessage}
                    cancelButtonText={this.state.cancelButtonText}
                    validateButtonText={this.state.validateButtonText}
                    invitationsReady={this.invitationsReady}
                    addField={this.addField}/>}
                {this.state.view === 'emailList' &&
                <EmailListStep
                    changeStep={this.changeView.bind(null, 'main')}
                    validate={this.sendInvitations}
                    loading={this.state.loading}
                    addMultipleFields={this.addMultipleFields}
                    loadingInvitationsNow={this.state.loadingInvitationsNow}
                    errorMessage={this.state.errorMessage}
                    multipleAddNow={this.multipleAddNow}
                    multipleAddLater={this.multipleAddLater}/>}
              </Container>
            </div>
          </div>
        </div>
    )
  }
}

module.exports = withRouter(TeamAddMultipleUsersModal);