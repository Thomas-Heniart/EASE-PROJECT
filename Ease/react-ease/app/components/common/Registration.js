import React from "react";
import ReactCSSTransitionGroup from "react-addons-css-transition-group";
import {passwordRegexp, emailRegexp, jobRoles, checkTeamUsernameErrors, handleSemanticInput, userNameRuleString} from "../../utils/utils";
import queryString from "query-string";
import SingleEaseLogo from "../common/SingleEaseLogo";
import {withRouter} from "react-router-dom";
import { Header, Container, Segment, Checkbox, Form, Input,Divider, Icon, List, Select, Dropdown, Button, Grid, Message, Label,Transition } from 'semantic-ui-react';
var api = require('../../utils/api');
var post_api = require('../../utils/post_api');
import {connect} from "react-redux";

class Step1 extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      errorMessage: '',
      usernameError: false,
      loading: false
    }
  }
  onSubmit = (e) => {
    e.preventDefault();
    const usernameErrors = checkTeamUsernameErrors(this.props.username);
    if (usernameErrors.error){
      this.setState({usernameError: true});
      return;
    }
    this.setState({errorMessage: '', loading: true, usernameError: false});
    post_api.common.askRegistration(this.props.email).then(response => {
      this.setState({loading: false});
      this.props.onStepValidated();
    }).catch(err => {
      this.setState({errorMessage: err, loading: false});
    });
  };
  render(){
    return (
        <div class="contents">
          <Segment>
            <Header as="h1">
              What's your name?
            </Header>
            <Divider hidden clearing/>
            <Form onSubmit={this.onSubmit} error={this.state.errorMessage.length > 0}>
              <Form.Field required error={this.state.usernameError}>
                <label>Username</label>
                <Input
                    required
                    type="text"
                    placeholder="Username"
                    name="username"
                    value={this.props.username}
                    onChange={this.props.handleInput}/>
                <Label pointing color={this.state.usernameError ? 'red':null} basic={this.state.usernameError}>{userNameRuleString}</Label>
              </Form.Field>
              <Form.Input
                  required
                  label="Email"
                  placeholder="Email"
                  type="email"
                  name="email"
                  value={this.props.email}
                  onChange={this.props.handleInput}/>
              <Form.Checkbox label="It’s ok to send me very occasional emails about security and Ease.space"
                             name="newsletter"
                             checked={this.props.newsletter}
                             onClick={this.props.handleInput}/>
              <Message error content={this.state.errorMessage}/>
              <Form.Field>
                <Button positive fluid loading={this.state.loading} type="submit">Next</Button>
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
      confirmPasswordMessage: "Passwords are different"
    };
  }
  onSubmit = (e) => {
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
  };
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

@connect((store)=>{
  return {
    ws_id: store.common.ws_id,
    authenticated: store.common.authenticated
  };
})
class Registration extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      username: '',
      email: '',
      newsletter: false,
      digits: '',
      password: '',
      confirmPassword: '',
      currentStep: 0
    }
  }
  handleInput = handleSemanticInput.bind(this);
  incrementStep = () => {
    this.setState({currentStep: this.state.currentStep + 1});
  };
  finalizeRegistration = () => {
    window.location.href = "/";
  };
  componentDidMount(){
    if (this.props.authenticated)
      this.props.history.replace(`/main/simpleTeamCreation?plan_id=0`);
    const query = queryString.parse(this.props.location.search);
    if (query.email !== undefined){
      this.setState({email: query.email});
    }
  }
  render(){
    let steps = [];
    steps.push(<Step1 key="1"
                      onStepValidated={this.incrementStep}
                      handleInput={this.handleInput}
                      username={this.state.username}
                      email={this.state.email}
                      newsletter={this.state.newsletter}/>);
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
    steps.push(<StepCGU key="cgu"
                        email={this.state.email}
                        password={this.state.password}
                        newsletter={this.state.newsletter}
                        digits={this.state.digits}
                        username={this.state.username}
                        onStepValidated={this.finalizeRegistration}/>);
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

module.exports = withRouter(Registration);