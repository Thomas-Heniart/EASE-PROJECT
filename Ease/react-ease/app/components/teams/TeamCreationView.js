var React = require('react');
var classnames = require('classnames');
var post_api = require('../../utils/post_api');
var api = require('../../utils/api');
var axios = require('axios');
import { NavLink } from 'react-router-dom';
import {passwordRegexp, emailRegexp, checkTeamUsernameErrors, jobRoles} from "../../utils/utils";
import {withRouter} from "react-router-dom";
import {setLoginRedirectUrl} from "../../actions/commonActions";
import ReactCSSTransitionGroup from "react-addons-css-transition-group";
import SingleEaseLogo from "../common/SingleEaseLogo";
import {connect} from "react-redux";

import { Header, Container, Segment, Checkbox, Form, Input,Divider, Icon, List, Select, Dropdown, Button, Grid, Message, Label,Transition } from 'semantic-ui-react';

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
      confirmPasswordMessage: "Passwords doesn't match"
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
            <Container style={{maxHeight: '300px', overflow:'auto', marginBottom: '1rem', paddingLeft: '0'}}>
              <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed sit amet nulla ipsum. Ut tincidunt nisi
                nec risus scelerisque, in hendrerit ligula blandit. Etiam iaculis dui quis iaculis lobortis. Morbi
                bibendum fermentum diam, at blandit urna vulputate at. Donec commodo, sapien quis sollicitudin
                vestibulum, justo augue sagittis lacus, et varius diam nunc a massa. Vivamus et auctor mauris. Nunc ut
                aliquet massa. In euismod pellentesque urna, vel dictum nibh vulputate et. Phasellus posuere rutrum
                mauris, vel porta erat vestibulum id. Etiam aliquet fermentum porttitor. Nam fermentum in dolor vitae
                porta. Vivamus condimentum at urna sodales egestas. Phasellus tristique justo at scelerisque
                condimentum.</p>
              <p>Sed varius interdum tincidunt. Cras ac rhoncus nisl. Vestibulum id fringilla risus, in euismod ante.
                Etiam tristique nunc elit, sed venenatis risus mollis eu. Nulla risus nulla, fermentum eget orci in,
                bibendum sollicitudin felis. Vivamus eros sem, aliquet a tempus non, blandit eu justo. Suspendisse ut
                turpis at leo lacinia volutpat. Sed at ante at lacus facilisis porttitor. Vestibulum ante ipsum primis
                in faucibus orci luctus et ultrices posuere cubilia Curae; Nam risus dui, volutpat nec ipsum eu, viverra
                scelerisque nulla. Etiam imperdiet tortor finibus tellus faucibus tincidunt. Integer elit purus, dictum
                ac facilisis vel, sodales et orci. Maecenas egestas gravida</p>
              <p>Nunc viverra velit in ullamcorper lobortis. In pharetra hendrerit ultricies. Integer et ipsum vel
                tortor tempus ornare vitae nec libero. Praesent faucibus in dolor sed efficitur. Proin consequat ligula
                sed neque luctus faucibus. Aenean justo risus, convallis sed lacus ac, rutrum vestibulum quam. Nulla in
                dapibus lectus. Integer sit amet felis turpis. Pellentesque scelerisque sodales justo at varius. Nulla
                pulvinar cursus enim vitae lobortis. Mauris eu arcu euismod, dignissim mauris in, vestibulum ante.
                Aenean congue, tellus sit amet gravida ultricies, dolor lacus vehicula tortor, ut vestibulum elit turpis
                eu urna. Ut quis urna porttitor, viverra eros tristique, suscipit risus.</p>
            </Container>
            <p>
              By clicking « I Agree », you understand and agree to our General Terms and <a>Privacy Policy</a>.
            </p>
            <Button positive fluid loading={this.state.loading} onClick={this.submit}>I Agree</Button>
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
      errorMessage: '',
      loading: false,
    };
    this.onSubmit = this.onSubmit.bind(this);
  }
  onSubmit(e){
    e.preventDefault();
    var calls = [];
    this.props.invitations.map(function (item) {
      if (item.email.match(emailRegexp) !== null && item.username.length > 0){
        calls.push(post_api.teamUser.createTeamUser(this.props.ws_id, this.props.teamId, '', '', item.email, item.username, null, 1));
      }
    }, this);
    this.setState({errorMessage: '', loading: true});
    axios.all(calls).then(() => {
      this.props.handleInput(null, {name: "invitedPeople", value: calls.length});
      this.setState({loading: false});
      this.props.onStepValidated();
    }).catch(err => {
      this.setState({errorMessage: err, loading: false});
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
                  required
                  value={item.email}
                  placeholder="Email"
                  onChange={(e, {value}) => {
                    this.props.editInvitationEmail(value, idx)
                  }}/>
            </Form.Field>
            <Form.Input required width={7} type="text"
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
            <Divider hidden clearing/>
            <Form onSubmit={this.onSubmit} error={this.state.errorMessage.length > 0}>
              <Form.Group>
                <Form.Field width={9}><label>Email address</label></Form.Field>
                <Form.Field width={7}><label>Username (editable later)</label></Form.Field>
              </Form.Group>
              {fields}
              <Form.Button primary onClick={this.props.addInvitationField}>
                <Icon name="add user"/>
                Add another field
              </Form.Button>
              <Message error content={this.state.errorMessage}/>
              <Form.Group>
                <Form.Button width={8} fluid type="button" onClick={this.props.onStepValidated}>Skip for now</Form.Button>
                <Form.Button width={8} fluid positive type="submit" loading={this.state.loading}>Send invitations</Form.Button>
              </Form.Group>
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
    window.location.href = '/';
  }
  handleInput(e, {value , name}){
    this.setState({[name]: value});
  }
  incrementStep(){
    this.setState({currentStep: this.state.currentStep + 1});
  }
  componentDidMount() {
    if (this.props.authenticated){
      this.props.history.push('/main/simpleTeamCreation');
    }
  }
  render(){
    var steps = [];
    steps.push(<Step1 onStepValidated={this.incrementStep}
                      handleInput={this.handleInput}
                      email={this.state.email}
                      switchNewsletter={this.switchNewsletter}
                      newsletter={this.state.newsletter}
                      dispatch={this.props.dispatch}
                      history={this.props.history}
                      key="1"/>);
    steps.push(<Step2 onStepValidated={this.incrementStep}
                      digits={this.state.digits}
                      email={this.state.email}
                      handleInput={this.handleInput}
                      key="2"/>);
    steps.push(<Step3 onStepValidated={this.incrementStep}
                      password={this.state.password}
                      confirmPassword={this.state.confirmPassword}
                      handleInput={this.handleInput}
                      key="3"/>);
    steps.push(<Step4 onStepValidated={this.incrementStep}
                      lname={this.state.lname}
                      fname={this.state.fname}
                      username={this.state.username}
                      handleInput={this.handleInput}
                      key="4"/>);
    steps.push(<StepCGU key="cgu"
                        email={this.state.email}
                        password={this.state.password}
                        newsletter={this.state.newsletter}
                        digits={this.state.digits}
                        lname={this.state.lname}
                        fname={this.state.fname}
                        username={this.state.username}
                        onStepValidated={this.incrementStep}/>);
    steps.push(<Step5 incStep={this.incrementStep}
                      handleInput={this.handleInput}
                      jobRole={this.state.jobRole}
                      jobDetails={this.state.jobDetails}
                      key="5"/>);
    steps.push(<Step6 onStepValidated={this.incrementStep}
                      username={this.state.username}
                      first_name={this.state.fname}
                      last_name={this.state.lname}
                      email={this.state.email}
                      teamName={this.state.teamName}
                      jobRole={this.state.jobRole}
                      jobDetails={this.state.jobDetails}
                      handleInput={this.handleInput}
                      key="6"/>);
    steps.push(<Step7 onStepValidated={this.submitStep8}
                      teamId={this.state.teamId}
                      handleInput={this.handleInput}
                      invitations={this.state.invitations}
                      editInvitationEmail={this.editInvitationEmail}
                      editInvitationUsername={this.editInvitationUsername}
                      removeInvitationField={this.removeInvitationField}
                      addInvitationField={this.addInvitationField}
                      ws_id={this.props.ws_id}
                      key="7"/>);
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