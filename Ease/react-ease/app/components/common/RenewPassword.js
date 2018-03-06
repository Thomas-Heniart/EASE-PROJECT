import React, {Component} from "react";
import post_api from "../../utils/post_api";
import {handleSemanticInput, passwordRegexp} from "../../utils/utils";
import {Icon, Form, Header, Input, Message, Button} from 'semantic-ui-react';

class PasswordInput extends Component {
  constructor(props){
    super(props);
    this.state = {
      show: false
    };
  }
  toggle = () => {
    this.setState({show: !this.state.show})
  };
  render(){
    return (
        <Input {...this.props} type={this.state.show ? 'text': 'password'} icon>
          <input/>
          <Icon name={this.state.show ? 'hide': 'unhide'} link onClick={this.toggle}/>
        </Input>
    )
  }
}

export default class RenewPassword extends Component {
  constructor(props){
    super(props);
    this.state = {
      password: '',
      passwordError: false,
      confirmPassword: '',
      errorMessage: '',
      loading: false
    }
  }
  handleInput = handleSemanticInput.bind(this);
  passwordInput = (e, {value}) => {
    this.setState({password: value});
    const isMatch = !!value.match(passwordRegexp);
    if (!isMatch && !this.state.passwordError)
      this.setState({passwordError: true});
    else if (isMatch && this.state.passwordError)
      this.setState({passwordError: false})
  };
  confirm = (e) => {
    e.preventDefault();
    if (this.state.password !== this.state.confirmPassword){
      this.setState({errorMessage: "Passwords don't match"});
      return;
    }
    this.setState({errorMessage: '', loading:true});
    post_api.common.renewPassword({
      email: this.props.match.params.email,
      code: this.props.match.params.code,
      password: this.state.password
    }).then(response => {
      this.setState({loading: false});
      this.props.history.replace('/login');
    }).catch(err => {
      this.setState({loading: false, errorMessage: err});
    });
  };
  render(){
    return (
      <div className='on_boarding_wrapper'>
        <div id='new_team_creation'>
          <div id='left_bar'>
            <p className="renewPasswordAsideText">Enter a <strong>strong password</strong> without names, dates or info related to you.</p>
            <p>Your password must contain at least <strong>8 characters, an uppercase, a lowercase and a number</strong></p>
            <img src='/resources/images/ease_logo_white.svg'/>
          </div>

          <div style={{marginLeft: '30px', marginTop: '30px'}}>
            <div>
              <Header as="h1" className="mrgBottom10">
                Setup your new password
              </Header>
            </div>
            <Form error={!!this.state.errorMessage.length} onSubmit={this.confirm}>
              <Form.Field>
                <label>New password</label>
                <PasswordInput
                  className="mrgBottom5"
                  name="password"
                  required
                  onChange={this.passwordInput}
                  placeholder="New password"/>
              </Form.Field>
              <Form.Field>
                <label>Confirm new password</label>
                <PasswordInput
                  className="mrgBottom5"
                  name="confirmPassword"
                  required
                  onChange={this.handleInput}
                  placeholder="Confirm new password"/>
              </Form.Field>
              <Message error content={this.state.errorMessage}/>
            </Form>
            <span style={{color: this.state.passwordError ? '#E84855': null, display:'block', marginTop: '2px', fontSize: '14px'}}>Your password must contain at least 8 characters, 1 uppercase, 1 lowercase and 1 number</span>
            <div>
              <Button className="newPasswordButton" color="green" onClick={this.confirm} content={'Done!'}/>
            </div>
          </div>
        </div>
      </div>
    )
  }
}