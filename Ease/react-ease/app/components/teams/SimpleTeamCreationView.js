var React = require('react');
var classnames = require('classnames');
var post_api = require('../../utils/post_api');
var api = require('../../utils/api');
var axios = require('axios');
var StripeCardForm = require('../stripe/StripeCardForm');
var CompanyInformationForm = require('../common/CompanyInformationForm');

import {passwordRegexp, emailRegexp, checkTeamUsernameErrors, jobRoles} from "../../utils/utils";
import {connect} from "react-redux";
import {Elements} from 'react-stripe-elements';
import ReactCSSTransitionGroup from "react-addons-css-transition-group";
import { Header, Container, Segment, Checkbox, Form, Input,Divider, Icon, List, Select, Dropdown, Button, Grid, Message, Label,Transition } from 'semantic-ui-react';

class Step1 extends React.Component{
  constructor(props){
    super(props);
    this.state = {
      error: false,
      errorMessage: '',
      processing: false
    };
    this.onSubmit = this.onSubmit.bind(this);
  }
  onSubmit(e){
    e.preventDefault();
    this.setState({error: false, processing: true});
    post_api.teams.askTeamCreation(this.props.email).then(response => {
      this.setState({processing: false});
      if (response.need_digits)
        this.props.incrementStep(1);
      else
        this.props.incrementStep(2);
    }).catch(err => {
      this.setState({processing: false, error: true, errorMessage: err});
    });
  }
  render() {
    return (
        <div class="contents" id="step1">
          <Segment>
            <Header as="h1">
              Enter your professional email
            </Header>
            <Divider hidden clearing/>
            <Form onSubmit={this.onSubmit} error={this.state.error}>
              <Form.Input label="Your email address" required onChange={this.props.handleInput} type="email" name="email" placeholder="name@company.com"/>
              <Form.Checkbox label="It's ok to send me (very occasional) email about Ease.space service"
                             onClick={this.props.switchNewsletter}
                             checked={this.props.newsletter}/>
              <Message error content={this.state.errorMessage}/>
              <Form.Field>
                <Button positive fluid loading={this.state.processing} type="submit">Next</Button>
              </Form.Field>
            </Form>
          </Segment>
        </div>
    )
  }
}

class Step2 extends React.Component{
  constructor(props){
    super(props);
    this.state = {
      errorMessage: '',
      loading: false
    };
    this.onSubmit = this.onSubmit.bind(this);
  }
  onSubmit(e){
    e.preventDefault();
    this.setState({errorMessage: '', loading: true});
    post_api.teams.checkTeamCreationDigits(this.props.email, this.props.digits).then(response => {
      this.setState({loading: false});
      this.props.onStepValidated();
    }).catch(err => {
      this.setState({loading: false, errorMesage: err});
    })
  }
  render() {
    return (
        <div class="contents" id="step2">
          <Segment>
            <Header as="h1">
              Check your email
              <Header.Subheader>
                We've send a six-digit confirmation code to <strong>{this.props.email}</strong>. It will expire shortly, so enter your code soon.
              </Header.Subheader>
            </Header>
            <Divider hidden clearing/>
            <Form onSubmit={this.onSubmit} error={this.state.errorMessage.length > 0}>
              <Form.Input label="Your confirmation code"
                          onChange={this.props.handleInput}
                          type="number"
                          name="digits"
                          placeholder="Confirmation code"
                          required/>
              <Message color="yellow">
                Keep this window open while checking for your code.<br/> Haven't received our email ? Try your spam folder!
              </Message>
              <Message error content={this.state.errorMessage}/>
              <Form.Button fluid positive type="submit" loading={this.state.loading}>Next</Form.Button>
            </Form>
          </Segment>
        </div>
    )
  }
}

class Step4 extends React.Component{
  constructor(props){
    super(props);
    this.state = {
      errorMessage: ''
    };
    this.onSubmit = this.onSubmit.bind(this);
  }
  onSubmit(e){
    e.preventDefault();
    const usernameErrors = checkTeamUsernameErrors(this.props.username);
    if (usernameErrors.error){
      this.setState({errorMessage: usernameErrors.message});
      return;
    }
    this.props.onStepValidated();
  }
  render() {
    return (
        <div class="contents" id="step4">
          <Segment>
            <Header as="h1">
              What's your name
              <Header.Subheader>
                Your name will be displayed for your team members in Ease.space
              </Header.Subheader>
            </Header>
            <Divider hidden clearing/>
            <Form onSubmit={this.onSubmit} error={this.state.errorMessage.length > 0}>
              <Form.Group widths="equal">
                <Form.Input required label='First name' placeholder='First name' onChange={this.props.handleInput} name="fname" type="text"/>
                <Form.Input required label='Last name' placeholder='Last name' onChange={this.props.handleInput} name="lname" type="text"/>
              </Form.Group>
              <Form.Field required>
                <label>Username</label>
                <Input type="text"
                       onChange={this.props.handleInput}
                       name="username"
                       placeholder="Username"
                       required/>
                <Label pointing>Please choose a username that is all lowercase, containing only letters, numbers, periods, hyphens and underscores. Maximum 22 characters.</Label>
              </Form.Field>
              <Message error content={this.state.errorMessage}/>
              <Form.Field>
                <Button positive fluid type="submit">Next</Button>
              </Form.Field>
            </Form>
          </Segment>
        </div>
    )
  }
}

function Step5(props){
  const roles = jobRoles.map((item, idx) => {
    return {
      key: idx,
      value: idx,
      text: item
    }
  });
  const jobRole = props.jobRole;
  const jobDetails = props.jobDetails;
  return (
      <div class="contents" id="step5">
        <Segment>
          <Header as="h1">
            Tell us about your role
          </Header>
          <Divider hidden clearing/>
          <Form onSubmit={props.incStep}>
            <Form.Field>
              <label>What type of work do you do?</label>
              <Select placeholder="Select your role" name="jobRole" options={roles} onChange={props.handleInput}/>
            </Form.Field>
            {props.jobRole === 15 &&
            <Form.Input
                label="Could you please elaborate? (More info will help us improve Ease.space!)"
                placeholder="Details..."
                type="text"
                name="jobDetails"
                required
                onChange={props.handleInput}/>}
            <Form.Field>
              <Button positive fluid type="submit" disabled={jobRole === null || (jobRole == 15 && jobDetails.length == 0)}>Next</Button>
            </Form.Field>
          </Form>
        </Segment>
      </div>
  )
}

class Step6 extends React.Component{
  constructor(props){
    super(props);
    this.state = {
      errorMessage: '',
      loading: false
    };
    this.onSubmit = this.onSubmit.bind(this);
  }
  onSubmit(e){
    e.preventDefault();
    this.setState({errorMessage: '', loading: true});
    post_api.teams.createTeam(this.props.teamName, this.props.email,this.props.first_name, this.props.last_name, this.props.username, this.props.jobRole, this.props.jobDetails, this.props.digits).then(response => {
      const teamId = response.id;
      this.props.handleInput(null, {name:"teamId", value:teamId});
      this.setState({loading: false});
      this.props.onStepValidated();
    }).catch(err => {
      this.setState({errorMessage: err, loading: false});
    });
  }
  render() {
    return (
        <div class="contents" id="step6">
          <Segment error={this.state.errorMessage.length > 0}>
            <Header as="h1">
              What's your company called ?
            </Header>
            <Divider hidden clearing/>
            <Form onSubmit={this.onSubmit} error={this.state.errorMessage.length > 0}>
              <Form.Field>
                <label>Team name</label>
                <Input
                    type="text"
                    onChange={this.props.handleInput}
                    name="teamName"
                    placeholder="Company name"/>
                <Label pointing>We will use this to name your Ease.space team, which can always change later</Label>
              </Form.Field>
              <Message error content={this.state.errorMessage}/>
              <Form.Field>
                <Button positive fluid loading={this.state.loading} type="submit" disabled={this.props.teamName.length === 0}>Next</Button>
              </Form.Field>
            </Form>
          </Segment>
        </div>
    )
  }
}

class Step7 extends React.Component{
  constructor(props){
    super(props);
    this.state = {
      loading: false
    };
    this.onSubmit = this.onSubmit.bind(this);
  }
  onSubmit(e){
    e.preventDefault();
    var calls = [];
    this.props.invitations.map(function (item) {
      if (item.email.match(emailRegexp) !== null && item.username.length > 0){
        calls.push(post_api.teamUser.createTeamUser(this.props.ws_id, this.props.teamId, '', '', item.email, item.username, null, '1'));
      }
    }, this);
    this.setState({loading: true});
    axios.all(calls).then(() => {
      this.props.handleInput(null, {name: "invitedPeople", value: calls.length});
      this.setState({loading: false});
      this.props.onStepValidated();
    });
  }
  render() {
    const fields = this.props.invitations.map((item, idx) => {
      return (
          <Form.Group key={idx}>
            <Form.Field width={9}>
              <Input
                  action={<Button icon="delete" onClick={this.props.removeInvitationField.bind(null, idx)}/>}
                  actionPosition="left"
                  type="email"
                  value={item.email}
                  placeholder="Email"
                  onChange={(e, {value}) => {
                    this.props.editInvitationEmail(value, idx)
                  }}/>
            </Form.Field>
            <Form.Input width={7} type="text"
                        placeholder="Username"
                        value={item.username}
                        onChange={(e, {value}) => {
                          this.props.editInvitationUsername(value, idx)
                        }}/>
          </Form.Group>
      )
    }, this);
    return (
        <div class="contents" id="step7">
          <Segment>
            <Header as="h1">
              Send invitations
              <Header.Subheader>
                Your Ease.space team is ready to go. Know few coworkers who'd like to stop using passwords with you?
              </Header.Subheader>
            </Header>
            <Form>
              <Divider hidden clearing/>
              <Form.Group>
                <Form.Field width={9}><label>Email address</label></Form.Field>
                <Form.Field width={7}><label>Username (editable later)</label></Form.Field>
              </Form.Group>
                {fields}
              <Form.Button primary onClick={this.props.addInvitationField}>
                <Icon name="add user"/>
                Add another field
              </Form.Button>
              <Form.Group>
                <Form.Button width={8} fluid onClick={this.props.incStep}>Skip for now</Form.Button>
                <Form.Button width={8} fluid positive onClick={this.onSubmit} loading={this.state.loading}>Send invitations</Form.Button>
              </Form.Group>
            </Form>
          </Segment>
        </div>
    )
  }
}

class Step8 extends React.Component{
  constructor(props){
    super(props);
    this.state = {
      companyInfoConfirmed: false,
      friendsInvited: false,
      errorMessage: '',
      loading: false
    };
    this.confirmCompanyInfo = this.confirmCompanyInfo.bind(this);
    this.tokenCallback = this.tokenCallback.bind(this);
    this.inviteFriends = this.inviteFriends.bind(this);
  }
  inviteFriends(e){
    e.preventDefault();
    const f = this.props.friends;

    this.setState({erorrMessage: '', loading: true});
    post_api.teams.inviteFriends(this.props.teamId, f[0].email,f[1].email,f[2].email).then(response => {
      this.props.handleInput({target: {value: 15, name: 'credits'}});
      this.setState({friendsInvited: true, errorMessage: '', loading: false});
    }).catch(err => {
      this.setState({errorMessage: '', loading: false});
    });
  }
  confirmCompanyInfo(state){
    this.setState({companyInfoConfirmed: state});
  }
  tokenCallback(token){
    const i = this.props.companyInfo;
    post_api.teams.subscribeToPlan(this.props.teamId, token, i.vat_id, i.company_name,i.street_address, i.unit,
        i.zip, i.state, i.country, i.city).then(response => {
      this.props.onStepValidated();
    }).catch(err => {

    });
  }
  render() {
    return (
        <div class="contents" id="step6">
          <Segment>
            <Header as="h1" color={this.state.friendsInvited ? 'green': 'black'}>
              Checkout
              <Header.Subheader>
                You have {this.props.credits} euros in credits !
              </Header.Subheader>
            </Header>
            <Divider hidden clearing/>
            {!this.state.friendsInvited &&
                <Form onSubmit={this.inviteFriends} error={this.state.errorMessage.length > 0}>
                  <Form.Field>
                    <strong>Invite 3 friends and get up to 15 euros in credits</strong>
                    </Form.Field>
                  {this.props.friends.map((item, idx) => {
                    return (
                        <Form.Input type="email"
                                    value={item.email}
                                    onChange={(e, {value}) => {
                                      this.props.editFriendsEmail(value, idx)
                                    }}
                                    placeholder="friend@company.com"
                                    key={idx}
                                    required/>
                    )
                  }, this)}
                  <Message color="yellow">
                    Make sure these are the right email addresses, otherwise it won't work, and you'll get charged back:(
                  </Message>
                  <Message error content={this.state.errorMessage}/>
                  <Form.Button fluid type="submit" primary>Update the bill</Form.Button>
                </Form>
            }
            {!this.state.friendsInvited &&
            <Divider />}
            <Button positive fluid onClick={this.props.onStepValidated}>Ok ! I wanna see my team now !</Button>
            </Segment>
        </div>
    )
  }
}

function test(props){
  return (
      <div>
        /*          <div class="content_row flex_direction_column">
         <h1>Add Company information</h1>
         {!this.state.companyInfoConfirmed ?
         <CompanyInformationForm
         companyInfo={this.props.companyInfo}
         handleCompanyInfoInput={this.props.handleCompanyInfoInput}
         onSubmit={e => {e.preventDefault();this.confirmCompanyInfo(true);}}/>
         :
         <div class="display_flex">
         <button class="button-unstyle mrgnRight5"
         onClick={this.confirmCompanyInfo.bind(null, false)}>
         <u>Edit</u>
         </button>
         <div class="display-flex flex_direction_column">
         <strong>{this.props.companyInfo.company_name}</strong>
         <span>{this.props.companyInfo.street_address}</span>
         <span>{this.props.companyInfo.city}</span>
         <span>{this.props.companyInfo.country}</span>
         </div>
         </div>
         }
         </div>
         {this.state.companyInfoConfirmed &&
         <div class="content_row flex_direction_column">
         <h1>Add payment method</h1>
         <StripeCardForm tokenCallback={this.tokenCallback}/>
         </div>}*/
      </div>
  )
}

@connect((store)=>{
  return {
    ws_id: store.common.ws_id
  };
})
class SimpleTeamCreationView extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      currentStep : 0,
      steps : [],
      email: '',
      skipDigits: false,
      newsletter: false,
      digits: '',
      password: '',
      confirmPassword: '',
      fname: '',
      lname: '',
      username:'',
      jobRole: null,
      jobDetails: '',
      teamName: '',
      teamId: -1,
      credits: 0,
      invitations: [{email: '', username: ''},{email: '', username: ''},{email: '', username: ''}],
      invitedPeople: 0,
      friends: [{email:''},{email:''},{email:''}],
      companyInformation: {
        country: 'France',
        company_name: '',
        street_address:'',
        unit: '',
        city: '',
        state: '',
        zip: '',
        vat_id: ''
      },
      stripeToken: null
    };
    this.incrementStep = this.incrementStep.bind(this);
    this.handleInput = this.handleInput.bind(this);
    this.switchNewsletter = this.switchNewsletter.bind(this);
    this.addInvitationField = this.addInvitationField.bind(this);
    this.removeInvitationField = this.removeInvitationField.bind(this);
    this.editInvitationEmail = this.editInvitationEmail.bind(this);
    this.editInvitationUsername = this.editInvitationUsername.bind(this);
    this.editFriendsEmail = this.editFriendsEmail.bind(this);
    this.handleCompanyInfoInput = this.handleCompanyInfoInput.bind(this);
    this.submitStep8 = this.submitStep8.bind(this);
    this.incrementStepByValue = this.incrementStepByValue.bind(this);
  }
  incrementStepByValue(value){
    this.setState({currentStep: this.state.currentStep + value});
  }
  handleCompanyInfoInput(e){
    var info = this.state.companyInformation;
    console.log(e.target.name);
    console.log(e.target.value);
    info[e.target.name] = e.target.value;
    this.setState({companyInformation: info});
  }
  addInvitationField(){
    var invitations = this.state.invitations;
    invitations.push({email:'', username:''});
    this.setState({invitations:invitations});
  }
  removeInvitationField(idx){
    var invitations = this.state.invitations;

    invitations.splice(idx, 1);
    this.setState({invitations:invitations});
  }
  editFriendsEmail(email, idx){
    var friends = this.state.friends;

    friends[idx].email = email;
    this.setState({friends: friends});
  }
  editInvitationEmail(email, idx){
    var invitations = this.state.invitations;
    const delimiter = email.indexOf('@');

    invitations[idx].email = email;
    if (delimiter !== -1)
      invitations[idx].username = email.substring(0, delimiter);
    else
      invitations[idx].username = email;
    this.setState({invitations: invitations});
  }
  editInvitationUsername(username, idx){
    var invitations = this.state.invitations;

    invitations[idx].username = username;
    this.setState({invitations: invitations});
  }
  switchNewsletter(){
    this.setState({newsletter: !this.state.newsletter});
  }
  submitStep8(){
    this.props.history.push('/teams/' + this.state.teamId);
  }
  handleInput(e, {value , name}){
    this.setState({[name]: value});
  }
  incrementStep(){
    this.setState({currentStep: this.state.currentStep + 1});
  }
  render(){
    var steps = [];
    steps.push(<Step1 incrementStep={this.incrementStepByValue}
                      handleInput={this.handleInput}
                      email={this.state.email}
                      switchNewsletter={this.switchNewsletter}
                      newsletter={this.state.newsletter}
                      key="1"/>);
    steps.push(<Step2 onStepValidated={this.incrementStep}
                      digits={this.state.digits}
                      email={this.state.email}
                      handleInput={this.handleInput}
                      key="2"/>);
    steps.push(<Step4 onStepValidated={this.incrementStep}
                      email={this.state.email}
                      password={this.state.password}
                      newsletter={this.state.newsletter}
                      digits={this.state.digits}
                      lname={this.state.lname}
                      fname={this.state.fname}
                      username={this.state.username}
                      handleInput={this.handleInput}
                      key="4"/>);
    steps.push(<Step5 incStep={this.incrementStep}
                      handleInput={this.handleInput}
                      jobRole={this.state.jobRole}
                      jobDetails={this.state.jobDetails}
                      key="5"/>);
    steps.push(<Step6 onStepValidated={this.incrementStep}
                      digits={this.state.digits}
                      username={this.state.username}
                      first_name={this.state.fname}
                      last_name={this.state.lname}
                      email={this.state.email}
                      teamName={this.state.teamName}
                      jobRole={this.state.jobRole}
                      jobDetails={this.state.jobDetails}
                      handleInput={this.handleInput}
                      key="6"/>);
    steps.push(<Step7 onStepValidated={this.incrementStep}
                      incStep={this.incrementStep}
                      teamId={this.state.teamId}
                      handleInput={this.handleInput}
                      invitations={this.state.invitations}
                      editInvitationEmail={this.editInvitationEmail}
                      editInvitationUsername={this.editInvitationUsername}
                      addInvitationField={this.addInvitationField}
                      removeInvitationField={this.removeInvitationField}
                      ws_id={this.props.ws_id}
                      key="7"/>);
    steps.push(<Step8 key="8"
                      onStepValidated={this.submitStep8}
                      friends={this.state.friends}
                      editFriendsEmail={this.editFriendsEmail}
                      invitedPeople={this.state.invitedPeople}
                      credits={this.state.credits}
                      teamId={this.state.teamId}
                      companyInfo={this.state.companyInformation}
                      handleInput={this.handleInput}
                      handleCompanyInfoInput={this.handleCompanyInfoInput}
    />);
    return (
        <div id="team_creation_view" class="display-flex justify_content_center full_flex">
          <ReactCSSTransitionGroup
              component="div"
              className="carousel"
              transitionName="slideLeft"
              transitionAppear={true}
              transitionAppearTimeout={200}
              transitionEnterTimeout={200}
              transitionLeaveTimeout={200}>
            {
              steps.map(function (item,idx) {
                if (idx === this.state.currentStep)
                  return(item);
                return null;
              }, this)
            }
          </ReactCSSTransitionGroup>
        </div>
    )
  }
}

module.exports = SimpleTeamCreationView;