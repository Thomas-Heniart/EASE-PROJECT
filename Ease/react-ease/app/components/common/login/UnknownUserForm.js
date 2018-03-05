import React from 'react';
import {processConnection} from "../../../actions/commonActions";
import {withCookies} from 'react-cookie';
import {connect} from 'react-redux';
import {Input, Button, Icon} from 'semantic-ui-react'

@connect((store)=>({
  authenticated: store.common.authenticated,
  redirect: store.common.loginRedirectUrl
}))
class UnknownUserForm extends React.Component{
  constructor(props){
    super(props);
    this.state = {
      email:'',
      password: '',
      errorMessage: '',
      error: false,
      name: this.props.cookies.get('fname')
    };
    if (!!this.state.name) {
      this.state.name = atob(this.state.name);
    }
    this.handleInput = this.handleInput.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
  }
  handleInput(e){
    this.setState({[e.target.name]: e.target.value});
  }
  onSubmit(e){
    e.preventDefault();
    this.setState({error: false});
    this.props.dispatch(processConnection({
      email:this.state.email,
      password:this.state.password
    })).then(response => {
      this.props.finishLogin();
    }).catch(err => {
      this.setState({errorMessage:err, error: true, password: ''});
    });
  }
  userAccount = e => {
    this.props.history.replace('/login');
  };
  passwordLost = () => {
    this.props.history.replace('/login/passwordLost')
  };
  render() {
    return (
      <div className="knowUserContainer">
        <div>
          <img className="loginLogo" src='/resources/images/ease_logo_blue.svg'/>
        </div>
        <div>
          <p className="loginAccess">ACCESS YOUR ACCOUNT</p>
        </div>
        <div>
          <form method="POST" onSubmit={this.onSubmit} id="unknownUserForm">
            <div>
            <p className="LoginInputTitle">Email</p>
            <Input className="mrgBottom5" type="email" name="email"
                   placeholder="Email"
                   value={this.state.email}
                   onChange={this.handleInput}
                   required/>
            <p className="LoginInputTitle">Password</p>
            <Input className="mrgBottom5" type="password" name="password"
                   placeholder="Password"
                   value={this.state.password}
                   onChange={this.handleInput}
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
          <p onClick={this.userAccount}>{this.state.name}'s account</p>
          }
          <p onClick={this.passwordLost}>Password lost ?</p>
          <a href="/discover">Create an account</a>
        </div>
      </div>
    )
  }
}

export default withCookies(UnknownUserForm);