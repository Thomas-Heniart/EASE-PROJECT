import React from 'react';
import { Grid } from 'semantic-ui-react';
import { Switch, Route, NavLink } from 'react-router-dom';
import {connect} from "react-redux";
import {withCookies} from 'react-cookie';
import {setLoginRedirectUrl} from "../../../actions/commonActions";
import UnknownUserForm from "./UnknownUserForm";
import PasswordLost from "./passwordLost";
import KnownUserForm from "./KnownUserForm";


function Loader(props){
  return (
    <div class="sk-fading-circle show" id="loading">
      <div class="ui large active centered inline loader">
      </div>
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
  }
  componentDidMount(){
    if (this.props.redirect.length > 0){
      this.state.redirect = this.props.redirect;
      this.props.dispatch(setLoginRedirectUrl(''));
    }
  }
  componentWillMount(){
    document.title = "Ease.space";
    console.log('INDEX/LOGIN/PROPS', this.props);
    console.log("cook", this.state.knownFname);
  }

  finishLoggingIn = () => {
    if (this.state.redirect.length > 0) {
      this.props.history.replace(this.state.redirect);
    }
    else
      this.props.history.replace('/main/dashboard');
  };

  render(){
    if (this.props.authenticated)
      return null;
    return (
      <div id="loginBody">
        <div class="myshow">
          {this.state.activeView === 'loading' &&
          <Loader/>}
        </div>

        <Grid.Column width={6}>
          <Switch>
            <Route exact path={`/login`} render={(props) => <KnownUserForm {...props} finishLogin={this.finishLoggingIn}/>}/>
            <Route path={`/login/unknownUser`} render={(props) => <UnknownUserForm {...props} finishLogin={this.finishLoggingIn}/>}/>
            <Route path={`/login/passwordLost`} component={PasswordLost}/>
          </Switch>
        </Grid.Column>

        <Grid.Column width={10}>
          <p> Lorem ipsum dolor sit ametis </p>
        </Grid.Column>
      </div>
    )
  }
}
// render={(props) => <KnownUserForm finishLogin={this.finishLoggingIn}/>}
export default withCookies(Login);