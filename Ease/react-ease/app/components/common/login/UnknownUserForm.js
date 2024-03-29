import React from 'react';
import {processConnection} from "../../../actions/commonActions";
import {withCookies} from 'react-cookie';
import {connect} from 'react-redux';
import {Input, Button, Icon} from 'semantic-ui-react';
import {NavLink} from 'react-router-dom';


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
      inputEmail: false,
      inputPassword: false,
      name: this.props.cookies.get('fname'),
      disable: false
    };
    if (!!this.state.name) {
      this.state.name = atob(this.state.name);
    }
    this.handleInput = this.handleInput.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
  }
  focusInputInEmail = () => {
    this.setState({inputEmail: true});
  };
  focusInputOutEmail = () => {
    this.setState({inputEmail: false});
  };
  focusInputInPassword = () => {
    this.setState({inputPassword: true});
  };
  focusInputOutPassword = () => {
    this.setState({inputPassword: false});
  };
  handleInput(e){
    this.setState({[e.target.name]: e.target.value});
  }
  onSubmit(e){
    e.preventDefault();
    this.setState({disable: true});
    this.setState({error: false});
    this.props.dispatch(processConnection({
      email:this.state.email,
      password:this.state.password
    })).then(response => {
      this.props.finishLogin();
    }).catch(err => {
      this.setState({errorMessage:err, error: true, password: ''});
      this.setState({disable: false});
    });
  }

  render() {
    return (
      <div className="knowUserContainer">
        <div>
          <img className="loginLogo" src='/resources/images/ease_logo_blue.svg'/>
        </div>
        <div>
          <p className="loginAccess">ACCESS YOUR ACCOUNT</p>
          <form method="POST" onSubmit={this.onSubmit} id="unknownUserForm">
            <div>
            <p className="LoginInputTitle" style={{color: this.state.inputEmail ? 'black' : null}}>Email</p>
            <Input className="mrgBottom5 loginPasswordInput" type="email" name="email"
                   placeholder="Email"
                   value={this.state.email}
                   onFocus={this.focusInputInEmail}
                   onBlur={this.focusInputOutEmail}
                   onChange={this.handleInput}
                   required/>
            <p className="LoginInputTitle" style={{color: this.state.inputPassword ? 'black' : null}}>Password</p>
            <Input className="mrgBottom5 loginPasswordInput" type="password" name="password"
                   placeholder="Password"
                   value={this.state.password}
                   onFocus={this.focusInputInPassword}
                   onBlur={this.focusInputOutPassword}
                   onChange={this.handleInput}
                   required/>
              <p className="LoginErrorMessage">{this.state.errorMessage}</p>
            </div>
            <div>
              <Button icon color="green" loading={this.state.disable === true} disabled={this.state.disable === true} type="submit">Login <Icon name='sign in' /></Button>
            </div>
          </form>
        </div>
        <div className="knowUserOtherLink">
          { this.props.cookies.get('fname') &&
            <div>
              <NavLink to="/login">{this.state.name}'s account</NavLink>
            </div>
          }
          <div>
            <NavLink to="/login/passwordLost">Password lost ?</NavLink>
          </div>
          <a href="/discover">Create an account</a>
        </div>
      </div>
    )
  }
}

export default withCookies(UnknownUserForm);