import React from 'react';
import {connect} from "react-redux";
import {withCookies} from 'react-cookie';
import {processConnection} from "../../../actions/commonActions";
import {Input, Button, Icon} from 'semantic-ui-react'

@connect((store)=>({
  authenticated: store.common.authenticated,
  redirect: store.common.loginRedirectUrl
}))
class KnownUserForm extends React.Component{
  constructor(props){
    super(props);
    this.state = {
      name: this.props.cookies.get('fname'),
      email: this.props.cookies.get('email'),
      password: '',
      errorMessage: '',
      error: false
    };
    if (!!this.state.name) {
      this.state.name = atob(this.state.name);
    }
  }
  onSubmit = e => {
    e.preventDefault();
    this.setState({error: false});
    this.props.dispatch(processConnection({
      email:this.state.email,
      password:this.state.password
    })).then(response => {
      this.props.finishLogin();
    }).catch(err => {
      this.setState({errorMessage:err, error: true, password: ''});
      this.props.setView('known');
    });
  };
  handleInput = e => {
    this.setState({[e.target.name]: e.target.value});
  };

  otherAccount = () => {
    this.props.history.replace('login/unknownUser');
  };
  passwordLost = () => {
    this.props.history.replace('login/passwordLost')
  };
  componentWillMount(){
    if (!this.props.cookies.get('fname') || !this.props.cookies.get('email')) {
      this.props.history.replace('/login/unknownUser');
    }
}
  render() {
    return (
      <div className="knowUserContainer">
        <div>
          <img className="loginLogo" src='/resources/images/ease_logo_blue.svg'/>
        </div>
        <div>
          <p className="LoginAccess">ACCESS YOUR ACCOUNT</p>
        </div>
        <div>
          <p className="loginTitle">Hello {this.state.name}</p>
          <form method="POST" onSubmit={this.onSubmit} id="knownUserForm">
            <div>
              <p className="LoginInputTitle">Please type your password</p>
              <Input className="mrgBottom5" type="password" name="password" placeholder="Password"
                     value={this.state.password}
                     onChange={this.handleInput}
                     autoFocus
                     required/>
              <p className="LoginErrorMessage">{this.state.errorMessage}</p>
            </div>
            <div>
              <Button icon color="green" type="submit">Login <Icon name='sign in' /></Button>
            </div>
          </form>
        </div>
        <div className="knowUserOtherLink">
          { this.props.cookies.get('fname') &&
          <p onClick={this.otherAccount}>Other account</p>
          }
          <p onClick={this.passwordLost}>Password lost ?</p>
          <a href="/discover">Create an account</a>
        </div>
      </div>

    )
  }
}

export default withCookies(KnownUserForm);