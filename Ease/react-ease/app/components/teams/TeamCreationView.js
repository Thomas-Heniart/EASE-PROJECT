var React = require('react');
var classnames = require('classnames');
var post_api = require('../../utils/post_api');
var api = require('../../utils/api');
var axios = require('axios');
import queryString from "query-string";
import InvitePeopleStep from "./InvitePeopleStep";
import {checkTeamUsernameErrors, jobRoles, passwordRegexp} from "../../utils/utils";
import {withRouter} from "react-router-dom";
import {setLoginRedirectUrl} from "../../actions/commonActions";
import ReactCSSTransitionGroup from "react-addons-css-transition-group";
import SingleEaseLogo from "../common/SingleEaseLogo";
import {connect} from "react-redux";

import {Button, Container, Divider, Form, Header, Input, Label, Message, Segment, Select} from 'semantic-ui-react';

class Step1 extends React.Component{
  constructor(props){
    super(props);
    this.state = {
      errorMessage: '',
      processing: false
    };
    this.onSubmit = this.onSubmit.bind(this);
    this.login = this.login.bind(this);
  }
  login(){
    this.props.dispatch(setLoginRedirectUrl('/main/simpleTeamCreation'));
    this.props.history.replace('/login');
  }
  onSubmit(e){
    e.preventDefault();
    this.setState({errorMessage: '', processing: true});
    post_api.common.askRegistration(this.props.email).then(response => {
      this.setState({processing: false});
      this.props.onStepValidated();
        easeTracker.trackEvent("TeamCreationEnterEmail", {
            "plan_id": this.props.plan_id
        });
        easeTracker.trackEvent("RegistrationEnterEmail");
    }).catch(err => {
      this.setState({errorMessage: err,processing: false});
    });
  }
  render() {
    return (
        <div class="contents" id="step1">
          <Segment>
            <Header as="h1">
              Create an account
            </Header>
            <Divider hidden clearing/>
            <Form onSubmit={this.onSubmit} error={this.state.errorMessage.length > 0}>
              <Form.Field required>
                <label>Your email address</label>
                <Input onChange={this.props.handleInput}
                       type="email"
                       name="email"
                       placeholder="name@company.com"
                       required/>
                <a onClick={this.login} style={{float:'right', marginTop:'5px'}}>I already have an account</a>
              </Form.Field>
              <Form.Checkbox label="It’s ok to send me very occasional emails about security and Ease.space"
                             onClick={this.props.switchNewsletter}
                             checked={this.props.newsletter}/>
              <Message error content={this.state.errorMessage}/>
              <Form.Button positive fluid loading={this.state.processing} type="submit">Next</Form.Button>
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
    post_api.common.checkRegistrationDigits(this.props.email, this.props.digits).then(response => {
      this.setState({loading: false});
      this.props.onStepValidated();
        easeTracker.trackEvent("TeamCreationEnterDigits", {
            "plan_id": this.props.plan_id
        });
        easeTracker.trackEvent("RegistrationEnterDigits");
    }).catch(err => {
      this.setState({loading: false, errorMessage: err});
    })
  }
  resendDigits(){
    this.setState({sendingEmail: true});
    post_api.common.askRegistration(this.props.email).then(response => {
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

class Step3 extends React.Component{
  constructor(props){
    super(props);
    this.state = {
      errorMessage: '',
      passwordError: false,
      confirmPasswordMessage: "Passwords are different"
    };
    this.onSubmit = this.onSubmit.bind(this);
  }
  onSubmit(e){
    e.preventDefault();

    this.setState({errorMessage: '', passwordError: false});
    if (this.props.password.match(passwordRegexp) === null){
      this.setState({passwordError: true});
      return;
    }
    if (this.props.password !== this.props.confirmPassword){
      this.setState({errorMessage: this.state.confirmPasswordMessage});
      return;
    }
    this.setState({errorMessage: '', passwordError: false});
      easeTracker.trackEvent("TeamCreationEnterPassword", {
          "plan_id": this.props.plan_id
      });
      easeTracker.trackEvent("RegistrationEnterPassword");
    this.props.onStepValidated();
  }
  render() {
    return (
        <div class="contents" id="step3">
          <Segment>
            <Header as="h1">
              Set your password
              <Header.Subheader>
                Choose a strong password for sign in to Ease.space
              </Header.Subheader>
            </Header>
            <Divider hidden clearing/>
            <Form onSubmit={this.onSubmit} error={this.state.errorMessage.length > 0}>
              <Form.Field required error={this.state.passwordError}>
                <label>Password</label>
                <Input
                    onChange={this.props.handleInput}
                    type="password"
                    name="password"
                    placeholder="Password"
                    required/>
                <Label pointing color={this.state.passwordError ? 'red': null} basic={this.state.passwordError}>Your password must contain at least 8 characters, 1 uppercase, 1 lowercase and 1 number</Label>
              </Form.Field>
              <Form.Input
                  label="Confirm password"
                  onChange={this.props.handleInput}
                  type="password"
                  name="confirmPassword"
                  placeholder="Confirmation"
                  required/>
              <Message error content={this.state.errorMessage}/>
              <Form.Button positive fluid type="submit">Next</Form.Button>
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
      easeTracker.trackEvent("TeamCreationEnterUsername", {
          "plan_id": this.props.plan_id
      });
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
                       onChange={this.props.handleUsernameInput}
                       name="username"
                       placeholder="username"
                       value={this.props.username}
                       required/>
                <Label pointing basic={this.props.usernameError} color={this.props.usernameError ? 'red': null}>Please choose a username that is all lowercase, containing only letters, numbers, periods, hyphens and underscores. From 3 to 22 characters.</Label>
              </Form.Field>
              <Message error content={this.state.errorMessage}/>
              <Form.Field>
                <Button positive fluid type="submit" disabled={this.props.username.length < 3}>Next</Button>
              </Form.Field>
            </Form>
          </Segment>
        </div>
    )
  }
}

class StepCGU extends React.Component{
  constructor(props){
    super(props);
    this.state = {
      loading: false
    }
  }
  submit = () => {
    this.setState({loading: true});
    post_api.common.registration(this.props.email, this.props.username, this.props.password, this.props.digits, null, this.props.newsletter).then(response => {
      this.setState({loading: false});
        easeTracker.trackEvent("TeamCreationAcceptCGU", {
            "plan_id": this.props.plan_id
        });
        easeTracker.trackEvent("RegistrationAcceptCGU");
      this.props.onStepValidated();
    }).catch(err => {
      this.setState({loading: false});
    });
  };
  render() {
    return (
        <div class="contents">
          <Segment>
            <Header as="h1">
              Review the General Terms
            </Header>
            <Divider hidden clearing/>
            <Container style={{maxHeight: '300px', overflow:'hidden', marginBottom: '1rem', paddingLeft: '0'}}>
              Before continuing your registration, please read our General Terms and Privacy Policy.
            </Container>
            <p>
              By clicking « I Agree », you understand and agree to our <a href="/resources/CGU_Ease.pdf" target="_blank">General Terms</a> and <a href="/resources/Privacy_Policy.pdf" target="_blank">Privacy Policy</a>.
            </p>
            <Button positive fluid loading={this.state.loading} onClick={this.submit}>I Agree</Button>
          </Segment>
        </div>
    )
  }
}

class Step5 extends React.Component {
    constructor(props) {
        super(props);
        this.onSubmit = this.onSubmit.bind(this);
        this.roles = jobRoles.map((item, idx) => {
            return {
                key: idx,
                value: idx,
                text: item
            }
        });
        this.jobRole = props.jobRole;
        this.jobDetails = props.jobDetails;
    }

    onSubmit(e) {
        e.preventDefault();
        this.props.incStep();
        easeTracker.trackEvent("TeamCreationEnterJob", {
            "job": this.roles[this.props.jobRole],
            "detail": this.props.jobDetails,
            "plan_id": this.props.plan_id
        });
    }

    render() {
        return (
            <div class="contents" id="step5">
                <Segment>
                    <Header as="h1">
                        Tell us about your role
                    </Header>
                    <Divider hidden clearing/>
                    <Form onSubmit={this.onSubmit}>
                        <Form.Field>
                            <label>What type of work do you do?</label>
                            <Select placeholder="Select your role" name="jobRole" options={this.roles}
                                    onChange={this.props.handleInput}/>
                        </Form.Field>
                        {this.props.jobRole === 15 &&
                        <Form.Input
                            label="Could you please elaborate? (More info will help us improve Ease.space!)"
                            placeholder="Details..."
                            type="text"
                            name="jobDetails"
                            required
                            onChange={this.props.handleInput}/>}
                        <Form.Field>
                            <Button positive fluid type="submit"
                                    disabled={this.props.jobRole === null || (this.props.jobRole === 15 && this.props.jobDetails.length === 0)}>Next</Button>
                        </Form.Field>
                    </Form>
                </Segment>
            </div>
        )
    }
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
    post_api.teams.createTeam({
      name: this.props.teamName,
      email: this.props.email,
      first_name: this.props.first_name,
      last_name: this.props.last_name,
      username: this.props.username,
      jobRole: this.props.jobRole,
      jobDetails: this.props.jobDetails,
      digits: this.props.digits,
        plan_id: this.props.plan_id
    }).then(response => {
      const teamId = response.id;
      this.props.handleInput(null, {name:"teamId", value:teamId});
      this.setState({loading: false});
        easeTracker.trackEvent("TeamCreationFinished", {
            "plan_id": this.props.plan_id
        });
        easeTracker.trackEvent("RegistrationDone");
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
    authenticated: store.common.authenticated
  };
})
class TeamCreationView extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      currentStep : 0,
      steps : [],
      email: '',
      newsletter: false,
      digits: '',
      password: '',
      confirmPassword: '',
      fname: '',
      lname: '',
      username:'',
      usernameError: false,
      jobRole: null,
      jobDetails: '',
      teamName: '',
      teamId: -1,
      plan_id: 0,
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
  }
  handleCompanyInfoInput(e){
    var info = this.state.companyInformation;
    console.log(e.target.name);
    console.log(e.target.value);
    info[e.target.name] = e.target.value;
    this.setState({companyInformation: info});
  }
  handleUsernameInput = (e, {name, value}) => {
    if (value && value.match(/[a-zA-Z0-9\s_\-]/gi)) {
      if (value.match(/[a-zA-Z0-9\s_\-]/gi).length === value.length && value.length <= 22)
        this.setState({ [name]: value.toLowerCase().replace(/\s/gi, '_'), usernameError: false });
      else
        this.setState({ usernameError: true });
    }
    else
      this.setState({ [name]: '', usernameError: true });
  };
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
    window.location.href = '/';
    window.location.reload(true);
  }
  handleInput(e, {value , name}){
    this.setState({[name]: value});
  }
  incrementStep(){
    this.setState({currentStep: this.state.currentStep + 1});
  }
  componentDidMount() {
    const query = queryString.parse(this.props.location.search);
    let plan_id = 0;
    if (query.plan_id !== undefined && query.plan_id.length !== 0)
      plan_id = query.plan_id;
    if (this.props.authenticated)
      this.props.history.replace(`/main/simpleTeamCreation?plan_id=${plan_id}`);
      this.setState({plan_id: Number(plan_id)});
  }
  render(){
    var steps = [];
    steps.push(<Step1 onStepValidated={this.incrementStep}
                      plan_id={this.state.plan_id}
                      handleInput={this.handleInput}
                      email={this.state.email}
                      switchNewsletter={this.switchNewsletter}
                      newsletter={this.state.newsletter}
                      dispatch={this.props.dispatch}
                      history={this.props.history}
                      key="1"/>);
    steps.push(<Step2 onStepValidated={this.incrementStep}
                      plan_id={this.state.plan_id}
                      digits={this.state.digits}
                      email={this.state.email}
                      handleInput={this.handleInput}
                      key="2"/>);
    steps.push(<Step3 onStepValidated={this.incrementStep}
                      plan_id={this.state.plan_id}
                      password={this.state.password}
                      confirmPassword={this.state.confirmPassword}
                      handleInput={this.handleInput}
                      key="3"/>);
    steps.push(<Step4 onStepValidated={this.incrementStep}
                      plan_id={this.state.plan_id}
                      lname={this.state.lname}
                      fname={this.state.fname}
                      username={this.state.username}
                      usernameError={this.state.usernameError}
                      handleInput={this.handleInput}
                      handleUsernameInput={this.handleUsernameInput}
                      key="4"/>);
    steps.push(<StepCGU key="cgu"
                        plan_id={this.state.plan_id}
                        email={this.state.email}
                        password={this.state.password}
                        newsletter={this.state.newsletter}
                        digits={this.state.digits}
                        lname={this.state.lname}
                        fname={this.state.fname}
                        username={this.state.username}
                        onStepValidated={this.incrementStep}/>);
    steps.push(<Step5 incStep={this.incrementStep}
                      plan_id={this.state.plan_id}
                      handleInput={this.handleInput}
                      jobRole={this.state.jobRole}
                      jobDetails={this.state.jobDetails}
                      key="5"/>);
    steps.push(<Step6 onStepValidated={this.incrementStep}
                      username={this.state.username}
                      first_name={this.state.fname}
                      last_name={this.state.lname}
                      email={this.state.email}
                      plan_id={this.state.plan_id}
                      teamName={this.state.teamName}
                      jobRole={this.state.jobRole}
                      jobDetails={this.state.jobDetails}
                      handleInput={this.handleInput}
                      key="6"/>);
    steps.push(<InvitePeopleStep
        key="7"
        ws_id={this.props.ws_id}
        dispatch={this.props.dispatch}
        team_id={this.state.teamId}
        onStepValidated={this.submitStep8}/>);
    return (
        <div id="team_creation_view" class="full_screen_centered_view">
          <SingleEaseLogo/>
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

module.exports = withRouter(TeamCreationView);