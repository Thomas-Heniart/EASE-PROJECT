var React = require('react');
var classnames = require('classnames');
import {withCookies, Cookies } from 'react-cookie';
import post_api from '../../utils/post_api';
import {connect} from "react-redux";
import {setLoginRedirectUrl, fetchMyInformation, processConnection} from "../../actions/commonActions"

class UnknownUserForm extends React.Component{
  constructor(props){
    super(props);
    this.state = {
      email:'',
      password: '',
      errorMessage: '',
      error: false
    };
    this.handleInput = this.handleInput.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
  }
  handleInput(e){
    this.setState({[e.target.name]: e.target.value});
  }
  onSubmit(e){
    e.preventDefault();
    this.setState({error: false});
    this.props.setView('loading');

    this.props.dispatch(processConnection({
      email:this.state.email,
      password:this.state.password
    })).then(response => {
      this.props.finishLogin();
    }).catch(err => {
      this.setState({errorMessage:err, error: true, password: ''});
      this.props.setView('unknown');
    });
  }
  render() {
    return (
        <div class={classnames('easePopup landingPopup', this.props.activeView === 'unknown' ? 'show' : null)} id="unknownUser">
          <div class="bodysHandler">
            <div class="popupBody show">
              <div class="handler">
                <div class="row">
                  <div class="title">
                    <p>Hello</p>
                  </div>
                </div>
                <form method="POST" onSubmit={this.onSubmit} id="unknownUserForm">
                  <div class="row" style={{marginBottom: "0.3vw"}}>
                    <input type="email" name="email" placeholder="Email"
                           value={this.state.email}
                           onChange={this.handleInput}
                           required/>
                    <input type="password" name="password" placeholder="Password"
                           value={this.state.password}
                           onChange={this.handleInput}
                           required/>
                  </div>
                  <div class="row">
                    <p class="buttonLink floatRight pwdLostButton"
                       onClick={this.props.setView.bind(null, 'passwordLost')}>Password lost ?</p>
                  </div>
                  <div class={classnames("row alertDiv text-center", this.state.error ? 'show' : null)}>
                    <p>{this.state.errorMessage}</p>
                  </div>
                  <div class="row text-center">
                    <button class="btn" type="submit">Login</button>
                  </div>
                  <div class="row">
                    {this.props.knownUser &&
                    <p class="buttonLink floatLeft knownAccountButton" onClick={this.props.setView.bind(null, 'known')}>{this.props.knownFname}'s account</p>}
                    <a class="buttonLink floatRight createAccount" href="/discover">Create an account</a>
                  </div>
                </form>
              </div>
            </div>
          </div>
        </div>
    )
  }
}

class KnownUserForm extends React.Component{
  constructor(props){
    super(props);
    this.state = {
      password: '',
      errorMessage: '',
      error: false
    };
    this.handleInput = this.handleInput.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
  }
  handleInput(e){
    this.setState({[e.target.name]: e.target.value});
  }
  onSubmit(e){
    e.preventDefault();
    this.setState({error: false});
    this.props.setView('loading');
    this.props.dispatch(processConnection({
      email:this.props.email,
      password:this.state.password
    })).then(response => {
      this.props.finishLogin();
    }).catch(err => {
      this.setState({errorMessage:err, error: true, password: ''});
      this.props.setView('known');
    });
  }
  render() {
    return (
        <div class={classnames('easePopup landingPopup', this.props.activeView === 'known' ? 'show' : null)} id="knownUser">
          <div class="bodysHandler">
            <div class="popupBody show">
              <div class="handler">
                <div class="row">
                  <div class="title">
                    <p>Hello<br/>{this.props.fname} !</p>
                  </div>
                </div>
                <form method="POST" onSubmit={this.onSubmit} id="knownUserForm">
                  <div class="row text-center">
                    <p class="popupText">Please type your password to access your space</p>
                  </div>
                  <div class="row" style={{marginBottom: "0.3vw"}}>
                    <input type="password" name="password" placeholder="Password"
                           value={this.state.password}
                           onChange={this.handleInput}
                           required/>
                  </div>
                  <div class="row">
                    <p class="buttonLink floatRight pwdLostButton"
                       onClick={this.props.setView.bind(null, 'passwordLost')}>Password lost ?</p>
                  </div>
                  <div class={classnames("row alertDiv text-center", this.state.error ? 'show' : null)}>
                    <p>{this.state.errorMessage}</p>
                  </div>
                  <div class="row text-center">
                    <button class="btn" type="submit">Login</button>
                  </div>
                  <div class="row">
                    <p class="buttonLink floatLeft otherAccountButton" onClick={this.props.setView.bind(null, 'unknown')}>Other account</p>
                    <a class="buttonLink floatRight createAccount" href="/discover">Create an account</a>
                  </div>
                </form>
              </div>
            </div>
          </div>
        </div>
    )
  }
}

class PasswordLost extends React.Component{
  constructor(props){
    super(props);
    this.state = {
      email: '',
      errorMessage: ''
    };
  }
  handleInput = (e) => {
    this.setState({[e.target.name]: e.target.value});
  };
  onSubmit = (e) => {
    e.preventDefault();
    this.setState({errorMessage: ''});
    post_api.common.passwordLost({
      email: this.state.email
    }).then(response => {
      this.setState({errorMessage: response.msg});
    }).catch(err => {
      this.setState({errorMessage: err});
    });
  };
  render() {
    return (
        <div class={classnames('easePopup landingPopup', this.props.activeView === 'passwordLost' ? 'show' : null)} id="passwordLost">
          <div class="bodysHandler">
            <div class="popupBody show">
              <div class="handler">
                <div class="row">
                  <div class="title">
                    <p>Lost password ?</p>
                  </div>
                </div>
                <form method="POST" onSubmit={this.onSubmit} id="passwordLostForm">
                  <div class="row text-center">
                    <p class="popupText">For security reasons, resetting your EASE password will delete all account
                      passwords you added to the platform.</p>
                  </div>
                  <div class="row">
                    <input type="email" name="email" placeholder="Email" value={this.state.email} onChange={this.handleInput}/>
                  </div>
                  <div class={classnames("row alertDiv text-center", !!this.state.errorMessage.length ? 'show' : null)}>
                    <p>{this.state.errorMessage}</p>
                  </div>
                  <div class="row text-center">
                    <button class="btn" type="submit">Reset password</button>
                  </div>
                  <div class="row text-center">
                    <p class="buttonLink backButton"
                       onClick={this.props.goBack}>Go back</p>
                  </div>
                </form>
              </div>
            </div>
          </div>
        </div>
    )
  }
}

function Loader(props){
  return (
      <div class="sk-fading-circle show" id="loading">
        <div class="sk-circle1 sk-circle"/>
        <div class="sk-circle2 sk-circle"/>
        <div class="sk-circle3 sk-circle"/>
        <div class="sk-circle4 sk-circle"/>
        <div class="sk-circle5 sk-circle"/>
        <div class="sk-circle6 sk-circle"/>
        <div class="sk-circle7 sk-circle"/>
        <div class="sk-circle8 sk-circle"/>
        <div class="sk-circle9 sk-circle"/>
        <div class="sk-circle10 sk-circle"/>
        <div class="sk-circle11 sk-circle"/>
        <div class="sk-circle12 sk-circle"/>
      </div>
  )
}

@connect((store)=>({
  authenticated: store.common.authenticated,
  redirect: store.common.loginRedirectUrl
}))
class Login extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      activeView: 'unknown',
      lastActive: '',
      knownFname: this.props.cookies.get('fname'),
      knownEmail: this.props.cookies.get('email'),
      knownUser: false,
      redirect : ''
    };
    if (this.props.authenticated)
      this.props.history.replace('/main/dashboard');
    this.state.knownUser = !!this.state.knownFname && !!this.state.knownEmail;
    if (this.state.knownUser) {
      this.state.activeView = 'known';
    }
  }
  componentDidMount(){
    if (this.props.redirect.length > 0){
      this.state.redirect = this.props.redirect;
      this.props.dispatch(setLoginRedirectUrl(''));
    }
  }
  finishLoggingIn = () => {
    if (this.state.redirect.length > 0)
      this.props.history.replace(this.state.redirect);
    else
      this.props.history.replace('/main/dashboard');
  };
  setView = (name) => {
    this.setState({lastActive: this.state.activeView, activeView: name});
  };
  goBack = () => {
    this.setState({lastActive: this.state.activeView, activeView:this.state.lastActive});
  };
  render(){
    if (this.props.authenticated)
      return null;
    return (
        <div id="loginBody">
          <div class="ease-logo">
            <img src="/resources/icons/Ease_logo_blue.svg"/>
          </div>
          <div class="popupHandler myshow">
            {this.state.activeView === 'loading' &&
            <Loader/>}
            <UnknownUserForm setView={this.setView}
                             dispatch={this.props.dispatch}
                             activeView={this.state.activeView}
                             knownFname={this.state.knownFname}
                             knownUser={this.state.knownUser}
                             finishLogin={this.finishLoggingIn}/>
            <KnownUserForm setView={this.setView}
                           dispatch={this.props.dispatch}
                           activeView={this.state.activeView}
                           fname={this.state.knownFname}
                           email={this.state.knownEmail}
                           finishLogin={this.finishLoggingIn}/>
            <PasswordLost setView={this.setView}
                          activeView={this.state.activeView}
                          goBack={this.goBack}/>
          </div>
        </div>
    )
  }
}

module.exports = withCookies(Login);