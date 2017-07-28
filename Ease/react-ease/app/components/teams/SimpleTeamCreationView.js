var React = require('react');
var classnames = require('classnames');
var post_api = require('../../utils/post_api');
var api = require('../../utils/api');
var axios = require('axios');
var StripeCardForm = require('../stripe/StripeCardForm');
var CompanyInformationForm = require('../common/CompanyInformationForm');

import {passwordRegexp, emailRegexp, jobRoles} from "../../utils/utils";
import {FormInput} from '../common/FormComponents';
import {Elements} from 'react-stripe-elements';
import ReactCSSTransitionGroup from "react-addons-css-transition-group";
import SingleEaseLogo from "../common/SingleEaseLogo";

class Step1 extends React.Component{
  constructor(props){
    super(props);
    this.state = {
      emailChecked: false,
      emailMessage: '',
      processing: false
    };
    this.onSubmit = this.onSubmit.bind(this);
  }
  onSubmit(e){
    e.preventDefault();
    this.setState({emailChecked: false, processing: true});
    post_api.teams.askTeamCreation(this.props.email).then(response => {
      this.setState({processing: false});
      if (response.need_digits)
        this.props.incrementStep(1);
      else
        this.props.incrementStep(2);
    }).catch(err => {
      this.setState({processing: false});
      this.setState({emailChecked: true, emailMessage: err});
    });
  }
  render() {
    return (
        <div class="contents" id="step1">
          <div class="content_row">
            <h1>Enter your professional email</h1>
          </div>
          <form onSubmit={this.onSubmit}>
            <div class="content_row flex_direction_column">
              <span>Your email address</span>
              <FormInput value={this.props.email}
                         onChange={this.props.handleInput}
                         checked={this.state.emailChecked}
                         validation_message={this.state.emailMessage}
                         type="email"
                         name="email"
                         classes="input_unstyle modal_input"
                         placeholder="name@company.com"
                         required={true}/>
            </div>
            <div class="content_row align_items_center">
              <input type="checkbox" style={{margin: "0 10px 0 0"}} checked={this.props.newsletter}
                     onClick={this.props.switchNewsletter}/>
              <span>It's ok to send me (very occasional) email about Ease.space service</span>
            </div>
            <div class="content_row justify_content_center">
              <button class="button-unstyle big-button" type="submit" disabled={this.state.processing}>
                Next
              </button>
            </div>
          </form>
        </div>
    )
  }
}

class Step2 extends React.Component{
  constructor(props){
    super(props);
    this.state = {
      digitsChecked: false,
      digitsMessage: '',
      processing: false
    };
    this.onSubmit = this.onSubmit.bind(this);
  }
  onSubmit(e){
    e.preventDefault();
    this.setState({digitsChecked: false, processing: true});
    post_api.teams.checkTeamCreationDigits(this.props.email, this.props.digits).then(response => {
      this.setState({processing: false});
      this.props.onStepValidated();
    }).catch(err => {
      this.setState({processing: false, digitsChecked: true, digitsMessage: err});
    })
  }
  render() {
    return (
        <div class="contents" id="step2">
          <div class="content_row flex_direction_column">
            <h1>Check your email</h1>
            <span>We've send a six-digit confirmation code to <strong>{this.props.email}</strong>.It will expire shortly, so enter your code soon.</span>
          </div>
          <form onSubmit={this.onSubmit}>
            <div class="content_row flex_direction_column">
              <span>Your confirmation code</span>
              <FormInput value={this.props.digits}
                         onChange={this.props.handleInput}
                         checked={this.state.digitsChecked}
                         validation_message={this.state.digitsMessage}
                         type="number"
                         name="digits"
                         classes="input_unstyle modal_input"
                         placeholder="Confirmation code"
                         required={true}/>
              <span>Keep this window open while checking for your code.<br/> Haven't received our email ? Try your spam folder!</span>
            </div>
            <div class="content_row justify_content_center">
              <button class="button-unstyle big-button" type="submit" disabled={this.state.processing}>
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
        <div class="contents" id="step3">
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

class Step4 extends React.Component{
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
        <div class="contents" id="step4">
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

function Step5(props){
  return (
      <div class="contents" id="step5">
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
          <span>Could you please elaborate? (More info will help us improve Ease.space!)</span>
          <input type="text" name="jobDetails" class="input_unstyle modal_input"
                 value={props.jobDetails} onChange={props.handleInput}/>
        </div>
        }
        <div class="content_row justify_content_center">
          <button class="button-unstyle big-button" onClick={props.incStep}
                  disabled={props.jobRoles == '15' && props.jobDetails.length === 0}>
            Next
          </button>
        </div>
      </div>
  )
}

class Step6 extends React.Component{
  constructor(props){
    super(props);
    this.onSubmit = this.onSubmit.bind(this);
  }
  onSubmit(e){
    e.preventDefault();
    post_api.teams.createTeam(this.props.teamName, this.props.email,this.props.first_name, this.props.last_name, this.props.username, this.props.jobRole, this.props.jobDetails, this.props.digits).then(response => {
      const teamId = response.id;
      this.props.handleInput({target: {name:"teamId", value:teamId}});
      this.props.onStepValidated();
    }).catch(err => {

    });
  }
  render() {
    return (
        <div class="contents" id="step6">
          <div class="content_row">
            <h1>What's your company called ?</h1>
          </div>
          <form onSubmit={this.onSubmit}>
            <div class="content_row flex_direction_column">
              <span>Company name</span>
              <input type="text"
                     value={this.props.teamName}
                     onChange={this.props.handleInput}
                     name="teamName"
                     class="input_unstyle modal_input"
                     placeholder="Company name"
                     required/>
              <span>We will use this to name your Ease.space team, which can always change later</span>
            </div>
            <div class="content_row justify_content_center">
              <button class="button-unstyle big-button" type="submit">
                Create team
              </button>
            </div>
          </form>
        </div>
    )
  }
}

class Step7 extends React.Component{
  constructor(props){
    super(props);
    this.onSubmit = this.onSubmit.bind(this);
  }
  onSubmit(e){
    e.preventDefault();
    var calls = [];
    this.props.invitations.map(function (item) {
      if (item.email.match(emailRegexp) !== null && item.username.length > 0){
        calls.push(post_api.teamUser.createTeamUser(this.props.teamId, '', '', item.email, item.username, null, '1'));
      }
    }, this);
    axios.all(calls).then(() => {
      this.props.handleInput({target: {name: "invitedPeople", value: calls.length}});
      this.props.onStepValidated();
    });
  }
  render() {
    return (
        <div class="contents" id="step7">
          <div class="content_row flex_direction_column">
            <h1>Send invitations</h1>
            <span>Your Ease.space team is ready to go. Know few coworkers who'd like to stop using passwords with you?</span>
          </div>
          <div class="content_row flex_direction_column">
            <div class="display-flex mrgnBottom5">
              <span style={{width: "55%", marginRight: "15px"}}>Email address</span>
              <span style={{width: "45%"}}>Username(editable later)</span>
            </div>
            {this.props.invitations.map(function (item, idx) {
              return (
                  <div class="display-flex mrgnBottom5" key={idx}>
                    <input type="email" class="input_unstyle modal_input"
                           placeholder="Email"
                           style={{width: "55%", marginRight: "15px"}}
                           value={item.email}
                           onChange={e => {
                             this.props.editInvitationEmail(e.target.value, idx)
                           }}/>
                    <input type="text" class="input_unstyle modal_input"
                           placeholder="Username"
                           style={{width: "45%"}}
                           value={item.username}
                           onChange={e => {
                             this.props.editInvitationUsername(e.target.value, idx)
                           }}/>
                  </div>
              )
            }, this)}
          </div>
          <div class="content_row">
            <button class="button-unstyle mrgnBottom5" onClick={this.props.addInvitationField}>
              <i class="fa fa-plus mrgnRight5"/>
              <u>Add another field</u>
            </button>
          </div>
          <div class="content_row justify_content_center">
            <button class="button-unstyle big-button"
                    style={{marginRight: "10px"}}
                    onClick={this.props.incStep}>
              Skip for now
            </button>
            <button class="button-unstyle big-button"
                    onClick={this.onSubmit}>
              Send invitations
            </button>
          </div>
        </div>
    )
  }
}

class Step8 extends React.Component{
  constructor(props){
    super(props);
    this.state = {
      companyInfoConfirmed: false,
      friendsInvited: false
    };
    this.confirmCompanyInfo = this.confirmCompanyInfo.bind(this);
    this.tokenCallback = this.tokenCallback.bind(this);
    this.inviteFriends = this.inviteFriends.bind(this);
  }
  inviteFriends(e){
    e.preventDefault();
    const f = this.props.friends;

    post_api.teams.inviteFriends(this.props.teamId, f[0].email,f[1].email,f[2].email).then(response => {
      this.props.handleInput({target: {value: 15, name: 'credits'}});
      this.setState({friendsInvited: true});
    }).catch(err => {

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
          <div class="content_row flex_direction_column">
            <h1>Checkout</h1>
            {this.state.friendsInvited ?
                <span>You have {this.props.credits} euros in credits !</span>
                :
                <form class="display-flex flex_direction_column" onSubmit={this.inviteFriends}>
                  <span>You have {this.props.credits} euros in credits !</span>
                  <strong>Invite 3 friends and get up to 15 euros in credits</strong>
                  {
                    this.props.friends.map(function (item, idx) {
                      return (
                          <input type="email"
                                 class="input_unstyle modal_input"
                                 value={item.email}
                                 onChange={e => {
                                   this.props.editFriendsEmail(e.target.value, idx)
                                 }}
                                 placeholder="friend@company.com"
                                 key={idx}
                                 required/>
                      )
                    }, this)
                  }
                  <span>
              Make sure these are the right email addresses, otherwise it won't work, and you'll get charged back:(
            </span>
                  <button class="button-unstyle big-button" type="submit">
                    Update the bill !
                  </button>
                </form>
            }
          </div>
          <div class="content_row flex_direction_column">
            <h1>Billing summary</h1>
            <span>You are buying the <strong>Starter plan</strong></span>
            <span>You are paying <strong>Mensually</strong></span>
            <span>Your price per seat is 3,99e (before 20% VAT)</span>
            <span>You will pay 3,99e now (after credit)</span>
            <span>With our Fair Billing Policy, we believe you only pay for active users. Right now you will pay for 1 user only (you), and we will update your billing when your members arrive or leave your team.</span>
            <span>Estimations: you invited {this.props.invitedPeople} people in your team, when everybody has arrived, you will pay {(this.props.invitedPeople + 1) * 3.99}e/month (before 20% VAT).</span>
          </div>
          <div class="content_row flex_direction_column">
            <button class="button-unstyle big-button" onClick={this.props.onStepValidated}>
              Ok ! I wanna see my team now !
            </button>
          </div>
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
      jobRole: '0',
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
    console.log('All done fine');
    this.props.history.push('/teams/' + this.state.teamId);
  }
  handleInput(e){
    this.setState({[e.target.name]: e.target.value});
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