import React, {Component} from "react";
import post_api from "../../utils/post_api";
import {handleSemanticInput, passwordRegexp} from "../../utils/utils";
import {Button, Icon, Container, Divider, Form, Header, Input, Label, Message, Segment} from 'semantic-ui-react';

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
        <div class="full_screen_centered_view" style={{backgroundColor: '#373B60'}}>
          <div id="single_ease_logo_handler">
            <img class="logoImg" src="/resources/images/Ease_Logo.svg"/>
          </div>
          <Segment style={{width: '500px', paddingLeft: '30px', paddingRight: '30px'}}>
            <Header as="h2" class="text-center">
              Setup a new password
            </Header>
            <Form error={!!this.state.errorMessage.length} onSubmit={this.confirm}>
              <Form.Field>
                Take care of remembering this password. In case you lose it, all passwords on your Ease.space account will be disappear.
              </Form.Field>
              <Form.Field>
                <label>New password</label>
                <PasswordInput
                    name="password"
                    required
                    onChange={this.passwordInput}
                    placeholder="New password"/>
                <span style={{color: this.state.passwordError ? '#E84855': null, display:'block', marginTop: '2px', fontSize: '14px'}}>Your password must contain at least 8 characters, 1 uppercase, 1 lowercase and 1 number</span>
              </Form.Field>
              <Form.Field>
                <label>Confirm new password</label>
                <PasswordInput
                    name="confirmPassword"
                    required
                    onChange={this.handleInput}
                    placeholder="Confirm new password"/>
              </Form.Field>
              <Message error content={this.state.errorMessage}/>
              <Form.Button fluid positive content={'Confirm'}/>
            </Form>
          </Segment>
        </div>
    )
  }
}