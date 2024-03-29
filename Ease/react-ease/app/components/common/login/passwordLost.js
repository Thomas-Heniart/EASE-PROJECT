import React from 'react';
import {withCookies} from 'react-cookie';
import {Input, Button} from 'semantic-ui-react';
import post_api from "../../../utils/post_api";
import {NavLink} from 'react-router-dom';


class PasswordLost extends React.Component{
  constructor(props){
    super(props);
    this.state = {
      errorMessage: '',
      error: false,
      disable: false
    };
    if (!!this.state.name) {
      this.state.name = atob(this.state.name);
    }
  }
  onSubmit = (e) => {
    e.preventDefault();
    this.setState({disable: true, errorMessage: ''});
    post_api.common.passwordLost({
      email: this.state.email
    }).then(response => {
      this.setState({errorMessage: response.msg, disable: false});
    }).catch(err => {
      this.setState({errorMessage: err});
      this.setState({disable: false});
    });
  };
  handleInput = e => {
    this.setState({[e.target.name]: e.target.value});
  };
  otherAccount = () => {
    this.props.history.replace('/login/unknownUser');
  };
  render() {
    return (
      <div className="knowUserContainer">
        <div>
          <img className="loginLogo" src='/resources/images/ease_logo_blue.svg'/>
        </div>
        <div>
          <p className="LoginAccess">PASSWORD LOST</p>
          <p className="passwordLostText">For security reasons, resetting your<br />
            password will make you lose your<br /> personal passwords, and you
            'll need to<br /> be re-accepted in your team to have<br /> acces again to
            your team passwords</p>
          <form method="POST" onSubmit={this.onSubmit} id="knownUserForm">
            <div>
              <p className="LoginInputTitle" style={{color: this.state.inputFocus ? 'black' : null}}>Email</p>
              <Input className="loginPasswordInput" type="email" name="email" placeholder="Email"
                     value={this.state.password}
                     onChange={this.handleInput}
                     onFocus={this.focusInputIn}
                     onBlur={this.focusInputOut}
                     autoFocus
                     required/>
              <p className="LoginErrorMessage">{this.state.errorMessage}</p>
            </div>
            <div>
              <Button disabled={this.state.disable} loading={this.state.disable} color="green" type="submit">Reset my password</Button>
            </div>
          </form>
        </div>
        <div className="knowUserOtherLink">
          <div>
            <NavLink to="/login/unknownUser">Other account</NavLink>
          </div>
            <a href="/discover">Create an account</a>
        </div>
      </div>
    )
  }
}

export default withCookies(PasswordLost);