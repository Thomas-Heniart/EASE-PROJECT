import React from "react";
import ReactCSSTransitionGroup from "react-addons-css-transition-group";
import {passwordRegexp, emailRegexp, jobRoles, checkTeamUsernameErrors} from "../../utils/utils";
import queryString from "query-string";
import {processLogout, setLoginRedirectUrl} from "../../actions/commonActions";
import {connect} from "react-redux";
import SingleEaseLogo from "../common/SingleEaseLogo";
import CGUStep from "./CGUStep";
import LoadingScreen from '../common/LoadingScreen';
import { Header, Container, Segment, Checkbox, Form, Input,Divider, Icon, List, Select, Dropdown, Button, Grid, Message, Label,Transition } from 'semantic-ui-react';
var api = require('../../utils/api');
var post_api = require('../../utils/post_api');

class Step1 extends React.Component {
  constructor(props){
    super(props);
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
            <Divider hidden/>
            <Button primary onClick={this.props.onStepValidated} fluid>
              Not yet! I am new on Ease.space
            </Button>
            <Divider/>
            <Button positive onClick={this.props.login} fluid>
              Yes! I already have an account
            </Button>
          </Segment>
        </div>
    )
  }
}

class Step1AlreadyHaveAnAccount extends React.Component {
  constructor(props){
    super(props);
  }
  render(){
    return (
        <div class="contents">
          <Segment>
            <Header as="h1">
              Before joining {this.props.team_name}, connect to your account ({this.props.email})
            </Header>
            <Divider hidden/>
            <Button positive onClick={this.props.login} fluid>
              Continue
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
      errorMessage: '',
      usernameError: false
    };
    this.onSubmit = this.onSubmit.bind(this);
  }
  onSubmit(e){
    e.preventDefault();
    let usernameErrors = checkTeamUsernameErrors(this.props.username);
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
              <Form.Field required error={this.state.usernameError}>
                <label>Username</label>
                <Input type="text"
                       value={this.props.username}
                       onChange={this.props.handleInput}
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
              <Button positive
                      fluid
                      type="submit"
                      loading={props.loading}
                      disabled={jobRole === null || (jobRole === 15 && jobDetails.length === 0)}>Next</Button>
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
      account_exists: false,
      email: '',
      team_name: '',
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
      skipRegistration: false,
      loading: true,
      lastStepLoading: false
    };
    this.handleInput = this.handleInput.bind(this);
    this.incrementStep = this.incrementStep.bind(this);
    this.finalizeModal = this.finalizeModal.bind(this);
    this.canSkip = this.canSkip.bind(this);
  }
  login = () => {
    this.props.dispatch(setLoginRedirectUrl(this.props.match.url + '?skip'));
    this.props.history.replace('/login');
  };
  canSkip(){
    return this.state.skipRegistration && this.props.common.authenticated;
  }
  finalizeModal(){
    this.setState({lastStepLoading: true});
    if (this.canSkip()){
      post_api.teams.finalizeRegistration(this.props.common.ws_id, this.state.fname, this.state.lname, this.state.username, this.state.jobRole, this.state.jobDetails, this.state.code).then(response => {
        this.setState({lastStepLoading: false});
        window.location.href = '/';
      }).catch(err => {
        console.log(err);
      });
    }else {
      post_api.common.registration(this.state.email, this.state.username, this.state.password, null, this.state.code, false).then(r => {
        post_api.teams.finalizeRegistration(this.props.common.ws_id, this.state.fname, this.state.lname, this.state.username, this.state.jobRole, this.state.jobDetails, this.state.code).then(response => {
          this.setState({lastStepLoading: false});
          window.location.href = '/';
        }).catch(err => {
          console.log(err);
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
    const query = queryString.parse(this.props.location.search);
    if (query.skip !== undefined) {
      this.state.skipRegistration = true;
    }
    this.setState({loading: true});
    if (this.props.common.authenticated && !this.state.skipRegistration)
      this.props.dispatch(processLogout()).then(() => {
        api.teams.getInvitationInformation({code: this.props.match.params.code}).then(response => {
          const info = response;
          const teamUser = info.teamUser;
          this.setState({
            code: this.props.match.params.code,
            fname: teamUser.first_name,
            lname: teamUser.last_name,
            username: teamUser.username,
            email: info.email,
            team_name: info.team_name,
            account_exists:info.account_exists,
            loading: false
          });
        }).catch(err => {
          window.location.href = '/';
        });
      });
    else {
      api.teams.getInvitationInformation({code: this.props.match.params.code}).then(response => {
        const info = response;
        const teamUser = info.teamUser;
        this.setState({
          code: this.props.match.params.code,
          fname: teamUser.first_name,
          lname: teamUser.last_name,
          username: teamUser.username,
          email: info.email,
          team_name: info.team_name,
          account_exists:info.account_exists,
          loading: false
        });
      }).catch(err => {
        window.location.href = '/';
      });
    }
  }
  render(){
    var steps = [];
    if (!this.canSkip()) {
      if (!this.state.account_exists)
        steps.push(<Step1 onStepValidated={this.incrementStep}
                          login={this.login}
                          key="1"/>);
      else
        steps.push(<Step1AlreadyHaveAnAccount key="1"
                                              team_name={this.state.team_name}
                                              email={this.state.email}
                                              login={this.login}/>)
    }
    steps.push(<Step2 onStepValidated={this.incrementStep}
                      handleInput={this.handleInput}
                      lname={this.state.lname}
                      fname={this.state.fname}
                      username={this.state.username}
                      key="2"/>);
    if (!this.canSkip())
      steps.push(<CGUStep key="cgu" onStepValidated={this.incrementStep}/>);
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
                      loading={this.state.lastStepLoading}
                      key="4"/>);

    return (
        <div id="team_join_view" class="full_screen_centered_view">
          <SingleEaseLogo/>
          {this.state.loading &&
          <LoadingScreen/>}
          {!this.state.loading &&
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
          </ReactCSSTransitionGroup>}
        </div>
    )
  }
}

module.exports = TeamJoinView;