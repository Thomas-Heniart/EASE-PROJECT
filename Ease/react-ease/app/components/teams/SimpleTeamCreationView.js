var React = require('react');
var classnames = require('classnames');
var post_api = require('../../utils/post_api');

import InvitePeopleStep from "./InvitePeopleStep";
import {passwordRegexp, emailRegexp, checkTeamUsernameErrors, jobRoles} from "../../utils/utils";
import {connect} from "react-redux";
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
              <Form.Input label="Your email address"
                          required
                          value={this.props.email}
                          onChange={this.props.handleInput}
                          type="email" name="email"
                          placeholder="name@company.com"/>
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
      loading: false,
      sendingEmail: false,
      sendEmailButtonText: 'Resend email'
    };
    this.onSubmit = this.onSubmit.bind(this);
    this.resendDigits = this.resendDigits.bind(this);
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
  resendDigits(){
    this.setState({sendingEmail: true});
    post_api.teams.askTeamCreation(this.props.email).then(response => {
      this.setState({sendingEmail: false});
      this.setState({sendEmailButtonText: 'Sent!'});
      window.setTimeout(() => {this.setState({sendEmailButtonText: 'Resend email'})}, 2000);
    }).catch(err => {
      this.setState({sendingEmail: false});
    });
  }
  render() {
    return (
        <div class="contents" id="step2">
          <Segment>
            <Header as="h1">
              Check your email
              <Header.Subheader>
                We've sent a six-digit confirmation code to <strong>{this.props.email}</strong>. It will expire shortly, so enter your code soon.
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
              <Message color="yellow" size="mini">
                Keep this window open while checking for your code.<br/> Haven't received our email ? Try your spam folder!
                Or <Button basic type="button" className="textlike" size="mini" loading={this.state.sendingEmail} onClick={this.resendDigits} content={this.state.sendEmailButtonText}/>.
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
      errorMessage: '',
      usernameError: false
    };
    this.onSubmit = this.onSubmit.bind(this);
  }
  onSubmit(e){
    e.preventDefault();
    const usernameErrors = checkTeamUsernameErrors(this.props.username);
    if (usernameErrors.error){
      this.setState({usernameError: true});
      return;
    }
    this.props.onStepValidated();
  }
  render() {
    return (
        <div class="contents" id="step4">
          <Segment>
            <Header as="h1">
              What's your name?
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
              <Form.Field required error={this.state.usernameError}>
                <label>Username</label>
                <Input type="text"
                       onChange={this.props.handleInput}
                       value={this.props.username}
                       name="username"
                       placeholder="Username"
                       required/>
                <Label pointing basic={this.state.usernameError} color={this.state.usernameError ? 'red': null}>Please choose a username that is all lowercase, containing only letters, numbers, periods, hyphens and underscores. From 3 to 22 characters.</Label>
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
          <Segment>
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

@connect((store)=>{
  return {
    ws_id: store.common.ws_id,
    user: store.common.user
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
      invitations: [{email: '', username: ''},{email: '', username: ''},{email: '', username: ''}]
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
  componentDidMount(){
    if (this.props.user !== null){
      this.setState({email: this.props.user.email, username: this.props.user.first_name});
    }
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
    window.location.href = `/teams#/teams/${this.state.teamId}`;
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
    steps.push(<InvitePeopleStep
        key="7"
        ws_id={this.props.ws_id}
        team_id={this.state.teamId}
        onStepValidated={this.submitStep8}/>);
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