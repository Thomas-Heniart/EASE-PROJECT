import React from 'react';
import { Switch, Route} from 'react-router-dom';
import {connect} from "react-redux";
import {withCookies} from 'react-cookie';
import {setDashboardFooterState} from "../../../actions/dashboardActions";
import {setLoginRedirectUrl} from "../../../actions/commonActions";
import UnknownUserForm from "./UnknownUserForm";
import PasswordLost from "./passwordLost";
import KnownUserForm from "./KnownUserForm";
import extension from "../../../utils/extension_api";
import ReactGA from 'react-ga';

function Loader(props){
  return (
    <div className="sk-fading-circle show" id="loading">
      <div className="ui large active centered inline loader">
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
      knownFname: this.props.cookies.get('fname'),
      knownEmail: this.props.cookies.get('email'),
      knownUser: false,
      redirect : ''
    };
    if (this.props.authenticated)
      this.props.history.replace('main/dashboard/');
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
    ReactGA.pageview("/login");
  }
  finishLoggingIn = () => {
    extension.easeLogin();
    if (this.state.redirect.length > 0) {
      this.props.history.replace(this.state.redirect);
    }
    else {
      this.props.history.replace('/main/dashboard');
      this.props.dispatch(setDashboardFooterState({
        active: true
      }));
    }
  };
  render(){
    if (this.props.authenticated)
      return null;
    return (
      <div className="containerLogin">
        <div className="loginAside">
          <Switch>
            <Route exact path={`/login`} render={(props) => <KnownUserForm {...props} finishLogin={this.finishLoggingIn}/>}/>
            <Route path={`/login/unknownUser`} render={(props) => <UnknownUserForm {...props} finishLogin={this.finishLoggingIn}/>}/>
            <Route path={`/login/passwordLost`} component={PasswordLost}/>
          </Switch>
        </div>
        <div className="loginContent"/>
      </div>
    )
  }
}

export default withCookies(Login);