import React from "react";
import ReactCSSTransitionGroup from "react-addons-css-transition-group";
import {passwordRegexp, emailRegexp, jobRoles, checkTeamUsernameErrors} from "../../utils/utils";
import queryString from "query-string";
import {processLogout, setLoginRedirectUrl} from "../../actions/commonActions";
import {connect} from "react-redux";
import SingleEaseLogo from "../common/SingleEaseLogo";
import { Header, Container, Segment, Checkbox, Form, Input,Divider, Icon, List, Select, Dropdown, Button, Grid, Message, Label,Transition } from 'semantic-ui-react';
var api = require('../../utils/api');
var post_api = require('../../utils/post_api');

class Step1 extends React.Component {
  constructor(props){
    super(props);
    this.createAccount = this.createAccount.bind(this);
    this.login = this.login.bind(this);
  }
  createAccount(){
    this.props.onStepValidated();
  }
  login(){
    this.props.dispatch(setLoginRedirectUrl(this.props.match.url + '?skip'));
    this.props.history.replace('/login');
  }
  render(){
    return (
        <div class="contents" id="step1">
          <Segment>
            <Header as="h1">
              Do you already have an Ease.space account ?
              <Header.Subheader style={{marginTop:'1rem'}}>
                Ease.space helps you secure, manage and organize web accesses in your team.
              </Header.Subheader>
              </Header>
            <Divider hidden fitted/>
            <Button primary onClick={this.createAccount} fluid>
              Not yet! I am new on Ease.space
            </Button>
            <Divider/>
            <Button positive onClick={this.login} fluid>
              Yes! I already have an account
            </Button>
          </Segment>
        </div>
    )
  }
}

class Step2 extends React.Component{
  constructor(props){
    super(props);
    this.state = {
      errorMessage: ''
    };
    this.onSubmit = this.onSubmit.bind(this);
  }
  onSubmit(e){
    e.preventDefault();
    let usernameErrors = checkTeamUsernameErrors(this.props.username);
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
                <Form.Input required
                            value={this.props.fname}
                            label='First name'
                            placeholder='First name'
                            onChange={this.props.handleInput}
                            name="fname" type="text"/>
                <Form.Input required
                            value={this.props.lname}
                            label='Last name'
                            placeholder='Last name'
                            onChange={this.props.handleInput}
                            name="lname" type="text"/>
              </Form.Group>
              <Form.Field required>
                <label>Username</label>
                <Input type="text"
                       value={this.props.username}
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
  }
  submit = () => {
      this.props.onStepValidated();
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
            <Button positive fluid onClick={this.submit}>I Agree</Button>
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

function Step4(props){
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
          <Form onSubmit={props.onStepValidated}>
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
              <Button positive fluid type="submit" disabled={jobRole === null || (jobRole === 15 && jobDetails.length === 0)}>Next</Button>
            </Form.Field>
          </Form>
        </Segment>
      </div>
  )
}

@connect((store)=>{
  return {
    common: store.common
  };
})
class TeamJoinView extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      email: '',
      fname: '',
      lname: '',
      username: '',
      newsletter:'',
      password: '',
      confirmPassword: '',
      jobRole:null,
      jobDetails: '',
      currentStep: 0,
      code: '',
      teamUser: null,
      skipRegistration: false
    };
    this.handleInput = this.handleInput.bind(this);
    this.incrementStep = this.incrementStep.bind(this);
    this.finalizeModal = this.finalizeModal.bind(this);
    this.canSkip = this.canSkip.bind(this);
    const query = queryString.parse(this.props.location.search);
    if (query.skip !== undefined) {
      this.state.skipRegistration = true;
    }
  }
  canSkip(){
    return this.state.skipRegistration && this.props.common.authenticated;
  }
  finalizeModal(){
    if (this.canSkip()){
      post_api.teams.finalizeRegistration(this.props.common.ws_id, this.state.fname, this.state.lname, this.state.username, this.state.jobRole, this.state.jobDetails, this.state.code).then(response => {
        window.location.href = '/';
      });
    }else {
      post_api.common.registration(this.state.teamUser.email, this.state.username, this.state.password, null, this.state.code, false).then(r => {
        post_api.teams.finalizeRegistration(this.props.common.ws_id, this.state.fname, this.state.lname, this.state.username, this.state.jobRole, this.state.jobDetails, this.state.code).then(response => {
          window.location.href = '/';
        });
      })
    }
  }
  handleInput(e, {value , name}){
    this.setState({[name]: value});
  }
  incrementStep(){
      this.setState({currentStep: this.state.currentStep + 1});
  }
  componentDidMount(){
    if (this.props.common.authenticated && !this.state.skipRegistration)
      this.props.dispatch(processLogout()).then(() => {
        api.teams.finalizeRegistration(this.props.match.params.code).then(response => {
          this.setState({teamUser: response, code: this.props.match.params.code});
        }).catch(err => {
          console.log(err);
        });
      });
    else {
      api.teams.finalizeRegistration(this.props.match.params.code).then(response => {
        this.setState({teamUser: response, code: this.props.match.params.code});
      }).catch(err => {
        console.log(err);
      });
    }
  }
  render(){
    var steps = [];
    if (!this.canSkip())
      steps.push(<Step1 onStepValidated={this.incrementStep}
                      dispatch={this.props.dispatch}
                      match={this.props.match}
                      history={this.props.history}
                      key="1"/>);
    steps.push(<Step2 onStepValidated={this.incrementStep}
                      handleInput={this.handleInput}
                      lname={this.state.lname}
                      fname={this.state.fname}
                      username={this.state.username}
                      key="2"/>);
    if (!this.canSkip())
      steps.push(<StepCGU onStepValidated={this.incrementStep}/>);
    if (!this.canSkip())
      steps.push(<Step3 onStepValidated={this.incrementStep}
                      password={this.state.password}
                      confirmPassword={this.state.confirmPassword}
                      handleInput={this.handleInput}
                      key="3"/>);
    steps.push(<Step4 onStepValidated={this.finalizeModal}
                      handleInput={this.handleInput}
                      jobRole={this.state.jobRole}
                      jobDetails={this.state.jobDetails}
                      key="4"/>);

    return (
        <div id="team_join_view" class="full_screen_centered_view">
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

module.exports = TeamJoinView;