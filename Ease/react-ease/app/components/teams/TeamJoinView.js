import React from "react";
import classnames from "classnames";
import {FormInput} from '../common/FormComponents';
import ReactCSSTransitionGroup from "react-addons-css-transition-group";
import {passwordRegexp, emailRegexp, jobRoles} from "../../utils/utils";
import queryString from "query-string";
import {processLogout} from "../../actions/commonActions"
import {connect} from "react-redux"
var api = require('../../utils/api');
var post_api = require('../../utils/post_api');

class Step1 extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      choice: -1
    };
    this.chooseMode = this.chooseMode.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
  }
  onSubmit(){
      if (this.state.choice === 0){
        this.props.onStepValidated();
      }
      if (this.state.choice === 1){
        //redirect to login page
      }
  }
  chooseMode(mode){
    this.setState({choice: mode});
  }
  render(){
    return (
        <div class="contents" id="step1">
          <div class="cotent_row text-center">
            <h1>Do we know each other?</h1>
          </div>
          <div class="content_row justify_content_center">
            <button class={classnames('button-unstyle choice-button', this.state.choice === 0 ? 'selected':null)}
                    onClick={this.chooseMode.bind(null, 0)}
                    style={{marginRight: '20px'}}>
              Not yet! I am new on Ease.space
            </button>
            <button class={classnames('button-unstyle choice-button', this.state.choice === 1 ? 'selected':null)}
                    onClick={this.chooseMode.bind(null, 1)}>
              Yes! I already have an Ease.space account
            </button>
          </div>
          <div class="content_row justify_content_center">
            <button class="button-unstyle big-button" type="submit"
                    disabled={this.state.choice === -1}
                    onClick={this.onSubmit}>
              Next
            </button>
          </div>
        </div>
    )
  }
}

class Step2 extends React.Component{
  constructor(props){
    super(props);
    this.onSubmit = this.onSubmit.bind(this);
  }
  onSubmit(e){
    e.preventDefault();
    this.props.onStepValidated();
  }
  render() {
    return (
        <div class="contents" id="step1">
          <div class="content_row flex_direction_column">
            <h1>What's your name</h1>
            <span>Your name will be displayed for your team members in Ease.space</span>
          </div>
          <form onSubmit={this.onSubmit}>
            <div class="content_row flex_direction_column">
              <span>Your name</span>
              <div class="display-flex">
                <input type="text"
                       value={this.props.fname}
                       onChange={this.props.handleInput}
                       name="fname"
                       class="input_unstyle modal_input full_flex mrgnRight5"
                       placeholder="First name"
                       required/>
                <input type="text"
                       value={this.props.lname}
                       onChange={this.props.handleInput}
                       name="lname"
                       class="input_unstyle modal_input full_flex"
                       placeholder="Last name"
                       required/>
              </div>
            </div>
            <div class="content_row flex_direction_column">
              <span>Username</span>
              <input type="text"
                     value={this.props.username}
                     onChange={this.props.handleInput}
                     name="username"
                     class="modal_input input_unstyle"
                     placeholder="Username"
                     required/>
              <span>Please choose a username that is all lowercase, containing only letters, numbers, periods, hyphens and underscores. Maximum 22 characters.</span>
            </div>
            <div class="content_row justify_content_center">
              <button class="button-unstyle big-button" type="submit">
                Next
              </button>
            </div>
          </form>
        </div>
    )
  }
}

class Step3 extends React.Component{
  constructor(props){
    super(props);
    this.state = {
      passwordChecked: false,
      passwordMessage: 'Password must be at least 8 character long and contain 1 uppercase, 1 lowercase, 1 number',
      confirmPasswordChecked: false,
      confirmPasswordMessage: "Passwords doesn't match"
    };
    this.onSubmit = this.onSubmit.bind(this);
  }
  onSubmit(e){
    e.preventDefault();

    if (this.props.password.match(passwordRegexp) === null){
      this.setState({passwordChecked: true});
      return;
    } else {
      this.setState({passwordChecked: false})
    }
    if (this.props.password != this.props.confirmPassword){
      this.setState({confirmPasswordChecked: true});
      return;
    } else {
      this.setState({confirmPasswordChecked: false});
    }
    this.props.onStepValidated();
  }
  render() {
    return (
        <div class="contents" id="step2">
          <div class="content_row  flex_direction_column">
            <h1>Set your password</h1>
            <span>Choose a strong password for sign in to Ease.space</span>
          </div>
          <form onSubmit={this.onSubmit}>
            <div class="content_row flex_direction_column">
              <span>Password</span>
              <FormInput value={this.props.password}
                         onChange={this.props.handleInput}
                         checked={this.state.passwordChecked}
                         validation_message={this.state.passwordMessage}
                         type="password"
                         name="password"
                         classes="input_unstyle modal_input"
                         placeholder="Password"
                         required={true}/>
            </div>
            <div class="content_row flex_direction_column">
              <span>Confirm password</span>
              <FormInput value={this.props.confirmPassword}
                         onChange={this.props.handleInput}
                         checked={this.state.confirmPasswordChecked}
                         validation_message={this.state.confirmPasswordMessage}
                         type="password"
                         name="confirmPassword"
                         classes="input_unstyle modal_input"
                         placeholder="Confirmation"
                         required={true}/>
            </div>
            <div class="content_row justify_content_center">
              <button type="submit" class="button-unstyle big-button">
                Continue to Team info
              </button>
            </div>
          </form>
        </div>
    )
  }
}

function Step4(props){
  return (
      <div class="contents" id="step3">
        <div class="content_row">
          <h1>Tell us about your role</h1>
        </div>
        <div class="content_row flex_direction_column">
          <span>What type of work do you do?</span>
          <select name='jobRole' class="full_width select_unstyle modal_input"
                  value={props.jobRole} onChange={props.handleInput}>
            {jobRoles.map(function (item, idx) {
              return (
                  <option value={idx} key={idx}>{item}</option>
              )})}
          </select>
        </div>
        {props.jobRole == '15' &&
                <div class="content_row flex_direction_column">
                  <span>Could you please elaborate? (More info will help us improve Slack!)</span>
                  <input type="text" name="jobDetails" class="input_unstyle modal_input"
                          value={props.jobDetail} onChange={props.handleInput}/>
                </div>
        }
        <div class="content_row justify_content_center">
          <button class="button-unstyle big-button" onClick={props.onStepValidated}
                  disabled={props.jobRoles == '15' && props.jobDetails.length === 0}>
            Next
          </button>
        </div>
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
      jobRole:0,
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
    if (this.canSkip()){
      this.state.currentStep = 1;
    }
  }
  canSkip(){
    return this.state.skipRegistration && this.props.common.authenticated;
  }
  finalizeModal(){
    if (this.canSkip()){
      post_api.teams.finalizeRegistration(this.state.fname, this.state.lname, this.state.username, this.state.jobRole, this.state.jobDetails, this.state.code).then(response => {
        this.props.history.push('/teams/' + this.state.teamUser.team_id);
      });
    }else {
      post_api.common.registration(this.props.common.user.email, this.state.username, this.state.password,this.state.code, false).then(r => {
        post_api.teams.finalizeRegistration(this.state.fname, this.state.lname, this.state.username, this.state.jobRole, this.state.jobDetails, this.state.code).then(response => {
          this.props.history.push('/teams/' + this.state.teamUser.team_id);
        });
      })
    }
  }
  handleInput(e){
    this.setState({[e.target.name]: e.target.value});
  }
  incrementStep(){
    if (this.state.currentStep === 1 && this.canSkip())
      this.setState({currentStep: 3});
    else
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
    steps.push(<Step1 onStepValidated={this.incrementStep}
                      key="1"/>);
    steps.push(<Step2 onStepValidated={this.incrementStep}
                      handleInput={this.handleInput}
                      lname={this.state.lname}
                      fname={this.state.fname}
                      username={this.state.username}
                      key="2"/>);
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